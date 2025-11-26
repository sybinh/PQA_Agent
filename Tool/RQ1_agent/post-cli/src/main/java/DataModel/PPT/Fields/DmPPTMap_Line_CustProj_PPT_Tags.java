/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.PPT.Records.DmPPTLine;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import Rq1Cache.Types.Rq1XmlTableColumn;
import Rq1Cache.Types.Rq1XmlTable_ProPlaToProjectPstRail;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author moe83wi
 */
public class DmPPTMap_Line_CustProj_PPT_Tags{
    private EcvTableRow INFORMATION;
    public final Rq1XmlTable_ProPlaToProjectPstRail DESC;
    
    public DmPPTMap_Line_CustProj_PPT_Tags(DmPPTLine parent,  String nameForUserInterface) {
        DmPPTLine line = parent;
        INFORMATION = null;
        DmRq1Field_Table<Rq1XmlTable_ProPlaToProjectPstRail> pstRailInfo = line.BELONGS_TO_PROJECT.getElement().getPROPLATO_PSTRAILS();
        DESC = pstRailInfo.getTableDescription();
        EcvTableData data = pstRailInfo.getValue();
        for(EcvTableRow row : data.getRows()){
            if(row.getValueAt(DESC.RAIL).toString().equals(line.NAME.getValueAsText())){
                INFORMATION = row;
                break;
            }
        }
    }

    public String getElement(Rq1XmlTableColumn column) {
        if(INFORMATION!=null && DESC.getColumns().contains(column) && INFORMATION.getValueAt(column) != null){
            return INFORMATION.getValueAt(column).toString();
        }else{
            return "";
        }
    }
}
