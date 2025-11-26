/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 * Represents a JSON string value.
 *
 * @author GUG2WI
 */
public class EcvJsonString extends EcvJsonValue {

    static final private CharSequence[][] JSON_ENCODE_TABLE = {
        {"\n", "\\n"},
        {"\t", "\\t"},
        {"\b", "\\b"},
        {"\r", "\\r"},
        {"\f", "\\f"},
        {"\"", "&quot;"}};

    static public String encodeJsonString(String clearText) {
        return (EcvParser.encodeWithEscapeSequence(clearText, JSON_ENCODE_TABLE));
    }

    final private String value;

    public EcvJsonString(String value) {
        assert (value != null);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString(String prefix) {
        return ('"' + encodeJsonString(value) + '"');
    }

    @Override
    public String toJsonLine() {
        return ('"' + encodeJsonString(value) + '"');
    }

}
