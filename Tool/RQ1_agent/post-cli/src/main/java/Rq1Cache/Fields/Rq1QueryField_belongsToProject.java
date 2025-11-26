/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Rq1Cache.Records.Rq1Project;
import Rq1Cache.Rq1RecordType;

/**
 *
 * @author gug2wi
 */
public class Rq1QueryField_belongsToProject extends Rq1QueryField {

    final protected Rq1Project project;

    public Rq1QueryField_belongsToProject(Rq1Project parent, String fieldName, Rq1RecordType referencedRecordType) {
        super(parent, fieldName, referencedRecordType);

        this.project = parent;
        super.addCriteria_Reference("belongsToProject", parent);
    }

}
