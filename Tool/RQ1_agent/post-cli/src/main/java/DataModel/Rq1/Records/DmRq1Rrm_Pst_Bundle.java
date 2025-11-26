/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmMappedElement;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1Rrm_Pst_Bundle;
import UiSupport.AssigneeFilter;
import util.EcvMapSet;

/**
 *
 * @author MIN9COB
 */
public class DmRq1Rrm_Pst_Bundle extends DmRq1Rrm {

    public final DmRq1Field_Reference<DmRq1BundleRelease> MAPPED_PARENT;
    public final DmRq1Field_Reference<DmRq1Pst> MAPPED_CHILDREN;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Rrm_Pst_Bundle(Rq1Rrm_Pst_Bundle rq1Rrm) {
        super("RRM-PST-Bundle", rq1Rrm);

        //
        // Create and add fields
        //
        addField(MAPPED_CHILDREN = new DmRq1Field_Reference<>(this, rq1Rrm.HAS_MAPPED_CHILD_RELEASE, "Children"));
        addField(MAPPED_PARENT = new DmRq1Field_Reference<>(this, rq1Rrm.HAS_MAPPED_PARENT_RELEASE, "Parent"));
    }

    public static DmRq1Rrm_Pst_Bundle create(DmRq1BundleRelease bundleRelease, DmRq1Pst pstRelease) throws DmRq1MapExistsException {
        assert (bundleRelease != null);
        assert (pstRelease != null && (pstRelease instanceof DmRq1PverRelease || pstRelease instanceof DmRq1PverCollection));

        //
        // Ensure that IRM does not yet exist
        //
        if (bundleRelease.MAPPED_CHILDREN.isLoaded() == true) {
            for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : bundleRelease.MAPPED_CHILDREN.getElementList()) {
                if (m.getTarget() == pstRelease ) {
                    throw (new DmRq1MapExistsException());
                }
            }
        }

        DmRq1Rrm_Pst_Bundle rrm = DmRq1ElementCache.createRrm_Pst_Bundle();

        rrm.ACCOUNT_NUMBERS.setValue(pstRelease.ACCOUNT_NUMBERS.getValue());

        //
        // Connect PST - Map - Bundle
        //
        rrm.MAPPED_PARENT.setElement(bundleRelease);
        rrm.MAPPED_CHILDREN.setElement(pstRelease);
        bundleRelease.MAPPED_CHILDREN.addElement(rrm, pstRelease);
        pstRelease.MAPPED_PARENT.addElement(rrm, bundleRelease);

        return (rrm);
    }
    
    @Override
    public EcvMapSet<AssigneeFilter, DmRq1Project> getProjects() {
        EcvMapSet<AssigneeFilter, DmRq1Project> set = new EcvMapSet<>();
        if (MAPPED_CHILDREN.getElement() != null) {
            set.addValueIfNotNull(AssigneeFilter.RRMCHILDRELEASEPROJECT, MAPPED_CHILDREN.getElement().PROJECT.getElement());
            set.addValueIfNotNull(AssigneeFilter.RRM_PROJECT, MAPPED_CHILDREN.getElement().PROJECT.getElement());
        }
        if (MAPPED_PARENT.getElement() != null) {
            set.addValueIfNotNull(AssigneeFilter.RRMPARENTRELEASEPROJECT, MAPPED_PARENT.getElement().PROJECT.getElement());
            set.addValueIfNotNull(AssigneeFilter.RRM_PROJECT, MAPPED_PARENT.getElement().PROJECT.getElement());
        }
        return set;
    }

    @Override
    public DmRq1Field_Reference<? extends DmRq1Release> getParentField() {
        return (MAPPED_PARENT);
    }

}
