/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

/**
 *
 * @author gug2wi
 */
public class Warning extends Marker {

    public Warning(RuleI rule, String title, String description) {
        super(rule, title, description);
    }

    public Warning(RuleI rule, String title) {
        super(rule, title);
    }
    
        @Override
    public String getType() {
        return ("Warning");
    }
}
