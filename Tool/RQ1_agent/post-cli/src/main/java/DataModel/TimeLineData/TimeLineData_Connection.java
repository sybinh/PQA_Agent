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
public class TimeLineData_Connection implements TimeLineData_ConnectionI {

    final private TimeLineData_SingleRowElementI fromElement;
    final private TimeLineData_SingleRowElementI toElement;

    public TimeLineData_Connection(TimeLineData_SingleRowElementI fromElement, TimeLineData_SingleRowElementI toElement) {
        assert (fromElement != null);
        assert (toElement != null);

        this.fromElement = fromElement;
        this.toElement = toElement;
    }

    @Override
    public TimeLineData_SingleRowElementI getFromElement() {
        return (fromElement);
    }

    @Override
    public TimeLineData_SingleRowElementI getToElement() {
        return (toElement);
    }

}
