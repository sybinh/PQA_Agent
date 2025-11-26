/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Monitoring.Rq1DataRule;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1RecordInterface;

/**
 * Represents a field of an RQ1 record which exists in this form in the RQ1
 * database and holds a text value of any content.
 *
 * @author GUG2WI
 */
public class Rq1DatabaseField_Text extends Rq1DatabaseField_StringAccess<String> implements Rq1FieldI_Text {

    private Rq1DataRule textToLongRule = null;

    public Rq1DatabaseField_Text(Rq1RecordInterface parent, Rq1AttributeName attribute) {
        super(parent, attribute.getName(), "");
    }

    public Rq1DatabaseField_Text(Rq1RecordInterface parent, String dbFieldName) {
        super(parent, dbFieldName, "");
    }

    @Override
    protected String getOslcValue_Internal() {
        return (getDataSourceValue());
    }

    @Override
    public boolean setOslcValue_Internal(String dbValue, Source source) {
        removeMarker();

        if (dbValue.length() > MAX_NUMBER_OF_CHARACTERS_IN_TEXT_FIELD) {
            if (textToLongRule == null) {
                textToLongRule = new Rq1DataRule(textToLongRuleDescription);
            }
            this.getParentRecord().setMarker(new TextToLongFailure(textToLongRule, getParentRecord(), this, dbValue.length()));
        }

        return (setDataSourceValue(dbValue, source));
    }

    private void removeMarker() {
        if (textToLongRule != null) {
            this.getParentRecord().removeMarkers(textToLongRule);
        }
    }

    @Override
    public void dbValueWasWrittenToDb() {
        removeMarker();
        super.dbValueWasWrittenToDb();
    }
}
