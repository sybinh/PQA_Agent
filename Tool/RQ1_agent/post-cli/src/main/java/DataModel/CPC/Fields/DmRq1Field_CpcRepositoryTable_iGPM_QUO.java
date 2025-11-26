/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CPC.Fields;

import Rq1Data.CPC.CpcXmlTable_iGPM_QUO.CpcIGpmQuoData;
import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Data.CPC.CpcXmlTable_iGPM_QUO;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_iGPM_QUO extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_iGPM_QUO, CpcIGpmQuoData> {
    
    public DmRq1Field_CpcRepositoryTable_iGPM_QUO(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_iGPM_QUO> cpcField, String nameForUserInterface) {
        super(project, cpcField, nameForUserInterface);
    }
    
    @Override
    public List<CpcIGpmQuoData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcIGpmQuoData> repositoryDataList = CpcXmlTable_iGPM_QUO.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcIGpmQuoData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_iGPM_QUO.DESC.pack(dataList);
        setValue(tableData);
    }
}
