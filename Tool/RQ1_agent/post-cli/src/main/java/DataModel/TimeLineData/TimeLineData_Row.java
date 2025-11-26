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
import java.util.Collections;
import java.util.List;
import javafx.scene.image.Image;
import util.EcvDate;

/**
 * A container holding the data defined in TimeLineData_RowI.
 *
 * This class is a container for the data about a row in the time line. It
 * supports the setting and getting of the data.
 *
 * @author GUG2WI
 */
public class TimeLineData_Row implements TimeLineData_RowI {

    private List<TimeLineData_RowHeaderElementI> header = new ArrayList<>(2);
    private final String title;
    private final String styleClassForTitle;
    final private List<TimeLineData_SingleRowElementI> elements = new ArrayList<>();
    final private List<TimeLineData_SingleRowElementI> childRowElements = new ArrayList<>();
    private boolean hasChildRows = true;
    private List<TimeLineData_RowI> childRows = null;

    public TimeLineData_Row() {
        this.title = "";
        this.styleClassForTitle = null;
        header.add(new TimeLineData_Text("", styleClassForTitle));
    }

    public TimeLineData_Row(String title) {
        assert (title != null);
        assert (title.isEmpty() == false);

        this.title = title;
        this.styleClassForTitle = null;
        header.add(new TimeLineData_Text(title, styleClassForTitle));
    }

    public TimeLineData_Row(String title, String styleClassForTitle) {
        assert (title != null);
        assert (title.isEmpty() == false);
        assert (styleClassForTitle != null);
        assert (styleClassForTitle.isEmpty() == false);

        this.title = title;
        this.styleClassForTitle = styleClassForTitle;
        header.add(new TimeLineData_Text(title, styleClassForTitle));
    }

    public TimeLineData_Row(Image icon, String title) {
        assert (icon != null);
        assert (title != null);
        assert (title.isEmpty() == false);

        this.title = title;
        this.styleClassForTitle = null;
        header.add(new TimeLineData_Icon(icon));
        header.add(new TimeLineData_Text(title, styleClassForTitle));
    }

    public TimeLineData_Row(Image icon, String title, String styleClassForTitle) {
        assert (icon != null);
        assert (title != null);
        assert (title.isEmpty() == false);
        assert (styleClassForTitle != null);
        assert (styleClassForTitle.isEmpty() == false);

        this.title = title;
        this.styleClassForTitle = styleClassForTitle;
        header.add(new TimeLineData_Icon(icon));
        header.add(new TimeLineData_Text(title, styleClassForTitle));
    }

    public TimeLineData_Row(String space, Image icon, String title) {
        assert (icon != null);
        assert (title != null);
        assert (title.isEmpty() == false);

        this.title = title;
        this.styleClassForTitle = null;
        header.add(new TimeLineData_Text(space));
        header.add(new TimeLineData_Icon(icon));
        header.add(new TimeLineData_Text(title, styleClassForTitle));
    }

    public TimeLineData_Row(String space, Image icon, String title, String styleClassForTitle) {
        assert (icon != null);
        assert (title != null);
        assert (title.isEmpty() == false);
        assert (styleClassForTitle != null);
        assert (styleClassForTitle.isEmpty() == false);

        this.title = title;
        this.styleClassForTitle = styleClassForTitle;
        header.add(new TimeLineData_Text(space));
        header.add(new TimeLineData_Icon(icon));
        header.add(new TimeLineData_Text(title, styleClassForTitle));
    }

    public TimeLineData_Row(Image icon1, String spaceBetweenIcons, Image icon2, String title, String styleClassForTitle, String icon1ToolTipText) {
        assert (icon1 != null);
        assert (icon2 != null);
        assert (title != null);
        assert (title.isEmpty() == false);
        assert (styleClassForTitle != null);
        assert (styleClassForTitle.isEmpty() == false);

        this.title = title;
        this.styleClassForTitle = styleClassForTitle;
        header.add(new TimeLineData_Icon(icon1, icon1ToolTipText));
        header.add(new TimeLineData_Text(spaceBetweenIcons));
        header.add(new TimeLineData_Icon(icon2));
        header.add(new TimeLineData_Text(title, styleClassForTitle));
    }

    @Override
    public List<TimeLineData_RowHeaderElementI> getRowHeader() {
        return (Collections.unmodifiableList(header));
    }

    @Override
    public void setRowHeader(List<TimeLineData_RowHeaderElementI> headerData) {
        assert (headerData != null);
        this.header = headerData;
    }

    @Override
    public String getStyleClassForTitle() {
        return (styleClassForTitle);
    }

    @Override
    public EcvDate getFirstDay() {
        EcvDate firstDay = EcvDate.getEmpty();
        for (TimeLineData_SingleRowElementI element : elements) {
            firstDay = firstDay.getEarliest(element.getFirstDay());
        }
        return (firstDay);
    }

    @Override
    public EcvDate getLastDay() {
        EcvDate lastDay = EcvDate.getEmpty();
        for (TimeLineData_SingleRowElementI element : elements) {
            lastDay = lastDay.getLatest(element.getLastDay());
        }
        return (lastDay);
    }

    @Override
    public Collection<TimeLineDataCommandI> getCommands() {
        return TimeLineData_RowI.super.getCommands();
    }

    //--------------------------------------------------------------------------
    //
    // Manage row elements
    //
    //--------------------------------------------------------------------------
    final public void addElement(TimeLineData_SingleRowElementI elementData) {
        assert (elementData != null);
        elements.add(elementData);
    }

    final public void addElementNotNull(TimeLineData_SingleRowElementI elementData) {
        if (elementData != null) {
            addElement(elementData);
        }
    }

    @Override
    public Collection<TimeLineData_SingleRowElementI> getElements() {
        return (elements);
    }

    //--------------------------------------------------------------------------
    //
    // Manage childs rows
    //
    //--------------------------------------------------------------------------
    public void setHasChildRows(boolean hasChildRows) {
        this.hasChildRows = hasChildRows;
    }

    @Override
    public boolean hasChildRows() {
        return (hasChildRows);
    }

    public void addChildRow(TimeLineData_RowI row) {
        assert (row != null);
        if (childRows == null) {
            childRows = new ArrayList<>();
        }
        childRows.add(row);
    }

    @Override
    public List<TimeLineData_RowI> getChildRows() {
        return (childRows);
    }

    @Override
    public void loadChildRows() {
        childRows = new ArrayList<>();
    }

    @Override
    public void sort(RowSorterI sorter) {
        if (sorter != null && getChildRows() != null) {
            Collections.sort(getChildRows(), sorter);
            for (TimeLineData_RowI row : childRows) {
                row.sort(sorter);
            }
        }
    }
    //--------------------------------------------------------------------------
    //
    // Others
    //
    //--------------------------------------------------------------------------
    public void addChildElement(TimeLineData_SingleRowElementI elementData) {
        assert (elementData != null);
        childRowElements.add(elementData);
    }

    public void addChildElementNotNull(TimeLineData_SingleRowElementI elementData) {
        if (elementData != null) {
            addChildElement(elementData);
        }
    }

    @Override
    public List<TimeLineData_SingleRowElementI> getChildRowElements() {
        return childRowElements;
    }

    @Override
    public String toString() {
        return (title);
    }

}
