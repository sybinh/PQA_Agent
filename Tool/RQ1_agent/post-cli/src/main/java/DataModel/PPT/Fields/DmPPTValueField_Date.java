/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.DmElementI;
import util.EcvDate;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Date extends DmPPTValueField<EcvDate>{

    public DmPPTValueField_Date(DmElementI parent, EcvDate v, String nameForUserInterface) {
        super(parent, v, nameForUserInterface);
    }
    
}
