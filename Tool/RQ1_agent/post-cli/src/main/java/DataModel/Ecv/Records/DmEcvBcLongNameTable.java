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
public class DmEcvBcLongNameTable extends DmXmlTable<DmEcvBcLongName> {

    final private static Logger LOGGER = Logger.getLogger(DmEcvBcLongNameTable.class.getCanonicalName());

    private final TreeMap<String, DmEcvBcLongName> map;
    private final Set<String> emptyLongNames = new TreeSet<>();
    private final Set<String> missingLongNames = new TreeSet<>();

    public DmEcvBcLongNameTable() {
        map = new TreeMap<>();
    }

    @Override
    protected DmEcvBcLongName createElement(EcvXmlContainerElement elem) {
        return new DmEcvBcLongName("Dm Ecv Bc LongName");
    }

    @Override
    protected void initTree() {
        for (DmEcvBcLongName entry : getElements()) {
            if (!map.containsKey(entry.BC_NAME.getValueAsText().toLowerCase())) {
                map.put(entry.BC_NAME.getValueAsText().toLowerCase(), entry);
            }
        }
    }

    public String getLongNameEnglish(String bcName) {
        assert (bcName != null);
        assert (bcName.isEmpty() == false);
        String lowCaseBcName = bcName.toLowerCase();
        if (map.containsKey(lowCaseBcName)) {
            String longName = map.get(lowCaseBcName).BC_LONGNAME_ENGLISH.getValueAsText();
            if (longName.isEmpty()) {
                if (emptyLongNames.contains(bcName) == false) {
                    LOGGER.warning("Empty long name for BC " + bcName);
                    emptyLongNames.add(bcName);
                }

            }
            return longName;
        } else {
            if (missingLongNames.contains(bcName) == false) {
                LOGGER.warning("No long name found for BC " + bcName);
                missingLongNames.add(bcName);
            }
            return bcName;
        }
    }

    public String getLongNameGerman(String bcName) {
        assert (bcName != null);
        assert (bcName.isEmpty() == false);
        String lowCaseBcName = bcName.toLowerCase();
        if (map.containsKey(lowCaseBcName)) {
            return map.get(lowCaseBcName).BC_LONGNAME_GERMAN.getValueAsText();
        } else {
            return bcName;
        }
    }

    public boolean isLoaded() {
        return !map.isEmpty();
    }

}
