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
import DataModel.Rq1.Records.DmRq1WorkItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author HRN4KOR
 */
public class TimeLineDataEditablePeriod implements TimeLineDataEditable_PeriodI {

    private String toolTipText;
    private EcvDate firstDay;
    private EcvDate lastDay;
    final private String barStyleForEditablePeriod;
    final private DmElement currRowElement;
    private CrpGanttChartModel chartModel;
    final private DmElement parentRowElement;
    private Boolean isEditingRequired = false;
    final private List<TimeLineData_ConnectionI> toConnections = new ArrayList<>();
    private String assigneeName = null;
    private String title = null;
    private EcvDate plannedDate = null;
    private String estimatedEffort = null;
    private String remEffort = null;
    private String description = null;

    /**
     *
     * @param toolTipText given string will be set has toolTip of bar.
     * @param firstDay given firstDay date is the source x start point of bar.
     * @param lastDay given lastDay date is the destination x end point of bar.
     * @param barStyleCss given style will be applied to bar in a chart.
     * @param rowElement
     */
    public TimeLineDataEditablePeriod(String toolTipText, EcvDate firstDay, EcvDate lastDay, String barStyleCss, DmElement rowElement) {
        this.toolTipText = toolTipText;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.barStyleForEditablePeriod = barStyleCss;
        this.currRowElement = rowElement;
        this.chartModel = null;
        this.parentRowElement = null;

    }

    /**
     * This constructor is called from CrpListTimeLineManager.
     *
     * @param toolTipText given string will be set has toolTip of bar.
     * @param firstDay given firstDay date is the source x start point of bar.
     * @param lastDay given lastDay date is the destination x end point of bar.
     * @param barStyleCss given style will be applied to bar in a chart.
     * @param rowElement given element will be updated to current row.
     * @param chartModel given model will be updated to current row model.
     * @param parentRowElement given parentRowElement will be updated to current
     * row parent row.
     */
    public TimeLineDataEditablePeriod(String toolTipText, EcvDate firstDay, EcvDate lastDay, String barStyleCss, DmElement rowElement, CrpGanttChartModel chartModel, DmElement parentRowElement, boolean isEditingRequired) {
        this.toolTipText = toolTipText;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.barStyleForEditablePeriod = barStyleCss;
        this.currRowElement = rowElement;
        this.chartModel = chartModel;
        this.parentRowElement = parentRowElement;
        this.isEditingRequired = isEditingRequired;
    }


    @Override
    public String getToolTipText() {
        return toolTipText;
    }

    @Override
    public EcvDate getFirstDay() {
        return (firstDay);
    }

    @Override
    public EcvDate getLastDay() {
        return (lastDay);
    }

    @Override
    public String getStyleClassForEditablePeriod() {
        return (barStyleForEditablePeriod);
    }

    @Override
    public DmElement getRowElement() {
        return currRowElement;
    }

    @Override
    public CrpGanttChartModel getChartModel() {
        return chartModel;
    }

    @Override
    public DmElement getParentRowElement() {
        return parentRowElement;
    }

    @Override
    public void setFirstDay(EcvDate firstDay) {
        this.firstDay = firstDay;
    }

    @Override
    public void setLastDay(EcvDate lastDay) {
        this.lastDay = lastDay;
    }

    @Override
    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }

    @Override
    public boolean isEditingRequired() {
        return isEditingRequired;
    }

    public void addToElement(TimeLineDataEditablePeriod otherPeriod) {
        assert (otherPeriod != null);
        toConnections.add(new TimeLineData_Connection(this, otherPeriod));
    }

    @Override
    public Collection<TimeLineData_ConnectionI> getToConnections() {
        return (toConnections);
    }

    @Override
    public String getAssignee() {
        return assigneeName;
    }

    @Override
    public void setAssignee(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    @Override
    public String getEstimatedEffort() {
        return estimatedEffort;
    }

    @Override
    public void setEstimatedEffort(String estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }

    @Override
    public String getRemEffort() {
        return remEffort;
    }

    @Override
    public void setRemEffort(String remEffort) {
        this.remEffort = remEffort;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public EcvDate getPlannedDate() {
        return plannedDate;
    }

    @Override
    public void setPlannedDate(EcvDate plannedDate) {
        this.plannedDate = plannedDate;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

}
