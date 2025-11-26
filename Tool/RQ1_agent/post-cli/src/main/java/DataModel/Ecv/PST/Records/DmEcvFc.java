/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.PST.Records;

import DataModel.DmElement;
import DataModel.Ecv.Records.DmEcvFcLongNameTable;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import DataModel.PPT.Records.DmPPTFCRelease;
import DataModel.Xml.DmXmlStringField;
import java.util.logging.Level;

/**
 *
 * @author moe83wi
 */
public class DmEcvFc extends DmElement {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DmEcvFc.class.getCanonicalName());

    public final DmXmlStringField FC_NAME;
    public final DmXmlStringField FC_VERSION;

    public final DmPPTValueField_Text FC_ID;
    private String fcLongName = null;
    public final DmPPTValueField_Text FC_PREDECESSOR_ID;
    public final DmPPTValueField_Text FC_PREDECESSOR_VERSION;
    public final DmPPTValueField_Text FC_RESPONSIBLE;

    public DmEcvFc(String elementType) {
        super("Dm PST Fc");

        //ProPlaToView
        addField(FC_ID = new DmPPTValueField_Text(this, "", "ProPlaTo: <ID> "));
        addField(FC_NAME = new DmXmlStringField(this, "Kurzbezeichnung", "ProPlaTo: <Kurzbezeichnung> "));
        addField(FC_VERSION = new DmXmlStringField(this, "Version", "ProPlaTo: <Version> "));
        addField(FC_PREDECESSOR_ID = new DmPPTValueField_Text(this, "", "ProPlaTo: <VorgaengerID> "));
        addField(FC_PREDECESSOR_VERSION = new DmPPTValueField_Text(this, "", "ProPlaTo: <VorgaengerVersion> "));
        addField(FC_RESPONSIBLE = new DmPPTValueField_Text(this, "unbekannt", "ProPlaTo: <Funktionsverantwortlicher> "));
    }

    public DmEcvFc(DmPPTFCRelease fcRelease) {
        this("Dm PST Fc");
        updateInformation(fcRelease);
    }

    public final void enhanceInformation(DmEcvFcLongNameTable fcLongnames, DmEcvBc bc) {
        if (this.FC_NAME.getValueAsText().isEmpty()) {
            this.FC_NAME.setValue("unbekannt");
        }

        setFcLongName(fcLongnames.getLongNameGerman(FC_NAME.getValueAsText()));
        this.FC_RESPONSIBLE.setValue(bc.BC_RESPONSIBLE_ONLY_NAME.getValue());
        if (this.FC_PREDECESSOR_VERSION.getValue().isEmpty()) {
            //calc the version of the predecessors
            String version = this.FC_VERSION.getValueAsText();
            if (!version.contains("_") && version.contains(".") && version.split("[.]").length > 2) {
                try {
                    int first = Integer.parseInt(version.split("[.]")[0]);
                    int second = Integer.parseInt(version.split("[.]")[1]);
                    int third = Integer.parseInt(version.split("[.]")[2]);
                    if (third > 0) {
                        third--;
                    } else if (second > 0) {
                        second--;
                    }
                    version = String.valueOf(first) + "." + String.valueOf(second) + "." + String.valueOf(third);
                    this.FC_PREDECESSOR_VERSION.setValue(version);
                } catch (Exception e) {
                    LOGGER.info("Unable to calc version of predecessor" + this.FC_VERSION.getValueAsText());
                }
            }
        }
    }

    public final void updateInformation(DmPPTFCRelease fcRelease) {
        this.FC_NAME.setValue(fcRelease.getName());
        this.FC_VERSION.setValue(fcRelease.getVersion());

        this.FC_ID.setValue(fcRelease.FC_ID.getValueAsText());
        setFcLongName(fcRelease.getLangbezeichnung());

        DmPPTFCRelease fcPredecessor = fcRelease.getPredecessor();
        if (fcPredecessor != null) {
            this.FC_PREDECESSOR_ID.setValue(fcPredecessor.FC_ID.getValueAsText());
            this.FC_PREDECESSOR_VERSION.setValue(fcPredecessor.FC_VERSION.getValueAsText());
        }
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        return this.FC_NAME.getValueAsText();
    }

    private void setFcLongName(String fcLongName) {
        if ((fcLongName == null) || (fcLongName.isEmpty())) {
            LOGGER.log(Level.SEVERE, "Empty fcLongName", new Exception("Empty fcLongName"));
        } else {
            this.fcLongName = fcLongName;
        }
    }

    public String getLangbezeichnung() {
        if (fcLongName == null) {
            LOGGER.warning("fcLongName null for " + FC_NAME.getValueAsText());
            return ("");
        }
        return (fcLongName);
    }

    @Override
    public String getId() {
        return this.FC_ID.getValueAsText();
    }

    public DmEcvFc getCopy() {
        DmEcvFc fc = new DmEcvFc("Dm Ecv Fc");
        fc.FC_NAME.setValue(this.FC_NAME.getValueAsText());
        fc.FC_VERSION.setValue(this.FC_VERSION.getValueAsText());
        return fc;
    }

}
