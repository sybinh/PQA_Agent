/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * A field that stores a text value.
 *
 * @author gug2wi
 */
public class DmValueField_Text extends DmValueField<String> implements DmValueFieldI_Text {

    public DmValueField_Text(String nameForUserInterface, String value) {
        super(nameForUserInterface, value);
    }

}
