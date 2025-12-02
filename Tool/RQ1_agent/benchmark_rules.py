#!/usr/bin/env python
"""
Benchmark rule execution time
"""

import sys
import time
sys.path.insert(0, 'src')

from rule_registry import get_registry
from datetime import datetime, timezone

print("="*80)
print("Rule Execution Time Benchmark")
print("="*80)

registry = get_registry()

# Test data for each rule
test_cases = {
    "PRPL 15.00.00": {
        "args": ({
            'id': 'RQONE99999999',
            'dcterms__title': 'Test BC',
            'lifecyclestate': 'Planned',
            'planneddate': '2025-11-01T12:00:00+00:00',
            'actualdate': None
        },),
        "iterations": 1000
    },
    "PRPL 02.00.00": {
        "args": ({
            'id': 'RQONE99999999',
            'dcterms__title': 'Test Workitem',
            'dcterms__type': 'Release',
            'lifecyclestate': 'Planned',
            'planneddate': None
        },),
        "iterations": 1000
    },
    "PRPL 16.00.00": {
        "args": ({
            'id': 'RQONE99999999',
            'dcterms__title': 'Test Workitem',
            'dcterms__type': 'Release',
            'lifecyclestate': 'Developed',
            'planneddate': '2025-11-01T12:00:00+00:00'
        },),
        "iterations": 1000
    },
    "PRPL 07.00.00": {
        "args": (
            {
                'id': 'RQONE99999999',
                'dcterms__title': 'Test BC',
                'lifecyclestate': 'Planned',
                'planneddate': '2025-12-15T12:00:00+00:00'
            },
            []  # Empty PST mappings
        ),
        "iterations": 1000
    },
    "PRPL 12.00.00": {
        "args": (
            {
                'id': 'RQONE99999999',
                'dcterms__title': 'Test IFD',
                'lifecyclestate': 'Committed'
            },
            [
                {'id': 'RQONE00000001', 'lifecyclestate': 'Closed'},
                {'id': 'RQONE00000002', 'lifecyclestate': 'Closed'}
            ]
        ),
        "iterations": 1000
    },
    "PRPL 13.00.00": {
        "args": (
            {
                'id': 'RQONE99999999',
                'dcterms__title': 'Test IFD',
                'lifecyclestate': 'Committed'
            },
            []  # Empty BC mappings
        ),
        "iterations": 1000
    }
}

print("\nTesting each rule with 1000 iterations...\n")

results = []

for rule_id, config in test_cases.items():
    iterations = config["iterations"]
    args = config["args"]
    
    # Warm-up run
    registry.execute_rule(rule_id, *args)
    
    # Benchmark
    start_time = time.perf_counter()
    
    for _ in range(iterations):
        registry.execute_rule(rule_id, *args)
    
    end_time = time.perf_counter()
    
    total_time = end_time - start_time
    avg_time = total_time / iterations
    avg_time_ms = avg_time * 1000
    
    results.append({
        'rule_id': rule_id,
        'total_time': total_time,
        'avg_time': avg_time,
        'avg_time_ms': avg_time_ms,
        'iterations': iterations
    })
    
    print(f"{rule_id}")
    print(f"  Total time: {total_time:.3f}s for {iterations} runs")
    print(f"  Average: {avg_time_ms:.3f}ms per execution")
    print(f"  Throughput: {iterations/total_time:.0f} rules/second")
    print()

# Summary
print("="*80)
print("SUMMARY")
print("="*80)

results_sorted = sorted(results, key=lambda x: x['avg_time_ms'])

print("\nFastest to Slowest:")
for i, r in enumerate(results_sorted, 1):
    print(f"{i}. {r['rule_id']}: {r['avg_time_ms']:.3f}ms")

print(f"\nOverall average: {sum(r['avg_time_ms'] for r in results) / len(results):.3f}ms per rule")

# Estimate time for scanning
print("\n" + "="*80)
print("TIME ESTIMATES FOR PRODUCTION SCANNING")
print("="*80)

avg_rule_time = sum(r['avg_time'] for r in results) / len(results)

scenarios = [
    ("Single workitem (6 rules)", 1, 6),
    ("10 workitems", 10, 6),
    ("100 workitems", 100, 6),
    ("User scan (50 workitems)", 50, 6),
    ("Project scan (500 workitems)", 500, 6),
]

print("\nNote: Times exclude RQ1 network fetch time (only rule logic)")
for desc, workitems, rules_per_item in scenarios:
    total_rules = workitems * rules_per_item
    total_time = total_rules * avg_rule_time
    print(f"\n{desc}:")
    print(f"  {total_rules} total rule executions")
    print(f"  Rule logic time: {total_time:.3f}s ({total_time*1000:.0f}ms)")
    print(f"  Estimated with RQ1 fetch (0.5s/item): {workitems * 0.5 + total_time:.1f}s")

print("\n" + "="*80)
