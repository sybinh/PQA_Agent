/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.EcvEnumeration;
import util.EcvTableColumnI.Visibility;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;

/**
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_ChangesToConfiguration extends Rq1XmlTable implements Rq1DatabaseField_Xml.Converter {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Rq1XmlTable_ChangesToConfiguration.class.getCanonicalName());

    static public enum Type implements EcvEnumeration {

        ADD_SYSTEM_CONSTANT("Add SC"),
        CONFIG("Config"),
        ENVBLK("EnvBlk"),
        MAPSIZE("MapSize"),
        REMOVE_SYSTEM_CONSTANT("Remove SC"),
        SCHED("Sched"),
        SONST("Sonst"),
        SWADP("Swadp"),
        SY("SY");

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

    }

    static final private String FIELDNAME_TYPE = "Type";
    static final private String FIELDNAME_NAME = "Name";
    static final private String FIELDNAME_BASE = "Base";
    static final private String FIELDNAME_TARGET = "Target";
    static final private String FIELDNAME_CUST_VISIBLE = "CustVisible";
    static final private String FIELDNAME_DISCUSSED = "Discussed";
    static final private String FIELDNAME_INTEGRATED = "Integrated";
    static final private String FIELDNAME_INTERNAL_DESCRIPTION = "Description";
    static final private String FIELDNAME_EXTERNAL_COMMENT = "Comment";
    //
    static final private String FIELDNAME_EXTERNAL_SYSTEM_CONSTANTS = "SystemConstants";
    static final private String FIELDNAME_EXTERNAL_PREDECESSOR_PROCESS = "Predecessor";
    static final private String FIELDNAME_EXTERNAL_SUCCESSOR_PROCESS = "Successor";
    static final private String FIELDNAME_EXTERNAL_SECTION = "Section";

    final public Rq1XmlTableColumn_ComboBox TYPE;
    final public Rq1XmlTableColumn_String NAME;
    final public Rq1XmlTableColumn_String BASE;
    final public Rq1XmlTableColumn_StringMultiline TARGET;
    final public Rq1XmlTableColumn_String INTERNAL_DESCRIPTION;
    final public Rq1XmlTableColumn_String EXTERNAL_COMMENT;
    final public Rq1XmlTableColumn_Set DERIVATIVE;
    final public Rq1XmlTableColumn_CheckBox CUST_VISIBLE;
    final public Rq1XmlTableColumn_CheckBox DISCUSSED;
    final public Rq1XmlTableColumn_CheckBox INTEGRATED;
    //
    final public Rq1XmlTableColumn_StringMultiline SYSTEM_CONSTANTS;
    final public Rq1XmlTableColumn_String PREDECESSOR_PROCESS;
    final public Rq1XmlTableColumn_String SUCCESSOR_PROCESS;
    final public Rq1XmlTableColumn_String SECTION;

    public Rq1XmlTable_ChangesToConfiguration() {

        addXmlColumn(TYPE = new Rq1XmlTableColumn_ComboBox("Type", 19, Type.values(), FIELDNAME_TYPE, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(NAME = new Rq1XmlTableColumn_String("Name", 15, FIELDNAME_NAME, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(BASE = new Rq1XmlTableColumn_String("Base", 10, FIELDNAME_BASE, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(TARGET = new Rq1XmlTableColumn_StringMultiline("Target", 20, FIELDNAME_TARGET, ColumnEncodingMethod.ATTRIBUTE));

        addXmlColumn(CUST_VISIBLE = new Rq1XmlTableColumn_CheckBox("CustVisible", 7, FIELDNAME_CUST_VISIBLE, ColumnEncodingMethod.ATTRIBUTE,
                "ja", "nein", new String[]{"yes"}, new String[]{"no", ""}));
        addXmlColumn(DISCUSSED = new Rq1XmlTableColumn_CheckBox("Discussed", 7, FIELDNAME_DISCUSSED, ColumnEncodingMethod.ATTRIBUTE,
                "ja", "nein", new String[]{"yes"}, new String[]{"no", ""}));
        addXmlColumn(INTEGRATED = new Rq1XmlTableColumn_CheckBox("Integrated", 6, FIELDNAME_INTEGRATED, ColumnEncodingMethod.ATTRIBUTE,
                "ja", "nein", new String[]{"yes"}, new String[]{"no", ""}));

        addXmlColumn(DERIVATIVE = new Rq1XmlTableColumn_Set("Derivative", 20, "Derivate", ColumnEncodingMethod.ELEMENT_LIST));
        addXmlColumn(INTERNAL_DESCRIPTION = new Rq1XmlTableColumn_String("Internal Description", 50, FIELDNAME_INTERNAL_DESCRIPTION, ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(EXTERNAL_COMMENT = new Rq1XmlTableColumn_String("External Comment", 50, FIELDNAME_EXTERNAL_COMMENT, ColumnEncodingMethod.ATTRIBUTE));

        addXmlColumn(SYSTEM_CONSTANTS = new Rq1XmlTableColumn_StringMultiline("System Constants", 20, FIELDNAME_EXTERNAL_SYSTEM_CONSTANTS, ColumnEncodingMethod.ATTRIBUTE));
        SYSTEM_CONSTANTS.setOptional();
        addXmlColumn(PREDECESSOR_PROCESS = new Rq1XmlTableColumn_String("Predecessor Process", 10, FIELDNAME_EXTERNAL_PREDECESSOR_PROCESS, ColumnEncodingMethod.ATTRIBUTE));
        PREDECESSOR_PROCESS.setOptional();
        addXmlColumn(SUCCESSOR_PROCESS = new Rq1XmlTableColumn_String("Successor Process", 10, FIELDNAME_EXTERNAL_SUCCESSOR_PROCESS, ColumnEncodingMethod.ATTRIBUTE));
        SUCCESSOR_PROCESS.setOptional();
        addXmlColumn(SECTION = new Rq1XmlTableColumn_String("Section", 10, FIELDNAME_EXTERNAL_SECTION, ColumnEncodingMethod.ATTRIBUTE));
        SECTION.setOptional();

        BASE.setVisibility(Visibility.DEFAULT_VISIBLE);
        TARGET.setVisibility(Visibility.DEFAULT_VISIBLE);

        CUST_VISIBLE.setVisibility(Visibility.DEFAULT_VISIBLE);
        DISCUSSED.setVisibility(Visibility.DEFAULT_VISIBLE);
        INTEGRATED.setVisibility(Visibility.DEFAULT_VISIBLE);

        DERIVATIVE.setVisibility(Visibility.DEFAULT_VISIBLE);
        INTERNAL_DESCRIPTION.setVisibility(Visibility.DEFAULT_VISIBLE);
        EXTERNAL_COMMENT.setVisibility(Visibility.DEFAULT_VISIBLE);

        SYSTEM_CONSTANTS.setVisibility(Visibility.DEFAULT_HIDDEN);
        PREDECESSOR_PROCESS.setVisibility(Visibility.DEFAULT_HIDDEN);
        SUCCESSOR_PROCESS.setVisibility(Visibility.DEFAULT_HIDDEN);
        SECTION.setVisibility(Visibility.DEFAULT_HIDDEN);
    }

    //--------------------------------------------------------------------------
    //
    // Support for alternativ field content
    //
    //--------------------------------------------------------------------------
    @Override
    public EcvXmlContainerElement convertContentToXml(String dbValue) {
        assert (dbValue != null);
        assert (dbValue.isEmpty() == false);

        EcvXmlContainerElement result = convertStandardRq1Format(dbValue);
        if (result != null) {
            return (result);
        }

        result = convertForECD(dbValue);
        if (result != null) {
            return (result);
        }

        return (convertFreeText(dbValue));
    }

    //--------------------------------------------------------------------------
    //
    // Support for standard RQ1 format
    //
    //--------------------------------------------------------------------------
    final static private Pattern rq1StandardFormatPattern = Pattern.compile(".*\\[(\\+|m)\\] *([a-zA-Z0-9_]+) *\\= ?(.+) *");

    private EcvXmlContainerElement convertStandardRq1Format(String dbValue) {
        assert (dbValue != null);
        assert (dbValue.isEmpty() == false);

        EcvXmlContainerElement result = new EcvXmlContainerElement("convertContentToXml");

        String[] lines = dbValue.split("\n");
        boolean ok = true;
        int lineNo = 1;
        for (String line : lines) {
            String trimedLine = line.trim();
            if (trimedLine.isEmpty() == false) {
                Matcher matcher = rq1StandardFormatPattern.matcher(trimedLine);
                if (matcher.matches() == true) {
                    String action = matcher.group(1);
                    String name = matcher.group(2);
                    String value = matcher.group(3);

                    result.addElement(buildStandardRq1Entry(trimedLine, action, name, value));
                } else {
                    logger.warning("No match at line " + lineNo);
                    ok = false;
                }
            }
            lineNo++;
        }

        if (ok == true) {
            return (result);
        }

        return (null);
    }

    /**
     * Creates an entry for the RQ1 standard format
     *
     * @param description Line that was parsed. Used for the internal
     * description.
     * @param action Action from the line. Is ignored.
     * @param name Name of the parameter.
     * @param value New Value for the parameter
     * @return
     */
    private EcvXmlElement buildStandardRq1Entry(String line, String action, String name, String value) {

        EcvXmlEmptyElement entry = new EcvXmlEmptyElement("ChangeConfig");

        entry.addAttribute(FIELDNAME_TYPE, Type.CONFIG.getText());
        entry.addAttribute(FIELDNAME_NAME, name);
        entry.addAttribute(FIELDNAME_BASE, "");
        entry.addAttribute(FIELDNAME_TARGET, value);
        entry.addAttribute(FIELDNAME_CUST_VISIBLE, "");
        entry.addAttribute(FIELDNAME_DISCUSSED, "");
        entry.addAttribute(FIELDNAME_INTEGRATED, "");
        entry.addAttribute(FIELDNAME_INTERNAL_DESCRIPTION, line);
        entry.addAttribute(FIELDNAME_EXTERNAL_COMMENT, "");

        return (entry);
    }

    //--------------------------------------------------------------------------
    //
    // Support for ECD template
    //
    //--------------------------------------------------------------------------
    final static private Set<String> linesToIgnoreForECD = new TreeSet<>(Arrays.asList(
            "#Changes to Configuration necessary?",
            "YES",
            "NO",
            "YES / NO",
            "# Configuration",
            "Add Configuration here",
            "# find values of system constants here:",
            "# https://inside-ilm.bosch.com/irj/go/nui/sid/20c7ebb8-a790-3110-2abd-ddb177ae32b7",
            "# Syntax",
            "# [+] add to configuration",
            "# [-] remove from configuraiton",
            "# [m] modify configuration"
    ));

    final static private Pattern ecdSimpleAssignmentPattern = Pattern.compile("([a-zA-Z0-9_]+) *\\=+ ?(.+)");
    final static private Pattern ecdRq1WithoutValuePattern = Pattern.compile(".*\\[(\\+|m)\\] *([a-zA-Z0-9_]+) *$");
    final static private Pattern ecdRq1WithDoubleEqualPattern = Pattern.compile(".*\\[(\\+|m)\\] *([a-zA-Z0-9_]+) *\\=+ ?(.+) *");

    private EcvXmlContainerElement convertForECD(String dbValue) {

        EcvXmlContainerElement result = new EcvXmlContainerElement("convertForECD");
        boolean ok = true;

        String lines[] = dbValue.split("\n");

        int lineNo = 1;
        for (String line : lines) {
            String trimedLine = line.trim();
            if ((trimedLine.isEmpty() == false) && (linesToIgnoreForECD.contains(trimedLine) == false)) {

                Matcher ecdSimpleAssignmentMatcher = ecdSimpleAssignmentPattern.matcher(trimedLine);
                Matcher ecdPseudoRq1Matcher = ecdRq1WithoutValuePattern.matcher(trimedLine);
                Matcher ecdWithDoubleEqualMatcher = ecdRq1WithDoubleEqualPattern.matcher(trimedLine);
                if (ecdSimpleAssignmentMatcher.matches() == true) {
                    String action = "";
                    String name = ecdSimpleAssignmentMatcher.group(1);
                    String value = ecdSimpleAssignmentMatcher.group(2);
                    result.addElement(buildStandardRq1Entry(trimedLine, action, name, value));
                } else if (ecdPseudoRq1Matcher.matches() == true) {
                    String action = ecdPseudoRq1Matcher.group(1);;
                    String name = ecdPseudoRq1Matcher.group(2);
                    String value = "";
                    result.addElement(buildStandardRq1Entry(trimedLine, action, name, value));
                } else if (ecdWithDoubleEqualMatcher.matches() == true) {
                    String action = ecdWithDoubleEqualMatcher.group(1);
                    String name = ecdWithDoubleEqualMatcher.group(2);
                    String value = ecdWithDoubleEqualMatcher.group(3);
                    result.addElement(buildStandardRq1Entry(trimedLine, action, name, value));
                } else {
                    logger.warning("No match at line " + lineNo);
                    ok = false;
                    break;
                }

            }
            lineNo++;
        }

        if (ok == true) {
            return (result);
        } else {
            return (null);
        }

    }

    //--------------------------------------------------------------------------
    //
    // Support for free text
    //
    //--------------------------------------------------------------------------
    private EcvXmlContainerElement convertFreeText(String dbValue) {

        EcvXmlContainerElement result = new EcvXmlContainerElement("convertFreeText");

        String lines[] = dbValue.split("\n");

        int lineNo = 1;
        for (String line : lines) {
            String trimedLine = line.trim();
            if (trimedLine.isEmpty() == false) {
                result.addElement(buildFreeTextEntry(trimedLine, lineNo));
            }
            lineNo++;
        }

        return (result);
    }

    private EcvXmlElement buildFreeTextEntry(String line, int lineNo) {

        EcvXmlEmptyElement entry = new EcvXmlEmptyElement("ChangeConfig");

        entry.addAttribute(FIELDNAME_TYPE, Type.SONST.getText());
        entry.addAttribute(FIELDNAME_NAME, "Line " + lineNo);
        entry.addAttribute(FIELDNAME_BASE, "");
        entry.addAttribute(FIELDNAME_TARGET, line);
        entry.addAttribute(FIELDNAME_CUST_VISIBLE, "");
        entry.addAttribute(FIELDNAME_DISCUSSED, "");
        entry.addAttribute(FIELDNAME_INTEGRATED, "");
        entry.addAttribute(FIELDNAME_INTERNAL_DESCRIPTION, "Converted from line " + lineNo + " of free text description.");
        entry.addAttribute(FIELDNAME_EXTERNAL_COMMENT, "");

        return (entry);
    }

}
