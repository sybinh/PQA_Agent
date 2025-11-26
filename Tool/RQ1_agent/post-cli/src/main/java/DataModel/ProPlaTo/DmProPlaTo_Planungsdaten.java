/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ProPlaTo;

import DataModel.Xml.DmXmlElementListField;
import DataModel.DmConstantField_Date;
import DataModel.DmConstantField_Text;
import UiSupport.UiTreeViewRootElementI;
import java.nio.file.Path;
import java.nio.file.Paths;
import util.EcvXmlContainerElement;

/**
 *
 * @author GUG2WI
 */
public class DmProPlaTo_Planungsdaten extends DmProPlaTo_Element implements UiTreeViewRootElementI {

    final public DmConstantField_Text FILE_PATH;
    final public DmConstantField_Text FILE_NAME;
    final public DmConstantField_Text CREATED_NAME;
    final public DmConstantField_Date CREATED_DATE;
    final public DmConstantField_Text LIEFERANT;

    final public DmXmlElementListField<DmProPlaTo_PSTSchiene> PST_SCHIENE;

    public DmProPlaTo_Planungsdaten(EcvXmlContainerElement planungsdaten, String fullPath) {
        super("ProPlaTo-Planungsdaten");
        assert (planungsdaten != null);
        assert (fullPath != null);

        addField(FILE_PATH = new DmConstantField_Text("Path", fullPath));
        addField(FILE_NAME = new DmConstantField_Text("Filename", getFileName(fullPath)));
        addField(CREATED_NAME = createTextField(planungsdaten, "Created", "Name", "Version"));
        addField(CREATED_DATE = createDateField(planungsdaten, "Created", "Datum", "Datum"));
        addField(LIEFERANT = createTextField(planungsdaten, "Lieferant", "Name", "Lieferant"));
        addField(PST_SCHIENE = createXmlListField(planungsdaten, "Lieferant", "PSTSchiene", "PSTSchiene", (EcvXmlContainerElement xmlContainer) -> {
            return (new DmProPlaTo_PSTSchiene(xmlContainer));
        }));
    }

    private String getFileName(String fullPath) {
        Path path = Paths.get(fullPath);
        return (path.getFileName().toString());
    }

    @Override
    public String getViewTitle() {
        return (getElementType() + ": " + getTitle());
    }

    @Override
    public String getTitle() {
        return (FILE_NAME.getValueAsText());
    }

    @Override
    public String getId() {
        return (CREATED_NAME.getValue() + "-" + CREATED_DATE.getValue().getXmlValue());
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + CREATED_NAME.getValueAsText() + " " + CREATED_DATE.getValue().getXmlValue());
    }

}
