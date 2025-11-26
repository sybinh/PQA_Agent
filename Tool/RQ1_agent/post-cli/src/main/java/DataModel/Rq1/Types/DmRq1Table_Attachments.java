/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1Attachment;
import DataModel.UiSupport.DmUiTableSource;
import OslcAccess.Rq1.OslcRq1ServerDescription;
import util.EcvTableColumn_Integer;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;
import util.UiWorker;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Table_Attachments extends EcvTableDescription implements DmUiTableSource {

    final public EcvTableColumn_String FILENAME;
    final private EcvTableColumn_String DESCRIPTION;
    final private EcvTableColumn_Integer FILESIZE;

    final DmRq1Field_ReferenceList<DmRq1Attachment> attachmentField;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public DmRq1Table_Attachments(DmRq1Field_ReferenceList<DmRq1Attachment> attachmentField) {
        assert (attachmentField != null);

        this.attachmentField = attachmentField;

        addIpeColumn(FILENAME = new EcvTableColumn_String("Filename", 18));
        addIpeColumn(DESCRIPTION = new EcvTableColumn_String("Description", 10));
        addIpeColumn(FILESIZE = new EcvTableColumn_Integer("Size", 8));

        setDefaultSortColumn(FILENAME, true);
    }

    @Override
    public DmRq1Field_ReferenceList<DmRq1Attachment> getDmField() {
        return (attachmentField);
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData tableData = super.createTableData();
        for (DmRq1Attachment attachment : attachmentField.getElementList()) {
            EcvTableRow row = tableData.createRow();
            DESCRIPTION.setValue(row, attachment.DESCRIPTION.getValue());
            FILENAME.setValue(row, attachment.FILENAME.getValue());
            FILESIZE.setValue(row, Integer.valueOf(attachment.FILESIZE.getValue()));
            tableData.addRow(row);
        }
        return (tableData);
    }

    @Override
    final public boolean handleDoubleClick(EcvTableData data, int row, int column) {
        if (data != null) {
            // download and open a file
            downloadAndOpenAttachment(row);
        }
        return (true);
    }

    private void downloadAndOpenAttachment(Integer row) {
        DmRq1Table_Attachments _this = this;

        String fileName = (String) _this.getValue().getValueAt(row, FILENAME.getColumnIndexData());
        DmRq1Attachment attachment = DmRq1Attachment.getAttachmentForFilename(attachmentField, fileName);
        if (attachment == null) {
            return;
        }
        UiWorker.execute(new UiWorker<Void>(UiWorker.DOWNLOAD_FILE) {
            @Override
            protected Void backgroundTask() {
                attachment.downloadAndOpen();
                return (null);
            }
        });

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

    protected abstract OslcRq1ServerDescription.LinkType getLinkType();

}
