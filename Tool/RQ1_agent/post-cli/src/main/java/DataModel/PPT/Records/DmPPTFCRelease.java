/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.PPT.Fields.DmPPTField_FinalReference;
import DataModel.PPT.Fields.DmPPTField_Reference_FCRelease_IssueFDs;
import DataModel.PPT.Fields.DmPPTField_Reference_FCRelease_Issues;
import DataModel.PPT.Fields.DmPPTValueField;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import DataModel.PPT.Fields.DmPPTValueField_Text_FCRelease_Neutral_Calibration;
import DataModel.PPT.Fields.DmPPTValueField_Text_FC_Hexneutral;
import DataModel.PPT.Fields.DmPPTValueField_Text_FC_Longname;
import DataModel.Rq1.Records.DmRq1Fc;
import DataModel.Rq1.Records.DmRq1Release;
import Rq1Cache.Types.Rq1XmlTable_ChangesToPartlist;
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
public class DmPPTFCRelease extends DmPPTRecord implements Comparable<DmPPTFCRelease> {

    final private static Logger LOGGER = Logger.getLogger(DmPPTFCRelease.class.getCanonicalName());

    public final DmPPTField_FinalReference<DmRq1Fc> RQ1_FC_RELEASE;
    public final DmPPTField_Reference_FCRelease_Issues PPT_ISSUES_SWS;
    public final DmPPTField_FinalReference<DmPPTBCRelease> HAS_PARENT_RELEASE;

    //Only for the Planungsdaten dummy issues
    public final DmPPTField_Reference_FCRelease_IssueFDs RQ1_ISSUE_FDS;

    public final DmPPTValueField_Text FC_ID;
    public final DmPPTValueField_Text FC_SHORTNAME;
    public final DmPPTValueField_Text FC_VERSION;
    public final DmPPTValueField_Text_FCRelease_Neutral_Calibration FC_NEUTRAL_CALIBRATION;
    public final DmPPTValueField_Text FC_SUBMITTER;
    public final DmPPTValueField_Text FC_FIRST_CALIBRATION;
    public final DmPPTValueField_Text FC_SYSTEM_CONSTANTS_DEPENDENCE;
    public final DmPPTValueField_Text FC_APPLICATION_RESPONSIBLE_SUPPLIER;
    private final DmPPTValueField_Text_FC_Longname FC_LONGNAME;

    public final DmPPTValueField_Text_FC_Hexneutral PPT_FC_HEXNEUTRAL;

    public final DmPPTValueField<EcvDate> FC_SUBMIT_DATE;

    public final DmPPTValueField_Text FC_CTP;

    public DmPPTFCRelease(DmRq1Fc rq1FcRelease, DmPPTBCRelease bcRelease) {
        super("ProPlaTo FC Release");
        //Only for the tree
        addField(RQ1_FC_RELEASE = new DmPPTField_FinalReference<>(this, rq1FcRelease, "RQ1 FC Release"));
        addField(PPT_ISSUES_SWS = new DmPPTField_Reference_FCRelease_Issues(this, "ProPlaTo Issues"));
        addField(RQ1_ISSUE_FDS = new DmPPTField_Reference_FCRelease_IssueFDs(this, this.RQ1_FC_RELEASE.getElement().MAPPED_ISSUES, "ProPlaTo Issue FDs"));

        addField(HAS_PARENT_RELEASE = new DmPPTField_FinalReference<>(this, bcRelease, "PPT Parent BC"));

        //ProPlaToView
        addField(FC_ID = new DmPPTValueField_Text(this, this.RQ1_FC_RELEASE.getElement().getRq1Id(), "ProPlaTo: <ID> "));
        addField(FC_VERSION = new DmPPTValueField_Text(this, this.RQ1_FC_RELEASE.getElement().getVersion(), "ProPlaTo: <Version> "));
        addField(FC_SHORTNAME = new DmPPTValueField_Text(this, this.RQ1_FC_RELEASE.getElement().getName(), "ProPlaTo: <Kurzbezeichnung> "));
        addField(FC_LONGNAME = new DmPPTValueField_Text_FC_Longname(this, "ProPlaTo: <Langbezeichnung> "));
        if (this.RQ1_FC_RELEASE.getElement().SUBMITTER.getValueAsText().isEmpty()) {
            addField(FC_SUBMITTER = new DmPPTValueField_Text(this, "unbekannt", "ProPlaTo: <Funktionsverantwortlicher> "));
        } else {
            addField(FC_SUBMITTER = new DmPPTValueField_Text(this, this.RQ1_FC_RELEASE.getElement().SUBMITTER.getValueAsText(), "ProPlaTo: <Funktionsverantwortlicher> "));
        }

        addField(FC_NEUTRAL_CALIBRATION = new DmPPTValueField_Text_FCRelease_Neutral_Calibration(RQ1_FC_RELEASE.getElement(), "ProPlaTo PD: <Neutralbedatung> "));
        addField(FC_FIRST_CALIBRATION = new DmPPTValueField_Text(this, "false", "ProPlaTo PD: <Erstbedatung>"));
        addField(FC_SYSTEM_CONSTANTS_DEPENDENCE = new DmPPTValueField_Text(this, "Systemkonstantenabhaengigkeit fehlt", "ProPlaTo PD: <Systemkonstantenabhaengigkeit> "));
        addField(FC_APPLICATION_RESPONSIBLE_SUPPLIER = new DmPPTValueField_Text(this, "unbekannt", "ProPlaTo PD: <ApplVerantwLieferant>"));

        //Only for the internal view
        addField(FC_CTP = new DmPPTValueField_Text(this, "", "Internal FC CTP"));
        addField(FC_SUBMIT_DATE = new DmPPTValueField<>(this, this.RQ1_FC_RELEASE.getElement().SUBMIT_DATE.getValue(), "Internal FC Submit Date"));

        addField(PPT_FC_HEXNEUTRAL = new DmPPTValueField_Text_FC_Hexneutral(this, this.HAS_PARENT_RELEASE.getElement().CHANGES_TO_PARTLIST, "ProPlaTo Internal: Hexneutral"));

        //get the CTP
        String ctp = "";
        EcvTableData dataCTP = this.HAS_PARENT_RELEASE.getElement().CHANGES_TO_PARTLIST.getValue();
        Rq1XmlTable_ChangesToPartlist descCTP = this.HAS_PARENT_RELEASE.getElement().CHANGES_TO_PARTLIST.getTableDescription();
        for (EcvTableRow row : dataCTP.getRows()) {
            if (row.getValueAt(descCTP.NAME).toString().equals(FC_SHORTNAME.getValueAsText())
                    && row.getValueAt(descCTP.TARGET).toString().equals(this.FC_VERSION.getValueAsText())) {
                if (this.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().areDerivativesRelevant()
                        && row.getValueAt(descCTP.DERIVATIVE) != null) { //If derivatives are mentioned
                    Iterator<String> iter = this.getIteratorOfObject(row.getValueAt(descCTP.DERIVATIVE));
                    while (iter.hasNext()) {
                        String derivate = iter.next();
                        derivate = this.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getProPlaToLineExternal(derivate);
                        if (derivate.equals(this.HAS_PARENT_RELEASE.getElement().HAS_PARENT_RELEASE.getElement().BELONG_TO_SCHIENE.getElement().NAME.getValueAsText())) {
                            if (row.getValueAt(descCTP.EXTERNAL_COMMENT) != null && !row.getValueAt(descCTP.EXTERNAL_COMMENT).toString().isEmpty()) {
                                if (!ctp.contains(row.getValueAt(descCTP.EXTERNAL_COMMENT).toString())) { //check if the text is already added
                                    ctp += row.getValueAt(descCTP.EXTERNAL_COMMENT).toString() + "\n";
                                }
                            }
                        }
                    }
                } else {
                    //Belongs to all derivatives
                    if (row.getValueAt(descCTP.EXTERNAL_COMMENT) != null && !row.getValueAt(descCTP.EXTERNAL_COMMENT).toString().isEmpty()) {
                        if (!ctp.contains(row.getValueAt(descCTP.EXTERNAL_COMMENT).toString())) {
                            ctp += row.getValueAt(descCTP.EXTERNAL_COMMENT).toString() + "\n";
                        }
                    }
                }
            }
        }
        this.FC_CTP.setValue(ctp);

    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCanceled() {
        return this.RQ1_FC_RELEASE.getElement().isCanceled();
    }

    @Override
    public String getTitle() {
        return this.RQ1_FC_RELEASE.getElement().getTitle();
    }

    @Override
    public String getId() {
        return this.RQ1_FC_RELEASE.getElement().getId();
    }

    public String getName() {
        return this.RQ1_FC_RELEASE.getElement().getName();
    }

    public String getLangbezeichnung() {
        String lb = FC_LONGNAME.getValueAsText();
        if (lb.isEmpty()) {
            LOGGER.warning("Empty Langbezeichner for " + getName() + "/" + getVersion());
        }
        return (lb);
    }

    public String getVersion() {
        return this.RQ1_FC_RELEASE.getElement().getVersion();
    }

    public EcvDate getPlannedDate() {
        return this.RQ1_FC_RELEASE.getElement().getPlannedDate();
    }

    public DmRq1Fc getRelease() {
        return this.RQ1_FC_RELEASE.getElement();
    }

    public DmPPTFCRelease getPredecessor() {
        DmRq1Fc rq1Fc = RQ1_FC_RELEASE.getElement();
        rq1Fc.loadCacheForPredecessorList(3);
        DmRq1Release predecessor = rq1Fc.PREDECESSOR.getElement();
        if (predecessor != null) {
            return new DmPPTFCRelease((DmRq1Fc) predecessor, HAS_PARENT_RELEASE.getElement());
        }
        return null;
    }

    public boolean isPredecessor(DmPPTFCRelease release) {
        DmPPTFCRelease currFC = release.getPredecessor();
        while (currFC != null) {
            if (currFC.getRq1().equals(this.getRq1())) {
                return true;
            }
            currFC = currFC.getPredecessor();
        }
        return false;
    }

    private String getRq1() {
        return this.RQ1_FC_RELEASE.getElement().getRq1Id();
    }

    @Override
    public EcvAppendedData getEcvAppendedData() {
        Map<String, String> appendedData = new HashMap<>();
        return new EcvAppendedData(appendedData);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.FC_ID.getValue());
        hash = 97 * hash + Objects.hashCode(this.FC_SHORTNAME.getValue());
        hash = 97 * hash + Objects.hashCode(this.FC_VERSION.getValue());
        hash = 97 * hash + Objects.hashCode(this.FC_SUBMITTER.getValue());
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
        final DmPPTFCRelease other = (DmPPTFCRelease) obj;
        if (!Objects.equals(this.FC_ID.getValue(), other.FC_ID.getValue())) {
            return false;
        }
        if (!Objects.equals(this.FC_SHORTNAME.getValue(), other.FC_SHORTNAME.getValue())) {
            return false;
        }
        if (!Objects.equals(this.FC_VERSION.getValue(), other.FC_VERSION.getValue())) {
            return false;
        }
        if (!Objects.equals(this.FC_SUBMITTER.getValue(), other.FC_SUBMITTER.getValue())) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Iterator<String> getIteratorOfObject(Object d) {
        return ((TreeSet<String>) d).iterator();
    }

    @Override
    public int compareTo(DmPPTFCRelease t) {
        assert (t != null);
        return (getId().compareTo(t.getId()));
    }
}
