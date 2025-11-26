/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import OslcAccess.OslcRecordTypeI;

/**
 *
 * @author gug2wi
 * @param <T> Type of the implement referenced by the hit.
 */
public interface DmQueryHitI<T extends DmElementI> {

    /**
     * Returns a string representation of the ID of the record found in the
     * database.
     *
     * @return
     */
    public String getId();

    public OslcRecordTypeI getReferencedRecordType();

    /**
     * Returns the title of the hit.
     *
     * @return
     */
    public String getTitle();

    public T getReferencedElement();

}
