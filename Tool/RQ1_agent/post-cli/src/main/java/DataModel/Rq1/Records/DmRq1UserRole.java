/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.DmRq1LinkInterface;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1UserRole;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public class DmRq1UserRole extends DmRq1Element implements DmRq1LinkInterface {

    public enum Role {

        NONE(""),
        PROJECT_MEMBER("ProjectMember"),
        PROJECT_LEADER("ProjectLeader");

        final private String roleTitleInRq1;

        private Role(String textInDatabase) {
            assert (textInDatabase != null);
            this.roleTitleInRq1 = textInDatabase;
        }

        public Role getRoleWithMoreAccessRights(Role otherRole) {
            if (otherRole != null) {
                if (otherRole.ordinal() > this.ordinal()) {
                    return (otherRole);
                }
            }
            return (this);
        }

        static public Role getRoleForRoleTitle(String roleTitleInRq1) {
            assert (roleTitleInRq1 != null);

            for (Role role : values()) {
                if (role.roleTitleInRq1.equals(roleTitleInRq1)) {
                    return (role);
                }
            }
            return (NONE);
        }

    }

    final private static String TYPE = "RQ1-UserRole";

    final public DmRq1Field_Text ROLE_NAME;
    final public DmRq1Field_Reference<DmRq1SoftwareProject> IS_PROJECT_MEMBER;
    final public DmRq1Field_Reference<DmRq1User> LINKED_USER;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1UserRole(Rq1UserRole rq1Role) {
        super(TYPE, rq1Role);

        //
        // Create and add fields
        //
        addField(ROLE_NAME = new DmRq1Field_Text(this, rq1Role.ROLE_NAME, "Role Name"));
        addField(IS_PROJECT_MEMBER = new DmRq1Field_Reference<>(this, rq1Role.IS_PROJECT_MEMBER, "Project"));
        addField(LINKED_USER = new DmRq1Field_Reference<>(this, rq1Role.LINKED_USER, "Linked User"));
    }

    @Override
    public EcvEnumeration getLifeCycleState() {
        return (LINKED_USER.getElement().IS_ACTIVE.getValue());
    }

    @Override
    public String getTitle() {
        return (ROLE_NAME.getValue());
    }

    @Override
    public String toString() {
        return (TYPE + ": " + ROLE_NAME.getValueAsText());
    }

    @Override
    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (LINKED_USER.getElement().IS_ACTIVE.getValidInputValues());
    }

    public Role getRole() {
        return (Role.getRoleForRoleTitle(ROLE_NAME.getValue()));
    }

}
