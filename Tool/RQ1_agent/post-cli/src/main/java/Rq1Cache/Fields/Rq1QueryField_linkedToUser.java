/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields;

import Rq1Cache.Records.Rq1User;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Reference;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class Rq1QueryField_linkedToUser extends Rq1QueryField {

    public enum LinkType {

        ASSIGNED("Assignee"),
        REQUESTED("Requester"),
        SUBMITTED("Submitter");

        final private String fieldName;

        LinkType(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return (fieldName);
        }

    }

    final private Rq1User parent;
    final private LinkType referenceType;
    private boolean critertiaSet;

    public Rq1QueryField_linkedToUser(Rq1User parent, String fieldName, Rq1RecordType referencedRecordType, LinkType referenceType) {
        super(parent, fieldName, referencedRecordType);
        assert (referenceType != null);

        this.parent = parent;
        this.referenceType = referenceType;
        this.critertiaSet = false;
    }

    @Override
    public List<Rq1Reference> getDataModelValue() {
        if (critertiaSet == false) {
            switch (referenceType) {
                case ASSIGNED:
                case REQUESTED:
                    addCriteria_Reference(referenceType.getFieldName(), parent);
                    break;
                case SUBMITTED:
                    StringBuilder submitterValue = new StringBuilder();
                    submitterValue.append(parent.FULLNAME.getDataModelValue()).append(" (").append(parent.LOGIN_NAME.getDataModelValue()).append(")");
                    addCriteria_Value(referenceType.getFieldName(), submitterValue.toString());
                    break;
                default:
                    throw (new Error("Unknown reference type: " + referenceType.name()));
            }
            critertiaSet = true;
        }
        return (super.getDataModelValue());
    }

}
