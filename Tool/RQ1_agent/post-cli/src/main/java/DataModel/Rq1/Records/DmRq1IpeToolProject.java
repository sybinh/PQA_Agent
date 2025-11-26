/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import OslcAccess.Rq1.OslcRq1Client;
import OslcAccess.Rq1.OslcRq1ServerDescription;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsMilestones;
import Rq1Cache.Types.Rq1XmlTable_SwMetricsTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import util.EcvTableData;
import util.SafeArrayList;

/**
 * Access to the project that is used by IPE to store general settings for the
 * IPE.
 *
 * @author GUG2WI
 */
public class DmRq1IpeToolProject {

    static private final Logger LOGGER = Logger.getLogger(DmRq1IpeToolProject.class.getCanonicalName());

    private static String toolProjectId = null;
    private static DmRq1SwCustomerProject_Leaf ipeToolProject = null;
    private static SafeArrayList<Rq1XmlTable_SwMetricsTypes.Type> types = null;
    private static SafeArrayList<Rq1XmlTable_SwMetricsMilestones.Milestone> milestones = null;
    private static boolean loadDone = false;

    //--------------------------------------------------------------------------
    //
    // Support access to special project for IPE
    //
    //--------------------------------------------------------------------------
    public static String getIpeToolProjectId() {
        if (toolProjectId == null) {
            OslcRq1ServerDescription database = OslcRq1Client.getCurrentDatabase();
            if (database != null) {
                toolProjectId = database.getToolProjectId();
            } else {
                toolProjectId = "";
            }
        }
        return (toolProjectId);
    }

    public static DmRq1SwCustomerProject_Leaf getIpeToolProject() {
        if (loadDone == false) {
            ipeToolProject = (DmRq1SwCustomerProject_Leaf) DmRq1Element.getElementById(getIpeToolProjectId());
            loadDone = true;
            if (ipeToolProject == null) {
                LOGGER.severe("IPE tool project " + getIpeToolProjectId() + " not found.");
            }
        }
        return (ipeToolProject);
    }

    public static boolean isIpeToolProject(DmRq1Project project) {
        if (project != null) {
            return (getIpeToolProjectId().equals(project.getRq1Id()));
        }
        return (false);
    }

    //--------------------------------------------------------------------------
    //
    // Support access to SW metric values
    //
    //--------------------------------------------------------------------------
    public static List<Rq1XmlTable_SwMetricsTypes.Type> getSoftwareMetricTypes() {
        if (types == null) {
            types = new SafeArrayList<>();
            if (getIpeToolProject() != null) {
                Rq1XmlTable_SwMetricsTypes table = getIpeToolProject().SW_METRICS_TYPES.getTableDescription();
                EcvTableData data = DmRq1IpeToolProject.getIpeToolProject().SW_METRICS_TYPES.getValue();
                types.addAll(table.extract(data));
            }
        }
        return (types.getImmutableList());
    }

    public static List<Rq1XmlTable_SwMetricsMilestones.Milestone> getSoftwareMetricMilestones() {
        if (milestones == null) {
            milestones = new SafeArrayList<>();
            if (getIpeToolProject() != null) {
                Rq1XmlTable_SwMetricsMilestones table = getIpeToolProject().SW_METRICS_MILESTONES.getTableDescription();
                EcvTableData data = getIpeToolProject().SW_METRICS_MILESTONES.getValue();
                milestones.addAll(table.extract(data));
            }
        }
        return (milestones.getImmutableList());
    }
    public static List<String> getQmmFilterCriteriaProfitCenter() {
        List<String> qmmFilterCriteriaProfitCenter = new ArrayList<>();
        if (getIpeToolProject() != null) {
            qmmFilterCriteriaProfitCenter = new ArrayList<>(getIpeToolProject().PROFIT_CENTER.getList());
        }
        return qmmFilterCriteriaProfitCenter;
    }

    public static List<String> getQmmFilterCriteriaPIdDisplayName() {
        List<String> qmmFilterCriteriaPIdDisplayNames = new ArrayList<>();
        if (getIpeToolProject() != null) {
            qmmFilterCriteriaPIdDisplayNames = new ArrayList<>(getIpeToolProject().P_ID_DISPLAY_NAME.getList());
        }
        return qmmFilterCriteriaPIdDisplayNames;
    }

    public static List<String> getQmmFilterCriteriaProjectStatus() {
        List<String> qmmFilterCriteriaProjectStatus = new ArrayList<>();
        if (getIpeToolProject() != null) {
            qmmFilterCriteriaProjectStatus = new ArrayList<>(getIpeToolProject().PROJECT_STATUS.getList());
        }
        return qmmFilterCriteriaProjectStatus;
    }

}
