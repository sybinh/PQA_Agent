#!/usr/bin/env python
"""Test building-block-rq1 with real RQ1 connection"""
from rq1 import Client, BaseUrl
from rq1.models import Issue, IssueProperty, Users, UsersProperty
from rq1.base import reference

# Initialize client
client = Client(
    base_url=BaseUrl.ACCEPTANCE,
    username="DAB5HC",
    password="12021998@abcD",
    toolname="OfficeUtils",
    toolversion="1.0"
)

print("Fetching issue RQONE03999302...")
try:
    # Get issue with select properties
    issue = client.get_record_by_rq1_number(
        Issue,
        "RQONE03999302",
        select=[
            IssueProperty.id,
            IssueProperty.dcterms__title,
            IssueProperty.lifecyclestate,
            IssueProperty.category,
            IssueProperty.domain,
            IssueProperty.assignee
        ]
    )
    
    print(f"\n? Successfully fetched issue!")
    print(f"  ID: {issue.id}")
    print(f"  Title: {issue.dcterms__title[:80]}...")
    print(f"  State: {issue.lifecyclestate}")
    print(f"  Category: {issue.category}")
    print(f"  Domain: {issue.domain}")
    print(f"\n? building-block-rq1 working perfectly with RQ1!")
    
    # Test 2: Query all issues assigned to DAB5HC
    print(f"\n{'='*80}")
    print("Test 2: Query issues assigned to DAB5HC")
    print('='*80)
    
    # First, get user URI for DAB5HC
    user_query = client.query(Users, where=UsersProperty.login_name == "DAB5HC")
    if user_query.total_count > 0:
        user = user_query.members[0]
        
        # Query issues assigned to this user
        issue_query = client.query(
            Issue,
            where=IssueProperty.assignee == reference(user.uri),
            select=[
                IssueProperty.id,
                IssueProperty.dcterms__title,
                IssueProperty.lifecyclestate,
                IssueProperty.category,
                IssueProperty.domain
            ],
            paging=True,
            page_size=10
        )
        
        print(f"\n? Found {issue_query.total_count} total issues assigned to DAB5HC")
        print(f"  Showing first {len(issue_query.members)} issues:\n")
        
        for i, issue in enumerate(issue_query.members, 1):
            print(f"{i}. {issue.id}")
            print(f"   Title: {issue.dcterms__title[:70]}...")
            print(f"   State: {issue.lifecyclestate}")
            print(f"   Category: {issue.category}")
            print(f"   Domain: {issue.domain}")
            print()
    else:
        print(f"\n? User DAB5HC not found")
    
except Exception as e:
    print(f"\n? Error: {e}")
    import traceback
    traceback.print_exc()
