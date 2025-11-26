/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import ToolUsageLogger.ToolUsageLogger;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import javax.swing.SwingWorker;

/**
 * Wrapper for a SwingWorker object. This class enhances the SwingWorker with
 * functionality needed in IPE to shows the state of background tasks in the
 * status line.
 *
 * This class executes the method backgroundTask() in a SwingWorker and the
 * method uiEndTask() in the Swing Dispatcher thread.
 *
 * @author GUG2WI
 * @param <RETURN_TYPE>
 */
public abstract class UiWorker<RETURN_TYPE> extends SwingWorker<RETURN_TYPE, Void> implements UiWorkerI {

    static public abstract class UiWorkerVoid extends UiWorker<Void> {

        public UiWorkerVoid() {
        }

        public UiWorkerVoid(String action) {
            super(action);
        }

        @Override
        final protected Void backgroundTask() {
            backgroundTaskVoid();
            return (null);
        }

        protected abstract void backgroundTaskVoid();
    }

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(UiWorker.class.getCanonicalName());

    private String action;

    public UiWorker() {
        this(LOADING);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public UiWorker(String action) {
        assert (action != null);
        assert (action.isEmpty() == false);
        this.action = action;
        UiWorkerManager.addWorker(this);
    }

    @Override
    final protected RETURN_TYPE doInBackground() throws Exception {
        UiWorkerManager.setWorkerRunning(this);
        RETURN_TYPE result = null;
        try {
            result = backgroundTask();
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Exception in background task for '" + action + "'.", t);
            ToolUsageLogger.logError(UiWorker.class.getCanonicalName(), t);
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

    @Override
    final protected void done() {
        uiEndTask();
    }

    /**
     * This method implemented in the sub class can do actions that shall be
     * executed after the background task.It is called in the Swing Dispatcher
     * task.
     */
    protected void uiEndTask() {
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
            ToolUsageLogger.logError(UiWorker.class.getCanonicalName(), ex);
        } catch (CancellationException ex) {
            LOGGER.log(Level.INFO, "Getting result cancelled.", ex);
        }
        return (null);
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

    static public <T> void execute(UiWorker<T> task) {
        task.execute();
    }

    static public <T> T executeAndWait(UiWorker<T> task) {
        task.execute();
        return (task.getResult());
    }

}
