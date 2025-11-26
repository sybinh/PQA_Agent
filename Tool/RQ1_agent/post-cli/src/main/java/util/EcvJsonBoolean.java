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
 * @author gug2wi
 */
public class EcvJsonBoolean extends EcvJsonValue {

    final static String JSON_TRUE = "true";
    final static String JSON_FALSE = "false";

    private final boolean value;

    public EcvJsonBoolean(boolean value) {
        this.value = value;
    }
    
    public boolean getValue() {
        return (value);
    }

    @Override
    public String toString(String prefix) {
        return (value == true ? JSON_TRUE : JSON_FALSE);
    }

    @Override
    public String toJsonLine() {
        return (value == true ? JSON_TRUE : JSON_FALSE);
    }

}
