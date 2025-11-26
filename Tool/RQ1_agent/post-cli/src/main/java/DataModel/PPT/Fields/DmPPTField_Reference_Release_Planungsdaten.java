/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTBCRelease;
import DataModel.PPT.Records.DmPPTFCRelease;
import DataModel.PPT.Records.DmPPTAenderung_Bc;
import DataModel.PPT.Records.DmPPTAenderung_Fc;
import DataModel.PPT.Records.DmPPTAenderung_IssueMappedOnPst;
import DataModel.PPT.Records.DmPPTAenderung_OnPst;
import DataModel.PPT.Records.DmPPTRelease;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Release_Planungsdaten extends DmPPTField_ReferenceList<DmPPTAenderung_OnPst> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmPPTField_Reference_Release_Planungsdaten.class.getCanonicalName());

    private final DmPPTRelease PROGRAMMSTAND;

    public DmPPTField_Reference_Release_Planungsdaten(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, new LinkedList<DmPPTAenderung_OnPst>(), nameForUserInterface);
        this.PROGRAMMSTAND = parent;
    }

    @Override
    public List<DmPPTAenderung_OnPst> getElementList() {

        if (element.isEmpty()) {
            PROGRAMMSTAND.getRq1Release().loadCacheForPPT();
            logger.info("PPT Programmstand: " + PROGRAMMSTAND.getId() + " Line: " + PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().getId() + " get Issues Ohne BC Bezug");
//            try {
                for (DmPPTAenderung_IssueMappedOnPst issueOhneBCBezug : PROGRAMMSTAND.PPT_ISSUES_WITHOUT_BC_RELATION.getElementList()) {
                    addElement(issueOhneBCBezug);
                }
//            } catch (Throwable thr) {
//                logger.log(Level.SEVERE, "PPT WARNING: DmPPTField_Reference_Release_Planungsdaten: Unable to Load the Issues Without BC Releations " + thr.toString(), thr);
//            }
            logger.info("PPT Programmstand: " + PROGRAMMSTAND.getId() + " get BCs");
            for (DmPPTBCRelease bc : PROGRAMMSTAND.PPT_BCS.getElementList()) {
                logger.info("PPT Programmstand: " + PROGRAMMSTAND.getId() + " BC: " + bc.getId() + " get Issues Without FC Relation");
//                try {
                    for (DmPPTAenderung_Bc issueOhneFcBezug : bc.PPT_ISSUE_SWS_WITHOUT_FC_RELATION.getElementList()) {
                        addElement(issueOhneFcBezug);
                    }
//                } catch (Throwable thr) {
//                    logger.log(Level.SEVERE, "PPT WARNING: DmPPTField_Reference_Release_Planungsdaten: Unable to Load the Issues Without FC Releations " + thr.toString(), thr);
//                }
                logger.info("PPT Programmstand: " + PROGRAMMSTAND.getId() + " BC: " + bc.getId() + " get FCs");
//                try {
                    for (DmPPTFCRelease fc : bc.PPT_FCS.getElementList()) {
                        for (DmPPTAenderung_Fc issues : fc.PPT_ISSUES_SWS.getElementList()) {
                            addElement(issues);
                        }
                    }
//                } catch (Throwable thr) {
//                    logger.log(Level.SEVERE, "PPT WARNING: DmPPTField_Reference_Release_Planungsdaten: Unable to Load the Issues with FC Releations " + thr.toString(), thr);
//                }
            }
        }
        return element;
    }
}
