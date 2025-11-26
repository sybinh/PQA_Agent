/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 * Contains a non null and non empty name of a OSLC property.
 *
 * @author gug2wi
 */
public class OslcPropertyName {

    final private String name;

    public OslcPropertyName(String name) {
        assert (name != null);
        assert (name.isEmpty() == false);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public final boolean equals(Object o) {
        if (o instanceof OslcPropertyName) {
            return (name.equals(((OslcPropertyName) o).name));
        } else if (o instanceof String) {
            return (name.equals((String) o));
        }
        return (false);
    }

    @Override
    public String toString() {
        return (name);
    }

}
