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

import Rq1Data.MetaData.Rq1MetadataDateLessThanOrEqualExpression;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * A table to store the metric types defined for the BBM SW metric.
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_SwMetricsTypes extends Rq1XmlTable {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Rq1XmlTable_SwMetricsTypes.class.getCanonicalName());

    final private Rq1XmlTableColumn_String TYPE_ID; // The internal id of the type;
    final private Rq1XmlTableColumn_String ORDER; // A value to sort the type.
    final private Rq1XmlTableColumn_String NAME; // The name of the type shown on the UI.
    final private Rq1XmlTableColumn_String DESCRIPTION; // A description for the type.
    final private Rq1XmlTableColumn_String UNIT; // The unit of the measurement type.
    final private Rq1XmlTableColumn_String MIN; // The minimal value allowed  for the type.
    final private Rq1XmlTableColumn_String MAX; // The maxumim value allowed for the type.

    public Rq1XmlTable_SwMetricsTypes() {
        addXmlColumn(TYPE_ID = new Rq1XmlTableColumn_String("Id", 10, "id", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(ORDER = new Rq1XmlTableColumn_String("Order", 10, "order", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 10, "name", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(DESCRIPTION = new Rq1XmlTableColumn_String("Description", 10, "description", ColumnEncodingMethod.ATTRIBUTE));
        DESCRIPTION.setOptional();
        addXmlColumn(UNIT = new Rq1XmlTableColumn_String("Unit", 10, "unit", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(MIN = new Rq1XmlTableColumn_String("min", 10, "min", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(MAX = new Rq1XmlTableColumn_String("max", 10, "max", ColumnEncodingMethod.ATTRIBUTE));
        MIN.setOptional();
        MAX.setOptional();
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    public static class Type implements Comparable<Type> {

        final private String id;
        final private int order;
        final private String name;
        final private String description;
        final private String unit;
        final private Integer minValue;
        final private Integer maxValue;

        public Type(String id, int order, String name, String description, String unit, Integer minValue, Integer maxValue) {
            assert (id != null);
            assert (id.isEmpty() == false);
            assert (name != null);
            assert (name.isEmpty() == false);
            assert (unit != null);
            assert (unit.isEmpty() == false);

            this.id = id;
            this.order = order;
            this.name = name;
            this.description = description;
            this.unit = unit;
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        public String getTypeId() {
            return id;
        }

        public int getOrder() {
            return order;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return (description);
        }

        public String getUnit() {
            return unit;
        }

        public String getNameAndUnit() {
            return (name + " [" + unit + "]");
        }

        public Integer getMinValue() {
            return minValue;
        }

        public Integer getMaxValue() {
            return maxValue;
        }

        @Override
        public int compareTo(Type t) {
            return (id.compareTo(t.id));
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Type) {
                Type t = (Type) o;
                if (id.equals(t.id)) {
                    return (true);
                }
            }
            return (false);
        }

        @Override
        public String toString() {
            return (name + "-" + id + "-" + order + "-" + unit);
        }
    }

    public List<Type> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_SwMetricsTypes);

        List<Type> result = new ArrayList<>();

        for (EcvTableRow row : data.getRows()) {

            String id = TYPE_ID.getValueNotNull(row);
            int order = parse(ORDER.getValue(row));
            String name = NAME.getValueNotNull(row);
            String description = DESCRIPTION.getValueNotNull(row);
            String unit = UNIT.getValueNotNull(row);
            String m = MIN.getValue(row);
            Integer minValue = parse(m);
            Integer maxValue = parse(MAX.getValue(row));

            result.add(new Type(id, order, name, description, unit, minValue, maxValue));
        }
        return (result);
    }

    static private Integer parse(String value) {
        if (value == null) {
            return (null);
        }
        try {
            return (Integer.parseInt(value));
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, "Cannot parse as integer: >" + value + "<", ex);
            ToolUsageLogger.logError(Rq1XmlTable_SwMetricsTypes.class.getCanonicalName(), ex);
            return (null);
        }

    }

}
