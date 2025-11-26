/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import DataModel.GPM.ConfigData.DmGpmConfig_Task;
import DataModel.GPM.ConfigData.Plib.DmGpmPlibConfig_Task;
import DataModel.GPM.ConfigData.Plib.DmGpmPlibRow_Role;
import DataModel.GPM.ConfigData.Plib.DmGpmPlibTable_Roles;
import DataModel.GPM.ConfigData.Plib.DmGpmPlibTable_Tasks;
import Rq1Data.Enumerations.ImpactCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

public class DmGpmPlibConfig_Manager {

    final private static Logger LOGGER = Logger.getLogger(DmGpmPlibConfig_Manager.class.getCanonicalName());

    private static List<DmGpmPlibConfig_Task> validTaskList = null;
    private static List<DmGpmPlibConfig_Task> allTaskList = null;

    static List<DmGpmPlibConfig_Task> getAllPlibTasks() {
        if (allTaskList == null) {
            var newTasklist = new ArrayList<DmGpmPlibConfig_Task>();
            for (DmGpmPlibRow_Task row : DmGpmPlibTable_Tasks.table().getRows()) {
                newTasklist.add(new DmGpmPlibConfig_Task(row));
            }
            allTaskList = newTasklist;
        }
        return (allTaskList);
    }

    static List<DmGpmPlibConfig_Task> getValidPlibTasks() {
        if (validTaskList == null) {
            var newTasklist = new ArrayList<DmGpmPlibConfig_Task>();
            for (var row : DmGpmPlibTable_Tasks.table().getRows()) {
                var task = new DmGpmPlibConfig_Task(row);
                if (task.isTaskValid() == true) {
                    newTasklist.add(new DmGpmPlibConfig_Task(row));
                }
            }
            validTaskList = newTasklist;
        }
        return (validTaskList);
    }

    public static Set<String> getAllPLibRoles() {
        Set<String> result = new TreeSet<>();
        result.add("");
        for (DmGpmPlibRow_Role row : DmGpmPlibTable_Roles.table().getRows()) {
            result.add(row.getShortName());
        }
        return (result);
    }

    static public Set<String> getPLibRoles(ImpactCategory impactCategory) {
        assert (impactCategory != null);

        Set<String> result = new TreeSet<>();
        for (var task : getValidPlibTasks()) {
            if (task.getImpactCategory().contains(impactCategory) == true) {
                result.add(task.getResponsibleRole());
            }
        }
        return (result);
    }

    public static Set<String> getPLibMilestones() {
        Set<String> result = new TreeSet<>();
        for (DmGpmConfig_Task task : getValidPlibTasks()) {
            result.addAll(task.getLinkedMilestones());
        }
        return (result);
    }

    /**
     * Creates a new set for all milestones from the Plib for this impact
     * category.
     *
     * @param impactCategory
     * @return A new set that is not used by this code after returning it.
     */
    static public Set<String> getPLibMilestones(ImpactCategory impactCategory) {
        assert (impactCategory != null);

        Set<String> result = new TreeSet<>();
        for (DmGpmConfig_Task task : getValidPlibTasks()) {
            if (task.getImpactCategory().contains(impactCategory) == true) {
                result.addAll(task.getLinkedMilestones());
            }
        }
        return (result);
    }

    static public Set<String> getAllPLibMilestones(ImpactCategory impactCategory) {
        assert (impactCategory != null);

        Set<String> result = new TreeSet<>();
        for (DmGpmConfig_Task task : getAllPlibTasks()) {
            if (task.getImpactCategory().contains(impactCategory) == true) {
                result.addAll(task.getLinkedMilestones());
            }
        }
        return (result);
    }

    public static List<DmGpmPlibConfig_Task> getPLibTasks() {
        return (getValidPlibTasks());
    }

    public static List<DmGpmPlibConfig_Task> getPLibTasks(ImpactCategory impactCategory, String milestoneName) {
        assert (impactCategory != null);
        assert (milestoneName != null);
        assert (milestoneName.isEmpty() == false);

        List<DmGpmPlibConfig_Task> result = new ArrayList<>();
        for (DmGpmPlibConfig_Task task : getValidPlibTasks()) {
            Set<String> rowMilestones = task.getLinkedMilestones();
            if (rowMilestones.contains(milestoneName) == true) {
                if (task.getImpactCategory().contains(impactCategory) == true) {
                    result.add(task);
                }
            }
        }
        return (result);
    }

    static List<DmGpmPlibConfig_Task> getTasksForRole(ImpactCategory impactCategory, String role) {
        var result = new ArrayList<DmGpmPlibConfig_Task>();
        for (DmGpmPlibConfig_Task task : getValidPlibTasks()) {
            if (task.getImpactCategory().contains(impactCategory) == true) {
                if (task.getResponsibleRole().equals(role) == true) {
                    result.add(task);
                }
            }
        }
        return (result);
    }

}
