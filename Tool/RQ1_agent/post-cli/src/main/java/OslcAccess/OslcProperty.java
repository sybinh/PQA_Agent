/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 *
 * @author GUG2WI
 */
public class OslcProperty {

    private final String name;

    public OslcProperty(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);
        assert (name.contains(".") == false) : name;
        this.name = name;
    }

    public String getName() {
        return (name);
    }
}
