/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.ProjektBewertung;
import DataModel.Rq1.Records.DmRq1IssueSW;

/**
 *
 * @author gug2wi
 */
public class DmPPTAenderung_Fc_WithVisibleIssue_NotMappedToPst extends DmPPTAenderung_Fc_WithVisibleIssue {

    public DmPPTAenderung_Fc_WithVisibleIssue_NotMappedToPst(DmRq1IssueSW rq1IssueSW, DmPPTFCRelease fc, String issueFD) {
        super(rq1IssueSW, fc, null, null, issueFD, "", "");
        assert (rq1IssueSW != null);
        assert (fc != null);
        assert (issueFD != null);
        assert (issueFD.isEmpty() == false);
    }

    @Override
    public ProjektBewertung getProjektBewertung() {
        return (ProjektBewertung.NONE);
    }

}
