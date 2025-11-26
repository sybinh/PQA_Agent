/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import java.util.Collection;
import util.EcvDate;

/**
 * The interface for an element in a row of a time line.
 *
 * @author GUG2WI
 */
public interface TimeLineData_SingleRowElementI extends TimeLineData_ElementI {

    /**
     * Get the first day in the time line on which the element is visible.
     *
     * This date is used to determine the scope of time that shall be visible in
     * the time line.
     *
     * @return The first day in the time line on which the element is visible.
     */
    @Override
    public EcvDate getFirstDay();

    /**
     * Get the last day in the time line on which the element is visible.
     *
     * This date is used to determine the scope of time that shall be visible in
     * the time line.
     *
     * @return The last day in the time line on which the element is visible.
     */
    @Override
    public EcvDate getLastDay();

    /**
     * Get all connections that point to an other element.
     *
     * @return
     */
    default Collection<TimeLineData_ConnectionI> getToConnections() {
        return (null);
    }

}
