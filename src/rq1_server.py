"""
RQ1 MCP Server - Integration with RQ1/ClearQuest system
Provides tools for Issue/Workitem management using building-block-rq1 library
"""
import os
import logging
from typing import Any
from mcp.server.fastmcp import FastMCP
from rq1_client import RQ1Client, DEFAULT_TOOLNAME, DEFAULT_TOOLVERSION

# Configure logging to stderr (not stdout for MCP servers)
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[logging.StreamHandler()]  # This writes to stderr by default
)
logger = logging.getLogger(__name__)

# Initialize FastMCP server
mcp = FastMCP("rq1-server")

# Initialize RQ1 client (will be done on first use)
rq1_client: RQ1Client | None = None


def get_rq1_client() -> RQ1Client:
    """Get or create RQ1 client instance"""
    global rq1_client
    if rq1_client is None:
        rq1_user = os.environ.get("RQ1_USER")
        rq1_password = os.environ.get("RQ1_PASSWORD")
        rq1_toolname = os.environ.get("RQ1_TOOLNAME", DEFAULT_TOOLNAME)
        rq1_toolversion = os.environ.get("RQ1_TOOLVERSION", DEFAULT_TOOLVERSION)
        rq1_environment = os.environ.get("RQ1_ENVIRONMENT", "PRODUCTIVE")
        
        if not all([rq1_user, rq1_password]):
            raise ValueError(
                "Missing required environment variables: RQ1_USER, RQ1_PASSWORD"
            )
        
        rq1_client = RQ1Client(
            username=rq1_user,
            password=rq1_password,
            toolname=rq1_toolname,
            toolversion=rq1_toolversion,
            environment=rq1_environment
        )
        logger.info("RQ1 client initialized successfully")
    
    return rq1_client


@mcp.tool()
def get_issue_details(rq1_number: str) -> dict[str, Any]:
    """
    Retrieve detailed information about a specific RQ1 Issue record.
    
    Args:
        rq1_number: The RQ1 number (format: RQ1234567)
        
    Returns:
        Dictionary containing Issue details including:
        - rq1_number: The Issue identifier
        - dcterms__title: Issue title/summary
        - status: Current Issue status
        - assignee: Person assigned to the Issue
        - effort: Estimated effort in hours
        - package: Package/component name
        - delivery_plan: Delivery plan information
        - dcterms__description: Full Issue description
        - submitdate: When the Issue was created
        - modifydate: Last modification date
    """
    logger.info(f"Fetching details for Issue: {rq1_number}")
    
    try:
        client = get_rq1_client()
        issue = client.get_record_by_rq1_number(rq1_number)
        
        if not issue:
            return {
                "error": f"Issue {rq1_number} not found",
                "rq1_number": rq1_number
            }
        
        logger.info(f"Successfully retrieved Issue {rq1_number}")
        return issue
        
    except Exception as e:
        logger.error(f"Error fetching Issue {rq1_number}: {str(e)}")
        return {
            "error": f"Failed to fetch Issue: {str(e)}",
            "rq1_number": rq1_number
        }


@mcp.tool()
def query_my_issues() -> list[dict[str, Any]]:
    """
    Query all Issues assigned to the current user.
    
    Returns:
        List of Issue records, each containing:
        - rq1_number: The Issue identifier
        - dcterms__title: Issue title
        - status: Current status
        - priority: Issue priority
        - effort: Estimated effort
    """
    logger.info("Querying Issues for current user")
    
    try:
        client = get_rq1_client()
        issues = client.query_my_issues()
        
        logger.info(f"Found {len(issues)} Issues for current user")
        return issues
        
    except Exception as e:
        logger.error(f"Error querying Issues: {str(e)}")
        return [{
            "error": f"Failed to query Issues: {str(e)}"
        }]


@mcp.tool()
def query_issues(title: str | None = None, status: str | None = None) -> list[dict[str, Any]]:
    """
    Query Issues by title, status, or other criteria using OSLC query.
    
    Args:
        title: Optional title filter (searches in dcterms:title)
        status: Optional status filter (e.g., "Open", "In Progress", "Closed")
        
    Returns:
        List of matching Issue records with summary information
    """
    logger.info(f"Querying Issues with title: '{title}', status: {status}")
    
    try:
        client = get_rq1_client()
        issues = client.query_issues(title=title, status=status)
        
        logger.info(f"Found {len(issues)} Issues matching query criteria")
        return issues
        
    except Exception as e:
        logger.error(f"Error querying Issues: {str(e)}")
        return [{
            "error": f"Failed to query Issues: {str(e)}"
        }]


@mcp.tool()
def get_issue_history(rq1_number: str) -> list[dict[str, Any]]:
    """
    Retrieve the History records for an Issue.
    
    Args:
        rq1_number: The RQ1 number (format: RQ1234567)
        
    Returns:
        List of History records, each containing:
        - dcterms__title: History entry title
        - submitdate: When the change occurred
        - submitter: Who made the change
        - dcterms__description: Description of the change
        - belongsToIssue: Reference to the Issue
    """
    logger.info(f"Fetching History for Issue: {rq1_number}")
    
    try:
        client = get_rq1_client()
        history_records = client.get_issue_history(rq1_number)
        
        logger.info(f"Retrieved {len(history_records)} History entries for Issue {rq1_number}")
        return history_records
        
    except Exception as e:
        logger.error(f"Error fetching Issue History: {str(e)}")
        return [{
            "error": f"Failed to fetch History: {str(e)}",
            "rq1_number": rq1_number
        }]


def main():
    """Run the MCP server"""
    logger.info("Starting RQ1 MCP Server")
    
    # Verify environment variables are set
    try:
        get_rq1_client()
        logger.info("RQ1 client initialized successfully")
    except ValueError as e:
        logger.error(f"Configuration error: {str(e)}")
        logger.error("Please set all required environment variables")
        return
    
    # Run the server
    mcp.run(transport='stdio')


if __name__ == "__main__":
    main()
