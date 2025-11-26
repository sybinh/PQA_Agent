/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmConstantField_Text;
import DataModel.Doors.Monitoring.DmDoorsRequirementWithoutUrlWarning;

/**
 *
 * @author gug2wi
 */
public class DmDoorsRequirementWithoutUrlRecord extends DmDoorsElement {

    final public DmConstantField_Text REQUIREMENT;

    public DmDoorsRequirementWithoutUrlRecord(String requirement) {
        super(ElementType.REQUIREMENT_WITHOUT_URL, null);

        assert (requirement != null);
        assert (requirement.isEmpty() == false);

        addField(REQUIREMENT = new DmConstantField_Text("Requirement", requirement));

        setMarker(new DmDoorsRequirementWithoutUrlWarning(requirement));
    }

    @Override
    public String getId() {
        return ("Missing URL: " + REQUIREMENT.getValueAsText());
    }

    @Override
    public String getTitle() {
        return ("Missing URL: " + REQUIREMENT.getValueAsText());
    }

    @Override
    public String toString() {
        return ("DmDoorsRequirementWithoutUrlRecord: " + REQUIREMENT.getValueAsText());
    }

}
