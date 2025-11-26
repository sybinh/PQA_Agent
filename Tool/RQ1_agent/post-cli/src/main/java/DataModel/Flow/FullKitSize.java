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
 * @author ser4cob
 */
public enum FullKitSize implements EcvEnumeration {

    EMPTY(""),
    S("S"),
    M("M"),
    L("L"),
    XL("XL"),
    XXL("XXL");

    //
    private final String dbText;

    private FullKitSize(String dbText) {
        assert (dbText != null);
        this.dbText = dbText;
    }

    @Override
    public String getText() {
        return dbText;
    }

}
