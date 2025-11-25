<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

## Project: RQ1 MCP Server

This is a Model Context Protocol (MCP) server for integrating with RQ1/ClearQuest ticketing system.

### References
- MCP Documentation: https://modelcontextprotocol.io
- Python SDK: https://github.com/modelcontextprotocol/python-sdk

### Project Details
- **Language**: Python
- **Purpose**: Provide AI assistant access to RQ1 ticketing system
- **Authentication**: Environment variables (RQ1_USER, RQ1_PASSWORD, RQ1_TOOLNAME, RQ1_TOOLVERSION)

### Project Structure
```
RQ1_agent/
??? docs/                    # All documentation (English only, no emoji)
?   ??? RULES_COMPLETE.md   # All 101 rules (single source of truth)
?   ??? EXCEL_TO_JAVA_MAPPING.md
?   ??? IMPLEMENTATION_GUIDE.md
??? scripts/                 # Excel parsing utilities
?   ??? parse_excel_rules.py
?   ??? excel_converter.py
?   ??? excel_change_detector.py
??? src/                     # Source code
?   ??? rq1_client.py       # RQ1 client (TO BE IMPLEMENTED)
?   ??? rq1_server.py       # MCP server
?   ??? test_connection.py
??? rq1/POST_V_1.0.3/       # Reference data (Java/VBS rules)
```

### Development Phases

#### Phase 1: RQ1 Data Access (IN PROGRESS)
**Goal**: Build and test basic RQ1 data retrieval

**Progress**:
- [x] Setup project structure
- [x] Install building-block-rq1 library (v1.6.0)
- [x] Define 4 MCP tools (get_issue_details, query_my_issues, query_issues, get_issue_history)
- [x] Parse all 101 Excel rules (BBM 23 + QAM 22 + IPT 56)
- [x] Map Excel rules to 37 Java rules from POST Tool
- [x] Organize documentation in docs/ folder
- [ ] **PENDING**: Implement rq1_client.py using building-block-rq1
- [ ] **PENDING**: Test connection with ACCEPTANCE environment
- [ ] **PENDING**: Verify all tools work correctly

#### Phase 2: Rule Implementation (READY TO START)
**Status**: Excel analysis complete, ready for implementation
- **Total Excel Rules**: 101 (BBM 23 + QAM 22 + IPT 56)
- **Total Java Rules**: 37 from POST Tool
- **Intersection**: ~30-36 rules with BOTH Excel + Java backing

**Excel Parser**: COMPLETE & OPTIMIZED
- Script: `scripts/parse_excel_rules.py`
- Features: Priority sorting, grouping by execution level
- Output: Consolidated docs/RULES_COMPLETE.md
- Reusable: Ready for future Excel versions

**Priority Breakdown**:
- CRITICAL (mandatory): 23 rules (BBM 9 + QAM 10 + IPT 4)
- HIGH (optional): 22 rules (BBM 6 + IPT 16)
- MEDIUM (new): 3 rules (QAM 3)
- LOW (open): 7 rules (QAM 7)
- SKIP (outdated): 12 rules
- N/A (not specified): 34 rules

**Phase 2A**: Top 10 priority rules (30-40 hours)
- All have BOTH Excel (official process) + Java (proven implementation)
- See docs/EXCEL_TO_JAVA_MAPPING.md for details
- See docs/IMPLEMENTATION_GUIDE.md for step-by-step process

**Phase 2B**: Remaining 26 rules with Excel+Java backing

#### Phase 3: Integration (FUTURE)
**Status**: After Phase 1 & 2 complete
