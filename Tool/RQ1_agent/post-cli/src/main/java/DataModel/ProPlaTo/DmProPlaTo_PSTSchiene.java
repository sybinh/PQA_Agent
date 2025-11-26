/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ProPlaTo;

import DataModel.Xml.DmXmlMappedElementListField;
import DataModel.DmConstantField_Text;
import util.EcvXmlContainerElement;

/**
 *
 * @author GUG2WI
 */
public class DmProPlaTo_PSTSchiene extends DmProPlaTo_Element {

    final public DmConstantField_Text ID;
    final public DmConstantField_Text NAME;
    final public DmXmlMappedElementListField<DmProPlaTo_Programmstand> PROGRAMMSTAND;

    public DmProPlaTo_PSTSchiene(EcvXmlContainerElement pstSchiene) {
        super("PST-Schiene");
        assert (pstSchiene != null);

        addField(ID = createTextField(pstSchiene, "ID", "ID"));
        addField(NAME = createTextField(pstSchiene, "Name", "Name"));
        addField(PROGRAMMSTAND = createXmlMappedListField(pstSchiene, "Programmstand", "Programmstände", (EcvXmlContainerElement xmlContainer) -> {
            return (new DmProPlaTo_Programmstand(xmlContainer));
        }));
    }

    @Override
    public String getTitle() {
        return (NAME.getValueAsText());
    }

    @Override
    public String getId() {
        return (ID.getValueAsText());
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + ID.getValueAsText() + " " + NAME.getValueAsText());
    }

}
