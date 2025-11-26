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
import Rq1Cache.Records.Rq1Irm_EcuRelease_IssueHw;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Irm_EcuRelease_IssueHw extends DmRq1HardwareIrm {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Irm_EcuRelease_IssueHw(Rq1Irm_EcuRelease_IssueHw irm) {
        super("IRM-HW_ECU-I_HW_ECU", irm);

    }

    static private DmRq1Irm_EcuRelease_IssueHw create() {
        Rq1Irm_EcuRelease_IssueHw rq1Record = new Rq1Irm_EcuRelease_IssueHw();
        DmRq1Irm_EcuRelease_IssueHw dmElement = new DmRq1Irm_EcuRelease_IssueHw(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmElement);
        return (dmElement);
    }

    /**
     * Creates an IRM between an I-ECU-HW and an HW-ECU-RELEASE
     *
     * @param hwEcuRelease
     * @param issueHwEcu
     * @return the IRM between I-ECU-HW and HW-ECU-RELEASE
     * @throws DmRq1MapExistsException
     */
    public static DmRq1Irm_EcuRelease_IssueHw create(DmRq1EcuRelease hwEcuRelease, DmRq1IssueHwEcu issueHwEcu) throws DmRq1MapExistsException {

        assert (hwEcuRelease != null);
        assert (issueHwEcu != null);

        //
        // Ensure that IRM does not yet exist
        //
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : hwEcuRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == issueHwEcu) {
                throw (new DmRq1MapExistsException());
            }
        }
        DmRq1Irm_EcuRelease_IssueHw irm = create();

        //
        // Take over content from HW-ECU-RELEASE
        //
        irm.ACCOUNT_NUMBERS.setValue(hwEcuRelease.ACCOUNT_NUMBERS.getValue());

        //
        // Add mapping on map
        //
        irm.HAS_MAPPED_RELEASE.setElement(hwEcuRelease);
        irm.HAS_MAPPED_ISSUE.setElement(issueHwEcu);

        //
        // Add mapping on release
        //
        hwEcuRelease.MAPPED_ISSUES.addElement(irm, issueHwEcu);

        //
        // Add mapping on issue
        //
        issueHwEcu.HAS_MAPPED_RELEASES.addElement(irm, hwEcuRelease);

        return (irm);
    }

    public static DmRq1Irm_EcuRelease_IssueHw create(DmRq1EcuRelease ecuRelease, DmRq1Irm_EcuRelease_IssueHw existingMap, DmRq1IssueHwEcu issueHwEcu) throws DmRq1MapExistsException {
        assert (ecuRelease != null);
        assert (existingMap != null);
        assert (issueHwEcu != null);

        //
        // Create new IRM
        //
        DmRq1Irm_EcuRelease_IssueHw newMap = create(ecuRelease, issueHwEcu);

        //
        // Take over values from IRM
        //
        return (newMap);
    }

    public static DmRq1Irm_EcuRelease_IssueHw moveToEcu(DmRq1Irm_EcuRelease_IssueHw map, DmRq1EcuRelease newEcuRelease) throws DmRq1MapExistsException {
        assert (map != null);
        assert (newEcuRelease != null);

        //
        // Ensure that IRM does not yet exist
        //
        DmRq1IssueHwEcu issueHwEcu = (DmRq1IssueHwEcu) map.HAS_MAPPED_ISSUE.getElement();
        assert (issueHwEcu != null);
        for (DmMappedElement<DmRq1Irm, DmRq1Issue> m : newEcuRelease.MAPPED_ISSUES.getElementList()) {
            if (m.getTarget() == issueHwEcu) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1EcuRelease oldEcuRelease = (DmRq1EcuRelease) map.HAS_MAPPED_RELEASE.getElement();

        //
        // Change mapping on BC
        //
        oldEcuRelease.MAPPED_ISSUES.removeElement(issueHwEcu);
        newEcuRelease.MAPPED_ISSUES.addElement(map, issueHwEcu);

        //
        // Change mapping on issue
        //
        issueHwEcu.MAPPED_RELEASES.removeElement(oldEcuRelease);
        issueHwEcu.MAPPED_RELEASES.addElement(map, newEcuRelease);

        //
        // Change mapping on IRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        map.HAS_MAPPED_RELEASE.setElement(newEcuRelease);

        return (map);
    }

    public static DmRq1Irm_EcuRelease_IssueHw moveToIssueHwEcu(DmRq1Irm_EcuRelease_IssueHw map, DmRq1IssueHwEcu newIssueHwEcu) throws DmRq1MapExistsException {
        assert (map != null);
        assert (newIssueHwEcu != null);

        //
        // Ensure that IRM does not yet exist
        //
        DmRq1EcuRelease ecuRelease = (DmRq1EcuRelease) map.HAS_MAPPED_RELEASE.getElement();
        assert (ecuRelease != null);
        for (DmMappedElement<DmRq1Irm_EcuRelease_IssueHw, DmRq1EcuRelease> m : newIssueHwEcu.MAPPED_RELEASES.getElementList()) {
            if (m.getTarget() == ecuRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from IRM
        //
        DmRq1IssueHwEcu oldIssueHwEcu = (DmRq1IssueHwEcu) map.HAS_MAPPED_ISSUE.getElement();

        //
        // Change mapping issues
        //
        oldIssueHwEcu.MAPPED_RELEASES.removeElement(ecuRelease);
        newIssueHwEcu.MAPPED_RELEASES.addElement(map, ecuRelease);

        //
        // Change mapping on release
        //
        ecuRelease.MAPPED_ISSUES.removeElement(oldIssueHwEcu);
        ecuRelease.MAPPED_ISSUES.addElement(map, newIssueHwEcu);

        //
        // Change mapping on IRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        map.HAS_MAPPED_ISSUE.setElement(newIssueHwEcu);

        return (map);
    }

}
