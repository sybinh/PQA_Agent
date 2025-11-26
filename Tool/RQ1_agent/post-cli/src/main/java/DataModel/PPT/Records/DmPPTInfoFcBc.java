/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.Ecv.PST.Records.DmEcvBc;
import DataModel.Ecv.PST.Records.DmEcvFc;
import DataModel.PPT.Fields.DmPPTField_Reference;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import util.EcvAppendedData;

/**
 *
 * @author moe83wi
 */
public class DmPPTInfoFcBc extends DmPPTRecord {

    private final DmPPTField_Reference<DmPPTRelease> PPT_RELEASE;
    private final DmPPTField_Reference<DmEcvFc> PST_FC_RELEASE;
    private final DmPPTField_Reference<DmEcvBc> PST_BC_RELEAS;

    public final DmPPTValueField_Text CLUSTER;
    public final DmPPTValueField_Text PROJECT;
    public final DmPPTValueField_Text LINE;
    public final DmPPTValueField_Text RELEASE;
    public final DmPPTValueField_Text RELEASE_PLANNED_DATE;
    public final DmPPTValueField_Text PAKET;
    /**
     * The Version of the BC with a prefixed blank and encapsulated in "" This
     * has to be done because Excel would interpret the information as date
     * otherwise.
     */
    public final DmPPTValueField_Text BCVERSION;
    public final DmPPTValueField_Text FUNKTION;
    /**
     * The Version of the FC with a prefixed blank and encapsulated in "" This
     * has to be done because Excel would interpret the information as date
     * otherwise.
     */
    public final DmPPTValueField_Text FCVERSION;

    public DmPPTInfoFcBc(DmPPTRelease pptRelease, DmEcvBc bcRelease) {
        super("ProPlaTo Systemkonstante");
        assert (pptRelease != null);
        assert (bcRelease != null);

        addField(PPT_RELEASE = new DmPPTField_Reference<>(this, pptRelease, "PPT Release"));
        addField(PST_FC_RELEASE = new DmPPTField_Reference<>(this, null, "PST FC Release"));
        addField(PST_BC_RELEAS = new DmPPTField_Reference<>(this, bcRelease, "PST BC Release"));

        addField(CLUSTER = new DmPPTValueField_Text(this, pptRelease.BELONG_TO_SCHIENE.getElement().getClusterOfTags(), "ProPlaTo: <Cluster>"));
        addField(PROJECT = new DmPPTValueField_Text(this, pptRelease.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getTitle(), "ProPlaTo: <Project>"));
        addField(LINE = new DmPPTValueField_Text(this, pptRelease.BELONG_TO_SCHIENE.getElement().getTitle(), "ProPlaTo: <Line>"));
        addField(RELEASE = new DmPPTValueField_Text(this, PPT_RELEASE.getElement().getTitle(), "ProPlaTo: <Release>"));
        addField(RELEASE_PLANNED_DATE = new DmPPTValueField_Text(this, PPT_RELEASE.getElement().AUSLIEFERDATUM_SOLL.getValue().getXmlValue(), "ProPlaTo: <Release Planned Date>"));
        addField(PAKET = new DmPPTValueField_Text(this, PST_BC_RELEAS.getElement().BC_NAME.getValueAsText(), "ProPlaTo: <Paket>"));
        addField(BCVERSION = new DmPPTValueField_Text(this, "\" " + PST_BC_RELEAS.getElement().BC_VERSION.getValueAsText() + "\"", "ProPlaTo: <BC Version>"));
        addField(FUNKTION = new DmPPTValueField_Text(this, "", "ProPlaTo: <Funktion>"));
        addField(FCVERSION = new DmPPTValueField_Text(this, "", "ProPlaTo: <FC Version>"));
    }

    public DmPPTInfoFcBc(DmPPTRelease pptRelease, DmEcvFc fcRelease, DmEcvBc bcRelease) {
        super("ProPlaTo Systemkonstante");
        assert (pptRelease != null);
        assert (fcRelease != null);
        assert (bcRelease != null);

        addField(PPT_RELEASE = new DmPPTField_Reference<>(this, pptRelease, "PPT Release"));
        addField(PST_FC_RELEASE = new DmPPTField_Reference<>(this, fcRelease, "PST FC Release"));
        addField(PST_BC_RELEAS = new DmPPTField_Reference<>(this, bcRelease, "PST BC Release"));

        addField(CLUSTER = new DmPPTValueField_Text(this, pptRelease.BELONG_TO_SCHIENE.getElement().getClusterOfTags(), "ProPlaTo: <Cluster>"));
        addField(PROJECT = new DmPPTValueField_Text(this, pptRelease.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getTitle(), "ProPlaTo: <Project>"));
        addField(LINE = new DmPPTValueField_Text(this, pptRelease.BELONG_TO_SCHIENE.getElement().getTitle(), "ProPlaTo: <Line>"));
        addField(RELEASE = new DmPPTValueField_Text(this, PPT_RELEASE.getElement().getTitle(), "ProPlaTo: <Release>"));
        addField(RELEASE_PLANNED_DATE = new DmPPTValueField_Text(this, PPT_RELEASE.getElement().AUSLIEFERDATUM_SOLL.getValue().getXmlValue(), "ProPlaTo: <Release Planned Date>"));
        addField(PAKET = new DmPPTValueField_Text(this, PST_BC_RELEAS.getElement().BC_NAME.getValueAsText(), "ProPlaTo: <Paket>"));
        addField(BCVERSION = new DmPPTValueField_Text(this, "\" " + PST_BC_RELEAS.getElement().BC_VERSION.getValueAsText() + "\"", "ProPlaTo: <BC Version>"));
        addField(FUNKTION = new DmPPTValueField_Text(this, PST_FC_RELEASE.getElement().FC_NAME.getValueAsText(), "ProPlaTo: <Funktion>"));
        addField(FCVERSION = new DmPPTValueField_Text(this, "\" " + PST_FC_RELEASE.getElement().FC_VERSION.getValueAsText() + "\"", "ProPlaTo: <FC Version>"));
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public String getTitle() {
        return this.FUNKTION.getValueAsText() + "/" + this.FCVERSION.getValueAsText();
    }

    @Override
    public String getId() {
        return this.FUNKTION.getValueAsText() + "/" + this.FCVERSION.getValueAsText();
    }

    @Override
    public EcvAppendedData getEcvAppendedData() {
        Map<String, String> appendedData = new HashMap<>();

        return new EcvAppendedData(appendedData);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.CLUSTER.getValueAsText());
        hash += 89 * hash + Objects.hashCode(this.PROJECT.getValueAsText());
        hash += 89 * hash + Objects.hashCode(this.LINE.getValueAsText());
        hash += 89 * hash + Objects.hashCode(this.RELEASE.getValueAsText());
        hash += 89 * hash + Objects.hashCode(this.PAKET.getValueAsText());
        hash += 89 * hash + Objects.hashCode(this.FUNKTION.getValueAsText());
        hash += 89 * hash + Objects.hashCode(this.FCVERSION.getValueAsText());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DmPPTInfoFcBc other = (DmPPTInfoFcBc) obj;
        if (!Objects.equals(this.CLUSTER.getValueAsText(), other.CLUSTER.getValueAsText())) {
            return false;
        }
        if (!Objects.equals(this.PROJECT.getValueAsText(), other.PROJECT.getValueAsText())) {
            return false;
        }
        if (!Objects.equals(this.LINE.getValueAsText(), other.LINE.getValueAsText())) {
            return false;
        }
        if (!Objects.equals(this.RELEASE.getValueAsText(), other.RELEASE.getValueAsText())) {
            return false;
        }
        if (!Objects.equals(this.FUNKTION.getValueAsText(), other.FUNKTION.getValueAsText())) {
            return false;
        }
        if (!Objects.equals(this.FCVERSION.getValueAsText(), other.FCVERSION.getValueAsText())) {
            return false;
        }
        if (!Objects.equals(this.PAKET.getValueAsText(), other.PAKET.getValueAsText())) {
            return false;
        }
        return true;
    }

}
