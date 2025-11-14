# PQA_AGENT STRATEGY: Excel-Driven Implementation

**Date**: 2024-11-13
**Status**: Approved Strategy

---

## ?? CORE STRATEGY

### Problem Statement:
- **Excel** (SW_QAMRuleSet_Tmplt.xlsm) = Official process, gets updated regularly
- **VBS/Java** (POST Tool) = Proven implementation, but not updated
- **PQA_Agent** = New MCP server, needs to align with official process

### Solution:
**Implement rules that have BOTH Excel backing AND Java/VBS proof**

---

## ? IMPLEMENTATION APPROACH

### 1. Excel as Source of Truth
- ? Official QA process document
- ? Updated by process owners
- ? Version tracked (currently v3.8, 2022-07-06)
- ? Defines WHAT to check

### 2. Java/VBS as Implementation Reference
- ? Proven validation logic
- ? Shows HOW to implement
- ? Field usage examples
- ? Edge cases handled

### 3. PQA_Agent Implementation
- ? Only implement rules with BOTH Excel + Java
- ? Use Excel rule IDs for documentation
- ? Use Java logic for implementation
- ? Track mapping in `EXCEL_JAVA_MAPPING.md`

---

## ?? BENEFITS

### 1. Process Alignment ?
- PQA_Agent follows official QA process
- Easy to explain to stakeholders
- Compliance with corporate standards

### 2. Proven Logic ?
- Don't reinvent the wheel
- Use tested Java code as reference
- Avoid implementation mistakes

### 3. Change Management ?
- Detect when Excel updates
- Analyze impact automatically
- Decide: implement, skip, or review

### 4. Future-Proof ?
- Can add new rules when Excel updates
- Can remove deprecated rules
- Always aligned with latest process

---

## ?? TOOLS CREATED

### 1. `parse_excel_rules.py`
**Purpose**: Parse Excel to clean markdown
```bash
python parse_excel_rules.py
```
**Output**:
- `BBM_RULES_CLEAN.md` (23 rules)
- `QAM_RULES_CLEAN.md` (5 rules)
- `EXCEL_RULES_SUMMARY.md`

### 2. `excel_change_detector.py`
**Purpose**: Detect changes and analyze impact
```bash
python excel_change_detector.py \
  --old rq1/POST_V_1.0.3/SW_QAMRuleSet_Tmplt.xlsm \
  --new rq1/POST_V_2.0.0/SW_QAMRuleSet_Tmplt.xlsm \
  --output IMPACT_REPORT.md
```
**Output**:
- Impact report with HIGH/MEDIUM/LOW severity
- Action items for each change
- Recommendations

### 3. `EXCEL_JAVA_MAPPING.md`
**Purpose**: Track which Excel rules map to Java rules
**Content**:
- Confirmed mappings (Excel ? Java)
- Implementation priorities
- PQA relevance flags

---

## ?? CURRENT STATUS

### Excel Rules (28 total)
- **BBM**: 23 rules (metrics)
- **QAM**: 5 rules (process)
- **IPT**: Not parsed (customer-specific)

### Java Rules (37 total)
From POST Tool v1.0.3 extracted code

### Mapped Rules (10 priority)
Rules with both Excel + Java:
1. ? FMEA Check (QADO 02 ? Java)
2. ? ASIL Safety (BBM 07 ? Java)
3. ? Baseline Links (BBM 10,12,16 ? Java)
4. ? BC Naming (BBM implicit ? Java)
5. ? FC Naming (BBM implicit ? Java)
6. ? Date Logic (BBM dates ? Java)
7. ? Issue-FD Closure (QADO 01 ? Java)
8. ? PST Links (BBM ? Java)
9. ? Comments (QADO 02 ? Java)
10. ? PST Dates (BBM ? Java)

---

## ?? WORKFLOW

### When Implementing New Rule:

1. **Check Excel** - Is rule in Excel? What does it say?
2. **Check Java** - Is there Java implementation?
3. **Check Mapping** - Is it already mapped in `EXCEL_JAVA_MAPPING.md`?
4. **Implement** - Use Java logic, document Excel ID
5. **Test** - Validate with real RQ1 data
6. **Document** - Update all docs with Excel reference

### When Excel Updates:

1. **Receive new Excel** file (e.g., v3.9)
2. **Run detector**:
   ```bash
   python excel_change_detector.py \
     --old rq1/POST_V_1.0.3/SW_QAMRuleSet_Tmplt.xlsm \
     --new rq1/POST_V_1.0.4/SW_QAMRuleSet_Tmplt.xlsm
   ```
3. **Review impact report** - HIGH/MEDIUM/LOW
4. **Take action**:
   - HIGH: Update code immediately
   - MEDIUM: Add to backlog
   - LOW: No action
5. **Update mapping** - Adjust `EXCEL_JAVA_MAPPING.md`
6. **Re-parse Excel** - Generate new clean docs
7. **Update version** - Track Excel version used

---

## ?? DOCUMENTATION STRUCTURE

### Primary Docs (Excel + Java):
1. **`EXCEL_JAVA_MAPPING.md`** - Mapping table
2. **`RQ1_RULES.md`** - Quick reference (with Excel IDs)
3. **`RQ1_RULES_DETAILED.md`** - Full specs (with Excel IDs)

### Supporting Docs:
4. **`EXCEL_VS_JAVA_RULES_COMPARISON.md`** - Why they're different
5. **`rq1/POST_V_1.0.3/parsed_rules/`** - Clean Excel rules
6. **`EXCEL_IMPACT_REPORT.md`** - Generated when Excel changes

### Code:
7. **`rq1_client.py`** - Implementation with Excel comments
8. **`rq1_server.py`** - MCP tools
9. **`parse_excel_rules.py`** - Excel parser
10. **`excel_change_detector.py`** - Change detector

---

## ?? PHASE 2A IMPLEMENTATION (Updated)

### Top 10 Rules with Excel References:

| # | Java Rule | Excel Reference | Priority |
|---|-----------|-----------------|----------|
| 1 | Rule_IssueSW_FmeaCheck | QADO 02.00.00 | ??? |
| 2 | Rule_IssueSW_ASIL | BBM 07 (safety) | ??? |
| 3 | Rule_CheckForMissing_BaselineLink | BBM 10, 12, 16 | ??? |
| 4 | Rule_Bc_NamingConvention | BBM (implicit) | ?? |
| 5 | Rule_Fc_NamingConvention | BBM (implicit) | ?? |
| 6 | Rule_CheckDatesForBcAndFc | BBM dates | ?? |
| 7 | Rule_IssueFD_WithoutLinkToBc | QADO 01.00.00 | ?? |
| 8 | Rule_Bc_WithoutLinkToPst | BBM traceability | ? |
| 9 | Rule_IssueSW_MissingAffectedIssueComment | QADO comments | ? |
| 10 | Rule_Bc_CheckPstDates | BBM dates | ? |

**Code Comments Format**:
```python
def _check_fmea_comment(self, issue: Dict) -> Dict:
    """
    Rule: FMEA comment validation
    
    Excel Reference: QADO 02.00.00
    - "Check for requirement based development"
    - Documentation quality validation
    
    Java Source: Rule_IssueSW_FmeaCheck.java
    - Checks FMEA_STATE = "Not Required" ? comment required
    - Only after 2015-01-01
    """
    # Implementation...
```

---

## ?? MAINTENANCE WORKFLOW

### Monthly/Quarterly:
1. Check for new Excel versions
2. Run change detector if new version found
3. Review impact report
4. Update implementation if needed
5. Update mapping document

### When Implementing:
1. Always note Excel rule ID in code comments
2. Always note Java source file
3. Update both `EXCEL_JAVA_MAPPING.md` and rule docs

### When Testing:
1. Reference Excel rule for expected behavior
2. Use Java test cases for validation
3. Document any discrepancies

---

## ?? EDGE CASES

### Case 1: Excel has rule, Java doesn't
**Action**: Document in mapping as "No Java implementation"
**Decision**: 
- If PQA-relevant ? Add to backlog (Phase 2B/2C)
- If not relevant ? Skip

### Case 2: Java has rule, Excel doesn't
**Action**: Document as "Implementation-specific"
**Decision**: Keep implementation (proven to work)

### Case 3: Excel changes, Java doesn't exist
**Action**: Run change detector ? Shows as HIGH/MEDIUM impact
**Decision**:
- If HIGH ? Implement new logic
- If MEDIUM ? Plan for future
- If LOW ? No action

### Case 4: Conflicting logic
**Action**: Document discrepancy
**Decision**: Excel wins (official process), but note Java approach

---

## ? SUCCESS CRITERIA

### For Phase 2A:
- [ ] All 10 rules implemented with Excel references
- [ ] Code comments include Excel rule IDs
- [ ] `EXCEL_JAVA_MAPPING.md` is complete
- [ ] All docs reference Excel v3.8 (2022-07-06)
- [ ] Change detector tested and working

### For Long-term:
- [ ] Excel version tracked in all docs
- [ ] Change detection automated (CI/CD?)
- [ ] Stakeholders understand Excel-driven approach
- [ ] Easy to explain PQA_Agent rules to auditors

---

## ?? REFERENCES

### Excel Files:
- Current: `rq1/POST_V_1.0.3/SW_QAMRuleSet_Tmplt.xlsm` (v3.8, 2022-07-06)
- Future: `rq1/POST_V_X.Y.Z/SW_QAMRuleSet_Tmplt.xlsm`

### Java Source:
- Location: `rq1/POST_extracted/DataModel/Rq1/Monitoring/Rule_*.java`
- Version: POST Tool v1.0.3

### Tools:
- Parser: `parse_excel_rules.py`
- Detector: `excel_change_detector.py`
- Mapping: `EXCEL_JAVA_MAPPING.md`

---

**Status**: ? Strategy Approved
**Next Action**: Begin Phase 2A implementation with Excel references
**Owner**: PQA_Agent Development Team
