/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Monitoring;

import ConfigurableRules.ConfigRule.util.Constants;
import ConfigurableRules.ConfigRule.util.Utils;
import DataModel.Doors.Records.DmDoorsObject;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1Issue;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Requirements.DmRq1Field_DoorsRequirementsOnIssueFromTables;
import util.EcvTableRow;

/**
 *
 * @author RHO2HC
 */
public class ConfigurableRuleManagerRule_DoorsObject extends ConfigurableRuleManagerRule<DmDoorsObject> {

    public ConfigurableRuleManagerRule_DoorsObject(DmDoorsObject doorsObject) {
        super(doorsObject);
    }

    @Override
    protected boolean checkAssignedElementIsInTheGivenProject(DmRq1Project project) {
        boolean isInTheGivenProject = false;

        // Go through each Issues mapped with current Doors object
        for (DmRq1ElementInterface rq1Element : dmElement.REFERENCED_RQ1_ELEMENTS.getElementList()) {

            if (rq1Element instanceof DmRq1Issue) {
                DmRq1Issue issue = (DmRq1Issue) rq1Element;

                if ((project != null) && (project == issue.getProject())) {
                    isInTheGivenProject = true;
                }
            }
        }

        return (isInTheGivenProject);
    }
    
    /**
     * Get the level of Doors object. There are 4 level 1 2 3 4.
     * @param table : table of Doors object map to current RQ1 object
     * @param thisUrl : url of current Doors object
     * @return 
     */
    private String getRq1DoorsLevel(DmRq1Field_DoorsRequirementsOnIssueFromTables table, String thisUrl) {
        thisUrl = Utils.editDoorsLink(thisUrl);

        // In Issue, go throw each Doors requirement to find the one map with current Doors object
        for (EcvTableRow row : table.getValue().getRows()) {
            String level = row.getValueAt(0).toString();
            String url = row.getValueAt(4).toString();

            url = Utils.editDoorsLink(url);

            // RQ1 object taht map with current Doors object is found -> return its level
            if (url.startsWith(thisUrl)) {
                return level;
            }
        }
        return Constants.BLANK;
    }
    
    /**
     * Update RQ1 level for Doors object
     */
    @Override
    public void updateRq1Level() {
        // Go through each Issues mapped with current Doors object
        for (DmRq1ElementInterface rq1Element : dmElement.REFERENCED_RQ1_ELEMENTS.getElementList()) {

            if (rq1Element instanceof DmRq1Issue) {
                DmRq1Issue issue = (DmRq1Issue) rq1Element;

                if (issue instanceof DmRq1IssueFD) {
                    dmElement.rq1Level = getRq1DoorsLevel(((DmRq1IssueFD) issue).MAPPED_DOORS_REQUIREMENTS, dmElement.getUrl());
                } else if (issue instanceof DmRq1IssueSW) {
                    dmElement.rq1Level = getRq1DoorsLevel(((DmRq1IssueSW) issue).MAPPED_DOORS_REQUIREMENTS, dmElement.getUrl());
                }
            }
        }
    }
}
