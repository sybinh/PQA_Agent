/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.Fields;

import DataModel.DmElementI;
import DataModel.DmField;

/**
 *
 * @author moe83wi
 */
public class DmEcvField extends DmField {

    public DmEcvField(DmElementI parent, String nameForUserInterface) {
        super(nameForUserInterface);
    }

    @Override
    public boolean isReadOnly() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
