/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Monitoring;

/**
 *
 * @author GUG2WI
 */
public class Rq1FieldContentException extends Exception {

    String s;

    public Rq1FieldContentException(String s) {
        super(s);
        this.s = s;
    }

    public Rq1FieldContentException(String s, Throwable cause) {
        super(s, cause);
        this.s = s;
    }
}
