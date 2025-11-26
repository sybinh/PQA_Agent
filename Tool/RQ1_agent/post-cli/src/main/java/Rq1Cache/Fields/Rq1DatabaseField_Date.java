/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Date;
import Rq1Cache.Monitoring.Rq1DataRule;
import Rq1Cache.Monitoring.Rq1ParseFieldException;
import Rq1Cache.Monitoring.Rq1UnexpectedDataFailure;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Data.Monitoring.Rq1RuleDescription;
import java.util.EnumSet;
import util.EcvDate;
import util.EcvDate.DateParseException;

/**
 * Represents a field of an RQ1 record which exists in this form in the RQ1
 * database and holds a date value.
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_Date extends Rq1DatabaseField_StringAccess<EcvDate> implements Rq1FieldI_Date {

    public enum DateFormat {
        DEFAULT,
        YYYYMMDD_HHMMSS;
    }

    @EcvElementList("Rq1Data.Monitoring.Rq1RuleDescription")
    static final public Rq1RuleDescription dateDescription = new Rq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.RQ1_DATA),
            "Valid format for date field.",
            "The content of a field in the RQ1 database, which should contain a date value, does not contain a valid date value.\n"
            + "\n"
            + "The warning 'Unexpected data read from RQ1 database.' is set on the element that contains the field. "
            + "The field name and the read value is added to the description of the warning.");

    private Rq1DataRule dateRule = null;
    private DateFormat dateFormat = DateFormat.DEFAULT;

    public Rq1DatabaseField_Date(Rq1RecordInterface parent, String dbFieldName) {
        this(parent, dbFieldName, dbFieldName);
    }

    public Rq1DatabaseField_Date(Rq1RecordInterface parent, Rq1AttributeName attribute) {
        this(parent, attribute.getName(), attribute.getName());
    }

    public Rq1DatabaseField_Date(Rq1RecordInterface parent, String fieldName, String dbFieldName) {
        super(parent, fieldName, dbFieldName, EcvDate.getEmpty());
    }

    public void setDateFormat(DateFormat dateFormat) {
        assert (dateFormat != null);
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean setOslcValue_Internal(String dbValue, Source source) {
        assert (dbValue != null);
        assert (source != null);

        removeDateRule();
        EcvDate dbDate = EcvDate.getEmpty();
        if (source == Source.DATABASE) {
            switch (dateFormat) {
                case DEFAULT:
                    try {
                        dbDate = EcvDate.parseOslcValue(dbValue);
                    } catch (DateParseException ex) {
                        StringBuilder s = new StringBuilder(40);
                        s.append("Problem processing data from field ").append(getOslcPropertyName()).append(".");
                        @SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
                        Rq1ParseFieldException containerEx = new Rq1ParseFieldException(s.toString(), ex);
                        getParentRecord().setMarker(new Rq1UnexpectedDataFailure(getDateRule(), getParentRecord(), this, containerEx));
                    }
                    break;
                case YYYYMMDD_HHMMSS:
                    try {
                        dbDate = EcvDate.parseSpecialOslcValue(dbValue);
                    } catch (DateParseException ex) {
                        StringBuilder s = new StringBuilder(40);
                        s.append("Problem processing data from field ").append(getOslcPropertyName()).append(".");
                        @SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
                        Rq1ParseFieldException containerEx = new Rq1ParseFieldException(s.toString(), ex);
                        getParentRecord().setMarker(new Rq1UnexpectedDataFailure(getDateRule(), getParentRecord(), this, containerEx));
                    }
                    break;
                default:
                    throw (new Error("Unexpected date format:" + dateFormat.name()));
            }

        } else if (source == Source.TEMPLATE) {
            try {
                dbDate = EcvDate.parseRq1TemplateValue(dbValue);
            } catch (DateParseException ex) {
                StringBuilder s = new StringBuilder(40);
                s.append("Problem processing data from field ").append(getOslcPropertyName()).append(".");
                @SuppressWarnings({"ThrowableInstanceNotThrown", "ThrowableInstanceNeverThrown"})
                Rq1ParseFieldException containerEx = new Rq1ParseFieldException(s.toString(), ex);
                getParentRecord().setMarker(new Rq1UnexpectedDataFailure(getDateRule(), getParentRecord(), this, containerEx));
            }
        } else {
            throw (new Error("Unexpected source " + source.getClass().getCanonicalName()));
        }
        return (setDataSourceValue(dbDate, source));
    }

    @Override
    protected String getOslcValue_Internal() {
        return (getDataModelValue().getOslcValue());
    }

    @Override
    public boolean equalDbValues(String value1, String value2) {
        if ((value1 != null) && (value2 != null)) {
            if (value1.equals(value2) == true) {
                return (true);
            } else if ((value1.length() > 10) && (value2.length() > 10)) {
                return (value1.substring(0, 10).equals(value2.substring(0, 10)));
            } else {
                return (false);
            }

        } else {
            return (value1 == value2);
        }
    }

    private Rq1DataRule getDateRule() {
        if (dateRule == null) {
            dateRule = new Rq1DataRule(dateDescription);
        }
        return (dateRule);
    }

    private void removeDateRule() {
        if (dateRule != null) {
            this.getParentRecord().removeMarkers(dateRule);
        }
    }

    @Override
    protected void dbValueWasWrittenToDb() {
        removeDateRule();
        super.dbValueWasWrittenToDb(); //To change body of generated methods, choose Tools | Templates.
    }

}
