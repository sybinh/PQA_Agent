/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Types.Rq1XmlTable_Milestones;
import Rq1Data.Enumerations.ExportCategory;
import Rq1Data.Enumerations.ExportScope;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import util.EcvDate;
import util.EcvTableColumn_ComboBox;
import util.EcvTableColumn_Date;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * Implements the data model representation of the RQ1 milestone field.
 * <br>
 * The main functionality is the conversion of the name between the format in
 * RQ1 and the format shown to the user.
 *
 * @author GUG2WI
 */
public class DmRq1Field_MilestoneTable extends DmRq1Field_TableDescription {

    final public EcvTableColumn_String NAME;
    final public EcvTableColumn_Date DATE;
    final public EcvTableColumn_ComboBox CATEGORY;
    final public EcvTableColumn_ComboBox SCOPE;

    final private Rq1XmlSubField_Table<Rq1XmlTable_Milestones> rq1MilestoneField;

    public DmRq1Field_MilestoneTable(Rq1XmlSubField_Table<Rq1XmlTable_Milestones> rq1MilestoneField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (rq1MilestoneField != null);
        assert (nameForUserInterface != null);
        assert (nameForUserInterface.isEmpty() == false);

        this.rq1MilestoneField = rq1MilestoneField;

        addIpeColumn(NAME = new EcvTableColumn_String("Name", 40));
        addIpeColumn(DATE = new EcvTableColumn_Date("Date", 30));
        addIpeColumn(CATEGORY = new EcvTableColumn_ComboBox("Category", ExportCategory.values()));
        addIpeColumn(SCOPE = new EcvTableColumn_ComboBox("Scope", ExportScope.values()));
    }

    @Override
    public synchronized EcvTableData getValue() {

        EcvTableData rq1Data = rq1MilestoneField.getDataModelValue();
        Rq1XmlTable_Milestones rq1Table = (Rq1XmlTable_Milestones) rq1Data.getDescription();

        EcvTableData newDmData = createTableData();
        for (EcvTableRow rq1Row : rq1Data.getRows()) {
            EcvTableRow dmRow = newDmData.createAndAddRow();
            String name = rq1Table.MILESTONE_NAME.getValue(rq1Row);
            if ((name == null) || (name.isEmpty())) {
                name = rq1Table.TAG_NAME.getValue(rq1Row);
            }
            NAME.setValue(dmRow, name);
            DATE.setValue(dmRow, rq1Table.DATE.getValue(rq1Row));
            CATEGORY.setValue(dmRow, rq1Table.EXPORT_CATEGORY.getValue(rq1Row));
            SCOPE.setValue(dmRow, rq1Table.EXPORT_SCOPE.getValue(rq1Row));
        }

        return (newDmData);
    }

    @Override
    public synchronized void setValue(EcvTableData newData) {
        assert (newData != null);

        Rq1XmlTable_Milestones rq1Table = new Rq1XmlTable_Milestones();
        EcvTableData rq1Data = rq1Table.createTableData();
        DmRq1Field_MilestoneTable dmTable = (DmRq1Field_MilestoneTable) newData.getDescription();

        for (EcvTableRow dmRow : newData.getRows()) {
            String name = dmTable.NAME.getValue(dmRow);
            if ((name != null) && (name.isEmpty() == false)) {
                EcvTableRow rq1Row = rq1Data.createAndAddRow();
                String tagName = extractTagName(dmTable.NAME.getValue(dmRow));
                rq1Table.TAG_NAME.setValue(rq1Row, tagName);
                if (name.equals(tagName) == false) {
                    rq1Table.MILESTONE_NAME.setValue(rq1Row, name);
                }
                rq1Table.DATE.setValue(rq1Row, dmTable.DATE.getValue(dmRow));
                rq1Table.EXPORT_CATEGORY.setValue(rq1Row, dmTable.CATEGORY.getValue(dmRow));
                rq1Table.EXPORT_SCOPE.setValue(rq1Row, dmTable.SCOPE.getValue(dmRow));
            }
        }

        rq1MilestoneField.setDataModelValue(rq1Data);
    }

    final private static String tagAllowedFirstCharacter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
    final private static String tagAllowedOtherCharacter = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-0123456789";

    static String extractTagName(String milestoneName) {
        assert (milestoneName != null);
        assert (milestoneName.isEmpty() == false);

        String trimmedMilestoneName = milestoneName.trim();

        //
        // Remove invalid start sequences
        //
        boolean startSequencePositiveChecked = false;
        while ((startSequencePositiveChecked == false) && (trimmedMilestoneName.isEmpty() == false)) {

            if (trimmedMilestoneName.toLowerCase().startsWith("xml") == true) {

                trimmedMilestoneName = trimmedMilestoneName.substring(3, trimmedMilestoneName.length());

            } else if (Character.isDigit(trimmedMilestoneName.charAt(0)) == true) {

                trimmedMilestoneName = trimmedMilestoneName.substring(1, trimmedMilestoneName.length());

            } else {
                startSequencePositiveChecked = true;
            }
        }

        //
        // Build tag name from remaining valid characters
        //
        String tagName = "";
        for (int i = 0; i < trimmedMilestoneName.length(); i++) {
            char c = trimmedMilestoneName.charAt(i);
            if (i == 0) {
                if (tagAllowedFirstCharacter.indexOf(c) >= 0) {
                    tagName += c;
                }
            } else {
                if (tagAllowedOtherCharacter.indexOf(c) >= 0) {
                    tagName += c;
                }
            }
        }

        //
        // Use default tag name if name does not contain any valid character.
        //
        if (tagName.isEmpty() == true) {
            tagName = "Milestone";
        }

        return (tagName);
    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }

    @Override
    public boolean isReadOnly() {
        return (rq1MilestoneField.isReadOnly());
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    static public class Record {

        final private String name;
        final private EcvDate date;
        final private ExportCategory exportCategory;
        final private ExportScope exportScope;

        @SuppressWarnings("unchecked")
        private Record(EcvTableData data, EcvTableRow row) {
            assert (data != null);
            assert (row != null);

            DmRq1Field_MilestoneTable d = (DmRq1Field_MilestoneTable) data.getDescription();

            name = d.NAME.getValue(row);
            date = d.DATE.getValue(row);
            exportCategory = ExportCategory.getValueOf(d.CATEGORY.getValue(row));
            exportScope = ExportScope.getValueOf(d.SCOPE.getValue(row));
        }

        public Record(String name, EcvDate date, ExportCategory category, ExportScope scope) {
            assert (name != null);
            assert (name.isEmpty() == false);
            assert (date != null);

            this.name = name;
            this.date = date;
            this.exportCategory = category;
            this.exportScope = scope;
        }

        public String getName() {
            return name;
        }

        public EcvDate getDate() {
            return date;
        }

        public ExportCategory getExportCategory() {
            return exportCategory;
        }

        public ExportScope getExportScope() {
            return exportScope;
        }

        @Override
        public String toString() {
            String s = name != null ? name : null;
            s += ",";
            s += date != null ? date.getXmlValue() : null;
            return (s);
        }

    }

    public List<Record> getValueAsList() {
        EcvTableData data = getValue();
        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            result.add(new Record(data, row));
        }
        return (result);
    }

    public void setValueAsList(List<Record> recordList) {
        assert (recordList != null);

        EcvTableData data = createTableData();
        for (Record record : recordList) {
            EcvTableRow row = data.createAndAddRow();
            NAME.setValue(row, record.getName());
            DATE.setValue(row, record.getDate());
            CATEGORY.setValue(row, record.getExportCategory());
            SCOPE.setValue(row, record.getExportScope());
        }
        setValue(data);
    }

    /**
     * Implemented to simplify testing.
     *
     * @param name
     * @param date
     * @param exportCategory
     * @param exportScope
     */
    public void addRecord(String name, EcvDate date, ExportCategory exportCategory, ExportScope exportScope) {
        List<Record> list = getValueAsList();
        list.add(new Record(name, date, exportCategory, exportScope));
        setValueAsList(list);
    }

    public void removeRecord(String name) {
        List<Record> list = getValueAsList();
        for (ListIterator<Record> it = list.listIterator(); it.hasNext();) {
            Record r = it.next();
            if (r.getName().equals(name)) {
                it.remove();
            }
        }
        setValueAsList(list);
    }

}
