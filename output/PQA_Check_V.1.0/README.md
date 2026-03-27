# RQ1 PRPL Validation Tool

**Version 1.2** - Automated validation for 12 PRPL rules with password caching

---

## Quick Start (30 seconds)

```powershell
# 1. Configure .env file
RQ1_USER=your_username
RQ1_PROJECT_IDS=RQONE00001940

# 2. Run validation (recommended - auto password caching)
.\validate.ps1 <NTID>
# First time: Enter password once
# Later runs: Uses cached password automatically
```

---

## 12 PRPL Rules

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

---

## Configuration

**File**: `.env`

```ini
# Your RQ1 username
RQ1_USER=your_username

# Project IDs to validate (comma-separated)
RQ1_PROJECT_IDS=RQONE00001940,RQONE00002345
```

**Find Project IDs**: Open User in IPE ? Projects ? Copy RQ1 number

---

## Usage

### Method 1: Wrapper Script (Recommended)
```powershell
.\validate.ps1 <NTID>
```
Password cached automatically after first entry

### Method 2: Direct Execution
```powershell
.\validate_user_items.exe <NTID>
```
Prompts for password every time

### With GitHub Copilot
Open Copilot Chat in VS Code:
```
validate my RQ1 items
check my deviation
show PRPL violations for ABC1HC
```
See `COPILOT_INSTRUCTIONS.md` for details.

---

## Output

### Severity Levels
- **WARNING**: Must fix (impacts pass rate)
- **INFO**: Review only (doesn't impact pass rate)

### Pass Rate
```
Pass Rate = (Total - WARNING violations) / Total × 100%
```

### Example Output
```
Total items: 17
Violations: 4 (WARNING: 0, INFO: 4)
Pass rate: 100.0%
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Authentication failed | Re-run and enter correct password |
| No items found | Check `RQ1_PROJECT_IDS` in `.env` |
| Connection timeout | Check VPN connection |
| Wrong cached password | Close terminal and reopen |

---

## Password Caching

**How it works:**
1. First run: Enter password ? cached in memory
2. Later runs: Uses cached password
3. Close terminal: Password cleared

**Manual cache clear:**
```powershell
Remove-Item Env:\RQ1_PASSWORD
```

---

## Changelog

### Version 1.2 (Jan 8, 2026)
- Auto password caching with wrapper script
- Password masking (****) during input
- Clear authentication error messages
- Hardcoded PRODUCTIVE environment
- Simplified documentation

### Version 1.1
- 12 PRPL rules implementation
- Executable package
- GitHub Copilot integration

---

## Files Included

- `validate.ps1` - Wrapper with auto password caching
- `validate_user_items.exe` - Main validation tool (21.6 MB)
- `README.md` - This file
- `COPILOT_INSTRUCTIONS.md` - GitHub Copilot guide
- `.env.example` - Configuration template
