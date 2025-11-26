/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1User;
import DataModel.Rq1.Records.DmRq1UserRole;
import DataModel.DmMappedElement;
import Rq1Cache.Fields.Interfaces.Rq1ListField_DmInterface;
import Rq1Cache.Types.Rq1MappedReference;
import Rq1Data.Enumerations.Rq1UserIsActive;
import java.util.List;
import util.SafeArrayList;

public class DmRq1Field_ActiveProjectMembers extends DmRq1Field_MappedReferenceList<DmRq1UserRole, DmRq1User> {

    private SafeArrayList<DmMappedElement<DmRq1UserRole, DmRq1User>> activeUserList = null;

    public DmRq1Field_ActiveProjectMembers(DmElementI parent, Rq1ListField_DmInterface<Rq1MappedReference> rq1Field, String nameForUserInterface) {
        super(parent, rq1Field, nameForUserInterface);
    }

    @Override
    public List<DmMappedElement<DmRq1UserRole, DmRq1User>> getElementList() {
        if (activeUserList != null) {
            return (activeUserList.getImmutableList());
        } else {

            SafeArrayList<DmMappedElement<DmRq1UserRole, DmRq1User>> completeUserList = super.getElementList_Synchronized();
            SafeArrayList<DmMappedElement<DmRq1UserRole, DmRq1User>> selectedUserList = new SafeArrayList<>();
            for (DmMappedElement<DmRq1UserRole, DmRq1User> user : completeUserList) {
                if (Rq1UserIsActive.YES.equals(user.getTarget().IS_ACTIVE.getValue())) {
                    selectedUserList.add(user);
                }
            }
            activeUserList = selectedUserList;

            return (activeUserList.getImmutableList());
        }
    }

    @Override
    public void reload() {
        if (activeUserList != null) {
            activeUserList = null;
            super.reload();
        }
    }
}
