/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Fields;

import ConfigurableRules.ConfigRule.Records.ConfigurableRuleRecord;
import DataModel.DmElementI;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import java.util.List;
import java.util.Map;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author RHO2HC
 */
public class DmRq1Field_ConfigurableRulesTable extends DmRq1Field_Table<Rq1XmlTable_ConfigurableRules> {
    private EcvTableData displayedData;
    
    public DmRq1Field_ConfigurableRulesTable(DmElementI parent, Rq1XmlSubField_Table<Rq1XmlTable_ConfigurableRules> rq1TableField, String nameForUserInterface) {
        super(parent, rq1TableField, nameForUserInterface);
        
    }

    @Override
    public EcvTableData getValue() {
        if (displayedData == null) {
            displayedData = dsField.getDataModelValue();
        }
        return displayedData;
    }
    
    public void setDisplayedDatasByQuery(List<String> keys, Map<String, Map<String, ConfigurableRuleRecord>> recordsSearchByQuery) {
        displayedData = getTableDescription().createTableData();
        keys.stream().forEach(key -> {
            recordsSearchByQuery.get(key).values().stream().forEach(value -> {
                EcvTableRow newRow = getTableDescription().createAnEcvTableRowFromRecord(value);
                displayedData.addRow(newRow);
            });
        });
    }
}
