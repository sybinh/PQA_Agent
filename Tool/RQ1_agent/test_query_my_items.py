#!/usr/bin/env python3
"""
Test: Query all RQ1 items assigned to current user
==================================================

This script queries ALL record types that can be assigned to a user:
- Issues (IFD, ISW)
- Releases (PVER, BC, FC)
- Workitems
- Problems
"""

import os
from rq1 import BaseUrl, Client
from rq1.issue import Issue
from rq1.release import Release
from rq1.workitem import Workitem
from rq1.problem import Problem
from rq1.models import IssueProperty, ReleaseProperty, WorkitemProperty, ProblemProperty

def query_my_items():
    """Query all items assigned to current user."""
    
    # Initialize client
    username = os.getenv('RQ1_USER', 'DAB5HC')
    password = os.getenv('RQ1_PASSWORD', '12021998@abcD')
    
    client = Client(
        base_url=BaseUrl.ACCEPTANCE,
        username=username,
        password=password,
        toolname="OfficeUtils",
        toolversion="1.0"
    )
    print(f"\n{'='*80}")
    print(f"Querying all items assigned to: {username}")
    print(f"{'='*80}\n")
    
    results = {
        'issues': [],
        'releases': [],
        'workitems': [],
        'problems': []
    }
    
    # 1. Query Issues assigned to me
    print("\n[1] Querying Issues...")
    try:
        # Simpler query - get all Issues in a state, then filter by assignee
        issue_query = client.query(
            Issue,
            where='cq:LifeCycleState!="Canceled"',
            select=[
                IssueProperty.identifier,
                IssueProperty.dcterms__title,
                IssueProperty.lifecyclestate,
                IssueProperty.assignee
            ],
            page_size=100
        )
        
        count = 0
        for issue in issue_query.members:
            # Check if assignee matches (assignee is a Resource object)
            if issue.assignee and hasattr(issue.assignee, 'uri'):
                # Try to extract username from URI or check directly
                assignee_str = str(issue.assignee.uri) if issue.assignee.uri else ""
                if username in assignee_str or (hasattr(issue.assignee, 'login_name') and issue.assignee.login_name == username):
                    results['issues'].append({
                        'rq1': issue.identifier,
                        'title': issue.dcterms__title,
                        'state': issue.lifecyclestate
                    })
                    print(f"  ? {issue.identifier}: {issue.dcterms__title[:60] if issue.dcterms__title else 'N/A'}")
                    count += 1
        
        print(f"\n[OK] Found {count} Issues assigned to you")
        
    except Exception as e:
        print(f"[ERROR] Failed to query Issues: {e}")
    
    # 2. Query Releases assigned to me
    print("\n[2] Querying Releases...")
    try:
        release_query = client.query(
            Release,
            where=f'cq:assignee/cq:login_name="{username}"',
            select=[
                ReleaseProperty.identifier,
                ReleaseProperty.dcterms__title,
                ReleaseProperty.lifecyclestate,
                ReleaseProperty.assignee
            ],
            page_size=50
        )
        
        for release in release_query.members:
            results['releases'].append({
                'rq1': release.identifier,
                'title': release.dcterms__title,
                'state': release.lifecyclestate
            })
            print(f"  ? {release.identifier}: {release.dcterms__title[:60] if release.dcterms__title else 'N/A'}")
        
        print(f"\n[OK] Found {len(results['releases'])} Releases")
        
    except Exception as e:
        print(f"[ERROR] Failed to query Releases: {e}")
    
    # 3. Query Workitems assigned to me
    print("\n[3] Querying Workitems...")
    try:
        workitem_query = client.query(
            Workitem,
            where=f'cq:assignee/cq:login_name="{username}"',
            select=[
                WorkitemProperty.identifier,
                WorkitemProperty.shorttitle,
                WorkitemProperty.lifecyclestate,
                WorkitemProperty.category,
                WorkitemProperty.assignee
            ],
            page_size=50
        )
        
        for wi in workitem_query.members:
            results['workitems'].append({
                'rq1': wi.identifier,
                'title': wi.shorttitle,
                'state': wi.lifecyclestate,
                'category': wi.category
            })
            print(f"  ? {wi.identifier}: {wi.shorttitle[:60] if wi.shorttitle else 'N/A'}")
        
        print(f"\n[OK] Found {len(results['workitems'])} Workitems")
        
    except Exception as e:
        print(f"[ERROR] Failed to query Workitems: {e}")
    
    # 4. Query Problems assigned to me
    print("\n[4] Querying Problems...")
    try:
        problem_query = client.query(
            Problem,
            where=f'cq:assignee/cq:login_name="{username}"',
            select=[
                ProblemProperty.identifier,
                ProblemProperty.shorttitle,
                ProblemProperty.state,
                ProblemProperty.assignee
            ],
            page_size=50
        )
        
        for prob in problem_query.members:
            results['problems'].append({
                'rq1': prob.identifier,
                'title': prob.shorttitle,
                'state': prob.state
            })
            print(f"  ? {prob.identifier}: {prob.shorttitle[:60] if prob.shorttitle else 'N/A'}")
        
        print(f"\n[OK] Found {len(results['problems'])} Problems")
        
    except Exception as e:
        print(f"[ERROR] Failed to query Problems: {e}")
    
    # Summary
    print(f"\n{'='*80}")
    print("SUMMARY")
    print(f"{'='*80}")
    print(f"Issues:    {len(results['issues'])}")
    print(f"Releases:  {len(results['releases'])}")
    print(f"Workitems: {len(results['workitems'])}")
    print(f"Problems:  {len(results['problems'])}")
    print(f"{'='*80}")
    print(f"TOTAL:     {sum(len(v) for v in results.values())} items assigned to you")
    print(f"{'='*80}\n")
    
    return results


if __name__ == "__main__":
    results = query_my_items()
