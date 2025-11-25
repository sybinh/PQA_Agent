# Download Maven dependencies manually (if Maven not installed)

Write-Host "Downloading dependencies..."

$deps = @(
    @{url="https://repo1.maven.org/maven2/info/picocli/picocli/4.7.5/picocli-4.7.5.jar"; name="picocli-4.7.5.jar"},
    @{url="https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar"; name="gson-2.10.1.jar"},
    @{url="https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar"; name="slf4j-api-2.0.9.jar"},
    @{url="https://repo1.maven.org/maven2/ch/qos/logback/logback-classic/1.4.11/logback-classic-1.4.11.jar"; name="logback-classic-1.4.11.jar"},
    @{url="https://repo1.maven.org/maven2/ch/qos/logback/logback-core/1.4.11/logback-core-1.4.11.jar"; name="logback-core-1.4.11.jar"}
)

New-Item -ItemType Directory -Force -Path "lib" | Out-Null

foreach ($dep in $deps) {
    $outFile = "lib\$($dep.name)"
    if (-not (Test-Path $outFile)) {
        Write-Host "Downloading $($dep.name)..."
        Invoke-WebRequest -Uri $dep.url -OutFile $outFile
    } else {
        Write-Host "$($dep.name) already exists"
    }
}

Write-Host "Done! Dependencies in lib/ folder"
