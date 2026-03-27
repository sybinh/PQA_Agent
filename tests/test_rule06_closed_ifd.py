"""
Test PRPL 06 rule with CLOSED IFD (RQONE04984940)
Verify behavior: should rule still check attributes when IFD is Closed?
"""
import sys
import os
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))

from rules.rule_prpl_06_ifd_defect_attributes import Rule_Ifd_DefectAttributes


def make_ifd(state: str, fill_fields: bool = False) -> dict:
    """Helper to create IFD test data."""
    base = {
        'id': 'RQONE04984940',
        'dcterms__title': 'Test IFD - PRPL06 closed state check',
        'cq__Type': 'Issue FD',
        'category': 'Defect',
        'lifecyclestate': state,
    }

    if fill_fields:
        base.update({
            'defectdetectionlocation': 'Testing',
            'defectdetectionprocess': 'Review',
            'defectdetectionorga': 'EPC22',
            'defectdetectiondate': '2025-01-01',
            'occurrence': 'First Time',
            'severity': 'Major',
            'defectiveworkproducttype': 'Code',
            'defectclassification': 'Logic Error',
            'defectinjectionorga': 'EPC22',
            'defectinjectiondate': '2024-12-01',
        })
    # else: all fields empty (None)

    return base


def run_test(label: str, state: str, fill_fields: bool):
    """Run rule and print result."""
    data = make_ifd(state, fill_fields)
    rule = Rule_Ifd_DefectAttributes(data)
    result = rule.execute()

    status = "? PASS" if result.passed else "? FAIL"
    print(f"\n{'='*60}")
    print(f"Test: {label}")
    print(f"State: {state} | Fields filled: {fill_fields}")
    print(f"Result: {status} [{result.severity}]")
    print(f"Detail: {result.description[:100]}...")
    return result


if __name__ == "__main__":
    print("PRPL 06 - Testing behavior with CLOSED IFD (RQONE04984940)")
    print("="*60)

    # Case 1: Closed + fields EMPTY ? should PASS (rule skips Closed)
    r1 = run_test("Closed IFD, fields EMPTY", "Closed", fill_fields=False)

    # Case 2: Closed + fields FILLED ? should PASS
    r2 = run_test("Closed IFD, fields FILLED", "Closed", fill_fields=True)

    # Case 3: Implemented + fields EMPTY ? should FAIL (rule checks)
    r3 = run_test("Implemented IFD, fields EMPTY", "Implemented", fill_fields=False)

    # Case 4: Implemented + fields FILLED ? should PASS
    r4 = run_test("Implemented IFD, fields FILLED", "Implemented", fill_fields=True)

    # Summary
    print(f"\n{'='*60}")
    print("SUMMARY")
    print(f"  Closed + empty fields  ? {'PASS ?' if r1.passed else 'FAIL ?'} (expected: PASS)")
    print(f"  Closed + filled fields ? {'PASS ?' if r2.passed else 'FAIL ?'} (expected: PASS)")
    print(f"  Implemented + empty    ? {'PASS ?' if r3.passed else 'FAIL ?'} (expected: FAIL)")
    print(f"  Implemented + filled   ? {'PASS ?' if r4.passed else 'FAIL ?'} (expected: PASS)")

    print(f"\n{'='*60}")
    print("CONCLUSION:")
    if r1.passed:
        print("? Rule 06 SKIPS validation for Closed IFDs (correct behavior)")
        print("   Closed state is treated same as Canceled/Conflicted.")
    else:
        print("??  Rule 06 STILL validates Closed IFDs!")
        print("   Consider: should Closed IFDs be exempt from Rule 06?")
