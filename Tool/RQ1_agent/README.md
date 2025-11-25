# PQA Agent - RQ1 MCP Server

**PQA_Agent v1.0.0** - An MCP (Model Context Protocol) server for integrating with RQ1/ClearQuest system using the `building-block-rq1` library.

## Project Status

### Phase 1: RQ1 Data Access (IN PROGRESS)
- [x] Project structure setup
- [x] building-block-rq1 library installed (v1.6.0)
- [x] Excel rules parsed (101 rules: BBM 23 + QAM 22 + IPT 56)
- [x] Documentation organized in docs/ folder
- [ ] RQ1 client implementation
- [ ] Connection testing with ACCEPTANCE environment

### Phase 2: Rule Implementation (READY TO START)
- Top 10 priority rules identified
- Excel-to-Java mapping completed
- Implementation guide prepared
- Estimated: 30-40 hours for Phase 2A

### Phase 3: Integration (FUTURE)
- MCP server integration
- Testing and optimization

## Features

- **Get Issue Details**: Retrieve comprehensive information about RQ1 Issues by rq1_number
- **Query My Issues**: Query all Issues assigned to the current user
- **Query Issues**: Query Issues by title, status, or other criteria using OSLC query
- **Get Issue History**: View the History records for an Issue

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

### Running the Server

```bash
python src/rq1_server.py
```

### MCP Client Configuration

For Claude Desktop or other MCP clients, add to config file:

```json
{
  "mcpServers": {
    "rq1": {
      "command": "python",
      "args": ["C:/path/to/RQ1_agent/src/rq1_server.py"],
      "env": {
        "RQ1_USER": "your_username",
        "RQ1_PASSWORD": "your_password"
      }
    }
  }
}
```

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
