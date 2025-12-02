# Implementation Priority Matrix

Based on deep rule mapping analysis: **14 matched (43.8%)** | **18 unmatched (56.2%)**

## Executive Summary

| Phase | Rules | Complexity | Effort (hours) | Status |
|-------|-------|-----------|----------------|--------|
| **Phase 1A** | 5 PRPL (Top Priority) | LOW (Java backed) | 15-30 | READY |
| **Phase 1B** | 6 PRPL (IFD rules) | MEDIUM (Java backed) | 18-36 | READY |
| **Phase 1C** | 3 Other (QAWP, PS-SC) | MEDIUM (Java backed) | 9-18 | READY |
| **Phase 2A** | 12 BBM Metrics | HIGH (No Java, need metrics logic) | 96-144 | BLOCKED (need Excel formulas) |
| **Phase 2B** | 6 Other unmatched | HIGH (No Java) | 48-72 | BLOCKED (need specs) |
| **TOTAL** | 32 rules | - | **186-300 hours** | 23-37 days |

---

## Phase 1A: Top Priority PRPL Rules (QUICK WINS)

**Effort**: 15-30 hours | **Status**: READY TO IMPLEMENT

These 5 rules are:
1. Most critical for planning workflow
2. Have Java implementations (Rule_CheckDatesForBcAndFc, Rule_Bc_CheckPstDates)
3. Already tested in Python (Rule_Bc_Close exists)

### PRPL 15.00.00 (Priority 1) ?????
**Description**: Release is not closed after planned date for BC and FC  
**Java Class**: `Rule_CheckDatesForBcAndFc`  
**Python Status**: ALREADY IMPLEMENTED in `src/rules/rule_bc_close.py`  
**Effort**: 2 hours (validation + testing)  
**Activation**: 1-Jan-16 (oldest rule, likely most important)

**Implementation**:
```python
# Already exists! Just needs testing
from src.rules.rule_bc_close import validate_bc_closure
```

### PRPL 07.00.00 (Priority 2) ????
**Description**: Planned date of BC later than requested delivery date of any mapped PVER or PVAR  
**Java Class**: `Rule_Bc_CheckPstDates`  
**Python Status**: NEW (but similar to PRPL 15)  
**Effort**: 4-6 hours  

**Implementation Plan**:
1. Fetch BC Release with PVER/PVAR links
2. Compare BC planned_date vs PVER/PVAR requested_delivery_date
3. Flag if BC.planned > PVER.requested_delivery

### PRPL 01.00.00 (Priority 3) ????
**Description**: BC-R is not in requested state, 8 weeks before PVER planned delivery date  
**Java Class**: `Rule_Bc_CheckPstDates`  
**Python Status**: NEW  
**Effort**: 5-8 hours  

**Implementation Plan**:
1. Fetch BC Release with PVER links
2. Calculate: PVER.planned_delivery_date - 8 weeks = deadline
3. Check if BC.state == 'Requested' before deadline

### PRPL 02.00.00 (Priority 4) ???
**Description**: Workitem is in started state, but planned date for workitem is not entered in planning tab  
**Java Class**: `Rule_Bc_CheckPstDates`  
**Python Status**: NEW  
**Effort**: 3-5 hours  

**Implementation Plan**:
1. Query workitems with state='Started'
2. Check if planned_date is None or empty
3. Flag missing planned dates

### PRPL 16.00.00 (Priority 5) ???
**Description**: Workitem is not closed after planned date  
**Java Class**: `Rule_Bc_CheckPstDates`  
**Python Status**: NEW  
**Effort**: 3-5 hours  

**Implementation Plan**:
1. Query workitems with state != 'Closed'
2. Check if current_date > planned_date
3. Flag overdue workitems

---

## Phase 1B: IFD (Issue-FD) Rules (MEDIUM PRIORITY)

**Effort**: 18-36 hours | **Status**: READY TO IMPLEMENT

These 6 rules handle IFD (Issue Feature Development) workflow.

### PRPL 12.00.00 ????
**Description**: IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled  
**Java Class**: `Rule_IssueFD_WithoutLinkToBc`  
**Effort**: 4-6 hours  

**Implementation**:
1. Fetch IFD issues with BC links
2. Check all linked BCs: are they Closed or Cancelled?
3. If yes, IFD should also be Closed

### PRPL 13.00.00 ????
**Description**: IFD is not implemented or closed, after planned dated of BC-R  
**Java Class**: `Rule_IssueFD_WithoutLinkToBc`  
**Effort**: 4-6 hours  

### PRPL 11.00.00 ???
**Description**: IFD 5 day SLA reached  
**Java Class**: `Rule_IssueFD_Pilot`  
**Effort**: 3-5 hours  

**Implementation**:
1. Fetch IFD with creation_date
2. Calculate: current_date - creation_date > 5 days
3. Flag SLA violations

### PRPL 14.00.00 ???
**Description**: IFD is not committed, eventhough attached Issue-SW is committed  
**Java Class**: `Rule_IssueFD_Pilot`  
**Effort**: 3-5 hours  

### PRPL 18.00.00 ???
**Description**: I-FD not committed 5 or more working days after attached I-SW was committed  
**Java Class**: `Rule_IssueFD_Pilot`  
**Effort**: 4-6 hours  

### PRPL 06.00.00 ??
**Description**: Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled  
**Java Class**: `Rule_IssueFD_Pilot`  
**Effort**: 3-5 hours  

---

## Phase 1C: Other Matched Rules

**Effort**: 9-18 hours | **Status**: READY TO IMPLEMENT

### QAWP 06.00.00 ???
**Description**: Content of Test Container for FC (and related classes) in SCM (SDOM)  
**Java Class**: `Rule_CheckDatesForBcAndFc`  
**Effort**: 4-6 hours  

### QAWP 08.00.00 ??
**Description**: Content of Test Container for Packages (BC or related classes) in SCM  
**Java Class**: `Rule_Bc_CheckPstDates`  
**Effort**: 3-5 hours  

### PS-SC_4 ??
**Description**: Taglists in BC-R missing/multiple  
**Java Class**: `Rule_Bc_CheckPstDates`  
**Effort**: 2-4 hours  

---

## Phase 2A: BBM Metrics (HIGH COMPLEXITY - NO JAVA)

**Effort**: 96-144 hours | **Status**: BLOCKED (Need Excel formulas)

All 12 BBM metrics rules have **NO Java implementation**. Must extract formulas from:
- Excel file: `rq1/POST_V_1.0.3/SW_QAMRuleSet_Tmplt.xlsm`
- VBA code: `rq1/vba_code.txt`

### BBM_1 ????
**Description**: Ratio of software requirements implemented  
**Formula**: (Implemented SW Reqs / Total SW Reqs) * 100  
**Effort**: 8-12 hours  

### BBM_15 ????
**Description**: Ratio of software requirements reviewed  
**Effort**: 8-12 hours  

### BBM_16 ????
**Description**: Ratio of software requirements traceable to its source  
**Effort**: 8-12 hours  

### BBM_3, BBM_6, BBM_8, BBM_9, BBM_11, BBM_12, BBM_20, BBM_23
**Effort**: 8-12 hours each

---

## Phase 2B: Other Unmatched Rules

**Effort**: 48-72 hours | **Status**: BLOCKED (Need specs)

### PRPL 03.00.00
**Description**: Issue/Release/workitem is still in "Conflicted" state  
**Effort**: 6-8 hours  

### QADO 04.00.00
**Description**: SW Metrics Relevance is set and BMI is given as lower than -50 but assessment is not documented  
**Effort**: 8-12 hours  

### QAWP 01.00.00, 02.00.00, 03.00.00, 04.00.00, 05.00.00
**Description**: Various review and SCM checks  
**Effort**: 6-8 hours each

---

## Recommended Implementation Order

### Week 1-2: Quick Wins (Phase 1A)
1. **PRPL 15.00.00** (2h) - Already done, test it!
2. **PRPL 07.00.00** (6h) - BC vs PVER dates
3. **PRPL 01.00.00** (8h) - BC requested state check
4. **PRPL 02.00.00** (5h) - Workitem planned date
5. **PRPL 16.00.00** (5h) - Workitem closure

**Total: 26 hours ? 5 rules done**

### Week 3-4: IFD Rules (Phase 1B)
6-11. All 6 IFD rules (18-36h)

**Total: +30 hours ? 11 rules done**

### Week 5-6: Other Matched (Phase 1C)
12-14. QAWP + PS-SC rules (9-18h)

**Total: +15 hours ? 14 rules done**

### Week 7+: Unmatched Rules (Phase 2)
15-32. BBM metrics + remaining rules (144-216h)

---

## Next Steps

1. **TODAY**: Implement PRPL 15.00.00 (already exists, just test)
2. **This Week**: Complete Phase 1A (5 rules, 26 hours)
3. **Next 2 Weeks**: Phase 1B + 1C (9 rules, 45 hours)
4. **Month 2**: Phase 2 (18 rules, 144-216 hours) after Excel formula extraction

**Total Phase 1 (14 rules)**: 42-84 hours (1-2 weeks full-time)  
**Total Phase 2 (18 rules)**: 144-216 hours (3-5 weeks full-time)
