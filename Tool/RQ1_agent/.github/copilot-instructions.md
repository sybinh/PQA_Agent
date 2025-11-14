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

### Development Phases

#### Phase 1: RQ1 Data Access (COMPLETED ?)
**Goal**: Build and test basic RQ1 data retrieval

**Progress**:
- [x] Setup project structure
- [x] Install building-block-rq1 library (v1.6.0)
- [x] Explore library documentation and examples
- [x] Update terminology to match library (Issue, rq1_number, query)
- [x] Define 4 MCP tools (get_issue_details, query_my_issues, query_issues, get_issue_history)
- [x] Cleanup: Remove PQA placeholder code (not in requirements)
- [x] Compare POST Tool vs building-block-rq1 (chose Python library)
- [x] Implement rq1_client.py using building-block-rq1
- [x] Update rq1_server.py to use new client methods
- [ ] **PENDING**: Create .env with test credentials
- [ ] Test each tool with ACCEPTANCE environment
- [ ] Verify all tools work correctly

#### Phase 2: Rule Checking (READY TO START)
**Status**: Excel analysis complete, ready for implementation
- **Total Excel Rules**: 101 (BBM 23 + QAM 22 + IPT 56)
- **Total Java Rules**: 37 from POST Tool
- **Intersection**: ~30-36 rules with BOTH Excel + Java backing

**Excel Parser**: ? COMPLETE & OPTIMIZED
- Script: `parse_excel_rules.py`
- Features: Priority sorting, grouping by execution level
- Output: 4 markdown files (BBM, QAM, IPT, Summary)
- Reusable: Ready for future Excel versions

**Priority Breakdown**:
- ??? CRITICAL (mandatory): 23 rules (BBM 9 + QAM 10 + IPT 4)
- ?? HIGH (optional): 22 rules (BBM 6 + IPT 16)
- ? MEDIUM (new): 3 rules (QAM 3)
- ?? LOW (open): 7 rules (QAM 7)
- ? SKIP (outdated): 12 rules
- N/A (not specified): 34 rules (mostly PRAS project management)

**Phase 2A**: Top 10 priority rules (30-40 hours)
- All have BOTH Excel (official process) + Java (proven implementation)
- 8 out of 10 have IPT rule backing (stronger justification)
- Priority: mandatory or optional execution level

**Phase 2B**: Remaining 26 rules with Excel+Java backing

**Documentation**: 
- RQ1_RULES_SUMMARY.md - Top 10 rules
- RQ1_RULES_DETAILED.md - All 37 Java rules
- EXCEL_RULES_BY_PRIORITY.md - All 101 Excel rules prioritized
- EXCEL_JAVA_MAPPING.md - Excel ? Java mapping
- PARSER_OPTIMIZATION_COMPLETE.md - How to use parser for future versions

#### Phase 3: Integration (FUTURE)
**Status**: After Phase 1 & 2 complete
