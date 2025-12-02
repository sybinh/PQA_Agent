"""
RQ1 Client - Direct OSLC API client
This module provides a Python interface to RQ1 using direct REST API calls
(Alternative to building-block-rq1 when proxy/network blocks artifactory access)
"""
import logging
import os
import requests
from typing import Any, Dict, List, Optional
from requests.auth import HTTPBasicAuth

logger = logging.getLogger(__name__)

# Default Tool Registration
__version__ = "1.0.0"
DEFAULT_TOOLNAME = "OfficeUtils"  # POST tool's registered name in RQ1 whitelist
DEFAULT_TOOLVERSION = "1.0"


class RQ1Client:
    """
    Simple client for RQ1 OSLC API using direct REST calls
    """
    
    # RQ1 OSLC endpoints (actual endpoints from POST tool)
    ENVIRONMENTS = {
        'ACCEPTANCE': 'https://rb-dgsrq1-oslc-q.de.bosch.com/cqweb/oslc/repo/RQ1_ACCEPTANCE/db/RQONE',
        'PRODUCTIVE': 'https://rb-dgsrq1-oslc.de.bosch.com/cqweb/oslc/repo/RQ1_PRODUCTIVE/db/RQONE'
    }
    
    def __init__(self, 
                 username: str = None, 
                 password: str = None, 
                 environment: str = 'ACCEPTANCE',
                 toolname: str = DEFAULT_TOOLNAME,
                 toolversion: str = DEFAULT_TOOLVERSION,
                 verify_ssl: bool = True):
        """
        Initialize RQ1 Client
        
        Args:
            username: RQ1 username (default: from RQ1_USER env var)
            password: RQ1 password (default: from RQ1_PASSWORD env var)
            environment: ACCEPTANCE or PRODUCTIVE
            toolname: Tool name registered in RQ1 whitelist (default: OfficeUtils)
            toolversion: Tool version (default: 1.0)
            verify_ssl: Whether to verify SSL certificates (set False for dev/testing)
        """
        self.username = username or os.getenv('RQ1_USER')
        self.password = password or os.getenv('RQ1_PASSWORD')
        self.environment = environment.upper()
        self.toolname = toolname
        self.toolversion = toolversion
        self.verify_ssl = verify_ssl
        
        if not self.username or not self.password:
            raise ValueError("Username and password required (set RQ1_USER and RQ1_PASSWORD env vars or pass to constructor)")
        
        base_url = self.ENVIRONMENTS.get(self.environment)
        if not base_url:
            raise ValueError(f"Invalid environment: {environment}. Use ACCEPTANCE or PRODUCTIVE")
        
        # Ensure base_url ends with /
        self.base_url = base_url if base_url.endswith('/') else base_url + '/'
        
        # Create requests session with authentication
        self.session = requests.Session()
        self.session.verify = False  # Disable SSL verification (like building-block-rq1)
        self.session.auth = HTTPBasicAuth(self.username, self.password)
        self.session.headers.update({
            'OSLC-Core-Version': '2.0',
            'x-requester': f'toolname={self.toolname};toolversion={self.toolversion};user={self.username}',
            'Accept': 'application/json'
        })
        
        # Disable SSL warnings if verify_ssl is False
        if not self.verify_ssl:
            import urllib3
            urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)
        
        logger.info(f"RQ1 Client initialized for user: {username} on {environment}")
    
    def get_issue_summary(self, rq1_number: str) -> Optional[Dict[str, Any]]:
        """
        Get a formatted summary of an issue with key fields
        
        Args:
            rq1_number: The RQ1 number
            
        Returns:
            Dictionary with formatted key fields or None if not found
        """
        issue = self.get_record_by_rq1_number(rq1_number)
        if not issue:
            return None
        
        # Extract key fields with proper formatting
        assignee = issue.get('cq:Assignee', {})
        if isinstance(assignee, dict):
            assignee_id = assignee.get('rdf:resource', '').split('/')[-1]
        else:
            assignee_id = str(assignee)
        
        creator = issue.get('dcterms:creator', {})
        if isinstance(creator, dict):
            creator_name = creator.get('rdf:resource', '').split('/')[-1]
        else:
            creator_name = str(creator)
        
        return {
            'id': issue.get('cq:id'),
            'title': issue.get('dcterms:title'),
            'state': issue.get('cq:LifeCycleState'),
            'category': issue.get('cq:Category'),
            'domain': issue.get('cq:Domain'),
            'assignee': assignee_id,
            'creator': creator_name,
            'submit_date': issue.get('cq:SubmitDate'),
            'account_numbers': issue.get('cq:AccountNumbers'),
            'classification': issue.get('cq:CommercialClassification'),
            'allocation': issue.get('cq:Allocation'),
            'description': issue.get('dcterms:description'),
            'full_data': issue  # Keep full data for advanced processing
        }
    
    def get_record_by_rq1_number(self, rq1_number: str) -> Optional[Dict[str, Any]]:
        """
        Retrieve detailed information about an Issue by RQ1 number
        
        Args:
            rq1_number: The RQ1 number (e.g., "RQONE04815588" or "04815588")
            
        Returns:
            Dictionary with Issue details or None if not found
        """
        logger.info(f"Fetching Issue: {rq1_number}")
        
        # Query using cq:id (building-block-rq1 format)
        # 16777231 is Issue shape_id
        url = f"{self.base_url}simpleQuery/16777231"
        params = {
            'oslc.where': f'cq:id="{rq1_number}"',
            'oslc.select': '*',
            'oslc.pageSize': 1
        }
        
        try:
            response = self.session.get(url, params=params, verify=self.verify_ssl, timeout=30)
            
            if response.status_code >= 400:
                logger.error(f"Response: {response.text}")
                
            response.raise_for_status()
            
            data = response.json()
            # OSLC response structure: rdfs:member contains the results
            members = data.get('rdfs:member', [])
            total_count = data.get('oslc:responseInfo', {}).get('oslc:totalCount', 0)
            
            if members and len(members) > 0:
                logger.info(f"Successfully fetched Issue: {rq1_number}")
                return members[0]
            else:
                logger.warning(f"Issue not found: {rq1_number} (total_count={total_count})")
                return None
            
        except requests.exceptions.RequestException as e:
            logger.error(f"Error fetching Issue {rq1_number}: {str(e)}")
            raise
    
    def query_my_issues(self) -> List[Dict[str, Any]]:
        """
        Query all Issues assigned to the current user
        
        Returns:
            List of Issue summaries
        """
        logger.info(f"Querying Issues for user: {self.username}")
        
        return self.query_issues(assignee=self.username)
    
    def query_issues(self, 
                     title: Optional[str] = None, 
                     status: Optional[str] = None,
                     assignee: Optional[str] = None,
                     limit: int = 100) -> List[Dict[str, Any]]:
        """
        Query Issues by title, status, assignee, or other criteria
        
        Args:
            title: Optional title filter (partial match)
            status: Optional status filter
            assignee: Optional assignee filter
            limit: Maximum number of results (default: 100)
            
        Returns:
            List of matching Issues
        """
        logger.info(f"Querying Issues with title='{title}', status={status}, assignee={assignee}")
        
        # 16777231 is Issue shape_id
        url = f"{self.base_url}simpleQuery/16777231"
        params = {
            'oslc.select': '*',
            'oslc.pageSize': limit
        }
        
        # Build OSLC where clause
        conditions = []
        if title:
            conditions.append(f'dcterms:title="{title}"')
        if status:
            conditions.append(f'state="{status}"')
        if assignee:
            conditions.append(f'owner="{assignee}"')
        
        if conditions:
            params['oslc.where'] = ' and '.join(conditions)
        else:
            params['oslc.where'] = None
        
        try:
            response = self.session.get(url, params=params, verify=self.verify_ssl, timeout=30)
            response.raise_for_status()
            
            data = response.json()
            members = data.get('rdfs:member', [])
            
            logger.info(f"Found {len(members)} Issues matching criteria")
            return members
            
        except requests.exceptions.RequestException as e:
            logger.error(f"Error querying Issues: {str(e)}")
            raise
    
    def get_issue_history(self, rq1_number: str) -> List[Dict[str, Any]]:
        """
        Get the change history for an Issue
        
        Args:
            rq1_number: The RQ1 number (e.g., "RQONE04815588" or "04815588")
            
        Returns:
            List of History records
        """
        logger.info(f"Fetching history for Issue: {rq1_number}")
        
        # Query history using simpleQuery
        # 16777224 is History shape_id
        url = f"{self.base_url}simpleQuery/16777224"
        params = {
            'oslc.where': f'cq:id="{rq1_number}"',
            'oslc.select': '*',
            'oslc.pageSize': 100
        }
        
        try:
            response = self.session.get(url, params=params, verify=self.verify_ssl, timeout=30)
            response.raise_for_status()
            
            data = response.json()
            members = data.get('rdfs:member', [])
            
            logger.info(f"Found {len(members)} history records for Issue: {rq1_number}")
            return members
            
        except requests.exceptions.RequestException as e:
            logger.error(f"Error fetching history for Issue {rq1_number}: {str(e)}")
            raise
    
    def test_connection(self) -> bool:
        """
        Test connection to RQ1 by looking up current user
        
        Returns:
            True if connection successful, False otherwise
        """
        # 16777228 is Users shape_id
        url = f"{self.base_url}simpleQuery/16777228"
        params = {
            'oslc.where': f'login_name="{self.username}"',
            'oslc.select': 'fullname,email',
            'oslc.pageSize': 1
        }
        
        try:
            response = self.session.get(url, params=params, verify=self.verify_ssl, timeout=30)
            response.raise_for_status()
            
            data = response.json()
            members = data.get('rdfs:member', [])
            
            if members and len(members) > 0:
                user = members[0]
                logger.info(f"? Connected to RQ1 {self.environment}")
                logger.info(f"? User: {user.get('cq:fullname', self.username)}")
                return True
            else:
                logger.warning(f"? User {self.username} not found in RQ1")
                return False
                
        except requests.exceptions.RequestException as e:
            logger.error(f"? Connection failed: {str(e)}")
            return False


# Example usage
if __name__ == "__main__":
    # Configure logging
    logging.basicConfig(level=logging.INFO, format='%(levelname)s: %(message)s')
    
    # Test connection with credentials
    client = RQ1Client(
        username="DAB5HC",
        password="12021998@abcD",
        environment="ACCEPTANCE",
        verify_ssl=False  # Disable SSL verification for testing
    )
    
    print("\n=== Testing RQ1 Connection ===")
    print("Fetching issue RQONE03999302...")
    
    try:
        # Test full data fetch
        issue = client.get_record_by_rq1_number("RQONE03999302")
        if issue:
            print(f"\n? Successfully connected to RQ1 {client.environment}!")
            print(f"? Full issue data retrieved")
            print(f"  Total fields: {len(issue)}")
            print(f"  Non-empty fields: {len([k for k,v in issue.items() if v not in ['', [], {}]])}")
            
            # Test formatted summary
            print("\n=== Issue Summary ===")
            summary = client.get_issue_summary("RQONE03999302")
            if summary:
                print(f"  ID: {summary['id']}")
                print(f"  Title: {summary['title'][:80]}...")
                print(f"  State: {summary['state']}")
                print(f"  Category: {summary['category']}")
                print(f"  Domain: {summary['domain']}")
                print(f"  Assignee: {summary['assignee']}")
                print(f"  Submit Date: {summary['submit_date']}")
                print(f"  Allocation: {summary['allocation']}")
            
            print(f"\n? RQ1 Client working perfectly!")
            print(f"? Can fetch full data ({len(issue)} fields)")
            print(f"? Can create formatted summaries")
        else:
            print("? Issue not found")
    except Exception as e:
        print(f"\n? Failed to connect: {e}")
