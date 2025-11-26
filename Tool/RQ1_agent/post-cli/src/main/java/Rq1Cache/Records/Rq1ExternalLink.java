/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1DatabaseField_Date.DateFormat;
import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;

/**
 *
 * @author GUG2WI
 */
public class Rq1ExternalLink extends Rq1Record implements Rq1NodeInterface {

    final static public Rq1AttributeName ATTRIBUTE_DEFECT_ID = new Rq1AttributeName("belongsToProblem", "hasBackground", "DefectID");
    final static public Rq1AttributeName ATTRIBUTE_PRIMARY_ROOT_CAUSE = new Rq1AttributeName("belongsToProblem", "hasBackground", "PrimaryRootCause");

    //
    // General fields
    //
    final public Rq1DatabaseField_Text ID;
    final public Rq1DatabaseField_Text NAME;
    final public Rq1DatabaseField_Text EXT_LINK_TYPE;
    final public Rq1DatabaseField_Text LINK_STATE;
    final public Rq1DatabaseField_Text VALIDITY;
    final public Rq1DatabaseField_Text EXT_LINK_COMMENT;
    final public Rq1DatabaseField_Text HISTORY_LOG;
    final public Rq1DatabaseField_Text SOURCE_ID;
    final public Rq1DatabaseField_Text SOURCE_RECORD_TYPE;
    final public Rq1DatabaseField_Text TARGET_SYSTEM;
    final public Rq1DatabaseField_Text SUBMITTER;
    final public Rq1DatabaseField_Date SUBMIT_DATE;
    final public Rq1DatabaseField_Text LAST_MODIFIED_USER;
    final public Rq1DatabaseField_Date LAST_MODIFIED_DATE;

    final public Rq1DatabaseField_Reference BELONGS_TO_ISSUE;
    final public Rq1DatabaseField_Reference BELONGS_TO_ISSUE_RELEASE_MAP;
    final public Rq1DatabaseField_Reference BELONGS_TO_PROJECT;
    final public Rq1DatabaseField_Reference BELONGS_TO_RELEASE;
    final public Rq1DatabaseField_Reference BELONGS_TO_RELEASE_REL_MAP;
    final public Rq1DatabaseField_Reference BELONGS_TO_WORKITEM;
    final public Rq1DatabaseField_Reference BELONGS_TO_PROBLEM;

    //
    // ALM fields
    //
    final public Rq1DatabaseField_Text TARGET_SERVER_1;
    final public Rq1DatabaseField_Text TARGET_SERVER_2;
    final public Rq1DatabaseField_Text TARGET_TYPE;
    final public Rq1DatabaseField_Text TARGET_ID;
    final public Rq1DatabaseField_Text TARGET_PROJECT_ID;
    final public Rq1DatabaseField_Text TARGET_COMPONENT_ID;
    final public Rq1DatabaseField_Text TARGET_PARAMETER;
    final public Rq1DatabaseField_Text TARGET_CONFIGURATION_SCOPE;
    final public Rq1DatabaseField_Text TARGET_CONFIGURATION_TYPE;
    final public Rq1DatabaseField_Text TARGET_CONFIGURATION_ID;
    final public Rq1DatabaseField_Text TARGET_URL;

    //
    // SDOM fields
    //
    // Fields not found:
    // - Primary Root Cause
    // - Defect ID
    //
    final public Rq1DatabaseField_Text DEFECT_ID;
    final public Rq1DatabaseField_Text EFFECT_OF_THE_DEFECT;
    final public Rq1DatabaseField_Text EXTERNAL_DESCRIPTION;
    final public Rq1DatabaseField_Text FREE_TEXT;
    final public Rq1DatabaseField_Text INTERNAL_DESCRIPTION;
    final public Rq1DatabaseField_Text LOCK_CONDITION;
    final public Rq1DatabaseField_Text PRIMARY_ROOT_CAUSE;
    final public Rq1DatabaseField_Text SOLUTION;
    final public Rq1DatabaseField_Text VERSION_NAME;

    public Rq1ExternalLink() {
        super(Rq1NodeDescription.EXTERNAL_LINK);

        //
        // General fields
        //
        addField(ID = new Rq1DatabaseField_Text(this, "id"));
        addField(NAME = new Rq1DatabaseField_Text(this, "Name"));
        addField(EXT_LINK_TYPE = new Rq1DatabaseField_Text(this, "ExtLinkType"));
        addField(LINK_STATE = new Rq1DatabaseField_Text(this, "LinkState"));
        addField(VALIDITY = new Rq1DatabaseField_Text(this, "Validity"));
        addField(EXT_LINK_COMMENT = new Rq1DatabaseField_Text(this, "ExtLinkComment"));
        addField(HISTORY_LOG = new Rq1DatabaseField_Text(this, "HistoryLog"));
        addField(SOURCE_ID = new Rq1DatabaseField_Text(this, "SourceID"));
        addField(SOURCE_RECORD_TYPE = new Rq1DatabaseField_Text(this, "SourceRecordType"));
        addField(TARGET_SYSTEM = new Rq1DatabaseField_Text(this, "TargetSystem"));

        addField(SUBMITTER = new Rq1DatabaseField_Text(this, "Submitter"));
        addField(SUBMIT_DATE = new Rq1DatabaseField_Date(this, "SubmitDate"));
        SUBMIT_DATE.setDateFormat(DateFormat.YYYYMMDD_HHMMSS);
        addField(LAST_MODIFIED_USER = new Rq1DatabaseField_Text(this, "LastModifiedUser"));
        addField(LAST_MODIFIED_DATE = new Rq1DatabaseField_Date(this, "LastModifiedDate"));
        LAST_MODIFIED_DATE.setDateFormat(DateFormat.YYYYMMDD_HHMMSS);

        addField(BELONGS_TO_ISSUE = new Rq1DatabaseField_Reference(this, "belongsToIssue", Rq1RecordType.ISSUE));
        addField(BELONGS_TO_ISSUE_RELEASE_MAP = new Rq1DatabaseField_Reference(this, "belongsToIssuereleasemap", Rq1RecordType.IRM));
        addField(BELONGS_TO_PROJECT = new Rq1DatabaseField_Reference(this, "belongsToProject", Rq1RecordType.PROJECT));
        addField(BELONGS_TO_RELEASE = new Rq1DatabaseField_Reference(this, "belongsToRelease", Rq1RecordType.RELEASE));
        addField(BELONGS_TO_RELEASE_REL_MAP = new Rq1DatabaseField_Reference(this, "belongsToReleaserelmap", Rq1RecordType.RRM));
        addField(BELONGS_TO_WORKITEM = new Rq1DatabaseField_Reference(this, "belongsToWorkitem", Rq1RecordType.WORKITEM));
        addField(BELONGS_TO_PROBLEM = new Rq1DatabaseField_Reference(this, "belongsToProblem", Rq1RecordType.PROBLEM));

        //
        // ALM fields
        //
        addField(TARGET_SERVER_1 = new Rq1DatabaseField_Text(this, "TargetServer1"));
        addField(TARGET_SERVER_2 = new Rq1DatabaseField_Text(this, "TargetServer2"));
        addField(TARGET_TYPE = new Rq1DatabaseField_Text(this, "TargetType"));
        addField(TARGET_ID = new Rq1DatabaseField_Text(this, "TargetID"));
        addField(TARGET_PROJECT_ID = new Rq1DatabaseField_Text(this, "TargetProjectID"));
        addField(TARGET_COMPONENT_ID = new Rq1DatabaseField_Text(this, "TargetComponentID"));
        addField(TARGET_PARAMETER = new Rq1DatabaseField_Text(this, "TargetParameter"));
        addField(TARGET_CONFIGURATION_SCOPE = new Rq1DatabaseField_Text(this, "TargetConfigurationScope"));
        addField(TARGET_CONFIGURATION_TYPE = new Rq1DatabaseField_Text(this, "TargetConfigurationType"));
        addField(TARGET_CONFIGURATION_ID = new Rq1DatabaseField_Text(this, "TargetConfigurationID"));
        addField(TARGET_URL = new Rq1DatabaseField_Text(this, "TargetURL"));

        //
        // SDOM fields
        //
        addField(DEFECT_ID = new Rq1DatabaseField_Text(this, ATTRIBUTE_DEFECT_ID));
        DEFECT_ID.setOptional().setReadOnly();
        addField(EFFECT_OF_THE_DEFECT = new Rq1DatabaseField_Text(this, "EffectOfTheDefect"));
        addField(EXTERNAL_DESCRIPTION = new Rq1DatabaseField_Text(this, "ExternalDescription"));
        addField(FREE_TEXT = new Rq1DatabaseField_Text(this, "Freetext"));
        addField(INTERNAL_DESCRIPTION = new Rq1DatabaseField_Text(this, "InternalDescription"));
        addField(LOCK_CONDITION = new Rq1DatabaseField_Text(this, "LockCondition"));
        addField(PRIMARY_ROOT_CAUSE = new Rq1DatabaseField_Text(this, ATTRIBUTE_PRIMARY_ROOT_CAUSE));
        PRIMARY_ROOT_CAUSE.setOptional().setReadOnly();
        addField(SOLUTION = new Rq1DatabaseField_Text(this, "Solution"));
        addField(VERSION_NAME = new Rq1DatabaseField_Text(this, "VersionName"));
    }

    @Override
    public synchronized void reload() {
        if (existsInDatabase() == true) {
            Rq1Client.client.loadRecordByIdentifier(this.getOslcRecordIdentifier());
        }
    }

    @Override
    public String toString() {
        return (getOslcShortTitle());
    }

}
