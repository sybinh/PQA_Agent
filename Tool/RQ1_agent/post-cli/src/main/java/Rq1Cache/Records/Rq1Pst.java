/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import DataModel.Dgs.DmDgsBcRelease;
import DataModel.Dgs.DmDgsBcReleaseI;
import DataModel.Dgs.DmDgsFcRelease;
import DataModel.Dgs.DmDgsFcReleaseI;
import DataModel.Flow.ExpertState;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.KingState;
import DataModel.Flow.TaskStatus;
import DataStore.DsField_Xml;
import Rq1Cache.Fields.Rq1DatabaseField_DerivativeMapping;
import Rq1Cache.Fields.Rq1DatabaseField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_DerivativesDate;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Types.Rq1Reference;
import Rq1Cache.Types.Rq1XmlTable_CriticalResource;
import Rq1Cache.Types.Rq1XmlTable_FLowCCPMConfiguraion;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Cache.Types.Rq1XmlTable_MissingPlanningForOpt;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsValues;
import Rq1Data.Enumerations.ProPlaTo_KaufmaennischGeplant;
import Rq1Data.Enumerations.ProPlaTo_Programmstandskennung;
import Rq1Data.Enumerations.QualityMeasureState;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import util.EcvMapList;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class Rq1Pst extends Rq1SoftwareRelease {

    final public static String CATEGORY_SW_VERSION = "SW-Version";
    final public static String CATEGORY_MIGRATION_BASELINE = "Migration-Baseline";

    final private static java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Rq1Pst.class.getCanonicalName());

    final public Rq1DatabaseField_Enumeration SWTEST_BFT;
    final public Rq1DatabaseField_Enumeration SWTEST_COM;
    final public Rq1DatabaseField_Enumeration SWTEST_CST;
    final public Rq1DatabaseField_Enumeration SWTEST_EEPROM;
    final public Rq1DatabaseField_Enumeration SWTEST_FT;
    final public Rq1DatabaseField_Enumeration SWTEST_IO;
    final public Rq1DatabaseField_Enumeration SWTEST_OPT;
    final public Rq1DatabaseField_Enumeration SWTEST_OST;
    final public Rq1DatabaseField_Enumeration SWTEST_PVER_CONF;
    final public Rq1DatabaseField_Enumeration SWTEST_PVER_I;
    final public Rq1DatabaseField_Enumeration SWTEST_ROBUSTNESS;
    final public Rq1DatabaseField_Enumeration SWTEST_SRR;
    final public Rq1DatabaseField_Enumeration SWTEST_VIVA;

    final public Rq1XmlSubField_Text SWTEST_BFT_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_COM_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_CST_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_EEPROM_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_FT_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_IO_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_OPT_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_OST_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_PVER_CONF_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_PVER_I_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_SRR_CHANGE_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_VIVA_CHANGE_COMMENT;

    final public Rq1XmlSubField_Text SWTEST_BFT_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_COM_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_CST_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_EEPROM_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_FT_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_IO_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_OPT_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_OST_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_PVER_CONF_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_PVER_I_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_SRR_COMMENT;
    final public Rq1XmlSubField_Text SWTEST_VIVA_COMMENT;
    final public Rq1XmlSubField_Date DELIVERY_TO_CALIBRATION;
    final public Rq1XmlSubField_Date SPECIFICATION_FREEZE_XML;
    final public Rq1XmlSubField_Date TEST_FREEZE;
    final public Rq1XmlSubField_DerivativesDate PLANNED_DATE_XML;
    final public Rq1DatabaseField_DerivativeMapping DERIVATIVES;
    final public Rq1XmlSubField_Xml PROPLATO;
    final public Rq1XmlSubField_Enumeration PROPLATO_PROGRAMMSTANDSKENNUNG;
    final public Rq1XmlSubField_Enumeration PROPLATO_KAUFMAENNISCH_GEPLANT;
    final public Rq1XmlSubField_Date PROPLATO_ABLIEFERSTAND_DATUM;

    final protected Rq1XmlSubField_Xml SW_METRICS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_SwMetricsValues> SW_METRICS_VALUES;

    final protected Rq1XmlSubField_Xml IPE_RULES;
    final public Rq1XmlSubField_Table<Rq1XmlTable_MissingPlanningForOpt> MISSING_PLANNING_FOR_OPT;

    final public Rq1XmlSubField_Table<Rq1XmlTable_FLowCCPMConfiguraion> FLOW_CCPM_CONFIG;
    final public Rq1XmlSubField_Xml FLOW;
    final public Rq1XmlSubField_Text FLOW_VERSION;
    final public Rq1XmlSubField_Enumeration FLOW_KIT_STATUS;
    final public Rq1XmlSubField_Text FLOW_RANK;
    final public Rq1XmlSubField_Text FLOW_GROUP;
    final public Rq1XmlSubField_Text FLOW_IRM_GROUP;
    final public Rq1XmlSubField_Text FLOW_R_DATE;
    final public Rq1XmlSubField_Text FLOW_INTERNAL_RANK;
    final public Rq1XmlSubField_Enumeration FLOW_SIZE;
    final public Rq1XmlSubField_Text FLOW_CLUSTERNAME;
    final public Rq1XmlSubField_Text FLOW_CLUSTERID;
    final public Rq1XmlSubField_Text FLOW_R_EFFORT;
    final public Rq1XmlSubField_Text FLOW_NO_OF_DEVELOPERS;
    final public Rq1XmlSubField_Text FLOW_ISW_IRM_TASK;
    final public Rq1XmlSubField_Text FLOW_EXP_AVAl_EFFORT;
    final public Rq1XmlSubField_Enumeration FLOW_STATUS;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> FLOW_SUBTASK_TABLE;
    final public Rq1XmlSubField_Date TO_RED_DATE;
    final public Rq1XmlSubField_Text FLOW_BOARD_SWIMLANE_HEADING;
    final public Rq1XmlSubField_Text PARENT_SWIMLANE;
    final public Rq1XmlSubField_Enumeration KING_STATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> FLOW_BLOCKER_TABLE;
    final public Rq1XmlSubField_Enumeration EXPERT_STATE;
    final public Rq1XmlSubField_Date TARGET_DATE;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CriticalResource> CRITICAL_RESOURCE;
    final public Rq1XmlSubField_Text FLOW_INCLUDE_TO_LIST;

    public Rq1Pst(Rq1NodeDescription subjectDescription) {
        super(subjectDescription);

        addField(DERIVATIVES = new Rq1DatabaseField_DerivativeMapping(this, "Derivatives"));

        addField(SWTEST_BFT = new Rq1DatabaseField_Enumeration(this, "SWTest_BFT", QualityMeasureState.values()));
        addField(SWTEST_COM = new Rq1DatabaseField_Enumeration(this, "SWTest_COM", QualityMeasureState.values()));
        addField(SWTEST_CST = new Rq1DatabaseField_Enumeration(this, "SWTest_CST", QualityMeasureState.values()));
        addField(SWTEST_EEPROM = new Rq1DatabaseField_Enumeration(this, "SWTest_EEPROM", QualityMeasureState.values()));
        addField(SWTEST_FT = new Rq1DatabaseField_Enumeration(this, "SWTest_FT", QualityMeasureState.values()));
        addField(SWTEST_IO = new Rq1DatabaseField_Enumeration(this, "SWTest_IO", QualityMeasureState.values()));
        addField(SWTEST_OPT = new Rq1DatabaseField_Enumeration(this, "SWTest_OPT", QualityMeasureState.values()));
        addField(SWTEST_OST = new Rq1DatabaseField_Enumeration(this, "SWTest_OST", QualityMeasureState.values()));
        addField(SWTEST_PVER_CONF = new Rq1DatabaseField_Enumeration(this, "SWTest_PVER_Conf", QualityMeasureState.values()));
        addField(SWTEST_PVER_I = new Rq1DatabaseField_Enumeration(this, "SWTest_PVER_I", QualityMeasureState.values()));
        addField(SWTEST_ROBUSTNESS = new Rq1DatabaseField_Enumeration(this, "SWTest_Robustness", QualityMeasureState.values()));
        addField(SWTEST_SRR = new Rq1DatabaseField_Enumeration(this, "SWTest_SRR", QualityMeasureState.values()));
        addField(SWTEST_VIVA = new Rq1DatabaseField_Enumeration(this, "SWTest_VIVA", QualityMeasureState.values()));

        SWTEST_BFT.acceptInvalidValuesInDatabase();
        SWTEST_COM.acceptInvalidValuesInDatabase();
        SWTEST_CST.acceptInvalidValuesInDatabase();
        SWTEST_EEPROM.acceptInvalidValuesInDatabase();
        SWTEST_FT.acceptInvalidValuesInDatabase();
        SWTEST_IO.acceptInvalidValuesInDatabase();
        SWTEST_OPT.acceptInvalidValuesInDatabase();
        SWTEST_OST.acceptInvalidValuesInDatabase();
        SWTEST_PVER_CONF.acceptInvalidValuesInDatabase();
        SWTEST_PVER_I.acceptInvalidValuesInDatabase();
        SWTEST_ROBUSTNESS.acceptInvalidValuesInDatabase();
        SWTEST_SRR.acceptInvalidValuesInDatabase();
        SWTEST_VIVA.acceptInvalidValuesInDatabase();

        addField(SWTEST_BFT_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_BFT_ChangeComment"));
        addField(SWTEST_COM_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_COM_ChangeComment"));
        addField(SWTEST_CST_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_CST_ChangeComment"));
        addField(SWTEST_EEPROM_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_EEPROM_ChangeComment"));
        addField(SWTEST_FT_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_FT_ChangeComment"));
        addField(SWTEST_IO_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_IO_ChangeComment"));
        addField(SWTEST_OPT_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_OPT_ChangeComment"));
        addField(SWTEST_OST_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_OST_ChangeComment"));
        addField(SWTEST_PVER_CONF_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_PVER_Conf_ChangeComment"));
        addField(SWTEST_PVER_I_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_PVER_I_ChangeComment"));
        addField(SWTEST_SRR_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_SRR_ChangeComment"));
        addField(SWTEST_VIVA_CHANGE_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_VIVA_ChangeComment"));

        addField(SWTEST_BFT_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_BFT_Comment"));
        addField(SWTEST_COM_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_COM_Comment"));
        addField(SWTEST_CST_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_CST_Comment"));
        addField(SWTEST_EEPROM_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_EEPROM_Comment"));
        addField(SWTEST_FT_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_FT_Comment"));
        addField(SWTEST_IO_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_IO_Comment"));
        addField(SWTEST_OPT_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_OPT_Comment"));
        addField(SWTEST_OST_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_OST_Comment"));
        addField(SWTEST_PVER_CONF_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_PVER_Conf_Comment"));
        addField(SWTEST_PVER_I_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_PVER_I_Comment"));
        addField(SWTEST_SRR_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_SRR_Comment"));
        addField(SWTEST_VIVA_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "SWTest_VIVA_Comment"));

        addField(DELIVERY_TO_CALIBRATION = new Rq1XmlSubField_Date(this, MILESTONES, "DeliveryToCalibration"));
        addField(SPECIFICATION_FREEZE_XML = new Rq1XmlSubField_Date(this, MILESTONES, "SpecificationFreeze"));
        addField(TEST_FREEZE = new Rq1XmlSubField_Date(this, MILESTONES, "TestFreeze"));
        addField(PLANNED_DATE_XML = new Rq1XmlSubField_DerivativesDate(this, MILESTONES, "PlannedDate"));

        addField(PROPLATO = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKNOWN_ELEMENTS_ALLOWED, "ProPlaTo"));
        addField(PROPLATO_PROGRAMMSTANDSKENNUNG = new Rq1XmlSubField_Enumeration(this, PROPLATO, "Programmstandskennung", ProPlaTo_Programmstandskennung.values(), ProPlaTo_Programmstandskennung.EMPTY));
        addField(PROPLATO_KAUFMAENNISCH_GEPLANT = new Rq1XmlSubField_Enumeration(this, PROPLATO, "KaufmaennischGeplant", ProPlaTo_KaufmaennischGeplant.values(), ProPlaTo_KaufmaennischGeplant.TRUE));
        addField(PROPLATO_ABLIEFERSTAND_DATUM = new Rq1XmlSubField_Date(this, PROPLATO, "AblieferstandDatum"));

        PROPLATO_KAUFMAENNISCH_GEPLANT.addAlternativName("Kaufmaennisch_geplant");
        PROPLATO_KAUFMAENNISCH_GEPLANT.acceptInvalidValuesInDatabase();

        addField(SW_METRICS = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKOWN_ELEMENTS_NOT_ALLOWED, "SW-Metric"));
        SW_METRICS.setOptional();
        addField(SW_METRICS_VALUES = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_SwMetricsValues(), SW_METRICS, "Value"));
        SW_METRICS_VALUES.setOptional();

        addField(IPE_RULES = new Rq1XmlSubField_Xml(this, TAGS, DsField_Xml.ContentMode.UNKOWN_ELEMENTS_NOT_ALLOWED, "IpeRules"));
        IPE_RULES.setOptional();
        addField(MISSING_PLANNING_FOR_OPT = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_MissingPlanningForOpt(), IPE_RULES, "PlanningOpt"));
        MISSING_PLANNING_FOR_OPT.setOptional();

        addField(FLOW = new Rq1XmlSubField_Xml(this, TAGS, "FLOW"));
        FLOW.setOptional();
        addField(FLOW_VERSION = new Rq1XmlSubField_Text(this, FLOW, "V"));
        addField(FLOW_CCPM_CONFIG = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FLowCCPMConfiguraion(), FLOW, "CCPM"));

        addField(FLOW_KIT_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "FKS", FullKitStatus.values(), null));
        addField(FLOW_RANK = new Rq1XmlSubField_Text(this, FLOW, "RANK"));
        addField(FLOW_GROUP = new Rq1XmlSubField_Text(this, FLOW, "GROUP"));
        addField(FLOW_IRM_GROUP = new Rq1XmlSubField_Text(this, FLOW, "IRM_GROUP"));
        addField(FLOW_R_DATE = new Rq1XmlSubField_Text(this, FLOW, "R_DATE"));
        addField(FLOW_INTERNAL_RANK = new Rq1XmlSubField_Text(this, FLOW, "INTERNAL_RANK"));
        addField(FLOW_SIZE = new Rq1XmlSubField_Enumeration(this, FLOW, "SIZE", FullKitSize.values(), null));
        addField(FLOW_CLUSTERNAME = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER"));
        addField(FLOW_CLUSTERID = new Rq1XmlSubField_Text(this, FLOW, "CLUSTER_ID"));
        addField(FLOW_R_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "R_EFFORT"));
        addField(FLOW_NO_OF_DEVELOPERS = new Rq1XmlSubField_Text(this, FLOW, "NB_D"));
        addField(FLOW_ISW_IRM_TASK = new Rq1XmlSubField_Text(this, FLOW, "TASK"));
        addField(FLOW_STATUS = new Rq1XmlSubField_Enumeration(this, FLOW, "STATUS", TaskStatus.values(), null));
        addField(FLOW_SUBTASK_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowSubTask(), FLOW, "SUBTASK"));
        addField(TO_RED_DATE = new Rq1XmlSubField_Date(this, FLOW, "TO_RED"));
        addField(FLOW_BOARD_SWIMLANE_HEADING = new Rq1XmlSubField_Text(this, FLOW, "SL_H"));
        addField(PARENT_SWIMLANE = new Rq1XmlSubField_Text(this, FLOW, "PARENT_SWIMLANE"));
        addField(KING_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "KING", KingState.values(), null));
        addField(FLOW_BLOCKER_TABLE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_FlowBlocker(), FLOW, "BLOCKER"));
        addField(EXPERT_STATE = new Rq1XmlSubField_Enumeration(this, FLOW, "EXP", ExpertState.values(), null));
        addField(FLOW_EXP_AVAl_EFFORT = new Rq1XmlSubField_Text(this, FLOW, "EXP_EFFORT"));
        addField(TARGET_DATE = new Rq1XmlSubField_Date(this, FLOW, "T_DATE"));
        addField(CRITICAL_RESOURCE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CriticalResource(), FLOW, "C_RES"));
        addField(FLOW_INCLUDE_TO_LIST = new Rq1XmlSubField_Text(this, FLOW, "INC_TO_LIST"));
        CRITICAL_RESOURCE.setOptional();
        FLOW_EXP_AVAl_EFFORT.setOptional();
        FLOW_IRM_GROUP.setOptional();
        FLOW_INCLUDE_TO_LIST.setOptional();
    }

    //-------------------------------------------------------------------------------------
    //
    // Handle FC/BC-List for ECV
    //
    //-------------------------------------------------------------------------------------
    boolean isFcBcListLoaded = false; // Prevents repeated loading of the list.
    private Set<String> fcBcListFound = null; // Set of derivatives for which a FC/BC-List was found.
    private Map<String, EcvMapList<String, DmDgsBcReleaseI>> bcMap = null; // BC-Name, Derivative, BC-Release
    private Map<String, EcvMapList<String, DmDgsFcReleaseI>> fcMap = null; // FC-Name, Derivative, FC-Release
    private Map<String, Map<String, EcvMapList<String, DmDgsFcReleaseI>>> bcFcMap = null; // BC-Name, FC-Name, Derivative, FC-Release

    /**
     * Indicates whether FC-BC-List is attached to this release.
     *
     * @return true, if at least one FC-BC-List is attached to the release;
     * false otherwise.
     */
    final public boolean hasFcBcList() {
        loadFcBcList();
        return (fcBcListFound.isEmpty() == false);
    }

    /**
     * Returns the BCs for the given BC name in a derivate specific map list.
     * The BCs are read from the FC-BC-Lists attached to the PST.
     *
     * @param bcName BC name for which the map shall be returned.
     * @return A map, if BCs with the given name where found; null otherwise.
     */
    final public EcvMapList<String, DmDgsBcReleaseI> getBcFromFcBcList(String bcName) {
        assert (bcName != null);
        assert (bcName.isEmpty() == false);

        loadFcBcList();

        return (bcMap.get(bcName));
    }

    /**
     * Returns the FCs for the given FC name in a derivate specific map list.
     * The FCs are read from the FC-BC-Lists attached to the PST.
     *
     * @param fcName FC name for which the map shall be returned.
     * @return A map, if FCs with the given name where found; null otherwise.
     */
    final public EcvMapList<String, DmDgsFcReleaseI> getFcFromFcBcList(String fcName) {
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        loadFcBcList();

        return (fcMap.get(fcName));
    }

    /**
     * Returns the FCs for the given FC name within the given BC in a derivate
     * specific map list. The FCs are read from the FC-BC-Lists attached to the
     * PST.
     *
     * @param bcName BC name in which the FC shall be searched.
     * @param fcName FC name for which the map shall be returned.
     * @return A map, if FCs with the given name where found; null otherwise.
     */
    final public EcvMapList<String, DmDgsFcReleaseI> getFcFromFcBcList(String bcName, String fcName) {
        assert (bcName != null);
        assert (bcName.isEmpty() == false);
        assert (fcName != null);
        assert (fcName.isEmpty() == false);

        loadFcBcList();

        Map<String, EcvMapList<String, DmDgsFcReleaseI>> fcMappedToBc = bcFcMap.get(bcName);
        if (fcMappedToBc != null) {
            return (fcMappedToBc.get(fcName));
        }

        return (null);
    }

    private synchronized void loadFcBcList() {

        //
        // Prevent repeated read
        //
        if (isFcBcListLoaded == true) {
            return;
        }

        fcBcListFound = new TreeSet<>();
        bcMap = new TreeMap<>();
        fcMap = new TreeMap<>();
        bcFcMap = new TreeMap<>();

        //
        // Loop over all attachments
        // 
        for (Rq1Reference reference : ATTACHMENTS.getDataModelValue()) {
            assert (reference.getRecord() instanceof Rq1Attachment);
            Rq1Attachment attachment = (Rq1Attachment) reference.getRecord();

            //
            // Process only attachments matching the file name conventions for FC/BC-List
            //
            String fileName = attachment.FILENAME.getDataModelValue();
            if ((fileName != null) && fileName.contains("FC_BC_") && fileName.contains(".xml")) {
                //
                // Parse FC/BC-List
                //
                EcvXmlContainerElement fileContent = attachment.getXmlFileContent();
                if (fileContent == null) {
                    LOGGER.log(Level.WARNING, "Cannot read content of attachment " + fileName);
                } else {

                    //
                    // Extract derivatives from file
                    //
                    try {
                        EcvXmlContainerElement lieferant = fileContent.getContainerElement("Lieferant");
                        lieferant.getTextElement("Name");
                        for (EcvXmlContainerElement derivative : lieferant.getContainerElementList("PSTSchiene")) {
                            loadFcBcList_Derivative(derivative);
                        }
                    } catch (EcvXmlElement.NotfoundException ex) {
                        LOGGER.log(Level.WARNING, fileName, ex);
                    }

                }
            }
        }

        //
        // Mark list as loaded
        //
        isFcBcListLoaded = true;
    }

    private void loadFcBcList_Derivative(EcvXmlContainerElement derivative) throws EcvXmlElement.NotfoundException {
        assert (derivative != null);
        assert (bcMap != null);
        assert (fcMap != null);
        assert (bcFcMap != null);
        assert (fcBcListFound != null);

        String derivativeName = derivative.getTextElement("Name").getText();
        fcBcListFound.add(derivativeName);
        EcvXmlContainerElement programmStand = derivative.getContainerElement("Programmstand");

        //
        // Loop over Pakete (BCs)
        //
        for (EcvXmlContainerElement paket : programmStand.getContainerElementList("Paket")) {
            String paketName = paket.getTextElement("Name").getText();

            //
            // Skip conf components for BC list
            //
            if (paketName.equals("Conf-Component") == false) {

                String paketVersion = paket.getTextElement("Version").getText();
                //
                // Add BC to map
                //
                EcvMapList<String, DmDgsBcReleaseI> bcList = bcMap.get(paketName);
                if (bcList == null) {
                    bcList = new EcvMapList<>();
                    bcMap.put(paketName, bcList);
                }
                bcList.addValueUnique(derivativeName, new DmDgsBcRelease("BC", paketName, paketVersion));
            }

            //
            // Loop over Funktionen (FCs)
            //
            for (EcvXmlContainerElement funktion : paket.getContainerElementList("Funktion")) {
                String funktionName = funktion.getTextElement("Kurzbezeichnung").getText();
                String funktionVersion = funktion.getTextElement("Version").getText();

                //
                // Add FC to FC map
                //
                EcvMapList<String, DmDgsFcReleaseI> fcList = fcMap.get(funktionName);
                if (fcList == null) {
                    fcList = new EcvMapList<>();
                    fcMap.put(funktionName, fcList);
                }
                fcList.addValueUnique(derivativeName, new DmDgsFcRelease(funktionName, funktionVersion));

                //
                // Add FC to BC/FC map
                //
                Map<String, EcvMapList<String, DmDgsFcReleaseI>> fcMapForBc = bcFcMap.get(paketName);
                if (fcMapForBc == null) {
                    fcMapForBc = new TreeMap<>();
                    bcFcMap.put(paketName, fcMapForBc);
                }
                fcList = fcMapForBc.get(funktionName);
                if (fcList == null) {
                    fcList = new EcvMapList<>();
                    fcMapForBc.put(funktionName, fcList);
                }
                fcList.addValueUnique(derivativeName, new DmDgsFcRelease(funktionName, funktionVersion));
            }
        }
    }
}
