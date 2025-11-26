/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmSourceField_ReadOnly_Text;
import Doors.DoorsModule;
import OslcAccess.Doors.OslcDoorsProperty;

/**
 *
 * @author gug2wi
 */
public class DmDoorsProperty extends DmDoorsElement {

    final public DmSourceField_ReadOnly_Text NAME;
    final public DmSourceField_ReadOnly_Text NAMESPACE;
    final public DmSourceField_ReadOnly_Text TITLE;
    final public DmSourceField_ReadOnly_Text DESCRIPTION;
    final public DmSourceField_ReadOnly_Text VALUE_TYPE;
    final public DmSourceField_ReadOnly_Text OCCURENCE;
    final public DmSourceField_ReadOnly_Text READ_ONLY;

    public DmDoorsProperty(OslcDoorsProperty oslcProperty, DoorsModule parentModule) {
        super(ElementType.PROPERTY, parentModule);
        assert (oslcProperty != null);

        addField(NAME = new DmSourceField_ReadOnly_Text("Name") {
            @Override
            public String getValue() {
                return (oslcProperty.getName());
            }
        });
        addField(NAMESPACE = new DmSourceField_ReadOnly_Text("Namespace") {
            @Override
            public String getValue() {
                return (oslcProperty.getNameSpace().getNamespaceString());
            }
        });
        addField(TITLE = new DmSourceField_ReadOnly_Text("Title") {
            @Override
            public String getValue() {
                return (oslcProperty.getTitle());
            }
        });
        addField(DESCRIPTION = new DmSourceField_ReadOnly_Text("Description") {
            @Override
            public String getValue() {
                return (oslcProperty.getDescription());
            }
        });
        addField(VALUE_TYPE = new DmSourceField_ReadOnly_Text("Value Type") {
            @Override
            public String getValue() {
                return (oslcProperty.getValueType().name());
            }
        });
        addField(OCCURENCE = new DmSourceField_ReadOnly_Text("Occurence") {
            @Override
            public String getValue() {
                return (oslcProperty.getOccurence().name());
            }
        });
        addField(READ_ONLY = new DmSourceField_ReadOnly_Text("Read Only") {
            @Override
            public String getValue() {
                return (oslcProperty.isReadOnly() ? "true" : "false");
            }
        });
    }

    @Override
    public String getId() {
        return (TITLE.getValueAsText());
    }

    @Override
    public String getTitle() {
        return (TITLE.getValueAsText());
    }

    @Override
    public String toString() {
        return (NAMESPACE.getValue() + ":" + NAME.getValue() + " - " + TITLE.getValue());
    }

}
