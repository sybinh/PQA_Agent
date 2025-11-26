/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CPC.Fields;

import Rq1Data.CPC.CpcXmlTable_iGPM_MCR_ECU.CpcIGpmMcrData;
import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Data.CPC.CpcXmlTable_iGPM_MCR_ECU;
import Rq1Data.CPC.CpcXmlTable_iGPM_MCR_System_Parent_Project;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author fuj1wi
 */
public class DmRq1Field_CpcRepositoryTable_iGPM_MCR_ECU extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_iGPM_MCR_ECU, CpcIGpmMcrData> {
    
    public DmRq1Field_CpcRepositoryTable_iGPM_MCR_ECU(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_iGPM_MCR_ECU> cpcField, String nameForUserInterface, String descriptionForUI) {
        super(project, cpcField, nameForUserInterface, descriptionForUI);
    }
    
    public List<String> getMcrIdList() {
        List<CpcIGpmMcrData> repositoryDataList = getDataList();
        List<String> mcrPlanIdList = new ArrayList<>();
        for(CpcIGpmMcrData data: repositoryDataList) {
            String mcrPlanId = data.getMcrPlanId().trim();
            if(mcrPlanId.isEmpty() == false) {
                mcrPlanIdList.add(mcrPlanId);
            }
        }
        return (mcrPlanIdList);
    }
    
    @Override
    public List<CpcIGpmMcrData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcIGpmMcrData> repositoryDataList = CpcXmlTable_iGPM_MCR_ECU.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcIGpmMcrData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_iGPM_MCR_ECU.DESC.pack(dataList);
        setValue(tableData);
    }
}
