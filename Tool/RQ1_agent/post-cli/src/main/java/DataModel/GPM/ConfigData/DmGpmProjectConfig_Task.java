/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData;

import Rq1Data.Enumerations.ImpactCategory;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 * Defines a project specific task definition
 *
 * @author GUG2WI
 */
public class DmGpmProjectConfig_Task implements DmGpmConfig_Task, Comparable<DmGpmProjectConfig_Task> {

    public static final String TAGNAME = "Task";

    private static final String ATTRIBUTE_TASK_ID = "taskId";
    private static final String ATTRIBUTE_TASK_NAME = "taskName";
    private static final String ATTRIBUTE_CATEGORY = "category";
    private static final String ATTRIBUTE_RESPONSIBLE_ROLE = "role";
    private static final String ATTRIBUTE_PROCESS_LINK = "process";
    private static final String ATTRIBUTE_ARTEFACT_NAME = "artefactName";
    private static final String ATTRIBUTE_LINK_TOARTEFACT = "artefactLink";
    private static final String ATTRIBUTE_TIME_RELATION = "time";
    private static final String ATTRIBUTE_PLIB_VERSION = "pLib";
    private static final String ATTRIBUTE_EFFORT = "effort";

    private static final String TAGNAME_PROCESS = "Process";
    private static final String TAGNAME_ARTEFACT = "Artefact";
    private static final String TAGNAME_MILESTONE = "Milestone";
    private static final String TAGNAME_DESCRIPTION = "Description";
    private static final String TAGNAME_COMMENT = "Comment";

    private static final String LINK = "link";
    private static final String NAME = "name";

    final private String taskId;
    final private String taskName;
    final private String description;
    //
    final private EnumSet<ImpactCategory> impactCategory;
    final private String responsibleRole;
    final private Set<String> linkedMilestones;
    //
    final private String processLink;
    final private String artefactName;
    final private String linkToArtefact;
    //
    final private int timeRelation;
    final private String pLibVersion;
    final private String effort;
    //
    final private String comment;

    public DmGpmProjectConfig_Task(String taskId, String taskName, String description,
            EnumSet<ImpactCategory> impactCategory, Collection<String> linkedMilestones, String responsibleRole,
            String processLink, String artefactName, String linkToArtefact,
            int timeRelation, String effort,
            String pLibVersion,
            String comment) {
        assert (taskId != null);
        assert (taskId.isEmpty() == false);
        assert (taskName != null);
        assert (taskName.isEmpty() == false);
        assert (impactCategory != null);
        assert (linkedMilestones != null);

        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description != null ? description : "";
        this.impactCategory = impactCategory;
        this.responsibleRole = responsibleRole != null ? responsibleRole : "";
        this.linkedMilestones = new TreeSet<>(linkedMilestones);
        this.processLink = processLink != null ? processLink : "";
        this.artefactName = artefactName != null ? artefactName : "";
        this.linkToArtefact = linkToArtefact != null ? linkToArtefact : "";
        this.timeRelation = timeRelation;
        this.pLibVersion = pLibVersion != null ? pLibVersion : "";
        this.effort = effort != null ? effort : "";
        this.comment = comment != null ? comment : "";
    }

    @Override
    public int compareTo(DmGpmProjectConfig_Task other) {
        return (taskId.compareTo(other.taskId));
    }

    @Override
    public boolean equals(Object other) {

        if (other instanceof DmGpmProjectConfig_Task == false) {
            return (false);
        }
        DmGpmProjectConfig_Task otherTask = (DmGpmProjectConfig_Task) other;

        if (taskId.equals(otherTask.taskId) == false) {
            return (false);
        }
        if (taskName.equals(otherTask.taskName) == false) {
            return (false);
        }
        if (description.equals(otherTask.description) == false) {
            return (false);
        }
        if (impactCategory.equals(otherTask.impactCategory) == false) {
            return (false);
        }
        if (responsibleRole.equals(otherTask.responsibleRole) == false) {
            return (false);
        }
        if (linkedMilestones.equals(otherTask.linkedMilestones) == false) {
            return (false);
        }
        if (processLink.equals(otherTask.processLink) == false) {
            return (false);
        }
        if (artefactName.equals(otherTask.artefactName) == false) {
            return (false);
        }
        if (linkToArtefact.equals(otherTask.linkToArtefact) == false) {
            return (false);
        }
        if (timeRelation != otherTask.timeRelation) {
            return (false);
        }
        if (pLibVersion.equals(otherTask.pLibVersion) == false) {
            return (false);
        }
        if (effort.equals(otherTask.effort) == false) {
            return (false);
        }
        if (comment.equals(otherTask.comment) == false) {
            return (false);
        }

        return (true);
    }

    //--------------------------------------------------------------------------
    //
    // Read & Write XML
    //
    //--------------------------------------------------------------------------
    DmGpmProjectConfig_Task(EcvXmlElement xmlTask) throws EcvXmlElement.NotfoundException {
        assert (xmlTask != null);

        taskId = myGetAttribute(xmlTask, ATTRIBUTE_TASK_ID);
        if (taskId.isEmpty()) {
            throw (new EcvXmlElement.NotfoundException("Attribute for task id", xmlTask.getFullName(), ATTRIBUTE_TASK_ID));
        }
        taskName = myGetAttribute(xmlTask, ATTRIBUTE_TASK_NAME);
        if (taskName.isEmpty()) {
            throw (new EcvXmlElement.NotfoundException("Attribute for task name", xmlTask.getFullName(), ATTRIBUTE_TASK_NAME));
        }
        //
        String tempDescription = "";
        if (xmlTask instanceof EcvXmlContainerElement) {
            EcvXmlElement descriptionElement = ((EcvXmlContainerElement) xmlTask).getOptionalElement(TAGNAME_DESCRIPTION);
            if (descriptionElement instanceof EcvXmlTextElement) {
                tempDescription = ((EcvXmlTextElement) descriptionElement).getText();
            }
        }
        description = tempDescription;
        //
        impactCategory = EnumSet.noneOf(ImpactCategory.class);
        String categories = myGetAttribute(xmlTask, ATTRIBUTE_CATEGORY);
        for (String category : categories.split(",")) {
            impactCategory.add(ImpactCategory.getByValue(category));
        }
        //
        responsibleRole = myGetAttribute(xmlTask, ATTRIBUTE_RESPONSIBLE_ROLE);
        //
        linkedMilestones = new TreeSet<>();
        if (xmlTask instanceof EcvXmlContainerElement) {
            for (EcvXmlElement milestoneElement : ((EcvXmlContainerElement) xmlTask).getElementList(TAGNAME_MILESTONE)) {
                String name = milestoneElement.getAttribute(NAME);
                if (name != null && name.isEmpty() == false) {
                    linkedMilestones.add(name);
                }
            }
        }
        //
        processLink = getLink(xmlTask, TAGNAME_PROCESS);
        artefactName = myGetAttribute(xmlTask, ATTRIBUTE_ARTEFACT_NAME);
        linkToArtefact = getLink(xmlTask, TAGNAME_ARTEFACT);
        //
        String timeString = myGetAttribute(xmlTask, ATTRIBUTE_TIME_RELATION);
        if (timeString.isEmpty() == false) {
            timeRelation = Integer.parseInt(timeString);
        } else {
            timeRelation = 0;
        }
        //
        pLibVersion = myGetAttribute(xmlTask, ATTRIBUTE_PLIB_VERSION);
        effort = myGetAttribute(xmlTask, ATTRIBUTE_EFFORT);
        String tempComment = "";
        if (xmlTask instanceof EcvXmlContainerElement) {
            EcvXmlElement commentElement = ((EcvXmlContainerElement) xmlTask).getOptionalElement(TAGNAME_COMMENT);
            if (commentElement instanceof EcvXmlTextElement) {
                tempComment = ((EcvXmlTextElement) commentElement).getText();
            }
        }
        comment = tempComment;

    }

    private String myGetAttribute(EcvXmlElement element, String attributeName) {
        String s = element.getAttribute(attributeName);
        return (s != null ? s : "");
    }

    private String getLink(EcvXmlElement element, String elementName) {
        if (element instanceof EcvXmlContainerElement) {
            EcvXmlElement linkElement = ((EcvXmlContainerElement) element).getOptionalElement(elementName);
            if (linkElement != null) {
                return (myGetAttribute(linkElement, LINK));
            }
        }
        return ("");
    }

    EcvXmlElement provideAsXml() {

        EcvXmlContainerElement mainContainer = new EcvXmlContainerElement(TAGNAME);

        mainContainer.addAttribute(ATTRIBUTE_TASK_ID, taskId);
        mainContainer.addAttribute(ATTRIBUTE_TASK_NAME, taskName);
        if (description.isEmpty() == false) {
            mainContainer.addElement(new EcvXmlTextElement(TAGNAME_DESCRIPTION, description));
        }
        mainContainer.addAttributeIfNotEmpty(ATTRIBUTE_CATEGORY, impactCategory.stream().map((ImpactCategory c) -> {
            return (c.getText());
        }).collect(Collectors.joining(",")));
        mainContainer.addAttributeIfNotEmpty(ATTRIBUTE_RESPONSIBLE_ROLE, responsibleRole);
        for (String milestone : linkedMilestones) {
            mainContainer.addElement(new EcvXmlEmptyElement(TAGNAME_MILESTONE).addAttribute(NAME, milestone));
        }
        if (processLink.isEmpty() == false) {
            mainContainer.addElement(new EcvXmlEmptyElement(TAGNAME_PROCESS).addAttribute(LINK, processLink));
        }
        if (artefactName != null && artefactName.isEmpty() == false) {
            mainContainer.addAttributeIfNotEmpty(ATTRIBUTE_ARTEFACT_NAME, artefactName);
        }
        if (linkToArtefact.isEmpty() == false) {
            mainContainer.addElement(new EcvXmlEmptyElement(TAGNAME_ARTEFACT).addAttribute(LINK, linkToArtefact));
        }
        mainContainer.addAttribute(ATTRIBUTE_TIME_RELATION, Integer.toString(timeRelation));
        mainContainer.addAttributeIfNotEmpty(ATTRIBUTE_PLIB_VERSION, pLibVersion);
        mainContainer.addAttributeIfNotEmpty(ATTRIBUTE_EFFORT, effort);
        if (comment.isEmpty() == false) {
            mainContainer.addElement(new EcvXmlTextElement(TAGNAME_COMMENT, comment));
        }
        return (mainContainer);
    }

//--------------------------------------------------------------------------
//
// Getter
//
//--------------------------------------------------------------------------
    @Override
    public String getTaskId() {
        return (taskId);
    }

    @Override
    public String getTaskName() {
        return (taskName);
    }

    @Override
    public String getProcessLink() {
        return (processLink);
    }

    @Override
    public String getDescription() {
        return (description);
    }

    @Override
    public String getPlibVersion() {
        return (pLibVersion);
    }

    @Override
    public Source getSource() {
        return (Source.PROJECT);
    }

    @Override
    public EnumSet<ImpactCategory> getImpactCategory() {
        return (impactCategory);
    }

    @Override
    public String getResponsibleRole() {
        return (responsibleRole);
    }

    @Override
    public Set<String> getLinkedMilestones() {
        return (linkedMilestones);
    }

    @Override
    public int getTimeRelation() {
        return (timeRelation);
    }

    @Override
    public String getLinkToArtefact() {
        return linkToArtefact;
    }

    @Override
    public String getArtefactName() {
        return artefactName;
    }

    @Override
    public String getEffort() {
        return (effort);
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return (DmGpmProjectConfig_Task.class.getSimpleName() + "(" + taskId + "," + taskName + ")");
    }

    @Override
    public String getEstimatedComment() {
        return "";
    }
}
