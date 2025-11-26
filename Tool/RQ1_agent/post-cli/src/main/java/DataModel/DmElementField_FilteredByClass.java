/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * A class that shows only
 *
 * @author GUG2WI
 */
public class DmElementField_FilteredByClass<T_SOURCEELEMENT extends DmElementI, T_ELEMENT extends T_SOURCEELEMENT> extends DmField implements DmElementFieldI<T_ELEMENT> {

    final private DmElementFieldI<T_SOURCEELEMENT> sourceList;
    final private Class<T_ELEMENT> wantedClass;

    public DmElementField_FilteredByClass(DmElementFieldI<T_SOURCEELEMENT> sourceList, Class<T_ELEMENT> wantedClass, String nameForUserInterface) {
        super(nameForUserInterface);

        assert (sourceList != null);
        assert (wantedClass != null);

        this.sourceList = sourceList;
        this.wantedClass = wantedClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T_ELEMENT getElement() {
        DmElementI element = sourceList.getElement();
        if ((element != null) && (wantedClass.isAssignableFrom(element.getClass()) == true)) {
            return ((T_ELEMENT) element);
        } else {
            return (null);
        }
    }

    @Override
    public void setElement(T_ELEMENT element) {
        sourceList.setElement(element);
    }

    @Override
    public void removeElement() {
        sourceList.removeElement();
    }

}
