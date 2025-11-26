/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Doors;

import DataModel.Doors.Records.DmDoorsElement;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Types.DmMappedElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_RequirementsOnPst extends DmRq1Field_RequirementsOnRelease {

    final private DmRq1Pst pstRelease;

    public DmRq1Field_RequirementsOnPst(DmRq1Pst parent, String nameForUserInterface) {
        super(parent, nameForUserInterface);
        this.pstRelease = parent;
    }

    @Override
    protected List<DmRq1IssueToDoorsLink> getContent() {

        List<DmRq1IssueToDoorsLink> content = new ArrayList<>();

        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : pstRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getMap().isCanceled() == false) {
                DmRq1IssueSW i_sw = (DmRq1IssueSW) m.getTarget();
                for (DmMappedElement<DmRq1IssueToDoorsLink, DmDoorsElement> map : i_sw.MAPPED_REQUIREMENTS.getElementList()) {
                    content.add(map.getMap());
                }
                i_sw.MAPPED_REQUIREMENTS.addChangeListener(this);
            }
        }

        pstRelease.MAPPED_ISSUES.addChangeListener(this);

        return (content);
    }

}
