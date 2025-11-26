/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import ToolUsageLogger.ToolUsageLogger;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 *
 * @author GUG2WI
 * @param <RETURN_TYPE> The type of value returned by the task.
 */
public abstract class UiWorkerForJavaFx<RETURN_TYPE> extends Task<RETURN_TYPE> implements UiWorkerI {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(UiWorkerForJavaFx.class.getCanonicalName());

    private static ThreadPoolExecutor executorService;

    private String action;

    public UiWorkerForJavaFx(String action) {
        assert (action != null);
        assert (action.isEmpty() == false);
        this.action = action;
        UiWorkerManager.addWorker(this);
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

    @Override
    final protected RETURN_TYPE call() throws Exception {
        UiWorkerManager.setWorkerRunning(this);
        RETURN_TYPE result = null;
        try {
            result = backgroundTaskForJavaFx();
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Exception in background task for '" + action + "'.", t);
            ToolUsageLogger.logError(UiWorkerForJavaFx.class.getCanonicalName(), t);
        }

        RETURN_TYPE resultForEndTask = result;
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                uiEndTaskForJavaFx(resultForEndTask);
            }
        });

        UiWorkerManager.removeWorker(this);
        return (result);
    }

    /**
     * This method implemented in the sub class shall do the actions that have
     * to be done in the background task. It is called in a SwingWorker thread.
     *
     * @return
     */
    protected abstract RETURN_TYPE backgroundTaskForJavaFx();

    /**
     * This method implemented in the sub class can do actions that shall be
     * executed after the background task. It is called in the Swing Dispatcher
     * task.
     *
     * @param result
     */
    protected void uiEndTaskForJavaFx(RETURN_TYPE result) {
    }

    static public synchronized <T> void execute(UiWorkerForJavaFx<T> task) {
        assert (task != null);
        if (executorService == null) {
            executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
            executorService.setCorePoolSize(1);
            executorService.setMaximumPoolSize(10);
        }
        executorService.submit(task);
    }

}
