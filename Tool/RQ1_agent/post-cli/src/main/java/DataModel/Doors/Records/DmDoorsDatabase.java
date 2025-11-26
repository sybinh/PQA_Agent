/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import Doors.DoorsDatabase;
import Doors.DoorsRecordFactory;
import UiSupport.UiTreeViewRootElementI;

/**
 * Reflects a doors database in the data model.
 *
 * @author gug2wi
 */
public class DmDoorsDatabase extends DmDoorsFolder implements UiTreeViewRootElementI {

    //--------------------------------------------------------------------------
    //
    // Access to active database
    //
    //--------------------------------------------------------------------------
    static private DmDoorsDatabase activeDatabase = null;

    static public synchronized DmDoorsDatabase getActiveDatabase() {
        if (activeDatabase == null) {
            activeDatabase = new DmDoorsDatabase(DoorsRecordFactory.getDatabase());
        }
        return (activeDatabase);
    }

    //--------------------------------------------------------------------------
    //
    // Implementation of database class
    //
    //--------------------------------------------------------------------------
    public DmDoorsDatabase(DoorsDatabase doorsDatabase) {
        super(ElementType.DATABASE, doorsDatabase);
    }

    @Override
    public String getId() {
        return (getTitle());
    }

    @Override
    public String getViewTitle() {
        return (getTitle());
    }

}
