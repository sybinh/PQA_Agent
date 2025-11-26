/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THS83WI
 * @param <T>
 */
public class DmElementListField_ReadOnly_FilteredByClass<T extends DmElementI> extends DmField implements DmElementListField_ReadOnlyI<T> {

    final private DmElementListField_ReadOnlyI<? extends DmElementI> sourceList;
    final private Class<? extends T> wantedClass;

    private List<T> elementList = null;

    public DmElementListField_ReadOnly_FilteredByClass(String nameForUserInterface, DmElementListField_ReadOnlyI<? extends DmElementI> sourceList, Class<? extends T> wantedClass) {
        super(nameForUserInterface);

        assert (sourceList != null);
        assert (wantedClass != null);

        this.sourceList = sourceList;
        this.wantedClass = wantedClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getElementList() {
        if (elementList == null) {
            List<T> newElementList = new ArrayList<>();
            for (DmElementI element : sourceList.getElementList()) {
                if (wantedClass.isAssignableFrom(element.getClass()) == true) {
                    newElementList.add((T) element);
                }
            }
            elementList = newElementList;
        }
        return (elementList);
    }

    @Override
    public void reload() {
    }

}
