# Activated Rules Mapping

Mapping of 32 activated rules from CSV to Java/Python implementations.

## Rules with Java Implementations

- PRPL 12.00.00: IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled -> Rule_Bc_Close
- PRPL 13.00.00: IFD is not implemented or closed, after planned dated of BC-R -> Rule_Bc_Close
- PRPL 15.00.00: Release is not closed after planned date for BC and FC -> Rule_Bc_Close

## Rules Needing New Python Implementation


## Rules with Unknown Mapping

- PRPL 01.00.00: BC-R is not in requested state, 8 weeks before PVER planned delivery date
- PRPL 02.00.00: Workitem is in started state, but  planned date for workitem is not entered in planning tab
- PRPL 03.00.00: Issue/Release/workitem is still in "Conflicted" state
- PRPL 07.00.00: Planned date of BC later than requested delivery date of any mapped PVER or PVAR
- PRPL 11.00.00: IFD 5 day SLA reached
- PRPL 14.00.00: IFD is not committed, eventhough attached Issue-SW is committed
- PRPL 16.00.00: Workitem is not closed after planned date
- PRPL 18.00.00: I-FD not committed 5 or more working days after attached I-SW was committed
- PRPL 06.00.00: Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled
- QAWP 08.00.00: Content of Test Container for Packages (BC or related classes) in SCM
- QADO 04.00.00: SW Metrics Relevance is set and BMI is given as lower than -50 but assessment is not documented
- QAWP 02.00.00: Specification / Function Review
- QAWP 03.00.00: Software / Code Review
- QAWP 05.00.00: LifeCycle in SCM (SDOM)
- QAWP 04.00.00: Verification and Validation Condition in SCM (SDOM)
- QAWP 06.00.00: Content of Test Container for FC (and related classes) in SCM (SDOM)
- QAWP 01.00.00: Concept Review
- BBM_11: Ratio of system requirements reviewed
- BBM_12: Ratio of system requirements traceable to its source
- BBM_15: Ratio of software requirements reviewed
- BBM_16: Ratio of software requirements traceable to its source
- BBM_20: Ratio of software components/units successfully verified
- BBM_23: MISRA violations without deviation record
- BBM_3: Ratio of system requirements successfully verified
- BBM_1: Ratio of software requirements implemented
- BBM_6: Ratio of software requirements successfully verified
- BBM_8: Ratio of defects closed
- BBM_9: Ratio of change requests closed
- PS-SC_4: Taglists in BC-R missing/multiple
