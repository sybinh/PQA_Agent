/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import DataModel.DmElement;
import util.EcvDate;

/**
 *
 * @author HRN4KOR
 */
public class TimeLineDataParentPeriod implements TimeLineDataParentPeriodI {

    private String toolTipText;
    private EcvDate firstDay;
    private EcvDate lastDay;
    final private String barStyleForPeriod;
    final private DmElement dmElement;
    final private DmElement parentRowElement;

    /**
     *
     * @param toolTipText given string will be set has toolTip of bar.
     * @param firstDay given firstDay date is the source x start point of bar.
     * @param lastDay given lastDay date is the destination x end point of bar.
     * @param barStyleCss given style will be applied to bar in a chart.
     */
    public TimeLineDataParentPeriod(String toolTipText, EcvDate firstDay, EcvDate lastDay, String barStyleCss) {
        this.toolTipText = toolTipText;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.barStyleForPeriod = barStyleCss;
        this.dmElement = null;
        this.parentRowElement = null;
    }

    /**
     * This constructor is called from CrpListTimeLineManager. Using this
     * constructor will receive all the needed info,given info will be updated
     * to Parent Rectangle Bar.
     *
     * @param toolTipText given string will be set has toolTip of bar.
     * @param firstDay given firstDay date is the source x start point of bar.
     * @param lastDay given lastDay date is the destination x end point of bar.
     * @param barStyleCss given style will be applied to bar in a chart.
     * @param currRowElement given currRowElement will be set to the respected
     * Row.
     * @param parentRowElement given parentRowElement will be updated to
     * respected Parent Bar
     */
    public TimeLineDataParentPeriod(String toolTipText, EcvDate firstDay, EcvDate lastDay, String barStyleCss, DmElement currRowElement, DmElement parentRowElement) {
        this.toolTipText = toolTipText;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.barStyleForPeriod = barStyleCss;
        this.dmElement = currRowElement;
        this.parentRowElement = parentRowElement;
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
    public String getStyleClassForPeriod() {
        return (barStyleForPeriod);
    }

    @Override
    public DmElement getRowElement() {
        return (dmElement);
    }

    @Override
    public DmElement getMainParentRowElement() {
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
    public void setToolTipText(String tooltipText) {
        this.toolTipText = tooltipText;
    }
}
