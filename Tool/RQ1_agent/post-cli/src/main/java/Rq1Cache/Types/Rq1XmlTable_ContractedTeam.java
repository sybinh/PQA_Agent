/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.Flow.ContractedTeam;
import DataModel.Flow.IncludeBcState;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author bel5cob
 */
public class Rq1XmlTable_ContractedTeam extends Rq1XmlTable {

    final public Rq1XmlTableColumn_ComboBox FLOW_INCLUDE_BC_STATE;
    final public Rq1XmlTableColumn_String FLOW_CONTRACTED_TASKS;

    public Rq1XmlTable_ContractedTeam() {

        addXmlColumn(FLOW_INCLUDE_BC_STATE = new Rq1XmlTableColumn_ComboBox("INC_BC", 10, IncludeBcState.values(), "INC_BC", Rq1XmlTable.ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(FLOW_CONTRACTED_TASKS = new Rq1XmlTableColumn_String("TASKS", 500, "TASKS", ColumnEncodingMethod.ATTRIBUTE));
        FLOW_INCLUDE_BC_STATE.setOptional();
        FLOW_CONTRACTED_TASKS.setOptional();
    }

    final public EcvTableRow addContractedTeamData(ContractedTeam contractedTeam) {
        assert (contractedTeam != null);

        EcvTableData data = createTableData();
        EcvTableRow row = data.createRow();

        FLOW_INCLUDE_BC_STATE.setValue(row, contractedTeam.getIncludeBcState().getText());
        FLOW_CONTRACTED_TASKS.setValue(row, contractedTeam.getContractedTasks());

        data.addRow(row);
        return (row);
    }

    final public List<ContractedTeam> getContractedTeamData(EcvTableData data) {
        assert (data != null);
        List<ContractedTeam> contractedTeamList = new ArrayList<>();

        ContractedTeam contractedTeam = null;
        for (EcvTableRow row : data.getRows()) {

            assert (row.getValueAt(FLOW_INCLUDE_BC_STATE) != null);

            IncludeBcState includeBcState = null;
            String contractedTasks = "";

            if (row.getValueAt(FLOW_INCLUDE_BC_STATE) != null) {
                includeBcState = getContractedTeamStatus(row.getValueAt(FLOW_INCLUDE_BC_STATE).toString());
            }

            if (row.getValueAt(FLOW_CONTRACTED_TASKS) != null) {
                contractedTasks = row.getValueAt(FLOW_CONTRACTED_TASKS).toString();
            }

            contractedTeam = new ContractedTeam(includeBcState, contractedTasks);
            contractedTeamList.add(contractedTeam);

        }
        return contractedTeamList;

    }

    private IncludeBcState getContractedTeamStatus(String teamStatus) {
        IncludeBcState includeBcStatus = IncludeBcState.NO;
        switch (teamStatus) {
            case "Yes":
                includeBcStatus = IncludeBcState.YES;
                break;
            case "No":
                includeBcStatus = IncludeBcState.NO;
                break;
            default:
                break;
        }
        return includeBcStatus;
    }

    final public EcvTableRow updateContractedTeamData(ContractedTeam teamStatus, EcvTableData data) {
        assert (data != null);
        assert (teamStatus != null);
        EcvTableRow updatedRow = null;

        for (EcvTableRow row : data.getRows()) {

            FLOW_INCLUDE_BC_STATE.setValue(row, teamStatus.getIncludeBcState().getText());
            FLOW_CONTRACTED_TASKS.setValue(row, teamStatus.getContractedTasks());

            updatedRow = row;
            break;

        }
        return updatedRow;

    }

    public void removeContractedTeamData(EcvTableData data) {
        assert (data != null);
        data.clearRows();
    }

}
