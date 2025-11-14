# RQ1 RULES - Quick Reference Guide

> **37 Rules** from POST Tool for RQ1 Compliance Validation  
> **Purpose**: MCP implementation reference for PQA_Agent

---

## ?? Overview

- **Total Rules**: 37
- **Source**: POST Tool v1.0.3
- **Target**: Issues, BC, FC, Releases validation
- **Output**: Warnings/Failures for violations
- **Detailed Docs**: See `RQ1_RULES_DETAILED.md` for complete specifications

---

## ?? Top 10 Priority Rules (Phase 2A)

| # | Rule | Record | What It Checks | Example Violation |
|---|------|--------|----------------|-------------------|
| 1 | **FmeaCheck** | IssueSW | FMEA comment when state="Not Required" | ? FMEA_STATE=NotRequired but no comment |
| 2 | **ASIL** | IssueSW | ASIL level match I-SW ? I-FD | ? I-SW has ASIL_B but no child with B/C/D |
| 3 | **BaselineLink** | Release | DOORS link in description/comment | ? No doors:// link found |
| 4 | **Bc_NamingConvention** | BC | BC name format: BC12345AB | ? Name 'MyBaseline' invalid |
| 5 | **Fc_NamingConvention** | FC | FC name format validation | ? Name doesn't match pattern |
| 6 | **CheckDatesForBcAndFc** | BC+FC | Date logic: BC vs FC | ? BC.planned > FC.req |
| 7 | **IssueFD_WithoutLinkToBc** | IssueFD | I-FD must link to BC | ? No BC link found |
| 8 | **Bc_WithoutLinkToPst** | BC | BC must link to PST | ? No PST link found |
| 9 | **MissingAffectedIssueComment** | IssueSW | Comment for affected issues | ? Affected issues but no comment |
| 10 | **Bc_CheckPstDates** | BC | PST date validation | ? PST dates inconsistent |

**Estimated Effort**: 30-40 hours for top 10 rules

---

## ?? All 37 Rules - Quick Table

| # | Rule Name | Category | Record | Key Fields | Priority |
|---|-----------|----------|--------|------------|----------|
| 1 | Rule_IssueSW_FmeaCheck | IssueSW | IssueSW | FMEA_STATE, FMEA_COMMENT | ? HIGH |
| 2 | Rule_IssueSW_ASIL | IssueSW | IssueSW+Children | ASIL_CLASSIFICATION | ? HIGH |
| 3 | Rule_IssueSW_MissingAffectedIssueComment | IssueSW | IssueSW | AFFECTED_ISSUE_COMMENT | ? HIGH |
| 4 | Rule_IssueFD_Pilot | IssueFD | IssueFD | PILOT_DERIVATIVE | MEDIUM |
| 5 | Rule_IssueFD_WithoutLinkToBc | IssueFD | IssueFD | BC link | ? HIGH |
| 6 | Rule_Bc_CheckPstDates | BC | BC | PST dates | ? HIGH |
| 7 | Rule_Bc_Close | BC | BC | LIFE_CYCLE_STATE | MEDIUM |
| 8 | Rule_Bc_NamingConvention | BC | BC | NAME | ? HIGH |
| 9 | Rule_Bc_WithoutLinkToPst | BC | BC | PST link | ? HIGH |
| 10 | Rule_Fc_NamingConvention | FC | FC | NAME | ? HIGH |
| 11 | Rule_Fc_PlannedDate | FC | FC | PLANNED_DATE | MEDIUM |
| 12 | Rule_Fc_ReqDate | FC | FC | REQ_DATE | MEDIUM |
| 13 | Rule_Fc_WithoutLinkToBc | FC | FC | BC link | MEDIUM |
| 14 | Rule_CheckDatesForBcAndFc | Dates | BC+FC | Various dates | ? HIGH |
| 15 | Rule_CheckDatesForPvar | Dates | PVAR | Various dates | MEDIUM |
| 16 | Rule_CheckDatesForPver | Dates | PVER | Various dates | MEDIUM |
| 17 | Rule_CheckForMissing_BaselineLink | Baseline | Release | DESCRIPTION, TAGLIST | ? HIGH |
| 18 | Rule_Ecu_LumpensammlerExists | ECU | ECU | Lumpensammler | LOW |
| 19 | Rule_Ecu_PstLines | ECU | ECU | PST lines | LOW |
| 20 | Rule_Irm_OnlyOnePilotDerivative | IRM | IRM | Pilot count | MEDIUM |
| 21 | Rule_Irm_Prg_HintForExclude | IRM | IRM+PRG | Exclude hints | LOW |
| 22 | Rule_Irm_Prg_IssueSw_PilotSet | IRM | IRM+PRG+IssueSW | Pilot set | MEDIUM |
| 23 | Rule_Irm_PstRelease_IssueSw_Severity | IRM | IRM+PST+IssueSW | SEVERITY | MEDIUM |
| 24 | Rule_Prg_Lumpensammler | PRG | PRG | Lumpensammler | LOW |
| 25 | Rule_Pvar_Derivatives | Release | PVAR | Derivatives | MEDIUM |
| 26 | Rule_Pver_Derivatives | Release | PVER | Derivatives | MEDIUM |
| 27 | Rule_Pver_NotSuitableForINMA | Release | PVER | INMA fields | LOW |
| 28 | Rule_Release_Predecessor | Release | Release | Predecessor | MEDIUM |
| 29 | Rule_Rrm_Bc_Fc_DeliveryDate | RRM | RRM+BC+FC | Delivery dates | MEDIUM |
| 30 | Rule_Rrm_Pst_Bc_DeliveryDate | RRM | RRM+PST+BC | Delivery dates | MEDIUM |
| 31 | Rule_Rrm_Pst_Bc_HintForExclude | RRM | RRM+PST+BC | Hints | LOW |
| 32 | Rule_Rrm_Pver_Bc_PilotSet | RRM | RRM+PVER+BC | Pilot set | LOW |
| 33 | Rule_ToDo_InternalComment | Info | Various | INTERNAL_COMMENT | LOW |
| 34 | Rule_Info_InternalComment | Info | Various | INTERNAL_COMMENT | LOW |
| 35 | Rule_UnknownIssue_Exists | Existence | Issue | Existence check | LOW |
| 36 | Rule_UnknownProject_Exists | Existence | Project | Existence check | LOW |
| 37 | Rule_AccountNumberFormat | Account | Account | ACCOUNT_NUMBER | LOW |

---

## ?? Categories Summary

| Category | Count | High Priority | Description |
|----------|-------|---------------|-------------|
| **IssueSW** | 3 | 3 | Software Issue validation (FMEA, ASIL, comments) |
| **IssueFD** | 2 | 1 | Function Development validation |
| **BC** | 4 | 3 | Baseline Configuration (naming, dates, links) |
| **FC** | 4 | 1 | Function Changes (naming, dates) |
| **Dates** | 3 | 1 | Cross-record date validation |
| **Baseline** | 1 | 1 | DOORS baseline links |
| **IRM** | 4 | 0 | Issue-Release mapping |
| **RRM** | 4 | 0 | Release-Requirement mapping |
| **Release** | 4 | 0 | PVAR/PVER validation |
| **ECU** | 2 | 0 | ECU specific checks |
| **PRG** | 1 | 0 | Program checks |
| **Info** | 2 | 0 | Comment validation |
| **Existence** | 2 | 0 | Unknown items detection |
| **Account** | 1 | 0 | Account format validation |

---

## ?? Common Patterns

| Pattern | Count | Description | Example Rules |
|---------|-------|-------------|---------------|
| **Required Field** | 15 | Field must not be empty | FMEA comment, comments |
| **Cross-Record** | 10 | Validate relationships | ASIL matching, BC-FC links |
| **Date Logic** | 8 | Date consistency checks | BC.planned < FC.req |
| **Pattern Match** | 5 | Regex validation | BC naming, DOORS links |
| **State-Based** | 12 | Check only specific states | DEVELOPED, CLOSED only |

---

## ?? MCP Implementation

### Tool Definition

```python
@mcp.tool()
def validate_rq1_compliance(
    rq1_number: str,
    rules: list[str] = None  # Optional: specific rules
) -> dict:
    """
    Validate RQ1 Issue against compliance rules
    
    Returns:
    {
        "rq1_number": "RQ1234567",
        "compliant": false,
        "violations": [
            {
                "rule": "Rule_IssueSW_FmeaCheck",
                "severity": "WARNING",
                "message": "Comment for not required FMEA missing",
                "field": "FMEA_COMMENT",
                "suggestion": "Add comment when FMEA_STATE='Not Required'"
            }
        ],
        "rules_checked": 10,
        "timestamp": "2025-11-13T10:00:00Z"
    }
    """
```

### Output Format Examples

**? Compliant**:
```
? RQ1234567 - Compliant
No violations found. Checked 10 rules.
```

**? Non-Compliant**:
```
? RQ1234568 - 3 Violations

??  WARNING: FMEA comment missing
    Rule: Rule_IssueSW_FmeaCheck
    Field: FMEA_COMMENT
    ?? Add comment when FMEA_STATE = "Not Required"

??  WARNING: DOORS baseline link missing
    Rule: Rule_CheckForMissing_BaselineLink
    Fields: DESCRIPTION, INTERNAL_COMMENT, TAGLIST
    ?? Add link: doors://... or https://...dwa/rm/...

?? FAILURE: ASIL level mismatch
    Rule: Rule_IssueSW_ASIL
    Issue: I-SW has ASIL_B but no child I-FD with B/C/D level
    ?? Update child I-FD ASIL classification

Checked: 10 rules | Violations: 3 (2 warnings, 1 failure)
```

---

## ?? Most Used Fields

| Field | Usage | Purpose |
|-------|-------|---------|
| LIFE_CYCLE_STATE | 30+ rules | State filtering (NEW, CLOSED, etc.) |
| SUBMIT_DATE | 20+ rules | Date range filtering |
| INTERNAL_COMMENT | 15+ rules | Required comments, DOORS links |
| DESCRIPTION | 12+ rules | DOORS links, details |
| CHILDREN/PARENT | 25+ rules | Relationship validation |
| ASIL_CLASSIFICATION | 5 rules | Safety level validation |
| TAGLIST | 8 rules | DOORS links |
| Date fields | 20+ rules | Date logic validation |
| FMEA_* | 2 rules | FMEA validation |
| NAME/TITLE | 10+ rules | Naming conventions |

---

## ?? Implementation Phases

### Phase 2A: Top 10 Rules (~30-40 hours)
- Focus: Most critical compliance checks
- Patterns: Field validation, cross-record, date logic, links
- Testing: After PQA_Agent whitelist approval

### Phase 2B: Remaining 27 Rules (~80-100 hours)
- IRM, RRM, Release, ECU, PRG rules
- Lower priority, add based on user feedback

### Phase 2C: Optimization
- Rule engine framework
- Batch validation
- Performance tuning

---

## ?? Next Steps

1. ? **Review** - Confirm top 10 priority
2. ? **Design** - Rule engine architecture
3. ? **Implement** - Phase 2A top 10 rules
4. ? **Test** - With real RQ1 data
5. ? **Expand** - Add remaining rules

---

## ?? Additional Documentation

- **Detailed Rules**: `RQ1_RULES_DETAILED.md` - Complete specs for all 37 rules
- **Java Source**: `rq1/POST_extracted/DataModel/Rq1/Monitoring/` - Original rule implementations
- **Analysis**: `POST_RULES_ANALYSIS.md` - Pattern analysis and code examples

---

**Status**: Ready for Phase 2A implementation  
**Last Updated**: 2025-11-13  
**Version**: 1.0
