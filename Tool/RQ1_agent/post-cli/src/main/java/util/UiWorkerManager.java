/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.IdentityHashMap;
import java.util.List;

/**
 * The UiWorkerManager provides information about the currently waiting and
 * running worker threads. It is used to show the current state of background
 * processes in the task bar of IPE.
 *
 * @author GUG2WI
 */
public class UiWorkerManager {

    final static private SafeArrayList<UiWorkerI> waitingWorkers = new SafeArrayList<>();
    final static private SafeArrayList<UiWorkerI> runningWorkersList = new SafeArrayList<>();
    final static private IdentityHashMap<Thread, UiWorkerI> runningWorkersMap = new IdentityHashMap<>();

    static private int lastActionShown = 0;

    static public int getNumberOfRunningWorker() {
        synchronized (runningWorkersList) {
            return (runningWorkersList.size());
        }
    }

    static public int getNumberOfWaitingWorker() {
        synchronized (waitingWorkers) {
            return (waitingWorkers.size());
        }
    }

    static public String getCurrentAction() {
        synchronized (runningWorkersList) {
            if (runningWorkersList.isEmpty() == true) {
                return ("");
            }
            lastActionShown++;
            if (lastActionShown >= runningWorkersList.size()) {
                lastActionShown = 0;
            }
            return (runningWorkersList.get(lastActionShown).getAction() + " ...");
        }
    }

    static public String getMyTaskAction() {
        synchronized (runningWorkersList) {
            UiWorkerI myWorker = runningWorkersMap.get(Thread.currentThread());
            if (myWorker != null) {
                return (myWorker.getAction());
            } else {
                return ("");
            }
        }
    }

    static public void setMyTaskAction(String newAction) {
        assert (newAction != null);

        synchronized (runningWorkersList) {
            UiWorkerI myWorker = runningWorkersMap.get(Thread.currentThread());
            if (myWorker != null) {
                myWorker.setAction(newAction);
            }
        }
    }

    /**
     * Adds a worker to the waiting worker list.
     *
     * @param newWorker
     */
    static protected void addWorker(UiWorkerI newWorker) {
        assert (newWorker != null);
        synchronized (waitingWorkers) {
            waitingWorkers.add(newWorker);
        }
    }

    /**
     * Informs the manager that the worker is now running.
     *
     * @param worker Worker that switched to state running.
     */
    static protected void setWorkerRunning(UiWorkerI worker) {
        assert (worker != null);
        synchronized (waitingWorkers) {
            waitingWorkers.remove(worker);
        }
        synchronized (runningWorkersList) {
            runningWorkersMap.put(Thread.currentThread(), worker);
            runningWorkersList.add(worker);
        }
    }

    /**
     * Removes the worker from the waiting and running workers.
     *
     * @param oldWorker
     */
    static protected void removeWorker(UiWorkerI oldWorker) {
        assert (oldWorker != null);
        synchronized (runningWorkersList) {
            runningWorkersMap.remove(Thread.currentThread());
            runningWorkersList.remove(oldWorker);
        }
    }

    static public List<UiWorkerI> getAllRunningWorkerList() {
        return runningWorkersList.getImmutableList();
    }

}
