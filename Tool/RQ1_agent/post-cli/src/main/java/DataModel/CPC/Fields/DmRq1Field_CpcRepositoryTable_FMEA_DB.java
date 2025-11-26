/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CPC.Fields;

import Rq1Data.CPC.CpcXmlTable_FMEA_DB.CpcFmeaDbData;
import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Data.CPC.CpcXmlTable_FMEA_DB;
import java.util.List;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_FMEA_DB extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_FMEA_DB, CpcFmeaDbData> {
    
    public DmRq1Field_CpcRepositoryTable_FMEA_DB(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_FMEA_DB> cpcField, String nameForUserInterface) {
        super(project, cpcField, nameForUserInterface);
    }
    
    @Override
    public List<CpcFmeaDbData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcFmeaDbData> repositoryDataList = CpcXmlTable_FMEA_DB.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }
    
    @Override
    public void setValueAsList(List<CpcFmeaDbData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_FMEA_DB.DESC.pack(dataList);
        setValue(tableData);
    }
}
