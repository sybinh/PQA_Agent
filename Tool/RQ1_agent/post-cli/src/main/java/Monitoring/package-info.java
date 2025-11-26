/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
/**
 * This package supports:<br>
 * a) The marking of objects with warnings, failures, hints, todos and infos. b)
 * The monitoring of rules on objects. Markers may be set on objects based on
 * such rules.
 * <p>
 * The functionality is used within EcvTool and Ipe to pass information from
 * lower architecture levels to the GUI.
 * <p>
 * Example:<br>
 * A tasks that is managed by a planning tool, might have a date till which it
 * has to be processed. If this date is passed and the tasks is still open, a
 * warning shall be given to the user of the planning tool, saying that the
 * planned date is passed.<br>
 * This can be done by a {@link RuleI} that is added to the tasks. The rule adds
 * a {@link Warning} on the object, if the planning date is passed and the task
 * is still open. This warning can be shown to the user via {@link MarkerDialog}
 * or written to a file via {@link MarkerExportFile}.
 *
 * <p>
 * The following class diagram shows the main classes of the package:
 * <p>
 * <img alt="ClassDiagramm" src="doc-files/ClassDiagramm.png">
 *
 */
package Monitoring;
