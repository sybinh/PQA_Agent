#!/usr/bin/env python
"""
PRPL 18.00.00: IFD not committed 5+ working days after attached ISW committed

Rule Logic:
- For each IFD (Issue FD) in state New/Submitted/Evaluated
- Check if parent Issue-SW is in Committed state
- If yes, check when ISW was committed (from history log)
- If 5+ working days have passed, IFD should be committed

Requirements:
- IFD must be committed within 5 working days after parent ISW committed
- Excludes: Canceled, Conflicted, Closed states
- Excludes: Already Committed/Implemented/Closed IFDs
- Requires: HistoryLog access to find ISW commit date

Pseudo Code from Excel:
if issueFD = canceledOrConflicted then return
if IFD = COMMITTED || IFD = IMPLEMENTED || IFD = CLOSED then return
if issueFD.PARENT instanceof issueSW && parent.LIFE_CYCLE_STATE = COMMITTED 5+ working days earlier then
    Warning "Not committed, even though ISW is committed 5+ days"
"""

from enum import Enum
from datetime import datetime, date, timedelta
from typing import Optional, Tuple


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


class Rule_IFD_ISW_Commitment_Delay:
    """
    PRPL 18.00.00: IFD not committed 5+ working days after ISW committed
    
    Checks if an IFD should be committed because parent ISW has been committed for 5+ working days.
    Requires HistoryLog to find ISW commit date.
    """
    
    RULE_ID = "PRPL 18"
    RULE_NAME = "IFD not committed 5+ days after ISW committed"
    SLA_WORKING_DAYS = 5
    
    def __init__(self, ifd_data: dict, parent_isw_data: dict = None, client=None):
        """
        Args:
            ifd_data: Dict with keys: id, cq__Type, lifecyclestate, dcterms__title
            parent_isw_data: Dict with keys: id, lifecyclestate, uri (optional, can be None)
            client: RQ1 Client for querying HistoryLog (optional)
        """
        self.ifd_id = ifd_data.get('id', 'UNKNOWN')
        self.ifd_type = ifd_data.get('cq__Type', '')
        self.ifd_state = ifd_data.get('lifecyclestate', '')
        self.ifd_title = ifd_data.get('dcterms__title', '')
        
        # Parent ISW info
        self.parent_isw = parent_isw_data
        self.parent_isw_id = parent_isw_data.get('id', 'UNKNOWN') if parent_isw_data else None
        self.parent_isw_state = parent_isw_data.get('lifecyclestate', '') if parent_isw_data else None
        self.parent_isw_uri = parent_isw_data.get('uri') if parent_isw_data else None
        
        self.client = client
    
    def _get_isw_committed_date(self) -> Tuple[Optional[date], str]:
        """
        Query HistoryLog to find when ISW was committed.
        
        Returns:
            Tuple of (committed_date, source)
        """
        if not self.client or not self.parent_isw_uri:
            return (None, "no history query (client or URI missing)")
        
        try:
            from rq1.historylog import Historylog
            from rq1.models import HistorylogProperty
            from rq1.base import reference
            import xml.etree.ElementTree as ET
            
            # Query Historylog records for ISW
            logs_query = self.client.query(
                Historylog,
                where=(HistorylogProperty.belongstoissue == reference(self.parent_isw_uri)),
                select=[
                    HistorylogProperty.lastmodifieddate,
                    HistorylogProperty.lifecyclestate,
                    HistorylogProperty.previouslifecyclestate,
                    HistorylogProperty.historylog
                ]
            )
            
            # Find log where lifecyclestate changed to Committed
            for log in logs_query.members:
                curr_state = getattr(log, 'lifecyclestate', None)
                prev_state = getattr(log, 'previouslifecyclestate', None)
                
                # Check if this is the transition to Committed
                if curr_state == LifeCycleState_Issue.COMMITTED.value:
                    committed_datetime = getattr(log, 'lastmodifieddate', None)
                    if committed_datetime:
                        if isinstance(committed_datetime, datetime):
                            committed_date = committed_datetime.date()
                        else:
                            committed_date = datetime.fromisoformat(str(committed_datetime)).date()
                        
                        return (committed_date, f"ISW committed on {committed_date}")
            
            # ISW is committed but no commit event found in history
            return (None, "ISW committed but no commit date found in history")
            
        except Exception as e:
            return (None, f"history query error: {e}")
    
    def execute(self) -> ValidationResult:
        """
        Execute the validation rule
        
        Returns:
            ValidationResult with passed status and details
        """
        # Only check IFD (Issue FD)
        if self.ifd_type != 'Issue FD':
            return ValidationResult(
                passed=True,
                severity="INFO",
                description="Not an IFD issue"
            )
        
        # Skip Canceled, Conflicted, Closed
        if self.ifd_state in [LifeCycleState_Issue.CANCELED.value,
                              LifeCycleState_Issue.CONFLICTED.value,
                              LifeCycleState_Issue.CLOSED.value]:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.ifd_id} is {self.ifd_state} (excluded from check)"
            )
        
        # Skip already Committed/Implemented/Closed
        if self.ifd_state in [LifeCycleState_Issue.COMMITTED.value,
                              LifeCycleState_Issue.IMPLEMENTED.value,
                              LifeCycleState_Issue.CLOSED.value]:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.ifd_id} already {self.ifd_state}"
            )
        
        # Check if parent ISW exists and is committed
        if not self.parent_isw:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.ifd_id} has no parent ISW"
            )
        
        if self.parent_isw_state != LifeCycleState_Issue.COMMITTED.value:
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"Parent ISW {self.parent_isw_id} not committed (state: {self.parent_isw_state})"
            )
        
        # Get ISW commit date from history
        isw_committed_date, date_source = self._get_isw_committed_date()
        
        if not isw_committed_date:
            # Can't determine commit date - fallback to PRPL 14 (no time check)
            violation_msg = (
                f"IFD {self.ifd_id} not committed, parent ISW {self.parent_isw_id} is committed "
                f"(commit date unknown: {date_source})"
            )
            
            details = (
                f"IFD {self.ifd_id} commitment delay (exact delay unknown).\n"
                f"Title: {self.ifd_title}\n"
                f"IFD state: {self.ifd_state}\n"
                f"Parent ISW: {self.parent_isw_id}\n"
                f"Parent ISW state: {self.parent_isw_state}\n"
                f"ISW commit date: {date_source}\n\n"
                f"Hint: Commit IFD {self.ifd_id} to match parent ISW."
            )
            
            return ValidationResult(
                passed=False,
                severity="WARNING",
                description=details,
                violations=[violation_msg]
            )
        
        # Calculate working days since ISW committed
        today = date.today()
        days_since_isw_committed = count_working_days(isw_committed_date, today)
        
        if days_since_isw_committed < self.SLA_WORKING_DAYS:
            # Within SLA - no violation
            return ValidationResult(
                passed=True,
                severity="INFO",
                description=f"IFD {self.ifd_id} within SLA ({days_since_isw_committed} working days since ISW committed)"
            )
        
        # SLA exceeded - VIOLATION
        violation_msg = (
            f"IFD {self.ifd_id} not committed {days_since_isw_committed} working days after "
            f"parent ISW {self.parent_isw_id} committed (SLA: {self.SLA_WORKING_DAYS} days)"
        )
        
        details = (
            f"IFD {self.ifd_id} commitment SLA exceeded.\n"
            f"Title: {self.ifd_title}\n"
            f"IFD state: {self.ifd_state}\n"
            f"Parent ISW: {self.parent_isw_id}\n"
            f"Parent ISW state: {self.parent_isw_state}\n"
            f"ISW committed: {isw_committed_date}\n"
            f"Working days since ISW committed: {days_since_isw_committed} days\n"
            f"SLA: {self.SLA_WORKING_DAYS} working days\n\n"
            f"Hint: Commit IFD {self.ifd_id} immediately (SLA exceeded by {days_since_isw_committed - self.SLA_WORKING_DAYS} days)."
        )
        
        return ValidationResult(
            passed=False,
            severity="WARNING",
            description=details,
            violations=[violation_msg]
        )
