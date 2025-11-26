/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.ProjektBewertung;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1IssueSW;

/**
 *
 * @author gug2wi
 */
public abstract class DmPPTAenderung_OnPst extends DmPPTAenderung {

    protected DmPPTAenderung_OnPst(DmRq1IssueSW rq1IssueSW, DmPPTBCRelease bc, DmPPTFCRelease fc, DmPPTRelease release, DmRq1Irm irm, String rbComment, String ctc, String issueFdRq1) {
        super(rq1IssueSW, bc, fc, release, irm, rbComment, ctc, issueFdRq1);
    }
    
    abstract public ProjektBewertung getProjektBewertung();

}
