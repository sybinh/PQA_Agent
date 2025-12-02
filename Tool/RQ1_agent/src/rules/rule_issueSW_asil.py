#!/usr/bin/env python
"""
Rule_IssueSW_ASIL - ASIL level validation for I-SW and I-FD

Original Java: DataModel/Rq1/Monitoring/Rule_IssueSW_ASIL.java
Rule ID: I_SW_10__ASIL
Execution Group: I_SW_PLANNING, EXCITED_CPC

Description:
Implements the check for the ASIL level of an I-SW and its child I-FD.

Checks:
1. The ASIL level of the I-SW has to be set
   - WARNING if not set or invalid
2. At least one I-FD has to have an ASIL level high enough for the I-SW
   - WARNING if no I-FD meets the requirement

Only checked for I-SW in states: Evaluated, Committed, Implemented
Skipped for states: New, Closed, Canceled, Conflicted
"""

from enum import Enum
from typing import List, Optional
from dataclasses import dataclass


class AsilClassification(Enum):
    """ASIL classification levels from ISO 26262"""
    QM = "QM"
    ASIL_A = "ASIL A"
    ASIL_B = "ASIL B"
    ASIL_C = "ASIL C"
    ASIL_D = "ASIL D"
    ASIL_A_B = "ASIL A(B)"
    ASIL_A_C = "ASIL A(C)"
    ASIL_A_D = "ASIL A(D)"
    ASIL_B_C = "ASIL B(C)"
    ASIL_B_D = "ASIL B(D)"
    ASIL_C_D = "ASIL C(D)"


class LifeCycleState(Enum):
    """Issue lifecycle states"""
    NEW = "New"
    EVALUATED = "Evaluated"
    COMMITTED = "Committed"
    IMPLEMENTED = "Implemented"
    CLOSED = "Closed"
    CANCELED = "Canceled"
    CONFLICTED = "Conflicted"


@dataclass
class ValidationResult:
    """Result of rule validation"""
    passed: bool
    severity: str  # "PASS", "WARNING", "FAILURE"
    title: str
    description: str


class Rule_IssueSW_ASIL:
    """
    ASIL level validation rule for Issue-SW and Issue-FD
    
    Validates that:
    1. I-SW has valid ASIL classification
    2. At least one I-FD has sufficient ASIL level for I-SW
    """
    
    RULE_ID = "I_SW_10__ASIL"
    RULE_NAME = "ASIL level of I-SW and I-FD"
    
    # States where rule should NOT execute
    SKIP_STATES = {
        LifeCycleState.NEW,
        LifeCycleState.CLOSED,
        LifeCycleState.CANCELED,
        LifeCycleState.CONFLICTED
    }
    
    def __init__(self, issue_sw: dict, child_issues_fd: List[dict] = None):
        """
        Initialize rule with Issue-SW data
        
        Args:
            issue_sw: Issue-SW record with ASIL_CLASSIFICATION and lifecyclestate
            child_issues_fd: List of child Issue-FD records
        """
        self.issue_sw = issue_sw
        self.child_issues_fd = child_issues_fd or []
    
    def execute(self) -> ValidationResult:
        """
        Execute ASIL validation rule
        
        Returns:
            ValidationResult with pass/warning/failure status
        """
        # Step 1: Check if rule should be skipped based on state
        lifecycle_state = self._get_lifecycle_state()
        if lifecycle_state in self.SKIP_STATES:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Rule skipped",
                description=f"Rule not applicable for state: {lifecycle_state.value}"
            )
        
        # Step 2: Check that ASIL level is set and valid on I-SW
        asil_sw = self._get_asil_classification(self.issue_sw)
        if asil_sw is None:
            return ValidationResult(
                passed=False,
                severity="WARNING",
                title="ASIL level missing for I-SW",
                description="The ASIL level set for the I-SW is not a valid one."
            )
        
        # Step 3: Get required ASIL levels for I-FD based on I-SW level
        required_fd_levels = self._get_required_fd_asil_levels(asil_sw)
        
        # QM doesn't require checking children
        if required_fd_levels is None:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="ASIL check passed",
                description="I-SW has QM classification - no I-FD check required"
            )
        
        # Step 4: No warning if no I-FD connected
        if not self.child_issues_fd:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="ASIL check passed",
                description="No child I-FD connected - check not applicable"
            )
        
        # Step 5: Check if at least one I-FD has sufficient ASIL level
        for i_fd in self.child_issues_fd:
            asil_fd = self._get_asil_classification(i_fd)
            if asil_fd and asil_fd in required_fd_levels:
                return ValidationResult(
                    passed=True,
                    severity="PASS",
                    title="ASIL check passed",
                    description=f"I-FD with {asil_fd.value} meets I-SW requirement {asil_sw.value}"
                )
        
        # Step 6: No I-FD has sufficient ASIL level
        required_levels_text = ", ".join(level.value for level in required_fd_levels)
        return ValidationResult(
            passed=False,
            severity="WARNING",
            title="ASIL level on I-FD not sufficient for I-SW",
            description=f"The ASIL level of the I-SW is {asil_sw.value}\n\n"
                       f"Therefore, at least one child I-FD has to have one of the following ASIL levels:\n"
                       f"{required_levels_text}"
        )
    
    def _get_lifecycle_state(self) -> LifeCycleState:
        """Get lifecycle state from issue_sw"""
        state_text = self.issue_sw.get('lifecyclestate', '').strip()
        for state in LifeCycleState:
            if state.value.lower() == state_text.lower():
                return state
        return LifeCycleState.NEW  # Default
    
    def _get_asil_classification(self, issue: dict) -> Optional[AsilClassification]:
        """
        Parse ASIL classification from issue record
        
        Args:
            issue: Issue record with asilclassification field
            
        Returns:
            AsilClassification enum or None if invalid
        """
        asil_text = issue.get('asilclassification', '').strip()
        if not asil_text:
            return None
        
        # Normalize text for comparison
        asil_text_normalized = asil_text.upper().replace('(', '').replace(')', '')
        
        for asil in AsilClassification:
            asil_enum_normalized = asil.value.upper().replace('(', '').replace(')', '')
            if asil_enum_normalized == asil_text_normalized:
                return asil
        
        return None
    
    def _get_required_fd_asil_levels(self, asil_sw: AsilClassification) -> Optional[List[AsilClassification]]:
        """
        Get list of acceptable ASIL levels for I-FD based on I-SW level
        
        Args:
            asil_sw: ASIL classification of I-SW
            
        Returns:
            List of acceptable AsilClassification for I-FD, or None for QM
        """
        # QM - no check necessary
        if asil_sw == AsilClassification.QM:
            return None
        
        # ASIL A and derivatives - accept A, B, C, D
        if asil_sw in (AsilClassification.ASIL_A, 
                       AsilClassification.ASIL_A_B,
                       AsilClassification.ASIL_A_C,
                       AsilClassification.ASIL_A_D):
            return [AsilClassification.ASIL_A, 
                   AsilClassification.ASIL_B,
                   AsilClassification.ASIL_C, 
                   AsilClassification.ASIL_D]
        
        # ASIL B and derivatives - accept B, C, D
        if asil_sw in (AsilClassification.ASIL_B,
                       AsilClassification.ASIL_B_C,
                       AsilClassification.ASIL_B_D):
            return [AsilClassification.ASIL_B,
                   AsilClassification.ASIL_C,
                   AsilClassification.ASIL_D]
        
        # ASIL C and derivatives - accept C, D
        if asil_sw in (AsilClassification.ASIL_C,
                       AsilClassification.ASIL_C_D):
            return [AsilClassification.ASIL_C,
                   AsilClassification.ASIL_D]
        
        # ASIL D - only accept D
        if asil_sw == AsilClassification.ASIL_D:
            return [AsilClassification.ASIL_D]
        
        return []


# Example usage
if __name__ == "__main__":
    # Test case 1: I-SW with ASIL B, I-FD with ASIL C (should pass)
    issue_sw_1 = {
        'id': 'RQONE03999302',
        'lifecyclestate': 'Committed',
        'asilclassification': 'ASIL B'
    }
    
    child_fd_1 = [{
        'id': 'RQONE03999303',
        'asilclassification': 'ASIL C'
    }]
    
    rule1 = Rule_IssueSW_ASIL(issue_sw_1, child_fd_1)
    result1 = rule1.execute()
    print(f"Test 1: {result1.severity} - {result1.title}")
    print(f"  {result1.description}\n")
    
    # Test case 2: I-SW with ASIL D, I-FD with ASIL B (should fail)
    issue_sw_2 = {
        'id': 'RQONE03999304',
        'lifecyclestate': 'Committed',
        'asilclassification': 'ASIL D'
    }
    
    child_fd_2 = [{
        'id': 'RQONE03999305',
        'asilclassification': 'ASIL B'
    }]
    
    rule2 = Rule_IssueSW_ASIL(issue_sw_2, child_fd_2)
    result2 = rule2.execute()
    print(f"Test 2: {result2.severity} - {result2.title}")
    print(f"  {result2.description}\n")
    
    # Test case 3: I-SW with missing ASIL (should warn)
    issue_sw_3 = {
        'id': 'RQONE03999306',
        'lifecyclestate': 'Committed',
        'asilclassification': ''
    }
    
    rule3 = Rule_IssueSW_ASIL(issue_sw_3, [])
    result3 = rule3.execute()
    print(f"Test 3: {result3.severity} - {result3.title}")
    print(f"  {result3.description}")
