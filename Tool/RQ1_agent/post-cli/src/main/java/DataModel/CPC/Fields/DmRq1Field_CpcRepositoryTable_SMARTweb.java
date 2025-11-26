/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CPC.Fields;

import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Data.CPC.CpcXmlTable_SMARTweb;
import Rq1Data.CPC.CpcXmlTable_SMARTweb.CpcSmartWebData;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_SMARTweb extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_SMARTweb, CpcSmartWebData> {
    
    public DmRq1Field_CpcRepositoryTable_SMARTweb(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_SMARTweb> cpciGpmMcrField, String nameForUserInterface) {
        super(project, cpciGpmMcrField, nameForUserInterface);
    }
    
    @Override
    public List<CpcSmartWebData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcSmartWebData> repositoryDataList = CpcXmlTable_SMARTweb.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcSmartWebData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_SMARTweb.DESC.pack(dataList);
        setValue(tableData);
    }
}
