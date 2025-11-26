/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Xml;

import DataModel.Ecv.Fields.*;
import DataModel.DmElement;
import DataModel.DmElementI;
import DataModel.DmElementListFieldI;
import java.util.ArrayList;
import java.util.List;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 * Implements a field that reads it's elements from an XML Container
 *
 */
public abstract class DmXmlField_ReferenceList<T_ELEMENT extends DmElement> extends DmEcvField implements DmElementListFieldI<T_ELEMENT> {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DmXmlField_ReferenceList.class.getCanonicalName());

    private final EcvXmlContainerElement xmlContainer;
    private final String xmlElementName;
    private List<T_ELEMENT> elements = null;

    public DmXmlField_ReferenceList(DmElementI parent, EcvXmlContainerElement xmlContainer, String nameForUserInterface) {
        super(parent, nameForUserInterface);

        this.xmlContainer = xmlContainer;
        this.xmlElementName = null;
    }

    public DmXmlField_ReferenceList(DmElementI parent, EcvXmlContainerElement xmlContainer, String xmlElementName, String nameForUserInterface) {
        super(parent, nameForUserInterface);
        assert (xmlElementName != null);
        assert (xmlElementName.isEmpty() == false);

        this.xmlContainer = xmlContainer;
        this.xmlElementName = xmlElementName;
    }

    @Override
    public final List<T_ELEMENT> getElementList() {
        if (elements == null) {
            elements = new ArrayList<>();
            EcvXmlContainerElement sourceContainer = getSourceContainer();
            if (sourceContainer != null) {
                DmXmlTable<T_ELEMENT> xmlTable = new DmXmlTable<T_ELEMENT>() {
                    @Override
                    protected T_ELEMENT createElement(EcvXmlContainerElement elem) {
                        return internalCreateElement(elem);
                    }
                };
                xmlTable.load(sourceContainer);
                for (T_ELEMENT element : xmlTable.getElements()) {
                    elements.add(element);
                }
            }
        }
        return elements;
    }

    private EcvXmlContainerElement getSourceContainer() {
        if (xmlContainer != null) {
            if ((xmlElementName != null) && (xmlContainer.containsElement(xmlElementName) == true)) {
                try {
                    EcvXmlContainerElement sourceContainer = new EcvXmlContainerElement(xmlElementName);
                    sourceContainer.addElement(xmlContainer.getElement(xmlElementName));
                    return (sourceContainer);
                } catch (EcvXmlElement.NotfoundException ex) {
                    LOGGER.warning(getNameForUserInterface() + "/" + xmlElementName + ": There is no Komponente Tag in the given data");
                }
            } else {
                return (xmlContainer);
            }
        }
        return (null);
    }

    @Override
    public final void addElement(T_ELEMENT e) {
        elements.add(e);
    }

    @Override
    public final void reload() {

    }

    private T_ELEMENT internalCreateElement(EcvXmlContainerElement elementData) {
        return (createElement(elementData));
    }

    protected abstract T_ELEMENT createElement(EcvXmlContainerElement elementData);

}
