/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData;

import java.util.Set;
import java.util.TreeSet;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 * Describes the delta for a Plib Task in a project.
 *
 * @author GUG2WI
 */
public class DmGpmProjectConfig_PlibTaskDelta implements Comparable<DmGpmProjectConfig_PlibTaskDelta> {

    public static final String TAGNAME = "TaskDelta";

    private static final String ATTRIBUTE_TASK_ID = "taskId";

    private static final String TAGNAME_COMMENT = "Comment";
    private static final String TAGNAME_REMOVED_MILESTONE = "RemovedMilestone";
    private static final String TAGNAME_ADDED_MILESTONE = "AddedMilestone";
    private static final String TAGNAME_EFFORT = "Effort";
    private static final String TAGNAME_TIME_RELATION = "TimeRelation";
    private static final String TAGNAME_RESPONSIBLE = "Responsible";

    private static final String NAME = "name";

    final private String pLibTaskId;
    final private String comment;
    final private String effort;
    final private String timeRelation;
    final private String responsible;

    final private Set<String> removedMilestoneTypes;
    final private Set<String> addedMilestoneTypes;

    public DmGpmProjectConfig_PlibTaskDelta(String pLibTaskId, String comment, String effort, String timeRelation, String responsible) {
        assert (pLibTaskId != null);
        assert (pLibTaskId.isEmpty() == false);

        this.pLibTaskId = pLibTaskId;
        this.comment = comment != null ? comment : "";
        this.effort = effort != null ? effort : "";
        this.timeRelation = timeRelation != null ? timeRelation : "";
        this.responsible = responsible != null ? responsible : "";
        removedMilestoneTypes = new TreeSet<>();
        addedMilestoneTypes = new TreeSet<>();
    }

    public DmGpmProjectConfig_PlibTaskDelta addMilestone(String milestoneTypeName) {
        assert (milestoneTypeName != null);
        assert (milestoneTypeName.isEmpty() == false);

        removedMilestoneTypes.remove(milestoneTypeName);
        addedMilestoneTypes.add(milestoneTypeName);
        return (this);
    }

    public DmGpmProjectConfig_PlibTaskDelta removeMilestone(String milestoneTypeName) {
        assert (milestoneTypeName != null);
        assert (milestoneTypeName.isEmpty() == false);

        addedMilestoneTypes.remove(milestoneTypeName);
        removedMilestoneTypes.add(milestoneTypeName);
        return (this);
    }

    @Override
    public int compareTo(DmGpmProjectConfig_PlibTaskDelta other) {
        return (pLibTaskId.compareTo(other.pLibTaskId));
    }

    @Override
    public boolean equals(Object other) {

        if (other instanceof DmGpmProjectConfig_PlibTaskDelta == false) {
            return (false);
        }
        DmGpmProjectConfig_PlibTaskDelta otherDelta = (DmGpmProjectConfig_PlibTaskDelta) other;

        if (pLibTaskId.equals(otherDelta.pLibTaskId) == false) {
            return (false);
        }

        if (comment.equals(otherDelta.comment) == false) {
            return (false);
        }

        if (effort.equals(otherDelta.effort) == false) {
            return (false);
        }

        if (timeRelation.equals(otherDelta.timeRelation) == false) {
            return (false);
        }

        if (responsible.equals(otherDelta.responsible) == false) {
            return (false);
        }

        if (removedMilestoneTypes.equals(otherDelta.removedMilestoneTypes) == false) {
            return (false);
        }

        if (addedMilestoneTypes.equals(otherDelta.addedMilestoneTypes) == false) {
            return (false);
        }

        return (true);
    }

    public String toString() {
        return ("DmGpmProjectConfig_PlibTaskDelta:" + pLibTaskId);
    }

    public String getpLibTaskId() {
        return pLibTaskId;
    }

    public String getComment() {
        return comment;
    }

    public String getEffort() {
        return effort;
    }

    public String getTimeRelation() {
        return timeRelation;
    }

    public String getResponsible() {
        return responsible;
    }

    public Set<String> getRemovedMilestoneTypes() {
        return removedMilestoneTypes;
    }

    public Set<String> getAddedMilestoneTypes() {
        return addedMilestoneTypes;
    }

    //--------------------------------------------------------------------------
    //
    // Read & Write XML
    //
    //--------------------------------------------------------------------------
    DmGpmProjectConfig_PlibTaskDelta(EcvXmlElement xmlDelta) throws EcvXmlElement.NotfoundException {

        pLibTaskId = xmlDelta.getAttribute(ATTRIBUTE_TASK_ID);
        if (pLibTaskId == null || pLibTaskId.isEmpty()) {
            throw (new EcvXmlElement.NotfoundException("Attribute for task id", xmlDelta.getFullName(), ATTRIBUTE_TASK_ID));
        }
        //
        String tempComment = "";
        if (xmlDelta instanceof EcvXmlContainerElement) {
            EcvXmlElement commentElement = ((EcvXmlContainerElement) xmlDelta).getOptionalElement(TAGNAME_COMMENT);
            if (commentElement instanceof EcvXmlTextElement) {
                tempComment = ((EcvXmlTextElement) commentElement).getText();
            }
        }
        comment = tempComment;
        //
        String tempEffort = "";
        if (xmlDelta instanceof EcvXmlContainerElement) {
            EcvXmlElement effortElement = ((EcvXmlContainerElement) xmlDelta).getOptionalElement(TAGNAME_EFFORT);
            if (effortElement instanceof EcvXmlTextElement) {
                tempEffort = ((EcvXmlTextElement) effortElement).getText();
            }
        }
        effort = tempEffort;
        //
        String tempTimeRelation = "";
        if (xmlDelta instanceof EcvXmlContainerElement) {
            EcvXmlElement timeRelationElement = ((EcvXmlContainerElement) xmlDelta).getOptionalElement(TAGNAME_TIME_RELATION);
            if (timeRelationElement instanceof EcvXmlTextElement) {
                tempTimeRelation = ((EcvXmlTextElement) timeRelationElement).getText();
            }
        }
        timeRelation = tempTimeRelation;
        //
        String tempResponsible = "";
        if (xmlDelta instanceof EcvXmlContainerElement) {
            EcvXmlElement responsibleElement = ((EcvXmlContainerElement) xmlDelta).getOptionalElement(TAGNAME_RESPONSIBLE);
            if (responsibleElement instanceof EcvXmlTextElement) {
                tempResponsible = ((EcvXmlTextElement) responsibleElement).getText();
            }
        }
        responsible = tempResponsible;
        //
        removedMilestoneTypes = new TreeSet<>();
        if (xmlDelta instanceof EcvXmlContainerElement) {
            for (EcvXmlElement milestoneElement : ((EcvXmlContainerElement) xmlDelta).getElementList(TAGNAME_REMOVED_MILESTONE)) {
                String name = milestoneElement.getAttribute(NAME);
                if (name != null && name.isEmpty() == false) {
                    removedMilestoneTypes.add(name);
                }
            }
        }
        //
        addedMilestoneTypes = new TreeSet<>();
        if (xmlDelta instanceof EcvXmlContainerElement) {
            for (EcvXmlElement milestoneElement : ((EcvXmlContainerElement) xmlDelta).getElementList(TAGNAME_ADDED_MILESTONE)) {
                String name = milestoneElement.getAttribute(NAME);
                if (name != null && name.isEmpty() == false) {
                    addedMilestoneTypes.add(name);
                }
            }
        }

    }

    EcvXmlElement provideAsXml() {
        EcvXmlContainerElement mainContainer = new EcvXmlContainerElement(TAGNAME);

        mainContainer.addAttribute(ATTRIBUTE_TASK_ID, pLibTaskId);
        if (comment.isEmpty() == false) {
            mainContainer.addElement(new EcvXmlTextElement(TAGNAME_COMMENT, comment));
        }
        if (effort.isEmpty() == false) {
            mainContainer.addElement(new EcvXmlTextElement(TAGNAME_EFFORT, effort));
        }
        if (timeRelation.isEmpty() == false) {
            mainContainer.addElement(new EcvXmlTextElement(TAGNAME_TIME_RELATION, timeRelation));
        }
        if (responsible.isEmpty() == false) {
            mainContainer.addElement(new EcvXmlTextElement(TAGNAME_RESPONSIBLE, responsible));
        }
        for (String milestone : removedMilestoneTypes) {
            mainContainer.addElement(new EcvXmlEmptyElement(TAGNAME_REMOVED_MILESTONE).addAttribute(NAME, milestone));
        }
        for (String milestone : addedMilestoneTypes) {
            mainContainer.addElement(new EcvXmlEmptyElement(TAGNAME_ADDED_MILESTONE).addAttribute(NAME, milestone));
        }

        return (mainContainer);
    }

}
