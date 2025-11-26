/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import util.EcvDate;

/**
 * The interface for an element in a day of a time line.
 * 
 * The element spans all rows but only one day of the time line.
 * 
 * @author GUG2WI
 */
public interface TimeLineData_SingleDayElementI extends TimeLineData_ElementI {

    public EcvDate getDate();

    @Override
    public default EcvDate getFirstDay() {
        return (getDate());
    }

    @Override
    public default EcvDate getLastDay() {
        return (getDate());
    }

}
