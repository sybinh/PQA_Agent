/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

/**
 *
 * @author GUG2WI
 */
public class WriteToDatabaseFailure extends Failure {
    
    public WriteToDatabaseFailure(RuleI rule, String title, String description) {
        super(rule, title, description);
    }
    
}
