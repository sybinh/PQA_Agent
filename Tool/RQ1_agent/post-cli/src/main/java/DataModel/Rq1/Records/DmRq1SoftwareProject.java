/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_SoftwareMetrics;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_TableAsList;
import DataModel.Rq1.Types.DmRq1Table_PstCreationOffset;
import OslcAccess.OslcLoadHint;
import Rq1Cache.Records.Rq1Bc;
import Rq1Cache.Records.Rq1Irm;
import Rq1Cache.Records.Rq1IssueFD;
import Rq1Cache.Records.Rq1IssueSW;
import Rq1Cache.Records.Rq1Pst;
import Rq1Cache.Records.Rq1Release;
import Rq1Cache.Records.Rq1SoftwareProject;
import Rq1Cache.Records.Rq1WorkItem;
import Rq1Cache.Types.Rq1XmlTable_Enumeration;
import Rq1Cache.Types.Rq1XmlTable_ProjectDerivatives;
import Rq1Cache.Types.Rq1XmlTable_QmmFilterCriteria;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
public class DmRq1SoftwareProject extends DmRq1Project {

    final public DmRq1Field_ReferenceList<DmRq1Pst> ALL_PST;
    final public DmRq1Field_ReferenceList<DmRq1Bc> ALL_BC;
    final public DmRq1Field_ReferenceList<DmRq1Fc> ALL_FC;
    final public DmRq1Field_ReferenceList<DmRq1Pst> OPEN_PST;
    final public DmRq1Field_ReferenceList<DmRq1Pst> OPEN_PST_RELEASE;
    final public DmRq1Field_ReferenceList<DmRq1Bc> OPEN_BC;
    final public DmRq1Field_ReferenceList<DmRq1Fc> OPEN_FC;

    final public DmRq1Field_ReferenceList<DmRq1Pst> PROPLATO_PST;

    final public DmRq1Field_ReferenceList<DmRq1IssueSW> OPEN_ISSUE_SW;
    final public DmRq1Field_ReferenceList<DmRq1IssueFD> OPEN_ISSUE_FD;
    final public DmRq1Field_ReferenceList<DmRq1IssueSW> ALL_ISSUE_SW;
    final public DmRq1Field_ReferenceList<DmRq1IssueFD> ALL_ISSUE_FD;

    final public DmRq1Field_Table<Rq1XmlTable_Enumeration> ISSUE_SW_SUB_CATEGORIES;

    final public DmRq1Field_ReferenceList<DmRq1Pst> PROPLATO_PST_ROADMAP;
    final public DmRq1Field_ReferenceList<DmRq1Pst> PROPLATO_PST_ROADMAP_WITHOUT_DATE;
    final public DmRq1Field_TableAsList<Rq1XmlTable_ProjectDerivatives, String> DERIVATIVES;

    final public DmRq1Table_PstCreationOffset PST_CREATION_OFFSET;

    final public DmRq1Field_ReferenceList<DmRq1IssueSW> NOT_CANCELED_ISSUE_SW;

    final public DmRq1Field_SoftwareMetrics SW_METRICS;
    final public DmRq1Field_TableAsList<Rq1XmlTable_QmmFilterCriteria, String> PROFIT_CENTER;
    final public DmRq1Field_TableAsList<Rq1XmlTable_QmmFilterCriteria, String> P_ID_DISPLAY_NAME;
    final public DmRq1Field_TableAsList<Rq1XmlTable_QmmFilterCriteria, String> PROJECT_STATUS;

    public DmRq1SoftwareProject(String subjectType, Rq1SoftwareProject rq1Project) {
        super(subjectType, rq1Project);

        //
        // Releases
        //
        addField(ALL_PST = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_PST, "All PVER/PFAM"));
        addField(ALL_BC = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_BC, "All BC"));
        addField(ALL_FC = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_FC, "All FC"));
        addField(OPEN_PST_RELEASE = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_PST_RELEASE, "Open PVER/PFAM Release"));
        addField(OPEN_PST = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_PST, "Open PVER/PFAM"));
        addField(OPEN_BC = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_BC, "Open BC"));
        addField(OPEN_FC = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_FC, "Open FC"));

        addField(PROPLATO_PST = new DmRq1Field_ReferenceList<>(this, rq1Project.PROPLATO_PST, "ProPlaTo"));

        //
        // Issues
        //
        addField(OPEN_ISSUE_SW = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_ISSUE_SW, "Open I-SW"));
        addField(OPEN_ISSUE_FD = new DmRq1Field_ReferenceList<>(this, rq1Project.OPEN_ISSUE_FD, "Open I-FD"));
        addField(ALL_ISSUE_SW = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_ISSUE_SW, "All I-SW"));
        addField(ALL_ISSUE_FD = new DmRq1Field_ReferenceList<>(this, rq1Project.ALL_ISSUE_FD, "All I-FD"));

        addField(ISSUE_SW_SUB_CATEGORIES = new DmRq1Field_Table<Rq1XmlTable_Enumeration>(this, rq1Project.ISSUE_SW_SUB_CATEGORIES, "IssueSWC"));

        addField(DERIVATIVES = new DmRq1Field_TableAsList<>(rq1Project.DERIVATIVES, "Derivatives"));

        addField(PST_CREATION_OFFSET = new DmRq1Table_PstCreationOffset(rq1Project.PST_CREATION_OFFSET, "Offset for new PVER/PVAR"));

        addField(PROPLATO_PST_ROADMAP = new DmRq1Field_ReferenceList<>(this, rq1Project.PPT_PST_SINCE_2014, "ProPlaTo Roadmap Pst"));
        addField(PROPLATO_PST_ROADMAP_WITHOUT_DATE = new DmRq1Field_ReferenceList<>(this, rq1Project.PPT_PST_ALL, "ProPlaTo Roadmap Pst without Date Limitation"));
        addField(NOT_CANCELED_ISSUE_SW = new DmRq1Field_ReferenceList<>(this, rq1Project.NOT_CANCELED_ISSUE_SW, "Pro Pla To I-SW"));

        addField(SW_METRICS = new DmRq1Field_SoftwareMetrics(rq1Project.SW_METRICS_VALUES, "Software Metrics"));

        addField(PROFIT_CENTER = new DmRq1Field_TableAsList<Rq1XmlTable_QmmFilterCriteria, String>(rq1Project.PROFIT_CENTER, "Profit Center"));
        addField(P_ID_DISPLAY_NAME = new DmRq1Field_TableAsList<Rq1XmlTable_QmmFilterCriteria, String>(rq1Project.P_ID_DISPLAY_NAME, "P Id Display Name"));
        addField(PROJECT_STATUS = new DmRq1Field_TableAsList<Rq1XmlTable_QmmFilterCriteria, String>(rq1Project.PROJECT_STATUS, "Project Status"));

    }

    /**
     * Returns all derivatives defined in this project.
     *
     * @return A list of all derivatives.
     */
    public Collection<String> getDerivativeNames() {
        return (DERIVATIVES.getList());
    }

    //-------------------------------------------------------------------------------------
    //
    // Load optimization
    //
    //-------------------------------------------------------------------------------------
    private boolean isLoadCacheWithProPlaToIssuesDone = false;
    private boolean isLoadCacheForFlowListBcDone = false;
    private boolean isLoadCacheForFlowListIFDDone = false;
    private boolean isLoadCacheForFlowListIFDNoOptDone = false;
    private boolean isLoadCacheForFlowListBcNoOptDone = false;
    private boolean isLoadCacheForCeFlowDone = false;
    private boolean isLoadCacheForCrpDone = false;

    /**
     * Loads NOT_CANCELED_ISSUE_SW -> FIELDNAME_HAS_CHILDREN ->
     * FIELDNAME_AFFECTED_ISSUE
     */
    public void loadCacheWithProPlaToIssues() {

        if (isLoadCacheWithProPlaToIssuesDone == false) {

            OslcLoadHint loadHintIssueSw = new OslcLoadHint(true);
            loadHintIssueSw.followField(Rq1IssueSW.FIELDNAME_HAS_CHILDREN, true);
            loadHintIssueSw.followField(Rq1IssueSW.FIELDNAME_AFFECTED_ISSUE, true);

            NOT_CANCELED_ISSUE_SW.loadCache(loadHintIssueSw);

            isLoadCacheWithProPlaToIssuesDone = true;
        }
    }

    /**
     * OPEN_PST
     *
     * OPEN_PST_RELEASE -> ATTRIBUTE_HAS_MAPPED_ISSUES ->
     * ATTRIBUTE_HAS_MAPPED_ISSUE
     *
     * OPEN_PST_RELEASE -> FIELDNAME_HAS_MAPPED_CHILDREN ->
     * ATTRIBUTE_HAS_MAPPED_RELEASE
     */
    public void loadCacheForCEFlow() {

        if (isLoadCacheForCeFlowDone == false) {

            final List<DmRq1Pst> pstList = OPEN_PST.getElementList();

            if (!pstList.isEmpty()) {
                OslcLoadHint loadHintIssue = new OslcLoadHint(false);
                loadHintIssue.followField(Rq1Pst.ATTRIBUTE_HAS_MAPPED_ISSUES, true).followField(Rq1Irm.ATTRIBUTE_HAS_MAPPED_ISSUE, true);
                OPEN_PST_RELEASE.loadCache(loadHintIssue);

                OslcLoadHint loadHintBc = new OslcLoadHint(false);
                loadHintBc.followField(Rq1Pst.FIELDNAME_HAS_MAPPED_CHILDREN, true).followField(Rq1Irm.ATTRIBUTE_HAS_MAPPED_RELEASE, true);
                OPEN_PST_RELEASE.loadCache(loadHintBc);
            }

            for (DmRq1Pst dmRq1Pst : pstList) {
                dmRq1Pst.MAPPED_CHILDREN.getElementList();
                dmRq1Pst.MAPPED_ISSUES.getElementList();
            }

            isLoadCacheForCeFlowDone = true;
        }
    }

    /**
     * OPEN_PST -> MAPPED_CHILDREN -> MAPPED_ISSUES
     */
    public void loadCacheForCEFlow_NoOptimization() {

        if (isLoadCacheForCeFlowDone == false) {

            final List<DmRq1Pst> pstList = OPEN_PST.getElementList();

            for (DmRq1Pst dmRq1Pst : pstList) {
                dmRq1Pst.MAPPED_CHILDREN.getElementList();
                dmRq1Pst.MAPPED_ISSUES.getElementList();
            }

            isLoadCacheForCeFlowDone = true;
        }
    }

    /**
     * OPEN_BC -> ATTRIBUTE_HAS_MAPPED_ISSUES -> ATTRIBUTE_HAS_MAPPED_RELEASES
     */
    public void loadCacheForFlowListFromBC() {

        if (isLoadCacheForFlowListBcDone == false) {

            final List<DmRq1Bc> bcList = OPEN_BC.getElementList();
            if (!bcList.isEmpty()) {
                OslcLoadHint loadHint = new OslcLoadHint(false);
                loadHint.followField(Rq1Bc.ATTRIBUTE_HAS_MAPPED_ISSUES, true).followField(Rq1IssueFD.ATTRIBUTE_HAS_MAPPED_RELEASES, true);
                OPEN_BC.loadCache(loadHint);
            }

            for (DmRq1Bc dmRq1Bc : bcList) {
                dmRq1Bc.MAPPED_ISSUES.getElementList();
            }

            isLoadCacheForFlowListBcDone = true;
        }
    }

    /**
     * OPEN_BC -> MAPPED_ISSUES
     */
    public void loadCacheForFlowListFromBC_NoOptimization() {

        if (isLoadCacheForFlowListBcNoOptDone == false) {

            final List<DmRq1Bc> bcList = OPEN_BC.getElementList();

            for (DmRq1Bc dmRq1Bc : bcList) {
                dmRq1Bc.MAPPED_ISSUES.getElementList();
            }

            isLoadCacheForFlowListBcNoOptDone = true;
        }
    }

    /**
     * OPEN_ISSUE_FD -> ATTRIBUTE_HAS_MAPPED_RELEASES ->
     * ATTRIBUTE_HAS_MAPPED_RELEASE
     *
     */
    public void loadCacheForFlowListFromIFD(boolean doReload) {

        if (isLoadCacheForFlowListIFDDone == false || doReload) {

            final List<DmRq1IssueFD> ifdList = OPEN_ISSUE_FD.getElementList();

            OslcLoadHint loadHint = new OslcLoadHint(false);
            loadHint.followField(Rq1IssueFD.ATTRIBUTE_HAS_MAPPED_RELEASES, true).followField(Rq1Irm.ATTRIBUTE_HAS_MAPPED_RELEASE, true);
            OPEN_ISSUE_FD.loadCache(loadHint);

            for (DmRq1IssueFD dmRq1Ifd : ifdList) {
                dmRq1Ifd.MAPPED_BC.getElementList();
            }

            isLoadCacheForFlowListIFDDone = true;
        }
    }

    /**
     * OPEN_ISSUE_FD -> MAPPED_BC
     */
    public void loadCacheForFlowListFromIFD_NoOptimization() {

        if (isLoadCacheForFlowListIFDNoOptDone == false) {

            final List<DmRq1IssueFD> ifdList = OPEN_ISSUE_FD.getElementList();

            for (DmRq1IssueFD dmRq1Ifd : ifdList) {
                dmRq1Ifd.MAPPED_BC.getElementList();
            }

            isLoadCacheForFlowListIFDNoOptDone = true;
        }
    }

    /**
     * OPEN_WORKITEMS -> SUCCESSOR and PREDECESSOR
     */
    public void loadCacheForCrp() {

        if (isLoadCacheForCrpDone == false) {

            OslcLoadHint loadHintForPreAndSucc = new OslcLoadHint(true);
            loadHintForPreAndSucc.followField(Rq1WorkItem.ATTRIBUTE_HAS_PREDECESSOR, true); // Predecessor on WI
            loadHintForPreAndSucc.followField(Rq1WorkItem.ATTRIBUTE_HAS_SUCCESSOR, true); // Successor on WI

            ALL_WORKITEMS.loadCache(loadHintForPreAndSucc);

            OslcLoadHint loadHintRelease = new OslcLoadHint(true);
            loadHintRelease.followField(Rq1Release.ATTRIBUTE_HAS_WORKITEMS, true);
            ALL_RELEASES.loadCache(loadHintRelease);

            isLoadCacheForCrpDone = true;
        }

    }

}
