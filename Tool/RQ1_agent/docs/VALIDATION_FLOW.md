# Validation Flow - POST Tool vs Python Implementation

## POST Tool Flow (Original Java)

```
1. START
   ?? Initialize EcvApplication (tool name: "OfficeUtils" v1.0)
   ?? Set login credentials (username, password)
   ?? Connect to RQ1 environment (ACCEPTANCE/PRODUCTIVE)

2. FETCH ISSUE DATA
   ?? Connect via OSLC API (OslcRq1Client)
   ?? Query issue by RQ1 number (e.g., RQONE03999302)
   ?? Load full issue data (~178 fields)

3. LOAD RULES
   ?? Read Excel rules (SW_QAMRuleSet_Tmplt.xlsm)
   ?  ?? BBM Rules: 23 rules
   ?  ?? QAM Rules: 22 rules
   ?  ?? IPT Rules: 56 rules
   ?? Java rule implementations (37 rules coded)

4. EXECUTE VALIDATION
   ?? For each rule:
   ?  ?? Check if rule applies (execution level, state, category)
   ?  ?? Execute rule logic against issue data
   ?  ?? Collect result (PASS / WARNING / ERROR)
   ?? Generate warnings list

5. GENERATE REPORT
   ?? Aggregate all warnings/errors
   ?? Format report (HTML/PDF/Text)
   ?? Include:
   ?  ?? Issue summary
   ?  ?? Rules executed
   ?  ?? Pass/Fail counts
   ?  ?? Detailed warnings with explanations
   ?? OUTPUT: Validation report

6. END
```

## Current Status

**? COMPLETED:**
- Step 1: Connection ? (credentials working)
- Step 2: Fetch issue data ? (RQ1Client working, 178 fields)
- Step 3: Rules documented ? (101 Excel rules parsed, 37 Java rules found)

**? PENDING:**
- Step 4: Execute validation logic ? (not implemented)
- Step 5: Generate report ? (not implemented)

**?? BLOCKED:**
- POST Java tool: SSL certificate issue prevents direct execution

---

## Python Implementation Flow (NEW)

```
1. START - Initialize RQ1Client
   ?? Username: DAB5HC
   ?? Password: from env/config
   ?? Tool: OfficeUtils v1.0 (or PQA_Agent when whitelisted)
   ?? Environment: ACCEPTANCE (for testing)

2. FETCH ISSUE DATA ? WORKING
   ?? client.get_record_by_rq1_number("RQONE03999302")
   ?? Returns: Full issue dict (178 fields)
   ?? Key fields extracted:
      ?? dcterms:title (headline)
      ?? cq:LifeCycleState (state)
      ?? cq:Category (Requirement/Bug/...)
      ?? cq:Domain (Software/Hardware/...)
      ?? cq:Assignee (owner)
      ?? ... 173 more fields

3. LOAD RULES (TO IMPLEMENT)
   Option A: Use Excel rules as source of truth
      ?? Read: docs/RULES_COMPLETE.md (101 rules)
      ?? Priority: CRITICAL > HIGH > MEDIUM
      ?? Implement in Python

   Option B: Port Java rules
      ?? Read: POST Java source (37 rules)
      ?? Translate to Python
      ?? Maintain logic consistency

   Option C: Hybrid (RECOMMENDED)
      ?? Start with top 10 CRITICAL rules
      ?? Use Excel spec + Java reference
      ?? Implement as Python functions

4. EXECUTE VALIDATION (TO IMPLEMENT)
   For each rule:
   ```python
   def validate_rule_BBM01(issue_data):
       """BBM 01: Ratio of technical customer requirements accepted"""
       category = issue_data.get('cq:Category')
       classification = issue_data.get('cq:CommercialClassification')
       
       if category != 'Requirement':
           return {'result': 'SKIP', 'reason': 'Not a requirement'}
       
       if classification not in ['New Requirement', 'Changed Requirement']:
           return {'result': 'WARNING', 
                   'message': 'Requirement must be classified'}
       
       return {'result': 'PASS'}
   ```

5. GENERATE REPORT (TO IMPLEMENT)
   ```python
   {
       "issue_id": "RQONE03999302",
       "validation_date": "2025-11-27T10:30:00Z",
       "rules_executed": 10,
       "results": {
           "passed": 8,
           "warnings": 2,
           "errors": 0
       },
       "warnings": [
           {
               "rule_id": "BBM 01",
               "severity": "WARNING",
               "message": "Requirement must be classified",
               "field": "cq:CommercialClassification"
           }
       ]
   }
   ```

6. MCP INTEGRATION (FUTURE)
   ?? Define MCP tool: validate_rq1_issue
   ?? Input: RQ1 number
   ?? Output: Validation report JSON
   ?? AI assistant can query results
```

---

## Next Steps

### Phase 1: Core Validation Engine (Current)

**1.1 Create rule executor framework**
```python
# src/rule_engine.py
class RuleEngine:
    def __init__(self, rq1_client):
        self.client = rq1_client
        self.rules = []
    
    def register_rule(self, rule_func):
        self.rules.append(rule_func)
    
    def validate_issue(self, rq1_number):
        issue = self.client.get_record_by_rq1_number(rq1_number)
        results = []
        for rule in self.rules:
            result = rule(issue)
            results.append(result)
        return self.generate_report(results)
```

**1.2 Implement top 10 CRITICAL rules**
- Priority order from docs/RULES_COMPLETE.md
- Start with simpler field checks
- Add complex logic rules incrementally

**1.3 Create report generator**
- JSON format (for MCP)
- Human-readable text format
- Summary statistics

### Phase 2: MCP Integration
- Define MCP tools
- Handle async validation
- Return structured results

### Phase 3: Scale Up
- Add remaining rules
- Performance optimization
- Caching and batch processing

---

## Key Decisions

**Q: Port Java rules or implement from Excel?**
**A:** Hybrid approach - Use Excel as specification, reference Java for complex logic

**Q: Which rules to prioritize?**
**A:** CRITICAL rules from BBM/QAM that apply to most issues

**Q: How to handle POST tool?**
**A:** Python reimplementation - POST has insurmountable SSL issues

**Q: Report format?**
**A:** JSON primary (MCP), with optional text/HTML

---

## Current Capabilities

? **RQ1 Connection**
- Fetch full issue data (178 fields)
- Query issues by filters
- Get issue history
- Authentication working

? **Rule Documentation**  
- 101 Excel rules parsed
- 37 Java rules identified
- Priority levels defined
- Field mappings complete

? **Rule Execution**
- Not yet implemented
- Framework needed
- Need to choose implementation approach

? **Report Generation**
- Not yet implemented
- Format to be decided
- MCP integration pending
