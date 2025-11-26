/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.Collection;
import util.EcvTableData;

/**
 * Adds list based access to the Rq1XmlTable.
 * @author gug2wi
 * @param <T_LIST> Type of the row elements when accessed as list.
 */
public abstract class Rq1XmlTableAsList<T_LIST> extends Rq1XmlTable {
    
    public abstract Collection<T_LIST> getList(EcvTableData data);
    
}
