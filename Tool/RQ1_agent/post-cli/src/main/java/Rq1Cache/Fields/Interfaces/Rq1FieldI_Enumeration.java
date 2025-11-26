/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Fields.Interfaces;

import DataStore.DsFieldI_Enumeration;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Records.Rq1RecordInterface;
import util.EcvEnumeration;

/**
 *
 * @author gug2wi
 */
public interface Rq1FieldI_Enumeration extends DsFieldI_Enumeration<Rq1RecordInterface>, Rq1FieldI<EcvEnumeration> {

}
