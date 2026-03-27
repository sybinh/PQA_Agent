#!/usr/bin/env python3
"""
Rule Mapping System: Map Java rules to Excel rules by category
Combines:
- Java rule files (37 rules)
- Excel rules (101 rules from BBM/QAM/IPT)
- Manual mapping based on functionality
"""
from pathlib import Path
from typing import Dict, List, Optional
from dataclasses import dataclass, field
import json

@dataclass
class RuleMapping:
    """Complete rule mapping"""
    # Identifiers
    java_class: str
    rule_id: Optional[str] = None  # From Java: "BC_04__CLOSE"
    
    # Excel references
    qam_id: Optional[str] = None   # "QAM ID-2.5.0"
    bbm_id: Optional[str] = None   # "BBM 10"
    ipt_id: Optional[str] = None   # "PRPL 10.00.00"
    
    # Metadata
    title: Optional[str] = None
    description: Optional[str] = None
    category: str = "UNKNOWN"  # QAM, BBM, IPT
    priority: str = "MEDIUM"   # CRITICAL, HIGH, MEDIUM, LOW
    
    # Implementation status
    has_java: bool = True
    has_excel: bool = False
    has_python: bool = False
    
    # Functional grouping
    functional_group: Optional[str] = None  # e.g., "Date Validation", "Naming Convention"
    
    def to_dict(self) -> Dict:
        return {
            "java_class": self.java_class,
            "rule_id": self.rule_id,
            "qam_id": self.qam_id,
            "bbm_id": self.bbm_id,
            "ipt_id": self.ipt_id,
            "title": self.title,
            "description": self.description,
            "category": self.category,
            "priority": self.priority,
            "has_java": self.has_java,
            "has_excel": self.has_excel,
            "has_python": self.has_python,
            "functional_group": self.functional_group
        }

# COMPREHENSIVE MAPPING based on:
# 1. Java rule analysis
# 2. Excel rules from BBM/QAM/IPT
# 3. EXCEL_TO_JAVA_MAPPING.md
# 4. Rule naming patterns and functionality

RULE_MAPPINGS = [
    # === QAM RULES (Quality Assurance Manual) ===
    
    RuleMapping(
        java_class="Rule_Bc_Close",
        rule_id="BC_04__CLOSE",
        qam_id="QAM ID-2.5.0",
        title="Release is not closed after planned date for BC",
        description="BC must be closed after its planned date",
        category="QAM",
        priority="CRITICAL",
        has_excel=True,
        functional_group="Release Closure"
    ),
    
    RuleMapping(
        java_class="Rule_CheckDatesForBcAndFc",
        rule_id="CHECK_DATES_BC_FC",
        qam_id="QAM ID-2.5.0",  # Part of same QAM rule
        ipt_id="PRPL 10.00.00",
        title="Check dates for BC and FC",
        description="Validate BC planned date vs FC requested date",
        category="QAM",
        priority="CRITICAL",
        has_excel=True,
        functional_group="Date Validation"
    ),
    
    # === BBM RULES (Building Block Manual) ===
    
    RuleMapping(
        java_class="Rule_CheckForMissing_BaselineLink",
        rule_id="MISSING_BASELINE_LINK",
        bbm_id="BBM 10",  # Traceability requirement
        title="Check for missing baseline link",
        description="Ensure proper baseline linkage",
        category="BBM",
        priority="HIGH",
        has_excel=True,
        functional_group="Traceability"
    ),
    
    RuleMapping(
        java_class="Rule_IssueSW_FmeaCheck",
        rule_id="I_SW_11__FMEA",
        bbm_id="BBM 12",  # FMEA requirement
        title="FMEA check for Issue-SW",
        description="Validate FMEA classification",
        category="BBM",
        priority="HIGH",
        has_excel=True,
        functional_group="Safety & Quality"
    ),
    
    RuleMapping(
        java_class="Rule_IssueSW_ASIL",
        rule_id="I_SW_10__ASIL",
        bbm_id="BBM 13",  # ASIL requirement
        title="ASIL classification check",
        description="Validate ASIL level consistency",
        category="BBM",
        priority="CRITICAL",
        has_excel=True,
        has_python=True,  # We already implemented this
        functional_group="Safety & Quality"
    ),
    
    RuleMapping(
        java_class="Rule_IssueSW_MissingAffectedIssueComment",
        rule_id="I_SW_AFFECTED_COMMENT",
        bbm_id="BBM 15",  # Documentation requirement
        title="Missing affected issue comment",
        description="Check for required comments on affected issues",
        category="BBM",
        priority="MEDIUM",
        has_excel=True,
        functional_group="Documentation"
    ),
    
    # === IPT RULES (Integration & Planning Tool) ===
    
    RuleMapping(
        java_class="Rule_Bc_NamingConvention",
        rule_id="BC_NAMING",
        ipt_id="PRPL 20.00.00",
        title="BC naming convention check",
        description="Validate BC name format: BC[0-9]{5}[A-Z]{2}",
        category="IPT",
        priority="HIGH",
        has_excel=True,
        functional_group="Naming Convention"
    ),
    
    RuleMapping(
        java_class="Rule_Fc_NamingConvention",
        rule_id="FC_NAMING",
        ipt_id="PRPL 19.00.00",
        title="FC naming convention check",
        description="Validate FC name format: FC[0-9]{5}[A-Z]{2}",
        category="IPT",
        priority="HIGH",
        has_excel=True,
        functional_group="Naming Convention"
    ),
    
    RuleMapping(
        java_class="Rule_Bc_CheckPstDates",
        rule_id="BC_PST_DATES",
        ipt_id="PRPL 15.00.00",
        title="Check PST dates for BC",
        description="Validate PST date consistency",
        category="IPT",
        priority="MEDIUM",
        has_excel=True,
        functional_group="Date Validation"
    ),
    
    RuleMapping(
        java_class="Rule_Bc_WithoutLinkToPst",
        rule_id="BC_WITHOUT_PST",
        ipt_id="PRPL 18.00.00",
        title="BC without link to PST",
        description="BC must have PST link",
        category="IPT",
        priority="HIGH",
        has_excel=True,
        functional_group="Traceability"
    ),
    
    RuleMapping(
        java_class="Rule_IssueFD_WithoutLinkToBc",
        rule_id="I_FD_WITHOUT_BC",
        ipt_id="QADO 01.00.00",
        title="Issue-FD without link to BC",
        description="Issue-FD must be linked to BC when FC is delivered",
        category="IPT",
        priority="HIGH",
        has_excel=True,
        functional_group="Traceability"
    ),
    
    RuleMapping(
        java_class="Rule_Fc_WithoutLinkToBc",
        rule_id="FC_WITHOUT_BC",
        ipt_id="PRPL 17.00.00",
        title="FC without link to BC",
        description="FC must be linked to BC",
        category="IPT",
        priority="HIGH",
        has_excel=True,
        functional_group="Traceability"
    ),
    
    RuleMapping(
        java_class="Rule_Fc_PlannedDate",
        rule_id="FC_PLANNED_DATE",
        ipt_id="PRPL 08.00.00",
        title="FC planned date check",
        description="Validate FC planned date",
        category="IPT",
        priority="MEDIUM",
        has_excel=True,
        functional_group="Date Validation"
    ),
    
    RuleMapping(
        java_class="Rule_Fc_ReqDate",
        rule_id="FC_REQ_DATE",
        ipt_id="PRPL 07.00.00",
        title="FC requested date check",
        description="Validate FC requested date",
        category="IPT",
        priority="MEDIUM",
        has_excel=True,
        functional_group="Date Validation"
    ),
    
    RuleMapping(
        java_class="Rule_CheckDatesForPver",
        rule_id="CHECK_DATES_PVER",
        ipt_id="PRPL 10.00.00",
        title="Check dates for PVER",
        description="Validate PVER date consistency",
        category="IPT",
        priority="MEDIUM",
        has_excel=True,
        has_python=True,  # Partially implemented
        functional_group="Date Validation"
    ),
    
    RuleMapping(
        java_class="Rule_CheckDatesForPvar",
        rule_id="CHECK_DATES_PVAR",
        ipt_id="PRPL 10.00.00",
        title="Check dates for PVAR",
        description="Validate PVAR date consistency",
        category="IPT",
        priority="MEDIUM",
        has_excel=True,
        functional_group="Date Validation"
    ),
    
    # === Rules with unclear/no Excel mapping ===
    
    RuleMapping(
        java_class="Rule_Release_Predecessor",
        rule_id="RELEASE_PREDECESSOR",
        title="Check release predecessor",
        description="Validate release predecessor relationships",
        category="UNKNOWN",
        priority="LOW",
        functional_group="Release Management"
    ),
    
    RuleMapping(
        java_class="Rule_AccountNumberFormat",
        rule_id="ACCOUNT_NUMBER_FORMAT",
        title="Account number format check",
        description="Validate account number format",
        category="UNKNOWN",
        priority="LOW",
        functional_group="Data Quality"
    ),
    
    RuleMapping(
        java_class="Rule_IssueFD_Pilot",
        rule_id="I_FD_PILOT",
        title="Issue-FD pilot check",
        description="Validate pilot configuration for Issue-FD",
        category="UNKNOWN",
        priority="LOW",
        functional_group="Pilot Management"
    ),
    
    # RRM Rules (Release & Risk Management)
    RuleMapping(
        java_class="Rule_Rrm_Bc_Fc_DeliveryDate",
        rule_id="RRM_BC_FC_DELIVERY",
        title="RRM BC/FC delivery date check",
        description="Validate delivery dates for BC and FC",
        category="UNKNOWN",
        priority="MEDIUM",
        functional_group="Release Management"
    ),
    
    RuleMapping(
        java_class="Rule_Rrm_Pst_Bc_DeliveryDate",
        rule_id="RRM_PST_BC_DELIVERY",
        title="RRM PST/BC delivery date check",
        description="Validate PST and BC delivery dates",
        category="UNKNOWN",
        priority="MEDIUM",
        functional_group="Release Management"
    ),
    
    # IRM Rules (Issue & Risk Management)
    RuleMapping(
        java_class="Rule_Irm_PstRelease_IssueSw_Severity",
        rule_id="IRM_SEVERITY",
        title="IRM severity check",
        description="Validate issue severity for PST releases",
        category="UNKNOWN",
        priority="MEDIUM",
        functional_group="Issue Management"
    ),
    
    # Additional rules...
    RuleMapping(
        java_class="Rule_Ecu_LumpensammlerExists",
        rule_id="ECU_LUMPENSAMMLER",
        title="ECU Lumpensammler check",
        description="Check for ECU Lumpensammler existence",
        category="UNKNOWN",
        priority="LOW",
        functional_group="ECU Management"
    ),
    
    RuleMapping(
        java_class="Rule_Info_InternalComment",
        rule_id="INFO_COMMENT",
        title="Internal comment check",
        description="Validate internal comments",
        category="UNKNOWN",
        priority="LOW",
        functional_group="Documentation"
    ),
    
    RuleMapping(
        java_class="Rule_ToDo_InternalComment",
        rule_id="TODO_COMMENT",
        title="TODO internal comment check",
        description="Check for TODO comments",
        category="UNKNOWN",
        priority="LOW",
        functional_group="Documentation"
    ),
]

class RuleMappingRegistry:
    """Registry for rule mappings"""
    
    def __init__(self, mappings: List[RuleMapping] = None):
        self.mappings = mappings or RULE_MAPPINGS
    
    def get_by_category(self, category: str) -> List[RuleMapping]:
        """Get rules by category"""
        return [m for m in self.mappings if m.category == category]
    
    def get_by_functional_group(self, group: str) -> List[RuleMapping]:
        """Get rules by functional group"""
        return [m for m in self.mappings if m.functional_group == group]
    
    def get_by_priority(self, priority: str) -> List[RuleMapping]:
        """Get rules by priority"""
        return [m for m in self.mappings if m.priority == priority]
    
    def get_with_excel_backing(self) -> List[RuleMapping]:
        """Get rules that have Excel documentation"""
        return [m for m in self.mappings if m.has_excel]
    
    def get_implemented_in_python(self) -> List[RuleMapping]:
        """Get rules already implemented in Python"""
        return [m for m in self.mappings if m.has_python]
    
    def export_to_json(self, output_file: str = "docs/rule_mappings.json"):
        """Export mappings to JSON"""
        data = {
            "total_rules": len(self.mappings),
            "by_category": {
                "QAM": len(self.get_by_category("QAM")),
                "BBM": len(self.get_by_category("BBM")),
                "IPT": len(self.get_by_category("IPT")),
                "UNKNOWN": len(self.get_by_category("UNKNOWN"))
            },
            "by_priority": {
                "CRITICAL": len(self.get_by_priority("CRITICAL")),
                "HIGH": len(self.get_by_priority("HIGH")),
                "MEDIUM": len(self.get_by_priority("MEDIUM")),
                "LOW": len(self.get_by_priority("LOW"))
            },
            "with_excel_backing": len(self.get_with_excel_backing()),
            "implemented_in_python": len(self.get_implemented_in_python()),
            "mappings": [m.to_dict() for m in self.mappings]
        }
        
        output_path = Path(output_file)
        output_path.write_text(json.dumps(data, indent=2), encoding='utf-8')
        print(f"? Exported {len(self.mappings)} rule mappings to {output_file}")
        
        return data
    
    def print_summary(self):
        """Print comprehensive summary"""
        print("\n" + "="*80)
        print("RULE MAPPING REGISTRY")
        print("="*80)
        
        print(f"\nTotal Mapped Rules: {len(self.mappings)}")
        print(f"  With Excel Backing: {len(self.get_with_excel_backing())}")
        print(f"  Already in Python: {len(self.get_implemented_in_python())}")
        
        print("\nBy Category:")
        for category in ["QAM", "BBM", "IPT", "UNKNOWN"]:
            rules = self.get_by_category(category)
            print(f"  {category:10s}: {len(rules):2d} rules")
        
        print("\nBy Priority:")
        for priority in ["CRITICAL", "HIGH", "MEDIUM", "LOW"]:
            rules = self.get_by_priority(priority)
            print(f"  {priority:10s}: {len(rules):2d} rules")
        
        print("\nBy Functional Group:")
        groups = set(m.functional_group for m in self.mappings if m.functional_group)
        for group in sorted(groups):
            rules = self.get_by_functional_group(group)
            print(f"  {group:25s}: {len(rules):2d} rules")
        
        # Show QAM rules
        print("\n" + "-"*80)
        print("QAM RULES (Quality Assurance Manual)")
        print("-"*80)
        for rule in self.get_by_category("QAM"):
            excel_mark = "??" if rule.has_excel else "  "
            python_mark = "??" if rule.has_python else "  "
            print(f"{excel_mark}{python_mark} {rule.java_class:40s} {rule.qam_id}")
            print(f"      ?? {rule.title}")
        
        # Show BBM rules
        print("\n" + "-"*80)
        print("BBM RULES (Building Block Manual)")
        print("-"*80)
        for rule in self.get_by_category("BBM"):
            excel_mark = "??" if rule.has_excel else "  "
            python_mark = "??" if rule.has_python else "  "
            print(f"{excel_mark}{python_mark} {rule.java_class:40s} {rule.bbm_id}")
            print(f"      ?? {rule.title}")
        
        # Show IPT rules
        print("\n" + "-"*80)
        print("IPT RULES (Integration & Planning Tool)")
        print("-"*80)
        for rule in self.get_by_category("IPT"):
            excel_mark = "??" if rule.has_excel else "  "
            python_mark = "??" if rule.has_python else "  "
            print(f"{excel_mark}{python_mark} {rule.java_class:40s} {rule.ipt_id}")
            print(f"      ?? {rule.title}")

def main():
    """Main function"""
    print("=== Rule Mapping System ===")
    
    registry = RuleMappingRegistry()
    
    # Print summary
    registry.print_summary()
    
    # Export to JSON
    print("\n" + "="*80)
    registry.export_to_json()
    
    print("\n?? = Has Excel documentation")
    print("?? = Already implemented in Python")

if __name__ == "__main__":
    main()