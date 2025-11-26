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
import java.util.Objects;
import java.util.logging.Level;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * A table to stored the milestones defined for the BBM SW metric.
 * @author GUG2WI
 */
public class Rq1XmlTable_SwMetricsMilestones extends Rq1XmlTable {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Rq1XmlTable_SwMetricsMilestones.class.getCanonicalName());

    final private Rq1XmlTableColumn_String ID; // The internal id of the milestone.
    final private Rq1XmlTableColumn_String ORDER; // A value to sort the milestones.
    final private Rq1XmlTableColumn_String NAME; // The name of the milestone shown on the UI.

    public Rq1XmlTable_SwMetricsMilestones() {
        addXmlColumn(ID = new Rq1XmlTableColumn_String("Id", 10, "id", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(ORDER = new Rq1XmlTableColumn_String("Order", 10, "order", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 10, "name", ColumnEncodingMethod.ATTRIBUTE));
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    public static class Milestone implements Comparable<Milestone> {

        final private String id;
        final private int order;
        final private String name;

        public Milestone(String id, int order, String name) {
            assert (id != null);
            assert (id.isEmpty() == false);
            assert (order >= 0);
            assert (name != null);
            assert (name.isEmpty() == false);

            this.id = id;
            this.order = order;
            this.name = name;
        }

        public String getMilestoneId() {
            return id;
        }

        public int getOrder() {
            return order;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Milestone) {
                Milestone r = (Milestone) o;
                if (r.order != order) {
                    return (false);
                } else {
                    return (r.id.equals(id));
                }
            } else {
                return (false);
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.id);
            hash = 17 * hash + this.order;
            return hash;
        }

        @Override
        public int compareTo(Milestone r) {
            if (order != r.order) {
                return (order - r.order);
            } else {
                return (id.compareTo(r.id));
            }
        }

        public String toString() {
            return (name + "-" + id + "-" + order);
        }
    }

    static public List<Milestone> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_SwMetricsMilestones);

        Rq1XmlTable_SwMetricsMilestones d = (Rq1XmlTable_SwMetricsMilestones) data.getDescription();
        List<Milestone> result = new ArrayList<>();

        for (EcvTableRow row : data.getRows()) {

            String id = d.ID.getValueNotNull(row);
            int order = parse(d.ORDER.getValue(row));
            String name = d.NAME.getValueNotNull(row);

            result.add(new Rq1XmlTable_SwMetricsMilestones.Milestone(id, order, name));
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
            ToolUsageLogger.logError(Rq1XmlTable_SwMetricsMilestones.class.getCanonicalName(), ex);
            return (null);
        }

    }

}
