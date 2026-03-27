"""
Analyze activated_rules.csv and map to existing Java implementations.

This script:
1. Reads activated_rules.csv
2. Checks which rules exist in POST tool (37 Java rules)
3. Identifies rules needing new Python implementation
4. Generates implementation priority report
"""

import csv
from pathlib import Path
from typing import Dict, List, Set
import re

# Known Java rules from POST tool (from previous parse_java_rules.py analysis)
JAVA_RULES = {
    # BC Planning (RuleExecutionGroup.BC_PLANNING)
    "Rule_Bc_Close": "QAM ID-2.5.0",  # BC closure after planned date
    "Rule_Bc_IfdClosed": "UNKNOWN",   # IFD closure before BC
    "Rule_CheckDatesForBcAndFc": "UNKNOWN",  # BC/FC date validation
    
    # FC Planning (RuleExecutionGroup.FC_PLANNING)
    "Rule_Fc_Close": "UNKNOWN",  # FC closure
    
    # I_SW Planning
    "Rule_IssueSW_ASIL": "BBM 13",  # ASIL classification
    "Rule_IssueSW_DevelopmentCategory": "UNKNOWN",
    
    # Element Integrity
    "Rule_Release_IfdReference": "UNKNOWN",
    "Rule_Release_IpsReference": "UNKNOWN",
    "Rule_ReleaseIssue_BcFcHasWp": "UNKNOWN",
    
    # Excited CPC
    "Rule_ExcitedCpc_Checks": "UNKNOWN",
    
    # Requirements Traceability
    "Rule_Traceability_Complete": "UNKNOWN",
    "Rule_Traceability_Consistent": "UNKNOWN",
    
    # BBM Metrics
    "Rule_CalculateBbmMetric15": "BBM 15",  # Requirements traceability
    "Rule_CalculateBbmMetric16": "BBM 16",  # Design traceability
    "Rule_CalculateBbmMetric17": "BBM 17",  # Verification traceability
    
    # And 20 more rules...
}

def parse_activated_rules(csv_path: Path) -> List[Dict[str, str]]:
    """Parse activated_rules.csv into structured data."""
    rules = []
    with open(csv_path, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            # Clean up rule ID (remove extra spaces)
            rule_id = row.get('Rule ID', '').strip()  # Changed from 'Rule Id'
            rules.append({
                'rule_id': rule_id,
                'description': row.get('Rule Desc', '').strip(),  # Changed from 'Rule Description'
                'activation_date': row.get('Activation Date', '').strip(),
                'category': rule_id.split()[0] if ' ' in rule_id else 'UNKNOWN'
            })
    return rules

def map_csv_to_java(csv_rules: List[Dict[str, str]]) -> Dict[str, List[str]]:
    """
    Map CSV rules to Java implementations.
    
    Returns:
        {
            'has_java': [list of rules with Java implementation],
            'needs_python': [list of rules needing new implementation],
            'unknown': [list of rules with unknown mapping]
        }
    """
    mapping = {
        'has_java': [],
        'needs_python': [],
        'unknown': []
    }
    
    # Simple keyword matching (will need manual verification)
    for rule in csv_rules:
        rule_id = rule['rule_id']
        desc_lower = rule['description'].lower()
        
        # Check for known patterns
        if 'bc' in desc_lower and 'close' in desc_lower:
            mapping['has_java'].append(f"{rule_id}: {rule['description']} -> Rule_Bc_Close")
        elif 'fc' in desc_lower and 'close' in desc_lower:
            mapping['has_java'].append(f"{rule_id}: {rule['description']} -> Rule_Fc_Close")
        elif 'asil' in desc_lower:
            mapping['has_java'].append(f"{rule_id}: {rule['description']} -> Rule_IssueSW_ASIL")
        elif 'bbm' in rule['category'].lower():
            # BBM metrics - check if we have Java BBM calculators
            if any(x in desc_lower for x in ['15', '16', '17', 'traceability']):
                mapping['has_java'].append(f"{rule_id}: {rule['description']} -> Rule_CalculateBbmMetric*")
            else:
                mapping['needs_python'].append(f"{rule_id}: {rule['description']}")
        else:
            mapping['unknown'].append(f"{rule_id}: {rule['description']}")
    
    return mapping

def generate_priority_report(csv_rules: List[Dict[str, str]], mapping: Dict[str, List[str]]):
    """Generate implementation priority report."""
    
    print("\n" + "="*80)
    print("ACTIVATED RULES ANALYSIS REPORT")
    print("="*80)
    
    # Count by category
    categories = {}
    for rule in csv_rules:
        cat = rule['category']
        categories[cat] = categories.get(cat, 0) + 1
    
    print(f"\n[STATS] TOTAL RULES: {len(csv_rules)}")
    print("\nBREAKDOWN BY CATEGORY:")
    for cat, count in sorted(categories.items(), key=lambda x: -x[1]):
        print(f"  {cat}: {count} rules")
    
    print(f"\n[MAPPING] TO JAVA IMPLEMENTATIONS:")
    print(f"  [OK] Has Java implementation: {len(mapping['has_java'])} rules")
    print(f"  [TODO] Needs new Python code: {len(mapping['needs_python'])} rules")
    print(f"  [?] Unknown mapping: {len(mapping['unknown'])} rules")
    
    print("\n" + "="*80)
    print("RULES WITH JAVA IMPLEMENTATIONS (Can reuse POST logic)")
    print("="*80)
    for rule_mapping in mapping['has_java']:
        print(f"  {rule_mapping}")
    
    print("\n" + "="*80)
    print("RULES NEEDING NEW PYTHON IMPLEMENTATION")
    print("="*80)
    for rule_desc in mapping['needs_python']:
        print(f"  {rule_desc}")
    
    print("\n" + "="*80)
    print("RULES WITH UNKNOWN MAPPING (Need manual analysis)")
    print("="*80)
    for rule_desc in mapping['unknown']:
        print(f"  {rule_desc}")
    
    print("\n" + "="*80)
    print("RECOMMENDED IMPLEMENTATION PHASES")
    print("="*80)
    
    print("\n[PHASE 1] (Quick Wins - Reuse Java logic):")
    print("   Implement rules with existing Java code in POST tool")
    print(f"   Estimated effort: {len(mapping['has_java'])} rules x 2-4 hours = {len(mapping['has_java'])*2}-{len(mapping['has_java'])*4} hours")
    
    print("\n[PHASE 2] (New Development):")
    print("   Implement rules without Java backing")
    print(f"   Estimated effort: {len(mapping['needs_python'])} rules x 4-8 hours = {len(mapping['needs_python'])*4}-{len(mapping['needs_python'])*8} hours")
    
    print("\n[PHASE 3] (Manual Analysis):")
    print("   Analyze unknown rules, map to Excel/VBA logic")
    print(f"   Estimated effort: {len(mapping['unknown'])} rules x 6-12 hours = {len(mapping['unknown'])*6}-{len(mapping['unknown'])*12} hours")
    
    total_min = len(mapping['has_java'])*2 + len(mapping['needs_python'])*4 + len(mapping['unknown'])*6
    total_max = len(mapping['has_java'])*4 + len(mapping['needs_python'])*8 + len(mapping['unknown'])*12
    print(f"\n[TOTAL] ESTIMATED EFFORT: {total_min}-{total_max} hours ({total_min//8}-{total_max//8} days)")

def main():
    project_root = Path(__file__).parent.parent
    csv_path = project_root / "rq1" / "activated_rules.csv"
    
    if not csv_path.exists():
        print(f"[ERROR] {csv_path} not found!")
        return
    
    # Parse CSV
    rules = parse_activated_rules(csv_path)
    print(f"[OK] Parsed {len(rules)} rules from {csv_path.name}")
    
    # Map to Java implementations
    mapping = map_csv_to_java(rules)
    
    # Generate report
    generate_priority_report(rules, mapping)
    
    # Save detailed mapping to file
    output_path = project_root / "docs" / "ACTIVATED_RULES_MAPPING.md"
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write("# Activated Rules Mapping\n\n")
        f.write("Mapping of 32 activated rules from CSV to Java/Python implementations.\n\n")
        
        f.write("## Rules with Java Implementations\n\n")
        for rule_mapping in mapping['has_java']:
            f.write(f"- {rule_mapping}\n")
        
        f.write("\n## Rules Needing New Python Implementation\n\n")
        for rule_desc in mapping['needs_python']:
            f.write(f"- {rule_desc}\n")
        
        f.write("\n## Rules with Unknown Mapping\n\n")
        for rule_desc in mapping['unknown']:
            f.write(f"- {rule_desc}\n")
    
    print(f"\n[SAVED] Detailed mapping saved to: {output_path}")

if __name__ == "__main__":
    main()
