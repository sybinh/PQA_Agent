#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
PRPL 03: Conflicted State Check
================================
Rule: Issue/Release/workitem is still in "Conflicted" state

Validation Logic:
- Check if item lifecyclestate == "Conflicted"
- If yes, violation

Reference: activated_rules.csv - PRPL 03.00.00, Activated 1-Jul-16
"""

from dataclasses import dataclass


@dataclass
class RuleResult:
    """Rule validation result"""
    rule_id: str
    passed: bool
    severity: str
    description: str


class Rule_Conflicted_State:
    """
    PRPL 03: Items must not remain in Conflicted state
    """
    
    def __init__(self, item_data: dict, item_type: str):
        """
        Args:
            item_data: Item data with keys: id, dcterms__title, lifecyclestate
            item_type: Type of item ("Issue", "Release", "Workitem")
        """
        self.item = item_data
        self.item_type = item_type
    
    def execute(self) -> RuleResult:
        """Execute the rule validation"""
        
        item_id = self.item.get('id', 'UNKNOWN')
        item_title = self.item.get('dcterms__title', '')
        item_state = self.item.get('lifecyclestate', '')
        
        # Check if in Conflicted state
        if item_state == 'Conflicted':
            description = (
                f"{self.item_type} {item_id} is in 'Conflicted' state.\n"
                f"Title: {item_title}\n"
                f"\n"
                f"Hint: This item is in Conflicted state (used during clarification).\n"
                f"Please resolve when clarification is complete."
            )
            
            return RuleResult(
                rule_id="PRPL 03",
                passed=False,
                severity="INFO",
                description=description
            )
        
        # Not in Conflicted state - pass
        return RuleResult(
            rule_id="PRPL 03",
            passed=True,
            severity="INFO",
            description=f"{self.item_type} {item_id} is not in Conflicted state"
        )


# Example usage for testing
if __name__ == "__main__":
    # Test case: Item in Conflicted state (should fail)
    item_data = {
        'id': 'RQONE12345',
        'dcterms__title': 'Test Item with Conflict',
        'lifecyclestate': 'Conflicted'
    }
    
    rule = Rule_Conflicted_State(item_data, "Issue")
    result = rule.execute()
    
    print(f"Rule: {result.rule_id}")
    print(f"Passed: {result.passed}")
    print(f"Severity: {result.severity}")
    print(f"Description:\n{result.description}")
