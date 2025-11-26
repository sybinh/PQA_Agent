/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Monitoring.Rq1ParseFieldException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlElement.NotfoundException;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;
import util.EcvTableColumnI;

/**
 * <p>
 * Supports the encoding and decoding of a table to/from an XML element.
 * </p>
 *
 * @author GUG2WI
 */
public class Rq1XmlTable extends EcvTableDescription {

    final private static Logger LOGGER = Logger.getLogger(Rq1XmlTable.class.getCanonicalName());

    protected enum ColumnEncodingMethod {

        ATTRIBUTE, // The value of the column is stored in an attribute of the row element.
        CONTENT, // The value of the column is the value of the row element. Only one content column is allowed.
        TAG_NAME, // The value of the column is the name of the row element. Only one tag name column is allowed.
        /*
        Note: For COMMENT, only writing is implemented yet.
         */
        COMMENT, // The value of the column is the comment in the element. Only one comment is allowed in the element.
        ELEMENT_LIST, // The value of the column is a list of strings stored in sub elements of the row element.
        ELEMENT_CONTENT // The value of the column is the content of an sub Element in the row element
    }

    protected interface ElementListSupported {

        Object loadFromElementList(Iterable<EcvXmlElement> elements);

        List<String> provideElementForDb(Object o);
    }

    private static enum RowEncodingMethod {

        EMPTY_ELEMENT, // An empty element (IpeEmptyElement) is used.
        TEXT_ELEMENT, // A text element (IpeTextElement) is used.
        CONTAINER_ELEMENT // A container element (IpeContainerElement) is used.
    }

    private RowEncodingMethod rowEncodingMethod = RowEncodingMethod.EMPTY_ELEMENT;
    private Rq1XmlTableColumn contentColumn = null;
    private Rq1XmlTableColumn_String tagNameColumn = null;
    private Rq1XmlTableColumn commentColumn = null;

    protected Rq1XmlTable() {
    }

    @Override
    protected void addIpeColumn(EcvTableColumnI column) {
        throw (new Error("Call not allowed"));
    }

    final protected void addXmlColumn(Rq1XmlTableColumn newColumn) {
        super.addIpeColumn(newColumn);
        switch (((Rq1XmlTableColumn) newColumn).getEncodingMethod()) {
            case CONTENT:
                assert (contentColumn == null) : "Content column already set to " + contentColumn.getSourceName();
                assert (rowEncodingMethod == RowEncodingMethod.EMPTY_ELEMENT) : rowEncodingMethod.toString();
                rowEncodingMethod = RowEncodingMethod.TEXT_ELEMENT;
                contentColumn = newColumn;
                break;

            case TAG_NAME:
                assert (newColumn instanceof Rq1XmlTableColumn_String) : "Tag name column has to be of type Rq1XmlTableColumn_String";
                assert (tagNameColumn == null) : "Tag name column already set to " + tagNameColumn.getSourceName();
                tagNameColumn = (Rq1XmlTableColumn_String) newColumn;
                break;

            case COMMENT:
                assert (commentColumn == null) : "Comment column already set to " + commentColumn.getSourceName();
                commentColumn = newColumn;
                break;

            case ELEMENT_LIST:
            case ELEMENT_CONTENT:
                assert (rowEncodingMethod != RowEncodingMethod.TEXT_ELEMENT) : rowEncodingMethod.toString();
                rowEncodingMethod = RowEncodingMethod.CONTAINER_ELEMENT;
                break;
        }
    }

    /**
     * Extracts the data for a row from the given rowElement.
     *
     * The extraction is done according to the field definitions added to the
     * class.
     *
     * Overwrite this method to implement special encodings. E.g. to support old
     * formats that shall be converted to the default format.
     *
     * @param data The data to which the extracted row will be added.
     * @param rowElement The XML structure that contains the information for the
     * row.
     * @throws Rq1ParseFieldException If the parsing fails for format reasons.
     */
    public void loadRowFromDb(EcvTableData data, EcvXmlElement rowElement) throws Rq1ParseFieldException {
        assert (data != null);
        assert (rowElement != null);

        //
        // Create new, empty row
        //
        EcvTableRow newRow = data.createRow();

        for (EcvTableColumnI ipeColumn : getColumns()) {
            assert (ipeColumn instanceof Rq1XmlTableColumn) : ipeColumn.getUiName() + " - " + ipeColumn.getClass().getCanonicalName();
            Rq1XmlTableColumn xmlColumn = (Rq1XmlTableColumn) ipeColumn;

            switch (xmlColumn.getEncodingMethod()) {
                case ATTRIBUTE:
                    loadColumnFromAttribute(xmlColumn, rowElement, newRow);
                    break;

                case CONTENT:
                    loadColumnFromContent(xmlColumn, rowElement, newRow);
                    break;

                case TAG_NAME:
                    loadColumnFromTagName(xmlColumn, rowElement, newRow);
                    break;

                case ELEMENT_LIST:
                    loadColumnFromElementList(xmlColumn, rowElement, newRow);
                    break;

                case ELEMENT_CONTENT:
                    loadColumnFromElementContent(xmlColumn, rowElement, newRow);
                    break;

                default:
                    throw (new Error("Unexpected encoding method: " + xmlColumn.getEncodingMethod().toString()));
            }
        }

        data.addRow(newRow);
    }

    private void loadColumnFromAttribute(Rq1XmlTableColumn<?> xmlColumn, EcvXmlElement rowElement, EcvTableRow newRow) throws Rq1ParseFieldException {
        assert (xmlColumn != null);
        assert (rowElement != null);
        assert (newRow != null);

        //
        // The value of the column is stored in an attribute of the row element.
        //
        String stringValue = rowElement.getAttribute(xmlColumn.getSourceName());
        if (stringValue == null) {
            for (String alternativeSourceName : xmlColumn.getAlternativeSourceNames()) {
                stringValue = rowElement.getAttribute(alternativeSourceName);
                if (stringValue != null) {
                    break;
                }
            }
        }
        Object objectValue = xmlColumn.loadValueFromDatabase(stringValue);
        if (objectValue == null) {
            if (xmlColumn.isOptional() == true) {
                if (xmlColumn instanceof Rq1XmlTableColumn_CheckBox) {
                    ((Rq1XmlTableColumn_CheckBox) xmlColumn).setValue(newRow, false);
                } else {
                    newRow.setValueAt(xmlColumn.getColumnIndexData(), null);
                }
            } else {
                //
                // Attribute not set but mandatory field -> error
                //
                StringBuilder b = new StringBuilder(100);
                b.append("Error when parsing field ").append(xmlColumn.getUiName()).append(" in element ").append(rowElement.getName()).append(":\n");
                b.append("Attribute ").append(xmlColumn.getSourceName()).append(" missing.");
                throw (new Rq1ParseFieldException(b.toString()));
            }
        }
        if (xmlColumn instanceof Rq1XmlTableColumn_CheckBox) {
            boolean booleanValue = ((Rq1XmlTableColumn_CheckBox) xmlColumn).convertToBoolean(stringValue);
            ((Rq1XmlTableColumn_CheckBox) xmlColumn).setValue(newRow, booleanValue);
        } else {
            newRow.setValueAt(xmlColumn.getColumnIndexData(), objectValue);
        }
    }

    private void loadColumnFromContent(Rq1XmlTableColumn xmlColumn, EcvXmlElement rowElement, EcvTableRow newRow) throws Rq1ParseFieldException {
        assert (xmlColumn != null);
        assert (rowElement != null);
        assert (newRow != null);

        //
        // The value of the column is the value of the row element.
        //
        if (rowElement instanceof EcvXmlTextElement) {
            String text = ((EcvXmlTextElement) rowElement).getText();
            newRow.setValueAt(xmlColumn.getColumnIndexData(), xmlColumn.loadValueFromDatabase(text));
        } else if (rowElement instanceof EcvXmlEmptyElement) {
            newRow.setValueAt(xmlColumn.getColumnIndexData(), xmlColumn.loadValueFromDatabase(""));
        } else {
            StringBuilder b = new StringBuilder(100);
            b.append("Error when parsing field ").append(xmlColumn.getUiName()).append(" in element ").append(rowElement.getName()).append(":\n");
            b.append("Element is not a simple element.");
            throw (new Rq1ParseFieldException(b.toString()));
        }
    }

    private void loadColumnFromTagName(Rq1XmlTableColumn xmlColumn, EcvXmlElement rowElement, EcvTableRow newRow) throws Rq1ParseFieldException {
        assert (xmlColumn != null);
        assert (rowElement != null);
        assert (newRow != null);

        //
        // The value of the column is the name of the row element.
        //
        newRow.setValueAt(xmlColumn.getColumnIndexData(), rowElement.getName());
    }

    private void loadColumnFromElementList(Rq1XmlTableColumn xmlColumn, EcvXmlElement rowElement, EcvTableRow newRow) throws Rq1ParseFieldException {
        assert (xmlColumn != null);
        assert (rowElement != null);
        assert (newRow != null);

        //
        // The value of the column is a list of strings stored in sub elements of the row element.
        //
        if (xmlColumn instanceof ElementListSupported) {
            if (rowElement instanceof EcvXmlContainerElement) {
                List<EcvXmlElement> l = ((EcvXmlContainerElement) rowElement).getElementList(xmlColumn.getSourceName());
                Object o = ((ElementListSupported) xmlColumn).loadFromElementList(l);
                newRow.setValueAt(xmlColumn.getColumnIndexData(), o);
            }
        } else {
            StringBuilder combinedValue = new StringBuilder();
            if (rowElement instanceof EcvXmlContainerElement) {
                for (EcvXmlElement currentValue : ((EcvXmlContainerElement) rowElement).getElementList(xmlColumn.getSourceName())) {
                    if (currentValue instanceof EcvXmlTextElement) {
                        if (combinedValue.length() > 0) {
                            combinedValue.append(", ");
                        }
                        combinedValue.append(((EcvXmlTextElement) currentValue).getText());
                    }
                }
            }

            newRow.setValueAt(xmlColumn.getColumnIndexData(), combinedValue.toString());
        }

    }

    private void loadColumnFromElementContent(Rq1XmlTableColumn xmlColumn, EcvXmlElement rowElement, EcvTableRow newRow) throws Rq1ParseFieldException {
        assert (xmlColumn != null);
        assert (rowElement != null);
        assert (newRow != null);

        //
        // The value of the column is the content of an sub Element in the  row element
        //
        if (rowElement instanceof EcvXmlContainerElement) {
            try {
                EcvXmlElement columnElement = ((EcvXmlContainerElement) rowElement).getElement(xmlColumn.getSourceName());
                if (columnElement instanceof EcvXmlEmptyElement) {
                    newRow.setValueAt(xmlColumn.getColumnIndexData(), "");
                } else if (columnElement instanceof EcvXmlTextElement) {
                    newRow.setValueAt(xmlColumn.getColumnIndexData(), ((EcvXmlTextElement) columnElement).getText());
                } else {
                    StringBuilder b = new StringBuilder(100);
                    b.append("Error when parsing field ").append(xmlColumn.getUiName()).append(" in element ").append(rowElement.getName()).append(":\n");
                    b.append("Element is an invalid type");
                    throw (new Rq1ParseFieldException(b.toString()));
                }
            } catch (NotfoundException e) {
                if (xmlColumn.isOptional() == false) {
                    StringBuilder b = new StringBuilder(100);
                    b.append("Error when parsing field ").append(xmlColumn.getUiName()).append(" in element ").append(rowElement.getName()).append(":\n");
                    b.append("Element not found.");
                    throw (new Rq1ParseFieldException(b.toString(), e));
                }
            }
        } else if (xmlColumn.isOptional() == false) {
            StringBuilder b = new StringBuilder(100);
            b.append("Error when parsing field ").append(xmlColumn.getUiName()).append(" in element ").append(rowElement.getName()).append(":\n");
            b.append("Element not found.");
            throw (new Rq1ParseFieldException(b.toString()));
        }

    }

    public EcvXmlElement provideRowAsXmlElementForDb(String elementName, EcvTableRow row) {
        assert (row != null);

        String tagName = null;
        if (elementName != null) {
            assert (elementName.isEmpty() == false);
            tagName = elementName;
        } else {
            assert (tagNameColumn != null);
            tagName = tagNameColumn.getValue(row);
            assert (tagName != null);
            assert (tagName.isEmpty() == false);
        }

        //
        // Create row element
        //
        EcvXmlElement rowElement;
        switch (rowEncodingMethod) {
            case EMPTY_ELEMENT:
                rowElement = new EcvXmlEmptyElement(tagName);
                break;
            case CONTAINER_ELEMENT:
                rowElement = new EcvXmlContainerElement(tagName);
                break;
            case TEXT_ELEMENT:
                String content = contentColumn.provideValueForDatabase(row);
                if ((commentColumn != null) && (row.getValueAt(commentColumn) != null)) {
                    assert (content != null) : this.getClass().getSimpleName() + ":" + contentColumn.getColumnIndexData();
                    assert (content.isEmpty() == false) : this.getClass().getSimpleName() + ":" + contentColumn.getColumnIndexData();
                    rowElement = new EcvXmlTextElement(tagName, content, " " + row.getValueAt(commentColumn).toString() + " ");
                } else if ((content != null) && (content.isEmpty() == false)) {
                    rowElement = new EcvXmlTextElement(tagName, content);
                } else {
                    if (contentColumn.isOptional() == false) {
                        LOGGER.log(Level.WARNING, this.getClass().getSimpleName() + ": No Content for column " + contentColumn.getColumnIndexData());
                    }
                    rowElement = new EcvXmlEmptyElement(tagName);
                }
                break;
            default:
                throw (new Error("Unknown encoding method"));
        }

        //
        // Add columns to row element
        //
        for (EcvTableColumnI ipeColumn : getColumns()) {
            assert (ipeColumn instanceof Rq1XmlTableColumn) : ipeColumn.getUiName() + "- " + ipeColumn.getClass().getCanonicalName();
            Rq1XmlTableColumn xmlColumn = (Rq1XmlTableColumn) ipeColumn;
            Object objectValue = row.getValues()[ipeColumn.getColumnIndexData()];

            switch (xmlColumn.getEncodingMethod()) {
                case ATTRIBUTE:
                    if ((objectValue != null) || (xmlColumn.isOptional() == false)) {
                        rowElement.addAttribute(xmlColumn.getSourceName(), xmlColumn.provideValueForDatabase(row));
                    }
                    break;

                case CONTENT:
                case TAG_NAME:
                case COMMENT:
                    // Already handled during row element creation.
                    break;

                case ELEMENT_LIST:
                    if (objectValue != null) {
                        if (xmlColumn instanceof ElementListSupported) {
                            for (String s : ((ElementListSupported) xmlColumn).provideElementForDb(objectValue)) {
                                ((EcvXmlContainerElement) rowElement).addElement(new EcvXmlTextElement(xmlColumn.getSourceName(), s));
                            }

                        } else {
                            //
                            // Split list in single elements
                            //
                            assert (objectValue instanceof String);
                            String[] stringValues = ((String) objectValue).split(",");

                            for (String s : stringValues) {
                                String s_trimmed = s.trim();
                                if (s_trimmed.isEmpty() == false) {
                                    ((EcvXmlContainerElement) rowElement).addElement(new EcvXmlTextElement(xmlColumn.getSourceName(), s.trim()));
                                }
                            }
                        }
                    }
                    break;

                case ELEMENT_CONTENT:
                    if ((objectValue != null) && (objectValue.toString().isEmpty() == false)) {
                        ((EcvXmlContainerElement) rowElement).addElement(new EcvXmlTextElement(xmlColumn.getSourceName(), objectValue.toString()));
                    }
                    break;

                default:
                    throw (new Error("Unknown source type: " + xmlColumn.getEncodingMethod().toString()));
            }
        }

        return (rowElement);
    }
}
