"""
RQ1 Relationship Helper
=======================

Helper functions to fetch and resolve RQ1 relationships:
- IRM (Issue Release Map): Issue ? Release mappings
- RRM (Release Release Map): Release ? Release mappings
"""

from typing import List, Optional, Dict, Any
from rq1 import Client
from rq1.issue import Issue
from rq1.release import Release
from rq1.issuereleasemap import Issuereleasemap
from rq1.releasereleasemap import Releasereleasemap
from rq1.models import IssuereleasemapProperty, ReleasereleasemapProperty


class RelationshipHelper:
    """Helper class for fetching RQ1 relationships."""
    
    def __init__(self, client: Client):
        """
        Args:
            client: RQ1 Client instance
        """
        self.client = client
    
    def get_issue_releases(self, issue_rq1: str) -> List[Release]:
        """
        Get all releases mapped to an issue via IRM.
        
        Args:
            issue_rq1: Issue RQ1 number (e.g., "RQONE04736888")
            
        Returns:
            List of Release objects mapped to this issue
        """
        try:
            # Query IRM records for this issue
            irm_query = self.client.query(
                Issuereleasemap,
                where=f'cq:belongsToIssue/dcterms:identifier="{issue_rq1}"',
                select=[
                    IssuereleasemapProperty.identifier,
                    IssuereleasemapProperty.lifecyclestate,
                    IssuereleasemapProperty.hasmappedissue,
                    IssuereleasemapProperty.hasmappedrelease
                ],
                page_size=50
            )
            
            releases = []
            for irm in irm_query.members:
                # Skip canceled/conflicted mappings
                if irm.lifecyclestate in ['Canceled', 'Conflicted']:
                    continue
                
                # Resolve release URI to Release object
                if irm.hasmappedrelease and hasattr(irm.hasmappedrelease, 'uri'):
                    try:
                        release = self.client.get_record_by_uri(Release, irm.hasmappedrelease.uri)
                        releases.append(release)
                    except Exception as e:
                        print(f"[WARN] Failed to resolve release {irm.hasmappedrelease.uri}: {e}")
            
            return releases
            
        except Exception as e:
            print(f"[ERROR] Failed to get IRM for {issue_rq1}: {e}")
            return []
    
    def get_release_issues(self, release_rq1: str) -> List[Issue]:
        """
        Get all issues mapped to a release via IRM.
        
        Args:
            release_rq1: Release RQ1 number
            
        Returns:
            List of Issue objects mapped to this release
        """
        try:
            irm_query = self.client.query(
                Issuereleasemap,
                where=f'cq:belongsToRelease/dcterms:identifier="{release_rq1}"',
                select=[
                    IssuereleasemapProperty.identifier,
                    IssuereleasemapProperty.lifecyclestate,
                    IssuereleasemapProperty.hasmappedissue,
                    IssuereleasemapProperty.hasmappedrelease
                ],
                page_size=50
            )
            
            issues = []
            for irm in irm_query.members:
                if irm.lifecyclestate in ['Canceled', 'Conflicted']:
                    continue
                
                if irm.hasmappedissue and hasattr(irm.hasmappedissue, 'uri'):
                    try:
                        issue = self.client.get_record_by_uri(Issue, irm.hasmappedissue.uri)
                        issues.append(issue)
                    except Exception as e:
                        print(f"[WARN] Failed to resolve issue {irm.hasmappedissue.uri}: {e}")
            
            return issues
            
        except Exception as e:
            print(f"[ERROR] Failed to get IRM for {release_rq1}: {e}")
            return []
    
    def get_parent_releases(self, child_release_rq1: str) -> List[Release]:
        """
        Get parent releases (e.g., BC parents of FC, PVER parents of BC).
        
        Args:
            child_release_rq1: Child release RQ1 number
            
        Returns:
            List of parent Release objects
        """
        try:
            # Query RRM where child is the "to" release
            rrm_query = self.client.query(
                Releasereleasemap,
                where=f'cq:toRelease/dcterms:identifier="{child_release_rq1}"',
                select=[
                    ReleasereleasemapProperty.identifier,
                    ReleasereleasemapProperty.lifecyclestate,
                    ReleasereleasemapProperty.hasmappedparentrelease,
                    ReleasereleasemapProperty.hasmappedchildrelease
                ],
                page_size=50
            )
            
            parents = []
            for rrm in rrm_query.members:
                if rrm.lifecyclestate in ['Canceled', 'Conflicted']:
                    continue
                
                if rrm.hasmappedparentrelease and hasattr(rrm.hasmappedparentrelease, 'uri'):
                    try:
                        release = self.client.get_record_by_uri(Release, rrm.hasmappedparentrelease.uri)
                        parents.append(release)
                    except Exception as e:
                        print(f"[WARN] Failed to resolve parent release {rrm.hasmappedparentrelease.uri}: {e}")
            
            return parents
            
        except Exception as e:
            print(f"[ERROR] Failed to get RRM parents for {child_release_rq1}: {e}")
            return []
    
    def get_child_releases(self, parent_release_rq1: str) -> List[Release]:
        """
        Get child releases (e.g., FC children of BC, BC children of PVER).
        
        Args:
            parent_release_rq1: Parent release RQ1 number
            
        Returns:
            List of child Release objects
        """
        try:
            rrm_query = self.client.query(
                Releasereleasemap,
                where=f'cq:fromRelease/dcterms:identifier="{parent_release_rq1}"',
                select=[
                    ReleasereleasemapProperty.identifier,
                    ReleasereleasemapProperty.lifecyclestate,
                    ReleasereleasemapProperty.hasmappedparentrelease,
                    ReleasereleasemapProperty.hasmappedchildrelease
                ],
                page_size=50
            )
            
            children = []
            for rrm in rrm_query.members:
                if rrm.lifecyclestate in ['Canceled', 'Conflicted']:
                    continue
                
                if rrm.hasmappedchildrelease and hasattr(rrm.hasmappedchildrelease, 'uri'):
                    try:
                        release = self.client.get_record_by_uri(Release, rrm.hasmappedchildrelease.uri)
                        children.append(release)
                    except Exception as e:
                        print(f"[WARN] Failed to resolve child release {rrm.hasmappedchildrelease.uri}: {e}")
            
            return children
            
        except Exception as e:
            print(f"[ERROR] Failed to get RRM children for {parent_release_rq1}: {e}")
            return []
    
    def get_pst_mapping(self, bc_rq1: str) -> Optional[Dict[str, Any]]:
        """
        Get PST (PVER) mapping for a BC release (used by PRPL 07).
        
        Args:
            bc_rq1: BC Release RQ1 number
            
        Returns:
            Dict with PST info: {
                'pst_rq1': str,
                'pst_delivery_date': date,
                'rrm_state': str
            }
        """
        try:
            # Query RRM from BC to PVER
            rrm_query = self.client.query(
                Releasereleasemap,
                where=f'cq:fromRelease/dcterms:identifier="{bc_rq1}"',
                select=[
                    ReleasereleasemapProperty.identifier,
                    ReleasereleasemapProperty.lifecyclestate,
                    ReleasereleasemapProperty.hasmappedchildrelease,
                    ReleasereleasemapProperty.requesteddeliverydate
                ],
                page_size=10
            )
            
            # Find first PVER (PST) mapping
            for rrm in rrm_query.members:
                if rrm.hasmappedchildrelease and hasattr(rrm.hasmappedchildrelease, 'uri'):
                    try:
                        pst = self.client.get_record_by_uri(Release, rrm.hasmappedchildrelease.uri)
                        # Check if it's PST (PVER subtype)
                        if hasattr(pst, 'subcategory') and pst.subcategory == 'PST':
                            return {
                                'pst_rq1': pst.identifier,
                                'pst_delivery_date': rrm.requesteddeliverydate,
                                'rrm_state': rrm.lifecyclestate
                            }
                    except Exception as e:
                        print(f"[WARN] Failed to check PST release: {e}")
            
            return None
            
        except Exception as e:
            print(f"[ERROR] Failed to get PST mapping for {bc_rq1}: {e}")
            return None
