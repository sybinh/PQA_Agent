/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.Rq1.Records.DmRq1IssueSW;

/**
 *
 * @author gug2wi
 */
public class DmPPTAenderung_IssueSW extends DmPPTAenderung {

    public DmPPTAenderung_IssueSW(DmRq1IssueSW rq1IssueSW) {
        super(rq1IssueSW, null, null, null, null, "", "", "");
        assert (rq1IssueSW != null);
    }

}
