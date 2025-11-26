/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmConstantField_Date;
import DataModel.DmConstantField_Text;
import DataModel.Rq1.Records.DmRq1Project;
import RestClient.HIS.RestClient_HisMilestone;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import Rq1Data.GPM.GpmXmlTable_DataOfExternalMilestones;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class DmGpmMilestone_FromHis extends DmGpmMilestone_FromExternalDatabase {

    final private String id;

    final public DmConstantField_Text TARGET_ECU_ID;
    final public DmConstantField_Text TARGET_ECU_NAME;

    public DmGpmMilestone_FromHis(DmRq1Project dmRq1Project, RestClient_HisMilestone hisMilestone) {
        super(getElementType(hisMilestone), getElementType(hisMilestone).getText(), GpmXmlMilestoneSource.HIS, dmRq1Project);

        id = hisMilestone.getEcuHardwareId() + "/" + hisMilestone.getMilestoneName();

        String name = hisMilestone.getMilestoneName() + " - " + hisMilestone.getTargetEcuName() + " (" + hisMilestone.getEcuHardwareId() + ")";

        addField(NAME = new DmConstantField_Text("Name", name));
        addField(DATE = new DmConstantField_Date("Date", hisMilestone.getDate() != null ? hisMilestone.getDate() : EcvDate.getEmpty()));

        addField(TARGET_ECU_ID = new DmConstantField_Text("Target ECU ID", hisMilestone.getEcuHardwareId()));
        addField(TARGET_ECU_NAME = new DmConstantField_Text("Target ECU Name", hisMilestone.getTargetEcuName()));

        addField(ALL_WORKITEMS = new DmGpmFieldOnMilestone_Workitems("All Workitems", project, GpmXmlMilestoneSource.HIS, id, NAME, project.ALL_WORKITEMS));

    }

    public DmGpmMilestone_FromHis(DmRq1Project dmRq1Project, GpmXmlTable_DataOfExternalMilestones.Record cachedMilestone) {
        super(getElementType(cachedMilestone), getElementType(cachedMilestone).getText(), GpmXmlMilestoneSource.HIS, dmRq1Project);
        assert (cachedMilestone.getSource() == GpmXmlMilestoneSource.HIS);

        //
        // Get available data from id
        //
        String ecuHardwareId;
        String milestoneName;
        id = cachedMilestone.getId();
        int index = id.indexOf("/");
        if (index > 0) {
            ecuHardwareId = id.substring(0, index);
            milestoneName = id.substring(index + 1, id.length());
        } else {
            ecuHardwareId = "?";
            milestoneName = id;
        }

        String name = milestoneName + " - ? (" + ecuHardwareId + ")";

        addField(NAME = new DmConstantField_Text("Name", name));
        addField(DATE = new DmConstantField_Date("Date", cachedMilestone.getLastConfirmedDate() != null ? cachedMilestone.getLastConfirmedDate() : EcvDate.getEmpty()));

        addField(TARGET_ECU_ID = new DmConstantField_Text("Target ECU ID", ecuHardwareId));
        addField(TARGET_ECU_NAME = new DmConstantField_Text("Target ECU Name", "?"));

        addField(ALL_WORKITEMS = new DmGpmFieldOnMilestone_Workitems("All Workitems", project, GpmXmlMilestoneSource.HIS, id, NAME, project.ALL_WORKITEMS));

        super.addMarker(new DmGpmWarning_ExternalMilestoneLoadedFromCache(this, GpmXmlMilestoneSource.HIS.getValueInDatabase()));
    }

    @Override
    public String getId() {
        return (id);
    }

    /**
     * This id was used for the storing of milestones related information.
     *
     * @return
     */
    @Override
    protected String getOldId() {
        return (TARGET_ECU_ID.getValueAsText() + "/" + NAME.getValueAsText());
    }

    static private Type getElementType(RestClient_HisMilestone hisMilestone) {
        assert (hisMilestone != null);

        switch (hisMilestone.getType()) {
            case MILESTONE:
                return (Type.HIS_MILESTONE);

            case QUALITY_GATE:
                return (Type.HIS_QUALITY_GATE);

            default:
                throw (new Error("Unexpected type: " + hisMilestone.getType().name()));
        }
    }

    static private Type getElementType(GpmXmlTable_DataOfExternalMilestones.Record cachedMilestone) {

        String typeString = cachedMilestone.getCachedDatabaseSpecificType();
        if ((typeString == null) || (typeString.isEmpty() == true)) {
            return (Type.HIS_UNKNOWN);
        }

        Type t = Type.getTypeForText(cachedMilestone.getCachedDatabaseSpecificType());
        if (t != null) {
            return (t);
        } else {
            return (Type.HIS_UNKNOWN);
        }
    }

}
