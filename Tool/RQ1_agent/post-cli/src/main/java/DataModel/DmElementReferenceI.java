/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

/**
 * Supports the lazy loading of an element.
 *
 * @author GUG2WI
 */
public interface DmElementReferenceI<T_REFERENCED_ELEMENT extends DmElementI> extends DmElementI {

    T_REFERENCED_ELEMENT getReferencedElement();

}
