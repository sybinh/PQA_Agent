"""
RQ1 Client - Wrapper for RQ1/ClearQuest API interactions
This module provides a Python interface to the RQ1 system using building-block-rq1
"""
import logging
import os
from typing import Any, Optional
import rq1
from rq1 import Issue, IssueProperty, History

logger = logging.getLogger(__name__)

# Default Tool Registration
# These are used if RQ1_TOOLNAME/RQ1_TOOLVERSION are not set in environment
# Update these to match YOUR tool name and version
__version__ = "1.0.0"  # PQA_Agent version
DEFAULT_TOOLNAME = "PQA_Agent"  # Tool name (must be whitelisted by RQ1 admins)
DEFAULT_TOOLVERSION = __version__


class RQ1Client:
    """
    Client for interacting with RQ1/ClearQuest system using building-block-rq1 library
    """
    
    def __init__(self, username: str, password: str, toolname: str, toolversion: str, 
                 environment: str = "PRODUCTIVE"):
        """
        Initialize RQ1 client with credentials
        
        Args:
            username: RQ1 username
            password: RQ1 password
            toolname: YOUR tool name - must be whitelisted by RQ1 admins
                      (e.g., "MyDevTool", "AutomationScript", "rq1-mcp-server")
            toolversion: YOUR tool version (e.g., "1.0.0", "2.1.3")
            environment: RQ1 environment (PRODUCTIVE, ACCEPTANCE, APPROVAL). Default: PRODUCTIVE
        """
        self.username = username
        self.password = password
        self.toolname = toolname
        self.toolversion = toolversion
        self.environment = environment
        
        # Map environment to base URL
        env_urls = {
            "PRODUCTIVE": "https://rq1.bosch.com/jazz/oslc",
            "ACCEPTANCE": "https://rq1-test.bosch.com/jazz/oslc",
            "APPROVAL": "https://rq1-approval.bosch.com/jazz/oslc"
        }
        
        base_url = env_urls.get(environment.upper())
        if not base_url:
            raise ValueError(f"Invalid environment: {environment}. Must be one of: {list(env_urls.keys())}")
        
        # Initialize building-block-rq1 Client
        self.client = rq1.Client(
            base_url=base_url,
            username=username,
            password=password,
            toolname=toolname,
            toolversion=toolversion
        )
        
        logger.info(f"RQ1 Client initialized for user: {username} on {environment}")
    
    def get_record_by_rq1_number(self, rq1_number: str) -> dict[str, Any]:
        """
        Retrieve detailed information about an Issue by RQ1 number
        
        Args:
            rq1_number: The RQ1 number (e.g., "RQ1234567")
            
        Returns:
            Dictionary with Issue details
        """
        logger.info(f"Fetching Issue: {rq1_number}")
        
        try:
            # Get Issue record with all properties
            issue = self.client.get_record_by_rq1_number(
                rtype=Issue,
                rq1_num=rq1_number,
                select="*"  # Get all properties
            )
            
            # Convert Pydantic model to dict for JSON serialization
            issue_dict = issue.model_dump(mode='json', exclude_none=True)
            
            logger.info(f"Successfully fetched Issue: {rq1_number}")
            return issue_dict
            
        except Exception as e:
            logger.error(f"Error fetching Issue {rq1_number}: {str(e)}")
            raise
    
    def query_my_issues(self) -> list[dict[str, Any]]:
        """
        Query all Issues assigned to the current user
        
        Returns:
            List of Issue summaries
        """
        logger.info(f"Querying Issues for user: {self.username}")
        
        try:
            # Query Issues where assignee is current user
            # Note: Property name is 'assignee', not 'owner'
            query_result = self.client.query(
                rtype=Issue,
                where=f'assignee="{self.username}"',
                select=[
                    IssueProperty.ID,
                    IssueProperty.TITLE,
                    IssueProperty.STATUS,
                    IssueProperty.PRIORITY,
                    IssueProperty.EFFORT,
                    IssueProperty.PACKAGE,
                    IssueProperty.SUBMITDATE,
                    IssueProperty.MODIFYDATE
                ],
                paging=True,
                page_size=100
            )
            
            # Convert results to list of dicts
            issues = []
            for issue in query_result.members:
                issues.append(issue.model_dump(mode='json', exclude_none=True))
            
            logger.info(f"Found {len(issues)} Issues for user: {self.username}")
            return issues
            
        except Exception as e:
            logger.error(f"Error querying Issues for user {self.username}: {str(e)}")
            raise
    
    def query_issues(self, title: Optional[str] = None, status: Optional[str] = None) -> list[dict[str, Any]]:
        """
        Query Issues by title, status, or other criteria
        
        Args:
            title: Optional title filter (searches in dcterms:title)
            status: Optional status filter
            
        Returns:
            List of matching Issues
        """
        logger.info(f"Querying Issues with title='{title}', status={status}")
        
        try:
            # Build WHERE clause
            conditions = []
            if title:
                # Use LIKE for partial match
                conditions.append(f'dcterms:title like "%{title}%"')
            if status:
                conditions.append(f'status="{status}"')
            
            where_clause = " and ".join(conditions) if conditions else None
            
            # Query Issues
            query_result = self.client.query(
                rtype=Issue,
                where=where_clause,
                select=[
                    IssueProperty.ID,
                    IssueProperty.TITLE,
                    IssueProperty.STATUS,
                    IssueProperty.PRIORITY,
                    IssueProperty.ASSIGNEE,
                    IssueProperty.EFFORT,
                    IssueProperty.PACKAGE,
                    IssueProperty.SUBMITDATE
                ],
                paging=True,
                page_size=100
            )
            
            # Convert results to list of dicts
            issues = []
            for issue in query_result.members:
                issues.append(issue.model_dump(mode='json', exclude_none=True))
            
            logger.info(f"Found {len(issues)} Issues matching criteria")
            return issues
            
        except Exception as e:
            logger.error(f"Error querying Issues: {str(e)}")
            raise
    
    def get_issue_history(self, rq1_number: str) -> list[dict[str, Any]]:
        """
        Get the change history for an Issue
        
        Args:
            rq1_number: The RQ1 number (e.g., "RQ1234567")
            
        Returns:
            List of History records
        """
        logger.info(f"Fetching history for Issue: {rq1_number}")
        
        try:
            # First, get the Issue to obtain its URI
            issue = self.client.get_record_by_rq1_number(
                rtype=Issue,
                rq1_num=rq1_number,
                select=[IssueProperty.IDENTIFIER]  # Just need the URI
            )
            
            # Query History records for this Issue
            # History records have a relationship to the Issue via 'belongsToIssue'
            query_result = self.client.query(
                rtype=History,
                where=f'belongsToIssue="{issue.rdf__about}"',
                select="*",  # Get all history properties
                paging=True,
                page_size=100
            )
            
            # Convert results to list of dicts
            history_records = []
            for history in query_result.members:
                history_records.append(history.model_dump(mode='json', exclude_none=True))
            
            logger.info(f"Found {len(history_records)} history records for Issue: {rq1_number}")
            return history_records
            
        except Exception as e:
            logger.error(f"Error fetching history for Issue {rq1_number}: {str(e)}")
            raise
        
        # Placeholder implementation
        return {
            "ticket_id": ticket_id,
            "history": [
                {
                    "timestamp": "2025-01-15 10:30:00",
                    "user": "john.doe",
                    "field": "status",
                    "old_value": "Open",
                    "new_value": "In Progress"
                },
                {
                    "timestamp": "2025-01-10 14:20:00",
                    "user": "jane.smith",
                    "field": "assignee",
                    "old_value": "unassigned",
                    "new_value": self.username
                },
                {
                    "timestamp": "2025-01-01 09:00:00",
                    "user": "admin",
                    "field": "created",
                    "old_value": None,
                    "new_value": "Initial creation"
                }
            ]
        }
