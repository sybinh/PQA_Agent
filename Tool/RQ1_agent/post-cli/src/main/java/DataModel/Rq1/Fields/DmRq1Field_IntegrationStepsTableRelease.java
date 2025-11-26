/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmField;
import DataModel.DmFieldI;
import DataModel.Rq1.Types.DmRq1Table_IntegrationSteps;
import util.EcvDate;
import util.EcvTableData;
import util.EcvTableDescription;
import DataModel.UiSupport.DmUiTableSource;
import Rq1Data.Enumerations.IntegrationStep;
import java.util.Map;
import java.util.TreeMap;
import util.EcvTableRow;
import DataModel.DmValueFieldI;
import DataModel.Rq1.Records.DmRq1SoftwareRelease;
import Rq1Cache.Types.Rq1XmlTable_IntegrationSteps;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_IntegrationStepsTableRelease extends DmField implements DmValueFieldI<EcvTableData>, DmUiTableSource {

    final private Map<IntegrationStep, DmRq1Field_Date> integrationStepMilestones;
    final private DmRq1Table_IntegrationSteps table;
    final private DmRq1Field_Table<Rq1XmlTable_IntegrationSteps> integrationStepTags;

    public DmRq1Field_IntegrationStepsTableRelease(DmRq1SoftwareRelease parent,
            Map<IntegrationStep, DmRq1Field_Date> integrationStepMilestones,
            DmRq1Field_Table<Rq1XmlTable_IntegrationSteps> integrationStepTags,
            String nameForUserInterface) {
        super(nameForUserInterface);
        assert (integrationStepMilestones != null);
        assert (integrationStepMilestones.values().contains(null) == false);
        assert (integrationStepTags != null);

        this.integrationStepMilestones = integrationStepMilestones;
        this.integrationStepTags = integrationStepTags;
        this.table = new DmRq1Table_IntegrationSteps();
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData tableData = table.createTableData();
        Map<String, IntegrationStepRecord> recordMap = new TreeMap<>();
        for (Map.Entry<IntegrationStep, DmRq1Field_Date> step : integrationStepMilestones.entrySet()) {
            DmRq1Field_Date field = step.getValue();
            if ((field.getValue() != null) && (field.getValue().isEmpty() == false)) {
                recordMap.put(step.getKey().getShortName(), new IntegrationStepRecord(field.getDate(), "", ""));
            }
        }
        for (EcvTableRow row : integrationStepTags.getValue().getRows()) {
            String id = (String) integrationStepTags.getTableDescription().ID.getValue(row);
            String name = (String) integrationStepTags.getTableDescription().NAME.getValue(row);
            String comment = (String) integrationStepTags.getTableDescription().COMMENT.getValue(row);
            if (recordMap.containsKey(IntegrationStep.getShortNameByFieldName(id))) {
                IntegrationStepRecord record = recordMap.get(IntegrationStep.getShortNameByFieldName(id));
                record.NAME = name;
                record.COMMENT = comment;
            } else {
                recordMap.put(IntegrationStep.getShortNameByFieldName(id), new IntegrationStepRecord(EcvDate.getEmpty(), name, comment));
            }
        }
        for (String key : recordMap.keySet()) {
            EcvTableRow newRow = tableData.createAndAddRow();
            IntegrationStepRecord record = recordMap.get(key);
            table.STEP.setValue(newRow, key);
            table.NAME.setValue(newRow, record.NAME);
            table.COMMENT.setValue(newRow, record.COMMENT);
            table.DATE.setValue(newRow, record.DATE);
        }
        return (tableData);
    }

    @Override
    public String getValueAsText() {
        return ("");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(EcvTableData value) {
        Map<String, IntegrationStepRecord> recordMap = new TreeMap<>();
        List<IntegrationStepRecord> unknownIntegrationSteps = new ArrayList();
        value.getRows().forEach((row) -> {
            IntegrationStepRecord record = new IntegrationStepRecord(
                    (EcvDate) row.getValueAt(table.DATE),
                    (String) row.getValueAt(table.NAME),
                    (String) row.getValueAt(table.COMMENT));
            if (row.getValueAt(table.STEP) != null) {
                recordMap.put((String) row.getValueAt(table.STEP), record);
            } else {
                //The Inegration Step is not known at this point we must process the rest of the records to know what is the next available step           
                unknownIntegrationSteps.add(record);
            }
        });

        for (IntegrationStepRecord unknownStep : unknownIntegrationSteps) {
            for (IntegrationStep step : IntegrationStep.values()) {
                if (step != IntegrationStep.EMPTY && step != IntegrationStep.FINAL && step != IntegrationStep.INTERMEDIATE && !recordMap.keySet().contains(step.getShortName())) {
                    recordMap.put(step.getShortName(), unknownStep);
                    //We found the first IntegrationStep without a name so we can stop searching
                    break;
                } else {
                    //This IntegrationStep is already defined find so we continue looking for the next available step
                }
            }
            //If there is still no IntegrationStep then all Integration Steps are defined so we just ignore the record
        }

        for (Map.Entry<IntegrationStep, DmRq1Field_Date> entry : integrationStepMilestones.entrySet()) {
            IntegrationStepRecord record = recordMap.get(entry.getKey().getShortName());
            if (record != null && record.DATE != null) {
                entry.getValue().setValue(record.DATE);
            } else {
                entry.getValue().setValue(EcvDate.getEmpty());
            }
        }

        EcvTableData d = integrationStepTags.getTableDescription().createTableData();
        recordMap.keySet().forEach((step) -> {
            EcvTableRow row = d.createAndAddRow();
            integrationStepTags.getTableDescription().ID.setValue(row, IntegrationStep.getFieldNameByShortName(step));
            integrationStepTags.getTableDescription().NAME.setValue(row, recordMap.get(step).NAME);
            integrationStepTags.getTableDescription().COMMENT.setValue(row, recordMap.get(step).COMMENT);
        });
        integrationStepTags.setValue(d);
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (table);
    }

    @Override
    public boolean isReadOnly() {
        return (false);
    }

    @Override
    public DmFieldI getDmField() {
        return (this);
    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }

    public static class IntegrationStepRecord {

        private EcvDate DATE;
        private String NAME;
        private String COMMENT;

        IntegrationStepRecord(EcvDate date, String name, String comment) {
            this.DATE = date;
            this.NAME = name;
            this.COMMENT = comment;
        }

        public String getName() {
            return NAME;
        }

        public String getComment() {
            return COMMENT;
        }
    }

    public static List<DmRq1Field_IntegrationStepsTableRelease.IntegrationStepRecord> extract(EcvTableData data) {
        assert (data != null);
        List<DmRq1Field_IntegrationStepsTableRelease.IntegrationStepRecord> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            EcvDate date = (EcvDate) row.getValueAt(2);
            String name = (String) row.getValueAt(1);
            String comment = (String) row.getValueAt(3);
            if ("".equals(name)) {
                name = IntegrationStep.getTextByShortName((String) row.getValueAt(0));
            }

            result.add(new DmRq1Field_IntegrationStepsTableRelease.IntegrationStepRecord(date, name, comment));
        }
        return result;
    }
}
