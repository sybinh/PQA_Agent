package util.error;

/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
/**
 *
 * @author miw83wi
 */
public class EcvMailException extends Exception {

    public EcvMailException() {
    }

    public EcvMailException(String msg) {
        super(msg);
    }
}
