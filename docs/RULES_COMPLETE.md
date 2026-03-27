# RQ1 Rules - Complete Reference

**Total Rules**: 101 (BBM 23 + QAM 22 + IPT 56)
**Source**: SW_QAMRuleSet_Tmplt.xlsm v3.8 (2022-07-06)
**Generated**: Auto-parsed from Excel with priority organization

---

## Table of Contents

1. [Priority Summary](#priority-summary)
2. [BBM Rules (23)](#bbm-rules)
3. [QAM Rules (22)](#qam-rules)
4. [IPT Rules (56)](#ipt-rules)

---

## Priority Summary

### Overall Distribution

| Priority Level | Execution | Total Count | BBM | QAM | IPT |
|---------------|-----------|-------------|-----|-----|-----|
| CRITICAL | mandatory | 23 | 9 | 10 | 4 |
| HIGH | optional | 22 | 6 | 0 | 16 |
| MEDIUM | new | 3 | 0 | 3 | 0 |
| LOW | open | 7 | 0 | 7 | 0 |
| SKIP | outdated | 12 | 0 | 2 | 7 |
| SKIP | to be deleted | 3 | 3 | 0 | 0 |
| N/A | (not specified) | 34 | 4 | 0 | 29 |

### By Rule Set

**BBM (Quality Metrics)**: 23 rules
- 9 mandatory (CRITICAL)
- 6 optional (HIGH)
- 3 to be deleted
- 4 not specified
- 1 optional/mandatory

**QAM (Quality Assurance - Documentation)**: 22 rules
- 10 mandatory (CRITICAL)
- 3 new (MEDIUM)
- 7 open (LOW)
- 2 outdated

**IPT (Project Planning/Process/Assessment)**: 56 rules
- 4 mandatory (CRITICAL)
- 16 optional (HIGH)
- 7 outdated
- 29 not specified

---

<a name="bbm-rules"></a>
## BBM Rules (23)

**Focus**: Quality Assurance Metrics
**Total**: 23 rules

### Priority Distribution

| Execution | Count |
|-----------|-------|
| mandatory | 9 |
| optional | 6 |
| to be deleted | 3 |
| (not specified) | 4 |
| optional / mandatory | 1 |

---

### CRITICAL: Mandatory (9 rules)

#### BBM 01
**Name**: Ratio of technical customer requirements accepted
**Execution**: mandatory
**Check Retrospectively**: 3m b / 8m f
**Implemented**: 2019-01-01

#### BBM 08
**Name**: Ratio of defects closed
**Execution**: mandatory
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 09
**Name**: Ratio of change requests closed
**Execution**: mandatory
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 10
**Name**: Ratio of technical customer requirements traceable to internal requirements
**Execution**: mandatory
**Check Retrospectively**: all
**Implemented**: 2019-01-01

#### BBM 11
**Name**: Ratio of system requirements reviewed
**Execution**: mandatory
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 12
**Name**: Ratio of system requirements traceable to its source
**Execution**: mandatory
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 15
**Name**: Ratio of software requirements reviewed
**Execution**: mandatory
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 16
**Name**: Ratio of software requirements traceable to its source
**Execution**: mandatory
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 23
**Name**: Sum of unevaluated errors and warnings from static code analysis
**Execution**: mandatory
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

---

### HIGH: Optional (6 rules)

#### BBM 03
**Name**: Ratio of system requirements successfully verified
**Execution**: optional
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 06
**Name**: Ratio of software requirements successfully verified
**Execution**: optional
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 07
**Name**: Ratio of SSL requirements successfully verified (only safety in 2017!)
**Execution**: optional
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 13
**Name**: Ratio of system requirements linked to at least one test case
**Execution**: optional
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 17
**Name**: Ratio of software requirements linked to at least one test case
**Execution**: optional
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

#### BBM 20
**Name**: Ratio of software components/units successfully verified
**Execution**: optional
**Check Retrospectively**: 6m b/f
**Implemented**: 2019-01-01

---

### SKIP: To Be Deleted (3 rules)

#### BBM 19
**Name**: Ratio of software units implemented or changed according to current release plan
**Execution**: to be deleted

#### BBM 21
**Name**: Consumption of processor capacity
**Execution**: to be deleted

#### BBM 22
**Name**: Consumption of memory
**Execution**: to be deleted

---

### N/A: Not Specified (4 rules)

#### BBM 02
**Name**: Ratio of system interfaces successfully verified

#### BBM 05
**Name**: Ratio of software interfaces successfully verified

#### BBM 14
**Name**: Ratio of defined system interfaces linked to at least one test case

#### BBM 18
**Name**: Ratio of software interfaces linked to at least one test case

---

### N/A: Optional / Mandatory (1 rule)

#### BBM 04
**Name**: Ratio of software requirements implemented
**Execution**: optional / mandatory
**Check Retrospectively**: all
**Implemented**: 2019-01-01

---

<a name="qam-rules"></a>
## QAM Rules (22)

**Focus**: Quality Assurance - Documentation and Workflow Process
**Total**: 22 rules

### Priority Distribution

| Execution | Count |
|-----------|-------|
| mandatory | 10 |
| new | 3 |
| open | 7 |
| outdated | 2 |

---

### CRITICAL: Mandatory (10 rules)

#### QADO 02.01.00
**Name**: FC in delivered state in SCM, requirement based development needed, check for URT documentation
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2018-02-01
**Relevant for PQA**: yes
**Remarks**: Included in QAWP 06.00.00

#### QADO 04.00.00
**Name**: BMI evaluation and documentation
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2021-07-01
**Relevant for PQA**: no

#### QAWP 01.00.00
**Name**: Concept Review
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2016-01-01
**Relevant for PQA**: no
**Remarks**: 3.7.0

#### QAWP 02.00.00
**Name**: Specification / Function Review
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2016-01-01
**Relevant for PQA**: yes
**Remarks**: 3.2.0

#### QAWP 03.00.00
**Name**: Software / Code Review
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2016-01-01
**Relevant for PQA**: yes
**Remarks**: 3.3.0

#### QAWP 04.00.00
**Name**: Verification and Validation Condition in SCM
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2016-01-01
**Relevant for PQA**: yes
**Remarks**: 3.5.0

#### QAWP 06.00.00
**Name**: Content of Test Container for Functions (FC or related classes) in SCM
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2015-10-01
**Relevant for PQA**: yes
**Remarks**: 3.6.0

#### QAWP 06.01.00
**Name**: Warning Assessment of Test Container for Functions (FC or related classes) in SCM
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2017-10-01
**Relevant for PQA**: yes
**Remarks**: 3.6.0 - Included in QAWP 06.00.00

#### QAWP 06.02.00
**Name**: Review of Test Specification Artefacts for Functions (FC or related classes) in SCM
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2019-08-01
**Relevant for PQA**: yes
**Remarks**: Included in QAWP 06.00.00

#### QAWP 08.01.00
**Name**: Warning Assessment of Test Container for Packages (BC or related classes) in SCM
**Execution**: mandatory
**Check Retrospectively**: last 12 month
**Implemented**: 2016-10-01
**Relevant for PQA**: yes
**Remarks**: 3.11.0 - Included in QAWP 08.00.00

---

### MEDIUM: New (3 rules)

#### QADO 01.00.00
**Name**: FC in delivered state in SCM, but Issue-FD is not closed
**Execution**: new
**Relevant for PQA**: yes

#### QADO 02.00.00
**Name**: FC in delivered state in SCM, check for requirement based development
**Execution**: new
**Relevant for PQA**: yes

#### QADO 03.00.00
**Name**: Surrounding of ECU generation - MISRA
**Execution**: new
**Relevant for PQA**: yes

---

### LOW: Open (7 rules)

#### QAWP 05.00.00
**Name**: LifeCycle in SCM
**Execution**: open
**Check Retrospectively**: last 12 month
**Relevant for PQA**: yes
**Remarks**: 3.4.0

#### QAWP 07.00.01
**Name**: Content of delivered Packages (BC or related classes) in SCM
**Execution**: open
**Check Retrospectively**: last 12 month
**Relevant for PQA**: yes

#### QAWP 07.01.00
**Name**: Tag-List for developed Packages (BC or related classes) in SCM
**Execution**: open
**Check Retrospectively**: last 12 month
**Relevant for PQA**: yes

#### QAWP 08.00.00
**Name**: Content of Test Container for Packages (BC or related classes) in SCM
**Execution**: open
**Check Retrospectively**: last 12 month
**Relevant for PQA**: yes
**Remarks**: 3.11.0

#### QAWP 09.00.00
**Name**: Content of delivered Program Version (PVER or related classes) in SCM
**Execution**: open
**Relevant for PQA**: yes

#### QAWP 10.00.00
**Name**: Content of Test Container for Program Version (PVER or related classes) in SCM
**Execution**: open
**Relevant for PQA**: yes

#### QAWP 10.01.00
**Name**: Warning Assessment of Test Container for Program Version (PVER or related classes) in SCM
**Execution**: open
**Relevant for PQA**: yes

---

### SKIP: Outdated (2 rules)

#### QAWP 02.01.00
**Name**: Specification / Function Review "not necessary"
**Execution**: outdated
**Relevant for PQA**: no
**Remarks**: 3.8.0

#### QAWP 03.01.00
**Name**: SW Review "exempt"
**Execution**: outdated
**Relevant for PQA**: no
**Remarks**: 3.9.0

---

<a name="ipt-rules"></a>
## IPT Rules (56)

**Focus**: Project Planning (PRPL), Process/Traceability (PRPS), Assessment (PRAS)
**Total**: 56 rules

### Priority Distribution

| Execution | Count |
|-----------|-------|
| mandatory | 4 |
| optional | 16 |
| outdated | 7 |
| (not specified) | 29 |

---

### CRITICAL: Mandatory (4 rules)

#### PRPL 13.00.00
**Name**: IFD is not implemented or closed, after planned dated of BC-R
**Execution**: mandatory
**Implemented**: 2016-07-01

#### PRPL 14.00.00
**Name**: IFD is not committed, eventhough attached Issue-SW is committed
**Execution**: mandatory
**Implemented**: 2016-07-01

#### PRPL 15.00.00
**Name**: Release is not closed after planned date for BC and FC
**Execution**: mandatory
**Implemented**: 2016-07-01

#### PRPL 16.00.00
**Name**: Workitem is not closed after planned date
**Execution**: mandatory
**Implemented**: 2016-07-01

---

### HIGH: Optional (16 rules)

#### PRPL 01.00.00
**Name**: BC-R is not in requested state, 8 weeks before PVER planned delivery date
**Execution**: optional
**Implemented**: 2016-07-01

#### PRPL 03.00.00
**Name**: Issue/Release/workitem is still in "Conflicted" state
**Execution**: optional
**Implemented**: 2016-07-01

#### PRPL 06.00.00
**Name**: Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled
**Execution**: optional
**Implemented**: 2019-09-01

#### PRPL 07.00.00
**Name**: Planned date of BC later than requested delivery date of any mapped PVER or PVAR
**Execution**: optional
**Implemented**: 2016-06-24

#### PRPL 08.00.00
**Name**: BC without mapping to PVER or PFAM
**Execution**: optional
**Implemented**: 2019-09-01

#### PRPL 09.00.00
**Name**: FC in time for BC
**Execution**: optional
**Implemented**: 2019-09-01

#### PRPL 10.00.00
**Name**: Check dates for PVER, BC and FC
**Execution**: optional
**Implemented**: 2019-09-01

#### PRPL 11.00.00
**Name**: IFD 5 day SLA reached
**Execution**: optional
**Implemented**: 2016-03-01

#### PRPL 12.00.00
**Name**: IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled
**Execution**: optional
**Implemented**: 2016-03-01

#### PRPL 19.00.00
**Name**: Name Check Function (FC)
**Execution**: optional
**Implemented**: open

#### PRPL 20.00.00
**Name**: Name Check Package (BC)
**Execution**: optional
**Implemented**: open

#### PRPL 21.00.00
**Name**: ISW is not closed or implemented, even though function development is finished and at least one or all IRM(s) are qualified
**Execution**: optional
**Implemented**: 2019-09-01

#### PRPL 22.00.00
**Name**: PVER-F testing is open even though IRM is implemented
**Execution**: optional
**Implemented**: 2019-09-01

#### PRPS 01.00.00
**Name**: Tracking of Change Requests
**Execution**: optional
**Implemented**: open

#### PRPS 01.01.00
**Name**: Decomposition missing of Issue to affected SW Components
**Execution**: optional
**Implemented**: 2019-09-01

#### PRPS 01.02.00
**Name**: Decomposition of ASIL not correct
**Execution**: optional
**Implemented**: open

---

### SKIP: Outdated (7 rules)

#### PRPL 02.00.00
**Name**: Workitem is in started state, but planned date for workitem is not entered in planning tab
**Execution**: outdated
**Implemented**: 2019-09-01

#### PRPL 17.00.00
**Name**: Status Canceled (planned)
**Execution**: outdated
**Implemented**: open

#### PRPS 02.02.00
**Name**: Traceability Check between Change Request to Customer Requirement
**Execution**: outdated
**Implemented**: 2019-09-01

#### PRPS 02.03.00
**Name**: Allocation Check for Customer Requirement
**Execution**: outdated
**Implemented**: 2019-09-01

#### PRPS 03.06.00
**Name**: Traceability Check between ECU System Requirement to Customer Requirement
**Execution**: outdated

#### PRPS 03.07.00
**Name**: Traceability Check between Change Request to System Functionality Requirement
**Execution**: outdated
**Implemented**: 2019-09-01

#### PRPS 03.09.00
**Name**: Traceability Check between System Functionality Requirement to ECU System Requirement
**Execution**: outdated
**Implemented**: 2019-09-01

---

### N/A: Not Specified (29 rules)

#### PRAS 01.00.00
**Name**: MAN.3 - Task Planning and Tracking
**Implemented**: open

#### PRAS 01.01.00
**Name**: MAN.3 - Resource Loading and Skill matrix
**Implemented**: open

#### PRAS 01.02.00
**Name**: Project Management Plan (- Project Guide) Contracting (Project, FMEA)
**Implemented**: open

#### PRAS 01.03.00
**Name**: Project Review and Reporting (Including CPC-SW, Traffic light list SW)
**Implemented**: open

#### PRAS 01.04.00
**Name**: ISO26262 artefacts check
**Implemented**: open

#### PRAS 02.00.00
**Name**: Planning and Tracking of Quality Assurance activities and reviews
**Implemented**: open

#### PRAS 02.01.00
**Name**: Quality Assurance Metrics Tracking
**Implemented**: open

#### PRAS 03.00.00
**Name**: Problem Resolution Management
**Implemented**: open

#### PRAS 03.01.00
**Name**: Risk Handling
**Implemented**: open

#### PRPL 04.00.00
**Name**: Effort estimation (planned)
**Implemented**: open

#### PRPL 05.00.00
**Name**: Planning of Quality Measures (planned)
**Implemented**: open

#### PRPL 18.00.00
**Name**: I-FD not committed 5 or more working days after attached I-SW was committed
**Implemented**: 2019-09-01

#### PRPS 02.01.00
**Name**: Missing mandatory Attributes of Customer Requirements
**Implemented**: 2019-09-01

#### PRPS 02.04.00
**Name**: Baseline Check of Customer Requirements
**Implemented**: 2019-09-01

#### PRPS 03.00.00
**Name**: Missing Impact Analysis for ECU System Requirements
**Implemented**: 2019-09-01

#### PRPS 03.01.00
**Name**: Missing mandatory Attributes of ECU System Requirements
**Implemented**: 2019-09-01

#### PRPS 03.02.00
**Name**: Missing mandatory Attributes of System Functionality Requirements
**Implemented**: 2019-09-01

#### PRPS 03.03.00
**Name**: Missing mandatory Attributes of System Functionality and System Interface Requirements
**Implemented**: 2019-09-01

#### PRPS 03.04.00
**Name**: Traceability Check between Change Request to ECU System Requirement
**Implemented**: 2019-09-01

#### PRPS 03.05.00
**Name**: Allocation Check of ECU System Requirements
**Implemented**: 2019-09-01

#### PRPS 03.08.00
**Name**: Traceability Check between System Functionality Requirement to Change Request
**Implemented**: 2019-09-01

#### PRPS 03.10.00
**Name**: Baseline Check of ECU System Requirements
**Implemented**: 2019-09-01

#### PRPS 03.11.00
**Name**: Baseline Check of System Functionality Requirements
**Implemented**: 2019-09-01

#### PRPS 04.00.00
**Name**: Missing mandatory Attributes of Software Requirements
**Implemented**: 2019-09-01

#### PRPS 04.01.00
**Name**: Missing mandatory Attributes of Software and Software Interface Requirements
**Implemented**: 2019-09-01

#### PRPS 04.02.00
**Name**: Traceability Check between Change Request to Software Requirement
**Implemented**: 2019-09-01

#### PRPS 04.03.00
**Name**: Traceability Check between Software Requirement to Change Request
**Implemented**: 2019-09-01

#### PRPS 04.04.00
**Name**: Traceability Check between Software Requirement to System Functionality Requirement
**Implemented**: 2019-09-01

#### PRPS 04.05.00
**Name**: Baseline Check of Software Requirements
**Implemented**: 2019-09-01

---

## Implementation Notes

### Rule Priority for Implementation

**Phase 1 - CRITICAL (23 mandatory rules)**:
- BBM: 9 rules - Quality metrics
- QAM: 10 rules - Documentation/workflow
- IPT: 4 rules - Planning compliance

**Phase 2 - HIGH (22 optional rules)**:
- BBM: 6 rules - Additional quality checks
- IPT: 16 rules - Enhanced planning/process

**Phase 3 - MEDIUM/LOW (10 new/open rules)**:
- QAM: 3 new + 7 open rules

**Skip - Outdated/Deprecated (15 rules)**:
- BBM: 3 to be deleted
- QAM: 2 outdated
- IPT: 7 outdated
- Not specified: 3

### Key Insights

1. **Mandatory rules focus on**:
   - Requirements traceability (BBM 10, 12, 16)
   - Review completion (BBM 11, 15)
   - Defect/change closure (BBM 08, 09)
   - Documentation completeness (QAM 10 rules)
   - Date compliance (IPT 4 rules)

2. **Optional rules add**:
   - Verification coverage (BBM 03, 06, 07, 13, 17, 20)
   - Planning validation (IPT 16 rules)
   - Process traceability (PRPS rules)

3. **Excel vs Java mapping**:
   - 37 Java rules from POST Tool
   - ~30-36 rules have both Excel + Java backing
   - Focus implementation on intersection

### Usage

This document serves as the single source of truth for all 101 Excel-defined rules. For implementation details and Java code references, see `IMPLEMENTATION_GUIDE.md` and `EXCEL_TO_JAVA_MAPPING.md`.
