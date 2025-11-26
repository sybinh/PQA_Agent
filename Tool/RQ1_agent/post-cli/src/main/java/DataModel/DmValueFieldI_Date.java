/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import util.EcvDate;

/**
 *
 * @author gug2wi
 */
public interface DmValueFieldI_Date extends DmValueFieldI<EcvDate> {

    default EcvDate getDate() {
        return (getValue());
    }

    default EcvDate getDateNotNull() {
        EcvDate date = getDate();
        if (date != null) {
            return (date);
        } else {
            return (EcvDate.getEmpty());
        }
    }

}
