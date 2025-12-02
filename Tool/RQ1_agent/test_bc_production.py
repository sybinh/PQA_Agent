#!/usr/bin/env python
"""
Test fetching BC-Release RQONE04815588 from PRODUCTION environment
"""

from rq1 import Client, BaseUrl
from rq1.models import Release, ReleaseProperty
import sys

print("="*80)
print("Testing BC-Release RQONE04815588 on PRODUCTIVE")
print("="*80)

# Initialize RQ1 client with PRODUCTION
try:
    client = Client(
        base_url=BaseUrl.PRODUCTIVE,  # Using PRODUCTIVE instead of ACCEPTANCE
        username="DAB5HC",
        password="12021998@abcD",
        toolname="OfficeUtils",
        toolversion="1.0"
    )
    print("? Connected to PRODUCTIVE environment")
except Exception as e:
    print(f"? Connection failed: {e}")
    sys.exit(1)

# Try fetching RQONE04815588
print(f"\nFetching RQONE04815588 as Release...")

try:
    bc_release = client.get_record_by_rq1_number(
        Release,
        "RQONE04815588",
        select=[
            ReleaseProperty.id,
            ReleaseProperty.dcterms__title,
            ReleaseProperty.dcterms__type,
            ReleaseProperty.lifecyclestate,
            ReleaseProperty.planneddate,
            ReleaseProperty.actualdate,
            ReleaseProperty.submitdate,
            ReleaseProperty.startdate
        ]
    )
    
    print(f"\n? SUCCESS! Found Release in PRODUCTION:")
    print(f"  ID: {bc_release.id}")
    print(f"  Title: {bc_release.dcterms__title[:80]}...")
    print(f"  Type: {bc_release.dcterms__type}")
    print(f"  State: {bc_release.lifecyclestate}")
    print(f"  Planned Date: {bc_release.planneddate}")
    print(f"  Actual Date: {bc_release.actualdate}")
    print(f"  Submit Date: {bc_release.submitdate}")
    print(f"  Start Date: {bc_release.startdate}")
    
    # Now test the rule
    print(f"\n{'='*80}")
    print("Testing Rule_Bc_Close with real data")
    print('='*80)
    
    # Import rule
    sys.path.insert(0, 'src/rules')
    from rule_bc_close import Rule_Bc_Close
    
    # Convert to dict for rule
    bc_data = {
        'id': bc_release.id,
        'dcterms__title': bc_release.dcterms__title,
        'dcterms__type': bc_release.dcterms__type,
        'lifecyclestate': bc_release.lifecyclestate,
        'planneddate': bc_release.planneddate,
        'actualdate': bc_release.actualdate,
        'submitdate': bc_release.submitdate
    }
    
    # Execute rule
    rule = Rule_Bc_Close(bc_data)
    result = rule.execute()
    
    print(f"\nRule Validation Result:")
    if result.passed:
        print(f"  ? {result.severity}: {result.title}")
    else:
        print(f"  ? {result.severity}: {result.title}")
    
    print(f"\n  Description:")
    print(f"  {result.description}")

except ValueError as e:
    print(f"\n? Not found: {e}")
    print(f"\nNote: RQONE04815588 might not exist in PRODUCTION either,")
    print(f"      or you might not have access permissions.")

except Exception as e:
    print(f"\n? Error: {e}")
    import traceback
    traceback.print_exc()
