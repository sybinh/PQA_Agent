/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1HistoryLog;
import DataModel.UiSupport.DmUiTableSource;
import util.EcvTableColumn_DateTime;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;

/**
 *
 * @author miw83wi
 */
public class DmRq1Table_HistoryLogsByState extends EcvTableDescription implements DmUiTableSource {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmRq1Table_HistoryLogsByState.class.getCanonicalName());

    final private EcvTableColumn_DateTime TIME;
    final private EcvTableColumn_String ACTION;
    final private EcvTableColumn_String LOGINNAME;
    final private EcvTableColumn_String CURRENT_LCS;
    final private EcvTableColumn_String PREVIOUS_LCS;
    final private EcvTableColumn_String OPERATION_CONTEXT;
    final private EcvTableColumn_String OPERATION_MODE;

    final DmRq1Field_ReferenceList<DmRq1HistoryLog> elementlist;

    public DmRq1Field_ReferenceList<DmRq1HistoryLog> getElementlist() {
        return elementlist;
    }

    public DmRq1Table_HistoryLogsByState(DmRq1Field_ReferenceList<DmRq1HistoryLog> elementlist) {
        assert (elementlist != null);
        this.elementlist = elementlist;
        addIpeColumn(TIME = new EcvTableColumn_DateTime("Date/Time", 18));
        addIpeColumn(LOGINNAME = new EcvTableColumn_String("Login", 6));
        addIpeColumn(OPERATION_CONTEXT = new EcvTableColumn_String("Context", 6));
        addIpeColumn(OPERATION_MODE = new EcvTableColumn_String("Mode", 12));
        addIpeColumn(ACTION = new EcvTableColumn_String("Action", 8));
        addIpeColumn(PREVIOUS_LCS = new EcvTableColumn_String("Old LifeCycleState", 15));
        addIpeColumn(CURRENT_LCS = new EcvTableColumn_String("New LifeCycleState", 15));
        setDefaultSortColumn(TIME, false);
    }

    @Override
    public DmRq1Field_ReferenceList getDmField() {
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

            String previousLCS = log.PREVIOUS_LIFE_CYCLE_STATE.getValue();
            String currentLCS = log.LIFE_CYCLE_STATE.getValue();

            if ((previousLCS.equals("")) || (!currentLCS.equals(previousLCS) && !previousLCS.equals(""))) {
                EcvTableRow row = tableData.createRow();
                TIME.setValue(row, log.LAST_MODIFIED_DATE.getValue());
                ACTION.setValue(row, log.ACTION_NAME.getValue());
                LOGINNAME.setValue(row, log.LAST_MODIFIED_USER.getValue());
                OPERATION_CONTEXT.setValue(row, log.OPERATION_CONTEXT.getValueAsText());
                OPERATION_MODE.setValue(row, log.OPERATION_MODE.getValueAsText());
                CURRENT_LCS.setValue(row, currentLCS);
                PREVIOUS_LCS.setValue(row, previousLCS);
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
