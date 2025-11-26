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
import Rq1Data.CPC.CpcXmlTable_SuperOPL;
import Rq1Data.CPC.CpcXmlTable_SuperOPL.CpcSuperOplData;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_SuperOPL extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_SuperOPL, CpcSuperOplData> {
    
    public DmRq1Field_CpcRepositoryTable_SuperOPL(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_SuperOPL> cpcField, String nameForUserInterface) {
        super(project, cpcField, nameForUserInterface);
    }
    
    @Override
    public List<CpcSuperOplData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcSuperOplData> repositoryDataList = CpcXmlTable_SuperOPL.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcSuperOplData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_SuperOPL.DESC.pack(dataList);
        setValue(tableData);
    }
}
