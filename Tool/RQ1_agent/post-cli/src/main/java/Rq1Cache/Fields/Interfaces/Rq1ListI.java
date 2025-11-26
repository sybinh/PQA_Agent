/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields.Interfaces;

import OslcAccess.OslcLoadHint;
import util.EcvListenerManager;

/**
 *
 * @author gug2wi
 */
public interface Rq1ListI {

    public interface ChangeListener extends EcvListenerManager.ChangeListener<Rq1ListI> {
    }

    void addChangeListener(ChangeListener newListener);

    void removeChangeListener(ChangeListener obsoletListener);

    void reload();

    void loadCache(OslcLoadHint loadHint);

}
