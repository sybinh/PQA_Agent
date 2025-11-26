/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Boolean;
import DataModel.ALM.Fields.DmAlmField_Enumeration;
import DataModel.ALM.Fields.DmAlmField_EnumerationFromText;
import DataModel.ALM.Fields.DmAlmField_ExternalResourceList;
import DataModel.ALM.Fields.DmAlmField_Number;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataModel.ALM.Monitoring.Rule_Capability_CheckReleaseNoteLink;
import DataModel.ALM.Monitoring.Rule_Capability_CheckRolloutCategory;
import DataModel.DmFieldI;
import DataStore.ALM.DsAlmRecord;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public class DmAlmWorkitem_Capability extends DmAlmWorkitem {

    public enum Status_Capability implements EcvEnumeration {

        VALIDATING_ON_STAGING("Validating on Staging"),
        REJECTED("Rejected"),
        ACCEPTED("Accepted"),
        APPROVED("Approved"),
        ANALYZING("Analyzing"),
        RELEASING("Releasing"),
        IMPLEMENTING("Implementing"),
        READY_FOR_APPROVAL("Ready (for Approval)"),
        DRAFT("Draft"),
        DEPLOYING_TO_PRODUCTION("Deploying to Production");

        private final String dbText;

        private Status_Capability(String dbText) {
            assert (dbText != null);
            this.dbText = dbText;
        }

        @Override
        public String getText() {
            return dbText;
        }

        @Override
        public String toString() {
            return (getText());
        }

    }

    public static final String ELEMENT_TYPE = "Capability";

    final public DmAlmField_EnumerationFromText STATUS;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Number ESTIMATED_STORY_POINTS;
    final public DmAlmField_Number ACTUAL_STORY_POINTS;
    final public DmAlmField_Number WSJF;
    final public DmAlmField_Boolean MVP;
    final public DmAlmField_Enumeration SAFE_WORKTYPE;
    final public DmAlmField_Enumeration SAFE_ENABLER;
    final public DmAlmField_Enumeration JOB_SIZE;
    final public DmAlmField_Enumeration USER_BUSINESS_VALUE;
    final public DmAlmField_Enumeration TIME_CRITICALITY;
    final public DmAlmField_Enumeration RROE;
    final public DmAlmField_ExternalResourceList RELATED_ARTIFACT;

    public DmAlmWorkitem_Capability(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);

        STATUS = addEnumerationFromTextField("oslc_cm:status", Status_Capability.values(), "Status", true);
        DESCRIPTION = addTextField("dcterms:description", "Description");
        DESCRIPTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        ESTIMATED_STORY_POINTS = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.estimatedStoryPoints", "Estimated Story Points");
        WSJF = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.wsjf", "WSJF");
        ACTUAL_STORY_POINTS = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.actualStoryPoints", "Actual Story Points");
        MVP = addBooleanField("rtc_ext:com.ibm.team.workitem.attribute.mvp", "MVP");
        SAFE_WORKTYPE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.safeWorkType", "SAFe Work Type", true);
        SAFE_ENABLER = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.safeEnablerType", "SAFe Enabler Type", true);
        JOB_SIZE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.jobSize", "Job Size");
        USER_BUSINESS_VALUE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.ubVal", "User/Business Value");
        TIME_CRITICALITY = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.timeCrit", "Time Criticality");
        RROE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.rroe", "RROE");
        RELATED_ARTIFACT = addExternalResourceField("rtc_cm:com.ibm.team.workitem.linktype.relatedartifact.relatedArtifact", "Related Artifacts", true);

        addRule(new Rule_Capability_CheckRolloutCategory(this));
        addRule(new Rule_Capability_CheckReleaseNoteLink(this));

        checkForUnusedFields();
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }

}
