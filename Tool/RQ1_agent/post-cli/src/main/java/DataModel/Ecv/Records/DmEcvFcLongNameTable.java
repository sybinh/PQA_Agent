/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Ecv.Records;

import DataModel.Xml.DmXmlTable;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import util.EcvXmlContainerElement;

/**
 *
 * @author mos83wi
 */
public class DmEcvFcLongNameTable extends DmXmlTable<DmEcvFcLongName> {

    private static final Logger LOGGER = Logger.getLogger(DmEcvFcLongNameTable.class.getCanonicalName());

    private final TreeMap<String, DmEcvFcLongName> map;
    private final Set<String> emptyLongNames = new TreeSet<>();
    private final Set<String> missingLongNames = new TreeSet<>();

    public DmEcvFcLongNameTable() {
        map = new TreeMap<>();
    }

    @Override
    protected DmEcvFcLongName createElement(EcvXmlContainerElement elem) {
        return new DmEcvFcLongName("Dm Ecv Fc LongName");
    }

    @Override
    protected void initTree() {
        for (DmEcvFcLongName entry : getElements()) {
            if (entry != null && entry.FC_NAME != null) {
                if (!map.containsKey(entry.FC_NAME.getValueAsText().toLowerCase())) {
                    map.put(entry.FC_NAME.getValueAsText().toLowerCase(), entry);
                }
            }
        }
    }

    public String getLongNameGerman(String fcName) {
        assert (fcName != null);
        assert (fcName.isEmpty() == false);
        String lowCaseFcName = fcName.toLowerCase();
        if (map.containsKey(lowCaseFcName)) {
            String longName = map.get(lowCaseFcName).FC_LONGNAME_GERMAN.getValueAsText();
            if (longName.isEmpty()) {
                if (emptyLongNames.contains(fcName) == false) {
                    LOGGER.warning("Empty long name for FC " + fcName);
                    emptyLongNames.add(fcName);
                }

            }
            return longName;
        } else {
            if (missingLongNames.contains(fcName) == false) {
                LOGGER.warning("No long name found for FC " + fcName);
                missingLongNames.add(fcName);
            }
            return fcName;
        }

    }

    public boolean isLoaded() {
        return !map.isEmpty();
    }

}
