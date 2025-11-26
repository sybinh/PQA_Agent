/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.scene.paint.Color;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public class TimeLineData_Period implements TimeLineData_PeriodI {

    final private String toolTipText;
    final private EcvDate firstDay;
    final private EcvDate lastDay;
    final private Color color;
    private Color borderColor = null;

    final private List<TimeLineData_ConnectionI> toConnections = new ArrayList<>();

    public TimeLineData_Period(String toolTipText, EcvDate firstDay, EcvDate lastDay, Color color) {
        this.toolTipText = toolTipText;
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.color = color;
    }

    @Override
    public String getToolTipText() {
        return toolTipText;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public EcvDate getFirstDay() {
        return (firstDay);
    }

    @Override
    public EcvDate getLastDay() {
        return (lastDay);
    }

    public TimeLineData_Period setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return (this);
    }

    @Override
    public Color getBorderColor() {
        return borderColor;
    }

    public String toString() {
        return (toolTipText);
    }

    public void addToElement(TimeLineData_Period otherPeriod) {
        assert (otherPeriod != null);
        toConnections.add(new TimeLineData_Connection(this, otherPeriod));
    }

    @Override
    public Collection<TimeLineData_ConnectionI> getToConnections() {
        return (toConnections);
    }

}
