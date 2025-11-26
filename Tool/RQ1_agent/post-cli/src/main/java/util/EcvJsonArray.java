/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a JSON array. An array contains a list of value.
 *
 * @author GUG2WI
 */
public class EcvJsonArray extends EcvJsonTopLevelValue {

    final private List<EcvJsonValue> elements = new ArrayList<>();

    public void addValue(EcvJsonValue value) {
        assert (value != null);
        elements.add(value);
    }

    /**
     * Add a EcvJsonString to the array.
     *
     * @param value Value of the string.
     */
    public void addJsonString(String value) {
        addValue(new EcvJsonString(value));
    }

    public List<EcvJsonValue> getElements() {
        return (elements);
    }

    @Override
    public String toString(String prefix) {

        if (elements.isEmpty() == true) {
            return ("[]");
        }

        StringBuilder b = new StringBuilder();
        boolean isFirst = true;
        String elementPrefix = prefix + LEVEL_PREFIX;

        b.append("[");
        for (EcvJsonValue value : elements) {
            if (isFirst == false) {
                b.append(",");
            }
            b.append("\n");
            b.append(elementPrefix).append(value.toString(elementPrefix));
            isFirst = false;
        }
        b.append("\n");
        b.append(prefix).append("]");

        return (b.toString());
    }

    @Override
    public String toJsonLine() {

        if (elements.isEmpty() == true) {
            return ("[]");
        }

        StringBuilder b = new StringBuilder();
        boolean isFirst = true;

        b.append("[");
        for (EcvJsonValue value : elements) {
            if (isFirst == false) {
                b.append(", ");
            }
            b.append(value.toJsonLine());
            isFirst = false;
        }
        b.append("]");

        return (b.toString());
    }

}
