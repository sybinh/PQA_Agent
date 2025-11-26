/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_EnumerationFromText;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataStore.ALM.DsAlmRecord;
import util.EcvDate;
import util.EcvDateTime;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public class DmAlmWorkitem_Task extends DmAlmWorkitem {

    public enum Status_Task implements EcvEnumeration {

        NEW("New"),
        INVALID("Invalid"),
        IN_DEVELOPMENT("In Development"),
        DONE("Done");

        private final String dbText;

        private Status_Task(String dbText) {
            assert (dbText != null);
            this.dbText = dbText;
        }

        @Override
        public String getText() {
            return (dbText);
        }

        @Override
        public String toString() {
            return (getText());
        }

    }

    public static final String ELEMENT_TYPE = "Task";

    public DmAlmField_EnumerationFromText STATUS;
    public DmAlmField_Text DESCRIPTION;

    public DmAlmWorkitem_Task(DsAlmRecord dsAlmRecord) {
        super(ELEMENT_TYPE, dsAlmRecord);
        init();
    }

    public DmAlmWorkitem_Task(DmAlmProjectArea projectArea) {
        super(ELEMENT_TYPE, projectArea);
        init();
    }

    private void init() {
        DESCRIPTION = addTextField("dcterms:description", "Description");
        STATUS = addEnumerationFromTextField("oslc_cm:status", Status_Task.values(), "Status", true);

        checkForUnusedFields();
    }

    public static DmAlmWorkitem_Task create(DmAlmProjectArea projectArea) {
        assert (projectArea != null);

        DmAlmWorkitem_Task newTask = new DmAlmWorkitem_Task(projectArea);

        //set default Title
        newTask.TITLE.setValue("Task <" + EcvDate.getToday() + " " + EcvDateTime.getNow() + ">");

        //add new Task to Project Area
        projectArea.REFERENCED_TASKS.addElement(newTask);

        return (newTask);
    }

    @Override
    public String getStatus() {
        return (STATUS.getValueAsText());
    }

}
