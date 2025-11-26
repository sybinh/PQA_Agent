/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Supports the implementation of a field whose referenced element list is taken
 * from some source. The access to the source has to be implemented in the
 * subclass.
 * <br>
 * The element list is cached in the object and only reloaded on explicit
 * request.
 * <br>
 * The field is read only by default.
 * <br>
 *
 * @author gug2wi
 * @param <T> Type of the elements hold in the list.
 */
public abstract class DmElementListField_ReadOnlyFromSource<T extends DmElementI> extends DmField implements DmElementListField_ReadOnlyI<T> {

    private List<T> elementList = null;

    public DmElementListField_ReadOnlyFromSource(String nameForUserInterface) {
        super(nameForUserInterface);
    }

    @Override
    final public synchronized List<T> getElementList() {
        if (elementList == null) {
            removeAllDependencies();
            Collection<T> loadedList = loadElementList();
            assert (loadedList != null);
            elementList = Collections.unmodifiableList(new ArrayList<>(loadedList));
        }
        return (elementList);
    }

    /**
     * Returns the first element in the list that matches to the given title.
     *
     * @param title Title of the searched element.
     * @return The found element or null if no matching element was found.
     */
    final public T getElementByTitle(String title) {
        assert (title != null);
        assert (title.isEmpty() == false);
        for (T element : getElementList()) {
            if (title.equals(element.getTitle())) {
                return (element);
            }
        }
        return (null);
    }

    @Override
    public synchronized void reload() {
        //
        // Reload only, if list was already loaded.
        //
        if (elementList != null) {
            elementList = null;
            getElementList();
        }
    }

    @Override
    protected void handleDependencyChange() {
        elementList = null;
        fireFieldChanged();
    }

    /**
     * Checks, whether the list was already loaded.
     *
     * @return true, if the list is already loaded and cached; false otherwise;
     */
    public boolean isLoaded() {
        return (elementList != null);
    }

    /**
     * Loads the element list from the source. This method has to be provided in
     * the subclass.
     *
     * @return The list of elements. Can be an empty list.
     */
    protected abstract Collection<T> loadElementList();

}
