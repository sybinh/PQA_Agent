# Build script - creates standalone validate_user_items.exe
# Requires PyInstaller in .venv

Write-Host "`n=== Quality Check - Build Executable ===" -ForegroundColor Cyan

# Activate virtual environment
& .\.venv\Scripts\Activate.ps1

# Check PyInstaller
Write-Host "[1/3] Checking PyInstaller..." -ForegroundColor Green
pip show pyinstaller > $null 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Installing PyInstaller..." -ForegroundColor Yellow
    pip install pyinstaller
}

# Create dist directory
if (-not (Test-Path "dist")) {
    New-Item -ItemType Directory -Path "dist" | Out-Null
}

# Build validate_user_items.exe
Write-Host "`n[2/3] Building validate_user_items.exe..." -ForegroundColor Green
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
    validate_user_items.py

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error building validate_user_items.exe" -ForegroundColor Red
    exit 1
}

# Clean up build artifacts
Write-Host "`n[3/3] Cleaning up..." -ForegroundColor Green
Remove-Item -Recurse -Force "build" -ErrorAction SilentlyContinue
Remove-Item -Force "validate_user_items.spec" -ErrorAction SilentlyContinue

# Copy to output/executables
$RELEASE_DIR = "output\executables"
Copy-Item "dist\validate_user_items.exe" "$RELEASE_DIR\validate_user_items.exe" -Force

$size = [math]::Round((Get-Item "dist\validate_user_items.exe").Length / 1MB, 1)
Write-Host "`nBuild complete: validate_user_items.exe (${size} MB)" -ForegroundColor Green
Write-Host "Copied to: $RELEASE_DIR" -ForegroundColor Yellow
