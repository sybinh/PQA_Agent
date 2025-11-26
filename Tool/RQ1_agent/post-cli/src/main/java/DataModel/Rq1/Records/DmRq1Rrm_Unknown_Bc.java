/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import ConfigurableRules.ConfigRule.Monitoring.ConfigurableRuleManagerRule_RRM;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1Rrm_Unknown_Bc;
import UiSupport.AssigneeFilter;
import util.EcvMapSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Rrm_Unknown_Bc extends DmRq1Rrm {

    final public DmRq1Field_Reference<DmRq1Release> HAS_MAPPED_CHILD_RELEASE;
    final public DmRq1Field_Reference<DmRq1Release> HAS_MAPPED_PARENT_RELEASE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1Rrm_Unknown_Bc(Rq1Rrm_Unknown_Bc rq1Rrm_Unknown_Bc) {
        super("RRM-UNKNOWN-BC", rq1Rrm_Unknown_Bc);

        //
        // Create and add fields
        //
        addField(HAS_MAPPED_CHILD_RELEASE = new DmRq1Field_Reference<>(this, rq1Rrm_Unknown_Bc.HAS_MAPPED_CHILD_RELEASE, "BC"));
        addField(HAS_MAPPED_PARENT_RELEASE = new DmRq1Field_Reference<>(this, rq1Rrm_Unknown_Bc.HAS_MAPPED_PARENT_RELEASE, "Unknown Release"));
        addRule(new ConfigurableRuleManagerRule_RRM(this, HAS_MAPPED_PARENT_RELEASE, HAS_MAPPED_CHILD_RELEASE));
    }

    @Override
    public String getTypeIdTitleforMail() {
        return getTitle() + " " + HAS_MAPPED_PARENT_RELEASE.getElement().getTitle() + " " + HAS_MAPPED_CHILD_RELEASE.getElement().getTitle();
    }

    @Override
    public EcvMapSet<AssigneeFilter, DmRq1Project> getProjects() {
        EcvMapSet<AssigneeFilter, DmRq1Project> set = new EcvMapSet<>();
        if (HAS_MAPPED_CHILD_RELEASE.getElement() != null) {
            set.addValueIfNotNull(AssigneeFilter.RRMCHILDRELEASEPROJECT, HAS_MAPPED_CHILD_RELEASE.getElement().PROJECT.getElement());
            set.addValueIfNotNull(AssigneeFilter.RRM_PROJECT, HAS_MAPPED_CHILD_RELEASE.getElement().PROJECT.getElement());
        }
        if (HAS_MAPPED_PARENT_RELEASE.getElement() != null) {
            set.addValueIfNotNull(AssigneeFilter.RRMPARENTRELEASEPROJECT, HAS_MAPPED_PARENT_RELEASE.getElement().PROJECT.getElement());
            set.addValueIfNotNull(AssigneeFilter.RRM_PROJECT, HAS_MAPPED_PARENT_RELEASE.getElement().PROJECT.getElement());
        }
        return set;
    }

    @Override
    public DmRq1Field_Reference<? extends DmRq1Release> getParentField() {
        return (HAS_MAPPED_PARENT_RELEASE);
    }

}
