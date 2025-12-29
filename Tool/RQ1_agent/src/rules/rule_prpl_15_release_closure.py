#!/usr/bin/env python
"""
PRPL 15.00.00: Release not closed after planned date for BC and FC

Rule Logic:
- For BC/FC Releases in Planned or Developed state
- Check if planned date has passed
- If yes, Release should be Closed

Requirements:
- BC/FC must be closed after planned delivery date
- Excludes: Canceled, Conflicted, Closed states
- Only checks: Planned, Developed states

Pseudo Code from Excel:
if bcRelease = canceledOrConflicted then return
if bcRelease.PLANNED_DATE = isInThePast && (bcRelease.LIFE_CYCLE_STATE = PLANNED || bcRelease.LIFE_CYCLE_STATE = DEVELOPED) then
    Warning "BCR not closed after planned date"
"""

from enum import Enum
from datetime import datetime, date


class LifeCycleState_Release(Enum):
    """Release lifecycle states"""
    NEW = "New"
    REQUESTED = "Requested"
    PLANNED = "Planned"
    DEVELOPED = "Developed"
    CLOSED = "Closed"
    CANCELED = "Canceled"
    CONFLICTED = "Conflicted"


class ValidationResult:
    """Result of a validation rule execution"""
    def __init__(self, passed: bool, severity: str, description: str, violations: list = None):
        self.passed = passed
        self.severity = severity
        self.description = description
        self.violations = violations or []


class Rule_Release_Closure:
    """
    PRPL 15.00.00: Release not closed after planned date for BC and FC
    
    Checks if a BC/FC Release should be closed because planned date has passed.
    """
    
    RULE_ID = "PRPL 15"
    RULE_NAME = "Release not closed after planned date"
    
    def __init__(self, release_data: dict):
        """
        Args:
            release_data: Dict with keys: id, releasetype, lifecyclestate, planneddate, dcterms__title
        """
        self.release_id = release_data.get('id', 'UNKNOWN')
        self.release_type = release_data.get('releasetype', '')
        self.state = release_data.get('lifecyclestate', '')
        self.planned_date = release_data.get('planneddate')
        self.title = release_data.get('dcterms__title', '')
    
    def execute(self) -> ValidationResult:
        """
        Execute the validation rule
        
        Returns:
            ValidationResult with passed status and details
        """
        # Only check BC and FC releases
        if self.release_type not in ['BC', 'FC']:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description="Not a BC or FC release"
            )
        
        # Skip Canceled, Conflicted, Closed
        if self.state in [LifeCycleState_Release.CANCELED.value,
                          LifeCycleState_Release.CONFLICTED.value,
                          LifeCycleState_Release.CLOSED.value]:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"Release {self.release_id} is {self.state} (excluded from check)"
            )
        
        # Only check Planned and Developed states
        if self.state not in [LifeCycleState_Release.PLANNED.value,
                              LifeCycleState_Release.DEVELOPED.value]:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"Release {self.release_id} is in {self.state} state (not Planned/Developed)"
            )
        
        # Skip if no planned date
        if not self.planned_date:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"Release {self.release_id} has no planned date"
            )
        
        # Parse planned date
        try:
            if isinstance(self.planned_date, datetime):
                planned_date_obj = self.planned_date.date()
            elif isinstance(self.planned_date, date):
                planned_date_obj = self.planned_date
            else:
                planned_date_obj = datetime.fromisoformat(str(self.planned_date)).date()
        except:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"Release {self.release_id} has invalid planned date format"
            )
        
        # Check if planned date is in the past
        today = date.today()
        if planned_date_obj >= today:
            # Planned date not yet passed
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"Release {self.release_id} planned date {planned_date_obj} not yet passed"
            )
        
        # Planned date passed but not closed - VIOLATION
        days_overdue = (today - planned_date_obj).days
        
        violation_msg = (
            f"{self.release_type} {self.release_id} not closed after planned date "
            f"({planned_date_obj}, {days_overdue} days overdue)"
        )
        
        details = (
            f"{self.release_type} {self.release_id} closure overdue.\n"
            f"Title: {self.title}\n"
            f"Planned date: {planned_date_obj}\n"
            f"Current state: {self.state}\n"
            f"Days overdue: {days_overdue} days\n\n"
            f"Hint: Close {self.release_type} {self.release_id} as planned date has passed."
        )
        
        return ValidationResult(
            passed=False,
            severity="WARNING",
            description=details,
            violations=[violation_msg]
        )
