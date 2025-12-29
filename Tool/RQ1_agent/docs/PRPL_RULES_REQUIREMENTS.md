# PRPL Rules Requirements Analysis

## Overview
This document defines the requirements, data needs, and implementation approach for each PRPL rule.

**Status**: 4/12 implemented, 8 remaining

---

## ? IMPLEMENTED RULES

### PRPL 02 - Workitem Planned Date
**Status**: ? Implemented (`rule_prpl_02_workitem_planned.py`)

**Rule**: Workitem is in started state, but planned date for workitem is not entered in planning tab

**Data Required**:
- Workitem.lifecyclestate
- Workitem.planneddate

**Logic**:
```
IF workitem.lifecyclestate == "Started" AND workitem.planneddate IS NULL
THEN violation
```

---

### PRPL 12 - IFD-BC Closure
**Status**: ? Implemented (`rule_prpl_12_ifd_bc_closure.py`)

**Rule**: IFD is not closed, even though all the BC-Rs mapped to it are closed or cancelled

**Data Required**:
- Issue (IFD type only, using cq__Type == "Issue FD")
- Issue.lifecyclestate
- Issuereleasemap (to get BC mappings)
- Release (BC type, using cq__Type == "BC")
- Release.lifecyclestate

**Logic**:
```
FOR each IFD issue:
  Get all mapped BC releases via Issuereleasemap
  IF all BCs are (Closed OR Cancelled) AND IFD is NOT (Closed OR Cancelled OR Implemented)
  THEN violation
```

---

### PRPL 13 - IFD-BC Planned Date
**Status**: ? Implemented (`rule_prpl_13_ifd_bc_planned.py`)

**Rule**: IFD is not implemented or closed, after planned dated of BC-R

**Data Required**:
- Issue (IFD type, cq__Type == "Issue FD")
- Issue.lifecyclestate
- Issuereleasemap
- Release (BC type, cq__Type == "BC")
- Release.planneddate
- Current date

**Logic**:
```
FOR each IFD issue:
  Get all mapped BCs with planned dates
  Find latest BC planned date
  IF current_date > latest_bc_planned_date AND IFD is NOT (Implemented OR Closed OR Cancelled)
  THEN violation
```

---

### PRPL 16 - Workitem Closure
**Status**: ? Implemented (`rule_prpl_16_workitem_close.py`)

**Rule**: Workitem is not closed after planned date

**Data Required**:
- Workitem.lifecyclestate
- Workitem.planneddate
- Current date

**Logic**:
```
IF workitem.planneddate < current_date AND workitem.lifecyclestate NOT IN (Closed, Canceled, Implemented)
THEN violation
```

---

## ? TO BE IMPLEMENTED

### PRPL 01 - BC-R Requested State (8 weeks before PVER)
**Status**: ? Not implemented

**Rule**: BC-R is not in requested state, 8 weeks before PVER planned delivery date

**Data Required**:
- Release (BC type, cq__Type == "BC")
- Release.lifecyclestate
- Release.planneddate (c?a BC)
- ReleaseRequirementMap (to get PVER/PVAR mappings) ?? NEED TO INVESTIGATE
- Release (PVER/PVAR type, cq__Type == "PVER" or "PVAR")
- Release.planneddate (c?a PVER/PVAR - "planned delivery date")
- Current date

**Logic**:
```
FOR each BC release:
  Get all mapped PVER/PVAR releases via ReleaseRequirementMap
  FOR each PVER/PVAR:
    Calculate: pver_planned_date - 8 weeks
    IF current_date >= (pver_planned_date - 8 weeks) AND BC.lifecyclestate != "Requested"
    THEN violation
```

**Questions**:
1. ? Có ReleaseRequirementMap trong RQ1 API không? (t??ng t? Issuereleasemap)
2. ? "Requested" state tên chính xác là gì? (Requested, Request, In Request?)
3. ? Có c?n check c? BX type không? (gi?ng PRPL 07/12/13)

---

### PRPL 03 - Conflicted State
**Status**: ? Not implemented

**Rule**: Issue/Release/workitem is still in "Conflicted" state

**Data Required**:
- Issue.lifecyclestate
- Release.lifecyclestate
- Workitem.lifecyclestate

**Logic**:
```
FOR each item (Issue, Release, Workitem):
  IF item.lifecyclestate == "Conflicted"
  THEN violation
```

**Questions**:
1. ? Có c?n threshold time không? (e.g., conflicted > 5 days)
2. ? Có lo?i item nào ???c exempt không?

**Note**: Simple rule, straightforward implementation

---

### PRPL 06 - Defect Detection/Injection Attributes
**Status**: ? Not implemented

**Rule**: Not all fields for defect detection/injection attributes in a Bug Fix Issue (IFD) are filled

**Data Required**:
- Issue (IFD type, cq__Type == "Issue FD")
- Issue.??? (defect detection fields) ?? NEED TO IDENTIFY FIELDS
- Issue.??? (defect injection fields) ?? NEED TO IDENTIFY FIELDS

**Logic**:
```
FOR each IFD that is a Bug Fix:
  Check required defect attributes are filled:
    - Detection phase/activity
    - Injection phase/activity
    - Root cause
    - [other fields???]
  IF any required field is NULL or empty
  THEN violation
```

**Questions**:
1. ? Làm sao identify "Bug Fix Issue"? (category? classification? another field?)
2. ? Các field c? th? là gì? Check IssueProperty for:
   - defectclassification
   - defectrootcauseclass
   - defectiveworkproducttype
   - Others?
3. ? Có rule nào ?? bi?t field nào là mandatory không?

---

### PRPL 07 - BC-PVER/PVAR Date Check
**Status**: ?? Code exists (`rule_prpl_07_bc_pst_dates.py`) but not integrated

**Rule**: Planned date of BC later than requested delivery date of any mapped PVER or PVAR

**Data Required**:
- Release (BC/BX type, cq__Type == "BC" or "BX")
- Release.planneddate (c?a BC)
- ReleaseRequirementMap ?? NEED TO INVESTIGATE
- Release (PVER/PVAR type)
- Release.planneddate (c?a PVER/PVAR - "requested delivery date")

**Logic**:
```
FOR each BC/BX release:
  Get all mapped PVER/PVAR releases via ReleaseRequirementMap
  FOR each PVER/PVAR:
    IF bc.planneddate > pver.planneddate
    THEN violation
```

**Questions**:
1. ? ReleaseRequirementMap API có s?n không?
2. ? PVER vs PVAR có x? lý khác nhau không?

---

### PRPL 11 - IFD 5 Day SLA
**Status**: ? Not implemented

**Rule**: IFD 5 day SLA reached

**Data Required**:
- Issue (IFD type, cq__Type == "Issue FD")
- Issue.lifecyclestate
- Issue.createddate (ho?c submission date) ?? NEED TO IDENTIFY FIELD
- Current date

**Logic**:
```
FOR each IFD issue:
  Calculate: current_date - ifd.createddate
  IF days_open >= 5 AND ifd.lifecyclestate NOT IN (Closed, Cancelled, Implemented)
  THEN violation
```

**Questions**:
1. ? Field nào ch?a creation date? (createddate, dcterms__created, submitdate?)
2. ? 5 days = calendar days hay working days?
3. ? SLA start t? created date hay t? submitted/assigned date?

---

### PRPL 14 - IFD Commitment vs ISW
**Status**: ? Not implemented

**Rule**: IFD is not committed, eventhough attached Issue-SW is committed

**Data Required**:
- Issue (IFD type, cq__Type == "Issue FD")
- Issue (ISW type, cq__Type == "Issue SW")
- Issue.lifecyclestate (ho?c commitment status field) ?? NEED TO IDENTIFY
- Issue relationships (IFD ? ISW) ?? NEED TO INVESTIGATE API

**Logic**:
```
FOR each IFD issue:
  Get all attached ISW issues (via relationship API?)
  FOR each ISW:
    IF isw.is_committed == TRUE AND ifd.is_committed == FALSE
    THEN violation
```

**Questions**:
1. ? "Committed" là lifecyclestate hay separate field?
2. ? Làm sao query relationship gi?a IFD và ISW? (có IssueIssueMap không?)
3. ? "Attached" relationship type là gì?

---

### PRPL 15 - Release Closure (BC/FC)
**Status**: ? Not implemented

**Rule**: Release is not closed after planned date for BC and FC

**Data Required**:
- Release (BC/FC type, cq__Type == "BC" or "FC")
- Release.lifecyclestate
- Release.planneddate
- Current date

**Logic**:
```
FOR each BC or FC release:
  IF current_date > release.planneddate AND release.lifecyclestate NOT IN (Closed, Cancelled)
  THEN violation
```

**Questions**:
1. ? Có include BX type không?
2. ? Grace period? (e.g., 1 week after planned date)

**Note**: Similar to PRPL 16 but for Releases instead of Workitems

---

### PRPL 18 - IFD Commitment Delay (5+ days after ISW)
**Status**: ? Not implemented

**Rule**: I-FD not committed 5 or more working days after attached I-SW was committed

**Data Required**:
- Issue (IFD type, cq__Type == "Issue FD")
- Issue (ISW type, cq__Type == "Issue SW")
- Issue commitment status ?? NEED TO IDENTIFY FIELD
- Issue commitment date ?? NEED TO IDENTIFY FIELD
- Issue relationships (IFD ? ISW)
- Current date

**Logic**:
```
FOR each IFD issue:
  Get all attached ISW issues
  FOR each committed ISW:
    Calculate: working_days_between(isw.committed_date, current_date)
    IF working_days >= 5 AND ifd.is_committed == FALSE
    THEN violation
```

**Questions**:
1. ? Same as PRPL 14 - commitment field?
2. ? Same as PRPL 14 - IFD-ISW relationship API?
3. ? Working days calculation - có API/utility không?
4. ? Có exclude holidays không?

---

## ? DATA INVESTIGATION RESULTS

### High Priority - RESOLVED
1. **? Releasereleasemap** - C?n cho PRPL 01, 07
   - ? EXISTS: `from rq1.releasereleasemap import Releasereleasemap`
   - ? Properties: `hasmappedchildrelease`, `hasmappedparentrelease`
   - ? Usage: BC (child) ? PVER/PVAR (parent)
   - Query: `where=(ReleasereleasemapProperty.hasmappedchildrelease == reference(bc_uri))`

2. **? Issue Commitment Fields** - C?n cho PRPL 14, 18
   - ? Field name ch?a rõ - c?n test thêm
   - Possible: lifecyclestate có state "Committed"?
   - Commitment date field: ch?a tìm th?y

3. **? Issue-Issue Relationships** - C?n cho PRPL 14, 18
   - ? EXISTS: `from rq1.issueissuemap import Issueissuemap`
   - ? Properties: `hasmappedissue1`, `hasmappedissue2`
   - ? Usage: IFD ? ISW relationship
   - Query: `where=(IssueissuemapProperty.hasmappedissue1 == reference(ifd_uri))`

4. **? Defect Attributes** - C?n cho PRPL 06
   - ? EXISTS: Full set of defect fields
   - Fields found:
     * defectclassification
     * defectdetectiondate, defectdetectionlocation, defectdetectionorga, defectdetectionprocess
     * defectinjectiondate, defectinjectionorga
     * defectiveworkproducttype
     * defectrootcauseclass
     * relateddefectclass, relateddefectid
   - Need to identify: Which fields are mandatory for Bug Fix IFD?

### Medium Priority
5. **? Issue Creation/Submission Date** - C?n cho PRPL 11
   - ? EXISTS: `submitdate`
   - Format: datetime (e.g., "2011-04-29 12:20:20+00:00")
   - Usage: Calculate days since submission

6. **Lifecycle State Names** - C?n cho PRPL 01
   - Confirm exact name: "Requested" vs "Request" vs "In Request"

---

## NEXT STEPS

### Phase 1: Data Investigation (HIGH PRIORITY)
1. Search RQ1 API/models for:
   ```python
   - ReleaseRequirementMap
   - IssueIssueMap or similar
   - IssueProperty fields containing "commit"
   - IssueProperty fields containing "defect"
   ```

2. Test queries for relationships:
   - IFD ? ISW connections
   - BC ? PVER/PVAR connections

### Phase 2: Simple Rules First
After investigation, implement in order:
1. **PRPL 03** - Conflicted state (simplest, no dependencies)
2. **PRPL 15** - Release closure (similar to PRPL 16)
3. **PRPL 11** - IFD SLA (once we identify date field)

### Phase 3: Complex Rules
After Phase 2:
4. **PRPL 06** - Defect attributes (after field investigation)
5. **PRPL 14** - IFD-ISW commitment (after relationship API)
6. **PRPL 18** - IFD-ISW commitment delay (builds on PRPL 14)
7. **PRPL 01** - BC-PVER state (after RRM investigation)
8. **PRPL 07** - BC-PVER dates (integrate existing code with RRM)

---

## TEMPLATES FOR NEW RULES

### Rule File Template
```python
# src/rules/rule_prpl_XX_description.py

class Rule_PRPL_XX:
    def __init__(self, item_data, related_data=None):
        self.item = item_data
        self.related = related_data
    
    def execute(self):
        # Logic here
        passed = True
        description = ""
        
        if violation_condition:
            passed = False
            description = "Violation details..."
        
        return RuleResult(
            rule_id="PRPL XX",
            passed=passed,
            severity="WARNING",
            description=description
        )
```

### Integration in validate_user_items.py
```python
# Import
from rules.rule_prpl_XX_description import Rule_PRPL_XX

# In validation loop
ruleXX = Rule_PRPL_XX(item_data, related_data)
resultXX = ruleXX.execute()
total_checks += 1
if not resultXX.passed:
    violations.append({...})
```
