/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import java.util.EnumSet;

/**
 *
 * @author ths83wi
 */
public enum TitleSuffix {

    TITLE {
        @Override
        public String getSuffixValue(DmRq1Element element) {
            return (element.getTitle());
        }
    },
    ID {
        @Override
        public String getSuffixValue(DmRq1Element element) {
            return (element.getId());
        }
    };

    abstract public String getSuffixValue(DmRq1Element element);

    /**
     * This method returns a suffix for workitems if wanted
     *
     * @param suffixSet EnumSet with Title and Id
     * @param separator Separator for suffix
     * @param element the root element of the workitem
     * @return full suffix
     */
    public static String generateSuffix(EnumSet<TitleSuffix> suffixSet, String separator, DmRq1SubjectElement element) {
        assert (element != null);

        String suffix = "";
        if (suffixSet != null) {
            if (separator == null) {
                separator = "-";
            }
            for (TitleSuffix suf : suffixSet) {
                suffix = suffix + " " + separator + " " + suf.getSuffixValue(element);
            }
        }
        return suffix;
    }

}
