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
import Rq1Cache.Records.Rq1Rrm_Mod_Comp;
import UiSupport.AssigneeFilter;
import util.EcvMapSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Rrm_Mod_Comp extends DmRq1Rrm {

    public final DmRq1Field_Reference<DmRq1ModRelease> MAPPED_PARENT;
    public final DmRq1Field_Reference<DmRq1CompRelease> MAPPED_CHILDREN;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Rrm_Mod_Comp(Rq1Rrm_Mod_Comp rq1Rrm) {
        super("RRM-HW_MOD-HW_COMP", rq1Rrm);

        //
        // Create and add fields
        //
        addField(MAPPED_CHILDREN = new DmRq1Field_Reference<>(this, rq1Rrm.HAS_MAPPED_CHILD_RELEASE, "Children"));
        addField(MAPPED_PARENT = new DmRq1Field_Reference<>(this, rq1Rrm.HAS_MAPPED_PARENT_RELEASE, "Parent"));
    }

    private static DmRq1Rrm_Mod_Comp create() {
        Rq1Rrm_Mod_Comp rq1Rrm = new Rq1Rrm_Mod_Comp();
        DmRq1Rrm_Mod_Comp dmRrm = new DmRq1Rrm_Mod_Comp(rq1Rrm);
        DmRq1ElementCache.addElement(rq1Rrm, dmRrm);
        return (dmRrm);
    }

    public static DmRq1Rrm_Mod_Comp create(DmRq1ModRelease modRelease, DmRq1CompRelease dmCompRelease) throws DmRq1MapExistsException {
        assert (modRelease != null);
        assert (dmCompRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : modRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == dmCompRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        DmRq1Rrm_Mod_Comp rrm = create();

        //
        // Take over content from parent
        //
        rrm.ACCOUNT_NUMBERS.setValue(modRelease.ACCOUNT_NUMBERS.getValue());

        //
        // Connect map
        //
        rrm.MAPPED_PARENT.setElement(modRelease);
        rrm.MAPPED_CHILDREN.setElement(dmCompRelease);
        modRelease.MAPPED_CHILDREN.addElement(rrm, dmCompRelease);
        dmCompRelease.MAPPED_PARENT.addElement(rrm, modRelease);

        return (rrm);
    }

    public static DmRq1Rrm_Mod_Comp create(DmRq1ModRelease modRelease, DmRq1Rrm_Mod_Comp existingMap, DmRq1CompRelease compRelease) throws DmRq1MapExistsException {
        assert (modRelease != null);
        assert (compRelease != null);
        assert (existingMap != null);

        //
        // Create new map
        //
        DmRq1Rrm_Mod_Comp newMap = create(modRelease, compRelease);

        //
        // Take over values from map
        //
        return (newMap);
    }

    public static DmRq1Rrm_Mod_Comp moveToMod(DmRq1Rrm_Mod_Comp map, DmRq1ModRelease newModRelease) throws DmRq1MapExistsException {
        assert (map != null);
        assert (newModRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        DmRq1CompRelease compRelease = map.MAPPED_CHILDREN.getElement();
        assert (compRelease != null);
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : newModRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == compRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from RRM
        //
        DmRq1ModRelease oldModRelease = map.MAPPED_PARENT.getElement();

        //
        // Change mapping on ECU
        //
        oldModRelease.MAPPED_CHILDREN.removeElement(compRelease);
        newModRelease.MAPPED_CHILDREN.addElement(map, compRelease);

        //
        // Change mapping on MOD
        //
        compRelease.MAPPED_PARENT.removeElement(oldModRelease);
        compRelease.MAPPED_PARENT.addElement(map, newModRelease);

        //
        // Change mapping on RRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        map.MAPPED_PARENT.setElement(newModRelease);

        return (map);
    }

    public static DmRq1Rrm_Mod_Comp moveToComp(DmRq1Rrm_Mod_Comp map, DmRq1CompRelease newCompRelease) throws DmRq1MapExistsException {
        assert (map != null);
        assert (newCompRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        DmRq1ModRelease modRelease = map.MAPPED_PARENT.getElement();
        assert (modRelease != null);
        for (DmMappedElement<DmRq1Rrm_Mod_Comp, DmRq1ModRelease> m : newCompRelease.MAPPED_PARENT.getElementList()) {
            if (m.getTarget() == modRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from RRM
        //
        DmRq1CompRelease oldCompRelease = map.MAPPED_CHILDREN.getElement();

        //
        // Change mapping on MOD
        //
        oldCompRelease.MAPPED_PARENT.removeElement(modRelease);
        newCompRelease.MAPPED_PARENT.addElement(map, modRelease);

        //
        // Change mapping on MOD
        //
        modRelease.MAPPED_CHILDREN.removeElement(oldCompRelease);
        modRelease.MAPPED_CHILDREN.addElement(map, newCompRelease);

        //
        // Change mapping on RRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        map.MAPPED_CHILDREN.setElement(newCompRelease);

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
