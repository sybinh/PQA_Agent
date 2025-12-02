#!/usr/bin/env python
"""
Rule_Bc_Close - BC Release closure validation after planned date

Original Java: DataModel/Rq1/Monitoring/Rule_Bc_Close.java
Rule ID: BC_04__CLOSE
QAM ID: ID-2.5.0 / PRPL 15.00.00
Execution Group: ELEMENT_INTEGRITY, EXCITED_CPC, BC_PLANNING

Description:
Creates a warning if the BC is not closed after its planned date.

Logic:
- Skip if BC is in Canceled or Conflicted state
- Check if Planned Date is in the past
- Warn if BC is still in PLANNED, REQUESTED, or DEVELOPED state
"""

from enum import Enum
from datetime import datetime, date
from typing import Optional
from dataclasses import dataclass


class LifeCycleState_Release(Enum):
    """Release lifecycle states"""
    NEW = "New"
    REQUESTED = "Requested"
    PLANNED = "Planned"
    DEVELOPED = "Developed"
    CLOSED = "Closed"
    CANCELED = "Canceled"
    CONFLICTED = "Conflicted"


@dataclass
class ValidationResult:
    """Result of BC closure validation"""
    passed: bool
    severity: str  # "PASS", "WARNING"
    title: str
    description: str


class Rule_Bc_Close:
    """
    BC Release closure validation rule
    
    Validates that BC-Release is closed after its planned date.
    Warning if BC is in PLANNED/REQUESTED/DEVELOPED state after planned date.
    """
    
    RULE_ID = "BC_04__CLOSE"
    QAM_ID = "ID-2.5.0"
    RULE_NAME = "Release is not closed after planned date for BC (QAM ID-2.5.0)"
    
    # States that should trigger warning if planned date passed
    WARNING_STATES = {
        LifeCycleState_Release.PLANNED,
        LifeCycleState_Release.REQUESTED,
        LifeCycleState_Release.DEVELOPED
    }
    
    # States where rule is not executed
    SKIP_STATES = {
        LifeCycleState_Release.CANCELED,
        LifeCycleState_Release.CONFLICTED
    }
    
    def __init__(self, bc_release: dict):
        """
        Initialize rule with BC-Release data
        
        Args:
            bc_release: BC-Release record with:
                - id: Release ID
                - lifecyclestate: Current state
                - planneddate: Planned delivery date
        """
        self.bc_release = bc_release
        self.today = date.today()
    
    def execute(self) -> ValidationResult:
        """
        Execute BC closure validation
        
        Returns:
            ValidationResult with pass/warning status
        """
        # Step 1: Check if rule should be skipped
        lifecycle_state = self._get_lifecycle_state()
        
        if lifecycle_state in self.SKIP_STATES:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Rule skipped",
                description=f"Rule not executed for BC in state {lifecycle_state.value}"
            )
        
        # Step 2: Get planned date
        planned_date = self._get_planned_date()
        
        if planned_date is None:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="No planned date",
                description="Planned date not set - cannot validate closure"
            )
        
        # Step 3: Check if planned date is in the past
        if planned_date >= self.today:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Planned date not reached",
                description=f"Planned date {planned_date} is in the future or today"
            )
        
        # Step 4: Check if BC should be closed by now
        if lifecycle_state in self.WARNING_STATES:
            return ValidationResult(
                passed=False,
                severity="WARNING",
                title=self.RULE_NAME,
                description=f"LifeCycleState {lifecycle_state.value} after planned date ({planned_date}).\n"
                           f"BC should be in CLOSED state by now."
            )
        
        # BC is already closed or in acceptable state
        return ValidationResult(
            passed=True,
            severity="PASS",
            title="BC closure check passed",
            description=f"BC is in {lifecycle_state.value} state - acceptable"
        )
    
    def _get_lifecycle_state(self) -> LifeCycleState_Release:
        """Get lifecycle state from BC release record"""
        state_text = self.bc_release.get('lifecyclestate', '').strip()
        for state in LifeCycleState_Release:
            if state.value.lower() == state_text.lower():
                return state
        return LifeCycleState_Release.NEW  # Default
    
    def _get_planned_date(self) -> Optional[date]:
        """
        Get planned date from BC release record
        
        Returns:
            date object or None if not set
        """
        planned_date_str = self.bc_release.get('planneddate')
        
        if not planned_date_str:
            return None
        
        # Handle datetime object from RQ1 API
        if isinstance(planned_date_str, datetime):
            return planned_date_str.date()
        
        if isinstance(planned_date_str, date):
            return planned_date_str
        
        # Try parsing string
        try:
            return datetime.fromisoformat(str(planned_date_str).split('T')[0]).date()
        except (ValueError, AttributeError):
            return None


# Test with real RQ1 data
if __name__ == "__main__":
    from rq1 import Client, BaseUrl
    from rq1.models import Release, ReleaseProperty
    
    print("="*80)
    print("Rule_Bc_Close - Test Cases")
    print("="*80)
    
    from datetime import timedelta
    
    # Test Case 1: BC in PLANNED state, planned date passed (should WARN)
    print("\nTest 1: BC in PLANNED state with past planned date")
    bc_data_1 = {
        'id': 'RQONE04815588',
        'dcterms__title': 'Test BC-Release 1',
        'dcterms__type': 'BC-Release',
        'lifecyclestate': 'Planned',
        'planneddate': (date.today() - timedelta(days=15)).isoformat(),
        'submitdate': (date.today() - timedelta(days=60)).isoformat()
    }
    
    rule1 = Rule_Bc_Close(bc_data_1)
    result1 = rule1.execute()
    print(f"  {result1.severity}: {result1.title}")
    if not result1.passed:
        print(f"  {result1.description}")
    
    # Test Case 2: BC in DEVELOPED state, planned date passed (should WARN)
    print("\nTest 2: BC in DEVELOPED state with past planned date")
    bc_data_2 = {
        'id': 'RQONE04815589',
        'dcterms__title': 'Test BC-Release 2',
        'dcterms__type': 'BC-Release',
        'lifecyclestate': 'Developed',
        'planneddate': (date.today() - timedelta(days=5)).isoformat(),
        'submitdate': (date.today() - timedelta(days=45)).isoformat()
    }
    
    rule2 = Rule_Bc_Close(bc_data_2)
    result2 = rule2.execute()
    print(f"  {result2.severity}: {result2.title}")
    if not result2.passed:
        print(f"  {result2.description}")
    
    # Test Case 3: BC in CLOSED state, planned date passed (should PASS)
    print("\nTest 3: BC in CLOSED state with past planned date")
    bc_data_3 = {
        'id': 'RQONE04815590',
        'dcterms__title': 'Test BC-Release 3',
        'dcterms__type': 'BC-Release',
        'lifecyclestate': 'Closed',
        'planneddate': (date.today() - timedelta(days=10)).isoformat(),
        'submitdate': (date.today() - timedelta(days=50)).isoformat()
    }
    
    rule3 = Rule_Bc_Close(bc_data_3)
    result3 = rule3.execute()
    print(f"  {result3.severity}: {result3.title}")
    
    # Test Case 4: BC in PLANNED state, planned date future (should PASS)
    print("\nTest 4: BC in PLANNED state with future planned date")
    bc_data_4 = {
        'id': 'RQONE04815591',
        'dcterms__title': 'Test BC-Release 4',
        'dcterms__type': 'BC-Release',
        'lifecyclestate': 'Planned',
        'planneddate': (date.today() + timedelta(days=20)).isoformat(),
        'submitdate': (date.today() - timedelta(days=30)).isoformat()
    }
    
    rule4 = Rule_Bc_Close(bc_data_4)
    result4 = rule4.execute()
    print(f"  {result4.severity}: {result4.title}")
    
    # Test Case 5: BC in CANCELED state (should SKIP)
    print("\nTest 5: BC in CANCELED state (rule skipped)")
    bc_data_5 = {
        'id': 'RQONE04815592',
        'dcterms__title': 'Test BC-Release 5',
        'dcterms__type': 'BC-Release',
        'lifecyclestate': 'Canceled',
        'planneddate': (date.today() - timedelta(days=30)).isoformat(),
        'submitdate': (date.today() - timedelta(days=60)).isoformat()
    }
    
    rule5 = Rule_Bc_Close(bc_data_5)
    result5 = rule5.execute()
    print(f"  {result5.severity}: {result5.title}")
    print(f"  {result5.description}")
    
    print(f"\n{'='*80}")
    print("Summary: Rule BC_04__CLOSE (QAM ID-2.5.0) Implementation Complete")
    print("="*80)
