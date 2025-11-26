/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Monitoring;

import DataModel.ALM.Records.DmAlmWorkitem_Capability;
import DataModel.Monitoring.DmRule;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author CNI83WI
 */
public class Rule_Capability_CheckReleaseNoteLink extends DmRule<DmAlmWorkitem_Capability> {

    @EcvElementList("DataModel.ALM.Monitoring.DmAlmRuleDescription")
    static final public DmAlmRuleDescription description = new DmAlmRuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "Capability, check, if Release Note is linked",
            "Creates a warning, if no Release Note is linked to Capability");

    public Rule_Capability_CheckReleaseNoteLink(DmAlmWorkitem_Capability capability) {
        super(description, capability);
    }

    @Override
    protected void executeRule() {    
        //only check capabilities, that are in rollout (check for "pmt-rollout-cat-*"-Tag)
        //"pmt-no-release-note"-Tag means, no release note required
        if (dmElement.TAGS.getValueAsText().contains("pmt-rollout-cat") == false || dmElement.TAGS.getValueAsText().contains("pmt-no-release-note")) {
            return;
        }
        
        //ignore Enabler Topics
        if("Enabler".equals(dmElement.SAFE_WORKTYPE.getValueAsText())) {
            return;
        }
        
        //check, if there is no release note at all
        if (dmElement.RELATED_ARTIFACT.getValueAsText().equals("")) {
            addMarker(dmElement, new Warning(this, "Release Notes missing.", "No Release Notes linked to Capability."));
            return;
        }
        
        //count if number of Release Notes has a 1-1 mapping with pmt product tags
        int productCount = 0;
        String[] tags = dmElement.TAGS.getValueAsText().split(",");
        
        //count number of pmt product tags
        for(String productTag : tags) {
            if(productTag.contains("pmt-po-")) {
                productCount++;
            }
        }
        
        int releaseNoteCount = 0;
        List<String> relatedArtifacts = dmElement.RELATED_ARTIFACT.getUrlList();
        
        //count number of release notes
        //Note: only URLs linked to Bosch Connect are release Notes
        for(String url : relatedArtifacts) {
            if(url.contains("connect.bosch.com")) {
                releaseNoteCount++;
            }
        }
        
        if(releaseNoteCount < productCount) {
            addMarker(dmElement, new Warning(this, "Release Notes missing.", "Number of linked Release Notes is not equal to number of products in Capability."));
        }

    }

}
