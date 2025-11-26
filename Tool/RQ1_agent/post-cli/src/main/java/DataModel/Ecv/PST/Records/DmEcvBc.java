/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.PST.Records;

import DataModel.DmElement;
import DataModel.Ecv.PST.Fields.DmEcvField_Reference_BC_FC;
import DataModel.Ecv.Records.DmEcvBcLongNameTable;
import DataModel.Ecv.Records.DmEcvBcResponsibleTable;
import DataModel.Ecv.Records.DmEcvFcLongNameTable;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import static DataModel.PPT.Records.DmPPTAenderung.getTheAnfordererName;
import DataModel.PPT.Records.DmPPTBCRelease;
import DataModel.Xml.DmXmlStringField;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvXmlContainerElement;

/**
 *
 * @author moe83wi
 */
public class DmEcvBc extends DmElement {

    final private static Logger LOGGER = Logger.getLogger(DmEcvBc.class.getCanonicalName());

    public final DmXmlStringField BC_NAME;
    public final DmXmlStringField BC_VERSION;
    public final DmEcvField_Reference_BC_FC BC_FC;

    public final DmPPTValueField_Text BC_DESCRIPTION;
    public final DmPPTValueField_Text BC_RESPONSIBLE_ONLY_NAME;
    public final DmPPTValueField_Text BC_RESPONSIBLE_FULLNAME;
    private String bcLongNameGerman = null;

    public final DmPPTValueField_Text BC_ID;
    public final DmPPTValueField_Text BC_PREDECESSOR_ID;
    public final DmPPTValueField_Text BC_PREDECESSOR_VERSION;

    public DmEcvBc(String elementType, EcvXmlContainerElement container) {
        super("Dm PST Bc");
        addField(BC_FC = new DmEcvField_Reference_BC_FC(this, container, "ProPlaTo:  FCs"));

        //ProPlaTo View
        addField(BC_ID = new DmPPTValueField_Text(this, "", "ProPlaTo: <ID> "));
        addField(BC_NAME = new DmXmlStringField(this, "Name", "ProPlaTo: <Name> "));
        addField(BC_VERSION = new DmXmlStringField(this, "Version", "ProPlaTo:  <Version> "));

        addField(BC_RESPONSIBLE_ONLY_NAME = new DmPPTValueField_Text(this, "unbekannt", "ProPlaTo: <Paketverantwortlicher> "));
        addField(BC_PREDECESSOR_ID = new DmPPTValueField_Text(this, "", "ProPlaTo: <VorgaengerID> "));
        addField(BC_PREDECESSOR_VERSION = new DmPPTValueField_Text(this, "", "ProPlaTo:  <VorgaengerVersion> "));

        //Internal View
        addField(BC_DESCRIPTION = new DmPPTValueField_Text(this, "", "Internal:  Description"));
        addField(BC_RESPONSIBLE_FULLNAME = new DmPPTValueField_Text(this, "unbekannt", "Internal: Responsible Fullname"));
    }

    public DmEcvBc(DmPPTBCRelease bcInRq1) {
        this("Dm PST BC", null);
        updateInformation(bcInRq1);
    }

    public final void updateInformation(DmPPTBCRelease bcInRq1) {
        this.BC_NAME.setValue(bcInRq1.getName());
        this.BC_VERSION.setValue(bcInRq1.getVersion());

        if (bcInRq1.BC_DESCRIPTION.getValueAsText().isEmpty() == false) {
            this.BC_DESCRIPTION.setValue(bcInRq1.BC_DESCRIPTION.getValueAsText());
        }
        if (bcInRq1.BC_RESPONSIBLE.getValueAsText().isEmpty() == false) {
            String resp = bcInRq1.BC_RESPONSIBLE.getValueAsText();
            this.BC_RESPONSIBLE_ONLY_NAME.setValue(getTheAnfordererName(resp));
            this.BC_RESPONSIBLE_FULLNAME.setValue(resp);
        }
        if (bcInRq1.getLangbezeichnung().isEmpty() == false) {
            setBcLongNameGerman(bcInRq1.getLangbezeichnung());
        }
        this.BC_ID.setValue(bcInRq1.BC_ID.getValueAsText());
        DmPPTBCRelease bcPredecessor = bcInRq1.getPredecessor();
        if (bcPredecessor != null) {
            this.BC_PREDECESSOR_ID.setValue(bcPredecessor.BC_ID.getValueAsText());
            this.BC_PREDECESSOR_VERSION.setValue(bcPredecessor.BC_VERSION.getValueAsText());
        }
    }

    public final void enhanceInformation(DmEcvBcLongNameTable bcLongnames, DmEcvFcLongNameTable fcLongnames, DmEcvBcResponsibleTable bcResponsibles) {
        String longname = bcLongnames.getLongNameGerman(this.BC_NAME.getValueAsText());
        String responsible = bcResponsibles.getResponsible(this.BC_NAME.getValueAsText());
        String description = bcResponsibles.getDescription(this.BC_NAME.getValueAsText());

        if (longname.isEmpty() == false) {
            setBcLongNameGerman(longname);
        } else {
            setBcLongNameGerman(BC_NAME.getValueAsText());
        }
        if (responsible.isEmpty() == false) {
            this.BC_RESPONSIBLE_ONLY_NAME.setValue(getTheAnfordererName(responsible));
            this.BC_RESPONSIBLE_FULLNAME.setValue(responsible);
        }
        if (description.isEmpty() == false) {
            this.BC_DESCRIPTION.setValue(description);
        }
        for (DmEcvFc fc : this.BC_FC.getElementList()) {
            fc.enhanceInformation(fcLongnames, this);
        }
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        return this.BC_NAME.getValueAsText();
    }

    private void setBcLongNameGerman(String bcLongName) {
        if ((bcLongName == null) || (bcLongName.isEmpty())) {
            LOGGER.log(Level.SEVERE, "Empty bcLongName", new Exception("Empty bcLongName"));
        } else {
            bcLongNameGerman = bcLongName;
        }
    }

    public String getBcLongNameGerman() {
        if (bcLongNameGerman == null) {
            LOGGER.warning("bcLongNameGerman null for " + BC_NAME.getValueAsText());
            return ("");
        }
        return bcLongNameGerman;
    }

    @Override
    public String getId() {
        return this.BC_ID.getValueAsText();
    }

    public DmEcvBc getCopy() {
        DmEcvBc bc = new DmEcvBc("Dm Ecv Bc", null);
        bc.BC_NAME.setValue(this.BC_NAME.getValueAsText());
        bc.BC_VERSION.setValue(this.BC_VERSION.getValueAsText());
        for (DmEcvFc fc : this.BC_FC.getElementList()) {
            bc.BC_FC.addElement(fc.getCopy());
        }
        return bc;
    }

}
