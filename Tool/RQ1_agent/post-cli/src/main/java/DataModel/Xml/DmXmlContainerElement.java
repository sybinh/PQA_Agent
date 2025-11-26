/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Xml;

import DataModel.DmConstantField_Date;
import DataModel.DmConstantField_Text;
import DataModel.DmElement;
import DataModel.DmValueField_Enumeration;
import DataModel.DmValueField_Text;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvDate;
import util.EcvEnumeration;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public abstract class DmXmlContainerElement extends DmElement {

    protected interface XmlElementBuilderI<T extends DmXmlContainerElement> {

        T build(EcvXmlContainerElement xmlContainer);

    }

    protected interface XmlMappedElementBuilderI<T extends DmXmlMappedContainerElementI> {

        T build(EcvXmlContainerElement xmlContainer);

    }

    private static final Logger LOGGER = Logger.getLogger(DmXmlContainerElement.class.getCanonicalName());

    public DmXmlContainerElement(String elementType) {
        super(elementType);
    }

    final protected EcvXmlContainerElement getContainer(EcvXmlContainerElement xmlContainer, String elementName) {
        assert (xmlContainer != null);
        assert (elementName != null);
        assert (elementName.isEmpty() == false);

        try {
            return (xmlContainer.getContainerElement(elementName));
        } catch (EcvXmlElement.NotfoundException ex) {
            LOGGER.log(Level.INFO, xmlContainer.getName() + "->" + elementName + " not found.", ex);
            return (null);
        }
    }

    final protected DmConstantField_Text createTextField(EcvXmlContainerElement xmlContainer, String elementName, String nameForUserInterface) {
        String text = getText(xmlContainer, elementName);
        return (DmXmlContainerElement.this.createTextField(nameForUserInterface, text));
    }

    final protected DmConstantField_Text createTextField(EcvXmlContainerElement xmlContainer, String elementName, String subElementName, String nameForUserInterface) {
        String text = getText(xmlContainer, elementName, subElementName);
        return (DmXmlContainerElement.this.createTextField(nameForUserInterface, text));
    }

    final protected DmValueField_Text createValueTextField(EcvXmlContainerElement xmlContainer, String elementName, String subElementName, String nameForUserInterface) {
        String text = getText(xmlContainer, elementName, subElementName);
        return (DmXmlContainerElement.this.createValueTextField(nameForUserInterface, text));
    }

    final protected DmValueField_Enumeration createValueEnumerationField(String nameForUserInterface, EcvEnumeration value, EcvEnumeration[] validValues) {
        return (new DmValueField_Enumeration(nameForUserInterface, value, validValues));
    }
    
    final protected String getTextasString(EcvXmlContainerElement xmlContainer, String elementName, String subElementName){
        return getText(xmlContainer, elementName, subElementName);
    }

    final protected DmConstantField_Date createDateField(EcvXmlContainerElement xmlContainer, String elementName, String subElementName, String nameForUserInterface) {
        String text = getText(xmlContainer, elementName, subElementName);
        return (DmXmlContainerElement.this.createDateField(nameForUserInterface, text));
    }

    final protected DmConstantField_Date createDateField(EcvXmlContainerElement xmlContainer, String elementName, String nameForUserInterface) {
        String text = getText(xmlContainer, elementName);
        return (DmXmlContainerElement.this.createDateField(nameForUserInterface, text));
    }

    private DmConstantField_Text createTextField(String nameForUserInterface, String text) {
        if (text == null) {
            return (new DmConstantField_Text(nameForUserInterface, ""));
        }
        return (new DmConstantField_Text(nameForUserInterface, text));
    }

    private DmValueField_Text createValueTextField(String nameForUserInterface, String text) {
        if (text == null) {
            return (new DmValueField_Text(nameForUserInterface, ""));
        }
        return (new DmValueField_Text(nameForUserInterface, text));
    }

    private DmConstantField_Date createDateField(String nameForUserInterface, String text) {
        if (text == null) {
            return (new DmConstantField_Date(nameForUserInterface, EcvDate.getEmpty()));
        }

        try {
            EcvDate date = EcvDate.parseXmlValue(text);
            return (new DmConstantField_Date(nameForUserInterface, date));
        } catch (EcvDate.DateParseException ex) {
            LOGGER.log(Level.INFO, "'" + text + "' cannot be parsed as date.", ex);
            return (new DmConstantField_Date(nameForUserInterface, EcvDate.getEmpty()));
        }

    }

    final protected <T extends DmXmlContainerElement> DmXmlElementListField<T> createXmlListField(EcvXmlContainerElement xmlContainer, String elementName, String subElementName, String nameForUserInterface, XmlElementBuilderI<T> builder) {
        assert (xmlContainer != null);
        assert (elementName != null);
        assert (elementName.isEmpty() == false);
        assert (subElementName != null);
        assert (subElementName.isEmpty() == false);
        assert (builder != null);

        List<T> content = new ArrayList<>();
        EcvXmlContainerElement xmlSubContainer;
        try {
            xmlSubContainer = xmlContainer.getContainerElement(elementName);
        } catch (EcvXmlElement.NotfoundException ex) {
            LOGGER.log(Level.INFO, xmlContainer.getName() + "->" + elementName + " not found.", ex);
            return (new DmXmlElementListField<>(nameForUserInterface, content));
        }

        for (EcvXmlContainerElement xmlSubSubContainer : xmlSubContainer.getContainerElementList(subElementName)) {
            T dmXmlContainerElement = builder.build(xmlSubSubContainer);
            if (dmXmlContainerElement != null) {
                content.add(dmXmlContainerElement);
            }
        }
        return (new DmXmlElementListField<>(nameForUserInterface, content));
    }

    final protected <T extends DmXmlMappedContainerElementI> DmXmlMappedElementListField<T> createXmlMappedListField(EcvXmlContainerElement xmlContainer, String elementName, String nameForUserInterface, XmlMappedElementBuilderI<T> builder) {
        assert (xmlContainer != null);
        assert (elementName != null);
        assert (elementName.isEmpty() == false);
        assert (builder != null);

        List<T> content = new ArrayList<>();
        for (EcvXmlContainerElement xmlSubSubContainer : xmlContainer.getContainerElementList(elementName)) {
            T dmXmlContainerElement = builder.build(xmlSubSubContainer);
            if (dmXmlContainerElement != null) {
                content.add(dmXmlContainerElement);
            }
        }
        return (new DmXmlMappedElementListField<>(nameForUserInterface, content));
    }

    private String getText(EcvXmlContainerElement xmlContainer, String elementName) {
        assert (xmlContainer != null);
        assert (elementName != null);
        assert (elementName.isEmpty() == false);

        try {
            return (xmlContainer.getText(elementName));
        } catch (EcvXmlElement.NotfoundException ex) {
            LOGGER.log(Level.INFO, xmlContainer.getName() + "->" + elementName + " not found.", ex);
            return (null);
        }
    }

    private String getText(EcvXmlContainerElement xmlContainer, String elementName, String subElementName) {
        assert (xmlContainer != null);
        assert (elementName != null);
        assert (elementName.isEmpty() == false);
        assert (subElementName != null);
        assert (subElementName.isEmpty() == false);

        try {
            return (xmlContainer.getContainerElement(elementName).getText(subElementName));
        } catch (EcvXmlElement.NotfoundException ex) {
            LOGGER.log(Level.INFO, xmlContainer.getName() + "->" + elementName + "->" + subElementName + " not found.", ex);
            return (null);
        }
    }

}
