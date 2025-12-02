# Rule Implementation Strategy

**Date**: November 28, 2025  
**Approach**: Convert POST tool workflow to Python, grouped by category (QAM/BBM/IPT)

---

## Strategy Overview

### Objectives
1. **Reuse POST Tool logic** - Convert Java rules to Python (exclude UI)
2. **Group by category** - Organize rules by QAM, BBM, IPT standards
3. **Map to Excel documentation** - Link implementation to official process docs
4. **Maintain proven logic** - Keep validated business rules from POST tool

### Rule Categories

**QAM (Quality Assurance Manual)**: 2 rules  
- Focus: Process compliance, quality gates
- Priority: CRITICAL
- Example: BC closure validation

**BBM (Building Block Manual)**: 4 rules  
- Focus: Safety, traceability, FMEA/ASIL
- Priority: CRITICAL to HIGH
- Example: ASIL classification check ? (already in Python)

**IPT (Integration & Planning Tool)**: 10 rules  
- Focus: Naming conventions, dates, traceability
- Priority: HIGH to MEDIUM
- Example: BC/FC naming validation

**UNKNOWN**: 9 rules  
- Focus: Various specialized checks
- Priority: MEDIUM to LOW
- Need: Further investigation for Excel mapping

---

## Implementation Status

### Summary
- **Total Mapped Rules**: 25 (of 37 Java rules)
- **With Excel Backing**: 16 rules (official documentation exists)
- **Already in Python**: 2 rules (ASIL check, Date validation partial)
- **Ready to Implement**: 14 rules (Excel + Java backing)

### By Priority
```
CRITICAL (3):  QAM/BBM rules - immediate business impact
HIGH (7):      BBM/IPT rules - data quality & safety
MEDIUM (9):    IPT rules - consistency checks
LOW (6):       Specialized rules - nice to have
```

### By Functional Group
```
Traceability (4):     Link validation between records
Date Validation (6):  Timeline consistency checks
Naming Convention (2): Format validation
Safety & Quality (2):  ASIL, FMEA compliance
Release Closure (1):   QAM process gates
Documentation (3):     Comment requirements
Others (7):            Specialized validations
```

---

## Top 10 Rules for Implementation

### 1. Rule_IssueSW_ASIL ? DONE
- **Category**: BBM 13
- **Priority**: CRITICAL
- **Status**: ? Python implementation complete
- **Group**: Safety & Quality

### 2. Rule_Bc_Close
- **Category**: QAM ID-2.5.0
- **Priority**: CRITICAL
- **Status**: ?? Python version ready (test_bc_production.py)
- **Group**: Release Closure

### 3. Rule_CheckDatesForBcAndFc
- **Category**: QAM ID-2.5.0 + PRPL 10.00.00
- **Priority**: CRITICAL
- **Status**: ?? Partial Python (rule_check_dates_bc_fc.py)
- **Group**: Date Validation

### 4. Rule_CheckForMissing_BaselineLink
- **Category**: BBM 10
- **Priority**: HIGH
- **Status**: ? Ready to implement
- **Group**: Traceability

### 5. Rule_IssueSW_FmeaCheck
- **Category**: BBM 12
- **Priority**: HIGH
- **Status**: ? Ready to implement
- **Group**: Safety & Quality

### 6. Rule_Bc_NamingConvention
- **Category**: PRPL 20.00.00
- **Priority**: HIGH
- **Status**: ? Ready to implement
- **Group**: Naming Convention
- **Pattern**: `^BC[0-9]{5}[A-Z]{2}$`

### 7. Rule_Fc_NamingConvention
- **Category**: PRPL 19.00.00
- **Priority**: HIGH
- **Status**: ? Ready to implement
- **Group**: Naming Convention
- **Pattern**: `^FC[0-9]{5}[A-Z]{2}$`

### 8. Rule_IssueFD_WithoutLinkToBc
- **Category**: QADO 01.00.00
- **Priority**: HIGH
- **Status**: ? Ready to implement
- **Group**: Traceability

### 9. Rule_Bc_WithoutLinkToPst
- **Category**: PRPL 18.00.00
- **Priority**: HIGH
- **Status**: ? Ready to implement
- **Group**: Traceability

### 10. Rule_Fc_WithoutLinkToBc
- **Category**: PRPL 17.00.00
- **Priority**: HIGH
- **Status**: ? Ready to implement
- **Group**: Traceability

---

## Workflow Architecture

### POST Tool Workflow (Java)
```
1. Connect to RQ1 (OSLC)
2. Query records by filters
3. Load record data + relationships
4. Execute rule groups
5. Collect warnings/errors
6. Generate report (UI)
```

### Python Tool Workflow (Planned)
```
1. Connect to RQ1 (building-block-rq1)
2. Query records by filters
3. Load record data + relationships
4. Execute rule groups (by category)
   ?? QAM rules
   ?? BBM rules
   ?? IPT rules
   ?? Custom rules
5. Collect validation results
6. Export results (JSON/CSV/Report)
```

### Advantages of Python Version
- ? Use building-block-rq1 (modern RQ1 client)
- ? Easier to extend with new rules
- ? Better data export options (JSON, CSV, APIs)
- ? Can integrate with CI/CD pipelines
- ? Simpler deployment (no Java UI dependencies)

---

## Next Steps

### Phase 1: Core Infrastructure (1-2 days)
- [ ] Create rule execution engine
- [ ] Build validation result data structures
- [ ] Setup category-based rule loading
- [ ] Create report generation

### Phase 2A: Implement Top 5 Rules (1 week)
- [x] Rule_IssueSW_ASIL (DONE)
- [ ] Rule_Bc_Close (refactor existing)
- [ ] Rule_CheckDatesForBcAndFc (refactor existing)
- [ ] Rule_CheckForMissing_BaselineLink (NEW)
- [ ] Rule_IssueSW_FmeaCheck (NEW)

### Phase 2B: Implement Next 5 Rules (1 week)
- [ ] Rule_Bc_NamingConvention
- [ ] Rule_Fc_NamingConvention
- [ ] Rule_IssueFD_WithoutLinkToBc
- [ ] Rule_Bc_WithoutLinkToPst
- [ ] Rule_Fc_WithoutLinkToBc

### Phase 3: Integration & Testing (1 week)
- [ ] Test with real RQ1 data (PRODUCTIVE)
- [ ] Validate against Excel rules
- [ ] Performance optimization
- [ ] Documentation

### Phase 4: Remaining Rules (2 weeks)
- [ ] Medium priority rules (9 rules)
- [ ] Low priority rules (6 rules)
- [ ] Investigate UNKNOWN category (9 rules)

---

## Files Generated

- `docs/rule_mappings.json` - Complete rule mappings with categories
- `docs/java_rules_catalog.json` - Parsed Java rules metadata
- `scripts/parse_java_rules.py` - Java rule parser
- `scripts/rule_mapping_system.py` - Rule mapping registry

---

## References

- Java Rules: `rq1/POST_extracted/DataModel/Rq1/Monitoring/Rule_*.java`
- Excel Rules: `docs/RULES_COMPLETE.md`
- Mapping Guide: `docs/EXCEL_TO_JAVA_MAPPING.md`
- Implementation Guide: `docs/IMPLEMENTATION_GUIDE.md`