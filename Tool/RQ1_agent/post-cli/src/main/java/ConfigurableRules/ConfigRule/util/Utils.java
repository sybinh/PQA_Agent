/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.util;

import ConfigurableRules.ConfigRule.Query.Criteria;
import DataModel.DmMappedElement;
import DataModel.Doors.Records.DmDoorsObject;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1User;
import DataModel.Rq1.Records.DmRq1UserRole;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ULV81HC
 */
public class Utils {
    
    static String userLoginCompany = Constants.BLANK;
    
    /**
     * Get user's company name from LoginFullName 
     * @param userLoginFullName get from Login Data. Format: "<username> (<companyName>/<GroupOrDepartment>)"
     * @return company name
     */
    public static String getCompanyFromName(String userLoginFullName) {
        String company = Constants.BLANK;
        
        if(!userLoginFullName.contains(Constants.OPEN) || !userLoginFullName.contains(Constants.BACK_SLASH)) {
            return company;
        }
        int openIndex = userLoginFullName.indexOf(Constants.OPEN) + 1;
        int slashIndex = userLoginFullName.indexOf(Constants.BACK_SLASH);
        
        company = userLoginFullName.substring(openIndex, slashIndex);
        return company;
    }
    
    /**
     * Get user's company name and check if belong to RBEI / RBVH
     * RBEI / RBVH : Full Mode
     * Others : Restricted Mode
     * @return true -> able to use Full Mode
     */
    public static boolean isUsingFullMode() {
        try {
            String userLoginFullName = EcvLoginManager.getCurrentUserFullName();
            
            // If userLoginCompany not have value yet -> get from userLoginFullName
            if(userLoginCompany == null || userLoginCompany.isEmpty()) {
                userLoginCompany = Utils.getCompanyFromName(userLoginFullName);
            }
            
            // If user is RBEI/RBVH -> return True (Full Mode available)
            if(userLoginCompany.equals(Constants.MS) || userLoginCompany.equals(Constants.RBEI) || userLoginCompany.equals(Constants.RBVH)) {
                return true;
            }

        } catch(Exception e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(Utils.class.getName(), e);
        }
        return false;
    }
    
    /**
     * Check if Login User is Project Leader of input project.
     * @param project
     * @param userLoginFullName
     * @return 
     */
    public static boolean isProjectLeader(DmRq1Project project, String userLoginFullName) {
        try {
            for (DmMappedElement<DmRq1UserRole, DmRq1User> member : project.HAS_PROJECT_MEMBERS.getElementList()) {

                String memberName = member.getTarget().getTitle();
                DmRq1UserRole.Role memberRole = member.getMap().getRole();

                // If log-in user is member of currProject and being Project Leader
                if (userLoginFullName.equals(memberName) && memberRole.equals(DmRq1UserRole.Role.PROJECT_LEADER)) {
                    return true;
                }
            }
        } catch(Exception e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(Utils.class.getName(), e);
        }
        return false;
    }
    
    public static boolean isNumeric(String str) { 
        try {  
          Double.parseDouble(str);  
          return true;
          
        } catch(Exception e){  
          return false;  
        }  
    }
    
    public static Criteria getCriteriaById(List<Criteria> criterias, String id) {
        try {
            Criteria criteria = criterias.stream().filter(c -> id.equals(c.getId())).findFirst().get();
            return criteria;
            
        } catch(Exception e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, e);
            ToolUsageLogger.logError(Utils.class.getName(), e);
            return null;
        }
    }
    
    public static void updateMissingCriteriaId(List<Criteria> criterias) {
        boolean isMissingId = criterias.stream().anyMatch(c -> (c.getId() == null || c.getId().isEmpty()));
        if(!isMissingId) {
            return;
        }
        
        for(int i = 0; i < criterias.size(); i++) {
            if(criterias.get(i).getId() == null || criterias.get(i).getId().isEmpty()) {
                criterias.get(i).setId((i + 1) + Constants.BLANK);
            }
        }
    }
    
    /**
     * Create default configuration for Criteria Editor. Default is "And" condition.
     * @param criterias
     * @return String follow format "(1,2,3,...)". The number is criteria id.
     */
    public static String createDefaultCriteriaEditor(List<Criteria> criterias) {
        List<String> idList = new ArrayList<>();
        
        criterias.stream().forEach(c -> idList.add(c.getId()));
        String defaultEditor = Constants.OPEN_AND + String.join(Constants.COMMA, idList) + Constants.CLOSE;
        
        return defaultEditor;
    }
    
    /**
     * Convert a string of Criteria Editor configuration to a list.
     * Example: "(1,[2,(3,4)])" -> list of ( 1 [ 2 ( 3 4 ) ] )
     * Only keep bracket (round and square) and number.
     * @param str
     * @return a list of string
     */
    public static List<String> convertCriteriaEditorToList(String str) {
        List<String> list = Arrays.asList(str.split(Constants.BLANK));
        List<String> resultList = new ArrayList<>();
        
        // Go throgh each element in a 'str' input
        for(int i = 0; i < list.size(); i++) {
            String e = list.get(i);
            
            // If character is "AND(" -> keep the bracket
            if(e.equals("A")) {
                String bracket = e + list.get(i + 1) + list.get(i + 2) + list.get(i + 3);
                resultList.add(bracket);
                i += 3;
                
            // If character is "OR(" -> keep the bracket
            } else if (e.equals("O")) {
                String bracket = e + list.get(i + 1) + list.get(i + 2);
                resultList.add(bracket);
                i += 2;
                
            // If character is ")" -> keep the bracket
            } else if (e.equals(Constants.CLOSE)) { 
                resultList.add(e);
                
            // If the character is a number -> keep the number
            } else if(isNumeric(e)) {
                String num = e;
                
                // The number may has more than 1 character (example: 11,22,...)
                for(int j = i + 1; j < list.size(); j++) {
                    if(!isNumeric(list.get(j))) {
                        break;
                    } else {
                        num += list.get(j);
                        i = j;
                    }
                }
                resultList.add(num);
            
            // If charater is colon "," -> keep the comma
            } else if (e.equals(Constants.COMMA)) {
                resultList.add(e);
            }
        }
        return resultList;
    }
    
    /**
     * 
     * @param criterias
     * @param str
     * @return 
     */
    public static boolean isCriteriaEditorCorrect(List<Criteria> criterias, String str) {
        // Check if the brackets are correct -----------------------------------
        // If input is null -> return false
        if(criterias == null || str == null) {
            return false;
        // If no configuration -> no need to check
        } else if(str.isEmpty()) {
            return true;
        // If there is configuration -> should open and end by same braket type.
        } else if (!((str.startsWith(Constants.OPEN_AND) || str.startsWith(Constants.OPEN_OR)) && str.endsWith(Constants.CLOSE))) {
            return false;
        }
        
        List<String> criteriaEditor = convertCriteriaEditorToList(str);
        
        // Check if there are enough ","
        for(int i = 0; i < criteriaEditor.size() - 2; i++) {
            String e = criteriaEditor.get(i);
            
            // Don't check if e is "AND(" or "OR(" or ","
            if(e.equals(Constants.OPEN_AND) || e.equals(Constants.OPEN_OR) || e.equals(Constants.COMMA)) {
                continue;
                
            // If after a number is not close bracket ")" or comma "," -> return False
            } else if (!criteriaEditor.get(i + 1).equals(Constants.CLOSE) && !criteriaEditor.get(i + 1).equals(Constants.COMMA)) {
                return false;
            }
        }
        
        // Use "bracket algorithm" to check if number and arrangement of brakets are correct.
        Stack<String> stack = new Stack<>();
        criteriaEditor.removeIf(e -> e.equals(Constants.COMMA));

        for(String e : criteriaEditor) {
            if(e.equals(Constants.OPEN_AND) || e.equals(Constants.OPEN_OR)) {
                stack.push(e);
                
            } else if(e.equals(Constants.CLOSE)) {
                if(stack.empty())
                    return false;
                else if(stack.peek().equals(Constants.OPEN_AND) || stack.peek().equals(Constants.OPEN_OR))
                    stack.pop();
                else
                    return false;
            }
        }
        
        // After check, the stak should be empty
        if(!stack.empty()) {
            return false;
        }
        
        // Check if all criterias ID is match with configuration -----------------
        List<String> brackets;
        brackets = new ArrayList<>();
        brackets.add(Constants.OPEN_AND);
        brackets.add(Constants.OPEN_OR);
        brackets.add(Constants.CLOSE);
        
        criteriaEditor.removeAll(brackets);
        
        // Check if all id from criteria editor exist in criterias
        for(String e : criteriaEditor) {
            boolean anyMatch = criterias.stream().anyMatch(a -> a.getId().equals(e));
            if(!anyMatch) {
                return false;
            }
        }
        // Check if all criteria id exist in from criteria editor
        for(Criteria criteria : criterias) {
            if(!criteriaEditor.contains(criteria.getId())) {
                return false;
            }
        }
        return true;
    }
    
    public static String editDoorsLink(String url) {
        url = url.replace("-V-", Constants.BLANK);
        url = url.replace("-O-", Constants.BLANK);
        url = url.replace("-M-", Constants.BLANK);
        return url;
    }
    
    public static String createDoorsRecordType(DmDoorsObject doorsElement) {
        // Get level: Only get the number (example: "L2 planned"  ->  "2")
        String level = doorsElement.rq1Level;
        level = (level.length() >= 2) ? level.substring(1, 2) : level;
        
        // Level 1 or 2 -> "SW" . Level 3 or 4 -> "FD"
        String issueType = (level.equals("1") || level.equals("2")) ? Constants.SW : Constants.FD;

        String doorsRecordType = String.format(Constants.DOORS_RECORD_TYPE, issueType, level);
        return doorsRecordType;
    }
}
