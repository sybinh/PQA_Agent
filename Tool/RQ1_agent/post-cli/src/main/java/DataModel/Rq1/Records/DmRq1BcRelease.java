/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Monitoring.Rule_Bc_Conflicted;
import DataModel.Monitoring.Rule_Bc_Requested;
import DataModel.Monitoring.Rule_Bc_Requested_FourWeeks;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Monitoring.Rule_Bc_CheckPstDates;
import DataModel.Rq1.Monitoring.Rule_Bc_Close;
import DataModel.Rq1.Monitoring.Rule_Bc_NamingConvention;
import DataModel.Rq1.Monitoring.Rule_Bc_WithoutLinkToPst;
import DataModel.Rq1.Monitoring.Rule_CheckDatesForBcAndFc;
import DataModel.DmMappedElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Records.Rq1BcRelease;
import Rq1Data.Enumerations.CategoryBcRelease;
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
public class DmRq1BcRelease extends DmRq1Bc {

    final public DmRq1Field_Enumeration CATEGORY;
    final public DmRq1Field_Enumeration CLASSIFICATION;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1BcRelease(Rq1BcRelease rq1BcRelease) {
        super("BC", rq1BcRelease);

        addField(CATEGORY = new DmRq1Field_Enumeration(this, rq1BcRelease.CATEGORY, "Category"));
        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1BcRelease.CLASSIFICATION, "Classification"));

        //
        // Create and add rules
        //
        addRule(new Rule_Bc_NamingConvention(this));
        addRule(new Rule_CheckDatesForBcAndFc(this));
        addRule(new Rule_Bc_WithoutLinkToPst(this));
        addRule(new Rule_Bc_Close(this));
        addRule(new Rule_Bc_Requested(this));
        addRule(new Rule_Bc_Conflicted(this));
        addRule(new Rule_Bc_CheckPstDates(this));
        addRule(new Rule_Bc_Requested_FourWeeks(this));

//
        // Add switchable rule groups
        //
        addSwitchableGroup(EnumSet.of(RuleExecutionGroup.BC_PLANNING));
    }

    @Override
    final protected void setSwitchableGroupActiveForSubElements(RuleExecutionGroup group, boolean activate) {
        assert (group != null);

        if (group == RuleExecutionGroup.BC_PLANNING) {
            for (DmMappedElement<DmRq1Rrm, DmRq1Pst> issueMap : MAPPED_PST.getElementList()) {
                issueMap.getMap().setSwitchableGroupActive(group, activate);
                issueMap.getTarget().setSwitchableGroupActive(group, activate);
            }
            for (DmMappedElement<DmRq1Rrm, DmRq1Release> issueMap : MAPPED_CHILDREN.getElementList()) {
                issueMap.getMap().setSwitchableGroupActive(group, activate);
                issueMap.getTarget().setSwitchableGroupActive(group, activate);
            }
            for (DmMappedElement<DmRq1Irm, DmRq1Issue> issueMap : MAPPED_ISSUES.getElementList()) {
                issueMap.getMap().setSwitchableGroupActive(group, activate);
                issueMap.getTarget().setSwitchableGroupActive(group, activate);
            }
        }

    }

    public static DmRq1BcRelease create() {
        Rq1BcRelease rq1Record = new Rq1BcRelease();
        DmRq1BcRelease dmRecord = new DmRq1BcRelease(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmRecord);
        return (dmRecord);
    }

    public static DmRq1BcRelease createBasedOnProject(final DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        assert (targetProject != null);

        final DmRq1BcRelease newBcRelease = create();

        //
        // Connect with Project
        //
        newBcRelease.PROJECT.setElement(targetProject);
        UiWorker.execute(new UiWorker<Void>("Add BC to project") {

            @Override
            protected Void backgroundTask() {
                // Execution in background because of https://rb-wam.bosch.com/tracker01/browse/ECVTOOL-404 - Create Successor BC lasts too long
                // targetProject.ALL_RELEASES.addElement(newBcRelease); -> not necessary
                targetProject.OPEN_BC.addElement(newBcRelease);
                return (null);
            }
        });
        if (template != null) {
            template.execute(newBcRelease);
        }
        return (newBcRelease);
    }

    /**
     * Creates an BC as successor of the given BC. The BC is created in the
     * given target project. The new created BC is not stored in the database.
     *
     * @param predecessor BC which shall be used as predecessor for the new BC.
     * @param targetProject Project in which the new BC shall be created.
     * @param template
     * @return The new created BC.
     */
    public static DmRq1BcRelease createBasedOnPredecessor(DmRq1Bc predecessor, final DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        final DmRq1BcRelease newBcRelease = create();

        //
        // Take over content from parent
        //
//        newBcRelease.ACCOUNT_NUMBERS.setValue(bcRelease.ACCOUNT_NUMBERS.getValueNeverNull());
        newBcRelease.TITLE.setValue(predecessor.TITLE.getValue() + "_succ");
//        newBcRelease.BASED_ON_PREDECESSOR.setValue(YesNoEmpty.YES);

        if (predecessor instanceof DmRq1BcRelease) {
            newBcRelease.CATEGORY.setValue(((DmRq1BcRelease) predecessor).CATEGORY.getValue());
            newBcRelease.CLASSIFICATION.setValue(((DmRq1BcRelease) predecessor).CLASSIFICATION.getValue());
        } else {
            newBcRelease.CATEGORY.setValue(CategoryBcRelease.SW_VERSION);
        }

        //
        // Connect with predecessor
        //
        predecessor.SUCCESSORS.addElement(newBcRelease);
        newBcRelease.PREDECESSOR.setElement(predecessor);

        //
        // Connect with Project
        //
        newBcRelease.PROJECT.setElement(targetProject);
        UiWorker.execute(new UiWorker<Void>("Add BC to project") {

            @Override
            protected Void backgroundTask() {
                // Execution in background because of https://rb-wam.bosch.com/tracker01/browse/ECVTOOL-404 - Create Successor BC lasts too long
                // targetProject.ALL_RELEASES.addElement(newBcRelease); -> not necessary
                targetProject.OPEN_BC.addElement(newBcRelease);
                return (null);
            }
        });
        if (template != null) {
            template.execute(newBcRelease);
        }
        return (newBcRelease);
    }

    public static DmRq1BcRelease createBCBasedOnPredeccessorAndIRM(DmRq1SoftwareProject targetProject, DmRq1Bc predeccessor, DmRq1IssueFD issueFd, Rq1TemplateI template) {
        DmRq1BcRelease bcRelease = DmRq1BcRelease.createBasedOnPredecessor(predeccessor, targetProject, template);
        try {
            DmRq1Irm_Bc_IssueFd.create(bcRelease, issueFd);
        } catch (DmRq1MapExistsException ex) {
            Logger.getLogger(DmRq1IssueFD.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueFD.class.getName(), ex);
            throw new Error("Unexepected Exception during creation of IRM with new bcRelease");
        }
        return bcRelease;
    }

    /**
     * Returns all BC releases which have a date of last change later or equal
     * to the given date.
     *
     * @param date Date to filter the returned BC releases.
     * @return The list of BC releases. An empty list, if not BC release fits to
     * the filter.
     */
    public static List<DmRq1BcRelease> getAllChangedLaterOrEqual(EcvDate date) {
        assert (date != null);

        List<DmRq1BcRelease> result = new ArrayList<>();
        for (Rq1BcRelease bcRelease : Rq1BcRelease.getAllChangedLaterOrEqual(date, null)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(bcRelease);
            if (dmElement instanceof DmRq1BcRelease) {
                result.add((DmRq1BcRelease) dmElement);
            }
        }
        return (result);
    }

    /**
     * Returns all BC releases which have a date of last change later or equal
     * to the given date and are in one of the wanted life cycle states.
     *
     * @param date Date to filter the returned BC releases.
     * @param wantedLifeCycleState Set of wanted life cycles state. Must not be
     * empty.
     * @return The list of BC releases. An empty list, if not BC release fits to
     * the filter.
     */
    public static List<DmRq1BcRelease> getAllChangedLaterOrEqual(EcvDate date, EnumSet<LifeCycleState_Release> wantedLifeCycleState) {
        assert (date != null);
        assert (wantedLifeCycleState != null);
        assert (wantedLifeCycleState.isEmpty() == false);

        List<DmRq1BcRelease> result = new ArrayList<>();
        for (Rq1BcRelease bcRelease : Rq1BcRelease.getAllChangedLaterOrEqual(date, wantedLifeCycleState)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(bcRelease);
            if (dmElement instanceof DmRq1BcRelease) {
                result.add((DmRq1BcRelease) dmElement);
            }
        }
        return (result);
    }

    /**
     * Calculates the total estimated effort. This contains:
     *
     * - Estimated Effort of I-FD.
     *
     * - Estimated Effort of all workitems on the BcRelease.
     *
     * @return The Total Estimated Effort as integer.
     */
    public int calculateTotalEstimatedEffort() {
        int totalEffort = 0;
        //totalEffort += parseIntNoException(this.ESTIMATED_EFFORT.getValueAsText());
        for (DmRq1WorkItem bcrelease_wi : this.WORKITEMS.getElementList()) {
            if (bcrelease_wi.isCanceled() == false) {
                totalEffort += parseIntNoException(bcrelease_wi.EFFORT_ESTIMATION.getValueAsText());
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

    public final void addSuccessor(DmRq1Bc newSuccessor) throws ExistsAlready {
        super.addSuccessor(newSuccessor);
    }
}
