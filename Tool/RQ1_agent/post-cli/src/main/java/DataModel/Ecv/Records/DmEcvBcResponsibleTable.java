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
 * @author moe83wi
 */
public class DmEcvBcResponsibleTable extends DmXmlTable<DmEcvBcResponsible> {

    final private static Logger LOGGER = Logger.getLogger(DmEcvBcResponsibleTable.class.getCanonicalName());

    private ProjectType currentType;
    private final TreeMap<String, DmEcvBcResponsible> map;
    private final Set<String> missingResponsible = new TreeSet<>();

    public DmEcvBcResponsibleTable() {
        currentType = ProjectType.DS;
        map = new TreeMap<>();
    }

    @Override
    protected DmEcvBcResponsible createElement(EcvXmlContainerElement elem) {
        return new DmEcvBcResponsible("BC Verantwortliche");
    }

    public enum ProjectType {
        GS, DS
    }

    public void setCompanyDataId(String companyDataId) {
        assert (companyDataId != null);
        switch (companyDataId) {
            case "RB-DS":
                this.setProjectType(ProjectType.DS);
                break;
            case "RB-GS":
                this.setProjectType(ProjectType.GS);
                break;
        }
    }

    private void setProjectType(ProjectType type) {
        assert (type != null);
        this.currentType = type;
    }

    public String getResponsible(String bcName) {
        assert (bcName != null);
        assert (bcName.isEmpty() == false);
        String lowCaseBcName = bcName.toLowerCase();
        String retval = "";
        if (map.containsKey(lowCaseBcName)) {
            if (this.currentType == ProjectType.DS) {
                retval = map.get(lowCaseBcName).DS_RESPONSIBLE.getValueAsText();
            }
            if (this.currentType == ProjectType.GS) {
                retval = map.get(lowCaseBcName).GS_RESPONSIBLE.getValueAsText();
            }
//            retval = retval.replaceAll("\\(([^)])*\\)", "");
//            retval = retval.replace(")", "");
//            retval = retval.replace("(", "");
        }
        if (retval.isEmpty()) {
            if (missingResponsible.contains(bcName) == false) {
                LOGGER.warning("No responsible found for BC " + bcName);
                missingResponsible.add(bcName);
            }
            return "unbekannt";
        }
        return retval;
    }

    public String getDescription(String bcName) {
        assert (bcName != null);
        assert (bcName.isEmpty() == false);
        String lowCasebcName = bcName.toLowerCase();
        if (map.containsKey(lowCasebcName)) {
            return map.get(lowCasebcName).BC_DESCRIPTION.getValueAsText();
        }
//        LOGGER.warning("No description found for BC " + bcName);
        return "unbekannt";
    }

    @Override
    protected void initTree() {
        for (DmEcvBcResponsible entry : getElements()) {
            map.put(entry.BC_NAME.getValueAsText().toLowerCase(), entry);
        }
    }

    public boolean isLoaded() {
        return !map.isEmpty();
    }

}
