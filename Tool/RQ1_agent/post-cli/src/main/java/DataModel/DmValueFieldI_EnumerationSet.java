/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import java.util.List;
import util.EcvEnumeration;
import util.EcvEnumerationSet;

/**
 * Identifies a enumeration field for which the valid values determined without
 * the need of a database access.
 *
 * @author gug2wi
 */
public interface DmValueFieldI_EnumerationSet extends DmValueFieldI<EcvEnumerationSet> {

    /**
     * Returns the valid input values for this field.
     *
     * This method is called in the UI thread and has to return immediately to
     * prevent the blocking of UI. If the values cannot be returned immediately,
     * then the method shall return the null value. The framework will then call
     * loadValidInputValues() in a background thread to do the loading. After
     * return of loadValidInputValues(), getValidInputValues() will be called
     * again in the UI thread. The method shall then return the list of valid
     * values. If no valid values exist, then an empty list shall be returned.
     *
     * @return The valid values or null if the valid values are not yet
     * available.
     */
    public List<EcvEnumeration> getValidInputValues();

    /**
     * Loads the valid input values that later will be returned by
     * getValidInputValues().
     *
     * This method is called in an background thread after getValidInputValues()
     * returned null.
     */
    default void loadValidInputValues() {

    }

}
