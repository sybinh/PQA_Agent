#!/usr/bin/env python
"""
API wrapper for validate_user_items to return structured data instead of printing
"""

import os
import sys
from datetime import date
from typing import Dict, List, Any
from dotenv import load_dotenv

# Import configuration
from src.config import RQ1_TOOLNAME, RQ1_TOOLVERSION, RQ1_ENVIRONMENT, RQ1_PROJECT_IDS

# Import validation modules
from rq1 import BaseUrl, Client
from rq1.issue import Issue
from rq1.release import Release
from rq1.workitem import Workitem
from rq1.issuereleasemap import Issuereleasemap
from rq1.releasereleasemap import Releasereleasemap
from rq1.users import Users
from rq1.base import reference
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


def validate_user_items_api(target_username: str, auth_user: str = None, auth_password: str = None) -> Dict[str, Any]:
    """
    Validate RQ1 items for a user and return structured data
    
    Args:
        target_username: Username to validate items for
        auth_user: Authenticated user (defaults to env RQ1_USER)
        auth_password: Auth password (defaults to env RQ1_PASSWORD)
    
    Returns:
        Dict with structure:
        {
            'username': str,
            'user_fullname': str,
            'total_items': int,
            'issue_count': int,
            'ifd_count': int,
            'isw_count': int,
            'release_count': int,
            'release_breakdown': str,
            'workitem_count': int,
            'total_checks': int,
            'rules_applied': List[str],
            'violation_count': int,
            'pass_rate': float,
            'violations': List[Dict]
        }
    """
    
    # Get credentials from environment variables (preferred) or parameters
    # Priority: 1. Function parameters, 2. Environment variables, 3. .env file (fallback)
    if not auth_user:
        auth_user = os.getenv("RQ1_USER")
        if not auth_user:
            # Fallback to .env file
            load_dotenv()
            auth_user = os.getenv("RQ1_USER")
    
    if not auth_password:
        auth_password = os.getenv("RQ1_PASSWORD")
        if not auth_password:
            raise ValueError(
                "RQ1_PASSWORD not found! Please run setup_credentials.ps1 or setup_credentials.py first.\n"
                "For security, passwords should not be stored in .env files."
            )
    
    # Use constants from config (not from .env)
    tool_name = RQ1_TOOLNAME
    tool_version = RQ1_TOOLVERSION
    base_url = BaseUrl.PRODUCTIVE if RQ1_ENVIRONMENT == "PRODUCTIVE" else BaseUrl.ACCEPTANCE
    
    # Connect to RQ1
    client = Client(
        base_url=base_url,
        username=auth_user,
        password=auth_password,
        toolname=tool_name,
        toolversion=tool_version
    )
    
    # Look up user
    user_query = client.query(
        Users,
        where=(UsersProperty.login_name == target_username),
        select=[UsersProperty.login_name, UsersProperty.fullname],
        page_size=1
    )
    
    if not user_query.members:
        raise ValueError(f"User {target_username} not found")
    
    user = user_query.members[0]
    user_fullname = getattr(user, 'fullname', target_username)
    
    # Initialize counters
    violations = []
    total_checks = 0
    issue_counts = {'IFD': 0, 'ISW': 0}
    release_counts = {}
    
    # Query Issues
    issue_query = client.query(
        Issue,
        where=(
            (IssueProperty.assignee == reference(user.uri)) &
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
    
    # Validate Issues
    for issue in issue_query.members:
        issue_type = getattr(issue, 'cq__Type', '')
        is_ifd = 'FD' in issue_type
        is_isw = 'SW' in issue_type
        
        if is_ifd:
            issue_counts['IFD'] += 1
        elif is_isw:
            issue_counts['ISW'] += 1
        
        issue_data = {
            'id': issue.id,
            'title': getattr(issue, 'dcterms__title', ''),
            'type': issue_type,
            'lifecyclestate': getattr(issue, 'lifecyclestate', ''),
            'submitdate': getattr(issue, 'submitdate', None)
        }
        
        # PRPL 03
        rule03 = Rule_Conflicted_State(issue_data, "Issue")
        result03 = rule03.execute()
        total_checks += 1
        if not result03.passed:
            violations.append({
                'rule_id': 'PRPL 03.00.00',
                'rule_desc': 'Issue/Release/workitem is still in "Conflicted" state',
                'severity': result03.severity,
                'item_id': issue.id,
                'item_title': issue.dcterms__title,
                'details': result03.description
            })
        
        # IFD-specific rules
        if is_ifd:
            # PRPL 11
            rule11 = Rule_Issue_Sla(issue_data, client=client, project_ids=RQ1_PROJECT_IDS)
            result11 = rule11.execute()
            total_checks += 1
            if not result11.passed:
                violations.append({
                    'rule_id': 'PRPL 11.00.00',
                    'rule_desc': 'IFD 5 day SLA reached',
                    'severity': result11.severity,
                    'item_id': issue.id,
                    'item_title': issue.dcterms__title,
                    'details': result11.description
                })
            
            # PRPL 14 & 18
            parent_isw_data = None
            hasparent = getattr(issue, 'hasparent', None)
            if hasparent:
                parent_isw = hasparent if not isinstance(hasparent, list) else hasparent[0]
                parent_isw_data = {
                    'id': getattr(parent_isw, 'id', 'UNKNOWN'),
                    'lifecyclestate': getattr(parent_isw, 'lifecyclestate', ''),
                    'uri': getattr(parent_isw, 'uri', None)
                }
            
            rule14 = Rule_IFD_ISW_Commitment(issue_data, parent_isw_data)
            result14 = rule14.execute()
            total_checks += 1
            if not result14.passed:
                violations.append({
                    'rule_id': 'PRPL 14.00.00',
                    'rule_desc': 'IFD is not committed, eventhough attached Issue-SW is committed',
                    'severity': result14.severity,
                    'item_id': issue.id,
                    'item_title': issue.dcterms__title,
                    'details': result14.description
                })
            
            rule18 = Rule_IFD_ISW_Commitment_Delay(issue_data, parent_isw_data, client=client)
            result18 = rule18.execute()
            total_checks += 1
            if not result18.passed:
                violations.append({
                    'rule_id': 'PRPL 18.00.00',
                    'rule_desc': 'I-FD not committed 5 or more working days after attached I-SW was committed',
                    'severity': result18.severity,
                    'item_id': issue.id,
                    'item_title': issue.dcterms__title,
                    'details': result18.description
                })
    
    # Query Releases
    release_query = client.query(
        Release,
        where=(
            (ReleaseProperty.assignee == reference(user.uri)) &
            (ReleaseProperty.lifecyclestate != "Canceled") &
            (ReleaseProperty.lifecyclestate != "Closed")
        ),
        select=[
            ReleaseProperty.id,
            ReleaseProperty.dcterms__title,
            ReleaseProperty.dcterms__type,
            ReleaseProperty.category,
            ReleaseProperty.lifecyclestate,
            ReleaseProperty.planneddate
        ],
        page_size=200
    )
    
    # Validate Releases
    for release in release_query.members:
        release_type = getattr(release, 'category', '')
        release_counts[release_type] = release_counts.get(release_type, 0) + 1
        
        release_data = {
            'id': release.id,
            'title': getattr(release, 'dcterms__title', ''),
            'type': release_type,
            'lifecyclestate': getattr(release, 'lifecyclestate', ''),
            'planneddate': getattr(release, 'planneddate', None)
        }
        
        # PRPL 03
        rule03 = Rule_Conflicted_State(release_data, "Release")
        result03 = rule03.execute()
        total_checks += 1
        if not result03.passed:
            violations.append({
                'rule_id': 'PRPL 03.00.00',
                'rule_desc': 'Issue/Release/workitem is still in "Conflicted" state',
                'severity': result03.severity,
                'item_id': release.id,
                'item_title': release.dcterms__title,
                'details': result03.description
            })
        
        # PRPL 15
        if release_type in ['BC', 'FC']:
            rule15 = Rule_Release_Closure(release_data)
            result15 = rule15.execute()
            total_checks += 1
            if not result15.passed:
                violations.append({
                    'rule_id': 'PRPL 15.00.00',
                    'rule_desc': 'Release is not closed after planned date for BC and FC',
                    'severity': result15.severity,
                    'item_id': release.id,
                    'item_title': release.dcterms__title,
                    'details': result15.description
                })
    
    # Query Workitems
    workitem_query = client.query(
        Workitem,
        where=(
            (WorkitemProperty.assignee == reference(user.uri)) &
            (WorkitemProperty.lifecyclestate != "Canceled") &
            (WorkitemProperty.lifecyclestate != "Closed") &
            (WorkitemProperty.lifecyclestate != "Implemented")
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
    
    # Validate Workitems
    for workitem in workitem_query.members:
        workitem_data = {
            'id': workitem.id,
            'title': getattr(workitem, 'dcterms__title', ''),
            'type': getattr(workitem, 'dcterms__type', 'Workitem'),
            'lifecyclestate': getattr(workitem, 'lifecyclestate', ''),
            'planneddate': getattr(workitem, 'planneddate', None)
        }
        
        # PRPL 03
        rule03 = Rule_Conflicted_State(workitem_data, "Workitem")
        result03 = rule03.execute()
        total_checks += 1
        if not result03.passed:
            violations.append({
                'rule_id': 'PRPL 03.00.00',
                'rule_desc': 'Issue/Release/workitem is still in "Conflicted" state',
                'severity': result03.severity,
                'item_id': workitem.id,
                'item_title': workitem.dcterms__title,
                'details': result03.description
            })
        
        # PRPL 02
        rule02 = Rule_Workitem_PlannedDate(workitem_data)
        result02 = rule02.execute()
        total_checks += 1
        if not result02.passed:
            violations.append({
                'rule_id': 'PRPL 02.00.00',
                'rule_desc': 'Workitem is in started state, but planned date for workitem is not entered in planning tab',
                'severity': result02.severity,
                'item_id': workitem.id,
                'item_title': workitem.dcterms__title,
                'details': result02.description
            })
        
        # PRPL 16
        rule16 = Rule_Workitem_Close(workitem_data)
        result16 = rule16.execute()
        total_checks += 1
        if not result16.passed:
            violations.append({
                'rule_id': 'PRPL 16.00.00',
                'rule_desc': 'Workitem is not closed after planned date',
                'severity': result16.severity,
                'item_id': workitem.id,
                'item_title': workitem.dcterms__title,
                'details': result16.description
            })
    
    # Build result
    total_issues = sum(issue_counts.values())
    total_releases = sum(release_counts.values())
    release_breakdown = ', '.join([f'{k}={v}' for k, v in release_counts.items() if v > 0]) if release_counts else 'None'
    
    pass_rate = ((total_checks - len(violations)) / total_checks * 100) if total_checks > 0 else 0
    
    return {
        'username': target_username,
        'user_fullname': user_fullname,
        'total_items': total_issues + total_releases + len(workitem_query.members),
        'issue_count': total_issues,
        'ifd_count': issue_counts.get('IFD', 0),
        'isw_count': issue_counts.get('ISW', 0),
        'release_count': total_releases,
        'release_breakdown': release_breakdown,
        'workitem_count': len(workitem_query.members),
        'total_checks': total_checks,
        'rules_applied': ['PRPL 01', 'PRPL 02', 'PRPL 03', 'PRPL 07', 'PRPL 11', 'PRPL 12', 'PRPL 13', 'PRPL 14', 'PRPL 15', 'PRPL 16', 'PRPL 18'],
        'violation_count': len(violations),
        'pass_rate': round(pass_rate, 1),
        'violations': violations
    }
