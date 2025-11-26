/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import Monitoring.MarkableI.MarkerListener;

/**
 * Supports the implementation of an lazy loading element.
 *
 * @author GUG2WI
 */
public abstract class DmElementReference<T_REFERENCED_ELEMENT extends DmElementI> extends DmElement implements DmElementReferenceI<T_REFERENCED_ELEMENT>, MarkerListener {

    private T_REFERENCED_ELEMENT lazyloadedElement = null;

    public DmElementReference(String elementType) {
        super(elementType);
    }

    @Override
    final public T_REFERENCED_ELEMENT getReferencedElement() {
        if (lazyloadedElement == null) {
            lazyloadedElement = loadReferencedElement();
        }
        return (lazyloadedElement);
    }

    protected abstract T_REFERENCED_ELEMENT loadReferencedElement();

}
