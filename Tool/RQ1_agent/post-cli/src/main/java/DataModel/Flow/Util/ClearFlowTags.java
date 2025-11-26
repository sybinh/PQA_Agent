/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Flow.Util;

import DataModel.Flow.ExpertState;
import DataModel.Flow.FullKitSize;
import DataModel.Flow.FullKitStatus;
import DataModel.Flow.KingState;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1DevelopmentProject;
import DataModel.Rq1.Records.DmRq1ElementCache;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1Irm;
import DataModel.Rq1.Records.DmRq1Irm_Bc_IssueFd;
import DataModel.Rq1.Records.DmRq1IssueFD;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1SoftwareProject;
import DataModel.Rq1.Records.DmRq1SubjectElement;
import DataModel.Rq1.Records.DmRq1SwCustomerProject;
import DataModel.Rq1.Records.DmRq1WorkItem;
import DataModel.DmMappedElement;
import OslcAccess.OslcFieldI;
import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import Rq1Cache.Records.Rq1SubjectInterface;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import util.EcvApplication;
import util.EcvDate;
import util.EcvTableRow;

/**
 *
 * @author rot2cob
 */
public class ClearFlowTags {

    public static Set<DmRq1SoftwareProject> getLinkedProjects(DmRq1SoftwareProject project) {

        IdentityHashMap<DmRq1SoftwareProject, String> linkedProjects = new IdentityHashMap<>();
        if (project != null) {

            if (project instanceof DmRq1SwCustomerProject) {
                DmRq1SwCustomerProject custPrj = (DmRq1SwCustomerProject) project;

                linkedProjects.put((DmRq1SoftwareProject) custPrj, "");
                final List<EcvTableRow> availableRelatedProjRows = custPrj.RELATIVE_PROJECT_CONFIGURATION.getValue().getRows();
                for (EcvTableRow availableClusterRow : availableRelatedProjRows) {

                    String rq1Id = ((String) availableClusterRow.getValueAt(0));
                    Rq1SubjectInterface rq1Project = Rq1Cache.Rq1RecordIndex.getSubjectByRq1Id(rq1Id);
                    if (rq1Project != null) {
                        DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1Project);
                        if (dmElement instanceof DmRq1SwCustomerProject) {
                            linkedProjects.put(((DmRq1SoftwareProject) dmElement), "");

                        }

                    }
                }
            } else if (project instanceof DmRq1DevelopmentProject) {
                DmRq1DevelopmentProject custPrj = (DmRq1DevelopmentProject) project;

                linkedProjects.put((DmRq1SoftwareProject) custPrj, "");
                final List<EcvTableRow> availableRelatedProjRows = custPrj.RELATIVE_PROJECT_CONFIGURATION.getValue().getRows();
                for (EcvTableRow availableClusterRow : availableRelatedProjRows) {

                    String rq1Id = ((String) availableClusterRow.getValueAt(0));
                    Rq1SubjectInterface rq1Project = Rq1Cache.Rq1RecordIndex.getSubjectByRq1Id(rq1Id);
                    if (rq1Project != null) {
                        DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(rq1Project);
                        if (dmElement instanceof DmRq1DevelopmentProject) {
                            linkedProjects.put(((DmRq1SoftwareProject) dmElement), "");

                        }

                    }
                }
            }

        }

        return linkedProjects.keySet();
    }

    public static void clearTags(DmRq1Project targetProject, DmRq1SubjectElement element) {

        if (element instanceof DmRq1IssueFD) {
            DmRq1IssueFD issueFd = (DmRq1IssueFD) element;
            DmRq1SoftwareProject curProject = (DmRq1SoftwareProject) issueFd.PROJECT.getElement();
            if (curProject != null) {
                Set<DmRq1SoftwareProject> linkedProjects = getLinkedProjects(curProject);
                if (!linkedProjects.contains((DmRq1SoftwareProject) targetProject)) {
                    clearIssueFdFlowTags(issueFd);
                }
            }
        } else if (element instanceof DmRq1Bc) {
            DmRq1Bc bcRelease = (DmRq1Bc) element;
            DmRq1SoftwareProject curProject = (DmRq1SoftwareProject) bcRelease.PROJECT.getElement();
            if (curProject != null) {
                Set<DmRq1SoftwareProject> linkedProjects = getLinkedProjects(curProject);
                if (!linkedProjects.contains((DmRq1SoftwareProject) targetProject)) {
                    clearBcFlowTags(bcRelease);
                }
            }
        } else if (element instanceof DmRq1WorkItem) {
            DmRq1WorkItem workitem = (DmRq1WorkItem) element;
            if (workitem.PROJECT.getElement() instanceof DmRq1SoftwareProject) {
                DmRq1SoftwareProject curProject = (DmRq1SoftwareProject) workitem.PROJECT.getElement();
                Set<DmRq1SoftwareProject> linkedProjects = getLinkedProjects(curProject);
                if (!linkedProjects.contains(targetProject)) {
                    clearWIFlowTags(workitem);
                }
            }
        }

    }

    public static void clearIssueFdFlowTags(DmRq1IssueFD issueFd) {
        List<DmMappedElement<DmRq1Irm, DmRq1Release>> elementLists = issueFd.HAS_MAPPED_RELEASES.getElementList();
        if (!elementLists.isEmpty()) {
            for (DmMappedElement<DmRq1Irm, DmRq1Release> elementList : elementLists) {
                DmRq1Irm irm = elementList.getMap();
                Iterator<? extends OslcFieldI> iterator = irm.getRq1Record().getOslcFields().iterator();
                while (iterator.hasNext()) {
                    Object element = iterator.next();
                    if (element instanceof Rq1DatabaseField_Xml) {
                        Rq1DatabaseField_Xml elementXml = (Rq1DatabaseField_Xml) element;
                        if (elementXml.getFieldName().contentEquals("Tags")) {
                            String message = elementXml.provideLastValueFromDbAsStringForDb();
                            Pattern pattern = Pattern.compile("<FLOW>");
                            Matcher matcher = pattern.matcher(message);
                            while (matcher.find()) {
                                DmRq1Release release = elementLists.get(0).getTarget();
                                if (release instanceof DmRq1Bc && irm instanceof DmRq1Irm_Bc_IssueFd) {
                                    irm.FLOW_VERSION.setValue("");
                                    irm.FLOW_GROUP.setValue("");
                                    irm.FLOW_IRM_GROUP.setValue("");
                                    irm.FLOW_KIT_STATUS.setValue(FullKitStatus.EMPTY);
                                    irm.FLOW_RANK.setValue("");
                                    irm.FLOW_R_DATE.setValue("");
                                    irm.FLOW_INTERNAL_RANK.setValue("");
                                    irm.FLOW_CLUSTERNAME.setValue("");
                                    irm.FLOW_CLUSTERID.setValue("");
                                    irm.FLOW_R_EFFORT.setValue("");
                                    irm.FLOW_NO_OF_DEVELOPERS.setValue("");
                                    irm.FLOW_SIZE.setValue(FullKitSize.EMPTY);
                                    irm.FLOW_TO_RED_DATE.setValue(EcvDate.getEmpty());
                                    irm.FLOW_TARGET_DATE.setValue(EcvDate.getEmpty());
                                    irm.FLOW_BOARD_SWIMLANE_HEADING.setValue("");
                                    final List<EcvTableRow> rows = irm.FLOW_SUBTASK.getValue().getRows();
                                    if (!rows.isEmpty()) {
                                        int confirm = JOptionPane.showConfirmDialog(EcvApplication.getMainWindow(), "Do you want to retain the flow subtasks of this I-FD/IRM ?", "Subtask Notification", JOptionPane.YES_NO_OPTION);
                                        if (confirm == JOptionPane.NO_OPTION) {
                                            irm.FLOW_SUBTASK.getTableDescription().removeFlowSubTaskData(irm.FLOW_SUBTASK.getValue());
                                            irm.FLOW_SUBTASK.setValue(irm.FLOW_SUBTASK.getValue());
                                        }
                                    }
                                    List<EcvTableRow> blockerRows = irm.FLOW_BLOCKER.getValue().getRows();
                                    if (!blockerRows.isEmpty()) {
                                        irm.FLOW_BLOCKER.getTableDescription().removeBlockerEntries(irm.FLOW_BLOCKER.getValue());
                                        irm.FLOW_BLOCKER.setValue(irm.FLOW_BLOCKER.getValue());
                                    }
                                    final List<EcvTableRow> subtaskAnalysisRows = irm.FLOW_SUBTASK_ANALYSIS.getValue().getRows();
                                    if (!subtaskAnalysisRows.isEmpty()) {
                                        irm.FLOW_SUBTASK_ANALYSIS.getTableDescription().removeFlowSubTaskData(irm.FLOW_SUBTASK_ANALYSIS.getValue());
                                        irm.FLOW_SUBTASK_ANALYSIS.setValue(irm.FLOW_SUBTASK_ANALYSIS.getValue());
                                    }
                                    final List<EcvTableRow> blockerkAnalysisRows = irm.FLOW_BLOCKER_ANALYSIS.getValue().getRows();
                                    if (!blockerkAnalysisRows.isEmpty()) {
                                        irm.FLOW_BLOCKER_ANALYSIS.getTableDescription().removeBlockerEntries(irm.FLOW_BLOCKER_ANALYSIS.getValue());
                                        irm.FLOW_BLOCKER_ANALYSIS.setValue(irm.FLOW_BLOCKER_ANALYSIS.getValue());
                                    }
                                    final List<EcvTableRow> analysisDoneRows = irm.FLOW_ANALYSIS_DONE_STATE.getValue().getRows();
                                    if (!analysisDoneRows.isEmpty()) {
                                        irm.FLOW_ANALYSIS_DONE_STATE.getTableDescription().removeAnalysisDoneData(irm.FLOW_ANALYSIS_DONE_STATE.getValue());
                                        irm.FLOW_ANALYSIS_DONE_STATE.setValue(irm.FLOW_ANALYSIS_DONE_STATE.getValue());
                                    }
                                    irm.KING_STATE.setValue(KingState.EMPTY);
                                    irm.EXPERT_STATE.setValue(ExpertState.EMPTY);
                                    irm.FLOW_EXP_AVAL_EFFORT.setValue("");
                                    irm.FLOW_EXC_BOARD.setValue("");
                                    irm.FLOW_SIZE_ANALYSIS.setValue(FullKitSize.EMPTY);
                                    irm.FLOW_TO_RED_DATE_ANALYSIS.setValue(EcvDate.getEmpty());
                                    irm.FLOW_ISW_IRM_TASK.setValue("");
                                    final List<EcvTableRow> pverStartedRows = irm.FLOW_PVER_F_STARTED.getValue().getRows();
                                    if (!pverStartedRows.isEmpty()) {
                                        irm.FLOW_PVER_F_STARTED.getTableDescription().removePverFStartData(irm.FLOW_PVER_F_STARTED.getValue());
                                        irm.FLOW_PVER_F_STARTED.setValue(irm.FLOW_ANALYSIS_DONE_STATE.getValue());
                                    }
                                    List<EcvTableRow> criticalReoueceRows = irm.CRITICAL_RESOURCE.getValue().getRows();
                                    if (!criticalReoueceRows.isEmpty()) {
                                        irm.CRITICAL_RESOURCE.getTableDescription().removeCriticalResourceData(irm.CRITICAL_RESOURCE.getValue());
                                        irm.CRITICAL_RESOURCE.setValue(irm.CRITICAL_RESOURCE.getValue());
                                    }

                                    irm.save();
                                }
                            }
                        }
                    }
                }

            }
        }
        Iterator<? extends OslcFieldI> iterator = issueFd.getRq1Record().getOslcFields().iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            if (element instanceof Rq1DatabaseField_Xml) {
                Rq1DatabaseField_Xml elementXml = (Rq1DatabaseField_Xml) element;
                if (elementXml.getFieldName().contentEquals("Tags")) {
                    String message = elementXml.provideLastValueFromDbAsStringForDb();
                    Pattern pattern = Pattern.compile("<FLOW>");
                    Matcher matcher = pattern.matcher(message);
                    while (matcher.find()) {
                        issueFd.FLOW_VERSION.setValue("");
                        issueFd.FLOW_GROUP.setValue("");
                        issueFd.FLOW_KIT_STATUS.setValue(FullKitStatus.EMPTY);
                        issueFd.FLOW_RANK.setValue("");
                        issueFd.FLOW_R_DATE.setValue("");
                        issueFd.FLOW_INTERNAL_RANK.setValue("");
                        issueFd.FLOW_CLUSTERNAME.setValue("");
                        issueFd.FLOW_CLUSTERID.setValue("");
                        issueFd.FLOW_R_EFFORT.setValue("");
                        issueFd.FLOW_NO_OF_DEVELOPERS.setValue("");
                        issueFd.FLOW_SIZE.setValue(FullKitSize.EMPTY);
                        issueFd.FLOW_TO_RED_DATE.setValue(EcvDate.getEmpty());
                        issueFd.FLOW_TARGET_DATE.setValue(EcvDate.getEmpty());
                        issueFd.FLOW_BOARD_SWIMLANE_HEADING.setValue("");
                        List<EcvTableRow> rows = issueFd.FLOW_SUBTASK.getValue().getRows();
                        if (!rows.isEmpty()) {
                            int confirm = JOptionPane.showConfirmDialog(EcvApplication.getMainWindow(), "Do you want to retain the flow subtasks of this I-FD ?", "Subtask Notification", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.NO_OPTION) {
                                issueFd.FLOW_SUBTASK.getTableDescription().removeFlowSubTaskData(issueFd.FLOW_SUBTASK.getValue());
                                issueFd.FLOW_SUBTASK.setValue(issueFd.FLOW_SUBTASK.getValue());
                            }
                        }
                        List<EcvTableRow> blockerRows = issueFd.FLOW_BLOCKER.getValue().getRows();
                        if (!blockerRows.isEmpty()) {
                            issueFd.FLOW_BLOCKER.getTableDescription().removeBlockerEntries(issueFd.FLOW_BLOCKER.getValue());
                            issueFd.FLOW_BLOCKER.setValue(issueFd.FLOW_BLOCKER.getValue());
                        }
                        issueFd.KING_STATE.setValue(KingState.EMPTY);
                        issueFd.EXPERT_STATE.setValue(ExpertState.EMPTY);
                        issueFd.FLOW_EXP_AVAL_EFFORT.setValue("");
                        issueFd.FLOW_EXC_BOARD.setValue("");
                        List<EcvTableRow> criticalReoueceRows = issueFd.CRITICAL_RESOURCE.getValue().getRows();
                        if (!criticalReoueceRows.isEmpty()) {
                            issueFd.CRITICAL_RESOURCE.getTableDescription().removeCriticalResourceData(issueFd.CRITICAL_RESOURCE.getValue());
                            issueFd.CRITICAL_RESOURCE.setValue(issueFd.CRITICAL_RESOURCE.getValue());
                        }
                        issueFd.save();
                    }
                }
            }
        }

    }

    public static void clearBcFlowTags(DmRq1Bc bcRelease) {
        Iterator<? extends OslcFieldI> iterator = bcRelease.getRq1Record().getOslcFields().iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            if (element instanceof Rq1DatabaseField_Xml) {
                Rq1DatabaseField_Xml elementXml = (Rq1DatabaseField_Xml) element;
                if (elementXml.getFieldName().contentEquals("Tags")) {
                    String message = elementXml.provideLastValueFromDbAsStringForDb();
                    Pattern pattern = Pattern.compile("<FLOW>");
                    Matcher matcher = pattern.matcher(message);
                    while (matcher.find()) {
                        bcRelease.FLOW_VERSION.setValue("");
                        bcRelease.FLOW_GROUP.setValue("");
                        bcRelease.FLOW_IRM_GROUP.setValue("");
                        bcRelease.FLOW_KIT_STATUS.setValue(FullKitStatus.EMPTY);
                        bcRelease.FLOW_RANK.setValue("");
                        bcRelease.FLOW_R_DATE.setValue("");
                        bcRelease.FLOW_INTERNAL_RANK.setValue("");
                        bcRelease.FLOW_CLUSTERNAME.setValue("");
                        bcRelease.FLOW_CLUSTERID.setValue("");
                        bcRelease.FLOW_R_EFFORT.setValue("");
                        bcRelease.FLOW_NO_OF_DEVELOPERS.setValue("");
                        bcRelease.FLOW_SIZE.setValue(FullKitSize.EMPTY);
                        bcRelease.FLOW_TO_RED_DATE.setValue(EcvDate.getEmpty());
                        bcRelease.FLOW_TARGET_DATE.setValue(EcvDate.getEmpty());
                        bcRelease.FLOW_BOARD_SWIMLANE_HEADING.setValue("");
                        List<EcvTableRow> rows = bcRelease.FLOW_SUBTASK.getValue().getRows();
                        if (!rows.isEmpty()) {
                            int confirm = JOptionPane.showConfirmDialog(EcvApplication.getMainWindow(), "Do you want to retain the flow subtasks of this BC ?", "Subtask Notification", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.NO_OPTION) {
                                bcRelease.FLOW_SUBTASK.getTableDescription().removeFlowSubTaskData(bcRelease.FLOW_SUBTASK.getValue());
                                bcRelease.FLOW_SUBTASK.setValue(bcRelease.FLOW_SUBTASK.getValue());
                            }
                        }
                        List<EcvTableRow> blockerRows = bcRelease.FLOW_BLOCKER.getValue().getRows();
                        if (!blockerRows.isEmpty()) {
                            bcRelease.FLOW_BLOCKER.getTableDescription().removeBlockerEntries(bcRelease.FLOW_BLOCKER.getValue());
                            bcRelease.FLOW_BLOCKER.setValue(bcRelease.FLOW_BLOCKER.getValue());
                        }
                        bcRelease.KING_STATE.setValue(KingState.EMPTY);
                        bcRelease.EXPERT_STATE.setValue(ExpertState.EMPTY);
                        bcRelease.FLOW_EXP_AVAL_EFFORT.setValue("");
                        bcRelease.FLOW_EXC_BOARD.setValue("");
                        List<EcvTableRow> criticalReoueceRows = bcRelease.CRITICAL_RESOURCE.getValue().getRows();
                        if (!criticalReoueceRows.isEmpty()) {
                            bcRelease.CRITICAL_RESOURCE.getTableDescription().removeCriticalResourceData(bcRelease.CRITICAL_RESOURCE.getValue());
                            bcRelease.CRITICAL_RESOURCE.setValue(bcRelease.CRITICAL_RESOURCE.getValue());
                        }
                        bcRelease.save();
                    }
                }
            }

        }

    }

    public static void clearWIFlowTags(DmRq1WorkItem workitem) {
        Iterator<? extends OslcFieldI> iterator = workitem.getRq1Record().getOslcFields().iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            if (element instanceof Rq1DatabaseField_Xml) {
                Rq1DatabaseField_Xml elementXml = (Rq1DatabaseField_Xml) element;
                if (elementXml.getFieldName().contentEquals("Tags")) {
                    String message = elementXml.provideLastValueFromDbAsStringForDb();
                    Pattern pattern = Pattern.compile("<FLOW>");
                    Matcher matcher = pattern.matcher(message);
                    while (matcher.find()) {
                        workitem.FLOW_VERSION.setValue("");
                        workitem.FLOW_GROUP.setValue("");
                        workitem.FLOW_IRM_GROUP.setValue("");
                        workitem.FLOW_KIT_STATUS.setValue(FullKitStatus.EMPTY);
                        workitem.FLOW_RANK.setValue("");
                        workitem.FLOW_R_DATE.setValue("");
                        workitem.FLOW_INTERNAL_RANK.setValue("");
                        workitem.FLOW_CLUSTERNAME.setValue("");
                        workitem.FLOW_CLUSTERID.setValue("");
                        workitem.FLOW_R_EFFORT.setValue("");
                        workitem.FLOW_NO_OF_DEVELOPERS.setValue("");
                        workitem.FLOW_WI_TASK.setValue("");
                        workitem.FLOW_RELATED_WI.setValue("");
                        workitem.FLOW_SIZE.setValue(FullKitSize.EMPTY);
                        workitem.FLOW_TO_RED_DATE.setValue(EcvDate.getEmpty());
                        workitem.FLOW_TARGET_DATE.setValue(EcvDate.getEmpty());
                        workitem.FLOW_BOARD_SWIMLANE_HEADING.setValue("");
                        workitem.PARENT_SWIMLANE.setValue("");
                        List<EcvTableRow> rows = workitem.FLOW_SUBTASK.getValue().getRows();
                        if (!rows.isEmpty()) {
                            if (rows.size() == 1 && rows.get(0).getValues()[3].toString().equals(workitem.getId())) {
                                //dont ask for retainment
                                workitem.FLOW_SUBTASK.getTableDescription().removeFlowSubTaskData(workitem.FLOW_SUBTASK.getValue());
                                workitem.FLOW_SUBTASK.setValue(workitem.FLOW_SUBTASK.getValue());
                            } else {
                                int confirm = JOptionPane.showConfirmDialog(EcvApplication.getMainWindow(), "Do you want to retain the flow subtasks of this Workitem ?", "Subtask Notification", JOptionPane.YES_NO_OPTION);
                                if (confirm == JOptionPane.NO_OPTION) {
                                    workitem.FLOW_SUBTASK.getTableDescription().removeFlowSubTaskData(workitem.FLOW_SUBTASK.getValue());
                                    workitem.FLOW_SUBTASK.setValue(workitem.FLOW_SUBTASK.getValue());
                                }
                            }
                        }
                        List<EcvTableRow> blockerRows = workitem.FLOW_BLOCKER.getValue().getRows();
                        if (!blockerRows.isEmpty()) {
                            workitem.FLOW_BLOCKER.getTableDescription().removeBlockerEntries(workitem.FLOW_BLOCKER.getValue());
                            workitem.FLOW_BLOCKER.setValue(workitem.FLOW_BLOCKER.getValue());
                        }
                        workitem.KING_STATE.setValue(KingState.EMPTY);
                        workitem.EXPERT_STATE.setValue(ExpertState.EMPTY);
                        workitem.FLOW_EXP_AVAL_EFFORT.setValue("");
                        workitem.FLOW_EXC_BOARD.setValue("");
                        List<EcvTableRow> criticalReoueceRows = workitem.CRITICAL_RESOURCE.getValue().getRows();
                        if (!criticalReoueceRows.isEmpty()) {
                            workitem.CRITICAL_RESOURCE.getTableDescription().removeCriticalResourceData(workitem.CRITICAL_RESOURCE.getValue());
                            workitem.CRITICAL_RESOURCE.setValue(workitem.CRITICAL_RESOURCE.getValue());
                        }
                        workitem.save();
                    }
                }
            }
        }
    }

}
