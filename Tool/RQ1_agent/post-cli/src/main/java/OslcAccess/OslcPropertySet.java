/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author GUG2WI
 */
public class OslcPropertySet {

    final private Map<String, OslcProperty> content;
    
    public OslcPropertySet() {
        content = new TreeMap<>();
    }

    public boolean isEmpty() {
        return (content.isEmpty());
    }

    public void addProperty(OslcProperty newProperty) {
        assert (newProperty != null);
        OslcProperty oldProperty;
        if ((oldProperty = content.get(newProperty.getName())) == null) {
            //
            // New property. Simply add to map.
            content.put(newProperty.getName(), newProperty);
            //
        } else if (newProperty instanceof OslcPropertyField) {
            //
            // Allow duplicate setting of property field, if fields are empty or contain the same data.
            // This is done to simplify the creation of result sets.
            //
            assert (oldProperty instanceof OslcPropertyField) : "Name: " + newProperty.getName() + "\nClass old: " + oldProperty.getClass().getName() + "\nClass new: " + newProperty.getClass().getName();
            assert (((OslcPropertyField) oldProperty).equals(((OslcPropertyField) newProperty)) == false) : "Name=" + newProperty.getName() + " Value1=" + ((OslcPropertyField)oldProperty).getValue() + " Value2=" + ((OslcPropertyField)newProperty).getValue();
            //
        } else if (newProperty instanceof OslcPropertyListField) {
            //
            // Set multiple property fields to one name.
            //
            assert (oldProperty instanceof OslcPropertyListField) : "Name: " + newProperty.getName() + "\nClass old: " + oldProperty.getClass().getName() + "\nClass new: " + newProperty.getClass().getName();
            ((OslcPropertyListField)oldProperty).addValues(((OslcPropertyListField) newProperty).getValues());
            //
        } else {
            //
            // Join the fields of property references.
            // This is done to simplify the creation of result sets.
            //
            assert (newProperty instanceof OslcPropertyRecord) : "Class: " + newProperty.getClass().getName();
            assert (oldProperty instanceof OslcPropertyRecord) : "Class: " + oldProperty.getClass().getName();
            for (OslcProperty property : ((OslcPropertyRecord) newProperty).getProperties()) {
                ((OslcPropertyRecord) oldProperty).addProperty(property);
            }
        }

    }

    public void addPropertyField(String propertyPath) {
        assert (propertyPath != null);
        assert (propertyPath.isEmpty() == false);

        String propertyPathArray[] = propertyPath.split("\\.");

        int i = propertyPathArray.length - 1;
        OslcProperty newProperty = new OslcPropertyField(propertyPathArray[i]);

        while (i > 0) {
            i--;
            OslcProperty newPropertyRecord = new OslcPropertyRecord(propertyPathArray[i]).addProperty(newProperty);
            newProperty = newPropertyRecord;
        }

        addProperty(newProperty);
    }

    public Collection<OslcProperty> getProperties() {
        return (content.values());
    }
}
