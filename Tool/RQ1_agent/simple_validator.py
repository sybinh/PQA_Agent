#!/usr/bin/env python3
"""
Simple RQ1 Validator - Scan by User ID
Approach: Query user's records ? Run validation rules ? Generate report
"""
from rq1 import Client, BaseUrl
from rq1.models import Issue, IssueProperty, Release, ReleaseProperty, Users
from typing import List, Dict, Any
from dataclasses import dataclass, field
from datetime import datetime
import json

@dataclass
class ValidationResult:
    """Validation result for a single rule"""
    record_id: str
    record_type: str  # Issue, Release, etc.
    rule_id: str
    rule_title: str
    passed: bool
    message: str = ""
    severity: str = "WARNING"  # WARNING, ERROR, INFO

@dataclass
class UserValidationReport:
    """Complete validation report for a user"""
    user_id: str
    scan_date: datetime
    total_records: int = 0
    total_violations: int = 0
    records_checked: List[str] = field(default_factory=list)
    results: List[ValidationResult] = field(default_factory=list)
    
    def add_result(self, result: ValidationResult):
        """Add a validation result"""
        self.results.append(result)
        if not result.passed:
            self.total_violations += 1
    
    def to_dict(self) -> Dict:
        """Export to dictionary"""
        return {
            "user_id": self.user_id,
            "scan_date": self.scan_date.isoformat(),
            "summary": {
                "total_records": self.total_records,
                "total_violations": self.total_violations,
                "pass_rate": f"{((self.total_records - self.total_violations) / self.total_records * 100):.1f}%" if self.total_records > 0 else "N/A"
            },
            "records_checked": self.records_checked,
            "violations": [
                {
                    "record_id": r.record_id,
                    "record_type": r.record_type,
                    "rule": r.rule_title,
                    "severity": r.severity,
                    "message": r.message
                }
                for r in self.results if not r.passed
            ],
            "all_results": [
                {
                    "record_id": r.record_id,
                    "rule": r.rule_title,
                    "passed": r.passed,
                    "message": r.message
                }
                for r in self.results
            ]
        }

class SimpleRQ1Validator:
    """Simple validator - scan user's records and apply rules"""
    
    def __init__(self, base_url: BaseUrl, username: str, password: str):
        """Initialize validator with RQ1 connection"""
        self.client = Client(
            base_url=base_url,
            username=username,
            password=password,
            toolname="RQ1_Validator",
            toolversion="1.0"
        )
        print(f"? Connected to RQ1: {base_url.name}")
    
    def scan_user(self, user_id: str) -> UserValidationReport:
        """Scan all records for a user"""
        print(f"\n{'='*70}")
        print(f"Scanning records for user: {user_id}")
        print(f"{'='*70}")
        
        report = UserValidationReport(
            user_id=user_id,
            scan_date=datetime.now()
        )
        
        # 1. Get user's Issues
        print(f"\n1. Fetching Issues assigned to {user_id}...")
        issues = self._get_user_issues(user_id)
        print(f"   Found {len(issues)} issues")
        
        # 2. Validate Issues
        for issue in issues:
            report.records_checked.append(f"Issue: {issue}")
            # Run issue validation rules here
            # TODO: Implement issue rules
        
        # 3. Get user's Releases (BC/FC)
        print(f"\n2. Fetching Releases assigned to {user_id}...")
        releases = self._get_user_releases(user_id)
        print(f"   Found {len(releases)} releases")
        
        # 4. Validate Releases
        for release in releases:
            report.records_checked.append(f"Release: {release}")
            # Run release validation rules here
            # TODO: Implement release rules
        
        report.total_records = len(issues) + len(releases)
        
        return report
    
    def _get_user_issues(self, user_id: str) -> List[str]:
        """Get issues assigned to user"""
        try:
            # Query for issues assigned to user
            results = self.client.query(
                Issue,
                where=f"assignee/assignee_id='{user_id}'",
                select=[IssueProperty.RQ1_NUMBER],
                paging={"page": 1, "page_size": 100}
            )
            
            issue_numbers = []
            for issue in results.records:
                # Extract RQ1 number from properties
                if hasattr(issue, 'properties') and issue.properties:
                    for prop in issue.properties:
                        if hasattr(prop, 'name') and prop.name == 'RQ1_NUMBER':
                            issue_numbers.append(prop.value)
                            break
            
            return issue_numbers
            
        except Exception as e:
            print(f"   ??  Error fetching issues: {e}")
            return []
    
    def _get_user_releases(self, user_id: str) -> List[str]:
        """Get releases assigned to user"""
        try:
            # Query for releases assigned to user
            results = self.client.query(
                Release,
                where=f"responsible_person/user_id='{user_id}'",
                select=[ReleaseProperty.DESCRIPTION],
                paging={"page": 1, "page_size": 100}
            )
            
            release_ids = []
            for release in results.records:
                if hasattr(release, 'properties') and release.properties:
                    for prop in release.properties:
                        if hasattr(prop, 'name') and prop.name == 'DESCRIPTION':
                            release_ids.append(prop.value)
                            break
            
            return release_ids
            
        except Exception as e:
            print(f"   ??  Error fetching releases: {e}")
            return []
    
    def generate_report(self, report: UserValidationReport, output_file: str = None):
        """Generate validation report"""
        print(f"\n{'='*70}")
        print(f"VALIDATION REPORT")
        print(f"{'='*70}")
        print(f"User: {report.user_id}")
        print(f"Scan Date: {report.scan_date.strftime('%Y-%m-%d %H:%M:%S')}")
        print(f"Total Records: {report.total_records}")
        print(f"Total Violations: {report.total_violations}")
        
        if report.total_records > 0:
            pass_rate = (report.total_records - report.total_violations) / report.total_records * 100
            print(f"Pass Rate: {pass_rate:.1f}%")
        
        print(f"\n{'='*70}")
        
        # Export to JSON if output file specified
        if output_file:
            with open(output_file, 'w', encoding='utf-8') as f:
                json.dump(report.to_dict(), f, indent=2)
            print(f"? Report exported to: {output_file}")

def main():
    """Main function - demo simple validator"""
    print("=== Simple RQ1 Validator ===")
    print("Approach: Scan user ? Validate records ? Generate report")
    
    # Initialize validator
    validator = SimpleRQ1Validator(
        base_url=BaseUrl.ACCEPTANCE,  # or PRODUCTIVE
        username="DAB5HC",
        password="12021998@abcD"
    )
    
    # Scan user's records
    user_id = "DAB5HC"
    report = validator.scan_user(user_id)
    
    # Generate report
    validator.generate_report(report, output_file=f"validation_report_{user_id}.json")
    
    print("\n? Validation complete!")

if __name__ == "__main__":
    main()