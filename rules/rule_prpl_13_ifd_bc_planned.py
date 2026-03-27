#!/usr/bin/env python
"""
PRPL 13.00.00: IFD not implemented or closed after BC planned date

Rule Logic:
1. Get all BC-Releases mapped to IFD
2. For each BC, check if BC planned date is in the past
3. If any BC is past its planned date, IFD should be in Implemented or Closed state
4. Warn if IFD is still in earlier states (Committed, Evaluated, etc.)

This ensures IFDs are properly closed/implemented when their BCs are due.
"""

from enum import Enum
from datetime import datetime, timezone
from typing import List, Optional
from dataclasses import dataclass


class LifeCycleState_Issue(Enum):
    """Issue lifecycle states"""
    NEW = "New"
    EVALUATED = "Evaluated"
    COMMITTED = "Committed"
    IMPLEMENTED = "Implemented"
    CLOSED = "Closed"
    CANCELED = "Canceled"


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
class BcMapping:
    """BC-Release mapping to IFD"""
    bc_id: str
    bc_title: str
    bc_state: str
    planned_date: Optional[datetime]


@dataclass
class ValidationResult:
    """Validation result"""
    passed: bool
    severity: str
    title: str
    description: str
    violations: List[str]


class Rule_IFD_BcPlannedDate:
    """
    PRPL 13.00.00: IFD not implemented/closed after BC planned date
    
    Validates that IFD is implemented or closed when its mapped
    BC-Releases are past their planned dates.
    """
    
    RULE_ID = "PRPL 13.00.00"
    RULE_TITLE = "IFD is not implemented or closed, after planned dated of BC-R"
    
    def __init__(self, ifd_data: dict, bc_mappings: List[BcMapping]):
        """
        Args:
            ifd_data: IFD Issue data with fields:
                - id: RQ1 number
                - dcterms__title: Title
                - lifecyclestate: Current state
            bc_mappings: List of BC-Release mappings
        """
        self.ifd_data = ifd_data
        self.bc_mappings = bc_mappings
    
    def execute(self) -> ValidationResult:
        """Execute validation rule."""
        
        ifd_id = self.ifd_data.get('id', 'UNKNOWN')
        ifd_state_str = self.ifd_data.get('lifecyclestate', '')
        
        # Parse IFD state
        try:
            ifd_state = LifeCycleState_Issue[ifd_state_str.upper().replace(' ', '_')]
        except (KeyError, AttributeError):
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="IFD state check skipped",
                description=f"Unknown IFD state: {ifd_state_str}",
                violations=[]
            )
        
        # Skip if no mapped BCs
        if not self.bc_mappings:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="IFD has no mapped BCs",
                description="Rule PRPL 13.00.00 not applicable - no BC-Releases mapped",
                violations=[]
            )
        
        # Skip if IFD is already Implemented or Closed
        if ifd_state in [LifeCycleState_Issue.IMPLEMENTED, LifeCycleState_Issue.CLOSED]:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="IFD is in acceptable state",
                description=f"IFD is in {ifd_state.value} state - acceptable",
                violations=[]
            )
        
        # Check if any BC has passed its planned date
        now = datetime.now(timezone.utc)
        overdue_bcs = []
        
        for bc in self.bc_mappings:
            if not bc.planned_date:
                continue
            
            planned_date = bc.planned_date
            if isinstance(planned_date, str):
                planned_date = datetime.fromisoformat(planned_date.replace('Z', '+00:00'))
            
            # Skip canceled/conflicted BCs
            try:
                bc_state = LifeCycleState_Release[bc.bc_state.upper().replace(' ', '_')]
                if bc_state in [LifeCycleState_Release.CANCELED, LifeCycleState_Release.CONFLICTED]:
                    continue
            except (KeyError, AttributeError):
                pass
            
            # Check if BC planned date is in the past
            if planned_date < now:
                days_overdue = (now - planned_date).days
                overdue_bcs.append(
                    f"{bc.bc_id}: Planned {planned_date.strftime('%Y-%m-%d')} "
                    f"({days_overdue} days ago)"
                )
        
        # If any BC is overdue, IFD should be Implemented or Closed
        if overdue_bcs:
            bc_details = "\n".join([f"  - {bc}" for bc in overdue_bcs])
            return ValidationResult(
                passed=False,
                severity="WARNING",
                title=f"IFD not implemented/closed after BC planned date ({self.RULE_ID})",
                description=(
                    f"IFD {ifd_id} is in {ifd_state.value} state.\n"
                    f"{len(overdue_bcs)} mapped BC(s) are past their planned dates:\n"
                    f"{bc_details}\n\n"
                    f"Hint: IFD should be in IMPLEMENTED or CLOSED state."
                ),
                violations=overdue_bcs
            )
        
        # All BCs are not yet at planned date - IFD can remain in current state
        return ValidationResult(
            passed=True,
            severity="PASS",
            title="IFD state acceptable",
            description=(
                f"IFD is in {ifd_state.value} state. "
                f"No BCs are past their planned dates yet ({len(self.bc_mappings)} BCs mapped)."
            ),
            violations=[]
        )


def validate_ifd_bc_planned_date(rq1_client, ifd_rq1_number: str) -> ValidationResult:
    """
    Convenience function to validate IFD using RQ1 client.
    
    Args:
        rq1_client: RQ1 Client instance
        ifd_rq1_number: IFD RQ1 number
    
    Returns:
        ValidationResult
    """
    from rq1.models import Issue, IssueProperty
    
    # Fetch IFD
    ifd = rq1_client.get_record_by_rq1_number(
        Issue,
        ifd_rq1_number,
        select=[
            IssueProperty.id,
            IssueProperty.dcterms__title,
            IssueProperty.lifecyclestate
        ]
    )
    
    ifd_data = {
        'id': ifd.id,
        'dcterms__title': ifd.dcterms__title,
        'lifecyclestate': ifd.lifecyclestate
    }
    
    # Fetch mapped BCs
    # TODO: Query mapped BCs using relationship navigation
    bc_mappings = []
    
    # Execute rule
    rule = Rule_IFD_BcPlannedDate(ifd_data, bc_mappings)
    return rule.execute()
