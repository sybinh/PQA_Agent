#!/usr/bin/env python
"""
Test PRPL 02 and PRPL 16
"""

import sys
sys.path.insert(0, 'src')

from rule_registry import get_registry

print("="*80)
print("Testing PRPL 02.00.00 and PRPL 16.00.00")
print("="*80)

registry = get_registry()

# Test PRPL 02.00.00: Workitem started without planned date
print("\n[TEST 1] PRPL 02.00.00: Workitem in started state without planned date")
print("="*80)

# Scenario 1: Workitem in Planned state, no planned date (VIOLATION)
workitem_violation = {
    'id': 'RQONE33333333',
    'dcterms__title': 'Test BC Release',
    'dcterms__type': 'Release',
    'lifecyclestate': 'Planned',
    'planneddate': None  # Missing!
}

result = registry.execute_rule("PRPL 02.00.00", workitem_violation)
print(f"\nScenario 1: Workitem in Planned state, no planned date")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")

# Scenario 2: Workitem in Planned state, has planned date (PASS)
workitem_pass = {
    'id': 'RQONE44444444',
    'dcterms__title': 'Test BC Release',
    'dcterms__type': 'Release',
    'lifecyclestate': 'Planned',
    'planneddate': '2025-12-31T12:00:00+00:00'
}

result = registry.execute_rule("PRPL 02.00.00", workitem_pass)
print(f"\nScenario 2: Workitem in Planned state, has planned date")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")

# Scenario 3: Workitem in New state, no planned date (PASS - exempt)
workitem_new = {
    'id': 'RQONE55555555',
    'dcterms__title': 'Test Issue',
    'dcterms__type': 'Issue',
    'lifecyclestate': 'New',
    'planneddate': None
}

result = registry.execute_rule("PRPL 02.00.00", workitem_new)
print(f"\nScenario 3: Workitem in New state (exempt)")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")

# Test PRPL 16.00.00: Workitem not closed after planned date
print("\n" + "="*80)
print("[TEST 2] PRPL 16.00.00: Workitem not closed after planned date")
print("="*80)

# Scenario 1: Workitem overdue, not closed (VIOLATION)
workitem_overdue = {
    'id': 'RQONE66666666',
    'dcterms__title': 'Test FC Release',
    'dcterms__type': 'Release',
    'lifecyclestate': 'Developed',
    'planneddate': '2025-11-01T12:00:00+00:00'  # Past
}

result = registry.execute_rule("PRPL 16.00.00", workitem_overdue)
print(f"\nScenario 1: Workitem past planned date, not closed")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")

# Scenario 2: Workitem overdue but closed (PASS)
workitem_closed = {
    'id': 'RQONE77777777',
    'dcterms__title': 'Test IFD Issue',
    'dcterms__type': 'Issue',
    'lifecyclestate': 'Closed',
    'planneddate': '2025-11-01T12:00:00+00:00'  # Past but closed
}

result = registry.execute_rule("PRPL 16.00.00", workitem_closed)
print(f"\nScenario 2: Workitem past planned date, already closed")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")

# Scenario 3: Workitem not yet due (PASS)
workitem_future = {
    'id': 'RQONE88888888',
    'dcterms__title': 'Test BC Release',
    'dcterms__type': 'Release',
    'lifecyclestate': 'Planned',
    'planneddate': '2026-06-30T12:00:00+00:00'  # Future
}

result = registry.execute_rule("PRPL 16.00.00", workitem_future)
print(f"\nScenario 3: Workitem not yet at planned date")
print(f"Result: {result.severity}")
print(f"Passed: {result.passed}")
print(f"Title: {result.title}")

print("\n" + "="*80)
print("Tests Complete!")
print("Implemented: 6/32 rules (18.8%)")
print("="*80)
