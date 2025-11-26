/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Xml;

import DataModel.DmElementListField_ReadOnlyFromSource;
import java.util.Collection;

/**
 *
 * @author GUG2WI
 */
public class DmXmlElementListField<T extends DmXmlContainerElement> extends DmElementListField_ReadOnlyFromSource<T> {

    final private Collection<T> value;

    public DmXmlElementListField(String nameForUserInterface, Collection<T> value) {
        super(nameForUserInterface);
        assert (value != null);
        this.value = value;
    }

    @Override
    protected Collection<T> loadElementList() {
        return (value);
    }

}
