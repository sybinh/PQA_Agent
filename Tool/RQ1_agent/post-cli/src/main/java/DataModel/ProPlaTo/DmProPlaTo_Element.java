/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ProPlaTo;

import DataModel.Rq1.DmRq1ElementReference;
import DataModel.Xml.DmXmlContainerElement;
import DataModel.DmConstantField_Text;
import DataModel.DmElementI;

/**
 *
 * @author GUG2WI
 */
public abstract class DmProPlaTo_Element extends DmXmlContainerElement {

    public DmProPlaTo_Element(String elementType) {
        super(elementType);
    }

    protected DmElementI mappingRq1Element(DmConstantField_Text idField) {
        assert (idField != null);
        String rq1Id = idField.getValue();
        return (new DmRq1ElementReference(rq1Id));
    }

    @Override
    final public void reload() {
        // Not supported.
    }

}
