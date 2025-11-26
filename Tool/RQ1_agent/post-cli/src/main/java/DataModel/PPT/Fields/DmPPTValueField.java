/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT.Fields;

import DataModel.DmElementI;
import DataModel.DmValueFieldI;

/**
 *
 * @author moe83wi
 * @param <T>
 */
public class DmPPTValueField<T> extends DmPPTField implements DmValueFieldI<T> {

    private T element;

    public DmPPTValueField(DmElementI parent, T v, String nameForUserInterface) {
        super(parent, nameForUserInterface);
        assert (v != null);
        this.element = v;
    }

    @Override
    public T getValue() {
        return element;
    }

    @Override
    public void setValue(T v) {
        this.element = v;
    }

}
