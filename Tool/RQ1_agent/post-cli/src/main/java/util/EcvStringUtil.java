/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 *
 * @author GUG2WI
 */
public class EcvStringUtil {

    /**
     * Returns the maxLenght left characters of the string.
     *
     * @param s
     * @param maxLength
     * @return
     */
    static public String left(String s, int maxLength) {
        if (s == null || maxLength <= 0) {
            return ("");
        }
        if (s.length() <= maxLength) {
            return (s);
        }
        return (s.substring(0, maxLength));
    }

    /**
     * Truncates the given text to fit into a line with the given length. If the
     * text would not fit into the line, then it is truncated and '...' is
     * appended to indicate the truncation. Newlines are replaced by backslash.
     *
     * @param s
     * @param maxLength
     * @return
     */
    static public String truncateLine(String s, int maxLength) {
        assert (maxLength >= 5);

        if (s == null) {
            return ("");
        }
        String trimmed = s.trim().replace('\n', '\\');
        if (trimmed.length() <= maxLength) {
            return (trimmed);
        } else {
            return (trimmed.substring(0, maxLength - 3) + " ...");
        }

    }

    /**
     * Truncates the given text to fit into a text area with the given number of
     * characters and lines.
     *
     * Each line is cut as described for truncateLine().
     *
     * If the text fits not into the given lines, then the last line contains
     * '...'.
     *
     * @param s
     * @param maxCharacters
     * @param maxLines
     * @return
     */
    static public String truncateArea(String s, int maxCharacters, int maxLines) {
        assert (maxCharacters >= 5);
        assert (maxLines >= 2);

        if (s == null) {
            return ("");
        }

        String[] lines = s.split("\\n");

        StringBuilder result = new StringBuilder(2 * s.length());
        int noLinesToAdd = lines.length <= maxLines ? lines.length : maxLines;
        for (int l = 0; l < noLinesToAdd; l++) {
            if (result.length() > 0) {
                result.append("\n");
            }
            result.append(truncateLine(lines[l], maxCharacters));
        }
        if (noLinesToAdd < lines.length) {
            result.append(" ...");
        }

        return (result.toString());
    }

}
