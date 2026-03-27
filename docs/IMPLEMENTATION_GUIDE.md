# Implementation Guide

**Project**: PQA_Agent - RQ1 Rule Validation MCP Server
**Language**: Python
**Target**: Model Context Protocol server for AI assistant integration

---

## Quick Start

### Prerequisites

1. **Python 3.11+**
2. **building-block-rq1** library (v1.6.0)
3. **RQ1 Credentials** (ACCEPTANCE environment)
4. **MCP Python SDK**

### Installation

```bash
# Navigate to project
cd RQ1_agent

# Create virtual environment
python -m venv .venv
.venv\Scripts\activate  # Windows
source .venv/bin/activate  # Linux/Mac

# Install dependencies
pip install building-block-rq1==1.6.0
pip install mcp pandas

# Setup environment variables
cp .env.example .env
# Edit .env with your credentials:
# RQ1_USER=your_username
# RQ1_PASSWORD=your_password
# RQ1_TOOLNAME=PQA_Agent
# RQ1_TOOLVERSION=1.0.0
```

### Test Connection

```python
from rq1_client import RQ1Client

client = RQ1Client()
issue = client.get_issue("RQ1000000001234")
print(f"Found issue: {issue.get('rq1_number')}")
```

---

## Architecture

### Project Structure

```
RQ1_agent/
??? docs/                           # Documentation
?   ??? RULES_COMPLETE.md          # All 101 Excel rules
?   ??? EXCEL_TO_JAVA_MAPPING.md   # Excel-Java mapping
?   ??? IMPLEMENTATION_GUIDE.md    # This file
??? rq1/
?   ??? POST_V_1.0.3/              # Original POST Tool (Java)
?       ??? parsed_rules/          # Excel parsed output
??? rq1_client.py                  # RQ1 data access layer
??? rq1_server.py                  # MCP server implementation
??? parse_excel_rules.py           # Excel parser (reusable)
??? .env.example                   # Environment template
??? README.md                      # Project overview
```

### Components

**rq1_client.py**: RQ1 Data Access
- Uses building-block-rq1 library
- Methods: get_issue_details, query_my_issues, query_issues, get_issue_history
- Handles authentication and connection

**rq1_server.py**: MCP Server
- Exposes 4 MCP tools
- Integrates with rq1_client
- Provides AI assistant interface

**parse_excel_rules.py**: Rule Parser
- Parses SW_QAMRuleSet_Tmplt.xlsm
- Outputs markdown documentation
- Reusable for future Excel versions

---

## Rule Implementation Pattern

### Step-by-Step Process

#### 1. Choose a Rule

Start with Top 10 priority rules from `EXCEL_TO_JAVA_MAPPING.md`:
1. Rule_IssueSW_FmeaCheck
2. Rule_IssueSW_ASIL
3. Rule_CheckForMissing_BaselineLink
4. Rule_Bc_NamingConvention
5. Rule_Fc_NamingConvention
6. Rule_CheckDatesForBcAndFc
7. Rule_IssueFD_WithoutLinkToBc
8. Rule_Bc_WithoutLinkToPst
9. Rule_IssueSW_MissingAffectedIssueComment
10. Rule_Bc_CheckPstDates

#### 2. Study Rule Specifications

**Excel Reference**: `docs/RULES_COMPLETE.md`
- Find rule by ID (e.g., BBM 10, QADO 01.00.00)
- Note execution level (mandatory/optional)
- Check retrospective period

**Java Reference**: `RQ1_RULES_DETAILED.md` (in root)
- Find corresponding Java rule
- Review field requirements
- Check validation logic

#### 3. Create Rule Class

```python
# rules/rule_issue_sw_fmea_check.py
from typing import Dict, List
from rq1_client import RQ1Client

class Rule_IssueSW_FmeaCheck:
    """
    Excel: BBM rules (implicit FMEA validation)
    Java: Rule_IssueSW_FmeaCheck (Priority #1)
    
    Purpose: Ensure FMEA comment provided when FMEA state = "Not Required"
    """
    
    def __init__(self, client: RQ1Client):
        self.client = client
        self.rule_id = "BBM_FMEA"
        self.name = "FMEA Comment Validation"
        self.severity = "WARNING"
    
    def validate(self, issue_sw: Dict) -> List[Dict]:
        """
        Validate single Issue-SW record
        
        Args:
            issue_sw: Issue-SW record from RQ1
        
        Returns:
            List of violations (empty if compliant)
        """
        violations = []
        
        # Skip if state is CANCELED or CONFLICTED
        if issue_sw.get('STATE') in ['CANCELED', 'CONFLICTED']:
            return violations
        
        # Check FMEA state
        fmea_state = issue_sw.get('FMEA_STATE')
        fmea_comment = issue_sw.get('FMEA_COMMENT', '').strip()
        fmea_change_comment = issue_sw.get('FMEA_CHANGE_COMMENT', '').strip()
        
        # Rule: If FMEA_STATE = "Not Required", must have comment
        if fmea_state == "Not Required":
            if not fmea_comment and not fmea_change_comment:
                violations.append({
                    'rule_id': self.rule_id,
                    'severity': self.severity,
                    'record_type': 'Issue-SW',
                    'record_id': issue_sw.get('rq1_number'),
                    'message': 'FMEA_STATE is "Not Required" but no FMEA_COMMENT or FMEA_CHANGE_COMMENT provided',
                    'field': 'FMEA_COMMENT',
                    'expected': 'Non-empty comment',
                    'actual': 'Empty'
                })
        
        return violations
    
    def validate_query(self, query: str = None) -> List[Dict]:
        """
        Validate multiple records from query
        
        Args:
            query: ClearQuest query string (optional)
        
        Returns:
            List of all violations found
        """
        all_violations = []
        
        # Query Issue-SW records
        if query:
            issues = self.client.query_issues(query)
        else:
            # Default: My open Issue-SW records
            issues = self.client.query_my_issues()
        
        # Validate each issue
        for issue in issues:
            if issue.get('record_type') == 'Issue-SW':
                violations = self.validate(issue)
                all_violations.extend(violations)
        
        return all_violations
```

#### 4. Add Unit Tests

```python
# tests/test_rule_issue_sw_fmea_check.py
import pytest
from unittest.mock import Mock
from rules.rule_issue_sw_fmea_check import Rule_IssueSW_FmeaCheck

def test_fmea_not_required_without_comment():
    """Test violation when FMEA state = Not Required and no comment"""
    client = Mock()
    rule = Rule_IssueSW_FmeaCheck(client)
    
    issue = {
        'rq1_number': 'RQ1000000001234',
        'record_type': 'Issue-SW',
        'STATE': 'OPEN',
        'FMEA_STATE': 'Not Required',
        'FMEA_COMMENT': '',
        'FMEA_CHANGE_COMMENT': ''
    }
    
    violations = rule.validate(issue)
    
    assert len(violations) == 1
    assert violations[0]['rule_id'] == 'BBM_FMEA'
    assert violations[0]['severity'] == 'WARNING'

def test_fmea_not_required_with_comment():
    """Test no violation when comment provided"""
    client = Mock()
    rule = Rule_IssueSW_FmeaCheck(client)
    
    issue = {
        'rq1_number': 'RQ1000000001234',
        'record_type': 'Issue-SW',
        'STATE': 'OPEN',
        'FMEA_STATE': 'Not Required',
        'FMEA_COMMENT': 'No FMEA needed for documentation change',
        'FMEA_CHANGE_COMMENT': ''
    }
    
    violations = rule.validate(issue)
    
    assert len(violations) == 0

def test_canceled_issue_skipped():
    """Test that CANCELED issues are skipped"""
    client = Mock()
    rule = Rule_IssueSW_FmeaCheck(client)
    
    issue = {
        'rq1_number': 'RQ1000000001234',
        'record_type': 'Issue-SW',
        'STATE': 'CANCELED',
        'FMEA_STATE': 'Not Required',
        'FMEA_COMMENT': ''
    }
    
    violations = rule.validate(issue)
    
    assert len(violations) == 0
```

#### 5. Integrate with MCP Server

```python
# rq1_server.py
from rules.rule_issue_sw_fmea_check import Rule_IssueSW_FmeaCheck

# Add to MCP tools
@server.call_tool()
async def validate_rule(arguments: dict) -> Sequence[TextContent | ImageContent | EmbeddedResource]:
    """Validate specific rule against RQ1 records"""
    rule_id = arguments.get("rule_id")
    query = arguments.get("query")
    
    if rule_id == "BBM_FMEA":
        rule = Rule_IssueSW_FmeaCheck(client)
        violations = rule.validate_query(query)
        
        return [
            TextContent(
                type="text",
                text=json.dumps({
                    "rule": rule_id,
                    "violations_count": len(violations),
                    "violations": violations
                }, indent=2)
            )
        ]
```

#### 6. Test with Real Data

```bash
# Test with ACCEPTANCE environment
python -c "
from rq1_client import RQ1Client
from rules.rule_issue_sw_fmea_check import Rule_IssueSW_FmeaCheck

client = RQ1Client()
rule = Rule_IssueSW_FmeaCheck(client)

# Test with specific issue
issue = client.get_issue_details('RQ1000000001234')
violations = rule.validate(issue)
print(f'Found {len(violations)} violations')

# Test with query
query = 'STATE in (OPEN, COMMITTED, TESTED) AND SUBMIT_DATE > 2024-01-01'
violations = rule.validate_query(query)
print(f'Found {len(violations)} violations in query results')
"
```

---

## Common Patterns

### Pattern 1: State Filtering

Most rules skip CANCELED and CONFLICTED states:

```python
if record.get('STATE') in ['CANCELED', 'CONFLICTED']:
    return []
```

### Pattern 2: Date Filtering

Many rules check retrospectively (e.g., 6 months):

```python
from datetime import datetime, timedelta

def should_check_record(record: Dict, months_back: int = 6) -> bool:
    submit_date = record.get('SUBMIT_DATE')
    if not submit_date:
        return False
    
    cutoff_date = datetime.now() - timedelta(days=months_back * 30)
    return submit_date >= cutoff_date
```

### Pattern 3: Link Traversal

Check related records (parent/child):

```python
def validate_with_links(self, issue_fd: Dict) -> List[Dict]:
    violations = []
    
    # Get linked BC records
    bc_links = issue_fd.get('links', {}).get('BC', [])
    
    for bc_id in bc_links:
        bc = self.client.get_issue_details(bc_id)
        # Validate BC...
    
    return violations
```

### Pattern 4: Naming Pattern Validation

Use regex for naming conventions:

```python
import re

def validate_naming(name: str, pattern: str) -> bool:
    """
    Validate record name matches pattern
    
    Args:
        name: Record name
        pattern: Regex pattern (e.g., r'^BC[0-9]{5}[A-Z]{2}$')
    
    Returns:
        True if matches, False otherwise
    """
    return bool(re.match(pattern, name))
```

---

## Testing Strategy

### Unit Tests

Test each rule in isolation with mock data:

```bash
pytest tests/test_rule_*.py
```

### Integration Tests

Test with real RQ1 ACCEPTANCE data:

```bash
pytest tests/integration/ --env=acceptance
```

### Regression Tests

Compare outputs against POST Tool:

```bash
# Run POST Tool
java -jar rq1/POST_V_1.0.3/POST-1.0.3-jar-with-dependencies.jar \
  --query "STATE=OPEN" --output post_output.json

# Run PQA_Agent
python run_validation.py --query "STATE=OPEN" --output pqa_output.json

# Compare
python compare_outputs.py post_output.json pqa_output.json
```

---

## Performance Tips

### 1. Batch Queries

Query multiple records at once instead of individual lookups:

```python
# Bad: N queries
for issue_id in issue_ids:
    issue = client.get_issue_details(issue_id)

# Good: 1 query
query = f"rq1_number IN ({','.join(issue_ids)})"
issues = client.query_issues(query)
```

### 2. Cache Results

Cache frequently accessed data:

```python
from functools import lru_cache

@lru_cache(maxsize=1000)
def get_issue_cached(issue_id: str) -> Dict:
    return client.get_issue_details(issue_id)
```

### 3. Parallel Processing

Use async for independent validations:

```python
import asyncio

async def validate_all_rules(issue: Dict) -> List[Dict]:
    tasks = [
        rule1.validate_async(issue),
        rule2.validate_async(issue),
        rule3.validate_async(issue)
    ]
    results = await asyncio.gather(*tasks)
    return [v for result in results for v in result]
```

---

## Deployment

### Local Testing

```bash
# Start MCP server
python rq1_server.py

# Test with MCP client
python test_mcp_client.py
```

### VS Code Integration

Configure in VS Code settings (mcp.json):

```json
{
  "mcpServers": {
    "rq1": {
      "command": "python",
      "args": ["c:/path/to/RQ1_agent/rq1_server.py"],
      "env": {
        "RQ1_USER": "${env:RQ1_USER}",
        "RQ1_PASSWORD": "${env:RQ1_PASSWORD}"
      }
    }
  }
}
```

### Production Deployment

Use supervisor or systemd to manage MCP server process.

---

## Troubleshooting

### Connection Issues

```python
# Test RQ1 connection
from rq1_client import RQ1Client

try:
    client = RQ1Client()
    result = client.query_issues("rq1_number = 'RQ1000000001234'")
    print("Connection OK")
except Exception as e:
    print(f"Connection failed: {e}")
```

### Authentication Errors

Check .env file has correct credentials:
- RQ1_USER
- RQ1_PASSWORD
- RQ1_TOOLNAME
- RQ1_TOOLVERSION

### Query Syntax Errors

Refer to building-block-rq1 documentation for ClearQuest query syntax.

---

## Next Steps

1. **Review Top 10 Rules**: Study `EXCEL_TO_JAVA_MAPPING.md`
2. **Implement Rule #1**: Start with Rule_IssueSW_FmeaCheck
3. **Test Thoroughly**: Unit tests + ACCEPTANCE data
4. **Document Findings**: Update this guide with learnings
5. **Iterate**: Move to Rule #2, #3, etc.

---

## References

- **Excel Rules**: `docs/RULES_COMPLETE.md`
- **Java Rules**: `RQ1_RULES_DETAILED.md`
- **Mapping**: `docs/EXCEL_TO_JAVA_MAPPING.md`
- **building-block-rq1**: https://github.com/bosch/building-block-rq1
- **MCP Docs**: https://modelcontextprotocol.io
