/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
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
 * @author CNI83WI
 */
public class DmAlmWorkitem_AdoptionItem extends DmAlmWorkitem {

    public static final String ELEMENT_TYPE = "Adoption Item";

    final public DmAlmField_Text STATUS;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Enumeration IMPACT;

    public DmAlmWorkitem_AdoptionItem(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);

        STATUS = addTextField("oslc_cm:status", "Status");
        DESCRIPTION = addTextField("dcterms:description", "Description");
        IMPACT = addEnumerationField("rtc_ext:com.ibm.team.rtc.attribute.impact", "Impact");

        checkForUnusedFields();
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }
}
