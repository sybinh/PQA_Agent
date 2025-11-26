/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.CRP.CrpGanttData;
import DataModel.CRP.CrpGanttDataImpl;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author HRN4KOR
 */
public class Rq1XmlTable_CrpGantt extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String START_DATE;
    final public Rq1XmlTableColumn_String END_DATE;

    public Rq1XmlTable_CrpGantt() {
        addXmlColumn(START_DATE = new Rq1XmlTableColumn_String("S_D", 10, "S_D", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(END_DATE = new Rq1XmlTableColumn_String("E_D", 10, "E_D", ColumnEncodingMethod.ATTRIBUTE));
    }

    final public EcvTableRow addCrpData(CrpGanttData crpGantt) {
        assert (crpGantt != null);

        EcvTableData data = createTableData();
        EcvTableRow row = data.createRow();

        START_DATE.setValue(row, crpGantt.getStartDate());
        END_DATE.setValue(row, crpGantt.getEndDate());

        data.addRow(row);
        return (row);
    }

    final public CrpGanttData getCrpData(EcvTableData data) {
        assert (data != null);
        CrpGanttData resource = null;
        for (EcvTableRow row : data.getRows()) {
            assert (row.getValueAt(START_DATE) != null);
            assert (row.getValueAt(END_DATE) != null);

            String startDate = "";
            String endDate = "";

            if (row.getValueAt(START_DATE) != null) {
                startDate = row.getValueAt(START_DATE).toString();
            }
            if (row.getValueAt(END_DATE) != null) {
                endDate = row.getValueAt(END_DATE).toString();
            }
            resource = new CrpGanttDataImpl(startDate, endDate);
        }
        return resource;

    }

    final public EcvTableRow updateCrpData(CrpGanttData ganttData, EcvTableData data) {
        assert (data != null);
        assert (ganttData != null);
        EcvTableRow updatedRow = null;
        for (EcvTableRow row : data.getRows()) {
            START_DATE.setValue(row, ganttData.getStartDate());
            END_DATE.setValue(row, ganttData.getEndDate());
            updatedRow = row;
            break;
        }
        return updatedRow;
    }

    public void removeCrpData(EcvTableData data) {
        assert (data != null);
        data.clearRows();
    }
}
