/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmField;
import DataModel.DmValueFieldI;
import DataModel.Rq1.Records.DmRq1IpeToolProject;
import DataModel.Rq1.Types.DmRq1SoftwareMetrics;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsMilestones.Milestone;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsTypes.Type;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsValues;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsValues.Value;
import java.util.ArrayList;
import java.util.List;
import util.EcvMapMap;
import util.EcvTableData;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_SoftwareMetrics extends DmField implements DmValueFieldI<DmRq1SoftwareMetrics> {

    private final Rq1XmlSubField_Table<Rq1XmlTable_SwMetricsValues> rq1SoftwareMetricField;
    private List<Value> hiddenValues = null; // Used to save values from the database which are not configured.

    public DmRq1Field_SoftwareMetrics(Rq1XmlSubField_Table<Rq1XmlTable_SwMetricsValues> rq1SoftwareMetricField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (rq1SoftwareMetricField != null);
        this.rq1SoftwareMetricField = rq1SoftwareMetricField;
    }

    @Override
    public DmRq1SoftwareMetrics getValue() {

        EcvTableData data = rq1SoftwareMetricField.getDataModelValue();
        List<Value> values = rq1SoftwareMetricField.getTable().extract(data);
        List<Value> hiddenValues = new ArrayList<>();

        List<Milestone> milestones = DmRq1IpeToolProject.getSoftwareMetricMilestones();
        List<Type> types = DmRq1IpeToolProject.getSoftwareMetricTypes();

        DmRq1SoftwareMetrics result = new DmRq1SoftwareMetrics();

        //
        // Add values to result set.
        // Note: values for unknown milestones or types are ignored.
        //
        for (Value value : values) {
            boolean found = false;
            for (Milestone milestone : milestones) {
                if (milestone.getMilestoneId().equals(value.getMilestoneId()) == true) {
                    for (Type type : types) {
                        if (type.getTypeId().equals(value.getTypeId()) == true) {
                            result.put(milestone, type, value.getValue());
                            found = true;
                            continue;
                        }
                    }
                    continue;
                }
            }
            if (found == false) {
                hiddenValues.add(value);
            }
        }

        return (result);
    }

//    @Override
//    public String getValueAsText() {
//        throw (new Error("not yet"));
//    }

    @Override
    public void setValue(DmRq1SoftwareMetrics metrics) {

        List<Value> result = new ArrayList<>();
        for (EcvMapMap.Entry<Milestone, Type, Integer> entry : metrics.getEntrySet()) {
            if (entry.getValue() != null) {
                result.add(new Value(entry.getKey1().getMilestoneId(),
                        entry.getKey2().getTypeId(),
                        entry.getValue()));
            }
        }
        if (hiddenValues != null) {
            result.addAll(hiddenValues);
        }

        Rq1XmlTable_SwMetricsValues table = rq1SoftwareMetricField.getTable();
        rq1SoftwareMetricField.setDataModelValue(table.pack(result));
    }

    @Override
    public boolean isReadOnly() {
        return (false);
    }

}
