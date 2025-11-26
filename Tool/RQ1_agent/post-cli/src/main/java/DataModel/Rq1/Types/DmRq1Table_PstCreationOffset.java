/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Types.Rq1XmlTable_PstCreationOffset;
import Rq1Cache.Types.Rq1XmlTable_PstCreationOffset.PstCreationOffset;
import Rq1Cache.Types.Rq1XmlTable_PstCreationOffset.PstCreationOffsetUnit;
import static Rq1Data.Enumerations.DevelopmentMethod.REQUIREMENT_BASED_DEVELOPMENT;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Table_PstCreationOffset extends DmRq1Field_Table<Rq1XmlTable_PstCreationOffset> {

    private static final Logger logger = Logger.getLogger(DmRq1Table_PstCreationOffset.class.getName());

    public DmRq1Table_PstCreationOffset(
            Rq1XmlSubField_Table<Rq1XmlTable_PstCreationOffset> rq1TableField, String nameForUserInterface) {
        super(rq1TableField, nameForUserInterface);
    }

    public enum DmPstCreationOffsetDefault {
        SPECIFICATION_FREEZE("Specification Freeze", "-15", PstCreationOffsetUnit.WEEK, "Specification Freeze (default: -12/-15 weeks)") {
            @Override
            public String getValue(DmRq1Project project) {
                if (project.DEFAULT_DEVELOPMENT_METHOD.getValue() == REQUIREMENT_BASED_DEVELOPMENT) {
                    return ("-15");
                } else {
                    return ("-12");
                }
            }
        },
        PLANING_FREEZE("Planning Freeze", "-6", PstCreationOffsetUnit.WEEK, "Planning Freeze (default: -6 weeks)"),
        DELIVERY_FREEZE("Delivery Freeze", "-5", PstCreationOffsetUnit.WEEK, "Delivery Freeze (default: -5 weeks)"),
        DEFAULT_REQUESTED_DELIVERY_DATE("Default Requested Delivery Date", "-5", PstCreationOffsetUnit.WEEK, "Default Requested Delivery Date (default: -5 weeks)"),
        DELIVERY_TO_CALIBRATION("Delivery To Calibration", "-2", PstCreationOffsetUnit.WEEK, "Delivery To Calibration (default: -2 weeks)"),
        DELAY_FOR_DERIVATIVES("Delay For Derivatives", "1", PstCreationOffsetUnit.WEEK, "Delay For Derivatives (default: 1 week)");

        private final String name;
        private final String value;
        private final PstCreationOffsetUnit unit;
        private final String uiText;

        private DmPstCreationOffsetDefault(String name, String value, PstCreationOffsetUnit unit, String uiText) {
            assert (name != null);
            this.name = name;
            this.uiText = uiText;
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

        public String getUiText() {
            return uiText;
        }

        static public DmPstCreationOffsetDefault getByOffsetName(String offsetName) {
            assert (offsetName != null);

            for (DmPstCreationOffsetDefault value : values()) {
                if (value.name.equals(offsetName)) {
                    return (value);
                }
            }
            logger.log(Level.WARNING, offsetName + " is not a valid parameter for the function getByOffsetName");
            return (null);
        }

        static public String getUiTextForName(String offsetName) {
            if (offsetName != null) {
                for (DmPstCreationOffsetDefault value : values()) {
                    if (value.getName().equals(offsetName)) {
                        return (value.getUiText());
                    }
                }
            }
            return (null);
        }

        static public String getNameForUiText(String uiText) {
            if (uiText != null) {
                for (DmPstCreationOffsetDefault value : values()) {
                    if (value.getUiText().equals(uiText)) {
                        return (value.getName());
                    }
                }
            }
            return (null);
        }
    }

    private List<PstCreationOffset> getValueAsRecordList() {

        //-----------------------------------------------
        // Get pst creation offsets defined in XML field
        //-----------------------------------------------
        EcvTableData dsData = super.getValue();
        List<PstCreationOffset> pstCreationOffsetsFromXmlField
                = Rq1XmlTable_PstCreationOffset.extract(dsData);

        //-----------------------------------------------------------------
        // Add pst creation offsets defined in DmPstCreationOffsetDefault
        //-----------------------------------------------------------------
        List<PstCreationOffset> pstCreationOffsetList = new ArrayList<>(0);

        for (DmPstCreationOffsetDefault defaultOffset : DmPstCreationOffsetDefault.values()) {

            String value = "";
            PstCreationOffsetUnit unit = defaultOffset.getUnit();
            for (PstCreationOffset offsetFromXml : pstCreationOffsetsFromXmlField) {
                if (offsetFromXml.getName().equals(defaultOffset.getName())) {
                    value = offsetFromXml.getValue();
                    unit = offsetFromXml.getUnit();
                }
            }

            pstCreationOffsetList.add(new PstCreationOffset(defaultOffset.getName(), value, unit));
        }

        return (pstCreationOffsetList);
    }

    /**
     * Returns the pst creation offsets defined in XML field plus those hard
     * coded offsets for which no value is assigned in the XML field.
     *
     * @return
     */
    @Override
    public EcvTableData getValue() {

        EcvTableData dsData = super.getValue();
        List<PstCreationOffset> pstCreationOffsetList = getValueAsRecordList();

        EcvTableData dmData = ((Rq1XmlTable_PstCreationOffset) dsData.getDescription())
                .pack(pstCreationOffsetList);

        // replace values in column name with uiText of each offset
        dmData.getRows().forEach((row) -> {
            row.setValueAt(0, DmPstCreationOffsetDefault.getUiTextForName(row.getValueAt(0).toString()));
        });

        return (dmData);
    }

    /**
     * Removes lines containing default value and unit
     *
     * @param uiValue
     */
    @Override
    public void setValue(EcvTableData uiValue) {

        Rq1XmlTable_PstCreationOffset offset = (Rq1XmlTable_PstCreationOffset) uiValue.getDescription();
        EcvTableData dmData = offset.createTableData();
        
        for (EcvTableRow row : uiValue.getRows()) {
            String name = DmPstCreationOffsetDefault.getNameForUiText(offset.DATE_CALC_NAME.getValue(row));
            String value = offset.DATE_CALC_VALUE.getValue(row);
            
            // let user save any value, even the default value
            if (value.isEmpty() == false) {
                EcvTableRow temp_row = row.copy();
                temp_row.setValueAt(0, name);
                dmData.addRow(temp_row);
            }
        }

        super.setValue(dmData);
    }

    public int getCalculatedDaysForOffset(DmPstCreationOffsetDefault defaultOffset) {
        assert (defaultOffset != null);

        for (PstCreationOffset creationOffset : getValueAsRecordList()) {
            if (defaultOffset.getName().equals(creationOffset.getName())) {
                String offsetValue = creationOffset.getValue();
                if (offsetValue.isEmpty()) {
                    offsetValue = defaultOffset.getValue();
                }
                if (creationOffset.getUnit() == PstCreationOffsetUnit.DAY) {
                    return parseToInt(offsetValue);
                } else {
                    return 7 * parseToInt(offsetValue);
                }
            }
        }
        logger.log(Level.SEVERE, defaultOffset.getName() + " is not a valid parameter for the function getCalculatedDaysForOffset");
        throw new Error(defaultOffset.getName() + " is not a valid parameter for the function getCalculatedDaysForOffset");
    }

    public int getCalculatedDaysForOffset(DmPstCreationOffsetDefault defaultOffset, DmRq1Project project) {
        assert (defaultOffset != null);
        assert (project != null);

        for (PstCreationOffset creationOffset : getValueAsRecordList()) {
            if (defaultOffset.getName().equals(creationOffset.getName())) {
                String offsetValue = creationOffset.getValue(project);
                if (offsetValue.isEmpty()) {
                    offsetValue = defaultOffset.getValue(project);
                }
                if (creationOffset.getUnit() == PstCreationOffsetUnit.DAY) {
                    return parseToInt(offsetValue);
                } else {
                    return 7 * parseToInt(offsetValue);
                }
            }
        }
        logger.log(Level.SEVERE, defaultOffset.getName() + " is not a valid parameter for the function getCalculatedDaysForOffset");
        throw new Error(defaultOffset.getName() + " is not a valid parameter for the function getCalculatedDaysForOffset");
    }

    /**
     * parseToInt try to parse the given string into an int. On failure it
     * return 0.
     *
     * @param valueAsString
     * @return
     */
    private static int parseToInt(String valueAsString) {
        try {
            return Integer.parseInt(valueAsString);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

}
