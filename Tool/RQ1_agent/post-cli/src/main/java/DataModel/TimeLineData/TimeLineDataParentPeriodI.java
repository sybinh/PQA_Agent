/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import DataModel.DmElement;
import util.EcvDate;

/**
 *
 * @author HRN4KOR
 */
public interface TimeLineDataParentPeriodI extends TimeLineData_SingleRowElementI {

    String getToolTipText();

    default String getStyleClassForPeriod() {
        return null;
    }

    DmElement getRowElement();

    DmElement getMainParentRowElement();

    void setFirstDay(EcvDate firstDay);

    void setLastDay(EcvDate lastDay);
    
    void setToolTipText(String tooltipText);

}
