/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1ExternalLink;

/**
 *
 * @author GUG2WI
 */
public class DmRq1LinkToDng_OnIssueAndIrm extends DmRq1LinkToRequirement_OnIssueAndIrm {

    final private DmRq1ExternalLink externalLinkToDng;

    public DmRq1LinkToDng_OnIssueAndIrm(DmRq1Element rq1Element, DmRq1ExternalLink externalLinkToDng) {
        super(rq1Element, DmRq1LinkToRequirement_Type.NONE, externalLinkToDng.getId(), externalLinkToDng.TARGET_URL.getValueAsText(), externalLinkToDng.EXT_LINK_COMMENT.getValueAsText());

        this.externalLinkToDng = externalLinkToDng;
    }

    @Override
    public DmElementI getRequirement() {
        return (externalLinkToDng.EXTERNAL_LINK.getElement());
    }
    
    public DmRq1ExternalLink getExternalLink(){
        return externalLinkToDng;
    }

}
