/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import Rq1Data.Types.Rq1DerivativeMapping;
import util.EcvTableColumn_ComboBox;
import util.EcvTableColumn_String;
import util.EcvTableDescription;

/**
 * A derivative table in which the derivatives can be entered as text by the user.
 * @author GUG2WI
 */
public class DmRq1Table_MappingToDerivatives_String extends EcvTableDescription {

    final public EcvTableColumn_ComboBox MAPPING;
    final public EcvTableColumn_String DERIVATIVE;

    public DmRq1Table_MappingToDerivatives_String() {

        String[] mappingModes = new String[Rq1DerivativeMapping.Mode.values().length];
        int i = 0;
        for (Rq1DerivativeMapping.Mode m : Rq1DerivativeMapping.Mode.values()) {
            mappingModes[i++] = m.getModeString();
        }
        addIpeColumn(MAPPING = new EcvTableColumn_ComboBox("Mapping", mappingModes));
        addIpeColumn(DERIVATIVE = new EcvTableColumn_String("Derivative"));
    }
}
