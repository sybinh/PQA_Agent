# IPT Rules - Clean Rules Documentation

**Total Rules**: 56
**Source**: SW_QAMRuleSet_Tmplt.xlsm
**Parsed**: Auto-generated from Excel
**Sorted By**: Priority (execution), then Rule ID

## Priority Summary

| Execution | Priority | Count |
|-----------|----------|-------|
| mandatory | ??? CRITICAL | 4 |
| optional | ?? HIGH | 16 |
| outdated | ? SKIP | 7 |
| (not specified) | - N/A | 29 |

---

## ??? CRITICAL: MANDATORY (4 rules)

### PRPL 13.00.00

**Name**: IFD is not implemented or closed, after planned dated of BC-R

**Execution**: mandatory
**Implemented**: 2016-07-01 00:00:00

---

### PRPL 14.00.00

**Name**: IFD is not committed, eventhough attached Issue-SW is committed

**Execution**: mandatory
**Implemented**: 2016-07-01 00:00:00

---

### PRPL 15.00.00

**Name**: Release is not closed after planned date for BC and FC

**Execution**: mandatory
**Implemented**: 2016-07-01 00:00:00

---

### PRPL 16.00.00

**Name**: Workitem is not closed after planned date

**Execution**: mandatory
**Implemented**: 2016-07-01 00:00:00

---

## ?? HIGH: OPTIONAL (16 rules)

### PRPL 01.00.00

**Name**: BC-R is not in requested state, 8 weeks before PVER planned delivery date 

**Execution**: optional
**Implemented**: 2016-07-01 00:00:00

---

### PRPL 03.00.00

**Name**: Issue/Release/workitem is still in "Conflicted" state

**Execution**: optional
**Implemented**: 2016-07-01 00:00:00

---

### PRPL 06.00.00

**Name**: Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled

**Execution**: optional
**Implemented**: 2019-09-01 00:00:00

---

### PRPL 07.00.00

**Name**: Planned date of BC later than requested delivery date of any mapped PVER or PVAR

**Execution**: optional
**Implemented**: 2016-06-24 00:00:00

---

### PRPL 08.00.00

**Name**: BC without mapping to PVER or PFAM

**Execution**: optional
**Implemented**: 2019-09-01 00:00:00

---

### PRPL 09.00.00

**Name**: FC in time for BC

**Execution**: optional
**Implemented**: 2019-09-01 00:00:00

---

### PRPL 10.00.00

**Name**: Check dates for PVER, BC and FC

**Execution**: optional
**Implemented**: 2019-09-01 00:00:00

---

### PRPL 11.00.00

**Name**: IFD 5 day SLA reached

**Execution**: optional
**Implemented**: 2016-03-01 00:00:00

---

### PRPL 12.00.00

**Name**: IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled 

**Execution**: optional
**Implemented**: 2016-03-01 00:00:00

---

### PRPL 19.00.00

**Name**: Name Check Function (FC)

**Execution**: optional
**Implemented**: open

---

### PRPL 20.00.00

**Name**: Name Check Package (BC)

**Execution**: optional
**Implemented**: open

---

### PRPL 21.00.00

**Name**: ISW is not closed or imeplemented, even though function developement is finished and at least one or all IRM(s) are qualified

**Execution**: optional
**Implemented**: 2019-09-01 00:00:00

---

### PRPL 22.00.00

**Name**: PVER-F testing is open even though IRM is impleneted

**Execution**: optional
**Implemented**: 2019-09-01 00:00:00

---

### PRPS 01.00.00

**Name**: Tracking of Change Requests

**Execution**: optional
**Implemented**: open

---

### PRPS 01.01.00

**Name**: Decomposition missing of Issue to affected SW Components

**Execution**: optional
**Implemented**: 2019-09-01 00:00:00

---

### PRPS 01.02.00

**Name**: Decomposition of ASIL not correct

**Execution**: optional
**Implemented**: open

---

## ? SKIP: OUTDATED (7 rules)

### PRPL 02.00.00

**Name**: Workitem is in started state, but  planned date for workitem is not entered in planning tab 

**Execution**: outdated
**Implemented**: 2019-09-01 00:00:00

---

### PRPL 17.00.00

**Name**: Status Canceled (planned)

**Execution**: outdated
**Implemented**: open

---

### PRPS 02.02.00

**Name**: Traceability Check between Change Request to Customer Requirement

**Execution**: outdated
**Implemented**: 2019-09-01 00:00:00

---

### PRPS 02.03.00

**Name**: Allocation Check for Customer Requirement

**Execution**: outdated
**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.06.00

**Name**: Traceability Check between ECU System Requirement to Customer Requirement

**Execution**: outdated

---

### PRPS 03.07.00

**Name**: Traceability Check between Change Request to System Functionality Requirement

**Execution**: outdated
**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.09.00

**Name**: Traceability Check between System Functionality Requirement to ECU System Requirement

**Execution**: outdated
**Implemented**: 2019-09-01 00:00:00

---

## - N/A: (NOT SPECIFIED) (29 rules)

### PRAS 01.00.00

**Name**: MAN.3 - Task Planning and Tracking

**Implemented**: open

---

### PRAS 01.01.00

**Name**: MAN.3 - Resource Loading and Skill matrix

**Implemented**: open

---

### PRAS 01.02.00

**Name**: Project Management Plan (- Project Guide) Contracting (Project, FMEA)

**Implemented**: open

---

### PRAS 01.03.00

**Name**: Project Review and Reporting (Including CPC-SW, Traffic light list SW)

**Implemented**: open

---

### PRAS 01.04.00

**Name**: ISO26262 artefacts check

**Implemented**: open

---

### PRAS 02.00.00

**Name**: Planning and Tracking of Quality Assurance activities and reviews

**Implemented**: open

---

### PRAS 02.01.00

**Name**: Quality Assurance Metrics Tracking

**Implemented**: open

---

### PRAS 03.00.00

**Name**: Problem Resolution Management

**Implemented**: open

---

### PRAS 03.01.00

**Name**: Risk Handling

**Implemented**: open

---

### PRPL 04.00.00

**Name**: Effort estimation (planned)

**Implemented**: open

---

### PRPL 05.00.00

**Name**: Planning of Quality Measures (planned)

**Implemented**: open

---

### PRPL 18.00.00

**Name**: I-FD not committed 5 or more working days after attached I-SW was committed

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 02.01.00

**Name**: Missing mandatory Attributes of Customer Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 02.04.00

**Name**: Baseline Check of Customer Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.00.00

**Name**: Missing Impact Analysis for ECU System Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.01.00

**Name**: Missing mandatory Attributes of ECU System Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.02.00

**Name**: Missing mandatory Attributes of System Functionality Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.03.00

**Name**: Missing mandatory Attributes of System Functionality and System Interface Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.04.00

**Name**: Traceability Check between Change Request to ECU System Requirement

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.05.00

**Name**: Allocation Check of ECU System Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.08.00

**Name**: Traceability Check between System Functionality Requirement to Change Request

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.10.00

**Name**: Baseline Check of ECU System Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 03.11.00

**Name**: Baseline Check of System Functionality Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 04.00.00

**Name**: Missing mandatory Attributes of Software Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 04.01.00

**Name**: Missing mandatory Attributes of Software and Software Interface Requirements

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 04.02.00

**Name**: Traceability Check between Change Request to Software Requirement

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 04.03.00

**Name**: Traceability Check between Software Requirement to Change Request

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 04.04.00

**Name**: Traceability Check between Software Requirement to System Functionality Requirement

**Implemented**: 2019-09-01 00:00:00

---

### PRPS 04.05.00

**Name**: Baseline Check of Software Requirements

**Implemented**: 2019-09-01 00:00:00

---

