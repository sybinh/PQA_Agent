# Deep Rule Mapping Analysis

Total CSV rules: 32
Total Java rules: 37
Matched: 14 (43.8%)

## Matched Rules (Can Reuse Java Logic)

### PRPL 01.00.00

**CSV**: BC-R is not in requested state, 8 weeks before PVER planned delivery date

**Java Class**: `Rule_Bc_CheckPstDates`

**Execution Group**: EXCITED_CPC

**IDs**:**Java File**: `Rule_Bc_CheckPstDates.java`

---

### PRPL 02.00.00

**CSV**: Workitem is in started state, but  planned date for workitem is not entered in planning tab

**Java Class**: `Rule_Bc_CheckPstDates`

**Execution Group**: EXCITED_CPC

**IDs**:**Java File**: `Rule_Bc_CheckPstDates.java`

---

### PRPL 07.00.00

**CSV**: Planned date of BC later than requested delivery date of any mapped PVER or PVAR

**Java Class**: `Rule_Bc_CheckPstDates`

**Execution Group**: EXCITED_CPC

**IDs**:**Java File**: `Rule_Bc_CheckPstDates.java`

---

### PRPL 11.00.00

**CSV**: IFD 5 day SLA reached

**Java Class**: `Rule_IssueFD_Pilot`

**Execution Group**: I_FD_PILOT

**IDs**:**Java File**: `Rule_IssueFD_Pilot.java`

---

### PRPL 12.00.00

**CSV**: IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled

**Java Class**: `Rule_IssueFD_WithoutLinkToBc`

**Execution Group**: UI_VISIBLE

**IDs**:**Java File**: `Rule_IssueFD_WithoutLinkToBc.java`

---

### PRPL 13.00.00

**CSV**: IFD is not implemented or closed, after planned dated of BC-R

**Java Class**: `Rule_IssueFD_WithoutLinkToBc`

**Execution Group**: UI_VISIBLE

**IDs**:**Java File**: `Rule_IssueFD_WithoutLinkToBc.java`

---

### PRPL 14.00.00

**CSV**: IFD is not committed, eventhough attached Issue-SW is committed

**Java Class**: `Rule_IssueFD_Pilot`

**Execution Group**: I_FD_PILOT

**IDs**:**Java File**: `Rule_IssueFD_Pilot.java`

---

### PRPL 15.00.00

**CSV**: Release is not closed after planned date for BC and FC

**Java Class**: `Rule_CheckDatesForBcAndFc`

**Execution Group**: ELEMENT_INTEGRITY

**IDs**: QAM 2.5.0**Java File**: `Rule_CheckDatesForBcAndFc.java`

---

### PRPL 16.00.00

**CSV**: Workitem is not closed after planned date

**Java Class**: `Rule_Bc_CheckPstDates`

**Execution Group**: EXCITED_CPC

**IDs**:**Java File**: `Rule_Bc_CheckPstDates.java`

---

### PRPL 18.00.00

**CSV**: I-FD not committed 5 or more working days after attached I-SW was committed

**Java Class**: `Rule_IssueFD_Pilot`

**Execution Group**: I_FD_PILOT

**IDs**:**Java File**: `Rule_IssueFD_Pilot.java`

---

### PRPL 06.00.00

**CSV**: Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled

**Java Class**: `Rule_IssueFD_Pilot`

**Execution Group**: I_FD_PILOT

**IDs**:**Java File**: `Rule_IssueFD_Pilot.java`

---

### QAWP 08.00.00

**CSV**: Content of Test Container for Packages (BC or related classes) in SCM

**Java Class**: `Rule_Bc_CheckPstDates`

**Execution Group**: EXCITED_CPC

**IDs**:**Java File**: `Rule_Bc_CheckPstDates.java`

---

### QAWP 06.00.00

**CSV**: Content of Test Container for FC (and related classes) in SCM (SDOM)

**Java Class**: `Rule_CheckDatesForBcAndFc`

**Execution Group**: ELEMENT_INTEGRITY

**IDs**: QAM 2.5.0**Java File**: `Rule_CheckDatesForBcAndFc.java`

---

### PS-SC_4

**CSV**: Taglists in BC-R missing/multiple

**Java Class**: `Rule_Bc_CheckPstDates`

**Execution Group**: EXCITED_CPC

**IDs**:**Java File**: `Rule_Bc_CheckPstDates.java`

---

## Unmatched Rules (Need New Python Implementation)

### PRPL 03.00.00

**Description**: Issue/Release/workitem is still in "Conflicted" state

**Category**: PRPL

**Activation Date**: 1-Jul-16

---

### QADO 04.00.00

**Description**: SW Metrics Relevance is set and BMI is given as lower than -50 but assessment is not documented

**Category**: QADO

**Activation Date**: 1-Jan-20

---

### QAWP 02.00.00

**Description**: Specification / Function Review

**Category**: QAWP

**Activation Date**: 1-Jan-16

---

### QAWP 03.00.00

**Description**: Software / Code Review

**Category**: QAWP

**Activation Date**: 1-Jan-16

---

### QAWP 05.00.00

**Description**: LifeCycle in SCM (SDOM)

**Category**: QAWP

**Activation Date**: 1-Jan-16

---

### QAWP 04.00.00

**Description**: Verification and Validation Condition in SCM (SDOM)

**Category**: QAWP

**Activation Date**: 1-Jan-16

---

### QAWP 01.00.00

**Description**: Concept Review

**Category**: QAWP

**Activation Date**: 1-Jan-16

---

### BBM_11

**Description**: Ratio of system requirements reviewed

**Category**: UNKNOWN

**Activation Date**: 1-Jan-22

---

### BBM_12

**Description**: Ratio of system requirements traceable to its source

**Category**: UNKNOWN

**Activation Date**: 1-Jan-22

---

### BBM_15

**Description**: Ratio of software requirements reviewed

**Category**: UNKNOWN

**Activation Date**: 1-Mar-21

---

### BBM_16

**Description**: Ratio of software requirements traceable to its source

**Category**: UNKNOWN

**Activation Date**: 1-Mar-21

---

### BBM_20

**Description**: Ratio of software components/units successfully verified

**Category**: UNKNOWN

**Activation Date**: 1-Mar-21

---

### BBM_23

**Description**: MISRA violations without deviation record

**Category**: UNKNOWN

**Activation Date**: 1-Jan-21

---

### BBM_3

**Description**: Ratio of system requirements successfully verified

**Category**: UNKNOWN

**Activation Date**: 1-Jan-23

---

### BBM_1

**Description**: Ratio of software requirements implemented

**Category**: UNKNOWN

**Activation Date**: 1-Mar-21

---

### BBM_6

**Description**: Ratio of software requirements successfully verified

**Category**: UNKNOWN

**Activation Date**: 1-Jan-23

---

### BBM_8

**Description**: Ratio of defects closed

**Category**: UNKNOWN

**Activation Date**: 1-Mar-21

---

### BBM_9

**Description**: Ratio of change requests closed

**Category**: UNKNOWN

**Activation Date**: 1-Mar-21

---

