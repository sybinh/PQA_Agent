/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.Ecv.PST.Records.DmEcvBc;
import DataModel.Ecv.PST.Records.DmEcvFc;
import DataModel.Ecv.PST.Records.DmEcvProgrammstand;
import DataModel.PPT.Records.DmPPTBCRelease;
import DataModel.PPT.Records.DmPPTFCRelease;
import DataModel.PPT.Records.DmPPTAenderung;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.Rq1.Records.DmRq1Fc;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.DmMappedElement;
import Rq1Cache.Types.Rq1XmlTable_ChangesToPartlist;
import Rq1Data.Enumerations.IntegrationAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_BCRelease_FCRelease extends DmPPTField_ReferenceList<DmPPTFCRelease> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmPPTField_Reference_BCRelease_FCRelease.class.getCanonicalName());

    private final DmPPTBCRelease BC_RELEASE;

    public DmPPTField_Reference_BCRelease_FCRelease(DmPPTBCRelease parent, String nameForUserInterface) {
        super(parent, new ArrayList<DmPPTFCRelease>(), nameForUserInterface);
        this.BC_RELEASE = parent;
    }

    public List<DmPPTFCRelease> getElementByName(String name) {
        List<DmPPTFCRelease> fcs = new LinkedList<>();
        for (DmPPTFCRelease fc : this.getElementList()) {
            if (fc.getName().equals(name)) {
                fcs.add(fc);
            }
        }
        return fcs;
    }

    @Override
    public List<DmPPTFCRelease> getElementList() {
        if (element.isEmpty()) {
            for (DmMappedElement<DmRq1Rrm, DmRq1Release> mapElem : this.BC_RELEASE.getMAPPED_CHILDREN().getElementList()) {
                if (!mapElem.getMap().isCanceled() && !mapElem.getTarget().isCanceled() && !mapElem.getMap().INTEGRATION_ACTION.getValue().equals(IntegrationAction.REMOVE)) {
                    assert (mapElem.getTarget() instanceof DmRq1Fc);

                    //Check if the FC should be ignored, is deactivated or is activated
                    //The information, if it is activated or deactivated is to find in the ctp of the rrm between PST and BC
                    EcvTableData ecv = this.BC_RELEASE.CHANGES_TO_PARTLIST.getValue();
                    Rq1XmlTable_ChangesToPartlist desc = this.BC_RELEASE.CHANGES_TO_PARTLIST.getTableDescription();
                    //Go Through the data and check if the derivative of the row is ok
                    //if derivatives are relevant

                    Rq1XmlTable_ChangesToPartlist.FcActive fcState = Rq1XmlTable_ChangesToPartlist.FcActive.NOTDEFINED;
                    if (this.BC_RELEASE.HAS_PARENT_RELEASE.getElement().areDerivativesRelevant()) {
                        //PVAR

                        for (EcvTableRow row : ecv.getRows()) {
                            if (row.getValueAt(desc.DERIVATIVE) != null) {
                                for (String derivative : this.getSetOfObject(row.getValueAt(desc.DERIVATIVE))) {
                                    String externalDerivative = this.BC_RELEASE.HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().LINE_INTERNAL_TO_EXTERNAL.get(derivative);
                                    if (externalDerivative != null
                                            && externalDerivative.equals(this.BC_RELEASE.HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().getTitle())) {
                                        Rq1XmlTable_ChangesToPartlist.FcActive fcStateTemp = desc.getFcStatusOneRow(row, ecv, ((DmRq1Fc) mapElem.getTarget()).getName(),
                                                ((DmRq1Fc) mapElem.getTarget()).getVersion());

                                        if (fcState.equals(Rq1XmlTable_ChangesToPartlist.FcActive.NOTDEFINED)) {
                                            fcState = fcStateTemp;
                                        }
                                    }
                                }
                            } else {
                                Rq1XmlTable_ChangesToPartlist.FcActive fcStateTemp = desc.getFcStatusOneRow(row, ecv, ((DmRq1Fc) mapElem.getTarget()).getName(),
                                        ((DmRq1Fc) mapElem.getTarget()).getVersion());

                                if (fcState.equals(Rq1XmlTable_ChangesToPartlist.FcActive.NOTDEFINED)) {
                                    fcState = fcStateTemp;
                                }
                            }

                            if (!fcState.equals(Rq1XmlTable_ChangesToPartlist.FcActive.NOTDEFINED)) {
                                break;
                            }
                        }
                    } else {
                        fcState = desc.getFcStatus(ecv, ((DmRq1Fc) mapElem.getTarget()).getName(),
                                ((DmRq1Fc) mapElem.getTarget()).getVersion());

                    }
                    //if the FC is Deactive, it shouldn't be taken
                    if (fcState.equals(Rq1XmlTable_ChangesToPartlist.FcActive.ACTIVE)) {
                        this.addElement(new DmPPTFCRelease((DmRq1Fc) mapElem.getTarget(), BC_RELEASE));
                    } else if (fcState.equals(Rq1XmlTable_ChangesToPartlist.FcActive.NOTDEFINED)) {
                        //If it is not defined, we have to check the predecessor PST List
                        logger.info("PPT BC Release: " + this.BC_RELEASE.getId() + " get the FCBC List of the Predecessor Release");
                        DmPPTRelease predec = this.BC_RELEASE.HAS_PARENT_RELEASE.getElement().PREDECESSOR.getElement();
                        if (predec != null) {
                            DmEcvProgrammstand pstList = predec.PST_PROGRAMMSTAND.getElement();
                            if (pstList != null) {
                                //Check if the BC is in there
                                DmEcvBc bcPst = pstList.PROGRAMMSTAND_PAKETE.getPaketByTitle(this.BC_RELEASE.getName());

                                if (bcPst != null) {
                                    DmEcvFc fcPst = null;
                                    if (((DmRq1Fc) mapElem.getTarget()).getVersion().contains(".")) {
                                        fcPst = bcPst.BC_FC.getFunctionByTitleAndAlternative(((DmRq1Fc) mapElem.getTarget()).getName(),
                                                ((DmRq1Fc) mapElem.getTarget()).getVersion().substring(0, ((DmRq1Fc) mapElem.getTarget()).getVersion().indexOf(".")));
                                    }
                                    if (fcPst != null) {
                                        this.addElement(new DmPPTFCRelease((DmRq1Fc) mapElem.getTarget(), BC_RELEASE));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Collections.sort(element);
        }
        return element;
    }

    public boolean isIssueUnderFc(DmPPTAenderung issue) {
        getElementList();
        for (DmPPTFCRelease fc : this.element) {
            if (fc.PPT_ISSUES_SWS.containsIssue(issue)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private Set<String> getSetOfObject(Object d) {
        return (Set<String>) d;
    }
}
