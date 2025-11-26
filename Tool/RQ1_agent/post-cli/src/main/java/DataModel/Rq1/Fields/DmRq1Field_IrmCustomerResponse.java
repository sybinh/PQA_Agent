/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Types.Rq1XmlTable_CustomerResponse_IRM;
import java.util.Collection;
import java.util.Set;
import util.EcvTableData;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_IrmCustomerResponse extends DmRq1Field_Table<Rq1XmlTable_CustomerResponse_IRM> {

    public DmRq1Field_IrmCustomerResponse(DmElementI parent, Rq1XmlSubField_Table<Rq1XmlTable_CustomerResponse_IRM> rq1TableField, String nameForUserInterface) {
        super(parent, rq1TableField, nameForUserInterface);
    }

    public boolean containsRejected() {
        return (getTableDescription().containsRejected(getValue()));
    }

    public Set<String> getAllCustomerState() {
        return (getTableDescription().getAllCustomerState(getValue()));
    }

    public boolean removeAllBcAbgelehnt(Collection<String> derivatives) {
        assert (derivatives != null);

        EcvTableData data = getValue();

        if (getTableDescription().removeAllBcAbgelehnt(data, derivatives) == true) {
            setValue(data);
            return (true);
        } else {
            return (false);
        }
    }

}
