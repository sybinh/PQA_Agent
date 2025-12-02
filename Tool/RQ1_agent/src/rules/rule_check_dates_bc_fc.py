#!/usr/bin/env python
"""
Rule_CheckDatesForBcAndFc - Date validation for BC and FC releases

Original Java: DataModel/Rq1/Monitoring/Rule_CheckDatesForBcAndFc.java
Rule ID: PRPL 10.00.00 / PRPL 15.00.00
Execution Group: ELEMENT_INTEGRITY

Description:
Check dates for BC-Release and FC-Release against lifecycle state rules.

Validation Rules by State:

REQUESTED state:
- Planning Freeze (if set) must be today or in the future
- Implementation Freeze (if set) must be today or in the future  
- Planned Date must be set

PLANNED state:
- Implementation Freeze (if set) must be today or in the future
- Planned Date must be set

DEVELOPED state:
- (Planned Date check covered by QAM ID-2.5.0)

NEW, CLOSED, CANCELED, CONFLICTED states:
- No date checks performed
"""

from enum import Enum
from datetime import datetime, date
from typing import Optional, List
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
    """Result of date validation"""
    passed: bool
    severity: str  # "PASS", "WARNING"
    title: str
    description: str
    affected_field: Optional[str] = None


class Rule_CheckDatesForBcAndFc:
    """
    Date validation rule for BC-Release and FC-Release
    
    Validates dates based on lifecycle state:
    - REQUESTED: Planning Freeze, Implementation Freeze, Planned Date
    - PLANNED: Implementation Freeze, Planned Date
    - Other states: No validation
    """
    
    RULE_ID = "PRPL_10_CheckDatesForBcAndFc"
    RULE_NAME = "Check dates for BC and FC"
    
    def __init__(self, release: dict):
        """
        Initialize rule with Release data
        
        Args:
            release: BC-Release or FC-Release record with:
                - lifecyclestate: Current state
                - planningfreeze: Planning freeze date (optional)
                - implementationfreeze: Implementation freeze date (optional)
                - planneddate: Planned delivery date
        """
        self.release = release
        self.today = date.today()
        self.warnings: List[ValidationResult] = []
    
    def execute(self) -> List[ValidationResult]:
        """
        Execute date validation based on lifecycle state
        
        Returns:
            List of ValidationResult (empty if all checks pass)
        """
        self.warnings = []
        
        lifecycle_state = self._get_lifecycle_state()
        
        if lifecycle_state == LifeCycleState_Release.REQUESTED:
            self._check_planning_freeze()
            self._check_implementation_freeze()
            self._check_planned_date()
        
        elif lifecycle_state == LifeCycleState_Release.PLANNED:
            self._check_implementation_freeze()
            self._check_planned_date()
        
        elif lifecycle_state == LifeCycleState_Release.DEVELOPED:
            # Planned Date check covered by QAM ID-2.5.0
            pass
        
        # NEW, CLOSED, CANCELED, CONFLICTED: no checks
        
        return self.warnings
    
    def _get_lifecycle_state(self) -> LifeCycleState_Release:
        """Get lifecycle state from release record"""
        state_text = self.release.get('lifecyclestate', '').strip()
        for state in LifeCycleState_Release:
            if state.value.lower() == state_text.lower():
                return state
        return LifeCycleState_Release.NEW  # Default
    
    def _parse_date(self, date_str: str) -> Optional[date]:
        """
        Parse date string to date object
        
        Args:
            date_str: Date in ISO format or datetime object
            
        Returns:
            date object or None if empty/invalid
        """
        if not date_str:
            return None
        
        # Handle datetime object from RQ1 API
        if isinstance(date_str, datetime):
            return date_str.date()
        
        if isinstance(date_str, date):
            return date_str
        
        # Try parsing string
        try:
            # ISO format: YYYY-MM-DD
            return datetime.fromisoformat(str(date_str).split('T')[0]).date()
        except (ValueError, AttributeError):
            return None
    
    def _check_planning_freeze(self):
        """
        Check Planning Freeze date
        
        Warning if:
        - Planning Freeze is set AND in the past
        - Release should be in PLANNED state by now
        """
        planning_freeze_str = self.release.get('planningfreeze')
        planning_freeze = self._parse_date(planning_freeze_str)
        
        if planning_freeze is None:
            return  # Not set, no check needed
        
        if planning_freeze < self.today:
            lifecycle_state = self._get_lifecycle_state()
            self.warnings.append(ValidationResult(
                passed=False,
                severity="WARNING",
                title="Release not in state Planned after Planning Freeze",
                description=f"{self._get_release_info()}\n"
                           f"Planning Freeze: {planning_freeze}\n"
                           f"Current LifeCycleState: {lifecycle_state.value}\n"
                           f"Expected: Should be in PLANNED state by now",
                affected_field="planningfreeze"
            ))
    
    def _check_implementation_freeze(self):
        """
        Check Implementation Freeze date
        
        Warning if:
        - Implementation Freeze is set AND in the past
        - Release should be in DEVELOPED state by now
        """
        impl_freeze_str = self.release.get('implementationfreeze')
        impl_freeze = self._parse_date(impl_freeze_str)
        
        if impl_freeze is None:
            return  # Not set, no check needed
        
        if impl_freeze < self.today:
            lifecycle_state = self._get_lifecycle_state()
            self.warnings.append(ValidationResult(
                passed=False,
                severity="WARNING",
                title="Release not in state Developed after Implementation Freeze",
                description=f"{self._get_release_info()}\n"
                           f"Implementation Freeze: {impl_freeze}\n"
                           f"Current LifeCycleState: {lifecycle_state.value}\n"
                           f"Expected: Should be in DEVELOPED state by now",
                affected_field="implementationfreeze"
            ))
    
    def _check_planned_date(self):
        """
        Check Planned Date is set
        
        Warning if:
        - Planned Date is not set (required for REQUESTED and PLANNED states)
        
        Note: Check for "Release not closed after Planned Date" is covered by QAM ID-2.5.0
        """
        planned_date_str = self.release.get('planneddate')
        planned_date = self._parse_date(planned_date_str)
        
        if planned_date is None:
            lifecycle_state = self._get_lifecycle_state()
            self.warnings.append(ValidationResult(
                passed=False,
                severity="WARNING",
                title="Planned Date not set",
                description=f"{self._get_release_info()}\n"
                           f"LifeCycleState: {lifecycle_state.value}\n"
                           f"Planned Date is required for {lifecycle_state.value} state",
                affected_field="planneddate"
            ))
        
        # Note: Check if planned_date is in the past is covered by QAM ID-2.5.0
        # (corresponds to PRPL 15.00.00: "Release is not closed after planned date")
    
    def _get_release_info(self) -> str:
        """Get formatted release type and ID"""
        release_id = self.release.get('id', 'Unknown')
        release_type = self.release.get('dcterms__type', 'Release')
        return f"{release_type}: {release_id}"


# Example usage and test cases
if __name__ == "__main__":
    from datetime import timedelta
    
    print("="*80)
    print("Rule_CheckDatesForBcAndFc - Test Cases")
    print("="*80)
    
    # Test 1: REQUESTED state with past Planning Freeze (should warn)
    print("\nTest 1: REQUESTED state with past Planning Freeze")
    release1 = {
        'id': 'RQONE03999401',
        'dcterms__type': 'BC-Release',
        'lifecyclestate': 'Requested',
        'planningfreeze': (date.today() - timedelta(days=5)).isoformat(),
        'implementationfreeze': (date.today() + timedelta(days=30)).isoformat(),
        'planneddate': (date.today() + timedelta(days=60)).isoformat()
    }
    
    rule1 = Rule_CheckDatesForBcAndFc(release1)
    results1 = rule1.execute()
    
    if results1:
        for result in results1:
            print(f"  ? {result.title}")
            print(f"    {result.description}")
    else:
        print("  ? All checks passed")
    
    # Test 2: PLANNED state with missing Planned Date (should warn)
    print("\nTest 2: PLANNED state with missing Planned Date")
    release2 = {
        'id': 'RQONE03999402',
        'dcterms__type': 'FC-Release',
        'lifecyclestate': 'Planned',
        'implementationfreeze': (date.today() + timedelta(days=15)).isoformat(),
        'planneddate': None  # Missing!
    }
    
    rule2 = Rule_CheckDatesForBcAndFc(release2)
    results2 = rule2.execute()
    
    if results2:
        for result in results2:
            print(f"  ? {result.title}")
            print(f"    {result.description}")
    else:
        print("  ? All checks passed")
    
    # Test 3: PLANNED state with past Implementation Freeze (should warn)
    print("\nTest 3: PLANNED state with past Implementation Freeze")
    release3 = {
        'id': 'RQONE03999403',
        'dcterms__type': 'BC-Release',
        'lifecyclestate': 'Planned',
        'implementationfreeze': (date.today() - timedelta(days=10)).isoformat(),
        'planneddate': (date.today() + timedelta(days=20)).isoformat()
    }
    
    rule3 = Rule_CheckDatesForBcAndFc(release3)
    results3 = rule3.execute()
    
    if results3:
        for result in results3:
            print(f"  ? {result.title}")
            print(f"    {result.description}")
    else:
        print("  ? All checks passed")
    
    # Test 4: CLOSED state (no checks)
    print("\nTest 4: CLOSED state (no date checks)")
    release4 = {
        'id': 'RQONE03999404',
        'dcterms__type': 'FC-Release',
        'lifecyclestate': 'Closed',
        'planneddate': (date.today() - timedelta(days=30)).isoformat()  # Past date OK for closed
    }
    
    rule4 = Rule_CheckDatesForBcAndFc(release4)
    results4 = rule4.execute()
    
    if results4:
        for result in results4:
            print(f"  ? {result.title}")
    else:
        print("  ? All checks passed (no validation for CLOSED state)")
    
    # Test 5: REQUESTED state - all dates valid (should pass)
    print("\nTest 5: REQUESTED state with all valid dates")
    release5 = {
        'id': 'RQONE03999405',
        'dcterms__type': 'BC-Release',
        'lifecyclestate': 'Requested',
        'planningfreeze': (date.today() + timedelta(days=10)).isoformat(),
        'implementationfreeze': (date.today() + timedelta(days=30)).isoformat(),
        'planneddate': (date.today() + timedelta(days=60)).isoformat()
    }
    
    rule5 = Rule_CheckDatesForBcAndFc(release5)
    results5 = rule5.execute()
    
    if results5:
        for result in results5:
            print(f"  ? {result.title}")
    else:
        print("  ? All checks passed")
