# Quality Check

**Version 1.3** - Automated validation tool for RQ1 PRPL rules, targeting QAM, QAMi and BBM rule sets.

## Overview

This tool validates RQ1 items (BC, IFD, Release, Workitem) against 12 PRPL business rules, providing detailed violation reports with severity levels and pass rate calculations. The primary use case is to help engineers and PQAs identify deviations before review meetings.

**Supported Rules**: PRPL 01, 02, 03, 06, 07, 11, 12, 13, 14, 15, 16, 18

### 12 PRPL Rules Implemented

| Rule ID | Description |
|---------|-------------|
| **PRPL 01** | BC-R is not in requested state, 8 weeks before PVER planned delivery date |
| **PRPL 02** | Workitem is in started state, but planned date for workitem is not entered in planning tab |
| **PRPL 03** | Issue/Release/workitem is still in "Conflicted" state |
| **PRPL 06** | Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled |
| **PRPL 07** | Planned date of BC later than requested delivery date of any mapped PVER or PVAR |
| **PRPL 11** | IFD 5 day SLA reached |
| **PRPL 12** | IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled |
| **PRPL 13** | IFD is not implemented or closed, after planned dated of BC-R |
| **PRPL 14** | IFD is not committed, eventhough attached Issue-SW is committed |
| **PRPL 15** | Release is not closed after planned date for BC and FC |
| **PRPL 16** | Workitem is not closed after planned date |
| **PRPL 18** | I-FD not committed 5 or more working days after attached I-SW was committed |

**Target Rule Sets**: QAM, QAMi, BBM (IPT rules are candidate for future extension)

## Status

- [x] RQ1 connection and authentication
- [x] Data retrieval for all item types (BC, IFD, Release, Workitem)
- [x] 12 PRPL rules implemented and tested
- [x] Excel rules analysis (101 rules: BBM 23 + QAM 22 + IPT 56)
- [x] Validation engine with severity levels (WARNING, INFO)
- [x] Pass rate calculation (WARNING-only)
- [x] Executable package with password caching
- [x] Multi-user testing successful

## Features

### Validation Engine
- **12 PRPL Rules**: Business rule validation covering QAM, QAMi and BBM rule sets
- **Severity Levels**: WARNING (impacts pass rate), INFO (informational only)
- **Pass Rate**: Calculated from WARNING violations only
- **Detailed Reports**: Item ID, rule, description, severity, resolution hint
- **Multi-Item Support**: Validate all active items assigned to any user

### Deployment
- **Executable Package**: No Python required, standalone `.exe` with wrapper script
- **Python Source**: For development and adding new rules
- **Password Caching**: Enter password once per terminal session

## Project Structure

```
RQ1_agent/
  rules/                   # PRPL rule implementations (12 rules)
  docs/                    # Documentation
    RULES_COMPLETE.md      # All 101 Excel rules (BBM + QAM + IPT)
    EXCEL_TO_JAVA_MAPPING.md
    IMPLEMENTATION_GUIDE.md
  scripts/                 # Excel parsing and rule analysis utilities
  tests/                   # Test scripts
  output/                  # Distribution packages
    executables/           # Latest release (validate.ps1 + exe)
  building-block-rq1-ref/  # RQ1 library source reference
  validate_user_items.py   # Main validation script
  .env.example             # Environment template
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
git clone https://github.com/sybinh/Quality_Check.git
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

### Executable (recommended for end users)

```powershell
cd output\executables
.\validate.ps1 <NTID>

# Example
.\validate.ps1 IOH81HC
```

First run: prompts for password once, caches it for the terminal session.  
Subsequent runs in the same terminal: password reused automatically.

### Python script (for development)

```powershell
python validate_user_items.py <NTID>
```

**Output includes:**
- Total items found (Issues, Releases, Workitems)
- Rule violations with severity and resolution hint
- Pass rate (based on WARNING violations only)

## Documentation

- `docs/RULES_COMPLETE.md` - All 101 Excel rules (BBM 23 + QAM 22 + IPT 56)
- `docs/EXCEL_TO_JAVA_MAPPING.md` - Mapping between Excel rules and Java reference implementation
- `docs/IMPLEMENTATION_GUIDE.md` - Guide for adding new rules
- `building-block-rq1-ref/` - RQ1 library source (restricted access repo, kept here for reference)

## Extending the Tool

To add a new PRPL rule:
1. Create `rules/rule_prpl_XX_<name>.py` following the existing rule pattern
2. Import and call it in `validate_user_items.py`
3. Add test cases in `tests/`
4. Rebuild the executable with `build_exe.ps1`

See `docs/IMPLEMENTATION_GUIDE.md` for details.

## Future Potential

- RAG / AI assistant integration (Copilot or Claude) for natural language queries
- Automated reporting and trend tracking
- Extension to IPT rule set
