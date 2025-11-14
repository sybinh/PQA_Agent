#!/usr/bin/env python3
"""
Parse Excel Rule Sets and create clean structured documentation
"""

import pandas as pd
import json
from pathlib import Path
from typing import Dict, List


def parse_bbm_rules(excel_file: str) -> List[Dict]:
    """Parse BBM Rule Set from Excel"""
    print("?? Parsing BBM Rule Set...")
    
    df = pd.read_excel(excel_file, sheet_name='BBM Rule Set', engine='openpyxl')
    
    rules = []
    current_rule = None
    
    for idx, row in df.iterrows():
        # Get first non-null value in row
        first_col = row.iloc[3] if pd.notna(row.iloc[3]) else None
        
        # Check if this is a rule ID (BBM XX)
        if first_col and isinstance(first_col, str) and first_col.startswith('BBM '):
            # Save previous rule
            if current_rule:
                rules.append(current_rule)
            
            # Start new rule
            rule_id = first_col.strip()
            rule_name = row.iloc[4] if pd.notna(row.iloc[4]) else ""
            execution = row.iloc[9] if pd.notna(row.iloc[9]) else ""
            check_retro = row.iloc[10] if pd.notna(row.iloc[10]) else ""
            implemented = row.iloc[11] if pd.notna(row.iloc[11]) else ""
            measured_in = row.iloc[12] if pd.notna(row.iloc[12]) else ""
            
            current_rule = {
                'id': rule_id,
                'name': rule_name,
                'execution': execution,
                'check_retrospectively': check_retro,
                'implemented_in_api': implemented,
                'measured_in': measured_in,
                'cluster': '',
                'where_defined': '',
                'description': '',
                'statement': '',
                'hints': '',
                'pseudo_code': ''
            }
        
        # Parse rule details
        elif current_rule and first_col:
            if first_col == 'Cluster':
                current_rule['cluster'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
            elif first_col == 'Where defined?':
                current_rule['where_defined'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
            elif first_col == 'Description':
                current_rule['description'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
            elif first_col == 'Pseudo Code':
                current_rule['pseudo_code'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
            elif first_col == 'Statement':
                current_rule['statement'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
            elif first_col == 'Hints':
                current_rule['hints'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
    
    # Add last rule
    if current_rule:
        rules.append(current_rule)
    
    print(f"   ? Found {len(rules)} BBM rules")
    return rules


def parse_qam_rules(excel_file: str) -> List[Dict]:
    """Parse QAM Rule Set from Excel"""
    print("?? Parsing QAM Rule Set (FKT)...")
    
    df = pd.read_excel(excel_file, sheet_name='QAM Rule Set (FKT)', engine='openpyxl')
    
    rules = []
    current_rule = None
    
    for idx, row in df.iterrows():
        first_col = row.iloc[3] if pd.notna(row.iloc[3]) else None
        
        # Check if this is a rule ID (QADO or QAWP patterns)
        if first_col and isinstance(first_col, str):
            # QAM rules can be QADO or QAWP
            if any(prefix in first_col for prefix in ['QADO ', 'QAWP ', 'QATP ', 'QACO ', 'QARC ']):
                # Save previous rule
                if current_rule:
                    rules.append(current_rule)
            
                # Start new rule
                rule_id = first_col.strip()
                rule_name = row.iloc[4] if pd.notna(row.iloc[4]) else ""
                execution = row.iloc[9] if pd.notna(row.iloc[9]) else ""
                check_retro = row.iloc[10] if pd.notna(row.iloc[10]) else ""
                implemented = row.iloc[11] if pd.notna(row.iloc[11]) else ""
                relevant_pqa = row.iloc[12] if pd.notna(row.iloc[12]) else ""
                remarks = row.iloc[14] if pd.notna(row.iloc[14]) and len(row) > 14 else ""
            
                current_rule = {
                    'id': rule_id,
                    'name': rule_name,
                    'execution': execution,
                    'check_retrospectively': check_retro,
                    'implemented_in_qam': implemented,
                    'relevant_for_pqa': relevant_pqa,
                    'cluster': '',
                    'where_defined': '',
                    'description': '',
                    'statement': '',
                    'hints': '',
                    'remarks': remarks
                }
        
        # Parse rule details
        elif current_rule and first_col:
            if first_col == 'Cluster':
                current_rule['cluster'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
            elif first_col == 'Where defined?':
                current_rule['where_defined'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
            elif first_col == 'Description':
                desc = row.iloc[4] if pd.notna(row.iloc[4]) else ""
                # Accumulate multi-line descriptions
                if desc:
                    if current_rule['description']:
                        current_rule['description'] += "\n" + str(desc)
                    else:
                        current_rule['description'] = str(desc)
            elif first_col == 'Statement':
                current_rule['statement'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
            elif first_col == 'Hints':
                current_rule['hints'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
    
    # Add last rule
    if current_rule:
        rules.append(current_rule)
    
    print(f"   ? Found {len(rules)} QAM rules")
    return rules


def parse_ipt_rules(excel_file: str) -> List[Dict]:
    """Parse IPT Rule Set from Excel"""
    print("?? Parsing IPT Rule Set (CUST)...")
    
    df = pd.read_excel(excel_file, sheet_name='IPT Rule Set (CUST)', engine='openpyxl')
    
    rules = []
    current_rule = None
    
    for idx, row in df.iterrows():
        first_col = row.iloc[3] if pd.notna(row.iloc[3]) else None
        
        # Check if this is a rule ID (various formats)
        if first_col and isinstance(first_col, str):
            # IPT rules have various ID formats: PRPL, PRPS, PRAS, QA** patterns
            if any(prefix in first_col for prefix in ['PRPL', 'PRPS', 'PRAS', 'QAWP', 'QATP', 'QACO', 'QARC']):
                # Save previous rule
                if current_rule:
                    rules.append(current_rule)
                
                # Start new rule
                rule_id = first_col.strip()
                rule_name = row.iloc[4] if pd.notna(row.iloc[4]) else ""
                execution = row.iloc[9] if pd.notna(row.iloc[9]) else ""
                check_retro = row.iloc[10] if pd.notna(row.iloc[10]) else ""
                implemented = row.iloc[11] if pd.notna(row.iloc[11]) else ""
                relevant_pqa = row.iloc[12] if pd.notna(row.iloc[12]) else ""
                
                current_rule = {
                    'id': rule_id,
                    'name': rule_name,
                    'execution': execution,
                    'check_retrospectively': check_retro,
                    'implemented': implemented,
                    'relevant_for_pqa': relevant_pqa,
                    'cluster': '',
                    'where_defined': '',
                    'description': '',
                    'statement': '',
                    'hints': ''
                }
            
            # Parse rule details
            elif current_rule:
                if first_col == 'Cluster':
                    current_rule['cluster'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
                elif first_col == 'Where defined?':
                    current_rule['where_defined'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
                elif first_col == 'Description':
                    desc = row.iloc[4] if pd.notna(row.iloc[4]) else ""
                    if desc:
                        if current_rule['description']:
                            current_rule['description'] += "\n" + str(desc)
                        else:
                            current_rule['description'] = str(desc)
                elif first_col == 'Statement':
                    current_rule['statement'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
                elif first_col == 'Hints':
                    current_rule['hints'] = row.iloc[4] if pd.notna(row.iloc[4]) else ""
    
    # Add last rule
    if current_rule:
        rules.append(current_rule)
    
    print(f"   ? Found {len(rules)} IPT rules")
    return rules


def get_priority_order(execution: str) -> int:
    """Get priority order for sorting (lower number = higher priority)"""
    priority_map = {
        'mandatory': 1,
        'optional': 2,
        'new': 3,
        'open': 4,
        'outdated': 5,
        'to be deleted': 6,
    }
    # Default priority for empty or unknown values
    return priority_map.get(str(execution).strip().lower(), 7)


def create_clean_markdown(rules: List[Dict], rule_type: str, output_file: str):
    """Create clean markdown documentation from rules, sorted by priority"""
    print(f"\n?? Creating clean markdown: {output_file}")
    
    # Sort rules by priority (execution), then by ID
    sorted_rules = sorted(rules, key=lambda r: (get_priority_order(r.get('execution', '')), r.get('id', '')))
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(f"# {rule_type} - Clean Rules Documentation\n\n")
        f.write(f"**Total Rules**: {len(sorted_rules)}\n")
        f.write(f"**Source**: SW_QAMRuleSet_Tmplt.xlsm\n")
        f.write(f"**Parsed**: Auto-generated from Excel\n")
        f.write(f"**Sorted By**: Priority (execution), then Rule ID\n\n")
        
        # Group by execution/priority
        groups = {}
        for rule in sorted_rules:
            exec_val = rule.get('execution', '').strip() or '(not specified)'
            if exec_val not in groups:
                groups[exec_val] = []
            groups[exec_val].append(rule)
        
        # Write summary by priority
        f.write("## Priority Summary\n\n")
        f.write("| Execution | Priority | Count |\n")
        f.write("|-----------|----------|-------|\n")
        priority_labels = {
            'mandatory': '??? CRITICAL',
            'optional': '?? HIGH',
            'new': '? MEDIUM',
            'open': '?? LOW',
            'outdated': '? SKIP',
            'to be deleted': '? SKIP'
        }
        for exec_val in sorted(groups.keys(), key=get_priority_order):
            count = len(groups[exec_val])
            priority = priority_labels.get(exec_val, '- N/A')
            f.write(f"| {exec_val} | {priority} | {count} |\n")
        
        f.write("\n---\n\n")
        
        # Write rules grouped by priority
        for exec_val in sorted(groups.keys(), key=get_priority_order):
            priority = priority_labels.get(exec_val, '- N/A')
            f.write(f"## {priority}: {exec_val.upper()} ({len(groups[exec_val])} rules)\n\n")
            
            for rule in groups[exec_val]:
                f.write(f"### {rule['id']}\n\n")
                
                # Name
                if rule.get('name'):
                    f.write(f"**Name**: {rule['name']}\n\n")
                
                # Execution (already grouped, but show for clarity)
                if rule.get('execution'):
                    f.write(f"**Execution**: {rule['execution']}\n")
                
                # Check Retrospectively
                if rule.get('check_retrospectively'):
                    f.write(f"**Check Retrospectively**: {rule['check_retrospectively']}\n")
                
                # Implemented date (handle both field names)
                impl_field = rule.get('implemented_in_qam') or rule.get('implemented_in_api') or rule.get('implemented')
                if impl_field and str(impl_field).strip():
                    f.write(f"**Implemented**: {impl_field}\n")
                
                # Relevant for PQA
                if rule.get('relevant_for_pqa'):
                    f.write(f"**Relevant for PQA**: {rule['relevant_for_pqa']}\n")
                
                # Cluster
                if rule.get('cluster'):
                    f.write(f"**Cluster**: {rule['cluster']}\n")
                
                # Where defined
                if rule.get('where_defined'):
                    f.write(f"**Where Defined**: {rule['where_defined']}\n")
                
                # Description
                if rule.get('description'):
                    f.write(f"\n**Description**:\n{rule['description']}\n")
                
                # Statement
                if rule.get('statement'):
                    f.write(f"\n**Statement**: {rule['statement']}\n")
                
                # Hints
                if rule.get('hints'):
                    f.write(f"\n**Hints**: {rule['hints']}\n")
                
                # Remarks
                if rule.get('remarks'):
                    f.write(f"\n**Remarks**: {rule['remarks']}\n")
                
                f.write("\n---\n\n")
    
    print(f"   ? Created: {output_file}")


def create_summary_markdown(bbm_rules: List[Dict], qam_rules: List[Dict], ipt_rules: List[Dict], output_file: str):
    """Create summary comparison file with priority information"""
    print(f"\n?? Creating summary: {output_file}")
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write("# Excel Rule Sets - Summary\n\n")
        f.write(f"**Source**: SW_QAMRuleSet_Tmplt.xlsm\n\n")
        
        f.write("## Overview\n\n")
        f.write(f"| Rule Set | Count | Description |\n")
        f.write(f"|----------|-------|-------------|\n")
        f.write(f"| BBM | {len(bbm_rules)} | Quality Assurance - BBM metrics |\n")
        f.write(f"| QAM (FKT) | {len(qam_rules)} | Quality Assurance - Dokumentation |\n")
        f.write(f"| IPT (CUST) | {len(ipt_rules)} | PMT - Rule Set |\n")
        f.write(f"| **TOTAL** | **{len(bbm_rules) + len(qam_rules) + len(ipt_rules)}** | |\n\n")
        
        # BBM Rules List
        f.write("## BBM Rules (Metrics)\n\n")
        for rule in bbm_rules:
            f.write(f"- **{rule['id']}**: {rule['name']}\n")
            if rule.get('execution'):
                f.write(f"  - Execution: {rule['execution']}\n")
        f.write("\n")
        
        # QAM Rules List
        f.write("## QAM Rules (Documentation)\n\n")
        for rule in qam_rules:
            f.write(f"- **{rule['id']}**: {rule['name']}\n")
            if rule.get('relevant_for_pqa'):
                f.write(f"  - Relevant for PQA: {rule['relevant_for_pqa']}\n")
        f.write("\n")
        
        # IPT Rules List
        f.write("## IPT Rules (Customer)\n\n")
        for rule in ipt_rules:
            f.write(f"- **{rule['id']}**: {rule['name']}\n")
            if rule.get('relevant_for_pqa'):
                f.write(f"  - Relevant for PQA: {rule['relevant_for_pqa']}\n")
        f.write("\n")
    
    print(f"   ? Created: {output_file}")


def main():
    excel_file = "rq1/POST_V_1.0.3/SW_QAMRuleSet_Tmplt.xlsm"
    output_dir = Path("rq1/POST_V_1.0.3/parsed_rules")
    output_dir.mkdir(exist_ok=True)
    
    print("\n?? Starting Excel Rule Parsing...\n")
    
    # Parse all rule sets
    bbm_rules = parse_bbm_rules(excel_file)
    qam_rules = parse_qam_rules(excel_file)
    ipt_rules = parse_ipt_rules(excel_file)
    
    # Create clean markdown files
    create_clean_markdown(bbm_rules, "BBM Rules", output_dir / "BBM_RULES_CLEAN.md")
    create_clean_markdown(qam_rules, "QAM Rules", output_dir / "QAM_RULES_CLEAN.md")
    create_clean_markdown(ipt_rules, "IPT Rules", output_dir / "IPT_RULES_CLEAN.md")
    
    # Create summary
    create_summary_markdown(bbm_rules, qam_rules, ipt_rules, output_dir / "EXCEL_RULES_SUMMARY.md")
    
    # Save as JSON for programmatic access
    all_rules = {
        'bbm_rules': bbm_rules,
        'qam_rules': qam_rules,
        'ipt_rules': ipt_rules
    }
    
    json_file = output_dir / "all_rules.json"
    with open(json_file, 'w', encoding='utf-8') as f:
        json.dump(all_rules, f, indent=2, ensure_ascii=False)
    
    print(f"\n? JSON data saved: {json_file}")
    print(f"\n?? Parsing complete!")
    print(f"\n?? Output files in: {output_dir}/")
    print(f"   - BBM_RULES_CLEAN.md")
    print(f"   - QAM_RULES_CLEAN.md")
    print(f"   - IPT_RULES_CLEAN.md")
    print(f"   - EXCEL_RULES_SUMMARY.md")
    print(f"   - all_rules.json\n")


if __name__ == '__main__':
    main()
