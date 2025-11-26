/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CPC.Fields;

import Rq1Data.CPC.CpcXmlTable_SVN.CpcSvnData;
import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Data.CPC.CpcXmlTable_SVN;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_SVN extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_SVN, CpcSvnData> {
    
    public DmRq1Field_CpcRepositoryTable_SVN(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_SVN> cpcField, String nameForUserInterface) {
        super(project, cpcField, nameForUserInterface);
    }
    
    @Override
    public List<CpcSvnData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcSvnData> repositoryDataList = CpcXmlTable_SVN.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcSvnData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_SVN.DESC.pack(dataList);
        setValue(tableData);
    }
}
