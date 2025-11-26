/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Implements the header and the footer for Log Files written by ECVTOOL.
 *
 * @author gug2wi
 */
public class EcvFormatter extends SimpleFormatter {

    @Override
    public String getHead(Handler hndlr) {
        if (EcvApplication.isApplicationDataSet() == true) {
            return (format(new LogRecord(Level.INFO,
                    "Log file started: "
                    + EcvApplication.getToolName()
                    + " "
                    + EcvApplication.getToolVersionForLogging()
                    + " (" + EcvApplication.getToolVersionForAccessControl() + ")"
                    + " Build: " + EcvApplication.getBuild())));
        } else {
            return (format(new LogRecord(Level.INFO,
                    "Log file started: No application data set. "
                    + " Build: " + EcvApplication.getBuild())));
        }
    }

    @Override
    public String getTail(Handler hndlr) {
        return (format(new LogRecord(Level.INFO, "Log file closed.")));
    }

}
