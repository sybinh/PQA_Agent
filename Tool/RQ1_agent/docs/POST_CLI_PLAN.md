# POST CLI Wrapper - Implementation Plan

## Overview

Convert POST Tool (JavaFX GUI) to CLI-based validation engine for MCP integration.

## Current Analysis

### Extracted Source Code
- **Location**: `DataModel/Monitoring/` - 40+ rule implementations
- **Key Files**:
  - `Rule_IssueSW_FmeaCheck.java` - Example rule (clean, simple logic)
  - `DmRule.java` - Base rule class
  - `FnCCat_ToolLauncher.java` - JavaFX launcher (to be replaced)
  
### Architecture Discovery

```
POST Tool Current Structure:
???????????????????????????????????
?  FnCCat_ToolLauncher (JavaFX)   ?  ? GUI Entry Point
?  - Project selection dialog     ?
?  - Login prompts                 ?
?  - Progress alerts               ?
???????????????????????????????????
           ?
???????????????????????????????????
?  OslcRq1Client                   ?  ? RQ1 Connection
?  - Authentication                ?
?  - OSLC queries                  ?
???????????????????????????????????
           ?
???????????????????????????????????
?  DmRule (Base Class)             ?  ? Rule Engine
?  - executeRule()                 ?
?  - addMarker()                   ?
???????????????????????????????????
           ?
???????????????????????????????????
?  40+ Rule Implementations        ?  ? Business Logic
?  - Rule_IssueSW_FmeaCheck        ?
?  - Rule_IssueSW_ASIL             ?
?  - Rule_Bc_Conflicted            ?
?  - ...                           ?
???????????????????????????????????
```

## Implementation Plan

### Phase 1: Extract & Analyze (2-3 hours)

**Status**: ? COMPLETE
- [x] Extract JAR contents
- [x] Identify source code availability (Java sources included!)
- [x] Locate rule implementations (DataModel/Monitoring/)
- [x] Understand architecture (OslcRq1Client ? DmRule ? Rule_*)

### Phase 2: Create CLI Launcher (3-5 hours)

**Goal**: Replace JavaFX launcher with CLI argument parser

**New File**: `src/PostCLI.java`

```java
package cli;

import DataModel.Monitoring.*;
import DataModel.Rq1.Records.*;
import OslcAccess.Rq1.OslcRq1Client;
import com.google.gson.Gson;
import java.util.*;

public class PostCLI {
    
    public static void main(String[] args) {
        // Parse CLI arguments
        CliArgs cliArgs = parseArgs(args);
        
        // Initialize RQ1 client (headless)
        OslcRq1Client client = initializeClient(
            cliArgs.username,
            cliArgs.password,
            cliArgs.environment
        );
        
        // Fetch RQ1 record
        DmRq1IssueSW issue = client.getRecord(cliArgs.rq1Number);
        
        // Execute rules
        List<ValidationResult> results = executeRules(issue, cliArgs.ruleIds);
        
        // Output JSON
        Gson gson = new Gson();
        System.out.println(gson.toJson(results));
    }
    
    static class CliArgs {
        String username;
        String password;
        String environment;
        String rq1Number;
        List<String> ruleIds;
    }
    
    static class ValidationResult {
        String ruleId;
        String ruleName;
        String status; // PASS, FAIL, WARNING
        String message;
        Map<String, Object> details;
    }
}
```

**Usage Example**:
```bash
java -jar post-cli.jar \
  --username "your_ntid" \
  --password "your_pass" \
  --env "ACCEPTANCE" \
  --rq1 "RQ100123" \
  --rules "Rule_IssueSW_FmeaCheck,Rule_IssueSW_ASIL" \
  --format json
```

### Phase 3: Refactor Dependencies (5-8 hours)

**Remove**:
- JavaFX dependencies (javafx.application, javafx.stage, etc.)
- GUI components (Alert, Dialog, Stage)
- SharePoint file downloaders (FnCCat_SharePointResourceFileDownloader)
- Resource management for GUI

**Keep**:
- OslcRq1Client (RQ1 connection)
- DmRule and all Rule_* classes
- Data models (DmRq1IssueSW, etc.)
- Validation logic

**Add**:
- CLI argument parser (Apache Commons CLI or picocli)
- JSON output library (Gson or Jackson)
- Logging framework (SLF4J instead of JavaFX alerts)

### Phase 4: Build Configuration (2-3 hours)

**Create**: `pom.xml` for Maven build

```xml
<project>
    <groupId>com.bosch</groupId>
    <artifactId>post-cli</artifactId>
    <version>2.0.0</version>
    
    <dependencies>
        <!-- Keep existing RQ1 dependencies -->
        <dependency>
            <groupId>org.eclipse.ldt.core</groupId>
            <artifactId>oslc-client</artifactId>
        </dependency>
        
        <!-- Remove JavaFX -->
        <!-- <dependency><artifactId>javafx-controls</artifactId></dependency> -->
        
        <!-- Add CLI parsing -->
        <dependency>
            <groupId>info.picocli</groupId>
            <artifactId>picocli</artifactId>
            <version>4.7.5</version>
        </dependency>
        
        <!-- Add JSON -->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>cli.PostCLI</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Phase 5: Test & Package (2-3 hours)

**Test scenarios**:
1. Single rule validation
2. Multiple rules validation
3. Invalid RQ1 number handling
4. Authentication failure handling
5. JSON output format validation

**Build**:
```bash
mvn clean package
# Output: target/post-cli-2.0.0-jar-with-dependencies.jar
```

## Integration with PQA_Agent

### Python Wrapper (1-2 hours)

**File**: `src/post_cli_wrapper.py`

```python
import subprocess
import json
from typing import List, Dict

class PostCLIWrapper:
    def __init__(self, jar_path: str, username: str, password: str, env: str = "ACCEPTANCE"):
        self.jar_path = jar_path
        self.username = username
        self.password = password
        self.env = env
    
    def validate_issue(self, rq1_number: str, rule_ids: List[str]) -> Dict:
        """Run POST CLI validation"""
        cmd = [
            "java", "-jar", self.jar_path,
            "--username", self.username,
            "--password", self.password,
            "--env", self.env,
            "--rq1", rq1_number,
            "--rules", ",".join(rule_ids),
            "--format", "json"
        ]
        
        result = subprocess.run(cmd, capture_output=True, text=True, check=True)
        return json.loads(result.stdout)
```

### MCP Integration

```python
# In rq1_server.py
from post_cli_wrapper import PostCLIWrapper

# Initialize wrapper
post_cli = PostCLIWrapper(
    jar_path="rq1/POST_V_1.0.3/post-cli-2.0.0.jar",
    username=os.getenv("RQ1_USER"),
    password=os.getenv("RQ1_PASSWORD")
)

@server.call_tool()
async def validate_rules(rq1_number: str, rule_ids: List[str]):
    """Validate RQ1 issue using POST rules"""
    results = post_cli.validate_issue(rq1_number, rule_ids)
    return results
```

## Time Estimate

| Phase | Task | Hours | Status |
|-------|------|-------|--------|
| 1 | Extract & Analyze | 2-3 | ? DONE |
| 2 | Create CLI Launcher | 3-5 | ?? NEXT |
| 3 | Refactor Dependencies | 5-8 | ? |
| 4 | Build Configuration | 2-3 | ? |
| 5 | Test & Package | 2-3 | ? |
| 6 | Python Wrapper | 1-2 | ? |
| **TOTAL** | | **15-24 hours** | |

## Next Steps

1. Create `cli/PostCLI.java` with argument parsing
2. Test with single rule (Rule_IssueSW_FmeaCheck)
3. Gradually add more rules
4. Build and test JAR
5. Integrate with Python MCP server

## Benefits vs Re-implementation

| Approach | Effort | Pros | Cons |
|----------|--------|------|------|
| **CLI Wrapper** | 15-24h | ? Reuse proven logic<br>? All 37 rules<br>? Faster | ?? Java dependency<br>?? Larger artifact |
| **Re-implement** | 30-40h | ? Pure Python<br>? Better control | ?? More work<br>?? Need re-testing |

**Recommendation**: Proceed with CLI Wrapper approach
