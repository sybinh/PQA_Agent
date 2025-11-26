/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementListField_ReadOnlyFromSource;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Fc;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.Rq1.Records.DmRq1Rrm_Bc_Fc;
import DataModel.Rq1.Records.DmRq1Rrm_Pst_Bc;
import DataModel.DmMappedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Field_FcMappedToPst extends DmElementListField_ReadOnlyFromSource<DmRq1Fc> {

    final private DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Release> mappedChildren;

    public DmRq1Field_FcMappedToPst(DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Release> mappedChildren, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (mappedChildren != null);

        this.mappedChildren = mappedChildren;
    }

    @Override
    protected Collection<DmRq1Fc> loadElementList() {
        List<DmRq1Fc> result = new ArrayList<>();

        for (DmMappedElement<DmRq1Rrm, DmRq1Release> mappedBc : mappedChildren.getElementList()) {
            DmRq1Rrm rrmToBc = mappedBc.getMap();
            if ((rrmToBc instanceof DmRq1Rrm_Pst_Bc) && (rrmToBc.isCanceled() == false)) {
                DmRq1Release bc = mappedBc.getTarget();
                if (bc instanceof DmRq1Bc) {
                    for (DmMappedElement<DmRq1Rrm, DmRq1Release> mappedFc : ((DmRq1Bc) bc).MAPPED_CHILDREN.getElementList()) {
                        DmRq1Rrm rrmToFc = mappedFc.getMap();
                        if ((rrmToFc instanceof DmRq1Rrm_Bc_Fc) && (rrmToFc.isCanceled() == false)) {
                            DmRq1Release fc = mappedFc.getTarget();
                            if (fc instanceof DmRq1Fc) {
                                if (result.contains(fc) == false) {
                                    result.add((DmRq1Fc) fc);
                                }
                            }
                        }
                    }
                }
            }

        }

        return (result);
    }

}
