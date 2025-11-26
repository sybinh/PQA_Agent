/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Flow;

import util.EcvEnumeration;

/**
 *
 * @author mos8cob
 */
public enum ProjectorMode implements EcvEnumeration {

    DEFAULT_MODE("Default Mode"),
    LIGHT_MODE("Light Mode"),
    DARK_MODE("Dark Mode");
    //
    private final String dbText;

    /**
     * It is used to give Enumeration for Projector Mode by specifying three
     * modes. ie, Default mode, Light mode and Dark mode. They will help in
     * setting different colors to background for better view on projector.
     *
     * @param dbText shows the condition of projector mode.
     */
    private ProjectorMode(String dbText) {
        assert (dbText != null);
        this.dbText = dbText;
    }

    @Override
    public String getText() {
        return dbText;
    }

}
