/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Enumeration;
import DataModel.ALM.Fields.DmAlmField_Number;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord;

/**
 *
 * @author GUG2WI
 */
public class DmAlmWorkitem_HighLevelEpic extends DmAlmWorkitem {

    final public DmAlmField_Text STATUS;
    final public DmAlmField_Number ACTUAL_STORY_POINTS;
    final public DmAlmField_Text EPIC_HYPOTHESIS_STATEMENT;
    final public DmAlmField_Number ESTIMATED_STORY_POINTS;
    final public DmAlmField_Number WSJF;
    final public DmAlmField_Enumeration JOB_SIZE;
    final public DmAlmField_Enumeration USER_BUSINESS_VALUE;
    final public DmAlmField_Enumeration TIME_CRITICALITY;
    final public DmAlmField_Enumeration RROE;
    final public DmAlmField_Enumeration SAFE_ENABLE_TYPE;
    final public DmAlmField_Enumeration SAFE_WORK_TYPE;
    final public DmAlmField_Text SUCCESS_CRITERIA;

    public DmAlmWorkitem_HighLevelEpic(String elementType, DsAlmRecord dsAlmRecord) {
        super(elementType, dsAlmRecord);
        
        STATUS = addTextField("oslc_cm:status", "Status");
        ACTUAL_STORY_POINTS = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.actualStoryPoints", "Actual Story Points");
        EPIC_HYPOTHESIS_STATEMENT = addTextField("rtc_ext:com.ibm.team.workitem.attribute.epicHypothesisStatement", "Epic Hypothesis Statement");
        WSJF = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.wsjf", "WSJF");
        ESTIMATED_STORY_POINTS = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.estimatedStoryPoints", "Estimated Story Points");
        JOB_SIZE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.jobSize", "Job Size");
        USER_BUSINESS_VALUE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.ubVal", "User/Business Value");
        TIME_CRITICALITY = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.timeCrit", "Time Criticality");
        RROE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.rroe", "RROE");
        SAFE_ENABLE_TYPE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.safeEnablerType", "SAFe Enabler Type");
        SAFE_WORK_TYPE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.safeWorkType", "SAFe Work Type");
        SUCCESS_CRITERIA = addTextField("rtc_ext:com.ibm.team.workitem.attribute.successCriteria", "Success Criteria");

        ignoreField("dcterms:description");
        ignoreField("acc:accessContext");
        ignoreField("acp:accessControl");
        ignoreField("oslc:discussedBy");
        ignoreField("oslc:instanceShape");
        ignoreField("oslc:serviceProvider");
        ignoreField("oslc_cm:approved");
        ignoreField("oslc_cm:closed");
        ignoreField("oslc_cm:fixed");
        ignoreField("oslc_cm:inprogress");
        ignoreField("oslc_cm:reviewed");
        ignoreField("oslc_cm:verified");
        ignoreField("oslc_cmx:priority");
        ignoreField("oslc_cmx:severity");
        ignoreField("oslc_pl:schedule");
        ignoreField("process:iteration");
        ignoreField("rtc_cm:state");
        ignoreField("rtc_cm:subscribers");
        ignoreField("rtc_cm:timeSheet");
        ignoreField("rtc_ext:archived");
        ignoreField("rtc_ext:com.ibm.team.workitem.attribute.wsjfDecimal");
        ignoreField("rtc_ext:contextId");
//        ignoreField("");

        checkForUnusedFields();
    }

    @Override
    public String getId() {
        return (IDENTIFIER.getValue());
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }

    @Override
    public String getTitle() {
        return (TITLE.getValue());
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + IDENTIFIER.getValueAsText() + " - " + TITLE.getValueAsText());
    }

}
