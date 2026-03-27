#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
PRPL 01: BC-R Requested State Check
====================================
Rule: BC-R is not in requested state, 8 weeks before PVER planned delivery date

Validation Logic:
- For each BC release assigned to user
- Get all mapped PVER/PVAR releases (via Releasereleasemap)
- For each PVER/PVAR with planned date:
  * Calculate: pver_planned_date - 8 weeks = threshold_date
  * If current_date >= threshold_date AND BC not in "Requested" state
  * Then violation

Reference: activated_rules.csv - PRPL 01.00.00, Activated 1-Jul-16
"""

from datetime import datetime, timezone, timedelta
from dataclasses import dataclass
from typing import List, Optional


@dataclass
class PverMapping:
    """PVER/PVAR release mapping data"""
    pver_id: str
    pver_title: str
    pver_type: str  # "PVER" or "PVAR"
    planned_date: Optional[datetime]


@dataclass
class RuleResult:
    """Rule validation result"""
    rule_id: str
    passed: bool
    severity: str
    description: str


class Rule_BC_RequestedState:
    """
    PRPL 01: BC-R must be in Requested state 8 weeks before PVER planned delivery
    """
    
    def __init__(self, bc_data: dict, pver_mappings: List[PverMapping]):
        """
        Args:
            bc_data: BC release data with keys: id, dcterms__title, lifecyclestate
            pver_mappings: List of mapped PVER/PVAR releases with planned dates
        """
        self.bc = bc_data
        self.pver_mappings = pver_mappings
        self.current_date = datetime.now(timezone.utc)
    
    def execute(self) -> RuleResult:
        """Execute the rule validation"""
        
        bc_id = self.bc.get('id', 'UNKNOWN')
        bc_title = self.bc.get('dcterms__title', '')
        bc_state = self.bc.get('lifecyclestate', '')
        
        # If BC already in Requested state or later (Developed, Closed, Cancelled), pass
        if bc_state in ['Requested', 'Developed', 'Planned', 'Closed', 'Canceled']:
            return RuleResult(
                rule_id="PRPL 01",
                passed=True,
                severity="INFO",
                description=f"BC {bc_id} is in {bc_state} state"
            )
        
        # Check each PVER/PVAR mapping
        violations = []
        for pver in self.pver_mappings:
            if not pver.planned_date:
                continue
            
            # Calculate threshold: PVER planned date - 8 weeks
            threshold_date = pver.planned_date - timedelta(weeks=8)
            
            # Check if we're within 8 weeks of PVER planned date
            if self.current_date >= threshold_date:
                days_until_pver = (pver.planned_date - self.current_date).days
                
                violations.append({
                    'pver_id': pver.pver_id,
                    'pver_title': pver.pver_title,
                    'pver_type': pver.pver_type,
                    'pver_planned_date': pver.planned_date.strftime('%Y-%m-%d'),
                    'threshold_date': threshold_date.strftime('%Y-%m-%d'),
                    'days_until_pver': days_until_pver
                })
        
        # If any violations found
        if violations:
            description_lines = [
                f"Hint: BC {bc_id} should be in 'Requested' state.",
                f"Title: {bc_title}",
                f"Current date: {self.current_date.strftime('%Y-%m-%d')}",
                "",
                f"Mapped PVER/PVAR releases requiring 'Requested' state:"
            ]
            
            for v in violations:
                description_lines.extend([
                    f"",
                    f"  - {v['pver_type']} {v['pver_id']}: {v['pver_title']}",
                    f"    Planned delivery: {v['pver_planned_date']} ({v['days_until_pver']} days from now)",
                    f"    Threshold date: {v['threshold_date']} (8 weeks before delivery)",
                    f"    BC must be in 'Requested' state by this date."
                ])
            
            return RuleResult(
                rule_id="PRPL 01",
                passed=False,
                severity="WARNING",
                description="\n".join(description_lines)
            )
        
        # No violations - either no PVER mappings or all PVER dates are > 8 weeks away
        return RuleResult(
            rule_id="PRPL 01",
            passed=True,
            severity="INFO",
            description=f"BC {bc_id} - No PRPL 01 violations (not yet within 8 weeks of any PVER delivery)"
        )


# Example usage for testing
if __name__ == "__main__":
    # Test case 1: BC in New state, PVER delivery in 6 weeks (should fail)
    bc_data = {
        'id': 'RQONE12345',
        'dcterms__title': 'BC : Test Release',
        'lifecyclestate': 'New'
    }
    
    pver_mapping = PverMapping(
        pver_id='RQONE67890',
        pver_title='PVER : 2025.06',
        pver_type='PVER',
        planned_date=datetime.now(timezone.utc) + timedelta(weeks=6)
    )
    
    rule = Rule_BC_RequestedState(bc_data, [pver_mapping])
    result = rule.execute()
    
    print(f"Rule: {result.rule_id}")
    print(f"Passed: {result.passed}")
    print(f"Severity: {result.severity}")
    print(f"Description:\n{result.description}")
