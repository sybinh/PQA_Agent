/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmField;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import Rq1Data.GPM.GpmXmlTable_DataOfExternalMilestones;
import Rq1Data.GPM.GpmXmlTable_DataOfExternalMilestones.Record;
import java.util.ArrayList;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class DmGpmFieldOnProject_DataOfExternalMilestones extends DmField {

    final public Rq1XmlSubField_Table<GpmXmlTable_DataOfExternalMilestones> rq1Field;

    public DmGpmFieldOnProject_DataOfExternalMilestones(Rq1XmlSubField_Table<GpmXmlTable_DataOfExternalMilestones> rq1Field, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (rq1Field != null);

        this.rq1Field = rq1Field;
    }

    @Override
    public boolean isReadOnly() {
        return (rq1Field.isReadOnly());
    }

    public List<Record> getCachedMilestones(GpmXmlMilestoneSource source) {
        assert (source != null);

        ArrayList<Record> result = new ArrayList<Record>();
        List<Record> records = rq1Field.getTable().extract(rq1Field.getDataModelValue());
        for (Record record : records) {
            if (source == record.getSource()) {
                result.add(record);
            }
        }
        return (result);
    }

    /**
     * Searches for the last confirmed date. The key for the search is the
     * milestoneId.
     *
     * Note that in older implementation for HIS, the name was part of the id.
     * To keep compatible with earlier written data, the old format has to be
     * provided for the search.
     *
     * @param source
     * @param id
     * @param oldId
     * @return
     */
    public EcvDate getLastConfirmedDate(GpmXmlMilestoneSource source, String id, String oldId) {
        assert (source != null);
        assert (id != null);
        assert (id.isEmpty() == false);

        List<Record> records = rq1Field.getTable().extract(rq1Field.getDataModelValue());
        for (Record record : records) {
            if (source == record.getSource()) {
                if (id.equals(record.getId()) == true) {
                    return ((record.getLastConfirmedDate() != null) ? record.getLastConfirmedDate() : EcvDate.getEmpty());
                } else if ((oldId != null) && (oldId.equals(record.getId()) == true)) {
                    return ((record.getLastConfirmedDate() != null) ? record.getLastConfirmedDate() : EcvDate.getEmpty());
                }
            }
        }

        return (null);
    }

    public void confirmDate(GpmXmlMilestoneSource source, String id, String oldId, String type, EcvDate newDate) {
        assert (source != null);
        assert (id != null);
        assert (id.isEmpty() == false);
        assert (type != null);
        assert (type.isEmpty() == false);

        List<Record> oldRecords = rq1Field.getTable().extract(rq1Field.getDataModelValue());
        List<Record> newRecords = new ArrayList<>();

        // Copy non matching records
        for (Record oldRecord : oldRecords) {
            boolean oldIdMatch = (oldId != null) && (oldId.equals(oldRecord.getId()));
            if ((source != oldRecord.getSource()) || ((id.equals(oldRecord.getId()) == false) && (oldIdMatch == false))) {
                newRecords.add(oldRecord);
            }
        }

        newRecords.add(new Record(source, id, type, newDate));

        rq1Field.setDataModelValue(rq1Field.getTable().pack(newRecords));
    }

}