/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Boolean;
import DataModel.ALM.Fields.DmAlmField_Number;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord;

/**
 *
 * @author GUG2WI
 */
public class DmAlmWorkitem_Feature extends DmAlmWorkitem {

    public static final String ELEMENT_TYPE = "Feature";

    final public DmAlmField_Text STATUS;
    final public DmAlmField_Text BENEFIT_HYPOTHESIS;
    final public DmAlmField_Number ESTIMATED_STORY_POINTS;
    final public DmAlmField_Number ACTUAL_STORY_POINTS;
    final public DmAlmField_Boolean MVP;

    public DmAlmWorkitem_Feature(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);

        BENEFIT_HYPOTHESIS = addTextField("dcterms:description", "Benefit Hypothesis");
        ESTIMATED_STORY_POINTS = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.estimatedStoryPoints", "Estimated Story Points");
        ACTUAL_STORY_POINTS = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.actualStoryPoints", "Actual Story Points");
        MVP = addBooleanField("rtc_ext:com.ibm.team.workitem.attribute.mvp", "MVP");
        STATUS = addTextField("oslc_cm:status", "Status");

        addResourceListField("rtc_cm:com.ibm.team.workitem.linktype.blocksworkitem.blocks", "Blocks Workitem", true);

        checkForUnusedFields();
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }

}
