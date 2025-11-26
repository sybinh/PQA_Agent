/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ProPlaTo;

import DataModel.Xml.DmXmlMappedElementListField;
import DataModel.Xml.DmXmlMappedContainerElementI;
import DataModel.DmConstantField_Text;
import DataModel.DmElementField_ReadOnlyI;
import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import util.EcvXmlContainerElement;

/**
 *
 * @author GUG2WI
 */
public class DmProPlaTo_Programmstand extends DmProPlaTo_Element implements DmXmlMappedContainerElementI {

    final public DmConstantField_Text ID;
    final public DmConstantField_Text NAME;
    final public DmElementField_ReadOnlyI<DmRq1ElementInterface> PST;
    final public DmXmlMappedElementListField<DmProPlaTo_Aenderung> AENDERUNGEN;

    DmProPlaTo_Programmstand(EcvXmlContainerElement programmStand) {
        super("Programmstand");
        assert (programmStand != null);

        addField(ID = createTextField(programmStand, "ID", "ID"));
        addField(NAME = createTextField(programmStand, "Name", "Name"));
        addField(PST = new DmProPlaTo_Rq1ElementField(ID, "PVER/PVAR"));
        addField(AENDERUNGEN = createXmlMappedListField(programmStand, "Aenderung", "Änderungen", (EcvXmlContainerElement xmlContainer) -> {
            return (new DmProPlaTo_Aenderung(xmlContainer));
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
    public DmElementI getMappingElement() {
        return (mappingRq1Element(ID));
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + ID.getValueAsText() + " " + NAME.getValueAsText());
    }

}
