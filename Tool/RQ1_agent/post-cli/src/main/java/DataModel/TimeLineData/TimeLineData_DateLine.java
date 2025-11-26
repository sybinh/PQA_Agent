/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import javafx.scene.paint.Color;
import util.EcvDate;

/**
 *
 * @author HRN4KOR
 */
public class TimeLineData_DateLine implements TimeLineData_DateLineI {

    final private String toolTipText;
    private EcvDate date;
    final private Color color;

    /**
     * @param toolTipText The given toolTipText will shown in toolTip.
     * @param date based on given date the palnnedDate Line will be placed.
     * @param color The given color will be set to PlannedDate Line.
     */
    public TimeLineData_DateLine(String toolTipText, EcvDate date, Color color) {
        assert (toolTipText != null);
        assert (toolTipText.isEmpty() == false);
        assert (date != null);
        assert (date.isEmpty() == false);
        assert (color != null);

        this.toolTipText = toolTipText;
        this.date = date;
        this.color = color;
    }

    @Override
    public String getToolTipText() {
        return (toolTipText);
    }

    @Override
    public EcvDate getDate() {
        return (date);
    }

    @Override
    public EcvDate getFirstDay() {
        return (getDate());
    }

    @Override
    public EcvDate getLastDay() {
        return (getDate());
    }

    @Override
    public Color getColor() {
        return (color);
    }

    @Override
    public void setDate(EcvDate newDate) {
        date = newDate;
    }

}
