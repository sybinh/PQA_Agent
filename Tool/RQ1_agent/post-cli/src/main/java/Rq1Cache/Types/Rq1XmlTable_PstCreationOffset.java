/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Types.DmRq1Table_PstCreationOffset.DmPstCreationOffsetDefault;
import java.util.ArrayList;
import java.util.List;
import util.EcvEnumeration;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author hfi5wi
 */
public class Rq1XmlTable_PstCreationOffset extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String DATE_CALC_NAME;
    final public Rq1XmlTableColumn_Number DATE_CALC_VALUE;
    final public Rq1XmlTableColumn_ComboBox DATE_CALC_UNIT;

    public Rq1XmlTable_PstCreationOffset() {
        addXmlColumn(DATE_CALC_NAME = new Rq1XmlTableColumn_String("Name", 27, "name", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(DATE_CALC_VALUE = new Rq1XmlTableColumn_Number("Value", 8, "value", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(DATE_CALC_UNIT = new Rq1XmlTableColumn_ComboBox("Unit", 8, PstCreationOffsetUnit.values(), "unit", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        DATE_CALC_NAME.setReadOnly();
        setCreateAutoSorter(false);
    }

    //--------------------------------------------------------------------------
    //  DmPstCreationOffset 
    //--------------------------------------------------------------------------
    public enum PstCreationOffsetUnit implements EcvEnumeration {
        DAY("Day"),
        WEEK("Week");

        private final String pstCreationOffsetUnit;

        private PstCreationOffsetUnit(String pstCreationOffsetUnit) {
            assert (pstCreationOffsetUnit != null);
            this.pstCreationOffsetUnit = pstCreationOffsetUnit;
        }

        public static PstCreationOffsetUnit getByString(String unitString) {
            if (unitString != null) {
                for (PstCreationOffsetUnit unit : values()) {
                    if (unit.pstCreationOffsetUnit.equals(unitString)) {
                        return (unit);
                    }
                }
            }
            return (null);
        }

        @Override
        public String getText() {
            return pstCreationOffsetUnit;
        }
    }

    public static class PstCreationOffset {

        private final String name;
        private final String value;
        private final PstCreationOffsetUnit unit;
        
        public PstCreationOffset(String name, String value, PstCreationOffsetUnit unit) {
            assert (name != null);
            this.name = name;
            this.value = value;
            this.unit = unit;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
        
        public String getValue(DmRq1Project project) {
            return value;
        }
        
        public PstCreationOffsetUnit getUnit() {
            return unit;
        }
    }

    static public List<PstCreationOffset> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_PstCreationOffset);

        Rq1XmlTable_PstCreationOffset d = (Rq1XmlTable_PstCreationOffset) data.getDescription();

        List<PstCreationOffset> result = new ArrayList<>(0);
        data.getRows().forEach((row) -> {
            String date_calc_name = d.DATE_CALC_NAME.getValue(row);
            String date_calc_value = d.DATE_CALC_VALUE.getValue(row);
            PstCreationOffsetUnit date_calc_unit = PstCreationOffsetUnit.getByString(d.DATE_CALC_UNIT.getValue(row));
            result.add(new PstCreationOffset(date_calc_name, date_calc_value, date_calc_unit));
        });
        return (result);
    }

    public EcvTableData pack(List<PstCreationOffset> values) {
        assert (values != null);

        EcvTableData result = createTableData();
        for (PstCreationOffset value : values) {
            if (value != null) {
                EcvTableRow row = result.createAndAddRow();
                DATE_CALC_NAME.setValue(row, value.getName());
                if (value.getValue().equals("-")) {
                    DATE_CALC_VALUE.setValue(row, "");
                } else {
                    DATE_CALC_VALUE.setValue(row, value.getValue());
                }
                if (value.getUnit() != null) {
                    DATE_CALC_UNIT.setValue(row, value.getUnit().getText());
                } else {
                    DATE_CALC_UNIT.setValue(row, DmPstCreationOffsetDefault.getByOffsetName(value.getName()).getUnit().getText());
                }
            }
        }
        return (result);
    }

}
