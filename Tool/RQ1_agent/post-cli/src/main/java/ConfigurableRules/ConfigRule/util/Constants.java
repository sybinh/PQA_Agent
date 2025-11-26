/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.util;

/**
 *
 * @author ULV81HC
 */
public class Constants {
    
    public static final String USER_ASSISTANT_RULE_ID = "IPE_UserAssistant";
    public static final String USER_ASSISTANT_IMPORT_RULE_ID = "IPE_UserAssistant_Import";
    public static final String USER_ASSISTANT_CREATE_RULE_ID = "IPE_UserAssistant_Create";
    public static final String USER_ASSISTANT_EDIT_RULE_ID = "IPE_UserAssistant_Edit";
    
    public static final String USER_ASSISTANT_RULES = "User Assistant Rules";
    public static final String RQ1ID_REQUIRED_TITLE = "Require project ID";
    public static final String CRITERIA_EDITOR_WARN_TITLE = "Criteria Editor warning";
    public static final String CRITERIA_EDITOR_TITLE = "Criteria Editor";
    
    public static final String CRITERIA_EDITOR_CONFIG = "Configure checking condition of criterias";
    public static final String USER_ASSISTANT_FILE_NAME = "UserAssistantRulesCache.xml";
    public static final String DUPLICATE_NAME = "(Dupplicated Name)";
    
    public static final String USER_DOMAIN = "USERDOMAIN";
    public static final String RBEI = "RBEI";
    public static final String RBVH = "RBVH";
    public static final String MS = "MS";
    
    public static final String BLANK = "";
    public static final String SPACE = " ";
    public static final String COMMA = ",";
    public static final String NEW_LINE = "\n";
    public static final String SLASH = "\\";
    public static final String BACK_SLASH = "/";
    public static final String OPEN_AND = "AND(";
    public static final String OPEN_OR = "OR(";
    public static final String OPEN = "(";
    public static final String CLOSE = ")";
    public static final String SW = "SW";
    public static final String FD = "FD";
    
    public static final String DEV_ID_ULV81HC = "ulv81hc";
    
    public static final String OPEN_TAG = "<%s>";
    public static final String END_TAG = "</%s>";
    
    public static final String TAG_NAME_RULE = "Rule";
    
    public static final String RULE_CACH_PATH = System.getProperty("user.home") + SLASH + USER_ASSISTANT_FILE_NAME;
    
    public static final String RQ1ID_REQUIRED_MESSAGE = "Seem like RQONE ID of this project hasn't been added to User Assistant yet.\nPlease add project ID in Tools > Option > User Assistant Rules and restart IPE!";
    public static final String CRITERIA_EDITOR_MESSAGE  = "Seem like configuration of Criteria Editor is incorrect. Please check again!";
    
    
    // Doors Object key --------------------------------------------------
    public static final String DOORS_RECORD_TYPE = "I-%s/L%s";
}
