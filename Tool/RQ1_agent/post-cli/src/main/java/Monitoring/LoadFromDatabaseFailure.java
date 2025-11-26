/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

/**
 * The read of the object marked with this failure
 *
 * @author GUG2WI
 */
public class LoadFromDatabaseFailure extends Failure {

    public LoadFromDatabaseFailure(RuleI rule, String title) {
        super(rule, title);
    }

    public LoadFromDatabaseFailure(RuleI rule, String title, String description) {
        super(rule, title, description);
    }

}
