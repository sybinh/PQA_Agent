/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public interface TaskDataI extends TimeLineData_SingleRowElementI {

    /**
     * The date when the working on the task starts.
     *
     * @return
     */
    EcvDate getStartDate();

    /**
     * The date when the working on the task ends.
     *
     * @return
     */
    EcvDate getEndDate();

    /**
     * Returns the date for which the result of the task is planned. Note that
     * this date might be different to the end date of the task.
     *
     * @return
     */
    EcvDate getPlannedDate();

}
