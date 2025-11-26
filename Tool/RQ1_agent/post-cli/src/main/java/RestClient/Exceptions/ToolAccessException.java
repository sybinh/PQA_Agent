/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Exceptions;

/**
 * Used to indicate that the tool name and/or tool version is not in the white
 * list of RQ1.
 *
 * @author gug2wi
 */
public class ToolAccessException extends TemporaryServerError {

    public ToolAccessException() {
        super("The tool or the version of the tool is not supported.\n"
                + "\n"
                + "Please update to new tool version.");
    }
}
