/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * A field that stores a value.
 *
 * @param <T> Type of the value stored by the field.
 */
public class DmValueField<T> extends DmField implements DmValueFieldI<T> {

    private T value;

    public DmValueField(String nameForUserInterface, T value) {
        super(nameForUserInterface);
        this.value = value;
    }

    @Override
    public boolean isReadOnly() {
        return (false);
    }

    @Override
    public T getValue() {
        return (value);
    }

    @Override
    public void setValue(T newValue) {
        value = newValue;
    }

}
