#!/usr/bin/env python3
"""
Extract VBA code from Excel macro file
"""
import win32com.client
import os
from pathlib import Path

def extract_vba_from_excel(excel_path: str, output_dir: str = "rq1/vba_extracted"):
    """Extract VBA code from Excel file"""
    
    excel_path = os.path.abspath(excel_path)
    output_dir = Path(output_dir)
    output_dir.mkdir(parents=True, exist_ok=True)
    
    print(f"Extracting VBA from: {excel_path}")
    
    # Open Excel
    excel = win32com.client.Dispatch("Excel.Application")
    excel.Visible = False
    excel.DisplayAlerts = False
    
    try:
        # Open workbook
        workbook = excel.Workbooks.Open(excel_path)
        
        # Get VBA project
        vba_project = workbook.VBProject
        
        print(f"\nFound {vba_project.VBComponents.Count} VBA components:")
        
        # Extract each VBA component
        for component in vba_project.VBComponents:
            comp_name = component.Name
            comp_type = component.Type
            
            type_names = {
                1: "Module",
                2: "Class",
                3: "Form",
                100: "Document"
            }
            
            type_name = type_names.get(comp_type, "Unknown")
            print(f"  - {comp_name} ({type_name})")
            
            # Export component
            if comp_type in [1, 2]:  # Module or Class
                output_file = output_dir / f"{comp_name}.bas"
                try:
                    component.Export(str(output_file))
                    print(f"    ? Exported to: {output_file}")
                    
                    # Also read and print content
                    code_module = component.CodeModule
                    if code_module.CountOfLines > 0:
                        code = code_module.Lines(1, code_module.CountOfLines)
                        
                        # Save as .vba for better readability
                        vba_file = output_dir / f"{comp_name}.vba"
                        vba_file.write_text(code, encoding='utf-8')
                        print(f"    ? Saved readable version: {vba_file}")
                        
                except Exception as e:
                    print(f"    ? Error exporting: {e}")
        
        workbook.Close(SaveChanges=False)
        print(f"\n? VBA extraction complete!")
        print(f"Output directory: {output_dir}")
        
    except Exception as e:
        print(f"? Error: {e}")
        import traceback
        traceback.print_exc()
    
    finally:
        excel.Quit()

if __name__ == "__main__":
    excel_file = r"C:\Users\DAB5HC\Documents\Documents\workspace\Tool\RQ1_agent\rq1\check_QAMi.xlsm"
    extract_vba_from_excel(excel_file)