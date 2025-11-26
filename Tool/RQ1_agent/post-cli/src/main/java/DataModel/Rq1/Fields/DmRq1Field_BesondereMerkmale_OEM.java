/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.DmField;
import DataModel.DmValueFieldI_Text;
import Rq1Cache.Fields.Rq1FieldI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A read only field to extract Besondere Merkmale from the External Tags field.
 *
 * @author gug2wi
 */
public class DmRq1Field_BesondereMerkmale_OEM extends DmField implements DmValueFieldI_Text {

    final private Rq1FieldI<String> rq1ExternalTags;
    private final String tag;

    public DmRq1Field_BesondereMerkmale_OEM(DmElementI parent, Rq1FieldI<String> rq1ExternalTags, String tagName, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (rq1ExternalTags != null);
        this.tag = tagName;
        this.rq1ExternalTags = rq1ExternalTags;
    }

    @Override
    public boolean isReadOnly() {
        return (true);
    }

    @Override
    public String getValue() {
        String v = extractFromExternalTags(tag, rq1ExternalTags.getDataModelValue());
        return (v);
    }

    @Override
    public void setValue(String v) {
        throw (new Error("setValue() not allowed. New Value = >" + v + "<"));
    }

    /**
     * Made static public for test reasons only.
     *
     * @param tags
     * @param tag (for each ( and ) there has to be a \ for the regex
     * @return
     */
    static public String extractFromExternalTags(String tag, String tags) {
        assert (tags != null);

        Pattern pattern = Pattern.compile("(" + tag + ":)([0-9]*\\.)?(.*)");

        Matcher matcher = pattern.matcher(tags);
        matcher.useAnchoringBounds(true);

        if (matcher.find() == true) {
            return (matcher.group(3).trim());
        } else {
            return (null);
        }

    }

}
