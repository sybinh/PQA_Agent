/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * Supports the implementation of a field whose referenced element is taken from
 * some source. The access to the source has to be implemented in the subclass.
 * <br>
 * The field is read only by default.
 * <br>
 *
 * @author gug2wi
 * @param <T> Type of the elements hold in the list.
 */
public abstract class DmElementField_ReadOnlyFromSource<T extends DmElementI> extends DmField implements DmElementField_ReadOnlyI<T> {

    public DmElementField_ReadOnlyFromSource(String nameForUserInterface) {
        super(nameForUserInterface);
    }

}
