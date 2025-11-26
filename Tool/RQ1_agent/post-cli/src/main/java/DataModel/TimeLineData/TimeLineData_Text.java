/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

/**
 * Describes a text that shall be shown in the time line.
 *
 * @author GUG2WI
 */
public class TimeLineData_Text implements TimeLineData_TextI {

    final private String textToShow;
    final private String styleClass;
    private String style = null;

    public TimeLineData_Text(String textToShow) {
        assert (textToShow != null);
        this.textToShow = textToShow;
        this.styleClass = null;
    }

    public TimeLineData_Text(String textToShow, String styleClass) {
        assert (textToShow != null);
        this.textToShow = textToShow;
        this.styleClass = styleClass;
    }

    public TimeLineData_Text setStyle(String style) {
        this.style = style;
        return (this);
    }

    @Override
    public String getTextToShow() {
        return textToShow;
    }

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public String getStyleClass() {
        return styleClass;
    }

}
