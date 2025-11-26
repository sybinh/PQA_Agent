/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Flow;

import util.EcvEnumeration;

/**
 *
 * @author gug2wi
 */
public enum FullKitStatus implements EcvEnumeration {

    EMPTY(""),
    GREEN("GREEN"),
    LIGHT_BLUE("LIGHT_BLUE"),
    DARK_BLUE("DARK_BLUE"),
    RED("RED"),
    GREY("GREY");
    //
    private final String dbText;

    private FullKitStatus(String dbText) {
        assert (dbText != null);
        this.dbText = dbText;
    }

    @Override
    public String getText() {
        return dbText;
    }

}
