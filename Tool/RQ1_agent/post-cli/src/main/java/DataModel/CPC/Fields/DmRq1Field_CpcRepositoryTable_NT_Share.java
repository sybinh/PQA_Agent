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
import Rq1Data.CPC.CpcXmlTable_NT_Share;
import Rq1Data.CPC.CpcXmlTable_NT_Share.CpcNtShareData;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_NT_Share extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_NT_Share, CpcNtShareData> {
    
    public DmRq1Field_CpcRepositoryTable_NT_Share(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_NT_Share> cpcField, String nameForUserInterface, String descriptionForUI) {
        super(project, cpcField, nameForUserInterface, descriptionForUI);
    }
    
    @Override
    public List<CpcNtShareData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcNtShareData> repositoryDataList = CpcXmlTable_NT_Share.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcNtShareData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_NT_Share.DESC.pack(dataList);
        setValue(tableData);
    }
}
