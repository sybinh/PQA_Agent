/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1Bc;
import DataModel.Rq1.Records.DmRq1IssueSW;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Types.Rq1XmlTable_CustomerResponse.Derivatives;
import Rq1Cache.Types.Rq1XmlTable_CustomerResponse.Response;
import Rq1Cache.Types.Rq1XmlTable_CustomerResponse_Workitem;
import util.EcvTableData;

/**
 *
 * @author gug2wi
 */
public class DmRq1Field_CustomerResponse_Workitem extends DmRq1Field_Table<Rq1XmlTable_CustomerResponse_Workitem> {

    public DmRq1Field_CustomerResponse_Workitem(DmElementI parent, Rq1XmlSubField_Table<Rq1XmlTable_CustomerResponse_Workitem> rq1TableField, String nameForUserInterface) {
        super(parent, rq1TableField, nameForUserInterface);
    }

    /**
     * Adds the response to the field content.
     *
     * @param response
     * @param affectedIssue
     * @param derivatives
     * @param requestedAction
     * @return
     */
    public boolean addResponse(Response response, DmRq1IssueSW affectedIssue, Derivatives derivatives, String requestedAction) {
        assert (response != null);
        assert (affectedIssue != null);
        assert (derivatives != null);
        assert (requestedAction != null);

        EcvTableData tableData = this.getValue();
        if (getTableDescription().addResponse(tableData, response, affectedIssue, derivatives, requestedAction) == true) {
            setValue(tableData);
            return (true);
        } else {
            return (false);
        }
    }

    public boolean removeAction(Response response, DmRq1IssueSW affectedIssue, Derivatives derivatives, String requestedAction) {
        assert (response != null);
        assert (affectedIssue != null);
        assert (derivatives != null);
        assert (requestedAction != null);

        EcvTableData tableData = this.getValue();
        if (getTableDescription().removeAction(tableData, response, affectedIssue, derivatives, requestedAction) == true) {
            setValue(tableData);
            return (true);
        } else {
            return (false);
        }
    }

    public boolean addResponse(Response response, DmRq1Bc affectedBc, Derivatives derivatives, String requestedAction) {
        assert (response != null);
        assert (affectedBc != null);
        assert (derivatives != null);
        assert (requestedAction != null);

        EcvTableData tableData = this.getValue();
        if (getTableDescription().addResponse(tableData, response, affectedBc, derivatives, requestedAction) == true) {
            setValue(tableData);
            return (true);
        } else {
            return (false);
        }
    }

    public boolean removeAction(Response response, DmRq1Bc affectedBc, Derivatives derivatives, String requestedAction) {
        assert (response != null);
        assert (affectedBc != null);
        assert (derivatives != null);
        assert (requestedAction != null);

        EcvTableData tableData = this.getValue();
        if (getTableDescription().removeAction(tableData, response, affectedBc, derivatives, requestedAction) == true) {
            setValue(tableData);
            return (true);
        } else {
            return (false);
        }
    }

}
