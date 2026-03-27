# PQA Validator - VS Code Setup Guide

## For VS Code Users Only

This guide is specifically for teams using **VS Code with Copilot Chat**. No Claude Desktop needed.

---

## Quick Start

### 1. Installation

```bash
# Extract package
unzip pqa_validator_mcp.zip
cd pqa_validator

# Create virtual environment (recommended)
python -m venv .venv
.venv\Scripts\activate  # Windows
# source .venv/bin/activate  # Mac/Linux

# Install dependencies
pip install -r requirements.txt
```

### 2. Configure Credentials

```bash
# Copy template
cp .env.template .env

# Edit .env file
RQ1_USER=your_ntid
RQ1_PASSWORD=your_rq1_password
```

### 3. Test Server

```bash
python mcp_server_fast.py
```

You should see: FastMCP server running without errors.

### 4. Configure VS Code

**Method 1: Workspace Settings** (Recommended for team sharing)

Create `.vscode/settings.json` in your workspace:

```json
{
  "github.copilot.chat.mcp.servers": {
    "pqa_validator": {
      "command": "python",
      "args": ["${workspaceFolder}/mcp_server_fast.py"],
      "env": {
        "PYTHONPATH": "${workspaceFolder}"
      }
    }
  }
}
```

**Method 2: User Settings** (Personal use only)

1. Open VS Code Settings (Ctrl+,)
2. Search for "copilot mcp"
3. Edit `settings.json`:

```json
{
  "github.copilot.chat.mcp.servers": {
    "pqa_validator": {
      "command": "python",
      "args": ["C:\\path\\to\\pqa_validator\\mcp_server_fast.py"]
    }
  }
}
```

### 5. Restart VS Code

Close and reopen VS Code to load MCP server.

---

## Usage in Copilot Chat

### Basic Commands

```
Validate RQ1 items for user DUY3HC
```

```
List all PRPL rules
```

```
Check PRPL 11 compliance for user TRE5HC
```

### Advanced Queries

```
Can you validate my items and show violations in JSON format?
(Use username from RQ1_USER in .env)
```

```
Compare PRPL compliance between users DUY3HC and NHK5HC
```

```
Show me only critical violations for user TRE5HC
```

---

## Available Tools

### 1. `validate_user_items`

Validates all RQ1 items (Issues, Releases, Workitems) against PRPL rules.

**Parameters:**
- `username` (required): RQ1 username
- `auth_user` (optional): Override authenticated user
- `output_format` (optional): "text" or "json"

**Example Copilot prompts:**
```
Validate items for DUY3HC
Show validation results in JSON format for user NHK5HC
```

### 2. `list_prpl_rules`

Lists all 11 implemented PRPL rules with descriptions.

**Example:**
```
What PRPL rules are implemented?
List all available rules
```

### 3. `check_rule_compliance`

Checks compliance for a specific PRPL rule.

**Parameters:**
- `username` (required)
- `rule_id` (required): "PRPL 01" to "PRPL 18"

**Example:**
```
Check PRPL 14 compliance for TRE5HC
How many PRPL 11 violations does DUY3HC have?
```

---

## FastMCP Features

? **HTTP endpoint** - Server runs on `http://localhost:8000` by default  
? **Auto-reload** - Changes to code reload automatically  
? **Web UI** - Browse to `http://localhost:8000/docs` for interactive API docs  
? **Async execution** - Non-blocking validation  
? **VSCode-first** - Optimized for Copilot Chat integration

---

## Troubleshooting

### MCP Server Not Found in Copilot

1. **Check server is running:**
   ```bash
   python mcp_server_fast.py
   # Should show: Serving on http://localhost:8000
   ```

2. **Verify VS Code settings:**
   - Open Command Palette (Ctrl+Shift+P)
   - Type "Preferences: Open User Settings (JSON)"
   - Confirm `github.copilot.chat.mcp.servers` exists

3. **Restart VS Code completely** (not just reload window)

4. **Check Copilot logs:**
   - View ? Output ? Select "GitHub Copilot Chat" from dropdown
   - Look for MCP connection errors

### Authentication Errors

```
Error: Authentication failed
```

**Solution:**
- Check `.env` file has correct `RQ1_USER` and `RQ1_PASSWORD`
- Verify credentials work in RQ1 web interface
- Ensure you're on Bosch network (VPN if remote)

### Slow Responses

```
Validation taking 30+ seconds
```

**Expected behavior** for users with many items (>100). RQ1 API has pagination limits.

**Workaround:**
- Use `check_rule_compliance` for specific rules instead of full validation
- Run during off-peak hours

### Port Already in Use

```
Error: Address already in use
```

**Solution:**
```bash
# Kill existing server
Get-Process | Where-Object {$_.ProcessName -eq "python"} | Stop-Process -Force

# Or change port in mcp_server_fast.py:
# mcp.run(port=8001)
```

---

## Team Setup Best Practices

### For Team Leads

1. **Create shared workspace:**
   ```bash
   # Add to git repo
   .vscode/settings.json  # MCP config
   .env.template          # Credentials template
   ```

2. **Add to .gitignore:**
   ```
   .env
   .venv/
   __pycache__/
   ```

3. **Document in team wiki:**
   - Link to this guide
   - RQ1 credentials location (password vault)
   - Support contact

### For Team Members

1. Clone repo
2. Copy `.env.template` to `.env`
3. Get RQ1 password from team vault
4. Run `pip install -r requirements.txt`
5. Open workspace in VS Code
6. MCP auto-loads from `.vscode/settings.json`

---

## Advanced Configuration

### Custom Port

Edit `mcp_server_fast.py`:

```python
if __name__ == "__main__":
    mcp.run(port=8888)  # Change from default 8000
```

Update VS Code settings:
```json
{
  "github.copilot.chat.mcp.servers": {
    "pqa_validator": {
      "command": "python",
      "args": ["mcp_server_fast.py"],
      "env": {
        "FASTMCP_PORT": "8888"
      }
    }
  }
}
```

### Multiple Environments

```bash
# Development
RQ1_USER=DAB5HC
RQ1_ENV=ACCEPTANCE

# Production (edit mcp_server_fast.py to use RQ1_ENV)
RQ1_USER=DAB5HC
RQ1_ENV=PRODUCTION
```

### Proxy Settings

Already configured for Bosch network. Edit `.env` if needed:

```bash
HTTP_PROXY=http://rb-proxy-apac.bosch.com:8080
HTTPS_PROXY=http://rb-proxy-apac.bosch.com:8080
```

---

## What's Next

**Phase 1 Complete:** 11 priority PRPL rules ?

**Phase 2 In Progress:**
- Additional 26 rules (BBM, QAM, IPT)
- Batch user validation
- Scheduled compliance checks
- Email/Teams notifications

**Future:**
- Web dashboard
- Historical trend analysis
- Custom rule configuration

---

## Support

**Issues:**
- Check logs in terminal where server is running
- Enable debug mode: Set `DEBUG=true` in `.env`

**Questions:**
- Contact PQA team
- Check project README.md
- Review RQ1 Building Block docs

---

## Version

**Current:** 1.0.0 (FastMCP)  
**Updated:** December 30, 2025  
**Compatibility:** VS Code 1.85+, GitHub Copilot Chat extension
