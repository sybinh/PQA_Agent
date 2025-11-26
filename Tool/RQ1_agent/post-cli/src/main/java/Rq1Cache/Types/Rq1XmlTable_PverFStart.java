/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.Flow.PverFStart;
import DataModel.Flow.PverFStarted;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author bel5cob
 */
public class Rq1XmlTable_PverFStart extends Rq1XmlTable {

    final public Rq1XmlTableColumn_ComboBox FLOW_PVER_F_START_STATE;
    final public Rq1XmlTableColumn_String FLOW_PVER_F_START_DATE;

    public Rq1XmlTable_PverFStart() {

        addXmlColumn(FLOW_PVER_F_START_STATE = new Rq1XmlTableColumn_ComboBox("STATE", 10, PverFStarted.values(), "STATE", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(FLOW_PVER_F_START_DATE = new Rq1XmlTableColumn_String("DATE", 15, "DATE", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        FLOW_PVER_F_START_STATE.setOptional();
        FLOW_PVER_F_START_DATE.setOptional();

    }

    final public EcvTableRow addPverFStartData(PverFStart pverFStart) {
        assert (pverFStart != null);

        EcvTableData data = createTableData();
        EcvTableRow row = data.createRow();

        FLOW_PVER_F_START_STATE.setValue(row, pverFStart.getPverFState().getText());
        FLOW_PVER_F_START_DATE.setValue(row, pverFStart.getPverFDate());

        data.addRow(row);
        return (row);
    }

    final public List<PverFStart> getPverFStartData(EcvTableData data) {
        assert (data != null);
        List<PverFStart> pverFStartList = new ArrayList<>();

        PverFStart pverFStart = null;
        for (EcvTableRow row : data.getRows()) {

            assert (row.getValueAt(FLOW_PVER_F_START_STATE) != null);
            assert (row.getValueAt(FLOW_PVER_F_START_DATE) != null);

            PverFStarted pverFStartState = null;
            String pverFStartDate = "";

            if (row.getValueAt(FLOW_PVER_F_START_STATE) != null) {
                pverFStartState = getPverFStartStatus(row.getValueAt(FLOW_PVER_F_START_STATE).toString());
            }
            if (row.getValueAt(FLOW_PVER_F_START_DATE) != null) {
                pverFStartDate = row.getValueAt(FLOW_PVER_F_START_DATE).toString();
            }

            pverFStart = new PverFStart(pverFStartState, pverFStartDate);
            pverFStartList.add(pverFStart);

        }
        return pverFStartList;

    }

    private PverFStarted getPverFStartStatus(String pverStatus) {
        PverFStarted pverFStartStatus = PverFStarted.FALSE;
        switch (pverStatus) {
            case "True":
                pverFStartStatus = PverFStarted.TRUE;
                break;
            case "False":
                pverFStartStatus = PverFStarted.FALSE;
                break;
            default:
                break;
        }
        return pverFStartStatus;
    }

    final public EcvTableRow updatePverFStartData(PverFStart pverFStart, EcvTableData data) {
        assert (data != null);
        assert (pverFStart != null);
        EcvTableRow updatedRow = null;

        for (EcvTableRow row : data.getRows()) {

            FLOW_PVER_F_START_STATE.setValue(row, pverFStart.getPverFState().getText());
            FLOW_PVER_F_START_DATE.setValue(row, pverFStart.getPverFDate());

            updatedRow = row;
            break;

        }
        return updatedRow;

    }

    public void removePverFStartData(EcvTableData data) {
        assert (data != null);
        data.clearRows();
    }

}
