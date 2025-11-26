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
import DataModel.PPT.Records.DmPPTLine;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Cluster_InfoFcBc extends DmPPTField_ReferenceList<DmPPTInfoFcBc> {

    private final DmPPTCluster CLUSTER;
    private boolean alreadyLoaded;

    public DmPPTField_Reference_Cluster_InfoFcBc(DmPPTCluster parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTInfoFcBc>(), nameForUserInterface);
        this.CLUSTER = parent;
        alreadyLoaded = false;
    }

    @Override
    public List<DmPPTInfoFcBc> getElementList() {
        if (!alreadyLoaded) {
            for (DmPPTLine line : CLUSTER.PPT_LINES.getElementList()) {
                for (DmPPTInfoFcBc fcBcInfo : line.FCBC_TIDIED.getElementList()) {
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
