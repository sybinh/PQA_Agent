/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient;

import util.EcvJsonTopLevelValue;

/**
 *
 * @author GUG2WI
 */
public class RestResponseJson implements RestResponseI {

    final private EcvJsonTopLevelValue topLevelValue;

    public RestResponseJson(EcvJsonTopLevelValue jsonResponse) {
        assert (jsonResponse != null);

        this.topLevelValue = jsonResponse;
    }

    public EcvJsonTopLevelValue getTopLevelValue() {
        return (topLevelValue);
    }

}
