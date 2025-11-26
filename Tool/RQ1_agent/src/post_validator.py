#!/usr/bin/env python3
"""
POST CLI Wrapper - Python interface to POST validation tool

Since POST JAR requires JavaFX GUI, this wrapper provides CLI interface
by programmatically controlling the validation logic.
"""

import subprocess
import json
import os
from typing import List, Dict, Optional
from pathlib import Path


class PostValidator:
    """Wrapper for POST validation tool"""
    
    def __init__(self, java_home: Optional[str] = None):
        """
        Initialize POST validator
        
        Args:
            java_home: Path to Java 11 installation (uses POST embedded JRE if None)
        """
        base_path = Path(__file__).parent.parent
        
        if java_home is None:
            # Use embedded Java 11 from POST
            java_home = base_path / "rq1" / "POST_V_1.0.3" / "jre"
        
        self.java_home = Path(java_home)
        self.java_exe = self.java_home / "bin" / "java.exe"
        self.post_jar = base_path / "rq1" / "POST_V_1.0.3" / "jre" / "POST-1.0.3-jar-with-dependencies.jar"
        
        if not self.java_exe.exists():
            raise RuntimeError(f"Java not found at {self.java_exe}")
        if not self.post_jar.exists():
            raise RuntimeError(f"POST JAR not found at {self.post_jar}")
    
    def get_java_version(self) -> str:
        """Get Java version"""
        result = subprocess.run(
            [str(self.java_exe), "-version"],
            capture_output=True,
            text=True
        )
        return result.stderr.split('\n')[0]
    
    def validate_issue(
        self,
        rq1_number: str,
        username: str,
        password: str,
        rules: Optional[List[str]] = None,
        environment: str = "ACCEPTANCE"
    ) -> Dict:
        """
        Validate RQ1 issue with POST rules
        
        Args:
            rq1_number: RQ1 issue number (e.g., "RQ100123")
            username: RQ1 username  
            password: RQ1 password
            rules: List of rule IDs to execute (None = all rules)
            environment: ACCEPTANCE or PRODUCTIVE
        
        Returns:
            Validation results as dict
        """
        
        # Build command with credentials
        cmd = [
            str(self.java_exe),
            "-Djava.awt.headless=true",  # Try headless mode
            "-cp", str(self.post_jar),
            "cli.PostHeadless",
            username,
            password,
            environment,
            rq1_number
        ]
        
        if rules:
            cmd.extend(rules)
        
        try:
            result = subprocess.run(
                cmd,
                capture_output=True,
                text=True,
                timeout=60  # 60 second timeout
            )
            
            # Parse JSON output from stdout
            if result.stdout:
                try:
                    return json.loads(result.stdout)
                except json.JSONDecodeError:
                    pass
            
            # If JSON parsing fails, return error
            return {
                "rq1_number": rq1_number,
                "environment": environment,
                "status": "error",
                "message": "POST execution failed",
                "stderr": result.stderr,
                "exit_code": result.returncode
            }
            
        except subprocess.TimeoutExpired:
            return {
                "rq1_number": rq1_number,
                "status": "timeout",
                "message": "POST validation timed out after 60 seconds"
            }
        except Exception as e:
            return {
                "rq1_number": rq1_number,
                "status": "error",
                "message": str(e)
            }
    
    def list_available_rules(self) -> List[str]:
        """List all available rules in POST"""
        # Rules extracted from DataModel/Monitoring
        rules = [
            "Rule_IssueSW_FmeaCheck",
            "Rule_IssueSW_ASIL",
            "Rule_IssueSW_Conflicted",
            "Rule_IssueSW_DefectFlowModel",
            "Rule_IssueSW_MissingAffectedIssueComment",
            "Rule_IssueFD_Close",
            "Rule_IssueFD_Commit",
            "Rule_IssueFD_Conflicted",
            "Rule_IssueFD_DefectFlowModel",
            "Rule_IssueFD_Evaluation",
            "Rule_IssueFD_Implementation",
            "Rule_IssueFD_NotCommited",
            "Rule_IssueFD_Pilot",
            "Rule_IssueFD_WithoutLinkToBc",
            "Rule_Bc_CheckPstDates",
            "Rule_Bc_Close",
            "Rule_Bc_Conflicted",
            "Rule_Bc_NamingConvention",
            "Rule_Bc_Requested",
            "Rule_Bc_Requested_FourWeeks",
            "Rule_Bc_WithoutLinkToPst",
            "Rule_Fc_Close",
            "Rule_Fc_Conflicted",
            "Rule_Fc_NamingConvention",
            "Rule_Fc_PlannedDate",
            "Rule_Fc_ReqDate",
            "Rule_Fc_WithoutLinkToBc",
            # ... 59 rules total
        ]
        return rules


def main():
    """Test POST validator"""
    print("=== POST Validator Test ===\n")
    
    validator = PostValidator()
    print(f"Java version: {validator.get_java_version()}")
    print(f"POST JAR: {validator.post_jar.name}\n")
    
    # Test validation (mock)
    result = validator.validate_issue(
        rq1_number="RQ100123",
        username="test_user",
        password="test_pass",
        rules=["Rule_IssueSW_FmeaCheck", "Rule_IssueSW_ASIL"]
    )
    
    print("Validation result:")
    print(json.dumps(result, indent=2))
    
    print(f"\nAvailable rules: {len(validator.list_available_rules())}")


if __name__ == "__main__":
    main()
