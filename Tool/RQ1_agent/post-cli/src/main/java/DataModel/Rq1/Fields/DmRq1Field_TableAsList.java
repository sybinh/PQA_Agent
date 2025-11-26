/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Types.Rq1XmlTableAsList;
import java.util.Collection;

/**
 *
 * @author gug2wi
 * @param <T>
 * @param <T_LIST> Type of the element on the list interface.
 */
public class DmRq1Field_TableAsList<T extends Rq1XmlTableAsList<T_LIST>, T_LIST> extends DmRq1Field_Table<T> {

    public DmRq1Field_TableAsList(Rq1XmlSubField_Table<T> rq1TableField, String nameForUserInterface) {
        super(rq1TableField, nameForUserInterface);
    }

    /**
     * Returns the content of the table as a list.
     *
     * @return
     */
    public Collection<T_LIST> getList() {
        return (super.getTableDescription().getList(super.getValue()));
    }

}
