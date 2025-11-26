/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.ALM.Records.DmAlmElementFactory;
import DataModel.ALM.Records.DmAlmElementI;
import DataModel.DmElementField_ReadOnlyFromSource;
import DataModel.DmElementI;
import DataModel.DmFieldI;
import DataModel.DmValueFieldI_Text;
import DataModel.Rq1.DmRq1NodeInterface;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1ExternalLink;
import UiSupport.UiTreeViewRootElementI;
import util.EcvEnumeration;
import util.EcvEnumerationValue;

/**
 *
 * @author GUG2WI
 */
public class DmRq1ExternalLink extends DmRq1Element implements DmRq1NodeInterface, UiTreeViewRootElementI {

    public enum LinkState {
        UNKNOWN(""),
        ACTIVE("Active"),
        INACTIVE("Inactive");

        final private String valueInRq1Record;

        private LinkState(String valueInRq1Record) {
            this.valueInRq1Record = valueInRq1Record;
        }

        public String getValueInRq1Record() {
            return valueInRq1Record;
        }

        static public LinkState getState(DmValueFieldI_Text field) {
            assert (field != null);

            for (LinkState state : LinkState.values()) {
                if (state.valueInRq1Record.equals(field.getValue())) {
                    return (state);
                }
            }
            return (LinkState.UNKNOWN);
        }

    }

    public enum TargetSystem {
        DNG("DNG"),
        UNKNOWN("");

        final private String valueInRq1Record;

        private TargetSystem(String valueInRq1Record) {
            this.valueInRq1Record = valueInRq1Record;
        }

        static public TargetSystem getSystem(String valueInRq1Record) {
            for (TargetSystem type : TargetSystem.values()) {
                if (type.valueInRq1Record.equals(valueInRq1Record)) {
                    return (type);
                }
            }
            return (TargetSystem.UNKNOWN);
        }

    }

    //
    // General fields
    //
    final public DmRq1Field_Text ID;
    final public DmRq1Field_Text NAME;
    final public DmRq1Field_Text EXT_LINK_TYPE;
    final public DmRq1Field_Text LINK_STATE;
    final public DmRq1Field_Text VALIDITY;
    final public DmRq1Field_Text EXT_LINK_COMMENT;
    final public DmRq1Field_Text HISTORY_LOG;
    final public DmRq1Field_Text SOURCE_ID;
    final public DmRq1Field_Text SOURCE_RECORD_TYPE;
    final public DmRq1Field_Text TARGET_SYSTEM;
    final public DmRq1Field_Text SUBMITTER;
    final public DmRq1Field_Date SUBMIT_DATE;
    final public DmRq1Field_Text LAST_MODIFIED_USER;
    final public DmRq1Field_Date LAST_MODIFIED_DATE;

    final public DmRq1Field_Reference<DmRq1Issue> ISSUE;
    final public DmRq1Field_Reference<DmRq1Project> ISSUE_RELEASE_MAP;
    final public DmRq1Field_Reference<DmRq1Project> PROJECT;
    final public DmRq1Field_Reference<DmRq1Release> RELEASE;
    final public DmRq1Field_Reference<DmRq1Project> RELEASE_REL_MAP;
    final public DmRq1Field_Reference<DmRq1WorkItem> WORKITEM;

    final public DmElementField_ReadOnlyFromSource<DmElementI> EXTERNAL_LINK;
    final public DmRq1Field_Reference<DmRq1Problem> PROBLEM;

    //
    // ALM fields
    //
    final public DmRq1Field_Text TARGET_SERVER_1;
    final public DmRq1Field_Text TARGET_SERVER_2;
    final public DmRq1Field_Text TARGET_TYPE;
    final public DmRq1Field_Text TARGET_ID;
    final public DmRq1Field_Text TARGET_PROJECT_ID;
    final public DmRq1Field_Text TARGET_COMPONENT_ID;
    final public DmRq1Field_Text TARGET_PARAMETER;
    final public DmRq1Field_Text TARGET_CONFIGURATION_SCOPE;
    final public DmRq1Field_Text TARGET_CONFIGURATION_TYPE;
    final public DmRq1Field_Text TARGET_CONFIGURATION_ID;
    final public DmRq1Field_Text TARGET_URL;

    //
    // SDOM fields
    //
    final public DmRq1Field_Text DEFECT_ID;
    final public DmRq1Field_Text EFFECT_OF_THE_DEFECT;
    final public DmRq1Field_Text EXTERNAL_DESCRIPTION;
    final public DmRq1Field_Text FREE_TEXT;
    final public DmRq1Field_Text INTERNAL_DESCRIPTION;
    final public DmRq1Field_Text LOCK_CONDITION;
    final public DmRq1Field_Text PRIMARY_ROOT_CAUSE;
    final public DmRq1Field_Text SOLUTION;
    final public DmRq1Field_Text VERSION_NAME;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1ExternalLink(Rq1ExternalLink rq1Record) {
        super("EXTERNAL-LINK", rq1Record);

        //
        // General fields
        //
        addField(ID = new DmRq1Field_Text(rq1Record.ID, "ID"));
        addField(NAME = new DmRq1Field_Text(rq1Record.NAME, "Name"));
        NAME.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        addField(EXT_LINK_TYPE = new DmRq1Field_Text(rq1Record.EXT_LINK_TYPE, "Ext Link Type"));
        addField(LINK_STATE = new DmRq1Field_Text(rq1Record.LINK_STATE, "Link State"));
        addField(VALIDITY = new DmRq1Field_Text(rq1Record.VALIDITY, "Validity"));
        addField(EXT_LINK_COMMENT = new DmRq1Field_Text(rq1Record.EXT_LINK_COMMENT, "Ext Link Comment"));
        EXT_LINK_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        addField(HISTORY_LOG = new DmRq1Field_Text(rq1Record.HISTORY_LOG, "History Log"));
        addField(SOURCE_ID = new DmRq1Field_Text(rq1Record.SOURCE_ID, "Source-ID"));
        addField(SOURCE_RECORD_TYPE = new DmRq1Field_Text(rq1Record.SOURCE_RECORD_TYPE, "Source Record Type"));
        addField(TARGET_SYSTEM = new DmRq1Field_Text(rq1Record.TARGET_SYSTEM, "Target System"));
        addField(SUBMITTER = new DmRq1Field_Text(rq1Record.SUBMITTER, "Submitter"));
        addField(SUBMIT_DATE = new DmRq1Field_Date(rq1Record.SUBMIT_DATE, "Submit Date"));
        addField(LAST_MODIFIED_USER = new DmRq1Field_Text(rq1Record.LAST_MODIFIED_USER, "Last Modified User"));
        addField(LAST_MODIFIED_DATE = new DmRq1Field_Date(rq1Record.LAST_MODIFIED_DATE, "Last Modified Date"));

        addField(ISSUE = new DmRq1Field_Reference<>(rq1Record.BELONGS_TO_ISSUE, "Issue"));
        addField(ISSUE_RELEASE_MAP = new DmRq1Field_Reference<>(rq1Record.BELONGS_TO_ISSUE_RELEASE_MAP, "IRM"));
        addField(PROJECT = new DmRq1Field_Reference<>(rq1Record.BELONGS_TO_PROJECT, "Project"));
        addField(RELEASE = new DmRq1Field_Reference<>(rq1Record.BELONGS_TO_RELEASE, "Release"));
        addField(RELEASE_REL_MAP = new DmRq1Field_Reference<>(rq1Record.BELONGS_TO_RELEASE_REL_MAP, "RRM"));
        addField(WORKITEM = new DmRq1Field_Reference<>(rq1Record.BELONGS_TO_WORKITEM, "Workitem"));
        addField(PROBLEM = new DmRq1Field_Reference<>(rq1Record.BELONGS_TO_PROBLEM, "Problem"));

        addField(EXTERNAL_LINK = new DmElementField_ReadOnlyFromSource<DmElementI>("External Link") {
            @Override
            public DmElementI getElement() {
                return (getExternalElement());
            }
        });

        //
        // ALM fields
        //
        addField(TARGET_SERVER_1 = new DmRq1Field_Text(rq1Record.TARGET_SERVER_1, "Target Server 1"));
        addField(TARGET_SERVER_2 = new DmRq1Field_Text(rq1Record.TARGET_SERVER_2, "Target Server 2"));
        addField(TARGET_TYPE = new DmRq1Field_Text(rq1Record.TARGET_TYPE, "Target Type"));
        addField(TARGET_ID = new DmRq1Field_Text(rq1Record.TARGET_ID, "Target ID"));
        addField(TARGET_PROJECT_ID = new DmRq1Field_Text(rq1Record.TARGET_PROJECT_ID, "Target Project ID"));
        addField(TARGET_COMPONENT_ID = new DmRq1Field_Text(rq1Record.TARGET_COMPONENT_ID, "Target Component ID"));
        addField(TARGET_PARAMETER = new DmRq1Field_Text(rq1Record.TARGET_PARAMETER, "Target Parameter"));
        TARGET_PARAMETER.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);
        addField(TARGET_CONFIGURATION_SCOPE = new DmRq1Field_Text(rq1Record.TARGET_CONFIGURATION_SCOPE, "Target Configuration Scope"));
        addField(TARGET_CONFIGURATION_TYPE = new DmRq1Field_Text(rq1Record.TARGET_CONFIGURATION_TYPE, "Target Configuration Type"));
        addField(TARGET_CONFIGURATION_ID = new DmRq1Field_Text(rq1Record.TARGET_CONFIGURATION_ID, "Target Configuration ID"));
        addField(TARGET_URL = new DmRq1Field_Text(rq1Record.TARGET_URL, "Target URL"));
        TARGET_URL.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);

        //
        // SDOM fields
        //
        addField(DEFECT_ID = new DmRq1Field_Text(rq1Record.DEFECT_ID, "Defect ID"));
        addField(EFFECT_OF_THE_DEFECT = new DmRq1Field_Text(rq1Record.EFFECT_OF_THE_DEFECT, "Effect of the Defect"));
        addField(EXTERNAL_DESCRIPTION = new DmRq1Field_Text(rq1Record.EXTERNAL_DESCRIPTION, "External Description"));
        addField(FREE_TEXT = new DmRq1Field_Text(rq1Record.FREE_TEXT, "Free Text"));
        addField(INTERNAL_DESCRIPTION = new DmRq1Field_Text(rq1Record.INTERNAL_DESCRIPTION, "Internal Description"));
        addField(LOCK_CONDITION = new DmRq1Field_Text(rq1Record.LOCK_CONDITION, "Lock Condition"));
        addField(PRIMARY_ROOT_CAUSE = new DmRq1Field_Text(rq1Record.PRIMARY_ROOT_CAUSE, "Primary Root Cause"));
        addField(SOLUTION = new DmRq1Field_Text(rq1Record.SOLUTION, "Solution / Workaround"));
        addField(VERSION_NAME = new DmRq1Field_Text(rq1Record.VERSION_NAME, "Version Name"));
    }

    @Override
    public String getTitle() {
        return (NAME.getValueAsText());
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + ID.getValueAsText() + " - " + NAME.getValueAsText(60));
    }

    @Override
    public EcvEnumeration getLifeCycleState() {
        return (new EcvEnumerationValue(LINK_STATE.getValueAsText(), 0));
    }

    private DmElementI getExternalElement() {

        if ("DNG".equals(TARGET_SYSTEM.getValue())) {
            String almUrl = TARGET_URL.getValueAsText();
            if (almUrl.isEmpty() == false) {
                DmAlmElementI dngElement = DmAlmElementFactory.getElementByUrl(almUrl);
                return (dngElement);
            }
        }

        return (null);
    }

    public TargetSystem getTargetSystem() {
        return (TargetSystem.getSystem(TARGET_SYSTEM.getValue()));
    }

}
