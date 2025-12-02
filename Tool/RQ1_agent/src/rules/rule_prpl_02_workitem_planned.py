#!/usr/bin/env python
"""
PRPL 02.00.00: Workitem in started state but no planned date

Rule Logic:
1. Check if workitem is in "Started" or active states (Requested, Planned, Developed, Committed, etc.)
2. Warn if workitem is active but has no planned date in planning tab
3. This ensures all active work has proper schedule planning

Workitem can be BC, FC, IFD, Issue-SW, etc.
"""

from enum import Enum
from typing import Optional
from dataclasses import dataclass


@dataclass
class ValidationResult:
    """Validation result"""
    passed: bool
    severity: str
    title: str
    description: str


class Rule_Workitem_PlannedDate:
    """
    PRPL 02.00.00: Workitem in started state but no planned date
    
    Validates that active workitems have planned dates entered
    in the planning tab.
    """
    
    RULE_ID = "PRPL 02.00.00"
    RULE_TITLE = "Workitem is in started state, but planned date for workitem is not entered in planning tab"
    
    # States that are considered "started" (active work)
    STARTED_STATES = {
        'Requested', 'Planned', 'Developed', 'Committed', 
        'Evaluated', 'Implemented'
    }
    
    # States that don't require planned date (new or terminal)
    EXEMPT_STATES = {
        'New', 'Closed', 'Canceled', 'Conflicted'
    }
    
    def __init__(self, workitem_data: dict):
        """
        Args:
            workitem_data: Workitem data with fields:
                - id: RQ1 number
                - dcterms__title: Title
                - dcterms__type: Type (Release, Issue, etc.)
                - lifecyclestate: Current state
                - planneddate: Planned date (optional)
        """
        self.workitem_data = workitem_data
    
    def execute(self) -> ValidationResult:
        """Execute validation rule."""
        
        workitem_id = self.workitem_data.get('id', 'UNKNOWN')
        workitem_type = self.workitem_data.get('dcterms__type', 'Workitem')
        state = self.workitem_data.get('lifecyclestate', '')
        planned_date = self.workitem_data.get('planneddate')
        
        # Skip if in exempt states (New, Closed, etc.)
        if state in self.EXEMPT_STATES:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Workitem in exempt state",
                description=f"{workitem_type} is in {state} state - planned date not required"
            )
        
        # Check if workitem is in started state
        if state not in self.STARTED_STATES:
            # Unknown state - assume OK
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Workitem state check skipped",
                description=f"Unknown state '{state}' - rule not applicable"
            )
        
        # Workitem is in started state - check if planned date exists
        if planned_date and str(planned_date).strip():
            # Has planned date - OK
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Workitem has planned date",
                description=f"{workitem_type} in {state} state has planned date set"
            )
        
        # Workitem is started but has no planned date - VIOLATION
        return ValidationResult(
            passed=False,
            severity="WARNING",
            title=f"Workitem in started state without planned date ({self.RULE_ID})",
            description=(
                f"{workitem_type} {workitem_id} is in {state} state.\n"
                f"Planned date is not entered in planning tab.\n\n"
                f"Active workitems must have planned dates for proper scheduling."
            )
        )
