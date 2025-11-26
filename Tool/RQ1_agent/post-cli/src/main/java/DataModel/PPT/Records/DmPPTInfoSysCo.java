/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.Ecv.SystemConstants.Records.DmEcvSysConst;
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
public class DmPPTInfoSysCo extends DmPPTRecord {

    private final DmPPTField_Reference<DmPPTRelease> PPT_RELEASE;

    public final DmPPTValueField_Text CLUSTER;
    public final DmPPTValueField_Text PROJECT;
    public final DmPPTValueField_Text LINE;
    public final DmPPTValueField_Text RELEASE;
    public final DmPPTValueField_Text RELEASE_PLANNED_DATE;
    public final DmPPTValueField_Text NAME;
    public final DmPPTValueField_Text VALUE;

    public DmPPTInfoSysCo(DmPPTRelease pptRelease, DmEcvSysConst ecvSysconst) {
        super("ProPlaTo Systemkonstante");
        assert (pptRelease != null);
        assert (ecvSysconst != null);

        addField(PPT_RELEASE = new DmPPTField_Reference<>(this, pptRelease, "PPT Release"));

        addField(CLUSTER = new DmPPTValueField_Text(this, pptRelease.BELONG_TO_SCHIENE.getElement().getClusterOfTags(), "ProPlaTo: <Cluster>"));
        addField(PROJECT = new DmPPTValueField_Text(this, pptRelease.BELONG_TO_SCHIENE.getElement().BELONGS_TO_PROJECT.getElement().getTitle(), "ProPlaTo: <Project>"));
        addField(LINE = new DmPPTValueField_Text(this, pptRelease.BELONG_TO_SCHIENE.getElement().getTitle(), "ProPlaTo: <Line>"));
        addField(RELEASE = new DmPPTValueField_Text(this, PPT_RELEASE.getElement().getTitle(), "ProPlaTo: <Release>"));
        addField(RELEASE_PLANNED_DATE = new DmPPTValueField_Text(this, PPT_RELEASE.getElement().AUSLIEFERDATUM_SOLL.getValue().getXmlValue(), "ProPlaTo: <Release Planned Date>"));
        addField(NAME = new DmPPTValueField_Text(this, ecvSysconst.getNameNotNull(), "ProPlaTo: <Name>"));
        addField(VALUE = new DmPPTValueField_Text(this, ecvSysconst.getValueNotNull(), "ProPlaTo: <Value>"));
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
        return this.NAME.getValueAsText();
    }

    @Override
    public String getId() {
        return this.NAME.getValueAsText();
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
        hash = 89 * hash + Objects.hashCode(this.PROJECT.getValueAsText());
        hash = 89 * hash + Objects.hashCode(this.LINE.getValueAsText());
        hash = 89 * hash + Objects.hashCode(this.RELEASE.getValueAsText());
        hash = 89 * hash + Objects.hashCode(this.NAME.getValueAsText());
        hash = 89 * hash + Objects.hashCode(this.VALUE.getValueAsText());
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
        final DmPPTInfoSysCo other = (DmPPTInfoSysCo) obj;
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
        if (!Objects.equals(this.NAME.getValueAsText(), other.NAME.getValueAsText())) {
            return false;
        }
        if (!Objects.equals(this.VALUE.getValueAsText(), other.VALUE.getValueAsText())) {
            return false;
        }
        return true;
    }

}
