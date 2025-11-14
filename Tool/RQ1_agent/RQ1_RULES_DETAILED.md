# RQ1 RULES - DETAILED SPECIFICATIONS
# Complete Reference for All 37 Compliance Rules from POST Tool v1.0.3

**Document Version**: 1.0  
**Last Updated**: 2024  
**Purpose**: Comprehensive specifications for implementing RQ1 compliance validation in PQA_Agent MCP server

---

## ?? TABLE OF CONTENTS

1. [Overview](#overview)
2. [Rule Categories](#categories)
3. [Detailed Rule Specifications](#specifications)
   - [Issue SW Rules (3)](#issue-sw)
   - [Issue FD Rules (2)](#issue-fd)
   - [BC Rules (4)](#bc-rules)
   - [FC Rules (4)](#fc-rules)
   - [Date Validation Rules (3)](#date-rules)
   - [Baseline & Link Rules (1)](#baseline-rules)
   - [ECU Rules (2)](#ecu-rules)
   - [IRM Rules (4)](#irm-rules)
   - [PRG Rules (1)](#prg-rules)
   - [PVAR/PVER Rules (3)](#pvar-pver-rules)
   - [Release Rules (1)](#release-rules)
   - [RRM Rules (4)](#rrm-rules)
   - [Info/ToDo Rules (2)](#info-rules)
   - [Existence Rules (2)](#existence-rules)
   - [Account Rules (1)](#account-rules)
4. [Common Patterns](#patterns)
5. [Field Reference](#fields)
6. [Implementation Guide](#implementation)

---

<a name="overview"></a>
## ?? OVERVIEW

### Rule Summary
- **Total Rules**: 37
- **Source**: POST Tool v1.0.3 (Java/VBS)
- **Purpose**: Validate RQ1/ClearQuest record compliance
- **Output Format**: Warnings/Failures with violation details
- **Target**: MCP server integration for AI assistant

### Severity Levels
- **FAILURE**: Critical compliance violation, must be fixed
- **WARNING**: Non-critical issue, should be reviewed
- **INFO**: Informational message, suggestion for improvement

### Rule Execution Context
- **Records Checked**: Issue SW/FD, BC, FC, PVAR, PVER, Release, ECU, PRG, IRM, RRM
- **State Filtering**: Most rules skip CANCELED/CONFLICTED states
- **Date Filtering**: Many rules only apply after specific dates (2015-01-01, 2019-01-01)
- **Relationship Traversal**: Rules can check parent/child/linked records

---

<a name="categories"></a>
## ?? RULE CATEGORIES

| Category | Count | Focus Area |
|----------|-------|------------|
| Issue SW | 3 | Software Issue compliance |
| Issue FD | 2 | Function Development compliance |
| BC (Baseline Configuration) | 4 | BC record validation |
| FC (Function Change) | 4 | FC record validation |
| Date Validation | 3 | Cross-record date logic |
| Baseline & Links | 1 | DOORS baseline links |
| ECU | 2 | ECU-specific validation |
| IRM | 4 | Issue-Release mapping |
| PRG | 1 | Program validation |
| PVAR/PVER | 3 | Product variant/version |
| Release | 1 | Release predecessor |
| RRM | 4 | Release-Requirement mapping |
| Info/ToDo | 2 | Comment-based hints |
| Existence | 2 | Unknown record detection |
| Account | 1 | Account number format |

---

<a name="specifications"></a>
## ?? DETAILED RULE SPECIFICATIONS

<a name="issue-sw"></a>
### 1?? ISSUE SW (Software Issue) - 3 Rules

---

#### **Rule_IssueSW_FmeaCheck** ? Priority #1

**Purpose**: Ensure FMEA comment is provided when FMEA state is "Not Required"

**Record Type**: `IssueSW`

**Fields Required**:
- `FMEA_STATE` (enum)
- `FMEA_COMMENT` (text)
- `FMEA_CHANGE_COMMENT` (text)
- `SUBMIT_DATE` (date)
- `LIFE_CYCLE_STATE` (enum)

**Trigger Conditions**:
- `SUBMIT_DATE >= 2015-01-01`
- Record NOT in state: CANCELED, CONFLICTED

**Validation Logic**:
```
IF FMEA_STATE == "Not Required"
   AND (FMEA_COMMENT is empty OR null)
   AND (FMEA_CHANGE_COMMENT is empty OR null)
THEN
   Violation: "Comment for not required FMEA missing."
   Severity: WARNING
```

**Example Violation**:
```json
{
  "rq1_number": "RQ100123456",
  "record_type": "IssueSW",
  "violation": {
    "rule": "Rule_IssueSW_FmeaCheck",
    "severity": "WARNING",
    "message": "Comment for not required FMEA missing.",
    "fields_checked": ["FMEA_STATE", "FMEA_COMMENT", "FMEA_CHANGE_COMMENT"],
    "current_values": {
      "FMEA_STATE": "Not Required",
      "FMEA_COMMENT": null,
      "FMEA_CHANGE_COMMENT": null
    },
    "expected": "Provide explanation in FMEA_COMMENT or FMEA_CHANGE_COMMENT"
  }
}
```

**Implementation Notes**:
- Check both comment fields (either can satisfy requirement)
- Only validate issues submitted after 2015-01-01
- Skip canceled/conflicted issues

**Java Source Reference**:
```java
// From Rule_IssueSW_FmeaCheck.java
if (!issueSW.isCanceledOrConflicted() && issueSW.getSubmitDate() >= FMEA_COMMENT_CHECK_DATE) {
    if (IssueProperty.FMEA_STATE.getValue(issueSW) == FmeaState.NOT_REQUIRED) {
        String fmeaComment = IssueProperty.FMEA_COMMENT.getValue(issueSW);
        String fmeaChangeComment = IssueProperty.FMEA_CHANGE_COMMENT.getValue(issueSW);
        
        if (StringUtils.isEmpty(fmeaComment) && StringUtils.isEmpty(fmeaChangeComment)) {
            addWarning(issueSW, "Comment for not required FMEA missing.");
        }
    }
}
```

---

#### **Rule_IssueSW_ASIL** ? Priority #2

**Purpose**: Ensure ASIL safety level consistency between parent Issue SW and child Issue FD

**Record Types**: `IssueSW` (parent) + `IssueFD` (children)

**Fields Required**:
- Parent: `ASIL_CLASSIFICATION` (enum), `CHILDREN` (relationship), `LIFE_CYCLE_STATE`
- Child: `ASIL_CLASSIFICATION` (enum), `LIFE_CYCLE_STATE`

**Trigger Conditions**:
- Parent NOT in state: NEW, CLOSED, CANCELED, CONFLICTED
- Has at least one child Issue FD

**ASIL Hierarchy**:
| Parent ASIL | Required Child ASIL |
|-------------|---------------------|
| QM | No check (pass) |
| ASIL_A | Must have child with A, B, C, or D |
| ASIL_B | Must have child with B, C, or D |
| ASIL_C | Must have child with C or D |
| ASIL_D | Must have child with D |
| ASIL_A_B | Must have child with A, B, C, or D |
| ASIL_A_C | Must have child with A, C, or D |
| ASIL_A_D | Must have child with A, D |
| ASIL_B_C | Must have child with B, C, or D |
| ASIL_B_D | Must have child with B or D |
| ASIL_C_D | Must have child with C or D |

**Validation Logic**:
```
1. Get parent IssueSW ASIL classification
2. IF ASIL == QM, RETURN pass

3. Build required child ASIL levels based on hierarchy
4. Get all child IssueFD records
5. Check if at least ONE child has acceptable ASIL level

6. IF no child has required level:
   Violation: "No I-FD found with at least ASIL level X"
   Severity: WARNING or FAILURE (depends on context)
```

**Example Violation**:
```json
{
  "rq1_number": "RQ100123456",
  "record_type": "IssueSW",
  "violation": {
    "rule": "Rule_IssueSW_ASIL",
    "severity": "WARNING",
    "message": "No I-FD found with at least ASIL level B",
    "parent_asil": "ASIL_B",
    "required_child_asil": ["ASIL_B", "ASIL_C", "ASIL_D"],
    "children_found": [
      {"rq1_number": "RQ100123457", "asil": "ASIL_A"},
      {"rq1_number": "RQ100123458", "asil": "QM"}
    ],
    "recommendation": "Add or update I-FD with ASIL_B/C/D classification"
  }
}
```

**Implementation Notes**:
- Complex rule requires fetching child records
- Must handle combined ASIL levels (ASIL_A_B, etc.)
- Skip issues in terminal states

**Java Source Reference**:
```java
// From Rule_IssueSW_ASIL.java
private Set<AsilClassification> buildNeededAsilLevels(AsilClassification parentAsil) {
    Set<AsilClassification> needed = new HashSet<>();
    
    switch(parentAsil) {
        case ASIL_A: return Set.of(ASIL_A, ASIL_B, ASIL_C, ASIL_D);
        case ASIL_B: return Set.of(ASIL_B, ASIL_C, ASIL_D);
        case ASIL_C: return Set.of(ASIL_C, ASIL_D);
        case ASIL_D: return Set.of(ASIL_D);
        // Handle combined levels...
    }
}
```

---

#### **Rule_IssueSW_MissingAffectedIssueComment** ? Priority #9

**Purpose**: Ensure comment exists when affected issues are linked

**Record Type**: `IssueSW`

**Fields Required**:
- `AFFECTED_ISSUE_COMMENT` (text)
- `affected_issues` (relationship list)

**Validation Logic**:
```
IF affected_issues list is NOT empty
   AND AFFECTED_ISSUE_COMMENT is empty OR null
THEN
   Violation: "Comment for affected issues is missing"
   Severity: WARNING
```

**Example Violation**:
```json
{
  "rq1_number": "RQ100123456",
  "record_type": "IssueSW",
  "violation": {
    "rule": "Rule_IssueSW_MissingAffectedIssueComment",
    "severity": "WARNING",
    "message": "Comment for affected issues is missing",
    "affected_issues_count": 3,
    "affected_issues": ["RQ100111111", "RQ100222222", "RQ100333333"],
    "expected": "Provide explanation in AFFECTED_ISSUE_COMMENT field"
  }
}
```

---

<a name="issue-fd"></a>
### 2?? ISSUE FD (Function Development) - 2 Rules

---

#### **Rule_IssueFD_Pilot**

**Purpose**: Validate pilot derivative configuration for Function Development issues

**Record Type**: `IssueFD`

**Fields Required**:
- `PILOT_DERIVATIVE` (text/enum)
- Related pilot configuration data

**Validation Logic**:
```
IF PILOT_DERIVATIVE is configured
   AND configuration is invalid/incomplete
THEN
   Violation: "Pilot derivative configuration issue"
   Severity: WARNING
```

**Implementation Notes**:
- Specific validation logic depends on pilot requirements
- May require checking related configuration records

---

#### **Rule_IssueFD_WithoutLinkToBc** ? Priority #7

**Purpose**: Ensure Issue FD is linked to a Baseline Configuration (BC)

**Record Type**: `IssueFD`

**Fields Required**:
- BC relationship links (varies by link type)

**Validation Logic**:
```
IF Issue FD is in active state
   AND no link to BC record exists
THEN
   Violation: "Issue FD without link to BC"
   Severity: WARNING
```

**Example Violation**:
```json
{
  "rq1_number": "RQ100123456",
  "record_type": "IssueFD",
  "violation": {
    "rule": "Rule_IssueFD_WithoutLinkToBc",
    "severity": "WARNING",
    "message": "Issue FD without link to BC",
    "expected": "Link this Issue FD to at least one BC record"
  }
}
```

---

<a name="bc-rules"></a>
### 3?? BC (Baseline Configuration) - 4 Rules

---

#### **Rule_Bc_CheckPstDates** ? Priority #10

**Purpose**: Validate PST (Problem Solution Tracking) date consistency

**Record Type**: `BC`

**Fields Required**:
- PST-related date fields
- `DELIVERY_DATE`
- `PLANNED_DATE`

**Validation Logic**:
```
IF PST dates are inconsistent with BC dates
   OR PST delivery date > BC delivery date
THEN
   Violation: "PST dates inconsistent with BC dates"
   Severity: WARNING
```

**Example Violation**:
```json
{
  "rq1_number": "BC12345AB",
  "record_type": "BC",
  "violation": {
    "rule": "Rule_Bc_CheckPstDates",
    "severity": "WARNING",
    "message": "PST delivery date is after BC delivery date",
    "pst_delivery_date": "2024-12-31",
    "bc_delivery_date": "2024-06-30",
    "expected": "PST dates should be consistent with BC schedule"
  }
}
```

---

#### **Rule_Bc_Close**

**Purpose**: Validate BC can be closed based on all closure conditions

**Record Type**: `BC`

**Fields Required**:
- `LIFE_CYCLE_STATE`
- Closure prerequisite checks (linked records, completion status)

**Validation Logic**:
```
IF attempting to close BC
   AND closure conditions not met (e.g., linked issues not complete)
THEN
   Violation: "BC cannot be closed - conditions not met"
   Severity: WARNING
```

---

#### **Rule_Bc_NamingConvention** ? Priority #4

**Purpose**: Enforce BC naming convention format

**Record Type**: `BC`

**Fields Required**:
- `NAME` or `TITLE` (text)

**Pattern**: `^BC[0-9]{5}[A-Z]{2}$`

**Examples**:
- ? Valid: `BC12345AB`, `BC99999ZZ`, `BC00001AA`
- ? Invalid: `BC1234AB` (too short), `bc12345ab` (lowercase), `BC12345A` (missing char)

**Validation Logic**:
```
IF BC.NAME does NOT match pattern "^BC[0-9]{5}[A-Z]{2}$"
THEN
   Violation: "BC name does not match naming convention"
   Severity: WARNING
```

**Example Violation**:
```json
{
  "rq1_number": "BC1234AB",
  "record_type": "BC",
  "violation": {
    "rule": "Rule_Bc_NamingConvention",
    "severity": "WARNING",
    "message": "BC name does not match naming convention",
    "current_name": "BC1234AB",
    "expected_pattern": "^BC[0-9]{5}[A-Z]{2}$",
    "example": "BC12345AB"
  }
}
```

**Implementation**:
```python
import re

def validate_bc_naming(bc_name: str) -> bool:
    """Validate BC naming convention"""
    pattern = r'^BC[0-9]{5}[A-Z]{2}$'
    return bool(re.match(pattern, bc_name))
```

---

#### **Rule_Bc_WithoutLinkToPst** ? Priority #8

**Purpose**: Ensure BC is linked to PST (Problem Solution Tracking)

**Record Type**: `BC`

**Fields Required**:
- PST relationship links

**Validation Logic**:
```
IF BC is in active state
   AND no link to PST record exists
THEN
   Violation: "BC without link to PST"
   Severity: WARNING
```

**Example Violation**:
```json
{
  "rq1_number": "BC12345AB",
  "record_type": "BC",
  "violation": {
    "rule": "Rule_Bc_WithoutLinkToPst",
    "severity": "WARNING",
    "message": "BC without link to PST",
    "expected": "Link this BC to at least one PST record"
  }
}
```

---

<a name="fc-rules"></a>
### 4?? FC (Function Change) - 4 Rules

---

#### **Rule_Fc_NamingConvention** ? Priority #5

**Purpose**: Enforce FC naming convention format

**Record Type**: `FC`

**Fields Required**:
- `NAME` or `TITLE` (text)

**Pattern**: Similar to BC, typically `^FC[0-9]{5}[A-Z]{2}$`

**Examples**:
- ? Valid: `FC12345AB`, `FC99999ZZ`
- ? Invalid: `FC1234AB`, `fc12345ab`

**Validation Logic**:
```
IF FC.NAME does NOT match pattern
THEN
   Violation: "FC name does not match naming convention"
   Severity: WARNING
```

**Implementation**:
```python
def validate_fc_naming(fc_name: str) -> bool:
    """Validate FC naming convention"""
    pattern = r'^FC[0-9]{5}[A-Z]{2}$'
    return bool(re.match(pattern, fc_name))
```

---

#### **Rule_Fc_PlannedDate**

**Purpose**: Validate FC planned date is reasonable

**Record Type**: `FC`

**Fields Required**:
- `PLANNED_DATE` (date)
- Related date fields for validation

**Validation Logic**:
```
IF PLANNED_DATE is missing
   OR PLANNED_DATE is in the past (for new FC)
   OR PLANNED_DATE conflicts with related dates
THEN
   Violation: "FC planned date issue"
   Severity: WARNING
```

---

#### **Rule_Fc_ReqDate**

**Purpose**: Validate FC request date is set and reasonable

**Record Type**: `FC`

**Fields Required**:
- `REQ_DATE` or `REQUEST_DATE` (date)

**Validation Logic**:
```
IF REQ_DATE is missing
   OR REQ_DATE is in future (for existing FC)
THEN
   Violation: "FC request date issue"
   Severity: WARNING
```

---

#### **Rule_Fc_WithoutLinkToBc**

**Purpose**: Ensure FC is linked to BC

**Record Type**: `FC`

**Fields Required**:
- BC relationship links

**Validation Logic**:
```
IF FC is in active state
   AND no link to BC record exists
THEN
   Violation: "FC without link to BC"
   Severity: WARNING
```

---

<a name="date-rules"></a>
### 5?? DATE VALIDATION - 3 Rules

---

#### **Rule_CheckDatesForBcAndFc** ? Priority #6

**Purpose**: Ensure date consistency between BC and FC records

**Record Types**: `BC` + `FC` (linked)

**Fields Required**:
- BC: `PLANNED_DATE`, `DELIVERY_DATE`
- FC: `REQ_DATE`, `DELIVERY_DATE`

**Validation Logic**:
```
1. Check BC planned date vs FC request date:
   IF BC.PLANNED_DATE > FC.REQ_DATE
   THEN Warning: "BC planned date is after FC request date"

2. Check FC delivery vs BC delivery:
   IF FC.DELIVERY_DATE < BC.DELIVERY_DATE
   THEN Warning: "FC delivery date is before BC delivery date"

3. Check date ordering within each record:
   IF BC.DELIVERY_DATE < BC.PLANNED_DATE
   THEN Warning: "BC delivery before planned"
```

**Example Violation**:
```json
{
  "bc_rq1_number": "BC12345AB",
  "fc_rq1_number": "FC67890CD",
  "violation": {
    "rule": "Rule_CheckDatesForBcAndFc",
    "severity": "WARNING",
    "message": "Date inconsistency between BC and FC",
    "issues": [
      {
        "type": "BC_PLANNED_AFTER_FC_REQUEST",
        "bc_planned_date": "2024-12-31",
        "fc_req_date": "2024-06-01",
        "message": "BC planned date (2024-12-31) is after FC request date (2024-06-01)"
      }
    ],
    "expected": "BC should be planned before or at FC request date"
  }
}
```

**Implementation**:
```python
from datetime import datetime

def check_bc_fc_dates(bc: dict, fc: dict) -> list[dict]:
    """Check date consistency between BC and FC"""
    violations = []
    
    bc_planned = datetime.fromisoformat(bc.get('PLANNED_DATE'))
    fc_req = datetime.fromisoformat(fc.get('REQ_DATE'))
    
    if bc_planned > fc_req:
        violations.append({
            'type': 'BC_PLANNED_AFTER_FC_REQUEST',
            'message': f'BC planned ({bc_planned}) after FC request ({fc_req})'
        })
    
    # Additional checks...
    return violations
```

---

#### **Rule_CheckDatesForPvar**

**Purpose**: Validate date fields for Product Variant records

**Record Type**: `PVAR`

**Fields Required**:
- Various date fields specific to PVAR

**Validation Logic**:
```
IF date fields are inconsistent or invalid
THEN
   Violation: "PVAR date validation failed"
   Severity: WARNING
```

---

#### **Rule_CheckDatesForPver**

**Purpose**: Validate date fields for Product Version records

**Record Type**: `PVER`

**Fields Required**:
- Various date fields specific to PVER

**Validation Logic**:
```
IF date fields are inconsistent or invalid
THEN
   Violation: "PVER date validation failed"
   Severity: WARNING
```

---

<a name="baseline-rules"></a>
### 6?? BASELINE & LINKS - 1 Rule

---

#### **Rule_CheckForMissing_BaselineLink** ? Priority #3

**Purpose**: Ensure DOORS baseline link exists in software release records

**Record Types**: `SoftwareRelease` (includes PVER, PVAR, BC)

**Fields Required**:
- `TAGLIST` (text)
- `DESCRIPTION` (text)
- `INTERNAL_COMMENT` (text)
- `LIFE_CYCLE_STATE` (enum)
- `SUBMIT_DATE` (date)

**Trigger Conditions**:
- `LIFE_CYCLE_STATE` in: DEVELOPED, CLOSED
- `SUBMIT_DATE >= 2019-01-01`

**Pattern**: `(doors://)|(https?://.*dwa/rm/urn:rational)(.*)`

**Validation Logic**:
```
1. Search for DOORS link pattern in:
   - TAGLIST field
   - DESCRIPTION field
   - INTERNAL_COMMENT field

2. IF no link found
   AND state in [DEVELOPED, CLOSED]
   AND submit_date >= 2019-01-01
THEN
   Violation: "Link to DOORS baseline is missing"
   Severity: WARNING
```

**Valid Link Examples**:
- `doors://server/folder/document?version=1.0`
- `https://doors.bosch.com/dwa/rm/urn:rational::baseline::12345`

**Example Violation**:
```json
{
  "rq1_number": "BC12345AB",
  "record_type": "BC",
  "violation": {
    "rule": "Rule_CheckForMissing_BaselineLink",
    "severity": "WARNING",
    "message": "Link to DOORS baseline is missing",
    "fields_checked": ["TAGLIST", "DESCRIPTION", "INTERNAL_COMMENT"],
    "state": "DEVELOPED",
    "submit_date": "2023-05-15",
    "expected": "Add DOORS baseline link in format: doors:// or https://.../dwa/rm/urn:rational"
  }
}
```

**Implementation**:
```python
import re

def check_doors_baseline_link(record: dict) -> dict:
    """Check for DOORS baseline link"""
    doors_pattern = r'(doors://)|(https?://.*dwa/rm/urn:rational)'
    
    fields_to_check = [
        record.get('TAGLIST', ''),
        record.get('DESCRIPTION', ''),
        record.get('INTERNAL_COMMENT', '')
    ]
    
    for field_content in fields_to_check:
        if re.search(doors_pattern, field_content):
            return {'compliant': True}
    
    # Check trigger conditions
    state = record.get('LIFE_CYCLE_STATE')
    submit_date = datetime.fromisoformat(record.get('SUBMIT_DATE'))
    
    if state in ['DEVELOPED', 'CLOSED'] and submit_date >= datetime(2019, 1, 1):
        return {
            'compliant': False,
            'violation': 'Link to DOORS baseline is missing'
        }
    
    return {'compliant': True, 'reason': 'Not applicable'}
```

---

<a name="ecu-rules"></a>
### 7?? ECU - 2 Rules

---

#### **Rule_Ecu_LumpensammlerExists**

**Purpose**: Validate ECU lumpensammler configuration exists

**Record Type**: `ECU`

**Fields Required**:
- Lumpensammler configuration fields

**Validation Logic**:
```
IF ECU requires lumpensammler
   AND lumpensammler configuration missing or invalid
THEN
   Violation: "ECU lumpensammler validation failed"
   Severity: WARNING
```

---

#### **Rule_Ecu_PstLines**

**Purpose**: Validate PST lines configuration for ECU

**Record Type**: `ECU`

**Fields Required**:
- PST lines configuration
- Related PST data

**Validation Logic**:
```
IF PST lines configuration is incomplete or inconsistent
THEN
   Violation: "ECU PST lines validation failed"
   Severity: WARNING
```

---

<a name="irm-rules"></a>
### 8?? IRM (Issue Release Mapping) - 4 Rules

---

#### **Rule_Irm_OnlyOnePilotDerivative**

**Purpose**: Ensure only one pilot derivative is configured in IRM

**Record Type**: `IRM`

**Fields Required**:
- Pilot derivative configuration
- Derivative count

**Validation Logic**:
```
IF pilot derivative count > 1
THEN
   Violation: "Only one pilot derivative is allowed"
   Severity: WARNING
```

---

#### **Rule_Irm_Prg_HintForExclude**

**Purpose**: Provide hints for excluding items from PRG in IRM context

**Record Types**: `IRM` + `PRG`

**Severity**: INFO (informational)

**Validation Logic**:
```
IF conditions suggest exclusion is appropriate
THEN
   Info: "Consider excluding this item from PRG"
   Severity: INFO
```

---

#### **Rule_Irm_Prg_IssueSw_PilotSet**

**Purpose**: Validate pilot set configuration for Issue SW in PRG context

**Record Types**: `IRM` + `PRG` + `IssueSW`

**Fields Required**:
- Pilot set configuration
- PRG assignments

**Validation Logic**:
```
IF pilot set configuration is invalid or missing
THEN
   Violation: "Pilot set validation failed"
   Severity: WARNING
```

---

#### **Rule_Irm_PstRelease_IssueSw_Severity**

**Purpose**: Validate severity mapping between PST Release and Issue SW

**Record Types**: `IRM` + `PSTRelease` + `IssueSW`

**Fields Required**:
- `SEVERITY` (enum) from multiple records
- Severity mapping rules

**Validation Logic**:
```
IF severity levels are inconsistent between PST and Issue
THEN
   Violation: "Severity mismatch between PST Release and Issue SW"
   Severity: WARNING
```

---

<a name="prg-rules"></a>
### 9?? PRG (Program) - 1 Rule

---

#### **Rule_Prg_Lumpensammler**

**Purpose**: Validate program lumpensammler configuration

**Record Type**: `PRG`

**Fields Required**:
- Lumpensammler program configuration

**Validation Logic**:
```
IF program lumpensammler configuration is invalid or missing
THEN
   Violation: "Program lumpensammler validation failed"
   Severity: WARNING
```

---

<a name="pvar-pver-rules"></a>
### ?? PVAR/PVER (Product Variant/Version) - 3 Rules

---

#### **Rule_Pvar_Derivatives**

**Purpose**: Validate derivatives configuration for Product Variant

**Record Type**: `PVAR`

**Fields Required**:
- Derivative list
- Derivative configuration details

**Validation Logic**:
```
IF derivatives list is empty or invalid
   OR derivative configuration is inconsistent
THEN
   Violation: "PVAR derivatives validation failed"
   Severity: WARNING
```

---

#### **Rule_Pver_Derivatives**

**Purpose**: Validate derivatives configuration for Product Version

**Record Type**: `PVER`

**Fields Required**:
- Derivative list
- Derivative configuration details

**Validation Logic**:
```
IF derivatives list is empty or invalid
   OR derivative configuration is inconsistent
THEN
   Violation: "PVER derivatives validation failed"
   Severity: WARNING
```

---

#### **Rule_Pver_NotSuitableForINMA**

**Purpose**: Validate INMA (Initial Mass) suitability for Product Version

**Record Type**: `PVER`

**Fields Required**:
- INMA-related fields
- Suitability indicators

**Validation Logic**:
```
IF PVER is marked as suitable for INMA
   BUT conditions for INMA suitability are not met
THEN
   Violation: "PVER not suitable for INMA"
   Severity: WARNING
```

---

<a name="release-rules"></a>
### 1??1?? RELEASE - 1 Rule

---

#### **Rule_Release_Predecessor**

**Purpose**: Validate release predecessor relationships

**Record Type**: `Release`

**Fields Required**:
- Predecessor release links
- Release version information

**Validation Logic**:
```
IF release has no predecessor defined
   OR predecessor relationship is invalid
THEN
   Violation: "Release predecessor validation failed"
   Severity: WARNING
```

---

<a name="rrm-rules"></a>
### 1??2?? RRM (Release Requirement Mapping) - 4 Rules

---

#### **Rule_Rrm_Bc_Fc_DeliveryDate**

**Purpose**: Validate delivery date consistency between BC and FC in RRM context

**Record Types**: `RRM` + `BC` + `FC`

**Fields Required**:
- BC delivery date
- FC delivery date
- RRM mapping data

**Validation Logic**:
```
IF FC.DELIVERY_DATE < BC.DELIVERY_DATE
THEN
   Violation: "FC delivery date before BC delivery date in RRM"
   Severity: WARNING
```

---

#### **Rule_Rrm_Pst_Bc_DeliveryDate**

**Purpose**: Validate delivery date consistency between PST and BC in RRM context

**Record Types**: `RRM` + `PST` + `BC`

**Fields Required**:
- PST delivery date
- BC delivery date
- RRM mapping data

**Validation Logic**:
```
IF delivery dates are inconsistent
THEN
   Violation: "PST/BC delivery date mismatch in RRM"
   Severity: WARNING
```

---

#### **Rule_Rrm_Pst_Bc_HintForExclude**

**Purpose**: Provide hints for excluding PST/BC mapping from RRM

**Record Types**: `RRM` + `PST` + `BC`

**Severity**: INFO (informational)

**Validation Logic**:
```
IF conditions suggest exclusion is appropriate
THEN
   Info: "Consider excluding this PST/BC mapping from RRM"
   Severity: INFO
```

---

#### **Rule_Rrm_Pver_Bc_PilotSet**

**Purpose**: Validate pilot set configuration for PVER/BC in RRM context

**Record Types**: `RRM` + `PVER` + `BC`

**Fields Required**:
- Pilot set configuration
- PVER/BC mapping

**Validation Logic**:
```
IF pilot set configuration is invalid
THEN
   Violation: "Pilot set validation failed in RRM"
   Severity: WARNING
```

---

<a name="info-rules"></a>
### 1??3?? INFO/TODO - 2 Rules

---

#### **Rule_ToDo_InternalComment**

**Purpose**: Detect TODO markers in internal comments

**Record Types**: Various (all record types)

**Fields Required**:
- `INTERNAL_COMMENT` (text)

**Pattern**: `(?i)todo` (case-insensitive)

**Validation Logic**:
```
IF INTERNAL_COMMENT contains "TODO" (case-insensitive)
THEN
   Info: "TODO found in internal comment"
   Severity: INFO
```

**Example**:
```json
{
  "rq1_number": "RQ100123456",
  "violation": {
    "rule": "Rule_ToDo_InternalComment",
    "severity": "INFO",
    "message": "TODO found in internal comment",
    "comment": "TODO: Need to verify ASIL level with safety team",
    "recommendation": "Complete the TODO action"
  }
}
```

---

#### **Rule_Info_InternalComment**

**Purpose**: Detect INFO markers in internal comments

**Record Types**: Various (all record types)

**Fields Required**:
- `INTERNAL_COMMENT` (text)

**Pattern**: `(?i)info` (case-insensitive)

**Validation Logic**:
```
IF INTERNAL_COMMENT contains "INFO" (case-insensitive)
THEN
   Info: "INFO marker found in internal comment"
   Severity: INFO
```

---

<a name="existence-rules"></a>
### 1??4?? EXISTENCE - 2 Rules

---

#### **Rule_UnknownIssue_Exists**

**Purpose**: Detect references to unknown/invalid issues

**Record Type**: Various (all types with issue references)

**Fields Required**:
- Issue reference fields
- Related issue links

**Validation Logic**:
```
IF issue is referenced
   AND issue does not exist in RQ1 system
THEN
   Violation: "Unknown issue referenced"
   Severity: WARNING
```

---

#### **Rule_UnknownProject_Exists**

**Purpose**: Detect references to unknown/invalid projects

**Record Type**: Various (all types with project references)

**Fields Required**:
- Project reference fields
- Related project links

**Validation Logic**:
```
IF project is referenced
   AND project does not exist in RQ1 system
THEN
   Violation: "Unknown project referenced"
   Severity: WARNING
```

---

<a name="account-rules"></a>
### 1??5?? ACCOUNT - 1 Rule

---

#### **Rule_AccountNumberFormat**

**Purpose**: Validate account number format

**Record Type**: Account-related records

**Fields Required**:
- `ACCOUNT_NUMBER` (text)

**Pattern**: TBD (specific format not fully documented)

**Validation Logic**:
```
IF ACCOUNT_NUMBER does not match required format
THEN
   Violation: "Invalid account number format"
   Severity: WARNING
```

---

<a name="patterns"></a>
## ?? COMMON PATTERNS

### Pattern 1: Required Field Validation (15 rules)

**Template**:
```python
def check_required_field(record: dict, field_name: str) -> dict:
    """Check if required field has value"""
    value = record.get(field_name)
    
    if not value or (isinstance(value, str) and value.strip() == ''):
        return {
            'compliant': False,
            'violation': f'{field_name} is required but missing'
        }
    
    return {'compliant': True}
```

**Rules using this pattern**:
- Rule_IssueSW_FmeaCheck
- Rule_IssueSW_MissingAffectedIssueComment
- Rule_CheckForMissing_BaselineLink
- And 12 more...

---

### Pattern 2: Cross-Record Validation (10 rules)

**Template**:
```python
def check_cross_record(parent: dict, children: list[dict]) -> dict:
    """Validate consistency across related records"""
    violations = []
    
    # Get parent attributes
    parent_value = parent.get('some_field')
    
    # Check each child
    for child in children:
        child_value = child.get('some_field')
        
        if not is_compatible(parent_value, child_value):
            violations.append({
                'child_rq1': child.get('rq1_number'),
                'issue': f'Mismatch between parent and child'
            })
    
    return {
        'compliant': len(violations) == 0,
        'violations': violations
    }
```

**Rules using this pattern**:
- Rule_IssueSW_ASIL
- Rule_CheckDatesForBcAndFc
- All IRM rules
- All RRM rules
- And more...

---

### Pattern 3: Date Logic Validation (8 rules)

**Template**:
```python
from datetime import datetime

def check_date_logic(record1: dict, record2: dict) -> dict:
    """Validate date consistency between records"""
    date1 = datetime.fromisoformat(record1.get('date_field1'))
    date2 = datetime.fromisoformat(record2.get('date_field2'))
    
    violations = []
    
    if date1 > date2:  # Example: date1 should be before date2
        violations.append({
            'type': 'DATE_ORDER_VIOLATION',
            'date1': date1.isoformat(),
            'date2': date2.isoformat(),
            'message': 'Date1 should be before Date2'
        })
    
    return {
        'compliant': len(violations) == 0,
        'violations': violations
    }
```

**Rules using this pattern**:
- Rule_CheckDatesForBcAndFc
- Rule_Bc_CheckPstDates
- Rule_Fc_PlannedDate
- Rule_Fc_ReqDate
- Rule_CheckDatesForPvar
- Rule_CheckDatesForPver
- Rule_Rrm_Bc_Fc_DeliveryDate
- Rule_Rrm_Pst_Bc_DeliveryDate

---

### Pattern 4: Pattern Matching (5 rules)

**Template**:
```python
import re

def check_pattern_match(value: str, pattern: str) -> dict:
    """Validate value matches regex pattern"""
    if not re.match(pattern, value):
        return {
            'compliant': False,
            'violation': f'Value does not match pattern',
            'current_value': value,
            'expected_pattern': pattern
        }
    
    return {'compliant': True}
```

**Rules using this pattern**:
- Rule_Bc_NamingConvention (BC naming)
- Rule_Fc_NamingConvention (FC naming)
- Rule_CheckForMissing_BaselineLink (DOORS URL pattern)
- Rule_AccountNumberFormat (account number format)
- Rule_ToDo_InternalComment (TODO keyword)
- Rule_Info_InternalComment (INFO keyword)

---

### Pattern 5: State-Based Validation (12 rules)

**Template**:
```python
def check_state_based_rule(record: dict) -> dict:
    """Check rule only for specific lifecycle states"""
    state = record.get('LIFE_CYCLE_STATE')
    
    # Define states to check
    states_to_check = ['DEVELOPED', 'CLOSED']
    states_to_skip = ['CANCELED', 'CONFLICTED', 'NEW']
    
    # Skip if in excluded state
    if state in states_to_skip:
        return {'compliant': True, 'reason': 'State excluded from check'}
    
    # Only check if in specific states
    if state not in states_to_check:
        return {'compliant': True, 'reason': 'State not applicable'}
    
    # Perform actual validation
    # ...
    
    return validation_result
```

**Common States**:
- NEW - Initial state
- EVALUATED - Under evaluation
- COMMITTED - Committed to implementation
- IMPLEMENTED - Implementation complete
- DEVELOPED - Development complete
- CLOSED - Closed/complete
- CANCELED - Canceled
- CONFLICTED - Has conflicts

---

<a name="fields"></a>
## ?? FIELD REFERENCE

### Most Frequently Used Fields

| Field Name | Data Type | Usage Count | Description |
|------------|-----------|-------------|-------------|
| LIFE_CYCLE_STATE | enum | 30+ | Current state of record |
| SUBMIT_DATE | date | 20+ | Record submission date |
| INTERNAL_COMMENT | text | 15+ | Internal notes/comments |
| DESCRIPTION | text | 12+ | Record description |
| ASIL_CLASSIFICATION | enum | 5 | Safety integrity level |
| TAGLIST | text | 8 | Tags/keywords |
| DELIVERY_DATE | date | 8 | Delivery/completion date |
| PLANNED_DATE | date | 6 | Planned completion date |
| REQ_DATE | date | 4 | Request date |
| NAME/TITLE | text | 10+ | Record name |
| FMEA_STATE | enum | 2 | FMEA status |
| FMEA_COMMENT | text | 2 | FMEA comment |
| SEVERITY | enum | 3 | Issue severity |
| AFFECTED_ISSUE_COMMENT | text | 1 | Comment for affected issues |
| PILOT_DERIVATIVE | text | 3 | Pilot configuration |
| CHILDREN | relationship | 5+ | Child records |
| PARENT | relationship | 5+ | Parent record |

### Field Data Types

**Enum Fields**:
- `LIFE_CYCLE_STATE`: NEW, EVALUATED, COMMITTED, IMPLEMENTED, DEVELOPED, CLOSED, CANCELED, CONFLICTED
- `ASIL_CLASSIFICATION`: QM, ASIL_A, ASIL_B, ASIL_C, ASIL_D, ASIL_A_B, ASIL_A_C, ASIL_A_D, ASIL_B_C, ASIL_B_D, ASIL_C_D
- `FMEA_STATE`: Required, Not Required, (others)
- `SEVERITY`: (varies by record type)

**Date Fields**:
- Format: ISO 8601 (YYYY-MM-DD)
- Timezone: UTC or local (depends on RQ1 config)

**Text Fields**:
- Max length: Varies by field
- Can contain: Plain text, URLs, formatted text

**Relationship Fields**:
- Type: Record references
- Format: RQ1 number or internal ID
- Cardinality: One-to-one, one-to-many, many-to-many

---

<a name="implementation"></a>
## ?? IMPLEMENTATION GUIDE

### Phase 2A: Top 10 Priority Rules (30-40 hours)

#### Step 1: Extend rq1_client.py

Add validation methods:

```python
# In rq1_client.py

from typing import Dict, List, Optional
from datetime import datetime
import re

class RQ1Client:
    # ... existing methods ...
    
    # ============ VALIDATION METHODS ============
    
    def validate_issue_sw(self, rq1_number: str, rules: Optional[List[str]] = None) -> Dict:
        """
        Validate Issue SW against specified rules
        
        Args:
            rq1_number: RQ1 number of Issue SW
            rules: List of rule names to check (None = check all)
        
        Returns:
            {
                'rq1_number': str,
                'record_type': str,
                'compliant': bool,
                'violations': List[dict]
            }
        """
        # Get issue data
        issue = self.get_record_by_rq1_number(rq1_number)
        
        violations = []
        
        # Rule 1: FMEA Check
        if not rules or 'FmeaCheck' in rules:
            fmea_result = self._check_fmea_comment(issue)
            if not fmea_result['compliant']:
                violations.append(fmea_result['violation'])
        
        # Rule 2: ASIL Check
        if not rules or 'ASIL' in rules:
            asil_result = self._check_asil_levels(issue)
            if not asil_result['compliant']:
                violations.append(asil_result['violation'])
        
        # Rule 3: Affected Issue Comment
        if not rules or 'MissingAffectedIssueComment' in rules:
            affected_result = self._check_affected_issue_comment(issue)
            if not affected_result['compliant']:
                violations.append(affected_result['violation'])
        
        return {
            'rq1_number': rq1_number,
            'record_type': 'IssueSW',
            'compliant': len(violations) == 0,
            'violations': violations
        }
    
    def validate_bc(self, rq1_number: str, rules: Optional[List[str]] = None) -> Dict:
        """Validate BC record against rules"""
        bc = self.get_record_by_rq1_number(rq1_number)
        violations = []
        
        # Rule 4: BC Naming Convention
        if not rules or 'Bc_NamingConvention' in rules:
            naming_result = self._check_bc_naming(bc)
            if not naming_result['compliant']:
                violations.append(naming_result['violation'])
        
        # Rule 8: BC without PST link
        if not rules or 'Bc_WithoutLinkToPst' in rules:
            pst_link_result = self._check_bc_pst_link(bc)
            if not pst_link_result['compliant']:
                violations.append(pst_link_result['violation'])
        
        # Rule 10: BC PST Dates
        if not rules or 'Bc_CheckPstDates' in rules:
            pst_dates_result = self._check_bc_pst_dates(bc)
            if not pst_dates_result['compliant']:
                violations.append(pst_dates_result['violation'])
        
        # Rule 3: Baseline Link
        if not rules or 'CheckForMissing_BaselineLink' in rules:
            baseline_result = self._check_baseline_link(bc)
            if not baseline_result['compliant']:
                violations.append(baseline_result['violation'])
        
        return {
            'rq1_number': rq1_number,
            'record_type': 'BC',
            'compliant': len(violations) == 0,
            'violations': violations
        }
    
    # ============ PRIVATE VALIDATION HELPERS ============
    
    def _check_fmea_comment(self, issue: Dict) -> Dict:
        """Rule: FMEA comment required when state = 'Not Required'"""
        fmea_state = issue.get('FMEA_STATE')
        fmea_comment = issue.get('FMEA_COMMENT', '').strip()
        fmea_change_comment = issue.get('FMEA_CHANGE_COMMENT', '').strip()
        submit_date = datetime.fromisoformat(issue.get('SUBMIT_DATE'))
        state = issue.get('LIFE_CYCLE_STATE')
        
        # Skip if before 2015 or in excluded states
        if submit_date < datetime(2015, 1, 1):
            return {'compliant': True, 'reason': 'Before 2015-01-01'}
        
        if state in ['CANCELED', 'CONFLICTED']:
            return {'compliant': True, 'reason': 'Excluded state'}
        
        # Check rule
        if fmea_state == 'Not Required':
            if not fmea_comment and not fmea_change_comment:
                return {
                    'compliant': False,
                    'violation': {
                        'rule': 'Rule_IssueSW_FmeaCheck',
                        'severity': 'WARNING',
                        'message': 'Comment for not required FMEA missing',
                        'fields': ['FMEA_COMMENT', 'FMEA_CHANGE_COMMENT'],
                        'current_values': {
                            'FMEA_STATE': fmea_state,
                            'FMEA_COMMENT': fmea_comment,
                            'FMEA_CHANGE_COMMENT': fmea_change_comment
                        }
                    }
                }
        
        return {'compliant': True}
    
    def _check_asil_levels(self, issue: Dict) -> Dict:
        """Rule: ASIL level matching between parent and children"""
        asil = issue.get('ASIL_CLASSIFICATION')
        state = issue.get('LIFE_CYCLE_STATE')
        
        # Skip if in excluded states
        if state in ['NEW', 'CLOSED', 'CANCELED', 'CONFLICTED']:
            return {'compliant': True, 'reason': 'Excluded state'}
        
        # QM doesn't require check
        if asil == 'QM':
            return {'compliant': True, 'reason': 'QM level'}
        
        # Get required child ASIL levels
        required_levels = self._get_required_asil_levels(asil)
        
        # Get children (this would need to be implemented based on API)
        children = self._get_issue_children(issue.get('rq1_number'))
        
        # Check if at least one child has required level
        for child in children:
            if child.get('ASIL_CLASSIFICATION') in required_levels:
                return {'compliant': True}
        
        return {
            'compliant': False,
            'violation': {
                'rule': 'Rule_IssueSW_ASIL',
                'severity': 'WARNING',
                'message': f'No I-FD found with required ASIL level',
                'parent_asil': asil,
                'required_levels': list(required_levels),
                'children_found': [
                    {'rq1': c.get('rq1_number'), 'asil': c.get('ASIL_CLASSIFICATION')}
                    for c in children
                ]
            }
        }
    
    def _get_required_asil_levels(self, parent_asil: str) -> set:
        """Get required child ASIL levels based on parent"""
        mapping = {
            'ASIL_A': {'ASIL_A', 'ASIL_B', 'ASIL_C', 'ASIL_D'},
            'ASIL_B': {'ASIL_B', 'ASIL_C', 'ASIL_D'},
            'ASIL_C': {'ASIL_C', 'ASIL_D'},
            'ASIL_D': {'ASIL_D'},
            'ASIL_A_B': {'ASIL_A', 'ASIL_B', 'ASIL_C', 'ASIL_D'},
            'ASIL_A_C': {'ASIL_A', 'ASIL_C', 'ASIL_D'},
            'ASIL_A_D': {'ASIL_A', 'ASIL_D'},
            'ASIL_B_C': {'ASIL_B', 'ASIL_C', 'ASIL_D'},
            'ASIL_B_D': {'ASIL_B', 'ASIL_D'},
            'ASIL_C_D': {'ASIL_C', 'ASIL_D'},
        }
        return mapping.get(parent_asil, set())
    
    def _check_bc_naming(self, bc: Dict) -> Dict:
        """Rule: BC naming convention"""
        name = bc.get('NAME', bc.get('TITLE', ''))
        pattern = r'^BC[0-9]{5}[A-Z]{2}$'
        
        if not re.match(pattern, name):
            return {
                'compliant': False,
                'violation': {
                    'rule': 'Rule_Bc_NamingConvention',
                    'severity': 'WARNING',
                    'message': 'BC name does not match naming convention',
                    'current_name': name,
                    'expected_pattern': pattern,
                    'example': 'BC12345AB'
                }
            }
        
        return {'compliant': True}
    
    def _check_baseline_link(self, record: Dict) -> Dict:
        """Rule: DOORS baseline link required"""
        doors_pattern = r'(doors://)|(https?://.*dwa/rm/urn:rational)'
        
        # Check all relevant fields
        fields = {
            'TAGLIST': record.get('TAGLIST', ''),
            'DESCRIPTION': record.get('DESCRIPTION', ''),
            'INTERNAL_COMMENT': record.get('INTERNAL_COMMENT', '')
        }
        
        for field_name, field_value in fields.items():
            if re.search(doors_pattern, field_value):
                return {'compliant': True}
        
        # Check trigger conditions
        state = record.get('LIFE_CYCLE_STATE')
        submit_date = datetime.fromisoformat(record.get('SUBMIT_DATE'))
        
        if state in ['DEVELOPED', 'CLOSED'] and submit_date >= datetime(2019, 1, 1):
            return {
                'compliant': False,
                'violation': {
                    'rule': 'Rule_CheckForMissing_BaselineLink',
                    'severity': 'WARNING',
                    'message': 'Link to DOORS baseline is missing',
                    'fields_checked': list(fields.keys()),
                    'expected_pattern': 'doors:// or https://.../dwa/rm/urn:rational'
                }
            }
        
        return {'compliant': True, 'reason': 'Not applicable'}
```

#### Step 2: Add MCP Tool

Add to `rq1_server.py`:

```python
@mcp.tool()
def validate_rq1_compliance(
    rq1_number: str,
    record_type: str = "auto",
    rules: list[str] | None = None
) -> dict:
    """
    Validate RQ1 record against compliance rules.
    
    Args:
        rq1_number: RQ1 number to validate (e.g., RQ100123456, BC12345AB)
        record_type: Record type (auto, IssueSW, BC, FC, etc.)
        rules: Optional list of specific rules to check
    
    Returns:
        Validation result with compliance status and violations
    
    Example:
        validate_rq1_compliance("RQ100123456", "IssueSW")
        validate_rq1_compliance("BC12345AB", "BC", ["Bc_NamingConvention"])
    """
    try:
        client = _get_client()
        
        # Auto-detect record type if needed
        if record_type == "auto":
            if rq1_number.startswith("BC"):
                record_type = "BC"
            elif rq1_number.startswith("FC"):
                record_type = "FC"
            else:
                record_type = "IssueSW"
        
        # Validate based on record type
        if record_type == "IssueSW":
            result = client.validate_issue_sw(rq1_number, rules)
        elif record_type == "BC":
            result = client.validate_bc(rq1_number, rules)
        elif record_type == "FC":
            result = client.validate_fc(rq1_number, rules)
        else:
            return {
                'error': f'Unsupported record type: {record_type}',
                'supported_types': ['IssueSW', 'BC', 'FC']
            }
        
        return result
        
    except Exception as e:
        return {
            'error': str(e),
            'rq1_number': rq1_number,
            'record_type': record_type
        }
```

#### Step 3: Usage Examples

```python
# In AI assistant chat:

# Example 1: Validate Issue SW
result = validate_rq1_compliance("RQ100123456", "IssueSW")
# Returns: {'compliant': False, 'violations': [...]}

# Example 2: Check only specific rules
result = validate_rq1_compliance(
    "RQ100123456",
    "IssueSW",
    ["FmeaCheck", "ASIL"]
)

# Example 3: Validate BC
result = validate_rq1_compliance("BC12345AB", "BC")

# Example 4: Auto-detect type
result = validate_rq1_compliance("BC12345AB")  # Auto-detects BC
```

### Phase 2B: Medium Priority Rules (15 rules, 40-50 hours)

Continue with remaining BC, FC, IRM, date validation rules following same pattern.

### Phase 2C: Low Priority Rules (12 rules, 30-40 hours)

Implement ECU, PRG, RRM, INFO/TODO, existence rules.

---

## ?? EFFORT ESTIMATION

| Phase | Rules | Effort | Complexity |
|-------|-------|--------|------------|
| 2A - Top 10 | 10 | 30-40h | High (cross-record) |
| 2B - Medium | 15 | 40-50h | Medium |
| 2C - Low | 12 | 30-40h | Low |
| **Total** | **37** | **100-130h** | Mixed |

### Per-Rule Average: 2.7-3.5 hours

Breakdown:
- Analysis: 30 min
- Implementation: 1-2 hours
- Testing: 30 min
- Documentation: 30 min

---

## ?? NEXT STEPS

1. ? Review this documentation
2. ? Confirm top 10 priority rules
3. ? Get RQ1 whitelist approval for PQA_Agent
4. ? Test Phase 1 methods with real data
5. ? Implement Phase 2A (top 10 rules)
6. ? Test validation with known violations
7. ? Iterate based on feedback

---

## ?? REFERENCES

- POST Tool source: `rq1/POST_extracted/DataModel/Rq1/Monitoring/`
- building-block-rq1 docs: `BUILDING_BLOCK_RQ1_REFERENCE.md`
- RQ1 OSLC API: RQ1 system documentation
- Quick reference: `RQ1_RULES.md`

---

**End of Document**
