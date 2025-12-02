#!/usr/bin/env python3
"""Fetch 4 Workitems to analyze differences"""

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

workitem_ids = [
    'RQONE04853587',  # Original PVER example
    'RQONE04801359',  # Original BC example
    'RQONE04675098',  # New example 1
    'RQONE04831243'   # New example 2
]

results = []

for wi_id in workitem_ids:
    print(f"\n{'='*80}")
    print(f"Fetching: {wi_id}")
    print('='*80)
    
    try:
        result = client.query(
            Workitem,
            where=f'cq:Id="{wi_id}"',
            select=[
                WorkitemProperty.id,
                WorkitemProperty.dcterms__title,
                WorkitemProperty.dcterms__type,
                WorkitemProperty.lifecyclestate,
                WorkitemProperty.belongstorelease,
                WorkitemProperty.belongstoissue,
                WorkitemProperty.belongstoproject,
                WorkitemProperty.category,
                WorkitemProperty.subcategory,
                WorkitemProperty.planneddate,
                WorkitemProperty.actualdate
            ],
            page_size=1
        )
        
        if result.members:
            wi = result.members[0]
            info = {
                'id': wi.id,
                'title': wi.dcterms__title,
                'category': wi.category,
                'subcategory': wi.subcategory,
                'state': wi.lifecyclestate,
                'belongstorelease': wi.belongstorelease,
                'belongstoissue': wi.belongstoissue,
                'planneddate': str(wi.planneddate) if wi.planneddate else None
            }
            results.append(info)
            
            print(f"Title: {wi.dcterms__title}")
            print(f"Category: {wi.category}")
            print(f"SubCategory: {wi.subcategory}")
            print(f"State: {wi.lifecyclestate}")
            print(f"BelongsToRelease: {wi.belongstorelease}")
            print(f"BelongsToIssue: {wi.belongstoissue}")
            print(f"PlannedDate: {wi.planneddate}")
        else:
            print("NOT FOUND!")
            
    except Exception as e:
        print(f"ERROR: {e}")
        import traceback
        traceback.print_exc()

print("\n" + "="*80)
print("SUMMARY - All 4 Workitems")
print("="*80)

for r in results:
    print(f"\n{r['id']}: {r['title']}")
    print(f"  Category: {r['category']}")
    print(f"  SubCategory: {r['subcategory']}")
    print(f"  State: {r['state']}")
    print(f"  BelongsToRelease: {'YES' if r['belongstorelease'] else 'NO'}")
    print(f"  BelongsToIssue: {'YES' if r['belongstoissue'] else 'NO'}")

# Save to JSON
with open('all_4_workitems.json', 'w', encoding='utf-8') as f:
    json.dump(results, f, indent=2, default=str)
    
print("\n[SAVED] all_4_workitems.json")
