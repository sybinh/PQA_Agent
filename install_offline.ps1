# PQA Validator - Offline Installation Script
# No GitHub/Bosch Artifactory access needed!

Write-Host "Installing PQA Validator (Offline Mode)..." -ForegroundColor Cyan

# Detect best Python version (prefer 3.11+ via py launcher)
$pythonCmd = "python"
$pythonVersion = ""

# Try py launcher first (Windows Python Launcher)
if (Get-Command py -ErrorAction SilentlyContinue) {
    Write-Host "Checking available Python versions via py launcher..." -ForegroundColor Gray
    $pyList = py --list 2>&1 | Out-String
    
    # Check for Python 3.11 or 3.12
    if ($pyList -match '3\.1[12]') {
        $pythonCmd = "py -3.11"
        $pythonVersion = py -3.11 --version 2>&1
        Write-Host "Found: $pythonVersion (via py launcher)" -ForegroundColor Green
    }
    elseif ($pyList -match '3\.10') {
        $pythonCmd = "py -3.10"
        $pythonVersion = py -3.10 --version 2>&1
        Write-Host "Found: $pythonVersion (via py launcher)" -ForegroundColor Green
    }
}

# Fallback to default python command
if (-not $pythonVersion) {
    if (!(Get-Command python -ErrorAction SilentlyContinue)) {
        Write-Host "ERROR: Python not found. Please install Python 3.10+ first." -ForegroundColor Red
        Write-Host "Download from: https://python.org" -ForegroundColor Yellow
        exit 1
    }
    
    $pythonVersion = python --version 2>&1
    Write-Host "Found: $pythonVersion" -ForegroundColor Green
}

# Extract and validate version number
$versionMatch = $pythonVersion -match 'Python (\d+)\.(\d+)'
if ($versionMatch) {
    $major = [int]$Matches[1]
    $minor = [int]$Matches[2]
    
    if ($major -lt 3 -or ($major -eq 3 -and $minor -lt 10)) {
        Write-Host "`nERROR: Python 3.10 or higher required!" -ForegroundColor Red
        Write-Host "You have: Python $major.$minor" -ForegroundColor Yellow
        Write-Host "`nSolutions:" -ForegroundColor Cyan
        Write-Host "1. Install Python 3.11+ from: https://python.org" -ForegroundColor Yellow
        Write-Host "2. Or if you have Python 3.11+ installed, try:" -ForegroundColor Yellow
        Write-Host "   py -3.11 -m venv .venv" -ForegroundColor Gray
        exit 1
    }
}

# Create venv with detected Python
Write-Host "`nCreating virtual environment with $pythonCmd..." -ForegroundColor Cyan
if ($pythonCmd -like "py -*") {
    Invoke-Expression "$pythonCmd -m venv .venv"
} else {
    python -m venv .venv
}

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to create virtual environment" -ForegroundColor Red
    exit 1
}

# Activate venv
Write-Host "Activating virtual environment..." -ForegroundColor Cyan
& .\.venv\Scripts\Activate.ps1

# Verify activation
$venvPython = .\.venv\Scripts\python.exe --version 2>&1
Write-Host "Virtual environment Python: $venvPython" -ForegroundColor Green

# Install from wheels (offline)
Write-Host "`nInstalling packages from local wheels..." -ForegroundColor Cyan
Write-Host "(This includes building-block-rq1 - no GitHub access needed!)" -ForegroundColor Yellow
.\.venv\Scripts\pip.exe install --no-index --find-links=wheels -r requirements.txt

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n SUCCESS! Installation complete." -ForegroundColor Green
    Write-Host "`nInstalled packages:" -ForegroundColor Cyan
    .\.venv\Scripts\pip.exe list | Select-String "building-block|fastmcp|mcp|dotenv"
    
    Write-Host "`nNext steps:" -ForegroundColor Yellow
    Write-Host "1. Copy .env.template to .env"
    Write-Host "   cp .env.template .env"
    Write-Host ""
    Write-Host "2. Edit .env with your RQ1 credentials"
    Write-Host "   notepad .env"
    Write-Host ""
    Write-Host "3. Configure VS Code (see QUICKSTART.md)"
    Write-Host ""
    Write-Host "4. Test the server:"
    Write-Host "   .\.venv\Scripts\python.exe mcp_server_fast.py" -ForegroundColor Cyan
} else {
    Write-Host "`n ERROR: Installation failed!" -ForegroundColor Red
    Write-Host "Check error messages above." -ForegroundColor Yellow
    Write-Host "`nCommon issues:" -ForegroundColor Cyan
    Write-Host "- Python version < 3.10: Update to Python 3.11+" -ForegroundColor Yellow
    Write-Host "- Old .venv exists: Remove it and run script again" -ForegroundColor Yellow
}
