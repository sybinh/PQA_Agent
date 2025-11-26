/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * A field that holds a constant string value. The constant value is set during
 * the construction of the field.
 *
 * @author gug2wi
 */
public class DmConstantField_Text extends DmConstantField<String> implements DmValueFieldI_Text {

    public DmConstantField_Text(String nameForUserInterface, String value) {
        super(nameForUserInterface, value);
    }

}
