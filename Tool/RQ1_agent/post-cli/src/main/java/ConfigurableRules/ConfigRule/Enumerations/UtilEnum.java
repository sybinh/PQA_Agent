/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Enumerations;

import Rq1Data.Enumerations.*;
import util.EcvEnumeration;

/**
 *
 * @author gug2wi
 */
public enum UtilEnum implements EcvEnumeration {

    EMPTY("") {
        @Override
        public String toString() {
            return ("");
        }
    };
    //
    private final String dbText;

    private UtilEnum(String dbText) {
        assert (dbText != null);
        this.dbText = dbText;
    }

    @Override
    public String getText() {
        return dbText;
    }
}
