/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * Supports the implementation of a field whose text value is taken from some
 * source. The access to the source has to be implemented in the subclass.
 *
 * @author gug2wi
 */
public abstract class DmSourceField_Text extends DmSourceField<String> implements DmValueFieldI_Text {

    public DmSourceField_Text(String nameForUserInterface) {
        super(nameForUserInterface);
    }

}
