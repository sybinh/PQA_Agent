# POST CLI - Command Line Interface

Command-line wrapper for POST validation rules (replaces JavaFX GUI).

## Prerequisites

- Java 11 or higher
- Maven 3.6+ (for building)
- RQ1 credentials

## Project Structure

```
post-cli/
??? pom.xml                           # Maven build configuration
??? src/
?   ??? main/
?       ??? java/
?       ?   ??? cli/
?       ?       ??? PostCLI.java     # CLI entry point
?       ??? resources/
?           ??? logback.xml          # Logging configuration
??? target/                           # Build output (after mvn package)
    ??? post-cli-2.0.0-jar-with-dependencies.jar
```

## Building

### Step 1: Install Maven

**Windows (with Chocolatey)**:
```powershell
choco install maven
```

**Or download manually**:
- Download from https://maven.apache.org/download.cgi
- Extract to C:\tools\maven
- Add to PATH: `C:\tools\maven\bin`

### Step 2: Build JAR

```bash
cd post-cli
mvn clean package
```

Output: `target/post-cli-2.0.0-jar-with-dependencies.jar`

## Usage

### Basic Command

```bash
java -jar target/post-cli-2.0.0-jar-with-dependencies.jar \
  --username YOUR_NTID \
  --password YOUR_PASSWORD \
  --env ACCEPTANCE \
  RQ100123 \
  --rules "Rule_IssueSW_FmeaCheck,Rule_IssueSW_ASIL"
```

### Options

```
Usage: post-cli [-hVv] [-e=<environment>] [-f=<outputFormat>] -p[=<password>]
                [-r=<ruleIds>[,<ruleIds>...]]... -u=<username> <rq1Number>

Validate RQ1 issues using POST rules

Parameters:
      <rq1Number>         RQ1 issue number (e.g., RQ100123)

Options:
  -u, --username=<username>
                          RQ1 username (NTID)
  -p, --password[=<password>]
                          RQ1 password
  -e, --env, --environment=<environment>
                          RQ1 environment (ACCEPTANCE or PRODUCTIVE)
                          Default: ACCEPTANCE
  -r, --rules=<ruleIds>[,<ruleIds>...]
                          Comma-separated rule IDs to execute (or 'all')
                          Default: all
  -f, --format=<outputFormat>
                          Output format: json, text, summary
                          Default: json
  -v, --verbose           Verbose output
  -h, --help              Show this help message and exit.
  -V, --version           Print version information and exit.
```

### Examples

**Validate with specific rules (JSON output)**:
```bash
java -jar post-cli.jar -u dab5hc -p password -e ACCEPTANCE RQ100123 \
  --rules "Rule_IssueSW_FmeaCheck,Rule_IssueSW_ASIL" \
  --format json
```

**Validate all rules (text output)**:
```bash
java -jar post-cli.jar -u dab5hc -p password RQ100123 \
  --rules all \
  --format text
```

**Summary only**:
```bash
java -jar post-cli.jar -u dab5hc -p password RQ100123 --format summary
```

**With verbose logging**:
```bash
java -jar post-cli.jar -u dab5hc -p password RQ100123 --verbose
```

## Output Formats

### JSON (default)
```json
{
  "rq1Number": "RQ100123",
  "timestamp": "Mon Nov 25 10:00:00 GMT 2025",
  "totalRules": 2,
  "passed": 1,
  "failed": 0,
  "warnings": 1,
  "results": [
    {
      "ruleId": "Rule_IssueSW_FmeaCheck",
      "ruleName": "Comment for not required FMEA on I-SW",
      "status": "PASS",
      "message": "FMEA comment is present",
      "details": {
        "fmea_state": "NOT_REQUIRED",
        "fmea_comment": "Not applicable"
      }
    },
    {
      "ruleId": "Rule_IssueSW_ASIL",
      "ruleName": "ASIL level validation",
      "status": "WARNING",
      "message": "ASIL level not set",
      "details": {
        "asil_level": null
      }
    }
  ]
}
```

### Text
```
=== POST Validation Results ===
RQ1 Number: RQ100123
Environment: ACCEPTANCE

[PASS] Rule_IssueSW_FmeaCheck: FMEA comment is present
[WARNING] Rule_IssueSW_ASIL: ASIL level not set
```

### Summary
```
Total: 2 | Passed: 1 | Failed: 0 | Warnings: 1
```

## Exit Codes

- `0` - Success (all rules passed)
- `1` - Validation failures or warnings
- `2` - Error (connection, authentication, etc.)

## Integration with Python

### Python Wrapper

```python
import subprocess
import json

def validate_rq1_issue(rq1_number, rules, username, password):
    cmd = [
        "java", "-jar", "post-cli-2.0.0-jar-with-dependencies.jar",
        "--username", username,
        "--password", password,
        "--env", "ACCEPTANCE",
        rq1_number,
        "--rules", ",".join(rules),
        "--format", "json"
    ]
    
    result = subprocess.run(cmd, capture_output=True, text=True)
    return json.loads(result.stdout)

# Usage
results = validate_rq1_issue(
    "RQ100123",
    ["Rule_IssueSW_FmeaCheck", "Rule_IssueSW_ASIL"],
    "your_ntid",
    "your_password"
)
print(f"Passed: {results['passed']}, Failed: {results['failed']}")
```

## Current Status

**Phase 1**: ? CLI skeleton with argument parsing
**Phase 2**: ? Integrate POST validation logic
**Phase 3**: ? Connect to RQ1 via OSLC
**Phase 4**: ? Test with real issues

## Next Steps

1. Copy validation logic from extracted POST JAR
2. Integrate OslcRq1Client for RQ1 connection
3. Implement rule execution engine
4. Test with ACCEPTANCE environment
5. Package and deploy

## Development

### Test without Maven

You can compile and run manually:

```bash
# Compile
javac -cp "picocli-4.7.5.jar;gson-2.10.1.jar" src/main/java/cli/PostCLI.java

# Run
java -cp "picocli-4.7.5.jar;gson-2.10.1.jar;src/main/java" cli.PostCLI --help
```

### Debug

Add `-Dlogback.debug=true` for logging debug:
```bash
java -Dlogback.debug=true -jar post-cli.jar -u user -p pass RQ100123
```
