/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Enumeration;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord;

/**
 *
 * @author GUG2WI
 */
public class DmAlmWorkitem_Epic extends DmAlmWorkitem {

    public static final String ELEMENT_TYPE = "Epic";

    final public DmAlmField_Text STATUS;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Enumeration SCOPE;

    public DmAlmWorkitem_Epic(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);

        STATUS = addTextField("oslc_cm:status", "Status");
        DESCRIPTION = addTextField("dcterms:description", "Description");
        SCOPE = addEnumerationField("rtc_ext:com.bosch.rtc.configuration.workitemtype.customattribute.scope", "Scope");

        checkForUnusedFields();
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }

}
