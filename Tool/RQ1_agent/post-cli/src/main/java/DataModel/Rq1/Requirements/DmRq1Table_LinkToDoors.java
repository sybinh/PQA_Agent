/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.DmFieldI;
import DataModel.DmValueFieldI;
import DataModel.UiSupport.DmUiTableSource;
import java.util.List;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Table_LinkToDoors extends EcvTableDescription implements DmUiTableSource {

    final public EcvTableColumn_String RQ1_ELEMENT_TYPE;
    final public EcvTableColumn_String RQ1_ELEMENT_ID;
    final public EcvTableColumn_String LINK_TYPE;
    final public EcvTableColumn_String REQUIREMENT_ID;
    final public EcvTableColumn_String REQUIREMENT_URL;

    final private DmValueFieldI<List<DmRq1LinkToRequirement_OnIssueAndIrm>> linkField;

    public DmRq1Table_LinkToDoors(DmValueFieldI<List<DmRq1LinkToRequirement_OnIssueAndIrm>> linkField) {
        assert (linkField != null);
        this.linkField = linkField;

        addIpeColumn(RQ1_ELEMENT_TYPE = new EcvTableColumn_String("Rq1 Element Type", 12));
        addIpeColumn(RQ1_ELEMENT_ID = new EcvTableColumn_String("Rq1 Element ID", 12));
        addIpeColumn(LINK_TYPE = new EcvTableColumn_String("Link Type", 15));
        addIpeColumn(REQUIREMENT_ID = new EcvTableColumn_String("Requirement", 30));
        addIpeColumn(REQUIREMENT_URL = new EcvTableColumn_String("Link to Requirement", 100));
    }

    @Override
    public DmFieldI getDmField() {
        return (linkField);
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (this);
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData tableData = super.createTableData();
        for (DmRq1LinkToRequirement_OnIssueAndIrm link : linkField.getValue()) {
            EcvTableRow row = tableData.createAndAddRow();
            RQ1_ELEMENT_TYPE.setValue(row, link.getRq1ElementType());
            RQ1_ELEMENT_ID.setValue(row, link.getRq1Element().getId());
            LINK_TYPE.setValue(row, link.getLinkType().getLinkTypeName());
            REQUIREMENT_ID.setValue(row, link.REQUIREMENT_ID.getValue());
            REQUIREMENT_URL.setValue(row, link.REQUIREMENT_URL.getValue());
        }
        return (tableData);
    }

    @Override
    public void setValue(EcvTableData newData) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }

}
