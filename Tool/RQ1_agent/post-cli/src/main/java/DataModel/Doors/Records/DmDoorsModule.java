/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmElementListField_ReadOnlyFromSource;
import DataModel.DmToDsField_Text;
import Doors.DoorsBaseline;
import Doors.DoorsModule;
import Doors.DoorsObject;
import OslcAccess.Doors.OslcDoorsProperty;
import UiSupport.UiTreeViewRootElementI;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a module from the DOORS database on the data model level.
 *
 * @author gug2wi
 */
public class DmDoorsModule extends DmDoorsRecord implements UiTreeViewRootElementI {

    final public DmToDsField_Text TITLE;
    final public DmElementListField_ReadOnlyFromSource<DmDoorsBaseline> BASELINES;
    final public DmElementListField_ReadOnlyFromSource<DmDoorsProperty> PROPERTIES;
    final public DmElementListField_ReadOnlyFromSource<DmDoorsObject> OBJECTS;

    public DmDoorsModule(DoorsModule module) {
        super(ElementType.MODULE, module);

        final DmDoorsModule myThis = this;

        addField(TITLE = new DmToDsField_Text<>(module.NAME, "Title"));
        addField(BASELINES = new DmElementListField_ReadOnlyFromSource<DmDoorsBaseline>("Baselines") {
            @Override
            protected List<DmDoorsBaseline> loadElementList() {
                List<DmDoorsBaseline> result = new ArrayList<>();
                for (DoorsBaseline oslcBaseline : module.getBaselines()) {
                    result.add(new DmDoorsBaseline(myThis, oslcBaseline));
                }
                return (result);
            }
        });
        addField(PROPERTIES = new DmElementListField_ReadOnlyFromSource<DmDoorsProperty>("Properties") {
            @Override
            protected List<DmDoorsProperty> loadElementList() {
                List<DmDoorsProperty> result = new ArrayList<>();
                for (OslcDoorsProperty oslcProperty : module.getProperties()) {
                    result.add(new DmDoorsProperty(oslcProperty, module));
                }
                return (result);
            }
        });
        addField(OBJECTS = new DmElementListField_ReadOnlyFromSource<DmDoorsObject>("Objects") {
            @Override
            protected List<DmDoorsObject> loadElementList() {
                List<DmDoorsObject> result = new ArrayList<>();
                for (DoorsObject doorsObject : module.getObjects()) {
                    DmDoorsObject dmDoorsObject = (DmDoorsObject) DmDoorsFactory.getElementByRecord(doorsObject);
                    result.add(dmDoorsObject);
                }
                return (result);
            }
        });
    }

    @Override
    public String toString() {
        return ("Module " + TITLE.getValueAsText());
    }

    @Override
    final public String getId() {
        return (TITLE.getValueAsText());
    }

    @Override
    final public String getTitle() {
        return (TITLE.getValueAsText());
    }

    @Override
    public String getViewTitle() {
        return (TITLE.getValueAsText());
    }

}
