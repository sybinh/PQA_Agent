/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.Category_WorkItem;
import Rq1Data.Enumerations.Domain_WorkItem;
import Rq1Data.Enumerations.SubCategory_WorkItem;
import Rq1Cache.Types.Rq1XmlTable_CustomerResponse_Workitem;

/**
 *
 * @author gug2wi
 */
public class Rq1WorkItem_Pst extends Rq1WorkItem {

    final private Rq1XmlSubField_Xml CUSTOMER_RESPONSE_XML;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CustomerResponse_Workitem> CUSTOMER_RESPONSE;

    public Rq1WorkItem_Pst(Rq1NodeDescription node, SubCategory_WorkItem subCategory) {
        super(node, Domain_WorkItem.SOFTWARE, Category_WorkItem.RELEASE, subCategory);

        addField(CUSTOMER_RESPONSE_XML = new Rq1XmlSubField_Xml(this, TAGS, "CustomerResponse"));
        CUSTOMER_RESPONSE_XML.setOptional();
        addField(CUSTOMER_RESPONSE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CustomerResponse_Workitem(), CUSTOMER_RESPONSE_XML, "Response"));
        CUSTOMER_RESPONSE.setOptional();
    }

}
