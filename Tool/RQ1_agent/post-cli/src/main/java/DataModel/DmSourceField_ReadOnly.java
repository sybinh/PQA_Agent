/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * Supports the implementation of a value field whose value is taken from some
 * source.
 * <P>
 * The access to the source has to be implemented in the subclass.
 * <P>
 * The field is read only.
 *
 * @param <T> Type of the element provided by the field.
 */
public abstract class DmSourceField_ReadOnly<T> extends DmSourceField<T> {

    public DmSourceField_ReadOnly(String nameForUserInterface) {
        super(nameForUserInterface);
    }

    @Override
    final public void setValue(T v) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    final public boolean isReadOnly() {
        return (true);
    }

}
