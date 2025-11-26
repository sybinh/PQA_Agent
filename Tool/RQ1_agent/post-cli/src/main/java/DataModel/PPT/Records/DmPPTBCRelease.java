/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.Fields.DmPPTField_AenderungForBc;
import DataModel.PPT.Fields.DmPPTField_FinalReference;
import DataModel.PPT.Fields.DmPPTField_Reference_BCRelease_FCRelease;
import DataModel.PPT.Fields.DmPPTField_Reference_BCRelease_IssuesFDs;
import DataModel.PPT.Fields.DmPPTField_Reference_BCRelease_Issues_wihout_FC_relation;
import DataModel.PPT.Fields.DmPPTValueField;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import DataModel.PPT.Fields.DmPPTValueField_Text_BC_Description;
import DataModel.PPT.Fields.DmPPTValueField_Text_BC_Hexneutral;
import DataModel.PPT.Fields.DmPPTValueField_Text_BC_Longname;
import DataModel.PPT.Fields.DmPPTValueField_Text_BC_Responsible;
import static DataModel.PPT.Records.DmPPTAenderung.getTheAnfordererName;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1Rrm;
import DataModel.Rq1.Records.DmRq1Rrm_Pst_Bc;
import Rq1Cache.Types.Rq1XmlTable_ChangesToConfiguration;
import Rq1Cache.Types.Rq1XmlTable_ChangesToPartlist;
import Rq1Cache.Types.Rq1XmlTable_RrmChangesToIssues;
import Rq1Data.Enumerations.LifeCycleState_RRM;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.logging.Logger;
import util.EcvAppendedData;
import util.EcvDate;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTBCRelease extends DmPPTRecord {

    final private static Logger LOGGER = Logger.getLogger(DmPPTBCRelease.class.getCanonicalName());

    public final DmPPTField_FinalReference<DmRq1Bc> RQ1_BC_RELEASE;
    private final DmPPTField_FinalReference<DmRq1Rrm_Pst_Bc> RRM;

    //Only for the Planungsdaten dummy issues
    public final DmPPTField_Reference_BCRelease_IssuesFDs RQ1_ISSUE_FDS;

    public final DmPPTField_Reference_BCRelease_Issues_wihout_FC_relation PPT_ISSUE_SWS_WITHOUT_FC_RELATION;
    public final DmPPTField_AenderungForBc AENDERUNG_FOR_BC;
    public final DmPPTField_Reference_BCRelease_FCRelease PPT_FCS;
    //Can be null, if the bc is used as Predecessor
    public final DmPPTField_FinalReference<DmPPTRelease> HAS_PARENT_RELEASE;

    public final DmPPTValueField_Text BC_ID;
    public final DmPPTValueField_Text BC_NAME;
    private final DmPPTValueField_Text_BC_Longname BC_LONGNAME;
    public final DmPPTValueField_Text BC_VERSION;
    public final DmPPTValueField_Text_BC_Description BC_DESCRIPTION;
    public final DmPPTValueField_Text_BC_Responsible BC_RESPONSIBLE_FULLNAME;
    public final DmPPTValueField_Text BC_RESPONSIBLE;

    public final DmPPTValueField_Text PPT_RB_COMMENT;
    public final DmPPTValueField_Text PPT_CTC;

    public final DmRq1Field_Table<Rq1XmlTable_ChangesToConfiguration> CHANGES_TO_CONFIGURATION;
    public final DmRq1Field_Table<Rq1XmlTable_ChangesToPartlist> CHANGES_TO_PARTLIST;
    public final DmRq1Field_Table<Rq1XmlTable_RrmChangesToIssues> CHANGES_TO_ISSUES;

    public final DmPPTValueField_Text_BC_Hexneutral PPT_BC_HEXNEUTRAL;

    public LifeCycleState_RRM LCS_RRM;

    public final DmPPTValueField<EcvDate> BC_SUBMIT_DATE;

    public DmPPTBCRelease(DmRq1Bc rq1BcRelease, DmPPTRelease release, DmRq1Rrm_Pst_Bc rrm) {
        super("DataModel ProPlaTo BC Release");
        assert (rq1BcRelease != null);
        assert (release != null);
        assert (rrm != null);
        this.LCS_RRM = (LifeCycleState_RRM) rrm.LIFE_CYCLE_STATE.getValue();
        //Fields for the treaview
        addField(RRM = new DmPPTField_FinalReference<>(this, rrm, "RQ1 RRM Release BC"));
        addField(RQ1_BC_RELEASE = new DmPPTField_FinalReference<>(this, rq1BcRelease, "RQ1 BC Release"));
        addField(PPT_ISSUE_SWS_WITHOUT_FC_RELATION = new DmPPTField_Reference_BCRelease_Issues_wihout_FC_relation(this, this.RQ1_BC_RELEASE.getElement().MAPPED_ISSUES, "ProPlaTo Issues without FC Relation"));
        addField(AENDERUNG_FOR_BC = new DmPPTField_AenderungForBc(this, "ProPlaTo Issues"));
        addField(RQ1_ISSUE_FDS = new DmPPTField_Reference_BCRelease_IssuesFDs(this, this.RQ1_BC_RELEASE.getElement().MAPPED_ISSUES, "ProPlaTo Issue FDs"));

        addField(PPT_FCS = new DmPPTField_Reference_BCRelease_FCRelease(this, "ProPlaTo FC Releases"));
        addField(HAS_PARENT_RELEASE = new DmPPTField_FinalReference<>(this, release, "ProPlaTo Has Parent Release"));

        //Fields for the Information View
        addField(BC_ID = new DmPPTValueField_Text(this, this.RQ1_BC_RELEASE.getElement().getRq1Id(), "ProPlaTo: <ID> "));
        addField(BC_NAME = new DmPPTValueField_Text(this, this.RQ1_BC_RELEASE.getElement().getName(), "ProPlaTo: <Name> "));
        addField(BC_LONGNAME = new DmPPTValueField_Text_BC_Longname(this, "ProPlaTo: <Langbezeichnung> "));
        addField(BC_VERSION = new DmPPTValueField_Text(this, this.RQ1_BC_RELEASE.getElement().getVersion(), "ProPlaTo: <Version> "));

        addField(BC_RESPONSIBLE_FULLNAME = new DmPPTValueField_Text_BC_Responsible(this, "Internal: Responsible Fullname"));
        addField(BC_RESPONSIBLE = new DmPPTValueField_Text(this, getTheAnfordererName(BC_RESPONSIBLE_FULLNAME.getValueAsText()), "ProPlaTo: <Paketverantwortlicher> "));

        //Fields for the internal view
        addField(BC_SUBMIT_DATE = new DmPPTValueField<>(this, this.RQ1_BC_RELEASE.getElement().SUBMIT_DATE.getValue(), "Internal BC Submit Date"));
        addField(PPT_CTC = new DmPPTValueField_Text(this, "", "Internal RRM Release - BC CTC"));
        addField(PPT_RB_COMMENT = new DmPPTValueField_Text(this, "", "Internal RRM Release - BC RB_Comment"));
        addField(BC_DESCRIPTION = new DmPPTValueField_Text_BC_Description(this, "Internal BC Description"));

        addField(PPT_BC_HEXNEUTRAL = new DmPPTValueField_Text_BC_Hexneutral(this, rrm.CHANGES_TO_PARTLIST, "Internal BC Hexneutral"));

        CHANGES_TO_CONFIGURATION = rrm.CHANGES_TO_CONFIGURATION;
        CHANGES_TO_PARTLIST = rrm.CHANGES_TO_PARTLIST;
        CHANGES_TO_ISSUES = rrm.CHANGES_TO_ISSUES;
        //Only for output
        this.PPT_RB_COMMENT.setValue(rrm.RB_COMMENT.getValueAsText());
        String ctc = "";
        EcvTableData dataCTC = CHANGES_TO_CONFIGURATION.getValue();
        Rq1XmlTable_ChangesToConfiguration descCTC = rrm.CHANGES_TO_CONFIGURATION.getTableDescription();
        for (EcvTableRow row : dataCTC.getRows()) {
            if (row.getValueAt(descCTC.CUST_VISIBLE).toString().equals("true")) {
                //Convert Derivate to external view
                boolean takeCTC = false;
                if (this.HAS_PARENT_RELEASE.getElement().areDerivativesRelevant()
                        && row.getValueAt(descCTC.DERIVATIVE) != null) {
                    Iterator<String> iter = this.getIteratorOfObject(row.getValueAt(descCTC.DERIVATIVE));
                    while (iter.hasNext()) {
                        String derivate = iter.next();
                        derivate = this.HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getProPlaToLineExternal(derivate);
                        if (derivate.equals(this.HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().NAME.getValueAsText())) {
                            takeCTC = true;
                        }
                    }
                } else {
                    takeCTC = true;
                }

                if (takeCTC) {
                    String toAdd = row.getValueAt(descCTC.TYPE).toString() + " - "
                            + row.getValueAt(descCTC.NAME).toString() + " - "
                            + row.getValueAt(descCTC.TARGET).toString() + " - "
                            + row.getValueAt(descCTC.EXTERNAL_COMMENT).toString();
                    if (!ctc.contains(toAdd)) { //if the information is already there
                        ctc += "\n" + toAdd + "\n";
                    }
                }
            }
        }

        this.PPT_CTC.setValue(ctc);
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCanceled() {
        return this.RQ1_BC_RELEASE.getElement().isCanceled();
    }

    @Override
    public String getTitle() {
        return this.RQ1_BC_RELEASE.getElement().getTitle();
    }

    @Override
    public String getId() {
        return this.RQ1_BC_RELEASE.getElement().getId();
    }

    public DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Release> getMAPPED_CHILDREN() {
        return this.RQ1_BC_RELEASE.getElement().MAPPED_CHILDREN;
    }

    public String getVersion() {
        return this.RQ1_BC_RELEASE.getElement().getVersion();
    }

    public String getName() {
        return this.RQ1_BC_RELEASE.getElement().getName();
    }

    public String getLangbezeichnung() {
        String lb = BC_LONGNAME.getValueAsText();
        if (lb.isEmpty()) {
            LOGGER.warning("Empty Langbezeichner for " + getName() + "/" + getVersion());
        }
        return (lb);
    }

    public DmRq1Bc getRelease() {
        return this.RQ1_BC_RELEASE.getElement();
    }

    public DmPPTBCRelease getPredecessor() {
        DmRq1Bc rq1Bc = RQ1_BC_RELEASE.getElement();
        rq1Bc.loadCacheForPredecessorList(3);
        if (rq1Bc.PREDECESSOR.getElement() != null) {
            return new DmPPTBCRelease((DmRq1Bc) rq1Bc.PREDECESSOR.getElement(), HAS_PARENT_RELEASE.getElement(), RRM.getElement());
        }
        return null;
    }

    public boolean isPredecessor(DmPPTBCRelease release) {
        DmPPTBCRelease currBC = release.getPredecessor();
        while (currBC != null) {
            if (currBC.getRq1().equals(this.getRq1())) {
                return true;
            }
            currBC = currBC.getPredecessor();
        }
        return false;
    }

    public String getRq1() {
        return this.RQ1_BC_RELEASE.getElement().getRq1Id();
    }

    public EcvDate getPlannedDate() {
        return this.RQ1_BC_RELEASE.getElement().PLANNED_DATE.getValue();
    }

    @Override
    public EcvAppendedData getEcvAppendedData() {
        Map<String, String> appendedData = new HashMap<>();
        return new EcvAppendedData(appendedData);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.BC_ID.getValue());
        hash = 47 * hash + Objects.hashCode(this.BC_NAME.getValue());
        hash = 47 * hash + Objects.hashCode(this.BC_LONGNAME.getValue());
        hash = 47 * hash + Objects.hashCode(this.BC_VERSION.getValue());
        hash = 47 * hash + Objects.hashCode(this.BC_DESCRIPTION.getValue());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DmPPTBCRelease other = (DmPPTBCRelease) obj;
        if (!Objects.equals(this.BC_ID.getValue(), other.BC_ID.getValue())) {
            return false;
        }
        if (!Objects.equals(this.BC_NAME.getValue(), other.BC_NAME.getValue())) {
            return false;
        }
        if (!Objects.equals(this.BC_LONGNAME.getValue(), other.BC_LONGNAME.getValue())) {
            return false;
        }
        if (!Objects.equals(this.BC_VERSION.getValue(), other.BC_VERSION.getValue())) {
            return false;
        }
        if (!Objects.equals(this.BC_DESCRIPTION.getValue(), other.BC_DESCRIPTION.getValue())) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Iterator<String> getIteratorOfObject(Object d) {
        return ((TreeSet<String>) d).iterator();
    }
}
