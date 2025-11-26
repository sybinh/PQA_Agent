/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import util.EcvEnumeration;

/**
 * Identifies a enumeration field for which the valid values are loaded via lazy
 * loading.
 *
 * @author gug2wi
 */
public interface DmValueFieldI_Enumeration_LazyLoad extends DmValueFieldI_Enumeration {

    /**
     * Returns the valid input values for this field. The values may be loaded
     * from the database when the method is called. Therefore, the method should
     * be called in a background task.
     *
     * @return The input values valid for this field.
     */
    @Override
    public EcvEnumeration[] getValidInputValues();

    /**
     * Returns the valid input values, if they are immediately available.
     * Otherwise null is returned.
     *
     * @return The input values valid for this field, if they where already
     * loaded before. null, if loading would take some time.
     */
    public EcvEnumeration[] getValidInputValues_NonBlocking();

}
