# Quick Setup Guide - New Machine

## Step 1: Extract Package
Extract all files to your workspace folder.

## Step 2: Configure .env

```powershell
# Copy template
Copy-Item .env.example .env

# Edit .env file (use Notepad or VS Code)
# Set these values:
RQ1_USER=YOUR_NTID
RQ1_PROJECT_IDS=RQONE00001940
```

## Step 3: Tell Copilot (First Time Only)

Open Copilot Chat in VS Code and paste:

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

## Step 4: Test

Ask Copilot:
```
validate my RQ1 items
```

Enter password when prompted ? Done!

---

## Daily Usage

Just ask naturally:
- "validate my RQ1 items"
- "check my deviation"
- "show PRPL violations for ABC1HC"

Copilot knows what to do after Step 3.

---

See `README.md` for full documentation.
See `COPILOT_INSTRUCTIONS.md` for detailed Copilot guide.
