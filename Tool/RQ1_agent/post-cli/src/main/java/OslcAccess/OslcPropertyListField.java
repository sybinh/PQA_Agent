/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hfi5wi
 */
public class OslcPropertyListField extends OslcProperty {
    
    private final List<String> values = new ArrayList<>(0);
    
    public OslcPropertyListField(String name) {
        super(name);
    }
    
    public OslcPropertyListField(String name, List<String> values) {
        super(name);
        
        addValues(values);
    }
    
    public void addValue(String value) {
        assert (value != null);
        assert (value.isEmpty() == false);
        
        if (values.contains(value) == false) {
            values.add(value);
        }
    }

    public void addValues(List<String> values) {
        assert (values != null);
        
        values.forEach((value) -> {
            addValue(value);
        });
    }
    
    public List<String> getValues() {
        return (values);
    }
    
}
