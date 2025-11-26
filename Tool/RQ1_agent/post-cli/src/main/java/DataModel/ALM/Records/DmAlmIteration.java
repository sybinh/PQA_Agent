/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Boolean;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord;

/**
 *
 * @author CNI83WI
 */
public class DmAlmIteration extends DmAlmElement {
    public static final String ELEMENT_TYPE = "Iteration";

    final public DmAlmField_Text TITLE;
    final public DmAlmField_Text IDENTIFIER;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Boolean ARCHIVED;

    public DmAlmIteration(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);
        
        TITLE = addTextField("dcterms:title", "Title");
        ignoreField("rtc_cm:parent");
        ignoreField("rtc_cm:hasDeliverable");
        ignoreField("rtc_cm:projectArea");
        IDENTIFIER = addTextField("dcterms:identifier", "Identifier");
        ignoreField("rtc_cm:timeline");
        ignoreField("rtc_cm:startDate");
        ignoreField("rtc_cm:endDate");
        ARCHIVED = addBooleanField("rtc_cm:archived", "Archived");
        DESCRIPTION = addTextField("dcterms:description", "Description");
    }

    @Override
    public String getStatus() {
        return (ARCHIVED.getValueAsText());
    }

    @Override
    public String getTitle() {
        return (TITLE.getValue());
    }

    @Override
    public String getId() {
        return (IDENTIFIER.getValue());
    }

}
