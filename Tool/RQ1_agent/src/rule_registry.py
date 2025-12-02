#!/usr/bin/env python
"""
Rule Registry System

Central registry for all validation rules, allowing execution by Rule ID.

Usage:
    registry = RuleRegistry()
    result = registry.execute_rule("PRPL 15.00.00", bc_data)
"""

from typing import Dict, Optional, Any, Callable
from dataclasses import dataclass
from enum import Enum


class RuleCategory(Enum):
    """Rule categories from activated_rules.csv"""
    PRPL = "PRPL"  # Planning/Process rules
    QAWP = "QAWP"  # Quality Assurance Work Products
    BBM = "BBM"    # Building Block Manual metrics
    QADO = "QADO"  # Quality Assessment/Documentation
    PS_SC = "PS-SC"  # Product Standard - Software Components


@dataclass
class RuleMetadata:
    """Metadata for a validation rule"""
    rule_id: str
    category: RuleCategory
    description: str
    activation_date: str
    java_class: Optional[str]  # Java class from POST tool (if exists)
    python_module: str  # Python module path
    python_class: str  # Python class name
    execution_group: Optional[str]  # From Java (if exists)
    requires_relationships: bool  # Does rule need to query relationships?


class RuleRegistry:
    """
    Central registry for all validation rules.
    
    Maps Rule IDs (e.g. "PRPL 15.00.00") to Python implementations.
    """
    
    def __init__(self):
        self._rules: Dict[str, RuleMetadata] = {}
        self._load_rules()
    
    def _load_rules(self):
        """Load all registered rules."""
        
        # PRPL 15.00.00: Release not closed after planned date
        self._rules["PRPL 15.00.00"] = RuleMetadata(
            rule_id="PRPL 15.00.00",
            category=RuleCategory.PRPL,
            description="Release is not closed after planned date for BC and FC",
            activation_date="1-Jan-16",
            java_class="Rule_CheckDatesForBcAndFc",
            python_module="src.rules.rule_bc_close",
            python_class="Rule_Bc_Close",
            execution_group="ELEMENT_INTEGRITY",
            requires_relationships=False
        )
        
        # PRPL 12.00.00: IFD not closed when all BCs closed
        self._rules["PRPL 12.00.00"] = RuleMetadata(
            rule_id="PRPL 12.00.00",
            category=RuleCategory.PRPL,
            description="IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled",
            activation_date="1-Mar-16",
            java_class=None,  # No direct Java equivalent
            python_module="src.rules.rule_prpl_12_ifd_bc_closure",
            python_class="Rule_IFD_BcClosure",
            execution_group=None,
            requires_relationships=True  # Needs to fetch mapped BCs
        )
        
        # PRPL 07.00.00: BC planned date vs PVER/PVAR delivery dates
        self._rules["PRPL 07.00.00"] = RuleMetadata(
            rule_id="PRPL 07.00.00",
            category=RuleCategory.PRPL,
            description="Planned date of BC later than requested delivery date of any mapped PVER or PVAR",
            activation_date="24-Jun-16",
            java_class="Rule_Bc_CheckPstDates",
            python_module="src.rules.rule_prpl_07_bc_pst_dates",
            python_class="Rule_Bc_CheckPstDates",
            execution_group="EXCITED_CPC",
            requires_relationships=True  # Needs to fetch PST mappings
        )
        
        # PRPL 13.00.00: IFD not implemented after BC planned date
        self._rules["PRPL 13.00.00"] = RuleMetadata(
            rule_id="PRPL 13.00.00",
            category=RuleCategory.PRPL,
            description="IFD is not implemented or closed, after planned dated of BC-R",
            activation_date="1-Jul-16",
            java_class="Rule_IssueFD_WithoutLinkToBc",
            python_module="src.rules.rule_prpl_13_ifd_bc_planned",
            python_class="Rule_IFD_BcPlannedDate",
            execution_group="UI_VISIBLE",
            requires_relationships=True  # Needs to fetch BC mappings
        )
        
        # PRPL 16.00.00: Workitem not closed after planned date
        self._rules["PRPL 16.00.00"] = RuleMetadata(
            rule_id="PRPL 16.00.00",
            category=RuleCategory.PRPL,
            description="Workitem is not closed after planned date",
            activation_date="1-Jul-16",
            java_class="Rule_Bc_CheckPstDates",
            python_module="src.rules.rule_prpl_16_workitem_close",
            python_class="Rule_Workitem_Close",
            execution_group="EXCITED_CPC",
            requires_relationships=False
        )
        
        # PRPL 02.00.00: Workitem started without planned date
        self._rules["PRPL 02.00.00"] = RuleMetadata(
            rule_id="PRPL 02.00.00",
            category=RuleCategory.PRPL,
            description="Workitem is in started state, but planned date for workitem is not entered in planning tab",
            activation_date="1-Jan-16",
            java_class="Rule_Bc_CheckPstDates",
            python_module="src.rules.rule_prpl_02_workitem_planned",
            python_class="Rule_Workitem_PlannedDate",
            execution_group="EXCITED_CPC",
            requires_relationships=False
        )
        
        # TODO: Add remaining 26 rules as they are implemented
    
    def get_rule(self, rule_id: str) -> Optional[RuleMetadata]:
        """Get rule metadata by Rule ID."""
        return self._rules.get(rule_id)
    
    def list_rules(self, category: Optional[RuleCategory] = None) -> list[RuleMetadata]:
        """
        List all registered rules.
        
        Args:
            category: Optional filter by category
        
        Returns:
            List of RuleMetadata
        """
        rules = list(self._rules.values())
        
        if category:
            rules = [r for r in rules if r.category == category]
        
        return sorted(rules, key=lambda r: r.rule_id)
    
    def execute_rule(self, rule_id: str, *args, **kwargs):
        """
        Execute a rule by its Rule ID.
        
        Args:
            rule_id: Rule ID (e.g. "PRPL 15.00.00")
            *args, **kwargs: Arguments to pass to rule constructor
        
        Returns:
            ValidationResult from rule execution
        
        Raises:
            ValueError: If rule ID not found
            ImportError: If rule module cannot be imported
        """
        rule_meta = self.get_rule(rule_id)
        
        if not rule_meta:
            raise ValueError(
                f"Rule '{rule_id}' not found in registry. "
                f"Available rules: {list(self._rules.keys())}"
            )
        
        # Dynamically import rule module
        try:
            import importlib
            module = importlib.import_module(rule_meta.python_module)
            rule_class = getattr(module, rule_meta.python_class)
        except (ImportError, AttributeError) as e:
            raise ImportError(
                f"Failed to import rule '{rule_id}' from "
                f"{rule_meta.python_module}.{rule_meta.python_class}: {e}"
            )
        
        # Instantiate and execute rule
        rule_instance = rule_class(*args, **kwargs)
        return rule_instance.execute()
    
    def get_rules_by_category(self, category: RuleCategory) -> list[str]:
        """Get list of Rule IDs for a specific category."""
        return [
            rule_id for rule_id, meta in self._rules.items()
            if meta.category == category
        ]
    
    def get_implementation_status(self) -> dict:
        """Get implementation status summary."""
        total = len(self._rules)
        by_category = {}
        
        for meta in self._rules.values():
            cat = meta.category.value
            by_category[cat] = by_category.get(cat, 0) + 1
        
        return {
            'total_implemented': total,
            'total_activated': 32,  # From activated_rules.csv
            'by_category': by_category,
            'completion_percentage': (total / 32) * 100
        }


# Global registry instance
_registry = None


def get_registry() -> RuleRegistry:
    """Get global rule registry instance."""
    global _registry
    if _registry is None:
        _registry = RuleRegistry()
    return _registry
