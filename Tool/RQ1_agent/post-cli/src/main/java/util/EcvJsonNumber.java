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
public class EcvJsonNumber extends EcvJsonValue {

    private final Number value;

    public EcvJsonNumber(long value) {
        this.value = value;
    }

    public EcvJsonNumber(double value) {
        this.value = value;
    }

    public Number getNumber() {
        return (value);
    }

    @Override
    public String toString(String prefix) {
        return (value.toString());
    }

    @Override
    public String toJsonLine() {
        return (value.toString());
    }

}
