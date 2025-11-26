/*
 *
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 *
 */
package DataModel.Acquisition;

import DataModel.Xml.DmXmlContainerElement;

/**
 *
 * @author cil83wi
 */
public abstract class DmAcquisition_Element extends DmXmlContainerElement {

    public DmAcquisition_Element(String elementType) {
        super(elementType);
    }

    @Override
    final public void reload() {
        // Not supported.
    }

    @Override
    public String getTitle() {
        return "null";
    }

    @Override
    public String getId() {
        return "null";
    }
}
