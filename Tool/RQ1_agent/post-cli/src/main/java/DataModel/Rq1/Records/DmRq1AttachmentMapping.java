/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.DmRq1NodeInterface;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1AttachmentMapping;
import Rq1Data.Enumerations.ExportAttribute;
import Rq1Data.Enumerations.ExportAttribute.AttachmentType;
import Rq1Data.Enumerations.ExportState;

/**
 *
 * @author gug2wi
 */
public class DmRq1AttachmentMapping extends DmRq1Element implements DmRq1NodeInterface {

    final public DmRq1Field_Enumeration ATTRIBUTE;
    final public DmRq1Field_Text ENG_ATTACHMENT;
    final public DmRq1Field_Enumeration EXPORT_STATE;
    final public DmRq1Field_Reference<DmRq1Issue> HAS_ISSUE;
    final public DmRq1Field_Reference HAS_IRM;
    final public DmRq1Field_Text LINKED_ID;
    final public DmRq1Field_Text LINKED_RECORD_TYPE;
    final public DmRq1Field_Text SALES_ATTACHMENT;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1AttachmentMapping(Rq1AttachmentMapping rq1AttachmentMapping) {
        super("RQ1-Attachment-Mapping", rq1AttachmentMapping);

        //
        // Create and add fields
        //
        addField(ATTRIBUTE = new DmRq1Field_Enumeration(this, rq1AttachmentMapping.EXPORT_ATTRIBUTE, "Attribute"));
        addField(ENG_ATTACHMENT = new DmRq1Field_Text(this, rq1AttachmentMapping.ENG_ATTACHMENT, "Eng Attachment"));
        addField(EXPORT_STATE = new DmRq1Field_Enumeration(this, rq1AttachmentMapping.EXPORT_STATE, "Export State"));
        addField(HAS_ISSUE = new DmRq1Field_Reference<>(this, rq1AttachmentMapping.HAS_ISSUE, "Issue"));
        addField(HAS_IRM = new DmRq1Field_Reference<>(this, rq1AttachmentMapping.HAS_IRM, "IRM"));
        addField(LINKED_ID = new DmRq1Field_Text(this, rq1AttachmentMapping.LINKED_ID, "Linked Id"));
        addField(LINKED_RECORD_TYPE = new DmRq1Field_Text(this, rq1AttachmentMapping.LINKED_RECORD_TYPE, "Linked Record Type"));
        addField(SALES_ATTACHMENT = new DmRq1Field_Text(this, rq1AttachmentMapping.SALES_ATTACHMENT, "Sales Attachment"));
    }

    public String getFileName() {
        String fileName = ENG_ATTACHMENT.getValueAsText();
        if (fileName.isEmpty() == true) {
            fileName = SALES_ATTACHMENT.getValueAsText();
        }
        return (fileName);
    }

    @Override
    public boolean isCanceled() {
        return (false);
    }

    @Override
    public String getTitle() {
        return (((Rq1AttachmentMapping) getRq1Record()).getTitle());
    }

    @Override
    public String getId() {
        return ("");
    }

    /**
     * Create a mapping object in the database and connect it with the issue.
     *
     * @param exportState Export State for the mapping.
     * @param attribute Attribute for the mapping.
     * @param issue Issue for which the mapping will be created.
     * @param attachment File for which the mapping will be created.
     * @return Created attachment.
     */
    public static DmRq1AttachmentMapping createForIssue(ExportState exportState, ExportAttribute attribute, DmRq1IssueSW issue, DmRq1Attachment attachment) {
        assert (exportState != null);
        assert (attribute != null);
        assert (issue != null);
        assert (attachment != null);

        return (createForIssue(exportState, attribute, issue, attachment.FILENAME.getValue()));
    }

    /**
     * Create a mapping object in the database and connect it with the issue.
     *
     * @param exportState Export State for the mapping.
     * @param attribute Attribute for the mapping.
     * @param issue Issue for which the mapping will be created.
     * @param attachmentFileName Name of the file for which the mapping will be
     * created.
     * @return Created attachment.
     */
    public static DmRq1AttachmentMapping createForIssue(ExportState exportState, ExportAttribute attribute, DmRq1IssueSW issue, String attachmentFileName) {
        assert (exportState != null);
        assert (attribute != null);
        assert (issue != null);
        assert (attachmentFileName != null);
        assert (attachmentFileName.isEmpty() == false);

        //----------------------------------------------------------------------
        //
        // Create the Mapping
        //
        //----------------------------------------------------------------------
        DmRq1AttachmentMapping mapping = new DmRq1AttachmentMapping(new Rq1AttachmentMapping());

        mapping.ATTRIBUTE.setValue(attribute);
        if (attribute.getAttachmentType() == AttachmentType.ENG_ATTACHMENT) {
            mapping.ENG_ATTACHMENT.setValue(attachmentFileName);
        } else {
            mapping.SALES_ATTACHMENT.setValue(attachmentFileName);
        }
        mapping.EXPORT_STATE.setValue(exportState);
        mapping.HAS_ISSUE.setElement(issue);
        mapping.LINKED_ID.setValue(issue.getRq1Id());
        mapping.LINKED_RECORD_TYPE.setValue(issue.getRq1Record().getRecordDescription().getRecordType().getText());

        //----------------------------------------------------------------------
        //
        // Save Mapping
        //
        //----------------------------------------------------------------------
        if (attribute.getAttachmentType() == AttachmentType.ENG_ATTACHMENT) {
            mapping.save(Rq1AttachmentMapping.ATTRIBUTE_HAS_ISSUE,
                    Rq1AttachmentMapping.ATTRIBUTE_LINKED_ID,
                    Rq1AttachmentMapping.ATTRIBUTE_LINKED_RECORD_TYPE,
                    Rq1AttachmentMapping.ATTRIBUTE_EXPORT_STATE,
                    Rq1AttachmentMapping.ATTRIBUTE_EXPORT_ATTRIBUTE,
                    Rq1AttachmentMapping.ATTRIBUTE_ENG_ATTACHMENT);
        } else {
            mapping.save(Rq1AttachmentMapping.ATTRIBUTE_HAS_ISSUE,
                    Rq1AttachmentMapping.ATTRIBUTE_LINKED_ID,
                    Rq1AttachmentMapping.ATTRIBUTE_LINKED_RECORD_TYPE,
                    Rq1AttachmentMapping.ATTRIBUTE_EXPORT_STATE,
                    Rq1AttachmentMapping.ATTRIBUTE_EXPORT_ATTRIBUTE,
                    Rq1AttachmentMapping.ATTRIBUTE_SALES_ATTACHMENT);
        }

        return (mapping);
    }

}
