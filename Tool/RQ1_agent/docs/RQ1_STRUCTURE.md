# RQ1 Data Structure & Relationships

## Hierarchy

```
Project (RQONE00001940)
??? Issue FD (IFD)
?   ??[IRM]? BC/BX, FC
?
??? Issue SW (ISW)
?   ??[IRM]? PVER, BC
?   ?????? IFD (ISW maps to IFD directly)
?
??? Releases
    ??? PVER ?[RRM]? BC
    ??? BC/BX ?[RRM]? FC
    ??? FC
```

## Terminology

### Items
- **Project**: Highest level container (e.g., RQONE00001940)
- **Issue**: Problem/requirement to be solved
  - **Issue FD (IFD)**: Function Description issue
  - **Issue SW (ISW)**: Software issue
- **Release**: Delivery package
  - **PVER**: Platform Version
  - **BC/BX**: Base Component
  - **FC**: Function Component
- **Workitem**: Generic term for any Issue/Release

### Relationships
- **IRM (Issue Release Map)**: Maps between Issue and Release
  - IFD ? BC/BX, FC
  - ISW ? PVER, BC
  
- **RRM (Release Release Map)**: Maps between Release and Release
  - PVER ? BC
  - BC ? FC

### Mapping Rules
- 1 Issue can map to multiple Releases (and vice versa)
- 1 Release can map to multiple Releases (via RRM)
- ISW maps to IFD directly (not via IRM/RRM)

## Workflow Example
```
ISW ? PVER (via IRM) ? BC (via RRM) ? FC (via RRM)
IFD ? BC, FC (via IRM)
```

## States to Skip
- **Canceled/Cancelled**: Skip validation for these items

---
**Note**: Waiting for concrete examples to validate understanding...

## Real Examples

### Example 1: PVER to BC Mapping (RRM-PST-BC)
```
RQONE04762901 - PVER : MMD1204VPLC2055 / N3A5
      ? (RRM-PST-BC: 241847119)
RQONE04843969 - BC : ComVeh / 709.41.0
```

### Example 2: BC to FC Mapping (RRM-BC-FC)
```
RQONE04843969 - BC : ComVeh / 709.41.0
      ? (RRM-BC-FC: 242414960)
RQONE04879328 - FC-ARB : ComScl_NetMtrx / 709.309.0
```

### Example 3: BC to IFD Mapping (IRM-BC-ISSUE_FD)
```
RQONE04843969 - BC : ComVeh / 709.41.0
      ? (IRM-BC-ISSUE_FD: 238009664)
RQONE04660700 - Robustheitsmaßnahme für COMScl 10ms - E3 - BC: ComVeh 709.x.x (IFD)
```

### Example 4: ISW to PVER Mapping (IRM-PST-ISSUE_SW)
```
RQONE04642535 - Robustheitsmaßnahme für COMScl 10ms (ISW)
      ? (IRM-PST-ISSUE_SW: 239123833)
RQONE04762901 - PVER : MMD1204VPLC2055 / N3A5
```

### Example 5: FC to IFD Mapping (IRM-FC-ISSUE_FD)
```
RQONE04660700 - Robustheitsmaßnahme für COMScl 10ms - E3 - BC: ComVeh 709.x.x (IFD)
      ? (IRM-FC-ISSUE_FD: 242414909)
RQONE04879328 - FC-ARB : ComScl_NetMtrx / 709.309.0
```

### Example 6: Workitems (WI) - 4 Real Examples

**Pattern Discovery**: Workitems have 2 main categories:

#### A. Release-based Workitems (Category="Release")
```
WI1: RQONE04853587
  Title: Analyse Specification of Test Cases for SIRTCom
  Category: Release
  SubCategory: BC  ? Parent is BC release
  BelongsToRelease: YES (PVER RQONE04762901)
  BelongsToIssue: NO
  
WI2: RQONE04801359
  Title: PL3A5: Post Delivery WI: Create HSM Updater
  Category: Release
  SubCategory: PVER  ? Parent is PVER release
  BelongsToRelease: YES (BC RQONE04843969)
  BelongsToIssue: NO
```

#### B. Issue-based Workitems (Category="Issue")
```
WI3: RQONE04675098
  Title: Additional Review of Implementation - Robustheitsmaßnahme...
  Category: Issue
  SubCategory: Issue FD  ? Parent is IFD
  BelongsToRelease: NO
  BelongsToIssue: YES (IFD RQONE04660700)

WI4: RQONE04831243
  Title: [CRM] Analyse and Clarify the Change Request...
  Category: Issue
  SubCategory: Issue SW  ? Parent is ISW
  BelongsToRelease: NO
  BelongsToIssue: YES (ISW)
```

**Key Rules**:
1. **Category** field indicates workitem type:
   - `Category="Release"` ? belongs to a Release (PVER/BC/FC)
   - `Category="Issue"` ? belongs to an Issue (IFD/ISW)

2. **SubCategory** field indicates **parent object type**:
   - For Release WI: subcategory = PVER/BC/FC (which release type)
   - For Issue WI: subcategory = "Issue FD"/"Issue SW" (which issue type)

3. **Relationship fields**:
   - Release WI: `belongstorelease` is populated
   - Issue WI: `belongstoissue` is populated

**NOTE**: Initially I misunderstood - subcategory DOES correctly indicate the parent type!


