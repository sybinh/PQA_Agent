/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.iCDM;

/**
 *
 * @author GUG2WI
 */
public class RestClient_iCDM_MilestoneType {

    final private int id;
    final private String name;
    final private String description;
    final private String format;
    final private String valueType;

    public RestClient_iCDM_MilestoneType(int id, String name, String description, String format, String valueType) {
        assert (name != null);
        assert (name.isEmpty() == false);
        this.id = id;
        this.name = name;
        this.description = description != null ? description : "";
        this.format = format != null ? format : "";
        this.valueType = valueType != null ? valueType : "";
    }

    public int getId() {
        return (id);
    }

    @Override
    public String toString() {
        return ("iCDM-MilestoneType," + id + "," + name + "," + format + "," + valueType);
    }

}
