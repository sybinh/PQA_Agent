/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmElement;
import Doors.DoorsRecord;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author gug2wi
 */
public abstract class DmDoorsElement extends DmElement {

    //--------------------------------------------------------------------------
    //
    // Member types and variables
    //
    //--------------------------------------------------------------------------
    public enum ElementType {

        REQUIREMENT_WITHOUT_URL("No URL"),
        NOT_FOUND("Not Found"),
        DATABASE("DOORS Database"),
        PROJECT("DOORS Project"),
        FOLDER("DOORS Folder"),
        MODULE("DOORS Module"),
        OBJECT("DOORS Object"),
        FUNC_REQ("FUNC_REQ"),
        NON_FUNC_REQ("NON_FUNC_REQ"),
        SYSTEM_REQ("SYSTEM_REQ"),
        HEADER("HEADER"),
        INFO("INFO"),
        TEST_CASE("TEST_CASE"),
        DETAILED_SW_REQ("DETAILED_SW_REQ"),
        SOFTWARE_REQ("SOFTWARE_REQ"),
        INTERFACE_REQ("INTERFACE_REQ"),
        PROPERTY("DOORS Property"),
        BASELINE("DOORS Baseline"),
        DESIGN_DECISION("DESIGN_DECISION"),
        PTSA_20_ATYPE_HEADER("Heading"),
        PTSA_20_ATYPE_INFO("Information"),
        PTSA_20_ATYPE_QLTY_NON_FUNC_REQ("Quality Requirement"),
        PTSA_20_ATYPE_MO_FUNC_REQ("Functional MO requirement"),
        PTSA_20_ATYPE_MO_NON_FUNC_REQ("Non-functional MO requirement"),
        PTSA_20_ATYPE_SC_FUNC_REQ("Functional SC requirement"),
        PTSA_20_ATYPE_SC_NON_FUNC_REQ("Non-functional SC requirement"),
        PTSA_20_ATYPE_SC_INTERFACE_REQ("Interface SC requirement"),
        PTSA_20_ATYPE_BC_FC_FUNC_REQ("Functional BC-FC requirement"),
        PTSA_20_ATYPE_BC_FC_NON_FUNC_REQ("Non-functional BC-FC requirement"),
        PTSA_20_ATYPE_BC_FC_INTERFACE_REQ("Interface BC-FC requirement"),
        PTSA_20_STKH_HEADER("Stakeholder Heading"),
        PTSA_20_STKH_INFO("Stakeholder Information"),
        PTSA_20_STKH_FUNC_REQ("Stakeholder Request Functional"),
        PTSA_20_STKH_NON_FUNC_REQ("Stakeholder Request Non-Functional");

        final private String type;

        ElementType(String type) {
            this.type = type;
        }

        public String getTypeString() {
            return type;
        }

    }
    final private ElementType type;
    private DoorsRecord doorsElement;

    protected DmDoorsElement(ElementType type, DoorsRecord doorsElement) {
        super(type.getTypeString());
        this.type = type;
        this.doorsElement = doorsElement;
    }

    @Override
    public void reload() {
        if (doorsElement == null) {
            return;
        }
        boolean hasChanged = doorsElement.reload();
        if (hasChanged) {
            super.fireChange();
        }
    }

    public ElementType getDoorsElementType() {
        return (type);
    }

    public Collection<String> getIdOfReferencedRq1Elements() {
        return (new ArrayList<>());
    }

    public String getDoorsEditState() {
        return ("<none>");
    }

    public String getDoorsObjectIdentifier() {
        return ("<none>");
    }

    public String getDoorsAsilLevel() {
        return ("<none>");
    }

    public String getDoorsAllocation() {
        return ("<none>");
    }

    public String getDoorsTags() {
        return ("");
    }

    public String getUrl() {
        return (doorsElement.getUrl());
    }

}
