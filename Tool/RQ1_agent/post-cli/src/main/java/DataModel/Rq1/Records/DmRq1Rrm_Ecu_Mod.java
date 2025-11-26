/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.DmMappedElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1Rrm_Ecu_Mod;
import UiSupport.AssigneeFilter;
import util.EcvMapSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Rrm_Ecu_Mod extends DmRq1Rrm {

    public final DmRq1Field_Reference<DmRq1EcuRelease> MAPPED_PARENT;
    public final DmRq1Field_Reference<DmRq1ModRelease> MAPPED_CHILDREN;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Rrm_Ecu_Mod(Rq1Rrm_Ecu_Mod rq1Rrm) {
        super("RRM-HW_ECU-HW_MOD", rq1Rrm);

        //
        // Create and add fields
        //
        addField(MAPPED_CHILDREN = new DmRq1Field_Reference<>(this, rq1Rrm.HAS_MAPPED_CHILD_RELEASE, "Children"));
        addField(MAPPED_PARENT = new DmRq1Field_Reference<>(this, rq1Rrm.HAS_MAPPED_PARENT_RELEASE, "Parent"));
    }

    private static DmRq1Rrm_Ecu_Mod create() {
        Rq1Rrm_Ecu_Mod rq1Rrm = new Rq1Rrm_Ecu_Mod();
        DmRq1Rrm_Ecu_Mod dmRrm = new DmRq1Rrm_Ecu_Mod(rq1Rrm);
        DmRq1ElementCache.addElement(rq1Rrm, dmRrm);
        return (dmRrm);
    }

    public static DmRq1Rrm_Ecu_Mod create(DmRq1EcuRelease ecuRelease, DmRq1ModRelease dmModRelease) throws DmRq1MapExistsException {
        assert (ecuRelease != null);
        assert (dmModRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : ecuRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == dmModRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        DmRq1Rrm_Ecu_Mod rrm = create();

        //
        // Take over content from parent
        //
        rrm.ACCOUNT_NUMBERS.setValue(ecuRelease.ACCOUNT_NUMBERS.getValue());

        //
        // Connect map
        //
        rrm.MAPPED_PARENT.setElement(ecuRelease);
        rrm.MAPPED_CHILDREN.setElement(dmModRelease);
        ecuRelease.MAPPED_CHILDREN.addElement(rrm, dmModRelease);
        dmModRelease.MAPPED_PARENT.addElement(rrm, ecuRelease);

        return (rrm);
    }

    public static DmRq1Rrm_Ecu_Mod create(DmRq1EcuRelease ecuRelease, DmRq1Rrm_Ecu_Mod existingMap, DmRq1ModRelease modRelease) throws DmRq1MapExistsException {
        assert (ecuRelease != null);
        assert (modRelease != null);
        assert (existingMap != null);

        //
        // Create new map
        //
        DmRq1Rrm_Ecu_Mod newMap = create(ecuRelease, modRelease);

        //
        // Take over values from map
        //
        return (newMap);
    }

    public static DmRq1Rrm_Ecu_Mod moveToEcu(DmRq1Rrm_Ecu_Mod map, DmRq1EcuRelease newEcuRelease) throws DmRq1MapExistsException {
        assert (map != null);
        assert (newEcuRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        DmRq1ModRelease modRelease = map.MAPPED_CHILDREN.getElement();
        assert (modRelease != null);
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : newEcuRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == modRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from RRM
        //
        DmRq1EcuRelease oldEcuRelease = map.MAPPED_PARENT.getElement();

        //
        // Change mapping on ECU
        //
        oldEcuRelease.MAPPED_CHILDREN.removeElement(modRelease);
        newEcuRelease.MAPPED_CHILDREN.addElement(map, modRelease);

        //
        // Change mapping on MOD
        //
        modRelease.MAPPED_PARENT.removeElement(oldEcuRelease);
        modRelease.MAPPED_PARENT.addElement(map, newEcuRelease);

        //
        // Change mapping on RRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        map.MAPPED_PARENT.setElement(newEcuRelease);

        return (map);
    }

    public static DmRq1Rrm_Ecu_Mod moveToMod(DmRq1Rrm_Ecu_Mod map, DmRq1ModRelease newModRelease) throws DmRq1MapExistsException {
        assert (map != null);
        assert (newModRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        DmRq1EcuRelease ecuRelease = map.MAPPED_PARENT.getElement();
        assert (ecuRelease != null);
        for (DmMappedElement<DmRq1Rrm_Ecu_Mod, DmRq1EcuRelease> m : newModRelease.MAPPED_PARENT.getElementList()) {
            if (m.getTarget() == ecuRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from RRM
        //
        DmRq1ModRelease oldModRelease = map.MAPPED_CHILDREN.getElement();

        //
        // Change mapping on MOD
        //
        oldModRelease.MAPPED_PARENT.removeElement(ecuRelease);
        newModRelease.MAPPED_PARENT.addElement(map, ecuRelease);

        //
        // Change mapping on MOD
        //
        ecuRelease.MAPPED_CHILDREN.removeElement(oldModRelease);
        ecuRelease.MAPPED_CHILDREN.addElement(map, newModRelease);

        //
        // Change mapping on RRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        map.MAPPED_CHILDREN.setElement(newModRelease);

        return (map);
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTitle() + " " + MAPPED_PARENT.getElement().getTypeIdTitle() + " " + MAPPED_CHILDREN.getElement().getTypeIdTitle();
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
