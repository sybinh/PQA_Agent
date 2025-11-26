/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Arrays;
import java.util.Collection;

/**
 * Implements the EcvEnumeration.
 *
 * @author gug2wi
 */
public class EcvEnumerationValue implements EcvEnumeration, Comparable<EcvEnumeration> {

    //--------------------------------------------------------------------------
    //
    // Static methods
    //
    //--------------------------------------------------------------------------
    /**
     * Create an array of enumeration instances from the given string.
     *
     * The ordinal values of the enumerations start with 0.
     *
     * @param values Values to be taken in the array.
     * @return
     */
    static public EcvEnumeration[] createArray(Collection<String> values) {
        assert (values != null);

        EcvEnumeration[] result = new EcvEnumeration[values.size()];
        int o = 0;
        for (String value : values) {
            result[o] = new EcvEnumerationValue(value, o);
            o++;
        }
        return (result);
    }

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
    static public EcvEnumeration findInList(String text, Collection<EcvEnumeration> values) {
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
     * @param text The text for which the enumeration shall be found or created.
     * @param existingEnumerations The enumerations for which the text is
     * expected to match.
     * @param textForNullValue The text that shall be used, if text is null.
     * This parameter is there to facilitates the handling of null values.
     * @return <ul><li> The EcvEnumeration from 'values' that fits to the given
     * text.
     * <li>A new EcvEnumeration if no object from 'values' fits for the text.
     * </ul>
     */
    static public EcvEnumeration createFromList(String text, Collection<EcvEnumeration> existingEnumerations, String textForNullValue) {
        assert (textForNullValue != null);

        if (text == null) {
            return (new EcvEnumerationValue(textForNullValue, getNextFreeOrdinal(existingEnumerations)));
        }

        EcvEnumeration e = findInList(text, existingEnumerations);
        if (e == null) {
            e = new EcvEnumerationValue(text, getNextFreeOrdinal(existingEnumerations));
        }

        return (e);
    }

    /**
     * Short form of createFromList(String text, EcvEnumeration[] values, String
     * textForNullValue).
     *
     * The value for the parameter textForNullValue is set to the empty string.
     */
    static public EcvEnumeration createFromList(String text, Collection<EcvEnumeration> existingEnumeration) {
        return (createFromList(text, existingEnumeration, ""));
    }

    static public EcvEnumeration createFromList(String text, EcvEnumeration[] existingEnumeration) {
        return (createFromList(text, Arrays.asList(existingEnumeration), ""));
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

    /**
     * Returns the lowest free ordinal based on the given collection.
     *
     * @param existingEnumeration
     * @return
     */
    static public int getNextFreeOrdinal(Collection<EcvEnumeration> existingEnumeration) {
        int freeOrdinal = 0;

        for (EcvEnumeration e : existingEnumeration) {
            if (freeOrdinal <= e.ordinal()) {
                freeOrdinal = e.ordinal() + 1;
            }
        }

        return (freeOrdinal);
    }

    //--------------------------------------------------------------------------
    //
    // Class implementation
    //
    //--------------------------------------------------------------------------
    final private String text;
    final private int ordinal;

    /**
     * Create an instance with the given text and ordinal
     *
     * @param text Text for the instance.
     * @param ordinal Ordinal of the instance.
     */
    public EcvEnumerationValue(String text, int ordinal) {
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
    public boolean equals(Object o) {
        if (o instanceof EcvEnumeration) {
            return (EcvEnumeration.compare(this, (EcvEnumeration) o) == 0);
        } else {
            return (false);
        }
    }

    @Override
    public String toString() {
        return (text + "(" + ordinal + ')');
    }

}
