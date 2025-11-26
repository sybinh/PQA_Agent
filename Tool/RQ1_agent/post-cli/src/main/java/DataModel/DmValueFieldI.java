/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * Defines the interface of a field that contains a value which is not an
 * element.
 *
 * <P>
 * A value field is a field that contains a concrete value. E.g. a string, an
 * integer, a XML-string or a date.
 *
 * @param <T>
 *
 * @author GUG2WI
 */
public interface DmValueFieldI<T> extends DmFieldI {

    T getValue();

    void setValue(T v);

    /**
     * Returns the current value as a string. If the value is null, then an
     * empty string is returned. Never the null value.
     *
     * @return The current value as a string. If the value is null, then an
     * empty string is returned. Never the null value.
     */
    default String getValueAsText() {
        T v = getValue();
        if (v != null) {
            return (v.toString());
        } else {
            return ("");
        }
    }

    default String getValueAsText(int l_max) {
        assert (l_max > 0);
        T v = getValue();
        if (v != null) {
            String s = v.toString();
            if (s.length() > l_max) {
                return (s.substring(0, l_max));
            } else {
                return (v.toString());
            }
        } else {
            return ("");
        }
    }

    // returns default width for user Interface and -1
    // if no such width is defined.
    default int getWidthInCharacter() {
        return -1;
    }
}
