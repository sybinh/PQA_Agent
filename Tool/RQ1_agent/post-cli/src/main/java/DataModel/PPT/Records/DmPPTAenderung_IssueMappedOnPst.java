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
import Rq1Data.Enumerations.LifeCycleState_IRM;

/**
 * Issue Without FC and without BC connection
 *
 * @author gug2wi
 */
public class DmPPTAenderung_IssueMappedOnPst extends DmPPTAenderung_OnPst {

    final private DmRq1Irm irm;

    //Issue Without FC and without BC connection
    public DmPPTAenderung_IssueMappedOnPst(DmRq1IssueSW rq1IssueSW, DmPPTRelease release, DmRq1Irm irm, String rbComment, String ctc) {
        super(rq1IssueSW, null, null, release, irm, rbComment, ctc, "");
        assert (rq1IssueSW != null);
        assert (release != null);
        assert (irm != null);

        this.irm = irm;
    }

    @Override
    public ProjektBewertung getProjektBewertung() {
        if (irm.isInLifeCycleState(LifeCycleState_IRM.NEW, LifeCycleState_IRM.REQUESTED, LifeCycleState_IRM.CONFLICTED) == true) {
            return (ProjektBewertung.ANGEFORDERT);
        } else if (irm.isInLifeCycleState(LifeCycleState_IRM.PLANNED, LifeCycleState_IRM.IMPLEMENTED, LifeCycleState_IRM.QUALIFIED) == true) {
            return (ProjektBewertung.GEPLANT);
        } else {
            return (ProjektBewertung.NONE);
        }
    }

}
