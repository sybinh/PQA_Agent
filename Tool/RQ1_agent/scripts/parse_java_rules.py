#!/usr/bin/env python3
"""
Java Rules Parser - Extract QAM/BBM/IPT IDs from Java source files
"""
import re
from pathlib import Path
from typing import Dict, List, Optional, Set
from dataclasses import dataclass
import json

@dataclass
class JavaRule:
    """Java rule extracted from source"""
    file_name: str
    class_name: str
    rule_id: Optional[str] = None  # e.g., "BC_04__CLOSE"
    qam_id: Optional[str] = None   # e.g., "QAM ID-2.5.0"
    bbm_id: Optional[str] = None   # e.g., "BBM 10"
    ipt_id: Optional[str] = None   # e.g., "PRPL 10.00.00"
    warning_title: Optional[str] = None
    description: Optional[str] = None
    execution_groups: List[str] = None
    
    def __post_init__(self):
        if self.execution_groups is None:
            self.execution_groups = []
    
    @property
    def category(self) -> str:
        """Determine category based on IDs"""
        if self.qam_id:
            return "QAM"
        elif self.bbm_id:
            return "BBM"
        elif self.ipt_id:
            return "IPT"
        else:
            return "UNKNOWN"
    
    def to_dict(self) -> Dict:
        """Convert to dictionary"""
        return {
            "file_name": self.file_name,
            "class_name": self.class_name,
            "rule_id": self.rule_id,
            "category": self.category,
            "qam_id": self.qam_id,
            "bbm_id": self.bbm_id,
            "ipt_id": self.ipt_id,
            "warning_title": self.warning_title,
            "description": self.description,
            "execution_groups": self.execution_groups
        }

class JavaRulesParser:
    """Parse Java rule files to extract metadata"""
    
    def __init__(self, java_rules_dir: str = "rq1/POST_extracted/DataModel/Rq1/Monitoring"):
        self.java_rules_dir = Path(java_rules_dir)
        self.rules: List[JavaRule] = []
    
    def parse_all_rules(self) -> List[JavaRule]:
        """Parse all Java rule files"""
        if not self.java_rules_dir.exists():
            print(f"? Directory not found: {self.java_rules_dir}")
            return []
        
        rule_files = list(self.java_rules_dir.glob("Rule_*.java"))
        print(f"Found {len(rule_files)} Java rule files")
        
        for file_path in rule_files:
            rule = self._parse_rule_file(file_path)
            if rule:
                self.rules.append(rule)
        
        return self.rules
    
    def _parse_rule_file(self, file_path: Path) -> Optional[JavaRule]:
        """Parse a single Java rule file"""
        try:
            content = file_path.read_text(encoding='utf-8', errors='ignore')
            
            # Extract class name
            class_match = re.search(r'public class (\w+)', content)
            class_name = class_match.group(1) if class_match else file_path.stem
            
            # Extract rule ID (e.g., "BC_04__CLOSE")
            rule_id_match = re.search(r'"([A-Z_0-9]+)"[,\s]*\n\s*EnumSet\.of', content)
            rule_id = rule_id_match.group(1) if rule_id_match else None
            
            # Extract QAM ID
            qam_matches = re.findall(r'QAM\s+ID[:-]\s*([0-9.]+)', content, re.IGNORECASE)
            qam_id = f"QAM ID-{qam_matches[0]}" if qam_matches else None
            
            # Extract BBM ID
            bbm_matches = re.findall(r'BBM[:\s]+([0-9]+)', content, re.IGNORECASE)
            bbm_id = f"BBM {bbm_matches[0]}" if bbm_matches else None
            
            # Extract IPT/PRPL ID
            ipt_matches = re.findall(r'PRPL[:\s]+([0-9.]+)', content, re.IGNORECASE)
            ipt_id = f"PRPL {ipt_matches[0]}" if ipt_matches else None
            
            # Extract warning title
            warning_match = re.search(r'warningTitle\s*=\s*"([^"]+)"', content)
            warning_title = warning_match.group(1) if warning_match else None
            
            # Extract execution groups
            execution_groups = []
            group_matches = re.findall(r'RuleExecutionGroup\.(\w+)', content)
            execution_groups = list(set(group_matches))
            
            # Extract description comment
            desc_match = re.search(r'/\*\*\s*\n\s*\*\s*([^\n]+)', content)
            description = desc_match.group(1).strip() if desc_match else None
            
            rule = JavaRule(
                file_name=file_path.name,
                class_name=class_name,
                rule_id=rule_id,
                qam_id=qam_id,
                bbm_id=bbm_id,
                ipt_id=ipt_id,
                warning_title=warning_title,
                description=description,
                execution_groups=execution_groups
            )
            
            return rule
            
        except Exception as e:
            print(f"? Error parsing {file_path.name}: {e}")
            return None
    
    def group_by_category(self) -> Dict[str, List[JavaRule]]:
        """Group rules by category (QAM, BBM, IPT)"""
        categories = {"QAM": [], "BBM": [], "IPT": [], "UNKNOWN": []}
        
        for rule in self.rules:
            categories[rule.category].append(rule)
        
        return categories
    
    def export_to_json(self, output_file: str = "docs/java_rules_catalog.json"):
        """Export parsed rules to JSON"""
        data = {
            "total_rules": len(self.rules),
            "categories": {
                "QAM": len([r for r in self.rules if r.category == "QAM"]),
                "BBM": len([r for r in self.rules if r.category == "BBM"]),
                "IPT": len([r for r in self.rules if r.category == "IPT"]),
                "UNKNOWN": len([r for r in self.rules if r.category == "UNKNOWN"])
            },
            "rules": [rule.to_dict() for rule in self.rules]
        }
        
        output_path = Path(output_file)
        output_path.parent.mkdir(parents=True, exist_ok=True)
        output_path.write_text(json.dumps(data, indent=2), encoding='utf-8')
        print(f"? Exported to {output_file}")
        
        return data
    
    def print_summary(self):
        """Print summary of parsed rules"""
        categories = self.group_by_category()
        
        print("\n" + "="*70)
        print("JAVA RULES CATALOG")
        print("="*70)
        
        print(f"\nTotal Rules: {len(self.rules)}")
        print(f"  QAM: {len(categories['QAM'])}")
        print(f"  BBM: {len(categories['BBM'])}")
        print(f"  IPT: {len(categories['IPT'])}")
        print(f"  UNKNOWN: {len(categories['UNKNOWN'])}")
        
        for category, rules in categories.items():
            if not rules:
                continue
                
            print(f"\n{category} Rules ({len(rules)}):")
            print("-" * 70)
            
            for rule in sorted(rules, key=lambda r: r.qam_id or r.bbm_id or r.ipt_id or ""):
                id_str = rule.qam_id or rule.bbm_id or rule.ipt_id or "No ID"
                print(f"  {rule.class_name:40s} | {id_str:15s}")
                if rule.warning_title:
                    print(f"    ?? {rule.warning_title[:60]}")

def main():
    """Main function"""
    print("=== Java Rules Parser ===")
    
    parser = JavaRulesParser()
    
    # Parse all rules
    print("\n1. Parsing Java rule files...")
    rules = parser.parse_all_rules()
    
    if not rules:
        print("? No rules found")
        return
    
    # Print summary
    parser.print_summary()
    
    # Export to JSON
    print("\n2. Exporting to JSON...")
    parser.export_to_json()
    
    # Show examples
    print("\n3. Example rules with categories:")
    categories = parser.group_by_category()
    for category in ["QAM", "BBM", "IPT"]:
        if categories[category]:
            rule = categories[category][0]
            print(f"\n{category} Example:")
            print(f"  Class: {rule.class_name}")
            print(f"  ID: {rule.qam_id or rule.bbm_id or rule.ipt_id}")
            print(f"  Rule ID: {rule.rule_id}")
            print(f"  Warning: {rule.warning_title}")
            print(f"  Groups: {', '.join(rule.execution_groups[:3])}")

if __name__ == "__main__":
    main()