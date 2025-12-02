#!/usr/bin/env python
"""
PRPL 12.00.00: IFD not closed when all mapped BCs are closed/cancelled

Rule Logic:
1. Get all BC-Releases mapped to IFD
2. Check if ALL BCs are in Closed or Canceled state
3. If yes, warn if IFD is NOT in Closed/Implemented state

This rule has NO direct Java equivalent in POST tool.
Implementing from CSV specification.
"""

from enum import Enum
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
class ValidationResult:
    """Validation result"""
    passed: bool
    severity: str  # "PASS", "WARNING"
    title: str
    description: str


class Rule_IFD_BcClosure:
    """
    PRPL 12.00.00: IFD not closed when all BCs are closed/cancelled
    
    Validates that IFD is closed when all its mapped BC-Releases
    are in Closed or Canceled state.
    """
    
    RULE_ID = "PRPL 12.00.00"
    RULE_TITLE = "IFD not closed, even though all mapped BCs are closed or cancelled"
    
    def __init__(self, ifd_data: dict, mapped_bcs: List[dict]):
        """
        Args:
            ifd_data: IFD Issue data with fields:
                - id: RQ1 number
                - dcterms__title: Title
                - lifecyclestate: Current state
            mapped_bcs: List of BC-Release data, each with:
                - id: RQ1 number
                - dcterms__title: Title
                - lifecyclestate: Current state
        """
        self.ifd_data = ifd_data
        self.mapped_bcs = mapped_bcs
    
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
                description=f"Unknown IFD state: {ifd_state_str}"
            )
        
        # Skip if no mapped BCs
        if not self.mapped_bcs:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="IFD has no mapped BCs",
                description="Rule PRPL 12.00.00 not applicable - no BC-Releases mapped"
            )
        
        # Check if IFD is already Closed or Implemented
        if ifd_state in [LifeCycleState_Issue.CLOSED, LifeCycleState_Issue.IMPLEMENTED]:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="IFD is in acceptable state",
                description=f"IFD is in {ifd_state.value} state - acceptable"
            )
        
        # Check all mapped BCs
        all_bcs_closed_or_canceled = True
        bc_states = []
        
        for bc in self.mapped_bcs:
            bc_state_str = bc.get('lifecyclestate', '')
            bc_id = bc.get('id', 'UNKNOWN')
            
            try:
                bc_state = LifeCycleState_Release[bc_state_str.upper().replace(' ', '_')]
                bc_states.append((bc_id, bc_state))
                
                if bc_state not in [LifeCycleState_Release.CLOSED, LifeCycleState_Release.CANCELED]:
                    all_bcs_closed_or_canceled = False
            except (KeyError, AttributeError):
                # Unknown BC state - assume not closed
                all_bcs_closed_or_canceled = False
                bc_states.append((bc_id, f"Unknown: {bc_state_str}"))
        
        # If all BCs are closed/canceled, IFD should be closed/implemented
        if all_bcs_closed_or_canceled:
            # Build description
            bc_summary = "\n".join([
                f"  - {bc_id}: {state.value if isinstance(state, LifeCycleState_Release) else state}"
                for bc_id, state in bc_states
            ])
            
            return ValidationResult(
                passed=False,
                severity="WARNING",
                title=f"IFD not closed even though all mapped BCs are closed/cancelled ({self.RULE_ID})",
                description=(
                    f"IFD {ifd_id} is in {ifd_state.value} state.\n"
                    f"All {len(self.mapped_bcs)} mapped BC-Releases are Closed or Canceled:\n"
                    f"{bc_summary}\n\n"
                    f"IFD should be in CLOSED or IMPLEMENTED state."
                )
            )
        
        # At least one BC is not closed - IFD can remain open
        return ValidationResult(
            passed=True,
            severity="PASS",
            title="IFD can remain open",
            description=(
                f"IFD is in {ifd_state.value} state. "
                f"Not all BCs are closed/canceled yet ({len(self.mapped_bcs)} BCs mapped)."
            )
        )


def validate_ifd_bc_closure(rq1_client, ifd_rq1_number: str) -> ValidationResult:
    """
    Convenience function to validate IFD using RQ1 client.
    
    Args:
        rq1_client: RQ1 Client instance
        ifd_rq1_number: IFD RQ1 number (e.g. "RQONE12345678")
    
    Returns:
        ValidationResult
    """
    from rq1.models import Issue, IssueProperty, Release, ReleaseProperty
    
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
    
    # Fetch mapped BC-Releases
    # Note: This requires navigating relationships in RQ1
    # For now, simulate with empty list (needs actual implementation)
    mapped_bcs = []
    
    # TODO: Query mapped BCs using relationship navigation
    # This might require:
    # 1. Get IFD's MAPPED_BC relationship
    # 2. Fetch each BC's state
    
    # Execute rule
    rule = Rule_IFD_BcClosure(ifd_data, mapped_bcs)
    return rule.execute()
