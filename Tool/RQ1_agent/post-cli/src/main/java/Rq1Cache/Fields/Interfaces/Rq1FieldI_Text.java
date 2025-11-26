/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields.Interfaces;

import DataStore.DsFieldI;
import DataStore.DsFieldI_Text;
import DataStore.DsRecordI;
import DataStore.Exceptions.UnexpectedDataFailure;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.RuleI;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Data.Monitoring.Rq1RuleDescription;
import java.util.EnumSet;

/**
 *
 * @author gug2wi
 */
public interface Rq1FieldI_Text extends DsFieldI_Text<Rq1RecordInterface>, Rq1FieldI<String> {

    @EcvElementList("Rq1Data.Monitoring.Rq1RuleDescription")
    static final Rq1RuleDescription textToLongRuleDescription = new Rq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.RQ1_DATA),
            "Length of text field.",
            "The content of a field in the RQ1 database, which contains a text value, is too long.\n"
            + "\n"
            + "The failure 'Unexpected data in field ... read from database.' is set on the element that contains the field.\n"
            + "The field name and a problem text is added to the description of the failure.");

    static public class TextToLongFailure extends UnexpectedDataFailure {

        public TextToLongFailure(RuleI rule, DsRecordI dsRecord, DsFieldI<? extends DsRecordI, ?> dsField, int textLength) {
            super(rule, dsRecord, dsField, "Content of field too long. Length = " + textLength + ". Field value might not be shown correctly on GUI.");
        }

    }

}
