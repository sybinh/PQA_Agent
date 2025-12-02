#!/usr/bin/env python3
"""Fetch full details of releases and their workitems"""

from rq1 import Client, BaseUrl
from rq1.release import Release
import json

# Connect
client = Client(
    BaseUrl.PRODUCTIVE,
    'DAB5HC',
    '12021998@abcD',
    'OfficeUtils',
    '1.0'
)

print("=" * 80)
print("Fetching PVER: RQONE04762901")
print("=" * 80)

try:
    pver = client.get_record_by_rq1_number(Release, 'RQONE04762901')
    
    print(f"\nTitle: {pver.dcterms__title}")
    print(f"State: {pver.lifecyclestate}")
    print(f"Type: {pver.dcterms__type}")
    print("\n=== ALL FIELDS ===")
    data_pver = pver.model_dump()
    
    # Filter out None values for cleaner output
    data_clean = {k: v for k, v in data_pver.items() if v is not None}
    print(json.dumps(data_clean, indent=2, default=str))
    
    # Save to file
    with open('pver_full.json', 'w', encoding='utf-8') as f:
        json.dump(data_clean, f, indent=2, default=str)
    print("\nSaved to pver_full.json")
        
except Exception as e:
    print(f"ERROR: {e}")
    import traceback
    traceback.print_exc()

print("\n" + "=" * 80)
print("Fetching BC: RQONE04843969")
print("=" * 80)

try:
    bc = client.get_record_by_rq1_number(Release, 'RQONE04843969')
    
    print(f"\nTitle: {bc.dcterms__title}")
    print(f"State: {bc.lifecyclestate}")
    print(f"Type: {bc.dcterms__type}")
    print("\n=== ALL FIELDS ===")
    data_bc = bc.model_dump()
    
    # Filter out None values
    data_clean = {k: v for k, v in data_bc.items() if v is not None}
    print(json.dumps(data_clean, indent=2, default=str))
    
    # Save to file
    with open('bc_full.json', 'w', encoding='utf-8') as f:
        json.dump(data_clean, f, indent=2, default=str)
    print("\nSaved to bc_full.json")
        
except Exception as e:
    print(f"ERROR: {e}")
    import traceback
    traceback.print_exc()

print("\n" + "=" * 80)
print("DONE - Check pver_full.json and bc_full.json")
print("=" * 80)
