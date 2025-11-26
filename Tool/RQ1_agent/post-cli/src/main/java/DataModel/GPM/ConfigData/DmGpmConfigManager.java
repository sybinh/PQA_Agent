/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData;

import DataModel.GPM.ConfigData.Plib.DmGpmPlibConfig_Manager;
import DataModel.GPM.ConfigData.Plib.DmGpmPlibConfig_Task;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1WorkItem;
import Rq1Data.Enumerations.ImpactCategory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;
import util.EcvMapSet;

/**
 * Provides access to the configuration data for GPM.
 *
 * The configuration data has to parts: <br>
 * - The configuration extracted from the Plib and delivered with this
 * sources.<br>
 * - An optional configuration attached to the project.<br>
 *
 * @author GUG2WI
 */
public class DmGpmConfigManager {

    final private static Logger LOGGER = Logger.getLogger(DmGpmConfigManager.class.getCanonicalName());

    static private Collection<DmGpmConfig_Task> getPLibAndProjectTasks(DmRq1Project project) {
        assert (project != null);

        List<DmGpmConfig_Task> result = new ArrayList<>();
        result.addAll(DmGpmPlibConfig_Manager.getPLibTasks());
        result.addAll(DmGpmConfigManager_Project.getProjectConfigData(project).getProjectTasks());
        return (result);
    }

    /**
     * Returns the milestone types defined for this project.
     *
     * The list contains:<br>
     * - All milestones from the Plib, reduced by milestones which are releases.
     * - Additionally all predefined milestone types. - Additionally all
     * milestones defined in the project.
     *
     * @param impactCategory
     * @param project
     * @return
     */
    static public Set<String> getMilestones(ImpactCategory impactCategory, DmRq1Project project) {
        assert (impactCategory != null);
        assert (project != null);

        Set<String> result = DmGpmPlibConfig_Manager.getPLibMilestones(impactCategory);
        DmGpmPredefinedMilestoneTypes.addAndRemovedPredefinedMilestones(result);
        for (DmGpmProjectConfig_MilestoneType milestone : DmGpmConfigManager_Project.getProjectConfigData(project).getProjectMilestones()) {
            result.add(milestone.getMilestoneName());
        }
        return (result);
    }

    static public Set<String> getMilestonesAndReleases(ImpactCategory impactCategory, DmRq1Project project) {
        assert (impactCategory != null);
        assert (project != null);

        Set<String> result = DmGpmPlibConfig_Manager.getPLibMilestones(impactCategory);
//        DmGpmPredefinedMilestoneTypes.addPredefinedMilestones(result);
        for (DmGpmProjectConfig_MilestoneType milestone : DmGpmConfigManager_Project.getProjectConfigData(project).getProjectMilestones()) {
            result.add(milestone.getMilestoneName());
        }
        return (result);
    }

    /**
     * Returns the task defined for the given impact category, milestone name
     * and project.
     *
     * The result list contains the settings of the Plib adapted by the project
     * specific settings.
     *
     * @param impactCategory
     * @param milestoneName
     * @param project
     * @return
     */
    static public List<DmGpmConfig_Task> getPlibTasks(ImpactCategory impactCategory, String milestoneName, DmRq1Project project) {
        assert (impactCategory != null);
        assert (milestoneName != null);
        assert (milestoneName.isEmpty() == false);
        assert (project != null);

        List<DmGpmConfig_Task> result = new ArrayList<>();
        Map<String, DmGpmProjectConfig_PlibTaskDelta> deltaMap = DmGpmConfigManager_Project.getProjectConfigData(project).getPLibTaskDelta();

        for (DmGpmPlibConfig_Task plibTask : DmGpmPlibConfig_Manager.getPLibTasks()) {
            if (plibTask.isTaskValid() == true) {
                DmGpmProjectConfig_PlibTaskDelta taskDelta = deltaMap.get(plibTask.getTaskId());
                var combinedTask = new DmGpmPlibConfig_CombinedTask(plibTask, taskDelta);
                Set<String> milestones = combinedTask.getLinkedMilestones();
                if (milestones.contains(milestoneName) == true) {
                    if (combinedTask.getImpactCategory().contains(impactCategory) == true) {
                        result.add(combinedTask);
                    }
                }
            }
        }

        return (result);
    }

    static public List<DmGpmConfig_Task> getProjectTasks(ImpactCategory impactCategory, String milestoneName, DmRq1Project project) {
        assert (impactCategory != null);
        assert (milestoneName != null);
        assert (milestoneName.isEmpty() == false);
        assert (project != null);

        List<DmGpmConfig_Task> result = new ArrayList<>();
        Collection<DmGpmProjectConfig_Task> projectTasks = DmGpmConfigManager_Project.getProjectConfigData(project).getProjectTasks();

        for (DmGpmProjectConfig_Task projectTask : projectTasks) {
            Set<String> milestones = projectTask.getLinkedMilestones();
            if (milestones.contains(milestoneName) == true) {
                if (projectTask.getImpactCategory().contains(impactCategory) == true) {
                    result.add(projectTask);
                }
            }
        }

        return (result);
    }

    static public List<DmRq1WorkItem> getObsoletTasks(List<DmRq1WorkItem> existingWorkitems, List<DmGpmConfig_Task> pLibTasks, List<DmGpmConfig_Task> projectTasks) {
        assert (existingWorkitems != null);
        assert (pLibTasks != null);
        assert (projectTasks != null);

        var result = new ArrayList<DmRq1WorkItem>();

        var validTaskIds = new EcvMapSet<DmGpmConfig_Task.Source, String>();
        Stream.concat(pLibTasks.stream(), projectTasks.stream())
                .forEach(t -> validTaskIds.add(t.getSource(), t.getTaskId()));

        for (DmRq1WorkItem wi : existingWorkitems) {
            if ((wi.isCanceled() == false) && (wi.isClosed() == false)) {
                String taskId = wi.GPM_TASK_ID.getValueAsText();
                if (taskId.isBlank() == false) {
                    DmGpmConfig_Task.Source source = DmGpmConfig_Task.Source.getSourceForText(wi.GPM_SOURCE.getValueAsText());
                    if (validTaskIds.containsValue(source, taskId) == false) {
                        result.add(wi);
                    }
                }
            }
        }

        return (result);
    }

    /**
     * Returns all rows (tasks) configured for the given task name.
     * <br>
     * The method returns all tasks where the name of the task matches the start
     * of the given task name.
     *
     * @param taskName Name of the task for which the configuration data shall
     * be returned.
     * @param project DmRq1Project to take attached ProjectSpecificGpmConfig.csv
     * into account
     * @return Rows fitting to the given task name.
     */
    public static List<DmGpmConfig_Task> getTasksByTaskname(String taskName, DmRq1Project project) {

        List<DmGpmConfig_Task> result = new ArrayList<>();
        if ((taskName != null) && (taskName.isEmpty() == false)) {
            for (DmGpmConfig_Task task : getPLibAndProjectTasks(project)) {
                if (taskName.startsWith(task.getTaskName()) == true) {
                    result.add(task);
                }
            }
        }
        return (result);
    }

    public static List<DmGpmConfig_Task> getTasksByTaskId(String taskId, DmRq1Project project) {

        List<DmGpmConfig_Task> result = new ArrayList<>();
        if ((taskId != null) && (taskId.isEmpty() == false)) {
            for (DmGpmConfig_Task task : getPLibAndProjectTasks(project)) {
                if (taskId.equals(task.getTaskId()) == true) {
                    result.add(task);
                }
            }
        }
        return (result);
    }

}
