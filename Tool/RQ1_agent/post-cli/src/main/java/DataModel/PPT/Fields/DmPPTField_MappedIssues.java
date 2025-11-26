/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTAenderung_IssueMappedOnPst;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Irm_Pst_IssueSw;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.DmMappedElement;
import Rq1Cache.Types.Rq1XmlTable_ChangesToConfiguration;
import Rq1Data.Enumerations.Scope;
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
public class DmPPTField_MappedIssues extends DmPPTField_ReferenceList<DmPPTAenderung_IssueMappedOnPst> {

    private final DmPPTRelease PROGRAMMSTAND;

    public DmPPTField_MappedIssues(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, new ArrayList<DmPPTAenderung_IssueMappedOnPst>(), nameForUserInterface);
        this.PROGRAMMSTAND = parent;
    }

    @Override
    public List<DmPPTAenderung_IssueMappedOnPst> getElementList() {
        if (element.isEmpty()) {
            PROGRAMMSTAND.getRq1Release().loadCacheForPPT();
            for (DmMappedElement<DmRq1Irm, DmRq1Issue> elem : PROGRAMMSTAND.getMAPPED_ISSUES().getElementList()) {
                if (!elem.getMap().isCanceled() && !elem.getTarget().isCanceled()
                        && elem.getMap().SCOPE.getValue().equals(Scope.EXTERNAL) && elem.getTarget().SCOPE.getValue().equals(Scope.EXTERNAL)) {
                    boolean takeIssueDerivativesCheck = true;

                    if (PROGRAMMSTAND.areDerivativesRelevant()) {
                        takeIssueDerivativesCheck = false;
                        if (elem.getMap() instanceof DmRq1Irm_Pst_IssueSw) {
                            DmRq1Irm_Pst_IssueSw irm = (DmRq1Irm_Pst_IssueSw) elem.getMap();
                            if (!irm.MAPPING_TO_DERIVATIVES.getDerivatives().isEmpty()) { //If the Mapping is not empty
                                for (String line : irm.MAPPING_TO_DERIVATIVES.getActiveDerivatives()) {
                                    String extLine = PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getProPlaToLineExternal(line);
                                    if (extLine.equals(PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().getTitle())) {
                                        takeIssueDerivativesCheck = true;
                                    }
                                }
                            } else {
                                //if the mapping is empty than all are mandatory
                                takeIssueDerivativesCheck = true;
                            }
                        }
                    }

                    if (takeIssueDerivativesCheck) {
                        String issueComment = ((DmRq1Irm_Pst_IssueSw) elem.getMap()).RB_COMMENT.getValueAsText();
                        //give the issue the ctc
                        String ctc = "";
                        DmRq1Irm_Pst_IssueSw irmIssueRelease = ((DmRq1Irm_Pst_IssueSw) elem.getMap());
                        EcvTableData data = irmIssueRelease.CHANGES_TO_CONFIGURATION.getValue();
                        Rq1XmlTable_ChangesToConfiguration desc = irmIssueRelease.CHANGES_TO_CONFIGURATION.getTableDescription();
                        for (EcvTableRow row : data.getRows()) {
                            if (row.getValueAt(desc.CUST_VISIBLE).toString().equals("true")) {
                                boolean takeCTC = false;
                                if (!PROGRAMMSTAND.areDerivativesRelevant() || row.getValueAt(desc.DERIVATIVE) == null || row.getValueAt(desc.DERIVATIVE).toString().isEmpty()) {
                                    //If there is no derivate, it belongs to all derivatives

                                    takeCTC = true;

                                } else {
                                    //if there is a derivate then check it
                                    //TODO USE THE SETCOLUMN OF THE DERIVATIVES
                                    Iterator<String> iter = getIteratorOfObject(row.getValueAt(desc.DERIVATIVE));
                                    while (iter.hasNext()) {
                                        String derivate = iter.next();
                                        derivate = this.PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getProPlaToLineExternal(derivate);
                                        if (derivate.equals(this.PROGRAMMSTAND.BELONG_TO_SCHIENE.getElement().NAME.getValueAsText())) {

                                            takeCTC = true;

                                        }
                                    }
                                }
                                if (takeCTC) {
                                    ctc += "\n" + row.getValueAt(desc.TYPE).toString() + " - "
                                            + row.getValueAt(desc.NAME).toString() + " - "
                                            + row.getValueAt(desc.TARGET).toString() + " - "
                                            + row.getValueAt(desc.EXTERNAL_COMMENT).toString() + "\n";
                                }
                            }
                        }
                        DmPPTAenderung_IssueMappedOnPst pptIssueSW = new DmPPTAenderung_IssueMappedOnPst((DmRq1IssueSW) elem.getTarget(), PROGRAMMSTAND, elem.getMap(), issueComment, ctc);
                        addElement(pptIssueSW);
                    }
                }
            }
        }
        return element;
    }

    /**
     *
     * @param id of the searched Issue SW
     * @return null, if not connected, the Issue, when connected
     */
    public DmPPTAenderung_IssueMappedOnPst getIssueById(String id) {
        DmPPTAenderung_IssueMappedOnPst result = null;
        for (DmPPTAenderung_IssueMappedOnPst issue : getElementList()) {
            if (issue.getId().equals(id)) {
                return issue;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Iterator<String> getIteratorOfObject(Object d) {
        return ((TreeSet<String>) d).iterator();
    }
}
