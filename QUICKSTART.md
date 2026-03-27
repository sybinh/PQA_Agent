# RQ1 PRPL Validator - Quick Start Guide

##  What You Get

Automated RQ1 PRPL rules validation tool with **3 usage modes**:
1. **Console Tool** - Direct command line validation
2. **MCP Server** - AI Assistant (Copilot/Claude) integration
3. **API Tool** - JSON output for automation

**12 PRPL Rules Implemented**: 01, 02, 03, 06, 07, 11, 12, 13, 14, 15, 16, 18

---

##  Two Deployment Options

### Option A: Executable Package (Recommended) 

**No Python needed! Just run the .exe files.**

#### Setup (30 seconds)

1. **Extract package**
   ```powershell
   # Extract to your preferred location
   unzip RQ1_PRPL_Validation_Release.zip
   cd RQ1_PRPL_Validation_Release
   ```

2. **Configure credentials**
   ```powershell
   # Copy and edit .env file
   copy .env.example .env
   notepad .env
   ```
   
   Edit these values:
   ```ini
   RQ1_USER=your_username
   RQ1_PASSWORD=your_password
   RQ1_ENVIRONMENT=PRODUCTIVE
   RQ1_PROJECT_IDS=PS-EC/EFVC2,PS-EC/EFV
   ```

3. **Done!** No installation needed.

#### Usage

**Console Validation:**
```powershell
# Validate user's items
.\validate_user_items.exe TRE5HC

# Output shows:
# - Total checks performed
# - Violations by severity (WARNING, INFO)
# - Pass rate (based on WARNING only)
# - Detailed violation descriptions
```

**MCP Server (for Copilot/AI):**
```powershell
# Start server
.\mcp_server_fast.exe

# Server starts on http://localhost:8000
# API docs: http://localhost:8000/docs
# Now you can ask Copilot: "validate user TRE5HC"
```

**API Tool (JSON output):**
```powershell
# Get JSON response
.\validate_user_items_api.exe TRE5HC > results.json
```

---

### Option B: Python Source (For Development)

**Requires Python 3.10+**

#### Prerequisites

1. **Python 3.10 or higher**
   ```powershell
   python --version
   # Should show: Python 3.10.x or 3.11.x or higher
   ```

2. **No other requirements!** 
   -  Git NOT needed (offline install included)
   -  GitHub access NOT needed (all wheels included)
   -  VPN NOT needed (everything is local)

#### Step 1: Extract Package

```bash
# Extract to your preferred location
unzip pqa_validator_mcp.zip -d pqa_validator
cd pqa_validator
```

#### Step 2: Install Dependencies

**Offline Install** (Recommended - No GitHub/Bosch access needed)

```powershell
# Run the install script
.\install_offline.ps1
```

The script will:
- Create virtual environment
- Install all packages from included `wheels/` folder
- No internet/VPN required!

**Option B: Online Install** (If you have Bosch Artifactory access)

```bash
# Create virtual environment
python -m venv .venv

# Activate it
.venv\Scripts\activate     # Windows
# source .venv/bin/activate  # Mac/Linux

# Install packages
pip install -r requirements.txt
```

### Step 3: Configure Your Credentials

**Step 3a: Set your username**

```bash
# Copy template
cp .env.template .env

# Edit .env file with your favorite editor
notepad .env  # or code .env
```

**Only add your username** (password is entered separately):
```env
# Change this to your RQ1 username
RQ1_USER=your_ntid
```

**Step 3b: Set up password securely**

Run the credential setup script:

```powershell
# This will prompt for password (masked ******)
.\setup_credentials.ps1
```

The script will:
- Read your username from `.env`
- Prompt for password (hidden input)
- Store credentials in environment variables (session-only)
- **No password in files!** 

**Note**: You need to run `setup_credentials.ps1` each time you open a new terminal.

### Step 4: Configure VS Code

Open VS Code  Settings (Ctrl+,)  Search "copilot mcp"

Click "Edit in settings.json" and add:

```json
{
  "github.copilot.chat.mcp.servers": {
    "pqa_validator": {
      "command": "C:\\full\\path\\to\\pqa_validator\\.venv\\Scripts\\python.exe",
      "args": ["C:\\full\\path\\to\\pqa_validator\\mcp_server_fast.py"]
    }
  }
}
```

**IMPORTANT:** 
- Use **absolute paths** for both `command` and `args`
- Use the **venv Python** (`\.venv\Scripts\python.exe`), not system Python
- Replace `C:\\full\\path\\to\\pqa_validator` with your actual installation path
- Use double backslashes (`\\`) in paths

**Example:**
```json
{
  "github.copilot.chat.mcp.servers": {
    "pqa_validator": {
      "command": "C:\\Users\\JohnDoe\\Documents\\pqa_validator\\.venv\\Scripts\\python.exe",
      "args": ["C:\\Users\\JohnDoe\\Documents\\pqa_validator\\mcp_server_fast.py"]
    }
  }
}
```

### Step 5: Restart VS Code

Close VS Code completely and reopen.

---

##  Using the Tool

### Primary Method: Copilot Chat (Recommended)

Open Copilot Chat in VS Code (Ctrl+Shift+I) and ask in natural language:

```
@pqa_validator validate RQ1 items for user IOH81HC
```

```
@pqa_validator check PRPL compliance for DUY3HC
```

```
@pqa_validator list all PRPL rules
```

**That's it!** The tool understands natural language and handles everything automatically.

### Understanding User Roles

When you ask Copilot to validate a user:

```
@pqa_validator validate IOH81HC
                      
            Target user to check
            (can be anyone)
```

Your credentials (RQ1_USER in `.env`) are used to **authenticate**, but you can **validate any user**.

**Example:**
- You: DAB5HC (set in `.env`)
- Ask: `@pqa_validator validate IOH81HC`
- Result: Tool authenticates as DAB5HC, checks IOH81HC's items

### Alternative: Direct Script (For Testing/Debugging)

If Copilot is not available or you want quick testing:

```powershell
python validate_user_items.py <username>

# Example
python validate_user_items.py IOH81HC
```

**Note:** This is mainly for debugging. Use Copilot Chat for daily work.

---

##  What Can It Do

### 1. Full User Validation
```
Validate items for [username]
```
Checks all Issues, Releases, Workitems against 11 PRPL rules.

### 2. List Available Rules
```
What PRPL rules are available
```
Shows all 11 implemented rules with descriptions.

### 3. Specific Rule Check
```
Check PRPL 14 for user [username]
```
Focus on one rule across all items.

---

##  Troubleshooting

### Issue 1: Python Version Too Old 

**Error:** `Package 'mcp' requires a different Python: 3.9.2 not in '>=3.10'`

**Cause:** Python 3.9 or older installed.

**Solution:**
```powershell
# Check all Python versions installed
py --list

# If Python 3.11+ is listed, use it:
py -3.11 -m venv .venv

# Otherwise, install Python 3.11+ from python.org
# Then recreate venv:
python -m venv .venv
```

### Issue 2: Missing fastmcp Module

**Error:** `ModuleNotFoundError: No module named 'fastmcp'`

**Cause:** Package `fastmcp` not installed (requires both `mcp` and `fastmcp`).

**Solution:**
```powershell
# Activate venv first
.\.venv\Scripts\Activate.ps1

# Install from wheels (if using offline install)
.\install_offline.ps1

# Or install manually
pip install fastmcp mcp
```

**Note:** The offline installer includes `fastmcp`. If you see this error, re-run `install_offline.ps1`.

### Issue 3: Virtual Environment Keeps Deactivating

**Symptom:** Commands run with system Python instead of venv Python, causing "dotenv not found" errors.

**Cause:** Terminal not staying in venv context.

**Solution:**

Update VS Code settings.json to use absolute path:

```json
{
  "github.copilot.chat.mcp.servers": {
    "pqa_validator": {
      "command": "C:\\path\\to\\pqa_validator\\.venv\\Scripts\\python.exe",
      "args": ["C:\\path\\to\\pqa_validator\\mcp_server_fast.py"]
    }
  }
}
```

**Important:** Use absolute paths for both `command` and `args`.

### Issue 4: MCP Server Appears to Hang

**Symptom:** Running `python mcp_server_fast.py` shows no output and appears stuck.

**This is NORMAL!** MCP servers wait for stdio input from clients (VS Code Copilot).

**How to verify it works:**
1. Don't test by running the script manually
2. Configure VS Code settings.json (see Step 4)
3. Restart VS Code
4. Open Copilot Chat and ask: `@pqa_validator list rules`
5. If you get a response, the server works correctly

### Issue 5: No Virtual Environment in VS Code

**Symptom:** VS Code shows "No environment" or can't find Python.

**Solution:**
```powershell
# Create venv if missing
python -m venv .venv

# Reload VS Code window
# Ctrl+Shift+P  "Developer: Reload Window"

# Select Python interpreter
# Ctrl+Shift+P  "Python: Select Interpreter"
# Choose: .\.venv\Scripts\python.exe
```

### Issue 6: "Python 3.10 or higher is required" (Legacy)

### Issue 6: "Python 3.10 or higher is required" (Legacy)

**Error:** `ERROR: Could not find a version that satisfies the requirement fastmcp`

**Cause:** Python 3.9 or older (same as Issue 1).

**Solution:** See Issue 1 above.

### "Git not installed" or "Cannot find git"

**This should NOT happen with offline install!**

If you see this, you're using wrong requirements file.

**Solution:**
```powershell
# Use offline install script (includes all dependencies)
.\install_offline.ps1

# NOT this (needs GitHub access):
# pip install -r requirements.txt  
```

---

##  Quick Reference

### Daily Workflow (Copilot Chat)

```
1. Open VS Code
2. Open Copilot Chat (Ctrl+Shift+I)
3. Ask: @pqa_validator validate <username>
4. Review results in chat
```

**That's it!** No need to remember commands or scripts.

### First-Time Setup Only

```powershell
# 1. Extract package
unzip pqa_validator_mcp.zip

# 2. Run offline install (one time)
.\install_offline.ps1

# 3. Edit .env with your username
notepad .env

# 4. Setup password (per terminal session)
.\setup_credentials.ps1

# 5. Configure VS Code (see Step 4 above)

# 6. Restart VS Code
```

### Example Copilot Questions

```
@pqa_validator validate IOH81HC

@pqa_validator check compliance for DUY3HC

@pqa_validator show all PRPL rules

@pqa_validator explain PRPL 11

@pqa_validator validate my items
```

### Troubleshooting Commands (Terminal)

### Troubleshooting Commands (Terminal)

Only use these if Copilot is not working:

```powershell
# Test if credentials work
python validate_user_items.py <username>

# Check MCP server is running
python mcp_server_fast.py

# Re-setup credentials (if session expired)
.\setup_credentials.ps1
```

### File Structure

```
pqa_validator/
 .env                       Your RQ1 username (RQ1_USER=XXX)
 setup_credentials.ps1      Run this to enter password
 validate_user_items.py     Main script to check compliance
 mcp_server_fast.py         Copilot integration server
 install_offline.ps1        One-time setup
 wheels/                    All dependencies (49 packages)
 QUICKSTART.md             This file
```

---

##  Detailed Documentation

- **Full Setup Guide:** `VSCODE_SETUP.md`
- **Project Info:** `README.md`

---

##  Need Help

1. Check `VSCODE_SETUP.md` for detailed troubleshooting
2. Contact PQA team
3. Open issue in project repository

---

##  Current Status

 **Phase 1 Complete:** 11 priority PRPL rules
- PRPL 01, 02, 03, 07: Basic validations
- PRPL 11, 12, 13: IFD checks
- PRPL 14, 15, 16, 18: Advanced validations

 **Phase 2 In Progress:** Additional 26 rules

---

##  Security Note

- Never commit `.env` file to git
- Store RQ1 password securely
- Tool runs locally - data stays in Bosch network

---

**Version:** 1.0.0 (FastMCP)  
**Updated:** December 30, 2025
