/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.DmConstantField_Text;
import DataModel.DmElement;
import DataModel.DmValueFieldI_Text;

/**
 *
 * @author GUG2WI
 */
public class DmAlmToAlmReference extends DmElement {

    public static final String ELEMENT_TYPE = "ReferenceWithinAlm";

    public final DmValueFieldI_Text REFERENCE_TYPE;

    public DmAlmToAlmReference(String referenceType) {
        super(ELEMENT_TYPE);
        assert (referenceType != null);
        assert (referenceType.isEmpty() == false);

        addField(REFERENCE_TYPE = new DmConstantField_Text("Reference Type", referenceType));
    }

    @Override
    public void reload() {
    }

    @Override
    public String getTitle() {
        return (REFERENCE_TYPE.getValueAsText());
    }

    @Override
    public String getId() {
        return ("");
    }

    @Override
    public String toString() {
        return (REFERENCE_TYPE.getValueAsText());
    }

}
