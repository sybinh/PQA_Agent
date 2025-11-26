/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.DmElementI;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Boolean extends DmPPTValueField<Boolean>{

    public DmPPTValueField_Boolean(DmElementI parent, Boolean v, String nameForUserInterface) {
        super(parent, v, nameForUserInterface);
    }
    
}
