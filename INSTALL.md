# PQA Validator - Installation Guide

## For End Users

### Prerequisites
- Python 3.8+
- Access to Bosch network (for RQ1 connection)
- RQ1 credentials

### Installation Steps

1. **Download and extract package**
   ```bash
   # Extract pqa_validator_mcp.zip to your desired location
   cd path/to/pqa_validator
   ```

2. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

3. **Configure credentials**
   ```bash
   cp .env.template .env
   ```
   
   Edit `.env` file:
   ```bash
   RQ1_USER=your_ntid
   RQ1_PASSWORD=your_rq1_password
   ```

4. **Test the server**
   ```bash
   python mcp_server.py
   ```
   Should start without errors.

---

## Integration with MCP Clients

### Option 1: Claude Desktop (Recommended)

1. **Locate Claude config file:**
   - Windows: `%APPDATA%\Claude\claude_desktop_config.json`
   - Mac: `~/Library/Application Support/Claude/claude_desktop_config.json`

2. **Add server configuration:**
   ```json
   {
     "mcpServers": {
       "pqa_validator": {
         "command": "python",
         "args": ["C:\\path\\to\\pqa_validator\\mcp_server.py"],
         "env": {
           "RQ1_USER": "your_ntid",
           "RQ1_PASSWORD": "your_password"
         }
       }
     }
   }
   ```

3. **Restart Claude Desktop**

4. **Test in Claude:**
   ```
   Can you list all PRPL rules available?
   ```
   or
   ```
   Validate RQ1 items for user DUY3HC
   ```

### Option 2: Cline (VSCode Extension)

1. **Install Cline extension** in VSCode

2. **Open Cline settings** (gear icon in Cline panel)

3. **Add MCP Server:**
   - Click "Edit MCP Settings"
   - Add configuration:
   ```json
   {
     "mcpServers": {
       "pqa_validator": {
         "command": "python",
         "args": ["C:\\path\\to\\pqa_validator\\mcp_server.py"]
       }
     }
   }
   ```

4. **Reload Cline**

5. **Use in Cline chat:**
   ```
   @pqa_validator validate user TRE5HC
   ```

### Option 3: VS Code Copilot (Experimental)

See `.vscode/settings.json` for configuration example.

---

## Available Tools

### 1. `validate_user_items`
Validates all RQ1 items for a user.

**Parameters:**
- `username` (required): RQ1 username
- `auth_user` (optional): Override authenticated user
- `format` (optional): "text" or "json"

**Example:**
```
Validate items for user DUY3HC in JSON format
```

### 2. `list_prpl_rules`
Lists all implemented PRPL rules.

**Example:**
```
Show me all available PRPL rules
```

### 3. `check_rule_compliance`
Check compliance for specific rule.

**Parameters:**
- `username` (required)
- `rule_id` (required): e.g., "PRPL 11", "PRPL 14"

**Example:**
```
Check PRPL 11 compliance for user NHK5HC
```

---

## Troubleshooting

### Connection Issues

**Error: Authentication failed**
- Check RQ1_USER and RQ1_PASSWORD in `.env`
- Verify credentials work in RQ1 web interface

**Error: Cannot connect to RQ1**
- Ensure you're on Bosch network (VPN if remote)
- Check proxy settings if needed

**Error: Module 'mcp' not found**
- Run: `pip install mcp`
- Verify Python environment

### Performance

**Slow validation**
- Large item counts (>100) may take 30-60 seconds
- This is normal due to RQ1 API pagination

### Getting Help

- Check `DEVMATE_SETUP.md` for advanced configuration
- Review logs in terminal for error details
- Contact PQA team for support

---

## Security Notes

- **Never commit `.env` file** to version control
- Store passwords securely
- `.env` file is in `.gitignore` by default
- MCP server runs locally - data stays in Bosch network

---

## What's Included

**Current Implementation (Phase 1):**
- ? 11 PRPL rules (priority rules from BBM, QAM, IPT)
- ? Real-time RQ1 validation
- ? Detailed violation reports
- ? Multiple output formats

**Coming Soon (Phase 2):**
- ?? Additional 26 rules
- ?? Batch user validation
- ?? Scheduled compliance checks
- ?? Email notifications

---

## Rules Implemented

| Rule ID | Description |
|---------|-------------|
| PRPL 01 | BC-R requested state (8 weeks before PVER) |
| PRPL 02 | Workitem planned date validation |
| PRPL 03 | Conflicted state detection |
| PRPL 07 | BC planned vs PVER dates |
| PRPL 11 | IFD 5-day SLA |
| PRPL 12 | IFD closure check |
| PRPL 13 | IFD vs BC-R planned date |
| PRPL 14 | IFD-ISW commitment sync |
| PRPL 15 | BC/FC closure after planned date |
| PRPL 16 | Workitem closure validation |
| PRPL 18 | IFD-ISW 5-day commitment delay |

---

## Version

**Current Version:** 1.0.0  
**Last Updated:** December 30, 2025  
**Compatibility:** RQ1 Building Block v1.6.0
