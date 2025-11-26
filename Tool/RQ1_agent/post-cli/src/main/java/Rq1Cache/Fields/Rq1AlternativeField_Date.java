/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import DataStore.DsFieldI_Date;
import DataStore.DsField_AlternativeField_Date;
import DataStore.DsRecordI;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Date;
import Rq1Cache.Records.Rq1RecordInterface;

/**
 *
 * @author gug2wi
 */
public class Rq1AlternativeField_Date extends DsField_AlternativeField_Date<Rq1RecordInterface> implements Rq1FieldI_Date {
    
    public Rq1AlternativeField_Date(Rq1RecordInterface parent, String fieldName, DsFieldI_Date<? extends DsRecordI> primaryField, DsFieldI_Date<? extends DsRecordI> alternativField) {
        super(parent, fieldName, primaryField, alternativField);
    }
    
}
