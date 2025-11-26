/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
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
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1IssueMod;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Templates.Rq1Template;
import UiSupport.UiBookmarkType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public class DmRq1IssueMod extends DmRq1HardwareIssue {

    final public DmRq1Field_Enumeration APPROVAL;
    final public DmRq1Field_Reference<DmRq1Issue> PARENT;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm_ModRelease_IssueHw, DmRq1ModRelease> MAPPED_RELEASES;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1IssueMod(Rq1IssueMod issue) {
        super("I-HW-MOD", issue);

        addField(APPROVAL = new DmRq1Field_Enumeration(this, issue.APPROVAL, "Approval"));
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        addField(PARENT = new DmRq1Field_Reference<>(this, issue.HAS_PARENT, "Parent"));
        addField(MAPPED_RELEASES = new DmRq1Field_MappedReferenceList<>(this, issue.HAS_MAPPED_RELEASES, "HW-MOD-Releases"));
    }

    public static DmRq1IssueMod createBasedOnModRelease(DmRq1ModRelease release, DmRq1HardwareProject targetProject, Rq1Template template) {

        assert (targetProject != null);
        assert (release != null);

        DmRq1IssueMod issueHwMod = DmRq1ElementCache.createIssueMod();
        DmRq1Irm_ModRelease_IssueHw irm;

        try {
            irm = DmRq1Irm_ModRelease_IssueHw.create(release, issueHwMod);
        } catch (DmRq1MapExistsException ex) {
            Logger.getLogger(DmRq1IssueEcu.class.getName()).log(Level.SEVERE, null, ex);
            return (null);
        }

        //
        // Take over content from release
        //
        issueHwMod.ACCOUNT_NUMBERS.setValue(targetProject.ACCOUNT_NUMBERS.getValue());
        irm.ACCOUNT_NUMBERS.setValue(release.ACCOUNT_NUMBERS.getValue());
        irm.IS_PILOT.setValue(YesNoEmpty.YES);

        //
        // Connect Issue - Project
        //
        issueHwMod.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issueHwMod.rq1Record);
        }
        return (issueHwMod);
    }

    static public DmRq1IssueMod createBasedOnParent(DmRq1IssueEcu issueHwEcu, DmRq1HardwareProject project, Rq1Template template) {

        assert (issueHwEcu != null);
        assert (project != null);

        DmRq1IssueMod issueHwMod = DmRq1ElementCache.createIssueMod();

        //
        // Take over content from parent
        //
        issueHwMod.ACCOUNT_NUMBERS.setValue(issueHwEcu.ACCOUNT_NUMBERS.getValue());
        issueHwMod.DESCRIPTION.setValue(issueHwEcu.DESCRIPTION.getValue());
        issueHwMod.INTERNAL_COMMENT.setValue(issueHwEcu.INTERNAL_COMMENT.getValue());
        issueHwMod.SCOPE.setValue(Scope.INTERNAL);
        issueHwMod.TITLE.setValue(issueHwEcu.TITLE.getValue());
        issueHwMod.ASIL_CLASSIFICATION.setValue(issueHwEcu.ASIL_CLASSIFICATION.getValue());
        issueHwMod.FMEA_STATE.setValue(issueHwEcu.FMEA_STATE.getValue());
        issueHwMod.REQUIREMENTS_REVIEW.setValue(issueHwEcu.REQUIREMENTS_REVIEW.getValue());
        issueHwMod.DRBFM.setValue(issueHwEcu.DRBFM.getValue());
        issueHwMod.SPECIFICATION_REVIEW.setValue(issueHwEcu.SPECIFICATION_REVIEW.getValue());
        issueHwMod.DEVELOPMENT_METHOD.setValue(issueHwEcu.DEVELOPMENT_METHOD.getValue());

        issueHwMod.DEFECT_DETECTION_ORGANISATION.setValue(issueHwEcu.DEFECT_DETECTION_ORGANISATION.getValue());

        //
        // Connect parent-child
        //
        issueHwEcu.CHILDREN.addElement(issueHwMod);
        issueHwMod.PARENT.setElement(issueHwEcu);

        //
        // Connect project-issue
        //
        issueHwMod.PROJECT.setElement(project);
        //
        // Do not add the Issue to the project.
        // Reason: Before adding the issue, the whole list of issues is read from RQ1-DB which e.g. for the pool project are about 14.000 records. :-(
        // So better not to add it now. The connection in RQ1 is anyway done when the issue is stored in RQ1.
        //
//        project.ISSUES.addElement(issueFD);
        if (template != null) {
            template.execute(issueHwMod.rq1Record);
        }
        return (issueHwMod);
    }

    @Override
    public UiBookmarkType getBookmarkType() {
        return UiBookmarkType.ISSUEMOD;
    }
}
