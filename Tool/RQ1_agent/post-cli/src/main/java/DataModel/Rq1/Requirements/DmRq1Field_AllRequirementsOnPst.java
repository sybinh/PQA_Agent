/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.DmElementI;
import DataModel.Doors.Records.DmDoorsFactory;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.DmMappedElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_AllRequirementsOnPst extends DmRq1Field_AllRequirementsOnRelease {

    final private DmRq1Pst pstRelease;

    public DmRq1Field_AllRequirementsOnPst(DmRq1Pst parent, String nameForUserInterface) {
        super(parent, nameForUserInterface);
        this.pstRelease = parent;
    }

    @Override
    protected List<DmRq1LinkToRequirement_OnIssueAndIrm> getContent() {

        List<DmRq1LinkToRequirement_OnIssueAndIrm> content = new ArrayList<>();

        //--------------------------------------------------
        //
        // Load all DOORS elements into cache.
        //
        //--------------------------------------------------
        Set<String> urlToDoorsElements = new TreeSet<>();
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> mappedIssue : pstRelease.MAPPED_ISSUES.getElementList()) {
            if (mappedIssue.getMap().isCanceled() == false) {

                DmRq1IssueSW i_sw = (DmRq1IssueSW) mappedIssue.getTarget();
                urlToDoorsElements.addAll(i_sw.MAPPED_DOORS_REQUIREMENTS.getUrlToDoorsElements());

                if (mappedIssue.getMap() instanceof DmRq1Irm_Pst_IssueSw) {
                    DmRq1Irm_Pst_IssueSw irm = (DmRq1Irm_Pst_IssueSw) mappedIssue.getMap();
                    urlToDoorsElements.addAll(irm.MAPPED_REQUIREMENTS.getUrlToDoorsElements());
                }
            }
        }
        DmDoorsFactory.getElementsByUrls(urlToDoorsElements);

        //--------------------
        //
        // Load element list
        //
        //--------------------
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> mappedIssue : pstRelease.MAPPED_ISSUES.getElementList()) {
            if (mappedIssue.getMap().isCanceled() == false) {
                //
                // Add requirements from I-SW
                //
                DmRq1IssueSW i_sw = (DmRq1IssueSW) mappedIssue.getTarget();
                for (DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI> map : i_sw.MAPPED_REQUIREMENTS.getElementList()) {
                    content.add(map.getMap());
                }
                i_sw.MAPPED_REQUIREMENTS.addChangeListener(this);
                //
                // Add requirements from IRM
                //
                if (mappedIssue.getMap() instanceof DmRq1Irm_Pst_IssueSw) {
                    DmRq1Irm_Pst_IssueSw irm = (DmRq1Irm_Pst_IssueSw) mappedIssue.getMap();
                    for (DmMappedElement<DmRq1LinkToRequirement_OnIssueAndIrm, DmElementI> map : irm.MAPPED_REQUIREMENTS.getElementList()) {
                        content.add(map.getMap());
                    }
                    irm.MAPPED_REQUIREMENTS.addChangeListener(this);
                }
            }
        }

        pstRelease.MAPPED_ISSUES.addChangeListener(this);

        return (content);
    }

}
