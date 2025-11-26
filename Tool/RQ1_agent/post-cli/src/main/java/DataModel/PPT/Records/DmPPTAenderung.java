/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.Fields.DmPPTField_FinalReference;
import DataModel.PPT.Fields.DmPPTValueField;
import DataModel.PPT.Fields.DmPPTValueField_Boolean_ISSUE_ASAM;
import DataModel.PPT.Fields.DmPPTValueField_Boolean_ISSUE_Hexneutral;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_Anforderer;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_AnfordererMarke;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_AnfordererOE;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_Description;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_Einplanungsstatus;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_IssueComment;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_OEMHerkunftMarke;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_OEMHerkunftName;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_OEMHerkunftOE;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_ProjektverantwortlicherAnforderer;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_ProjektverantwortlicherAnfordererAbteilung;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_ProjektverantwortlicherAnfordererMarke;
import DataModel.PPT.Fields.DmPPTValueField_Text_Issue_Vorgangstyp;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Leaf;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Pool;
import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Cache.Types.Rq1XmlTable_Enumeration;
import Rq1Data.Enumerations.LifeCycleState_IRM;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import util.EcvAppendedData;
import util.EcvDate;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTAenderung extends DmPPTRecord {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmPPTAenderung.class.getCanonicalName());

    /*RULE MANAGEMENT*/
    static final public RuleDescription hauptEntwicklungsPaketRuleDescription = new RuleDescription(
            EnumSet.of(RuleExecutionGroup.PROPLATO),
            "Hauptentwicklungspaket set, which couldn't be converted into number",
            "There is a \"Hauptentwicklungspaket\" set, which couldn't be converted\n"
            + "into a number, over the conversion table in Tags in the Pool Project");
    private PPTDataRule hauptEntwicklungsPaketRule = null;

    public final DmPPTField_FinalReference<DmRq1IssueSW> RQ1_ISSUE_SW;
    public final DmPPTField_FinalReference<DmPPTBCRelease> PPT_BC;
    public final DmPPTField_FinalReference<DmPPTFCRelease> PPT_FC;
    public final DmPPTField_FinalReference<DmPPTRelease> PPT_RELEASE;
    public final DmPPTValueField_Text PPT_EXTERNAL_ID;
    public final DmPPTValueField_Text PPT_RCMS_OPL;
    public final DmPPTValueField_Text_Issue_Description PPT_DESCRIPTION;
    public final DmPPTValueField_Text_Issue_Anforderer PPT_ANFORDERER;
    public final DmPPTValueField_Text_Issue_AnfordererOE PPT_ANFORDERER_OE;
    public final DmPPTValueField_Text_Issue_AnfordererMarke PPT_ANFORDERER_MARKE;
    public final DmPPTValueField_Boolean_ISSUE_ASAM PPT_ASAM_ISSUE;
    public final DmPPTValueField_Text_Issue_OEMHerkunftName PPT_OEM_HERKUNFT_NAME;
    public final DmPPTValueField_Text_Issue_OEMHerkunftOE PPT_OEM_HERKUNFT_OE;
    public final DmPPTValueField_Text_Issue_OEMHerkunftMarke PPT_OEM_HERKUNFT_MARKE;

    public final DmPPTValueField_Text_Issue_ProjektverantwortlicherAnforderer PPT_PROJEKTVERANTWORTLICHER_ANFORDERER;
    public final DmPPTValueField_Text_Issue_ProjektverantwortlicherAnfordererAbteilung PPT_PROJEKTVERANTWORTLICHER_ANFORDERER_ABTEILUNG;
    public final DmPPTValueField_Text_Issue_ProjektverantwortlicherAnfordererMarke PPT_PROJEKTVERANTWORTLICHER_ANFORDERER_MARKE;

    public final DmPPTValueField<EcvDate> PPT_SUBMITDATE;
    public final DmPPTValueField_Text PPT_AFFECTED_ISSUE;
    public final DmPPTValueField_Text PPT_ISSUE_FD_PLANUNGSDATEN;
    public final List<String> PPT_ISSUE_FD_SPLITS;
    public final DmPPTValueField_Text_Issue_Einplanungsstatus PPT_RB_EINPLANUNGSSTATUS;
    public final DmPPTValueField_Text_Issue_IssueComment PPT_ISSUE_COMMENT;
    //Only avaylible if there is an IRM to the PVER
    public final DmPPTValueField_Text PPT_IRM_TO_RELEASE_CTC;
    public final DmPPTValueField_Text PPT_IRM_TO_RELEASE_RB_COMMENT;
    public final DmPPTValueField_Text PPT_RRM_RELEASE_BC_CTC;
    public final DmPPTValueField_Text PPT_RRM_RELEASE_BC_RB_COMMENT;
    public final DmPPTValueField_Text PPT_RRM_RELEASE_BC_CTP;
    public final DmPPTValueField_Text PPT_RRM_RELEASE_BC_CTI;
    private final DmPPTValueField_Text PPT_ID;
    public final DmPPTValueField_Text PPT_TITLE;

    public final DmPPTValueField_Text PPT_HAUPTENTWICKLUNGSPAKET;

    public final DmPPTValueField_Text PPT_BESONDERESMERKMAL_ANFORDERUNGSBEWERTUNG_S;
    public final DmPPTValueField_Text PPT_BESONDERESMERKMAL_KOMMENTAR_SICHERHEITSRELEVANT;
    public final DmPPTValueField_Text PPT_BESONDERESMERKMAL_ANFORDERUNGSBEWERTUNG_O;
    public final DmPPTValueField_Text PPT_BESONDERESMERKMAL_KOMMENTAR_OBD_RELEVANT;
    public final DmPPTValueField_Text PPT_BESONDERESMERKMAL_ANFORDERUNGSBEWERTUNG_D;
    public final DmPPTValueField_Text PPT_BESONDERESMERKMAL_KOMMENTAR_DOKUMENTATIONSPFLICHTIG;
    //Save the Life Cycle state of the IRM, if there is one;
    public final DmRq1Irm IRM;
    public final LifeCycleState_IRM LCS_IRM;

    public final DmPPTValueField_Boolean_ISSUE_Hexneutral PPT_HEXNEUTRAL;

    public final DmPPTValueField_Text_Issue_Vorgangstyp PPT_VORGANGSTYP;

    protected DmPPTAenderung(DmRq1IssueSW rq1IssueSW, DmPPTBCRelease bc, DmPPTFCRelease fc, DmPPTRelease release, DmRq1Irm irm, String rbComment, String ctc, String issueFdRq1) {
        super("Data Model Pro Pla To Issue Software");
        IRM = irm;
        if (irm != null) {
            LCS_IRM = (LifeCycleState_IRM) irm.LIFE_CYCLE_STATE.getValue();
        } else {
            LCS_IRM = null;
        }
        //tree view

        addField(RQ1_ISSUE_SW = new DmPPTField_FinalReference<>(this, rq1IssueSW, "RQ1 Issue SW"));
        addField(PPT_BC = new DmPPTField_FinalReference<>(this, bc, "PPT BC"));
        addField(PPT_RELEASE = new DmPPTField_FinalReference<>(this, release, "PPT Release"));
        addField(PPT_FC = new DmPPTField_FinalReference<>(this, fc, "PPT FC"));

        //ProPlaToView
        addField(PPT_ID = new DmPPTValueField_Text(this, this.getId(), "ProPlaTo: <VorgangsnummerLieferant>"));
        addField(PPT_TITLE = new DmPPTValueField_Text(this, this.getTitle(), "ProPlaTo: <Titel>"));
        addField(PPT_ISSUE_FD_PLANUNGSDATEN = new DmPPTValueField_Text(this, issueFdRq1, "ProPlaTo PD: <IssueFD>"));

        addField(PPT_ANFORDERER = new DmPPTValueField_Text_Issue_Anforderer(this, rq1IssueSW, bc, fc, "ProPlaTo: <Anforderer>"));
        addField(PPT_ANFORDERER_OE = new DmPPTValueField_Text_Issue_AnfordererOE(this, rq1IssueSW, bc, fc, "ProPlaTo: <AnfordererOE>"));
        addField(PPT_ANFORDERER_MARKE = new DmPPTValueField_Text_Issue_AnfordererMarke(this, rq1IssueSW, bc, fc, "ProPlaTo: <AnfordererMarke>"));
        addField(PPT_ASAM_ISSUE = new DmPPTValueField_Boolean_ISSUE_ASAM(this, "ProPlaTo: <ASAMIssue>"));
        addField(PPT_OEM_HERKUNFT_NAME = new DmPPTValueField_Text_Issue_OEMHerkunftName(this, rq1IssueSW, bc, fc, "ProPlaTo: <OEMHerkunftName>"));
        addField(PPT_OEM_HERKUNFT_OE = new DmPPTValueField_Text_Issue_OEMHerkunftOE(this, rq1IssueSW, bc, fc, "ProPlaTo: <OEMHerkunftOE>"));
        addField(PPT_OEM_HERKUNFT_MARKE = new DmPPTValueField_Text_Issue_OEMHerkunftMarke(this, rq1IssueSW, bc, fc, "ProPlaTo: <OEMHerkunftMarke>"));
        addField(PPT_RB_EINPLANUNGSSTATUS = new DmPPTValueField_Text_Issue_Einplanungsstatus(this, "ProPlaTo PD: <Einplanungsstatus>"));
        addField(PPT_DESCRIPTION = new DmPPTValueField_Text_Issue_Description(this, rq1IssueSW, bc, fc, "ProPlaTo: <Aenderungsbeschreibung>"));

        addField(PPT_PROJEKTVERANTWORTLICHER_ANFORDERER = new DmPPTValueField_Text_Issue_ProjektverantwortlicherAnforderer(this, "ProPlaTo: <ProjektverantwortlicherAnforderer>"));
        addField(PPT_PROJEKTVERANTWORTLICHER_ANFORDERER_ABTEILUNG = new DmPPTValueField_Text_Issue_ProjektverantwortlicherAnfordererAbteilung(this, "ProPlaTo: <ProjektverantwortlicherAnfordererMarke>"));
        addField(PPT_PROJEKTVERANTWORTLICHER_ANFORDERER_MARKE = new DmPPTValueField_Text_Issue_ProjektverantwortlicherAnfordererMarke(this, "ProPlaTo: <ProjektverantwortlicherAnfordererAbteilung>"));

        addField(PPT_VORGANGSTYP = new DmPPTValueField_Text_Issue_Vorgangstyp(this, ctc, "ProPlaTo: <Vorgangstyp>"));

        addField(PPT_HEXNEUTRAL = new DmPPTValueField_Boolean_ISSUE_Hexneutral(this, issueFdRq1, "ProPlaTo PD: <Hexneutral>"));

        PPT_ISSUE_FD_SPLITS = new LinkedList<>();
        if (rq1IssueSW != null) {
            String affectedIssue = "";
            if (rq1IssueSW.AFFECTED_ISSUE.getElement() != null) {
                affectedIssue = rq1IssueSW.AFFECTED_ISSUE.getElement().getId();
            }
            addField(PPT_AFFECTED_ISSUE = new DmPPTValueField_Text(this, affectedIssue, "ProPlaTo: <VorgangsnummerLieferantKorrektur>"));
            addField(PPT_EXTERNAL_ID = new DmPPTValueField_Text(this, rq1IssueSW.EXTERNAL_ID.getValueAsText(), "ProPlaTo: <VorgangsnummerOEM>"));
            addField(PPT_RCMS_OPL = new DmPPTValueField_Text(this, rq1IssueSW.RCMS_OPL.getValueAsText(), "ProPlaTo: <VorgangsnummerFP>"));

            addField(PPT_HAUPTENTWICKLUNGSPAKET = new DmPPTValueField_Text(this, getIdOfHauptentwicklungsPaket(rq1IssueSW), "ProPlaTo: <Hauptentwicklungspaket>"));

            //First take the external Submitter
            addField(PPT_SUBMITDATE = new DmPPTValueField<>(this, rq1IssueSW.SUBMIT_DATE.getValue(), "ProPlaTo: <EingereichtAm>"));
            for (DmRq1IssueFD issueFD : this.RQ1_ISSUE_SW.getElement().CHILDREN.getElementList()) {
                //nur Falls issue FD != canceled
                if (!issueFD.isCanceled()) {
                    PPT_ISSUE_FD_SPLITS.add(issueFD.getId());
                }
            }

            addField(PPT_BESONDERESMERKMAL_ANFORDERUNGSBEWERTUNG_S = new DmPPTValueField_Text(this, rq1IssueSW.BESONDERE_MERKMALE_OEM_S.getValueAsText(), "ProPlaTo: <A-BesonderesMerkmal-S>"));
            addField(PPT_BESONDERESMERKMAL_KOMMENTAR_SICHERHEITSRELEVANT = new DmPPTValueField_Text(this, rq1IssueSW.BESONDERE_MERKMALE_OEM_S_KOMMENTAR.getValueAsText(), "ProPlaTo: <A-BesonderesMerkmal-S-Kommentar>"));
            addField(PPT_BESONDERESMERKMAL_ANFORDERUNGSBEWERTUNG_O = new DmPPTValueField_Text(this, rq1IssueSW.BESONDERE_MERKMALE_OEM_O.getValueAsText(), "ProPlaTo: <A-BesonderesMerkmal-O>"));
            addField(PPT_BESONDERESMERKMAL_KOMMENTAR_OBD_RELEVANT = new DmPPTValueField_Text(this, rq1IssueSW.BESONDERE_MERKMALE_OEM_O_KOMMENTAR.getValueAsText(), "ProPlaTo: <A-BesonderesMerkmal-O-Kommentar>"));
            addField(PPT_BESONDERESMERKMAL_ANFORDERUNGSBEWERTUNG_D = new DmPPTValueField_Text(this, rq1IssueSW.BESONDERE_MERKMALE_OEM_D.getValueAsText(), "ProPlaTo: <A-BesonderesMerkmal-D>"));
            addField(PPT_BESONDERESMERKMAL_KOMMENTAR_DOKUMENTATIONSPFLICHTIG = new DmPPTValueField_Text(this, rq1IssueSW.BESONDERE_MERKMALE_OEM_D_KOMMENTAR.getValueAsText(), "ProPlaTo: <A-BesonderesMerkmal-D-Kommentar>"));

        } else {
            addField(PPT_AFFECTED_ISSUE = new DmPPTValueField_Text(this, "", "ProPlaTo: <VorgangsnummerLieferantKorrektur>"));
            addField(PPT_EXTERNAL_ID = new DmPPTValueField_Text(this, "", "ProPlaTo: <VorgangsnummerOEM>"));
            addField(PPT_RCMS_OPL = new DmPPTValueField_Text(this, "", "ProPlaTo: <VorgangsnummerFP>"));
            addField(PPT_HAUPTENTWICKLUNGSPAKET = new DmPPTValueField_Text(this, "", "ProPlaTo: <Hauptentwicklungspaket>"));
            addField(PPT_SUBMITDATE = new DmPPTValueField<>(this, EcvDate.getEmpty(), "ProPlaTo: <EingereichtAm>"));
            if (this.PPT_BC.getElement() != null) {
                PPT_SUBMITDATE.setValue(this.PPT_BC.getElement().BC_SUBMIT_DATE.getValue());
            } else if (this.PPT_FC.getElement() != null) {
                PPT_SUBMITDATE.setValue(this.PPT_FC.getElement().FC_SUBMIT_DATE.getValue());
            }

            addField(PPT_BESONDERESMERKMAL_ANFORDERUNGSBEWERTUNG_S = new DmPPTValueField_Text(this, "", "ProPlaTo: <A-BesonderesMerkmal-S>"));
            addField(PPT_BESONDERESMERKMAL_KOMMENTAR_SICHERHEITSRELEVANT = new DmPPTValueField_Text(this, "", "ProPlaTo: <A-BesonderesMerkmal-S-Kommentar>"));
            addField(PPT_BESONDERESMERKMAL_ANFORDERUNGSBEWERTUNG_O = new DmPPTValueField_Text(this, "", "ProPlaTo: <A-BesonderesMerkmal-O>"));
            addField(PPT_BESONDERESMERKMAL_KOMMENTAR_OBD_RELEVANT = new DmPPTValueField_Text(this, "", "ProPlaTo: <A-BesonderesMerkmal-O-Kommentar>"));
            addField(PPT_BESONDERESMERKMAL_ANFORDERUNGSBEWERTUNG_D = new DmPPTValueField_Text(this, "", "ProPlaTo: <A-BesonderesMerkmal-D>"));
            addField(PPT_BESONDERESMERKMAL_KOMMENTAR_DOKUMENTATIONSPFLICHTIG = new DmPPTValueField_Text(this, "", "ProPlaTo: <A-BesonderesMerkmal-D-Kommentar>"));

        }
        //Internal View
        //Only for initalisation
        addField(PPT_RRM_RELEASE_BC_CTC = new DmPPTValueField_Text(this, "", "Internal RRM Release BC CTC"));
        addField(PPT_RRM_RELEASE_BC_RB_COMMENT = new DmPPTValueField_Text(this, "", "Internal RRM Release BC RB Comment"));
        addField(PPT_RRM_RELEASE_BC_CTP = new DmPPTValueField_Text(this, "", "Internal RRM Release BC FC specific CTP"));
        addField(PPT_RRM_RELEASE_BC_CTI = new DmPPTValueField_Text(this, "", "Internal RRM Release BC Issue specific CTI"));
        addField(PPT_IRM_TO_RELEASE_CTC = new DmPPTValueField_Text(this, ctc, "Internal IRM To Release CTC"));
        addField(PPT_IRM_TO_RELEASE_RB_COMMENT = new DmPPTValueField_Text(this, rbComment, "Internal IRM To Release RB Comment"));

        addField(PPT_ISSUE_COMMENT = new DmPPTValueField_Text_Issue_IssueComment(this, ctc, rbComment, "ProPlaTo PD: <IssueKommentar>"));
    }

    public static String getTheAnfordererOE(String anforderer) {
        try {
            String company;
            //First delelte the FixedTerm
            anforderer = anforderer.replace("FIXED-TERM", "");
            //Second delete the )
            anforderer = anforderer.replace(")", "");
            //Split the anforderer by (
            String[] anfArr = anforderer.split("[(]");
            //Check the cases
            if (anfArr.length > 1) {
                if (anfArr[1].contains(" ") && anfArr[1].contains(",") && anfArr[1].contains("-/")) {
                    company = anfArr[1].split("[ ]")[1];
                } else if (anfArr[1].contains(",")) {
                    company = anfArr[1].split("[,]")[1];
                } else {
                    company = anfArr[1];
                }
                return company.replace(" ", "");
            } else {
                return "unbekannt";
            }
        } catch (Exception e) {
            return "unbekannt";
        }
    }

    public static String getTheAnfordererName(String anforderer) {
        try {
            //First delelte the FixedTerm
            anforderer = anforderer.replace("FIXED-TERM", "");
            //Second delete the )
            anforderer = anforderer.replace(")", "");
            //Split the anforderer by (
            String[] anfArr = new String[0];
            if (anforderer.contains(" (")) {
                anfArr = anforderer.split("( \\()");
            } else if (anforderer.contains("(")) {
                anfArr = anforderer.split("\\(");
            }
            if (anfArr.length == 0) {
                return "unbekannt";
            }
            return anfArr[0];
        } catch (Exception e) {
            return "unbekannt";
        }
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCanceled() {
        if (this.RQ1_ISSUE_SW.getElement() != null) {
            return this.RQ1_ISSUE_SW.getElement().isCanceled();
        } else {
            //Dummy Issue
            return false;
        }
    }

    @Override
    public final String getTitle() {
        if (this.RQ1_ISSUE_SW.getElement() != null) {
            return this.RQ1_ISSUE_SW.getElement().getTitle();
        } else {
            if (this.PPT_BC.getElement() != null) {
                return "Plattformübernahme von BC : " + this.PPT_BC.getElement().getName() + " / " + this.PPT_BC.getElement().getVersion();
            } else if (this.PPT_FC.getElement() != null) {
                if (this.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement() != null) {
                    return "Plattformübernahme von BC : " + this.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().getName() + " / "
                            + this.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().getVersion();
                } else {
                    return "Plattformübernahme von FC : " + this.PPT_FC.getElement().getName() + " / " + this.PPT_FC.getElement().getVersion();
                }
            }
            return "unbekannt";
        }
    }

    @Override
    public final String getId() {
        if (this.RQ1_ISSUE_SW.getElement() != null) {
            return this.RQ1_ISSUE_SW.getElement().getId();
        } else {
            //Dummy Issue
            //If there is a dummy issue it has to be from an BC or FC
            assert (this.PPT_BC.getElement() != null || this.PPT_FC.getElement() != null);

            if (this.PPT_BC.getElement() != null) {
                return this.PPT_BC.getElement().getId();
            }
            if (this.PPT_FC.getElement() != null) {
                if (this.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement() != null) {
                    return this.PPT_FC.getElement().HAS_PARENT_RELEASE.getElement().getId();
                } else {
                    return this.PPT_FC.getElement().getId();
                }
            }
            return "";
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof DmPPTAenderung == false) {
            return false;
        }
        return (getId().equals(((DmPPTAenderung) obj).getId()));
    }

    //Checks, if the given issue is a Member of the given Pool, or of the Member Projects of the given Pool Project
    public static boolean belongsToPool(DmRq1IssueSW rq1Issue, DmPPTPoolProject pool) {
        if (rq1Issue.PROJECT.getElement() instanceof DmRq1SwCustomerProject_Pool) {
            DmPPTPoolProject rq1Pool = new DmPPTPoolProject((DmRq1SwCustomerProject_Pool) rq1Issue.PROJECT.getElement());
            if (pool.equals(rq1Pool)) {
                return true;
            }
        } else if (rq1Issue.PROJECT.getElement() instanceof DmRq1SwCustomerProject_Leaf) {
            DmPPTCustomerProject custProj = new DmPPTCustomerProject((DmRq1SwCustomerProject_Leaf) rq1Issue.PROJECT.getElement(), pool);
            if (pool.PPT_CUSTOMER_PROJECT_MEMBERS.contains(custProj)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EcvAppendedData getEcvAppendedData() {
        Map<String, String> appendedData = new HashMap<>();
        String projectID = "", projectLeader = "", issueAssignee = "";

        try {
            if (this.PPT_RELEASE.getElement() != null) {
                appendedData.put("Release_ID", this.PPT_RELEASE.getElement().getId());
                appendedData.put("Release_Title", this.PPT_RELEASE.getElement().getTitle());
            }
            if (this.RQ1_ISSUE_SW.getElement() != null) { //Real Issue SW
                projectID = this.RQ1_ISSUE_SW.getElement().PROJECT.getElement().getId();
                projectLeader = this.RQ1_ISSUE_SW.getElement().PROJECT.getElement().PROJECT_LEADER.getElement().getTitle();
                issueAssignee = this.RQ1_ISSUE_SW.getElement().ASSIGNEE_FULLNAME.getValueAsText();
            } else if (this.PPT_FC.getElement() != null) { // Dummy FC Issue
                projectID = this.PPT_FC.getElement().getRelease().PROJECT.getElement().getId();
                projectLeader = this.PPT_FC.getElement().getRelease().PROJECT.getElement().PROJECT_LEADER.getElement().getTitle();
                issueAssignee = this.PPT_FC.getElement().getRelease().ASSIGNEE_FULLNAME.getValueAsText();
            } else if (this.PPT_BC.getElement() != null) {
                projectID = this.PPT_BC.getElement().getRelease().PROJECT.getElement().getId();
                projectLeader = this.PPT_BC.getElement().getRelease().PROJECT.getElement().PROJECT_LEADER.getElement().getTitle();
                issueAssignee = this.PPT_BC.getElement().getRelease().ASSIGNEE_FULLNAME.getValueAsText();
            }
        } catch (NullPointerException ex) {
            logger.warning("A Null pointer exception occured while calculating the ecvAppended Data of RQ1: " + this.getId());
        }

        appendedData.put("Projekt_ID", projectID);
        appendedData.put("Project_Leader", projectLeader);
        appendedData.put("Issue_ID", this.getId());
        appendedData.put("Issue_Assignee", issueAssignee);
        return new EcvAppendedData(appendedData);
    }

    private String getMarkderDetailInformation() {
        String details = "";
        if (this.PPT_FC.getElement() != null) { // Dummy FC Issue
            details += " Issue belonging to FC: " + this.PPT_FC.getElement().getId();
        } else if (this.PPT_BC.getElement() != null) {
            details += " Issue belonging to BC: " + this.PPT_BC.getElement().getId();
        }
        return details;
    }

    private String getIdOfHauptentwicklungsPaket(DmRq1IssueSW rq1IssueSW) {
        assert (rq1IssueSW != null);

        String hauptEntwicklungsPaket = rq1IssueSW.HAUPTENTWICKLUNGSPAKET.getValueAsText();

        if (hauptEntwicklungsPaket.isEmpty() == true) {
            return ("");
        }

        int hEP = -1;
        //Find the Pool Project, where the conversion Table of the Hauptentwicklungspakete is given
        DmRq1SwCustomerProject_Pool pool = null;
        if (rq1IssueSW.PROJECT.getElement().POOL_PROJECT.getElement() instanceof DmRq1SwCustomerProject_Pool) {
            pool = (DmRq1SwCustomerProject_Pool) rq1IssueSW.PROJECT.getElement().POOL_PROJECT.getElement();
        } else if (rq1IssueSW.PROJECT.getElement() instanceof DmRq1SwCustomerProject_Pool) {
            pool = (DmRq1SwCustomerProject_Pool) rq1IssueSW.PROJECT.getElement();
        }

        //if the Poolproject has been found, the conversion can be made
        if (pool != null) {
            if (pool.HAUPT_ENTWICKLUNGSPAKETE != null) {
                EcvTableData data = pool.HAUPT_ENTWICKLUNGSPAKETE.getValue();
                Rq1XmlTable_Enumeration desc = pool.HAUPT_ENTWICKLUNGSPAKETE.getTableDescription();
                for (EcvTableRow row : data.getRows()) {
                    if (row.getValueAt(desc.NAME).toString().toLowerCase().equals(hauptEntwicklungsPaket.toLowerCase())) {
                        if (row.getValueAt(desc.ID) != null) {
                            try {
                                hEP = Integer.parseInt(row.getValueAt(desc.ID).toString());
                            } catch (Exception ex) {
                                logger.warning("Unknown value for Hauptentwicklungspaket: >" + hauptEntwicklungsPaket + "<. I-SW: " + rq1IssueSW.getRq1Id());
                            }
                        }
                    }
                }
            }
        }

        if (hEP != -1) {//If the conversion worked
            return (String.valueOf(hEP));
        } else { // if not give a warning, if the value is given
            if (hauptEntwicklungsPaket != null && hauptEntwicklungsPaket.isEmpty() == false) {
                //WARNING IN REPORT => KEINE KONVERTIERUNG möglich
                //TODO
                StringBuilder s = new StringBuilder(40);
                s.append("The given \"Hauptentwicklungspaket\" couldn't be converted into an int Number \nThe given value: ").append(hauptEntwicklungsPaket);
                if (this.getMarkderDetailInformation().isEmpty() == false) {
                    s.append("\nIssue detail Information: ");
                    s.append(this.getMarkderDetailInformation());
                }
                this.addMarker(new Warning(this.getHauptEntwicklungsPaketRule(), "Hauptentwicklungspaket set, which couldn't be converted into number Warning", s.toString()));
            }
            return ("");
        }
    }

    private PPTDataRule getHauptEntwicklungsPaketRule() {
        if (hauptEntwicklungsPaketRule == null) {
            hauptEntwicklungsPaketRule = new PPTDataRule(hauptEntwicklungsPaketRuleDescription);
        }
        return (hauptEntwicklungsPaketRule);
    }

    @Override
    public String toString() {
        return (this.getClass().getSimpleName() + ": " + getId() + " - " + getTitle());
    }

}
