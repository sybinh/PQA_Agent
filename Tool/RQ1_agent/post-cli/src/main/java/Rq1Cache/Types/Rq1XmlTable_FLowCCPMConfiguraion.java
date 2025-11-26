/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

/**
 *
 * @author ser4cob
 */
public class Rq1XmlTable_FLowCCPMConfiguraion extends Rq1XmlTable {

    //
    // Example of SubTask structure:
    //
    //    <FLOW>
//<CCPM Id="1" S_Date="" F_Date="" Prog="" Buf="" > "desc"</CCPM>
    //        <SubTask Status="ToDo" Assignee="gey3si">Title of the subtask number 1</SubTask>
    //        <SubTask Status="Done" Assignee="gey3si">Title of the subtask number 2</SubTask>
    //    </FLOW>
    //
    final public Rq1XmlTableColumn_String ID;
    final public Rq1XmlTableColumn_String START_DATE;
    final public Rq1XmlTableColumn_String FREEZE_DATE;

    final public Rq1XmlTableColumn_String PROGRESS;
    final public Rq1XmlTableColumn_String BUFFER;
    final public Rq1XmlTableColumn_String TASK;

    public Rq1XmlTable_FLowCCPMConfiguraion() {

        addXmlColumn(ID = new Rq1XmlTableColumn_String("Id", 20, "Id", ColumnEncodingMethod.ATTRIBUTE));

        addXmlColumn(START_DATE = new Rq1XmlTableColumn_String("Start_Date", 10, "S_Date", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(FREEZE_DATE = new Rq1XmlTableColumn_String("Freeze_Date", 10, "F_Date", ColumnEncodingMethod.ATTRIBUTE));

        addXmlColumn(PROGRESS = new Rq1XmlTableColumn_String("Progress", 20, "Prog", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(BUFFER = new Rq1XmlTableColumn_String("Buffer", 20, "Buf", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(TASK = new Rq1XmlTableColumn_String("Task", 20, "Task", ColumnEncodingMethod.ATTRIBUTE));
        START_DATE.setOptional();
        FREEZE_DATE.setOptional();
        TASK.setOptional();
      //  setMaxRowCount(100);
    }

}
