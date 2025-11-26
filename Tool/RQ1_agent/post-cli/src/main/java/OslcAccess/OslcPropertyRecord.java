/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.Collection;

/**
 *
 * @author GUG2WI
 */
public class OslcPropertyRecord extends OslcProperty {

    private OslcPropertySet content;

    public OslcPropertyRecord(String name) {
        super(name);
        content = new OslcPropertySet();
    }

    public OslcPropertyRecord(String name, OslcPropertySet content) {
        super(name);
        assert (content != null);
        this.content = content;
    }

    public OslcPropertyRecord addProperty(OslcProperty newProperty) {
        content.addProperty(newProperty);
        return this;
    }

    public OslcPropertyRecord addPropertyField(String propertyPath) {
        assert (propertyPath != null);
        assert (propertyPath.isEmpty() == false);
        content.addPropertyField(propertyPath);
        return (this);
    }
    
    public void setPropertySet( OslcPropertySet propertySet){
        assert( propertySet != null);
        content = propertySet;
    }

    public Collection<OslcProperty> getProperties() {
        return (content.getProperties());
    }
}
