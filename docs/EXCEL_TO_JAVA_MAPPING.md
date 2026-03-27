# Excel to Java Rules Mapping

**Purpose**: Map official Excel rules to implemented Java rules from POST Tool
**Strategy**: Implement only rules with both Excel (official process) AND Java (proven code) backing

---

## Summary

**Excel Rules**: 101 total (BBM 23 + QAM 22 + IPT 56)
**Java Rules**: 37 total (from POST Tool v1.0.3)
**Intersection**: ~30-36 rules with both Excel + Java backing

### Priority for Implementation

**Top 10 Rules** (30-40 hours):
1. Rule_IssueSW_FmeaCheck
2. Rule_IssueSW_ASIL
3. Rule_CheckForMissing_BaselineLink
4. Rule_Bc_NamingConvention
5. Rule_Fc_NamingConvention
6. Rule_CheckDatesForBcAndFc
7. Rule_IssueFD_WithoutLinkToBc
8. Rule_Bc_WithoutLinkToPst
9. Rule_IssueSW_MissingAffectedIssueComment
10. Rule_Bc_CheckPstDates

All have:
- Excel backing (official process)
- Java implementation (proven code)
- IPT or mandatory execution level

---

## Mapping Groups

### Group 1: Issue-FD & FC Closure

**Excel Rules**:
- QADO 01.00.00: "FC in delivered state in SCM, but Issue-FD is not closed"
- Execution: new
- Relevant for PQA: yes

**Java Rules**:
- Rule_IssueFD_WithoutLinkToBc (Priority #7)
- Rule_Fc_Close (if exists)
- Rule_Bc_Close (BC closure validation)

**Implementation**: HIGH priority
- Check FC.STATE = "AVAILABLE"
- Check linked Issue-FD.STATE must be "CLOSED"

---

### Group 2: Naming Conventions

**Excel Rules**:
- BBM 10, 12, 16 (implicit in traceability requirements)
- PRPL 19.00.00: "Name Check Function (FC)"
- PRPL 20.00.00: "Name Check Package (BC)"
- Execution: optional
- Relevant for PQA: yes

**Java Rules**:
- Rule_Bc_NamingConvention (Priority #4)
  - Pattern: `^BC[0-9]{5}[A-Z]{2}$`
- Rule_Fc_NamingConvention (Priority #5)
  - Pattern: `^FC[0-9]{5}[A-Z]{2}$`

**Implementation**: HIGH priority
- Validate record names match standard patterns
- Ensures traceability and consistency

---

### Group 3: Date Validation

**Excel Rules**:
- BBM rules with "retrospectively 3m/6m/12m" checks
- PRPL 07.00.00: "Planned date of BC later than requested delivery date"
- PRPL 10.00.00: "Check dates for PVER, BC and FC"
- Execution: mandatory/optional

**Java Rules**:
- Rule_CheckDatesForBcAndFc (Priority #6)
  - Validates BC.PLANNED_DATE vs FC.REQ_DATE
- Rule_Bc_CheckPstDates (Priority #10)
  - PST date validation
- Rule_Fc_PlannedDate
- Rule_Fc_ReqDate

**Implementation**: MEDIUM-HIGH priority
- Cross-record date logic validation
- Delivery timeline compliance

---

### Group 4: Requirements & Traceability

**Excel Rules**:
- BBM 10: "Ratio of customer requirements traceable"
- BBM 12: "Ratio of system requirements traceable to source"
- BBM 16: "Ratio of software requirements traceable"
- Execution: mandatory

**Java Rules**:
- Rule_CheckForMissing_BaselineLink (Priority #3)
  - Checks DOORS baseline link exists
- Rule_IssueFD_WithoutLinkToBc (Priority #7)
- Rule_Bc_WithoutLinkToPst (Priority #8)
- Rule_Fc_WithoutLinkToBc

**Implementation**: CRITICAL priority
- Core traceability validation
- Ensures requirements coverage

---

### Group 5: ASIL & Safety

**Excel Rules**:
- BBM 07: "Ratio of SSL requirements successfully verified"
- Safety-critical requirements
- Execution: optional

**Java Rules**:
- Rule_IssueSW_ASIL (Priority #2)
  - ASIL level matching parent-child relationships
  - Safety integrity validation

**Implementation**: CRITICAL priority
- Safety compliance requirement
- Automotive standard (ISO 26262)

---

### Group 6: Comments & Documentation

**Excel Rules**:
- QADO 02.00.00: "Check for requirement based development"
- QADO 03.00.00: "MISRA compliance"
- QADO 04.00.00: "BMI evaluation and documentation"
- Execution: new/mandatory

**Java Rules**:
- Rule_IssueSW_FmeaCheck (Priority #1)
  - FMEA comment validation when state = "Not Required"
- Rule_IssueSW_MissingAffectedIssueComment (Priority #9)
  - Affected issue comments validation

**Implementation**: HIGH priority
- Documentation completeness
- Process compliance

---

### Group 7: Defect & Change Request Metrics

**Excel Rules**:
- BBM 08: "Ratio of defects closed"
- BBM 09: "Ratio of change requests closed"
- Execution: mandatory
- Check Retrospectively: 6m

**Java Rules**:
- Rule_Bc_Close (state validation)
- Rule_Fc_Close (if exists)
- State-based validation rules

**Implementation**: MEDIUM priority
- Metrics tracking
- Closure rate monitoring

---

### Group 8: IPT Planning Rules

**Excel Rules**:
- PRPL 13.00.00: "IFD not implemented/closed after planned BC-R date"
- PRPL 14.00.00: "IFD not committed though Issue-SW committed"
- PRPL 15.00.00: "Release not closed after planned date"
- PRPL 16.00.00: "Workitem not closed after planned date"
- Execution: mandatory

**Java Rules**:
- Date and state validation rules
- Commitment tracking rules

**Implementation**: MEDIUM-HIGH priority
- Planning compliance
- Delivery timeline tracking

---

## Implementation Strategy

### Phase 1: Top 10 Rules (30-40 hours)
Focus on rules with:
- Both Excel + Java backing
- Mandatory or optional execution level
- IPT rule backing (stronger justification)
- Clear implementation path

### Phase 2: Remaining 26 Rules with Excel+Java backing
- Expand to all intersection rules
- ~60-80 hours additional effort

### Phase 3: Excel-only rules (if needed)
- Implement remaining critical rules
- May need to reverse-engineer from Excel specs

---

## Field Reference

### Common RQ1 Fields Used

**Issue-SW**:
- FMEA_STATE, FMEA_COMMENT, FMEA_CHANGE_COMMENT
- ASIL_LEVEL
- SUBMIT_DATE, STATE

**Issue-FD**:
- STATE (check for CLOSED)
- Links to BC records

**BC (Baseline Configuration)**:
- NAME (naming pattern)
- PLANNED_DATE
- STATE (REQUESTED, CLOSED, CANCELED)
- Links to PST, PVER, Issue-FD

**FC (Function Change)**:
- NAME (naming pattern)
- REQ_DATE, PLANNED_DATE
- STATE (AVAILABLE, CLOSED)
- Links to BC

**DOORS Integration**:
- BASELINE_LINK field
- Requirement traceability links

---

## Testing Strategy

1. **Unit Tests**: Each rule independently
2. **Integration Tests**: Cross-record validation
3. **Regression Tests**: Against POST Tool outputs
4. **Test Data**: Use ACCEPTANCE environment records

---

## Next Steps

1. Set up test environment with RQ1 ACCEPTANCE credentials
2. Implement Rule_IssueSW_FmeaCheck (Priority #1)
3. Test against real RQ1 data
4. Iterate through Top 10 rules
5. Document findings and edge cases
