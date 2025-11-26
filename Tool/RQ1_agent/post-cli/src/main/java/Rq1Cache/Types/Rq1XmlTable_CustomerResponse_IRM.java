/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.PPT.BackChannel.BewertungOem;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Pst;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import util.EcvDate;
import util.EcvEnumeration;
import util.EcvTableData;
import util.EcvTableRow;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_CustomerResponse_IRM extends Rq1XmlTable_CustomerResponse {

    //--------------------------------------------------------------------------
    //
    // Table definition
    //
    //--------------------------------------------------------------------------
    static public enum Type implements EcvEnumeration {

        BC("BC"),
        PST("PVER/PVAR");
        //
        private final String dbText;

        private Type(String dbText) {
            assert (dbText != null);
            this.dbText = dbText;
        }

        @Override
        public String getText() {
            return dbText;
        }

        static public Type get(String typeString) {
            for (Type value : values()) {
                if (value.getText().equals(typeString) == true) {
                    return (value);
                }
            }
            return (null);
        }

        public int compareTo(String typeString) {
            Type c = get(typeString);
            if (c == null) {
                return (-1);
            }
            return (compareTo(c));
        }

    }

    final public Rq1XmlTableColumn_ComboBox TYPE;
    final public Rq1XmlTableColumn_String NAME;
    final public Rq1XmlTableColumn_String VERSION;
    final public Rq1XmlTableColumn_String ID;

    public Rq1XmlTable_CustomerResponse_IRM() {

        addXmlColumn(TYPE = new Rq1XmlTableColumn_ComboBox("Type", 5, Type.values(), "type", ColumnEncodingMethod.ATTRIBUTE));
        TYPE.setOptional();

        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 10, "name", ColumnEncodingMethod.ATTRIBUTE));
        NAME.setOptional();

        addXmlColumn(VERSION = new Rq1XmlTableColumn_String("Version", 10, "version", ColumnEncodingMethod.ATTRIBUTE));
        VERSION.setOptional();

        addXmlColumn(ID = new Rq1XmlTableColumn_String("RQ1-ID", 10, "rq1id", ColumnEncodingMethod.ATTRIBUTE));
        ID.setOptional();
    }

    //--------------------------------------------------------------------------
    //
    // Support for record based access
    //
    //--------------------------------------------------------------------------
    static public class Record extends Rq1XmlTable_CustomerResponse.Record {

        final private Type type;
        final private String name;
        final private String version;
        final private String id;

        @SuppressWarnings("unchecked")
        private Record(EcvTableData data, EcvTableRow row) {
            super(data, row);
            assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_IRM);

            Rq1XmlTable_CustomerResponse_IRM d = (Rq1XmlTable_CustomerResponse_IRM) data.getDescription();

            type = Type.get(d.TYPE.getValue(row));
            name = d.NAME.getValue(row);
            version = d.VERSION.getValue(row);
            id = d.ID.getValue(row);
        }

        final public Type getType() {
            return type;
        }

        final public String getTypeAsString() {
            if (type != null) {
                return (type.getText());
            } else {
                return ("");
            }
        }

        final public String getName() {
            return name;
        }

        final public String getVersion() {
            return version;
        }

        final public String getId() {
            return id;
        }

        @Override
        public void addContentAsAttributes(EcvXmlElement xmlElement) {
            super.addContentAsAttributes(xmlElement);
            exportNonEmpty(xmlElement, "type", getTypeAsString());
            exportNonEmpty(xmlElement, "name", getName());
            exportNonEmpty(xmlElement, "version", getVersion());
            exportNonEmpty(xmlElement, "id", getId());
        }

    }

    static public List<Record> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_IRM);

        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            result.add(new Record(data, row));
        }
        return (result);
    }

    //--------------------------------------------------------------------------
    //
    // Support for setting of customer response
    //
    //--------------------------------------------------------------------------
    public boolean setResponse(EcvTableData data, Response response, String derivative, DmRq1Pst pst) {
        assert (response != null);
        assert (pst != null);
        return (setResponse(data, response.getBewertungOem(), response.getKommentarOEM(), derivative, response.getOemDatumOrToday(), response.getAnsprechpartnerOEM(), Type.PST, pst.getName(), pst.getVersion(), pst.getRq1Id()));
    }

    public boolean setResponse(EcvTableData data, Response response, String derivative, DmRq1Bc bc) {
        assert (response != null);
        assert (bc != null);
        return (setResponse(data, response.getBewertungOem(), response.getKommentarOEM(), derivative, response.getOemDatumOrToday(), response.getAnsprechpartnerOEM(), Type.BC, bc.getName(), bc.getVersion(), bc.getRq1Id()));
    }

    boolean setResponse(EcvTableData data, BewertungOem state, String comment, String derivative, EcvDate date, String customerContact, Type type, String name, String version, String id) {
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_IRM);
        assert (state != null);
        assert (date != null);
        assert (type != null);
        assert (name != null && name.isEmpty() == false);
        if (type != Type.PST) {
            assert (version != null && version.isEmpty() == false);
        }
        assert (id != null && id.isEmpty() == false);

        if ((state != BewertungOem.ABGELEHNT) && (state != BewertungOem.GEFORDERT) && (state != BewertungOem.GEFORDERT_ABER_NICHT_ALS_SPLIT) && (state != BewertungOem.GEDULDET)) {
            return deleteEntry(data, derivative, type, name, version, id);
        } else {
            if ((state == BewertungOem.GEFORDERT) || (state == BewertungOem.GEFORDERT_ABER_NICHT_ALS_SPLIT) || (state == BewertungOem.GEDULDET)) {
                //If there is already the information about abgelehnt, so delete it and write the new Information
                deleteEntry(data, derivative, type, name, version, id);
            }
            boolean dataChanged = false;
            boolean newLine = true;

            for (EcvTableRow row : data.getRows()) {
                Object d = row.getValueAt(this.DERIVATIVE);
                if (d instanceof Set && (((Set) d).contains(derivative)) && (((Set) d).size() == 1)
                        || !(d instanceof Set) && derivative.isEmpty()) {
                    boolean rowChanged = false;
                    if (type != null && row.getValueAt(this.TYPE) != null && row.getValueAt(this.TYPE).equals(type.getText())
                            && row.getValueAt(this.NAME) != null && row.getValueAt(this.NAME).equals(name)
                            && row.getValueAt(this.VERSION) != null && row.getValueAt(this.VERSION).equals(version)
                            && row.getValueAt(this.ID) != null && row.getValueAt(this.ID).equals(id)
                            || (row.getValueAt(this.TYPE) == null && type == null)) {
                        newLine = false;

                        if (row.getValueAt(this.CUSTOMER_COMMENT) == null || row.getValueAt(this.CUSTOMER_COMMENT) != null && !row.getValueAt(this.CUSTOMER_COMMENT).toString().equals(comment)) {
                            dataChanged = true;
                            rowChanged = true;
                            CUSTOMER_COMMENT.setValue(row, comment);
                        }
                        if (row.getValueAt(this.CUSTOMER_STATE) == null || row.getValueAt(this.CUSTOMER_STATE) != null && !row.getValueAt(this.CUSTOMER_STATE).toString().equals(state.getTextInFile())) {
                            dataChanged = true;
                            rowChanged = true;
                            CUSTOMER_STATE.setValue(row, state.getTextInFile());
                        }
                        if (row.getValueAt(this.CUSTOMER_CONTACT) == null || row.getValueAt(this.CUSTOMER_CONTACT) != null && !row.getValueAt(this.CUSTOMER_CONTACT).toString().equals(customerContact)) {
                            dataChanged = true;
                            rowChanged = true;
                            CUSTOMER_CONTACT.setValue(row, customerContact);
                        }
                    }
                    if (rowChanged) {
                        dataChanged = true;
                        DATE.setValue(row, date.getXmlValue());
                    }
                } else if (d instanceof Set && (((Set) d).contains(derivative))) {
                    //There is another entry where the same derivative is set
                    //Check if there is the same Type information (or no type information), then uncheck the derivative there
                    //because a new line is needed
                    if ((type != null && row.getValueAt(this.TYPE) != null && row.getValueAt(this.TYPE).equals(type.getText())
                            && row.getValueAt(this.NAME) != null && row.getValueAt(this.NAME).equals(name)
                            && row.getValueAt(this.VERSION) != null && row.getValueAt(this.VERSION).equals(version)
                            && row.getValueAt(this.ID) != null && row.getValueAt(this.ID).equals(id)
                            || (row.getValueAt(this.TYPE) == null && type == null))) {

                        if ((row.getValueAt(this.CUSTOMER_COMMENT) == null && !comment.isEmpty()
                                || row.getValueAt(this.CUSTOMER_COMMENT) != null && comment.isEmpty()
                                || row.getValueAt(this.CUSTOMER_COMMENT) != null && !row.getValueAt(this.CUSTOMER_COMMENT).equals(comment))
                                || (row.getValueAt(this.CUSTOMER_STATE) != null && !row.getValueAt(this.CUSTOMER_STATE).equals(state.getTextInFile()))
                                || (row.getValueAt(this.CUSTOMER_CONTACT) == null && !customerContact.isEmpty()
                                || row.getValueAt(this.CUSTOMER_CONTACT) != null && customerContact.isEmpty()
                                || row.getValueAt(this.CUSTOMER_CONTACT) != null && !row.getValueAt(this.CUSTOMER_CONTACT).equals(customerContact))) {

                            Set derivatives = (Set) row.getValueAt(this.DERIVATIVE);
                            if (derivatives.contains(derivative)) {
                                derivatives.remove(derivative);
                                dataChanged = true;
                            }
                        } else {
                            //if there is the same information, no new Line is needed
                            newLine = false;
                            dataChanged = false;
                        }
                    }
                }
            }

            //Check if the same information is already there under another derivate
            if (newLine) {
                for (EcvTableRow row : data.getRows()) {
                    if (row.getValueAt(this.CUSTOMER_COMMENT) != null && row.getValueAt(this.CUSTOMER_COMMENT).equals(comment)
                            && row.getValueAt(this.CUSTOMER_STATE) != null && row.getValueAt(this.CUSTOMER_STATE).equals(state.getTextInFile())
                            && row.getValueAt(this.CUSTOMER_CONTACT) != null && row.getValueAt(this.CUSTOMER_CONTACT).equals(customerContact)
                            && ((type == null && row.getValueAt(this.TYPE) == null)
                            || (type != null && row.getValueAt(this.TYPE) != null
                            && ((row.getValueAt(this.ID) == null) || row.getValueAt(this.ID) != null && row.getValueAt(this.ID).equals(id))
                            && ((row.getValueAt(this.NAME) == null) || row.getValueAt(this.NAME) != null && row.getValueAt(this.NAME).equals(name))
                            && ((row.getValueAt(this.VERSION) == null) || row.getValueAt(this.VERSION) != null && row.getValueAt(this.VERSION).equals(version))
                            && ((row.getValueAt(this.TYPE) == null) || row.getValueAt(this.TYPE) != null && row.getValueAt(this.TYPE).equals(type.getText()))))) {
                        if (!derivative.isEmpty()) {
                            SortedSet<String> derivates = DERIVATIVE.getValue(row);
                            derivates.add(derivative);
                            DERIVATIVE.setValue(row, derivates);
                        }
                        DATE.setValue(row, date.getXmlValue());
                        newLine = false;
                        dataChanged = true;
                    }
                }
            }

            if (newLine) {
                EcvTableRow newRow = data.createAndAddRow();
                if (!derivative.isEmpty()) {
                    SortedSet<String> derivativeSet = new TreeSet<>();
                    derivativeSet.add(derivative);
                    DERIVATIVE.setValue(newRow, derivativeSet);
                }
                CUSTOMER_COMMENT.setValue(newRow, comment);
                CUSTOMER_STATE.setValue(newRow, state.getTextInFile());
                DATE.setValue(newRow, date.getXmlValue());
                CUSTOMER_CONTACT.setValue(newRow, customerContact);
                if (type != null && !name.isEmpty()
                        && ((type != Type.PST && !version.isEmpty()) || type == Type.PST)
                        && !id.isEmpty()) { //if there is a component information given
                    TYPE.setValue(newRow, type.getText());
                    NAME.setValue(newRow, name);
                    VERSION.setValue(newRow, version);
                    ID.setValue(newRow, id);
                }
                dataChanged = true;
            }

            foldList(data);

            return (dataChanged);
        }
    }

    public boolean removeAllBcAbgelehnt(EcvTableData data, Collection<String> derivatives) {
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_IRM);
        assert (derivatives != null);

        return (removeAllBcByState(data, derivatives, EnumSet.of(BewertungOem.ABGELEHNT)));
    }

    public boolean removeAllBcGefordert(EcvTableData data, Collection<String> derivatives) {
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_IRM);
        assert (derivatives != null);

        return (removeAllBcByState(data, derivatives, EnumSet.of(BewertungOem.GEFORDERT, BewertungOem.GEFORDERT_ABER_NICHT_ALS_SPLIT)));
    }

    private boolean removeAllBcByState(EcvTableData data, Collection<String> derivatives, EnumSet<BewertungOem> bewertung) {
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_IRM);
        assert (derivatives != null);
        assert (bewertung != null);

        boolean dataChanged = false;
        List<EcvTableRow> toDelete = new ArrayList<>();

        for (EcvTableRow row : data.getRows()) {
            if (Type.BC.compareTo(TYPE.getValue(row)) == 0) {
                if (bewertung.contains(BewertungOem.get(CUSTOMER_STATE.getValue(row))) == true) {
                    switch (removeDerivatives(row, derivatives)) {
                        case CHANGED:
                            dataChanged = true;
                            break;
                        case CHANGED_AND_NOW_EMPTY:
                            toDelete.add(row);
                            dataChanged = true;
                            break;
                    }
                }
            }
        }

        data.removeRows(toDelete);

        return (dataChanged);
    }

    //Check each entry, if they could be put together
    private void foldList(EcvTableData data) {
        //For each row, check the other rows, if they could be put together
        List<EcvTableRow> toDelete = new LinkedList<>();
        List<EcvTableRow> alreadySeen = new LinkedList<>();
        for (EcvTableRow row : data.getRows()) {
            alreadySeen.add(row);
            for (EcvTableRow row2 : data.getRows()) {
                if (!row2.equals(row) && !alreadySeen.contains(row2)) {
                    if (row.getValueAt(this.CUSTOMER_STATE) != null && row2.getValueAt(this.CUSTOMER_STATE) != null && row2.getValueAt(this.CUSTOMER_STATE).equals(row.getValueAt(this.CUSTOMER_STATE))
                            && row.getValueAt(this.CUSTOMER_COMMENT) != null && row2.getValueAt(this.CUSTOMER_COMMENT) != null && row2.getValueAt(this.CUSTOMER_COMMENT).equals(row.getValueAt(this.CUSTOMER_COMMENT))
                            && row.getValueAt(this.CUSTOMER_CONTACT) != null && row2.getValueAt(this.CUSTOMER_CONTACT) != null && row2.getValueAt(this.CUSTOMER_CONTACT).equals(row.getValueAt(this.CUSTOMER_CONTACT))) {
                        //Date does not have to be checked
                        //&& row.getValueAt(this.DATE) != null && row2.getValueAt(this.DATE) != null && row2.getValueAt(this.DATE).equals(row.getValueAt(this.DATE))
                        //The basic information is the same    
                        //Check if there is a Type
                        if ((row.getValueAt(this.TYPE) != null && row2.getValueAt(this.TYPE) != null
                                && row.getValueAt(this.NAME) != null && row2.getValueAt(this.NAME) != null && row.getValueAt(this.NAME).equals(row2.getValueAt(this.NAME))
                                && (!row.getValueAt(this.TYPE).equals(Type.PST.getText()) && row.getValueAt(this.VERSION) != null && row2.getValueAt(this.VERSION) != null && row.getValueAt(this.VERSION).equals(row2.getValueAt(this.VERSION)))
                                && row.getValueAt(this.ID) != null && row2.getValueAt(this.ID) != null && row.getValueAt(this.ID).equals(row2.getValueAt(this.ID))
                                && row.getValueAt(this.TYPE) != null && row2.getValueAt(this.TYPE) != null && row.getValueAt(this.TYPE).equals(row2.getValueAt(this.TYPE)))
                                || (row.getValueAt(this.TYPE) == null && row2.getValueAt(this.TYPE) == null)) {
                            //Both have no type or the whole type information is also the same
                            if (row.getValueAt(this.DERIVATIVE) instanceof Set && row2.getValueAt(this.DERIVATIVE) instanceof Set) {
                                //if bove have derivative informaiton
                                //get the derivative of the first row into the second one
                                SortedSet<String> derOfRow2 = DERIVATIVE.getValue(row2);
                                SortedSet<String> derOfRow1 = DERIVATIVE.getValue(row);
                                derOfRow2.addAll(derOfRow1);

                                //
                                // Take latest date into combined row
                                //
                                EcvDate date1 = EcvDate.getDateForXmlValueOrNull(row.getValueAt(DATE));
                                EcvDate date2 = EcvDate.getDateForXmlValueOrNull(row2.getValueAt(DATE));
                                if ((date2 == null) || ((date1 != null) && (date1.isLaterThen(date2) == true))) {
                                    DATE.setValue(row2, date1.getXmlValue());
                                }

                                toDelete.add(row);
                            } else if (row.getValueAt(this.DERIVATIVE) == null && row2.getValueAt(this.DERIVATIVE) == null) {
                                toDelete.add(row);
                            }
                        }
                    }
                }
            }
        }

        for (EcvTableRow row : toDelete) {
            data.removeRow(row);
        }
    }

    private boolean deleteEntry(EcvTableData data, String derivative, Type type, String name, String version, String id) {
        boolean dataChanged = false;
        List<EcvTableRow> toDelete = new LinkedList<>();
        for (EcvTableRow row : data.getRows()) {

            String rowType = TYPE.getValue(row);
            String rowId = ID.getValue(row);
            String rowName = NAME.getValue(row);
            String rowVersion = VERSION.getValue(row);

            //if both have no type, or both have a type, or the given parameter has the type pst and the parameter of the row is null
            if (((type == null && rowType == null)
                    || (type != null && type.equals(Type.PST) && rowType == null)
                    || (type != null && rowType != null
                    && ((rowId == null) || rowId != null && rowId.equals(id))
                    && ((rowName == null) || rowName != null && rowName.equals(name))
                    && ((rowVersion == null) || rowVersion != null && rowVersion.equals(version))
                    && ((rowType == null) || rowType != null && rowType.equals(type.getText()))))) {
                //the entry is the same
                //Check if there are derivatives on the record
                if (row.getValueAt(this.DERIVATIVE) instanceof Set) {
                    //Delete the given derivate out of the row
                    if (((Set) row.getValueAt(this.DERIVATIVE)).contains(derivative) && ((Set) row.getValueAt(this.DERIVATIVE)).size() > 1) {
                        ((Set) row.getValueAt(this.DERIVATIVE)).remove(derivative);
                        dataChanged = true;
                    } else if (((Set) row.getValueAt(this.DERIVATIVE)).contains(derivative)) {
                        //it is the only derivative, so delete the row
                        toDelete.add(row);
                        dataChanged = true;
                    }
                } else //There are no Derivatives on the Record
                //Check if there are Derivatives given
                {
                    if (derivative.isEmpty()) {
                        //Delete the row 
                        toDelete.add(row);
                        dataChanged = true;
                    }
                }
            }
        }

        for (EcvTableRow row : toDelete) {
            data.removeRow(row);
        }

        return dataChanged;
    }

}
