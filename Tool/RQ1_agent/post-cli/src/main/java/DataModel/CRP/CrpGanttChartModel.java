/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CRP;

import DataModel.DmElement;
import DataModel.Rq1.Records.DmRq1WorkItem;
import DataModel.TimeLineData.TimeLineDataEditable_PeriodI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import util.EcvDate;

/**
 *
 * @author HRN4KOR
 */
public class CrpGanttChartModel {

    private DmElement mainElement;
    private boolean mainElementVisibleStatus = false;
    private EcvDate mainElementFirstDate;
    private EcvDate mainElementLastDate;
    public EcvDate mainElementPlannedDate = EcvDate.getEmpty();
    private Map<DmElement, List<DmRq1WorkItem>> subParentElements = new LinkedHashMap<>();
    private Map<DmElement, List<EcvDate>> subElementFirstLastDates = new LinkedHashMap<>();
    private Map<DmElement, Boolean> subElementVisibilityStatus = new LinkedHashMap<>();
    private List<DmRq1WorkItem> childElements = new ArrayList<>();
    private Map<DmRq1WorkItem, TimeLineDataEditable_PeriodI> childWorkItemPeriodData = new LinkedHashMap<>();
    private boolean isWiUnderProject = false;

    public DmElement getMainElement() {
        return mainElement;
    }

    public Map<DmElement, List<DmRq1WorkItem>> getSubParentElements() {
        return subParentElements;
    }

    public List<DmRq1WorkItem> getChildElements() {
        return childElements;
    }

    public void setMainElement(DmElement mainElement) {
        this.mainElement = mainElement;
    }

    public void setChildElements(List<DmRq1WorkItem> childElements) {
        this.childElements = childElements;
    }

    public boolean isIsWiUnderProject() {
        return isWiUnderProject;
    }

    public void setIsWiUnderProject(boolean isWiUnderProject) {
        this.isWiUnderProject = isWiUnderProject;
    }

    public EcvDate getMainElementFirstDate() {
        return mainElementFirstDate;
    }

    public EcvDate getMainElementLastDate() {
        return mainElementLastDate;
    }

    public Map<DmElement, List<EcvDate>> getSubElementFirstLastDates() {
        return subElementFirstLastDates;
    }

    public void setMainElementFirstDate(EcvDate mainElementFirstDate) {
        this.mainElementFirstDate = mainElementFirstDate;
    }

    public void setMainElementLastDate(EcvDate mainElementLastDate) {
        this.mainElementLastDate = mainElementLastDate;
    }

    public EcvDate getMainElementPlannedDate() {
        return mainElementPlannedDate;
    }

    public void setMainElementPlannedDate(EcvDate mainElementPlannedDate) {
        this.mainElementPlannedDate = mainElementPlannedDate;
    }

    public Map<DmRq1WorkItem, TimeLineDataEditable_PeriodI> getChildWorkItemPeriodData() {
        return childWorkItemPeriodData;
    }

    public boolean getMainElementVisibleStatus() {
        return mainElementVisibleStatus;
    }

    public void setMainElementVisibleStatus(boolean mainElementVisibleStatus) {
        this.mainElementVisibleStatus = mainElementVisibleStatus;
    }

    public Map<DmElement, Boolean> getSubElementVisibilityStatus() {
        return subElementVisibilityStatus;
    }
}
