/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Fields;

import ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord;
import Rq1Cache.Types.Rq1XmlTable;
import Rq1Cache.Types.Rq1XmlTableColumn_String;
import java.util.List;
import util.EcvTableData;
import util.EcvTableRow;
import util.EcvXmlEmptyElement;

/**
 *
 * @author RHO2HC
 */
public class DmRq1XmlTable_ConfigurableRules extends Rq1XmlTable {

    static final private String FIELDNAME_NAME = "name";
    static final private String FIELDNAME_COMMENT = "comment";
    static final private String FIELDNAME_RECORD_TYPE = "recordType";
    static final private String FIELDNAME_CRITERIA = "Criteria";
    static final private String FIELDNAME_MARKER = "Marker";

    final public Rq1XmlTableColumn_String NAME;
    final public Rq1XmlTableColumn_String RECORD_TYPE;
    final public Rq1XmlTableColumn_String COMMENT;
    final public Rq1XmlTableColumn_String CRITERIAS;
    final public Rq1XmlTableColumn_String MARKERS;

    private EcvTableData data;

    public DmRq1XmlTable_ConfigurableRules() {
        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 15, FIELDNAME_NAME, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(RECORD_TYPE = new Rq1XmlTableColumn_String("Record Type", 15, FIELDNAME_RECORD_TYPE, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(COMMENT = new Rq1XmlTableColumn_String("Comment", 15, FIELDNAME_COMMENT, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(CRITERIAS = new Rq1XmlTableColumn_String("Criterias", 0, FIELDNAME_CRITERIA, ColumnEncodingMethod.ELEMENT_LIST));
        addXmlColumn(MARKERS = new Rq1XmlTableColumn_String("Markers", 0, FIELDNAME_MARKER, ColumnEncodingMethod.ELEMENT_LIST));
        data = createTableData();
    }

    public EcvTableRow createAnEcvTableRowFromRecord(ConfigurableRuleRecord record) {
//        EcvTableRow newRow = new EcvTableRow(this, data);
        EcvTableRow newRow = new EcvTableRow(this);
        NAME.setValue(newRow, record.getName());
        RECORD_TYPE.setValue(newRow, record.getRecordType());
        COMMENT.setValue(newRow, record.getComment());
        addEmptyXmlElementsListIntoARow(newRow, CRITERIAS, record.convertCriteriasToXmlObjects());
        addEmptyXmlElementsListIntoARow(newRow, MARKERS, record.convertMarkersToXmlObjects());
        return newRow;
    }

    private void addEmptyXmlElementsListIntoARow(EcvTableRow row, Rq1XmlTableColumn_String ipeColumn, List<EcvXmlEmptyElement> elements) {
        StringBuilder combinedValue = new StringBuilder();
        for (EcvXmlEmptyElement currentValue : elements) {
            if (combinedValue.length() > 0) {
                combinedValue.append(",\n");
            }
            combinedValue.append(((EcvXmlEmptyElement) currentValue).getXmlString());
        }

        ipeColumn.setValue(row, combinedValue.toString());
    }
}
