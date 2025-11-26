/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import javafx.scene.paint.Color;
import util.EcvDate;

/**
 * A vertical line on a day.
 *
 * @author GUG2WI
 */
public class TimeLineData_DayLine implements TimeLineData_SingleDayElementI {

    final private String title;
    final private String toolTipText;
    final private EcvDate date;
    final private Color color;

    public TimeLineData_DayLine(String title, EcvDate date, String toolTipText, Color color) {
        assert (title != null);
        assert (title.isEmpty() == false);
        assert (toolTipText != null);
        assert (toolTipText.isEmpty() == false);
        assert (date != null);
        assert (date.isEmpty() == false);
        assert (color != null);

        this.title = title;
        this.toolTipText = toolTipText;
        this.date = date;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getToolTipText() {
        return (toolTipText);
    }

    @Override
    public EcvDate getDate() {
        return (date);
    }

    public Color getColor() {
        return (color);
    }
}
