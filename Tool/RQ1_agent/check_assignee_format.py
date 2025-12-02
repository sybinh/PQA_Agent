#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Check Assignee Format
=====================
Sample some Issues/Releases/Workitems to see assignee format.
"""

import os
from rq1 import BaseUrl, Client
from rq1.issue import Issue
from rq1.release import Release
from rq1.workitem import Workitem
from rq1.users import Users
from rq1.models import IssueProperty, ReleaseProperty, WorkitemProperty


def check_assignee_format():
    """Check how assignee data looks in RQ1."""
    
    print(f"\n{'='*80}")
    print(f"Checking assignee format in RQ1 records")
    print(f"{'='*80}\n")
    
    # Initialize client
    client = Client(
        base_url=BaseUrl.PRODUCTIVE,
        username="DAB5HC",
        password="12021998@abcD",
        toolname="OfficeUtils",
        toolversion="1.0"
    )
    
    print("[INFO] Fetching 1 user to understand structure...")
    # Get first Issue with assignee
    issue_query = client.query(
        Issue,
        where=IssueProperty.lifecyclestate != "Canceled",
        select=[
            IssueProperty.identifier,
            IssueProperty.assignee
        ],
        page_size=1
    )
    
    if issue_query.members and issue_query.members[0].assignee:
        assignee_uri = issue_query.members[0].assignee.uri
        print(f"Found assignee URI: {assignee_uri}")
        
        # Fetch Users record
        try:
            user = client.get_record_by_uri(Users, assignee_uri)
            print(f"\nUsers record structure:")
            print(f"  Type: {type(user)}")
            print(f"  Dir: {[a for a in dir(user) if not a.startswith('_')]}")
            print(f"\nUser details:")
            if hasattr(user, 'identifier'):
                print(f"  identifier: {user.identifier}")
            if hasattr(user, 'login_name'):
                print(f"  login_name: {user.login_name}")
            if hasattr(user, 'email'):
                print(f"  email: {user.email}")
            if hasattr(user, 'fullname'):
                print(f"  fullname: {user.fullname}")
            if hasattr(user, 'dcterms__title'):
                print(f"  dcterms__title: {user.dcterms__title}")
        except Exception as e:
            print(f"[ERROR] Failed to fetch Users record: {e}")
    
    print(f"\n{'='*80}\n")


if __name__ == "__main__":
    check_assignee_format()
