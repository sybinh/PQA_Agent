/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import RestClient.Exceptions.WriteToDatabaseRejected;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import static Rq1Cache.Records.Rq1BaseRecord.ATTRIBUTE_LAST_MODIFIED_DATE;
import static Rq1Cache.Records.Rq1Release.ATTRIBUTE_CATEGORY;
import static Rq1Cache.Records.Rq1Release.ATTRIBUTE_LIFE_CYCLE_STATE;
import Rq1Cache.Rq1Client;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Data.Enumerations.CategoryBcRelease;
import Rq1Data.Enumerations.ClassificationBcRelease;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class Rq1BcRelease extends Rq1Bc {

    final public Rq1DatabaseField_Enumeration CATEGORY;
    final public Rq1DatabaseField_Enumeration CLASSIFICATION;

    public Rq1BcRelease() {
        super(Rq1NodeDescription.BC_RELEASE);

        addField(CATEGORY = new Rq1DatabaseField_Enumeration(this, ATTRIBUTE_CATEGORY, CategoryBcRelease.values()));
        addField(CLASSIFICATION = new Rq1DatabaseField_Enumeration(this, "Classification", ClassificationBcRelease.values()));
    }

    @Override
    public void forward(Rq1Project project, Rq1RecordInterface newAssignee) {
        try {
            Rq1Client.client.forward(this, project, newAssignee, PLANNING_GRANULARITY);
            handleWriteSuccess();
        } catch (WriteToDatabaseRejected ex) {
            handleWriteError(ex);
        } catch (Exception | Error ex) {
            handleWriteError(ex);
        }
    }

    /**
     * Retrieves a list of all BC in the RQ1 database which where changed after
     * the given date.
     *
     * @param date
     * @param wantedLifeCycleState
     * @return
     */
    static public Iterable<Rq1BcRelease> getAllChangedLaterOrEqual(EcvDate date, EnumSet<LifeCycleState_Release> wantedLifeCycleState) {
        assert (date != null);
        assert (date.isEmpty() == false);

        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.BC_RELEASE.getRecordType());
        Rq1NodeDescription.BC_RELEASE.setFixedRecordCriterias(query);
        query.addCriteria_isLaterOrEqualThen(ATTRIBUTE_LAST_MODIFIED_DATE, date);
        if (wantedLifeCycleState != null) {
            query.addCriteria_ValueList(ATTRIBUTE_LIFE_CYCLE_STATE, wantedLifeCycleState);
        }

        //
        // Handle result
        //
        List<Rq1BcRelease> result = new ArrayList<>();
        List<Rq1Reference> referenzList = query.getReferenceList();
        for (Rq1Reference r : referenzList) {
            if (r.getRecord() instanceof Rq1BcRelease) {
                result.add((Rq1BcRelease) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1Contact.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }
        return (result);
    }
}
