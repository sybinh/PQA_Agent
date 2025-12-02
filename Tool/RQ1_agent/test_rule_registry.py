#!/usr/bin/env python
"""
Test Rule Registry System

Demonstrates executing rules by Rule ID.
"""

import sys
sys.path.insert(0, 'src')

from rule_registry import get_registry, RuleCategory

print("="*80)
print("Testing Rule Registry System")
print("="*80)

# Get registry
registry = get_registry()

# Show implementation status
status = registry.get_implementation_status()
print(f"\n[IMPLEMENTATION STATUS]")
print(f"  Implemented: {status['total_implemented']}/{status['total_activated']} rules")
print(f"  Completion: {status['completion_percentage']:.1f}%")
print(f"  By category: {status['by_category']}")

# List all registered rules
print(f"\n[REGISTERED RULES]")
for rule_meta in registry.list_rules():
    print(f"  {rule_meta.rule_id}: {rule_meta.description[:60]}...")
    print(f"    Python: {rule_meta.python_module}.{rule_meta.python_class}")
    print(f"    Java: {rule_meta.java_class or 'N/A'}")
    print()

# Test executing PRPL 15.00.00
print("="*80)
print("[TEST 1] Execute PRPL 15.00.00 with mock data")
print("="*80)

mock_bc_data = {
    'id': 'RQONE99999999',
    'dcterms__title': 'Test BC Release',
    'lifecyclestate': 'Planned',
    'planneddate': '2025-11-01T12:00:00+00:00',
    'actualdate': None,
    'submitdate': '2025-10-15T10:00:00+00:00'
}

try:
    result = registry.execute_rule("PRPL 15.00.00", mock_bc_data)
    print(f"\nResult: {result.severity}")
    print(f"Title: {result.title}")
    print(f"Description: {result.description}")
    print(f"Passed: {result.passed}")
except Exception as e:
    print(f"[ERROR] {e}")
    import traceback
    traceback.print_exc()

# Test executing PRPL 12.00.00
print("\n" + "="*80)
print("[TEST 2] Execute PRPL 12.00.00 with mock data")
print("="*80)

mock_ifd_data = {
    'id': 'RQONE88888888',
    'dcterms__title': 'Test IFD Issue',
    'lifecyclestate': 'Committed'
}

mock_mapped_bcs = [
    {'id': 'RQONE00000001', 'lifecyclestate': 'Closed'},
    {'id': 'RQONE00000002', 'lifecyclestate': 'Canceled'},
    {'id': 'RQONE00000003', 'lifecyclestate': 'Closed'}
]

try:
    result = registry.execute_rule("PRPL 12.00.00", mock_ifd_data, mock_mapped_bcs)
    print(f"\nResult: {result.severity}")
    print(f"Title: {result.title}")
    print(f"Description: {result.description}")
    print(f"Passed: {result.passed}")
except Exception as e:
    print(f"[ERROR] {e}")
    import traceback
    traceback.print_exc()

# Test with non-existent rule
print("\n" + "="*80)
print("[TEST 3] Try non-existent rule")
print("="*80)

try:
    result = registry.execute_rule("BBM 1", {})
    print(f"Result: {result}")
except ValueError as e:
    print(f"[EXPECTED ERROR] {e}")

print("\n" + "="*80)
print("Rule Registry Test Complete")
print("="*80)
