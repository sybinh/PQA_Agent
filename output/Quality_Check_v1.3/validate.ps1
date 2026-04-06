# Quality Check Tool - PowerShell Wrapper with Auto Password Caching
# Prompts for password once per terminal session, caches for subsequent validations

param(
    [Parameter(Mandatory=$true, Position=0)]
    [string]$Username
)

# Check if password is already cached in session
if (-not $env:RQ1_PASSWORD) {
    Write-Host "?? Enter password once for this terminal session" -ForegroundColor Yellow
    
    # Prompt for password securely (masked input)
    $securePassword = Read-Host "Enter RQ1 password" -AsSecureString
    
    # Convert SecureString to plain text for environment variable
    $BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    $env:RQ1_PASSWORD = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)
    [System.Runtime.InteropServices.Marshal]::ZeroFreeBSTR($BSTR)
    
    Write-Host "? Password cached for this terminal session" -ForegroundColor Green
    Write-Host ""
} else {
    Write-Host "? Using cached password from session" -ForegroundColor Green
}

# Run the executable
$exePath = Join-Path $PSScriptRoot "validate_user_items.exe"
& $exePath $Username

# Check exit code
$exitCode = $LASTEXITCODE

if ($exitCode -ne 0) {
    # Failed - check if it was authentication error and clear cache
    if ($env:RQ1_PASSWORD) {
        Write-Host "`n? Validation failed. If password was incorrect, clearing cache..." -ForegroundColor Red
        Write-Host "?? Run again to re-enter password" -ForegroundColor Yellow
        Remove-Item Env:\RQ1_PASSWORD -ErrorAction SilentlyContinue
    }
}

exit $exitCode
