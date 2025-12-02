#!/usr/bin/env python3
"""
Test Relationship Helper
========================

Test IRM/RRM fetching with known RQ1 numbers.
"""

import os
import sys
sys.path.insert(0, 'src')

from rq1 import BaseUrl, Client
from relationship_helper import RelationshipHelper


def test_relationships():
    """Test IRM/RRM queries."""
    
    client = Client(
        base_url=BaseUrl.ACCEPTANCE,
        username='DAB5HC',
        password='12021998@abcD',
        toolname="OfficeUtils",
        toolversion="1.0"
    )
    
    helper = RelationshipHelper(client)
    
    print("\n" + "="*80)
    print("Test 1: Get Releases for IFD RQONE04736888")
    print("="*80)
    
    releases = helper.get_issue_releases("RQONE04736888")
    print(f"\nFound {len(releases)} releases mapped to this IFD:")
    for rel in releases:
        print(f"  - {rel.identifier}: {rel.dcterms__title} (State: {rel.lifecyclestate})")
    
    print("\n" + "="*80)
    print("Test 2: Get parent PVER for BC RQONE04843969")
    print("="*80)
    
    parents = helper.get_parent_releases("RQONE04843969")
    print(f"\nFound {len(parents)} parent releases:")
    for p in parents:
        print(f"  - {p.identifier}: {p.dcterms__title} (State: {p.lifecyclestate})")
    
    print("\n" + "="*80)
    print("Test 3: Get PST mapping for BC RQONE04843969")
    print("="*80)
    
    pst_info = helper.get_pst_mapping("RQONE04843969")
    if pst_info:
        print(f"\nPST Mapping found:")
        print(f"  PST RQ1: {pst_info['pst_rq1']}")
        print(f"  Delivery Date: {pst_info['pst_delivery_date']}")
        print(f"  RRM State: {pst_info['rrm_state']}")
    else:
        print("\nNo PST mapping found")


if __name__ == "__main__":
    test_relationships()
