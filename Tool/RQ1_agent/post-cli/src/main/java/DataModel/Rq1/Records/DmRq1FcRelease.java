/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Monitoring.Rule_Fc_Close;
import DataModel.Monitoring.Rule_Fc_Conflicted;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.Rule_CheckDatesForBcAndFc;
import DataModel.Rq1.Monitoring.Rule_Fc_NamingConvention;
import DataModel.Rq1.Monitoring.Rule_Fc_ReqDate;
import DataModel.Rq1.Monitoring.Rule_Fc_WithoutLinkToBc;
import Ipe.Annotations.IpeFactoryConstructor;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Records.Rq1FcRelease;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Templates.Rq1TemplateI;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;
import util.UiWorker;

/**
 *
 * @author GUG2WI
 */
public class DmRq1FcRelease extends DmRq1Fc {

    final public DmRq1Field_Enumeration CATEGORY;
    final public DmRq1Field_Enumeration CLASSIFICATION;

    final public DmRq1Field_Text EXTERNAL_SUBMITTER_NAME;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_ORGANIZATION;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_DEPARTMENT;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_EMAIL;
    final public DmRq1Field_Text EXTERNAL_SUBMITTER_PHONE;

    final public DmRq1Field_Reference<DmRq1Contact> EXTERNAL_ASSIGNEE;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_NAME;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_ORGANIZATION;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_DEPARTMENT;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_EMAIL;
    final public DmRq1Field_Text EXTERNAL_ASSIGNEE_PHONE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1FcRelease(Rq1FcRelease rq1FcRelease) {
        super("FC", rq1FcRelease);

        addField(CATEGORY = new DmRq1Field_Enumeration(this, rq1FcRelease.CATEGORY, "Category"));
        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1FcRelease.CLASSIFICATION, "Classification"));

        addField(EXTERNAL_SUBMITTER_NAME = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_SUBMITTER_NAME, "External Submitter Name"));
        addField(EXTERNAL_SUBMITTER_ORGANIZATION = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_SUBMITTER_ORGANIZATION, "External Submitter Organization"));
        addField(EXTERNAL_SUBMITTER_DEPARTMENT = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_SUBMITTER_DEPARTMENT, "External Submitter Department"));
        addField(EXTERNAL_SUBMITTER_EMAIL = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_SUBMITTER_EMAIL, "External Submitter E-Mail"));
        addField(EXTERNAL_SUBMITTER_PHONE = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_SUBMITTER_PHONE, "External Submitter Phone Number"));

        addField(EXTERNAL_ASSIGNEE = new DmRq1Field_Reference<>(this, rq1FcRelease.EXTERNAL_ASSIGNEE, "External Assignee"));
        addField(EXTERNAL_ASSIGNEE_NAME = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_ASSIGNEE_NAME, "External Assignee Name"));
        addField(EXTERNAL_ASSIGNEE_ORGANIZATION = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_ASSIGNEE_ORGANIZATION, "External Assignee Organization"));
        addField(EXTERNAL_ASSIGNEE_DEPARTMENT = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_ASSIGNEE_DEPARTMENT, "External Assignee Department"));
        addField(EXTERNAL_ASSIGNEE_EMAIL = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_ASSIGNEE_EMAIL, "External Assignee E-Mail"));
        addField(EXTERNAL_ASSIGNEE_PHONE = new DmRq1Field_Text(this, rq1FcRelease.EXTERNAL_ASSIGNEE_PHONE, "External Assignee Phone Number"));

        //
        // Create and add rules
        //
        addRule(new Rule_Fc_NamingConvention(this));
        addRule(new Rule_CheckDatesForBcAndFc(this));
        addRule(new Rule_Fc_Close(this));
        addRule(new Rule_Fc_Conflicted(this));
        addRule(new Rule_Fc_WithoutLinkToBc(this));
        addRule(new Rule_Fc_ReqDate(this));

        //
        // Add switchable rule groups
        //
        addSwitchableGroup(EnumSet.of(RuleExecutionGroup.FC_PLANNING));
    }

//    @Override
//    protected DmRq1Field_Enumeration getClassificationField() {
//        return (CLASSIFICATION);
//    }

    private static DmRq1FcRelease create() {
        Rq1FcRelease rq1Record = new Rq1FcRelease();
        DmRq1FcRelease dmRecord = new DmRq1FcRelease(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmRecord);
        return (dmRecord);
    }

    /**
     * Creates an FC as successor of the given FC. The FC is created in the
     * given target project. The new created FC is not stored in the database.
     *
     * @param predecessor FC which shall be used as predecessor for the new FC.
     * @param targetProject Project in which the new FC shall be created.
     * @param template
     * @return The new created FC.
     */
    public static DmRq1FcRelease createBasedOnPredecessor(DmRq1Fc predecessor, final DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        final DmRq1FcRelease newFcRelease = create();

        //
        // Take over content from parent
        //
//        newFcRelease.ACCOUNT_NUMBERS.setValue(fcRelease.ACCOUNT_NUMBERS.getValue());
        newFcRelease.TITLE.setValue(predecessor.TITLE.getValue() + "_succ");
//        newFcRelease.BASED_ON_PREDECESSOR.setValue(YesNoEmpty.YES);

        if (predecessor instanceof DmRq1FcRelease) {
            newFcRelease.CLASSIFICATION.setValue(((DmRq1FcRelease) predecessor).CLASSIFICATION.getValue());
        }

        //
        // Connect with predecessor
        //
        predecessor.SUCCESSORS.addElement(newFcRelease);
        newFcRelease.PREDECESSOR.setElement(predecessor);

        //
        // Connect with Project
        //
        newFcRelease.PROJECT.setElement(targetProject);
        UiWorker.execute(new UiWorker<Void>("Add FC to project") {

            @Override
            protected Void backgroundTask() {
                // Execution in background because of https://rb-wam.bosch.com/tracker01/browse/ECVTOOL-404 - Create Successor BC lasts too long
                // targetProject.ALL_RELEASES.addElement(newBcRelease); -> not necessary
                targetProject.OPEN_FC.addElement(newFcRelease);
                return (null);
            }
        });
        if (template != null) {
            template.execute(newFcRelease);
        }
        return (newFcRelease);
    }

    final public void addSuccessor(DmRq1Fc newSuccessor) throws ExistsAlready {
        super.addSuccessor(newSuccessor);
    }

    /**
     * Create a new FC release based on a existing FC and map it to an I-FD via
     * IRM.
     *
     * @param targetProject Project n which the FC shall be created.
     * @param predecessor FC which shall be the predecessor of the new FC.
     * @param issueFd I-FD to which the new FC shall be mapped.
     * @param template The RQ1 template that shall be used for the creation of
     * the FC.
     * @return The new created FC.
     */
    public static DmRq1FcRelease createFCBasedOnPredeccessorAndIRM(DmRq1SoftwareProject targetProject, DmRq1Fc predecessor, DmRq1IssueFD issueFd, Rq1TemplateI template) {
        DmRq1FcRelease fcRelease = DmRq1FcRelease.createBasedOnPredecessor(predecessor, targetProject, template);
        try {
            DmRq1Irm_Fc_IssueFd.create(fcRelease, issueFd);
        } catch (DmRq1MapExistsException ex) {
            Logger.getLogger(DmRq1IssueFD.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueFD.class.getName(), ex);
            throw new Error("Unexpected Exception during creation of IRM with new bcRelease");
        }
        return fcRelease;
    }

    /**
     * Returns all FC releases which have a date of last change later or equal
     * to the given date.
     *
     * @param date Date to filter the returned FC releases.
     * @return The list of FC releases. An empty list, if not FC release fits to
     * the filter.
     */
    public static List<DmRq1FcRelease> getAllChangedLaterOrEqual(EcvDate date) {
        assert (date != null);

        List<DmRq1FcRelease> result = new ArrayList<>();
        for (Rq1FcRelease fcRelease : Rq1FcRelease.getAllChangedLaterOrEqual(date, null)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(fcRelease);
            if (dmElement instanceof DmRq1FcRelease) {
                result.add((DmRq1FcRelease) dmElement);
            }
        }
        return (result);
    }

    /**
     * Returns all FC releases which have a date of last change later or equal
     * to the given date and are in one of the wanted life cycle states.
     *
     * @param date Date to filter the returned FC releases.
     * @param wantedLifeCycleState Set of wanted life cycles state. Must not be
     * empty.
     * @return The list of FC releases. An empty list, if not FC release fits to
     * the filter.
     */
    public static List<DmRq1FcRelease> getAllChangedLaterOrEqual(EcvDate date, EnumSet<LifeCycleState_Release> wantedLifeCycleState) {
        assert (date != null);
        assert (wantedLifeCycleState != null);
        assert (wantedLifeCycleState.isEmpty() == false);

        List<DmRq1FcRelease> result = new ArrayList<>();
        for (Rq1FcRelease fcRelease : Rq1FcRelease.getAllChangedLaterOrEqual(date, wantedLifeCycleState)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(fcRelease);
            if (dmElement instanceof DmRq1FcRelease) {
                result.add((DmRq1FcRelease) dmElement);
            }
        }
        return (result);
    }

    /**
     * Calculates the total estimated effort. This contains:
     *
     * - Estimated Effort of I-FD.
     *
     * - Estimated Effort of all workitems on the FcRelease.
     *
     * @return The Total Estimated Effort as integer.
     */
    public int calculateTotalEstimatedEffort() {
        int totalEffort = 0;
        //totalEffort += parseIntNoException(this.ESTIMATED_EFFORT.getValueAsText());
        for (DmRq1WorkItem fcrelease_wi : this.WORKITEMS.getElementList()) {
            if (fcrelease_wi.isCanceled() == false) {
                totalEffort += parseIntNoException(fcrelease_wi.EFFORT_ESTIMATION.getValueAsText());
            }
        }
        return (totalEffort);
    }

    /*
    * Parses a number stored as a string to an integer.
    * @param value, the string to be parsed to an integer.
    * @return Integer
     */
    static int parseIntNoException(String value) {
        int parI = 0;
        try {
            parI = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            parI = 0;
        }
        return (parI);
    }

}
