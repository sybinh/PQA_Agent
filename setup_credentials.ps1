# Setup RQ1 Credentials (Secure)
# Prompts for password (masked) and stores in PowerShell environment variables
# Credentials persist only for current PowerShell session

Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host "RQ1 Credentials Setup (Secure)" -ForegroundColor Cyan
Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host ""

# Get username (read from .env if exists)
$envUser = if (Test-Path .env) { 
    Get-Content .env | Select-String "^RQ1_USER=" | ForEach-Object { $_.ToString().Split('=')[1].Trim() }
} else { 
    $null 
}

if ($envUser) {
    Write-Host "Username from .env: $envUser" -ForegroundColor Gray
    $username = $envUser
} else {
    $username = Read-Host "RQ1 Username"
    if ([string]::IsNullOrWhiteSpace($username)) {
        Write-Host "ERROR: Username is required!" -ForegroundColor Red
        exit 1
    }
}

# Get password (masked input, no confirmation)
Write-Host ""
$password = Read-Host "RQ1 Password" -AsSecureString
$passwordPlain = [Runtime.InteropServices.Marshal]::PtrToStringAuto(
    [Runtime.InteropServices.Marshal]::SecureStringToBSTR($password)
)

if ([string]::IsNullOrWhiteSpace($passwordPlain)) {
    Write-Host "ERROR: Password is required!" -ForegroundColor Red
    exit 1
}

# Store in environment variables (session-level)
$env:RQ1_USER = $username
$env:RQ1_PASSWORD = $passwordPlain

Write-Host ""
Write-Host "================================================================================" -ForegroundColor Green
Write-Host " Credentials stored in environment variables (session only)" -ForegroundColor Green
Write-Host "================================================================================" -ForegroundColor Green
Write-Host ""
Write-Host "Environment variables set:" -ForegroundColor Cyan
Write-Host "  RQ1_USER = $username" -ForegroundColor Gray
Write-Host "  RQ1_PASSWORD = ******** (hidden)" -ForegroundColor Gray
Write-Host ""
Write-Host "You can now run:" -ForegroundColor Yellow
Write-Host "  python validate_user_items.py <username>" -ForegroundColor Cyan
Write-Host "  python mcp_server_fast.py" -ForegroundColor Cyan
Write-Host ""
Write-Host "Note: Credentials will be cleared when you close this terminal." -ForegroundColor Yellow
Write-Host "      Re-run this script after reopening terminal." -ForegroundColor Yellow
Write-Host ""
