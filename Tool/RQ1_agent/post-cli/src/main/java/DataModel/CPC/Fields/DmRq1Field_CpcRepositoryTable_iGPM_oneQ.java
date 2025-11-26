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
import Rq1Data.CPC.CpcXmlTable_iGPM_oneQ;
import Rq1Data.CPC.CpcXmlTable_iGPM_oneQ.CpcOneQData;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_iGPM_oneQ extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_iGPM_oneQ, CpcOneQData> {
    
    public DmRq1Field_CpcRepositoryTable_iGPM_oneQ(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_iGPM_oneQ> cpcField, String nameForUserInterface, String descriptionForUI) {
        super(project, cpcField, nameForUserInterface, descriptionForUI);
    }
    
    @Override
    public List<CpcOneQData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcOneQData> repositoryDataList = CpcXmlTable_iGPM_oneQ.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcOneQData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_iGPM_oneQ.DESC.pack(dataList);
        setValue(tableData);
    }
}
