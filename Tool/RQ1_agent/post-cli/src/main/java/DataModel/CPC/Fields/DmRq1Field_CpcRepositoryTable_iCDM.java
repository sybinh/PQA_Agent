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
import Rq1Data.CPC.CpcXmlTable_iCDM;
import Rq1Data.CPC.CpcXmlTable_iCDM.CpcICdmData;
import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_iCDM extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_iCDM, CpcICdmData> {
    
    public DmRq1Field_CpcRepositoryTable_iCDM(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_iCDM> cpcField, String nameForUserInterface, String descriptionForUI) {
        super(project, cpcField, nameForUserInterface, descriptionForUI);
    }
    
    public List<String> getPidcVersIdList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList == null) {
            getDataList();
        }
        List<String> repositoryDataList = new ArrayList<>();
        dataList.forEach((data) -> {
            repositoryDataList.add(data.getPidcVersId());
        });
        return (repositoryDataList);
    }
    
    @Override
    public List<CpcICdmData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcICdmData> repositoryDataList = CpcXmlTable_iCDM.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcICdmData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_iCDM.DESC.pack(dataList);
        setValue(tableData);
    }
}
