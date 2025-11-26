/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import Doors.DoorsRecord;
import Doors.Identifier.DoorsRecordIdentifier;

/**
 *
 * @author gug2wi
 */
public abstract class DmDoorsRecord extends DmDoorsElement {

    final protected DoorsRecord doorsElement;

    public DmDoorsRecord(ElementType type, DoorsRecord doorsElement) {
        super(type, doorsElement);
        assert (doorsElement != null);

        this.doorsElement = doorsElement;
    }

    public void openInDoors() {
        doorsElement.openInDoors();
    }

    public String getUrl() {
        return doorsElement.getUrl();
    }

    public DoorsRecordIdentifier getIdentifier() {
        return (doorsElement.getIdentifier());
    }

}
