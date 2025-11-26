/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import util.EcvEnumeration;

/**
 * Identifies a enumeration field for which the valid values determined without
 * the need of a database access.
 *
 * @author gug2wi
 */
public interface DmValueFieldI_Enumeration_EagerLoad extends DmValueFieldI_Enumeration {

    /**
     * Returns the valid input values for this field. The values are hard coded
     * or loaded earlier. Therefore the method will not block the execution and
     * can be called in any task.
     *
     * @return The input values valid for this field.
     */
    @Override
    public EcvEnumeration[] getValidInputValues();

    @Override
    public void setValue(EcvEnumeration v);
}
