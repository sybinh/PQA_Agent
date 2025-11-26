/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Doors;

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
public class DmRq1Table_LinksOnIssues extends EcvTableDescription implements DmUiTableSource {

    final public EcvTableColumn_String ISSUE_ID;
    final public EcvTableColumn_String LINK_TYPE;
    final public EcvTableColumn_String REQUIREMENT_ID;
    final public EcvTableColumn_String LINK_TO_DOORS;

    final private DmValueFieldI<List<DmRq1IssueToDoorsLink>> linkField;

    public DmRq1Table_LinksOnIssues(DmValueFieldI<List<DmRq1IssueToDoorsLink>> linkField) {
        assert (linkField != null);
        this.linkField = linkField;

        addIpeColumn(ISSUE_ID = new EcvTableColumn_String("Issue on Release", 12));
        addIpeColumn(LINK_TYPE = new EcvTableColumn_String("Link Type", 15));
        addIpeColumn(REQUIREMENT_ID = new EcvTableColumn_String("Requirement",30));
        addIpeColumn(LINK_TO_DOORS = new EcvTableColumn_String("Link to DOORS object",100));
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
        for ( DmRq1IssueToDoorsLink link : linkField.getValue()) {
            EcvTableRow row = tableData.createAndAddRow();
            ISSUE_ID.setValue(row, link.getRq1Issue().getRq1Id());
            LINK_TYPE.setValue(row, link.getLinkType().getLinkTypeName());
            REQUIREMENT_ID.setValue(row, link.REQUIREMENT_ID.getValueAsText());
            LINK_TO_DOORS.setValue(row, link.LINK_TO_DOORS.getValueAsText());
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
