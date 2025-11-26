/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.DmElement;
import util.EcvAppendedData;

/**
 *
 * @author moe83wi
 */
public abstract class DmPPTRecord extends DmElement {

    public DmPPTRecord(String elementType) {
        super(elementType);
    }

    public abstract EcvAppendedData getEcvAppendedData();

    public abstract boolean isCanceled();
}
