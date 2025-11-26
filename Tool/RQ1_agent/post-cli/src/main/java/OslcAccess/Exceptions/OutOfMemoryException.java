/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Exceptions;

/**
 *
 * @author gug2wi
 */
public class OutOfMemoryException extends TemporaryServerError {

    final private String additionalInfo;

    public OutOfMemoryException(String additionalInfo) {
        super("Get method for urlString=\"" + additionalInfo + "\".");
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String getMessageForUi() {
        StringBuilder b = new StringBuilder(100);
        b.append("Out of Memory on server.\n"
                + "\n"
                + "Additional Info: ");
        b.append(additionalInfo);
        return (b.toString());
    }
}
