/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCustomerProject;
import DataModel.PPT.Records.DmPPTPoolProject;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Leaf;
import DataModel.Rq1.Records.DmRq1SoftwareProject;
import Rq1Data.Enumerations.LifeCycleState_Project;
import java.util.LinkedList;
import java.util.List;
import util.EcvEnumeration;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_PoolPrj_MemberPrj extends DmPPTField_ReferenceList<DmPPTCustomerProject> {

    private final DmRq1Field_ReferenceList<DmRq1SoftwareProject> PROJECT_MEMBERS;
    private final DmPPTPoolProject POOL_PROJECT;

    public DmPPTField_Reference_PoolPrj_MemberPrj(DmPPTPoolProject pool, DmRq1Field_ReferenceList<DmRq1SoftwareProject> elements, String nameForUserInterface) {
        super(pool, new LinkedList<DmPPTCustomerProject>(), nameForUserInterface);
        this.PROJECT_MEMBERS = elements;
        this.POOL_PROJECT = pool;
    }

    @Override
    public List<DmPPTCustomerProject> getElementList() {
        if (element.isEmpty()) {
            for (DmRq1SoftwareProject memberProject : PROJECT_MEMBERS.getElementList()) {
                assert (memberProject instanceof DmRq1SwCustomerProject_Leaf);
                EcvEnumeration lcs = memberProject.getLifeCycleState();
                if (memberProject.getLifeCycleState().equals(LifeCycleState_Project.NEW)
                        || memberProject.getLifeCycleState().equals(LifeCycleState_Project.OPEN)) {
                if (((DmRq1SwCustomerProject_Leaf) memberProject).PROPLATO_PSTRAILS.getValue().isEmpty() == false) {
                    DmPPTCustomerProject toAdd = new DmPPTCustomerProject((DmRq1SwCustomerProject_Leaf) memberProject, null, POOL_PROJECT);
                    this.addElement(new DmPPTCustomerProject((DmRq1SwCustomerProject_Leaf) memberProject, null, POOL_PROJECT));
                }
            }
        }
        }
        return element;
    }

    public boolean contains(DmPPTCustomerProject custProj) {
        return this.element.contains(custProj);
    }
}
