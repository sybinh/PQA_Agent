/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package ConfigurableRules.ConfigRule.Fields;

import DataStore.DsField_XmlSourceI;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Records.Rq1RecordInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.EcvTableData;
import util.EcvXmlElement;

/**
 *
 * @author RHO2HC
 */
public class Rq1XmSubField_ConfigurableRulesTable extends Rq1XmlSubField_Table<Rq1XmlTable_ConfigurableRules> {
    
    final private Rq1XmlTable_ConfigurableRules table;
    
    public Rq1XmSubField_ConfigurableRulesTable(Rq1RecordInterface parent, Rq1XmlTable_ConfigurableRules table, DsField_XmlSourceI source, String elementName) {
        super(parent, table, source, elementName);
        assert (table != null);
        this.table = table;
    }

    @Override
    public List<EcvXmlElement> provideValueAsXmlListForDb() {
        EcvTableData dbData = (EcvTableData) getDataSourceValue();
        if (dbData.getRows().size() > 0) {
            return Arrays.asList(table.provideRowAsXmlElementForDb(getElementName(), dbData.getRows().get(0)));
        }
        return new ArrayList<>();
    }
}
