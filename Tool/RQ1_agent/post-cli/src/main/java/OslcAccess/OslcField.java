/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 *
 * @author gug2wi
 */
public abstract class OslcField implements OslcFieldI {

    final private String oslcPropertyName;
    private boolean optional = false;

    public OslcField(String oslcPropertyName) {
        assert (oslcPropertyName != null);
        assert (oslcPropertyName.isEmpty() == false);

        this.oslcPropertyName = oslcPropertyName;
    }

    @Override
    public String getOslcPropertyName() {
        return (oslcPropertyName);
    }

    @Override
    public boolean isOptional() {
        return (optional);
    }

}
