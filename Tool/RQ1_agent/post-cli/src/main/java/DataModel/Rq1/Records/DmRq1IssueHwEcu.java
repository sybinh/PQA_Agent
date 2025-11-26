/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
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
import static DataModel.Rq1.Records.DmRq1ElementCache.addElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1IssueHwEcu;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Templates.Rq1TemplateI;
import ToolUsageLogger.ToolUsageLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public class DmRq1IssueHwEcu extends DmRq1HardwareIssue {

    static public class ExistsAlreadyException extends Exception {

    }

    public static class ResultMap {

        final private DmRq1Irm_EcuRelease_IssueHw irm;
        final private DmRq1IssueHwEcu issue;

        public ResultMap(DmRq1Irm_EcuRelease_IssueHw irm, DmRq1IssueHwEcu issue) {
            assert (irm != null);
            assert (issue != null);
            this.irm = irm;
            this.issue = issue;
        }

        public DmRq1Irm_EcuRelease_IssueHw getIrm() {
            return irm;
        }

        public DmRq1IssueHwEcu getIssue() {
            return issue;
        }

    }

    final public DmRq1Field_Enumeration APPROVAL;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwMod> CHILDREN;
    final public DmRq1Field_MappedReferenceList<DmRq1Irm_EcuRelease_IssueHw, DmRq1EcuRelease> MAPPED_RELEASES;
    final public DmRq1Field_Reference<DmRq1IssueHwEcu> AFFECTED_ISSUE;
    final public DmRq1Field_ReferenceList<DmRq1IssueHwEcu> IS_AFFECTED_BY_DEFECT_ISSUE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1IssueHwEcu(Rq1IssueHwEcu rq1IssueHwEcu) {
        super("I-HW-ECU", rq1IssueHwEcu);

        addField(APPROVAL = new DmRq1Field_Enumeration(this, rq1IssueHwEcu.APPROVAL, "Approval"));
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_USER_DEFINED_TAB);
        APPROVAL.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);

        addField(CHILDREN = new DmRq1Field_ReferenceList<>(this, rq1IssueHwEcu.HAS_CHILDREN, "Children"));
        addField(AFFECTED_ISSUE = new DmRq1Field_Reference<>(this, rq1IssueHwEcu.AFFECTED_ISSUE, "Affected Issue"));
        addField(IS_AFFECTED_BY_DEFECT_ISSUE = new DmRq1Field_ReferenceList<>(this, rq1IssueHwEcu.IS_AFFECTED_BY_DEFECT_ISSUE, "Is Affected By Issues"));
        addField(MAPPED_RELEASES = new DmRq1Field_MappedReferenceList<>(this, rq1IssueHwEcu.HAS_MAPPED_RELEASES, "HW-ECU"));

    }

    public void addIssueHwMod(DmRq1IssueHwMod newIssueHwMod) throws ExistsAlreadyException {
        for (DmRq1IssueHwMod child : this.CHILDREN.getElementList()) {
            if (child == newIssueHwMod) {
                throw (new ExistsAlreadyException());
            }
        }
        DmRq1Issue oldParent = newIssueHwMod.PARENT.getElement();
        if (oldParent instanceof DmRq1IssueHwEcu) {
            ((DmRq1IssueHwEcu) oldParent).CHILDREN.removeElement(newIssueHwMod);
        }
        this.CHILDREN.addElement(newIssueHwMod);
        newIssueHwMod.PARENT.setElement(this);
    }

    public static DmRq1IssueHwEcu create() {
        Rq1IssueHwEcu rq1Issue = new Rq1IssueHwEcu();
        DmRq1IssueHwEcu dmIssue = new DmRq1IssueHwEcu(rq1Issue);
        addElement(rq1Issue, dmIssue);
        return (dmIssue);
    }

    /**
     * Creates a I-HW-ECU and IMR on an ECU-RELEASE-HW
     */
    public static ResultMap createBasedOnEcuRelease(DmRq1EcuRelease release, DmRq1Project targetProject, Rq1TemplateI template) {
        assert (release != null);
        assert (targetProject != null);

        DmRq1IssueHwEcu issue = create();
        DmRq1Irm_EcuRelease_IssueHw irm;

        try {
            irm = DmRq1Irm_EcuRelease_IssueHw.create(release, issue);
        } catch (DmRq1MapExistsException ex) {
            Logger.getLogger(DmRq1IssueHwEcu.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmRq1IssueHwEcu.class.getName(), ex);
            return (null);
        }

        //
        // Take over content from release
        //
        issue.ACCOUNT_NUMBERS.setValue(targetProject.ACCOUNT_NUMBERS.getValue());
        irm.ACCOUNT_NUMBERS.setValue(release.ACCOUNT_NUMBERS.getValue());
        irm.IS_PILOT.setValue(YesNoEmpty.YES);

        //
        // Connect Issue - Project
        //
        issue.PROJECT.setElement(targetProject);
        if (template != null) {
            template.execute(issue);
        }

        return (new ResultMap(irm, issue));
    }

    /**
     * Creates a I-HW-ECU based on a project
     *
     * @param targetProject
     * @param template
     * @return I-HW-ECU
     */
    public static DmRq1IssueHwEcu createBasedOnProject(DmRq1Project targetProject, Rq1TemplateI template) {

        assert (targetProject != null);

        DmRq1IssueHwEcu issue = create();

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
            template.execute(issue);
        }
        return (issue);
    }

}
