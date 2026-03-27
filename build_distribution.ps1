#!/usr/bin/env pwsh
# Build Distribution Package for RQ1 PRPL Validation Tool
# Creates pqa_validator_mcp.zip for distribution to other users

$ErrorActionPreference = "Stop"

Write-Host "=" * 80 -ForegroundColor Cyan
Write-Host "Building RQ1 PRPL Validation Tool - Distribution Package" -ForegroundColor Cyan
Write-Host "=" * 80 -ForegroundColor Cyan
Write-Host ""

# Define output file
$outputZip = "pqa_validator_mcp.zip"

# Remove old zip if exists
if (Test-Path $outputZip) {
    Write-Host "Removing old distribution package..." -ForegroundColor Yellow
    Remove-Item $outputZip -Force
}

# Files and folders to include
$includeItems = @(
    # Core files (Python source)
    "mcp_server_fast.py",
    "validate_user_items.py",
    "validate_user_items_api.py",
    
    # Setup scripts
    "setup_credentials.ps1",
    "setup_credentials.py",
    "install_offline.ps1",
    
    # Configuration
    ".env.example",
    "requirements.txt",
    
    # Source code
    "src",
    
    # Dependencies (offline install)
    "wheels",
    
    # Documentation
    "README.md",
    "QUICKSTART.md",
    "INSTALL.md",
    "VSCODE_SETUP.md",
    
    # Docs folder
    "docs"
)

Write-Host "Files to include:" -ForegroundColor Green
foreach ($item in $includeItems) {
    if (Test-Path $item) {
        Write-Host "  ? $item" -ForegroundColor Green
    } else {
        Write-Host "  ? $item (NOT FOUND)" -ForegroundColor Red
    }
}
Write-Host ""

# Create temporary directory for packaging
$tempDir = "temp_distribution"
if (Test-Path $tempDir) {
    Remove-Item $tempDir -Recurse -Force
}
New-Item -ItemType Directory -Path $tempDir | Out-Null

Write-Host "Copying files to temporary directory..." -ForegroundColor Yellow

# Copy each item
foreach ($item in $includeItems) {
    if (Test-Path $item) {
        if (Test-Path $item -PathType Container) {
            # It's a directory
            Copy-Item -Path $item -Destination $tempDir -Recurse -Force
        } else {
            # It's a file
            Copy-Item -Path $item -Destination $tempDir -Force
        }
    }
}

# Create the zip file
Write-Host "Creating distribution package: $outputZip..." -ForegroundColor Yellow
Compress-Archive -Path "$tempDir\*" -DestinationPath $outputZip -Force

# Clean up temp directory
Remove-Item $tempDir -Recurse -Force

# Get file size
$zipSize = (Get-Item $outputZip).Length
$zipSizeKB = [math]::Round($zipSize / 1KB, 2)
$zipSizeMB = [math]::Round($zipSize / 1MB, 2)

Write-Host ""
Write-Host "=" * 80 -ForegroundColor Green
Write-Host "? Distribution package created successfully!" -ForegroundColor Green
Write-Host "=" * 80 -ForegroundColor Green
Write-Host ""
Write-Host "Package: $outputZip" -ForegroundColor Cyan
Write-Host "Size: $zipSizeKB KB ($zipSizeMB MB)" -ForegroundColor Cyan
Write-Host ""
Write-Host "Ready for distribution!" -ForegroundColor Green
Write-Host ""
