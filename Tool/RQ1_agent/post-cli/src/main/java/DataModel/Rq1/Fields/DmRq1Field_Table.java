/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmFieldI;
import DataModel.DmToDsValueField;
import DataModel.UiSupport.DmUiTableSource;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Records.Rq1RecordInterface;
import Rq1Cache.Types.Rq1XmlTable;
import util.EcvTableData;

/**
 *
 * @author gug2wi
 * @param <T>
 */
public class DmRq1Field_Table<T extends Rq1XmlTable> extends DmToDsValueField<Rq1RecordInterface, EcvTableData> implements DmUiTableSource {

    final Rq1XmlSubField_Table<T> rq1TableField;

    public DmRq1Field_Table(DmElementI parent, Rq1XmlSubField_Table<T> rq1TableField, String nameForUserInterface) {
        this(rq1TableField, nameForUserInterface);
    }

    public DmRq1Field_Table(Rq1XmlSubField_Table<T> rq1TableField, String nameForUserInterface) {
        super(rq1TableField, nameForUserInterface);
        this.rq1TableField = rq1TableField;
    }

    @Override
    final public T getTableDescription() {
        return (rq1TableField.getTable());
    }

    @Override
    final public DmFieldI getDmField() {
        return (this);
    }

    @Override
    public boolean getCreateAutoSorter() {
        return (rq1TableField.getTable().getCreateAutoSorter());
    }

    @Override
    final public boolean useLazyLoad() {
        return (false);
    }
}
