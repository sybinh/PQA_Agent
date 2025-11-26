/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import javafx.scene.image.Image;

public class TimeLineData_Icon implements TimeLineData_IconI {

    private Image icon;
    private String toolTipText;

    public TimeLineData_Icon(Image icon) {
        assert (icon != null);
        this.icon = icon;
    }

    public TimeLineData_Icon(Image icon, String toolTipText) {
        this.icon = icon;
        this.toolTipText = toolTipText;
    }

    @Override
    public Image getImage() {
        return (icon);
    }

    @Override
    public String getToolTipText() {
        return this.toolTipText;
    }

}
