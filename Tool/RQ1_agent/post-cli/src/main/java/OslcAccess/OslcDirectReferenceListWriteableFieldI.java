/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import Rq1Cache.Types.Rq1Reference;
import java.util.List;

/**
 *
 * @author hfi5wi
 * @param <T_RECORD>
 */
public interface OslcDirectReferenceListWriteableFieldI<T_RECORD extends OslcRecordI> 
        extends OslcDirectReferenceListFieldI<T_RECORD>, OslcWriteableFieldI {
    
    /**
     * Returns the last value from the OSLC database.
     * 
     * @return 
     */
    public List<Rq1Reference> provideLastValuesFromDbAsReferenceListForDb();
    
    /**
     * Returns current value of the data store.
     * 
     * @return 
     */
    public List<Rq1Reference> provideValueAsReferenceListForDb();
    
    /**
     * Returns values that has to be removed at save
     */
    public List<Rq1Reference> getValuesToDeleteAtSave();
    
    /**
     * Returns values that has to be add at save
     */
    public List<Rq1Reference> getValuesToAddAtSave();
}
