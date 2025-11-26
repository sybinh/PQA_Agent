/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import DataModel.GPM.ConfigData.DmGpmConfig_Task;
import DataModel.GPM.DmGpmMilestone;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1WorkItem;
import Rq1Cache.Records.Rq1WorkItem;
import Rq1Data.Enumerations.ImpactCategory;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import util.EcvDate;

/**
 * A task loaded from the PLib-Configuration.
 *
 * @author GUG2WI
 */
public class DmGpmPlibConfig_Task implements DmGpmConfig_Task {

    final private static Logger LOGGER = Logger.getLogger(DmGpmPlibConfig_Task.class.getCanonicalName());

    final private DmGpmPlibRow_Task taskRow;

    //--------------------------------------------------------------------------
    //
    // Creation of row based on data from the CSV file
    //
    //--------------------------------------------------------------------------
    DmGpmPlibConfig_Task(DmGpmPlibRow_Task taskRow) {
        assert (taskRow != null);
        this.taskRow = taskRow;
    }

    @Override
    public Source getSource() {
        return (Source.PLIB);
    }

    //--------------------------------------------------------------------------
    //
    // (Read-)Access to row content
    //
    //--------------------------------------------------------------------------
    @Override
    public String getTaskName() {
        return (taskRow.getName());
    }

    public boolean isTaskValid() {
        int statusId = taskRow.getTaskStatusId();
        return (DmGpmPlibTable_TaskStatus.table().getRowById(statusId).isValid());
    }

    @Override
    public EnumSet<ImpactCategory> getImpactCategory() {
        EnumSet<ImpactCategory> result = EnumSet.noneOf(ImpactCategory.class);
        Set<Integer> impCatIds = taskRow.getImpactCategoryIds();
        var impCatRows = DmGpmPlibTable_ImpactCategories.table().getRowsById(impCatIds);
        for (var row : impCatRows) {
            result.add(row.getCategory());
        }
        return (result);
    }

    @Override
    public String getResponsibleRole() {
        int roleId = taskRow.getResponsibleRoleId();
        return (DmGpmPlibTable_Roles.table().getRowById(roleId).getShortName());
    }

    /**
     * Returns the link to the description of the task in the process lib.
     *
     * @return Link to task description.
     */
    @Override
    public String getProcessLink() {
        return (taskRow.getProcessLink());
    }

    @Override
    public String getLinkToArtefact() {
        List<DmGpmPlibRow_Artifact> artefacts = DmGpmPlibTable_Artifacts.table().getRowsById(taskRow.getArtifactIds());
        if (artefacts.size() > 0) {
            return (artefacts.get(0).getLink());
        } else {
            return (null);
        }
    }

    @Override
    public String getArtefactName() {
        List<DmGpmPlibRow_Artifact> artefacts = DmGpmPlibTable_Artifacts.table().getRowsById(taskRow.getArtifactIds());
        if (artefacts.size() > 0) {
            return (artefacts.get(0).getName());
        } else {
            return (null);
        }
    }

    /**
     * Name of the milestone that is connected with this row.
     *
     * @return Name of the milestone.
     */
    @Override
    public Set<String> getLinkedMilestones() {
        Set<String> result = new TreeSet<>();
        List<DmGpmPlibRow_Milestone> rows = DmGpmPlibTable_Milestones.table().getRowsById(taskRow.getMilestoneIds());
        for (var row : rows) {
            String trimedMilestone = row.getName().trim();
            if (trimedMilestone.isEmpty() == false) {
                result.add(trimedMilestone);
            }
        }
        return (result);
    }

    @Override
    public String getDescription() {
        return (taskRow.getDescription());
    }

    /**
     * The offset in days of this task in relation to the date of the connected
     * milestones.
     */
    @Override
    final public int getTimeRelation() {
        return (taskRow.getTimelyRelation());
    }

    @Override
    public String getPlibVersion() {
        DmGpmPlibRow_PlibVersion row = DmGpmPlibTable_PLibVersions.table().getRowById(taskRow.getPlibVersionId());
        return (row.getName());
    }

    @Override
    public String getEffort() {
        return (taskRow.getEffortEstimation());
    }

    @Override
    public String getTaskId() {
        return (taskRow.getId());
    }

    @Override
    public String getEstimatedComment() {
        return "";
    }

    public Set<String> getPredecessorIds(){
        return taskRow.getPredecessorIds();
    }

    //--------------------------------------------------------------------------
    //
    // Private methods to simplify field access
    //
    //--------------------------------------------------------------------------
    private String getInternalComment() {
        String internalComment = "";
        if ((getProcessLink() != null) && (getProcessLink().isEmpty() == false)) {
            internalComment += "Link to Plib: " + getProcessLink() + "\n";
        }
        if ((getLinkToArtefact() != null) && (getLinkToArtefact().isEmpty() == false)) {
            internalComment += "Link to artifact: " + getLinkToArtefact();
        }
        return (internalComment);
    }

    /**
     * Sets the values in the given element to those from the config row.
     *
     * @param dmRq1Element
     */
    public void execute(DmRq1ElementInterface dmRq1Element, String milestoneName) {
        if (dmRq1Element instanceof DmRq1WorkItem) {
            DmRq1WorkItem dmRq1Workitem = (DmRq1WorkItem) dmRq1Element;
            Rq1WorkItem rq1Workitem = (Rq1WorkItem) dmRq1Workitem.getRq1Record();

            rq1Workitem.TYPE.setValueFromTemplate("Misc");
            rq1Workitem.TITLE.setValueFromTemplate(getTaskName());
            rq1Workitem.DESCRIPTION.setValueFromTemplate(getDescription());
            rq1Workitem.INTERNAL_COMMENT.setValueFromTemplate(getInternalComment());

            EcvDate referenceDate = null;
            if (dmRq1Workitem.PROJECT.isElementSet()) {
                DmRq1Project project = dmRq1Workitem.PROJECT.getElement();
                for (DmGpmMilestone milestone : project.TIME_SCHEDULE_OF_PROJECT.getElementList()) {
                    if (getLinkedMilestones().contains(milestone.NAME.getValueAsText()) == true) {
                        referenceDate = milestone.DATE.getValue();
                    }
                }
            }

            if (referenceDate != null) {
                EcvDate plannedDate = referenceDate.addDays(getTimeRelation());
                dmRq1Workitem.PLANNED_DATE.setValue(plannedDate);
            }

        }

    }

    //--------------------------------------------------------------------------
    //
    // Support automatic check
    //
    //--------------------------------------------------------------------------
    public String isRowOk(String textForError) {

        List<String> errors = new ArrayList<>();

        if (getPlibVersion().startsWith("05")) {
            errors.add("Invalid PlibVersion: " + getPlibVersion());
        }

        if (isTextOk(getTaskName(), allowedPatternSingleline) == false) {
            errors.add("Invalid title.");
        }

        if (isTextOk(getDescription(), allowedPatternMultiline) == false) {
            errors.add("Invalid description.");
        }

        if (getTaskName().trim().isEmpty()) {
            errors.add("Empty task name.");
        }

        if (getTaskId().trim().isEmpty()) {
            errors.add("Empty task id.");
        }

        if (getArtefactName().trim().isEmpty() == false) {
            if (getLinkToArtefact().trim().isEmpty()) {
                errors.add("Empty link to artefact.");
            }
        }
        if (getLinkToArtefact().trim().isEmpty() == false) {
            if (getArtefactName().trim().isEmpty()) {
                errors.add("Empty name to artefact.");
            }
        }

        if (errors.isEmpty() == false) {
            StringBuilder b = new StringBuilder();
            b.append("Defect row: ").append(textForError);
            boolean isFirst = true;
            for (String error : errors) {
                if (isFirst == false) {
                    b.append("\n");
                }
                b.append(error);
                isFirst = false;
            }
            LOGGER.severe(b.toString());
            return (b.toString());
        }

        return (null);
    }

    final private static String allowedCharactersMultiline = "^.*([^-\\*\\[\\]\\?a-zA-Z0-9 _:/'\\.@\\n)(\\,&\";]).*$";
    final private static Pattern allowedPatternMultiline = Pattern.compile(allowedCharactersMultiline);

    final private static String allowedCharactersSingleline = "^.*([^-\\*\\[\\]\\?a-zA-Z0-9 _:/'\\.@)(\\,&\";]).*$";
    final private static Pattern allowedPatternSingleline = Pattern.compile(allowedCharactersSingleline);

    static boolean isTextOkSingleLine(String text) {
        return (isTextOk(text, allowedPatternSingleline));
    }

    static boolean isTextOkMultiLine(String text) {
        return (isTextOk(text, allowedPatternMultiline));
    }

    static boolean isTextOk(String text, Pattern pattern) {

        Matcher m = pattern.matcher(text);

        if (m.matches()) {
            LOGGER.severe("Invalid character in text at: " + m.start(1) + "\n"
                    + "0----+----1----+----2----+----3----+----4----+----5----+----6----+----7----+----8----+----9----+----0----+----1----+----2----+----3----+----4----+----5----+----6\n"
                    + text);
            return (false);
        }

        return (true);
    }

    @Override
    public String toString() {
        var b = new StringBuilder();
        b.append("DmGpmPlibConfig_Task(");
        b.append(getTaskName()).append(';');
        String predecessorIds = taskRow.getPredecessorIds().stream().collect(Collectors.joining(",", "", ";"));
        b.append(predecessorIds);
        DmGpmPlibRow_TaskStatus statusRow = DmGpmPlibTable_TaskStatus.table().getRowById(taskRow.getTaskStatusId());
        b.append(statusRow.isValid()).append(';');
        String categories = getImpactCategory().stream().map(category -> category.getText()).collect(Collectors.joining(",", "", ";"));
        b.append(categories);
        b.append(getResponsibleRole()).append(';');
        b.append(getProcessLink()).append(';');
        b.append(getArtefactName()).append(';');
        b.append(getLinkToArtefact()).append(';');
        String milestones = getLinkedMilestones().stream().collect(Collectors.joining(",", "", ";"));
        b.append(milestones);
        b.append(getTimeRelation()).append(';');
        b.append(getPlibVersion()).append(';');
        b.append(getEffort()).append(';');
        b.append(getTaskId());
        b.append(')');
        return (b.toString());
    }

}
