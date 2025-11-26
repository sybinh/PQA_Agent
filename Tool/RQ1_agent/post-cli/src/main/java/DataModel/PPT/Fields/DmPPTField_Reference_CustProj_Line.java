/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTCustomerProject;
import DataModel.PPT.Records.DmPPTLine;
import Rq1Cache.Types.Rq1XmlTable_ProPlaToProjectPstRail;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_CustProj_Line extends DmPPTField_ReferenceList<DmPPTLine> {

    private final DmPPTCustomerProject CUSTOMER_PROJECT;

    public DmPPTField_Reference_CustProj_Line(DmPPTCustomerProject parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTLine>(), nameForUserInterface);
        this.CUSTOMER_PROJECT = parent;
    }

    @Override
    public List<DmPPTLine> getElementList() {
        if (element.isEmpty()) {
            Set<String> proPlaToSchiene = new HashSet<>();
            if (this.CUSTOMER_PROJECT.getPROPLATO_PSTRAILS().getValue().isEmpty() == false) {
                EcvTableData data = CUSTOMER_PROJECT.getPROPLATO_PSTRAILS().getValue();
                Rq1XmlTable_ProPlaToProjectPstRail desc = CUSTOMER_PROJECT.getPROPLATO_PSTRAILS().getTableDescription();
                for (EcvTableRow row : data.getRows()) {
                    //Line Handling
                    if (!proPlaToSchiene.contains(row.getValueAt(desc.RAIL).toString())) {
                        proPlaToSchiene.add(row.getValueAt(desc.RAIL).toString());
                        if (row.getValueAt(desc.RAIL).toString() != null) {
                            Object clusterObject = row.getValueAt(desc.CLUSTER);
                            String cluster = clusterObject != null ? clusterObject.toString() : "";
                            this.addElement(new DmPPTLine(row.getValueAt(desc.RAIL).toString(), CUSTOMER_PROJECT, cluster));
                        }
                    }
                }

            }
        }
        return element;
    }
}
