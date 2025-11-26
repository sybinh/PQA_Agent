/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import Monitoring.Markable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import util.EcvListenerManager;

/**
 * Implements the handling of name, attributes and listeners for a field.
 *
 * @author gug2wi
 */
public abstract class DmField extends Markable implements DmFieldI {

    final private String nameForUserInterface;
    private EcvListenerManager<DmFieldI> listenerManager = null;
    private DmListener listener = null;

    protected DmField(String nameForUserInterface) {
        assert (nameForUserInterface != null);
        assert (nameForUserInterface.length() > 0);

        this.nameForUserInterface = nameForUserInterface;
    }

    @Override
    public String getNameForUserInterface() {
        return (nameForUserInterface);
    }

    //---------------------------------------------------------------------
    //
    // Interface to propagate changes
    //
    //---------------------------------------------------------------------
    @Override
    final public void addChangeListener(DmFieldI.ChangeListener newListener) {
        if (listenerManager == null) {
            listenerManager = new EcvListenerManager<DmFieldI>(this);
        }

        listenerManager.addChangeListener(newListener);
    }

    @Override
    final public void removeChangeListener(DmFieldI.ChangeListener obsoletListener) {
        if (listenerManager != null) {
            listenerManager.removeChangeListener(obsoletListener);
        }
    }

    /**
     * Distributes changes to listeners.
     */
    final protected void fireFieldChanged() {
        EcvListenerManager<DmFieldI> lm = listenerManager; // Use copy of reference to prevent problems with other thread.
        if (lm != null) {
            lm.fireChange();
        }
    }

    //---------------------------------------------------------------------
    //
    // Interface to receive changes from dependencies
    //
    //---------------------------------------------------------------------
    /**
     * Implement this method in the sub classes to provide a useful handling of
     * changes in the elements and fields added as dependency.
     */
    protected void handleDependencyChange() {

    }

    private synchronized DmListener getListener() {
        if (listener == null) {
            listener = new DmListener() {
                @Override
                protected void dependencyChanged() {
                    handleDependencyChange();
                }
            };
        }
        return (listener);
    }

    protected final void addDependency(DmElementI dmElement) {
        getListener().addDependency(dmElement);
    }

    protected final void addDependency(DmFieldI dmField) {
        getListener().addDependency(dmField);
    }

    protected final void removeAllDependencies() {
        if (listener != null) {
            getListener().removeAllDependencies();
        }
    }

    //---------------------------------------------------------------------
    //
    // Interface to handle attributes
    //
    //---------------------------------------------------------------------
    private List<Object> attributes = null;

    @Override
    public boolean hasAttribute(Object lockupAttribute) {
        assert (lockupAttribute != null);
        if (attributes == null) {
            return (false);
        } else {
            return (attributes.contains(lockupAttribute));
        }
    }

    @Override
    public void setAttribute(Object newAttribute) {
        assert (newAttribute != null);
        if (attributes == null) {
            attributes = new ArrayList<>(1);
        } else {
            for (Iterator<Object> it = attributes.iterator(); it.hasNext();) {
                if (it.next().equals(newAttribute)) {
                    it.remove();
                }
            }
        }
        attributes.add(newAttribute);
    }

    @Override
    public void setAttribute(Object newAttribute1, Object newAttribute2) {
        assert (newAttribute1 != null);
        assert (newAttribute2 != null);

        setAttribute(newAttribute1);
        setAttribute(newAttribute2);
    }

    public void setAttribute(Object newAttribute1, Object newAttribute2, Object newAttribute3) {
        assert (newAttribute1 != null);
        assert (newAttribute2 != null);
        assert (newAttribute3 != null);

        setAttribute(newAttribute1);
        setAttribute(newAttribute2);
        setAttribute(newAttribute3);
    }

    @Override
    public String toString() {
        return (this.getClass().getSimpleName() + ":" + nameForUserInterface);
    }

}
