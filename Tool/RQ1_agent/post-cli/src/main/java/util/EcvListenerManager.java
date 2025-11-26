/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author GUG2WI
 * @param <T>
 */
public class EcvListenerManager<T> {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EcvListenerManager.class.getCanonicalName());

    private static final EcvIdentityMapList<Thread, EcvListenerManager> transactions = new EcvIdentityMapList<>();

    public interface ChangeListener<T> {

        /**
         * Called to inform the listener about a change in the observed element.
         *
         * @param changedElement Element that was changed.
         */
        void changed(T changedElement);
    }

    final protected T managedElement;
    private EcvWeakList<ChangeListener<T>> changeListeners;
    private boolean fireChangeActive;

    public EcvListenerManager(T managedElement) {
        assert (managedElement != null);

        this.managedElement = managedElement;
        this.changeListeners = null;
        this.fireChangeActive = false;
    }

    final public synchronized void addChangeListener(ChangeListener<T> newListener) {
        assert (newListener != null);

        if (changeListeners == null) {
            changeListeners = new EcvWeakList<>(1);
        }
        changeListeners.add(newListener);
    }

    final public synchronized void removeChangeListener(ChangeListener<T> obsoletListener) {
        assert (obsoletListener != null);

        if (changeListeners != null) {
            changeListeners.remove(obsoletListener);
        }
    }

    final public void fireChange() {

        //
        // Prevent double call
        //
        if (setFireChangeActive() == false) {
            return;
        }

        if (checkTransaction() == true) {
            //
            // Work with a copy of the listener list, because the listener might change the listener list.
            //
            for (ChangeListener<T> listener : copyCurrentList()) {
                listener.changed(managedElement);
            }
        }

        resetFireChangeActive();

    }

    private synchronized boolean setFireChangeActive() {
        if (fireChangeActive == true) {
            Exception e = new Exception("Change propagation active: " + managedElement.getClass().toString() + " - " + managedElement.toString());
            logger.log(Level.WARNING, "Change propagation active. This is only a hint. No problem or error.", e);
            return (false);
        }
        fireChangeActive = true;
        return (true);
    }

    private synchronized void resetFireChangeActive() {
        fireChangeActive = false;
    }

    final protected synchronized List<ChangeListener<T>> copyCurrentList() {
        if (changeListeners != null) {
            return (changeListeners.getCopy());
        } else {
            return (Collections.emptyList());
        }
    }

    static public void startTransaction() {
        synchronized (transactions) {
            transactions.add(Thread.currentThread());
        }
    }

    static public void endTransaction() {
        List<EcvListenerManager> pendingActions = null;
        synchronized (transactions) {
            List<EcvListenerManager> l = transactions.remove(Thread.currentThread());
            if (l != null) {
                pendingActions = new ArrayList<>(l);
            }
        }
        if (pendingActions != null) {
            for (EcvListenerManager action : pendingActions) {
                action.fireChange();
            }
        }
    }

    private boolean checkTransaction() {
        synchronized (transactions) {
            if (transactions.containsKey(Thread.currentThread()) == true) {
                transactions.add(Thread.currentThread(), this);
                return (false);
            } else {
                return (true);
            }
        }
    }

}
