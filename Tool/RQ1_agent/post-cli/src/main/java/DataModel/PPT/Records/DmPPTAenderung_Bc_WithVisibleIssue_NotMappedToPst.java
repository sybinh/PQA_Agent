/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.ProjektBewertung;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;

/**
 * Issue with BC connection
 *
 * @author gug2wi
 */
public class DmPPTAenderung_Bc_WithVisibleIssue_NotMappedToPst extends DmPPTAenderung_Bc {

    public DmPPTAenderung_Bc_WithVisibleIssue_NotMappedToPst(DmRq1IssueSW rq1IssueSW, DmPPTBCRelease bc, DmRq1IssueFD issueFD) {
        super(rq1IssueSW, bc, null, null, issueFD, "", "");
        assert (rq1IssueSW != null);
        assert (bc != null);
        assert (issueFD != null);
    }

    @Override
    public ProjektBewertung getProjektBewertung() {
        return (ProjektBewertung.NONE);
    }

}
