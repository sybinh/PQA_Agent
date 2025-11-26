/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData;

import java.util.Collection;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvCollectionUtil;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;

/**
 *
 * @author GUG2WI
 */
public class DmGpmProjectConfig_Data {

    private static final Logger LOGGER = Logger.getLogger(DmGpmProjectConfig_Data.class.getCanonicalName());

    private static final String TAGNAME_ROOT = "GuidedProjectConfig";

    private static final String TAGNAME_PROJECT_MILESTONES = "ProjectMilestones";
    private static final String TAGNAME_PROJECT_TASKS = "ProjectTasks";
    private static final String TAGNAME_PLIB_TASK_DELTA = "PlibTask";

    private final SortedSet<DmGpmProjectConfig_MilestoneType> projectMilestones = new TreeSet<>();
    private final SortedSet<DmGpmProjectConfig_Task> projectTasks = new TreeSet<>();
    private final SortedMap<String, DmGpmProjectConfig_PlibTaskDelta> pLibTaskDelta = new TreeMap<>();

    @Override
    public boolean equals(Object other) {

        if (other instanceof DmGpmProjectConfig_Data == false) {
            return (false);
        }
        DmGpmProjectConfig_Data otherData = (DmGpmProjectConfig_Data) other;

        if (EcvCollectionUtil.equalsSortedSet(projectMilestones, otherData.projectMilestones) == false) {
            return (false);
        }

        if (EcvCollectionUtil.equalsSortedSet(projectTasks, otherData.projectTasks) == false) {
            return (false);
        }

        if (EcvCollectionUtil.equalsSortedMap(pLibTaskDelta, otherData.pLibTaskDelta) == false) {
            return (false);
        }

        return (true);
    }

    //--------------------------------------------------------------------------
    //
    // Public interface for getting and setting data
    //
    //--------------------------------------------------------------------------
    public DmGpmProjectConfig_Data() {
    }

    public SortedSet<DmGpmProjectConfig_MilestoneType> getProjectMilestones() {
        return projectMilestones;
    }

    public Collection<DmGpmProjectConfig_Task> getProjectTasks() {
        return projectTasks;
    }

    public SortedMap<String, DmGpmProjectConfig_PlibTaskDelta> getPLibTaskDelta() {
        return pLibTaskDelta;
    }

    public void addMilestone(DmGpmProjectConfig_MilestoneType newMilestone) {
        assert (newMilestone != null);
        projectMilestones.add(newMilestone);
    }

    public void addProjectTask(DmGpmProjectConfig_Task newTask) {
        assert (newTask != null);
        projectTasks.add(newTask);
    }

    public void addDelta(DmGpmProjectConfig_PlibTaskDelta newDelta) {
        assert (newDelta != null);
        pLibTaskDelta.put(newDelta.getpLibTaskId(), newDelta);
    }

    //--------------------------------------------------------------------------
    //
    // Package wide interface for storing in XML format
    //
    //--------------------------------------------------------------------------
    DmGpmProjectConfig_Data(EcvXmlElement xmlElement) {
        assert (xmlElement != null);
        if (xmlElement instanceof EcvXmlContainerElement) {
            extract((EcvXmlContainerElement) xmlElement);
        }
    }

    private void extract(EcvXmlContainerElement xmlContainer) {
        assert (xmlContainer != null);

        EcvXmlContainerElement xmlMilestones = xmlContainer.getOptionalContainerElement(TAGNAME_PROJECT_MILESTONES);
        if (xmlMilestones != null) {
            for (EcvXmlElement xmlMilestone : xmlMilestones.getElementList(DmGpmProjectConfig_MilestoneType.TAGNAME)) {
                try {
                    projectMilestones.add(new DmGpmProjectConfig_MilestoneType(xmlMilestone));
                } catch (EcvXmlElement.NotfoundException ex) {
                    LOGGER.log(Level.SEVERE, "Error parsing project milestone.", ex);
                }
            }
        }

        EcvXmlContainerElement xmlTasks = xmlContainer.getOptionalContainerElement(TAGNAME_PROJECT_TASKS);
        if (xmlTasks != null) {
            for (EcvXmlElement xmlTask : xmlTasks.getElementList(DmGpmProjectConfig_Task.TAGNAME)) {
                try {
                    projectTasks.add(new DmGpmProjectConfig_Task(xmlTask));
                } catch (EcvXmlElement.NotfoundException ex) {
                    LOGGER.log(Level.SEVERE, "Error parsing project tasks.", ex);
                }
            }
        }

        EcvXmlContainerElement xmlDeltas = xmlContainer.getOptionalContainerElement(TAGNAME_PLIB_TASK_DELTA);
        if (xmlDeltas != null) {
            for (EcvXmlElement xmlDelta : xmlDeltas.getElementList(DmGpmProjectConfig_PlibTaskDelta.TAGNAME)) {
                try {
                    DmGpmProjectConfig_PlibTaskDelta taskDelta = new DmGpmProjectConfig_PlibTaskDelta(xmlDelta);
                    pLibTaskDelta.put(taskDelta.getpLibTaskId(), taskDelta);
                } catch (EcvXmlElement.NotfoundException ex) {
                    LOGGER.log(Level.SEVERE, "Error parsing task delta.", ex);
                }
            }
        }
    }

    EcvXmlContainerElement provideAsXml() {

        EcvXmlContainerElement result = new EcvXmlContainerElement(TAGNAME_ROOT);

        if (projectMilestones.isEmpty() == false) {
            EcvXmlContainerElement xmlMilestones = new EcvXmlContainerElement(TAGNAME_PROJECT_MILESTONES);
            result.addElement(xmlMilestones);
            for (DmGpmProjectConfig_MilestoneType milestone : projectMilestones) {
                xmlMilestones.addElement(milestone.provideAsXml());
            }
        }

        if (projectTasks.isEmpty() == false) {
            EcvXmlContainerElement xmlTasks = new EcvXmlContainerElement(TAGNAME_PROJECT_TASKS);
            result.addElement(xmlTasks);
            for (DmGpmProjectConfig_Task xmlTask : projectTasks) {
                xmlTasks.addElement(xmlTask.provideAsXml());
            }
        }

        if (pLibTaskDelta.isEmpty() == false) {
            EcvXmlContainerElement xmlDeltas = new EcvXmlContainerElement(TAGNAME_PLIB_TASK_DELTA);
            result.addElement(xmlDeltas);
            for (DmGpmProjectConfig_PlibTaskDelta xmlDelta : pLibTaskDelta.values()) {
                xmlDeltas.addElement(xmlDelta.provideAsXml());
            }
        }

        return (result);
    }

}
