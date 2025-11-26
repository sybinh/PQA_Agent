/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTBCRelease;
import DataModel.PPT.Records.DmPPTCTC;
import DataModel.PPT.Records.DmPPTAenderung_IssueMappedOnPst;
import DataModel.PPT.Records.DmPPTRelease;
import Rq1Cache.Types.Rq1XmlTable_ChangesToConfiguration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Release_CTC extends DmPPTField_ReferenceList<DmPPTCTC> {

    private final DmPPTRelease PROGRAMMSTAND;

    public DmPPTField_Reference_Release_CTC(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, new ArrayList<DmPPTCTC>(), nameForUserInterface);
        this.PROGRAMMSTAND = parent;
    }

    @Override
    public List<DmPPTCTC> getElementList() {
        if (element.isEmpty()) {
            this.PROGRAMMSTAND.getRq1Release().loadCacheForPPT();
            for (DmPPTBCRelease bc : PROGRAMMSTAND.PPT_BCS.getElementList()) {
                EcvTableData ctcTable = bc.CHANGES_TO_CONFIGURATION.getValue();
                for (EcvTableRow row : ctcTable.getRows()) {
                    Rq1XmlTable_ChangesToConfiguration desc = (Rq1XmlTable_ChangesToConfiguration) ctcTable.getDescription();
                    if (checkAddElement(desc, row)) {
                        this.addElement(new DmPPTCTC(desc, row, PROGRAMMSTAND, bc));
                    }
                }
            }
            for ( DmPPTAenderung_IssueMappedOnPst issueSW : PROGRAMMSTAND.MAPPED_ISSUES.getElementList()) {
                if (issueSW.IRM != null) {
                    EcvTableData ctcTable = issueSW.IRM.CHANGES_TO_CONFIGURATION.getValue();
                    for (EcvTableRow row : ctcTable.getRows()) {
                        Rq1XmlTable_ChangesToConfiguration desc = (Rq1XmlTable_ChangesToConfiguration) ctcTable.getDescription();
                        if (checkAddElement(desc, row)) {
                            this.addElement(new DmPPTCTC((Rq1XmlTable_ChangesToConfiguration) ctcTable.getDescription(), row, PROGRAMMSTAND, issueSW));
                        }
                    }
                }
            }
        }
        return element;
    }

    public boolean checkAddElement(Rq1XmlTable_ChangesToConfiguration desc, EcvTableRow row) {
        if (row.getValueAt(desc.CUST_VISIBLE) == null) {
            return false;
        }
        if (!row.getValueAt(desc.CUST_VISIBLE).toString().equals("true")) {
            return false;
        }
        if (row.getValueAt(desc.DERIVATIVE) != null && !row.getValueAt(desc.DERIVATIVE).toString().isEmpty()) {
            //Check the derivates
            if (this.PROGRAMMSTAND.areDerivativesRelevant()) {
                Iterator<String> iter = this.getIteratorOfObject(row.getValueAt(desc.DERIVATIVE));
                while (iter.hasNext()) {
                    String derivate = iter.next();
                    derivate = PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getProPlaToLineExternal(derivate);
                    if (derivate.equals(PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().NAME.getValueAsText())) {
                        //the derivate is given so true
                        return true;
                    }
                }
                //The derivate is not in the list so no
                return false;
            }
        }
        //everything is ok so go on
        return true;
    }

    @SuppressWarnings("unchecked")
    private Iterator<String> getIteratorOfObject(Object d) {
        return ((TreeSet<String>) d).iterator();
    }
}
