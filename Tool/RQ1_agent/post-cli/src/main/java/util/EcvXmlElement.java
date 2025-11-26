/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author gug2wi
 */
abstract public class EcvXmlElement {

    static private CharSequence[][] xmlEncodeTable = {
        {"&", "&amp;"}, // &amp has to be the first element to encode and the last to decode when replace() is used !
        {"<", "&lt;"},
        {">", "&gt;"},
        {"\"", "&quot;"},
        {"'", "&apos;"},
        {"\r", "&#13;"}}; // CR

    static final private CharSequence[][] defaultXmlDecodeTable = {
        {"&", "&amp;"}, // &amp has to be the first element to encode and the last to decode when replace() is used !
        {"<", "&lt;"},
        {">", "&gt;"},
        {"\"", "&quot;"},
        {"'", "&apos;"},
        {"\r", "&#13;"},
        {"/", "%2F"},
        {":", "%3A"},
        {"=", "%3D"},
        {"<", "%3C"},
        {">", "%3E"},
        //
        //  {" ", "%20"},  line removed because of ECVTOOL-5576 - Link is invalid because of containing '%20'
        //                 This comment here remains to prevent a toggling of this line if some problem occur because of the removal.
        //
        {",", "%2C"},
        {"\"", "%22"},
        {"[", "%5B"},
        {"]", "%5D"},
        {"{", "%7B"},
        {"}", "%7D"}}; // CR

    static public String encodeXml(String clearText) {
        String xmlString = clearText;

        for (int i = 0; i < xmlEncodeTable.length; i++) {
            if (xmlEncodeTable[i][0].length() > 0) {
                xmlString = xmlString.replace(xmlEncodeTable[i][0], xmlEncodeTable[i][1]);
            }
        }
        return (xmlString);
    }

    static public String decodeXml(String xmlString) {
        return (decodeXml(xmlString, defaultXmlDecodeTable));
    }

    static public String decodeXml(String xmlString, CharSequence[][] xmlDecodeTable) {
        //
        // The following code was removed, becaus String.replace uses regular expressions for the replacement. This leads to a huge overhead in CPU and memory consumption.
        //
        //        String clearText = xmlString;
        //        for (int i = xmlSpecialCharacterTable.length - 1; i >= 0; i--) {
        //            clearText = clearText.replace(xmlSpecialCharacterTable[i][1], xmlSpecialCharacterTable[i][0]);
        //        }
        //        return (clearText);
        //
        int l_source = xmlString.length();
        char[] source = new char[l_source];
        char[] result = new char[l_source]; // result can only get shorter
        xmlString.getChars(0, l_source, source, 0);

        int p_source = 0;
        int p_result = 0;
        while (p_source < l_source) {
            if ((source[p_source] != '&') && (source[p_source] != '%')) {
                result[p_result++] = source[p_source++];
            } else {
                CharSequence[] matchingMap = null;
                for (CharSequence[] currentMap : xmlDecodeTable) {
                    boolean mapMatch = true;
                    for (int i = 0; i < currentMap[1].length(); i++) {
                        if ((p_source + i >= l_source) || (currentMap[1].charAt(i) != source[p_source + i])) {
                            mapMatch = false;
                            break;
                        }
                    }
                    if (mapMatch == true) {
                        matchingMap = currentMap;

                        break;
                    }
                }
                if (matchingMap != null) {
                    for (int i = 0; i < matchingMap[0].length(); i++) {
                        result[p_result + i] = matchingMap[0].charAt(i);
                    }
                    p_result += matchingMap[0].length();
                    p_source += matchingMap[1].length();
                } else {
                    result[p_result++] = source[p_source++];
                }
            }
        }

        return (new String(result, 0, p_result));
    }
    //
    final private String elementName;
    final private Map<String, String> attributes;
    private EcvXmlElement parent;

    protected EcvXmlElement(String elementName) {
        assert (elementName != null);
        assert (elementName.isEmpty() == false);

        this.elementName = elementName;
        this.attributes = new TreeMap<>();
        this.parent = null;
    }

    protected EcvXmlElement(String elementName, Map<String, String> attributes) {
        assert (elementName != null);
        assert (elementName.isEmpty() == false);
        assert (attributes != null);

        this.elementName = elementName;
        this.attributes = attributes;
        this.parent = null;
    }

    /**
     * Create a copy of the provided source element.
     *
     * @param source The original that shall be copied.
     */
    protected EcvXmlElement(EcvXmlElement source) {
        assert (source != null);
        this.elementName = source.elementName;
        this.attributes = new TreeMap<>();
        for (Map.Entry<String, String> a : source.attributes.entrySet()) {
            this.attributes.put(a.getKey(), a.getValue());
        }
        this.parent = null;
    }

    /**
     * Create a copy of the provided source element except for the element name.
     * The element name shall be set to the new element Name.
     *
     * @param source The original that shall be copied.
     * @param elementName Element name for the copy.
     */
    protected EcvXmlElement(EcvXmlElement source, String elementName) {
        assert (source != null);
        assert (elementName != null);
        assert (elementName.isEmpty() == false);

        this.elementName = elementName;
        this.attributes = new TreeMap<>();
        for (Map.Entry<String, String> a : source.attributes.entrySet()) {
            this.attributes.put(a.getKey(), a.getValue());
        }
        this.parent = null;
    }

    protected void setParent(EcvXmlElement parent) {
        assert (parent != null);
        this.parent = parent;
    }

    public String getName() {
        return (elementName);
    }

    /**
     * Returns the full name of the element. The full name is the name of all
     * it's parents separated by dots followed by the tag name separated to the
     * parents aso with a dot. This method is mainly added for debugging
     * reasons.
     *
     * @return The full name (path plus name) of the element.
     */
    public String getFullName() {
        if (parent != null) {
            return (parent.getFullName() + "." + elementName);
        } else {
            return (elementName);
        }
    }

    final public EcvXmlElement addAttribute(String attributeName, String value) {
        assert (attributeName != null);
        assert (attributeName.isEmpty() == false);
        assert (attributes.containsKey(attributeName) == false) : elementName + " - " + attributeName;

        attributes.put(attributeName, value);

        return (this);
    }

    final public EcvXmlElement addAttributeIfNotEmpty(String attributeName, String value) {
        assert (attributeName != null);
        assert (attributeName.isEmpty() == false);
        assert (attributes.containsKey(attributeName) == false) : elementName + " - " + attributeName;

        if (value != null && value.isEmpty() == false) {
            attributes.put(attributeName, value);
        }
        return (this);
    }

    /**
     * Returns the value of the attribute, or null if the element has no
     * attribute for the name.
     *
     * @param attributeName Name of the attribute whose associated value shall
     * be returned.
     * @return The value of the attribute, or null if the element has no
     * attribute with this name.
     */
    public String getAttribute(String attributeName) {
        assert (attributeName != null);
        assert (attributeName.isEmpty() == false);

        return (attributes.get(attributeName));
    }

    public boolean hasAttribute(String attributeName) {
        assert (attributeName != null);
        assert (attributeName.isEmpty() == false);

        return (attributes.containsKey(attributeName));
    }

    abstract public boolean isEmpty();

    abstract public EcvXmlElement copy();

    @Override
    final public String toString() {
        return (toString(""));
    }

    abstract protected String toString(String prefix);

    final public String toStringSorted() {
        return (toStringSorted(""));
    }

    protected String toStringSorted(String prefix) {
        return (toString(prefix));
    }

    public String getUiString() {
        return (getUiString("") + "\n");
    }

    abstract protected String getUiString(String prefix);

    public enum EncodeFormat {

        SHORT_EMPTY_VALUE,
        LONG_EMPTY_VALUE
    }

    /**
     * Returns the container and its content in a valid XML format.
     *
     * @return
     */
    final public String getXmlString() {
        return (getXmlString(EncodeFormat.SHORT_EMPTY_VALUE));
    }

    abstract public String getXmlString(EncodeFormat encodeFormat);

    protected void addAttributeString(StringBuilder builder) {
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            builder.append(' ');
            builder.append(attribute.getKey());
            if (attribute.getValue() != null) {
                builder.append("=\"");
                builder.append(encodeXml(attribute.getValue()));
                builder.append('"');
            }
        }
    }

    static public class NotfoundException extends Exception {

        public NotfoundException(String function, String fullName, String wantedElement) {
            super(function + "(\"" + wantedElement + "\") failed for " + fullName);
        }
    }

    final protected boolean equalsNameAndAttributes(EcvXmlElement e) {
        return (elementName.equals(e.elementName) && attributes.equals(e.attributes));
    }
}
