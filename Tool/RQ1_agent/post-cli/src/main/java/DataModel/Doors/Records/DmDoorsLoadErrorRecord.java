/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.Doors.Fields.DmDoorsField_Text;
import DataModel.Doors.Monitoring.DmDoorsNotFoundFailure;
import Doors.DoorsLoadErrorRecord;

/**
 *
 * @author gug2wi
 */
public class DmDoorsLoadErrorRecord extends DmDoorsElement {

    final public DmDoorsField_Text URL;
    final public DmDoorsField_Text PROBLEM;

    public DmDoorsLoadErrorRecord(String url, String problem) {
        this(new DoorsLoadErrorRecord(url, problem));
    }

    public DmDoorsLoadErrorRecord(DoorsLoadErrorRecord errorRecord) {
        super(ElementType.NOT_FOUND, errorRecord);
        assert (errorRecord != null);

        addField(URL = new DmDoorsField_Text(errorRecord.URL, "URL"));
        addField(PROBLEM = new DmDoorsField_Text(errorRecord.PROBLEM, "Problem"));

        setMarker(new DmDoorsNotFoundFailure(URL.getValue(), PROBLEM.getValue()));
    }

    @Override
    public String getId() {
        return ("Invalid URL: " + URL.getValueAsText());
    }

    @Override
    public String getTitle() {
        return ("Invalid URL: " + URL.getValueAsText().replaceAll("\n", " "));
    }

    @Override
    public String toString() {
        return ("DmDoorsNotFoundRecord: " + URL.getValueAsText());
    }

}
