/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * Supports the implementation of a field whose value is taken from some source
 * and written to some target. The access to source and target has to be
 * implemented in the subclass.
 * <br>
 * The field support read/write by default.
 *
 * @param <T> Type of the element provided by the field.
 */
public abstract class DmSourceField<T> extends DmField implements DmValueFieldI<T> {

    public DmSourceField(String nameForUserInterface) {
        super(nameForUserInterface);
    }

    @Override
    public boolean isReadOnly() {
        return (false);
    }

}
