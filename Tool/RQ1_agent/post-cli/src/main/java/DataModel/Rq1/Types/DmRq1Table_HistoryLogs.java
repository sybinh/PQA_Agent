/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.UiSupport.DmUiTableSource;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1HistoryLog;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableColumn_DateTime;
import util.EcvTableColumn_String;
import util.EcvTableColumn_StringMultiline;
import util.EcvTableRow;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Table_HistoryLogs extends EcvTableDescription implements DmUiTableSource {

    final private EcvTableColumn_String ACTION_NAME;
    final private EcvTableColumn_String HISTORY_ID;
    final private EcvTableColumn_String HISTORY_LOG;
    final private EcvTableColumn_DateTime LAST_MODIFIED_DATE;
    final private EcvTableColumn_String LAST_MODIFIED_USER;
    final private EcvTableColumn_String LIFE_CYCLE_STATE;
    final private EcvTableColumn_String PREVIOUS_LIFE_CYCLE_STATE;
    final private EcvTableColumn_String OPERATION_CONTEXT;
    final private EcvTableColumn_String OPERATION_MODE;

    final DmRq1Field_ReferenceList<DmRq1HistoryLog> historyField;

    public DmRq1Table_HistoryLogs(DmRq1Field_ReferenceList<DmRq1HistoryLog> historyField) {
        assert (historyField != null);

        this.historyField = historyField;

        addIpeColumn(HISTORY_ID = new EcvTableColumn_String("ID", 12));
        addIpeColumn(LAST_MODIFIED_DATE = new EcvTableColumn_DateTime("Date/Time", 20));
        addIpeColumn(LAST_MODIFIED_USER = new EcvTableColumn_String("Login", 10));
        addIpeColumn(OPERATION_CONTEXT = new EcvTableColumn_String("Context", 12));
        addIpeColumn(OPERATION_MODE = new EcvTableColumn_String("Mode", 20));
        addIpeColumn(ACTION_NAME = new EcvTableColumn_String("Action", 12));
        addIpeColumn(PREVIOUS_LIFE_CYCLE_STATE = new EcvTableColumn_String("Old LCS", 12));
        addIpeColumn(LIFE_CYCLE_STATE = new EcvTableColumn_String("New LCS", 12));
        addIpeColumn(HISTORY_LOG = new EcvTableColumn_StringMultiline("Changes", 100));

        setDefaultSortColumn(LAST_MODIFIED_DATE, false);
    }

    @Override
    public DmRq1Field_ReferenceList getDmField() {
        return (historyField);
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData tableData = super.createTableData();

        for (DmRq1HistoryLog log : historyField.getElementList()) {
            EcvTableRow row = tableData.createRow();
            ACTION_NAME.setValue(row, log.ACTION_NAME.getValue());
            LAST_MODIFIED_DATE.setValue(row, log.LAST_MODIFIED_DATE.getValue());
            LAST_MODIFIED_USER.setValue(row, log.LAST_MODIFIED_USER.getValue());
            HISTORY_ID.setValue(row, log.HISTORY_ID.getValue());
            LIFE_CYCLE_STATE.setValue(row, log.LIFE_CYCLE_STATE.getValue());
            PREVIOUS_LIFE_CYCLE_STATE.setValue(row, log.PREVIOUS_LIFE_CYCLE_STATE.getValue());
            HISTORY_LOG.setValue(row, log.extractChangesFromHistoryLog());
            OPERATION_CONTEXT.setValue(row, log.OPERATION_CONTEXT.getValueAsText());
            OPERATION_MODE.setValue(row, log.OPERATION_MODE.getValueAsText());

            tableData.addRow(row);
        }

        return (tableData);
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (this);
    }

    @Override
    public void setValue(EcvTableData newData) {

    }

    @Override
    public boolean useLazyLoad() {
        return (true);
    }

}
