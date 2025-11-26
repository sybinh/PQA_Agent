/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCluster;
import DataModel.PPT.Records.DmPPTInfoFcBc;
import DataModel.PPT.Records.DmPPTPoolProject;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_PoolProject_InfoFcBc extends DmPPTField_ReferenceList<DmPPTInfoFcBc> {

    private final DmPPTPoolProject POOL_PROJECT;
    private boolean alreadyLoaded;

    public DmPPTField_Reference_PoolProject_InfoFcBc(DmPPTPoolProject parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTInfoFcBc>(), nameForUserInterface);
        this.POOL_PROJECT = parent;
        alreadyLoaded = false;
    }

    @Override
    public List<DmPPTInfoFcBc> getElementList() {
        if (!alreadyLoaded) {
            for (DmPPTCluster cluster : POOL_PROJECT.PPT_CLUSTER.getElementList()) {
                for (DmPPTInfoFcBc fcBcInfo : cluster.FCBC_TIDIED.getElementList()) {
                    if (!this.element.contains(fcBcInfo)) {
                        this.addElement(fcBcInfo);
                    }
                }
            }
            alreadyLoaded = true;
        }
        return element;
    }
}
