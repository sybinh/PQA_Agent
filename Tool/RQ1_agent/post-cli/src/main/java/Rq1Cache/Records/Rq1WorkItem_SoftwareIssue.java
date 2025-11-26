/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Rq1NodeDescription;
import Rq1Data.Enumerations.Category_WorkItem;
import Rq1Data.Enumerations.Domain_WorkItem;
import Rq1Data.Enumerations.SubCategory_WorkItem;

/**
 *
 * @author gug2wi
 */
public class Rq1WorkItem_SoftwareIssue extends Rq1WorkItem {

    final public Rq1XmlSubField_Text COMPL_LINK;
    final public Rq1XmlSubField_Text COMPL_RESULT;
    final public Rq1XmlSubField_Text COMPL_MEASURES_ID;

    public Rq1WorkItem_SoftwareIssue(Rq1NodeDescription description, SubCategory_WorkItem subCategory) {
        super(description, Domain_WorkItem.SOFTWARE, Category_WorkItem.ISSUE, subCategory);

        addField(COMPL_LINK = new Rq1XmlSubField_Text(this, TAGS, "Compl_Link") {
            @Override
            protected String encodeValueForDb(String dataSourceValue) {
                if ((dataSourceValue != null) && (dataSourceValue.isEmpty() == false)) {
                    if (dataSourceValue.endsWith(" ") == false) {
                        return (dataSourceValue + " ");
                    }
                }
                return (dataSourceValue);
            }
        });
        addField(COMPL_RESULT = new Rq1XmlSubField_Text(this, TAGS, "Compl_Result"));
        addField(COMPL_MEASURES_ID = new Rq1XmlSubField_Text(this, TAGS, "Compl_MeasuresID"));
    }

}
