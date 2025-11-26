/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 * Represents a JSON value. Check the sub classes to see which type of values
 * exist.
 *
 * @author GUG2WI
 */
public abstract class EcvJsonValue {

    final static protected String LEVEL_PREFIX = "    ";

    /**
     * Returns the content of the value in one line.
     * @return
     */
    public abstract String toJsonLine();

    /**
     * Returns the content of the value in an structured format.
     *
     * @return
     */
    final public String toJsonString() {
        return (toString());
    }

    @Override
    final public String toString() {
        return (toString(""));
    }

    /**
     * Support for structured output during debugging.
     *
     * @param prefix
     * @return
     */
    protected abstract String toString(String prefix);

}
