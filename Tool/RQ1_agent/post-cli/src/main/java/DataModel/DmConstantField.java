/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * A field that holds a constant value. The constant value is set during the
 * construction of the field.
 *
 * @author gug2wi
 * @param <T> Type of the value hold by the field.
 */
public class DmConstantField<T> extends DmField implements DmValueFieldI<T> {

    final private T value;

    protected DmConstantField(String nameForUserInterface, T value) {
        super(nameForUserInterface);
        assert (value != null) : nameForUserInterface + " is null";
        this.value = value;
    }

    @Override
    final public T getValue() {
        return (value);
    }

    @Override
    final public void setValue(T v) {
        throw new UnsupportedOperationException("Not supported for field " + getNameForUserInterface());
    }

    @Override
    public boolean isReadOnly() {
        return (true);
    }

    @Override
    public String toString() {
        if (value == null) {
            return (this.getClass().getSimpleName() + " '" + getNameForUserInterface() + "'");
        } else {
            String valueString = value.toString();
            if (valueString.length() <= 40) {
                return (this.getClass().getSimpleName() + " '" + getNameForUserInterface() + "' >" + valueString + "<");
            } else {
                return (this.getClass().getSimpleName() + " '" + getNameForUserInterface() + "' >" + valueString.substring(0, 39) + "...<");
            }
        }
    }

}
