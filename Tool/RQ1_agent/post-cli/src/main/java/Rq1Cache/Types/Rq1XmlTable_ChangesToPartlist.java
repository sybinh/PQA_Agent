/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import Rq1Data.Enumerations.CtpClassification;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.EcvEnumeration;
import util.EcvEnumerationValue;
import util.EcvTableData;
import util.EcvTableRow;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_ChangesToPartlist extends Rq1XmlTable implements Rq1DatabaseField_Xml.Converter {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rq1XmlTable_ChangesToPartlist.class.getCanonicalName());

    static public enum Type implements EcvEnumeration {

        BC("BC"),
        BCC("BCC"),
        BC_MO("BC_MO"),
        BX("BX"),
        CEL("CEL"),
        CC("CC"),
        DX("DX"),
        FC("FC"),
        FCGEN("FCGEN"),
        FX("FX"),
        FY("FY"),
        FC_ARA("FC-ARA"),
        FC_ARB("FC-ARB"),
        FCC("FCC"),
        FCC_AR("FCC-AR"),
        FCL("FCL"),
        FCP("FCP"),
        FOS("FOS"),
        GC("GC"),
        GC_MO("GC_MO"),
        IC("IC"),
        MC("MC"),
        MISC("MISC"),
        SC("SC"),
        TEST("TEST");
        //
        private final String dbText;

        private Type(String dbText) {
            assert (dbText != null);
            this.dbText = dbText;
        }

        @Override
        public String getText() {
            return dbText;
        }

        public static String getTypePattern() {
            StringBuilder result = new StringBuilder();

            result.append("(");
            boolean first = true;
            for (Type value : values()) {
                if (first == false) {
                    result.append("|");

                }
                first = false;
                result.append(value.getText());
            }
            result.append(")");

            return (result.toString());
        }

    }

    static public enum State implements EcvEnumeration {

        EMPTY("") {
            @Override
            public String toString() {
                return ("<Empty>");
            }
        },
        NEW("New"),
        REQUESTED("Requested"),
        Planned("Planned"),
        IMPLEMENTED("Implemented"),
        CLOSED("Closed"),
        CANCELED("Canceled");

        private final String text;

        private State(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return (text);
        }

    }

    static final private String FIELDNAME_TYPE = "Type";
    static final private String FIELDNAME_NAME = "Name";
    static final private String FIELDNAME_BASE = "Base";
    static final private String FIELDNAME_TARGET = "Target";
    static final private String FIELDNAME_DEPEND = "Depend";
    static final private String FIELDNAME_INTERNAL_DESCRIPTION = "Comment";
    static final private String FIELDNAME_DERIVATIVE = "Derivate";
    static final private String FIELDNAME_STATE = "State";

    static public enum FcActive implements EcvEnumeration {

        ACTIVE("Active"),
        DEACTIVE("Deactivate"),
        NOTDEFINED("NotDefined");

        private final String active;

        private FcActive(String active) {
            assert (active != null);
            this.active = active;
        }

        @Override
        public String getText() {
            return active;
        }
    }

    final public Rq1XmlTableColumn_ComboBox TYPE;
    final public Rq1XmlTableColumn_String NAME;
    final public Rq1XmlTableColumn_String BASE;
    final public Rq1XmlTableColumn_StringMultiline TARGET;
    final public Rq1XmlTableColumn_String SY_CONDITION;
    final public Rq1XmlTableColumn_ComboBox CLASSIFICATION;
    final public Rq1XmlTableColumn_Set DERIVATIVE;
    final public Rq1XmlTableColumn_String INTERNAL_DESCRIPTION;
    final public Rq1XmlTableColumn_String EXTERNAL_COMMENT;
    final public Rq1XmlTableColumn_ComboBox STATE;

    public Rq1XmlTable_ChangesToPartlist() {

        addXmlColumn(TYPE = new Rq1XmlTableColumn_ComboBox("Type", 15, Type.values(), FIELDNAME_TYPE, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 15, FIELDNAME_NAME, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(BASE = new Rq1XmlTableColumn_String("Base", 10, FIELDNAME_BASE, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(TARGET = new Rq1XmlTableColumn_StringMultiline("Target", 20, FIELDNAME_TARGET, ColumnEncodingMethod.ATTRIBUTE));

        addXmlColumn(SY_CONDITION = new Rq1XmlTableColumn_String("SY Condition", 20, FIELDNAME_DEPEND, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(CLASSIFICATION = new Rq1XmlTableColumn_ComboBox("Classification", 14, CtpClassification.values(), "Classification", ColumnEncodingMethod.ATTRIBUTE));
        CLASSIFICATION.setOptional();
        addXmlColumn(DERIVATIVE = new Rq1XmlTableColumn_Set("Derivative", 20, "Derivate", ColumnEncodingMethod.ELEMENT_LIST));
        DERIVATIVE.setOptional();

        addXmlColumn(INTERNAL_DESCRIPTION = new Rq1XmlTableColumn_String("Internal Description", 50, FIELDNAME_INTERNAL_DESCRIPTION, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(EXTERNAL_COMMENT = new Rq1XmlTableColumn_String("External Comment", 50, "ExternalComment", ColumnEncodingMethod.ATTRIBUTE));
        EXTERNAL_COMMENT.setOptional();

        addXmlColumn(STATE = new Rq1XmlTableColumn_ComboBox("State", 20, State.values(), FIELDNAME_STATE, ColumnEncodingMethod.ATTRIBUTE));
        STATE.setOptional();
    }

    static public class Record {

        final private EcvTableRow row;
        private boolean deleted;
        //
        final private EcvEnumeration type;
        final private String name;
        final private String base;
        final private String target;
        final private String syCondition;
        private String classification;
        private SortedSet<String> derivative;
        final private String internalDescription;
        private String externalComment;
        final private EcvEnumeration state;

        @SuppressWarnings("unchecked")
        private Record(EcvTableData data, EcvTableRow row) {
            assert (data != null);
            assert (row != null);
            this.row = row;
            this.deleted = false;
            Rq1XmlTable_ChangesToPartlist d = (Rq1XmlTable_ChangesToPartlist) data.getDescription();

            type = EcvEnumerationValue.createFromList(d.TYPE.getValue(row), Type.values());
            name = d.NAME.getValue(row);
            base = d.BASE.getValue(row);
            //
            // Ensure that target is never null
            //
            String targetNull = d.TARGET.getValue(row);
            if (targetNull != null) {
                target = targetNull;
            } else {
                target = "";
            }
            classification = d.CLASSIFICATION.getValue(row);
            syCondition = d.SY_CONDITION.getValue(row);
            derivative = d.DERIVATIVE.getValue(row);
            internalDescription = d.INTERNAL_DESCRIPTION.getValue(row);
            externalComment = d.EXTERNAL_COMMENT.getValue(row);
            state = EcvEnumerationValue.createFromList(d.STATE.getValue(row), State.values());
        }

        public Record(Type type, String name, String target) {
            assert (type != null);
            assert (name != null);
            assert (name.isEmpty() == false);
            assert (target != null);
            assert (target.isEmpty() == false);

            this.row = null;
            this.deleted = false;

            this.type = type;
            this.name = name;
            base = null;
            this.target = target;
            classification = null;
            syCondition = null;
            derivative = new TreeSet<String>();
            internalDescription = null;
            externalComment = null;
            state = null;
        }

        private EcvTableRow getRow() {
            return (row);
        }

        private boolean fillRow(Rq1XmlTable_ChangesToPartlist d, EcvTableRow row) {
            boolean changed = false;
            changed |= d.TYPE.setValue(row, type.getText());
            changed |= d.NAME.setValue(row, name);
            changed |= d.BASE.setValue(row, base);
            changed |= d.TARGET.setValue(row, target);
            changed |= d.CLASSIFICATION.setValue(row, classification);
            changed |= d.SY_CONDITION.setValue(row, syCondition);
            changed |= d.DERIVATIVE.setValue(row, derivative);
            changed |= d.INTERNAL_DESCRIPTION.setValue(row, internalDescription);
            changed |= d.EXTERNAL_COMMENT.setValue(row, externalComment);
            changed |= d.STATE.setValue(row, state);
            return (changed);
        }

        /**
         * Returns the type of the part. Should be one of the values defined for
         * Type. But it might be, that it contains other values when wrong
         * entries exist in the RQ1 database.
         *
         * @return
         */
        public EcvEnumeration getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getTarget() {
            return target;
        }

        public String getClassification() {
            return classification;
        }

        public SortedSet<String> getDerivative() {
            return derivative;
        }

        public String getInternalDescription() {
            return internalDescription;
        }

        public String getExternalComment() {
            return externalComment;
        }

        public boolean isDeleted() {
            return (deleted);
        }

        public void setClassification(String classification) {
            this.classification = classification;
        }

        public void setDerivative(SortedSet<String> derivative) {
            assert (derivative != null);
            this.derivative = derivative;
        }

        public void setExternalComment(String text) {
            assert (text != null);
            externalComment = text;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

    }

    static public List<Record> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_ChangesToPartlist);

        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            result.add(new Record(data, row));
        }
        return (result);
    }

    static public boolean update(EcvTableData data, List<Record> newValues) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_ChangesToPartlist);
        assert (newValues != null);

        boolean changed = false;

        for (Record record : newValues) {
            if (record.getRow() != null) {
                if (record.isDeleted() == true) {
                    data.removeRow(record.getRow());
                    changed = true;
                } else {
                    changed |= record.fillRow((Rq1XmlTable_ChangesToPartlist) data.getDescription(), record.getRow());
                }

            } else {
                if (record.isDeleted() == false) {
                    EcvTableRow newRow = data.createRow();
                    record.fillRow((Rq1XmlTable_ChangesToPartlist) data.getDescription(), newRow);
                    data.addRow(newRow);
                    changed = true;
                }
            }
        }
        return (changed);
    }

    final static private Pattern rq1PartListPattern = Pattern.compile(".*\\[(\\+|\\-|m)\\] *(SDOM)? ?\\:? *"
            + Type.getTypePattern()
            + " ?\\: +([a-zA-Z0-9_]+) +\\/? ?([0-9\\\\._]+(\\; *[0-9]*)?) *(\\(.*\\))?.*");

    @Override
    public EcvXmlContainerElement convertContentToXml(String dbValue) {
        assert (dbValue != null);
        assert (dbValue.isEmpty() == false);

        EcvXmlContainerElement result = new EcvXmlContainerElement("convertContentToXml");

        String[] lines = dbValue.split("\r?\n");
        boolean ok = true;
        int lineNo = 1;
        for (String line : lines) {
            if (line.contains("<ChangePart") == true) {
                // Prevent decoding of partly converted values.
                return (null);
            }
            Matcher matcher = rq1PartListPattern.matcher(line);
            if (matcher.matches() == true) {
                String action = matcher.group(1);
                String type = matcher.group(3);
                String name = matcher.group(4);
                String version = matcher.group(5);
                String derivatives = matcher.group(7);

                result.addElement(buildEntry(line, action, type, name, version, derivatives));
            } else {
                logger.warning("No match at line " + lineNo);
                ok = false;
            }
            lineNo++;
        }

        if (ok == true) {
            return (result);
        }

        return (null);
    }

    private EcvXmlElement buildEntry(String line, String action, String type, String name, String version, String derivatives) {

        EcvXmlElement entry = new EcvXmlEmptyElement("ChangePart");

        if (derivatives != null) {
            EcvXmlContainerElement container = new EcvXmlContainerElement("ChangePart");
            String derivativeArray[] = derivatives.substring(1, derivatives.length() - 1).split(",");
            for (String d : derivativeArray) {
                String t = d.trim();
                if (t.isEmpty() == false) {
                    container.addElement(new EcvXmlTextElement("Derivate", t));
                }
            }
            if (container.isEmpty() == false) {
                entry = container;
            }
        }

        entry.addAttribute(FIELDNAME_INTERNAL_DESCRIPTION, line);

//        if (type.equals("SC") == true) {
//            type = "BC";
//        }
        entry.addAttribute(FIELDNAME_TYPE, type);

        entry.addAttribute(FIELDNAME_NAME, name);

        if (action.equals("-") == true) {
            entry.addAttribute(FIELDNAME_BASE, version);
            entry.addAttribute(FIELDNAME_TARGET, "");
        } else {
            entry.addAttribute(FIELDNAME_BASE, "");
            entry.addAttribute(FIELDNAME_TARGET, version);
        }

        entry.addAttribute(FIELDNAME_DEPEND, "");

        return (entry);
    }

    /**
     * Returns the status of a FC from the CTP.
     *
     * @param data CTP data.
     * @param fcName Name of the FC.
     * @param fcVersion Version of the FC.
     * @return State from CTP.
     */
    final static public FcActive getFcStatus(EcvTableData data, String fcName, String fcVersion) {
        Rq1XmlTable_ChangesToPartlist ctp = (Rq1XmlTable_ChangesToPartlist) data.getDescription();

        for (EcvTableRow row : data.getRows()) {
            if (row.getValueAt(ctp.TYPE).equals(Type.FC.getText())
                    && row.getValueAt(ctp.NAME).equals(fcName)
                    && row.getValueAt(ctp.TARGET).equals(fcVersion)) {
                if (row.getValueAt(ctp.CLASSIFICATION) != null) {
                    if (row.getValueAt(ctp.CLASSIFICATION).equals(CtpClassification.ACTIVATE.getText())) {
                        return FcActive.ACTIVE;
                    } else if (row.getValueAt(ctp.CLASSIFICATION).equals(CtpClassification.DEACTIVATE.getText())) {
                        return FcActive.DEACTIVE;
                    }
                }
            }
        }
        return FcActive.NOTDEFINED;
    }

    /**
     * Returns the status of a FC from the CTP for the IPE. For the IPE, the FC
     * is recognized as activated, if it is activated for at least one
     * derivative.
     *
     * @param data CTP data.
     * @param fcName Name of the FC.
     * @param fcVersion Version of the FC.
     * @return State from CTP.
     */
    final static public FcActive getFcStatusForIpe(EcvTableData data, String fcName, String fcVersion) {
        Rq1XmlTable_ChangesToPartlist ctp = (Rq1XmlTable_ChangesToPartlist) data.getDescription();

        FcActive result = FcActive.NOTDEFINED;

        for (EcvTableRow row : data.getRows()) {
            if (row.getValueAt(ctp.TYPE).equals(Type.FC.getText())
                    && row.getValueAt(ctp.NAME).equals(fcName)
                    && row.getValueAt(ctp.TARGET).equals(fcVersion)) {
                if (row.getValueAt(ctp.CLASSIFICATION) != null) {
                    if (row.getValueAt(ctp.CLASSIFICATION).equals(CtpClassification.ACTIVATE.getText())) {
                        //
                        // Any activation makes the FC active on the GUI, regardless of the derivative settings.
                        //
                        return FcActive.ACTIVE;
                    } else if (row.getValueAt(ctp.CLASSIFICATION).equals(CtpClassification.DEACTIVATE.getText())) {
                        result = FcActive.DEACTIVE;
                    }
                }
            }
        }

        return result;
    }

    final static public FcActive getFcStatusOneRow(EcvTableRow row, EcvTableData data, String fcName, String fcVersion) {
        Rq1XmlTable_ChangesToPartlist ctp = (Rq1XmlTable_ChangesToPartlist) data.getDescription();

        if (row.getValueAt(ctp.TYPE).equals(Type.FC.getText())
                && row.getValueAt(ctp.NAME).equals(fcName)
                && row.getValueAt(ctp.TARGET).equals(fcVersion)) {
            if (row.getValueAt(ctp.CLASSIFICATION) != null) {
                if (row.getValueAt(ctp.CLASSIFICATION).equals(CtpClassification.ACTIVATE.getText())) {
                    return FcActive.ACTIVE;
                } else if (row.getValueAt(ctp.CLASSIFICATION).equals(CtpClassification.DEACTIVATE.getText())) {
                    return FcActive.DEACTIVE;
                }
            }
        }
        return FcActive.NOTDEFINED;
    }
}
