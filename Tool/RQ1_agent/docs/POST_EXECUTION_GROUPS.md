# POST Tool Rule Execution Groups

**Discovery**: POST tool groups rules by **execution context**, not by category (QAM/BBM/IPT)

---

## RuleExecutionGroup Enum (from POST tool)

```java
public enum RuleExecutionGroup {
    DATA_STORE              // Data store validation
    RQ1_DATA                // RQ1 data validation  
    ELEMENT_INTEGRITY       // Field integrity checks
    UI_VISIBLE              // Visible in UI
    
    // Planning views
    PVER_PLANNING_VIEW      // PVER planning
    PVAR_PLANNING_VIEW      // PVAR planning
    BC_PLANNING             // BC planning
    FC_PLANNING             // FC planning
    I_SW_PLANNING           // Issue-SW planning
    I_FD_PLANNING           // Issue-FD planning
    I_FD_PILOT              // Issue-FD pilot checks
    
    // Special groups
    PROJECT                 // Project consistency
    PROPLATO                // ProPlaTo requirements
    PROPLATO_BACKCHANNEL    // ProPlaTo BackChannel
    EXCITED_CPC             // excITED CPC rules
    TEST                    // Automatic tests
}
```

---

## Key Insight: Multi-Group Rules

**Rules can belong to MULTIPLE execution groups**

### Example: Rule_Bc_Close

```java
EnumSet.of(
    RuleExecutionGroup.ELEMENT_INTEGRITY,  // Field integrity
    RuleExecutionGroup.EXCITED_CPC,        // CPC compliance
    RuleExecutionGroup.BC_PLANNING         // BC planning context
)
```

This means:
- Rule executes when checking **BC planning**
- Rule executes when checking **element integrity**
- Rule executes when checking **excITED CPC compliance**

---

## Rule Distribution by Execution Group

### BC_PLANNING (BC Planning Rules)
```
Rule_Bc_Close                    ? QAM ID-2.5.0
Rule_Bc_CheckPstDates
Rule_Bc_NamingConvention         ? PRPL 20.00.00
Rule_Bc_WithoutLinkToPst         ? PRPL 18.00.00
Rule_Release_Predecessor
Rule_Rrm_Bc_Fc_DeliveryDate
Rule_Rrm_Pst_Bc_DeliveryDate
```

### FC_PLANNING (FC Planning Rules)
```
Rule_Fc_WithoutLinkToBc          ? PRPL 17.00.00
Rule_Fc_NamingConvention         ? PRPL 19.00.00
Rule_Fc_PlannedDate              ? PRPL 08.00.00
Rule_Fc_ReqDate                  ? PRPL 07.00.00
Rule_CheckDatesForBcAndFc        ? QAM + PRPL 10.00.00
Rule_Release_Predecessor
Rule_Rrm_Bc_Fc_DeliveryDate
```

### I_SW_PLANNING (Issue-SW Planning Rules)
```
Rule_IssueSW_ASIL                ? BBM 13
Rule_IssueSW_FmeaCheck           ? BBM 12
Rule_IssueSW_MissingAffectedIssueComment ? BBM 15
```

### I_FD_PLANNING (Issue-FD Planning Rules)
```
Rule_IssueFD_WithoutLinkToBc     ? QADO 01.00.00
Rule_IssueFD_Pilot
```

### ELEMENT_INTEGRITY (Field Integrity Rules)
```
Rule_CheckForMissing_BaselineLink ? BBM 10
Rule_UnknownIssue_Exists
Rule_UnknownProject_Exists
Rule_Info_InternalComment
Rule_ToDo_InternalComment
Rule_Irm_Prg_HintForExclude
Rule_Irm_Prg_IssueSw_PilotSet
Rule_Rrm_Pst_Bc_HintForExclude
Rule_Rrm_Pver_Bc_PilotSet
Rule_Irm_OnlyOnePilotDerivative
Rule_Pvar_Derivatives
Rule_Pver_Derivatives
Rule_Prg_Lumpensammler
```

### EXCITED_CPC (excITED CPC Rules)
```
Rule_Bc_Close                    ? QAM ID-2.5.0
Rule_IssueSW_ASIL                ? BBM 13
```

### UI_VISIBLE (User Interface Visible Rules)
```
Rule_IssueFD_WithoutLinkToBc
Rule_IssueSW_FmeaCheck
Rule_IssueSW_MissingAffectedIssueComment
Rule_Irm_PstRelease_IssueSw_Severity
Rule_Pver_NotSuitableForINMA
```

---

## Execution Strategy

### POST Tool Approach
```
User selects validation scope:
?? "Validate BC" ? Execute BC_PLANNING group
?? "Validate FC" ? Execute FC_PLANNING group  
?? "Validate Issue-SW" ? Execute I_SW_PLANNING group
?? "Check All" ? Execute ELEMENT_INTEGRITY group
?? "excITED CPC" ? Execute EXCITED_CPC group
```

### Python Tool Should Implement
```python
class RuleExecutionContext(Enum):
    BC_PLANNING = "BC Planning"
    FC_PLANNING = "FC Planning"
    I_SW_PLANNING = "Issue-SW Planning"
    I_FD_PLANNING = "Issue-FD Planning"
    ELEMENT_INTEGRITY = "Element Integrity"
    EXCITED_CPC = "excITED CPC"
    ALL = "All Rules"

# Usage:
validator.run_rules(
    context=RuleExecutionContext.BC_PLANNING,
    records=[bc_release1, bc_release2]
)
```

---

## Mapping: Excel Categories ? Execution Groups

### QAM Rules
- Primarily in: **EXCITED_CPC**, **BC_PLANNING**, **FC_PLANNING**
- Focus: Process gates, compliance

### BBM Rules  
- Primarily in: **I_SW_PLANNING**, **ELEMENT_INTEGRITY**
- Focus: Safety, traceability, FMEA/ASIL

### IPT Rules
- Primarily in: **BC_PLANNING**, **FC_PLANNING**, **ELEMENT_INTEGRITY**
- Focus: Naming, dates, linkage

---

## Recommended Python Implementation

### Option 1: Match POST Structure (Execution Groups)
```python
# Group rules by execution context
rules_registry = {
    "BC_PLANNING": [
        Rule_Bc_Close(),
        Rule_Bc_NamingConvention(),
        Rule_Bc_WithoutLinkToPst(),
        # ...
    ],
    "FC_PLANNING": [
        Rule_Fc_NamingConvention(),
        Rule_Fc_WithoutLinkToBc(),
        # ...
    ],
    "I_SW_PLANNING": [
        Rule_IssueSW_ASIL(),
        Rule_IssueSW_FmeaCheck(),
        # ...
    ]
}
```

**Advantages:**
- ? Matches POST tool logic exactly
- ? Easy to execute rules by context
- ? Natural workflow (validate BC ? run BC rules)

### Option 2: Hybrid (Categories + Execution Groups)
```python
# Tag rules with both category AND execution groups
@rule_metadata(
    category="QAM",  # Documentation category
    excel_id="QAM ID-2.5.0",
    execution_groups=["BC_PLANNING", "EXCITED_CPC", "ELEMENT_INTEGRITY"]
)
class Rule_Bc_Close(BaseRule):
    pass
```

**Advantages:**
- ? Maintains Excel documentation mapping
- ? Supports execution by context
- ? Flexible filtering/reporting

---

## Conclusion

**Key Finding**: POST tool doesn't organize by QAM/BBM/IPT categories. It organizes by **execution context** (what you're validating).

**Recommendation**: Implement **execution groups** like POST tool, but also **tag with Excel categories** (QAM/BBM/IPT) for traceability and reporting.

**Best of both worlds:**
- Execute rules by context (like POST)
- Report by category (QAM/BBM/IPT compliance)
- Maintain Excel documentation mapping