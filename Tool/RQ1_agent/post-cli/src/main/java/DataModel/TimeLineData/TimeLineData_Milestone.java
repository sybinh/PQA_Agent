/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import javafx.scene.paint.Color;
import javax.swing.Icon;
import util.EcvDate;

public class TimeLineData_Milestone implements TimeLineData_MilestoneI {

    final private String toolTipText;
    final private EcvDate date;
    final private Color color;
    final private Icon icon;

    public TimeLineData_Milestone(String toolTipText, EcvDate date, Color color) {
        assert (toolTipText != null);
        assert (toolTipText.isEmpty() == false);
        assert (date != null);
        assert (date.isEmpty() == false);
        assert (color != null);

        this.toolTipText = toolTipText;
        this.date = date;
        this.color = color;
        this.icon = null;
    }

    public TimeLineData_Milestone(String toolTipText, EcvDate date, Icon icon) {
        assert (toolTipText != null);
        assert (toolTipText.isEmpty() == false);
        assert (date != null);
        assert (date.isEmpty() == false);
        assert (icon != null);

        this.toolTipText = toolTipText;
        this.date = date;
        this.icon = icon;
        this.color = null;
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
    public Icon getIcon() {
        return icon;
    }

    public String toString() {
        return (toolTipText);
    }
}
