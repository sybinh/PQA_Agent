"""
RQ1 Agent Configuration
Contains constants and configuration values used across the application.
"""

import os
from dotenv import load_dotenv

# Load environment variables from .env
load_dotenv()

# RQ1 Tool Identification
# These values identify this tool to RQ1 servers for logging/auditing purposes
RQ1_TOOLNAME = "OfficeUtils"
RQ1_TOOLVERSION = "1.0"

# Environment
# Use PRODUCTIVE for real data, ACCEPTANCE for testing
RQ1_ENVIRONMENT = "PRODUCTIVE"  # Options: "PRODUCTIVE", "ACCEPTANCE"

# Project Configuration
# Project RQ1 IDs for validation scope (comma-separated list)
# Examples: "RQONE12345678" or "RQONE12345678,RQONE87654321"
_project_ids_raw = os.getenv('RQ1_PROJECT_IDS', '')
RQ1_PROJECT_IDS = [pid.strip() for pid in _project_ids_raw.split(',') if pid.strip()]  # Parse comma-separated list
