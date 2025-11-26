/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Exceptions;

/**
 *
 * @author gug2wi
 */
public class WorkaroundForServerErrorException extends TemporaryServerError {

    final private String s;

    public WorkaroundForServerErrorException(String s, Throwable cause) {
        super(s, cause);
        this.s = s;
    }

    @Override
    public String getMessageForUi() {
        StringBuilder b = new StringBuilder(100);
        b.append("Workaround for Server Error: ");
        b.append(s);
        return (b.toString());
    }
}
