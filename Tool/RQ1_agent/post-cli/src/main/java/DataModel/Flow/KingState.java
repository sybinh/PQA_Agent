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
public enum KingState implements EcvEnumeration {

    EMPTY(""),
    TRUE("True"),
    FALSE("False");

    private final String dbText;

    /**
     * It is used to give Enumeration for King state by specifying true or false
     * condition to given task.
     *
     * @param dbText shows the condition of King State
     */
    private KingState(String dbText) {
        assert (dbText != null);
        this.dbText = dbText;
    }

    @Override
    public String getText() {
        return dbText;
    }

}
