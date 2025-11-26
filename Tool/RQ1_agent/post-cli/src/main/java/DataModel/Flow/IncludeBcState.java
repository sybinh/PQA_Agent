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
 * @author bel5cob
 */
public enum IncludeBcState implements EcvEnumeration {

    YES("Yes"),
    NO("No");

    private final String dbText;

    private IncludeBcState(String dbText) {
        assert (dbText != null);
        this.dbText = dbText;
    }

    @Override
    public String getText() {
        return dbText;
    }

}
