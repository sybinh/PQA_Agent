#!/usr/bin/env python3
"""
Smart RQ1 Validator - Project-based validation
===============================================

Validates all RQ1 items in a project by scanning Issues and Releases.

Usage:
------
python smart_validator_v2.py RQONE00001940

The validator will:
1. Fetch Project details
2. Query all Issues in project
3. Query all Releases in project  
4. Identify subtypes (IFD/PVER/BC/etc)
5. Apply appropriate PRPL/QAWP rules
6. Generate validation report
"""

import os
import sys
from dataclasses import dataclass
from typing import List, Dict, Any, Optional
from datetime import datetime

from rq1 import BaseUrl, Client
from rq1.project import Project
from rq1.issue import Issue
from rq1.release import Release
from rq1.models import IssueProperty, ReleaseProperty, ProjectProperty

sys.path.insert(0, 'src')
from rule_registry import get_registry


@dataclass
class WorkitemInfo:
    """Information about an RQ1 workitem to validate."""
    rq1_number: str
    title: str
    item_type: str  # "Issue" or "Release"
    subtype: str  # "IFD", "PVER", "BC", "FC", etc.
    state: str
    data: Dict[str, Any]  # Full record data


class SmartValidator:
    """
    Smart validator that scans all RQ1 items in a project.
    """
    
    # Rule mapping by subtype
    ISSUE_RULES = {
        'IFD': ['PRPL 02.00.00', 'PRPL 12.00.00', 'PRPL 13.00.00', 'PRPL 16.00.00'],
        'PVER': ['PRPL 02.00.00', 'PRPL 15.00.00', 'PRPL 16.00.00'],
        'default': ['PRPL 02.00.00', 'PRPL 16.00.00']
    }
    
    RELEASE_RULES = {
        'BC': ['PRPL 02.00.00', 'PRPL 07.00.00', 'PRPL 15.00.00', 'PRPL 16.00.00'],
        'FC': ['PRPL 02.00.00', 'PRPL 15.00.00', 'PRPL 16.00.00'],
        'PST': ['PRPL 02.00.00', 'PRPL 07.00.00', 'PRPL 16.00.00'],
        'default': ['PRPL 02.00.00', 'PRPL 16.00.00']
    }
    
    def __init__(self, rq1_client: Client, project_rq1: str):
        """
        Args:
            rq1_client: RQ1 Client instance
            project_rq1: Project RQ1 number (e.g., "RQONE00001940")
        """
        self.client = rq1_client
        self.project_rq1 = project_rq1
        self.project = None
        self.registry = get_registry()
        self.workitems: List[WorkitemInfo] = []
        self.results = {
            'total_items': 0,
            'issues': 0,
            'releases': 0,
            'violations': [],
            'passed': []
        }
    
    def load_project(self) -> None:
        """Load project details."""
        print(f"\n[STEP 1] Loading project {self.project_rq1}...")
        
        try:
            self.project = self.client.get_record_by_rq1_number(Project, self.project_rq1)
            print(f"[OK] Project: {self.project.dcterms__title}")
            print(f"     State: {self.project.lifecyclestate}")
            
        except Exception as e:
            print(f"[ERROR] Failed to load project: {e}")
            raise
    
    def scan_issues(self) -> None:
        """
        Scan all Issues in project.
        
        Note: building-block-rq1 doesn't support relationship queries yet.
        For now, we'll fetch a sample of recent issues and filter by project manually.
        """
        print(f"\n[STEP 2] Scanning Issues in project...")
        print(f"[WARN] Relationship queries not yet supported in building-block-rq1")
        print(f"[INFO] Fetching recent issues (will filter by project manually)...")
        
        try:
            # Query recent issues (no project filter - API limitation)
            query_result = self.client.query(
                Issue,
                where=None,  # Can't filter by project in OSLC query
                select=[
                    IssueProperty.id,
                    IssueProperty.dcterms__title,
                    IssueProperty.dcterms__type,
                    IssueProperty.lifecyclestate,
                    IssueProperty.submitdate
                ],
                page_size=50  # Fetch sample
            )
            
            issues = query_result.members
            print(f"[INFO] Fetched {len(issues)} recent issues from RQ1")
            
            # TODO: Filter by project relationship
            # This requires relationship query support in building-block-rq1
            # For now, we'll process all fetched issues
            
            for issue in issues:
                # Skip canceled items
                if issue.lifecyclestate in ('Canceled', 'Cancelled'):
                    continue
                    
                workitem = WorkitemInfo(
                    rq1_number=issue.id,
                    title=issue.dcterms__title,
                    item_type="Issue",
                    subtype=self._identify_issue_subtype(issue),
                    state=issue.lifecyclestate,
                    data={
                        'id': issue.id,
                        'dcterms__title': issue.dcterms__title,
                        'dcterms__type': issue.dcterms__type or 'Issue',
                        'lifecyclestate': issue.lifecyclestate,
                        'submitdate': issue.submitdate
                    }
                )
                self.workitems.append(workitem)
            
            self.results['issues'] = len([w for w in self.workitems if w.item_type == 'Issue'])
            print(f"[OK] Found {self.results['issues']} Issues")
            
        except Exception as e:
            print(f"[ERROR] Failed to scan issues: {e}")
            import traceback
            traceback.print_exc()
    
    def scan_releases(self) -> None:
        """
        Scan all Releases in project.
        
        Note: Same limitation as scan_issues - no relationship query support yet.
        """
        print(f"\n[STEP 3] Scanning Releases in project...")
        print(f"[INFO] Fetching recent releases (will filter by project manually)...")
        
        try:
            # Query recent releases
            query_result = self.client.query(
                Release,
                where=None,
                select=[
                    ReleaseProperty.id,
                    ReleaseProperty.dcterms__title,
                    ReleaseProperty.dcterms__type,
                    ReleaseProperty.lifecyclestate,
                    ReleaseProperty.planneddate,
                    ReleaseProperty.actualdate
                ],
                page_size=50
            )
            
            releases = query_result.members
            print(f"[INFO] Fetched {len(releases)} recent releases from RQ1")
            
            for release in releases:
                # Skip canceled items
                if release.lifecyclestate in ('Canceled', 'Cancelled'):
                    continue
                    
                workitem = WorkitemInfo(
                    rq1_number=release.id,
                    title=release.dcterms__title,
                    item_type="Release",
                    subtype=self._identify_release_subtype(release),
                    state=release.lifecyclestate,
                    data={
                        'id': release.id,
                        'dcterms__title': release.dcterms__title,
                        'dcterms__type': 'Release',
                        'lifecyclestate': release.lifecyclestate,
                        'planneddate': getattr(release, 'planneddate', None),
                        'actualdate': getattr(release, 'actualdate', None)
                    }
                )
                self.workitems.append(workitem)
            
            self.results['releases'] = len([w for w in self.workitems if w.item_type == 'Release'])
            print(f"[OK] Found {self.results['releases']} Releases")
            
        except Exception as e:
            print(f"[ERROR] Failed to scan releases: {e}")
            import traceback
            traceback.print_exc()
    
    def _identify_issue_subtype(self, issue) -> str:
        """Identify Issue subtype from title or properties."""
        title = issue.dcterms__title.upper() if issue.dcterms__title else ""
        
        if "IFD" in title or "[IFD]" in title:
            return "IFD"
        elif "PVER" in title or "[PVER]" in title:
            return "PVER"
        else:
            return "Issue"
    
    def _identify_release_subtype(self, release) -> str:
        """Identify Release subtype from title."""
        title = release.dcterms__title.upper() if release.dcterms__title else ""
        
        if "BC" in title or "BC:" in title or "BC :" in title:
            return "BC"
        elif "FC" in title or "FC:" in title or "FC :" in title:
            return "FC"
        elif "PST" in title or "PST:" in title:
            return "PST"
        else:
            return "Release"
    
    def validate_all(self) -> None:
        """Validate all workitems with appropriate rules."""
        print(f"\n[STEP 4] Validating {len(self.workitems)} workitems...")
        
        for workitem in self.workitems:
            self._validate_workitem(workitem)
        
        self.results['total_items'] = len(self.workitems)
    
    def _validate_workitem(self, workitem: WorkitemInfo) -> None:
        """Validate a single workitem with appropriate rules."""
        # Select rules based on type and subtype
        if workitem.item_type == "Issue":
            rule_ids = self.ISSUE_RULES.get(workitem.subtype, self.ISSUE_RULES['default'])
        else:
            rule_ids = self.RELEASE_RULES.get(workitem.subtype, self.RELEASE_RULES['default'])
        
        print(f"\n  [{workitem.rq1_number}] {workitem.subtype} - {workitem.state}")
        print(f"    Applying {len(rule_ids)} rules...")
        
        for rule_id in rule_ids:
            try:
                result = self.registry.execute_rule(rule_id, workitem.data)
                
                # Convert ValidationResult to dict-like access
                if hasattr(result, 'passed'):
                    status = 'PASS' if result.passed else 'VIOLATION'
                    message = result.description if hasattr(result, 'description') else str(result)
                    severity = result.severity if hasattr(result, 'severity') else 'MEDIUM'
                else:
                    # Fallback for dict-like results
                    status = result.get('status', 'UNKNOWN')
                    message = result.get('message', '')
                    severity = result.get('severity', 'MEDIUM')
                
                if status == 'PASS':
                    self.results['passed'].append({
                        'workitem': workitem.rq1_number,
                        'rule': rule_id,
                        'message': message
                    })
                    print(f"      ? {rule_id}: PASS")
                else:
                    self.results['violations'].append({
                        'workitem': workitem.rq1_number,
                        'rule': rule_id,
                        'message': message,
                        'severity': severity
                    })
                    print(f"      ? {rule_id}: {status}")
                    if message:
                        print(f"        {message}")
                    
            except Exception as e:
                print(f"      ! {rule_id}: ERROR - {e}")
    
    def print_report(self) -> None:
        """Print validation report."""
        print("\n" + "=" * 80)
        print("VALIDATION REPORT")
        print("=" * 80)
        print(f"\nProject: {self.project.dcterms__title if self.project else self.project_rq1}")
        print(f"Scanned: {self.results['total_items']} workitems")
        print(f"  Issues: {self.results['issues']}")
        print(f"  Releases: {self.results['releases']}")
        print(f"\nTotal validation checks: {len(self.results['passed']) + len(self.results['violations'])}")
        print(f"  ? Passed: {len(self.results['passed'])}")
        print(f"  ? Violations: {len(self.results['violations'])}")
        
        if self.results['violations']:
            print(f"\n{'?' * 80}")
            print("VIOLATIONS FOUND:")
            print(f"{'?' * 80}")
            for v in self.results['violations']:
                print(f"\n[{v['workitem']}] {v['rule']}")
                print(f"  {v['message']}")
        else:
            print(f"\n? No violations found - all workitems compliant!")


def main():
    if len(sys.argv) != 2:
        print("Usage: python smart_validator_v2.py <PROJECT_RQ1>")
        print("Example: python smart_validator_v2.py RQONE00001940")
        sys.exit(1)
    
    project_rq1 = sys.argv[1]
    
    # Credentials
    RQ1_USER = "DAB5HC"
    RQ1_PASSWORD = "12021998@abcD"
    RQ1_TOOLNAME = "OfficeUtils"  # Same as POST Tool (whitelisted in RQ1)
    RQ1_TOOLVERSION = "1.0"
    
    # Connect to RQ1
    print("Connecting to RQ1 PRODUCTIVE...")
    client = Client(
        base_url=BaseUrl.PRODUCTIVE,
        username=RQ1_USER,
        password=RQ1_PASSWORD,
        toolname=RQ1_TOOLNAME,
        toolversion=RQ1_TOOLVERSION
    )
    print("[OK] Connected to RQ1 PRODUCTIVE")
    
    # Create validator
    print("\n" + "=" * 80)
    print(f"Smart RQ1 Validator - Project: {project_rq1}")
    print("=" * 80)
    
    validator = SmartValidator(client, project_rq1)
    
    # Execute validation workflow
    validator.load_project()
    validator.scan_issues()
    validator.scan_releases()
    validator.validate_all()
    validator.print_report()


if __name__ == '__main__':
    main()
