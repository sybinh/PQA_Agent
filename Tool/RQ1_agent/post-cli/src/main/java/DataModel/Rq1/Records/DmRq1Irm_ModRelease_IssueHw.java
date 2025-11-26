/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmMappedElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1Irm_ModRelease_IssueHw;

/**
 *
 * @author ths83wi
 */
public class DmRq1Irm_ModRelease_IssueHw extends DmRq1HardwareIrm {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Irm_ModRelease_IssueHw(Rq1Irm_ModRelease_IssueHw irm) {
        super("IRM-HW_MOD-I_HW_MOD", irm);
    }

    /**
     * Creates an IRM between an I-MOD-HW and an HW-MOD-RELEASE
     *
     * @param hwModRelease
     * @param issueHwMod
     * @return the IRM between I-ECU-HW and HW-ECU-RELEASE
     * @throws DmRq1MapExistsException
     */
    public static DmRq1Irm_ModRelease_IssueHw create(DmRq1ModRelease hwModRelease, DmRq1IssueHwMod issueHwMod) throws DmRq1MapExistsException {

        assert (hwModRelease != null);
        assert (issueHwMod != null);

        //
        // Ensure that IRM does not yet exist
        //
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : hwModRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == issueHwMod) {
                throw (new DmRq1MapExistsException());
            }
        }
        DmRq1Irm_ModRelease_IssueHw irm = DmRq1ElementCache.createIrm_ModRelease_IssueHw();

        //
        // Take over content from HW-ECU-RELEASE
        //
        irm.ACCOUNT_NUMBERS.setValue(hwModRelease.ACCOUNT_NUMBERS.getValue());

        //
        // Add mapping on map
        //
        irm.HAS_MAPPED_RELEASE.setElement(hwModRelease);
        irm.HAS_MAPPED_ISSUE.setElement(issueHwMod);

        //
        // Add mapping on release
        //
        hwModRelease.MAPPED_ISSUES.addElement(irm, issueHwMod);

        //
        // Add mapping on issue
        //
        issueHwMod.HAS_MAPPED_RELEASES.addElement(irm, hwModRelease);

        return (irm);
    }

    public static DmRq1Irm_ModRelease_IssueHw create(DmRq1ModRelease modRelease, DmRq1Irm_ModRelease_IssueHw existingMap, DmRq1IssueHwMod issueHwMod) throws DmRq1MapExistsException {
        assert (modRelease != null);
        assert (existingMap != null);
        assert (issueHwMod != null);

        //
        // Create new IRM
        //
        DmRq1Irm_ModRelease_IssueHw newMap = create(modRelease, issueHwMod);

        //
        // Take over values from IRM
        //
        return (newMap);
    }

    public static DmRq1Irm_ModRelease_IssueHw moveToMod(DmRq1Irm_ModRelease_IssueHw map, DmRq1ModRelease newModRelease) throws DmRq1MapExistsException {
        assert (map != null);
        assert (newModRelease != null);

        //
        // Ensure that IRM does not yet exist
        //
        DmRq1IssueHwMod issueHwMod = (DmRq1IssueHwMod) map.HAS_MAPPED_ISSUE.getElement();
        assert (issueHwMod != null);
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : newModRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == issueHwMod) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1ModRelease oldModRelease = (DmRq1ModRelease) map.HAS_MAPPED_RELEASE.getElement();

        //
        // Change mapping on BC
        //
        oldModRelease.MAPPED_ISSUES.removeElement(issueHwMod);
        newModRelease.MAPPED_ISSUES.addElement(map, issueHwMod);

        //
        // Change mapping on issue
        //
        issueHwMod.MAPPED_RELEASES.removeElement(oldModRelease);
        issueHwMod.MAPPED_RELEASES.addElement(map, newModRelease);

        //
        // Change mapping on IRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        map.HAS_MAPPED_RELEASE.setElement(newModRelease);

        return (map);
    }

    public static DmRq1Irm_ModRelease_IssueHw moveToIssueHwMod(DmRq1Irm_ModRelease_IssueHw map, DmRq1IssueHwMod newIssueHwMod) throws DmRq1MapExistsException {
        assert (map != null);
        assert (newIssueHwMod != null);

        //
        // Ensure that IRM does not yet exist
        //
        DmRq1ModRelease modRelease = (DmRq1ModRelease) map.HAS_MAPPED_RELEASE.getElement();
        assert (modRelease != null);
        for (DmMappedElement<DmRq1Irm_ModRelease_IssueHw, DmRq1ModRelease> m : newIssueHwMod.MAPPED_RELEASES.getElementList()) {
            if (m.getTarget() == modRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1IssueHwMod oldIssueHwMod = (DmRq1IssueHwMod) map.HAS_MAPPED_ISSUE.getElement();

        //
        // Change mapping issues
        //
        oldIssueHwMod.MAPPED_RELEASES.removeElement(modRelease);
        newIssueHwMod.MAPPED_RELEASES.addElement(map, modRelease);

        //
        // Change mapping on release
        //
        modRelease.MAPPED_ISSUES.removeElement(oldIssueHwMod);
        modRelease.MAPPED_ISSUES.addElement(map, newIssueHwMod);

        //
        // Change mapping on IRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        map.HAS_MAPPED_ISSUE.setElement(newIssueHwMod);

        return (map);
    }
}
