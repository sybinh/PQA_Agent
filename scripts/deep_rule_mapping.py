"""
Deep analysis of Java rules to map to activated_rules.csv.

This script:
1. Reads all 37 Java rule files
2. Extracts execution groups, QAM/BBM/IPT IDs, descriptions
3. Maps to 32 CSV rules using keyword matching + semantic analysis
4. Generates detailed implementation plan
"""

import csv
import re
from pathlib import Path
from typing import Dict, List, Set, Optional
from dataclasses import dataclass

@dataclass
class JavaRule:
    """Java rule from POST tool."""
    filename: str
    class_name: str
    execution_group: Optional[str]
    qam_id: Optional[str]
    bbm_id: Optional[str]
    ipt_id: Optional[str]
    description: str
    keywords: Set[str]

@dataclass
class CSVRule:
    """Rule from activated_rules.csv."""
    rule_id: str
    description: str
    activation_date: str
    category: str
    
def extract_java_rule_info(java_file: Path) -> JavaRule:
    """Extract metadata from Java rule file."""
    content = java_file.read_text(encoding='utf-8', errors='ignore')
    
    # Extract class name
    class_match = re.search(r'public\s+class\s+(\w+)', content)
    class_name = class_match.group(1) if class_match else java_file.stem
    
    # Extract execution group
    exec_group_match = re.search(r'RuleExecutionGroup\.(\w+)', content)
    execution_group = exec_group_match.group(1) if exec_group_match else None
    
    # Extract QAM/BBM/IPT IDs from comments or string literals
    qam_match = re.search(r'(?:QAM|ID-)[\s-]*([\d.]+)', content, re.IGNORECASE)
    bbm_match = re.search(r'BBM[\s-]*([\d.]+)', content, re.IGNORECASE)
    ipt_match = re.search(r'IPT[\s-]*([\d.]+)', content, re.IGNORECASE)
    
    qam_id = f"QAM {qam_match.group(1)}" if qam_match else None
    bbm_id = f"BBM {bbm_match.group(1)}" if bbm_match else None
    ipt_id = f"IPT {ipt_match.group(1)}" if ipt_match else None
    
    # Extract description from comments
    desc_match = re.search(r'/\*\*\s*\n\s*\*\s*(.+?)\s*\*/', content, re.DOTALL)
    description = desc_match.group(1).strip() if desc_match else ""
    
    # Extract keywords for matching
    keywords = set()
    keywords.add(class_name.lower())
    if 'bc' in class_name.lower():
        keywords.update(['bc', 'release', 'baseline'])
    if 'fc' in class_name.lower():
        keywords.update(['fc', 'release'])
    if 'ifd' in class_name.lower() or 'issuefd' in class_name.lower():
        keywords.update(['ifd', 'issue', 'defect'])
    if 'close' in class_name.lower():
        keywords.add('close')
    if 'date' in class_name.lower():
        keywords.update(['date', 'planned'])
    if 'asil' in class_name.lower():
        keywords.add('asil')
    
    return JavaRule(
        filename=java_file.name,
        class_name=class_name,
        execution_group=execution_group,
        qam_id=qam_id,
        bbm_id=bbm_id,
        ipt_id=ipt_id,
        description=description,
        keywords=keywords
    )

def parse_csv_rules(csv_path: Path) -> List[CSVRule]:
    """Parse activated_rules.csv."""
    rules = []
    with open(csv_path, 'r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            rule_id = row.get('Rule ID', '').strip()
            rules.append(CSVRule(
                rule_id=rule_id,
                description=row.get('Rule Desc', '').strip(),
                activation_date=row.get('Activation Date', '').strip(),
                category=rule_id.split()[0] if ' ' in rule_id else 'UNKNOWN'
            ))
    return rules

def match_csv_to_java(csv_rule: CSVRule, java_rules: List[JavaRule]) -> Optional[JavaRule]:
    """
    Match CSV rule to Java rule using keyword similarity.
    
    Returns best matching Java rule or None.
    """
    csv_desc_lower = csv_rule.description.lower()
    csv_keywords = set(csv_desc_lower.split())
    
    best_match = None
    best_score = 0
    
    for java_rule in java_rules:
        score = 0
        
        # Exact BBM ID match (highest priority)
        if csv_rule.category == 'BBM' and csv_rule.rule_id.replace('_', ' ') in (java_rule.bbm_id or ''):
            score += 100
        
        # Keyword overlap
        keyword_overlap = csv_keywords & java_rule.keywords
        score += len(keyword_overlap) * 10
        
        # Specific patterns
        if 'bc' in csv_desc_lower and 'bc' in java_rule.keywords:
            score += 20
        if 'fc' in csv_desc_lower and 'fc' in java_rule.keywords:
            score += 20
        if 'ifd' in csv_desc_lower.replace('-', '') and 'ifd' in java_rule.keywords:
            score += 20
        if 'close' in csv_desc_lower and 'close' in java_rule.keywords:
            score += 15
        if 'planned date' in csv_desc_lower and 'date' in java_rule.keywords:
            score += 15
        if 'asil' in csv_desc_lower and 'asil' in java_rule.keywords:
            score += 30
        
        if score > best_score:
            best_score = score
            best_match = java_rule
    
    # Only return match if score is significant
    return best_match if best_score >= 20 else None

def main():
    project_root = Path(__file__).parent.parent
    
    # Read all Java rules
    java_dir = project_root / "rq1" / "POST_extracted" / "DataModel" / "Rq1" / "Monitoring"
    java_files = list(java_dir.glob("Rule_*.java"))
    
    print(f"[STEP 1] Reading {len(java_files)} Java rule files...")
    java_rules = []
    for java_file in java_files:
        try:
            rule = extract_java_rule_info(java_file)
            java_rules.append(rule)
        except Exception as e:
            print(f"  [WARN] Failed to parse {java_file.name}: {e}")
    
    print(f"[OK] Parsed {len(java_rules)} Java rules")
    
    # Read CSV rules
    csv_path = project_root / "rq1" / "activated_rules.csv"
    print(f"\n[STEP 2] Reading CSV rules from {csv_path.name}...")
    csv_rules = parse_csv_rules(csv_path)
    print(f"[OK] Parsed {len(csv_rules)} CSV rules")
    
    # Match rules
    print(f"\n[STEP 3] Matching CSV rules to Java implementations...")
    matched_rules = []
    unmatched_rules = []
    
    for csv_rule in csv_rules:
        java_match = match_csv_to_java(csv_rule, java_rules)
        if java_match:
            matched_rules.append((csv_rule, java_match))
        else:
            unmatched_rules.append(csv_rule)
    
    print(f"[OK] Matched {len(matched_rules)} rules, {len(unmatched_rules)} unmatched")
    
    # Generate report
    print("\n" + "="*80)
    print("DEEP RULE MAPPING REPORT")
    print("="*80)
    
    print(f"\n[STATS]")
    print(f"  Total CSV rules: {len(csv_rules)}")
    print(f"  Total Java rules: {len(java_rules)}")
    print(f"  Matched: {len(matched_rules)} ({len(matched_rules)/len(csv_rules)*100:.1f}%)")
    print(f"  Unmatched: {len(unmatched_rules)} ({len(unmatched_rules)/len(csv_rules)*100:.1f}%)")
    
    # Breakdown by category
    categories = {}
    for csv_rule in csv_rules:
        cat = csv_rule.category
        categories[cat] = categories.get(cat, 0) + 1
    
    print(f"\n[BREAKDOWN BY CATEGORY]")
    for cat, count in sorted(categories.items(), key=lambda x: -x[1]):
        matched_count = sum(1 for r, _ in matched_rules if r.category == cat)
        print(f"  {cat}: {matched_count}/{count} matched ({matched_count/count*100:.0f}%)")
    
    print("\n" + "="*80)
    print("MATCHED RULES (Can reuse Java logic)")
    print("="*80)
    for csv_rule, java_rule in matched_rules:
        print(f"\n{csv_rule.rule_id}: {csv_rule.description}")
        print(f"  -> Java: {java_rule.class_name}")
        print(f"     Group: {java_rule.execution_group or 'N/A'}")
        print(f"     IDs: {java_rule.qam_id or ''} {java_rule.bbm_id or ''} {java_rule.ipt_id or ''}".strip())
    
    print("\n" + "="*80)
    print("UNMATCHED RULES (Need new Python implementation)")
    print("="*80)
    for csv_rule in unmatched_rules:
        print(f"{csv_rule.rule_id}: {csv_rule.description}")
    
    # Effort estimation
    matched_effort_min = len(matched_rules) * 3
    matched_effort_max = len(matched_rules) * 6
    unmatched_effort_min = len(unmatched_rules) * 8
    unmatched_effort_max = len(unmatched_rules) * 12
    total_min = matched_effort_min + unmatched_effort_min
    total_max = matched_effort_max + unmatched_effort_max
    
    print("\n" + "="*80)
    print("IMPLEMENTATION EFFORT ESTIMATE")
    print("="*80)
    print(f"Phase 1 (Matched rules): {matched_effort_min}-{matched_effort_max} hours")
    print(f"Phase 2 (Unmatched rules): {unmatched_effort_min}-{unmatched_effort_max} hours")
    print(f"Total: {total_min}-{total_max} hours ({total_min//8}-{total_max//8} days)")
    
    # Save to markdown
    output_path = project_root / "docs" / "DEEP_RULE_MAPPING.md"
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write("# Deep Rule Mapping Analysis\n\n")
        f.write(f"Total CSV rules: {len(csv_rules)}\n")
        f.write(f"Total Java rules: {len(java_rules)}\n")
        f.write(f"Matched: {len(matched_rules)} ({len(matched_rules)/len(csv_rules)*100:.1f}%)\n\n")
        
        f.write("## Matched Rules (Can Reuse Java Logic)\n\n")
        for csv_rule, java_rule in matched_rules:
            f.write(f"### {csv_rule.rule_id}\n\n")
            f.write(f"**CSV**: {csv_rule.description}\n\n")
            f.write(f"**Java Class**: `{java_rule.class_name}`\n\n")
            f.write(f"**Execution Group**: {java_rule.execution_group or 'N/A'}\n\n")
            f.write(f"**IDs**: {java_rule.qam_id or ''} {java_rule.bbm_id or ''} {java_rule.ipt_id or ''}\n\n".strip())
            f.write(f"**Java File**: `{java_rule.filename}`\n\n")
            f.write("---\n\n")
        
        f.write("## Unmatched Rules (Need New Python Implementation)\n\n")
        for csv_rule in unmatched_rules:
            f.write(f"### {csv_rule.rule_id}\n\n")
            f.write(f"**Description**: {csv_rule.description}\n\n")
            f.write(f"**Category**: {csv_rule.category}\n\n")
            f.write(f"**Activation Date**: {csv_rule.activation_date}\n\n")
            f.write("---\n\n")
    
    print(f"\n[SAVED] Detailed report: {output_path}")

if __name__ == "__main__":
    main()
