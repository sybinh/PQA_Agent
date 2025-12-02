# RQ1 Checker - Python Implementation

**Based on**: check_QAMi.xlsm VBA tool  
**Approach**: Query user's assigned records ? Apply validation rules ? Generate report

---

## VBA Tool Flow (Discovered)

```
1. User Login
   - Get NT username (e.g., DAB5HC)
   - Authenticate with RQ1

2. Query Records by Assignee
   - Query Issues assigned to user
   - Query Releases (BC/FC) assigned to user
   - Fetch related data (projects, history, etc.)

3. Display in Excel
   - Each type in separate sheet
   - User manually reviews against QAM rules

4. Manual Validation
   - User checks rules using Excel formulas
   - Manual review of compliance
```

---

## Python Implementation Plan

### Simple Approach (Matching VBA Tool)

```python
class RQ1Checker:
    def __init__(self, username, password):
        # Connect to RQ1
        self.client = Client(...)
    
    def get_user_records(self, assignee_id):
        """Get all records assigned to user"""
        # Query Issues where Assignee = user
        issues = self.client.query(
            Issue,
            where=f"Assignee/user_id='{assignee_id}'"
        )
        
        # Query Releases where ResponsiblePerson = user  
        releases = self.client.query(
            Release,
            where=f"ResponsiblePerson/user_id='{assignee_id}'"
        )
        
        return issues, releases
    
    def validate_records(self, records):
        """Apply validation rules"""
        violations = []
        
        for record in records:
            # Apply rules based on record type
            if isinstance(record, Issue):
                violations += self.validate_issue(record)
            elif isinstance(record, Release):
                violations += self.validate_release(record)
        
        return violations
    
    def validate_issue(self, issue):
        """Validate Issue-SW/Issue-FD rules"""
        results = []
        
        # Rule 1: ASIL check (BBM 13)
        if issue.type == "Issue-SW":
            result = Rule_IssueSW_ASIL().execute(issue)
            if not result.passed:
                results.append(result)
        
        # Rule 2: FMEA check (BBM 12)
        if issue.type == "Issue-SW":
            result = Rule_IssueSW_FmeaCheck().execute(issue)
            if not result.passed:
                results.append(result)
        
        # More rules...
        
        return results
    
    def validate_release(self, release):
        """Validate BC/FC rules"""
        results = []
        
        # Rule 1: BC Close (QAM ID-2.5.0)
        if release.type == "BC-Release":
            result = Rule_Bc_Close().execute(release)
            if not result.passed:
                results.append(result)
        
        # Rule 2: BC Naming (PRPL 20)
        if release.type == "BC-Release":
            result = Rule_Bc_NamingConvention().execute(release)
            if not result.passed:
                results.append(result)
        
        # More rules...
        
        return results
    
    def generate_report(self, violations):
        """Export to Excel/JSON"""
        # Create report similar to VBA tool
        pass
```

---

## Advantages over VBA Tool

? **Automated Validation** - No manual rule checking  
? **Consistent Rules** - Python rules match POST tool logic  
? **Better Reporting** - JSON, Excel, HTML output  
? **CI/CD Integration** - Can run in pipelines  
? **Extensible** - Easy to add new rules  

---

## Next Steps

1. Implement `RQ1Checker` class with user query
2. Integrate existing rules (ASIL, BC Close, Date checks)
3. Add more rules from priority list
4. Generate Excel report matching VBA tool format
5. Add command-line interface

---

## Usage

```python
# Simple usage - like VBA tool
checker = RQ1Checker(username="DAB5HC", password="...")

# Get user's records
issues, releases = checker.get_user_records("DAB5HC")

# Validate
violations = checker.validate_records(issues + releases)

# Report
checker.generate_report(violations, output="report.xlsx")
```

---

## Comparison

| Feature | VBA Tool | Python Tool |
|---------|----------|-------------|
| **Input** | User NT login | User ID |
| **Query** | Assignee field | Assignee field |
| **Validation** | Manual (Excel formulas) | Automated (Python rules) |
| **Output** | Excel sheets | Excel/JSON/HTML |
| **Rules** | Manual reference | Automated execution |
| **Speed** | Slow (manual) | Fast (automated) |

---

This approach is **much simpler than POST tool** - just query user's records and validate them!