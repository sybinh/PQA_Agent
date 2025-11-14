# EXCEL RULES - PRIORITIZED BY EXECUTION

**Source**: SW_QAMRuleSet_Tmplt.xlsm v3.8 (2022-07-06)
**Total Rules**: 101
**Purpose**: Prioritize implementation based on Excel "Execution" column

---

## ?? EXECUTION PRIORITY BREAKDOWN

### Priority Levels:

| Execution Value | Priority | Count | Description |
|----------------|----------|-------|-------------|
| **mandatory** | ??? CRITICAL | 23 | Must implement |
| **optional** | ?? HIGH | 23 | Should implement |
| **new** | ? MEDIUM | 3 | Newly defined |
| **open** | ?? LOW | 16 | Not yet implemented in QAM |
| **outdated** | ? SKIP | 10 | Deprecated rules |
| **to be deleted** | ? SKIP | 3 | Will be removed |
| (empty/header) | - | 23 | BBM metrics (header rows) |

---

## ??? CRITICAL PRIORITY (mandatory = 23 rules)

### BBM Rules (9 mandatory):
1. **BBM 01** - Ratio of technical customer requirements accepted
2. **BBM 08** - Ratio of defects closed
3. **BBM 09** - Ratio of change requests closed
4. **BBM 10** - Ratio of technical customer requirements traceable
5. **BBM 11** - Ratio of system requirements reviewed
6. **BBM 12** - Ratio of system requirements traceable to source
7. **BBM 15** - Ratio of software requirements reviewed
8. **BBM 16** - Ratio of software requirements traceable to source
9. **BBM 04** - Ratio of software requirements implemented (optional/mandatory)

### QAM Rules (10 mandatory):
10. **QADO 02.01.00** - URT documentation check (last 12 month)
11. **QADO 04.00.00** - BMI evaluation (last 12 month)
12. **QAWP 01.00.00** - Concept Review (last 12 month)
13. **QAWP 02.00.00** - Specification/Function Review (last 12 month)
14. **QAWP 03.00.00** - Software/Code Review (last 12 month)
15. **QAWP 04.00.00** - V&V Condition in SCM (last 12 month)
16. **QAWP 06.00.00** - Test Container for FC (last 12 month)
17. **QAWP 06.01.00** - Warning Assessment FC Test (last 12 month)
18. **QAWP 06.02.00** - Review Test Specification FC (last 12 month)
19. **QAWP 08.01.00** - Warning Assessment BC Test (last 12 month)

### IPT Rules (4 mandatory):
20. **PRPL 13.00.00** - IFD not implemented after BC-R planned date
21. **PRPL 14.00.00** - IFD not committed when Issue-SW committed
22. **PRPL 15.00.00** - Release not closed after planned date
23. **PRPL 16.00.00** - Workitem not closed after planned date

**Implementation Priority**: ??? CRITICAL - These are REQUIRED by process

---

## ?? HIGH PRIORITY (optional = 23 rules)

### BBM Rules (7 optional):
1. **BBM 03** - Ratio of system requirements successfully verified
2. **BBM 06** - Ratio of software requirements successfully verified
3. **BBM 07** - Ratio of SSL requirements verified (safety!)
4. **BBM 13** - Ratio of system requirements linked to test case
5. **BBM 17** - Ratio of software requirements linked to test case

### IPT - PRPL Rules (16 optional):
6. **PRPL 01.00.00** - BC-R not in requested state (8 weeks before PVER)
7. **PRPL 03.00.00** - Issue/Release/Workitem in "Conflicted" state
8. **PRPL 06.00.00** - Defect detection/injection attributes in IFD
9. **PRPL 07.00.00** - BC planned date vs PVER/PVAR delivery date
10. **PRPL 08.00.00** - BC without mapping to PVER/PFAM
11. **PRPL 09.00.00** - FC in time for BC
12. **PRPL 10.00.00** - Check dates for PVER, BC, FC
13. **PRPL 11.00.00** - IFD 5 day SLA reached
14. **PRPL 12.00.00** - IFD not closed even though BC-Rs closed
15. **PRPL 19.00.00** - Name Check Function (FC)
16. **PRPL 20.00.00** - Name Check Package (BC)
17. **PRPL 21.00.00** - ISW not closed when function development finished
18. **PRPL 22.00.00** - PVER-F testing open even though IRM implemented

### IPT - PRPS Rules (3 optional):
19. **PRPS 01.00.00** - Tracking of Change Requests
20. **PRPS 01.01.00** - Decomposition missing of Issue to SW Components
21. **PRPS 01.02.00** - Decomposition of ASIL not correct

**Implementation Priority**: ?? HIGH - Recommended for quality

---

## ? MEDIUM PRIORITY (new = 3 rules)

### QAM Rules (3 new):
1. **QADO 01.00.00** - FC in delivered state, but Issue-FD not closed
2. **QADO 02.00.00** - Check for requirement based development
3. **QADO 03.00.00** - MISRA compliance

**Implementation Priority**: ? MEDIUM - Newly defined rules

---

## ?? LOW PRIORITY (open = 16 rules)

### QAM - QAWP Rules (7 open):
1. **QAWP 05.00.00** - Lifecycle in SCM
2. **QAWP 07.00.01** - Content of delivered BC packages
3. **QAWP 07.01.00** - Tag-List for BC packages
4. **QAWP 08.00.00** - Test Container for BC
5. **QAWP 09.00.00** - Content of delivered PVER
6. **QAWP 10.00.00** - Test Container for PVER
7. **QAWP 10.01.00** - Warning Assessment PVER Test

### IPT - PRPL Rules (2 open):
8. **PRPL 04.00.00** - Effort estimation (planned)
9. **PRPL 05.00.00** - Planning of Quality Measures (planned)

### IPT - PRAS Rules (9 open - ALL):
10. **PRAS 01.00.00** - MAN.3 - Task Planning and Tracking
11. **PRAS 01.01.00** - MAN.3 - Resource Loading and Skill matrix
12. **PRAS 01.02.00** - Project Management Plan
13. **PRAS 01.03.00** - Project Review and Reporting
14. **PRAS 01.04.00** - ISO26262 artefacts check
15. **PRAS 02.00.00** - Planning QA activities and reviews
16. **PRAS 02.01.00** - Quality Assurance Metrics Tracking
17. **PRAS 03.00.00** - Problem Resolution Management
18. **PRAS 03.01.00** - Risk Handling

**Implementation Priority**: ?? LOW - Future enhancements

**Note**: All 9 PRAS rules are **project management/QA processes**, NOT RQ1 data validation ? Not applicable for PQA_Agent

---

## ? SKIP (outdated + to be deleted = 13 rules)

### BBM Rules (3 to be deleted):
1. **BBM 19** - Software units implemented (to be deleted)
2. **BBM 21** - Average number of test sessions (to be deleted)
3. **BBM 22** - Test case status distribution (to be deleted)

### QAM Rules (2 outdated):
4. **QAWP 02.01.00** - Function Review "not necessary" (outdated)
5. **QAWP 03.01.00** - SW Review "exempt" (outdated)

### IPT - PRPL Rules (2 outdated):
6. **PRPL 02.00.00** - Workitem planned date not entered (outdated)
7. **PRPL 17.00.00** - Status Canceled (outdated)
8. **PRPL 18.00.00** - IFD not committed 5 days after Issue-SW (outdated, 2019-09-01)

### IPT - PRPS Rules (6 outdated):
9. **PRPS 02.02.00** - Traceability CR to Customer Requirement (outdated)
10. **PRPS 02.03.00** - Allocation Check Customer Requirement (outdated)
11. **PRPS 03.06.00** - Traceability ECU to Customer (outdated)
12. **PRPS 03.07.00** - Traceability CR to System Functionality (outdated)
13. **PRPS 03.09.00** - Traceability System Functionality to ECU (outdated)

**Implementation Priority**: ? SKIP - Deprecated rules

---

## ?? PRIORITY SUMMARY FOR IMPLEMENTATION

### Recommended Implementation Order:

| Phase | Priority | Count | Rules | Effort |
|-------|----------|-------|-------|--------|
| **Phase 2A** | ??? CRITICAL | 10 | Top 10 mandatory with Java backing | 30-40h |
| **Phase 2B** | ??? CRITICAL | 13 | Remaining mandatory rules | 40-50h |
| **Phase 2C** | ?? HIGH | 23 | Optional rules with Java backing | 60-80h |
| **Phase 3** | ? MEDIUM | 3 | New rules (QADO) | 10-15h |
| **Future** | ?? LOW | 16 | Open/planning features | TBD |
| **Skip** | ? | 13 | Outdated/deprecated | 0h |
| **N/A** | - | 23 | BBM metrics (reporting only) | 0h |

### Key Insights:

1. **23 CRITICAL (mandatory)** rules must be implemented
   - 9 BBM metrics (traceability, review, closure)
   - 10 QAM work products (reviews, tests, documentation)
   - 4 IPT planning (IFD/release/workitem closure)

2. **23 HIGH (optional)** rules should be implemented
   - 7 BBM quality metrics
   - 16 PRPL planning rules (dates, naming, IFD)
   - 3 PRPS process rules (ASIL, decomposition)

3. **3 MEDIUM (new)** rules are newly defined
   - All in QADO (FC/BC delivery, RBD, MISRA)

4. **16 LOW (open)** rules not yet in QAM implementation
   - 7 QAWP work products
   - **9 PRAS project management (NOT APPLICABLE)**

5. **13 SKIP (outdated)** rules should not be implemented
   - Marked as "outdated" or "to be deleted" in Excel

---

## ?? RECOMMENDED APPROACH

### Excel-Java Intersection Strategy:

**Criteria for PQA_Agent Implementation**:
1. ? Has Java implementation in POST Tool
2. ? Has Excel backing (official process)
3. ? **Execution = mandatory or optional** (not outdated/open)
4. ? Relevant for PQA = yes (if specified)

**Result**:
- **~30-36 rules** meet all criteria
- Focus on **mandatory (23)** first
- Add **optional (23)** based on Java availability
- Skip **outdated (13)** and **open/PRAS (16)**

**Phase 2A Focus** (Top 10):
- All have **mandatory** or **optional** execution
- All have Java implementation
- All have Excel process backing
- Strongest justification for implementation

---

**Status**: Priority analysis complete
**Excel Version**: v3.8 (2022-07-06)
**Total Analyzed**: 101 rules
**Implementation Ready**: 46 rules (23 mandatory + 23 optional)
