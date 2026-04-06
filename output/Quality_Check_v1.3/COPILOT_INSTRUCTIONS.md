# Using RQ1 PRPL Validator with GitHub Copilot

## Overview

This guide shows you how to use **GitHub Copilot Chat** to run RQ1 PRPL validation in your VS Code workspace.

> **Note**: This is a **standalone executable package** - no Python installation or MCP server needed. Copilot simply runs the tools via terminal commands.

---

## Quick Setup (First Time on New Machine)

### Step 1: Extract Package to Workspace

1. Extract the package folder to your workspace
2. Your workspace should contain:
   ```
   your-workspace/
   ??? validate.ps1 (wrapper script)
   ??? validate_user_items.exe (main tool)
   ??? .env.example (template)
   ??? README.md
   ??? COPILOT_INSTRUCTIONS.md (this file)
   ```

### Step 2: Configure Credentials

1. **Copy `.env.example` to `.env`**:
   ```powershell
   Copy-Item .env.example .env
   ```

2. **Edit `.env` file** (use Notepad or VS Code):
   ```ini
   # Your RQ1 username
   RQ1_USER=YOUR_NTID_HERE
   
   # Project IDs (RQ1 numbers of Releases)
   # Find in RQ1: Open Release ? Copy RQ1 number (e.g., RQONE00001940)
   RQ1_PROJECT_IDS=RQONE00001940,RQONE00002345
   ```

3. **Do NOT store password** - tool will prompt when running (masked as ****)

### Step 3: Open VS Code with Copilot

1. Open your workspace in VS Code
2. Ensure GitHub Copilot extension is installed and active
3. Open Copilot Chat: `Ctrl+Alt+I` (Windows) or `Cmd+Shift+I` (Mac)

### Step 4: Tell Copilot About the Tool

**First time on new machine**, tell Copilot:

```
I have RQ1 PRPL validation tool in this workspace.
Files: validate.ps1, validate_user_items.exe, and .env
This is a standalone executable - no Python setup needed.

To run validation:
- Use: .\validate.ps1 <NTID>
- First run: Tool will prompt for RQ1 password (masked)
- Later runs: Uses cached password automatically

Please help me validate RQ1 items when I ask.
```

? **Done!** Now Copilot knows how to run the tool.

---

## How It Works

### What Copilot Can Do

- ? Run `.\validate.ps1 <NTID>` in PowerShell terminal
- ? Parse validation results
- ? Explain violations in plain language
- ? Suggest fixes for PRPL violations
- ? Show pass rate and severity breakdown

### What You Need to Provide

- ? Your RQ1 password (tool prompts - Copilot cannot access it)
- ? NTID to validate (in your request)

---

## Usage Examples

### Example 1: Validate Your Own Items

**You ask:**
```
validate my RQ1 items
```

**Copilot will:**
1. Read `RQ1_USER` from `.env` to get your NTID
2. Run: `.\validate.ps1 <your_NTID>`
3. Tool prompts you for password
4. Show results and explain violations

Press `Ctrl+Alt+I` (or `Cmd+Alt+I` on Mac) to open Copilot Chat panel.

## Usage Examples

### Example 1: Validate Your Own Items

Simply ask Copilot in natural language:

```
validate my RQ1 items
check my deviation
check QAMi warning for me
show my PRPL violations
check my RQ1 status
```

Or more specifically:

```
run validate_user_items.exe with my NTID
```

**What Copilot does:**
1. Reads your .env file to get RQ1_USER
2. Runs: `.\validate_user_items.exe <your_NTID>`
3. Tool prompts: "Enter RQ1 password: ****" (you type password)
4. Shows the validation results
5. Explains any PRPL violations found

### Example 2: Validate Another User

**You ask:**
```
validate ABC1HC
check deviation for ABC1HC
show PRPL violations for ABC1HC
```

**Copilot will:**
1. Run: `.\validate.ps1 ABC1HC`
2. Tool prompts you for password (if not cached)
3. Show results for ABC1HC

### Example 3: Natural Language Queries

**You can ask naturally:**
```
check my deviation
check QAMi warning for me
show my RQ1 status
what are my PRPL violations?
```

**Copilot understands:**
- "my" = use RQ1_USER from .env
- "deviation" / "QAMi warning" = PRPL violations
- Automatically uses `.\validate.ps1` wrapper

---

## Important: Tool Selection

### ? Copilot Should Use: `.\validate.ps1`

**Why?**
- Auto password caching (enter once per session)
- Better user experience
- Handles authentication errors gracefully

### ?? Alternative: `.\validate_user_items.exe`

**When?**
- If wrapper fails
- Direct execution needed
- Password prompt every time

**Tell Copilot:**
```
Use validate.ps1 wrapper by default.
Only use validate_user_items.exe if wrapper fails.
```

---

## Troubleshooting Setup

### Issue: Copilot Asks "Which Tool?"

**Solution:** Tell Copilot once:
```
For RQ1 validation, always use:
.\validate.ps1 <NTID>

This is the recommended method with auto password caching.
```

### Issue: Copilot Asks "Terminal or Copilot?"

**Solution:** Clarify:
```
Run validation in PowerShell terminal using:
.\validate.ps1 <NTID>

This is a terminal tool, not a Copilot extension.
```

### Issue: "Tool Not Found"

**Solution:**
1. Check files are in workspace root
2. Tell Copilot:
   ```
   The tool is in workspace root:
   - validate.ps1
   - validate_user_items.exe
   - .env
   ```

### Issue: ".env Not Configured"

**Solution:**
```
I need to configure .env first:
1. Copy .env.example to .env
2. Set RQ1_USER=<my_NTID>
3. Set RQ1_PROJECT_IDS=<project_ids>
```

---

## First-Time Setup Checklist

When setting up on a **new machine**, tell Copilot:

```
Setup checklist for RQ1 PRPL validation:

1. Extract package to workspace ?
2. Copy .env.example to .env
3. Edit .env with my RQ1_USER and RQ1_PROJECT_IDS
4. Test: .\validate.ps1 <NTID>
5. Enter password when prompted

Tool info:
- Standalone executable (no Python needed)
- Use validate.ps1 wrapper for auto password caching
- Password prompted at runtime (not stored in .env)

Help me with steps 2-4.
```

**What Copilot does:**
1. Runs: `.\validate_user_items.exe ABC1HC`
2. Analyzes the output
3. Highlights critical violations (PRPL 01, 02, 06, 07, 11)

### Example 3: Explain Violations

After running validation:

```
explain the PRPL 11 violations in detail
```

Or:

```
which items have the most critical violations?
```

**What Copilot does:**
- Reads the validation output
- Explains each rule violation
- Suggests actions to resolve issues

## Validation Rules

The tool checks 12 PRPL rules:

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

## Tips for Best Results

### Be Specific

? **Vague**: "check RQ1"

? **Clear**: "run validate_user_items.exe for user ABC1HC"

### Ask for Analysis

After validation runs:

```
summarize the validation results
show me only CRITICAL violations
which items need immediate attention?
```

### Troubleshooting with Copilot

If tool fails:

```
why did the validation fail?
check if .env file is configured correctly
test RQ1 connection
```

## Advanced Usage

### Batch Validation

```
validate items for users ABC1HC, DEF2HC, and GHI3HC
```

Copilot will run the tool 3 times and compare results.

### Filtering Results

```
run validation and show only PRPL 11 violations
check for IFD SLA issues in my items
```

### Integration with Your Workflow

```
validate my items before creating the release report
check if there are any blocking issues for sprint closure
```

## What Copilot CANNOT Do

? **Cannot update RQ1** - Tool is read-only  
? **Cannot create @agents** - Not an MCP server integration  
? **Cannot cache password** - You must enter it when prompted  

---

## Quick Reference: Tell Copilot on New Machine

Copy-paste this into Copilot Chat on **first setup**:

```
I have RQ1 PRPL validation tool (standalone executable).

Files in workspace:
- validate.ps1 (wrapper with auto password caching)
- validate_user_items.exe (main tool)
- .env (credentials: RQ1_USER, RQ1_PROJECT_IDS)

Usage:
- Always use: .\validate.ps1 <NTID>
- First run: Tool prompts for password (masked)
- Later runs: Uses cached password
- This is a terminal tool (run in PowerShell)

When I ask to "validate" or "check deviation", 
run .\validate.ps1 with appropriate NTID.
```

? Save this as a snippet - paste on every new machine!

---

## Summary

**Key Points:**
1. ? Standalone executable - no Python/MCP setup
2. ? Use `.\validate.ps1` for auto password caching
3. ? Tell Copilot once on new machine about tool location
4. ? Copilot runs terminal commands - you provide password
5. ? Natural language works: "check my deviation"

**First Time Setup:**
```
1. Extract ? 2. Configure .env ? 3. Tell Copilot ? 4. Ask naturally
```

---

For more details, see `README.md`
validate items for ABC1HC and explain PRPL violations
```

Copilot automatically:
- Runs the command
- Parses the output
- Explains violations in natural language
- Suggests next actions

## Example Session

**You:**
```
validate my RQ1 items
```

**Copilot:**
```
I'll run the validation tool for you using your NTID from .env file.

[Runs: .\validate_user_items.exe <your_NTID>]

Found 3 violations:
- PRPL 11: 2 IFDs exceeded 5-day SLA (RQONE12345678, RQONE87654321)
- PRPL 02: 1 workitem in started state without planned date (RQONE11111111)

The PRPL 11 violations are CRITICAL and require immediate attention.
Would you like me to explain the specific SLA calculation?
```

**You:**
```
yes, explain PRPL 11 for RQONE12345678
```

**Copilot:**
```
For IFD RQONE12345678:
- Created: 2026-01-01
- Status: Opened
- Days since creation: 7 days
- SLA threshold: 5 working days
- Projects: RQONE00001940

Action needed: Either commit to a project or provide reason in 
internalcomment field within next 2 days to avoid process violation.
```

## Workspace Customization (Optional)

You can add instructions to `.github/copilot-instructions.md` in your workspace:

```markdown
## RQ1 PRPL Validation

When I ask to "validate RQ1 items" or "check PRPL rules":
1. Run `.\validate_user_items.exe` with appropriate NTID
2. Parse the output and highlight CRITICAL violations
3. Explain rule violations in simple terms
4. Suggest actions to resolve issues

Tool location: `validate_user_items.exe` in workspace root
Configuration: `.env` file with RQ1 credentials
```

## Troubleshooting

### "Tool not found"

Copilot says the executable doesn't exist:
- Ensure `validate_user_items.exe` is in your workspace
- Check file path: ask "list files in current directory"

### "Authentication failed" or "Password: INCORRECT"

Tool reports login error:
- **Most common**: Wrong password entered at prompt
- **Solution**: Re-run and enter correct password (masked as ****)
- Verify username in `.env` is correct
- Test password in RQ1 web interface first

### "No violations found"

Tool returns empty results:
- Check RQ1_PROJECT_IDS is set correctly (use RQ1 numbers)
- Verify user has items in those projects
- Ask Copilot: "check if ABC1HC has any items in project RQONE00001940"

## Support

For tool issues:
- Check README.md for detailed documentation
- Review QUICKSTART.md for configuration help
- Contact tool maintainer (not GitHub Copilot support)

For Copilot issues:
- Visit: https://github.com/copilot
- Check VS Code Copilot extension settings

---

**Version:** 1.2  
**Last Updated:** January 2026
