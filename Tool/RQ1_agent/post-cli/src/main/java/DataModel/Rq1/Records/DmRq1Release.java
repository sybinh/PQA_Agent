/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import ConfigurableRules.ConfigRule.Monitoring.ConfigurableRuleManagerRule_Rq1AssignedRecord;
import DataModel.DmFieldI;
import static DataModel.DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Number;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Fields.DmRq1Field_Xml;
import DataModel.Rq1.Monitoring.Rule_Release_Predecessor;
import OslcAccess.OslcLoadHint;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1Release;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Enumerations.YesNoEmpty;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.RoadmapRowI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import util.EcvDate;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Release extends DmRq1ReleaseRecord implements RoadmapRowI {

    final public DmRq1Field_Date ACTUAL_DATE;
    final public DmRq1Field_Enumeration APPROVAL;
    final public DmRq1Field_Date DEFAULT_REQUESTED_DELIVERY_DATE;
    final public DmRq1Field_Date DELIVERY_FREEZE;
    final public DmRq1Field_Number ESTIMATED_EFFORT;
    final public DmRq1Field_Text ESTIMATION_COMMENT;
    final public DmRq1Field_Text EXTERNAL_COMMENT;
    final public DmRq1Field_Text EXTERNAL_DESCRIPTION;
    final public DmRq1Field_Text EXTERNAL_ID;
    final public DmRq1Field_Text EXTERNAL_STATE;
    final public DmRq1Field_Text EXTERNAL_TITLE;
    final public DmRq1Field_Date IMPLEMENTATION_FREEZE;
    final public DmRq1Field_Xml MILESTONES;

    final public DmRq1Field_Enumeration PLANNING_GRANULARITY;
    final public DmRq1Field_Text PRODUCT;
    final public DmRq1Field_Text SCM_REFERENCES;

    final public DmRq1Field_Date START_DATE;
    //
    final public DmRq1Field_Reference<DmRq1Release> PREDECESSOR;
    final public DmRq1Field_ReferenceList<DmRq1Release> SUCCESSORS;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm, DmRq1Issue> MAPPED_ISSUES;
    final public DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Release> MAPPED_CHILDREN;
    final public DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Release> MAPPED_PARENT;
    //
    final public DmRq1Field_Enumeration EXPORT_SCOPE;

    public DmRq1Release(String subjectType, Rq1Release release) {
        super(subjectType, release);

        //
        // Create and add fields
        //
        addField((ACTUAL_DATE = new DmRq1Field_Date(this, release.ACTUAL_DATE, "Actual Date")));
        ACTUAL_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        ACTUAL_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(DEFAULT_REQUESTED_DELIVERY_DATE = new DmRq1Field_Date(this, release.DEFAULT_REQUESTED_DELIVERY_DATE, "Default Req. Del. Date"));
        DEFAULT_REQUESTED_DELIVERY_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(DELIVERY_FREEZE = new DmRq1Field_Date(this, release.DELIVERY_FREEZE, "Delivery Freeze"));
        DELIVERY_FREEZE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(ESTIMATED_EFFORT = new DmRq1Field_Number(release.ESTIMATED_EFFORT, "Estimated Effort"));
        ESTIMATED_EFFORT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.SINGLELINE_TEXT);
        ESTIMATED_EFFORT.setAttribute(FIELD_FOR_BULK_OPERATION);

        addField(ESTIMATION_COMMENT = new DmRq1Field_Text(this, release.ESTIMATION_COMMENT, "Estimation Comment"));
        ESTIMATION_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB, DmFieldI.Attribute.MULTILINE_TEXT);

        addField(EXTERNAL_COMMENT = new DmRq1Field_Text(this, release.EXTERNAL_COMMENT, "External Comment"));
        EXTERNAL_COMMENT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        addField(EXTERNAL_DESCRIPTION = new DmRq1Field_Text(this, release.EXTERNAL_DESCRIPTION, "External Description"));
        EXTERNAL_DESCRIPTION.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);
        addField(EXTERNAL_ID = new DmRq1Field_Text(this, release.EXTERNAL_ID, "External ID"));
        addField(EXTERNAL_STATE = new DmRq1Field_Text(this, release.EXTERNAL_STATE, "External State"));
        addField(EXTERNAL_TITLE = new DmRq1Field_Text(this, release.EXTERNAL_TITLE, "External Title"));
        EXTERNAL_TITLE.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF);

        addField(IMPLEMENTATION_FREEZE = new DmRq1Field_Date(this, release.IMPLEMENTATION_FREEZE, "Implementation Freeze"));

        addField(MILESTONES = new DmRq1Field_Xml(this, release.MILESTONES, "Milestones"));

        addField(APPROVAL = new DmRq1Field_Enumeration(this, release.APPROVAL, "Approval"));
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(PLANNING_GRANULARITY = new DmRq1Field_Enumeration(this, release.PLANNING_GRANULARITY, "Planning Granularity"));
        PLANNING_GRANULARITY.setAttribute(FIELD_FOR_BULK_OPERATION);
        addField(PRODUCT = new DmRq1Field_Text(this, release.PRODUCT, "Product"));
        addField(SCM_REFERENCES = new DmRq1Field_Text(this, release.SCM_REFERENCES, "SCM References"));

        addField((START_DATE = new DmRq1Field_Date(this, release.START_DATE, "Start Date")));
        START_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
        START_DATE.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        ASSIGNEE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        addField(PREDECESSOR = new DmRq1Field_Reference<>(this, release.HAS_PREDECESSOR, "Predecessor"));
        addField(SUCCESSORS = new DmRq1Field_ReferenceList<>(this, release.HAS_SUCCESSOR, "Successor"));

        addField(MAPPED_CHILDREN = new DmRq1Field_MappedReferenceList<>(this, release.HAS_MAPPED_CHILDREN, "Children"));
        addField(MAPPED_PARENT = new DmRq1Field_MappedReferenceList<>(this, release.HAS_MAPPED_PARENTS, "Parent"));
        addField(MAPPED_ISSUES = new DmRq1Field_MappedReferenceList<>(this, release.HAS_MAPPED_ISSUES, "Issues"));

        addField(EXPORT_SCOPE = new DmRq1Field_Enumeration(release.EXPORT_SCOPE, "Guided PjM Export Scope"));

        //
        // Create and add rules
        //
        addRule(new Rule_Release_Predecessor(this));
        addRule(new ConfigurableRuleManagerRule_Rq1AssignedRecord(this));
    }

    @Override
    final public String getElementClass() {
        return ("Release");
    }

    @Override
    final public EcvEnumeration getLifeCycleState() {
        return (LIFE_CYCLE_STATE.getValue());
    }

    @Override
    final public boolean isCanceled() {
        return (LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.CANCELED);
    }

    final public boolean isClosed() {
        return (LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.CLOSED);
    }

    final public boolean isCanceledOrConflicted() {
        return (isCanceled() || isConflicted());
    }

    final public boolean isConflicted() {
        return (LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.CONFLICTED);
    }

    public final boolean isDeveloped() {
        return LIFE_CYCLE_STATE.getValue() == LifeCycleState_Release.DEVELOPED;
    }

    @Override
    public boolean save() {
        setBasedOnPredecessor();
        if (super.existsInDatabase() == false) {
            return (super.save(Rq1Release.ATTRIBUTE_DOMAIN, Rq1Release.ATTRIBUTE_TYPE, Rq1Release.ATTRIBUTE_CATEGORY, Rq1Release.ATTRIBUTE_BASED_ON_PREDECESSOR));
        } else {
            return (super.save());
        }
    }

    @Override
    public boolean save(Rq1AttributeName... fieldOrder) {
        assert (fieldOrder != null);

        Rq1AttributeName[] newFieldOrder;

        if (setBasedOnPredecessor() == true) {

            ArrayList<Rq1AttributeName> al = new ArrayList<Rq1AttributeName>();
            al.add(Rq1Release.ATTRIBUTE_BASED_ON_PREDECESSOR);
            al.addAll(Arrays.asList(fieldOrder));
            newFieldOrder = al.toArray(fieldOrder);

        } else {
            newFieldOrder = fieldOrder;
        }

        return super.save(newFieldOrder);
    }

    private boolean setBasedOnPredecessor() {
        if ((PREDECESSOR.isElementSet() == true) && (BASED_ON_PREDECESSOR.getValue() != YesNoEmpty.YES)) {
            BASED_ON_PREDECESSOR.setValue(YesNoEmpty.YES);
            return (true);
        }
        return (false);
    }

    @Override
    public String getResponsible() {
        return (PROJECT.getElement().RESPONSIBLE_AT_BOSCH.getValue());
    }

    @Override
    public String getType() {
        int indexDot = getTitle().indexOf(":");
        if (indexDot >= 0) {
            return (getTitle().substring(0, indexDot - 1).trim());
        } else {
            return ("");
        }
    }

    /**
     * Returns the name part of the title.
     *
     * @return The name part parsed from the title.
     */
    @Override
    public String getName() {
        int indexSlash = getTitle().indexOf("/");
        String titleWithoutVersion;
        if (indexSlash >= 0) {
            titleWithoutVersion = getTitle().substring(0, indexSlash - 1).trim();
        } else {
            titleWithoutVersion = getTitle().trim();
        }

        int indexDot = getTitle().indexOf(":");
        if (indexDot >= 0) {
            String name = titleWithoutVersion.substring(indexDot + 1).trim();
            return (name);
        } else {
            return (titleWithoutVersion);
        }
    }

    /**
     * Returns the version part of the title.
     *
     * @return The version part parsed from the title.
     */
    @Override
    final public String getVersion() {
        int indexSlash = getTitle().indexOf("/");
        if (indexSlash >= 0) {
            String version = getTitle().substring(indexSlash + 1).trim();
            return (version);
        } else {
            return (getTitle());
        }
    }

    public int compareVersion(DmRq1Release other) {
        assert (other != null);
        return (compareVersion(getVersion(), other.getVersion()));
    }

    /**
     * Made static public for test reasons only. Do not user from outside of the
     * class.
     *
     * @param myVersionString
     * @param otherVersionString
     * @return
     */
    static public int compareVersion(String myVersionString, String otherVersionString) {
        assert (myVersionString != null);
        assert (otherVersionString != null);

        try {
            String[] myVersion = myVersionString.split("(\\.+|_+)");
            String[] otherVersion = otherVersionString.split("(\\.+|_+)");

            int minLength = myVersion.length < otherVersion.length ? myVersion.length : otherVersion.length;

            for (int i = 0; i < minLength; i++) {
                int c = compareNumber(myVersion[i], otherVersion[i]);
                if (c != 0) {
                    return (c);
                }
            }

            return (myVersion.length - otherVersion.length);
        } catch (Throwable ex) {
            Logger.getLogger(DmRq1Release.class.getCanonicalName()).severe("myVersionString=>" + myVersionString + "<");
            Logger.getLogger(DmRq1Release.class.getCanonicalName()).severe("otherVersionString=>" + otherVersionString + "<");
            ToolUsageLogger.logError(DmRq1Release.class.getCanonicalName(), ex);
            throw (ex);
        }
    }

    static private int compareNumber(String s1, String s2) {
        assert (s1 != null);
        assert (s2 != null);

        try {
            if (isNumber(s1) && isNumber(s2)) {
                return (Integer.parseInt(s1) - Integer.parseInt(s2));
            } else {
                return (s1.compareTo(s2));
            }
        } catch (java.lang.NumberFormatException ex) {
            Logger logger = Logger.getLogger(DmRq1Release.class.getCanonicalName());
            logger.severe("s1 = >" + s1 + "<");
            logger.severe("s2 = >" + s2 + "<");
            ToolUsageLogger.logError(DmRq1Release.class.getCanonicalName(), ex);
            throw (ex);
        }

    }

    static private boolean isNumber(String s) {
        assert (s != null);
        for (int i = 0; i < s.length(); i++) {
            if ("0123456789".indexOf(s.charAt(i)) < 0) {
                return (false);
            }
        }
        return (s.length() > 0);
    }

    @Override
    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (LIFE_CYCLE_STATE.getValidInputValues());
    }

    static public class ExistsAlready extends Exception {

    }

    final protected void addSuccessor(DmRq1Release newSuccessor) throws ExistsAlready {
        //
        // Prevent double entries
        //
        for (DmRq1Release p : SUCCESSORS.getElementList()) {
            if (p == newSuccessor) {
                throw (new ExistsAlready());
            }
        }

        //
        // Change references
        //
        DmRq1Release oldPredecessor = (DmRq1Release) newSuccessor.PREDECESSOR.getElement();
        if (oldPredecessor != null) {
            oldPredecessor.SUCCESSORS.removeElement(newSuccessor);
        }
        SUCCESSORS.addElement(newSuccessor);
        newSuccessor.PREDECESSOR.setElement(this);
    }

    @Override
    public DmRq1Project getProjectOfPredecessor() {
        DmRq1Release predecessor = PREDECESSOR.getElement();
        if (predecessor != null) {
            return (predecessor.PROJECT.getElement());
        } else {
            return (null);
        }
    }

//    /**
//     * Returns the field CLASSIFICATION. The field CLASSIFICATION is defined in
//     * the sub classes, because the allowed values differ from type to type.
//     *
//     * @return Content of CLASSIFICATION field.
//     */
//    protected abstract DmRq1Field_Enumeration getClassificationField();
//
//    public String getClassificationAsText() {
//        return (getClassificationField().getValueAsText());
//    }
    //-------------------------------------------------------------------------------------
    //
    // Load optimization
    //
    //-------------------------------------------------------------------------------------
    private boolean isloadCacheForPredecessorListDone = false;

    public void loadCacheForPredecessorList(int depth) {
        assert (depth > 0);

        if (isloadCacheForPredecessorListDone == false) {

            //
            // Load data
            //
            OslcLoadHint loadPredecessor = new OslcLoadHint(false);
            OslcLoadHint currentHint = loadPredecessor;
            for (int i = 0; i <= depth; i++) {
                currentHint = currentHint.followField(Rq1Release.ATTRIBUTE_HAS_PREDECESSOR, true);
            }
            loadIntoCache(loadPredecessor);

            isloadCacheForPredecessorListDone = true;

            DmRq1Release nextRelease = PREDECESSOR.getElement();
            for (int i = 0; (i < depth) && (nextRelease != null); i++) {
                nextRelease.isloadCacheForPredecessorListDone = true;
                nextRelease = nextRelease.PREDECESSOR.getElement();
            }
        }
    }

    @Override
    public EcvDate getMappedPlannedDate() {
        return PLANNED_DATE.getDate();
    }

    @Override
    public int getPositionInRoadMap() {
        return -65;
    }

}
