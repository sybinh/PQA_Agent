/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
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
public class OslcPropertyField extends OslcProperty {
    String value;

    public OslcPropertyField(String name) {
        super(name);
        value = null;
    }

    public OslcPropertyField(String name, String value) {
        super(name);
//        assert (value != null);
        this.value = value;
    }

    public void setValue(String value) {
        assert (value != null);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }
    
}
