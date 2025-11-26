/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTBCRelease;
import DataModel.PPT.Records.DmPPTAenderung;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Pvar;
import DataModel.Rq1.Records.DmRq1Pver;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.Rq1.Records.DmRq1Rrm_Pst_Bc;
import DataModel.DmMappedElement;
import Rq1Data.Enumerations.IntegrationAction;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import util.EcvProPlaToConfigClass;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Release_BC extends DmPPTField_ReferenceList<DmPPTBCRelease> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmPPTField_Reference_Release_BC.class.getCanonicalName());

    private final DmPPTRelease PROGRAMMSTAND;

    public DmPPTField_Reference_Release_BC(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, new ArrayList<DmPPTBCRelease>(), nameForUserInterface);
        this.PROGRAMMSTAND = parent;
    }

    public List<DmPPTBCRelease> getElementsByName(String name) {
        List<DmPPTBCRelease> bcs = new LinkedList<>();
        for (DmPPTBCRelease bc : this.getElementList()) {
            if (bc.getName().equals(name)) {
                bcs.add(bc);
            }
        }
        return bcs;
    }

    public DmPPTBCRelease getElementByID(String id) {
        for (DmPPTBCRelease bc : this.getElementList()) {
            if (bc.getId().equals(id)) {
                return bc;
            }
        }
        return null;
    }

    @Override
    public List<DmPPTBCRelease> getElementList() {
        if (element.isEmpty()) {
            try {
                //this.PROGRAMMSTAND.getRELEASE().getElement().loadCacheForPPT();
                //Only if the bcs have not been loaded before
                for (DmMappedElement<DmRq1Rrm, DmRq1Release> mapElement
                        : this.PROGRAMMSTAND.getMAPPED_CHILDREN().getElementList()) {
                    //If RRM is not canceled and BC is not canceled then
                    if (!mapElement.getMap().isCanceled() && !mapElement.getTarget().isCanceled() && (mapElement.getMap().INTEGRATION_ACTION.getValue() != null && !mapElement.getMap().INTEGRATION_ACTION.getValue().equals(IntegrationAction.REMOVE))) {
                        //Is the target a BC Release
                        if (mapElement.getTarget() instanceof DmRq1Bc) {
                            DmRq1Bc bc = (DmRq1Bc) mapElement.getTarget();
                            //Check the bc name
                            if (!EcvProPlaToConfigClass.BC_NAMES_WHICH_SHOULD_NOT_BE_SENT.contains(bc.getTitle())) {
                                //If the release is a PFAM/PVAR then check the derivatives of the RRM
                                if (this.PROGRAMMSTAND.getRq1Release() instanceof DmRq1Pver) {
                                    //we don't have the check the derivatives here
                                    this.addElement(new DmPPTBCRelease(bc, this.PROGRAMMSTAND, (DmRq1Rrm_Pst_Bc) mapElement.getMap()));
                                } else if (this.PROGRAMMSTAND.getRq1Release() instanceof DmRq1Pvar) {
                                    //Check, if the BC suitable for the given line
                                    if (mapElement.getMap() instanceof DmRq1Rrm_Pst_Bc) {
                                        //Get the rrm and cast it to the right class
                                        DmRq1Rrm_Pst_Bc rrm = (DmRq1Rrm_Pst_Bc) mapElement.getMap();
                                        //check if there are active Derivates
                                        if (rrm.SELECTION_OF_DERIVATIVES.getActiveDerivatives().isEmpty()) {
                                            this.addElement(new DmPPTBCRelease(bc, this.PROGRAMMSTAND, (DmRq1Rrm_Pst_Bc) mapElement.getMap()));
                                        }
                                        for (String activeDerivative : rrm.SELECTION_OF_DERIVATIVES.getActiveDerivatives()) {
                                            //get the external view of the derivative from the Project
                                            String externalDerivative = this.PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().LINE_INTERNAL_TO_EXTERNAL.get(activeDerivative);
                                            if (externalDerivative != null) {
                                                //if external derivative equals the line
                                                if (externalDerivative.equals(this.PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().NAME.getValueAsText())) {
                                                    this.addElement(new DmPPTBCRelease(bc, this.PROGRAMMSTAND, (DmRq1Rrm_Pst_Bc) mapElement.getMap()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            } catch (Exception ex) {
                logger.warning("PPT: WARNING: Unable to Load the BCs for the given Programmstand: " + this.PROGRAMMSTAND.getId());
            }
        }
        return element;
    }

    public boolean isIssueUnderBC(DmPPTAenderung issue) {
        getElementList();
        for (DmPPTBCRelease bc : this.element) {
            if (bc.PPT_ISSUE_SWS_WITHOUT_FC_RELATION.containsIssue(issue)) {
                return true;
            }
        }
        return false;
    }
}
