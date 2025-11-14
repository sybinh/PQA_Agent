# Excel Rule Sets - Summary

**Source**: SW_QAMRuleSet_Tmplt.xlsm

## Overview

| Rule Set | Count | Description |
|----------|-------|-------------|
| BBM | 23 | Quality Assurance - BBM metrics |
| QAM (FKT) | 22 | Quality Assurance - Dokumentation |
| IPT (CUST) | 56 | PMT - Rule Set |
| **TOTAL** | **101** | |

## BBM Rules (Metrics)

- **BBM 01**: Ratio of technical customer requirements accepted
  - Execution: mandatory
- **BBM 02**: Ratio of system interfaces successfully verified
- **BBM 03**: Ratio of system requirements successfully verified
  - Execution: optional
- **BBM 04**: Ratio of software requirements implemented
  - Execution: optional / mandatory
- **BBM 05**: Ratio of software interfaces successfully verified
- **BBM 06**: Ratio of software requirements successfully verified
  - Execution: optional
- **BBM 07**: Ratio of SSL requirements successfully verified (only safety in 2017!)
  - Execution: optional
- **BBM 08**: Ratio of defects closed
  - Execution: mandatory
- **BBM 09**: Ratio of change requests closed
  - Execution: mandatory
- **BBM 10**: Ratio of technical customer requirements traceable to internal requirements
  - Execution: mandatory
- **BBM 11**: Ratio of system requirements reviewed
  - Execution: mandatory
- **BBM 12**: Ratio of system requirements traceable to its source
  - Execution: mandatory
- **BBM 13**: Ratio of system requirements linked to at least one test case
  - Execution: optional
- **BBM 14**: Ratio of defined system interfaces linked to at least one test case
- **BBM 15**: Ratio of software requirements reviewed
  - Execution: mandatory
- **BBM 16**: Ratio of software requirements traceable to its source
  - Execution: mandatory
- **BBM 17**: Ratio of software requirements linked to at least one test case
  - Execution: optional
- **BBM 18**: Ratio of software interfaces linked to at least one test case
- **BBM 19**: Ratio of software units implemented or changed according to current release plan
  - Execution: to be deleted
- **BBM 20**: Ratio of software components/units successfully verified
  - Execution: optional
- **BBM 21**: Consumption of processor capacity
  - Execution: to be deleted
- **BBM 22**: Consumption of memory
  - Execution: to be deleted
- **BBM 23**: Sum of unevaluated errors and warnings from static code analysis
  - Execution: mandatory

## QAM Rules (Documentation)

- **QADO 01.00.00**: FC in delivered state in SCM, but Issue-FD is not closed
  - Relevant for PQA: yes
- **QADO 02.00.00**: FC in delivered state in SCM, check for requirement based development
  - Relevant for PQA: yes
- **QADO 02.01.00**: FC in delivered state in SCM, requirement based development needed, check for URT documentation
  - Relevant for PQA: yes
- **QADO 03.00.00**: Sorrounding of ECU generation - MISRA
  - Relevant for PQA: yes
- **QADO 04.00.00**: BMI evaluation and documentation
  - Relevant for PQA: no
- **QAWP 01.00.00**: Concept Review
  - Relevant for PQA: no
- **QAWP 02.00.00**: Specification / Function Review
  - Relevant for PQA: yes
- **QAWP 02.01.00**: Specification / Function Review "not necessary"
  - Relevant for PQA: no
- **QAWP 03.00.00**: Software / Code Review
  - Relevant for PQA: yes
- **QAWP 03.01.00**: SW Review "exempt"
  - Relevant for PQA: no
- **QAWP 04.00.00**: Verification and Validation Condition in SCM
  - Relevant for PQA: yes
- **QAWP 05.00.00**: LifeCycle in SCM
  - Relevant for PQA: yes
- **QAWP 06.00.00**: Content of Test Container for Functions (FC or related classes) in SCM
  - Relevant for PQA: yes
- **QAWP 06.01.00**: Warning Assessment of Test Container for Functions (FC or related classes) in SCM 
  - Relevant for PQA: yes
- **QAWP 06.02.00**: Review of Test Specification Artefacts for Functions (FC or related classes) in SCM 
  - Relevant for PQA: yes
- **QAWP 07.00.01**: Content of delivered Packages (BC or related classes) in SCM
  - Relevant for PQA: yes
- **QAWP 07.01.00**: Tag-List for developed Packages (BC or related classes) in SCM
  - Relevant for PQA: yes
- **QAWP 08.00.00**: Content of Test Container for Packages (BC or related classes) in SCM
  - Relevant for PQA: yes
- **QAWP 08.01.00**: Warning Assessment of Test Container for Packages (BC or related classes) in SCM 
  - Relevant for PQA: yes
- **QAWP 09.00.00**: Content of delivered Programm Version (PVER or related classes) in SCM
  - Relevant for PQA: yes
- **QAWP 10.00.00**: Content of Test Container for Programm Version (PVER or related classes) in SCM
  - Relevant for PQA: yes
- **QAWP 10.01.00**: Warning Assessment of Test Container for Programm Version (PVER or related classes) in SCM
  - Relevant for PQA: yes

## IPT Rules (Customer)

- **PRPL 01.00.00**: BC-R is not in requested state, 8 weeks before PVER planned delivery date 
- **PRPL 02.00.00**: Workitem is in started state, but  planned date for workitem is not entered in planning tab 
- **PRPL 03.00.00**: Issue/Release/workitem is still in "Conflicted" state
- **PRPL 04.00.00**: Effort estimation (planned)
- **PRPL 05.00.00**: Planning of Quality Measures (planned)
- **PRPL 06.00.00**: Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled
- **PRPL 07.00.00**: Planned date of BC later than requested delivery date of any mapped PVER or PVAR
- **PRPL 08.00.00**: BC without mapping to PVER or PFAM
- **PRPL 09.00.00**: FC in time for BC
- **PRPL 10.00.00**: Check dates for PVER, BC and FC
- **PRPL 11.00.00**: IFD 5 day SLA reached
- **PRPL 12.00.00**: IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled 
- **PRPL 13.00.00**: IFD is not implemented or closed, after planned dated of BC-R
- **PRPL 14.00.00**: IFD is not committed, eventhough attached Issue-SW is committed
- **PRPL 15.00.00**: Release is not closed after planned date for BC and FC
- **PRPL 16.00.00**: Workitem is not closed after planned date
- **PRPL 17.00.00**: Status Canceled (planned)
- **PRPL 18.00.00**: I-FD not committed 5 or more working days after attached I-SW was committed
- **PRPL 19.00.00**: Name Check Function (FC)
- **PRPL 20.00.00**: Name Check Package (BC)
- **PRPL 21.00.00**: ISW is not closed or imeplemented, even though function developement is finished and at least one or all IRM(s) are qualified
- **PRPL 22.00.00**: PVER-F testing is open even though IRM is impleneted
- **PRPS 01.00.00**: Tracking of Change Requests
- **PRPS 01.01.00**: Decomposition missing of Issue to affected SW Components
- **PRPS 01.02.00**: Decomposition of ASIL not correct
- **PRPS 02.01.00**: Missing mandatory Attributes of Customer Requirements
- **PRPS 02.02.00**: Traceability Check between Change Request to Customer Requirement
- **PRPS 02.03.00**: Allocation Check for Customer Requirement
- **PRPS 02.04.00**: Baseline Check of Customer Requirements
- **PRPS 03.00.00**: Missing Impact Analysis for ECU System Requirements
- **PRPS 03.01.00**: Missing mandatory Attributes of ECU System Requirements
- **PRPS 03.02.00**: Missing mandatory Attributes of System Functionality Requirements
- **PRPS 03.03.00**: Missing mandatory Attributes of System Functionality and System Interface Requirements
- **PRPS 03.04.00**: Traceability Check between Change Request to ECU System Requirement
- **PRPS 03.05.00**: Allocation Check of ECU System Requirements
- **PRPS 03.06.00**: Traceability Check between ECU System Requirement to Customer Requirement
- **PRPS 03.07.00**: Traceability Check between Change Request to System Functionality Requirement
- **PRPS 03.08.00**: Traceability Check between System Functionality Requirement to Change Request
- **PRPS 03.09.00**: Traceability Check between System Functionality Requirement to ECU System Requirement
- **PRPS 03.10.00**: Baseline Check of ECU System Requirements
- **PRPS 03.11.00**: Baseline Check of System Functionality Requirements
- **PRPS 04.00.00**: Missing mandatory Attributes of Software Requirements
- **PRPS 04.01.00**: Missing mandatory Attributes of Software and Software Interface Requirements
- **PRPS 04.02.00**: Traceability Check between Change Request to Software Requirement
- **PRPS 04.03.00**: Traceability Check between Software Requirement to Change Request
- **PRPS 04.04.00**: Traceability Check between Software Requirement to System Functionality Requirement
- **PRPS 04.05.00**: Baseline Check of Software Requirements
- **PRAS 01.00.00**: MAN.3 - Task Planning and Tracking
- **PRAS 01.01.00**: MAN.3 - Resource Loading and Skill matrix
- **PRAS 01.02.00**: Project Management Plan (- Project Guide) Contracting (Project, FMEA)
- **PRAS 01.03.00**: Project Review and Reporting (Including CPC-SW, Traffic light list SW)
- **PRAS 01.04.00**: ISO26262 artefacts check
- **PRAS 02.00.00**: Planning and Tracking of Quality Assurance activities and reviews
- **PRAS 02.01.00**: Quality Assurance Metrics Tracking
- **PRAS 03.00.00**: Problem Resolution Management
- **PRAS 03.01.00**: Risk Handling

