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
 * @author mos8cob
 */
public enum FlowBlockerBg implements EcvEnumeration{
    
    WHITE("White"),
    BLUE("Blue");

    //
    private final String dbText;

    private FlowBlockerBg(String dbText) {
        assert (dbText != null);
        this.dbText = dbText;
    }

    @Override
    public String getText() {
        return dbText;
    }
}
