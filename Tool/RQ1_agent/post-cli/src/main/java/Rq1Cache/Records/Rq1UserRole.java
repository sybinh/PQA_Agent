/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Reference;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1LinkDescription;
import Rq1Cache.Rq1RecordType;

/**
 *
 * @author gug2wi
 */
public class Rq1UserRole extends Rq1Record implements Rq1LinkInterface {

    final public Rq1DatabaseField_Text ROLE_NAME;
    final public Rq1DatabaseField_Reference IS_PROJECT_MEMBER;
    final public Rq1DatabaseField_Reference LINKED_USER;

    final private Rq1LinkDescription nodeDescription;

    // isProjectMember
    // LinkedUser
    // record_type
    // RoleName
    //
    public Rq1UserRole(Rq1LinkDescription nodeDescription) {
        super(nodeDescription);

        this.nodeDescription = nodeDescription;

        addField(ROLE_NAME = new Rq1DatabaseField_Text(this, "RoleName"));
        addField(IS_PROJECT_MEMBER = new Rq1DatabaseField_Reference(this, "isProjectMember", Rq1RecordType.PROJECT));
        addField(LINKED_USER = new Rq1DatabaseField_Reference(this, "LinkedUser", Rq1RecordType.USER));

        ROLE_NAME.setReadOnly();
        IS_PROJECT_MEMBER.setReadOnly();
        LINKED_USER.setReadOnly();

    }

    @Override
    public void reload() {
        // No reload supported.
    }

}
