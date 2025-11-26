/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Xml;

import DataModel.DmElementI;
import DataModel.DmField;
import DataModel.DmValueFieldI;

/**
 * Used to define a field that can be read from XML string via the DmXmlTable.
 *
 * @author gug2wi
 */
public abstract class DmXmlField<T> extends DmField implements DmValueFieldI<T> {

    final private String tagName;
    private T value;

    public DmXmlField(DmElementI parent, String tagName, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (tagName != null);
        assert (tagName.isEmpty() == false);

        this.tagName = tagName;
    }

    final public String getTagName() {
        return (tagName);
    }

    @Override
    final public boolean isReadOnly() {
        return (true);
    }

    @Override
    public T getValue() {
        return (value);
    }

    @Override
    public void setValue(T v) {
        value = v;
    }

}
