#!/usr/bin/env python3
"""
Setup RQ1 Credentials (Secure)
Prompts user for password (masked input) and stores in environment variables.
Password persists only for current terminal session.
"""

import os
import sys
try:
    import pwinput
except ImportError:
    import getpass
    pwinput = None
from dotenv import load_dotenv

def setup_credentials():
    """
    Securely prompt for RQ1 credentials and store in environment variables.
    Password is masked during input and only stored in memory (not disk).
    """
    print("=" * 80)
    print("RQ1 Credentials Setup (Secure)")
    print("=" * 80)
    print()
    
    # Load existing .env for other settings (toolname, toolversion)
    load_dotenv()
    
    # Get username (read from .env if exists)
    default_user = os.getenv("RQ1_USER", "")
    if default_user:
        print(f"Username from .env: {default_user}")
        username = default_user
    else:
        username = input("RQ1 Username: ").strip()
    
    if not username:
        print("ERROR: Username is required!")
        sys.exit(1)
    
    # Get password (masked input with asterisks)
    print()
    if pwinput:
        password = pwinput.pwinput("RQ1 Password: ", mask='*')
    else:
        password = getpass.getpass("RQ1 Password: ")
    
    if not password:
        print("ERROR: Password is required!")
        sys.exit(1)
    
    # Store in environment variables (session-level)
    os.environ["RQ1_USER"] = username
    os.environ["RQ1_PASSWORD"] = password
    
    print()
    print("=" * 80)
    print("? Credentials stored in environment variables (session only)")
    print("=" * 80)
    print()
    print("Environment variables set:")
    print(f"  RQ1_USER = {username}")
    print(f"  RQ1_PASSWORD = ******** (hidden)")
    print()
    print("You can now run:")
    print("  python validate_user_items.py <username>")
    print("  python mcp_server_fast.py")
    print()
    print("Note: Credentials will be cleared when you close this terminal.")
    print("      Re-run this script after reopening terminal.")
    print()
    
    return True

if __name__ == "__main__":
    try:
        setup_credentials()
    except KeyboardInterrupt:
        print("\n\nSetup cancelled by user.")
        sys.exit(1)
    except Exception as e:
        print(f"\nERROR: {e}")
        sys.exit(1)
