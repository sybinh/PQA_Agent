/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.OrgData;

/**
 *
 * @author GUG2WI
 */
public class OrgData_Project extends OrgData_Element {

    private String name;

    public OrgData_Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equalsTree(OrgData_Element otherData) {

        if (otherData instanceof OrgData_Project) {

            if (name.equals(((OrgData_Project) otherData).name) == false) {
                return (false);
            }

            return super.equalsTree(otherData);
        } else {
            return (false);
        }
    }

    @Override
    public String toString() {
        return ("OrgData_Project: " + name);
    }

    @Override
    protected OrgData_Element copyElement() {
        return (new OrgData_Project(name));
    }

}
