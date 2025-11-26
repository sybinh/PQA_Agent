/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.Flow.FlowBoardTaskSeverity;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Types.Rq1XmlTable_FlowBlocker;
import Rq1Cache.Types.Rq1XmlTable_FlowSubTask;
import Rq1Cache.Types.Rq1XmlTable_Milestones;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import util.EcvDate;
import util.EcvEnumeration;
import util.EcvTableColumn_ComboBox;
import util.EcvTableColumn_Date;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableRow;

/**
 * Implements the data model representation of the RQ1XmlTable_FlowBlocker with the matching Tile of the RQ1XmlTable_FlowSubtask.
 *
 * @author TRB83WI
 */
public class DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle extends DmRq1Field_TableDescription {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EcvDate.class.getCanonicalName());
    final public EcvTableColumn_String SUBTASKTITLE;
    final public EcvTableColumn_String START_DATE;
    final public EcvTableColumn_String END_DATE;
    final public EcvTableColumn_ComboBox SEVERITY;
    final public EcvTableColumn_String COMMENT;

    final private Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> rq1FlowBlocker;
    final private Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> rq1FlowSubTask;

    public DmRq1Field_FlowBlockerTableWithFlowSubTaskTitle(Rq1XmlSubField_Table<Rq1XmlTable_FlowBlocker> rq1FlowBlocker, Rq1XmlSubField_Table<Rq1XmlTable_FlowSubTask> rq1FlowSubTask, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (rq1FlowBlocker != null);
        assert (nameForUserInterface != null);
        assert (nameForUserInterface.isEmpty() == false);

        this.rq1FlowBlocker = rq1FlowBlocker;
        this.rq1FlowSubTask = rq1FlowSubTask;

        addIpeColumn(SUBTASKTITLE = new EcvTableColumn_String("Title", 100));
        addIpeColumn(START_DATE = new EcvTableColumn_String("Blocker set Date", 20));
        addIpeColumn(END_DATE = new EcvTableColumn_String("Blocker End Date", 20));
        addIpeColumn(SEVERITY = new EcvTableColumn_ComboBox("Severity",15, FlowBoardTaskSeverity.values()));
        addIpeColumn(COMMENT = new EcvTableColumn_String("Comment", 60));
        
    }

    @Override
    public synchronized EcvTableData getValue() {
        EcvTableData rq1DataFlowBlocker = rq1FlowBlocker.getDataModelValue();
        Rq1XmlTable_FlowBlocker rq1TableFlowBlocker = (Rq1XmlTable_FlowBlocker) rq1DataFlowBlocker.getDescription();
        EcvTableData rq1DataFlowSubTask = rq1FlowSubTask.getDataModelValue();
        Rq1XmlTable_FlowSubTask rq1TableFlowSubTask = (Rq1XmlTable_FlowSubTask) rq1DataFlowSubTask.getDescription();
        
        EcvTableData newDmData = createTableData();
        for (EcvTableRow rq1RowBlocker : rq1DataFlowBlocker.getRows()) {
            EcvTableRow dmRow = newDmData.createAndAddRow();
            
            for (EcvTableRow rq1RowSubTask : rq1DataFlowSubTask.getRows()) {
                if (rq1TableFlowBlocker.ID.getValue(rq1RowBlocker).equals(rq1TableFlowSubTask.ID.getValue(rq1RowSubTask))){
                        SUBTASKTITLE.setValue(dmRow, rq1TableFlowSubTask.TITLE.getValue(rq1RowSubTask));
                }
            }
            START_DATE.setValue(dmRow, rq1TableFlowBlocker.START_DATE.getValue(rq1RowBlocker));
            END_DATE.setValue(dmRow, rq1TableFlowBlocker.END_DATE.getValue(rq1RowBlocker));
            SEVERITY.setValue(dmRow, rq1TableFlowBlocker.SEVERITY.getValue(rq1RowBlocker));
            COMMENT.setValue(dmRow, rq1TableFlowBlocker.COMMENT.getValue(rq1RowBlocker));
            
        }

        return (newDmData);
    }
    
    @Override
    public synchronized void setValue(EcvTableData newData) {
        throw new UnsupportedOperationException("This function is not available for merged tables, they must be read only");
    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }

    @Override
    public boolean isReadOnly() {
        return (rq1FlowBlocker.isReadOnly());
    }
}
