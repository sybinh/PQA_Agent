/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1IssueSW;

/**
 *
 * @author gug2wi
 */
public abstract class DmPPTAenderung_Fc extends DmPPTAenderung_OnPst {

    protected DmPPTAenderung_Fc(DmPPTFCRelease fc) {
        super(null, null, fc, null, null, "", "", "");
        assert (fc != null);
    }

    protected DmPPTAenderung_Fc(DmRq1IssueSW rq1IssueSW, DmPPTFCRelease fc, DmPPTRelease release, DmRq1Irm irm, String issueFD, String rbComment, String ctc) {
        super(rq1IssueSW, null, fc, release, irm, rbComment, ctc, issueFD);
        assert (rq1IssueSW != null);
        assert (fc != null);
        assert (issueFD != null);
        assert (issueFD.isEmpty() == false);
    }

}
