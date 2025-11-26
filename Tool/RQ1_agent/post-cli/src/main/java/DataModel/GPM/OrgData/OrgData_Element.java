/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.OrgData;

import DataModel.GPM.DmGpmUserRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author GUG2WI
 */
public abstract class OrgData_Element {

    private OrgData_Element parent = null;
    private List<OrgData_Element> childs = new ArrayList<>();
    private List<OrgData_Element> supporters = new ArrayList<>();

    //--------------------------------------------------------------------------
    //
    // Provide information about the element
    //
    //--------------------------------------------------------------------------
    public boolean hasParent() {
        return (parent != null);
    }

    public boolean isChild() {
        if (parent != null) {
            return (parent.childs.contains(this));
        }
        return (false);
    }

    public boolean isSupporter() {
        if (parent != null) {
            return (parent.supporters.contains(this));
        }
        return (false);
    }

    public OrgData_Element getParent() {
        return parent;
    }

    public List<OrgData_Element> getChilds() {
        return childs;
    }

    public List<OrgData_Element> getSupporters() {
        return supporters;
    }

    public boolean supporterAllowed() {
        return (false);
    }

    //--------------------------------------------------------------------------
    //
    // Tree manipulation
    //
    //--------------------------------------------------------------------------
    public void addChild(OrgData_Element childElement) {
        assert (childElement != null);

        childs.add(childElement);
        childElement.parent = this;
    }

    public void addSupporter(OrgData_Element supporterData) {
        assert (supporterData != null);

        supporters.add(supporterData);
        supporterData.parent = this;
    }

    public void removeFromDataTree() {
        assert (parent != null);

        //
        // Put own childs into the child list of the parent at the same place as this object was.
        //
        int childIndex = parent.childs.indexOf(this);
        if (childIndex >= 0) {
            parent.childs.addAll(childIndex, childs);
        } else {
            parent.childs.addAll(childs);
        }
        parent.childs.remove(this);
        for (OrgData_Element child : childs) {
            child.parent = parent;
        }

        //
        // Put own supporttes into the suppoerter list of the parent at the same place as this object was.
        //
        int supporterIndex = parent.supporters.indexOf(this);
        if (supporterIndex >= 0) {
            parent.supporters.addAll(supporterIndex, supporters);
        } else {
            parent.supporters.addAll(supporters);
        }
        for (OrgData_Element supporter : supporters) {
            supporter.parent = parent;
        }
        parent.supporters.remove(this);

        parent = null;
        childs.clear();
        supporters.clear();
    }

    public void insertParent(OrgData_Element newParent) {
        assert (newParent != null);

        int childIndex = parent.childs.indexOf(this);
        if (childIndex >= 0) {
            parent.childs.set(childIndex, newParent);
            newParent.parent = parent;
            newParent.childs.add(this);
            parent = newParent;
        } else {
            int supporterIndex = parent.supporters.indexOf(this);
            assert (supporterIndex >= 0);
            parent.supporters.set(supporterIndex, newParent);
            newParent.parent = parent;
            newParent.supporters.add(this);
            parent = newParent;
        }
    }

    public void moveChildUp() {
        assert (parent != null);

        //
        // Exchange supporters
        //
        if (supporterAllowed() && parent.supporterAllowed()) {
            List<OrgData_Element> parentSupporters = parent.supporters;
            parent.supporters = this.supporters;
            this.supporters = parentSupporters;
            parent.setParentForSupporters();
            this.setParentForSupporters();
        }

        //
        // Exchange childs
        //
        OrgData_Element parentOfParent = parent.parent;

        OrgData_Element currentParent = parent;
        List<OrgData_Element> childsOfParent = parent.childs;

        OrgData_Element parentOfThis = parent;
        List<OrgData_Element> childsOfThis = childs;

        // Exchange childs
        this.childs = childsOfParent;
        currentParent.childs = childsOfThis;

        // Adapt childs
        int i = childsOfParent.indexOf(this);
        childsOfParent.set(i, currentParent);
        i = parentOfParent.childs.indexOf(currentParent);
        parentOfParent.childs.set(i, this);

        // Adapt parents
        this.parent = parentOfParent;
        currentParent.setParentForChilds();
        this.setParentForChilds();

    }

    private void setParentForSupporters() {
        for (OrgData_Element supporter : supporters) {
            supporter.parent = this;
        }
    }

    private void setParentForChilds() {
        for (OrgData_Element child : childs) {
            child.parent = this;
        }
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

    public boolean equalsTree(OrgData_Element otherData) {

        List<OrgData_Element> myChilds = childs;
        List<OrgData_Element> otherChilds = otherData.childs;
        if (myChilds.size() != otherChilds.size()) {
            return (false);
        }
        for (int i = 0; i < myChilds.size(); i++) {
            if (myChilds.get(i).equalsTree(otherChilds.get(i)) == false) {
                return (false);
            }
        }

        List<OrgData_Element> mySupporters = supporters;
        List<OrgData_Element> otherSupporters = otherData.supporters;
        if (mySupporters.size() != otherSupporters.size()) {
            return (false);
        }
        for (int i = 0; i < mySupporters.size(); i++) {
            if (mySupporters.get(i).equalsTree(otherSupporters.get(i)) == false) {
                return (false);
            }
        }

        return (true);
    }

    //--------------------------------------------------------------------------
    //
    // Support for project/chart specific data
    //
    //--------------------------------------------------------------------------
    /**
     * Returns all roles that are predefined and all additional roles used in
     * the tree.
     *
     * @return A list of all used or predefined roles.
     */
    public Collection<String> getAllRoles() {

        // Go to top of tree
        OrgData_Element topElement = this;
        while (topElement.parent != null) {
            topElement = topElement.parent;
        }

        // Get all used roles
        Set<String> allRoles = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String role1, String role2) {
                return role1.toLowerCase().compareTo(role2.toLowerCase());
            }
        });
        topElement.addRolesFromSubTree(allRoles);

        // Add all predefined roles
        for (DmGpmUserRole role : DmGpmUserRole.values()) {
            allRoles.add(role.getCombinedName());
        }

        return (allRoles);
    }

    protected void addRolesFromSubTree(Set<String> usedRoles) {
        assert (usedRoles != null);
        for (OrgData_Element child : childs) {
            child.addRolesFromSubTree(usedRoles);
        }
        for (OrgData_Element supporter : supporters) {
            supporter.addRolesFromSubTree(usedRoles);
        }
    }

    public OrgData_Element copyTree() {

        OrgData_Element elementCopy = copyElement();
        for (OrgData_Element child : childs) {
            elementCopy.addChild(child.copyTree());
        }
        for (OrgData_Element supporter : supporters) {
            elementCopy.addSupporter(supporter.copyTree());
        }
        return (elementCopy);
    }

    abstract protected OrgData_Element copyElement();

}
