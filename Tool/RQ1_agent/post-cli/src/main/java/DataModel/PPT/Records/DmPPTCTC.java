/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Records;

import DataModel.DmElement;
import DataModel.PPT.Fields.DmPPTField_Reference;
import DataModel.PPT.Fields.DmPPTValueField_Text;
import Rq1Cache.Types.Rq1XmlTable_ChangesToConfiguration;
import java.util.Objects;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTCTC extends DmElement {

    public final DmPPTField_Reference<DmPPTAenderung> PPT_ISSUE;
    public final DmPPTField_Reference<DmPPTBCRelease> PPT_BC;
    public final DmPPTField_Reference<DmPPTRelease> PPT_RELEASE;

    public final EcvTableRow ctc;

    public final DmPPTValueField_Text CTC_TYPE;
    public final DmPPTValueField_Text CTC_NAME;
    public final DmPPTValueField_Text CTC_BASE;
    public final DmPPTValueField_Text CTC_TARGET;
    public final DmPPTValueField_Text CTC_EXTERNAL_DESCRIPTION;

    //CTC between Reelase and BC
    public DmPPTCTC(Rq1XmlTable_ChangesToConfiguration desc, EcvTableRow ctc, DmPPTRelease release, DmPPTBCRelease bc) {
        this(desc, ctc, release, bc, null);
    }

    public DmPPTCTC(Rq1XmlTable_ChangesToConfiguration desc, EcvTableRow ctc, DmPPTRelease release, DmPPTAenderung issue) {
        this(desc, ctc, release, null, issue);
    }

    public DmPPTCTC(Rq1XmlTable_ChangesToConfiguration desc, EcvTableRow ctc, DmPPTRelease release, DmPPTBCRelease bc, DmPPTAenderung issue) {
        super("DataModel ProPlaTo CTC");
        this.ctc = ctc;
        addField(PPT_ISSUE = new DmPPTField_Reference<>(this, issue, "CTC Issue"));
        addField(PPT_BC = new DmPPTField_Reference<>(this, bc, "CTC BC"));
        addField(PPT_RELEASE = new DmPPTField_Reference<>(this, release, "CTC Release"));
        if (ctc.getValueAt(desc.TYPE) != null) {
            addField(CTC_TYPE = new DmPPTValueField_Text(this, ctc.getValueAt(desc.TYPE).toString(), "CTC Type"));
        } else {
            addField(CTC_TYPE = new DmPPTValueField_Text(this, "", "CTC Type"));
        }

        if (ctc.getValueAt(desc.NAME) != null) {
            addField(CTC_NAME = new DmPPTValueField_Text(this, ctc.getValueAt(desc.NAME).toString(), "CTC Name"));
        } else {
            addField(CTC_NAME = new DmPPTValueField_Text(this, "", "CTC Name"));
        }

        if (ctc.getValueAt(desc.BASE) != null) {
            addField(CTC_BASE = new DmPPTValueField_Text(this, ctc.getValueAt(desc.BASE).toString(), "CTC Base"));
        } else {
            addField(CTC_BASE = new DmPPTValueField_Text(this, "", "CTC Base"));
        }

        if (ctc.getValueAt(desc.TARGET) != null) {
            addField(CTC_TARGET = new DmPPTValueField_Text(this, ctc.getValueAt(desc.TARGET).toString(), "CTC Target"));
        } else {
            addField(CTC_TARGET = new DmPPTValueField_Text(this, "", "CTC Target"));
        }

        if (ctc.getValueAt(desc.EXTERNAL_COMMENT) != null) {
            addField(CTC_EXTERNAL_DESCRIPTION = new DmPPTValueField_Text(this, ctc.getValueAt(desc.EXTERNAL_COMMENT).toString(), "CTC External Comment"));
        } else {
            addField(CTC_EXTERNAL_DESCRIPTION = new DmPPTValueField_Text(this, "", "CTC External Comment"));
        }
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTitle() {
        return this.CTC_NAME.getValueAsText();
    }

    @Override
    public String getId() {
        return this.CTC_NAME.getValueAsText();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.CTC_TYPE.getValue());
        hash = 71 * hash + Objects.hashCode(this.CTC_NAME.getValue());
        hash = 71 * hash + Objects.hashCode(this.CTC_BASE.getValue());
        hash = 71 * hash + Objects.hashCode(this.CTC_TARGET.getValue());
        hash = 71 * hash + Objects.hashCode(this.CTC_EXTERNAL_DESCRIPTION.getValue());
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
        final DmPPTCTC other = (DmPPTCTC) obj;
        if (!Objects.equals(this.CTC_TYPE.getValue(), other.CTC_TYPE.getValue())) {
            return false;
        }
        if (!Objects.equals(this.CTC_NAME.getValue(), other.CTC_NAME.getValue())) {
            return false;
        }
        if (!Objects.equals(this.CTC_BASE.getValue(), other.CTC_BASE.getValue())) {
            return false;
        }
        if (!Objects.equals(this.CTC_TARGET.getValue(), other.CTC_TARGET.getValue())) {
            return false;
        }
        if (!Objects.equals(this.CTC_EXTERNAL_DESCRIPTION.getValue(), other.CTC_EXTERNAL_DESCRIPTION.getValue())) {
            return false;
        }
        return true;
    }

}
