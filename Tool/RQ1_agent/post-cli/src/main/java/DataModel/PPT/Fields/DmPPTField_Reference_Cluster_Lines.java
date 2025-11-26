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
import DataModel.PPT.Records.DmPPTLine;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Cluster_Lines extends DmPPTField_ReferenceList<DmPPTLine> {

    private final DmPPTCluster parentCluster;

    public DmPPTField_Reference_Cluster_Lines(DmPPTCluster parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTLine>(), nameForUserInterface);
        this.parentCluster = parent;
    }

    @Override
    public List<DmPPTLine> getElementList() {
        if (element.isEmpty()) {
            for(DmPPTCustomerProject custProj : parentCluster.PPT_MEMBER_PROJECTS.getElementList()){
                for(DmPPTLine line : custProj.PPT_LINES.getElementList()){
                    this.addElement(line);
                }
            }
            Collections.sort(element);
        }
        return element;
    }
}
