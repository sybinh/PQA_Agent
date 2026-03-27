#!/usr/bin/env python3
"""Quick test script for Rule 18 only."""
import os
import sys

os.environ["CI_ENVIRONMENT"] = "1"
try:
    from dotenv import load_dotenv
    load_dotenv()
except ImportError:
    pass

from rq1 import BaseUrl, Client
from rq1.issue import Issue
from rq1.models import IssueProperty

sys.path.append('src')
from config import RQ1_TOOLNAME, RQ1_TOOLVERSION
from rules.rule_prpl_18_ifd_isw_commitment_delay import Rule_IFD_ISW_Commitment_Delay
from rules.rule_prpl_06_ifd_defect_attributes import Rule_Ifd_DefectAttributes

# --- Config ---
TARGET_IFD_IDS = ["RQONE04984940", "RQONE04958329", "RQONE04979880"]
# --------------

client = Client(
    base_url=BaseUrl.PRODUCTIVE,
    username=os.getenv("RQ1_USER"),
    password=os.getenv("RQ1_PASSWORD"),
    toolname=RQ1_TOOLNAME,
    toolversion=RQ1_TOOLVERSION
)

for ifd_id in TARGET_IFD_IDS:
    print(f"\n{'='*60}")
    print(f"Testing Rule 18 for: {ifd_id}")

    r = client.query(
        Issue,
        where=(IssueProperty.id == ifd_id),
        select=[
            IssueProperty.id,
            IssueProperty.lifecyclestate,
            IssueProperty.cq__Type,
            IssueProperty.dcterms__title,
            IssueProperty.category,
            "cq:hasParent{cq:id,cq:LifeCycleState,cq:Type}"
        ]
    )

    if not r.members:
        print(f"  NOT FOUND")
        continue

    issue = r.members[0]
    print(f"  IFD state : {issue.lifecyclestate}")
    print(f"  IFD type  : {issue.cq__Type}")

    hasparent = getattr(issue, 'hasparent', None)
    parent_isw_data = None
    if hasparent:
        parent_isw_data = {
            'id': getattr(hasparent, 'id', 'UNKNOWN'),
            'lifecyclestate': getattr(hasparent, 'lifecyclestate', ''),
            'uri': getattr(hasparent, 'uri', None)
        }
        print(f"  Parent ISW: {parent_isw_data['id']} ({parent_isw_data['lifecyclestate']})")
    else:
        print(f"  No parent ISW found")

    issue_data = {
        'id': issue.id,
        'cq__Type': issue.cq__Type,
        'category': getattr(issue, 'category', ''),
        'lifecyclestate': issue.lifecyclestate,
        'dcterms__title': getattr(issue, 'dcterms__title', ''),
        # Defect attributes for rule 06 (may be None if not fetched)
        'defectdetectionlocation': getattr(issue, 'defectdetectionlocation', None),
        'defectdetectionprocess': getattr(issue, 'defectdetectionprocess', None),
        'defectdetectionorga': getattr(issue, 'defectdetectionorga', None),
        'defectdetectiondate': getattr(issue, 'defectdetectiondate', None),
        'defectiveworkproducttype': getattr(issue, 'defectiveworkproducttype', None),
        'defectclassification': getattr(issue, 'defectclassification', None),
        'defectinjectionorga': getattr(issue, 'defectinjectionorga', None),
        'defectinjectiondate': getattr(issue, 'defectinjectiondate', None),
    }

    # Rule 06
    rule06 = Rule_Ifd_DefectAttributes(issue_data)
    result06 = rule06.execute()
    status06 = "PASS" if result06.passed else f"FAIL ({result06.severity})"
    print(f"  Rule 06   : {status06} | {result06.description[:80].strip()}")

    # Rule 18
    rule = Rule_IFD_ISW_Commitment_Delay(issue_data, parent_isw_data, client=client)
    result = rule.execute()
    status = "PASS" if result.passed else f"FAIL ({result.severity})"
    print(f"  Rule 18   : {status}")
    print(f"  Detail    : {result.description.strip()}")
