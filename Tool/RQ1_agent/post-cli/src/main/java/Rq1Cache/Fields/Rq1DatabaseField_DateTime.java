/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Monitoring.Rq1DataRule;
import Rq1Cache.Monitoring.Rq1ParseFieldException;
import Rq1Cache.Monitoring.Rq1UnexpectedDataFailure;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Data.Monitoring.Rq1RuleDescription;
import java.util.EnumSet;
import util.EcvDateTime;
import util.EcvDateTime.DateTimeParseException;

/**
 * Represents a field of an RQ1 record which exists in this form in the RQ1
 * database and holds a date value.
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_DateTime extends Rq1DatabaseField_StringAccess<EcvDateTime> implements Rq1FieldI<EcvDateTime> {

    @EcvElementList("Rq1Data.Monitoring.Rq1RuleDescription")
    static final public Rq1RuleDescription dateTimeDescription = new Rq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.RQ1_DATA),
            "Valid format for time field.",
            "The content of a field in the RQ1 database, which should contain a time value, does not contain a valid time value.\n"
            + "\n"
            + "The warning 'Unexpected data read from RQ1 database.' is set on the element that contains the field. "
            + "The field name and the read value is added to the description of the warning.");

    private Rq1DataRule dateTimeRule = null;

    public Rq1DatabaseField_DateTime(Rq1RecordInterface parent, String dbFieldName) {
        this(parent, dbFieldName, dbFieldName);
    }

    public Rq1DatabaseField_DateTime(Rq1RecordInterface parent, String fieldName, String dbFieldName) {
        super(parent, fieldName, dbFieldName, new EcvDateTime());
    }

    @Override
    public boolean setOslcValue_Internal(String dbValue, Source source) {
        assert (dbValue != null);
        assert (source != null);

        removeMarker();
        EcvDateTime dbDate = new EcvDateTime();
        try {
            dbDate.setRq1Value(dbValue);
        } catch (DateTimeParseException ex) {
            StringBuilder s = new StringBuilder(40);
            s.append("Problem processing data from field ").append(getOslcPropertyName()).append(".");
            @SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
            Rq1ParseFieldException containerEx = new Rq1ParseFieldException(s.toString(), ex);
            getParentRecord().setMarker(new Rq1UnexpectedDataFailure(getRule(), getParentRecord(), this, containerEx));
        }
        return (setDataSourceValue(dbDate, source));
    }

    @Override
    protected String getOslcValue_Internal() {
        return (getDataModelValue().getRq1Value());
    }

    private Rq1DataRule getRule() {
        if (dateTimeRule == null) {
            dateTimeRule = new Rq1DataRule(dateTimeDescription);
        }
        return (dateTimeRule);
    }

    private void removeMarker() {
        if (dateTimeRule != null) {
            this.getParentRecord().removeMarkers(dateTimeRule);
        }
    }

    @Override
    protected void dbValueWasWrittenToDb() {
        removeMarker();
        super.dbValueWasWrittenToDb(); //To change body of generated methods, choose Tools | Templates.
    }
}
