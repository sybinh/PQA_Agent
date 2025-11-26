/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.Rq1.Fields.DmRq1Field_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Data.GPM.GpmXmlTable_GpmUserRoles;
import java.util.EnumSet;
import java.util.List;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * Note that this field is replaced by the org chart field DmGpmField_OrgData.
 * The user role table will no longer shown to the user. But the implementation
 * remains, because existing data has to be read by IPE if no org chart was
 * created by the user yet.
 *
 * Provides the list of user roles available in a project. The user roles
 * available are those in the XML-Table for user roles plus all roles in
 * DmGpmUserRole.
 *
 * @author GUG2WI
 */
public class DmGpmFieldOnProject_UserRoles extends DmRq1Field_Table<GpmXmlTable_GpmUserRoles> {

    public DmGpmFieldOnProject_UserRoles(Rq1XmlSubField_Table<GpmXmlTable_GpmUserRoles> rq1TableField, String nameForUserInterface) {
        super(rq1TableField, nameForUserInterface);
    }

    public List<GpmXmlTable_GpmUserRoles.Record> getValueAsRecordList() {

        //--------------------------------
        // Get roles defined in XML field
        //--------------------------------
        EcvTableData dsData = super.getValue();
        List<GpmXmlTable_GpmUserRoles.Record> recordList = GpmXmlTable_GpmUserRoles.extract(dsData);

        //------------------------------------------------------------------
        // Add roles defined in DmGpmUserRole and not defined in XLS field
        //------------------------------------------------------------------
        EnumSet<DmGpmUserRole> missingRoles = EnumSet.allOf(DmGpmUserRole.class);
        recordList.forEach(record -> {
            DmGpmUserRole role = DmGpmUserRole.getByAbbreviation(record.getRole());
            if (role != null) {
                missingRoles.remove(role);
            }
        });

        for (DmGpmUserRole missingRole : missingRoles) {
            String abbrevaiation = missingRole.getAbbreviation();
            if (abbrevaiation.isEmpty() == false) {
                recordList.add(new GpmXmlTable_GpmUserRoles.Record(abbrevaiation, ""));
            }
            
        }

        return (recordList);
    }

    /**
     * Returns the roles defined in XML field plus those hard coded roles for
     * which no user is assigned in the XML field.
     *
     * @return
     */
    @Override
    public EcvTableData getValue() {

        EcvTableData dsData = super.getValue();
        List<GpmXmlTable_GpmUserRoles.Record> recordList = getValueAsRecordList();

        EcvTableData dmData = ((GpmXmlTable_GpmUserRoles) dsData.getDescription()).pack(recordList);

        return (dmData);
    }

    /**
     * Removes lines containing hard coded roles but no user.
     *
     * @param uiValue
     */
    @Override
    public void setValue(EcvTableData uiValue) {

        GpmXmlTable_GpmUserRoles d = (GpmXmlTable_GpmUserRoles) uiValue.getDescription();
        EcvTableData dmData = d.createTableData();

        for (EcvTableRow row : uiValue.getRows()) {
            String role = d.ROLE_NAME.getValue(row);
            String user = d.USER_ID.getValue(row);
            if (((user != null) && (user.trim().isEmpty() == false))
                    || ((role != null) && (role.trim().isEmpty() == false) && (DmGpmUserRole.getByAbbreviation(role) == null))) {
                dmData.addRow(row.copy());
            }
        }

        super.setValue(dmData);
    }

}
