/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import Doors.DoorsProject;

/**
 *
 * @author gug2wi
 */
public class DmDoorsProject extends DmDoorsFolder {

    public DmDoorsProject(DoorsProject container) {
        super(ElementType.PROJECT, container);
    }

    @Override
    public String toString() {
        return ("Project " + TITLE.getValueAsText());
    }

}
