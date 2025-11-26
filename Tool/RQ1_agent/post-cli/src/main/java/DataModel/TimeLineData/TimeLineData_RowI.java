/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import javafx.scene.input.MouseEvent;
import util.EcvDate;

/**
 * Provides the data for a row in the time line.
 *
 * This interface is used by the TimeLinePanel to get all the information about
 * a row shown in the time line.
 *
 * @author GUG2WI
 */
public interface TimeLineData_RowI {

    /**
     * Returns the title of the row.
     *
     * The title is shown in the left part of the TimeLinePanel.
     *
     * @return The object describing the title.
     */
    List<TimeLineData_RowHeaderElementI> getRowHeader();

    default void setRowHeader(List<TimeLineData_RowHeaderElementI> headerData) {

    }

    default String getStyleClassForTitle() {
        return null;
    }

    /**
     * Returns the elements in the row.
     *
     * This method shall return the elements immediately without blocking,
     * because it may be called in the UI task.
     *
     * @return The elements in the row.
     */
    Collection<TimeLineData_SingleRowElementI> getElements();

    /**
     * Indicates, if the row might have child rows.
     *
     * @return true, if the element mights have child rows; false if not.
     */
    boolean hasChildRows();

    /**
     * Returns the child rows of the row.
     *
     * This method shall return the rows immediately without blocking, because
     * it may be called in the UI task.
     *
     * If the rows have to be loaded before returning them, then null shall be
     * returned. The framework will then call loadChildRows() in a background
     * thread and repeat the call of getChildRows() later.
     *
     * @return The list of child rows or null if the child rows have to be
     * loaded first. If the row has no child rows, an empty list has to be
     * returned.
     */
    List<TimeLineData_RowI> getChildRows();

    /**
     * Called by the framework in a background thread to load the child rows
     * after getChildRows() returned null.
     *
     * The method shall return after the loading was done.
     */
    void loadChildRows();

    List<TimeLineData_SingleRowElementI> getChildRowElements();

    EcvDate getFirstDay();

    EcvDate getLastDay();
    
    default Collection<TimeLineDataCommandI> getCommands() {
        return null;
    }

    default void handleMouseClick(MouseEvent event) {

    }

    public interface RowSorterI extends Comparator<TimeLineData_RowI> {

        default RowSorterI getSorterForChildRows() {
            return (null);
        }

    }

    default public void sort(RowSorterI sorter) {

    }
    
}
