/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCluster;
import DataModel.PPT.Records.DmPPTPoolProject;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Leaf;
import DataModel.Rq1.Records.DmRq1SoftwareProject;
import Rq1Cache.Types.Rq1XmlTable_ProPlaToProjectPstRail;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_PoolPrj_Cluster extends DmPPTField_ReferenceList<DmPPTCluster> {

    private final DmRq1Field_ReferenceList<DmRq1SoftwareProject> PROJECT_MEMBERS;
    private final DmPPTPoolProject parentPool;

    public DmPPTField_Reference_PoolPrj_Cluster(DmPPTPoolProject parent, DmRq1Field_ReferenceList<DmRq1SoftwareProject> elements, String nameForUserInterface) {
        super(parent, new ArrayList<DmPPTCluster>(), nameForUserInterface);
        this.PROJECT_MEMBERS = elements;
        this.parentPool = parent;
    }

    @Override
    public List<DmPPTCluster> getElementList() {
        if (element.isEmpty()) {

            Set<String> proPlaToClusters = new HashSet<>();

            for (DmRq1SoftwareProject memberProject : PROJECT_MEMBERS.getElementList()) {
                assert (memberProject instanceof DmRq1SwCustomerProject_Leaf);

                EcvTableData pstRailsData = ((DmRq1SwCustomerProject_Leaf) memberProject).PROPLATO_PSTRAILS.getValue();
                if (pstRailsData.isEmpty() == false) {

                    Rq1XmlTable_ProPlaToProjectPstRail desc = (Rq1XmlTable_ProPlaToProjectPstRail) pstRailsData.getDescription();

                    for (EcvTableRow row : pstRailsData.getRows()) {
                        String cluster = desc.CLUSTER.getValue(row);

                        if ((cluster != null) && (cluster.isEmpty() == false)) {

                            if (proPlaToClusters.contains(cluster) == false) {
                                proPlaToClusters.add(cluster);
                                this.addElement(new DmPPTCluster(parentPool, cluster));
                            }
                        }

                    }
                }
            }
            Collections.sort(element);
        }

        return element;
    }
}
