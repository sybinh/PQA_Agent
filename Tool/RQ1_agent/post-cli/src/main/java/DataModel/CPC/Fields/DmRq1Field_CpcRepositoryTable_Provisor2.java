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
import Rq1Data.CPC.CpcXmlTable_Provisor2;
import Rq1Data.CPC.CpcXmlTable_Provisor2.CpcProvisor2Data;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_Provisor2 extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_Provisor2, CpcProvisor2Data> {
    
    public DmRq1Field_CpcRepositoryTable_Provisor2(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_Provisor2> cpciCDMField, String nameForUserInterface) {
        super(project, cpciCDMField, nameForUserInterface);
    }
    
    @Override
    public List<CpcProvisor2Data> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcProvisor2Data> repositoryDataList = CpcXmlTable_Provisor2.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcProvisor2Data> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_Provisor2.DESC.pack(dataList);
        setValue(tableData);
    }
}
