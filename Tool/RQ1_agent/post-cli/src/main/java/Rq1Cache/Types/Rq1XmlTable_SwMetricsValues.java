/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * A table to store the values for sw metrics. Each value belongs to a type and a milestone.
 * @author GUG2WI
 */
public class Rq1XmlTable_SwMetricsValues extends Rq1XmlTable {

    private static final Logger LOGGER = Logger.getLogger(Rq1XmlTable_SwMetricsValues.class.getCanonicalName());

    final private Rq1XmlTableColumn_String TYPE_ID; // Id of the metric type.
    final private Rq1XmlTableColumn_String MILESTONE_ID; // Id of the milestone.
    final private Rq1XmlTableColumn_String VALUE; // Value

    public Rq1XmlTable_SwMetricsValues() {
        addXmlColumn(TYPE_ID = new Rq1XmlTableColumn_String("Type Id", 10, "typeId", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(MILESTONE_ID = new Rq1XmlTableColumn_String("Milestone Id", 10, "milestoneId", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(VALUE = new Rq1XmlTableColumn_String("Value", 10, "value", ColumnEncodingMethod.ATTRIBUTE));
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    public static class Value {

        final private String milestoneId;
        final private String typeId;
        final private Integer value;

        public Value(String milestoneId, String typeId, Integer value) {
            this.milestoneId = milestoneId;
            this.typeId = typeId;
            this.value = value;
        }

        public String getMilestoneId() {
            return milestoneId;
        }

        public String getTypeId() {
            return typeId;
        }

        public Integer getValue() {
            return value;
        }

    }

    public List<Value> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_SwMetricsValues);

        List<Value> result = new ArrayList<>();

        for (EcvTableRow row : data.getRows()) {
            String typeId = TYPE_ID.getValueNotNull(row);
            String milestoneId = MILESTONE_ID.getValue(row);
            Integer value = parse(VALUE.getValue(row));

            result.add(new Value(milestoneId, typeId, value));
        }
        return (result);
    }

    public EcvTableData pack(List<Value> values) {
        assert (values != null);

        EcvTableData result = createTableData();
        for (Value value : values) {
            if (value.value != null) {
                EcvTableRow row = result.createAndAddRow();
                TYPE_ID.setValue(row, value.getTypeId());
                MILESTONE_ID.setValue(row, value.getMilestoneId());
                VALUE.setValue(row, value.getValue().toString());
            }
        }
        return (result);
    }

    private Integer parse(String value) {
        if (value == null) {
            return (null);
        }
        try {
            return (Integer.parseInt(value));
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, "Cannot parse as integer: >" + value + "<", ex);
            ToolUsageLogger.logError(Rq1XmlTable_SwMetricsValues.class.getCanonicalName(), ex);
            return (null);
        }

    }

}
