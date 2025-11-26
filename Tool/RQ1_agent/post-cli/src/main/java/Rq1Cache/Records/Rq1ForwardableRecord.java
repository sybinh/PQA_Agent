/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Reference;

/**
 * Defines the interface for records that can be forwarded to projects.
 * @author gug2wi
 */
public interface Rq1ForwardableRecord extends Rq1RecordInterface {

    void forward(Rq1Project project, Rq1RecordInterface assignee);

    Rq1DatabaseField_Reference getProjectField();
    
    Rq1DatabaseField_Reference getAssigneeField();
}
