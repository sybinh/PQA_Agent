/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.DmRq1NodeInterface;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList_PverWithAssignedIrmOrIsw;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1User;
import Rq1Cache.Rq1RecordIndex;
import UiSupport.UiTreeViewRootElementI;
import java.util.ArrayList;
import java.util.List;
import util.EcvApplication;
import util.EcvEnumeration;

/**
 *
 * @author gug2wi
 */
public class DmRq1User extends DmRq1Element implements DmRq1NodeInterface, UiTreeViewRootElementI {

    static private DmRq1User loginUser = null;

    public static DmRq1User getLoginUser() {

        if (loginUser != null) {
            return (loginUser);
        }

        Rq1RecordIndex.loadLoginUser();
        String loginName = EcvApplication.getUserName();
        if (loginName != null && loginName.trim().isEmpty() == false) {
            return (loginUser = (DmRq1User) DmRq1Element.getElementById(loginName));
        }
        return (null);
    }

    public static String getLoginName() {
        if (getLoginUser() != null) {
            return (loginUser.LOGIN_NAME.getValue());
        } else {
            return ("unknown");
        }
    }

    final public DmRq1Field_Text EMAIL;
    final public DmRq1Field_Text FULLNAME;
    final public DmRq1Field_Text LOGIN_NAME;
    final public DmRq1Field_Enumeration IS_ACTIVE;
    final public DmRq1Field_Text MISC_INFO;
    final public DmRq1Field_Text PHONE;

    final public DmRq1Field_ReferenceList<DmRq1IssueSW> OPEN_ASSIGNED_ISSUE_SW;
    final public DmRq1Field_ReferenceList<DmRq1IssueFD> OPEN_ASSIGNED_ISSUE_FD;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwEcu> OPEN_ASSIGNED_ISSUE_HW_ECU;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwMod> OPEN_ASSIGNED_ISSUE_HW_MOD;

    final public DmRq1Field_ReferenceList<DmRq1IssueSW> ALL_ASSIGNED_ISSUE_SW;
    final public DmRq1Field_ReferenceList<DmRq1IssueFD> ALL_ASSIGNED_ISSUE_FD;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwEcu> ALL_ASSIGNED_ISSUE_HW_ECU;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwMod> ALL_ASSIGNED_ISSUE_HW_MOD;

    final public DmRq1Field_ReferenceList<DmRq1IssueSW> OPEN_SUBMITTED_ISSUE_SW;
    final public DmRq1Field_ReferenceList<DmRq1IssueFD> OPEN_SUBMITTED_ISSUE_FD;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwEcu> OPEN_SUBMITTED_ISSUE_HW_ECU;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwMod> OPEN_SUBMITTED_ISSUE_HW_MOD;

    final public DmRq1Field_ReferenceList<DmRq1IssueSW> ALL_SUBMITTED_ISSUE_SW;
    final public DmRq1Field_ReferenceList<DmRq1IssueFD> ALL_SUBMITTED_ISSUE_FD;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwEcu> ALL_SUBMITTED_ISSUE_HW_ECU;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwMod> ALL_SUBMITTED_ISSUE_HW_MOD;

    final public DmRq1Field_ReferenceList<DmRq1IssueSW> OPEN_REQUESTED_ISSUE_SW;
    final public DmRq1Field_ReferenceList<DmRq1IssueFD> OPEN_REQUESTED_ISSUE_FD;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwEcu> OPEN_REQUESTED_ISSUE_HW_ECU;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwMod> OPEN_REQUESTED_ISSUE_HW_MOD;

    final public DmRq1Field_ReferenceList<DmRq1Milestone> OPEN_ASSIGNED_MILESTONE;
    final public DmRq1Field_ReferenceList<DmRq1Pst> OPEN_ASSIGNED_PST_COLLECTIONS;
    final public DmRq1Field_ReferenceList<DmRq1Pst> OPEN_ASSIGNED_PST_RELEASE;
    final public DmRq1Field_ReferenceList<DmRq1Bc> OPEN_SUBMITTED_BC;
    final public DmRq1Field_ReferenceList<DmRq1Bc> OPEN_ASSIGNED_BC;
    final public DmRq1Field_ReferenceList<DmRq1Bc> OPEN_REQUESTED_BC;
    final public DmRq1Field_ReferenceList<DmRq1Fc> OPEN_SUBMITTED_FC;
    final public DmRq1Field_ReferenceList<DmRq1Fc> OPEN_ASSIGNED_FC;
    final public DmRq1Field_ReferenceList<DmRq1Fc> OPEN_REQUESTED_FC;
    final public DmRq1Field_ReferenceList<DmRq1EcuRelease> OPEN_ASSIGNED_RELEASE_HW_ECU;
    final public DmRq1Field_ReferenceList<DmRq1ModRelease> OPEN_ASSIGNED_RELEASE_HW_MOD;
    final public DmRq1Field_ReferenceList<DmRq1CompRelease> OPEN_ASSIGNED_RELEASE_HW_COMP;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> OPEN_SUBMITTED_WORKITEM;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> OPEN_ASSIGNED_WORKITEM;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> OPEN_REQUESTED_WORKITEM;

    final public DmRq1Field_ReferenceList<DmRq1Milestone> ALL_ASSIGNED_MILESTONE;
    final public DmRq1Field_ReferenceList<DmRq1Pst> ALL_ASSIGNED_PST_COLLECTIONS;
    final public DmRq1Field_ReferenceList<DmRq1Pst> ALL_ASSIGNED_PST_RELEASE;
    final public DmRq1Field_ReferenceList<DmRq1Bc> ALL_ASSIGNED_BC;
    final public DmRq1Field_ReferenceList<DmRq1Fc> ALL_ASSIGNED_FC;
    final public DmRq1Field_ReferenceList<DmRq1EcuRelease> ALL_ASSIGNED_RELEASE_HW_ECU;
    final public DmRq1Field_ReferenceList<DmRq1ModRelease> ALL_ASSIGNED_RELEASE_HW_MOD;
    final public DmRq1Field_ReferenceList<DmRq1CompRelease> ALL_ASSIGNED_RELEASE_HW_COMP;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> ALL_ASSIGNED_WORKITEM;
    final public DmRq1Field_ReferenceList<DmRq1WorkItem> ALL_REQUESTED_WORKITEM;
    final public DmRq1Field_ReferenceList<DmRq1SoftwareProject> PROJECTS;

    final public DmRq1Field_ReferenceList<DmRq1Irm> OPEN_ASSIGNED_IRM;
    final public DmRq1Field_ReferenceList<DmRq1Irm> ALL_ASSIGNED_IRM;

    final public DmRq1Field_ReferenceList<DmRq1Rrm> OPEN_ASSIGNED_RRM;
    final public DmRq1Field_ReferenceList<DmRq1Rrm> ALL_ASSIGNED_RRM;

    final public DmRq1Field_ReferenceList<DmRq1Problem> OPEN_ASSIGNED_PROBLEM;
    final public DmRq1Field_ReferenceList<DmRq1Problem> ALL_ASSIGNED_PROBLEM;

    final public DmRq1Field_ReferenceList<DmRq1Problem> OPEN_CONTACT_PROBLEM;
    final public DmRq1Field_ReferenceList<DmRq1Problem> ALL_CONTACT_PROBLEM;
    
    final public DmRq1Field_ReferenceList<DmRq1Problem> OPEN_SUBMITTED_PROBLEM;
    final public DmRq1Field_ReferenceList<DmRq1Problem> ALL_SUBMITTED_PROBLEM;

    final public DmRq1Field_ReferenceList_PverWithAssignedIrmOrIsw PST_WITH_OPEN_ASSIGNED_IRM_OR_ISW;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")

    public DmRq1User(Rq1User rq1User) {
        super("RQ1-User", rq1User);

        //
        // Create and add fields
        //
        addField(EMAIL = new DmRq1Field_Text(this, rq1User.EMAIL, "E-Mail"));
        addField(FULLNAME = new DmRq1Field_Text(this, rq1User.FULLNAME, "Full Name"));
        addField(LOGIN_NAME = new DmRq1Field_Text(this, rq1User.LOGIN_NAME, "Login Name"));
        addField(IS_ACTIVE = new DmRq1Field_Enumeration(this, rq1User.IS_ACTIVE, "Is Active"));
        addField(MISC_INFO = new DmRq1Field_Text(this, rq1User.MISC_INFO, "Misc Info"));
        addField(PHONE = new DmRq1Field_Text(this, rq1User.PHONE, "Phone"));

        addField(OPEN_ASSIGNED_ISSUE_SW = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_ISSUE_SW, "Open assigned I-SW"));
        addField(OPEN_ASSIGNED_ISSUE_FD = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_ISSUE_FD, "Open assigned I-FD"));
        addField(OPEN_ASSIGNED_ISSUE_HW_ECU = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_ISSUE_HW_ECU, "Open assigned I-HW-ECU"));
        addField(OPEN_ASSIGNED_ISSUE_HW_MOD = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_ISSUE_HW_MOD, "Open assigned I-HW-MOD"));

        addField(ALL_SUBMITTED_ISSUE_SW = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_SUBMITTED_ISSUE_SW, "All submitted I-SW"));
        addField(ALL_SUBMITTED_ISSUE_FD = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_SUBMITTED_ISSUE_FD, "All submitted I-FD"));
        addField(ALL_SUBMITTED_ISSUE_HW_ECU = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_SUBMITTED_ISSUE_HW_ECU, "All submitted I-HW-ECU"));
        addField(ALL_SUBMITTED_ISSUE_HW_MOD = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_SUBMITTED_ISSUE_HW_MOD, "All submitted I-HW-MOD"));

        addField(OPEN_SUBMITTED_ISSUE_SW = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_SUBMITTED_ISSUE_SW, "Open submitted I-SW"));
        addField(OPEN_SUBMITTED_ISSUE_FD = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_SUBMITTED_ISSUE_FD, "Open submitted I-FD"));
        addField(OPEN_SUBMITTED_ISSUE_HW_ECU = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_SUBMITTED_ISSUE_HW_ECU, "Open submitted I-HW-ECU"));
        addField(OPEN_SUBMITTED_ISSUE_HW_MOD = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_SUBMITTED_ISSUE_HW_MOD, "Open submitted I-HW-MOD"));

        addField(OPEN_REQUESTED_ISSUE_SW = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_REQUESTED_ISSUE_SW, "Open requested I-SW"));
        addField(OPEN_REQUESTED_ISSUE_FD = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_REQUESTED_ISSUE_FD, "Open requested I-FD"));
        addField(OPEN_REQUESTED_ISSUE_HW_ECU = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_REQUESTED_ISSUE_HW_ECU, "Open requested I-HW-ECU"));
        addField(OPEN_REQUESTED_ISSUE_HW_MOD = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_REQUESTED_ISSUE_HW_MOD, "Open requested I-HW-MOD"));

        addField(OPEN_ASSIGNED_MILESTONE = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_MILESTONE, "Open Milestones"));
        addField(OPEN_ASSIGNED_PST_COLLECTIONS = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_PST_COLLECTIONS, "Open PVER/PFAM Collections"));
        addField(OPEN_ASSIGNED_PST_RELEASE = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_PST_RELEASE, "Open assigned PVER/PFAM"));
        addField(OPEN_SUBMITTED_BC = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_SUBMITTED_BC, "Open submitted BC"));
        addField(OPEN_ASSIGNED_BC = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_BC, "Open assigned BC"));
        addField(OPEN_REQUESTED_BC = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_REQUESTED_BC, "Open requested BC"));
        addField(OPEN_SUBMITTED_FC = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_SUBMITTED_FC, "Open submitted FC"));
        addField(OPEN_ASSIGNED_FC = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_FC, "Open assigned FC"));
        addField(OPEN_REQUESTED_FC = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_REQUESTED_FC, "Open requested FC"));
        addField(OPEN_ASSIGNED_RELEASE_HW_ECU = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_RELEASE_HW_ECU, "Open assigned HW-ECU Release"));
        addField(OPEN_ASSIGNED_RELEASE_HW_MOD = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_RELEASE_HW_MOD, "Open assigned HW-MOD Release"));
        addField(OPEN_ASSIGNED_RELEASE_HW_COMP = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_RELEASE_HW_COMP, "Open assigned HW-COMP Release"));

        addField(OPEN_SUBMITTED_WORKITEM = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_SUBMITTED_WORKITEM, "Open submitted Workitem"));
        addField(OPEN_ASSIGNED_WORKITEM = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_WORKITEM, "Open assigned Workitem"));
        addField(OPEN_REQUESTED_WORKITEM = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_REQUESTED_WORKITEM, "Open requested Workitem"));

        addField(ALL_ASSIGNED_ISSUE_SW = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_ISSUE_SW, "All assigned I-SW"));
        addField(ALL_ASSIGNED_ISSUE_FD = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_ISSUE_FD, "All assigned I-FD"));
        addField(ALL_ASSIGNED_ISSUE_HW_ECU = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_ISSUE_HW_ECU, "All assigned I-HW-ECU"));
        addField(ALL_ASSIGNED_ISSUE_HW_MOD = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_ISSUE_HW_MOD, "All assigned I-HW-MOD"));

        addField(ALL_ASSIGNED_MILESTONE = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_MILESTONE, "All Milestones"));
        addField(ALL_ASSIGNED_PST_COLLECTIONS = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_PST_COLLECTIONS, "All assigned PVER/PFAM Collections"));
        addField(ALL_ASSIGNED_PST_RELEASE = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_PST_RELEASES, "All assigned PVER/PFAM"));
        addField(ALL_ASSIGNED_BC = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_BC, "All assigned BC"));
        addField(ALL_ASSIGNED_FC = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_FC, "All assigned FC"));
        addField(ALL_ASSIGNED_RELEASE_HW_ECU = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_RELEASE_HW_ECU, "All assigned HW-ECU Release"));
        addField(ALL_ASSIGNED_RELEASE_HW_MOD = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_RELEASE_HW_MOD, "All assigned HW-MOD Release"));
        addField(ALL_ASSIGNED_RELEASE_HW_COMP = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_RELEASE_HW_COMP, "All assigned HW-COMP Release"));

        addField(ALL_ASSIGNED_WORKITEM = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_WORKITEM, "All assigned Workitem"));
        addField(ALL_REQUESTED_WORKITEM = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_REQUESTED_WORKITEM, "All requested Workitem"));

        addField(PROJECTS = new DmRq1Field_ReferenceList<>(this, rq1User.PROJECTS, "Projects"));

        addField(OPEN_ASSIGNED_IRM = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_IRM, "Open assigned IRM"));
        addField(ALL_ASSIGNED_IRM = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_IRM, "All assigned IRM"));

        addField(OPEN_ASSIGNED_RRM = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_RRM, "Open assigned RRM"));
        addField(ALL_ASSIGNED_RRM = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_RRM, "All assigned RRM"));

        addField(OPEN_ASSIGNED_PROBLEM = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_ASSIGNED_PROBLEM, "Open assigned Problem"));
        addField(ALL_ASSIGNED_PROBLEM = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_ASSIGNED_PROBLEM, "All assigned Problem"));

        addField(OPEN_CONTACT_PROBLEM = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_CONTACT_PROBLEM, "Open contact Problem"));
        addField(ALL_CONTACT_PROBLEM = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_CONTACT_PROBLEM, "All contact Problem"));
        
        addField(OPEN_SUBMITTED_PROBLEM = new DmRq1Field_ReferenceList<>(this, rq1User.OPEN_SUBMITTED_PROBLEM, "Open submitted Problem"));
        addField(ALL_SUBMITTED_PROBLEM  = new DmRq1Field_ReferenceList<>(this, rq1User.ALL_SUBMITTED_PROBLEM, "All submitted Problem"));

        addField(PST_WITH_OPEN_ASSIGNED_IRM_OR_ISW = new DmRq1Field_ReferenceList_PverWithAssignedIrmOrIsw("PVER & PVAR with open assigned IRM or I-SW", OPEN_ASSIGNED_IRM, OPEN_ASSIGNED_ISSUE_SW));
    }

    @Override
    public boolean isCanceled() {
        return (false);
    }

    @Override
    public EcvEnumeration getLifeCycleState() {
        return (IS_ACTIVE.getValue());
    }

    @Override
    public String getTitle() {
        return (FULLNAME.getValue());
    }

    @Override
    final public String toString() {
        return (LOGIN_NAME.getValue() + " - " + FULLNAME.getValue());
    }

    @Override
    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (IS_ACTIVE.getValidInputValues());
    }

    @Override

    public String getId() {
        return this.LOGIN_NAME.getValueAsText();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DmRq1User) {
            DmRq1User otherUser = (DmRq1User) o;
            return (this.LOGIN_NAME.getValueAsText().equals(otherUser.LOGIN_NAME.getValueAsText()));
        }
        return (false);
    }

    static List<DmRq1User> allActiveUser = null;

    static public List<DmRq1User> getAllActiveUser() {

        if (allActiveUser == null) {
            allActiveUser = new ArrayList<>();
            for (Rq1User rq1User : Rq1User.getAllActiveUser()) {
                DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1User);
                if (dmElement instanceof DmRq1User) {
                    allActiveUser.add((DmRq1User) dmElement);
                }
            }
        }

        return (allActiveUser);
    }

    static public DmRq1User getUserByLoginName(String loginName) {
        assert (loginName != null);
        assert (loginName.isEmpty() == false);

        DmRq1ElementInterface element = DmRq1Element.getElementById(loginName);
        if (element instanceof DmRq1User) {
            return (DmRq1User) (element);
        }

        return (null);
    }
}
