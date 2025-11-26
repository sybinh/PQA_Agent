/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.OrgData;

import DataModel.GPM.DmGpmUserRole;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author GUG2WI
 */
public class OrgData_Person extends OrgData_Element {

    final static private Logger LOGGER = Logger.getLogger(OrgData_Person.class.getCanonicalName());

    private String role;
    private String rq1UserId;
    private String customName;
    private String forwardProjectId;

    public OrgData_Person(String role, String rq1UserId, String customName) {
        assert (role != null);
        assert (customName != null);
        this.role = role;
        this.rq1UserId = rq1UserId;
        this.customName = customName;
        this.forwardProjectId = "";
    }

    public OrgData_Person(String role, String rq1UserId, String customName, String forwardProject) {
        this(role, rq1UserId, customName);
        assert (forwardProject != null);
        this.forwardProjectId = forwardProject;
    }

    @Override
    public boolean supporterAllowed() {
        return (true);
    }

    public String getRole() {
        return role;
    }

    public String getRq1UserId() {
        return rq1UserId;
    }

    public String getCustomName() {
        return customName;
    }

    public String getForwardProjectId() {
        return forwardProjectId;
    }

    public void setRole(String role) {
        assert (role != null);
        this.role = role;
    }

    public void setRq1UserId(String rq1UserId) {
        this.rq1UserId = rq1UserId;
    }

    public void setCustomName(String customName) {
        assert (customName != null);
        this.customName = customName;
    }

    public void setForwardProjectId(String forwardProject) {
        this.forwardProjectId = forwardProject;
    }

    @Override
    public boolean equalsTree(OrgData_Element otherData) {

        if (otherData instanceof OrgData_Person) {

            if (role.equals(((OrgData_Person) otherData).role) == false) {
                return (false);
            }

            if (Objects.equals(rq1UserId, ((OrgData_Person) otherData).rq1UserId) == false) {
                return (false);
            }

            if (customName.equals(((OrgData_Person) otherData).customName) == false) {
                return (false);
            }

            if (forwardProjectId != null && forwardProjectId.equals(((OrgData_Person) otherData).forwardProjectId) == false) {
                return (false);
            }

            return super.equalsTree(otherData);
        } else {
            return (false);
        }
    }

    @Override
    public String toString() {
        String userId = (rq1UserId != null) ? (rq1UserId + "/") : "";
        return ("OrgData_Person: " + role + "/" + userId + customName);
    }

    @Override
    protected void addRolesFromSubTree(Set<String> usedRoles) {
        if ((role != null) && (role.isEmpty() == false)) {
            usedRoles.add(role);
        }
        super.addRolesFromSubTree(usedRoles);
    }

    static public OrgData_Person getPersonForRole(String role, OrgData_Element rootElement) {
        assert (rootElement != null);

        if ((role != null) && (role.isEmpty() == false)) {

            //
            // Get customName and description from the given role string
            //
            String roleName = role;
            String roleDescription = role;
            DmGpmUserRole userRole = DmGpmUserRole.getByLongNameOrAbbreviation(role);
            if (userRole != null) {
                roleName = userRole.getCombinedName();
                roleDescription = userRole.getLongName();
            }

            List<OrgData_Person> result = new ArrayList<>();
            addPersonForRole(roleName, roleDescription, rootElement, result);
            if (result.size() > 1) {
                LOGGER.warning("More than one person fits to role.\n"
                        + "role: >" + role + "<\n"
                        + result.toString());
            }
            if (result.size() >= 1) {
                return (result.get(0));
            }
        }
        return (null);
    }

    static public Set<String> getPersonSetForRole(String role, OrgData_Element orgData) {
        assert (orgData != null);

        if ((role != null) && (role.isEmpty() == false)) {
            String roleName = role;
            String roleDescription = role;
            DmGpmUserRole userRole = DmGpmUserRole.getByLongNameOrAbbreviation(role);
            if (userRole != null) {
                roleName = userRole.getAbbreviation();
                roleDescription = userRole.getLongName();
            }

            List<OrgData_Person> result = new ArrayList<>();
            Set<String> personsForRole = new HashSet<>();
            addPersonForRole(roleName, roleDescription, orgData, result);
            for (OrgData_Person person : result) {
                personsForRole.add(person.customName);
            }
            return personsForRole;
        }
        return null;
    }

    static private void addPersonForRole(String roleName, String roleDescription, OrgData_Element rootElement, List<OrgData_Person> result) {
        assert (roleName != null);
        assert (roleName.isEmpty() == false || roleDescription.isEmpty() == false);
        assert (roleDescription != null);
        assert (rootElement != null);
        assert (result != null);

        if (rootElement instanceof OrgData_Person) {
            OrgData_Person person = (OrgData_Person) rootElement;
            if (roleName.equals(person.getRole()) == true) {
                result.add(person);
            } else if (roleDescription.equals(person.getRole()) == true) {
                result.add(person);
            }
        }
        for (OrgData_Element child : rootElement.getChilds()) {
            addPersonForRole(roleName, roleDescription, child, result);
        }
        for (OrgData_Element supported : rootElement.getSupporters()) {
            addPersonForRole(roleName, roleDescription, supported, result);
        }
    }

    @Override
    protected OrgData_Element copyElement() {
        return (new OrgData_Person(role, rq1UserId, customName, forwardProjectId));
    }

}
