/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Boolean;
import DataModel.ALM.Fields.DmAlmField_Enumeration;
import DataModel.ALM.Fields.DmAlmField_Number;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord;

/**
 *
 * @author CNI83WI
 */
public class DmAlmWorkitem_PIObjective extends DmAlmWorkitem {

    public static final String ELEMENT_TYPE = "PI Objective";

    final public DmAlmField_Text STATUS;
    final public DmAlmField_Text DESCRIPTION;
    final public DmAlmField_Enumeration PI_TYPE;
    final public DmAlmField_Enumeration BUS_VALUE_PLANNED;
    final public DmAlmField_Enumeration BUS_VALUE_ACTUAL;
    final public DmAlmField_Number ACHIEVED_VALUE;
    final public DmAlmField_Boolean STRETCH_OBJECTIVE;
   
    public DmAlmWorkitem_PIObjective(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);

        DESCRIPTION = addTextField("dcterms:description", "Description");
        STATUS = addTextField("oslc_cm:status", "Status");
        PI_TYPE = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.piType", "PI Objective Type");
        BUS_VALUE_PLANNED = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.busValuePlanned", "Business Value (planned)");
        BUS_VALUE_ACTUAL = addEnumerationField("rtc_ext:com.ibm.team.workitem.attribute.busValueActual", "Business Value (actual)");
        ACHIEVED_VALUE = addNumberField("rtc_ext:com.ibm.team.workitem.attribute.achievedValue", "Achieved Value");
        STRETCH_OBJECTIVE = addBooleanField("rtc_ext:com.ibm.team.workitem.attribute.stretch", "Stretch Objective");

        checkForUnusedFields();
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }
    
}
