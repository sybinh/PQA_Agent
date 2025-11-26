/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1IssueEcu;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Templates.Rq1Template;
import UiSupport.UiBookmarkType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public class DmRq1IssueEcu extends DmRq1HardwareIssue {

    final public DmRq1Field_Enumeration APPROVAL;
    final public DmRq1Field_ReferenceList<DmRq1IssueMod> CHILDREN;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm_EcuRelease_IssueHw, DmRq1EcuRelease> MAPPED_RELEASES;
    final public DmRq1Field_Reference<DmRq1IssueEcu> AFFECTED_ISSUE;
    final public DmRq1Field_ReferenceList<DmRq1IssueEcu> IS_AFFECTED_BY_DEFECT_ISSUE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1IssueEcu(Rq1IssueEcu rq1IssueHwEcu) {
        super("I-HW-ECU", rq1IssueHwEcu);

        addField(APPROVAL = new DmRq1Field_Enumeration(this, rq1IssueHwEcu.APPROVAL, "Approval"));
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);

        addField(CHILDREN = new DmRq1Field_ReferenceList<>(this, rq1IssueHwEcu.HAS_CHILDREN, "Children"));
        addField(AFFECTED_ISSUE = new DmRq1Field_Reference<>(this, rq1IssueHwEcu.AFFECTED_ISSUE, "Affected Issue"));
        addField(IS_AFFECTED_BY_DEFECT_ISSUE = new DmRq1Field_ReferenceList<>(this, rq1IssueHwEcu.IS_AFFECTED_BY_DEFECT_ISSUE, "Is Affected By Issues"));
        addField(MAPPED_RELEASES = new DmRq1Field_MappedReferenceList<>(this, rq1IssueHwEcu.HAS_MAPPED_RELEASES, "HW-ECU-Releases"));

    }

    /**
     * Creates a I-HW-ECU based on an ECU-RELEASE-HW
     *
     * @param release
     * @param targetProject
     * @param template
     * @return I-HW-ECU
     */
    public static DmRq1IssueEcu createBasedOnEcuRelease(DmRq1EcuRelease release, DmRq1HardwareProject targetProject, Rq1Template template) {

        assert (targetProject != null);
        assert (release != null);

        DmRq1IssueEcu issueHwEcu = DmRq1ElementCache.createIssueEcu();
        DmRq1Irm_EcuRelease_IssueHw irm;

        try {
            irm = DmRq1Irm_EcuRelease_IssueHw.create(release, issueHwEcu);
        } catch (DmRq1MapExistsException ex) {
            Logger.getLogger(DmRq1IssueEcu.class.getName()).log(Level.SEVERE, null, ex);
            return (null);
        }

        //
        // Take over content from release
        //
        issueHwEcu.ACCOUNT_NUMBERS.setValue(targetProject.ACCOUNT_NUMBERS.getValue());
        irm.ACCOUNT_NUMBERS.setValue(release.ACCOUNT_NUMBERS.getValue());
        irm.IS_PILOT.setValue(YesNoEmpty.YES);

        //
        // Connect Issue - Project
        //
        issueHwEcu.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issueHwEcu.rq1Record);
        }
        return (issueHwEcu);
    }

    /**
     * Creates a I-HW-ECU based on a hardware project
     *
     * @param targetProject
     * @param template
     * @return I-HW-ECU
     */
    public static DmRq1IssueEcu createBasedOnHardwareProject(DmRq1HardwareProject targetProject, Rq1Template template) {

        assert (targetProject != null);

        DmRq1IssueEcu issue = DmRq1ElementCache.createIssueEcu();

        //
        // Take over content from project
        //
        issue.ACCOUNT_NUMBERS.setValue(targetProject.ACCOUNT_NUMBERS.getValue());

        //
        // Connect Issue - Project
        //
        issue.PROJECT.setElement(targetProject);
        targetProject.OPEN_ISSUE_ECU.addElement(issue);
        if (template != null) {
            template.execute(issue.rq1Record);
        }
        return (issue);
    }

    @Override
    public UiBookmarkType getBookmarkType() {
        return UiBookmarkType.ISSUEECU;
    }

}
