#!/usr/bin/env python
"""
PRPL 07.00.00: BC planned date later than PVER/PVAR requested delivery date

Original Java: Rule_Bc_CheckPstDates.java
Execution Group: EXCITED_CPC, BC_PLANNING

Rule Logic:
1. Skip if BC is in Canceled, Developed, Conflicted, or Closed state
2. For each mapped PVER/PVAR (PST):
   - Skip if PST mapping (RRM) is in Canceled or Conflicted state
   - Compare BC planned date with PST requested delivery date
   - Warn if BC planned date > PST requested delivery date

This checks if BC can meet its delivery commitments to PVERs/PVARs.
"""

from enum import Enum
from datetime import datetime, date
from typing import List, Optional
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


class LifeCycleState_RRM(Enum):
    """RRM (Release Requirement Mapping) lifecycle states"""
    NEW = "New"
    REQUESTED = "Requested"
    PLANNED = "Planned"
    COMMITTED = "Committed"
    DELIVERED = "Delivered"
    CLOSED = "Closed"
    CANCELED = "Canceled"
    CONFLICTED = "Conflicted"


@dataclass
class PstMapping:
    """PST (PVER/PVAR) mapping to BC"""
    pst_id: str  # PVER/PVAR RQ1 number
    pst_type: str  # "PVER" or "PVAR"
    rrm_state: str  # RRM state
    requested_delivery_date: Optional[datetime]


@dataclass
class ValidationResult:
    """Validation result"""
    passed: bool
    severity: str  # "PASS", "WARNING"
    title: str
    description: str
    violations: List[str]  # List of violation details


class Rule_Bc_CheckPstDates:
    """
    PRPL 07.00.00: BC planned date vs PVER/PVAR delivery dates
    
    Validates that BC planned date does not exceed requested delivery
    dates of mapped PVERs/PVARs.
    """
    
    RULE_ID = "PRPL 07.00.00"
    RULE_TITLE = "Planned date of BC later than requested delivery date of any mapped PVER or PVAR"
    
    def __init__(self, bc_data: dict, pst_mappings: List[PstMapping]):
        """
        Args:
            bc_data: BC-Release data with fields:
                - id: RQ1 number
                - dcterms__title: Title
                - lifecyclestate: Current state
                - planneddate: BC planned date
            pst_mappings: List of PST (PVER/PVAR) mappings
        """
        self.bc_data = bc_data
        self.pst_mappings = pst_mappings
    
    def execute(self) -> ValidationResult:
        """Execute validation rule."""
        
        bc_id = self.bc_data.get('id', 'UNKNOWN')
        bc_state_str = self.bc_data.get('lifecyclestate', '')
        bc_planned_date = self.bc_data.get('planneddate')
        
        # Parse BC state
        try:
            bc_state = LifeCycleState_Release[bc_state_str.upper().replace(' ', '_')]
        except (KeyError, AttributeError):
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="BC state check skipped",
                description=f"Unknown BC state: {bc_state_str}",
                violations=[]
            )
        
        # Skip if BC is in terminal states
        if bc_state in [
            LifeCycleState_Release.CANCELED,
            LifeCycleState_Release.DEVELOPED,
            LifeCycleState_Release.CONFLICTED,
            LifeCycleState_Release.CLOSED
        ]:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="BC in terminal state",
                description=f"BC is in {bc_state.value} state - date check not applicable",
                violations=[]
            )
        
        # Parse BC planned date
        if not bc_planned_date:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="BC has no planned date",
                description="Rule PRPL 07.00.00 not applicable - no planned date set",
                violations=[]
            )
        
        if isinstance(bc_planned_date, str):
            bc_planned_date = datetime.fromisoformat(bc_planned_date.replace('Z', '+00:00'))
        
        # Check all PST mappings
        violations = []
        
        for pst_mapping in self.pst_mappings:
            # Skip if RRM is in Canceled or Conflicted state
            try:
                rrm_state = LifeCycleState_RRM[pst_mapping.rrm_state.upper().replace(' ', '_')]
                if rrm_state in [LifeCycleState_RRM.CANCELED, LifeCycleState_RRM.CONFLICTED]:
                    continue
            except (KeyError, AttributeError):
                # Unknown RRM state - skip
                continue
            
            # Check if PST has requested delivery date
            if not pst_mapping.requested_delivery_date:
                continue
            
            pst_req_date = pst_mapping.requested_delivery_date
            if isinstance(pst_req_date, str):
                pst_req_date = datetime.fromisoformat(pst_req_date.replace('Z', '+00:00'))
            
            # Compare dates: BC planned date should be <= PST requested delivery date
            if bc_planned_date > pst_req_date:
                violations.append(
                    f"{pst_mapping.pst_type} {pst_mapping.pst_id}: "
                    f"Requested delivery {pst_req_date.strftime('%Y-%m-%d')}, "
                    f"BC planned {bc_planned_date.strftime('%Y-%m-%d')} "
                    f"(BC is {(bc_planned_date - pst_req_date).days} days late)"
                )
        
        # Return result
        if violations:
            violation_details = "\n".join([f"  - {v}" for v in violations])
            return ValidationResult(
                passed=False,
                severity="WARNING",
                title=f"BC planned date too late for mapped PVER/PVAR ({self.RULE_ID})",
                description=(
                    f"BC {bc_id} planned date ({bc_planned_date.strftime('%Y-%m-%d')}) "
                    f"is later than requested delivery date(s) of {len(violations)} mapped PST(s):\n"
                    f"{violation_details}\n\n"
                    f"Hint: BC may not meet delivery commitments."
                ),
                violations=violations
            )
        
        return ValidationResult(
            passed=True,
            severity="PASS",
            title="BC planned date acceptable",
            description=(
                f"BC planned date ({bc_planned_date.strftime('%Y-%m-%d')}) "
                f"meets all {len(self.pst_mappings)} mapped PST delivery dates."
            ),
            violations=[]
        )


def validate_bc_pst_dates(rq1_client, bc_rq1_number: str) -> ValidationResult:
    """
    Convenience function to validate BC using RQ1 client.
    
    Args:
        rq1_client: RQ1 Client instance
        bc_rq1_number: BC RQ1 number
    
    Returns:
        ValidationResult
    """
    from rq1.models import Release, ReleaseProperty
    
    # Fetch BC
    bc = rq1_client.get_record_by_rq1_number(
        Release,
        bc_rq1_number,
        select=[
            ReleaseProperty.id,
            ReleaseProperty.dcterms__title,
            ReleaseProperty.lifecyclestate,
            ReleaseProperty.planneddate
        ]
    )
    
    bc_data = {
        'id': bc.id,
        'dcterms__title': bc.dcterms__title,
        'lifecyclestate': bc.lifecyclestate,
        'planneddate': bc.planneddate
    }
    
    # Fetch mapped PSTs (PVERs/PVARs)
    # TODO: Query PST mappings using relationship navigation
    # This requires:
    # 1. Navigate BC.MAPPED_PST relationship
    # 2. Get RRM state and requested delivery date
    # 3. Get PST (PVER/PVAR) details
    
    pst_mappings = []  # Placeholder
    
    # Execute rule
    rule = Rule_Bc_CheckPstDates(bc_data, pst_mappings)
    return rule.execute()
