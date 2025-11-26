/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1ElementCache;
import Rq1Cache.Types.Rq1Reference;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class DmRq1Reference implements Comparable<DmRq1Reference> {

    final Rq1Reference rq1Reference;
    DmElementI dmElement;

    public DmRq1Reference(Rq1Reference rq1Reference) {
        assert (rq1Reference != null);
        this.rq1Reference = rq1Reference;
        this.dmElement = null;
    }

    final public DmElementI getElement() {
        if (dmElement == null) {
            dmElement = DmRq1ElementCache.getElement(rq1Reference.getRecord());
        }
        return (dmElement);
    }

    public final String getTitle() {
        return rq1Reference.getTitle();
    }

    static public List<DmRq1Reference> getReferenceList(List<? extends Rq1Reference> rq1ReferenceList) {
        assert (rq1ReferenceList != null);
        List<DmRq1Reference> result = new ArrayList<>(rq1ReferenceList.size());
        for (Rq1Reference rq1Reference : rq1ReferenceList) {
            result.add(new DmRq1Reference(rq1Reference));
        }
        return (result);
    }

    @Override
    public int compareTo(DmRq1Reference o) {
        return (getTitle().compareTo(o.getTitle()));
    }

    @Override
    public String toString() {
        return (getTitle());
    }

}
