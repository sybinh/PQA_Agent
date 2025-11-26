/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataStore.ALM.DsAlmEnumerationValue;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public class DmAlmEnumerationValue implements EcvEnumeration {

    final private DsAlmEnumerationValue dsEnumerationValue;
    final private int ordinal;

    public DmAlmEnumerationValue(DsAlmEnumerationValue dsEnumerationValue, int ordinal) {
        assert (dsEnumerationValue != null);
        this.dsEnumerationValue = dsEnumerationValue;
        this.ordinal = ordinal;
    }

    public String getIdentifier() {
        return (dsEnumerationValue.getIdentifier());
    }

    @Override
    public String getText() {
        return (dsEnumerationValue.getTitle());
    }
    
    public String getResource() {
        return (dsEnumerationValue.getResource());
    }
    
    @Override
    public int ordinal() {
        return (ordinal);
    }

    @Override
    public String toString() {
        return (getText() + "(" + ordinal + ')');
    }

}