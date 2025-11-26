/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementListField_FromSource;
import DataModel.DmElementListField_ReadOnlyI;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.DmMappedElement;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Field_ReferenceList_PverWithAssignedIrmOrIsw extends DmElementListField_FromSource<DmRq1Pst> implements DmElementListField_ReadOnlyI<DmRq1Pst> {

    private final DmRq1Field_ReferenceList<DmRq1Irm> openAssignedIrm;
    private final DmRq1Field_ReferenceList<DmRq1IssueSW> openAssignedIssueSW;

    public DmRq1Field_ReferenceList_PverWithAssignedIrmOrIsw(String nameForUserInterface, DmRq1Field_ReferenceList<DmRq1Irm> openAssignedIrm, DmRq1Field_ReferenceList<DmRq1IssueSW> openAssignedIssueSW) {
        super(nameForUserInterface);
        assert (openAssignedIrm != null);
        assert (openAssignedIssueSW != null);

        this.openAssignedIrm = openAssignedIrm;
        this.openAssignedIssueSW = openAssignedIssueSW;
    }

    @Override
    protected Collection<DmRq1Pst> loadElementList() {
        Map<DmRq1Pst, Integer> result = new IdentityHashMap<>();

        for (DmRq1Irm irm : openAssignedIrm.getElementList()) {
            DmRq1Release release = irm.HAS_MAPPED_RELEASE.getElement();
            if (release instanceof DmRq1Pst) {
                result.put((DmRq1Pst) release, 0);
            }
        }

        for (DmRq1IssueSW i_sw : openAssignedIssueSW.getElementList()) {
            for (DmMappedElement<DmRq1Irm_Pst_IssueSw, DmRq1Pst> mappedPst : i_sw.HAS_MAPPED_PST.getElementList()) {
                if (mappedPst.getMap().isOpen() == true) {
                    result.put(mappedPst.getTarget(), 1);
                }
            }
        }

        return (result.keySet());
    }

    @Override
    public void addElement(DmRq1Pst element) {
        throw new UnsupportedOperationException("Not supported.");
    }

}
