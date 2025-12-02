#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Find Projects with My Items
============================
Find all projects where I have assigned items.
"""

import os

# Skip RQ1 library tracking/telemetry (prevents timeout on import)
os.environ["CI_ENVIRONMENT"] = "1"

from rq1 import BaseUrl, Client
from rq1.base import reference
from rq1.project import Project
from rq1.issue import Issue
from rq1.release import Release
from rq1.workitem import Workitem
from rq1.users import Users
from rq1.models import ProjectProperty, IssueProperty, ReleaseProperty, WorkitemProperty, UsersProperty


def find_my_projects(target_username: str = "DAB5HC", login_username: str = "DAB5HC", login_password: str = None):
    """Find all projects where target user has assigned items.
    
    Args:
        target_username: Username to search items for
        login_username: Username to login with (default: DAB5HC)
        login_password: Password to login with (default: from env or hardcoded)
    """
    
    print(f"\n{'='*80}")
    print(f"Searching for projects with items assigned to: {target_username}")
    print(f"(Authenticated as: {login_username})")
    print(f"{'='*80}\n")
    
    # Initialize client
    if login_password is None:
        login_password = os.getenv("RQ1_PASSWORD", "12021998@abcD")
    
    client = Client(
        base_url=BaseUrl.PRODUCTIVE,
        username=login_username,
        password=login_password,
        toolname="OfficeUtils",
        toolversion="1.0"
    )
    
    # Get user's URI
    print(f"[0] Looking up user {target_username}...")
    try:
        user_query = client.query(
            Users,
            where=f'cq:login_name="{target_username}"',
            select=[UsersProperty.login_name, UsersProperty.fullname],
            page_size=1
        )
        
        if not user_query.members:
            print(f"[ERROR] User {target_username} not found in RQ1")
            return
        
        user = user_query.members[0]
        user_uri = user.uri
        print(f"[OK] Found user: {user.fullname} ({user.login_name})")
        print(f"     URI: {user_uri}\n")
        
    except Exception as e:
        print(f"[ERROR] Failed to query user: {e}")
        return
    
    # Strategy: Query all Issues/Releases/Workitems, get unique projects
    print("[1] Querying all Issues...")
    try:
        issue_query = client.query(
            Issue,
            where=(
                (IssueProperty.assignee == reference(user_uri)) &
                (IssueProperty.lifecyclestate != "Canceled") &
                (IssueProperty.lifecyclestate != "Conflicted") &
                (IssueProperty.lifecyclestate != "Closed")
            ),
            select=[
                IssueProperty.dcterms__title,
                IssueProperty.belongstoproject,
                IssueProperty.id
            ],
            page_size=200
        )
        
        my_projects = set()
        my_issues = []
        
        for issue in issue_query.members:
            my_issues.append(issue)
            # Get project info directly from belongstoproject
            if issue.belongstoproject and hasattr(issue.belongstoproject, 'uri'):
                my_projects.add(issue.belongstoproject.uri)
        
        print(f"[OK] Found {len(my_issues)} Issues assigned to me")
        for issue in my_issues:
            cq_id = getattr(issue, 'id', 'N/A')
            print(f"     - {cq_id}: {issue.dcterms__title}")
    
    except Exception as e:
        print(f"[ERROR] Failed to query Issues: {e}")
        my_projects = set()
        my_issues = []
    
    print(f"\n[2] Querying all Releases...")
    try:
        release_query = client.query(
            Release,
            where=(
                (ReleaseProperty.assignee == reference(user_uri)) &
                (ReleaseProperty.lifecyclestate != "Canceled") &
                (ReleaseProperty.lifecyclestate != "Conflicted") &
                (ReleaseProperty.lifecyclestate != "Closed")
            ),
            select=[
                ReleaseProperty.dcterms__title,
                ReleaseProperty.belongstoproject,
                ReleaseProperty.id
            ],
            page_size=200
        )
        
        my_releases = []
        
        for release in release_query.members:
            my_releases.append(release)
            # Get project info
            if release.belongstoproject and hasattr(release.belongstoproject, 'uri'):
                my_projects.add(release.belongstoproject.uri)
        
        print(f"[OK] Found {len(my_releases)} Releases assigned to me")
        for release in my_releases:
            cq_id = getattr(release, 'id', 'N/A')
            print(f"     - {cq_id}: {release.dcterms__title}")
    
    except Exception as e:
        print(f"[ERROR] Failed to query Releases: {e}")
        my_releases = []
    
    print(f"\n[3] Querying all Workitems...")
    try:
        workitem_query = client.query(
            Workitem,
            where=(
                (WorkitemProperty.assignee == reference(user_uri)) &
                (WorkitemProperty.lifecyclestate != "Canceled") &
                (WorkitemProperty.lifecyclestate != "Conflicted") &
                (WorkitemProperty.lifecyclestate != "Closed")
            ),
            select=[
                WorkitemProperty.dcterms__title,
                WorkitemProperty.belongstoproject,
                WorkitemProperty.id
            ],
            page_size=200
        )
        
        my_workitems = []
        
        for workitem in workitem_query.members:
            my_workitems.append(workitem)
            # Get project info
            if workitem.belongstoproject and hasattr(workitem.belongstoproject, 'uri'):
                my_projects.add(workitem.belongstoproject.uri)
        
        print(f"[OK] Found {len(my_workitems)} Workitems assigned to me")
        for workitem in my_workitems:
            cq_id = getattr(workitem, 'id', 'N/A')
            print(f"     - {cq_id}: {workitem.dcterms__title}")
    
    except Exception as e:
        print(f"[ERROR] Failed to query Workitems: {e}")
        my_workitems = []
    
    # Print summary
    print(f"\n{'='*80}")
    print(f"SUMMARY")
    print(f"{'='*80}")
    print(f"Total items assigned to me: {len(my_issues) + len(my_releases) + len(my_workitems)}")
    print(f"  - Issues:    {len(my_issues)}")
    print(f"  - Releases:  {len(my_releases)}")
    print(f"  - Workitems: {len(my_workitems)}")
    print(f"\nUnique projects: {len(my_projects)}")
    
    # # Fetch project details (batch mode to avoid timeout)
    # if my_projects:
    #     print(f"\n{'='*80}")
    #     print(f"PROJECTS WITH MY ITEMS")
    #     print(f"{'='*80}")
    #     
    #     # Collect unique project identifiers from items
    #     project_ids = {}
    #     for issue in my_issues[:50]:  # Sample to get project IDs
    #         if hasattr(issue, 'belongstoproject') and issue.belongstoproject:
    #             uri = issue.belongstoproject.uri if hasattr(issue.belongstoproject, 'uri') else None
    #             if uri and uri not in project_ids:
    #                 # Try to get identifier from Resource if available
    #                 if hasattr(issue.belongstoproject, 'identifier'):
    #                     project_ids[uri] = issue.belongstoproject.identifier
    #     
    #     for release in my_releases[:50]:
    #         if hasattr(release, 'belongstoproject') and release.belongstoproject:
    #             uri = release.belongstoproject.uri if hasattr(release.belongstoproject, 'uri') else None
    #             if uri and uri not in project_ids:
    #                 if hasattr(release.belongstoproject, 'identifier'):
    #                     project_ids[uri] = release.belongstoproject.identifier
    #     
    #     for workitem in my_workitems[:50]:
    #         if hasattr(workitem, 'belongstoproject') and workitem.belongstoproject:
    #             uri = workitem.belongstoproject.uri if hasattr(workitem.belongstoproject, 'uri') else None
    #             if uri and uri not in project_ids:
    #                 if hasattr(workitem.belongstoproject, 'identifier'):
    #                     project_ids[uri] = workitem.belongstoproject.identifier
    #     
    #     # If identifiers not embedded, fetch them
    #     if len(project_ids) < len(my_projects):
    #         print(f"\nFetching {len(my_projects)} project details...")
    #         for uri in my_projects:
    #             if uri not in project_ids:
    #                 try:
    #                     proj = client.get_record_by_uri(Project, uri)
    #                     project_ids[uri] = proj.identifier
    #                 except:
    #                     project_ids[uri] = uri.split('/')[-1]  # Fallback to URI part
    #     
    #     # Display sorted
    #     sorted_projects = sorted(project_ids.values())
    #     for i, proj_id in enumerate(sorted_projects, 1):
    #         print(f"[{i}] {proj_id}")
    
    print(f"\n{'='*80}\n")


if __name__ == "__main__":
    users = ["DBA2HC", "DAB5HC", "TYH5HC", "GNK7HC", "IOH81HC"]
    
    for user in users:
        find_my_projects(target_username=user, login_username="DAB5HC")
        print("\n")  # Extra spacing between users
