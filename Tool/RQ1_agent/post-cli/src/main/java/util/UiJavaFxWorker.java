/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import ToolUsageLogger.ToolUsageLogger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 * Wrapper for a (JavaFX)Task object. This class enhances the SwingWorker with
 * functionality needed in IPE to shows the state of background tasks in the
 * status line.
 *
 * This class executes the method backgroundTask() in a SwingWorker and the
 * method uiJavaFxEndTask() in the Swing Dispatcher thread.
 *
 * @author GUG2WI
 * @param <RETURN_TYPE>
 */
public abstract class UiJavaFxWorker<RETURN_TYPE> extends Task<RETURN_TYPE> implements UiWorkerI {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(UiJavaFxWorker.class.getCanonicalName());

    private String action;

    @SuppressWarnings("LeakingThisInConstructor")
    public UiJavaFxWorker(String action) {
        assert (action != null);
        assert (action.isEmpty() == false);
        this.action = action;
        UiWorkerManager.addWorker(this);
        setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                uiJavaFxEndTask();
            }
        });
    }

    @Override
    final protected RETURN_TYPE call() throws Exception {
        UiWorkerManager.setWorkerRunning(this);

        RETURN_TYPE result = null;
        try {
            result = backgroundTask();
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Exception in background task for '" + action + "'.", t);
            ToolUsageLogger.logError(UiJavaFxWorker.class.getCanonicalName(), t);
        }
        UiWorkerManager.removeWorker(this);
        return (result);
    }

    /**
     * This method implemented in the sub class shall do the actions that have
     * to be done in the background task. It is called in a SwingWorker thread.
     *
     * @return
     */
    protected abstract RETURN_TYPE backgroundTask();

    /**
     * This method implemented in the sub class can do actions that shall be
     * executed after the background task.It is called in the Swing Dispatcher
     * task.
     */
    protected void uiJavaFxEndTask() {
    }

    /**
     * Returns the result of doInBackground() or null.
     *
     * @return
     */
    final protected RETURN_TYPE getResult() {
        try {
            return (get());
        } catch (InterruptedException | ExecutionException ex) {
            LOGGER.log(Level.SEVERE, "Getting result failed.", ex);
            ToolUsageLogger.logError(UiJavaFxWorker.class.getCanonicalName(), ex);
            return (null);
        }
    }

    @Override
    public String getAction() {
        return (action);
    }

    @Override
    public void setAction(String newAction) {
        assert (newAction != null);
        assert (newAction.isEmpty() == false);
        action = newAction;
    }

    static public <T> void execute(UiJavaFxWorker<T> task) {
        new Thread(task).start();
    }

}
