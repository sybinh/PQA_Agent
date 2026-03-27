#!/usr/bin/env python
"""
PQA Validator MCP Server - FastMCP Implementation
For VS Code Copilot integration
"""

import os
import sys
from typing import Optional
from dotenv import load_dotenv
from fastmcp import FastMCP

# Load environment variables from .env file
load_dotenv()

# Add src to path
sys.path.append('src')

# Import RQ1 validation API
from validate_user_items_api import validate_user_items_api

# Initialize FastMCP server
mcp = FastMCP("PQA Validator")


@mcp.tool()
def validate_user_items(
    username: str,
    auth_user: Optional[str] = None,
    output_format: str = "text"
) -> str:
    """
    Validate all RQ1 items (Issues, Releases, Workitems) for a user against PRPL rules.
    
    Args:
        username: RQ1 username to validate (e.g., "DUY3HC")
        auth_user: Authenticated user (optional, defaults to RQ1_USER env)
        output_format: Output format - "text" or "json" (default: "text")
    
    Returns:
        Validation report with summary and violations
    """
    try:
        # Run validation
        result = validate_user_items_api(
            target_username=username,
            auth_user=auth_user
        )
        
        if output_format == "json":
            import json
            return json.dumps(result, indent=2)
        
        # Format as text
        output = []
        output.append("=" * 80)
        output.append(f"PRPL Rule Validation for User: {username}")
        output.append(f"Full Name: {result['user_fullname']}")
        if auth_user:
            output.append(f"(Authenticated as: {auth_user})")
        output.append("=" * 80)
        output.append("")
        
        # Summary
        output.append("VALIDATION SUMMARY")
        output.append("=" * 80)
        output.append(f"Total items assigned: {result['total_items']}")
        output.append(f"  - Issues: {result['issue_count']} (IFD={result['ifd_count']}, ISW={result['isw_count']})")
        output.append(f"  - Releases: {result['release_count']} ({result['release_breakdown']})")
        output.append(f"  - Workitems: {result['workitem_count']}")
        output.append(f"Total checks performed: {result['total_checks']}")
        output.append(f"Rules applied: {', '.join(result['rules_applied'])}")
        output.append(f"Violations found: {result['violation_count']}")
        output.append(f"Pass rate: {result['pass_rate']}%")
        output.append("")
        
        # Violations
        if result['violations']:
            output.append("=" * 80)
            output.append(f"VIOLATIONS FOUND ({len(result['violations'])})")
            output.append("=" * 80)
            output.append("")
            
            for i, v in enumerate(result['violations'], 1):
                output.append(f"[{i}] {v['rule_id']} - {v['severity']}")
                output.append(f"    Rule: {v['rule_desc']}")
                output.append(f"    Item: {v['item_id']}")
                output.append(f"    Title: {v['item_title']}")
                output.append(f"    {v['details']}")
                output.append("")
        else:
            output.append("? No violations found! All items comply with PRPL rules.")
        
        output.append("=" * 80)
        return "\n".join(output)
        
    except Exception as e:
        return f"Error validating user items: {str(e)}"


@mcp.tool()
def list_prpl_rules() -> str:
    """
    List all implemented PRPL rules with descriptions.
    
    Returns:
        List of all 11 implemented PRPL rules
    """
    rules = {
        "PRPL 01.00.00": "BC-R is not in requested state, 8 weeks before PVER planned delivery date",
        "PRPL 02.00.00": "Workitem is in started state, but planned date for workitem is not entered in planning tab",
        "PRPL 03.00.00": "Issue/Release/workitem is still in \"Conflicted\" state",
        "PRPL 07.00.00": "Planned date of BC later than requested delivery date of any mapped PVER or PVAR",
        "PRPL 11.00.00": "IFD 5 day SLA reached",
        "PRPL 12.00.00": "IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled",
        "PRPL 13.00.00": "IFD is not implemented or closed, after planned dated of BC-R",
        "PRPL 14.00.00": "IFD is not committed, eventhough attached Issue-SW is committed",
        "PRPL 15.00.00": "Release is not closed after planned date for BC and FC",
        "PRPL 16.00.00": "Workitem is not closed after planned date",
        "PRPL 18.00.00": "I-FD not committed 5 or more working days after attached I-SW was committed"
    }
    
    output = ["Available PRPL Rules (11 implemented)", "=" * 80, ""]
    for rule_id, description in rules.items():
        output.append(f"{rule_id}: {description}")
    output.append("")
    output.append("=" * 80)
    output.append("Phase 1: 11 priority rules ? Complete")
    output.append("Phase 2: 26 additional rules ?? In progress")
    output.append("Total planned: 101 rules (BBM 23 + QAM 22 + IPT 56)")
    
    return "\n".join(output)


@mcp.tool()
def check_rule_compliance(username: str, rule_id: str) -> str:
    """
    Check compliance statistics for a specific PRPL rule across user's items.
    
    Args:
        username: RQ1 username to check
        rule_id: PRPL rule ID (e.g., "PRPL 11", "PRPL 14")
    
    Returns:
        Compliance report for the specific rule
    """
    try:
        # Run validation
        result = validate_user_items_api(target_username=username)
        
        # Filter violations for specific rule
        rule_violations = [v for v in result['violations'] if v['rule_id'].startswith(rule_id)]
        
        output = []
        output.append(f"Rule Compliance Check: {rule_id}")
        output.append(f"User: {username}")
        output.append("=" * 80)
        output.append(f"Total items checked: {result['total_items']}")
        output.append(f"Violations found: {len(rule_violations)}")
        
        if len(rule_violations) > 0:
            compliance_rate = ((result['total_items'] - len(rule_violations)) / result['total_items'] * 100)
            output.append(f"Compliance rate: {compliance_rate:.1f}%")
            output.append("")
            output.append("Violations:")
            for v in rule_violations:
                output.append(f"  - {v['item_id']}: {v['item_title']}")
        else:
            output.append("Compliance rate: 100%")
            output.append("? All items comply with this rule!")
        
        return "\n".join(output)
        
    except Exception as e:
        return f"Error checking compliance: {str(e)}"


if __name__ == "__main__":
    # Run FastMCP server
    mcp.run()
