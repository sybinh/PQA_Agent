#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Query My Items - Correct Approach
==================================

Based on POST Tool pattern: Query via Project reference
"""

import os
from rq1 import BaseUrl, Client
from rq1.base import reference
from rq1.project import Project
from rq1.issue import Issue
from rq1.release import Release
from rq1.workitem import Workitem
from rq1.models import ProjectProperty, IssueProperty, ReleaseProperty, WorkitemProperty


def query_project_items(project_rq1: str):
    """Query all items in a project (Issue + Release + Workitem)."""
    
    client = Client(
        base_url=BaseUrl.ACCEPTANCE,
        username='DAB5HC',
        password='12021998@abcD',
        toolname="OfficeUtils",
        toolversion="1.0"
    )
    
    print(f"\n{'='*80}")
    print(f"Querying items in Project: {project_rq1}")
    print(f"{'='*80}\n")
    
    # 1. Get Project
    print("[1] Fetching Project...")
    project = client.get_record_by_rq1_number(Project, project_rq1)
    print(f"[OK] Project: {project.dcterms__title}")
    print(f"     URI: {project.uri}\n")
    
    # 2. Query Issues belonging to this project
    print("[2] Querying Issues...")
    issue_query = client.query(
        Issue,
        where=(
            (IssueProperty.belongstoproject == reference(project.uri)) &
            (IssueProperty.lifecyclestate != "Canceled") &
            (IssueProperty.lifecyclestate != "Conflicted")
        ),
        select=[
            IssueProperty.identifier,
            IssueProperty.dcterms__title,
            IssueProperty.lifecyclestate,
            IssueProperty.category,
            IssueProperty.assignee
        ],
        page_size=100
    )
    
    issues = []
    my_issues = []
    for issue in issue_query.members:
        issues.append(issue)
        # Check if assigned to me
        if issue.assignee and hasattr(issue.assignee, 'uri'):
            assignee_str = str(issue.assignee.uri)
            if 'DAB5HC' in assignee_str:
                my_issues.append(issue)
                print(f"  ? {issue.identifier}: {issue.dcterms__title[:60]} (State: {issue.lifecyclestate})")
    
    print(f"\n[OK] Found {len(issues)} Issues total, {len(my_issues)} assigned to me\n")
    
    # 3. Query Releases belonging to this project
    print("[3] Querying Releases...")
    release_query = client.query(
        Release,
        where=(
            (ReleaseProperty.belongstoproject == reference(project.uri)) &
            (ReleaseProperty.lifecyclestate != "Canceled") &
            (ReleaseProperty.lifecyclestate != "Conflicted")
        ),
        select=[
            ReleaseProperty.identifier,
            ReleaseProperty.dcterms__title,
            ReleaseProperty.lifecyclestate,
            ReleaseProperty.category,
            ReleaseProperty.assignee
        ],
        page_size=100
    )
    
    releases = []
    my_releases = []
    for release in release_query.members:
        releases.append(release)
        if release.assignee and hasattr(release.assignee, 'uri'):
            assignee_str = str(release.assignee.uri)
            if 'DAB5HC' in assignee_str:
                my_releases.append(release)
                print(f"  ? {release.identifier}: {release.dcterms__title[:60]} (State: {release.lifecyclestate})")
    
    print(f"\n[OK] Found {len(releases)} Releases total, {len(my_releases)} assigned to me\n")
    
    # 4. Query Workitems belonging to this project
    print("[4] Querying Workitems...")
    workitem_query = client.query(
        Workitem,
        where=(
            (WorkitemProperty.belongstoproject == reference(project.uri)) &
            (WorkitemProperty.lifecyclestate != "Canceled") &
            (WorkitemProperty.lifecyclestate != "Conflicted")
        ),
        select=[
            WorkitemProperty.identifier,
            WorkitemProperty.shorttitle,
            WorkitemProperty.lifecyclestate,
            WorkitemProperty.category,
            WorkitemProperty.assignee
        ],
        page_size=100
    )
    
    workitems = []
    my_workitems = []
    for wi in workitem_query.members:
        workitems.append(wi)
        if wi.assignee and hasattr(wi.assignee, 'uri'):
            assignee_str = str(wi.assignee.uri)
            if 'DAB5HC' in assignee_str:
                my_workitems.append(wi)
                print(f"  ? {wi.identifier}: {wi.shorttitle[:60] if wi.shorttitle else 'N/A'} (State: {wi.lifecyclestate})")
    
    print(f"\n[OK] Found {len(workitems)} Workitems total, {len(my_workitems)} assigned to me\n")
    
    # Summary
    print(f"{'='*80}")
    print("SUMMARY")
    print(f"{'='*80}")
    print(f"Total items in project:  {len(issues) + len(releases) + len(workitems)}")
    print(f"  - Issues:    {len(issues)}")
    print(f"  - Releases:  {len(releases)}")
    print(f"  - Workitems: {len(workitems)}")
    print(f"\nItems assigned to me:    {len(my_issues) + len(my_releases) + len(my_workitems)}")
    print(f"  - Issues:    {len(my_issues)}")
    print(f"  - Releases:  {len(my_releases)}")
    print(f"  - Workitems: {len(my_workitems)}")
    print(f"{'='*80}\n")
    
    return {
        'all_issues': issues,
        'all_releases': releases,
        'all_workitems': workitems,
        'my_issues': my_issues,
        'my_releases': my_releases,
        'my_workitems': my_workitems
    }


if __name__ == "__main__":
    # Test v?i Project ?ã bi?t
    results = query_project_items("RQONE00001940")
