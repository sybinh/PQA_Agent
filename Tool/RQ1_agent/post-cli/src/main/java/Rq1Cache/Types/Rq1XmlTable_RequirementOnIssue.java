/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
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
public class Rq1XmlTable_RequirementOnIssue extends Rq1XmlTable {

    final static public String ATTRIBUTE_NAME_STATUS = "status";
    final static public String ATTRIBUTE_NAME_BASELINE = "baseline";
    final static public String ATTRIBUTE_NAME_BASELINE_TYPE = "baselinetype";
    final static public String ATTRIBUTE_NAME_LINK = "link";
    final static public String ATTRIBUTE_NAME_TYPE = "type";

    final public Rq1XmlTableColumn_String REQUIREMENT;
    final public Rq1XmlTableColumn_String STATUS;
    final public Rq1XmlTableColumn_String TYPE;
    final public Rq1XmlTableColumn_String BASELINE;
    final public Rq1XmlTableColumn_String BASELINE_TYPE;
    final public Rq1XmlTableColumn_Link LINK;

    public Rq1XmlTable_RequirementOnIssue() {

        addXmlColumn(REQUIREMENT = new Rq1XmlTableColumn_String("Requirement", 30, "Requirement", ColumnEncodingMethod.CONTENT));
        REQUIREMENT.setOptional();
        addXmlColumn(STATUS = new Rq1XmlTableColumn_String("Status", 10, ATTRIBUTE_NAME_STATUS, ColumnEncodingMethod.ATTRIBUTE));
        STATUS.setOptional();
        addXmlColumn(TYPE = new Rq1XmlTableColumn_String("Type", 10, ATTRIBUTE_NAME_TYPE, ColumnEncodingMethod.ATTRIBUTE));
        TYPE.setOptional();
        addXmlColumn(BASELINE = new Rq1XmlTableColumn_String("Baseline", 12, ATTRIBUTE_NAME_BASELINE, ColumnEncodingMethod.ATTRIBUTE));
        BASELINE.setOptional();
        addXmlColumn(BASELINE_TYPE = new Rq1XmlTableColumn_String("Baseline Type", 15, ATTRIBUTE_NAME_BASELINE_TYPE, ColumnEncodingMethod.ATTRIBUTE));
        BASELINE_TYPE.setOptional();
        addXmlColumn(LINK = new Rq1XmlTableColumn_Link("Link to DOORS (double click to open)", 40, ATTRIBUTE_NAME_LINK, ColumnEncodingMethod.ATTRIBUTE));
        LINK.setOptional();

    }

    @Override
    public void loadRowFromDb(EcvTableData data, EcvXmlElement rowElement) throws Rq1ParseFieldException {
        assert (data != null);
        assert (rowElement != null);

        //
        // Check that rowElement is an text element
        //
        EcvXmlTextElement rowTextElement;
        if (rowElement instanceof EcvXmlTextElement) {
            rowTextElement = (EcvXmlTextElement) rowElement;
        } else if (rowElement instanceof EcvXmlEmptyElement) {
            if (rowElement.hasAttribute(ATTRIBUTE_NAME_LINK)) {
                // This format matches the default definition for this class, even with empty name for requirement.
                super.loadRowFromDb(data, rowElement);
            }
            //
            // An empty list is ok. Added for ECVTOOL-3332
            //
            return;
        } else {
            StringBuilder b = new StringBuilder(100);
            b.append("Error when parsing field element ").append(rowElement.getName()).append(": Wrong type ").append(rowElement.getClass().getCanonicalName());
            throw (new Rq1ParseFieldException(b.toString()));
        }

        if (rowTextElement.getCommentData() != null) {
            loadRowWithComments(data, rowTextElement.getCommentData());
        } else if (rowTextElement.getText().contains("\n")) {
            loadRowWithNewLine(data, rowTextElement);
        } else if (rowTextElement.hasAttribute(ATTRIBUTE_NAME_LINK)) {
            // This format matches the default definition for this class.
            super.loadRowFromDb(data, rowElement);
        } else {
            loadRowWithoutAttribute(data, rowTextElement);
        }

        //
        // Convert doors links
        // 
        for (EcvTableRow row : data.getRows()) {
            String convertedLink = convertDoorsLinkToHttpLink(LINK.getValue(row));
            if (convertedLink != null) {
                LINK.setValue(row, convertedLink);
            }
        }
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

//    private void loadRowWithAttribute(EcvTableData data, EcvXmlTextElement rowTextElement) {
//        EcvTableRow newRow = data.createRow();
//        newRow.setValueAt(REQUIREMENT, rowTextElement.getText());
//        if (rowTextElement.hasAttribute(ATTRIBUTE_NAME_BASELINE) == true) {
//            BASELINE.setValue(newRow, rowTextElement.getAttribute(ATTRIBUTE_NAME_BASELINE).trim());
//        }
//        newRow.setValueAt(LINK, rowTextElement.getAttribute(ATTRIBUTE_NAME_LINK).trim().replaceAll("\n", ""));
//        data.addRow(newRow);
//    }
    private void loadRowWithoutAttribute(EcvTableData data, EcvXmlTextElement rowTextElement) {
        EcvTableRow newRow = data.createRow();
        REQUIREMENT.setValue(newRow, rowTextElement.getText());
        data.addRow(newRow);
    }

    static String convertDoorsLinkToHttpLink(String doorsLink) {
        if (doorsLink == null) {
            return (null);
        }

        //
        // Convert only links from specific doors server
        //
        if (doorsLink.startsWith("doors://fe-dorapcm3.de.bosch.com:36679/") == false) {
            return (null);
        }

        // 
        // Extract view id
        //
        int indexViewStart = doorsLink.indexOf("view=", 0);
        if (indexViewStart < 0) {
            return (null);
        }
        int indexViewEnd = doorsLink.indexOf("&", indexViewStart);
        if (indexViewEnd < 0) {
            return (null);
        }
        String viewId = doorsLink.substring(indexViewStart + 5, indexViewEnd);

        //
        // Extract urn
        //
        int indexUrnStart = doorsLink.indexOf("urn=urn:telelogic:", 0);
        if (indexUrnStart < 0) {
            return (null);
        }
        String urn = doorsLink.substring(indexUrnStart + 18).trim();

        return ("https://rb-alm-05-p-dwa.de.bosch.com:8443/dwa/rm/urn:rational:" + urn + "?doors.view=" + viewId);
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    static public class Record {

        final private String requirement;
        final private String type;
        final private String baseline;
        final private String link;

        public Record(String requirement, String type, String baseline, String link) {
            assert (requirement != null);

            this.requirement = requirement.trim();
            this.type = type;
            this.baseline = baseline;
            this.link = link != null ? link.trim() : "";
        }

        public String getRequirement() {
            return requirement;
        }

        public String getType() {
            return type;
        }

        public String getBaseline() {
            return baseline;
        }

        public String getLink() {
            return link;
        }

        @Override
        public String toString() {
            if (baseline == null || baseline.isEmpty()) {
                return ("[" + requirement + "," + link + "]");
            } else {
                return ("[" + requirement + "," + baseline + "," + link + "]");
            }
        }

    }

    static public List<Record> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_RequirementOnIssue);

        Rq1XmlTable_RequirementOnIssue d = (Rq1XmlTable_RequirementOnIssue) data.getDescription();

        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            String requ = (String) row.getValueAt(d.REQUIREMENT);
            String type = (String) row.getValueAt(d.TYPE);
            String bl = d.BASELINE.getValue(row);
            String link = (String) row.getValueAt(d.LINK);
            result.add(new Record(requ, type, bl, link));
        }
        return (result);
    }

    static public EcvTableData pack(List<Record> values) {
        assert (values != null);

        Rq1XmlTable_RequirementOnIssue d = new Rq1XmlTable_RequirementOnIssue();
        EcvTableData result = d.createTableData();
        for (Record value : values) {
            if (value != null) {
                add(result, value);
            }
        }
        return (result);
    }

    static public EcvTableData add(EcvTableData data, Record newRecord) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_RequirementOnIssue);
        assert (newRecord != null);

        Rq1XmlTable_RequirementOnIssue d = (Rq1XmlTable_RequirementOnIssue) data.getDescription();

        EcvTableRow newRow = data.createAndAddRow();
        d.REQUIREMENT.setValue(newRow, newRecord.getRequirement());
        d.TYPE.setValue(newRow, newRecord.type);
        d.BASELINE.setValue(newRow, newRecord.getBaseline());
        d.LINK.setValue(newRow, newRecord.getLink());

        return (data);
    }
}
