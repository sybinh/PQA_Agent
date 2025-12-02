#!/usr/bin/env python
"""
Test PRPL 15.00.00: Release not closed after planned date

Test with mocked current time = 2025-11-10 to make BC appear overdue.
BC RQONE04815588 has planned date 2025-11-21, so with mocked time 2025-11-10:
- Planned date is in FUTURE -> should PASS
  
Then test with time = 2025-11-25 (after planned date):
- BC is Closed -> should PASS
- If BC was NOT closed -> should WARN
"""

from rq1 import Client, BaseUrl
from rq1.models import Release, ReleaseProperty
from datetime import datetime, timezone
from unittest.mock import patch
import sys

print("="*80)
print("Testing PRPL 15.00.00: BC not closed after planned date")
print("Testing with BC RQONE04815588 (Planned: 2025-11-21, State: Closed)")
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

# Fetch the BC-Release
print("\n[STEP 1] Fetching BC RQONE04815588...")

try:
    bc_release = client.get_record_by_rq1_number(
        Release,
        "RQONE04815588",
        select=[
            ReleaseProperty.id,
            ReleaseProperty.dcterms__title,
            ReleaseProperty.lifecyclestate,
            ReleaseProperty.planneddate,
            ReleaseProperty.actualdate,
            ReleaseProperty.submitdate
        ]
    )
    
    print(f"[OK] Found BC:")
    print(f"  ID: {bc_release.id}")
    print(f"  Title: {bc_release.dcterms__title[:60]}...")
    print(f"  State: {bc_release.lifecyclestate}")
    print(f"  Planned Date: {bc_release.planneddate}")
    print(f"  Actual Date: {bc_release.actualdate}")
    
    # Import rule
    sys.path.insert(0, 'src/rules')
    from rule_bc_close import Rule_Bc_Close
    
    # Convert to dict
    bc_data = {
        'id': bc_release.id,
        'dcterms__title': bc_release.dcterms__title,
        'lifecyclestate': bc_release.lifecyclestate,
        'planneddate': bc_release.planneddate,
        'actualdate': bc_release.actualdate,
        'submitdate': bc_release.submitdate
    }
    
    print("\n" + "="*80)
    print("[STEP 2] Test Scenario 1: Current time = 2025-11-10 (BEFORE planned date)")
    print("="*80)
    print("Expected: PASS (planned date is in future)")
    
    # Mock datetime to 2025-11-10
    mocked_time = datetime(2025, 11, 10, 12, 0, 0, tzinfo=timezone.utc)
    
    with patch('src.rules.rule_bc_close.datetime') as mock_datetime:
        mock_datetime.now.return_value = mocked_time
        mock_datetime.fromisoformat = datetime.fromisoformat
        
        rule = Rule_Bc_Close(bc_data)
        result = rule.execute()
        
        print(f"\nResult: {result.severity}")
        print(f"Title: {result.title}")
        print(f"Description: {result.description}")
        print(f"Passed: {result.passed}")
    
    print("\n" + "="*80)
    print("[STEP 3] Test Scenario 2: Current time = 2025-11-25 (AFTER planned date)")
    print("="*80)
    print("Expected: PASS (BC is already Closed)")
    
    # Mock datetime to 2025-11-25
    mocked_time = datetime(2025, 11, 25, 12, 0, 0, tzinfo=timezone.utc)
    
    with patch('src.rules.rule_bc_close.datetime') as mock_datetime:
        mock_datetime.now.return_value = mocked_time
        mock_datetime.fromisoformat = datetime.fromisoformat
        
        rule = Rule_Bc_Close(bc_data)
        result = rule.execute()
        
        print(f"\nResult: {result.severity}")
        print(f"Title: {result.title}")
        print(f"Description: {result.description}")
        print(f"Passed: {result.passed}")
    
    print("\n" + "="*80)
    print("[STEP 4] Test Scenario 3: Simulate BC NOT closed after planned date")
    print("="*80)
    print("Expected: WARNING (BC should be closed)")
    
    # Modify BC data to simulate "Planned" state
    bc_data_not_closed = bc_data.copy()
    bc_data_not_closed['lifecyclestate'] = 'Planned'
    
    mocked_time = datetime(2025, 11, 25, 12, 0, 0, tzinfo=timezone.utc)
    
    with patch('src.rules.rule_bc_close.datetime') as mock_datetime:
        mock_datetime.now.return_value = mocked_time
        mock_datetime.fromisoformat = datetime.fromisoformat
        
        rule = Rule_Bc_Close(bc_data_not_closed)
        result = rule.execute()
        
        print(f"\nResult: {result.severity}")
        print(f"Title: {result.title}")
        print(f"Description: {result.description}")
        print(f"Passed: {result.passed}")
        
        if not result.passed:
            print("\n? VIOLATION DETECTED (as expected)")
        else:
            print("\n? ERROR: Should have detected violation!")

except Exception as e:
    print(f"[ERROR] {e}")
    import traceback
    traceback.print_exc()
