/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmField;
import DataModel.DmFieldI;
import DataModel.DmValueFieldI;
import DataModel.Rq1.Records.DmRq1SoftwareRelease;
import DataModel.Rq1.Types.DmRq1Table_IntegrationStepsRelease;
import DataModel.Rq1.Types.DmRq1Table_IntegrationStepsRelease.Record;
import DataModel.UiSupport.DmUiTableSource;
import Rq1Cache.Types.Rq1XmlTable_IntegrationSteps;
import Rq1Data.Enumerations.IntegrationStep;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import util.EcvDate;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;

/**
 * Implements the source for the table showing the integration steps on a
 * release.
 *
 * @author gug2wi
 */
public class DmRq1Field_IntegrationStepsRelease extends DmField implements DmValueFieldI<EcvTableData>, DmUiTableSource {

    final static private DmRq1Table_IntegrationStepsRelease DESC = new DmRq1Table_IntegrationStepsRelease();

    final private Map<IntegrationStep, DmRq1Field_Date> integrationStepMilestones;

    final private DmRq1Field_Table<Rq1XmlTable_IntegrationSteps> integrationStepTags;

    public DmRq1Field_IntegrationStepsRelease(DmRq1SoftwareRelease parent,
            Map<IntegrationStep, DmRq1Field_Date> integrationStepMilestones,
            DmRq1Field_Table<Rq1XmlTable_IntegrationSteps> integrationStepTags,
            String nameForUserInterface) {
        super(nameForUserInterface);
        assert (integrationStepMilestones != null);
        assert (integrationStepMilestones.values().contains(null) == false);
        assert (integrationStepTags != null);

        this.integrationStepMilestones = integrationStepMilestones;
        this.integrationStepTags = integrationStepTags;

    }

    private Map<String, Record> getValueAsRecordMap() {
        Map<String, Record> recordMap = new TreeMap<>();

        //
        // Add data from milestones field.
        //
        for (Map.Entry<IntegrationStep, DmRq1Field_Date> step : integrationStepMilestones.entrySet()) {
            DmRq1Field_Date field = step.getValue();
            if ((field.getValue() != null) && (field.getValue().isEmpty() == false)) {
                String id = step.getKey().getShortName();
                recordMap.put(id, new Record(id, field.getDate(), null, null));
            }
        }

        //
        // Add data from tags field.
        //
        Rq1XmlTable_IntegrationSteps d = integrationStepTags.getTableDescription();
        for (EcvTableRow row : integrationStepTags.getValue().getRows()) {
            String fieldName = d.FIELDNAME.getValue(row);
            String name = d.NAME.getValue(row);
            String comment = d.COMMENT.getValue(row);
            String id = IntegrationStep.getShortNameByMilestoneName(fieldName);
            Record record = recordMap.get(id);
            if (record != null) {
                record.addGivenNameAndComment(name, comment);
            } else {
                record = new Record(id, null, name, comment);
                recordMap.put(id, record);
            }
        }

        return (recordMap);
    }

    public Collection<Record> getValueAsRecords() {
        return (getValueAsRecordMap().values());
    }

    /**
     * Gives the Integration step long name from given name in Integration step
     * table or tags.
     *
     * @param givenName is the name for specified integration step.
     * @return long name of the Integration step
     */
    public String getStepLongNameByGivenName(String givenName) {
        for (Record record : getValueAsRecords()) {
            if ((record.getGivenName() != null && record.getGivenName().equals(givenName))) {
                givenName = IntegrationStep.getLongNameByShortName(record.getShortName());
            }
        }
        return givenName;
    }

    @Override
    public EcvTableData getValue() {

        //
        // Build table data from record map
        //
        EcvTableData tableData = DESC.createTableData();
        for (Record record : getValueAsRecords()) {
            EcvTableRow newRow = tableData.createAndAddRow();

            DESC.ID.setValue(newRow, record.getShortName());
            DESC.NAME.setValue(newRow, record.getGivenName());
            DESC.COMMENT.setValue(newRow, record.getComment());
            DESC.DATE.setValue(newRow, record.getDate());
        }

        return (tableData);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(EcvTableData value) {

        Map<IntegrationStep, Record> recordMap = new TreeMap<>();
        List<Record> recordsWithoutId = new ArrayList();

        for (EcvTableRow row : value.getRows()) {
            String id = DESC.ID.getValue(row);
            EcvDate date = DESC.DATE.getValue(row);
            String name = DESC.NAME.getValue(row);
            String comment = DESC.COMMENT.getValue(row);
            Record record = new Record(id, date, name, comment);

            IntegrationStep step = IntegrationStep.getByShortName(id);
            if (step != null) {
                recordMap.put(step, record);
            } else {
                recordsWithoutId.add(record);
            }
        };

        for (Record recordWithoutId : recordsWithoutId) {
            for (IntegrationStep step : IntegrationStep.values()) {
                if (step != IntegrationStep.EMPTY && step != IntegrationStep.FINAL && step != IntegrationStep.INTERMEDIATE && (recordMap.get(step) == null)) {
                    recordMap.put(step, recordWithoutId);
                    //We found the first IntegrationStep without a name so we can stop searching
                    break;
                } else {
                    //This IntegrationStep is already defined find so we continue looking for the next available step
                }
            }
            //If there is still no IntegrationStep then all Integration Steps are defined so we just ignore the record
        }

        for (Map.Entry<IntegrationStep, DmRq1Field_Date> entry : integrationStepMilestones.entrySet()) {
            Record record = recordMap.get(entry.getKey());
            if (record != null && record.getDate() != null) {
                entry.getValue().setValue(record.getDate());
            } else {
                entry.getValue().setValue(EcvDate.getEmpty());
            }
        }

        Rq1XmlTable_IntegrationSteps isDesc = integrationStepTags.getTableDescription();
        EcvTableData isData = isDesc.createTableData();
        for (Map.Entry<IntegrationStep, Record> entry : recordMap.entrySet()) {
            if ((entry.getValue().getGivenName() != null) || (entry.getValue().getComment() != null)) {
                EcvTableRow row = isData.createAndAddRow();
                isDesc.FIELDNAME.setValue(row, entry.getKey().getMilestoneName());
                isDesc.NAME.setValue(row, entry.getValue().getGivenName());
                isDesc.COMMENT.setValue(row, entry.getValue().getComment());
            }

        };
        integrationStepTags.setValue(isData);
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (DESC);
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

}
