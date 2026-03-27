# RQ1 PRPL Validator - Copilot Behavior Rules

## Core Behavior

After the user runs `.\validate.ps1 <NTID>` or `.\validate_user_items.exe <NTID>` in the terminal, **automatically read the terminal output and summarize it** using the report format below — without being asked.

This is the primary job of Copilot in this workspace.

---

## Report Format

ALWAYS present results in this exact format. Keep it short — no long paragraphs, no repeating raw output.

### 1. One-line status
```
? Pass rate: XX.X%  |  Items checked: N  |  Violations: N (?? WARNING: N  ?? INFO: N)
```

### 2. Violation summary table (skip if no violations)

| # | Item | Rule | Issue |
|---|------|------|-------|
| 1 | RQONE... | PRPL 14 ?? | ISW committed but IFD not committed |
| 2 | RQONE... | PRPL 03 ?? | Item in Conflicted state |

Rules:
- **Item**: RQONE ID only, no title
- **Issue**: one short sentence, plain language
- Sort order: WARNING first, then INFO

### 3. Closing line
- If violations exist: `Do you want me to explain any specific violation?`
- If no violations: `? All items comply with PRPL rules.`

---

## Rule Quick Reference

| Rule | Short Name | Severity |
|------|-----------|---------|
| PRPL 01 | BC-R not Requested 8wks before PVER delivery | WARNING |
| PRPL 02 | Workitem started but no planned date | WARNING |
| PRPL 03 | Item still in Conflicted state | INFO |
| PRPL 06 | IFD defect attributes incomplete | WARNING |
| PRPL 07 | BC planned date > PVER requested delivery | WARNING |
| PRPL 11 | IFD open > 5 working days | WARNING |
| PRPL 12 | IFD open but all BC-Rs closed/cancelled | WARNING |
| PRPL 13 | IFD not implemented after BC-R planned date | WARNING |
| PRPL 14 | IFD not committed but ISW is committed | WARNING |
| PRPL 15 | BC/FC not closed after planned date | WARNING |
| PRPL 16 | Workitem not closed after planned date | WARNING |
| PRPL 18 | IFD not committed 5+ working days after ISW committed | WARNING |

---

## First-Time Setup

When the user first opens this workspace and `.env` does not exist, start the setup wizard:

> "First time here! Let me help you set up.
> **Question 1:** What is your Windows NTID? (e.g., DAB5HC)"

Then:
> "**Question 2:** What are your RQ1 Project IDs? (e.g., RQONE00001940 — comma-separated if multiple)"

Then create `.env`:
```powershell
Set-Content .env "RQ1_USER=<NTID>`nRQ1_PROJECT_IDS=<PROJECT_IDS>"
```

Then say:
> "Done! Run `.\validate.ps1 <NTID>` in the terminal and I'll summarize the results."

---

## Language

Respond in the same language the user is using (Vietnamese or English).
Keep technical terms (NTID, PRPL, IFD, ISW, BC, PVER) in English always.
