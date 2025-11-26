/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import static Rq1Cache.Records.Rq1Release.ATTRIBUTE_CATEGORY;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Data.Enumerations.CategoryPstRelease;
import Rq1Data.Enumerations.ClassificationPstRelease;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class Rq1PverRelease extends Rq1Pver implements Rq1PstReleaseI {

    final public Rq1DatabaseField_Enumeration CATEGORY;
    final public Rq1DatabaseField_Enumeration CLASSIFICATION;

    public Rq1PverRelease() {
        super(Rq1NodeDescription.PVER_RELEASE);

        addField(CATEGORY = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_CATEGORY, CategoryPstRelease.values()));
        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", ClassificationPstRelease.values()));
        CLASSIFICATION.acceptInvalidValuesInDatabase();
    }

    static public Iterable<Rq1PverRelease> getAllChangedLaterOrEqual(EcvDate date, EnumSet<LifeCycleState_Release> wantedLifeCycleState) {
        assert (date != null);
        assert (date.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.PVER_RELEASE.getRecordType());
        Rq1NodeDescription.PVER_RELEASE.setFixedRecordCriterias(query);
        query.addCriteria_isLaterOrEqualThen(ATTRIBUTE_LAST_MODIFIED_DATE, date);
        if (wantedLifeCycleState != null) {
            query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, wantedLifeCycleState);
        }

        //
        // Handle result
        //
        List<Rq1PverRelease> result = new ArrayList<>();
        List<Rq1Reference> referenzList = query.getReferenceList();
        for (Rq1Reference r : referenzList) {
            if (r.getRecord() instanceof Rq1PverRelease) {
                result.add((Rq1PverRelease) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }
        return (result);
    }

}
