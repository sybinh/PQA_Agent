/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmElementListField_ReadOnlyFromSource;
import DataModel.DmToDsField_Text;
import Doors.DoorsFolder;
import Doors.DoorsRecord;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class DmDoorsFolder extends DmDoorsRecord {

    final public DmToDsField_Text TITLE;
    final public DmToDsField_Text DESCRIPTION;
    final public DmElementListField_ReadOnlyFromSource<DmDoorsElement> FOLDER_CONTENT;

    public DmDoorsFolder(DoorsFolder folder) {
        this(ElementType.FOLDER, folder);
    }

    protected DmDoorsFolder(ElementType type, final DoorsFolder folder) {
        super(type, folder);

        addField(TITLE = new DmToDsField_Text<>(folder.NAME, "Title"));
        addField(DESCRIPTION = new DmToDsField_Text<>(folder.DESCRIPTION, "Description"));
        addField(FOLDER_CONTENT = new DmElementListField_ReadOnlyFromSource<DmDoorsElement>("Elements") {
            @Override
            protected List<DmDoorsElement> loadElementList() {
                List<DmDoorsElement> elementList = new ArrayList<>();
                for (DoorsRecord doorsElement : folder.getElementList()) {
                    elementList.add(DmDoorsFactory.getElementByRecord(doorsElement));
                }
                return (elementList);
            }
        });
    }

    @Override
    public String toString() {
        return ("Folder " + TITLE.getValueAsText());
    }

    @Override
    public String getId() {
        return (TITLE.getValueAsText());
    }

    @Override
    public String getTitle() {
        return (TITLE.getValueAsText());
    }

}
