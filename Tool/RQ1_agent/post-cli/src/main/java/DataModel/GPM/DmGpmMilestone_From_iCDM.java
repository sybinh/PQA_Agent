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
import RestClient.iCDM.RestClient_iCDM_Milestone;
import Rq1Data.GPM.GpmXmlMilestoneSource;
import Rq1Data.GPM.GpmXmlTable_DataOfExternalMilestones;
import java.util.logging.Level;
import java.util.logging.Logger;

import ToolUsageLogger.ToolUsageLogger;
import util.EcvDate;
import static DataModel.GPM.DmGpmMilestone.Type.I_CDM_MILESTONE;

/**
 *
 * @author GUG2WI
 */
public class DmGpmMilestone_From_iCDM extends DmGpmMilestone_FromExternalDatabase {

    final private static Logger LOGGER = Logger.getLogger(DmGpmMilestone_From_iCDM.class.getCanonicalName());

    final public DmConstantField_Text PIDC_VER_ID;
    final public DmConstantField_Text PIDC_VER_NAME;
    final public DmConstantField_Text ID;
    final public DmConstantField_Text DESCRIPTION;

    final private String id;

    public DmGpmMilestone_From_iCDM(DmRq1Project dmRq1Project, RestClient_iCDM_Milestone icdmMilestone) {
        super(I_CDM_MILESTONE, I_CDM_MILESTONE.getText(), GpmXmlMilestoneSource.I_CDM, dmRq1Project);
        assert (icdmMilestone != null);

        id = icdmMilestone.getPidcVerID() + "/" + icdmMilestone.getId();
        String name = icdmMilestone.getName() + " - " + icdmMilestone.getPidcVersionName() + " (" + icdmMilestone.getPidcVerID() + ")";

        EcvDate date = EcvDate.getEmpty();
        String dateString = icdmMilestone.getDate(); // yyyy.mm.dd
        if (dateString != null) {
            if (dateString.matches("\\d\\d\\d\\d\\.\\d\\d\\.\\d\\d") == false) {
                LOGGER.severe("Invalid date format: " + dateString);
                LOGGER.severe(icdmMilestone.toString());
                date = EcvDate.getEmpty();
            } else {
                String parseableDateString = dateString.substring(0, 4) + "-" + dateString.substring(5, 7) + "-" + dateString.substring(8, 10);
                try {
                    date = EcvDate.parseXmlValue(parseableDateString);
                } catch (EcvDate.DateParseException ex) {
                    LOGGER.log(Level.SEVERE, "Error when decoding date: " + dateString, ex);
                    LOGGER.severe(icdmMilestone.toString());
                    ToolUsageLogger.logError(DmGpmMilestone_From_iCDM.class.getCanonicalName(), ex);
                    date = EcvDate.getEmpty();
                }
            }
        }

        addField(NAME = new DmConstantField_Text("Name", name));
        addField(DATE = new DmConstantField_Date("Date", date));

        addField(PIDC_VER_ID = new DmConstantField_Text("PIDC Version ID", icdmMilestone.getPidcVerID()));
        addField(PIDC_VER_NAME = new DmConstantField_Text("Project ID Card Version Name", icdmMilestone.getPidcVersionName()));
        addField(ID = new DmConstantField_Text("Milestone-ID", Integer.toString(icdmMilestone.getId())));
        addField(DESCRIPTION = new DmConstantField_Text("Description", icdmMilestone.getDescription() != null ? icdmMilestone.getDescription() : ""));

        addField(ALL_WORKITEMS = new DmGpmFieldOnMilestone_Workitems("All Workitems", project, GpmXmlMilestoneSource.I_CDM, id, NAME, project.ALL_WORKITEMS));

    }

    public DmGpmMilestone_From_iCDM(DmRq1Project dmRq1Project, GpmXmlTable_DataOfExternalMilestones.Record cachedMilestone) {
        super(I_CDM_MILESTONE, I_CDM_MILESTONE.getText(), GpmXmlMilestoneSource.I_CDM, dmRq1Project);
        assert (cachedMilestone.getSource() == GpmXmlMilestoneSource.I_CDM);

        id = cachedMilestone.getId();
        String[] idParts = id.split("/");
        String pidcVerId = idParts.length >= 1 ? idParts[0] : "";
        String milestoneId = idParts.length >= 2 ? idParts[1] : "";

        addField(NAME = new DmConstantField_Text("Name", id));
        addField(DATE = new DmConstantField_Date("Date", EcvDate.getNotNull(cachedMilestone.getLastConfirmedDate())));

        addField(PIDC_VER_ID = new DmConstantField_Text("PidcVerID", pidcVerId));
        addField(PIDC_VER_NAME = new DmConstantField_Text("PidcVerName", ""));
        addField(ID = new DmConstantField_Text("ID", milestoneId));
        addField(DESCRIPTION = new DmConstantField_Text("Description", ""));

        addField(ALL_WORKITEMS = new DmGpmFieldOnMilestone_Workitems("All Workitems", project, GpmXmlMilestoneSource.I_CDM, id, NAME, project.ALL_WORKITEMS));

        super.addMarker(new DmGpmWarning_ExternalMilestoneLoadedFromCache(this, GpmXmlMilestoneSource.I_CDM.getValueInDatabase()));
    }

    @Override
    public String getId() {
        return (id);
    }

}
