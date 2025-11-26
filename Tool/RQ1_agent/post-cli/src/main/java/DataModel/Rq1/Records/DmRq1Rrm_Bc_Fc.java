/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import ConfigurableRules.ConfigRule.Monitoring.ConfigurableRuleManagerRule_RRM;
import DataModel.Rq1.Monitoring.Rule_Rrm_Bc_Fc_DeliveryDate;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.DmMappedElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1Rrm_Bc_Fc;
import UiSupport.AssigneeFilter;
import util.EcvMapSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Rrm_Bc_Fc extends DmRq1Rrm {

    public final DmRq1Field_Text CHANGES_TO_ISSUES;
    public final DmRq1Field_Reference<DmRq1Bc> MAPPED_PARENT;
    public final DmRq1Field_Reference<DmRq1Fc> MAPPED_CHILDREN;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Rrm_Bc_Fc(Rq1Rrm_Bc_Fc rq1Rrm_Bc_Fc) {
        super("RRM-BC-FC", rq1Rrm_Bc_Fc);

        //
        // Create and add fields
        //
        addField(CHANGES_TO_ISSUES = new DmRq1Field_Text(this, rq1Rrm_Bc_Fc.CHANGES_TO_ISSUES, "Changes To Issues"));
        addField(MAPPED_CHILDREN = new DmRq1Field_Reference<>(this, rq1Rrm_Bc_Fc.HAS_MAPPED_CHILD_RELEASE, "Children"));
        addField(MAPPED_PARENT = new DmRq1Field_Reference<>(this, rq1Rrm_Bc_Fc.HAS_MAPPED_PARENT_RELEASE, "Parent"));

        //
        // Create and add rules
        //
        addRule(new Rule_Rrm_Bc_Fc_DeliveryDate(this));
        addRule(new ConfigurableRuleManagerRule_RRM(this, MAPPED_PARENT, MAPPED_CHILDREN));
    }

    public static DmRq1Rrm_Bc_Fc create(DmRq1Bc bcRelease, DmRq1Fc dmFcRelease) throws DmRq1MapExistsException {
        assert (bcRelease != null);
        assert (dmFcRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : bcRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == dmFcRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        DmRq1Rrm_Bc_Fc rrm = DmRq1ElementCache.createRrm_Bc_Fc();

        //
        // Take over content from BC
        //
        rrm.ACCOUNT_NUMBERS.setValue(bcRelease.ACCOUNT_NUMBERS.getValue());

        //
        // Connect BC - Map - FC
        //
        rrm.MAPPED_PARENT.setElement(bcRelease);
        rrm.MAPPED_CHILDREN.setElement(dmFcRelease);
        bcRelease.MAPPED_CHILDREN.addElement(rrm, dmFcRelease);
        dmFcRelease.MAPPED_BC.addElement(rrm, bcRelease);

        return (rrm);
    }

    public static DmRq1Rrm_Bc_Fc create(DmRq1Bc bcRelease, DmRq1Rrm_Bc_Fc dmRrm_Bc_Fc, DmRq1Fc dmFcRelease) throws DmRq1MapExistsException {
        assert (bcRelease != null);
        assert (dmRrm_Bc_Fc != null);
        assert (dmFcRelease != null);

        //
        // Create new RRM
        //
        DmRq1Rrm_Bc_Fc newRrm = create(bcRelease, dmFcRelease);

        //
        // Take over values from RRM
        //
        return (newRrm);
    }

    public static DmRq1Rrm_Bc_Fc moveToBc(DmRq1Bc newBcRelease, DmRq1Rrm_Bc_Fc dmRrm_Bc_Fc, DmRq1Fc fcRelease) throws DmRq1MapExistsException {
        assert (newBcRelease != null);
        assert (dmRrm_Bc_Fc != null);
        assert (fcRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : newBcRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == fcRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from RRM
        //
        DmRq1Bc oldBcRelease = dmRrm_Bc_Fc.MAPPED_PARENT.getElement();

        //
        // Change mapping on BC
        //
        oldBcRelease.MAPPED_CHILDREN.removeElement(fcRelease);
        newBcRelease.MAPPED_CHILDREN.addElement(dmRrm_Bc_Fc, fcRelease);

        //
        // Change mapping on FC
        //
        fcRelease.MAPPED_BC.removeElement(oldBcRelease);
        fcRelease.MAPPED_BC.addElement(dmRrm_Bc_Fc, newBcRelease);

        //
        // Change mapping on RRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        dmRrm_Bc_Fc.MAPPED_PARENT.setElement(newBcRelease);

        return (dmRrm_Bc_Fc);
    }

    public static DmRq1Rrm_Bc_Fc moveToFc(DmRq1Bc bcRelease, DmRq1Rrm_Bc_Fc dmRrm_Bc_Fc, DmRq1Fc newFcRelease) throws DmRq1MapExistsException {
        assert (bcRelease != null);
        assert (dmRrm_Bc_Fc != null);
        assert (newFcRelease != null);

        //
        // Ensure that RRM does not yet exist
        //
        for (DmMappedElement<DmRq1Rrm, DmRq1Release> m : bcRelease.MAPPED_CHILDREN.getElementList()) {
            if (m.getTarget() == newFcRelease) {
                throw (new DmRq1MapExistsException());
            }
        }

        //
        // Get mapping from RRM
        //
        DmRq1Fc oldFcRelease = dmRrm_Bc_Fc.MAPPED_CHILDREN.getElement();

        //
        // Change mapping on FCs
        //
        oldFcRelease.MAPPED_BC.removeElement(bcRelease);
        newFcRelease.MAPPED_BC.addElement(dmRrm_Bc_Fc, bcRelease);

        //
        // Change mapping on BC
        //
        bcRelease.MAPPED_CHILDREN.removeElement(oldFcRelease);
        bcRelease.MAPPED_CHILDREN.addElement(dmRrm_Bc_Fc, newFcRelease);

        //
        // Change mapping on RRM *** This setting has to be done as last action. Otherwise, the loading of the mapped lists resets the change ***
        //
        dmRrm_Bc_Fc.MAPPED_CHILDREN.setElement(newFcRelease);

        return (dmRrm_Bc_Fc);
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
