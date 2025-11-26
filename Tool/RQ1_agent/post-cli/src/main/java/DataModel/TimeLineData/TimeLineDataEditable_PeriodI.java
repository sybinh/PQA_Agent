/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import DataModel.CRP.CrpGanttChartModel;
import DataModel.DmElement;
import javafx.scene.input.DragEvent;
import util.EcvDate;

/**
 *
 * @author HRN4KOR
 */
public interface TimeLineDataEditable_PeriodI extends TimeLineData_SingleRowElementI {

    String getToolTipText();

    default String getStyleClassForEditablePeriod() {
        return null;
    }

    DmElement getRowElement();

    CrpGanttChartModel getChartModel();

    DmElement getParentRowElement();

    void setFirstDay(EcvDate firstDay);

    void setLastDay(EcvDate lastDay);

    void setToolTipText(String toolTipText);

    boolean isEditingRequired();

    default void handleMouseDrop(boolean isLoadCurveUpdateRequired) {

    }

    default void handleVerticleDragAndDrop(DragEvent event) {

    }

    default void handleMouseClick() {

    }

    default void handleConnectionsOnRectangleDrop(int noOfDays, EcvDate firstDay, EcvDate lastDay) {

    }

    default void handleConnectionsOnEndDateDrop(int noOfDays, EcvDate firstDay, EcvDate lastDay) {

    }

    default void handleConnectionsOnStartDateDrop( EcvDate firstDay, EcvDate lastDay) {

    }

    String getTitle();

    void setTitle(String title);

    String getAssignee();

    void setAssignee(String assignee);

    String getDescription();

    void setDescription(String desc);

    EcvDate getPlannedDate();

    void setPlannedDate(EcvDate curPlanDate);

    String getEstimatedEffort();

    void setEstimatedEffort(String estEffort);

    String getRemEffort();

    void setRemEffort(String remEffort);
}
