/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.DmElementI;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1SoftwareProject;
import Rq1Data.Types.Rq1DerivativeMapping;
import java.awt.Font;
import util.EcvTableDescription;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import util.EcvDate;
import util.EcvTableColumn_ComboBox;
import util.EcvTableColumn_Date;
import util.EcvTableColumn_Derivate;
import util.EcvTableData;
import util.UiWorker;

/**
 * Table for displaying and editing of the derivative settings of a PVAR.
 *
 * @author GUG2WI
 */
public class DmRq1Table_PvarDerivatives extends EcvTableDescription {

    static public class DerivativeComboBox extends JComboBox<String> implements DmElementI.ChangeListener {

        private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DerivativeComboBox.class.getCanonicalName());

        private DmRq1SoftwareProject softwareProject = null;

        @SuppressWarnings("LeakingThisInConstructor")
        private DerivativeComboBox(final DmRq1Field_Reference<DmRq1Project> projectField) {

            final DerivativeComboBox myComboBox = this;

            UiWorker.execute(new UiWorker<Void>(UiWorker.LOADING) {

                private DmRq1Project project;

                @Override
                protected Void backgroundTask() {
                    project = projectField.getElement();
                    return (null);
                }

                @Override
                protected void uiEndTask() {
                    if (project instanceof DmRq1SoftwareProject) {
                        softwareProject = (DmRq1SoftwareProject) project;
                        for (String derivative : softwareProject.getDerivativeNames()) {
                            addItem(derivative);
                        }
                        softwareProject.addChangeListener(myComboBox);
                    } else {
                        LOGGER.warning("PST assigned to a project which is not a software project. "
                                + "Project-ID=" + project.getRq1Id());
                        softwareProject = null;
                    }
                }

            });

        }

        @Override
        public void changed(DmElementI changedElement) {
            removeAllItems();
            if (softwareProject != null) {
                for (String derivative : softwareProject.getDerivativeNames()) {
                    addItem(derivative);
                }
            }

        }

    }

    static public class DerivativeColumn extends EcvTableColumn_ComboBox {

        final private DmRq1Field_Reference<DmRq1Project> projectField;

        public DerivativeColumn(String uiName, DmRq1Field_Reference<DmRq1Project> projectField) {
            super(uiName, new String[]{"-"});
            assert (projectField != null);
            this.projectField = projectField;
        }

        @Override
        public TableCellEditor getTableCellEditor(Font font) {
            JComboBox<String> comboBox = new DerivativeComboBox(projectField);

            return (new DefaultCellEditor(comboBox));
        }
    }

    final public EcvTableColumn_Derivate MAPPING;
    final public DerivativeColumn DERIVATIVE;
    final public EcvTableColumn_Date PLANNED_DATE;

    public DmRq1Table_PvarDerivatives(DmRq1Field_Reference<DmRq1Project> projectField) {

        String[] mappingModes = new String[Rq1DerivativeMapping.Mode.values().length];
        int i = 0;
        for (Rq1DerivativeMapping.Mode m : Rq1DerivativeMapping.Mode.values()) {
            mappingModes[i++] = m.getModeString();
        }
        addIpeColumn(MAPPING = new EcvTableColumn_Derivate("Mapping", 12, mappingModes));
        addIpeColumn(DERIVATIVE = new DerivativeColumn("Derivative", projectField));
        addIpeColumn(PLANNED_DATE = new EcvTableColumn_Date("Planned Date"));
    }

    static public class Record {

        private String derivative;
        private String mode;
        private EcvDate plannedDate;

        public Record(String derivative, String mode, EcvDate plannedDate) {
            this.derivative = derivative;
            this.mode = mode;
            this.plannedDate = plannedDate;
        }

        public void setDerivative(String derivative) {
            this.derivative = derivative;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public void setPlannedDate(EcvDate plannedDate) {
            this.plannedDate = plannedDate;
        }

        public String getDerivative() {
            return derivative;
        }

        public String getMode() {
            return mode;
        }

        public EcvDate getPlannedDate() {
            return plannedDate;
        }
    }

    /**
     * Extracts the relevant data from the EcvTableData, which can be used to
     * access the data more convenient
     *
     * @param data EcvTableDate, used to generate the Records for the Record
     * list
     * @return List of Records, containing the table data
     */
    static public List<Record> extract(EcvTableData data) {
        //TODO: Maybe Refactor Code

        List<Record> records = new ArrayList<>();
        int rowCount = data.getRowCount();
        for (int i = 0; i < data.getRowCount(); i++) {
            String derivative = "";
            String mode = "";
            EcvDate plannedDate = null;
            for (int ii = 0; ii < data.getColumnCount(); ii++) {
                if (ii == 0) {
                    mode = data.getValueAt(i, ii).toString();
                }
                if (ii == 1) {
                    derivative = data.getValueAt(i, ii).toString();
                }
                if (ii == 2) {
                    plannedDate = (EcvDate) data.getValueAt(i, ii);
                }
                records.add(new Record(derivative, mode, plannedDate));
            }
        }
        return records;
    }

    static public List<Record> extractWithValidPlannedDate(EcvTableData data) {
        List<Record> recordList = extract(data);
        List<Record> plannedDateRecordList = new ArrayList<>();
        for (Record record : recordList) {
            if (record.getPlannedDate() != null) {
                plannedDateRecordList.add(record);
            }
        }
        return plannedDateRecordList;
    }
}
