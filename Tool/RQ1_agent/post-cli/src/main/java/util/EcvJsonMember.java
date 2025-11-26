/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
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
public class EcvJsonMember<T_VALUE extends EcvJsonValue> {

    final protected String name;
    final protected T_VALUE value;

    public EcvJsonMember(String name, T_VALUE value) {
        assert (name != null);
        assert (name.isEmpty() == false);

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T_VALUE getValue() {
        return value;
    }

    @Override
    public String toString() {
        return (toString(""));
    }

    public String toString(String prefix) {
        if (value != null) {
            return ('"' + name + "\" : " + value.toString(prefix));
        } else {
            return ('"' + name + "\" : null");
        }
    }

    public String toJsonLine() {
        if (value != null) {
            return ('"' + name + "\": " + value.toJsonLine());
        } else {
            return ('"' + name + "\": null");
        }
    }

}
