#!/usr/bin/env python3
"""Fetch and compare two Workitems"""

from rq1 import Client, BaseUrl
from rq1.workitem import Workitem
from rq1.models import WorkitemProperty
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
print("Fetching PVER Workitem: RQONE04853587")
print("=" * 80)

try:
    result1 = client.query(
        Workitem,
        where='cq:Id="RQONE04853587"',
        select=[
            WorkitemProperty.id,
            WorkitemProperty.dcterms__title,
            WorkitemProperty.dcterms__type,
            WorkitemProperty.lifecyclestate,
            WorkitemProperty.belongstorelease,
            WorkitemProperty.belongstoissue,
            WorkitemProperty.belongstoproject,
            WorkitemProperty.assignee,
            WorkitemProperty.planneddate,
            WorkitemProperty.actualdate,
            WorkitemProperty.category,
            WorkitemProperty.subcategory
        ],
        page_size=1
    )
    
    if result1.members:
        wi1 = result1.members[0]
        print(f"\nTitle: {wi1.dcterms__title}")
        print(f"State: {wi1.lifecyclestate}")
        print(f"Type: {wi1.dcterms__type}")
        print(f"BelongsToRelease: {wi1.belongstorelease}")
        print(f"BelongsToIssue: {wi1.belongstoissue}")
        print(f"BelongsToProject: {wi1.belongstoproject}")
        print(f"Category: {wi1.category}")
        print("\n=== ALL FIELDS ===")
        data1 = wi1.model_dump()
        data_clean = {k: v for k, v in data1.items() if v is not None}
        print(json.dumps(data_clean, indent=2, default=str))
        
        # Save to file
        with open('wi1_pver.json', 'w', encoding='utf-8') as f:
            json.dump(data_clean, f, indent=2, default=str)
        print("\nSaved to wi1_pver.json")
    else:
        print("Not found!")
        
except Exception as e:
    print(f"ERROR: {e}")
    import traceback
    traceback.print_exc()

print("\n" + "=" * 80)
print("Fetching BC Workitem: RQONE04801359")
print("=" * 80)

try:
    result2 = client.query(
        Workitem,
        where='cq:Id="RQONE04801359"',
        select=[
            WorkitemProperty.id,
            WorkitemProperty.dcterms__title,
            WorkitemProperty.dcterms__type,
            WorkitemProperty.lifecyclestate,
            WorkitemProperty.belongstorelease,
            WorkitemProperty.belongstoissue,
            WorkitemProperty.belongstoproject,
            WorkitemProperty.assignee,
            WorkitemProperty.planneddate,
            WorkitemProperty.actualdate,
            WorkitemProperty.category,
            WorkitemProperty.subcategory
        ],
        page_size=1
    )
    
    if result2.members:
        wi2 = result2.members[0]
        print(f"\nTitle: {wi2.dcterms__title}")
        print(f"State: {wi2.lifecyclestate}")
        print(f"Type: {wi2.dcterms__type}")
        print(f"BelongsToRelease: {wi2.belongstorelease}")
        print(f"BelongsToIssue: {wi2.belongstoissue}")
        print(f"BelongsToProject: {wi2.belongstoproject}")
        print(f"Category: {wi2.category}")
        print("\n=== ALL FIELDS ===")
        data2 = wi2.model_dump()
        data_clean = {k: v for k, v in data2.items() if v is not None}
        print(json.dumps(data_clean, indent=2, default=str))
        
        # Save to file
        with open('wi2_bc.json', 'w', encoding='utf-8') as f:
            json.dump(data_clean, f, indent=2, default=str)
        print("\nSaved to wi2_bc.json")
    else:
        print("Not found!")
        
except Exception as e:
    print(f"ERROR: {e}")
    import traceback
    traceback.print_exc()

print("\n" + "=" * 80)
print("DONE - Check wi1_pver.json and wi2_bc.json for full data")
print("=" * 80)
