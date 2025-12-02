#!/usr/bin/env python
"""
Smart RQ1 Validator - Scan all items assigned to user

Automatically:
1. Fetch all RQ1 items assigned to user (Issues, Releases, etc.)
2. Identify item type and properties
3. Apply appropriate validation rules based on type
4. Generate comprehensive validation report
"""

from rq1 import Client, BaseUrl
from rq1.models import Issue, IssueProperty, Release, ReleaseProperty
from typing import List, Dict, Any, Optional
from dataclasses import dataclass
from datetime import datetime
import sys

sys.path.insert(0, 'src')
from rule_registry import get_registry, RuleCategory


@dataclass
class WorkitemInfo:
    """Information about a workitem"""
    rq1_number: str
    title: str
    item_type: str  # "Issue", "Release"
    subtype: Optional[str]  # "BC", "FC", "IFD", "PVER", etc.
    state: str
    assignee: Optional[str]
    data: dict  # Full data for validation


class SmartValidator:
    """
    Smart validator that automatically determines which rules to apply
    based on workitem type and properties.
    """
    
    # Rule mapping by item type
    ISSUE_RULES = [
        "PRPL 02.00.00",  # Workitem started without planned date
        "PRPL 16.00.00",  # Workitem not closed after planned date
        "PRPL 12.00.00",  # IFD not closed when all BCs closed
        "PRPL 13.00.00",  # IFD not implemented after BC planned date
    ]
    
    RELEASE_RULES = [
        "PRPL 15.00.00",  # BC/FC not closed after planned date
        "PRPL 02.00.00",  # Workitem started without planned date
        "PRPL 16.00.00",  # Workitem not closed after planned date
        "PRPL 07.00.00",  # BC planned date vs PVER delivery date
    ]
    
    def __init__(self, rq1_client: Client, username: str):
        """
        Args:
            rq1_client: RQ1 Client instance
            username: User ID to scan (e.g., "DAB5HC")
        """
        self.client = rq1_client
        self.username = username
        self.registry = get_registry()
        self.results = {
            'total_items': 0,
            'issues': [],
            'releases': [],
            'violations': [],
            'passed': []
        }
    
    def fetch_user_issues(self) -> List[WorkitemInfo]:
        """Fetch all Issues assigned to user."""
        print(f"\n[STEP 1] Fetching Issues...")
        
        try:
            # Query recent Issues (no filter, just fetch some)
            query_result = self.client.query(
                Issue,
                where=None,  # Fetch all
                select=[
                    IssueProperty.id,
                    IssueProperty.dcterms__title,
                    IssueProperty.dcterms__type,
                    IssueProperty.lifecyclestate,
                    IssueProperty.submitdate
                ],
                page_size=10  # Small batch
            )
            
            issues = query_result.items
            
            workitems = []
            for issue in issues:
                workitem = WorkitemInfo(
                    rq1_number=issue.id,
                    title=issue.dcterms__title,
                    item_type="Issue",
                    subtype=self._identify_issue_subtype(issue),
                    state=issue.lifecyclestate,
                    assignee=None,  # Not available in query
                    data={
                        'id': issue.id,
                        'dcterms__title': issue.dcterms__title,
                        'dcterms__type': issue.dcterms__type or 'Issue',
                        'lifecyclestate': issue.lifecyclestate,
                        'submitdate': issue.submitdate
                    }
                )
                workitems.append(workitem)
            
            print(f"[OK] Found {len(workitems)} Issues")
            return workitems
            
        except Exception as e:
            print(f"[ERROR] Failed to fetch Issues: {e}")
            import traceback
            traceback.print_exc()
            return []
    
    def fetch_user_releases(self) -> List[WorkitemInfo]:
        """Fetch all Releases assigned to user."""
        print(f"\n[STEP 2] Fetching Releases...")
        
        try:
            # Query recent Releases
            query_result = self.client.query(
                Release,
                where=None,  # Fetch all
                select=[
                    ReleaseProperty.id,
                    ReleaseProperty.dcterms__title,
                    ReleaseProperty.dcterms__type,
                    ReleaseProperty.lifecyclestate,
                    ReleaseProperty.planneddate,
                    ReleaseProperty.actualdate
                ],
                page_size=10  # Small batch
            )
            
            releases = query_result.items
            
            workitems = []
            for release in releases:
                workitem = WorkitemInfo(
                    rq1_number=release.id,
                    title=release.dcterms__title,
                    item_type="Release",
                    subtype=self._identify_release_subtype(release),
                    state=release.lifecyclestate,
                    assignee=None,
                    data={
                        'id': release.id,
                        'dcterms__title': release.dcterms__title,
                        'dcterms__type': 'Release',
                        'lifecyclestate': release.lifecyclestate,
                        'planneddate': getattr(release, 'planneddate', None),
                        'actualdate': getattr(release, 'actualdate', None)
                    }
                )
                workitems.append(workitem)
            
            print(f"[OK] Found {len(workitems)} Releases")
            return workitems
            
        except Exception as e:
            print(f"[ERROR] Failed to fetch Releases: {e}")
            import traceback
            traceback.print_exc()
            return []
    
    def _identify_issue_subtype(self, issue) -> str:
        """Identify Issue subtype (IFD, Issue-SW, Bug, etc.)"""
        title = issue.dcterms__title.upper()
        
        if 'IFD' in title or 'I-FD' in title:
            return 'IFD'
        elif 'I-SW' in title or 'ISSUE-SW' in title:
            return 'Issue-SW'
        elif 'BUG' in title or 'BUGFIX' in title:
            return 'Bug'
        else:
            return 'Issue'
    
    def _identify_release_subtype(self, release) -> str:
        """Identify Release subtype (BC, FC, PVER, PVAR, etc.)"""
        title = release.dcterms__title.upper()
        
        if 'BC' in title[:20]:  # BC usually at start
            return 'BC'
        elif 'FC' in title[:20]:
            return 'FC'
        elif 'PVER' in title:
            return 'PVER'
        elif 'PVAR' in title:
            return 'PVAR'
        else:
            return 'Release'
    
    def validate_workitem(self, workitem: WorkitemInfo) -> List[Dict]:
        """
        Validate a workitem with appropriate rules.
        
        Returns:
            List of validation results
        """
        # Select rules based on item type
        if workitem.item_type == "Issue":
            applicable_rules = self.ISSUE_RULES
        elif workitem.item_type == "Release":
            applicable_rules = self.RELEASE_RULES
        else:
            applicable_rules = []
        
        results = []
        
        for rule_id in applicable_rules:
            try:
                # Get rule metadata
                rule_meta = self.registry.get_rule(rule_id)
                
                if not rule_meta:
                    continue
                
                # Skip rules that require relationships (for now)
                # We'll implement relationship fetching later
                if rule_meta.requires_relationships:
                    continue
                
                # Execute rule
                result = self.registry.execute_rule(rule_id, workitem.data)
                
                results.append({
                    'rule_id': rule_id,
                    'workitem': workitem.rq1_number,
                    'passed': result.passed,
                    'severity': result.severity,
                    'title': result.title,
                    'description': result.description
                })
                
            except Exception as e:
                print(f"[WARN] Rule {rule_id} failed for {workitem.rq1_number}: {e}")
        
        return results
    
    def scan_user(self) -> Dict:
        """
        Scan all workitems assigned to user and validate.
        
        Returns:
            Validation report
        """
        print("="*80)
        print(f"Smart RQ1 Validator - Scanning user: {self.username}")
        print("="*80)
        
        # Fetch all workitems
        issues = self.fetch_user_issues()
        releases = self.fetch_user_releases()
        
        all_workitems = issues + releases
        
        if not all_workitems:
            print("\n[WARN] No workitems found for user")
            return self.results
        
        print(f"\n[STEP 3] Found {len(all_workitems)} total workitems:")
        print(f"  Issues: {len(issues)}")
        print(f"  Releases: {len(releases)}")
        
        # Validate each workitem
        print(f"\n[STEP 4] Validating workitems...")
        
        for i, workitem in enumerate(all_workitems, 1):
            print(f"\n[{i}/{len(all_workitems)}] {workitem.rq1_number} ({workitem.subtype})")
            print(f"  Title: {workitem.title[:60]}...")
            print(f"  State: {workitem.state}")
            
            validation_results = self.validate_workitem(workitem)
            
            # Categorize results
            violations_found = False
            for result in validation_results:
                if result['passed']:
                    self.results['passed'].append(result)
                else:
                    self.results['violations'].append(result)
                    violations_found = True
                    print(f"    ? {result['rule_id']}: {result['title']}")
            
            if not violations_found:
                print(f"    ? All rules passed")
            
            # Track by type
            if workitem.item_type == "Issue":
                self.results['issues'].append(workitem)
            else:
                self.results['releases'].append(workitem)
        
        self.results['total_items'] = len(all_workitems)
        
        return self.results
    
    def print_report(self):
        """Print validation report."""
        print("\n" + "="*80)
        print("VALIDATION REPORT")
        print("="*80)
        
        print(f"\nScanned: {self.results['total_items']} workitems")
        print(f"  Issues: {len(self.results['issues'])}")
        print(f"  Releases: {len(self.results['releases'])}")
        
        total_checks = len(self.results['passed']) + len(self.results['violations'])
        print(f"\nTotal validation checks: {total_checks}")
        print(f"  ? Passed: {len(self.results['passed'])}")
        print(f"  ? Violations: {len(self.results['violations'])}")
        
        if self.results['violations']:
            print("\n" + "="*80)
            print("VIOLATIONS FOUND")
            print("="*80)
            
            # Group by workitem
            violations_by_item = {}
            for v in self.results['violations']:
                item = v['workitem']
                if item not in violations_by_item:
                    violations_by_item[item] = []
                violations_by_item[item].append(v)
            
            for item, violations in violations_by_item.items():
                print(f"\n{item} - {len(violations)} violation(s):")
                for v in violations:
                    print(f"  ? {v['rule_id']}")
                    print(f"    {v['title']}")
        else:
            print("\n? No violations found - all workitems compliant!")


def main():
    """Main entry point."""
    
    # Connect to RQ1
    try:
        client = Client(
            base_url=BaseUrl.PRODUCTIVE,
            username="DAB5HC",
            password="12021998@abcD",
            toolname="OfficeUtils",
            toolversion="1.0"
        )
        print("[OK] Connected to RQ1 PRODUCTIVE")
    except Exception as e:
        print(f"[ERROR] Connection failed: {e}")
        sys.exit(1)
    
    # Create validator and scan
    validator = SmartValidator(client, username="DAB5HC")
    validator.scan_user()
    validator.print_report()


if __name__ == "__main__":
    main()
