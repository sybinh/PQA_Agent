#!/usr/bin/env python
"""
Test PRPL 12.00.00 with real IFD: RQONE04736888

PRPL 12.00.00: IFD not closed when all mapped BCs are closed/cancelled
"""

from rq1 import Client, BaseUrl
from rq1.models import Issue, IssueProperty, Release, ReleaseProperty
import sys

print("="*80)
print("Testing PRPL 12.00.00: IFD not closed when all BCs closed")
print("Testing with IFD RQONE04736888")
print("="*80)

# Initialize RQ1 client
try:
    client = Client(
        base_url=BaseUrl.PRODUCTIVE,
        username="DAB5HC",
        password="12021998@abcD",
        toolname="OfficeUtils",
        toolversion="1.0"
    )
    print("[OK] Connected to PRODUCTIVE")
except Exception as e:
    print(f"[ERROR] Connection failed: {e}")
    sys.exit(1)

# Fetch the IFD
print("\n[STEP 1] Fetching IFD RQONE04736888...")

try:
    ifd = client.get_record_by_rq1_number(
        Issue,
        "RQONE04736888",
        select=[
            IssueProperty.id,
            IssueProperty.dcterms__title,
            IssueProperty.dcterms__type,
            IssueProperty.lifecyclestate,
            IssueProperty.submitdate
        ]
    )
    
    print(f"[OK] Found IFD:")
    print(f"  ID: {ifd.id}")
    print(f"  Title: {ifd.dcterms__title[:80]}...")
    print(f"  Type: {ifd.dcterms__type}")
    print(f"  State: {ifd.lifecyclestate}")
    print(f"  Submit Date: {ifd.submitdate}")
    
    # Query for mapped BC-Releases
    print("\n[STEP 2] Testing PRPL 12.00.00 rule...")
    
    # IFD is already Closed, so test with different scenarios
    print(f"  IFD is in '{ifd.lifecyclestate}' state")
    
    # Import rule
    sys.path.insert(0, 'src')
    from rule_registry import get_registry
    
    registry = get_registry()
    
    ifd_data = {
        'id': ifd.id,
        'dcterms__title': ifd.dcterms__title,
        'lifecyclestate': ifd.lifecyclestate
    }
    
    # Scenario 1: IFD already closed (should PASS regardless of BC states)
    print("\n--- Scenario 1: IFD already Closed (current state) ---")
    mock_bcs = [
        {'id': 'RQONE00000001', 'lifecyclestate': 'Closed'},
        {'id': 'RQONE00000002', 'lifecyclestate': 'Closed'},
    ]
    
    result = registry.execute_rule("PRPL 12.00.00", ifd_data, mock_bcs)
    print(f"Result: {result.severity}")
    print(f"Passed: {result.passed}")
    print(f"Title: {result.title}")
    print(f"Description: {result.description}")
    
    # Scenario 2: Simulate IFD in Committed state with all BCs closed (VIOLATION)
    print("\n--- Scenario 2: Simulate IFD in Committed state, all BCs Closed ---")
    ifd_data_committed = ifd_data.copy()
    ifd_data_committed['lifecyclestate'] = 'Committed'
    
    mock_bcs_all_closed = [
        {'id': 'RQONE00000001', 'lifecyclestate': 'Closed'},
        {'id': 'RQONE00000002', 'lifecyclestate': 'Canceled'},
        {'id': 'RQONE00000003', 'lifecyclestate': 'Closed'},
    ]
    
    result = registry.execute_rule("PRPL 12.00.00", ifd_data_committed, mock_bcs_all_closed)
    print(f"Result: {result.severity}")
    print(f"Passed: {result.passed}")
    print(f"Title: {result.title}")
    if not result.passed:
        print(f"\nDescription:\n{result.description}")
    
    # Scenario 3: IFD in Committed, some BCs still open (PASS)
    print("\n--- Scenario 3: IFD in Committed, some BCs still Developed ---")
    mock_bcs_some_open = [
        {'id': 'RQONE00000001', 'lifecyclestate': 'Closed'},
        {'id': 'RQONE00000002', 'lifecyclestate': 'Developed'},  # Still open
        {'id': 'RQONE00000003', 'lifecyclestate': 'Planned'},    # Still open
    ]
    
    result = registry.execute_rule("PRPL 12.00.00", ifd_data_committed, mock_bcs_some_open)
    print(f"Result: {result.severity}")
    print(f"Passed: {result.passed}")
    print(f"Title: {result.title}")
    
    print("\n[NOTE] Real IFD RQONE04736888 is already Closed - no violation")
    print("       Mock scenarios demonstrate rule logic with different states")

except ValueError as e:
    print(f"\n[ERROR] IFD not found: {e}")
    print("RQONE04736888 might not exist or you don't have access")

except Exception as e:
    print(f"\n[ERROR] {e}")
    import traceback
    traceback.print_exc()

print("\n" + "="*80)
print("Test Complete")
print("="*80)
