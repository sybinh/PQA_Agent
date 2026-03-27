#!/usr/bin/env python3
"""
Excel Rule Change Detector
Detects changes between Excel versions and analyzes impact on PQA_Agent
"""

import pandas as pd
import argparse
from pathlib import Path
from datetime import datetime
from typing import Dict, List, Set, Tuple


# Mapping Excel rules to Java implementation
EXCEL_TO_JAVA_MAPPING = {
    'QADO 01.00.00': ['Rule_IssueFD_WithoutLinkToBc', 'Rule_Fc_Close', 'Rule_Bc_Close'],
    'QADO 02.00.00': ['Rule_IssueSW_FmeaCheck'],
    'QADO 02.01.00': [],  # No Java implementation
    'QADO 03.00.00': [],  # Different tool
    'QADO 04.00.00': [],  # Not PQA relevant
    'BBM 01': [],  # Metrics only
    'BBM 03': [],  # Metrics only
    'BBM 04': [],  # Metrics only
    'BBM 06': [],  # Metrics only
    'BBM 07': ['Rule_IssueSW_ASIL'],  # Safety
    'BBM 08': ['Rule_Bc_Close'],  # Defect closure
    'BBM 09': ['Rule_Fc_Close'],  # CR closure
    'BBM 10': ['Rule_CheckForMissing_BaselineLink'],  # Traceability
    'BBM 11': [],  # Metrics only
    'BBM 12': ['Rule_CheckForMissing_BaselineLink'],  # Traceability
    'BBM 13': [],  # Metrics only
    'BBM 15': [],  # Metrics only
    'BBM 16': ['Rule_CheckForMissing_BaselineLink'],  # Traceability
    'BBM 17': [],  # Metrics only
    'BBM 23': [],  # Static analysis
}

PQA_RELEVANT_RULES = {
    'QADO 01.00.00', 'QADO 02.00.00', 'QADO 02.01.00', 'QADO 03.00.00',
    'BBM 07', 'BBM 08', 'BBM 09', 'BBM 10', 'BBM 12', 'BBM 16'
}


def parse_bbm_rules_simple(excel_file: str) -> List[Dict]:
    """Quick parse of BBM rules for comparison"""
    try:
        df = pd.read_excel(excel_file, sheet_name='BBM Rule Set', engine='openpyxl')
    except Exception as e:
        print(f"Error reading BBM sheet: {e}")
        return []
    
    rules = []
    for idx, row in df.iterrows():
        first_col = row.iloc[3] if pd.notna(row.iloc[3]) else None
        if first_col and isinstance(first_col, str) and first_col.startswith('BBM '):
            rules.append({
                'id': first_col.strip(),
                'name': row.iloc[4] if pd.notna(row.iloc[4]) else "",
                'execution': row.iloc[9] if pd.notna(row.iloc[9]) else "",
                'sheet': 'BBM Rule Set'
            })
    return rules


def parse_qam_rules_simple(excel_file: str) -> List[Dict]:
    """Quick parse of QAM rules for comparison"""
    try:
        df = pd.read_excel(excel_file, sheet_name='QAM Rule Set (FKT)', engine='openpyxl')
    except Exception as e:
        print(f"Error reading QAM sheet: {e}")
        return []
    
    rules = []
    for idx, row in df.iterrows():
        first_col = row.iloc[3] if pd.notna(row.iloc[3]) else None
        if first_col and isinstance(first_col, str) and first_col.startswith('QADO '):
            rules.append({
                'id': first_col.strip(),
                'name': row.iloc[4] if pd.notna(row.iloc[4]) else "",
                'execution': row.iloc[9] if pd.notna(row.iloc[9]) else "",
                'relevant_pqa': row.iloc[12] if pd.notna(row.iloc[12]) else "",
                'sheet': 'QAM Rule Set'
            })
    return rules


def get_all_rules(excel_file: str) -> List[Dict]:
    """Get all rules from Excel"""
    bbm = parse_bbm_rules_simple(excel_file)
    qam = parse_qam_rules_simple(excel_file)
    return bbm + qam


def compare_rules(old_rules: List[Dict], new_rules: List[Dict]) -> Dict:
    """Compare two sets of rules"""
    old_dict = {r['id']: r for r in old_rules}
    new_dict = {r['id']: r for r in new_rules}
    
    old_ids = set(old_dict.keys())
    new_ids = set(new_dict.keys())
    
    new_rules_list = new_ids - old_ids
    deleted_rules_list = old_ids - new_ids
    common_ids = old_ids & new_ids
    
    modified_rules = []
    for rule_id in common_ids:
        old_rule = old_dict[rule_id]
        new_rule = new_dict[rule_id]
        
        changes = []
        if old_rule['name'] != new_rule['name']:
            changes.append(f"Name: '{old_rule['name']}' ? '{new_rule['name']}'")
        if old_rule.get('execution') != new_rule.get('execution'):
            changes.append(f"Execution: '{old_rule.get('execution')}' ? '{new_rule.get('execution')}'")
        if old_rule.get('relevant_pqa') != new_rule.get('relevant_pqa'):
            changes.append(f"PQA Relevance: '{old_rule.get('relevant_pqa')}' ? '{new_rule.get('relevant_pqa')}'")
        
        if changes:
            modified_rules.append({
                'id': rule_id,
                'changes': changes,
                'old': old_rule,
                'new': new_rule
            })
    
    return {
        'new': [new_dict[rid] for rid in new_rules_list],
        'deleted': [old_dict[rid] for rid in deleted_rules_list],
        'modified': modified_rules
    }


def analyze_impact(changes: Dict) -> Dict:
    """Analyze impact on PQA_Agent"""
    high_impact = []
    medium_impact = []
    low_impact = []
    
    # New rules
    for rule in changes['new']:
        rule_id = rule['id']
        if rule_id in EXCEL_TO_JAVA_MAPPING and EXCEL_TO_JAVA_MAPPING[rule_id]:
            high_impact.append({
                'type': 'NEW',
                'rule': rule,
                'reason': f'New rule with Java mapping: {EXCEL_TO_JAVA_MAPPING[rule_id]}',
                'action': '?? Need to implement new Java rules'
            })
        elif rule_id in PQA_RELEVANT_RULES or rule.get('relevant_pqa') == 'yes':
            medium_impact.append({
                'type': 'NEW',
                'rule': rule,
                'reason': 'New PQA-relevant rule but no Java implementation yet',
                'action': '?? Add to backlog for future implementation'
            })
        else:
            low_impact.append({
                'type': 'NEW',
                'rule': rule,
                'reason': 'New rule not PQA-relevant',
                'action': '? No action needed'
            })
    
    # Deleted rules
    for rule in changes['deleted']:
        rule_id = rule['id']
        if rule_id in EXCEL_TO_JAVA_MAPPING and EXCEL_TO_JAVA_MAPPING[rule_id]:
            high_impact.append({
                'type': 'DELETED',
                'rule': rule,
                'reason': f'Deleted rule was implemented: {EXCEL_TO_JAVA_MAPPING[rule_id]}',
                'action': '??? Need to remove or deprecate implementation'
            })
        elif rule_id in PQA_RELEVANT_RULES:
            medium_impact.append({
                'type': 'DELETED',
                'rule': rule,
                'reason': 'Deleted PQA-relevant rule',
                'action': '?? Review if removal is intentional'
            })
        else:
            low_impact.append({
                'type': 'DELETED',
                'rule': rule,
                'reason': 'Deleted non-PQA rule',
                'action': '? No action needed'
            })
    
    # Modified rules
    for mod in changes['modified']:
        rule_id = mod['id']
        if rule_id in EXCEL_TO_JAVA_MAPPING and EXCEL_TO_JAVA_MAPPING[rule_id]:
            high_impact.append({
                'type': 'MODIFIED',
                'rule': mod,
                'reason': f'Modified implemented rule: {EXCEL_TO_JAVA_MAPPING[rule_id]}',
                'action': '?? Need to review and update implementation'
            })
        elif rule_id in PQA_RELEVANT_RULES:
            medium_impact.append({
                'type': 'MODIFIED',
                'rule': mod,
                'reason': 'Modified PQA-relevant rule',
                'action': '?? Review changes for future implementation'
            })
        else:
            low_impact.append({
                'type': 'MODIFIED',
                'rule': mod,
                'reason': 'Modified non-PQA rule',
                'action': '? No action needed'
            })
    
    return {
        'high': high_impact,
        'medium': medium_impact,
        'low': low_impact
    }


def create_impact_report(old_file: str, new_file: str, output_file: str):
    """Create detailed impact report"""
    print(f"\n?? Analyzing Excel changes...")
    print(f"   Old: {old_file}")
    print(f"   New: {new_file}\n")
    
    # Parse both files
    old_rules = get_all_rules(old_file)
    new_rules = get_all_rules(new_file)
    
    print(f"? Old Excel: {len(old_rules)} rules")
    print(f"? New Excel: {len(new_rules)} rules\n")
    
    # Compare
    changes = compare_rules(old_rules, new_rules)
    impact = analyze_impact(changes)
    
    # Generate report
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(f"# Excel Rule Changes Impact Report\n\n")
        f.write(f"**Generated**: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
        f.write(f"**Old File**: {Path(old_file).name}\n")
        f.write(f"**New File**: {Path(new_file).name}\n\n")
        
        f.write(f"---\n\n")
        f.write(f"## ?? Summary\n\n")
        f.write(f"| Category | Count |\n")
        f.write(f"|----------|-------|\n")
        f.write(f"| New Rules | {len(changes['new'])} |\n")
        f.write(f"| Deleted Rules | {len(changes['deleted'])} |\n")
        f.write(f"| Modified Rules | {len(changes['modified'])} |\n")
        f.write(f"| **Total Changes** | **{len(changes['new']) + len(changes['deleted']) + len(changes['modified'])}** |\n\n")
        
        f.write(f"## ?? Impact Analysis\n\n")
        f.write(f"| Impact Level | Count | Description |\n")
        f.write(f"|--------------|-------|-------------|\n")
        f.write(f"| ?? HIGH | {len(impact['high'])} | Requires code changes in PQA_Agent |\n")
        f.write(f"| ?? MEDIUM | {len(impact['medium'])} | Requires review and planning |\n")
        f.write(f"| ?? LOW | {len(impact['low'])} | No action needed |\n\n")
        
        # High Impact
        if impact['high']:
            f.write(f"---\n\n")
            f.write(f"## ?? HIGH IMPACT (Action Required)\n\n")
            for idx, item in enumerate(impact['high'], 1):
                f.write(f"### {idx}. {item['type']}: {item['rule']['id']}\n\n")
                if item['type'] == 'MODIFIED':
                    f.write(f"**Rule**: {item['rule']['new']['name']}\n\n")
                    f.write(f"**Changes**:\n")
                    for change in item['rule']['changes']:
                        f.write(f"- {change}\n")
                    f.write(f"\n")
                else:
                    f.write(f"**Rule**: {item['rule']['name']}\n\n")
                
                f.write(f"**Reason**: {item['reason']}\n\n")
                f.write(f"**Action**: {item['action']}\n\n")
        
        # Medium Impact
        if impact['medium']:
            f.write(f"---\n\n")
            f.write(f"## ?? MEDIUM IMPACT (Review Needed)\n\n")
            for idx, item in enumerate(impact['medium'], 1):
                f.write(f"### {idx}. {item['type']}: {item['rule']['id']}\n\n")
                if item['type'] == 'MODIFIED':
                    f.write(f"**Rule**: {item['rule']['new']['name']}\n\n")
                    f.write(f"**Changes**:\n")
                    for change in item['rule']['changes']:
                        f.write(f"- {change}\n")
                    f.write(f"\n")
                else:
                    f.write(f"**Rule**: {item['rule']['name']}\n\n")
                
                f.write(f"**Reason**: {item['reason']}\n\n")
                f.write(f"**Action**: {item['action']}\n\n")
        
        # Low Impact
        if impact['low']:
            f.write(f"---\n\n")
            f.write(f"## ?? LOW IMPACT (No Action)\n\n")
            f.write(f"Total: {len(impact['low'])} changes with no impact on PQA_Agent\n\n")
            for item in impact['low']:
                f.write(f"- {item['type']}: **{item['rule']['id']}** - {item['rule']['name']}\n")
        
        # Recommendations
        f.write(f"\n---\n\n")
        f.write(f"## ?? Recommendations\n\n")
        
        if impact['high']:
            f.write(f"### Immediate Actions:\n")
            f.write(f"1. Review all HIGH impact changes\n")
            f.write(f"2. Update PQA_Agent code for modified rules\n")
            f.write(f"3. Implement or remove rules as needed\n")
            f.write(f"4. Run full test suite after changes\n\n")
        
        if impact['medium']:
            f.write(f"### Planning Actions:\n")
            f.write(f"1. Review MEDIUM impact changes\n")
            f.write(f"2. Update backlog with new PQA-relevant rules\n")
            f.write(f"3. Consider implementation timeline\n\n")
        
        f.write(f"### Documentation:\n")
        f.write(f"1. Update `EXCEL_JAVA_MAPPING.md` with new mappings\n")
        f.write(f"2. Update `RQ1_RULES.md` and `RQ1_RULES_DETAILED.md`\n")
        f.write(f"3. Archive old Excel version for reference\n\n")
    
    print(f"?? Impact report created: {output_file}\n")
    
    # Print summary
    print(f"{'='*60}")
    print(f"IMPACT SUMMARY:")
    print(f"{'='*60}")
    print(f"?? HIGH:   {len(impact['high'])} changes require code updates")
    print(f"?? MEDIUM: {len(impact['medium'])} changes need review")
    print(f"?? LOW:    {len(impact['low'])} changes have no impact")
    print(f"{'='*60}\n")


def main():
    parser = argparse.ArgumentParser(
        description='Detect Excel rule changes and analyze impact on PQA_Agent'
    )
    parser.add_argument('--old', required=True, help='Path to old Excel file')
    parser.add_argument('--new', required=True, help='Path to new Excel file')
    parser.add_argument('--output', default='EXCEL_IMPACT_REPORT.md', 
                       help='Output report file (default: EXCEL_IMPACT_REPORT.md)')
    
    args = parser.parse_args()
    
    # Validate files exist
    if not Path(args.old).exists():
        print(f"ERROR: Old file not found: {args.old}")
        return 1
    
    if not Path(args.new).exists():
        print(f"ERROR: New file not found: {args.new}")
        return 1
    
    # Create report
    create_impact_report(args.old, args.new, args.output)
    
    print(f"? Done! Review {args.output} for details.\n")
    return 0


if __name__ == '__main__':
    exit(main())
