/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.PPT.BackChannel.BewertungOem;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import util.EcvDate;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_CustomerResponse_RRM extends Rq1XmlTable_CustomerResponse {

    final public Rq1XmlTableColumn_String FC_NAME;
    final public Rq1XmlTableColumn_String FC_VERSION;
    final public Rq1XmlTableColumn_String I_SW;

    public Rq1XmlTable_CustomerResponse_RRM() {

        addXmlColumn(FC_NAME = new Rq1XmlTableColumn_String("FC Name", 10, "fcName", ColumnEncodingMethod.ATTRIBUTE));
        FC_NAME.setOptional();
        addXmlColumn(FC_VERSION = new Rq1XmlTableColumn_String("FC Version", 10, "fcVersion", ColumnEncodingMethod.ATTRIBUTE));
        FC_VERSION.setOptional();

        addXmlColumn(I_SW = new Rq1XmlTableColumn_String("I-SW", 10, "issueSoftware", ColumnEncodingMethod.ATTRIBUTE));
        I_SW.setOptional();
    }

    //--------------------------------------------------------------------------
    //
    // Support for record based access
    //
    //--------------------------------------------------------------------------
    static public class Record extends Rq1XmlTable_CustomerResponse.Record {

        final private String fcName;
        final private String fcVersion;
        final private String issueSW;

        @SuppressWarnings("unchecked")
        private Record(EcvTableData data, EcvTableRow row) {
            super(data, row);
            assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_RRM);

            Rq1XmlTable_CustomerResponse_RRM d = (Rq1XmlTable_CustomerResponse_RRM) data.getDescription();

            fcName = (String) row.getValueAt(d.FC_NAME);
            fcVersion = (String) row.getValueAt(d.FC_VERSION);
            issueSW = (String) row.getValueAt(d.I_SW);
        }

        public String getFcName() {
            return fcName;
        }

        public String getFcVersion() {
            return fcVersion;
        }

        public String getIssueSW() {
            return issueSW;
        }

    }

    static public List<Record> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_RRM);

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
    public boolean setResponse(EcvTableData data, Rq1XmlTable_CustomerResponse_IRM.Response response, String derivative, String fcName, String fcVersion, String iSW) {
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_RRM);
        assert (response != null);
        return (setResponse(data, response.getBewertungOem(), response.getKommentarOEM(), derivative, fcName, fcVersion, iSW, response.getOemDatumOrToday(), response.getAnsprechpartnerOEM()));
    }

    public boolean setResponse(EcvTableData data, BewertungOem state, String comment, String derivative, String fcName, String fcVersion, String iSW, EcvDate date, String customerContact) {
        assert (data.getDescription() instanceof Rq1XmlTable_CustomerResponse_RRM);
        assert (date != null);

        if (customerContact == null) {
            customerContact = "";
        }
        if (comment == null) {
            comment = "";
        }

        if ((state != BewertungOem.ABGELEHNT) && (state != BewertungOem.GEFORDERT) && (state != BewertungOem.GEFORDERT_ABER_NICHT_ALS_SPLIT) && (state != BewertungOem.GEDULDET)) {
            return deleteEntry(data, derivative, fcName, fcVersion, iSW);
        } else {
            if ((state == BewertungOem.GEFORDERT) || (state == BewertungOem.GEFORDERT_ABER_NICHT_ALS_SPLIT) || (state == BewertungOem.GEDULDET)) {
                //First the entries, with abgelehnt have to be deleted, if there are ones, than the entry with gefordert should be written
                deleteEntry(data, derivative, fcName, fcVersion, iSW);
            }

            boolean retval = false;
            boolean newLine = true;

            for (EcvTableRow row : data.getRows()) {
                Object d = row.getValueAt(this.DERIVATIVE);
                if (d instanceof Set && (((Set) d).contains(derivative)) && ((Set) d).size() == 1
                        || !(d instanceof Set) && derivative.isEmpty()) {
                    if (((row.getValueAt(FC_NAME) == null && fcName.equals("")) || row.getValueAt(this.FC_NAME) != null && row.getValueAt(this.FC_NAME).equals(fcName))
                            && ((row.getValueAt(FC_VERSION) == null && fcVersion.equals("")) || row.getValueAt(this.FC_VERSION) != null && row.getValueAt(this.FC_VERSION).equals(fcVersion))
                            && ((row.getValueAt(I_SW) == null && iSW.equals("")) || row.getValueAt(this.I_SW) != null && row.getValueAt(this.I_SW).equals(iSW))) {

                        newLine = false;
                        boolean dataChanged = false;

                        if (row.getValueAt(this.CUSTOMER_COMMENT) == null || !row.getValueAt(this.CUSTOMER_COMMENT).equals(comment)) {
                            retval = true;
                            dataChanged = true;
                            CUSTOMER_COMMENT.setValue(row, comment);
                        }
                        if (row.getValueAt(CUSTOMER_STATE) == null || !row.getValueAt(this.CUSTOMER_STATE).equals(state.getTextInFile())) {
                            retval = true;
                            dataChanged = true;
                            CUSTOMER_STATE.setValue(row, state.getTextInFile());
                        }
                        if (row.getValueAt(CUSTOMER_CONTACT) == null || row.getValueAt(this.CUSTOMER_CONTACT) != null && !row.getValueAt(this.CUSTOMER_CONTACT).equals(customerContact)) {
                            retval = true;
                            dataChanged = true;
                            CUSTOMER_CONTACT.setValue(row, customerContact);
                        }
                        if (dataChanged) {
                            retval = true;
                            DATE.setValue(row, date.getXmlValue());
                        }
                    }
                } else if (d instanceof Set && (((Set) d).contains(derivative))) {
                    if (((row.getValueAt(FC_NAME) == null && fcName.equals("")) || row.getValueAt(this.FC_NAME) != null && row.getValueAt(this.FC_NAME).equals(fcName))
                            && ((row.getValueAt(FC_VERSION) == null && fcVersion.equals("")) || row.getValueAt(this.FC_VERSION) != null && row.getValueAt(this.FC_VERSION).equals(fcVersion))
                            && ((row.getValueAt(I_SW) == null && iSW.equals("")) || row.getValueAt(this.I_SW) != null && row.getValueAt(this.I_SW).equals(iSW))) {
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
                            }
                        } else {
                            retval = false;
                            newLine = false;
                        }
                    }

                }
            }

            //Check if the same information is already there under another derivate
            if (newLine) {
                for (EcvTableRow row : data.getRows()) {
                    if (row.getValueAt(this.CUSTOMER_COMMENT) != null && row.getValueAt(this.CUSTOMER_COMMENT).equals(comment)
                            && row.getValueAt(this.CUSTOMER_STATE) != null && row.getValueAt(this.CUSTOMER_STATE).equals(state.getTextInFile())
                            && ((row.getValueAt(this.FC_NAME) == null && fcName.isEmpty()) || row.getValueAt(this.FC_NAME) != null && row.getValueAt(FC_NAME).equals(fcName))
                            && ((row.getValueAt(this.FC_VERSION) == null && fcVersion.isEmpty()) || row.getValueAt(this.FC_VERSION) != null && row.getValueAt(FC_VERSION).equals(fcVersion))
                            && ((row.getValueAt(this.I_SW) == null && iSW.isEmpty()) || row.getValueAt(this.I_SW) != null && row.getValueAt(this.I_SW).equals(iSW))
                            && row.getValueAt(this.CUSTOMER_CONTACT) != null && row.getValueAt(this.CUSTOMER_CONTACT).equals(customerContact)) {
                        if (!derivative.isEmpty()) {
                            SortedSet<String> derivates = DERIVATIVE.getValue(row);
                            derivates.add(derivative);
                            DERIVATIVE.setValue(row, derivates);
                        }
                        //
                        // Take latest date into combined row
                        //
                        EcvDate dateFromRow = EcvDate.getDateForXmlValueOrNull(row.getValueAt(DATE));

                        DATE.setValue(row, date.getXmlValue());

                        newLine = false;
                        retval = true;
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
                CUSTOMER_CONTACT.setValue(newRow, customerContact);
                DATE.setValue(newRow, date.getXmlValue());
                if (!fcName.isEmpty()) {
                    FC_NAME.setValue(newRow, fcName);
                }
                if (!fcVersion.isEmpty()) {
                    FC_VERSION.setValue(newRow, fcVersion);
                }
                if (!iSW.isEmpty()) {
                    I_SW.setValue(newRow, iSW);
                }
                retval = true;
            }

            foldTable(data);

            return (retval);
        }
    }

    private void foldTable(EcvTableData data) {
        //For each row, check the other rows, if they could be put together
        List<EcvTableRow> toDelete = new LinkedList<>();
        List<EcvTableRow> alreadySeen = new LinkedList<>();
        for (EcvTableRow row : data.getRows()) {
            alreadySeen.add(row);
            for (EcvTableRow row2 : data.getRows()) {
                if (!row2.equals(row) && !alreadySeen.contains(row2)) {
                    if (row.getValueAt(this.CUSTOMER_STATE) != null && row2.getValueAt(this.CUSTOMER_STATE) != null && row2.getValueAt(this.CUSTOMER_STATE).equals(row.getValueAt(this.CUSTOMER_STATE))
                            && row.getValueAt(this.CUSTOMER_COMMENT) != null && row2.getValueAt(this.CUSTOMER_COMMENT) != null && row2.getValueAt(this.CUSTOMER_COMMENT).equals(row.getValueAt(this.CUSTOMER_COMMENT))
                            && row.getValueAt(this.CUSTOMER_CONTACT) != null && row2.getValueAt(this.CUSTOMER_CONTACT) != null && row2.getValueAt(this.CUSTOMER_CONTACT).equals(row.getValueAt(this.CUSTOMER_CONTACT))
                            //                            && row.getValueAt(this.DATE) != null && row2.getValueAt(this.DATE) != null && row2.getValueAt(this.DATE).equals(row.getValueAt(this.DATE))
                            && ((row.getValueAt(this.FC_NAME) == null && row2.getValueAt(FC_NAME) == null) || row.getValueAt(this.FC_NAME) != null && row2.getValueAt(this.FC_NAME) != null && row2.getValueAt(this.FC_NAME).equals(row.getValueAt(this.FC_NAME)))
                            && ((row.getValueAt(this.FC_VERSION) == null && row2.getValueAt(FC_VERSION) == null) || row.getValueAt(this.FC_VERSION) != null && row2.getValueAt(this.FC_VERSION) != null && row2.getValueAt(this.FC_VERSION).equals(row.getValueAt(this.FC_VERSION)))
                            && ((row.getValueAt(this.I_SW) == null && row2.getValueAt(I_SW) == null) || row.getValueAt(this.I_SW) != null && row2.getValueAt(this.I_SW) != null && row2.getValueAt(this.I_SW).equals(row.getValueAt(this.I_SW)))) {
                        //The basic information is the same    
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
                        }
                    }
                }
            }
        }

        for (EcvTableRow row : toDelete) {
            data.removeRow(row);
        }
    }

    private boolean deleteEntry(EcvTableData data, String derivative, String fcName, String fcVersion, String iSW) {
        boolean retval = false;
        List<EcvTableRow> toDelete = new LinkedList<>();
        for (EcvTableRow row : data.getRows()) {
            if (((row.getValueAt(this.FC_NAME) == null && fcName.isEmpty()) || row.getValueAt(this.FC_NAME) != null && row.getValueAt(FC_NAME).equals(fcName))
                    && ((row.getValueAt(this.FC_VERSION) == null && fcVersion.isEmpty()) || row.getValueAt(this.FC_VERSION) != null && row.getValueAt(FC_VERSION).equals(fcVersion))
                    && ((row.getValueAt(this.I_SW) == null && iSW.isEmpty()) || row.getValueAt(this.I_SW) != null && row.getValueAt(this.I_SW).equals(iSW))) {
                //the entry is the same
                //Check if there are derivatives on the record
                if (row.getValueAt(this.DERIVATIVE) instanceof Set) {
                    //Delete the given derivate out of the row
                    if (((Set) row.getValueAt(this.DERIVATIVE)).contains(derivative) && ((Set) row.getValueAt(this.DERIVATIVE)).size() > 1) {
                        ((Set) row.getValueAt(this.DERIVATIVE)).remove(derivative);
                        retval = true;
                    } else if (((Set) row.getValueAt(this.DERIVATIVE)).contains(derivative)) {
                        //it is the only derivative, so delete the row
                        toDelete.add(row);
                        retval = true;
                    }
                } else {
                    //There are no Derivatives on the Record
                    //Check if there are Derivatives given
                    if (derivative.isEmpty()) {
                        //Delete the row 
                        toDelete.add(row);
                        retval = true;
                    }
                }
            }
        }

        for (EcvTableRow row : toDelete) {
            data.removeRow(row);
        }
        return retval;
    }

}
