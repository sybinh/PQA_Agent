"""
Test script to verify RQ1 connection and basic operations
Run this after PQA_Agent is whitelisted by RQ1 admins
"""
import os
import sys
from dotenv import load_dotenv
from rq1_client import RQ1Client

# Load environment variables from .env file
load_dotenv()


def test_connection():
    """Test basic RQ1 connection"""
    print("=" * 60)
    print("PQA_Agent - RQ1 Connection Test")
    print("=" * 60)
    
    # Get credentials from environment
    username = os.environ.get("RQ1_USER")
    password = os.environ.get("RQ1_PASSWORD")
    toolname = os.environ.get("RQ1_TOOLNAME", "PQA_Agent")
    toolversion = os.environ.get("RQ1_TOOLVERSION", "1.0.0")
    environment = os.environ.get("RQ1_ENVIRONMENT", "ACCEPTANCE")
    
    # Validate credentials
    if not username or not password:
        print("? ERROR: Missing RQ1_USER or RQ1_PASSWORD in .env file")
        print("\nPlease create .env file with:")
        print("  RQ1_USER=your_username")
        print("  RQ1_PASSWORD=your_password")
        print("  RQ1_ENVIRONMENT=ACCEPTANCE")
        return False
    
    print(f"\n?? Configuration:")
    print(f"  User: {username}")
    print(f"  Tool: {toolname} v{toolversion}")
    print(f"  Environment: {environment}")
    print(f"  Status: {'??  Needs whitelist approval' if toolname == 'PQA_Agent' else '? Using custom toolname'}")
    
    try:
        print("\n?? Initializing RQ1 client...")
        client = RQ1Client(
            username=username,
            password=password,
            toolname=toolname,
            toolversion=toolversion,
            environment=environment
        )
        print("? Client initialized successfully")
        
        # Test 1: Query My Issues (simple test)
        print("\n?? Test 1: Query My Issues")
        print("  Querying Issues assigned to you...")
        try:
            issues = client.query_my_issues()
            print(f"? Success! Found {len(issues)} Issues assigned to you")
            
            if issues:
                print("\n  First Issue:")
                first = issues[0]
                print(f"    RQ1: {first.get('cq__id', 'N/A')}")
                print(f"    Title: {first.get('dcterms__title', 'N/A')[:60]}...")
                print(f"    Status: {first.get('status', 'N/A')}")
        except Exception as e:
            print(f"? Failed: {str(e)}")
            if "401" in str(e) or "Unauthorized" in str(e):
                print("  ? Check: Username/Password correct?")
            elif "403" in str(e) or "Forbidden" in str(e):
                print("  ? Check: Is 'PQA_Agent' whitelisted by RQ1 admins?")
            return False
        
        # Test 2: Query with filters (if Test 1 passed)
        print("\n?? Test 2: Query Issues with filters")
        print("  Querying Issues with status='Open'...")
        try:
            issues = client.query_issues(status="Open")
            print(f"? Success! Found {len(issues)} Open Issues")
        except Exception as e:
            print(f"? Failed: {str(e)}")
        
        # Test 3: Get Issue details (if we have an Issue)
        if issues and len(issues) > 0:
            print("\n?? Test 3: Get Issue Details")
            test_rq1 = issues[0].get('cq__id')
            if test_rq1:
                print(f"  Getting details for Issue: {test_rq1}")
                try:
                    issue_detail = client.get_record_by_rq1_number(test_rq1)
                    print(f"? Success! Retrieved Issue details")
                    print(f"    Fields: {len(issue_detail)} properties")
                except Exception as e:
                    print(f"? Failed: {str(e)}")
        
        print("\n" + "=" * 60)
        print("? All basic tests passed!")
        print("=" * 60)
        print("\n? PQA_Agent is ready to use!")
        return True
        
    except Exception as e:
        print(f"\n? Connection failed: {str(e)}")
        
        # Provide helpful error messages
        error_str = str(e).lower()
        if "401" in error_str or "unauthorized" in error_str:
            print("\n?? Troubleshooting:")
            print("  1. Check RQ1_USER and RQ1_PASSWORD are correct")
            print("  2. Try logging in to RQ1 web interface to verify credentials")
        elif "403" in error_str or "forbidden" in error_str:
            print("\n?? Troubleshooting:")
            print("  1. Tool 'PQA_Agent' may not be whitelisted yet")
            print("  2. Contact RQ1 administrators to register 'PQA_Agent'")
            print("  3. Wait for approval, then try again")
        elif "connection" in error_str or "network" in error_str:
            print("\n?? Troubleshooting:")
            print("  1. Check network connection to RQ1 server")
            print("  2. Try accessing RQ1 web interface")
            print("  3. Check if VPN is required")
        
        return False


if __name__ == "__main__":
    print("\n")
    success = test_connection()
    print("\n")
    sys.exit(0 if success else 1)
