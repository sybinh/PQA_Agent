/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Fields.Rq1QueryField;
import Rq1Cache.Fields.Rq1ReferenceListField_FilterByClass;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1XmlTable_Enumeration;
import Rq1Data.Enumerations.LifeCycleState_Release;

/**
 *
 * @author GUG2WI
 */
public class Rq1SwCustomerProject_Pool extends Rq1SwCustomerProject {

    final private Rq1XmlSubField_Xml CUSTOMER_SEVERITY_FIELD;
    final public Rq1XmlSubField_Table<Rq1XmlTable_Enumeration> CUSTOMER_SEVERITIES;
    final private Rq1XmlSubField_Xml CUSTOMER_PRIORITIES_FIELD;
    final public Rq1XmlSubField_Table<Rq1XmlTable_Enumeration> CUSTOMER_PRIORITIES;
    final public Rq1DatabaseField_Text DOMAIN;
    final public Rq1ReferenceListField_FilterByClass HAS_POOL_PROJECT_MEMBERS_CUSTOMER_PROJECTS;
    final private Rq1XmlSubField_Xml HAUPT_ENTWICKLUNGSPAKETE_FELD;
    final public Rq1XmlSubField_Table<Rq1XmlTable_Enumeration> HAUPT_ENTWICKLUNGSPAKETE;

    final public Rq1QueryField OPEN_PST_ON_MEMBER_PROJECTS;
    final public Rq1QueryField ALL_PST_ON_MEMBER_PROJECTS;

    @SuppressWarnings("LeakingThisInConstructor")
    public Rq1SwCustomerProject_Pool() {
        super(Rq1NodeDescription.SOFTWARE_CUSTOMER_PROJECT_POOL);

        addField(CUSTOMER_SEVERITY_FIELD = new Rq1XmlSubField_Xml(this, TAGS, "CustomerSeverities"));
        addField(CUSTOMER_SEVERITIES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_Enumeration(), CUSTOMER_SEVERITY_FIELD, "Severity"));
        CUSTOMER_SEVERITY_FIELD.setOptional();

        addField(CUSTOMER_PRIORITIES_FIELD = new Rq1XmlSubField_Xml(this, TAGS, "CustomerPriorities"));
        addField(CUSTOMER_PRIORITIES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_Enumeration(), CUSTOMER_PRIORITIES_FIELD, "Priority"));
        CUSTOMER_PRIORITIES_FIELD.setOptional();

        addField(DOMAIN = new Rq1DatabaseField_Text(this, "Domain"));
        addField(HAS_POOL_PROJECT_MEMBERS_CUSTOMER_PROJECTS = new Rq1ReferenceListField_FilterByClass(this, HAS_POOL_PROJECT_MEMBERS, Rq1SwCustomerProject_Leaf.class));

        addField(HAUPT_ENTWICKLUNGSPAKETE_FELD = new Rq1XmlSubField_Xml(this, TAGS, "HauptEntwicklungsPakete"));
        addField(HAUPT_ENTWICKLUNGSPAKETE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_Enumeration(), HAUPT_ENTWICKLUNGSPAKETE_FELD, "Paket"));
        HAUPT_ENTWICKLUNGSPAKETE_FELD.setOptional();

        addField(OPEN_PST_ON_MEMBER_PROJECTS = new Rq1QueryField(this, "OpenPstOnMemberProjects", Rq1NodeDescription.BC_RELEASE.getRecordType()));
        OPEN_PST_ON_MEMBER_PROJECTS.addCriteria_ValueList("Type", "PVER", "PVAR/PFAM");
        OPEN_PST_ON_MEMBER_PROJECTS.addCriteria_Reference("belongsToProject.belongsToPoolProject", this);
        OPEN_PST_ON_MEMBER_PROJECTS.addCriteria_ValueList("LifeCycleState", LifeCycleState_Release.getAllOpenState());

        addField(ALL_PST_ON_MEMBER_PROJECTS = new Rq1QueryField(this, "AllPstOnMemberProjects", Rq1NodeDescription.BC_RELEASE.getRecordType()));
        ALL_PST_ON_MEMBER_PROJECTS.addCriteria_ValueList("Type", "PVER", "PVAR/PFAM");
        ALL_PST_ON_MEMBER_PROJECTS.addCriteria_Reference("belongsToProject.belongsToPoolProject", this);
    }
}
