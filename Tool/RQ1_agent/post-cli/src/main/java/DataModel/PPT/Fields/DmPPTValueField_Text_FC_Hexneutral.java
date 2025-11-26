/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTFCRelease;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import Rq1Cache.Types.Rq1XmlTable_ChangesToPartlist;
import Rq1Data.Enumerations.CtpClassification;
import java.util.Iterator;
import java.util.TreeSet;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTValueField_Text_FC_Hexneutral extends DmPPTValueField_Text {

    public DmPPTValueField_Text_FC_Hexneutral(DmPPTFCRelease parent, DmRq1Field_Table<Rq1XmlTable_ChangesToPartlist> ctp, String nameForUserInterface) {
        super(parent, getHexneutralFromFC(ctp, parent), nameForUserInterface);
    }

    private static String getHexneutralFromFC(DmRq1Field_Table<Rq1XmlTable_ChangesToPartlist> ctp, DmPPTFCRelease parent) {
        String returnValue = "unbekannt";
        EcvTableData dataCTP = ctp.getValue();
        Rq1XmlTable_ChangesToPartlist descCTP = ctp.getTableDescription();
        for (EcvTableRow row : dataCTP.getRows()) {

            if (row.getValueAt(descCTP.TYPE) != null
                    && row.getValueAt(descCTP.TYPE).equals("FC")
                    && row.getValueAt(descCTP.NAME) != null && row.getValueAt(descCTP.NAME).equals(parent.FC_SHORTNAME.getValueAsText())
                    && row.getValueAt(descCTP.TARGET) != null && row.getValueAt(descCTP.TARGET).equals(parent.FC_VERSION.getValueAsText())) {

                if (parent.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().areDerivativesRelevant()
                        && row.getValueAt(descCTP.DERIVATIVE) != null) { //If derivatives are mentioned
                    Iterator<String> iter = getIteratorOfObject(row.getValueAt(descCTP.DERIVATIVE));
                    while (iter.hasNext()) {
                        String derivate = iter.next();
                        derivate = parent.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getProPlaToLineExternal(derivate);
                        if (derivate.equals(parent.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().NAME.getValueAsText())) {
                            if (row.getValueAt(descCTP.CLASSIFICATION) != null && row.getValueAt(descCTP.CLASSIFICATION).equals(CtpClassification.HEXNEUTRAL.getText())) {
                                //The CTP entry is linked to the BC and the Classification is Hexneutral
                                if (!returnValue.equals("false")) {
                                    returnValue = "true";
                                }
                            } else {
                                returnValue = "false";
                            }
                        }
                    }
                } else {
                    if (row.getValueAt(descCTP.CLASSIFICATION) != null && row.getValueAt(descCTP.CLASSIFICATION).equals(CtpClassification.HEXNEUTRAL.getText())) {
                        //The CTP entry is linked to the BC and the Classification is Hexneutral
                        if (!returnValue.equals("false")) {
                            returnValue = "true";
                        }
                    } else {
                        returnValue = "false";
                    }
                }
            }

        }

        return returnValue;
    }

    @SuppressWarnings("unchecked")
    private static Iterator<String> getIteratorOfObject(Object d) {
        return ((TreeSet<String>) d).iterator();
    }
}
