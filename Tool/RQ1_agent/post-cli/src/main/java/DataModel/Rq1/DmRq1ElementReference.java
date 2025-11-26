/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1;

import DataModel.DmElementReference;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import Monitoring.MarkableI;

/**
 *
 * @author GUG2WI
 */
public class DmRq1ElementReference extends DmElementReference<DmRq1ElementInterface> {

    final private String rq1Id;

    public DmRq1ElementReference(String rq1Id) {
        super("RQ1-ID");
        assert (rq1Id != null);
        assert (rq1Id.isEmpty() == false);
        this.rq1Id = rq1Id;
    }

    @Override
    protected DmRq1ElementInterface loadReferencedElement() {
        return (DmRq1Element.getElementById(rq1Id));
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        return ("Reference to RQ1-ID " + rq1Id);
    }

    @Override
    public String getId() {
        return ("RQ1-ID " + rq1Id);
    }

    @Override
    public void markerChanged(MarkableI changedMarkable) {

    }

    @Override
    public String toString() {
        return ("DmRq1ElementReference: " + rq1Id);
    }

}
