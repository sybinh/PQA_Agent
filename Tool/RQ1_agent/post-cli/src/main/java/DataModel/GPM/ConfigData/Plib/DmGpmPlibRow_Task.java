/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData.Plib;

import TablePlus.Csv.CsvLoadException;
import java.util.Set;

/**
 *
 * @author GUG2WI
 */
public class DmGpmPlibRow_Task extends DmGpmPlibRow<String> {

    //--------------------------------------------------------------------------
    //
    // Position of fields in the CSV file
    //
    //--------------------------------------------------------------------------
    static final int TASK_ID = 0;
    static final int TASK_NAME = 1;
    static final int TASK_STATUS_ID = 2;
    static final int RESPONSIBLE_ROLE_ID = 3;
    static final int PROCESS_LINK = 4;
    static final int ARTIFACT_ID = 5;
    static final int DESCRIPTION = 6;
    static final int TIMELY_RELATION = 7;
    static final int PLIB_VERSION_ID = 8;
    static final int MILESTONE_IDS = 9;
    static final int IMPACT_CATEGORY_IDS = 10;
    static final int PREDECESSOR_TASK_IDS = 11;
    static final int OPTIMISTIC_ESTIMATE = 12;
    static final int OPTIMISTIC_ESTIMATE_COMMENT = 13;
    static final int MOST_LIKELY_ESTIMATE = 14;
    static final int MOST_LIKELY_ESTIMATE_COMMENT = 15;
    static final int PESSIMISTIC_ESTIMATE = 16;
    static final int PESSIMISTIC_ESTIMATE_COMMENT = 17;
    static final int FINAL_ESTIMATION = 18;
    static final int LESSONS_LEARNED_FACTOR = 19;
    static final int EFFORT_ESTIMATION = 20;
    static final int REMARKS_ASSUMPTIONS = 21;
    static final int STUDY_PROJECT_ID = 22;
    static final int SWAAP_ID = 23;
    static final int PRODUCT_LICENSED_TO_CUSTOMER = 24;
    static final int EXTERNAL_SUPPLIERS_SUB_PROJECT = 25;
    static final int RBInternalSWSupplierNoPSECGuidelines = 26;
    static final int EndeavourIsInnovation = 27;
    static final int OBDResponsibilityIsAtRB = 28;
    static final int NonGenericConfigManagement = 29;
    static final int SWSharingIsDoneInProject = 30;
    static final int PrjIsNotSuccessorProject = 31;
    static final int HasSecurityRelevantRequirements = 32;
    static final int ASILD = 33;
    static final int ASILC = 34;
    static final int ASILB = 35;
    static final int ASILA = 36;
    static final int QM = 37;
    static final int EDC17Prj = 38;
    static final int SWaaPIsCustPrj = 39;

    DmGpmPlibRow_Task(String[] rowValues, int rowNumber) throws CsvLoadException {
        super(rowValues, rowNumber, 40);
    }

    @Override
    public String getId() {
        return (getStringValueForColumn(TASK_ID));
    }

    public String getName() {
        return (getStringValueForColumn(TASK_NAME));
    }

    public int getTaskStatusId() {
        return (getIntValueForColumn(TASK_STATUS_ID));
    }

    public int getResponsibleRoleId() {
        return (getIntValueForColumn(RESPONSIBLE_ROLE_ID));
    }

    public String getProcessLink() {
        return (getLinkValueForColumn(PROCESS_LINK));
    }

    public Set<Integer> getArtifactIds() {
        return (getIntValuesForColumn(ARTIFACT_ID));
    }

    public String getDescription() {
        return (getStringValueForColumn(DESCRIPTION));
    }

    public int getTimelyRelation() {
        return (getIntValueForColumn(TIMELY_RELATION));
    }

    public int getPlibVersionId() {
        return (getIntValueForColumn(PLIB_VERSION_ID));
    }

    public Set<Integer> getMilestoneIds() {
        return (getIntValuesForColumn(MILESTONE_IDS));
    }

    public Set<Integer> getImpactCategoryIds() {
        return (getIntValuesForColumn(IMPACT_CATEGORY_IDS));
    }

    public Set<String> getPredecessorIds() {
        return (getStringValuesForColumn(PREDECESSOR_TASK_IDS));
    }

    public String getEffortEstimation() {
        return (getStringValueForColumn(EFFORT_ESTIMATION));
    }

    public boolean getQM() {
        return (getBooleanValueForColumn(QM));
    }

    @Override
    public String toString() {
        return ("Task:" + getId() + "," + getName() + "," + getResponsibleRoleId() + "," + getMilestoneIds());
    }

}
