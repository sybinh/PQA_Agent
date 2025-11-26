/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import Rq1Data.Enumerations.IntegrationStep;
import java.util.ArrayList;
import util.EcvTableColumn_ComboBox;
import util.EcvTableColumn_Date;
import util.EcvTableColumn_String;
import util.EcvTableDescription;

/**
 *
 * @author GUG2WI
 */
public class DmRq1Table_IntegrationSteps extends EcvTableDescription {

    final public EcvTableColumn_ComboBox STEP;
    final public EcvTableColumn_Date DATE;
    final public EcvTableColumn_String NAME;
    final public EcvTableColumn_String COMMENT;

    public DmRq1Table_IntegrationSteps() {
        ArrayList<String> l = new ArrayList<>();
        for (IntegrationStep step : IntegrationStep.values()) {
            if (step.getShortName() != null) {
                l.add(step.getShortName());
            }
        }

        addIpeColumn(STEP = new EcvTableColumn_ComboBox("Step", l.toArray(new String[1])));
        addIpeColumn(NAME = new EcvTableColumn_String("Name"));
        addIpeColumn(DATE = new EcvTableColumn_Date("Date"));
        addIpeColumn(COMMENT = new EcvTableColumn_String("Comment"));
    }
}
