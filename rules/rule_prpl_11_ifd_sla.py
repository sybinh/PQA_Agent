#!/usr/bin/env python
"""
PRPL 11.00.00: IFD 5 day SLA reached

Rule Logic:
- For each assigned IFD (Issue FD) in "New" state
- Check if it has been in New state for >= 5 WORKING days (from PROJECT ASSIGNMENT date)
- Warn if Function Development hasn't evaluated within SLA

Requirements:
- Evaluation results shall be provided within 5 work days
- Count only working days (Mon-Fri, exclude weekends)
- State must be "New" (not yet evaluated)
- Baseline: Date when IFD was added to project (belongsToProject matches RQ1_PROJECT_ID)
- Fallback: Use submitdate if no project assignment found

Implementation:
- Query Historylog records for the Issue
- Parse historylog XML field to find belongsToProject field changes
- Extract date when belongsToProject was set to target project (from .env RQ1_PROJECT_ID)
- Calculate working days from that project assignment date to today
"""

from enum import Enum
from datetime import datetime, date, timedelta
from typing import Optional, Tuple
import xml.etree.ElementTree as ET


def count_working_days(start_date: date, end_date: date) -> int:
    """
    Count working days (Mon-Fri) between two dates.
    
    Args:
        start_date: Start date
        end_date: End date (inclusive)
    
    Returns:
        Number of working days
    """
    if start_date > end_date:
        return 0
    
    working_days = 0
    current = start_date
    
    while current <= end_date:
        # Monday = 0, Sunday = 6
        if current.weekday() < 5:  # Mon-Fri
            working_days += 1
        current += timedelta(days=1)
    
    return working_days


class LifeCycleState_Issue(Enum):
    """Issue lifecycle states"""
    NEW = "New"
    SUBMITTED = "Submitted"
    EVALUATED = "Evaluated"
    COMMITTED = "Committed"
    IMPLEMENTED = "Implemented"
    CLOSED = "Closed"
    CANCELED = "Canceled"
    CONFLICTED = "Conflicted"


class ValidationResult:
    """Result of a validation rule execution"""
    def __init__(self, passed: bool, severity: str, description: str, violations: list = None):
        self.passed = passed
        self.severity = severity
        self.description = description
        self.violations = violations or []


class Rule_Issue_Sla:
    """
    PRPL 11.00.00: IFD 5 day SLA reached
    
    Checks if an assigned IFD has been in "New" state for 5+ WORKING days 
    without evaluation by Function Development.
    
    Uses HistoryLog to find accurate project assignment date (when belongsToProject 
    was set to target project from RQ1_PROJECT_ID in .env).
    """
    
    RULE_ID = "PRPL 11"
    RULE_NAME = "IFD 5 day SLA reached"
    SLA_WORKING_DAYS = 5
    
    def __init__(self, issue_data: dict, client=None, project_ids: list = None):
        """
        Args:
            issue_data: Dict with keys: id, cq__Type, lifecyclestate, submitdate, dcterms__title, uri
            client: RQ1 Client instance for querying HistoryLog
            project_ids: List of project RQ1 IDs to check (e.g., ["RQONE12345678", ...])
        """
        self.issue_id = issue_data.get('id', 'UNKNOWN')
        self.issue_type = issue_data.get('cq__Type', '')
        self.state = issue_data.get('lifecyclestate', '')
        self.submitdate = issue_data.get('submitdate')
        self.title = issue_data.get('dcterms__title', '')
        self.issue_uri = issue_data.get('uri')
        self.client = client
        self.project_ids = project_ids or []
    
    def _parse_historylog_xml(self, xml_content: str, field_name: str) -> Optional[str]:
        """
        Parse historylog XML to extract field change value.
        
        XML format: <cq:FIELD name="FieldName"><cq:SET>NewValue</cq:SET><cq:PREV>OldValue</cq:PREV></cq:FIELD>
        
        Args:
            xml_content: XML string from HistoryLog.historylog field
            field_name: Field name to search for (e.g., "belongsToProject")
        
        Returns:
            New value (SET) if field found, None otherwise
        """
        if not xml_content:
            return None
        
        try:
            # Parse XML with namespace
            root = ET.fromstring(xml_content)
            
            # Find FIELD element with matching name attribute
            # Handle namespace: {http://jazz.net/xmlns/prod/jazz/calm/1.0/}FIELD
            for field in root.findall('.//{*}FIELD'):
                if field.get('name') == field_name:
                    # Get SET element
                    set_elem = field.find('{*}SET')
                    if set_elem is not None and set_elem.text:
                        return set_elem.text.strip()
            
            return None
        except Exception as e:
            # If XML parsing fails, try simple string search as fallback
            # Look for: <cq:FIELD name="belongsToProject"><cq:SET>RQONE00001940</cq:SET>
            import re
            pattern = rf'<cq:FIELD\s+name="{field_name}"><cq:SET>([^<]+)</cq:SET>'
            match = re.search(pattern, xml_content)
            if match:
                return match.group(1).strip()
            
            return None
    
    def _get_project_assignment_date(self) -> Tuple[Optional[date], str]:
        """
        Query HistoryLog to find when IFD was added to any target project.
        
        Returns:
            Tuple of (assignment_date, source)
            - assignment_date: Date when added to project, or None if not found
            - source: Description of date source ("added to project X on Y", "submitdate fallback")
        """
        if not self.client or not self.issue_uri or not self.project_ids:
            submit_date = self._parse_submitdate()
            return (submit_date, "submitdate (no project IDs or client)")
        
        try:
            # Import here to avoid circular dependency
            from rq1.historylog import Historylog
            from rq1.models import HistorylogProperty
            from rq1.base import reference
            
            # Query Historylog records for this Issue
            logs_query = self.client.query(
                Historylog,
                where=(HistorylogProperty.belongstoissue == reference(self.issue_uri)),
                select=[
                    HistorylogProperty.lastmodifieddate,
                    HistorylogProperty.lastmodifieduser,
                    HistorylogProperty.historylog
                ]
            )
            
            # Search for belongsToProject change to any target project (chronologically)
            for log in logs_query.members:
                xml_content = getattr(log, 'historylog', None)
                if not xml_content:
                    continue
                
                # Parse XML to check if belongsToProject was set to any target project
                new_project = self._parse_historylog_xml(xml_content, 'belongsToProject')
                if new_project:
                    # Check if this project matches any of our target projects
                    if new_project in self.project_ids:
                        # Found project assignment!
                        assigned_datetime = getattr(log, 'lastmodifieddate', None)
                        if assigned_datetime:
                            if isinstance(assigned_datetime, datetime):
                                assigned_date = assigned_datetime.date()
                            else:
                                assigned_date = datetime.fromisoformat(str(assigned_datetime)).date()
                            
                            modified_by = getattr(log, 'lastmodifieduser', 'unknown')
                            return (assigned_date, f"added to project {new_project} on {assigned_date} by {modified_by}")
            
            # No belongsToProject change found - use submitdate as fallback
            submit_date = self._parse_submitdate()
            project_list = ', '.join(self.project_ids[:3])
            if len(self.project_ids) > 3:
                project_list += f", ... ({len(self.project_ids)} total)"
            return (submit_date, f"submitdate (no project assignment to [{project_list}] found)")
            
        except Exception as e:
            # Error querying history - fallback to submitdate
            submit_date = self._parse_submitdate()
            return (submit_date, f"submitdate (history query error: {e})")
    
    def _parse_submitdate(self) -> Optional[date]:
        """Parse submitdate to date object"""
        if not self.submitdate:
            return None
        
        if isinstance(self.submitdate, datetime):
            return self.submitdate.date()
        elif isinstance(self.submitdate, date):
            return self.submitdate
        else:
            try:
                return datetime.fromisoformat(str(self.submitdate)).date()
            except:
                return None
    
    def execute(self) -> ValidationResult:
        """
        Execute the validation rule
        
        Returns:
            ValidationResult with passed status and details
        """
        # Only check IFD (Issue FD)
        if self.issue_type != 'Issue FD':
            return ValidationResult(
                passed=True,
                severity="INFO",
                description="Not an IFD issue"
            )
        
        # Only check if in "New" state
        if self.state != LifeCycleState_Issue.NEW.value:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.issue_id} not in New state (current: {self.state})"
            )
        
        # Skip if no submitdate
        if not self.submitdate:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.issue_id} has no submit date"
            )
        
        # Get project assignment date from HistoryLog
        today = date.today()
        baseline_date, date_source = self._get_project_assignment_date()
        
        if not baseline_date:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.issue_id} has no valid baseline date"
            )
        
        # Count working days since project assignment
        working_days_in_new = count_working_days(baseline_date, today)
        
        # Check if SLA exceeded
        if working_days_in_new >= self.SLA_WORKING_DAYS:
            violation_msg = (
                f"IFD {self.issue_id} has been in New state for {working_days_in_new} working days "
                f"({date_source}), exceeding {self.SLA_WORKING_DAYS}-day SLA"
            )
            
            details = (
                f"IFD {self.issue_id} evaluation SLA exceeded.\n"
                f"Title: {self.title}\n"
                f"Baseline date: {baseline_date} ({date_source})\n"
                f"Working days in New state: {working_days_in_new} days\n"
                f"SLA: {self.SLA_WORKING_DAYS} working days\n"
                f"Current state: {self.state}\n\n"
                f"Hint: Evaluate and estimate effort, move to Evaluated state."
            )
            
            return ValidationResult(
                passed=False,
                severity="WARNING",
                description=details,
                violations=[violation_msg]
            )
        
        # SLA not exceeded
        return ValidationResult(
            passed=True,
            severity="INFO",
            description=f"IFD {self.issue_id} within SLA ({working_days_in_new} working days in New state, {date_source})"
        )


