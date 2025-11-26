/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Monitoring.Rule_CheckDatesForPver;
import DataModel.Rq1.Monitoring.Rule_Pver_Derivatives;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1PverRelease;
import Rq1Data.Enumerations.CategoryPstRelease;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public class DmRq1PverRelease extends DmRq1Pver implements DmRq1PstReleaseI {

    final public DmRq1Field_Enumeration CATEGORY;
    final public DmRq1Field_Enumeration CLASSIFICATION;
    public static final String ELEMENTTYPE = "PVER";

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1PverRelease(Rq1PverRelease rq1PverRelease) {
        super(ELEMENTTYPE, rq1PverRelease);

        addField(CATEGORY = new DmRq1Field_Enumeration(this, rq1PverRelease.CATEGORY, "Category"));
        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1PverRelease.CLASSIFICATION, "Classification"));

        addRule(new Rule_CheckDatesForPver(this));
        addRule(new Rule_Pver_Derivatives(this));
    }

    @Override
    public CategoryPstRelease getCategory() {
        EcvEnumeration result = CATEGORY.getValue();
        if (result instanceof CategoryPstRelease) {
            return ((CategoryPstRelease) result);
        }
        return (null);
    }

    @Override
    public DmRq1Field_Enumeration getClassificationField() {
        return (CLASSIFICATION);
    }

    public static DmRq1PverRelease createBasedOnPredecessor(DmRq1Pst ecuRelease, DmRq1Project targetProject) {
        assert (ecuRelease != null);
        assert (targetProject != null);

        DmRq1PverRelease newPverRelease = create();

        //
        // Take over content from parent
        //
        newPverRelease.ACCOUNT_NUMBERS.setValue(ecuRelease.ACCOUNT_NUMBERS.getValue());

        newPverRelease.TITLE.setValue(ecuRelease.TITLE.getValue() + "_succ");
//        newPverRelease.BASED_ON_PREDECESSOR.setValue(YesNoEmpty.YES);

        //
        // Connect with predecessot
        //
        ecuRelease.SUCCESSORS.addElement(newPverRelease);
        newPverRelease.PREDECESSOR.setElement(ecuRelease);

        //
        // Connect with Project
        //
        newPverRelease.PROJECT.setElement(targetProject);
        targetProject.ALL_RELEASES.addElementIfLoaded(newPverRelease);
        targetProject.OPEN_RELEASES.addElementIfLoaded(newPverRelease);

        return (newPverRelease);
    }

    public static DmRq1PverRelease create() {
        Rq1PverRelease rq1Record = new Rq1PverRelease();
        DmRq1PverRelease dmElement = new DmRq1PverRelease(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmElement);
        return (dmElement);
    }

//    public static DmRq1PverRelease create(DmRq1SoftwareProject project, String title, EcvDate plannedDate, ClassificationPstRelease classification) {
//        assert (project != null);
//        assert (title != null);
//        assert (title.isEmpty() == false);
//        assert (plannedDate != null);
//        assert (plannedDate.isEmpty() == false);
//        assert (classification != null);
//
//        DmRq1PverRelease pver = create();
//
//        pver.PROJECT.setElement(project);
//        pver.TITLE.setValue(title);
//        pver.PLANNED_DATE.setValue(plannedDate);
//        pver.CLASSIFICATION.setValue(classification);
//        pver.BASED_ON_PREDECESSOR.setValue(YesNoEmpty.NO);
//
//        return (pver);
//    }
    /**
     * Returns all FC releases which have a date of last change later or equal
     * to the given date.
     *
     * @param date Date to filter the returned FC releases.
     * @return The list of FC releases. An empty list, if not FC release fits to
     * the filter.
     */
    public static List<DmRq1PverRelease> getAllChangedLaterOrEqual(EcvDate date) {
        assert (date != null);

        List<DmRq1PverRelease> result = new ArrayList<>();
        for (Rq1PverRelease fcRelease : Rq1PverRelease.getAllChangedLaterOrEqual(date, null)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(fcRelease);
            if (dmElement instanceof DmRq1PverRelease) {
                result.add((DmRq1PverRelease) dmElement);
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
    public static List<DmRq1PverRelease> getAllChangedLaterOrEqual(EcvDate date, EnumSet<LifeCycleState_Release> wantedLifeCycleState) {
        assert (date != null);
        assert (wantedLifeCycleState != null);
        assert (wantedLifeCycleState.isEmpty() == false);

        List<DmRq1PverRelease> result = new ArrayList<>();
        for (Rq1PverRelease fcRelease : Rq1PverRelease.getAllChangedLaterOrEqual(date, wantedLifeCycleState)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(fcRelease);
            if (dmElement instanceof DmRq1PverRelease) {
                result.add((DmRq1PverRelease) dmElement);
            }
        }
        return (result);
    }

}
