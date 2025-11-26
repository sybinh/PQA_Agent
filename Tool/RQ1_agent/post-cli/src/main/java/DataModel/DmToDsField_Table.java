/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataModel.UiSupport.DmUiTableSource;
import DataStore.DsField_XmlSubField_Table;
import DataStore.DsRecordI;
import Rq1Cache.Types.Rq1XmlTable;
import java.util.List;
import util.EcvTableData;
import util.EcvTableDescription;
import DataStore.DsXmlTable_RecordAdapterI;

/**
 *
 * @author GUG2WI
 * @param <T_DS_RECORD> Type of the data store record.
 * @param <T_TABLE> Type of the table.
 * @param <T_RECORD> Type of the record hold by the table.
 */
public class DmToDsField_Table<T_DS_RECORD extends DsRecordI<?>, T_TABLE extends Rq1XmlTable, T_RECORD>
        extends DmToDsValueField<T_DS_RECORD, EcvTableData> implements DmUiTableSource {

    final private DsField_XmlSubField_Table<T_DS_RECORD, T_TABLE> dsTableField;
    final private DsXmlTable_RecordAdapterI<T_RECORD> recordAdapter;

    public DmToDsField_Table(DsField_XmlSubField_Table<T_DS_RECORD, T_TABLE> dsTableField, DsXmlTable_RecordAdapterI<T_RECORD> recordAdapter, String nameForUserInterface) {
        super(dsTableField, nameForUserInterface);
        assert (recordAdapter != null);

        this.dsTableField = dsTableField;
        this.recordAdapter = recordAdapter;
    }

    public List<T_RECORD> getValueAsRecords() {
        EcvTableData value = super.getValue();
        return (recordAdapter.extract(value));
    }

    public void setValueAsRecords(List<T_RECORD> list) {
        assert (list != null);
        EcvTableData data = recordAdapter.pack(list);
        super.setValue(data);
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (dsTableField.getTable());
    }

    @Override
    public DmFieldI getDmField() {
        return (this);
    }

    @Override
    final public boolean useLazyLoad() {
        return (false);
    }

}
