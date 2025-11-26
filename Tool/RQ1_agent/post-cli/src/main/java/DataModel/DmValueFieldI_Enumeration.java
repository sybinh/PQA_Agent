/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import util.EcvEnumeration;
import util.EcvEnumerationFieldI;

/**
 *
 * @author gug2wi
 */
public interface DmValueFieldI_Enumeration extends DmValueFieldI<EcvEnumeration>, EcvEnumerationFieldI {

    default boolean isValueEqualTo(EcvEnumeration value1) {
        assert (value1 != null);

        EcvEnumeration value = getValue();
        return (value1.equals(value));
    }

    default boolean isValueEqualTo(EcvEnumeration value1, EcvEnumeration value2, EcvEnumeration value3, EcvEnumeration value4) {
        assert (value1 != null);
        assert (value2 != null);
        assert (value3 != null);
        assert (value4 != null);

        EcvEnumeration value = getValue();
        return (value1.equals(value)
                || value2.equals(value)
                || value3.equals(value)
                || value3.equals(value));
    }

    @Override
    default public String getValueAsText() {
        if (getValue() != null) {
            return (getValue().getText());
        } else {
            return ("");
        }
    }

}
