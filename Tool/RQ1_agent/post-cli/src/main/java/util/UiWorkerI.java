/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 *
 * @author GUG2WI
 */
public interface UiWorkerI {

    final static public String LOADING = "Loading";
    final static public String SAVE = "Save";
    final static public String CREATE = "Create";
    final static public String CHECK_RULES = "Check rules";
    final static public String BULK_OPERATION = "Bulk Operation";
    final static public String EXPORT_WARNING = "Export Warning Operation";
    final static public String OPEN_IN_RQ1 = "Open in RQ1";
    final static public String OPEN_IN_DOORS = "Open in DOORS";
    final static public String OPEN_IN_ALM = "Open in ALM";
    final static public String OPEN_IN_CI_DASHBOARD = "Open in CI-Dashboard";
    final static public String OPEN_IN_R2T2_TESTPLAN = "Open in R2T2-Testplan";
    final static public String OPEN_IN_PS_EC_INFOTOOL = "Open in PS-EC Infotool";
    final static public String DOWNLOAD_FILE = "Download File";
    final static public String EXPORTING_MILESTONES = "Exporting to MS Project";

    /**
     * Returns the name/description of the currently done action/work of the
     * worker.
     *
     * @return Current action of the worker.
     */
    public String getAction();

    /**
     * Sets the name/description of the currently done action/work of the
     * worker.
     *
     * @param newAction Current action of the worker.
     */
    public void setAction(String newAction);

}
