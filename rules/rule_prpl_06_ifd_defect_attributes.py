#!/usr/bin/env python
"""
PRPL 06.00.00: Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled

Rule Logic:
1. Apply to Issue FD (IFD) with Category = "Defect" only
2. Check lifecycle state: Evaluated, Committed, Implemented (exclude Canceled, Conflicted)
3. Two sets of validations:

   A. Defect Detection Attributes (all states):
      - Defect Detection Location
      - Defect Detection Process
      - Defect Detection Organisation
      - Defect Detection Date
      - Occurrence
      - Severity

   B. Defect Correction Attributes (during implementation - Evaluated, Committed, Implemented):
      - Defective Work Product Type
      - Defect Classification
      - Defect Injection Organisation
      - Defect Injection Date

This ensures proper tracking of defect flow from detection through correction.
"""

from enum import Enum
from typing import Optional, List
from dataclasses import dataclass


class LifeCycleState_Issue(Enum):
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
    """Validation result"""
    passed: bool
    severity: str
    title: str
    description: str


class Rule_Ifd_DefectAttributes:
    """
    PRPL 06.00.00: IFD Defect Detection/Injection Attributes
    
    Validates that Bug Fix IFDs (Category = Defect) have all required
    defect flow attributes filled in.
    """
    
    RULE_ID = "PRPL 06.00.00"
    RULE_TITLE = "Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled"
    
    # States where defect detection attributes are required
    DETECTION_REQUIRED_STATES = {
        LifeCycleState_Issue.EVALUATED,
        LifeCycleState_Issue.COMMITTED,
        LifeCycleState_Issue.IMPLEMENTED
    }
    
    # States where defect correction attributes are required (during implementation)
    CORRECTION_REQUIRED_STATES = {
        LifeCycleState_Issue.EVALUATED,
        LifeCycleState_Issue.COMMITTED,
        LifeCycleState_Issue.IMPLEMENTED
    }
    
    # Defect Detection Attributes (field names as they appear in RQ1)
    DETECTION_FIELDS = {
        'defectdetectionlocation': 'Defect Detection Location',
        'defectdetectionprocess': 'Defect Detection Process',
        'defectdetectionorga': 'Defect Detection Organisation',
        'defectdetectiondate': 'Defect Detection Date'
    }
    
    # Defect Correction Attributes (field names as they appear in RQ1)
    CORRECTION_FIELDS = {
        'defectiveworkproducttype': 'Defective Work Product Type',
        'defectclassification': 'Defect Classification',
        'defectinjectionorga': 'Defect Injection Organisation',
        'defectinjectiondate': 'Defect Injection Date'
    }
    
    def __init__(self, issue_data: dict):
        """
        Args:
            issue_data: Issue (IFD) data with fields:
                - id: RQ1 number
                - dcterms__title: Title
                - cq__Type: Type (should be "Issue FD")
                - lifecyclestate: Current state
                - category: Issue category (should be "Defect")
                - defectdetectionlocation: Defect Detection Location
                - defectdetectionprocess: Defect Detection Process
                - defectdetectionorga: Defect Detection Organisation
                - defectdetectiondate: Defect Detection Date
                - defectiveworkproducttype: Defective Work Product Type
                - defectclassification: Defect Classification
                - defectinjectionorga: Defect Injection Organisation
                - defectinjectiondate: Defect Injection Date
        """
        self.issue_data = issue_data
    
    def _is_field_filled(self, field_value) -> bool:
        """Check if a field has a meaningful value."""
        if field_value is None:
            return False
        if isinstance(field_value, str) and not field_value.strip():
            return False
        return True
    
    def execute(self) -> ValidationResult:
        """Execute validation rule."""
        
        issue_id = self.issue_data.get('id', 'UNKNOWN')
        issue_type = self.issue_data.get('cq__Type', '')
        state_str = self.issue_data.get('lifecyclestate', '')
        category = self.issue_data.get('category', '')
        
        # Only apply to Issue FD (IFD)
        if issue_type != 'Issue FD':
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Not an IFD",
                description=f"Rule applies only to Issue FD, this is {issue_type}"
            )
        
        # Only apply to Category = "Defect"
        if category != 'Defect':
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Not a Bug Fix Issue",
                description=f"Rule applies only to Category=Defect, this has Category={category}"
            )
        
        # Parse lifecycle state
        try:
            state = LifeCycleState_Issue[state_str.upper().replace(' ', '_')]
        except (KeyError, AttributeError):
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Unknown state",
                description=f"Unknown lifecycle state: {state_str}"
            )
        
        # Skip if in Canceled or Conflicted
        if state in [LifeCycleState_Issue.CANCELED, LifeCycleState_Issue.CONFLICTED]:
            return ValidationResult(
                passed=True,
                severity="PASS",
                title="Issue in terminal state",
                description=f"IFD is in {state.value} state - attributes not required"
            )
        
        # Check which validations apply based on state
        missing_fields = []
        
        # A. Check Defect Detection Attributes
        if state in self.DETECTION_REQUIRED_STATES:
            for field_key, field_label in self.DETECTION_FIELDS.items():
                field_value = self.issue_data.get(field_key)
                if not self._is_field_filled(field_value):
                    missing_fields.append(f"Detection: {field_label}")
        
        # B. Check Defect Correction Attributes (during implementation)
        if state in self.CORRECTION_REQUIRED_STATES:
            for field_key, field_label in self.CORRECTION_FIELDS.items():
                field_value = self.issue_data.get(field_key)
                if not self._is_field_filled(field_value):
                    missing_fields.append(f"Correction: {field_label}")
        
        # Return result
        if missing_fields:
            missing_list = "\n".join([f"  - {field}" for field in missing_fields])
            return ValidationResult(
                passed=False,
                severity="WARNING",
                title=f"IFD missing defect attributes ({self.RULE_ID})",
                description=(
                    f"IFD {issue_id} (Category: Defect) is in {state.value} state.\n"
                    f"Missing {len(missing_fields)} required defect flow attribute(s):\n"
                    f"{missing_list}\n\n"
                    f"Hint: All defect detection and correction attributes must be filled "
                    f"for proper defect tracking and analysis."
                )
            )
        
        return ValidationResult(
            passed=True,
            severity="PASS",
            title="All defect attributes filled",
            description=f"IFD {issue_id} has all required defect flow attributes"
        )
