/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 * Support for encoding to HTML and decoding from HTML
 * 
 * @author hfi5wi
 */
public class EcvHtmlEncode {
    
    static final private CharSequence[][] htmlSpecialCharacterTable = {
        {"&", "&amp;"}, // &amp has to be the first element to encode and the last to decode when replace() is used !
        {"<", "&lt;"},
        {">", "&gt;"},
        {"\"", "&quot;"},
        //        {"'", "&apos;"}, // Only available in XHTML
        {"\r", "&#13;"},
        {"\n", "<br>"}}; // CR

    /**
     * Encodes a text for HTML. All special characters of HTML are replaced by
     * there character entities.
     *
     * @param clearText Text that shall be encoded.
     * @return Encoded text. Ready to be added to a HTML document.
     */
    static public String encodeHtml(String clearText) {
        assert (clearText != null);

        String htmlString = clearText;

        for (int i = 0; i < htmlSpecialCharacterTable.length; i++) {
            if (htmlSpecialCharacterTable[i][0].length() > 0) {
                htmlString = htmlString.replace(htmlSpecialCharacterTable[i][0], htmlSpecialCharacterTable[i][1]);
            }
        }
        return (htmlString);
    }

    /**
     * Decodes the text from a HTML document.
     *
     * @param htmlString
     * @return
     */
    static public String decodeHtml(String htmlString) {
        assert (htmlString != null);
        //
        // The following code was removed, becaus String.replace uses regular expressions for the replacemant. This leads to a huge overhead in CPU and memory consumption.
        //
        //        String clearText = xmlString;
        //        for (int i = xmlSpecialCharacterTable.length - 1; i >= 0; i--) {
        //            clearText = clearText.replace(xmlSpecialCharacterTable[i][1], xmlSpecialCharacterTable[i][0]);
        //        }
        //        return (clearText);
        //
        int l_source = htmlString.length();
        char[] source = new char[l_source];
        char[] result = new char[l_source]; // result can only get shorter
        htmlString.getChars(0, l_source, source, 0);

        int p_source = 0;
        int p_result = 0;
        while (p_source < l_source) {
            if (source[p_source] != '&') {
                result[p_result++] = source[p_source++];
            } else {
                CharSequence[] matchingMap = null;
                for (CharSequence[] currentMap : htmlSpecialCharacterTable) {
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
}
