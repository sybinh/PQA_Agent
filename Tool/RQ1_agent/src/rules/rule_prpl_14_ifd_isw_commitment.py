#!/usr/bin/env python
"""
PRPL 14.00.00: IFD not committed though Issue-SW committed

Rule Logic:
- For each IFD (Issue FD) in state New/Submitted/Evaluated
- Check if parent Issue-SW exists and is in Committed state
- If yes, IFD should also be Committed

Requirements:
- IFD must be committed when parent ISW is committed
- Excludes: Canceled, Conflicted, Closed states
- Excludes: Already Committed/Implemented/Closed IFDs

Pseudo Code from Excel:
if issueFD = canceledOrConflicted then return
if IFD = COMMITTED || IFD = IMPLEMENTED || IFD = CLOSED then return
if issueFD.PARENT instanceof issueSW && parent.LIFE_CYCLE_STATE = COMMITTED then
    Warning "Not committed, even though ISW is committed"
"""

from enum import Enum


class LifeCycleState_Issue(Enum):
    """Issue lifecycle states"""
    NEW = "New"
    SUBMITTED = "Submitted"
    EVALUATED = "Evaluated"
    COMMITTED = "Committed"
    IMPLEMENTED = "Implemented"
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


class Rule_IFD_ISW_Commitment:
    """
    PRPL 14.00.00: IFD not committed though Issue-SW committed
    
    Checks if an IFD should be committed because its parent ISW is already committed.
    """
    
    RULE_ID = "PRPL 14"
    RULE_NAME = "IFD not committed though Issue-SW committed"
    
    def __init__(self, ifd_data: dict, parent_isw_data: dict = None):
        """
        Args:
            ifd_data: Dict with keys: id, cq__Type, lifecyclestate, dcterms__title
            parent_isw_data: Dict with keys: id, lifecyclestate (optional, can be None)
        """
        self.ifd_id = ifd_data.get('id', 'UNKNOWN')
        self.ifd_type = ifd_data.get('cq__Type', '')
        self.ifd_state = ifd_data.get('lifecyclestate', '')
        self.ifd_title = ifd_data.get('dcterms__title', '')
        
        # Parent ISW info (can be None if no parent)
        self.parent_isw = parent_isw_data
        self.parent_isw_id = parent_isw_data.get('id', 'UNKNOWN') if parent_isw_data else None
        self.parent_isw_state = parent_isw_data.get('lifecyclestate', '') if parent_isw_data else None
    
    def execute(self) -> ValidationResult:
        """
        Execute the validation rule
        
        Returns:
            ValidationResult with passed status and details
        """
        # Only check IFD (Issue FD)
        if self.ifd_type != 'Issue FD':
            return ValidationResult(
                passed=True,
                severity="INFO",
                description="Not an IFD issue"
            )
        
        # Skip Canceled, Conflicted, Closed
        if self.ifd_state in [LifeCycleState_Issue.CANCELED.value, 
                              LifeCycleState_Issue.CONFLICTED.value,
                              LifeCycleState_Issue.CLOSED.value]:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.ifd_id} is {self.ifd_state} (excluded from check)"
            )
        
        # Skip already Committed/Implemented/Closed
        if self.ifd_state in [LifeCycleState_Issue.COMMITTED.value,
                              LifeCycleState_Issue.IMPLEMENTED.value,
                              LifeCycleState_Issue.CLOSED.value]:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.ifd_id} already {self.ifd_state}"
            )
        
        # Check if parent ISW exists and is committed
        if not self.parent_isw:
            # No parent ISW - not a violation, just skip
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.ifd_id} has no parent ISW"
            )
        
        # If parent ISW is committed, IFD should also be committed
        if self.parent_isw_state == LifeCycleState_Issue.COMMITTED.value:
            violation_msg = (
                f"IFD {self.ifd_id} is not committed even though parent ISW {self.parent_isw_id} is committed"
            )
            
            details = (
                f"IFD {self.ifd_id} commitment mismatch with parent ISW.\n"
                f"Title: {self.ifd_title}\n"
                f"IFD state: {self.ifd_state}\n"
                f"Parent ISW: {self.parent_isw_id}\n"
                f"Parent ISW state: {self.parent_isw_state}\n\n"
                f"Hint: Commit IFD {self.ifd_id} to match parent ISW commitment."
            )
            
            return ValidationResult(
                passed=False,
                severity="WARNING",
                description=details,
                violations=[violation_msg]
            )
        
        # Parent ISW exists but not committed - no violation
        return ValidationResult(
            passed=True,
            severity="INFO",
            description=f"IFD {self.ifd_id} state {self.ifd_state}, parent ISW {self.parent_isw_id} state {self.parent_isw_state}"
        )
