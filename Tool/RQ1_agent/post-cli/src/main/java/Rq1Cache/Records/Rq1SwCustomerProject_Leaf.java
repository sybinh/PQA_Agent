/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataStore.DsField_Xml;
import Rq1Cache.Fields.Rq1QueryField;
import Rq1Cache.Fields.Rq1ReferenceListField_FilterByClass;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1XmlTable_ProPlaToProjectPstRail;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsMilestones;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsTypes;

/**
 *
 * @author GUG2WI
 */
@SuppressWarnings("serial")
public class Rq1SwCustomerProject_Leaf extends Rq1SwCustomerProject {

    final public Rq1XmlSubField_Xml PROPLATO;
    final public Rq1XmlSubField_Table<Rq1XmlTable_ProPlaToProjectPstRail> PROPLATO_PSTRAILS;

    final private Rq1QueryField ALL_ISSUES_ON_PST;
    final public Rq1ReferenceListField_FilterByClass ALL_ISSUE_SW_ON_PST;
    final public Rq1ReferenceListField_FilterByClass ALL_ISSUE_FD_ON_PST;
    final public Rq1QueryField ALL_BC_ON_PST;

    final public Rq1XmlSubField_Table<Rq1XmlTable_SwMetricsTypes> SW_METRICS_TYPES;
    final public Rq1XmlSubField_Table<Rq1XmlTable_SwMetricsMilestones> SW_METRICS_MILESTONES;

    public Rq1SwCustomerProject_Leaf() {
        super(Rq1NodeDescription.SOFTWARE_COSTUMER_PROJECT_LEAF);

        addField(PROPLATO = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKOWN_ELEMENTS_NOT_ALLOWED, "ProPlaTo"));
        PROPLATO.setOptional();
        addField(PROPLATO_PSTRAILS = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_ProPlaToProjectPstRail(), PROPLATO, "PSTSchiene"));

        addField(ALL_ISSUES_ON_PST = new Rq1QueryField(this, "AllIssuesOnPst", Rq1NodeDescription.ISSUE_SW.getRecordType()));
        ALL_ISSUES_ON_PST.addCriteria_Reference("hasMappedReleases.hasMappedRelease.belongsToProject", this);
        ALL_ISSUES_ON_PST.addCriteria_Value("hasMappedReleases.hasMappedRelease.Category", "SW-Version");

        addField(ALL_ISSUE_SW_ON_PST = new Rq1ReferenceListField_FilterByClass(this, ALL_ISSUES_ON_PST, Rq1IssueSW.class));
        addField(ALL_ISSUE_FD_ON_PST = new Rq1ReferenceListField_FilterByClass(this, ALL_ISSUES_ON_PST, Rq1IssueFD.class));

        addField(ALL_BC_ON_PST = new Rq1QueryField(this, "AllBcOnPst", Rq1NodeDescription.BC_RELEASE.getRecordType()));
        ALL_BC_ON_PST.addCriteria_Value("Type", "BC");
        ALL_BC_ON_PST.addCriteria_Reference("hasMappedParents.hasMappedParentRelease.belongsToProject", this);
        ALL_BC_ON_PST.addCriteria_Value("hasMappedParents.hasMappedParentRelease.Category", "SW-Version");

        addField(SW_METRICS_TYPES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_SwMetricsTypes(), SW_METRICS, "Type"));
        addField(SW_METRICS_MILESTONES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_SwMetricsMilestones(), SW_METRICS, "Milestone"));
    }
}
