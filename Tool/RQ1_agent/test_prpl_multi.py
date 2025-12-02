#!/usr/bin/env python
"""
Test PRPL 07, 12, 13 rules with mock data
"""

import sys
sys.path.insert(0, 'src')

from rule_registry import get_registry
from datetime import datetime, timezone

print("="*80)
print("Testing PRPL Rules: 07, 12, 13")
print("="*80)

registry = get_registry()
status = registry.get_implementation_status()

print(f"\n[STATUS] Implemented: {status['total_implemented']}/32 rules ({status['completion_percentage']:.1f}%)")
print(f"Categories: {status['by_category']}")

# Test PRPL 07.00.00: BC vs PVER dates
print("\n" + "="*80)
print("[TEST 1] PRPL 07.00.00: BC planned date vs PVER delivery dates")
print("="*80)

from rules.rule_prpl_07_bc_pst_dates import PstMapping

bc_data = {
    'id': 'RQONE12345678',
    'dcterms__title': 'Test BC Release',
    'lifecyclestate': 'Planned',
    'planneddate': '2025-12-15T12:00:00+00:00'
}

# Scenario 1: BC planned AFTER PVER delivery (VIOLATION)
pst_mappings_violation = [
    PstMapping(
        pst_id='RQONE99990001',
        pst_type='PVER',
        rrm_state='Committed',
        requested_delivery_date=datetime(2025, 12, 10, 12, 0, 0, tzinfo=timezone.utc)  # BC is 5 days late
    ),
    PstMapping(
        pst_id='RQONE99990002',
        pst_type='PVAR',
        rrm_state='Committed',
        requested_delivery_date=datetime(2025, 12, 8, 12, 0, 0, tzinfo=timezone.utc)  # BC is 7 days late
    )
]

result = registry.execute_rule("PRPL 07.00.00", bc_data, pst_mappings_violation)
print(f"\nScenario 1: BC planned AFTER PVER delivery")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")
print(f"Violations: {len(result.violations)}")
if result.violations:
    for v in result.violations:
        print(f"  - {v}")

# Scenario 2: BC planned BEFORE PVER delivery (PASS)
pst_mappings_pass = [
    PstMapping(
        pst_id='RQONE99990003',
        pst_type='PVER',
        rrm_state='Committed',
        requested_delivery_date=datetime(2025, 12, 20, 12, 0, 0, tzinfo=timezone.utc)  # BC is 5 days early
    )
]

result = registry.execute_rule("PRPL 07.00.00", bc_data, pst_mappings_pass)
print(f"\nScenario 2: BC planned BEFORE PVER delivery")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")

# Test PRPL 12.00.00: IFD not closed when all BCs closed
print("\n" + "="*80)
print("[TEST 2] PRPL 12.00.00: IFD not closed when all BCs closed")
print("="*80)

ifd_data = {
    'id': 'RQONE11111111',
    'dcterms__title': 'Test IFD Issue',
    'lifecyclestate': 'Committed'
}

bc_mappings_all_closed = [
    {'id': 'RQONE00000001', 'lifecyclestate': 'Closed'},
    {'id': 'RQONE00000002', 'lifecyclestate': 'Canceled'},
    {'id': 'RQONE00000003', 'lifecyclestate': 'Closed'}
]

result = registry.execute_rule("PRPL 12.00.00", ifd_data, bc_mappings_all_closed)
print(f"\nScenario: All 3 BCs are Closed/Canceled, IFD still Committed")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")

# Test PRPL 13.00.00: IFD not implemented after BC planned date
print("\n" + "="*80)
print("[TEST 3] PRPL 13.00.00: IFD not implemented after BC planned date")
print("="*80)

from rules.rule_prpl_13_ifd_bc_planned import BcMapping

ifd_data = {
    'id': 'RQONE22222222',
    'dcterms__title': 'Test IFD Issue',
    'lifecyclestate': 'Committed'
}

# Scenario 1: BC planned date is in the PAST (VIOLATION)
bc_mappings_overdue = [
    BcMapping(
        bc_id='RQONE00000010',
        bc_title='Test BC 1',
        bc_state='Developed',
        planned_date=datetime(2025, 11, 1, 12, 0, 0, tzinfo=timezone.utc)  # 1 month ago
    ),
    BcMapping(
        bc_id='RQONE00000011',
        bc_title='Test BC 2',
        bc_state='Developed',
        planned_date=datetime(2025, 11, 15, 12, 0, 0, tzinfo=timezone.utc)  # 2 weeks ago
    )
]

result = registry.execute_rule("PRPL 13.00.00", ifd_data, bc_mappings_overdue)
print(f"\nScenario 1: BCs are past their planned dates")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")
print(f"Violations: {len(result.violations)}")

# Scenario 2: BC planned date is in the FUTURE (PASS)
bc_mappings_future = [
    BcMapping(
        bc_id='RQONE00000012',
        bc_title='Test BC 3',
        bc_state='Planned',
        planned_date=datetime(2026, 1, 15, 12, 0, 0, tzinfo=timezone.utc)  # Future
    )
]

result = registry.execute_rule("PRPL 13.00.00", ifd_data, bc_mappings_future)
print(f"\nScenario 2: BC planned date in future")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")

print("\n" + "="*80)
print("All Tests Complete!")
print("="*80)
