/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields.Interfaces;

import util.EcvEnumeration;
import Rq1Cache.Records.Rq1RecordInterface;
import OslcAccess.OslcSelection;
import java.util.EnumSet;
import util.EcvDate;

/**
 *
 * @author gug2wi
 */
public interface Rq1QueryI extends Rq1ReferenceList_ReadOnlyI {

    void addCriteria_Reference(String fieldname, Rq1RecordInterface referencedRecord);

    void addCriteria_ValueList(String fieldname, String[] allowedValues);

    void addCriteria_ValueList(String fieldname, String allowedValue1, String allowedValue2);

    void addCriteria_ValueList(String fieldname, EnumSet<? extends EcvEnumeration> allowedValues);

    void addCriteria_Value(String fieldname, String wantedValue);

    void addCriteria_isLaterOrEqualThen(String fieldname, EcvDate testDate);

    OslcSelection getCriterias();
}
