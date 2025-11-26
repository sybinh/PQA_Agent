/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Data.Enumerations.CtiClassification;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_RrmChangesToIssues extends Rq1XmlTable {

    final public Rq1XmlTableColumn_ComboBox TYPE;
    final public Rq1XmlTableColumn_String ID;
    final public Rq1XmlTableColumn_String TITLE;
    final public Rq1XmlTableColumn_ComboBox CLASSIFICATION;
    final public Rq1XmlTableColumn_Set DERIVATIVE;
    final public Rq1XmlTableColumn_String INTERNAL_DESCRIPTION;
    final public Rq1XmlTableColumn_String EXTERNAL_COMMENT;

    public Rq1XmlTable_RrmChangesToIssues() {

        addXmlColumn(TYPE = new Rq1XmlTableColumn_ComboBox("Type", 5, new String[]{"I-FD"}, "Type", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(ID = new Rq1XmlTableColumn_String("RQ1-ID", 15, "Id", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(TITLE = new Rq1XmlTableColumn_String("Issue Title", 40, "Title", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(CLASSIFICATION = new Rq1XmlTableColumn_ComboBox("Classification", 14, CtiClassification.values(), "Classification", ColumnEncodingMethod.ATTRIBUTE));
        CLASSIFICATION.setOptional();
        addXmlColumn(DERIVATIVE = new Rq1XmlTableColumn_Set("Derivative", 20, "Derivate", ColumnEncodingMethod.ELEMENT_LIST));
        addXmlColumn(INTERNAL_DESCRIPTION = new Rq1XmlTableColumn_String("Internal Description", 50, "InternalDescription", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(EXTERNAL_COMMENT = new Rq1XmlTableColumn_String("External Comment", 50, "ExternalComment", ColumnEncodingMethod.ATTRIBUTE));

//        setMaxRowCount(66);
    }

    static public class Record {

        final private EcvTableData data;
        final private EcvTableRow row;
        private boolean deleted;
        //
        final private String type;
        final private String rq1Id;
        final private String title;
        private String classification;
        private SortedSet<String> derivative;
        final private String internalDescription;
        private String externalComment;

        @SuppressWarnings("unchecked")
        private Record(EcvTableData data, EcvTableRow row) {
            assert (data != null);
            assert (row != null);
            this.data = data;
            this.row = row;
            this.deleted = false;
            Rq1XmlTable_RrmChangesToIssues d = (Rq1XmlTable_RrmChangesToIssues) data.getDescription();

            type = (String) row.getValueAt(d.TYPE);
            rq1Id = (String) row.getValueAt(d.ID);
            title = (String) row.getValueAt(d.TITLE);
            classification = (String) row.getValueAt(d.CLASSIFICATION);
            derivative = (SortedSet<String>) row.getValueAt(d.DERIVATIVE);
            internalDescription = (String) row.getValueAt(d.INTERNAL_DESCRIPTION);
            externalComment = (String) row.getValueAt(d.EXTERNAL_COMMENT);
        }

        public Record(String type, String id, String title) {
            assert (type != null);
            assert (type.isEmpty() == false);
            assert (id != null);
            assert (id.isEmpty() == false);
            assert (title != null);
            assert (title.isEmpty() == false);

            this.data = null;
            this.row = null;
            this.deleted = false;

            this.type = type;
            this.rq1Id = id;
            this.title = title;
            classification = null;
            derivative = new TreeSet<>();
            internalDescription = null;
            externalComment = null;
        }

        private EcvTableRow getRow() {
            return (row);
        }

        private boolean updateRow() {
            assert (row != null);
            return (fillRow(data, row));
        }

        private boolean fillRow(EcvTableData data, EcvTableRow row) {
            Rq1XmlTable_RrmChangesToIssues d = (Rq1XmlTable_RrmChangesToIssues) data.getDescription();
            boolean changed = false;
            changed |= d.TYPE.setValue(row, type);
            changed |= d.ID.setValue(row, rq1Id);
            changed |= d.TITLE.setValue(row, title);
            changed |= d.CLASSIFICATION.setValue(row, classification);
            changed |= d.DERIVATIVE.setValue(row, derivative);
            changed |= d.INTERNAL_DESCRIPTION.setValue(row, internalDescription);
            changed |= d.EXTERNAL_COMMENT.setValue(row, externalComment);
            return (changed);
        }

        public String getType() {
            return type;
        }

        public String getRq1Id() {
            return rq1Id;
        }

        public String getTitle() {
            return title;
        }

        public Object getClassification() {
            return classification;
        }

        public SortedSet<String> getDerivative() {
            return derivative;
        }

        public String getInternalDescription() {
            return internalDescription;
        }

        public String getExternalComment() {
            return externalComment;
        }

        public boolean isDeleted() {
            return (deleted);
        }

        public void setClassification(String text) {
            classification = text;
        }

        public void setDerivative(SortedSet<String> derivative) {
            assert (derivative != null);
            this.derivative = derivative;
        }

        public void setExternalComment(String text) {
            assert (text != null);
            externalComment = text;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

    }

    static public List<Record> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_RrmChangesToIssues);

        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            result.add(new Record(data, row));
        }
        return (result);
    }

    /**
     * Updates the table data to the new values
     *
     * @param data Table data that shall be updated.
     * @param newValues
     * @return true, if the update did change the data; false otherwise
     */
    static public boolean update(EcvTableData data, List<Record> newValues) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_RrmChangesToIssues);
        assert (newValues != null);

        boolean changed = false;

        for (Record record : newValues) {
            if (record.getRow() != null) {
                if (record.isDeleted() == true) {
                    data.removeRow(record.getRow());
                    changed = true;
                } else {
                    changed |= record.updateRow();
                }

            } else {
                if (record.isDeleted() == false) {
                    EcvTableRow newRow = data.createRow();
                    record.fillRow(data, newRow);
                    data.addRow(newRow);
                    changed = true;
                }
            }
        }

        return (changed);
    }

}
