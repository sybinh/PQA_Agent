/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CPC.Fields;

import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Data.CPC.CpcXmlTable_RQ1;
import Rq1Data.CPC.CpcXmlTable_RQ1.CpcRq1Data;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_RQ1 extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_RQ1, CpcRq1Data> {
    
    public DmRq1Field_CpcRepositoryTable_RQ1(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_RQ1> cpciGpmMcrField, String nameForUserInterface, String descriptionForUI) {
        super(project, cpciGpmMcrField, nameForUserInterface, descriptionForUI);
    }
    
    @Override
    public List<CpcRq1Data> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcRq1Data> repositoryDataList = CpcXmlTable_RQ1.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcRq1Data> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_RQ1.DESC.pack(dataList);
        setValue(tableData);
    }
}
