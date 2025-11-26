/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

/**
 *
 * @author gug2wi
 */
public class EcvApplicationException extends RuntimeException {

    final private Throwable exception;

    public EcvApplicationException(Throwable exception) {
        super(exception);
        assert (exception != null);
        this.exception = exception;
    }

    public Throwable getWrappedException() {
        return (exception);
    }

    @Override
    public String getMessage() {
        return (exception.getMessage());
    }

}
