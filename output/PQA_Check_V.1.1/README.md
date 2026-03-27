# RQ1 PRPL Validation Tool

**Version 1.1** - Bug fixes for Rule 14 and Rule 18 detection

---

## What Changed in V.1.1

| Fix | Description |
|-----|-------------|
| **Rule 14** | Fixed lazy Resource loading for `hasParent` - ISW state now fetched correctly via OSLC nested select |
| **Rule 18** | Fixed ISW history pagination + XML parse for `LifeCycleState` field - working days calculation now correct |
| **Auth** | Auto-load `RQ1_USER` and `RQ1_PROJECT_IDS` from `.env` file - password is always prompted at runtime via `validate.ps1` |

---

## Quick Start with GitHub Copilot (Recommended)

1. Open this folder in VS Code
2. Open Copilot Chat (`Ctrl+Alt+I`)
3. **First time**: Send any message (e.g. `"hi"`) — Copilot detects `.env` is missing and starts the setup wizard automatically. It will ask for your NTID and Project IDs, then create `.env` for you.
4. **After setup**: Say `"validate my RQ1 items"` — Copilot gives you the command to run. Run it in terminal (enter password when prompted), then paste the output back to Copilot for a formatted report.

No manual `.env` editing needed.

---

## Quick Start (Manual)

```powershell
# 1. Configure .env file (copy from .env.example)
Copy-Item .env.example .env
# Edit .env: set RQ1_USER and RQ1_PROJECT_IDS

# 2. Run validation
.\validate.ps1 <NTID>
# Enter password when prompted (required each run)
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

**File**: `.env` (create from `.env.example`)

```ini
RQ1_USER=your_ntid_here
RQ1_PROJECT_IDS=RQONE00001940
# Optional - if set, Copilot can run validation automatically without password prompt
RQ1_PASSWORD=your_password_here
```

**Find Project IDs**: Open User in IPE > Projects > Copy RQ1 number (comma-separate multiple)

---

## Usage

### Method 1: Wrapper Script (Recommended)
```powershell
.\validate.ps1 <NTID>

# Example:
.\validate.ps1 DAB5HC
```
- Prompts for password each run (password is never stored)

### Method 2: Direct Executable
```powershell
$env:RQ1_PASSWORD = "your_password"
.\validate_user_items.exe <NTID>
```

---

## Example Output

```
================================================================================
PRPL Rule Validation for User: DAB5HC
================================================================================

[0] Looking up user DAB5HC...
[OK] Found: John Doe

[1] Querying Issues...
[OK] Found 5 Issues (IFD=4, ISW=1)

[2] Querying Releases...
[OK] Found 3 Releases (BC=2, PVER=1)

[3] Validating Workitems...
[OK] Validated 1 Workitems

================================================================================
VALIDATION SUMMARY
================================================================================
Total items: 9
Violations: 2 (WARNING: 2, INFO: 0)
Pass rate: 97.8% (based on WARNING violations)

================================================================================
VIOLATIONS FOUND (2)
================================================================================

[1] PRPL 14.00.00 - WARNING
    Rule: IFD is not committed, eventhough attached Issue-SW is committed
    Item: RQONE04958329
    ...
```

---

## Changelog

| Version | Changes |
|---------|---------|
| **V.1.1** | Fixed Rule 14 (hasParent lazy load), Rule 18 (pagination + XML parse), auto dotenv load |
| **V.1.0** | Initial release - 12 PRPL rules implemented |
