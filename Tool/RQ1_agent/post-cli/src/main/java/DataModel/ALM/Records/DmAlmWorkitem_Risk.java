/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Enumeration;
import DataModel.ALM.Fields.DmAlmField_Number;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord;

/**
 *
 * @author GUG2WI
 */
public class DmAlmWorkitem_Risk extends DmAlmWorkitem {

    public static final String ELEMENT_TYPE = "Risk";

    final public DmAlmField_Text STATUS;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Enumeration PROBABILITY;
    final public DmAlmField_Enumeration PROBABILITY_RISK;
    final public DmAlmField_Enumeration IMPACT;
    final public DmAlmField_Enumeration IMPACT_RISK;
    final public DmAlmField_Number EXPOSURE;
    final public DmAlmField_Number EXPOSURE_RISK;
    final public DmAlmField_Enumeration CATEGORY;
    final public DmAlmField_Enumeration CATEGORY_RISK;

    public DmAlmWorkitem_Risk(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);
        
        STATUS = addTextField("oslc_cm:status", "Status");
        DESCRIPTION = addTextField("dcterms:description", "Description");
        PROBABILITY = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.probability", "Probability", true);
        PROBABILITY_RISK = addEnumerationField("rtc_ext:com.ibm.team.workitem.workItemType.risk.probability", "Risk Probability", true);
        IMPACT = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.impact", "Impact", true);
        IMPACT_RISK = addEnumerationField("rtc_ext:com.ibm.team.workitem.workItemType.risk.impact", "Risk Impact", true);
        EXPOSURE = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.exposure", "Exposure", true);
        EXPOSURE_RISK = addNumberField("rtc_ext:com.ibm.team.workitem.workItemType.risk.exposure", "Risk Exposure", true);
        CATEGORY = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.riskCategory", "Category", true);
        CATEGORY_RISK = addEnumerationField("rtc_ext:com.ibm.team.workitem.workItemType.risk.riskcategory", "Risk Category", true);
        
        checkForUnusedFields();
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }

}
