/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.Ecv.SystemConstants.Records.DmEcvProgrammstand_SysConst;
import DataModel.Ecv.SystemConstants.Records.DmEcvSysConst;
import DataModel.Ecv.SystemConstants.Records.DmEcvSysConstSchiene;
import DataModel.Ecv.SystemConstants.Records.DmEcvSysConstSchieneTable;
import DataModel.PPT.Records.DmPPTCTC;
import DataModel.PPT.Records.DmPPTCustomerProject;
import DataModel.PPT.Records.DmPPTRelease;
import DataModel.PPT.Records.PPTDataRule;
import DataModel.Rq1.Records.DmRq1Attachment;
import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import ToolUsageLogger.ToolUsageLogger;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This field is used to specify the FcBc reading and delta calculation
 *
 * @author moe83wi
 */
public class DmPPTField_Reference_Release_Sysconst extends DmPPTField_Reference<DmEcvProgrammstand_SysConst> {

    final private static Logger LOGGER = Logger.getLogger(DmPPTField_Reference_Release_Sysconst.class.getName());

    /*RULE MANAGEMENT*/

 /*RULE MANAGEMENT*/
    static final public RuleDescription sysConstListDoubleRuleDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROPLATO),
            "Double fc_sysconst_list.xml",
            "For each Release and line there should only be one fc_sysconst list attached\n"
            + "otherwise this would lower the quality of the proplato data");
    private PPTDataRule sysConstListDoubleRule = null;

    static final public RuleDescription sysConstListRuleDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROPLATO),
            "Missing fc_sysconst_list.xml",
            "For each delivered program version (planned date of release in the past) \n"
            + "a fc_sysconst_list.xml has to be attached to the RQ1 release. \n"
            + "In case of a PVAR planning for each derivative within the PVAR a separate fc_sysconst_list.xml has to be attached.\n"
            + "\n"
            + "XML content check: name of PST line has to fit to the RQ1 release information. XML format has to fit to the requirements");
    private PPTDataRule sysConstListRule = null;

    static final public RuleDescription sysConstPlanningConflictRuleDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROPLATO),
            "System Constant planning conflicts",
            "There is only one CTC entry allowed, which defines the value of a system constant. \n"
            + "If there are two or more entries they must have the same value.");
    private PPTDataRule sysConstPlanningConflictListRule = null;

    /*RULE MANAGEMENT*/
    private final DmPPTRelease RELEASE;
    public List<DmPPTRelease> predec;

    private boolean loadFromCurrentElementDone = false;
    private boolean listAtCurrentElementExists = false;

    public DmPPTField_Reference_Release_Sysconst(DmPPTRelease parent, String nameForUserInterface) {
        super(parent, null, nameForUserInterface);
        this.RELEASE = parent;
        this.predec = new LinkedList<>();
    }

    @Override
    public DmEcvProgrammstand_SysConst getElement() {
        if (super.getElement() != null) {
            super.getElement().PROGRAMMSTAND_NAME.setValue(RELEASE.PROGRAMMSTAND.getValueAsText());
            return super.getElement();
        }
        if (getSysconstListAtCurrentElement() == false) { //There is no data available at this node
            this.RELEASE.getRq1Release().loadCacheForPPT();
            /*RULE MANAGEMENT*/
            if (this.RELEASE.AUSLIEFERDATUM_SOLL.getValue().isEarlierOrEqualThen(getProcessDate())) {
                StringBuilder s = new StringBuilder(40);
                s.append("Planned date of Release is in the Past: ").append(this.RELEASE.AUSLIEFERDATUM_SOLL.getValueAsText()).
                        append("\n There has to be a propper fc_sysconst_list.xml attached to the release for Line ").append(this.RELEASE.BELONG_TO_SCHIENE.getElement().getTitle());
                RELEASE.addMarker(new Warning(getSysConstListRuleRule(), "Missing fc_sysconst_list.xml Warning", s.toString()));
                predec.add(RELEASE);
            }
            /*RULE MANAGEMENT*/

            //We have to search at the predecessor
            //this release has no fc sysconst list
            //search the list of the predecessor
            if (this.RELEASE.PREDECESSOR.getElement() != null) {
                if (this.RELEASE.PREDECESSOR.getElement().PPT_SYSTEM_CONSTANTS.getElement() != null) {
                    this.setElement(this.RELEASE.PREDECESSOR.getElement().PPT_SYSTEM_CONSTANTS.getElement().getCopy());
                    this.predec.addAll(this.RELEASE.PREDECESSOR.getElement().PPT_SYSTEM_CONSTANTS.predec);
                    if (super.getElement() != null) {
                        super.getElement().PROGRAMMSTAND_NAME.setValue(RELEASE.getTitle());
                        makeDeltaCalculation();
                    }
                }
            }
        }

        //Now there should be data availible
        if (super.getElement() != null && super.getElement().PROGRAMMSTAND_SYSTEMKONSTANTEN != null
                && !super.getElement().PROGRAMMSTAND_SYSTEMKONSTANTEN.getElementList().isEmpty()) {
            //Check if Delta Calculation should be done
            if (this.RELEASE.AUSLIEFERDATUM_SOLL.getValue().isLaterOrEqualThen(getProcessDate())) {
                //Make the Delta Calculation
                makeDeltaCalculation();
            }
        }
        return super.getElement();
    }

    public void makeDeltaCalculation() {
        //First go through the CTC Information of the Release
        Map<String, String> NameToTarget = new HashMap<>();
        Map<String, DmPPTCTC> NameToCtc = new HashMap<>();

        for (DmPPTCTC ctc : this.RELEASE.PPT_CTCs.getElementList()) {
            //Check if the ctc is from Type SY
            if (ctc.CTC_TYPE.getValueAsText().equals("SY")) {
                //Add the information to the map, if the name is not in it
                if (NameToTarget.containsKey(ctc.CTC_NAME.getValueAsText()) == false) {
                    NameToTarget.put(ctc.CTC_NAME.getValueAsText(), ctc.CTC_TARGET.getValueAsText());
                    NameToCtc.put(ctc.CTC_NAME.getValueAsText(), ctc);
                } else {

                    //The name is already in the map
                    //Save the lexiographically bigger Target Value in the Map
                    String oldValue = NameToTarget.get(ctc.CTC_NAME.getValueAsText());
                    if (ctc.CTC_TARGET.getValueAsText().compareTo(oldValue) > 0) {

                        /*RULE MANAGEMENT*/
                        StringBuilder s = new StringBuilder(40);
                        s.append("There are more than one ctc entries, which define the value of the given system constant \n Sysconst Name: ").
                                append(ctc.CTC_NAME.getValueAsText()).append(" \n Sysconst Target: ").append(ctc.CTC_TARGET.getValueAsText());
                        s.append(" First Entry: \n");
                        if (NameToCtc.get(ctc.CTC_NAME.getValueAsText()).PPT_BC.getElement() != null) {
                            s.append("CTC Links from Release to BC: ").append(NameToCtc.get(ctc.CTC_NAME.getValueAsText()).PPT_BC.getElement().getId());
                        } else if (ctc.PPT_ISSUE.getElement() != null) {
                            s.append("CTC Links from Release to Issue: ").append(NameToCtc.get(ctc.CTC_NAME.getValueAsText()).PPT_ISSUE.getElement().getId());
                        }
                        s.append(" \nSecond Entry: \n");
                        if (ctc.PPT_BC.getElement() != null) {
                            s.append("CTC Links from Release to BC: ").append(ctc.PPT_BC.getElement().getId());
                        } else if (ctc.PPT_ISSUE.getElement() != null) {
                            s.append("CTC Links from Release to Issue: ").append(ctc.PPT_ISSUE.getElement().getId());
                        }
                        RELEASE.addMarker(new Warning(getSysConstPlanningConflictListRule(), "System Constant planning conflicts Warning", s.toString()));
                        /*RULE MANAGEMENT*/

                        NameToTarget.put(ctc.CTC_NAME.getValueAsText(), ctc.CTC_TARGET.getValueAsText());
                        NameToCtc.put(ctc.CTC_NAME.getValueAsText(), ctc);
                    }
                }
            }
        }

        //The information of the current release is in the map
        //Take the information and make the delta calculation
        for (String key : NameToTarget.keySet()) {
            List<DmEcvSysConst> sysConstList = super.getElement().PROGRAMMSTAND_SYSTEMKONSTANTEN.getElementByName(key);
            if (sysConstList.isEmpty() == false) {
                //the sysConst already exists
                for (DmEcvSysConst sysConst : sysConstList) {
                    sysConst.setValue(NameToTarget.get(key));
                }
            } else {
                //the sysConst is new
                DmEcvSysConst sysConst = new DmEcvSysConst("Dm Ecv Sysconst", key, NameToTarget.get(key));
                super.getElement().PROGRAMMSTAND_SYSTEMKONSTANTEN.addElement(sysConst);
            }
        }
    }

    public boolean getSysconstListAtCurrentElement() {

        if (loadFromCurrentElementDone == false) {
            loadFromCurrentElementDone = true;

            int firsttime = 0;
            ATTACHMENT_LOOP:
            for (DmRq1Attachment att : this.RELEASE.getATTACHMENTS().getElementList()) {
                //Check the filename, if it fits to the release
                if (att.FILENAME.getValueAsText().toLowerCase().contains("fc_sysconst_list") && att.FILENAME.getValueAsText().contains(".xml")) { //Spaceholder
                    DmEcvSysConstSchieneTable schieneTable = new DmEcvSysConstSchieneTable();
                    try {
                        schieneTable.loadFromEcvXmlContainerElement(att.getFileContent());
                    } catch (Exception ex) {
                        String problem = "Error loading attachment " + RELEASE.getId() + "/" + att.FILENAME.getValueAsText();
                        LOGGER.log(Level.SEVERE, problem, ex);
                        ToolUsageLogger.logError(DmPPTField_Reference_Release_Sysconst.class.getName(), ex);
                        RELEASE.addMarker(new Warning(getSysConstListDoubleRule(), problem, ex.getMessage()));
                        continue ATTACHMENT_LOOP;
                    }
                    //in one FcBc list there is only one PST Line
                    if (schieneTable.getElements().size() > 1) {
                        String problem = "Error loading attachment " + RELEASE.getId() + "/" + att.FILENAME.getValueAsText() + ", No. Schienen = " + schieneTable.getElements().size();
                        LOGGER.log(Level.SEVERE, problem);
                        RELEASE.addMarker(new Warning(getSysConstListDoubleRule(), problem, problem));
                        continue ATTACHMENT_LOOP;
                    }
                    DmEcvSysConstSchiene schiene = schieneTable.getElements().get(0);
                    //in one FcBc Line there is only one Programming stand
                    if (schiene.SCHIENEN_PROGRAMMSTAENDE.getElementList().size() > 1) {
                        String problem = "Error loading attachment " + RELEASE.getId() + "/" + att.FILENAME.getValueAsText() + ", No. PST = " + schiene.SCHIENEN_PROGRAMMSTAENDE.getElementList().size();
                        LOGGER.log(Level.SEVERE, problem);
                        RELEASE.addMarker(new Warning(getSysConstListDoubleRule(), problem, problem));
                        continue ATTACHMENT_LOOP;
                    }
                    //If the fcbc list belongs to the line, the programmstand belongs to
                    DmPPTCustomerProject project = this.RELEASE.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement();
                    DmEcvProgrammstand_SysConst programmstand = schiene.SCHIENEN_PROGRAMMSTAENDE.getElementList().get(0);
                    String sysConstLine = project.LINE_INTERNAL_TO_EXTERNAL.get(schiene.SCHIENEN_NAME.getValueAsText());

                    if (this.RELEASE.BELONG_TO_SCHIENE.getElement().getTitle().equals(sysConstLine)) {
                        if (firsttime == 0) {
                            if (super.getElement() == null) {
                                super.setElement(programmstand);
                                super.getElement().PROGRAMMSTAND_NAME.setValue(RELEASE.getTitle());
                            }
                            firsttime = 1;
                        } else if (firsttime == 1) {
                            /*RULE MANAGEMENT*/
                            //There are more than one suitable FC BC Lists which could be taken
                            StringBuilder s = new StringBuilder(40);
                            s.append("There are more than one fc_sysconst Lists attached for the Line ").append(RELEASE.BELONG_TO_SCHIENE.getElement().getTitle());
                            RELEASE.addMarker(new Warning(getSysConstListDoubleRule(), "Double fc_sysconst_list.xml Warning", s.toString()));
                            firsttime = 2;
                            /*RULE MANAGEMENT*/
                            return true; //There has already be found one propper file
                        }
                    }
                }
            }

            listAtCurrentElementExists = (firsttime > 0);
        }

        return (listAtCurrentElementExists);
    }

    private PPTDataRule getSysConstListRuleRule() {
        if (sysConstListRule == null) {
            sysConstListRule = new PPTDataRule(sysConstListRuleDescription);
        }
        return (sysConstListRule);
    }

    private PPTDataRule getSysConstPlanningConflictListRule() {
        if (sysConstPlanningConflictListRule == null) {
            sysConstPlanningConflictListRule = new PPTDataRule(sysConstPlanningConflictRuleDescription);
        }
        return (sysConstPlanningConflictListRule);
    }

    private PPTDataRule getSysConstListDoubleRule() {
        if (sysConstListDoubleRule == null) {
            sysConstListDoubleRule = new PPTDataRule(sysConstListDoubleRuleDescription);
        }
        return (sysConstListDoubleRule);
    }
}
