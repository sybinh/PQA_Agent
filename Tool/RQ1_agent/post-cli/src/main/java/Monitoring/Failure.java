/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import ToolUsageLogger.ToolUsageLogger;

import java.util.logging.Level;

/**
 * Indicates a failure.
 *
 * @author gug2wi
 */
public class Failure extends Marker {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Failure.class.getCanonicalName());

    /**
     * Protected to enforce subclasses for failures.
     *
     * @param rule
     * @param title
     * @param description
     */
    protected Failure(RuleI rule, String title, String description) {
        super(rule, title, description);
    }

    /**
     * Protected to enforce subclasses for failures.
     *
     * @param rule
     * @param title
     */
    protected Failure(RuleI rule, String title) {
        super(rule, title);
    }

    @Override
    public String getType() {
        return ("Failure");
    }

    public void logError() {
        Exception e = new Exception(getTitle());
        logger.log(Level.SEVERE, "Code should be called in background thread! Check and move to background thread.", e);
        logger.severe(getDescription());
        ToolUsageLogger.logError(Failure.class.getCanonicalName(), e);
    }
}
