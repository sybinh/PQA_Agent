/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmField;
import DataModel.DmValueFieldI_Text;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_Hauptentwicklungspaket extends DmField implements DmValueFieldI_Text {

//    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    final private Rq1FieldI_Text rq1Hauptentwicklungspaket;
    final private Rq1FieldI_Text rq1ExternalTags;
    private Rq1FieldI_Text source = null;

    static private Pattern paketPattern = null;

    public DmRq1Field_Hauptentwicklungspaket(DmElementI parent, Rq1FieldI_Text rq1Hauptentwicklungspaket, Rq1FieldI_Text rq1ExternalTags, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (rq1Hauptentwicklungspaket != null);
        assert (rq1ExternalTags != null);

        this.rq1ExternalTags = rq1ExternalTags;
        this.rq1Hauptentwicklungspaket = rq1Hauptentwicklungspaket;
    }

    @Override
    public boolean isReadOnly() {
        if (source == null) {
            getValue();
        }
        if (source == rq1ExternalTags) {
            return (true);
        } else {
            return (source.isReadOnly());
        }
    }

    @Override
    public String getValue() {
        String v = extractFromExternalTags(rq1ExternalTags.getDataModelValue());
        if (v != null) {
            source = rq1ExternalTags;
        } else {
            source = rq1Hauptentwicklungspaket;
            v = rq1Hauptentwicklungspaket.getDataModelValue();
        }
        return (v);
    }

    @Override
    public void setValue(String v) {
        if (source == null) {
            getValue();
        }
        if (source == rq1ExternalTags) {
            throw (new Error("setValue() not allowed. New Value = >" + v + "<"));
        } else {
            source.setDataModelValue(v);
        }
    }

    /**
     * Made static public for test reasons only.
     *
     * @param tags
     * @return
     */
    static public String extractFromExternalTags(String tags) {
        assert (tags != null);

        if (paketPattern == null) {
            paketPattern = Pattern.compile("(Konzernarbeitspaket:)([0-9]*\\.)?(.*)");
        }

        Matcher matcher = paketPattern.matcher(tags);
        matcher.useAnchoringBounds(true);

        if (matcher.find() == true) {
            return (matcher.group(3).trim());
        } else {
            return (null);
        }

    }

}
