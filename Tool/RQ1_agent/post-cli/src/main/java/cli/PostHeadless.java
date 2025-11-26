package cli;

import OslcAccess.Rq1.OslcRq1Client;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Monitoring.*;
import Rq1Cache.Records.Rq1RecordInterface;
import RestClient.GeneralServerDescription;
import util.EcvApplication;
import util.EcvLoginData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

/**
 * Headless POST validator - bypasses JavaFX GUI
 * Directly calls validation logic from POST with credentials from command line
 * 
 * Usage: 
 *   java -cp POST-1.0.3.jar:PostHeadless.class cli.PostHeadless <user> <pass> <env> <rq1> [rules...]
 */
public class PostHeadless {
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public static void main(String[] args) {
        if (args.length < 4) {
            printUsage();
            System.exit(1);
        }
        
        String username = args[0];
        String password = args[1];
        String environment = args[2];
        String rq1Number = args[3];
        List<String> ruleIds = new ArrayList<>();
        for (int i = 4; i < args.length; i++) {
            ruleIds.add(args[i]);
        }
        
        ValidationResult result = new ValidationResult();
        result.rq1Number = rq1Number;
        result.environment = environment;
        result.timestamp = new Date().toString();
        
        try {
            // Set application data (required by POST)
            EcvApplication.setApplicationData("POST_CLI", "2.0", "2.0", EcvApplication.ApplicationType.UserInterface);
            
            // Set login data (bypasses GUI dialog)
            GeneralServerDescription server = GeneralServerDescription.getDescriptionByName(environment);
            if (server == null) {
                throw new IllegalArgumentException("Invalid environment: " + environment + ". Use ACCEPTANCE or PRODUCTIVE");
            }
            
            EcvLoginData loginData = new EcvLoginData(
                username, 
                password.toCharArray(), 
                server
            );
            EcvApplication.setLoginData(loginData);
            
            // Connect to RQ1
            log("Connecting to RQ1 " + environment + "...");
            OslcRq1Client<Rq1RecordInterface> client = OslcRq1Client.getOslcClient();
            client.loadLoginUser();
            log("Connected as: " + username);
            
            // Fetch issue
            log("Fetching " + rq1Number + "...");
            // TODO: Implement issue fetching
            // DmRq1IssueSW issue = fetchIssue(client, rq1Number);
            
            // Execute rules
            log("Executing " + (ruleIds.isEmpty() ? "all" : ruleIds.size()) + " rules...");
            // TODO: Execute rules and collect results
            
            result.status = "SUCCESS";
            result.message = "Connected successfully. Rule execution not yet implemented.";
            
        } catch (Exception e) {
            result.status = "ERROR";
            result.message = e.getMessage();
            result.error = e.getClass().getSimpleName();
            log("ERROR: " + e.getMessage());
        }
        
        // Output JSON result
        System.out.println(gson.toJson(result));
        System.exit(result.status.equals("SUCCESS") ? 0 : 2);
    }
    
    private static void log(String message) {
        System.err.println("[POST-CLI] " + message);
    }
    
    private static void printUsage() {
        System.err.println("Usage: java -cp POST-1.0.3.jar:. cli.PostHeadless <username> <password> <environment> <rq1_number> [rule_ids...]");
        System.err.println();
        System.err.println("Arguments:");
        System.err.println("  username     : RQ1 username (NTID)");
        System.err.println("  password     : RQ1 password");
        System.err.println("  environment  : ACCEPTANCE or PRODUCTIVE");
        System.err.println("  rq1_number   : Issue number (e.g., RQ100123)");
        System.err.println("  rule_ids     : Optional rule IDs to execute (default: all)");
        System.err.println();
        System.err.println("Example:");
        System.err.println("  java -cp POST-1.0.3.jar:. cli.PostHeadless dab5hc mypass ACCEPTANCE RQ100123");
    }
    
    static class ValidationResult {
        String rq1Number;
        String environment;
        String timestamp;
        String status;
        String message;
        String error;
        List<RuleResult> results = new ArrayList<>();
    }
    
    static class RuleResult {
        String ruleId;
        String status;
        String message;
    }
}
