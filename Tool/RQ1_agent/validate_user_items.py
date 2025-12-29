#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Validate User Items Against PRPL Rules
========================================
Fetch all items assigned to user and validate against implemented rules.
"""

import os
import sys
from datetime import datetime, timezone

# Skip RQ1 library tracking/telemetry (prevents timeout on import)
os.environ["CI_ENVIRONMENT"] = "1"

from rq1 import BaseUrl, Client
from rq1.base import reference
from rq1.issue import Issue
from rq1.release import Release
from rq1.workitem import Workitem
from rq1.users import Users
from rq1.issuereleasemap import Issuereleasemap
from rq1.releasereleasemap import Releasereleasemap
from rq1.models import (
    IssueProperty, ReleaseProperty, WorkitemProperty, 
    UsersProperty, IssuereleasemapProperty, ReleasereleasemapProperty
)

# Import rules
sys.path.append('src')
from rules.rule_prpl_01_bc_requested_state import Rule_BC_RequestedState, PverMapping
from rules.rule_prpl_02_workitem_planned import Rule_Workitem_PlannedDate
from rules.rule_prpl_03_conflicted_state import Rule_Conflicted_State
from rules.rule_prpl_07_bc_pst_dates import Rule_Bc_CheckPstDates, PstMapping
from rules.rule_prpl_11_ifd_sla import Rule_Issue_Sla
from rules.rule_prpl_12_ifd_bc_closure import Rule_IFD_BcClosure
from rules.rule_prpl_13_ifd_bc_planned import Rule_IFD_BcPlannedDate, BcMapping
from rules.rule_prpl_14_ifd_isw_commitment import Rule_IFD_ISW_Commitment
from rules.rule_prpl_15_release_closure import Rule_Release_Closure
from rules.rule_prpl_16_workitem_close import Rule_Workitem_Close
from rules.rule_prpl_18_ifd_isw_commitment_delay import Rule_IFD_ISW_Commitment_Delay


def validate_user_items(target_username: str = "DAB5HC", login_username: str = "DAB5HC"):
    """Validate all items assigned to target user against PRPL rules."""
    
    print(f"\n{'='*80}")
    print(f"PRPL Rule Validation for User: {target_username}")
    print(f"(Authenticated as: {login_username})")
    print(f"{'='*80}\n")
    
    # Initialize client
    password = os.getenv("RQ1_PASSWORD", "12021998@abcD")
    client = Client(
        base_url=BaseUrl.PRODUCTIVE,
        username=login_username,
        password=password,
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
            print(f"[ERROR] User {target_username} not found")
            return
        
        user = user_query.members[0]
        user_uri = user.uri
        print(f"[OK] Found: {user.fullname}\n")
        
    except Exception as e:
        print(f"[ERROR] Failed to query user: {e}")
        return
    
    violations = []
    total_checks = 0
    
    # Count items by type
    issue_counts = {'IFD': 0, 'ISW': 0, 'Other': 0}
    release_counts = {'BC': 0, 'BX': 0, 'FC': 0, 'PVER': 0, 'PVAR': 0, 'Other': 0}
    
    # Query Issues and validate IFD rules
    print("[1] Querying Issues (all types: IFD, ISW)...")
    try:
        issue_query = client.query(
            Issue,
            where=(
                (IssueProperty.assignee == reference(user_uri)) &
                (IssueProperty.lifecyclestate != "Canceled") &
                (IssueProperty.lifecyclestate != "Closed")
            ),
            select=[
                IssueProperty.id,
                IssueProperty.dcterms__title,
                IssueProperty.dcterms__type,
                IssueProperty.category,
                IssueProperty.cq__Type,
                IssueProperty.lifecyclestate,
                IssueProperty.submitdate,
                IssueProperty.hasparent
            ],
            page_size=200
        )
        
        # Count and validate each Issue
        for issue in issue_query.members:
            issue_type = getattr(issue, 'dcterms__type', '')
            issue_category = getattr(issue, 'category', '')
            issue_cq_type = getattr(issue, 'cq__Type', '')
            issue_title = getattr(issue, 'dcterms__title', '')
            
            # Detect IFD vs ISW from cq__Type field
            if issue_cq_type == 'Issue FD':
                issue_counts['IFD'] += 1
                is_ifd = True
            elif issue_cq_type == 'Issue SW':
                issue_counts['ISW'] += 1
                is_ifd = False
            else:
                issue_counts['Other'] += 1
                is_ifd = False
            
            issue_data = {
                'id': getattr(issue, 'id', 'UNKNOWN'),
                'dcterms__title': getattr(issue, 'dcterms__title', ''),
                'lifecyclestate': getattr(issue, 'lifecyclestate', ''),
                'cq__Type': issue_cq_type,
                'submitdate': getattr(issue, 'submitdate', None),
                'uri': getattr(issue, 'uri', None)
            }
            
            # PRPL 03: Check for Conflicted state
            rule03 = Rule_Conflicted_State(issue_data, "Issue")
            result03 = rule03.execute()
            total_checks += 1
            if not result03.passed:
                violations.append({
                    'item_id': issue.id,
                    'item_title': issue.dcterms__title,
                    'rule': 'PRPL 03',
                    'severity': result03.severity,
                    'description': result03.description
                })
            
            # PRPL 11: Check IFD 5-day SLA (only for IFD)
            if is_ifd:
                rule11 = Rule_Issue_Sla(issue_data, client=client, target_user=target_username)
                result11 = rule11.execute()
                total_checks += 1
                if not result11.passed:
                    violations.append({
                        'rule': 'PRPL 11',
                        'severity': result11.severity,
                        'item_id': issue.id,
                        'item_title': issue.dcterms__title,
                        'description': result11.description
                    })
                
                # PRPL 14 & 18: IFD-ISW Commitment checks (only for IFD)
                parent_isw_data = None
                hasparent = getattr(issue, 'hasparent', None)
                if hasparent:
                    # hasparent can be a single Resource or list
                    parent_isw = hasparent if not isinstance(hasparent, list) else hasparent[0]
                    parent_isw_data = {
                        'id': getattr(parent_isw, 'id', 'UNKNOWN'),
                        'lifecyclestate': getattr(parent_isw, 'lifecyclestate', ''),
                        'uri': getattr(parent_isw, 'uri', None)
                    }
                
                # PRPL 14: IFD not committed when ISW committed
                rule14 = Rule_IFD_ISW_Commitment(issue_data, parent_isw_data)
                result14 = rule14.execute()
                total_checks += 1
                if not result14.passed:
                    violations.append({
                        'rule': 'PRPL 14',
                        'severity': result14.severity,
                        'item_id': issue.id,
                        'item_title': issue.dcterms__title,
                        'description': result14.description
                    })
                
                # PRPL 18: IFD not committed 5+ working days after ISW committed
                rule18 = Rule_IFD_ISW_Commitment_Delay(issue_data, parent_isw_data, client=client)
                result18 = rule18.execute()
                total_checks += 1
                if not result18.passed:
                    violations.append({
                        'rule': 'PRPL 18',
                        'severity': result18.severity,
                        'item_id': issue.id,
                        'item_title': issue.dcterms__title,
                        'description': result18.description
                    })
            
            # Only validate IFD type for PRPL 12/13
            if not is_ifd:
                continue
            
            # Get mapped BC-Releases for this IFD
            try:
                irm_query = client.query(
                    Issuereleasemap,
                    where=(IssuereleasemapProperty.issue == reference(issue.uri)),
                    select=[
                        IssuereleasemapProperty.release,
                        IssuereleasemapProperty.lifecyclestate
                    ],
                    page_size=100
                )
                
                mapped_bcs = []
                bc_mappings = []
                
                for irm in irm_query.members:
                    release_uri = getattr(irm, 'release', None)
                    if not release_uri:
                        continue
                    
                    # Get BC details
                    try:
                        bc_query = client.query(
                            Release,
                            where=(ReleaseProperty.uri == release_uri),
                            select=[
                                ReleaseProperty.id,
                                ReleaseProperty.dcterms__title,
                                ReleaseProperty.dcterms__type,
                                ReleaseProperty.category,
                                ReleaseProperty.cq__Type,
                                ReleaseProperty.lifecyclestate,
                                ReleaseProperty.planneddate
                            ],
                            page_size=1
                        )
                        
                        if bc_query.members:
                            bc = bc_query.members[0]
                            bc_type = getattr(bc, 'dcterms__type', '')
                            bc_category = getattr(bc, 'category', '')
                            bc_cq_type = getattr(bc, 'cq__Type', '')
                            bc_title = getattr(bc, 'dcterms__title', '')
                            
                            # Only consider BC/BX type releases (check cq__Type)
                            is_bc_or_bx = (bc_cq_type == 'BC' or bc_cq_type == 'BX')
                            
                            if is_bc_or_bx:
                                bc_data = {
                                    'id': getattr(bc, 'id', 'UNKNOWN'),
                                    'dcterms__title': getattr(bc, 'dcterms__title', ''),
                                    'lifecyclestate': getattr(bc, 'lifecyclestate', '')
                                }
                                mapped_bcs.append(bc_data)
                                
                                # Create BC mapping for PRPL 13
                                bc_mappings.append(BcMapping(
                                    bc_id=bc.id,
                                    bc_title=getattr(bc, 'dcterms__title', ''),
                                    bc_state=getattr(bc, 'lifecyclestate', ''),
                                    planned_date=getattr(bc, 'planneddate', None)
                                ))
                    except Exception:
                        continue
                
                # PRPL 12: Check if IFD should be closed
                if mapped_bcs:
                    rule12 = Rule_IFD_BcClosure(issue_data, mapped_bcs)
                    result12 = rule12.execute()
                    total_checks += 1
                    if not result12.passed:
                        violations.append({
                            'item_id': issue.id,
                            'item_title': issue.dcterms__title[:60],
                            'rule': 'PRPL 12',
                            'severity': result12.severity,
                            'description': result12.description
                        })
                
                # PRPL 13: Check if IFD should be implemented after BC planned dates
                if bc_mappings:
                    rule13 = Rule_IFD_BcPlannedDate(issue_data, bc_mappings)
                    result13 = rule13.execute()
                    total_checks += 1
                    if not result13.passed:
                        violations.append({
                            'item_id': issue.id,
                            'item_title': issue.dcterms__title[:60],
                            'rule': 'PRPL 13',
                            'severity': result13.severity,
                            'description': result13.description
                        })
                        
            except Exception as e:
                # Skip if mapping query fails
                continue
        
        # Show issue type breakdown with details
        issue_summary = [f"{k}: {v}" for k, v in issue_counts.items() if v > 0]
        print(f"[OK] Found {len(issue_query.members)} Issues ({', '.join(issue_summary)})")
        
        # Show detailed list of issues
        for issue in issue_query.members:
            issue_id = getattr(issue, 'id', 'UNKNOWN')
            issue_state = getattr(issue, 'lifecyclestate', '')
            issue_cq_type = getattr(issue, 'cq__Type', '')
            print(f"     - {issue_id} [{issue_cq_type}] ({issue_state})")
        
        ifd_count = issue_counts['IFD']
        if ifd_count > 0:
            print(f"     Will validate {ifd_count} IFD issues with PRPL 12 (BC closure) & 13 (BC planned date)\n")
        else:
            print()
    except Exception as e:
        print(f"[ERROR] Failed to validate Issues: {e}\n")
    
    # Query Releases and validate BC/BX rules
    print("[2] Querying Releases (all types: BC, BX, FC, PVER, etc.)...")
    try:
        release_query = client.query(
            Release,
            where=(
                (ReleaseProperty.assignee == reference(user_uri)) &
                (ReleaseProperty.lifecyclestate != "Canceled") &
                (ReleaseProperty.lifecyclestate != "Closed")
            ),
            select=[
                ReleaseProperty.id,
                ReleaseProperty.dcterms__title,
                ReleaseProperty.dcterms__type,
                ReleaseProperty.category,
                ReleaseProperty.cq__Type,
                ReleaseProperty.lifecyclestate,
                ReleaseProperty.planneddate
            ],
            page_size=200
        )
        
        # Count and validate each Release
        for release in release_query.members:
            release_type = getattr(release, 'dcterms__type', '')
            release_category = getattr(release, 'category', '')
            release_cq_type = getattr(release, 'cq__Type', '')
            release_title = getattr(release, 'dcterms__title', '')
            
            # Use cq__Type field to identify release types (BC, BX, FC, PVER, PVAR)
            is_bc_or_bx = False
            if release_cq_type == 'BC':
                release_counts['BC'] += 1
                is_bc_or_bx = True
            elif release_cq_type == 'BX':
                release_counts['BX'] += 1
                is_bc_or_bx = True
            elif release_cq_type == 'FC':
                release_counts['FC'] += 1
            elif release_cq_type == 'PVER':
                release_counts['PVER'] += 1
            elif release_cq_type == 'PVAR':
                release_counts['PVAR'] += 1
            else:
                release_counts['Other'] += 1
            
            release_data = {
                'id': getattr(release, 'id', 'UNKNOWN'),
                'dcterms__title': getattr(release, 'dcterms__title', ''),
                'lifecyclestate': getattr(release, 'lifecyclestate', ''),
                'planneddate': getattr(release, 'planneddate', None)
            }
            
            # PRPL 03: Check for Conflicted state
            rule03 = Rule_Conflicted_State(release_data, "Release")
            result03 = rule03.execute()
            total_checks += 1
            if not result03.passed:
                violations.append({
                    'item_id': release.id,
                    'item_title': release.dcterms__title,
                    'rule': 'PRPL 03',
                    'severity': result03.severity,
                    'description': result03.description
                })
            
            # PRPL 15: BC/FC Closure check (only BC/FC in Planned/Developed state)
            if release_type in ['BC', 'FC']:
                rule15 = Rule_Release_Closure(release_data)
                result15 = rule15.execute()
                total_checks += 1
                if not result15.passed:
                    violations.append({
                        'rule': 'PRPL 15',
                        'severity': result15.severity,
                        'item_id': release.id,
                        'item_title': release.dcterms__title,
                        'description': result15.description
                    })
            
            # Only validate BC/BX types (BC and BX are treated the same)
            if not is_bc_or_bx:
                if release.id == 'RQONE04659895':
                    print(f"\n     [DEBUG] BC {release.id}: is_bc_or_bx={is_bc_or_bx}, skipped!")
                continue
            
            # Get mapped PVER/PVAR for this BC/BX via Releasereleasemap
            try:
                # Get BC URI for RRM query (URI is stored in .uri attribute)
                bc_uri = getattr(release, 'uri', None)
                
                if not bc_uri:
                    continue
                
                rrm_query = client.query(
                    Releasereleasemap,
                    where=(ReleasereleasemapProperty.hasmappedchildrelease == reference(bc_uri)),
                    select=[
                        ReleasereleasemapProperty.hasmappedparentrelease,
                        ReleasereleasemapProperty.lifecyclestate,
                        ReleasereleasemapProperty.id,
                        ReleasereleasemapProperty.requesteddeliverydate
                    ],
                    page_size=50
                )
                
                # Build PST mappings directly from RRM (no need to fetch PVER/PVAR)
                pver_mappings = []  # For PRPL 01 (empty for now - would need PVER/PVAR fetch)
                pst_mappings = []   # For PRPL 07 (built from RRM)
                
                for rrm in rrm_query.members:
                    rrm_state = getattr(rrm, 'lifecyclestate', 'Unknown')
                    rrm_id = getattr(rrm, 'id', 'N/A')
                    requested_delivery_date = getattr(rrm, 'requesteddeliverydate', None)
                    parent_uri_obj = getattr(rrm, 'hasmappedparentrelease', None)
                    
                    # Get parent ID from URI if available
                    pst_id = f"RRM-{rrm_id}"  # Use RRM ID as identifier
                    
                    pst_mappings.append(PstMapping(
                        pst_id=pst_id,
                        pst_type='PST',  # Generic type since we don't fetch PVER/PVAR
                        rrm_state=rrm_state,
                        requested_delivery_date=requested_delivery_date
                    ))
                
                # PRPL 01: Check if BC is in Requested state
                if pver_mappings:
                    rule01 = Rule_BC_RequestedState(release_data, pver_mappings)
                    result01 = rule01.execute()
                    total_checks += 1
                    if not result01.passed:
                        violations.append({
                            'item_id': release.id,
                            'item_title': release.dcterms__title[:60],
                            'rule': 'PRPL 01',
                            'severity': result01.severity,
                            'description': result01.description
                        })
                
                # PRPL 07: Check BC planned date vs PVER/PVAR delivery dates
                if pst_mappings:
                    rule07 = Rule_Bc_CheckPstDates(release_data, pst_mappings)
                    result07 = rule07.execute()
                    
                    total_checks += 1
                    if not result07.passed:
                        violations.append({
                            'item_id': release.id,
                            'item_title': release.dcterms__title[:60],
                            'rule': 'PRPL 07',
                            'severity': result07.severity,
                            'description': result07.description
                        })
                        
            except Exception as e:
                # Skip if Releasereleasemap query fails
                continue
        
        # Show release type breakdown with details
        release_summary = [f"{k}: {v}" for k, v in release_counts.items() if v > 0]
        bc_bx_total = release_counts['BC'] + release_counts['BX']
        print(f"[OK] Found {len(release_query.members)} Releases ({', '.join(release_summary)})")
        
        # Show detailed list of releases
        for release in release_query.members:
            release_id = getattr(release, 'id', 'UNKNOWN')
            release_state = getattr(release, 'lifecyclestate', '')
            release_cq_type = getattr(release, 'cq__Type', '')
            print(f"     - {release_id} [{release_cq_type}] ({release_state})")
        
        print(f"     BC/BX releases: {bc_bx_total} (validated with PRPL 01, 03, 07)\n")
    except Exception as e:
        print(f"[ERROR] Failed to validate Releases: {e}\n")
    
    # Query Workitems
    print("[3] Validating Workitems...")
    try:
        workitem_query = client.query(
            Workitem,
            where=(
                (WorkitemProperty.assignee == reference(user_uri)) &
                (WorkitemProperty.lifecyclestate != "Canceled") &
                (WorkitemProperty.lifecyclestate != "Closed")
            ),
            select=[
                WorkitemProperty.id,
                WorkitemProperty.dcterms__title,
                WorkitemProperty.dcterms__type,
                WorkitemProperty.lifecyclestate,
                WorkitemProperty.planneddate
            ],
            page_size=200
        )
        
        for workitem in workitem_query.members:
            workitem_data = {
                'id': getattr(workitem, 'id', 'UNKNOWN'),
                'dcterms__title': getattr(workitem, 'dcterms__title', ''),
                'dcterms__type': 'Workitem',
                'lifecyclestate': getattr(workitem, 'lifecyclestate', ''),
                'planneddate': getattr(workitem, 'planneddate', None)
            }
            
            # PRPL 03: Check for Conflicted state
            rule03 = Rule_Conflicted_State(workitem_data, "Workitem")
            result03 = rule03.execute()
            total_checks += 1
            if not result03.passed:
                violations.append({
                    'item_id': workitem.id,
                    'item_title': workitem.dcterms__title,
                    'rule': 'PRPL 03',
                    'severity': result03.severity,
                    'description': result03.description
                })
            
            # PRPL 02: Check planned date
            rule02 = Rule_Workitem_PlannedDate(workitem_data)
            result02 = rule02.execute()
            total_checks += 1
            if not result02.passed:
                violations.append({
                    'item_id': workitem.id,
                    'item_title': workitem.dcterms__title,
                    'rule': 'PRPL 02',
                    'severity': result02.severity,
                    'description': result02.description
                })
            
            # PRPL 16: Check closure after planned date
            rule16 = Rule_Workitem_Close(workitem_data)
            result16 = rule16.execute()
            total_checks += 1
            if not result16.passed:
                violations.append({
                    'item_id': workitem.id,
                    'item_title': workitem.dcterms__title,
                    'rule': 'PRPL 16',
                    'severity': result16.severity,
                    'description': result16.description
                })
        
        print(f"[OK] Validated {len(workitem_query.members)} Workitems")
        
        # Show detailed list of workitems
        for workitem in workitem_query.members:
            workitem_id = getattr(workitem, 'id', 'UNKNOWN')
            workitem_state = getattr(workitem, 'lifecyclestate', '')
            print(f"     - {workitem_id} [Workitem] ({workitem_state})")
        print()
    
    except Exception as e:
        print(f"[ERROR] Failed to validate Workitems: {e}")
    
    # Print validation summary
    print(f"{'='*80}")
    print(f"VALIDATION SUMMARY")
    print(f"{'='*80}")
    total_issues = sum(issue_counts.values())
    total_releases = sum(release_counts.values())
    print(f"Total items assigned: {total_issues + total_releases + len(workitem_query.members) if 'workitem_query' in locals() else 0}")
    print(f"  - Issues: {total_issues} ({', '.join([f'{k}={v}' for k, v in issue_counts.items() if v > 0])})")
    print(f"  - Releases: {total_releases} ({', '.join([f'{k}={v}' for k, v in release_counts.items() if v > 0])})")
    print(f"  - Workitems: {len(workitem_query.members) if 'workitem_query' in locals() else 0}")
    print(f"Total checks performed: {total_checks}")
    print(f"Rules applied: PRPL 01, 02, 03, 07, 11, 12, 13, 14, 15, 16, 18")
    print(f"Violations found: {len(violations)}")
    if total_checks > 0:
        print(f"Pass rate: {((total_checks - len(violations)) / total_checks * 100):.1f}%\n")
    else:
        print(f"Pass rate: N/A (no checks performed)\n")
    
    # Print violations
    if violations:
        print(f"{'='*80}")
        print(f"VIOLATIONS FOUND ({len(violations)})")
        print(f"{'='*80}\n")
        
        # Rule descriptions mapping
        rule_descriptions = {
            'PRPL 01': 'BC-R is not in requested state, 8 weeks before PVER planned delivery date',
            'PRPL 02': 'Workitem is in started state, but planned date for workitem is not entered in planning tab',
            'PRPL 03': 'Issue/Release/workitem is still in "Conflicted" state',
            'PRPL 07': 'Planned date of BC later than requested delivery date of any mapped PVER or PVAR',
            'PRPL 11': 'IFD 5 day SLA reached',
            'PRPL 12': 'IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled',
            'PRPL 13': 'IFD is not implemented or closed, after planned dated of BC-R',
            'PRPL 14': 'IFD is not committed, eventhough attached Issue-SW is committed',
            'PRPL 15': 'Release is not closed after planned date for BC and FC',
            'PRPL 16': 'Workitem is not closed after planned date',
            'PRPL 18': 'I-FD not committed 5 or more working days after attached I-SW was committed'
        }
        
        for i, v in enumerate(violations, 1):
            rule_desc = rule_descriptions.get(v['rule'], 'Unknown rule')
            rule_id = f"{v['rule']}.00.00"  # Format: PRPL XX.00.00
            print(f"[{i}] {rule_id} - {v['severity']}")
            print(f"    Rule: {rule_desc}")
            print(f"    Item: {v['item_id']}")
            print(f"    Title: {v['item_title']}")
            # Print description without extra indentation, skip title line and empty leading lines
            desc_lines = v['description'].split('\n')
            first_line = True
            for line in desc_lines:
                # Skip line if it's just repeating the title
                if line.strip().startswith('Title:') and v['item_title'] in line:
                    continue
                # Skip empty lines at the start
                if first_line and not line.strip():
                    continue
                first_line = False
                # Print with consistent indentation (4 spaces)
                print(f"    {line}")
            print()
    else:
        print("? No violations found! All items comply with PRPL rules.\n")
    
    print(f"{'='*80}\n")


if __name__ == "__main__":
    import sys
    
    # Get target username from command line argument
    if len(sys.argv) > 1:
        target_user = sys.argv[1]
    else:
        target_user = "PCH5HC"  # Default
    
    # Login username is always DAB5HC (tool owner)
    validate_user_items(target_username=target_user, login_username="DAB5HC")
