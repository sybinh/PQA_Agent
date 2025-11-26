/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Boolean;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataModel.ALM.Fields.DmAlmField_Date;
import DataModel.ALM.Fields.DmAlmField_Enumeration;
import DataModel.ALM.Fields.DmAlmField_EnumerationFromOtherElement;
import DataModel.ALM.Fields.DmAlmField_Number;
import DataModel.ALM.Fields.DmAlmField_Resource;
import DataModel.DmFieldI;
import DataStore.ALM.DsAlmRecord;
import OslcAccess.ALM.OslcAlmServerDescription;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class DmAlmWorkitem extends DmAlmElement {

    public DmAlmField_Text TITLE;
    public DmAlmField_EnumerationFromOtherElement<DmAlmUser> CONTRIBUTOR;
    public DmAlmField_Date CREATED;
    public DmAlmField_EnumerationFromOtherElement<DmAlmUser> CREATOR;
    public DmAlmField_Text IDENTIFIER;
    public DmAlmField_Date MODIFIED_DATE;
    public DmAlmField_Resource MODIFIED_USER;
    public DmAlmField_Text TAGS;
    public DmAlmField_Text TYPE;
    public DmAlmField_Text SHORT_ID;
    public DmAlmField_Text SHORT_TITLE;
    public DmAlmField_Boolean APPROVED;
    public DmAlmField_Boolean CLOSED;
    public DmAlmField_Boolean FIXED;
    public DmAlmField_Boolean IN_PROGRESS;
    public DmAlmField_Boolean REVIEWED;
    public DmAlmField_Boolean VERIFIED;
    public DmAlmField_Enumeration PRIORITY;
    public DmAlmField_Resource RESOLVED_BY;
    public DmAlmField_Boolean ARCHIVED;
    public DmAlmField_Text ACCOUNT_NUMBERS;
    public DmAlmField_Text ADDITIONAL_INFORMATION_IN_HTML;
    public DmAlmField_Text ADDITIONAL_INFORMATION_IN_TEXT;
    public DmAlmField_Text COMMERCIAL_RATING_COMMENT;
    public DmAlmField_Number DEVIATION;
    public DmAlmField_Number ESTIMATED_EXECUTION_SUM;
    public DmAlmField_Number ESTIMATED_SUM;
    public DmAlmField_Text LIFE_CYCLE_STATE_COMMENT;
    public DmAlmField_Date CLOSED_DATE;
    public DmAlmField_EnumerationFromOtherElement<DmAlmProjectArea> PROJECT_AREA;
    public DmAlmField_EnumerationFromOtherElement<DmAlmTeamArea> FILED_AGAINST;
    public DmAlmField_EnumerationFromOtherElement<DmAlmIteration> PLANNED_FOR;
    public DmAlmField_EnumerationFromOtherElement<DmAlmIteration> PROPOSED;
    public DmAlmField_EnumerationFromOtherElement<DmAlmTeamArea> TEAM_AREA;
    public DmAlmField_Resource TYPESTRING;
    public DmAlmField_Enumeration SEVERITY;

    protected DmAlmWorkitem(String elementType, DsAlmRecord dsAlmRecord) {
        super(elementType, dsAlmRecord);
        PROJECT_AREA = addEnumerationField(elementType, null, "", "process:projectArea", "Project Area", true);
        TYPE = addTextField("dcterms:type", "Type");
        
        init();
        
        assert (IDENTIFIER.getValue() != SHORT_ID.getValue()) : "'" + IDENTIFIER.getValue() + "' != '" + SHORT_ID.getValue() + "'";
    }

    //empty Workitem for Element Creation
    protected DmAlmWorkitem(String elementType, DmAlmProjectArea projectArea) {
        super(elementType);
        
        //add initial fields
        PROJECT_AREA = addEnumerationField(elementType, projectArea.getUrl(), "", "process:projectArea", "Project Area", true);
        TYPE = addTextField("dcterms:type", "Type");
        
        //set initial values
        PROJECT_AREA.setValue(projectArea);
        TYPE.setValue(elementType);

        init();

        CREATED.setValue(EcvDate.getToday());
    }

    private void init() {
        ignoreField("acc:accessContext");
        ignoreField("acp:accessControl");
        TITLE = addTextField("dcterms:title", "Title");
        TITLE.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        IDENTIFIER = addTextField("dcterms:identifier", "Identifier");
        MODIFIED_DATE = addDateField("dcterms:modified", "Modified date");
        TAGS = addTextField("dcterms:subject", "Tags", true);
        TAGS.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        ignoreField("oslc:discussedBy");
        ignoreField("oslc:instanceShape");
        ignoreField("oslc:serviceProvider");
        SHORT_ID = addTextField("oslc:shortId", "Short Id");
        SHORT_ID.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        SHORT_TITLE = addTextField("oslc:shortTitle", "Short Title");
        SHORT_TITLE.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        CREATED = addDateField("dcterms:created", "Created");
        APPROVED = addBooleanField("oslc_cm:approved", "Approved");
        CLOSED = addBooleanField("oslc_cm:closed", "Closed");
        FIXED = addBooleanField("oslc_cm:fixed", "Fixed");
        IN_PROGRESS = addBooleanField("oslc_cm:inprogress", "In progress");
        REVIEWED = addBooleanField("oslc_cm:reviewed", "Reviewed");
        VERIFIED = addBooleanField("oslc_cm:verified", "Verified");
        PRIORITY = addEnumerationField("oslc_cmx:priority", "Priority");
        SEVERITY = addEnumerationField("oslc_cmx:severity", "Severity");
        ignoreField("oslc_cm:priority");
        ignoreField("oslc_cmx:priority");
        ignoreField("oslc_cmx:project");
        ignoreField("oslc_pl:schedule");
        ignoreField("process:iteration");
        ignoreField("process:teamArea");
        TYPESTRING = addResourceField("rtc_cm:type", "Type-String");
        TEAM_AREA = addEnumerationField(TYPE.getValue(), PROJECT_AREA.getValueUrl(), "teamArea", "rtc_cm:teamArea", "Team Area", true);
        CONTRIBUTOR = addEnumerationField(TYPE.getValueAsText(), PROJECT_AREA.getValueUrl(), "owner", "dcterms:contributor", "Owner", true);
        CONTRIBUTOR.setNullConverter(new DmAlmField_EnumerationFromOtherElement.NullConverter() {
            @Override
            public boolean isUrlNullElement(String url) {
                return (url.contains("unassigned"));
            }

            @Override
            public String getNullUrl() {
                String server = OslcAlmServerDescription.getServerDescriptionForUrl(getUrl()).getOslcUrl();
                return (server + "/jts/users/unassigned");
            }
        });

        CREATOR = addEnumerationField(TYPE.getValueAsText(), PROJECT_AREA.getValueUrl(), "owner", "dcterms:creator", "Creator", true);
        PLANNED_FOR = addEnumerationField(TYPE.getValueAsText(), PROJECT_AREA.getValueUrl(), "target", "rtc_cm:plannedFor", "Planned For", true);
        PROPOSED = addEnumerationField(TYPE.getValueAsText(), PROJECT_AREA.getValueUrl(), "com.ibm.team.workitem.attribute.proposed", "rtc_ext:com.ibm.team.workitem.attribute.proposed", "Proposed", true);
        FILED_AGAINST = addEnumerationField(TYPE.getValueAsText(), PROJECT_AREA.getValueUrl(), "category", "rtc_cm:filedAgainst", "Filed Against", false);
        MODIFIED_USER = addResourceField("rtc_cm:modifiedBy", "Modified by");
        ignoreField("rtc_cm:progressTracking");
        ignoreField("rtc_cm:repository");
        RESOLVED_BY = addResourceField("rtc_cm:resolvedBy", "Resolved by");
        ignoreField("rtc_cm:state");
        ignoreField("rtc_cm:timeSheet");
        ARCHIVED = addBooleanField("rtc_ext:archived", "Archived");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitem.customattribute.externaltriggernextstate");
        ACCOUNT_NUMBERS = addTextField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.accountnumbers", "Account Numbers", true);
        ADDITIONAL_INFORMATION_IN_HTML = addTextField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.additionalinformationinhtml", "Additional Information (HTML)", true);
        ADDITIONAL_INFORMATION_IN_TEXT = addTextField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.additionalinformationintext", "Additional Information (TEXT)", true);
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.aggregatedeffort");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.allocation");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.changesbyimporter");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.commenteddecision");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.commercialpreparation");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.commercialrating_forcustomerprojectonly");
        COMMERCIAL_RATING_COMMENT = addTextField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.commercialratingcomment", "Commercial Rating Comment", true);
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.commercialratings");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.developmentmethod.enum");
        DEVIATION = addNumberField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.deviation", "Deviation", true);
        ESTIMATED_EXECUTION_SUM = addNumberField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.estimatedexecutionsum", "Estimated Execution Sum", true);
        ESTIMATED_SUM = addNumberField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.estimationsum", "Estimated Sum", true);
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.exchangedattachments");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.exchangeworkflow");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.exchangeworkflow.list");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.executionprogress");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalacceptance");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalacceptancecriteria");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalclassification");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalcommentauthor");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalconversation");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalcreator");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externaldescription");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalhistory");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalid");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalmilestones");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalnewcomment");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalorganization");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalowner");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalreview");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalstate");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalstateparallel1");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalstateparallel2");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalsummary");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externaltags");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.externalupdateversion.string");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.flowtshirtsize");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.lastworkflowmodifier");
        LIFE_CYCLE_STATE_COMMENT = addTextField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.lifecyclestatecomment", "Life Cycle State Comment", true);
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.plannedestimate");

        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.pminterfaceelementid");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.requester");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.residualestimate");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.spentexecutiontime");
        ignoreField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.workflowgraph");
        ignoreField("rtc_ext:com.bosch.rtc.widget.customattribute.attachmenthandling.descriptions");
        ignoreField("rtc_ext:contextId");
        CLOSED_DATE = addDateField("oslc_cm:closeDate", "Closed Date", true);
        ignoreField("rtc_cm:resolution");

        addResourceListField("rtc_cm:com.ibm.team.workitem.linktype.parentworkitem.children", "Children", true);
        addResourceListField("rtc_cm:com.ibm.team.workitem.linktype.parentworkitem.parent", "Parent", true);
        addResourceListField("oslc_cm:tracksWorkItem", "Tracks workitem", true);
        addResourceListField("oslc_cm:trackedWorkItem", "Tracked workitem", true);
        addResourceListField("rtc_cm:com.ibm.team.workitem.linktype.copiedworkitem.copiedFrom", "Copied from", true);
        addResourceListField("rtc_cm:com.ibm.team.workitem.linktype.copiedworkitem.copies", "Copies", true);

        ignoreField("rtc_cm:subscribers");
        //
//        assert (TAGS.getValue().isEmpty()) : TAGS.getValue();    
    }

    @Override
    public String getId() {
        return (IDENTIFIER.getValue());
    }

    @Override
    public String getStatus() {
        return (null);
    }

    @Override
    public String getTitle() {
        return (TITLE.getValue().replace("\n", ""));
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + IDENTIFIER.getValueAsText() + " - " + TITLE.getValueAsText());
    }

}
