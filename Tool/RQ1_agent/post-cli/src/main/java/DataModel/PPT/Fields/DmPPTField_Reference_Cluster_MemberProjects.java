/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCluster;
import DataModel.PPT.Records.DmPPTCustomerProject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Cluster_MemberProjects extends DmPPTField_ReferenceList<DmPPTCustomerProject> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmPPTField_Reference_Cluster_MemberProjects.class.getCanonicalName());

    private final DmPPTField_Reference_PoolPrj_MemberPrj PPT_PROJECT_MEMBERS;
    private final DmPPTCluster parentCluster;

    public DmPPTField_Reference_Cluster_MemberProjects(DmPPTCluster parent, DmPPTField_Reference_PoolPrj_MemberPrj elements, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTCustomerProject>(), nameForUserInterface);
        this.PPT_PROJECT_MEMBERS = elements;
        this.parentCluster = parent;
    }

    @Override
    public List<DmPPTCustomerProject> getElementList() {

        if (element.isEmpty()) {
            for (DmPPTCustomerProject memberProject : PPT_PROJECT_MEMBERS.getElementList()) {
                try {
                    if (memberProject.isInCluster(parentCluster)) {
                        memberProject.PPT_CLUSTER.setElement(parentCluster);
                        addElement(memberProject);
                    }
                } catch (NullPointerException ex) {
                    logger.warning("NullPointer Exception in DmPPTField_Reference_Cluster_MemberProjects Exception Message: " + ex.getMessage());
                }
            }
            Collections.sort(element);
        }

        return element;
    }
}
