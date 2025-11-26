/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord_ChangeSet;

/**
 *
 * @author GUG2WI
 */
public class DmAlmChangeSet extends DmAlmElement {

    final public DmAlmField_Text TITLE;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Text IDENTIFIER;

    public DmAlmChangeSet(DsAlmRecord_ChangeSet dsAlmRecord) {
        super("Change Set", dsAlmRecord);

        TITLE = addTextField("dcterms:title", "Title");
        IDENTIFIER = addTextField("dcterms:identifier", "Identifier");
        DESCRIPTION = addTextField("dcterms:description", "Description", true);
    }

    @Override
    public String getId() {
        return (IDENTIFIER.getValueAsText());
    }

    @Override
    public String getStatus() {
        return (null);
    }

    @Override
    public String getTitle() {
        return (TITLE.getValueAsText());
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + IDENTIFIER.getValueAsText() + " - " + TITLE.getValueAsText());
    }

}
