/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_DateTime;
import Rq1Cache.Fields.Rq1DatabaseField_Text;
import Rq1Cache.Rq1NodeDescription;

/**
 *
 * @author gug2wi
 */
public class Rq1HistoryLog extends Rq1Record implements Rq1NodeInterface {

    final public Rq1DatabaseField_Text ACTION_NAME;
    final public Rq1DatabaseField_Text HISTORY_ID;
    final public Rq1DatabaseField_Text HISTORY_LOG;
    final public Rq1DatabaseField_DateTime LAST_MODIFIED_DATE;
    final public Rq1DatabaseField_Text LAST_MODIFIED_USER;
    final public Rq1DatabaseField_Text LIFE_CYCLE_STATE;
    final public Rq1DatabaseField_Text PREVIOUS_LIFE_CYCLE_STATE;
    final public Rq1DatabaseField_Text OPERATION_CONTEXT;
    final public Rq1DatabaseField_Text OPERATION_MODE;

    public Rq1HistoryLog() {
        super(Rq1NodeDescription.HISTORY_LOG);

        addField(ACTION_NAME = new Rq1DatabaseField_Text(this, "ActionName"));
        addField(HISTORY_ID = new Rq1DatabaseField_Text(this, "HistoryId"));
        addField(HISTORY_LOG = new Rq1DatabaseField_Text(this, "HistoryLog"));
        addField(LAST_MODIFIED_DATE = new Rq1DatabaseField_DateTime(this, "LastModifiedDate"));
        addField(LAST_MODIFIED_USER = new Rq1DatabaseField_Text(this, "LastModifiedUser"));
        addField(LIFE_CYCLE_STATE = new Rq1DatabaseField_Text(this, "LifeCycleState"));
        addField(PREVIOUS_LIFE_CYCLE_STATE = new Rq1DatabaseField_Text(this, "PreviousLifeCycleState"));
        addField(OPERATION_CONTEXT = new Rq1DatabaseField_Text(this, "OperationContext"));
        addField(OPERATION_MODE = new Rq1DatabaseField_Text(this, "OperationMode"));

        ACTION_NAME.setReadOnly();
        HISTORY_ID.setReadOnly();
        HISTORY_LOG.setReadOnly();
        LAST_MODIFIED_DATE.setReadOnly();
        LAST_MODIFIED_USER.setReadOnly();
        LIFE_CYCLE_STATE.setReadOnly();
        PREVIOUS_LIFE_CYCLE_STATE.setReadOnly();
        OPERATION_CONTEXT.setReadOnly();
        OPERATION_MODE.setReadOnly();
    }

    @Override
    public void reload() {
        // ignored
    }

}
