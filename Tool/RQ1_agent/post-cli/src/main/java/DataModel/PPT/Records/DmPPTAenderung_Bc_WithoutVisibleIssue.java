/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.ProjektBewertung;

/**
 * Dummy Issue for BC
 *
 * @author gug2wi
 */
public class DmPPTAenderung_Bc_WithoutVisibleIssue extends DmPPTAenderung_Bc {

    //Dummy Issue at BC
    public DmPPTAenderung_Bc_WithoutVisibleIssue(DmPPTBCRelease bc) {
        super(bc);
    }

    @Override
    public ProjektBewertung getProjektBewertung() {
        return (ProjektBewertung.NONE);
    }

}
