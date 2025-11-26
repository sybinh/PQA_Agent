/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.UiSupport.DmUiTableSource;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1AttachmentMapping;
import Rq1Data.Enumerations.ExportAttribute;
import Rq1Data.Enumerations.ExportState;
import util.EcvTableColumn_ComboBox;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableColumn_String;
import util.EcvTableRow;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Table_AttachmentMapping extends EcvTableDescription implements DmUiTableSource {

    final private EcvTableColumn_ComboBox EXPORT_STATE;
    final private EcvTableColumn_ComboBox ATTRIBUTE;
    final private EcvTableColumn_String ENG_ATTACHMENT;
    final private EcvTableColumn_String SALES_ATTACHMENT;

    final private DmRq1Field_ReferenceList<DmRq1AttachmentMapping> attachmentMappingField;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public DmRq1Table_AttachmentMapping(DmRq1Field_ReferenceList<DmRq1AttachmentMapping> attachmentMappingField) {
        assert (attachmentMappingField != null);

        addIpeColumn(EXPORT_STATE = new EcvTableColumn_ComboBox("Export State", 8, ExportState.values()));
        addIpeColumn(ATTRIBUTE = new EcvTableColumn_ComboBox("Export Attribute", 8, ExportAttribute.values()));
        addIpeColumn(ENG_ATTACHMENT = new EcvTableColumn_String("Eng Attachment", 18));
        addIpeColumn(SALES_ATTACHMENT = new EcvTableColumn_String("Sales Attachment", 18));

        setDefaultSortColumn(ENG_ATTACHMENT, true);

        this.attachmentMappingField = attachmentMappingField;
    }

    @Override
    public DmRq1Field_ReferenceList getDmField() {
        return (attachmentMappingField);
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData tableData = super.createTableData();

        for (DmRq1AttachmentMapping mapping : attachmentMappingField.getElementList()) {
            EcvTableRow row = tableData.createRow();
            EXPORT_STATE.setValue(row, mapping.EXPORT_STATE.getValue().getText());
            ATTRIBUTE.setValue(row, mapping.ATTRIBUTE.getValue().getText());
            ENG_ATTACHMENT.setValue(row, mapping.ENG_ATTACHMENT.getValue());
            SALES_ATTACHMENT.setValue(row, mapping.SALES_ATTACHMENT.getValue());
            tableData.addRow(row);
        }

        return (tableData);
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (this);
    }

    @Override
    public void setValue(EcvTableData newData) {

    }

    @Override
    public boolean useLazyLoad() {
        return (true);
    }

}
