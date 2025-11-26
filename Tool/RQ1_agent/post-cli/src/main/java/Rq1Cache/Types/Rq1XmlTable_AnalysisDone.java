/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.Flow.AnalysisDone;
import DataModel.Flow.AnalysisDoneState;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author bel5cob
 */
public class Rq1XmlTable_AnalysisDone extends Rq1XmlTable {

    final public Rq1XmlTableColumn_ComboBox FLOW_ANALYSIS_DONE_STATE;
    final public Rq1XmlTableColumn_String FLOW_ANALYSIS_DONE_DATE;

    public Rq1XmlTable_AnalysisDone() {

        addXmlColumn(FLOW_ANALYSIS_DONE_STATE = new Rq1XmlTableColumn_ComboBox("STATE", 10, AnalysisDoneState.values(), "STATE", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(FLOW_ANALYSIS_DONE_DATE = new Rq1XmlTableColumn_String("DATE", 15, "DATE", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        FLOW_ANALYSIS_DONE_STATE.setOptional();
        FLOW_ANALYSIS_DONE_DATE.setOptional();

    }

    final public EcvTableRow addAnalysisDoneData(AnalysisDone analysisDone) {
        assert (analysisDone != null);

        EcvTableData data = createTableData();
        EcvTableRow row = data.createRow();

        FLOW_ANALYSIS_DONE_STATE.setValue(row, analysisDone.getDoneState().getText());
        FLOW_ANALYSIS_DONE_DATE.setValue(row, analysisDone.getDoneDate());

        data.addRow(row);
        return (row);
    }

    final public List<AnalysisDone> getAnalysisDoneData(EcvTableData data) {
        assert (data != null);
        List<AnalysisDone> analysisDoneList = new ArrayList<>();

        AnalysisDone analysisDone = null;
        for (EcvTableRow row : data.getRows()) {

            assert (row.getValueAt(FLOW_ANALYSIS_DONE_STATE) != null);
            assert (row.getValueAt(FLOW_ANALYSIS_DONE_DATE) != null);

            AnalysisDoneState analysisDoneState = null;
            String analysisDoneDate = "";

            if (row.getValueAt(FLOW_ANALYSIS_DONE_STATE) != null) {
                analysisDoneState = getAnalysisDoneStatus(row.getValueAt(FLOW_ANALYSIS_DONE_STATE).toString());
            }
            if (row.getValueAt(FLOW_ANALYSIS_DONE_DATE) != null) {
                analysisDoneDate = row.getValueAt(FLOW_ANALYSIS_DONE_DATE).toString();
            }

            analysisDone = new AnalysisDone(analysisDoneState, analysisDoneDate);
            analysisDoneList.add(analysisDone);

        }
        return analysisDoneList;

    }

    private AnalysisDoneState getAnalysisDoneStatus(String analysisStatus) {
        AnalysisDoneState analysisDoneStatus = AnalysisDoneState.FALSE;
        switch (analysisStatus) {
            case "True":
                analysisDoneStatus = AnalysisDoneState.TRUE;
                break;
            case "False":
                analysisDoneStatus = AnalysisDoneState.FALSE;
                break;
            default:
                break;
        }
        return analysisDoneStatus;
    }

    final public EcvTableRow updateAnalysisDoneData(AnalysisDone analysisDone, EcvTableData data) {
        assert (data != null);
        assert (analysisDone != null);
        EcvTableRow updatedRow = null;

        for (EcvTableRow row : data.getRows()) {

            FLOW_ANALYSIS_DONE_STATE.setValue(row, analysisDone.getDoneState().getText());
            FLOW_ANALYSIS_DONE_DATE.setValue(row, analysisDone.getDoneDate());

            updatedRow = row;
            break;

        }
        return updatedRow;

    }

    public void removeAnalysisDoneData(EcvTableData data) {
        assert (data != null);
        data.clearRows();
    }

}
