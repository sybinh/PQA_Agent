/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Xml;

import DataModel.DmElement;
import DataModel.DmFieldI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlParser;
import util.EcvXmlTextElement;

/**
 * Implements a table that parses its elements from a XML string. The XML
 * structure of each element is defined by the DmXmlFields in the DmElement.
 *
 * @param <T_ELEMENT> Type of the elements in the table.
 */
public abstract class DmXmlTable<T_ELEMENT extends DmElement> {

    private static final Logger LOGGER = Logger.getLogger(DmXmlTable.class.getCanonicalName());

    final private String nameOfLineElements;
    private List<T_ELEMENT> elementList = null;

    public DmXmlTable() {
        this.nameOfLineElements = null;
    }

    public DmXmlTable(String nameOfLineElements) {
        assert (nameOfLineElements != null);
        assert (nameOfLineElements.isEmpty() == false);

        this.nameOfLineElements = nameOfLineElements;
    }

    public final void loadFromFile(File file) throws FileNotFoundException {
        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            InputStreamReader reader = new InputStreamReader(fis);
            load(new EcvXmlParser(reader));
            reader.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            LOGGER.severe("FileNotFoundException for " + file.toString());
            throw (ex);
        } catch (IOException ex) {
            LOGGER.severe("Closing failed for " + file.toString());
        }
    }

    public void loadFromString(String xmlString) {
        load(new EcvXmlParser(xmlString));
    }

    public final List<T_ELEMENT> getElements() {
        return (elementList);
    }

    @SuppressWarnings("unchecked")
    private void load(EcvXmlParser parser) {

        EcvXmlContainerElement xmlListe;
        try {
            xmlListe = (EcvXmlContainerElement) parser.parse();
        } catch (EcvXmlParser.ParseException ex) {
            throw new Error("Load Failed " + ex.toString());
        }

        load(xmlListe);
    }

    /**
     * Load the table from the given content in form of a XML container. Each
     * XML (sub-)container in the content that matches the name given in the
     * constructor is processed as one line of the table.
     *
     * @param content XML container from which the table shall be extracted.
     */
    public final void load(EcvXmlContainerElement content) {
        assert (content != null);

        EcvXmlContainerElement listContainer = extractListContainer(content);

        List<EcvXmlContainerElement> containerList;
        if (nameOfLineElements != null) {
            containerList = listContainer.getContainerElementList(nameOfLineElements);
        } else {
            containerList = listContainer.getContainerElementList();
        }

        elementList = new ArrayList<>();
        for (EcvXmlContainerElement line : containerList) {
            try {
                T_ELEMENT newElement = createElement(line);
                for (DmFieldI field : newElement.getFields()) {
                    if (field instanceof DmXmlStringField) {
                        DmXmlStringField xmlField = (DmXmlStringField) field;
                        try {
                            EcvXmlElement xmlElement = line.getElement(((DmXmlField) field).getTagName());
                            if (xmlElement instanceof EcvXmlTextElement) {
                                xmlField.setValue(((EcvXmlTextElement) xmlElement).getText());
                            }
                        } catch (EcvXmlElement.NotfoundException ex) {
                            xmlField.setValue(null);
                        }
                    }
                }
                elementList.add(newElement);
            } catch (Exception e) {
                LOGGER.info(e.toString());
            }
        }

        initTree();
    }

    /**
     * Create a new element for the given line.
     *
     * @param line XML container with the parameters for the new element.
     * @return A new element to store the content of the line.
     */
    abstract protected T_ELEMENT createElement(EcvXmlContainerElement line);

    /**
     * Extracts the container that contains the list from the content container.
     * By default, the content container contains the list. Overwrite this
     * method, if a sub element of the content contains the list.
     *
     * @param content Content container.
     * @return The list container.
     */
    protected EcvXmlContainerElement extractListContainer(EcvXmlContainerElement content) {
        return (content);
    }

    protected void initTree() {
    }

}
