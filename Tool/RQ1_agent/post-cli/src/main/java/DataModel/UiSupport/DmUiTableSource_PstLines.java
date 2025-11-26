/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.UiSupport;

import DataModel.DmFieldI;
import Rq1Data.Types.Rq1LineEcuProject;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableRow;
import util.EcvTableDescription;
import util.EcvTableColumn_String.FilterMode;
import DataModel.DmValueFieldI;

/**
 *
 * @author gug2wi
 */
public class DmUiTableSource_PstLines implements DmUiTableSource {

    static public class TableDescription extends EcvTableDescription {

        final public EcvTableColumn_String INTERNAL_NAME;
        final public EcvTableColumn_String EXTERNAL_NAME;

        private TableDescription() {
            addIpeColumn(INTERNAL_NAME = new EcvTableColumn_String("Internal Name", FilterMode.NO_BLANKS));
            addIpeColumn(EXTERNAL_NAME = new EcvTableColumn_String("External Name", FilterMode.NO_BLANKS));
        }
    }

    static final TableDescription tableDescription = new TableDescription();
    final DmValueFieldI<List<Rq1LineEcuProject>> dmField;

    public DmUiTableSource_PstLines(DmValueFieldI<List<Rq1LineEcuProject>> dmField) {
        assert (dmField != null);
        this.dmField = dmField;
    }

    @Override
    public DmFieldI getDmField() {
        return (dmField);
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (tableDescription);
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData data = tableDescription.createTableData();
        for (Rq1LineEcuProject pstLine : dmField.getValue()) {
            EcvTableRow row = data.createAndAddRow();
            tableDescription.INTERNAL_NAME.setValue(row, pstLine.getInternalName());
            tableDescription.EXTERNAL_NAME.setValue(row, pstLine.getExternalName());

        }
        return (data);
    }

    @Override
    public void setValue(EcvTableData newData) {
        assert (newData != null);
        List<Rq1LineEcuProject> newList = new ArrayList<>();
        for (EcvTableRow row : newData.getRows()) {
            newList.add(new Rq1LineEcuProject(tableDescription.INTERNAL_NAME.getValue(row), tableDescription.EXTERNAL_NAME.getValue(row)));
        }
        dmField.setValue(newList);
    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }

}
