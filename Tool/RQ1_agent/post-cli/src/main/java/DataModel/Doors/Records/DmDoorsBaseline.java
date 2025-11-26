/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmElementField_ReadOnlyFromSource;
import DataModel.DmElementListField_ReadOnlyFromSource;
import DataModel.DmSourceField_ReadOnly_Text;
import Doors.DoorsBaseline;
import Doors.DoorsObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class DmDoorsBaseline extends DmDoorsElement {

    final private DmDoorsModule parentModule;
    final public DmElementField_ReadOnlyFromSource<DmDoorsModule> PARENT_MODULE;
    final public DmElementListField_ReadOnlyFromSource<DmDoorsObject> OBJECTS;
    final public DmSourceField_ReadOnly_Text VERSION;
    final public DmSourceField_ReadOnly_Text NAME;

    public DmDoorsBaseline(DmDoorsModule parentModule, DoorsBaseline oslcBaseline) {
        super(ElementType.BASELINE, oslcBaseline);
        assert (parentModule != null);
        assert (oslcBaseline != null);

        this.parentModule = parentModule;

        addField(PARENT_MODULE = new DmElementField_ReadOnlyFromSource<DmDoorsModule>("Module") {
            @Override
            public DmDoorsModule getElement() {
                return (parentModule);
            }
        });
        addField(VERSION = new DmSourceField_ReadOnly_Text("Version") {
            @Override
            public String getValue() {
                return (oslcBaseline.getVersion());
            }
        });
        addField(NAME = new DmSourceField_ReadOnly_Text("Name") {
            @Override
            public String getValue() {
                return (oslcBaseline.getSuffix());
            }
        });
        addField(OBJECTS = new DmElementListField_ReadOnlyFromSource<DmDoorsObject>("Objects") {
            @Override
            protected List<DmDoorsObject> loadElementList() {
                List<DmDoorsObject> result = new ArrayList<>();
                for (DoorsObject doorsObject : oslcBaseline.getObjects()) {
                    DmDoorsObject dmDoorsObject = (DmDoorsObject) DmDoorsFactory.getElementByRecord(doorsObject);
                    result.add(dmDoorsObject);
                }
                return (result);
            }
        });
    }
    
    @Override
    public String getId() {
        return (parentModule.getId() + "/" + VERSION.getValueAsText());
    }

    @Override
    public String getTitle() {
        return (NAME.getValueAsText());
    }

    @Override
    public String toString() {
        return (getId() + getTitle());
    }

}
