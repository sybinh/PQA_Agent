/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1HistoryLog;
import DataModel.UiSupport.DmUiTableSource;
import java.util.Map;
import java.util.TreeMap;
import util.EcvTableColumn_DateTime;
import util.EcvTableColumn_String;
import util.EcvTableColumn_StringMultiline;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;

/**
 *
 * @author miw83wi
 */
public class DmRq1Table_HistoryLogsByField extends EcvTableDescription implements DmUiTableSource {

    final DmRq1Field_ReferenceList<DmRq1HistoryLog> elementlist;
    final private EcvTableColumn_String FIELD;
    final private EcvTableColumn_DateTime TIME;
    final private EcvTableColumn_String ACTION;
    final private EcvTableColumn_String LOGIN;
    final private EcvTableColumn_String CHANGE;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmRq1Table_HistoryLogsByField.class.getCanonicalName());

    public DmRq1Table_HistoryLogsByField(DmRq1Field_ReferenceList<DmRq1HistoryLog> elementlist) {
        assert (elementlist != null);
        this.elementlist = elementlist;
        addIpeColumn(FIELD = new EcvTableColumn_String("Field", 20));
        addIpeColumn(TIME = new EcvTableColumn_DateTime("Date/Time", 24));
        addIpeColumn(ACTION = new EcvTableColumn_String("Action", 12));
        addIpeColumn(LOGIN = new EcvTableColumn_String("Login", 16));
        addIpeColumn(CHANGE = new EcvTableColumn_StringMultiline("New Value", 80));
        setDefaultSortColumn(FIELD, true);
    }

    @Override
    public DmFieldI getDmField() {
        return elementlist;
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return this;
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData tableData = super.createTableData();
        for (DmRq1HistoryLog log : elementlist.getElementList()) {

            var fields = new TreeMap<>(log.extractFieldsFromHistoryLog());

            if (log.OPERATION_CONTEXT.getValueAsText().isBlank() == false) {
                fields.put(log.OPERATION_CONTEXT.getNameForUserInterface(), log.OPERATION_CONTEXT.getValueAsText());
            }
            if (log.OPERATION_MODE.getValueAsText().isBlank() == false) {
                fields.put(log.OPERATION_MODE.getNameForUserInterface(), log.OPERATION_MODE.getValueAsText());
            }

            for (Map.Entry<String, String> field : fields.entrySet()) {
                EcvTableRow row = tableData.createRow();
                FIELD.setValue(row, field.getKey());
                TIME.setValue(row, log.LAST_MODIFIED_DATE.getValue());
                ACTION.setValue(row, log.ACTION_NAME.getValue());
                LOGIN.setValue(row, log.LAST_MODIFIED_USER.getValue());
                CHANGE.setValue(row, field.getValue());
                tableData.addRow(row);
            }
        }

        return (tableData);
    }

    @Override
    public void setValue(EcvTableData newData) {
    }

    @Override
    public boolean useLazyLoad() {
        return false;
    }

}
