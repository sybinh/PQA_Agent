/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.UiSupport;

import DataModel.DmFieldI;
import java.util.Set;
import java.util.TreeSet;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableRow;
import util.EcvTableDescription;
import DataModel.DmValueFieldI;

/**
 *
 * @author gug2wi
 */
public class DmUiTableSource_Products implements DmUiTableSource {

    static public class TableDescription extends EcvTableDescription {

        final public EcvTableColumn_String PRODUCT;

        private TableDescription() {
            addIpeColumn(PRODUCT = new EcvTableColumn_String("Product"));
        }

    }

    static final TableDescription tableDescription = new TableDescription();
    final DmValueFieldI<Set<String>> dmField;

    public DmUiTableSource_Products(DmValueFieldI<Set<String>> dmField) {
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
        for (String product : dmField.getValue()) {
            tableDescription.PRODUCT.setValue(data.createAndAddRow(), product);
        }
        return (data);
    }

    @Override
    public void setValue(EcvTableData newData) {
        assert (newData != null);
        Set<String> newSet = new TreeSet<>();
        for (EcvTableRow row : newData.getRows()) {
            newSet.add(tableDescription.PRODUCT.getValue(row));
        }
        dmField.setValue(newSet);
    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }

}
