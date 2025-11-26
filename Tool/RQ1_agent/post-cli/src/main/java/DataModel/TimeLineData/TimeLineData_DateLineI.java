/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import javafx.scene.paint.Color;
import util.EcvDate;

/**
 *
 * @author HRN4KOR
 */
public interface TimeLineData_DateLineI extends TimeLineData_SingleRowElementI {

    String getToolTipText();

    EcvDate getDate();

    Color getColor();

    void setDate(EcvDate newDate);

}
