<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

## Project: RQ1 PRPL Validation Tool

This is a Python-based automated validation tool for RQ1 PRPL rules, targeting QAM, QAMi and BBM rule sets. Engineers and PQAs run it to find deviations in their RQ1 items before review meetings.

### References
- RQ1 library: `building-block-rq1-ref/` (local copy, original repo has restricted access)

### Project Details
- **Language**: Python 3.11
- **Purpose**: Validate RQ1 items (BC, IFD, Release, Workitem) against PRPL business rules
- **Deployment**: Standalone `.exe` + PowerShell wrapper, or Python script directly
- **Authentication**: Environment variables (RQ1_USER, RQ1_PASSWORD, RQ1_TOOLNAME, RQ1_TOOLVERSION)

### Project Structure
```
RQ1_agent/
  rules/                   # PRPL rule implementations (12 rules, one file per rule)
  docs/                    # Documentation (English only, no emoji)
    RULES_COMPLETE.md      # All 101 Excel rules (BBM + QAM + IPT)
    EXCEL_TO_JAVA_MAPPING.md
    IMPLEMENTATION_GUIDE.md
  scripts/                 # Excel parsing and rule analysis utilities
  tests/                   # Test scripts
  output/executables/      # Latest release package (validate.ps1 + exe)
  building-block-rq1-ref/  # RQ1 library reference (local copy)
  validate_user_items.py   # Main script
```

### Current State

- 12 PRPL rules implemented and working in production
- Standalone executable deployed to team
- Password caching via PowerShell wrapper
- Target rule sets: QAM, QAMi, BBM

### Rule Files

Each rule is in `rules/rule_prpl_XX_<name>.py` with a standard interface:
- Class with `execute()` method returning a result with `passed`, `severity`, `description`
- Severity is either `WARNING` (affects pass rate) or `INFO` (informational)

### Adding New Rules

1. Create `rules/rule_prpl_XX_<name>.py`
2. Import and call in `validate_user_items.py`
3. Add test in `tests/`
4. Rebuild with `build_exe.ps1`

See `docs/IMPLEMENTATION_GUIDE.md` for the full process.

### Future Potential (not current scope)

- RAG / AI assistant integration for natural language queries
- IPT rule set extension
- Automated reporting
