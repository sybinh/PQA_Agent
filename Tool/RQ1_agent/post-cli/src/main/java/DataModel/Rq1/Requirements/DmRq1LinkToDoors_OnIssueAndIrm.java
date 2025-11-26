/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.Doors.Records.DmDoorsElement;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1Irm;
import java.util.logging.Logger;

/**
 *
 * @author gug2wi
 */
public class DmRq1LinkToDoors_OnIssueAndIrm extends DmRq1LinkToRequirement_OnIssueAndIrm {

    final private static Logger LOGGER = Logger.getLogger(DmRq1LinkToDoors_OnIssueAndIrm.class.getCanonicalName());

    final private DmDoorsElement doorsElement;

    public DmRq1LinkToDoors_OnIssueAndIrm(DmRq1Element rq1Element, DmRq1LinkToRequirement_Type linkType, String requirementId, String linkToDoors, DmDoorsElement doorsElement) {
        super(rq1Element, linkType, requirementId, linkToDoors, "");
        assert (doorsElement != null);
        this.doorsElement = doorsElement;
    }

    public DmRq1LinkToDoors_OnIssueAndIrm(DmRq1Irm rq1Irm, DmRq1LinkToRequirement_Type linkType, String requirementId, String comment, String linkToDoors, DmDoorsElement doorsElement) {
        super(rq1Irm, linkType, requirementId, linkToDoors, comment);
        assert (doorsElement != null);
        this.doorsElement = doorsElement;
    }

    @Override
    public DmDoorsElement getRequirement() {
        return doorsElement;
    }

}
