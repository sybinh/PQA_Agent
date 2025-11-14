#!/usr/bin/env python3
"""
Excel Multi-Sheet Converter
Converts Excel file with multiple sheets to various formats (CSV, Markdown, JSON)
"""

import sys
import os
from pathlib import Path
import argparse


def convert_excel(excel_path: str, output_format: str = "markdown", output_dir: str = None):
    """
    Convert Excel file to specified format
    
    Args:
        excel_path: Path to Excel file
        output_format: Output format (markdown, csv, json, all)
        output_dir: Output directory (default: same as Excel file)
    """
    try:
        import pandas as pd
    except ImportError:
        print("ERROR: pandas not installed")
        print("Run: pip install pandas openpyxl")
        sys.exit(1)
    
    # Validate input file
    excel_file = Path(excel_path)
    if not excel_file.exists():
        print(f"ERROR: File not found: {excel_path}")
        sys.exit(1)
    
    if excel_file.suffix.lower() not in ['.xlsx', '.xls', '.xlsm']:
        print(f"ERROR: Not an Excel file: {excel_path}")
        sys.exit(1)
    
    # Setup output directory
    if output_dir is None:
        output_dir = excel_file.parent
    else:
        output_dir = Path(output_dir)
        output_dir.mkdir(parents=True, exist_ok=True)
    
    base_name = excel_file.stem
    
    print(f"\n?? Converting Excel file: {excel_file.name}")
    print(f"?? Output directory: {output_dir}")
    print(f"?? Output format: {output_format}\n")
    
    # Read all sheets
    try:
        excel_data = pd.read_excel(excel_path, sheet_name=None, engine='openpyxl')
    except Exception as e:
        print(f"ERROR: Failed to read Excel file: {e}")
        print("\nTrying with xlrd engine for .xls files...")
        try:
            excel_data = pd.read_excel(excel_path, sheet_name=None, engine='xlrd')
        except Exception as e2:
            print(f"ERROR: Failed with xlrd too: {e2}")
            sys.exit(1)
    
    sheet_names = list(excel_data.keys())
    print(f"? Found {len(sheet_names)} sheets: {', '.join(sheet_names)}\n")
    
    # Convert each sheet
    for sheet_name, df in excel_data.items():
        # Clean sheet name for filename
        safe_sheet_name = "".join(c if c.isalnum() or c in ['_', '-'] else '_' for c in sheet_name)
        
        print(f"?? Processing sheet: '{sheet_name}'")
        print(f"   Rows: {len(df)}, Columns: {len(df.columns)}")
        
        # Skip empty sheets
        if df.empty:
            print(f"   ??  Skipped (empty sheet)\n")
            continue
        
        # Convert to CSV
        if output_format in ['csv', 'all']:
            csv_file = output_dir / f"{base_name}_{safe_sheet_name}.csv"
            df.to_csv(csv_file, index=False, encoding='utf-8-sig')
            print(f"   ? CSV: {csv_file.name}")
        
        # Convert to Markdown
        if output_format in ['markdown', 'all']:
            md_file = output_dir / f"{base_name}_{safe_sheet_name}.md"
            with open(md_file, 'w', encoding='utf-8') as f:
                f.write(f"# {sheet_name}\n\n")
                f.write(f"**Source**: {excel_file.name}\n")
                f.write(f"**Sheet**: {sheet_name}\n")
                f.write(f"**Rows**: {len(df)}\n")
                f.write(f"**Columns**: {len(df.columns)}\n\n")
                
                # Write table
                f.write(df.to_markdown(index=False))
                f.write("\n")
            print(f"   ? Markdown: {md_file.name}")
        
        # Convert to JSON
        if output_format in ['json', 'all']:
            json_file = output_dir / f"{base_name}_{safe_sheet_name}.json"
            df.to_json(json_file, orient='records', indent=2, force_ascii=False)
            print(f"   ? JSON: {json_file.name}")
        
        print()
    
    # Create summary file
    summary_file = output_dir / f"{base_name}_SUMMARY.md"
    with open(summary_file, 'w', encoding='utf-8') as f:
        f.write(f"# Excel Conversion Summary\n\n")
        f.write(f"**Source File**: `{excel_file.name}`\n")
        f.write(f"**Total Sheets**: {len(sheet_names)}\n")
        f.write(f"**Output Format**: {output_format}\n")
        f.write(f"**Conversion Date**: {pd.Timestamp.now().strftime('%Y-%m-%d %H:%M:%S')}\n\n")
        
        f.write(f"## Sheets Overview\n\n")
        f.write(f"| # | Sheet Name | Rows | Columns | Output Files |\n")
        f.write(f"|---|------------|------|---------|-------------|\n")
        
        for idx, (sheet_name, df) in enumerate(excel_data.items(), 1):
            safe_name = "".join(c if c.isalnum() or c in ['_', '-'] else '_' for c in sheet_name)
            files = []
            if output_format in ['csv', 'all']:
                files.append(f"`{base_name}_{safe_name}.csv`")
            if output_format in ['markdown', 'all']:
                files.append(f"`{base_name}_{safe_name}.md`")
            if output_format in ['json', 'all']:
                files.append(f"`{base_name}_{safe_name}.json`")
            
            f.write(f"| {idx} | {sheet_name} | {len(df)} | {len(df.columns)} | {', '.join(files)} |\n")
        
        f.write(f"\n## Column Details\n\n")
        for sheet_name, df in excel_data.items():
            f.write(f"### {sheet_name}\n\n")
            f.write(f"Columns ({len(df.columns)}):\n")
            for col in df.columns:
                f.write(f"- `{col}` ({df[col].dtype})\n")
            f.write(f"\n")
    
    print(f"?? Summary file created: {summary_file.name}")
    print(f"\n? Conversion complete!\n")


def main():
    parser = argparse.ArgumentParser(
        description='Convert Excel file with multiple sheets to various formats',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Convert to Markdown (default)
  python excel_converter.py data.xlsx
  
  # Convert to CSV
  python excel_converter.py data.xlsx -f csv
  
  # Convert to all formats
  python excel_converter.py data.xlsx -f all
  
  # Specify output directory
  python excel_converter.py data.xlsx -o output/
        """
    )
    
    parser.add_argument('excel_file', help='Path to Excel file (.xlsx, .xls)')
    parser.add_argument('-f', '--format', 
                       choices=['markdown', 'csv', 'json', 'all'],
                       default='markdown',
                       help='Output format (default: markdown)')
    parser.add_argument('-o', '--output-dir',
                       help='Output directory (default: same as Excel file)')
    parser.add_argument('-v', '--version', action='version', version='%(prog)s 1.0')
    
    args = parser.parse_args()
    
    # Check dependencies
    try:
        import pandas
        import openpyxl
    except ImportError as e:
        print("ERROR: Missing required packages")
        print("\nInstall with:")
        print("  pip install pandas openpyxl")
        print("\nFor older .xls files, also install:")
        print("  pip install xlrd")
        sys.exit(1)
    
    convert_excel(args.excel_file, args.format, args.output_dir)


if __name__ == '__main__':
    main()
