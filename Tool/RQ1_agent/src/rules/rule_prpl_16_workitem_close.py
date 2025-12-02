#!/usr/bin/env python
"""
PRPL 16.00.00: Workitem not closed after planned date

Rule Logic:
1. Check if workitem has a planned date
2. Check if planned date is in the past
3. Warn if workitem is NOT in Closed state after planned date

This applies to any workitem (BC, FC, IFD, etc.) in RQ1.
Similar to PRPL 15.00.00 but more generic.
"""

from enum import Enum
from datetime import datetime, timezone
from typing import Optional
from dataclasses import dataclass


@dataclass
class ValidationResult:
    """Validation result"""
    passed: bool
    severity: str
    title: str
    description: str


class Rule_Workitem_Close:
    """
    PRPL 16.00.00: Workitem not closed after planned date
    
    Validates that workitem (BC/FC/IFD/Issue/Release) is closed
    after its planned date.
    """
    
    RULE_ID = "PRPL 16.00.00"
    RULE_TITLE = "Workitem is not closed after planned date"
    
    # States that are acceptable (closed/terminal states)
    ACCEPTABLE_STATES = {'Closed', 'Canceled', 'Conflicted', 'Implemented'}
    
    def __init__(self, workitem_data: dict):
        """
        Args:
            workitem_data: Workitem data with fields:
                - id: RQ1 number
                - dcterms__title: Title
                - dcterms__type: Type (Release, Issue, etc.)
                - lifecyclestate: Current state
                - planneddate: Planned date
        """
        self.workitem_data = workitem_data
    
    def execute(self) -> ValidationResult:
        """Execute validation rule."""
        
        workitem_id = self.workitem_data.get('id', 'UNKNOWN')
        workitem_type = self.workitem_data.get('dcterms__type', 'Workitem')
        state = self.workitem_data.get('lifecyclestate', '')
        planned_date = self.workitem_data.get('planneddate')
        
        # Check if planned date exists
        if not planned_date:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="No planned date set",
                description=f"{workitem_type} has no planned date - rule not applicable"
            )
        
        # Parse planned date
        if isinstance(planned_date, str):
            planned_date = datetime.fromisoformat(planned_date.replace('Z', '+00:00'))
        
        # Check if in acceptable state
        if state in self.ACCEPTABLE_STATES:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Workitem closure check passed",
                description=f"{workitem_type} is in {state} state - acceptable"
            )
        
        # Check if planned date is in the past
        now = datetime.now(timezone.utc)
        
        if planned_date >= now:
            # Planned date is in future - OK
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Workitem not yet due",
                description=(
                    f"{workitem_type} planned date is {planned_date.strftime('%Y-%m-%d')}, "
                    f"which is in the future. Current state {state} is acceptable."
                )
            )
        
        # Planned date is in past, but workitem is not closed
        days_overdue = (now - planned_date).days
        
        return ValidationResult(
            passed=False,
            severity="WARNING",
            title=f"Workitem not closed after planned date ({self.RULE_ID})",
            description=(
                f"{workitem_type} {workitem_id} is in {state} state.\n"
                f"Planned date: {planned_date.strftime('%Y-%m-%d')} ({days_overdue} days ago)\n"
                f"Current date: {now.strftime('%Y-%m-%d')}\n\n"
                f"Workitem should be in Closed, Canceled, or Implemented state."
            )
        )
