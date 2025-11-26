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
public class DmAlmWorkitem_Story extends DmAlmWorkitem {

    public static final String ELEMENT_TYPE = "Story";

    final public DmAlmField_Text STATUS;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Number STORY_POINTS;
    final public DmAlmField_Enumeration ACCEPTANCE_TEST;
    final public DmAlmField_Enumeration DOCUMENTATION;
    final public DmAlmField_Enumeration PERFORMANCE_TEST;
    final public DmAlmField_Enumeration UX_DESIGN;

    public DmAlmWorkitem_Story(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);

        DESCRIPTION = addTextField("dcterms:description", "Description");
        STORY_POINTS = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.storyPointsNumeric", "Story Points", true);
        ACCEPTANCE_TEST = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.acceptanceTest", "Acceptance Test", true);
        DOCUMENTATION = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.doccumentation", "Documentation", true);
        PERFORMANCE_TEST = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.performanceTest", "Performance Test", true);
        UX_DESIGN = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.uxDesign", "UX Design", true);
        STATUS = addTextField("oslc_cm:status", "Status", true);

        checkForUnusedFields();
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }

}
