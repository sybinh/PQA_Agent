/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.TimeLineData;

import javafx.scene.paint.Color;
import javax.swing.Icon;
import util.EcvDate;

/**
 * Represents a milestone in the time line. A milestone has only one date.
 *
 * @author GUG2WI
 */
public interface TimeLineData_MilestoneI extends TimeLineData_SingleRowElementI {

    String getToolTipText();

    /**
     * Returns the date of the milestone
     *
     * @return Date of the milestone.
     */
    EcvDate getDate();

    Color getColor();
    
    Icon getIcon();
}
