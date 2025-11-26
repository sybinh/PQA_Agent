/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Exceptions;

/**
 * Indicates that the response received for the content of a file is to big.
 *
 * @author gug2wi
 */
public class FileSizeException extends RestException {

    public FileSizeException() {
        super("File to big.");
    }

}
