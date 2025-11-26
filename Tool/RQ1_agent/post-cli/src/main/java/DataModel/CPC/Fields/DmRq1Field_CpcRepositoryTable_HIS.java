/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.CPC.Fields;

import Rq1Data.CPC.CpcXmlTable_HIS.CpcHisData;
import DataModel.Rq1.Records.DmRq1Project;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Data.CPC.CpcXmlTable_HIS;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import util.EcvTableData;

/**
 *
 * @author hfi5wi
 */
public class DmRq1Field_CpcRepositoryTable_HIS extends DmRq1Field_CpcRepositoryTable<CpcXmlTable_HIS, CpcHisData> {

    public DmRq1Field_CpcRepositoryTable_HIS(DmRq1Project project, Rq1XmlSubField_Table<CpcXmlTable_HIS> cpcField, String nameForUserInterface, String descriptionForUI) {
        super(project, cpcField, nameForUserInterface, descriptionForUI);
    }

    @Override
    public List<CpcHisData> getDataList() {
        //----------------------------------------------------
        // Get data defined in XML field
        //----------------------------------------------------
        if (dataList != null) {
            return (dataList);
        }
        EcvTableData tableData = super.getValue();
        List<CpcHisData> repositoryDataList = CpcXmlTable_HIS.DESC.extract(tableData);
        return (dataList = repositoryDataList);
    }

    public Collection<String> getAllTargetEcuId() {
        return (getDataList().stream().map(t -> t.getTargetEcuId()).collect(Collectors.toList()));
    }

    @Override
    public void setValueAsList(List<CpcHisData> dataList) {
        assert (dataList != null);
        EcvTableData tableData = CpcXmlTable_HIS.DESC.pack(dataList);
        setValue(tableData);
    }
}
