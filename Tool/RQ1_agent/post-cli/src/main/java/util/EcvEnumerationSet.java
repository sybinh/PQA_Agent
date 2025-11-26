/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * Implements a set with proper sorting for EcvEnumeration.
 *
 * @author gug2wi
 */
public class EcvEnumerationSet extends TreeSet<EcvEnumeration> {

    /**
     * Create a new empty set.
     */
    public EcvEnumerationSet() {
        super(EcvEnumeration::compare);
    }

    /**
     * Create a new set filled with the values form the given set.
     *
     * @param oldSet The set from which the values are copied.
     */
    public EcvEnumerationSet(EcvEnumerationSet oldSet) {
        this();
        assert (oldSet != null);
        addAll(oldSet);
    }

    /**
     * Create a set filled with the given values.
     *
     * @param values
     */
    public EcvEnumerationSet(EcvEnumeration[] values) {
        this();
        addAll(values);
    }

    /**
     * Create a set filled with the given values.
     *
     * @param values
     */
    public EcvEnumerationSet(List<EcvEnumeration> values) {
        this();
        addAll(values);
    }

    /**
     * Create a set filled with EcvEnumeration object build with the given
     * Strings.
     *
     * @param values
     */
    public EcvEnumerationSet(Collection<String> values) {
        this();

        if (values != null) {
            for (String value : values) {
                EcvEnumeration enumeration = new EcvEnumerationValue(value, getNextOrdinal());
                add(enumeration);
            }
        }
    }

    @Override
    public final boolean add(EcvEnumeration newElement) {
        if ((newElement != null) && (findByText(newElement) == null)) {
            return super.add(newElement);
        }
        return false;
    }

    /**
     * Adds all values to the set.
     *
     * @param values
     */
    public final void addAll(EcvEnumeration[] values) {
        addAll(Arrays.asList(values));
    }

    @Override
    public final boolean addAll(Collection<? extends EcvEnumeration> newCollection) {
        boolean result = false;
        for (EcvEnumeration newElement : newCollection) {
            result |= add(newElement);
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> retainCollection) {
        EcvEnumerationSet oldSet = new EcvEnumerationSet(this);
        this.clear();
        for (Object retainObject : retainCollection) {
            if (retainObject instanceof EcvEnumeration) {
                EcvEnumeration retainElement = (EcvEnumeration) retainObject;
                EcvEnumeration oldElement = oldSet.findByText(retainElement);
                if (oldElement != null) {
                    this.add(oldElement);
                }
            }
        }
        return isEmpty() == false;
    }

    /**
     * Returns the element from the set that matches with the searched element
     * by the text.
     *
     * @param searchedElement
     * @return
     */
    private EcvEnumeration findByText(EcvEnumeration searchedElement) {
        if (searchedElement != null) {
            String searchedText = searchedElement.getText();
            for (EcvEnumeration entry : this) {
                if (entry.getText().equals(searchedText)) {
                    return entry;
                }
            }
        }
        return null;
    }

    /**
     * Calculates the next ordinal for a new value.
     *
     * @return
     */
    public final int getNextOrdinal() {
        int nextOrdinal = 0;
        for (EcvEnumeration v : this) {
            if (v.ordinal() >= nextOrdinal) {
                nextOrdinal = v.ordinal() + 1;
            }
        }
        return (nextOrdinal);
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof EcvEnumeration) {
            EcvEnumeration enumerationToCheck = (EcvEnumeration) o;
            for (EcvEnumeration enumerationInSet : this) {
                if (enumerationInSet.equals(enumerationToCheck)) {
                    return (true);
                }
            }
        }
        return (false);
    }

    /**
     * Returns a string containing the result of getText() for each element in
     * the set.
     *
     * @return A string containing the getText() of each element in the set.
     */
    public String getText() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        boolean first = true;
        for (EcvEnumeration e : this) {
            if (first != true) {
                result.append(", ");
            }
            result.append(e.getText());
            first = false;
        }
        result.append("]");
        return (result.toString());
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof EcvEnumerationSet == false) {
            return (false);
        }

        EcvEnumerationSet otherSet = (EcvEnumerationSet) o;

        if (size() != otherSet.size()) {
            return (false);
        }

        Iterator<EcvEnumeration> myIt = iterator();
        Iterator<EcvEnumeration> otherIt = otherSet.iterator();
        while (myIt.hasNext()) {
            if (myIt.next().compareTo(otherIt.next()) != 0) {
                return (false);
            }
        }

        return (true);
    }

}
