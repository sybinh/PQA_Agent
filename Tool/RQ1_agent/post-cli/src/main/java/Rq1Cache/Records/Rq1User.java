/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import OslcAccess.OslcLoadHint;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1QueryField;
import Rq1Cache.Fields.Rq1QueryField_linkedToUser;
import Rq1Cache.Fields.Rq1ReferenceListField_FilterByClass;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import Rq1Data.Enumerations.LifeCycleState_IRM;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.LifeCycleState_Problem;
import Rq1Data.Enumerations.LifeCycleState_RRM;
import Rq1Data.Enumerations.LifeCycleState_Release;
import Rq1Data.Enumerations.LifeCycleState_WorkItem;
import Rq1Data.Enumerations.Rq1UserIsActive;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class Rq1User extends Rq1Record implements Rq1NodeInterface {

    final static String FIELDNAME_IS_ACTIVE = "is_active";
    final static String FIELDNAME_MISC_INFO = "misc_info";
    final static String FIELDNAME_LOGIN_NAME = "login_name";

    final public Rq1DatabaseField_Text EMAIL;
    final public Rq1DatabaseField_Text FULLNAME;
    final public Rq1DatabaseField_Text LOGIN_NAME;
    final public Rq1DatabaseField_Enumeration IS_ACTIVE;
    final public Rq1DatabaseField_Text MISC_INFO;
    final public Rq1DatabaseField_Text PHONE;

    final private Rq1QueryField OPEN_ASSIGNED_ISSUE;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_ISSUE_SW;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_ISSUE_FD;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_ISSUE_HW_ECU;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_ISSUE_HW_MOD;

    final private Rq1QueryField ALL_ASSIGNED_ISSUE;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_ISSUE_SW;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_ISSUE_FD;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_ISSUE_HW_ECU;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_ISSUE_HW_MOD;

    final private Rq1QueryField OPEN_SUBMITTED_ISSUE;
    final public Rq1ReferenceListField_FilterByClass OPEN_SUBMITTED_ISSUE_SW;
    final public Rq1ReferenceListField_FilterByClass OPEN_SUBMITTED_ISSUE_FD;
    final public Rq1ReferenceListField_FilterByClass OPEN_SUBMITTED_ISSUE_HW_ECU;
    final public Rq1ReferenceListField_FilterByClass OPEN_SUBMITTED_ISSUE_HW_MOD;

    final private Rq1QueryField ALL_SUBMITTED_ISSUE;
    final public Rq1ReferenceListField_FilterByClass ALL_SUBMITTED_ISSUE_SW;
    final public Rq1ReferenceListField_FilterByClass ALL_SUBMITTED_ISSUE_FD;
    final public Rq1ReferenceListField_FilterByClass ALL_SUBMITTED_ISSUE_HW_ECU;
    final public Rq1ReferenceListField_FilterByClass ALL_SUBMITTED_ISSUE_HW_MOD;

    final private Rq1QueryField OPEN_REQUESTED_ISSUE;
    final public Rq1ReferenceListField_FilterByClass OPEN_REQUESTED_ISSUE_SW;
    final public Rq1ReferenceListField_FilterByClass OPEN_REQUESTED_ISSUE_FD;
    final public Rq1ReferenceListField_FilterByClass OPEN_REQUESTED_ISSUE_HW_ECU;
    final public Rq1ReferenceListField_FilterByClass OPEN_REQUESTED_ISSUE_HW_MOD;

    final private Rq1QueryField OPEN_REQUESTED_RELEASES;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_MILESTONE;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_PST_COLLECTIONS;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_PST_RELEASE;
    final public Rq1ReferenceListField_FilterByClass OPEN_SUBMITTED_BC;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_BC;
    final public Rq1ReferenceListField_FilterByClass OPEN_REQUESTED_BC;
    final public Rq1ReferenceListField_FilterByClass OPEN_SUBMITTED_FC;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_FC;
    final public Rq1ReferenceListField_FilterByClass OPEN_REQUESTED_FC;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_RELEASE_HW_ECU;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_RELEASE_HW_MOD;
    final public Rq1ReferenceListField_FilterByClass OPEN_ASSIGNED_RELEASE_HW_COMP;

    final private Rq1QueryField ALL_ASSIGNED_RELEASES;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_MILESTONE;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_PST_COLLECTIONS;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_PST_RELEASES;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_BC;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_FC;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_RELEASE_HW_ECU;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_RELEASE_HW_MOD;
    final public Rq1ReferenceListField_FilterByClass ALL_ASSIGNED_RELEASE_HW_COMP;

    final public Rq1QueryField OPEN_SUBMITTED_WORKITEM;

    final public Rq1QueryField OPEN_ASSIGNED_WORKITEM;
    final public Rq1QueryField ALL_ASSIGNED_WORKITEM;

    final public Rq1QueryField OPEN_REQUESTED_WORKITEM;
    final public Rq1QueryField ALL_REQUESTED_WORKITEM;

    final public Rq1QueryField PROJECTS;

    final public Rq1QueryField OPEN_ASSIGNED_IRM;
    final public Rq1QueryField ALL_ASSIGNED_IRM;

    final public Rq1QueryField OPEN_ASSIGNED_RRM;
    final public Rq1QueryField ALL_ASSIGNED_RRM;

    final public Rq1QueryField OPEN_ASSIGNED_PROBLEM;
    final public Rq1QueryField ALL_ASSIGNED_PROBLEM;
    final public Rq1QueryField OPEN_CONTACT_PROBLEM;
    final public Rq1QueryField ALL_CONTACT_PROBLEM;
    final public Rq1QueryField OPEN_SUBMITTED_PROBLEM;
    final public Rq1QueryField ALL_SUBMITTED_PROBLEM;

    public Rq1User() {
        super(Rq1NodeDescription.USER);

        addField(EMAIL = new Rq1DatabaseField_Text(this, "email"));
        addField(FULLNAME = new Rq1DatabaseField_Text(this, "fullname"));
        addField(LOGIN_NAME = new Rq1DatabaseField_Text(this, "login_name"));
        addField(IS_ACTIVE = new Rq1DatabaseField_Enumeration(this, FIELDNAME_IS_ACTIVE, Rq1UserIsActive.values(), Rq1UserIsActive.EMPTY));
        addField(MISC_INFO = new Rq1DatabaseField_Text(this, FIELDNAME_MISC_INFO));
        addField(PHONE = new Rq1DatabaseField_Text(this, "phone"));

        EMAIL.setReadOnly();
        FULLNAME.setReadOnly();
        LOGIN_NAME.setReadOnly();
        IS_ACTIVE.setReadOnly();
        MISC_INFO.setReadOnly();
        PHONE.setReadOnly();

        //
        // Open assigned Issues
        //
        addField(OPEN_ASSIGNED_ISSUE = new Rq1QueryField_linkedToUser(this, "OpenAssignedIssues", Rq1NodeDescription.ISSUE_SW.getRecordType(), Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        OPEN_ASSIGNED_ISSUE.addCriteria_ValueList("LifeCycleState", LifeCycleState_Issue.getAllOpenState());
        addField(OPEN_ASSIGNED_ISSUE_SW = new Rq1ReferenceListField_FilterByClass(this, OPEN_ASSIGNED_ISSUE, Rq1IssueSW.class));
        addField(OPEN_ASSIGNED_ISSUE_FD = new Rq1ReferenceListField_FilterByClass(this, OPEN_ASSIGNED_ISSUE, Rq1IssueFD.class));
        addField(OPEN_ASSIGNED_ISSUE_HW_ECU = new Rq1ReferenceListField_FilterByClass(this, OPEN_ASSIGNED_ISSUE, Rq1IssueHwEcu.class));
        addField(OPEN_ASSIGNED_ISSUE_HW_MOD = new Rq1ReferenceListField_FilterByClass(this, OPEN_ASSIGNED_ISSUE, Rq1IssueMod.class));

        //
        // Open requested Issues
        //
        addField(OPEN_REQUESTED_ISSUE = new Rq1QueryField_linkedToUser(this, "OpenRequestedIssues", Rq1NodeDescription.ISSUE_SW.getRecordType(), Rq1QueryField_linkedToUser.LinkType.REQUESTED));
        OPEN_REQUESTED_ISSUE.addCriteria_ValueList("LifeCycleState", LifeCycleState_Issue.getAllOpenState());
        addField(OPEN_REQUESTED_ISSUE_SW = new Rq1ReferenceListField_FilterByClass(this, OPEN_REQUESTED_ISSUE, Rq1IssueSW.class));
        addField(OPEN_REQUESTED_ISSUE_FD = new Rq1ReferenceListField_FilterByClass(this, OPEN_REQUESTED_ISSUE, Rq1IssueFD.class));
        addField(OPEN_REQUESTED_ISSUE_HW_ECU = new Rq1ReferenceListField_FilterByClass(this, OPEN_REQUESTED_ISSUE, Rq1IssueHwEcu.class));
        addField(OPEN_REQUESTED_ISSUE_HW_MOD = new Rq1ReferenceListField_FilterByClass(this, OPEN_REQUESTED_ISSUE, Rq1IssueMod.class));

        //
        // All submitted Issues
        //
        addField(ALL_SUBMITTED_ISSUE = new Rq1QueryField_linkedToUser(this, "AllSubmittedIssues", Rq1NodeDescription.ISSUE_SW.getRecordType(), Rq1QueryField_linkedToUser.LinkType.SUBMITTED));
        addField(ALL_SUBMITTED_ISSUE_SW = new Rq1ReferenceListField_FilterByClass(this, ALL_SUBMITTED_ISSUE, Rq1IssueSW.class));
        addField(ALL_SUBMITTED_ISSUE_FD = new Rq1ReferenceListField_FilterByClass(this, ALL_SUBMITTED_ISSUE, Rq1IssueFD.class));
        addField(ALL_SUBMITTED_ISSUE_HW_ECU = new Rq1ReferenceListField_FilterByClass(this, ALL_SUBMITTED_ISSUE, Rq1IssueHwEcu.class));
        addField(ALL_SUBMITTED_ISSUE_HW_MOD = new Rq1ReferenceListField_FilterByClass(this, ALL_SUBMITTED_ISSUE, Rq1IssueMod.class));

        //
        // Open submitted Issues
        //
        addField(OPEN_SUBMITTED_ISSUE = new Rq1QueryField_linkedToUser(this, "OpenSubmittedIssues", Rq1NodeDescription.ISSUE_SW.getRecordType(), Rq1QueryField_linkedToUser.LinkType.SUBMITTED));
        OPEN_SUBMITTED_ISSUE.addCriteria_ValueList("LifeCycleState", LifeCycleState_Issue.getAllOpenState());
        addField(OPEN_SUBMITTED_ISSUE_SW = new Rq1ReferenceListField_FilterByClass(this, OPEN_SUBMITTED_ISSUE, Rq1IssueSW.class));
        addField(OPEN_SUBMITTED_ISSUE_FD = new Rq1ReferenceListField_FilterByClass(this, OPEN_SUBMITTED_ISSUE, Rq1IssueFD.class));
        addField(OPEN_SUBMITTED_ISSUE_HW_ECU = new Rq1ReferenceListField_FilterByClass(this, OPEN_SUBMITTED_ISSUE, Rq1IssueHwEcu.class));
        addField(OPEN_SUBMITTED_ISSUE_HW_MOD = new Rq1ReferenceListField_FilterByClass(this, OPEN_SUBMITTED_ISSUE, Rq1IssueMod.class));

        //
        // All assigned Issues
        //
        addField(ALL_ASSIGNED_ISSUE = new Rq1QueryField_linkedToUser(this, "AllIssues", Rq1NodeDescription.ISSUE_SW.getRecordType(), Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        addField(ALL_ASSIGNED_ISSUE_SW = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_ISSUE, Rq1IssueSW.class));
        addField(ALL_ASSIGNED_ISSUE_FD = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_ISSUE, Rq1IssueFD.class));
        addField(ALL_ASSIGNED_ISSUE_HW_ECU = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_ISSUE, Rq1IssueHwEcu.class));
        addField(ALL_ASSIGNED_ISSUE_HW_MOD = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_ISSUE, Rq1IssueMod.class));

        //
        // Open submitted Releases
        //
        Rq1QueryField_linkedToUser openSubmittedReleases;
        addField(openSubmittedReleases = new Rq1QueryField_linkedToUser(this, "OpenSubmittedReleases", Rq1RecordType.RELEASE, Rq1QueryField_linkedToUser.LinkType.SUBMITTED));
        openSubmittedReleases.addCriteria_ValueList("LifeCycleState", LifeCycleState_Release.getAllOpenState());
        addField(OPEN_SUBMITTED_BC = new Rq1ReferenceListField_FilterByClass(this, openSubmittedReleases, Rq1Bc.class));
        addField(OPEN_SUBMITTED_FC = new Rq1ReferenceListField_FilterByClass(this, openSubmittedReleases, Rq1Fc.class));

        //
        // Open assigned Releases
        //
        Rq1QueryField_linkedToUser openAssignedReleases;
        addField(openAssignedReleases = new Rq1QueryField_linkedToUser(this, "OpenAssignedReleases", Rq1RecordType.RELEASE, Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        openAssignedReleases.addCriteria_ValueList("LifeCycleState", LifeCycleState_Release.getAllOpenState());
        addField(OPEN_ASSIGNED_MILESTONE = new Rq1ReferenceListField_FilterByClass(this, openAssignedReleases, Rq1Milestone.class));
        addField(OPEN_ASSIGNED_PST_COLLECTIONS = new Rq1ReferenceListField_FilterByClass(this, openAssignedReleases, Rq1PstCollectionI.class));
        addField(OPEN_ASSIGNED_PST_RELEASE = new Rq1ReferenceListField_FilterByClass(this, openAssignedReleases, Rq1PstReleaseI.class));
        addField(OPEN_ASSIGNED_BC = new Rq1ReferenceListField_FilterByClass(this, openAssignedReleases, Rq1Bc.class));
        addField(OPEN_ASSIGNED_FC = new Rq1ReferenceListField_FilterByClass(this, openAssignedReleases, Rq1Fc.class));
        addField(OPEN_ASSIGNED_RELEASE_HW_ECU = new Rq1ReferenceListField_FilterByClass(this, openAssignedReleases, Rq1EcuRelease.class));
        addField(OPEN_ASSIGNED_RELEASE_HW_MOD = new Rq1ReferenceListField_FilterByClass(this, openAssignedReleases, Rq1ModRelease.class));
        addField(OPEN_ASSIGNED_RELEASE_HW_COMP = new Rq1ReferenceListField_FilterByClass(this, openAssignedReleases, Rq1CompRelease.class));

        //
        // Open requested Releases
        //
        addField(OPEN_REQUESTED_RELEASES = new Rq1QueryField_linkedToUser(this, "OpenRequestedReleases", Rq1RecordType.RELEASE, Rq1QueryField_linkedToUser.LinkType.REQUESTED));
        OPEN_REQUESTED_RELEASES.addCriteria_ValueList("LifeCycleState", LifeCycleState_Release.getAllOpenState());
        addField(OPEN_REQUESTED_BC = new Rq1ReferenceListField_FilterByClass(this, OPEN_REQUESTED_RELEASES, Rq1Bc.class));
        addField(OPEN_REQUESTED_FC = new Rq1ReferenceListField_FilterByClass(this, OPEN_REQUESTED_RELEASES, Rq1Fc.class));

        //
        // All assigned Releases
        //
        addField(ALL_ASSIGNED_RELEASES = new Rq1QueryField_linkedToUser(this, "AllReleases", Rq1RecordType.RELEASE, Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        addField(ALL_ASSIGNED_MILESTONE = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_RELEASES, Rq1Milestone.class));
        addField(ALL_ASSIGNED_PST_COLLECTIONS = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_RELEASES, Rq1PstCollectionI.class));
        addField(ALL_ASSIGNED_PST_RELEASES = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_RELEASES, Rq1PstReleaseI.class));
        addField(ALL_ASSIGNED_BC = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_RELEASES, Rq1Bc.class));
        addField(ALL_ASSIGNED_FC = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_RELEASES, Rq1Fc.class));
        addField(ALL_ASSIGNED_RELEASE_HW_ECU = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_RELEASES, Rq1EcuRelease.class));
        addField(ALL_ASSIGNED_RELEASE_HW_MOD = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_RELEASES, Rq1ModRelease.class));
        addField(ALL_ASSIGNED_RELEASE_HW_COMP = new Rq1ReferenceListField_FilterByClass(this, ALL_ASSIGNED_RELEASES, Rq1CompRelease.class));

        //
        // Open submitted Workitem
        //
        addField(OPEN_SUBMITTED_WORKITEM = new Rq1QueryField_linkedToUser(this, "OpenSubmittedWorkItem", Rq1NodeDescription.WORKITEM.getRecordType(), Rq1QueryField_linkedToUser.LinkType.SUBMITTED));
        EnumSet<LifeCycleState_WorkItem> openWorkitemLifeCycleStates = EnumSet.allOf(LifeCycleState_WorkItem.class);
        openWorkitemLifeCycleStates.remove(LifeCycleState_WorkItem.CANCELED);
        openWorkitemLifeCycleStates.remove(LifeCycleState_WorkItem.CLOSED);
        OPEN_SUBMITTED_WORKITEM.addCriteria_ValueList("LifeCycleState", openWorkitemLifeCycleStates);

        //
        // Open assigned Workitem
        //
        addField(OPEN_ASSIGNED_WORKITEM = new Rq1QueryField_linkedToUser(this, "OpenAssignedWorkItem", Rq1NodeDescription.WORKITEM.getRecordType(), Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        OPEN_ASSIGNED_WORKITEM.addCriteria_ValueList("LifeCycleState", openWorkitemLifeCycleStates);

        //
        // All assigned Workitem
        //
        addField(ALL_ASSIGNED_WORKITEM = new Rq1QueryField_linkedToUser(this, "AllAssignedWorkItem", Rq1NodeDescription.WORKITEM.getRecordType(), Rq1QueryField_linkedToUser.LinkType.ASSIGNED));

        //
        // Open requested Workitem
        //
        addField(OPEN_REQUESTED_WORKITEM = new Rq1QueryField_linkedToUser(this, "OpenRequestedWorkItem", Rq1NodeDescription.WORKITEM.getRecordType(), Rq1QueryField_linkedToUser.LinkType.REQUESTED));
        OPEN_REQUESTED_WORKITEM.addCriteria_ValueList("LifeCycleState", openWorkitemLifeCycleStates);

        //
        // All requested Workitem
        //
        addField(ALL_REQUESTED_WORKITEM = new Rq1QueryField_linkedToUser(this, "AllRequestedAssignedWorkItem", Rq1NodeDescription.WORKITEM.getRecordType(), Rq1QueryField_linkedToUser.LinkType.REQUESTED));

        //
        // All Projects
        //
        addField(PROJECTS = new Rq1QueryField(this, "Projects", Rq1NodeDescription.SOFTWARE_COSTUMER_PROJECT_LEAF.getRecordType()));
        PROJECTS.addCriteria_Reference("hasProjectMembers.LinkedUser", this);

        //
        // Assigned IRM
        //
        addField(OPEN_ASSIGNED_IRM = new Rq1QueryField_linkedToUser(this, "openAssignedIrm", Rq1RecordType.IRM, Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        OPEN_ASSIGNED_IRM.addCriteria_ValueList("LifeCycleState", LifeCycleState_IRM.getAllOpenState());
        addField(ALL_ASSIGNED_IRM = new Rq1QueryField_linkedToUser(this, "allAssignedIrm", Rq1RecordType.IRM, Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        // Set load hint for load optimization
        OslcLoadHint loadHintIrm = new OslcLoadHint(true);
        loadHintIrm.followField(Rq1Irm.ATTRIBUTE_HAS_MAPPED_ISSUE.getName(), true);
        loadHintIrm.followField(Rq1Irm.ATTRIBUTE_HAS_MAPPED_RELEASE.getName(), true);
        loadHintIrm.setDependsOnSubRecords(true);
        OPEN_ASSIGNED_IRM.setLoadHint(loadHintIrm);
        ALL_ASSIGNED_IRM.setLoadHint(loadHintIrm);

        //
        // Open assigned RRM
        //
        addField(OPEN_ASSIGNED_RRM = new Rq1QueryField_linkedToUser(this, "openAssignedRrm", Rq1RecordType.RRM, Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        OPEN_ASSIGNED_RRM.addCriteria_ValueList("LifeCycleState", LifeCycleState_RRM.getAllOpenState());
        addField(ALL_ASSIGNED_RRM = new Rq1QueryField_linkedToUser(this, "allAssignedRrm", Rq1RecordType.RRM, Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        // Set load hint for load optimization
        OslcLoadHint loadHintRrm = new OslcLoadHint(true);
        loadHintRrm.followField(Rq1Rrm.ATTRIBUTE_HAS_MAPPED_PARENT_RELEASE.getName(), true);
        loadHintRrm.followField(Rq1Rrm.ATTRIBUTE_HAS_MAPPED_CHILD_RELEASE.getName(), true);
        loadHintRrm.setDependsOnSubRecords(true);
        OPEN_ASSIGNED_RRM.setLoadHint(loadHintRrm);
        ALL_ASSIGNED_RRM.setLoadHint(loadHintRrm);

        //
        // Open assigned Problems
        //
        addField(OPEN_ASSIGNED_PROBLEM = new Rq1QueryField_linkedToUser(this, "OpenAssignedProblems", Rq1RecordType.PROBLEM, Rq1QueryField_linkedToUser.LinkType.ASSIGNED));
        OPEN_ASSIGNED_PROBLEM.addCriteria_ValueList("LifeCycleState", LifeCycleState_Problem.getAllOpenState());

        //
        // All assigned Problems
        //
        addField(ALL_ASSIGNED_PROBLEM = new Rq1QueryField_linkedToUser(this, "AllAssignedProblems", Rq1RecordType.PROBLEM, Rq1QueryField_linkedToUser.LinkType.ASSIGNED));

        //
        // Open contact Problems
        //
        addField(OPEN_CONTACT_PROBLEM = new Rq1QueryField(this, "OpenContactProblems", Rq1RecordType.PROBLEM));
        OPEN_CONTACT_PROBLEM.addCriteria_Reference(Rq1Problem.ATTRIBUTE_CONTACT_INFORMATION, this);
        OPEN_CONTACT_PROBLEM.addCriteria_ValueList("LifeCycleState", LifeCycleState_Problem.getAllOpenState());

        //
        // All contact Problems
        //
        addField(ALL_CONTACT_PROBLEM = new Rq1QueryField(this, "AllContactProblems", Rq1RecordType.PROBLEM));
        ALL_CONTACT_PROBLEM.addCriteria_Reference(Rq1Problem.ATTRIBUTE_CONTACT_INFORMATION, this);
        
        //
        // Open submitted Problem
        //
        addField(OPEN_SUBMITTED_PROBLEM = new Rq1QueryField_linkedToUser(this, "OpenSubmittedProblem", Rq1NodeDescription.PROBLEM.getRecordType(), Rq1QueryField_linkedToUser.LinkType.SUBMITTED));
        EnumSet<LifeCycleState_Problem> openProblemLifeCycleStates = EnumSet.allOf(LifeCycleState_Problem.class);
        openProblemLifeCycleStates.remove(LifeCycleState_Problem.CANCELED);
        openProblemLifeCycleStates.remove(LifeCycleState_Problem.CLOSED);
        OPEN_SUBMITTED_PROBLEM.addCriteria_ValueList("LifeCycleState", openProblemLifeCycleStates);
        
        //
        // All submitted Problem
        //
        addField(ALL_SUBMITTED_PROBLEM = new Rq1QueryField_linkedToUser(this, "AllSubmittedProblem", Rq1NodeDescription.PROBLEM.getRecordType(), Rq1QueryField_linkedToUser.LinkType.SUBMITTED));

    }

    @Override
    public void reload() {
        // ignored
    }

    static public Iterable<Rq1User> getAllActiveUser() {
        //
        // Prepare query
        //
        Rq1Query query = new Rq1Query(Rq1NodeDescription.USER.getRecordType());
        query.addCriteria_Value(FIELDNAME_IS_ACTIVE, Rq1UserIsActive.YES.getText());

        //
        // Handle result
        //
        List<Rq1User> result = new ArrayList<>();
        for (Rq1Reference r : query.getReferenceList()) {
            if (r.getRecord() instanceof Rq1User) {
                result.add((Rq1User) r.getRecord());
            } else {
                java.util.logging.Logger.getLogger(Rq1User.class.getCanonicalName()).severe("Unexpected record type: " + r.getRecord().getClass());
            }
        }

        return (result);
    }

}
