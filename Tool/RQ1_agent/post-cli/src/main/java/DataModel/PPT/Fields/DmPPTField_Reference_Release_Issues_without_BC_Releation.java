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
import DataModel.PPT.Records.DmPPTAenderung_IssueMappedOnPst;
import DataModel.PPT.Records.DmPPTRelease;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Release_Issues_without_BC_Releation extends DmPPTField_ReferenceList<DmPPTAenderung_IssueMappedOnPst> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmPPTField_Reference_Release_Issues_without_BC_Releation.class.getCanonicalName());

    private final DmPPTRelease PROGRAMMSTAND;

    public DmPPTField_Reference_Release_Issues_without_BC_Releation(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, new ArrayList<DmPPTAenderung_IssueMappedOnPst>(), nameForUserInterface);
        this.PROGRAMMSTAND = parent;
    }

    @Override
    public List<DmPPTAenderung_IssueMappedOnPst> getElementList() {
        if (element.isEmpty()) {
            this.PROGRAMMSTAND.getRq1Release().loadCacheForPPT();
            for ( DmPPTAenderung_IssueMappedOnPst pptIssueSW : this.PROGRAMMSTAND.MAPPED_ISSUES.getElementList()) {
                boolean takeIssue = true;
                for (DmPPTBCRelease bc : this.PROGRAMMSTAND.PPT_BCS.getElementList()) {
                    try {
                        for (DmPPTFCRelease fc : bc.PPT_FCS.getElementList()) {
                            if (fc.PPT_ISSUES_SWS.containsIssue(pptIssueSW)) {
                                //Issue is also under the fc
                                takeIssue = false;
                            }
                        }
                    } catch (Exception ex) {
                        logger.warning("PPT: WARNING: Unable to load the FCs to the given BC : " + bc.getId() + " PST: " + this.PROGRAMMSTAND.getId() + " Line " + this.PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().getId() + " \nException " + ex.toString());
                    }
                    if (bc.PPT_ISSUE_SWS_WITHOUT_FC_RELATION.containsIssue(pptIssueSW)) {
                        //Issue is also under the bc
                        takeIssue = false;
                    }
                }
                if (takeIssue) {
                    //issue is not under BC and FC
                    this.addElement(pptIssueSW);
                }
            }
        }
        return element;
    }
}
