/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_MappingToDerivatives_String;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Records.Rq1Irm_Fc_IssueFd;
import DataModel.DmMappedElement;
import Ipe.Annotations.IpeFactoryConstructor;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Irm_Fc_IssueFd extends DmRq1Irm {

    final public DmRq1Field_Text QUALIFICATION_STATUS_CHANGE_COMMENT;
    final public DmRq1Field_Text FCF_COMMENT;

    final public DmRq1Field_MappingToDerivatives_String MAPPING_TO_DERIVATIVES;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Irm_Fc_IssueFd(Rq1Irm_Fc_IssueFd rq1Irm_Fc_IssueFd) {
        super("IRM-FC-ISSUE_FD", rq1Irm_Fc_IssueFd);

        addField(QUALIFICATION_STATUS_CHANGE_COMMENT = new DmRq1Field_Text(this, rq1Irm_Fc_IssueFd.QUALIFICATION_STATUS_CHANGE_COMMENT, "Qualification Status Change Comment"));
        addField(FCF_COMMENT = new DmRq1Field_Text(this, rq1Irm_Fc_IssueFd.FCF_COMMENT, "FCF Comment"));

        addField(MAPPING_TO_DERIVATIVES = new DmRq1Field_MappingToDerivatives_String(this, rq1Irm_Fc_IssueFd.MAPPING_TO_DERIVATIVES, "Mapping To Derivatives"));

        //
        // Create and add fields
        //
        addField(new DmRq1Field_Reference<>(this, rq1Irm_Fc_IssueFd.HAS_MAPPED_ISSUE, "Issue FD"));
        addField(new DmRq1Field_Reference<>(this, rq1Irm_Fc_IssueFd.HAS_MAPPED_RELEASE, "FC Release"));
    }

    public static DmRq1Irm_Fc_IssueFd create(DmRq1Fc fcRelease, DmRq1IssueFD dmIssueFD) throws DmRq1MapExistsException {
        assert (fcRelease != null);
        assert (dmIssueFD != null);

        //
        // Ensure that IRM does not yet exist (if possible, use a cached list).
        //
        if (dmIssueFD.MAPPED_FC.isLoaded() == true) {
            for (DmMappedElement<DmRq1Irm_Fc_IssueFd, DmRq1Fc> m : dmIssueFD.MAPPED_FC.getElementList()) {
                if (m.getTarget() == fcRelease) {
                    throw (new DmRq1MapExistsException());
                }
            }
        } else {
            for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : fcRelease.MAPPED_ISSUES.getElementList()) {
                if (m.getTarget() == dmIssueFD) {
                    throw (new DmRq1MapExistsException());
                }
            }
        }

        DmRq1Irm_Fc_IssueFd irm = DmRq1ElementCache.createIrm_Fc_IssueFd();

        //
        // Take over content from BC
        //
        irm.ACCOUNT_NUMBERS.setValue(fcRelease.ACCOUNT_NUMBERS.getValue());

        //
        // Connect PVER - Map - Issue
        //
        irm.HAS_MAPPED_RELEASE.setElement(fcRelease);
        irm.HAS_MAPPED_ISSUE.setElement(dmIssueFD);
        fcRelease.MAPPED_ISSUES.addElement(irm, dmIssueFD);
        dmIssueFD.MAPPED_FC.addElement(irm, fcRelease);

        return (irm);
    }

    public static DmRq1Irm_Fc_IssueFd create(DmRq1Fc fcRelease, DmRq1Irm_Fc_IssueFd dmIrm_Fc_IssueFd, DmRq1IssueFD dmIssueFD) throws DmRq1MapExistsException {
        assert (fcRelease != null);
        assert (dmIrm_Fc_IssueFd != null);
        assert (dmIssueFD != null);

        //
        // Create new RRM
        //
        DmRq1Irm_Fc_IssueFd newIrm = create(fcRelease, dmIssueFD);

        //
        // Take over values from RRM
        //
        return (newIrm);
    }

    public static DmRq1Irm_Fc_IssueFd moveFromFcToFc(DmRq1Fc newFcRelease, DmRq1Irm_Fc_IssueFd dropedIrm, DmRq1IssueFD issueFd) throws DmRq1MapExistsException {
        assert (newFcRelease != null);
        assert (dropedIrm != null);
        assert (issueFd != null);

        //
        // Ensure that IRM does not yet exist
        //
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : newFcRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == issueFd) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1Release oldFcRelease = dropedIrm.HAS_MAPPED_RELEASE.getElement();

        //
        // Change mapping on FC
        //
        oldFcRelease.MAPPED_ISSUES.removeElement(issueFd);
        newFcRelease.MAPPED_ISSUES.addElement(dropedIrm, issueFd);

        //
        // Change mapping on I-FD
        //
        issueFd.MAPPED_FC.removeElement((DmRq1Fc) oldFcRelease);
        issueFd.MAPPED_FC.addElement(dropedIrm, newFcRelease);

        //
        // Change mapping on IRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        dropedIrm.HAS_MAPPED_RELEASE.setElement(newFcRelease);

        return (dropedIrm);
    }

    public static DmRq1Irm_Fc_IssueFd moveFromIssueToIssue(DmRq1Fc fcRelease, DmRq1Irm_Fc_IssueFd dropedIrm, DmRq1IssueFD newIssue) throws DmRq1MapExistsException {
        assert (fcRelease != null);
        assert (dropedIrm != null);
        assert (newIssue != null);

        //
        // Ensure that IRM does not yet exist
        //
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : fcRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == newIssue) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1IssueFD oldIssue = (DmRq1IssueFD) dropedIrm.HAS_MAPPED_ISSUE.getElement();

        //
        // Change mapping on FC
        //
        fcRelease.MAPPED_ISSUES.removeElement(oldIssue);
        fcRelease.MAPPED_ISSUES.addElement(dropedIrm, newIssue);

        //
        // Change mapping on I-FD
        //
        oldIssue.MAPPED_FC.removeElement(fcRelease);
        newIssue.MAPPED_FC.addElement(dropedIrm, fcRelease);

        //
        // Change mapping on IRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        dropedIrm.HAS_MAPPED_ISSUE.setElement(newIssue);

        return (dropedIrm);
    }
}
