# PQA Agent - RQ1 MCP Server

**PQA_Agent v1.0.0** - An MCP (Model Context Protocol) server for integrating with RQ1/ClearQuest system using the `building-block-rq1` library.

## ?? Current Status: PHASE 1 - RQ1 Data Access

**Focus**: Build and test basic RQ1 data access before adding additional features.

### ? Completed
- Project structure setup
- building-block-rq1 library installed (v1.6.0)
- Terminology aligned with library conventions
- 4 MCP tools defined (placeholder implementations)

### ?? Current Task
- **Implement rq1_client.py** - Connect to RQ1 using building-block-rq1
- **Test connection** - Verify data retrieval works

### ?? Next Phases (After Phase 1 works)
- Phase 2: Add rule checking functionality (TBD - requirements needed)
- Phase 3: Integration and optimization

## Features

- **Get Issue Details**: Retrieve comprehensive information about RQ1 Issues by rq1_number
- **Query My Issues**: Query all Issues assigned to the current user
- **Query Issues**: Query Issues by title, status, or other criteria using OSLC query
- **Get Issue History**: View the History records for an Issue

## Installation

### Prerequisites

- Python 3.10 or higher
- `uv` package manager (recommended)
- Access to RQ1/ClearQuest system
- RQ1 Python library (to be provided)

### Setup

1. Clone this repository:
```bash
git clone <repository-url>
cd rq1-mcp-server
```

2. Set up environment variables:
```bash
# Copy example and edit with your credentials
cp .env.example .env

# Edit .env file:
RQ1_USER=your_bosch_username
RQ1_PASSWORD=your_password
RQ1_TOOLNAME=YourToolName        # Tên tool c?a B?N
RQ1_TOOLVERSION=1.0.0            # Version tool c?a B?N
RQ1_ENVIRONMENT=PRODUCTIVE       # ho?c ACCEPTANCE ?? test
```

**?? Important: Tool Registration**

`RQ1_TOOLNAME` và `RQ1_TOOLVERSION` là ?? **??nh danh tool c?a b?n**:

- **TOOLNAME**: Tên tool b?n ?ang phát tri?n (ví d?: "MyDevAssistant", "AutoQA", "TeamXTool")
- **TOOLVERSION**: Version c?a tool ?ó (ví d?: "1.0.0", "2.1.5")
- ?? **TOOLNAME ph?i ???c RQ1 admins whitelist** tr??c khi s? d?ng!

**Ví d? th?c t?**:
```bash
# Tool c?a team b?n tên "DevAssistant" version "1.2.0"
RQ1_TOOLNAME=DevAssistant
RQ1_TOOLVERSION=1.2.0
```

**N?u ch?a ??ng ký toolname**:
- ?? tr?ng ho?c dùng default `PQA_Agent` (c?n ???c RQ1 admins whitelist)
- Contact RQ1 administrators ?? ??ng ký toolname "PQA_Agent"
- Chi ti?t xem file `TOOLNAME_VERSION.md`

3. Install dependencies:
```bash
uv sync
```

## Usage

### Running the Server

```bash
uv run rq1_server.py
```

### Connecting to Claude Desktop

Add this configuration to your Claude Desktop config file (`~/Library/Application Support/Claude/claude_desktop_config.json` on macOS or `%APPDATA%\Claude\claude_desktop_config.json` on Windows):

```json
{
  "mcpServers": {
    "rq1": {
      "command": "uv",
      "args": [
        "--directory",
        "/absolute/path/to/rq1-mcp-server",
        "run",
        "rq1_server.py"
      ],
      "env": {
        "RQ1_USER": "your_username",
        "RQ1_PASSWORD": "your_password",
        "RQ1_TOOLNAME": "your_tool_name",
        "RQ1_TOOLVERSION": "your_tool_version"
      }
    }
  }
}
```

### Connecting to Other MCP Clients

The server uses stdio transport, so it can be connected to any MCP-compatible client that supports this transport method.

## MCP Tools Available

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

## Reference Documentation

- **RQ1_LIBRARY_GUIDE.md** - Complete building-block-rq1 library documentation
- **building-block-rq1 examples/** - Example code in `rq1/extracted/building_block_rq1-1.6.0/examples/`

## Next Steps

1. **Implement rq1_client.py** - Replace placeholder with actual library calls
2. **Test connection** - Verify each tool works with ACCEPTANCE environment  
3. **Get user feedback** - Ensure data access meets requirements
4. **Then** consider additional features (rule checking, etc.)
