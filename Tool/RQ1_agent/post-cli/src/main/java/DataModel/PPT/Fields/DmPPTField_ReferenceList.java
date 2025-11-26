/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.DmElement;
import DataModel.DmElementListFieldI;
import java.util.List;

/**
 *
 * @author moe83wi
 * @param <T>
 */
public abstract class DmPPTField_ReferenceList<T extends DmElement> extends DmPPTField implements DmElementListFieldI<T> {

    protected final List<T> element;

    public DmPPTField_ReferenceList(DmElement parent, List<T> element, String nameForUserInterface) {
        super(parent, nameForUserInterface);
        assert (element != null);
        this.element = element;
    }

    @Override
    public void addElement(T e) {
        assert (element != null);
        element.add(e);
    }

    public void addElements(List<T> list) {
        for (T element : list) {
            addElement(element);
        }
    }

    @Override
    final public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
