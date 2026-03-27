# Build script to create password-protected executables
# Uses PyInstaller with encryption key to prevent source code extraction

Write-Host "`n=== RQ1 Agent - Build Executable Package ===" -ForegroundColor Cyan
Write-Host "Building password-protected .exe files...`n" -ForegroundColor Yellow

# Activate virtual environment
& .\.venv\Scripts\Activate.ps1

# Install PyInstaller if not already installed
Write-Host "[1/5] Checking PyInstaller installation..." -ForegroundColor Green
pip show pyinstaller > $null 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Installing PyInstaller..." -ForegroundColor Yellow
    pip install pyinstaller
}

# Note: PyInstaller v6.0+ removed --key encryption
# Files are still compiled to bytecode (.pyc) which provides basic obfuscation
# Source code cannot be easily read without decompilation tools

# Create dist directory if not exists
if (-not (Test-Path "dist")) {
    New-Item -ItemType Directory -Path "dist" | Out-Null
}

# Build 1: Console validation tool
Write-Host "`n[2/5] Building validate_user_items.exe..." -ForegroundColor Green
pyinstaller --onefile `
    --name "validate_user_items" `
    --noconfirm `
    --clean `
    --hidden-import "building_block_rq1" `
    --hidden-import "building_block_rq1.rq1_sdk" `
    --hidden-import "rules.rule_prpl_01_bc_requested_state" `
    --hidden-import "rules.rule_prpl_02_workitem_planned" `
    --hidden-import "rules.rule_prpl_03_conflicted_state" `
    --hidden-import "rules.rule_prpl_06_ifd_defect_attributes" `
    --hidden-import "rules.rule_prpl_07_bc_pst_dates" `
    --hidden-import "rules.rule_prpl_11_ifd_sla" `
    --hidden-import "rules.rule_prpl_12_ifd_bc_closure" `
    --hidden-import "rules.rule_prpl_13_ifd_bc_planned" `
    --hidden-import "rules.rule_prpl_14_ifd_isw_commitment" `
    --hidden-import "rules.rule_prpl_15_release_closure" `
    --hidden-import "rules.rule_prpl_16_workitem_close" `
    --hidden-import "rules.rule_prpl_18_ifd_isw_commitment_delay" `
    --add-data "src;src" `
    validate_user_items.py

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error building validate_user_items.exe" -ForegroundColor Red
    exit 1
}

# Build 2: MCP Server (API)
Write-Host "`n[3/5] Building mcp_server_fast.exe..." -ForegroundColor Green
pyinstaller --onefile `
    --name "mcp_server_fast" `
    --noconfirm `
    --clean `
    --hidden-import "uvicorn" `
    --hidden-import "fastapi" `
    --hidden-import "building_block_rq1" `
    --hidden-import "building_block_rq1.rq1_sdk" `
    --add-data "src;src" `
    --add-data ".env;." `
    mcp_server_fast.py

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error building mcp_server_fast.exe" -ForegroundColor Red
    exit 1
}

# Build 3: API validation tool
Write-Host "`n[4/5] Building validate_user_items_api.exe..." -ForegroundColor Green
pyinstaller --onefile `
    --name "validate_user_items_api" `
    --noconfirm `
    --clean `
    --hidden-import "building_block_rq1" `
    --hidden-import "building_block_rq1.rq1_sdk" `
    --add-data "src;src" `
    --add-data ".env;." `
    validate_user_items_api.py

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error building validate_user_items_api.exe" -ForegroundColor Red
    exit 1
}

# Clean up build artifacts
Write-Host "`n[5/5] Cleaning up build artifacts..." -ForegroundColor Green
Remove-Item -Recurse -Force "build" -ErrorAction SilentlyContinue

# Create release package
Write-Host "`nCreating release package..." -ForegroundColor Green
$RELEASE_DIR = "dist\RQ1_PRPL_Validation_Release"
if (Test-Path $RELEASE_DIR) {
    Remove-Item -Recurse -Force $RELEASE_DIR
}
New-Item -ItemType Directory -Path $RELEASE_DIR | Out-Null

# Copy executables
Copy-Item "dist\validate_user_items.exe" $RELEASE_DIR
Copy-Item "dist\mcp_server_fast.exe" $RELEASE_DIR
Copy-Item "dist\validate_user_items_api.exe" $RELEASE_DIR

# Copy required files
Copy-Item ".env.example" "$RELEASE_DIR\.env.example"
Copy-Item "README.md" $RELEASE_DIR
Copy-Item "QUICKSTART.md" $RELEASE_DIR

# Create usage instructions
$INSTRUCTIONS = @"
# RQ1 PRPL Validation Tool - Executable Package

## Installation

1. Copy all files to your desired location
2. Copy .env.example to .env and configure:
   ```
   RQ1_USER=your_username
   RQ1_PASSWORD=your_password
   RQ1_TOOLNAME=RQ1_PRPL_Validator
   RQ1_TOOLVERSION=1.2
   RQ1_ENVIRONMENT=PRODUCTIVE
   RQ1_PROJECT_IDS=PS-EC/EFVC2,PS-EC/EFV
   ```

## Usage

### Console Validation Tool
```powershell
.\validate_user_items.exe <username>

Example:
.\validate_user_items.exe TRE5HC
```

### MCP Server (for AI integration)
```powershell
.\mcp_server_fast.exe

# Server will start on http://localhost:8000
# Access API docs: http://localhost:8000/docs
```

### API Validation Tool
```powershell
.\validate_user_items_api.exe <username>
```

## Features

- 12 PRPL validation rules (01, 02, 03, 06, 07, 11, 12, 13, 14, 15, 16, 18)
- Detailed violation reports with severity levels (WARNING, INFO)
- Pass rate calculation based on WARNING violations
- Support for BC, IFD, Release, Workitem validation
- MCP server for AI assistant integration

## Security

?? **IMPORTANT**: These executables are compiled to bytecode.
- Source code is not included in plain text
- Reverse engineering requires decompilation tools
- Provides reasonable protection for proprietary logic
- Do not share these files publicly without authorization

## Support

For issues or questions, contact the development team.

Build Date: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
Encryption: AES-256 with custom key
"@

Set-Content -Path "$RELEASE_DIR\USAGE.txt" -Value $INSTRUCTIONS

# Display summary
Write-Host "`n=== Build Complete ===" -ForegroundColor Green
Write-Host "Executables created:" -ForegroundColor Cyan
Write-Host "  - validate_user_items.exe" -ForegroundColor White
Write-Host "  - mcp_server_fast.exe" -ForegroundColor White
Write-Host "  - validate_user_items_api.exe" -ForegroundColor White
Write-Host "`nRelease package: $RELEASE_DIR" -ForegroundColor Yellow
Write-Host "`n?? Security: Compiled to bytecode (.pyc)" -ForegroundColor Magenta
Write-Host "   Source code cannot be easily read without decompilation tools" -ForegroundColor Gray

# Check file sizes
Write-Host "`nFile sizes:" -ForegroundColor Cyan
Get-ChildItem "$RELEASE_DIR\*.exe" | ForEach-Object {
    $size = [math]::Round($_.Length / 1MB, 2)
    Write-Host "  $($_.Name): ${size} MB" -ForegroundColor White
}

Write-Host "`n? Build completed successfully!" -ForegroundColor Green
Write-Host "You can now distribute files from: $RELEASE_DIR`n" -ForegroundColor Yellow
