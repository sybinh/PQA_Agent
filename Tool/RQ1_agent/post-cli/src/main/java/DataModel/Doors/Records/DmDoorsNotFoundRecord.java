/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmConstantField_Text;
import DataModel.Doors.Monitoring.DmDoorsNotFoundFailure;

/**
 *
 * @author gug2wi
 */
public class DmDoorsNotFoundRecord extends DmDoorsElement {

    final public DmConstantField_Text URL;

    public DmDoorsNotFoundRecord(String url) {
        super(ElementType.NOT_FOUND, null);

        assert (url != null);
        assert (url.isEmpty() == false);

        addField(URL = new DmConstantField_Text("URL", url));

        setMarker(new DmDoorsNotFoundFailure(url));
    }

    @Override
    public String getId() {
        return ("Invalid URL: " + URL.getValueAsText());
    }

    @Override
    public String getTitle() {
        return ("Invalid URL: " + URL.getValueAsText());
    }

    @Override
    public String toString() {
        return ("DmDoorsNotFoundRecord: " + URL.getValueAsText());
    }

}
