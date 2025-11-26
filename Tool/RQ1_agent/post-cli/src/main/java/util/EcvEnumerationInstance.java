/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Arrays;

/**
 * Implements the EcvEnumeration.
 *
 * @author gug2wi
 */
public class EcvEnumerationInstance implements EcvEnumeration, Comparable<EcvEnumeration> {

    /**
     * Find the EcvEnumeration object for the given text in the given values.
     *
     * @param text Text for the EcvEnumeration.
     * @param values List of EcvEnumeration objects that shall be searched.
     * @return <ul><li> The EcvEnumeration from 'values' that fits to the given
     * text.
     * <li>null ... If no object from 'values' fits for the text.
     * </ul>
     *
     */
    static public EcvEnumeration findInList(String text, EcvEnumeration[] values) {
        assert (text != null);
        for (EcvEnumeration v : values) {
            if (v.getText().endsWith(text)) {
                return (v);
            }
        }
        return (null);
    }

    /**
     * Gets the EcvEnumeration from 'values' that fits for the 'text'. If no
     * fitting object is found, then a new EcvEnumeration for the text is
     * created. The orderNo of the new EcvEnumeration is set to the length of
     * 'values'.
     *
     * @param text
     * @param values
     * @return <ul><li> The EcvEnumeration from 'values' that fits to the given
     * text.
     * <li>A new EcvEnumeration if no object from 'values' fits for the text.
     * </ul>
     */
    static public EcvEnumeration createFromList(String text, EcvEnumeration[] values) {
        assert (text != null);
        EcvEnumeration e = findInList(text, values);
        if (e == null) {
            e = new EcvEnumerationInstance(text, values.length);
        }
        return (e);
    }

    /**
     * Builds an array of all values from the definedValues and the
     * currentValue. The result value is the definedValues, if the currentValue
     * is contained in the definedValues.
     *
     * @param definedValues
     * @param currentValue
     * @return
     */
    static public EcvEnumeration[] createValueList(EcvEnumeration[] definedValues, EcvEnumeration currentValue) {
        assert (definedValues != null);

        if (currentValue == null) {
            return (Arrays.copyOf(definedValues, definedValues.length));
        }

        for (EcvEnumeration v : definedValues) {
            if (v.getText().equals(currentValue.getText()) == true) {
                return (Arrays.copyOf(definedValues, definedValues.length));
            }
        }

        EcvEnumeration[] result = new EcvEnumeration[definedValues.length + 1];
        for (int i = 0; i < definedValues.length; i++) {
            result[i] = definedValues[i];
        }
        result[definedValues.length] = currentValue;
        return (result);
    }

    final private String text;
    final private int ordinal;

    public EcvEnumerationInstance(String text, int ordinal) {
        assert (text != null);

        this.text = text;
        this.ordinal = ordinal;
    }

    @Override
    final public String getText() {
        return (text);
    }

    @Override
    final public int ordinal() {
        return (ordinal);
    }

    @Override
    final public int compareTo(EcvEnumeration o) {
        return (EcvEnumeration.compare(this, o));
    }

    @Override
    public String toString() {
        return (text + "(" + ordinal + ')');
    }

}
