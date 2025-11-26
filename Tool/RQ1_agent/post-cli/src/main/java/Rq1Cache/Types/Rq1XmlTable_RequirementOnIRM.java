/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Monitoring.Rq1ParseFieldException;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;
import util.EcvTableRow;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_RequirementOnIRM extends Rq1XmlTable {

    final static public String ATTRIBUTE_NAME_COMMENT = "comment";
    final static public String ATTRIBUTE_NAME_LINK = "link";
    final static public String ATTRIBUTE_NAME_STATUS = "status";
    final static public String ATTRIBUTE_NAME_TYPE = "type";
    final static public String ATTRIBUTE_NAME_BASELINE = "baseline";
    final static public String ATTRIBUTE_NAME_BASELINE_TYPE = "baselinetype";

    final public Rq1XmlTableColumn_String REQUIREMENT;
    final public Rq1XmlTableColumn_String COMMENT;
    final public Rq1XmlTableColumn_Link LINK;
    final public Rq1XmlTableColumn_String TYPE;
    final public Rq1XmlTableColumn_String STATUS;
    final public Rq1XmlTableColumn_String BASELINE;
    final public Rq1XmlTableColumn_String BASELINE_TYPE;
    

    public Rq1XmlTable_RequirementOnIRM() {
        addXmlColumn(REQUIREMENT = new Rq1XmlTableColumn_String("Requirement", 20, "Requirement", ColumnEncodingMethod.CONTENT));
        REQUIREMENT.setOptional();
        addXmlColumn(STATUS = new Rq1XmlTableColumn_String("Status", 10, ATTRIBUTE_NAME_STATUS, ColumnEncodingMethod.ATTRIBUTE));
        STATUS.setOptional();
        addXmlColumn(TYPE = new Rq1XmlTableColumn_String("Type", 15, ATTRIBUTE_NAME_TYPE, ColumnEncodingMethod.ATTRIBUTE));
        TYPE.setOptional(); 
        addXmlColumn(BASELINE = new Rq1XmlTableColumn_String("Baseline", 12, ATTRIBUTE_NAME_BASELINE, ColumnEncodingMethod.ATTRIBUTE));
        BASELINE.setOptional();
        addXmlColumn(BASELINE_TYPE = new Rq1XmlTableColumn_String("Baseline Type", 15, ATTRIBUTE_NAME_BASELINE_TYPE, ColumnEncodingMethod.ATTRIBUTE));
        BASELINE_TYPE.setOptional();
        addXmlColumn(COMMENT = new Rq1XmlTableColumn_String("Comment", 40, ATTRIBUTE_NAME_COMMENT, ColumnEncodingMethod.ATTRIBUTE));
        COMMENT.setOptional();
        addXmlColumn(LINK = new Rq1XmlTableColumn_Link("Link to DOORS (double click to open)", 120, ATTRIBUTE_NAME_LINK, ColumnEncodingMethod.ATTRIBUTE));
        LINK.setOptional();
      
    }

    private void loadRowWithComments(EcvTableData data, EcvXmlTextElement.CommentData commentData) {

        List<String> contentList = commentData.getContentList();
        List<String> commentList = commentData.getCommentList();

        int i;
        for (i = 0; i < commentList.size(); i++) {
            EcvTableRow newRow = data.createRow();
            REQUIREMENT.setValue(newRow, contentList.get(i).trim());
            LINK.setValue(newRow, commentList.get(i).trim());
            data.addRow(newRow);
        }
        if (i < contentList.size()) {
            if (contentList.get(i).trim().isEmpty() == false) {
                EcvTableRow newRow = data.createRow();
                REQUIREMENT.setValue(newRow, contentList.get(i).trim());
                data.addRow(newRow);
            }
        }
    }

    private void loadRowWithNewLine(EcvTableData data, EcvXmlTextElement rowTextElement) {
        String[] lines = rowTextElement.getText().split("\n");
        for (String line : lines) {
            String trimedLine = line.trim();
            if (trimedLine.isEmpty() == false) {
                EcvTableRow newRow = data.createRow();
                REQUIREMENT.setValue(newRow, trimedLine);
                data.addRow(newRow);
            }
        }
    }

    private void loadRowWithoutAttribute(EcvTableData data, EcvXmlTextElement rowTextElement) {
        EcvTableRow newRow = data.createRow();
        REQUIREMENT.setValue(newRow, rowTextElement.getText());
        data.addRow(newRow);
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    static public class Record {

        final private String requirement;
        final private String type;
        final private String status;
        final private String baseline;
        final private String baseline_type;
        final private String comment;
        final private String link;

        public Record(String requirement, String link) {
            assert (requirement != null);

            this.requirement = requirement.trim();
            this.type = "";
            this.status = "";
            this.baseline = "";
            this.baseline_type = "";
            this.comment = "";
            this.link = link != null ? link.trim() : "";
        }

        public Record(String requirement, String type, String status, String baseline, String baseline_type, String comment, String link) {
            assert (requirement != null);

            this.requirement = requirement.trim();
            this.type = type;
            this.status = status;
            this.baseline = baseline;
            this.baseline_type = baseline_type;
            this.comment = comment;
            this.link = link != null ? link.trim() : "";
        }

        public String getRequirement() {
            return requirement;
        }
        
        public String getType() {
            return type;
        }
        
        public String getStatus() {
            return status;
        }
        
        public String getBaseline() {
            return baseline;
        }

        public String getBaselineType() {
            return baseline_type;
        }
        
        public String getComment() {
            return comment;
        }

        public String getLink() {
            return link;
        }

        @Override
        public String toString() {
            if (comment == null || comment.isEmpty()) {
                return ("[" + requirement + "," + link + "]");
            } else {
                return ("[" + requirement + "," + comment + "," + link + "]");
            }
        }

    }

    static public List<Record> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_RequirementOnIRM);

        Rq1XmlTable_RequirementOnIRM d = (Rq1XmlTable_RequirementOnIRM) data.getDescription();

        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            String requirement = d.REQUIREMENT.getValue(row);
            String status = d.STATUS.getValue(row);
            String type = d.TYPE.getValue(row);
            String baseline = d.BASELINE.getValue(row);
            String baseline_type = d.BASELINE_TYPE.getValue(row);
            String comment = d.COMMENT.getValue(row);
            String link = d.LINK.getValue(row);
            result.add(new Record(requirement, type, status, baseline, baseline_type, comment, link));
        }
        return (result);
    }

    static public EcvTableData add(EcvTableData data, Record newRecord) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_RequirementOnIRM);
        assert (newRecord != null);

        Rq1XmlTable_RequirementOnIRM d = (Rq1XmlTable_RequirementOnIRM) data.getDescription();

        EcvTableRow newRow = data.createAndAddRow();
        d.REQUIREMENT.setValue(newRow, newRecord.getRequirement());
        d.TYPE.setValue(newRow, newRecord.getType());
        d.STATUS.setValue(newRow, newRecord.getStatus());
        d.BASELINE.setValue(newRow, newRecord.getBaseline());
        d.BASELINE_TYPE.setValue(newRow, newRecord.getBaselineType());
        d.COMMENT.setValue(newRow, newRecord.getComment());
        d.LINK.setValue(newRow, newRecord.getLink());

        return (data);
    }
}
