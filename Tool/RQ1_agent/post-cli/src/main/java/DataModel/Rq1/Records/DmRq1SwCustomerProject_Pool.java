/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1SwCustomerProject_Pool;
import Rq1Cache.Types.Rq1XmlTable_Enumeration;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
public class DmRq1SwCustomerProject_Pool extends DmRq1SwCustomerProject implements DmRq1SwPoolProjectI {

    final public DmRq1Field_Table<Rq1XmlTable_Enumeration> CUSTOMER_SEVERITIES;
    final public DmRq1Field_Table<Rq1XmlTable_Enumeration> CUSTOMER_PRIORITIES;
    final public DmRq1Field_ReferenceList<DmRq1SoftwareProject> POOL_PROJECT_MEMBERS;
    final public DmRq1Field_ReferenceList<DmRq1SoftwareProject> POOL_PROJECT_MEMBERS_CUSTOMER_PROJECTS;
    final public DmRq1Field_Table<Rq1XmlTable_Enumeration> HAUPT_ENTWICKLUNGSPAKETE;

    final public DmRq1Field_ReferenceList<DmRq1Pst> OPEN_PST_ON_MEMBER_PROJECTS;
    final public DmRq1Field_ReferenceList<DmRq1Pst> ALL_PST_ON_MEMBER_PROJECTS;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1SwCustomerProject_Pool(Rq1SwCustomerProject_Pool rq1PoolProject) {
        super("CustPoolPrj", rq1PoolProject);

        //
        // Create and add fields
        //
        addField(CUSTOMER_SEVERITIES = new DmRq1Field_Table<>(this, rq1PoolProject.CUSTOMER_SEVERITIES, "Customer Severity"));
        addField(CUSTOMER_PRIORITIES = new DmRq1Field_Table<>(this, rq1PoolProject.CUSTOMER_PRIORITIES, "Customer Priority"));
        addField(new DmRq1Field_Text(this, rq1PoolProject.EXTERNAL_ID, "External ID"));
        addField(POOL_PROJECT_MEMBERS = new DmRq1Field_ReferenceList<>(this, rq1PoolProject.HAS_POOL_PROJECT_MEMBERS, "Pool Projects"));
        addField(POOL_PROJECT_MEMBERS_CUSTOMER_PROJECTS = new DmRq1Field_ReferenceList<>(this, rq1PoolProject.HAS_POOL_PROJECT_MEMBERS_CUSTOMER_PROJECTS, "Pool Customer Projects"));
        addField(HAUPT_ENTWICKLUNGSPAKETE = new DmRq1Field_Table<>(this, rq1PoolProject.HAUPT_ENTWICKLUNGSPAKETE, "HauptEntwicklungsPaket"));

        addField(OPEN_PST_ON_MEMBER_PROJECTS = new DmRq1Field_ReferenceList<>(this, rq1PoolProject.OPEN_PST_ON_MEMBER_PROJECTS, "Open PVER & PVAR on Member Projects"));
        addField(ALL_PST_ON_MEMBER_PROJECTS = new DmRq1Field_ReferenceList<>(this, rq1PoolProject.ALL_PST_ON_MEMBER_PROJECTS, "All PVER & PVAR on Member Projects"));
    }

    @Override
    public List<DmRq1Pst> getOpenPstOnMemberProjects() {
        return OPEN_PST_ON_MEMBER_PROJECTS.getElementList();
    }

    @Override
    public List<DmRq1Pst> getAllPstOnMemberProjects() {
        return ALL_PST_ON_MEMBER_PROJECTS.getElementList();
    }
}
