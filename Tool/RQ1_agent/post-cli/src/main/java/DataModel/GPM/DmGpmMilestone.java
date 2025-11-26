/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmConstantField_Enumeration;
import DataModel.DmElement;
import DataModel.DmElementListFieldI;
import DataModel.DmMilestoneI;
import DataModel.DmValueFieldI_Date;
import DataModel.DmValueFieldI_Enumeration_EagerLoad;
import DataModel.DmValueFieldI_Text;
import DataModel.GPM.ConfigData.DmGpmConfig_Task;
import DataModel.GPM.ConfigData.DmGpmConfig_Task.Source;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1User;
import DataModel.Rq1.Records.DmRq1WorkItem;
import DataModel.Rq1.Records.DmRq1WorkItem_Hardware;
import Rq1Data.Enumerations.Type_WorkItem;
import util.EcvDate;
import util.EcvEnumeration;

/**
 * Represents a milestone on a project in the GPM data model. The kind of
 * milestone (e.g. a PVER/PVAR or a milestone defined in the milestone field of
 * the project) is defined by the sub class.
 *
 * @author GUG2WI
 */
public abstract class DmGpmMilestone extends DmElement implements DmMilestoneI {

    public enum Type implements EcvEnumeration {

        RQ1_PROJECT("RQ1-Project"),
        RQ1_MILESTONE("Milestone"),
        PVER("PVER"),
        PVAR("PVAR"),
        ECU("HW-ECU"),
        HIS_MILESTONE("HIS-Milestone"),
        HIS_QUALITY_GATE("HIS-QualityGate"),
        HIS_UNKNOWN("HIS-Unknown"),
        MCR_MILESTONE("MCR-Milestone"),
        MCR_UNKOWN("MCR-Unknown"),
        I_CDM_MILESTONE("iCDM-Milestone");

        private final String text;

        private Type(String text) {
            assert (text != null);
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }

        static public Type getTypeForText(String text) {
            for (Type value : values()) {
                if (value.text.equals(text) == true) {
                    return (value);
                }
            }
            return (null);
        }
    }

    final protected Type type;
    final protected DmRq1Project project;

    final public DmValueFieldI_Enumeration_EagerLoad TYPE;
    public DmValueFieldI_Text NAME;
    public DmValueFieldI_Date DATE;
    public DmValueFieldI_Enumeration_EagerLoad EXPORT_CATEGORY;
    public DmValueFieldI_Enumeration_EagerLoad EXPORT_SCOPE;
    public DmElementListFieldI<DmRq1WorkItem> ALL_WORKITEMS;

    DmGpmMilestone(Type type, String elementType, final DmRq1Project dmRq1Project) {
        super(elementType);
        assert (type != null);
        assert (dmRq1Project != null);

        this.type = type;
        this.project = dmRq1Project;

        addField(TYPE = new DmConstantField_Enumeration("Type", type));
    }

    final public Type getType() {
        return type;
    }

    public DmRq1Project getProject() {
        return (project);
    }

    @Override
    public String getTitle() {
        return (NAME.getValue());
    }

    public EcvDate getValueFromField_DATE() {
        return (DATE != null ? DATE.getDate() : null);
    }

    @Override
    public void reload() {

    }

    @Override
    public String toString() {
        String type = TYPE != null ? TYPE.getValueAsText() : "null";
        String name = NAME != null ? NAME.getValue() : "null";
        String date = DATE != null ? DATE.getValue().getXmlValue() : "null";
        if (date.isEmpty()) {
            date = "empty";
        }
        return (this.getClass().getSimpleName() + "-" + type + "-" + name + "-" + date);
    }

    //--------------------------------------------------------------------------
    //
    // Support for tasks (work items) on the milestone
    //
    //--------------------------------------------------------------------------
    protected <T extends DmRq1WorkItem> T setMilestoneDataAndSaveNewWorkitem(T newWorkitem, DmGpmConfig_Task configTableRow, String title, EcvDate plannedDate, String effort, DmRq1User assignee) {
        assert (newWorkitem != null);

        //-------------------------------------------
        // Set values
        //-------------------------------------------
        if (newWorkitem instanceof DmRq1WorkItem_Hardware) {
            newWorkitem.TYPE.setValueAsText("Other");
        } else {
            newWorkitem.TYPE.setValue(Type_WorkItem.MISC);
        }
        newWorkitem.TITLE.setValue(title);
        newWorkitem.PLANNED_DATE.setValue(plannedDate);
        newWorkitem.EFFORT_ESTIMATION.setValue(effort);
        String linkToPLib = configTableRow.getProcessLink();
        if ((linkToPLib != null) && (linkToPLib.isEmpty() == false)) {
            newWorkitem.INTERNAL_COMMENT.setValue("Link to Plib: " + linkToPLib);
        }
        newWorkitem.PROJECT.setElement(project);
        newWorkitem.GPM_PLIB_VERSION.setValue(configTableRow.getPlibVersion());
        newWorkitem.GPM_TASK_ID.setValue(configTableRow.getTaskId());
        newWorkitem.GPM_PROJECT_ID.setValue(project.getId());
        newWorkitem.GPM_MILESTONE_ID.setValue(getIdForWorkitem());
        String description = configTableRow.getDescription();
        if ((description != null) && (description.isEmpty() == false)) {
            newWorkitem.DESCRIPTION.setValue(description);
        }
        if (assignee != null) {
            newWorkitem.ASSIGNEE.setElement(assignee);
        }

        // GPM_SOURCE and ESTIMATION_COMMENT
        switch (configTableRow.getSource()) {
            case PLIB:
                newWorkitem.ESTIMATION_COMMENT.setValue(configTableRow.getEstimatedComment());
                newWorkitem.GPM_SOURCE.setValue(Source.PLIB.getTextForSource());
                break;

            case PROJECT:
                newWorkitem.GPM_SOURCE.setValue(project.getId());
                break;

            default:
                throw (new Error("Unknown source " + configTableRow.getSource().name()));
        }

        //-------------------------------------------
        // Save (create in database
        //-------------------------------------------
        if (newWorkitem.save() == false) {
            return (null);
        }

        //--------------------------------------------
        // Connect with milestone
        //--------------------------------------------
        ALL_WORKITEMS.addElement(newWorkitem);

        return (newWorkitem);
    }

    protected String getIdForWorkitem() {
        return (getId());
    }

}
