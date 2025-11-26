/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport extends Rq1XmlTable {

    final static public String ATTRIBUTE_NAME_LINK = "link";
    final static public String ATTRIBUTE_NAME_OBJECT_ID = "objectId";
    final static public String ATTRIBUTE_NAME_ORIGINAL_ID = "originalId";
    final static public String ATTRIBUTE_NAME_ALLOCATION = "allocation";

    final public Rq1XmlTableColumn_String LINK;
    final public Rq1XmlTableColumn_String OBJECT_ID;
    final public Rq1XmlTableColumn_String ORIGINAL_ID;
    final public Rq1XmlTableColumn_String ALLOCATION;

    public Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport() {
        addXmlColumn(LINK = new Rq1XmlTableColumn_String("Link to DOORS", 40, ATTRIBUTE_NAME_LINK, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(OBJECT_ID = new Rq1XmlTableColumn_String("Object ID", 30, ATTRIBUTE_NAME_OBJECT_ID, ColumnEncodingMethod.ATTRIBUTE));
        OBJECT_ID.setOptional();
        addXmlColumn(ORIGINAL_ID = new Rq1XmlTableColumn_String("Original ID", 10, ATTRIBUTE_NAME_ORIGINAL_ID, ColumnEncodingMethod.ATTRIBUTE));
        ORIGINAL_ID.setOptional();
        addXmlColumn(ALLOCATION = new Rq1XmlTableColumn_String("Allocation", 10, ATTRIBUTE_NAME_ALLOCATION, ColumnEncodingMethod.ATTRIBUTE));
        ALLOCATION.setOptional();
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    static public class Record {

        final private String link;
        final private String objectId;
        final private String originalId;
        final private String allocation;

        public Record(String link, String objectId, String originalId, String allocation) {
            assert (link != null);
            assert (link.isEmpty() == false);

            this.link = link;
            this.objectId = objectId;
            this.originalId = originalId;
            this.allocation = allocation;
        }

        public String getLink() {
            return link;
        }

        public String getObjectId() {
            return objectId;
        }

        public String getOriginalId() {
            return originalId;
        }

        public String getAllocation() {
            return allocation;
        }

    }

    static public List<Record> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport);

        Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport d = (Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport) data.getDescription();

        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            String link = d.LINK.getValue(row);
            String objectId = d.OBJECT_ID.getValue(row);
            String originalId = d.ORIGINAL_ID.getValue(row);
            String allocation = d.ALLOCATION.getValue(row);
            result.add(new Record(link, objectId, originalId, allocation));
        }
        return (result);
    }

    static public EcvTableData pack(List<Record> values) {
        assert (values != null);

        Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport d = new Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport();
        EcvTableData result = d.createTableData();
        for (Record value : values) {
            if (value != null) {
                add(result, value);
            }
        }
        return (result);
    }

    static public EcvTableData add(EcvTableData data, Record newRecord) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport);
        assert (newRecord != null);

        Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport d = (Rq1XmlTable_RequirementOnIssue_AdditonalDataForLhIdImport) data.getDescription();

        EcvTableRow newRow = data.createAndAddRow();
        d.LINK.setValue(newRow, newRecord.getLink());
        d.OBJECT_ID.setValue(newRow, newRecord.getObjectId());
        d.ORIGINAL_ID.setValue(newRow, newRecord.getOriginalId());
        d.ALLOCATION.setValue(newRow, newRecord.getAllocation());

        return (data);
    }
}
