/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Monitoring.Rule_CheckDatesForPvar;
import DataModel.Rq1.Monitoring.Rule_Pvar_Derivatives;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1PvarRelease;
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
public class DmRq1PvarRelease extends DmRq1Pvar implements DmRq1PstReleaseI {

    final public DmRq1Field_Enumeration CATEGORY;
    final public DmRq1Field_Enumeration CLASSIFICATION;
    public static final String ELEMENTTYPE = "PVAR";

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1PvarRelease(Rq1PvarRelease rq1PvarRelease) {
        super(ELEMENTTYPE, rq1PvarRelease);

        addField(CATEGORY = new DmRq1Field_Enumeration(this, rq1PvarRelease.CATEGORY, "Category"));
        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1PvarRelease.CLASSIFICATION, "Classification"));

        //
        // Rules
        //
        addRule(new Rule_CheckDatesForPvar(this));
        addRule(new Rule_Pvar_Derivatives(this));
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

    public static DmRq1PvarRelease create() {
        Rq1PvarRelease rq1Record = new Rq1PvarRelease();
        DmRq1PvarRelease dmElement = new DmRq1PvarRelease(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmElement);
        return (dmElement);
    }

    public static DmRq1PvarRelease createBasedOnPredecessor(DmRq1Pst predecessor, DmRq1Project project) {
        assert (predecessor != null);
        assert (project != null);

        DmRq1PvarRelease newPvarRelease = create();

        //
        // Take over content from parent
        //
        newPvarRelease.ACCOUNT_NUMBERS.setValue(predecessor.ACCOUNT_NUMBERS.getValue());
        newPvarRelease.TITLE.setValue(predecessor.TITLE.getValue() + "_succ");

        //
        // Connect with predecessor
        //
        predecessor.SUCCESSORS.addElement(newPvarRelease);
        newPvarRelease.PREDECESSOR.setElement(predecessor);

        //
        // Connect with Project
        //
        newPvarRelease.PROJECT.setElement(project);
        project.ALL_RELEASES.addElementIfLoaded(newPvarRelease);
        project.OPEN_RELEASES.addElementIfLoaded(newPvarRelease);

        return (newPvarRelease);
    }

    /**
     * Returns all PVAR releases which have a date of last change later or equal
     * to the given date.
     *
     * @param date Date to filter the returned PVAR releases.
     * @return The list of PVAR releases. An empty list, if not PVAR release
     * fits to the filter.
     */
    public static List<DmRq1PvarRelease> getAllChangedLaterOrEqual(EcvDate date) {
        assert (date != null);

        List<DmRq1PvarRelease> result = new ArrayList<>();
        for (Rq1PvarRelease fcRelease : Rq1PvarRelease.getAllChangedLaterOrEqual(date, null)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(fcRelease);
            if (dmElement instanceof DmRq1PvarRelease) {
                result.add((DmRq1PvarRelease) dmElement);
            }
        }
        return (result);
    }

    /**
     * Returns all PVAR releases which have a date of last change later or equal
     * to the given date and are in one of the wanted life cycle states.
     *
     * @param date Date to filter the returned PVAR releases.
     * @param wantedLifeCycleState Set of wanted life cycles state. Must not be
     * empty.
     * @return The list of PVAR releases. An empty list, if not PVAR release
     * fits to the filter.
     */
    public static List<DmRq1PvarRelease> getAllChangedLaterOrEqual(EcvDate date, EnumSet<LifeCycleState_Release> wantedLifeCycleState) {
        assert (date != null);
        assert (wantedLifeCycleState != null);
        assert (wantedLifeCycleState.isEmpty() == false);

        List<DmRq1PvarRelease> result = new ArrayList<>();
        for (Rq1PvarRelease fcRelease : Rq1PvarRelease.getAllChangedLaterOrEqual(date, wantedLifeCycleState)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(fcRelease);
            if (dmElement instanceof DmRq1PvarRelease) {
                result.add((DmRq1PvarRelease) dmElement);
            }
        }
        return (result);
    }

}
