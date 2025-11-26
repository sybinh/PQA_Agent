/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import DataModel.Flow.CriticalResource;
import DataModel.Flow.CriticalResourceImpl;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author bel5cob
 */
public class Rq1XmlTable_CriticalResource extends Rq1XmlTable {

    final public Rq1XmlTableColumn_String COMMENT;
    final public Rq1XmlTableColumn_String START_DATE;
    final public Rq1XmlTableColumn_String END_DATE;
    final public Rq1XmlTableColumn_String D_PH1;
    final public Rq1XmlTableColumn_String D_PH2;
    final public Rq1XmlTableColumn_String DURATION;
    final public Rq1XmlTableColumn_String PH_REQ;
    final public Rq1XmlTableColumn_String RES_CONFLICT;

    public Rq1XmlTable_CriticalResource() {

        addXmlColumn(START_DATE = new Rq1XmlTableColumn_String("S_D", 10, "S_D", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(END_DATE = new Rq1XmlTableColumn_String("E_D", 10, "E_D", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(COMMENT = new Rq1XmlTableColumn_String("CMT", 100, "CMT", ColumnEncodingMethod.CONTENT));
        addXmlColumn(D_PH1 = new Rq1XmlTableColumn_String("D_PH1", 100, "D_PH1", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(D_PH2 = new Rq1XmlTableColumn_String("D_PH2", 100, "D_PH2", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(DURATION = new Rq1XmlTableColumn_String("DUR", 100, "DUR", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(PH_REQ = new Rq1XmlTableColumn_String("PH_REQ", 100, "PH_REQ", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(RES_CONFLICT = new Rq1XmlTableColumn_String("RES_CONFLICT", 100, "RES_CONFLICT", ColumnEncodingMethod.ATTRIBUTE));
        COMMENT.setOptional();
        D_PH1.setOptional();
        D_PH2.setOptional();
        DURATION.setOptional();
        PH_REQ.setOptional();
        RES_CONFLICT.setOptional();

    }

    final public EcvTableRow addCriticalResourceData(CriticalResource resource) {
        assert (resource != null);

        EcvTableData data = createTableData();
        EcvTableRow row = data.createRow();

        START_DATE.setValue(row, resource.getStartDate());
        END_DATE.setValue(row, resource.getEndDate());
        COMMENT.setValue(row, resource.getComment());
        D_PH1.setValue(row, resource.getDPhase1());
        D_PH2.setValue(row, resource.getDPhase2());
        DURATION.setValue(row, resource.getCrTime());
        PH_REQ.setValue(row, resource.getDPhase());
        RES_CONFLICT.setValue(row, resource.getResConflict());

        data.addRow(row);
        return (row);
    }

    final public List<CriticalResource> getCriticalResourceData(EcvTableData data) {
        assert (data != null);
        List<CriticalResource> resourceList = new ArrayList<>();

        CriticalResource resource = null;
        for (EcvTableRow row : data.getRows()) {

            assert (row.getValueAt(START_DATE) != null);
            assert (row.getValueAt(END_DATE) != null);

            String startDate = "";
            String endDate = "";
            String comment = "";
            String crTime = "";
            String dPhase1 = "";
            String dPhase2 = "";
            String dPhase = "";
            String resConflict = "";

            if (row.getValueAt(START_DATE) != null) {
                startDate = row.getValueAt(START_DATE).toString();
            }
            if (row.getValueAt(END_DATE) != null) {
                endDate = row.getValueAt(END_DATE).toString();
            }
            if (row.getValueAt(COMMENT) != null) {
                comment = row.getValueAt(COMMENT).toString();
            }
            if (row.getValueAt(DURATION) != null) {
                crTime = row.getValueAt(DURATION).toString();
            }
            if (row.getValueAt(D_PH1) != null) {
                dPhase1 = row.getValueAt(D_PH1).toString();
            }
            if (row.getValueAt(D_PH2) != null) {
                dPhase2 = row.getValueAt(D_PH2).toString();
            }
            if (row.getValueAt(PH_REQ) != null) {
                dPhase = row.getValueAt(PH_REQ).toString();
            }
            if (row.getValueAt(RES_CONFLICT) != null) {
                resConflict = row.getValueAt(RES_CONFLICT).toString();
            }
            resource = new CriticalResourceImpl(startDate, endDate, comment, crTime, dPhase1, dPhase2, dPhase, resConflict);
            resourceList.add(resource);

        }
        return resourceList;

    }

    final public EcvTableRow updateCriticalResourceData(CriticalResource resource, EcvTableData data) {
        assert (data != null);
        assert (resource != null);
        EcvTableRow updatedRow = null;

        for (EcvTableRow row : data.getRows()) {

            START_DATE.setValue(row, resource.getStartDate());
            END_DATE.setValue(row, resource.getEndDate());
            COMMENT.setValue(row, resource.getComment());
            DURATION.setValue(row, resource.getCrTime());
            D_PH1.setValue(row, resource.getDPhase1());
            D_PH2.setValue(row, resource.getDPhase2());
            PH_REQ.setValue(row, resource.getDPhase());
            RES_CONFLICT.setValue(row, resource.getResConflict());

            updatedRow = row;
            break;

        }
        return updatedRow;

    }

    public void removeCriticalResourceData(EcvTableData data) {
        assert (data != null);
        data.clearRows();
    }
}
