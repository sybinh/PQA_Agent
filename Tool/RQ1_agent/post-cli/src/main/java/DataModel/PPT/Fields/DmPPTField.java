/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.DmElementI;
import DataModel.DmField;
import util.EcvDate;

/**
 *
 * @author moe83wi
 */
public class DmPPTField extends DmField {

    // Set processing date by default to the actual date
    //
    private static EcvDate processDate = EcvDate.getToday();
    
    static public void setProcessDate(EcvDate date) {
        assert (date != null);
        assert (date.isEmpty() == false);

        processDate = date;
    }

    static public EcvDate getProcessDate() {
        return (processDate);
    }

    @Override
    public boolean isReadOnly() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DmPPTField(DmElementI parent, String nameForUserInterface) {
        super(nameForUserInterface);
    }
}
