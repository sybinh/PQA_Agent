/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 * Supports the unified processing of record fields and UI fields containing
 * enumeration values.
 *
 * With this interface, it is possible to:<br>
 * - Read values from database into the field.<br>
 * - Write values from the field into the database.<br>
 * - Edit the field on the UI of IPE via a drop down box.
 *
 * The goal of this interface is to provide a common handling of enumerations.
 * The handling shall be the same for enumerations based on the Java enums and
 * for enumerations derived from a set of strings. The ordinal was taken over
 * from the Java enums. It provides a way to sort the values not only
 * alphabetically but also in a defined order. For Java enums this is the
 * natural order of the enums. For other implementation, the order might e.g.
 * come from a list that is used as source for the enumerations.
 *
 * @author gug2wi
 */
public interface EcvEnumeration {

    /**
     * Returns the text representation of the enumeration value as it is used in
     * the database.
     *
     * @return Text representation of the enumeration value as it is used in the
     * database.
     */
    String getText();

    int ordinal();

    /**
     * Compares two EcvEnumeration first by the ordinal number and then by the
     * text. But there is one exception: If the text equals, then the elements
     * are considered to be equal.
     *
     * @param e1 The first enumeration or null.
     * @param e2 The second enumeration or null.
     * @return
     */
    static int compare(EcvEnumeration e1, EcvEnumeration e2) {
        if (e1 != null) {
            if (e2 != null) {
                if (e1.getText().equals(e2.getText())) {
                    return (0);
                }
                if (e1.ordinal() != e2.ordinal()) {
                    return (e1.ordinal() - e2.ordinal());
                } else {
                    return (e1.getText().compareTo(e2.getText()));
                }
            } else {
                return (-1);
            }
        } else if (e2 != null) {
            return (+1);
        } else {
            return (0);
        }
    }

    /**
     * Compares the EcvEnumeration with an other enumeration
     *
     * @param e The compare enumeration or null.
     * @return
     */
    default int compareTo(EcvEnumeration e) {
        return (compare(this, e));
    }

}
