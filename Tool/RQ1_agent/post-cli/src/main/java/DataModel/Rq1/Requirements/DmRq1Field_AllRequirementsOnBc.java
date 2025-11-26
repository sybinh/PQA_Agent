/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.DmMappedElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_AllRequirementsOnBc extends DmRq1Field_AllRequirementsOnRelease {

    final private DmRq1Bc bcRelease;

    public DmRq1Field_AllRequirementsOnBc(DmRq1Bc parent, String nameForUserInterface) {
        super(parent, nameForUserInterface);
        this.bcRelease = parent;
    }

    @Override
    protected List<DmRq1LinkToRequirement_OnIssueAndIrm> getContent() {

        List<DmRq1LinkToRequirement_OnIssueAndIrm> content = new ArrayList<>();

        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : bcRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getMap().isCanceled() == false) {
                DmRq1IssueFD i_fd = (DmRq1IssueFD) m.getTarget();
                for (DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI> mappedElement : i_fd.MAPPED_REQUIREMENTS.getElementList()) {
                    content.add(mappedElement.getMap());
                }
                i_fd.MAPPED_REQUIREMENTS.addChangeListener(this);
            }
        }

        bcRelease.MAPPED_ISSUES.addChangeListener(this);

        return (content);
    }

}
