/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.DmToDsField_TextAsEnumeration;
import DataStore.ALM.DsAlmRecordI;
import DataStore.DsFieldI;
import DataStore.DsRecordI;
import util.EcvEnumeration;

/**
 *
 * @author CNI83WI
 */
public class DmAlmField_EnumerationFromText extends DmToDsField_TextAsEnumeration<DsAlmRecordI> {
    
    public DmAlmField_EnumerationFromText(DsFieldI<? extends DsRecordI<?>, String> dsField, String nameForUserInterface, EcvEnumeration[] validValues) {
        super(dsField, nameForUserInterface, validValues);
    }
    
}
