"""
Parse all RQ1 rules from Java files and extract structured information
"""
import os
import re
import json
from pathlib import Path


def extract_rule_info(file_path):
    """Extract rule information from Java file"""
    with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
        content = f.read()
    
    rule_name = Path(file_path).stem
    
    # Extract description
    desc_match = re.search(r'DmRq1RuleDescription\s*=\s*new\s+.*?\(\s*(?:EnumSet\.of\([^)]+\),)?\s*"([^"]+)"', content, re.DOTALL)
    title = desc_match.group(1) if desc_match else "N/A"
    
    # Extract full description text
    full_desc_match = re.search(r'"([^"]+)"\s*\+?\s*\n.*?"(.*?)"(?:\s*\+\s*".*?")*;', content, re.DOTALL)
    description = ""
    if full_desc_match:
        description = full_desc_match.group(2).replace('" + "', ' ').replace('\\n', '\n')
    
    # Extract record type
    record_type_match = re.search(r'extends\s+DmRule<(\w+)>', content)
    record_type = record_type_match.group(1) if record_type_match else "N/A"
    
    # Extract fields being checked
    field_patterns = [
        r'dmElement\.(\w+)\.(?:getValue|isEmpty|getValueAsText)',
        r'(\w+)\.getValue\(\)',
        r'\.(\w+)\.get',
    ]
    fields = set()
    for pattern in field_patterns:
        fields.update(re.findall(pattern, content))
    
    # Extract severity
    severity = "WARNING"
    if "addMarker(dmElement, new Failure" in content:
        severity = "FAILURE"
    elif "addMarker(dmElement, new Warning" in content:
        severity = "WARNING"
    
    # Extract conditions
    conditions = []
    if "isInLifeCycleState" in content:
        lifecycle_match = re.search(r'isInLifeCycleState\(([^)]+)\)', content)
        if lifecycle_match:
            conditions.append(f"LifeCycleState: {lifecycle_match.group(1)}")
    
    if "SUBMIT_DATE" in content and "isLaterOrEqualThen" in content:
        date_match = re.search(r'isLaterOrEqualThen\(EcvDate\.getDate\((\d+),\s*(\d+),\s*(\d+)\)', content)
        if date_match:
            conditions.append(f"SubmitDate >= {date_match.group(1)}-{date_match.group(2)}-{date_match.group(3)}")
    
    return {
        "rule_name": rule_name,
        "title": title,
        "description": description[:200] + "..." if len(description) > 200 else description,
        "record_type": record_type,
        "fields_checked": sorted(list(fields))[:10],  # Top 10 fields
        "severity": severity,
        "conditions": conditions,
    }


def main():
    rules_dir = Path("rq1/POST_extracted/DataModel/Rq1/Monitoring")
    rule_files = sorted(rules_dir.glob("Rule_*.java"))
    
    all_rules = []
    for rule_file in rule_files:
        try:
            rule_info = extract_rule_info(rule_file)
            all_rules.append(rule_info)
        except Exception as e:
            print(f"Error parsing {rule_file.name}: {e}")
    
    # Group by category
    categories = {
        "IssueSW": [],
        "IssueFD": [],
        "BC": [],
        "FC": [],
        "Release": [],
        "Dates": [],
        "IRM": [],
        "RRM": [],
        "Other": []
    }
    
    for rule in all_rules:
        name = rule["rule_name"]
        if "IssueSW" in name:
            categories["IssueSW"].append(rule)
        elif "IssueFD" in name:
            categories["IssueFD"].append(rule)
        elif name.startswith("Rule_Bc_"):
            categories["BC"].append(rule)
        elif name.startswith("Rule_Fc_"):
            categories["FC"].append(rule)
        elif "Release" in name or "Pver" in name or "Pvar" in name:
            categories["Release"].append(rule)
        elif "CheckDates" in name:
            categories["Dates"].append(rule)
        elif "Irm" in name:
            categories["IRM"].append(rule)
        elif "Rrm" in name:
            categories["RRM"].append(rule)
        else:
            categories["Other"].append(rule)
    
    # Save to JSON
    output = {
        "total_rules": len(all_rules),
        "categories": {k: len(v) for k, v in categories.items() if v},
        "rules_by_category": categories
    }
    
    with open("rq1/RQ1_RULES_COMPLETE.json", "w", encoding="utf-8") as f:
        json.dump(output, f, indent=2, ensure_ascii=False)
    
    print(f"? Parsed {len(all_rules)} rules")
    print(f"?? Saved to: rq1/RQ1_RULES_COMPLETE.json")
    
    # Print summary
    print("\n?? Rules by Category:")
    for cat, rules in categories.items():
        if rules:
            print(f"  {cat}: {len(rules)} rules")


if __name__ == "__main__":
    main()
