# RQ1 PRPL Validation Tool

**Version 1.2** - Automated validation tool for RQ1 PRPL (Planning Quality Assurance) rules with MCP (Model Context Protocol) server integration.

## Overview

This tool validates RQ1 items (BC, IFD, Release, Workitem) against 12 PRPL business rules, providing detailed violation reports with severity levels and pass rate calculations.

**Supported Rules**: PRPL 01, 02, 03, 06, 07, 11, 12, 13, 14, 15, 16, 18

## Project Status

### ? Phase 1: RQ1 Data Access (COMPLETE)
- [x] Project structure setup
- [x] building-block-rq1 library integrated (v1.6.0)
- [x] RQ1 connection and authentication working
- [x] Data retrieval for all item types (BC, IFD, Release, Workitem)

### ? Phase 2: Rule Implementation (COMPLETE)
- [x] 12 PRPL rules implemented and tested
- [x] Excel rules analysis (101 rules: BBM 23 + QAM 22 + IPT 56)
- [x] Excel-to-Java mapping completed
- [x] Validation engine with severity levels (WARNING, INFO)
- [x] Pass rate calculation (WARNING-only)
- [x] Multi-user testing successful

### ? Phase 3: Integration (COMPLETE)
- [x] MCP server for AI assistant integration
- [x] Console validation tool
- [x] API validation tool (JSON output)
- [x] Executable packaging with encryption
- [x] Distribution package with offline install

## Features

### Validation Engine
- **12 PRPL Rules**: Comprehensive business rule validation
- **Severity Levels**: WARNING (impacts pass rate), INFO (informational)
- **Pass Rate**: Calculated from WARNING violations only
- **Detailed Reports**: Item ID, rule, description, severity
- **Multi-Item Support**: Validate all items for a user

### Deployment Modes
- **Executable Package**: No Python required, encrypted source code
- **Python Source**: For development and customization
- **Offline Install**: No GitHub/VPN needed, all dependencies included

### Integration Options
- **Console Tool**: Direct command line validation
- **MCP Server**: AI Assistant (Copilot/Claude) integration via FastAPI
- **API Tool**: JSON output for automation and scripting

## Project Structure

```
RQ1_agent/
??? docs/                    # Documentation
?   ??? RULES_COMPLETE.md   # All 101 rules consolidated
?   ??? EXCEL_TO_JAVA_MAPPING.md
?   ??? IMPLEMENTATION_GUIDE.md
??? scripts/                 # Excel parsing utilities
?   ??? parse_excel_rules.py
?   ??? excel_converter.py
?   ??? excel_change_detector.py
??? src/                     # Source code
?   ??? rq1_client.py       # RQ1 client implementation
?   ??? rq1_server.py       # MCP server
?   ??? test_connection.py  # Connection testing
??? rq1/                     # RQ1 reference data
?   ??? POST_V_1.0.3/       # POST Tool v1.0.3
??? .env.example            # Environment template
```

## Installation

### Prerequisites

- Python 3.10 or higher
- `uv` package manager (recommended)
- Access to RQ1/ClearQuest system
- building-block-rq1 library (v1.6.0)

### Setup

1. Clone this repository:
```bash
git clone https://github.com/sybinh/PQA_Agent.git
cd RQ1_agent
```

2. Set up environment variables:
```bash
cp .env.example .env
# Edit .env with your credentials
```

3. Install dependencies:
```bash
uv pip install -r requirements.txt
```

3. Install dependencies:
```bash
uv pip install -r requirements.txt
```

## Usage

### Primary Method: VS Code Copilot Chat

**This is the main way to use the tool - just chat with Copilot!**

Open Copilot Chat (Ctrl+Shift+I) and ask:

```
@pqa_validator validate RQ1 items for user IOH81HC
```

```
@pqa_validator check PRPL compliance for DUY3HC
```

The tool understands natural language and validates automatically.

**How it works:**
- Your RQ1_USER (in `.env`) authenticates to RQ1
- You can ask to validate **any user** (including yourself)
- Results show in Copilot Chat with full details

### Alternative: Direct Script (Testing/Debugging Only)

For quick testing without Copilot:

```powershell
# Setup credentials first
.\setup_credentials.ps1

# Validate any user
python validate_user_items.py <username>

# Example
python validate_user_items.py IOH81HC
```

**Note:** This is mainly for debugging. Use Copilot Chat for production work.

### Running the MCP Server (for VS Code Copilot)

### Running the MCP Server (for VS Code Copilot)

```bash
python mcp_server_fast.py
```

### VS Code Copilot Configuration

### VS Code Copilot Configuration

Open VS Code Settings (Ctrl+,) ? Search "copilot mcp" ? Edit settings.json:

```json
{
  "github.copilot.chat.mcp.servers": {
    "pqa_validator": {
      "command": "python",
      "args": ["C:\\full\\path\\to\\pqa_validator\\mcp_server_fast.py"]
    }
  }
}
```

**Note**: Replace path with your actual installation directory.

## Available Commands

### Direct Script Execution

```powershell
# Validate any user's items against PRPL rules
python validate_user_items.py <username>
```

**Output includes:**
- Total items assigned (Issues, Releases, Workitems)
- Rule violations with detailed hints
- Pass rate percentage
- Comprehensive validation summary

### 1. `get_issue_details(rq1_number)`
Retrieve detailed information about a specific RQ1 Issue record.

### 2. `query_my_issues()`
Query all Issues assigned to the current user.

### 3. `query_issues(title, status)`
Query Issues by title, status, or other criteria using OSLC query.

### 4. `get_issue_history(rq1_number)`
Retrieve the History records for an Issue.

## Development Approach

### Phase 1: RQ1 Data Access (CURRENT)
**Goal**: Get data from RQ1 successfully

**Tasks**:
1. Implement `rq1_client.py` using building-block-rq1 library
   - Use `rq1.Client` or `rq1.AsyncClient`
   - Implement `get_record_by_rq1_number()`
   - Implement `query()` with WHERE clauses
   - Handle History records
2. Create `.env` with credentials
3. Test each tool individually with ACCEPTANCE environment
4. Verify data retrieval works correctly

**Success Criteria**: All 4 tools can retrieve data from RQ1

### Phase 2: Rule Checking (AFTER Phase 1)
**Goal**: TBD - Requirements needed

**Questions to answer first**:
- What rules need to be checked?
- Where do these rules come from?
- What happens when a rule fails?

### Phase 3: Integration (AFTER Phase 2)
**Goal**: Connect everything together and optimize

## Development

## Key Terminology (from building-block-rq1)

| Concept | Use This | NOT This |
|---------|----------|----------|
| Record type | **Issue** / **Workitem** | ~~ticket~~ |
| Identifier | **rq1_number** (RQ1234567) | ~~ticket_id~~ |
| Get operation | **get_record_by_rq1_number()** | ~~get_ticket()~~ |
| List operation | **query()** with WHERE | ~~list_tickets()~~ |
| Title property | **dcterms__title** | ~~title~~ |
| Description | **dcterms__description** | ~~description~~ |

## Documentation

All documentation has been organized in the `docs/` folder:

- **docs/RULES_COMPLETE.md** - All 101 Excel rules consolidated (BBM 23 + QAM 22 + IPT 56)
- **docs/EXCEL_TO_JAVA_MAPPING.md** - Mapping between Excel rules and Java implementation
- **docs/IMPLEMENTATION_GUIDE.md** - Step-by-step guide for implementing rules
- **docs/RQ1_LIBRARY_GUIDE.md** - Complete building-block-rq1 library documentation
- **docs/RQ1_RULES_DETAILED.md** - Detailed specifications for all 37 Java rules
- **docs/EXCEL_RULES_BY_PRIORITY.md** - Excel rules organized by priority

## Quick Links

- **Excel Rules Parser**: `parse_excel_rules.py` - Reusable script for future Excel versions
- **Original POST Tool**: `rq1/POST_V_1.0.3/` - Java implementation reference
- **Parsed Rules Output**: `rq1/POST_V_1.0.3/parsed_rules/` - BBM, QAM, IPT markdown files

## Next Steps

1. **Complete Phase 1**: Implement and test RQ1 data access
2. **Begin Phase 2A**: Implement Top 10 priority rules (~30-40 hours)
3. **Test with ACCEPTANCE**: Validate against real RQ1 data
4. **Iterate**: Move through remaining rules with Excel+Java backing
4. **Then** consider additional features (rule checking, etc.)
