/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCluster;
import DataModel.PPT.Records.DmPPTLine;
import DataModel.PPT.Records.DmPPTInfoSysCo;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Cluster_InfoSysCo extends DmPPTField_ReferenceList<DmPPTInfoSysCo> {

    private final DmPPTCluster CLUSTER;
    private boolean alreadyLoaded;

    public DmPPTField_Reference_Cluster_InfoSysCo(DmPPTCluster parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTInfoSysCo>(), nameForUserInterface);
        this.CLUSTER = parent;
        alreadyLoaded = false;
    }

    @Override
    public List<DmPPTInfoSysCo> getElementList() {
        if (!alreadyLoaded) {
            for (DmPPTLine line : CLUSTER.PPT_LINES.getElementList()) {
                this.addElements(line.SYSTEMKONSTANTEN_TIDIED.getElementList());
            }
            alreadyLoaded = true;
        }
        return element;
    }
}
