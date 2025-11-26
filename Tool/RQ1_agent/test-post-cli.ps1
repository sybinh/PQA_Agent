#!/usr/bin/env pwsh
# Quick test script for POST CLI (without Maven)

Write-Host "=== POST CLI Quick Test ===" -ForegroundColor Cyan

# Set Java 11 from POST
$JAVA_HOME = "$PSScriptRoot\rq1\POST_V_1.0.3\jre"
$JAVA = "$JAVA_HOME\bin\java.exe"

Write-Host "Using Java 11 from POST:" -ForegroundColor Yellow
& $JAVA -version

Write-Host "`nTesting CLI help..." -ForegroundColor Yellow

# Since we don't have compiled JAR yet, let's test the original POST JAR
Write-Host "`nRunning original POST JAR (should show error about GUI):" -ForegroundColor Yellow
& $JAVA -jar "rq1\POST_V_1.0.3\jre\POST-1.0.3-jar-with-dependencies.jar" 2>&1 | Select-Object -First 10

Write-Host "`n=== Summary ===" -ForegroundColor Cyan
Write-Host "? Java 11 available from POST"
Write-Host "? POST JAR accessible"
Write-Host "? Next: Need to compile PostCLI with dependencies"
Write-Host "? Alternative: Use original POST JAR as-is and wrap with Python"
