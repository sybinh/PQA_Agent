package cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * POST CLI - Command-line interface for POST validation rules
 * 
 * Usage:
 *   java -jar post-cli.jar --username USER --password PASS --env ACCEPTANCE --rq1 RQ100123 --rules "Rule_IssueSW_FmeaCheck,Rule_IssueSW_ASIL"
 */
@Command(
    name = "post-cli",
    mixinStandardHelpOptions = true,
    version = "POST CLI 2.0.0",
    description = "Validate RQ1 issues using POST rules"
)
public class PostCLI implements Callable<Integer> {

    @Option(names = {"-u", "--username"}, 
            description = "RQ1 username (NTID)", 
            required = true)
    private String username;

    @Option(names = {"-p", "--password"}, 
            description = "RQ1 password", 
            required = true,
            interactive = true)
    private String password;

    @Option(names = {"-e", "--env", "--environment"}, 
            description = "RQ1 environment (ACCEPTANCE or PRODUCTIVE)", 
            defaultValue = "ACCEPTANCE")
    private String environment;

    @Parameters(index = "0", 
                description = "RQ1 issue number (e.g., RQ100123)")
    private String rq1Number;

    @Option(names = {"-r", "--rules"}, 
            description = "Comma-separated rule IDs to execute (or 'all' for all rules)", 
            defaultValue = "all",
            split = ",")
    private List<String> ruleIds;

    @Option(names = {"-f", "--format"}, 
            description = "Output format: json, text, summary", 
            defaultValue = "json")
    private String outputFormat;

    @Option(names = {"-v", "--verbose"}, 
            description = "Verbose output")
    private boolean verbose;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        int exitCode = new CommandLine(new PostCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        try {
            if (verbose) {
                System.err.println("Connecting to RQ1 " + environment + "...");
            }

            // TODO: Initialize RQ1 client
            // OslcRq1Client client = initializeClient(username, password, environment);
            
            // TODO: Fetch RQ1 record
            // DmRq1IssueSW issue = fetchIssue(client, rq1Number);
            
            // TODO: Execute rules
            // List<ValidationResult> results = executeRules(issue, ruleIds);

            // Mock results for now
            List<ValidationResult> results = createMockResults();

            // Output results
            outputResults(results);

            // Return exit code (0 = success, 1 = validation failures)
            boolean hasFailures = results.stream()
                .anyMatch(r -> "FAIL".equals(r.status) || "WARNING".equals(r.status));
            
            return hasFailures ? 1 : 0;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            if (verbose) {
                e.printStackTrace();
            }
            return 2; // Error exit code
        }
    }

    private List<ValidationResult> createMockResults() {
        List<ValidationResult> results = new ArrayList<>();
        
        ValidationResult result1 = new ValidationResult();
        result1.ruleId = "Rule_IssueSW_FmeaCheck";
        result1.ruleName = "Comment for not required FMEA on I-SW";
        result1.status = "PASS";
        result1.message = "FMEA comment is present";
        result1.details = new HashMap<>();
        result1.details.put("fmea_state", "NOT_REQUIRED");
        result1.details.put("fmea_comment", "Not applicable for this issue");
        results.add(result1);

        ValidationResult result2 = new ValidationResult();
        result2.ruleId = "Rule_IssueSW_ASIL";
        result2.ruleName = "ASIL level validation";
        result2.status = "WARNING";
        result2.message = "ASIL level not set";
        result2.details = new HashMap<>();
        result2.details.put("asil_level", null);
        results.add(result2);

        return results;
    }

    private void outputResults(List<ValidationResult> results) {
        switch (outputFormat.toLowerCase()) {
            case "json":
                outputJson(results);
                break;
            case "text":
                outputText(results);
                break;
            case "summary":
                outputSummary(results);
                break;
            default:
                outputJson(results);
        }
    }

    private void outputJson(List<ValidationResult> results) {
        ValidationReport report = new ValidationReport();
        report.rq1Number = rq1Number;
        report.timestamp = new Date().toString();
        report.totalRules = results.size();
        report.passed = (int) results.stream().filter(r -> "PASS".equals(r.status)).count();
        report.failed = (int) results.stream().filter(r -> "FAIL".equals(r.status)).count();
        report.warnings = (int) results.stream().filter(r -> "WARNING".equals(r.status)).count();
        report.results = results;

        System.out.println(gson.toJson(report));
    }

    private void outputText(List<ValidationResult> results) {
        System.out.println("=== POST Validation Results ===");
        System.out.println("RQ1 Number: " + rq1Number);
        System.out.println("Environment: " + environment);
        System.out.println();

        for (ValidationResult result : results) {
            System.out.printf("[%s] %s: %s%n", result.status, result.ruleId, result.message);
        }
    }

    private void outputSummary(List<ValidationResult> results) {
        long passed = results.stream().filter(r -> "PASS".equals(r.status)).count();
        long failed = results.stream().filter(r -> "FAIL".equals(r.status)).count();
        long warnings = results.stream().filter(r -> "WARNING".equals(r.status)).count();

        System.out.printf("Total: %d | Passed: %d | Failed: %d | Warnings: %d%n",
            results.size(), passed, failed, warnings);
    }

    // Data classes
    static class ValidationReport {
        String rq1Number;
        String timestamp;
        int totalRules;
        int passed;
        int failed;
        int warnings;
        List<ValidationResult> results;
    }

    static class ValidationResult {
        String ruleId;
        String ruleName;
        String status; // PASS, FAIL, WARNING
        String message;
        Map<String, Object> details;
    }
}
