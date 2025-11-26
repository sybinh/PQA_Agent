/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.Fields.DmPPTField;
import DataModel.PPT.Fields.DmPPTField_MappedIssues;
import DataModel.PPT.Fields.DmPPTField_Reference;
import DataModel.PPT.Fields.DmPPTField_Reference_Release_BC;
import DataModel.PPT.Fields.DmPPTField_Reference_Release_CTC;
import DataModel.PPT.Fields.DmPPTField_Reference_Release_InfoFcBc;
import DataModel.PPT.Fields.DmPPTField_Reference_Release_InfoSyCo;
import DataModel.PPT.Fields.DmPPTField_Reference_Release_Issues_without_BC_Releation;
import DataModel.PPT.Fields.DmPPTField_Reference_Release_PST_Release;
import DataModel.PPT.Fields.DmPPTField_Reference_Release_Planungsdaten;
import DataModel.PPT.Fields.DmPPTField_Reference_Release_Sysconst;
import DataModel.PPT.Fields.DmPPTValueField_Date;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import DataModel.PPT.Fields.DmPPTValueField_Text_Release_Hardware;
import DataModel.PPT.Fields.DmPPTValueField_Text_Release_Name;
import DataModel.PPT.Fields.DmPPTValueField_Text_Release_Programmstandskennung;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1Attachment;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1Pst;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import util.EcvAppendedData;
import util.EcvDate;

/**
 *
 * @author moe83wi
 */
public class DmPPTRelease extends DmPPTRecord implements Comparable<DmPPTRelease> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmPPTRelease.class.getCanonicalName());
    private static final java.util.logging.Logger CHANGE_LOGGER = java.util.logging.Logger.getLogger(DmPPTRelease.class.getCanonicalName() + "_Change");

    private static final EcvDate triggerDate_ECVTOOL_3783 = EcvDate.getDate(2018, 8, 1); // Date when the chnages for Auslieferstand gets effective.

    private final DmRq1Pst rq1Release;
    public final DmPPTField_Reference_Release_BC PPT_BCS;

    //Only Issues without BC connection
    public final DmPPTField_Reference_Release_Issues_without_BC_Releation PPT_ISSUES_WITHOUT_BC_RELATION;

    public final DmPPTValueField_Text_Release_Name PROGRAMMSTAND;

    public final DmPPTField_Reference_Release_InfoSyCo SYSTEMKONSTANTEN_TIDIED;
    public final DmPPTField_Reference_Release_InfoFcBc FCBC_TIDIED;

    public final DmPPTField_Reference<DmPPTLine> BELONG_TO_SCHIENE;
    /**
     * Planned Date of the Release
     */
    public final DmPPTValueField_Date AUSLIEFERDATUM_SOLL;
    public final DmPPTValueField_Text KENNUNG_META;
    public final DmPPTValueField_Text ID;
    public final DmPPTValueField_Text FILE_NAME_A2L;
    public final DmPPTValueField_Text KAUFMAENNISCH_GEPLANT;
    public final DmPPTValueField_Text_Release_Programmstandskennung PROGRAMMSTANDSKENNUNG;
    public final DmPPTValueField_Text_Release_Hardware HARDWARE;
    public final DmPPTValueField_Date ANFORDERUNGSFREEZE;
    public final DmPPTField_Reference<DmPPTRelease> PREDECESSOR;
    public final DmPPTField_Reference<DmPPTLine> META_SCHIENE;
    public final DmPPTField_Reference_Release_Planungsdaten PPT_PLANUNGSDATEN;
    public final DmPPTField_MappedIssues MAPPED_ISSUES;
    public final DmPPTField_Reference_Release_Sysconst PPT_SYSTEM_CONSTANTS;
    public final DmPPTField_Reference_Release_PST_Release PST_PROGRAMMSTAND;
    public final DmPPTField_Reference_Release_CTC PPT_CTCs;

    private List<DmPPTLine> VARIANTENBAUM_STARTING_LINES;

    public DmPPTRelease(DmRq1Pst rq1Release, DmPPTLine schiene) {
        super("ProPlaTo Programmstand");
        assert (rq1Release != null);
        assert (schiene != null);

        this.rq1Release = rq1Release;

        //Only for the tree view
        addField(BELONG_TO_SCHIENE = new DmPPTField_Reference<>(this, schiene, "ProPlaTo Schiene"));
        addField(PPT_ISSUES_WITHOUT_BC_RELATION = new DmPPTField_Reference_Release_Issues_without_BC_Releation(this, "ProPlaTo Issues ohne BC Bezug"));
        addField(PPT_BCS = new DmPPTField_Reference_Release_BC(this, "ProPlaTo BC Releases"));
        DmRq1Pst predecRelease = rq1Release.getProPlaToPredecessor(this.BELONG_TO_SCHIENE.getElement().getTitle());
        if (predecRelease != null) {
            DmPPTRelease rel = predecRelease.getPPT_Release(schiene);
            if (rel == null) {
                rel = new DmPPTRelease(predecRelease, this.BELONG_TO_SCHIENE.getElement());
                predecRelease.setPPT_Release(schiene, rel);
            }
            addField(PREDECESSOR = new DmPPTField_Reference<>(this, rel, "ProPlaTo Predecessor"));
        } else {
            addField(PREDECESSOR = new DmPPTField_Reference<>(this, null, "ProPlaTo Predecessor"));
        }
        addField(PPT_PLANUNGSDATEN = new DmPPTField_Reference_Release_Planungsdaten(this, "ProPlaTo Planungsdaten"));
        addField(MAPPED_ISSUES = new DmPPTField_MappedIssues(this, "ProPlaTo Release Issue"));
        addField(PST_PROGRAMMSTAND = new DmPPTField_Reference_Release_PST_Release(this, "ProPlaTo FcBc Programmstand"));
        if (rq1Release.getMetaSchiene() != null) { // If there is a META Line
            //Check if the line exists in the cluster
            DmPPTLine lineToTake = null;
            for (DmPPTLine line : this.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_LINES.getElementList()) {
                if (line.getTitle().equals(rq1Release.getMetaSchiene())) {
                    lineToTake = line;
                }
            }
            addField(META_SCHIENE = new DmPPTField_Reference<>(this, lineToTake, "ProPlaTo META Schiene"));
        } else {
            addField(META_SCHIENE = new DmPPTField_Reference<>(this, null, "ProPlaTo META Schiene"));
        }

        //Tidied
        addField(SYSTEMKONSTANTEN_TIDIED = new DmPPTField_Reference_Release_InfoSyCo(this, "ProPlaTo Systemkonstanten tidied"));
        addField(FCBC_TIDIED = new DmPPTField_Reference_Release_InfoFcBc(this, "ProPlaTo FCBC tidied"));

        //ProPlaTo View 
        addField(ID = new DmPPTValueField_Text(this, rq1Release.getRq1Id(), "ProPlaTo: <ID> "));
        addField(PROGRAMMSTAND = new DmPPTValueField_Text_Release_Name(this, "ProPlaTo: <Name> "));
        EcvDate date;
        if (isLumpensammler()) {
            date = EcvDate.getDate(2099, 12, 31);
        } else {
            date = rq1Release.getPlannedDateForPstLineByExternalName(this.BELONG_TO_SCHIENE.getElement().getTitle());
        }
        addField(AUSLIEFERDATUM_SOLL = new DmPPTValueField_Date(this, date, "ProPlaTo: <AuslieferdatumSoll> "));
        addField(FILE_NAME_A2L = new DmPPTValueField_Text(this, "", "ProPlaTo: <FileNameA2L>"));
        addField(KENNUNG_META = new DmPPTValueField_Text(this, Boolean.toString(rq1Release.getProPlaToKennungMeta(this.BELONG_TO_SCHIENE.getElement().getTitle())), "ProPlaTo: <KennungMETA>"));
        addField(KAUFMAENNISCH_GEPLANT = new DmPPTValueField_Text(this, "true", "ProPlaTo: <KaufmaennischGeplant>"));
        addField(ANFORDERUNGSFREEZE = new DmPPTValueField_Date(this, rq1Release.SPECIFICATION_FREEZE.getValue(), "ProPlaTo: <Anforderungsfreeze>"));
        addField(PROGRAMMSTANDSKENNUNG = new DmPPTValueField_Text_Release_Programmstandskennung(this, "ProPlaTo: <Programmstandskennung>"));
        addField(HARDWARE = new DmPPTValueField_Text_Release_Hardware(this, "ProPlaTo: <Hardware>"));

        //For the future
        addField(PPT_SYSTEM_CONSTANTS = new DmPPTField_Reference_Release_Sysconst(this, "ProPlaTo Systemconstants"));
        addField(PPT_CTCs = new DmPPTField_Reference_Release_CTC(this, "ProPlaTo CTCs"));
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCanceled() {
        return rq1Release.isCanceled();
    }

    @Override
    public String getTitle() {
        return this.PROGRAMMSTAND.getValueAsText();
    }

    @Override
    public String getId() {
        return rq1Release.getId();
    }

    public DmRq1Field_MappedReferenceList<DmRq1Irm, DmRq1Issue> getMAPPED_ISSUES() {
        return rq1Release.MAPPED_ISSUES;
    }

    public DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Release> getMAPPED_CHILDREN() {
        return rq1Release.MAPPED_CHILDREN;
    }

    public Map<String, String> getHardwareInformationProPlaTo() {
        return rq1Release.getHardwareInformationProPlaTo();
    }

    private boolean isLumpensammler() {
        if (rq1Release.getTitle().toLowerCase().endsWith("zzz")) {
            return (true);
        } else {
            return (false);
        }
    }

    private EcvDate getAuslieferdatumSoll() {
        return (AUSLIEFERDATUM_SOLL.getValue());
    }

    private EcvDate getAblieferstandDatum() {

        if (getAuslieferdatumSoll().isLaterThen(DmPPTField.getProcessDate())) {
            return (EcvDate.getEmpty());
        }

        EcvDate ablieferstandDatum = rq1Release.PROPLATO_ABLIEFERSTAND_DATUM.getValue();
        if ((ablieferstandDatum != null) && (ablieferstandDatum.isEmpty() == false)) {
            return (ablieferstandDatum);
        }

        if (isDeliveryDataSet() == true) {
            rq1Release.PROPLATO_ABLIEFERSTAND_DATUM.setValue(EcvDate.getToday());
            rq1Release.save();
            CHANGE_LOGGER.info(rq1Release.getTypeIdTitle() + " : " + rq1Release.PROPLATO_ABLIEFERSTAND_DATUM.getValue().getXmlValue());
            return (rq1Release.PROPLATO_ABLIEFERSTAND_DATUM.getValue());
        }

        CHANGE_LOGGER.warning(rq1Release.getTypeIdTitle() + " : Auslieferdatum soll reached but delivery data not set.");
        return (EcvDate.getEmpty());
    }

    private boolean isDeliveryDataSet() {
        if (PST_PROGRAMMSTAND.loadLocalFcBcList() == false) {
            return (false);
        }
        if (PPT_SYSTEM_CONSTANTS.getSysconstListAtCurrentElement() == false) {
            return (false);
        }
        return (true);
    }

    final public EcvDate getAuslieferungsdatumIst() {

        if (getAuslieferdatumSoll().isEarlierThen(triggerDate_ECVTOOL_3783)) {

            if (getAuslieferdatumSoll().isEarlierOrEqualThen(DmPPTField.getProcessDate())) {
                return (getAuslieferdatumSoll());
            } else {
                return (EcvDate.getEmpty());
            }

        } else {

            if (getAblieferstandDatum().isEmpty()) {
                return (EcvDate.getEmpty());
            } else {
                return (getAuslieferdatumSoll());
            }

        }
    }

    final public boolean isAblieferungsstand() {

        if (getAuslieferdatumSoll().isEarlierThen(triggerDate_ECVTOOL_3783)) {

            return (getAuslieferdatumSoll().isEarlierOrEqualThen(DmPPTField.getProcessDate()));

        } else {

            return (getAblieferstandDatum().isEmpty() == false);

        }
    }

    final public boolean exportPlanungsdaten() {

        if (getAuslieferdatumSoll().isEarlierThen(triggerDate_ECVTOOL_3783)) {

            return (getAuslieferdatumSoll().isLaterOrEqualThen(DmPPTField.getProcessDate().addDays(-6)));

        } else {

            if (getAblieferstandDatum().isEmpty()) {
                return (true);
            } else {
                return (getAblieferstandDatum().isLaterOrEqualThen(DmPPTField.getProcessDate().addDays(-6)));
            }

        }
    }

    public DmRq1Field_ReferenceList<DmRq1Attachment> getATTACHMENTS() {
        return rq1Release.ATTACHMENTS;
    }

    public boolean areDerivativesRelevant() {
        return rq1Release.areDerivativesRelevant();
    }

    @Override
    public EcvAppendedData getEcvAppendedData() {
        Map<String, String> appendedData = new HashMap<>();

        try {
            if (BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement() != null
                    && BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_CLUSTER.getElement() != null) {
                appendedData.put("Cluster", BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().PPT_CLUSTER.getElement().getTitle());
            } else {
                appendedData.put("Cluster", "");
            }
            appendedData.put("Schiene", BELONG_TO_SCHIENE.getElement().getTitle());
            appendedData.put("Projekt_ID", BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getId());
            appendedData.put("Project_Leader", BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().RQ1_CUSTOMER_PROJECT.getElement().PROJECT_LEADER_FULLNAME.getValueAsText());
            appendedData.put("Release_ID", this.getId());
            appendedData.put("Release_Title", this.getTitle());
            appendedData.put("Release_Lable", this.PROGRAMMSTANDSKENNUNG.getValueAsText());
            appendedData.put("Release_Assignee", rq1Release.ASSIGNEE_FULLNAME.getValueAsText());
            appendedData.put("Release_PlannedDate", this.AUSLIEFERDATUM_SOLL.getValue().getXmlValue());
            if (this.PREDECESSOR.getElement() != null) {
                appendedData.put("Release_Predecessor", this.PREDECESSOR.getElement().getId());
                appendedData.put("Release_Predecessor_PlannedDate", this.PREDECESSOR.getElement().AUSLIEFERDATUM_SOLL.getValue().getXmlValue());
            }
        } catch (NullPointerException ex) {
            logger.warning("A Nullpointer exception happend during calculating the ECVAppended Data of Release: " + this.getId());
        }
        return new EcvAppendedData(appendedData);
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == this.getClass()) {
            DmPPTRelease rel = (DmPPTRelease) o;
            return (rel.PROGRAMMSTAND.getValueAsText().equals(this.PROGRAMMSTAND.getValueAsText()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.PROGRAMMSTAND.getValueAsText());
        hash = 59 * hash + Objects.hashCode(this.ID.getValueAsText());
        return hash;
    }

    final public String getLineName() {
        return (BELONG_TO_SCHIENE.getElement().getTitle());
    }

    final public DmPPTLine getLine() {
        return (BELONG_TO_SCHIENE.getElement());
    }

    final public DmRq1Pst getRq1Release() {
        return (rq1Release);
    }

    @Override
    public String toString() {
        return (getIdTitle());
    }

    @Override
    public int compareTo(DmPPTRelease o) {
        assert (o != null);
        return (PROGRAMMSTAND.getValue().compareTo(o.PROGRAMMSTAND.getValue()));
    }

}
