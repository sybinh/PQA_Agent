/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

/**
 *
 * @author GUG2WI
 */
public interface TimeLineData_ConnectionI {

    public TimeLineData_SingleRowElementI getFromElement();
    public TimeLineData_SingleRowElementI getToElement();
}
