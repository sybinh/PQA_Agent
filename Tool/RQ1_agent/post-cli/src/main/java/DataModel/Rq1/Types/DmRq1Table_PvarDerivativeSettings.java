/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.Rq1.Records.DmRq1Pvar;
import DataModel.Rq1.Records.DmRq1SoftwareProject;
import Rq1Data.Types.Rq1DerivativeMapping;
import Rq1Data.Types.Rq1DerivativeMapping.Mode;
import Rq1Data.Types.Rq1DerivativePlannedDates;
import java.util.Map;
import java.util.TreeMap;
import util.EcvDate;
import util.EcvTableColumn_ComboBox;
import util.EcvTableColumn_Date;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;

/**
 * Table for displaying and editing of the derivative settings of a PVAR.
 *
 * @author GUG2WI
 */
public class DmRq1Table_PvarDerivativeSettings extends EcvTableDescription {

    final public EcvTableColumn_ComboBox MAPPING;
    final public EcvTableColumn_String DERIVATIVE;
    final public EcvTableColumn_Date PLANNED_DATE;
    final public EcvTableColumn_String PROPLATO_HARDWARE;

    /**
     *
     */
    public DmRq1Table_PvarDerivativeSettings() {

        String[] mappingModes = new String[Rq1DerivativeMapping.Mode.values().length];
        int i = 0;
        for (Rq1DerivativeMapping.Mode m : Rq1DerivativeMapping.Mode.values()) {
            mappingModes[i++] = m.getModeString();
        }

        addIpeColumn(MAPPING = new EcvTableColumn_ComboBox("Mapping", mappingModes));
        addIpeColumn(DERIVATIVE = new EcvTableColumn_String("Derivative"));
        DERIVATIVE.setReadOnly();
        addIpeColumn(PLANNED_DATE = new EcvTableColumn_Date("Planned Date"));
        addIpeColumn(PROPLATO_HARDWARE = new EcvTableColumn_String("ProPlaTo Hardware"));

        setDefaultSortColumn(DERIVATIVE, true);
    }

    /**
     * Creates the table data based on the given project. One row is created for
     * each PST line defined in the project.
     *
     * @param project
     * @return
     */
    public EcvTableData createTableDataForProject(DmRq1SoftwareProject project) {
        assert (project != null);

        EcvTableData data = super.createTableData();

        for (String derivative : project.getDerivativeNames()) {
            EcvTableRow row = data.createRow();
            MAPPING.setValue(row, Rq1DerivativeMapping.Mode.NOT_DECIDED.getModeString());
            DERIVATIVE.setValue(row, derivative);
            PLANNED_DATE.setValue(row, EcvDate.getEmpty());
            PROPLATO_HARDWARE.setValue(row, "");
            data.addRow(row);
        }

        return (data);
    }

    /**
     * Creates the table data based on the given project and PVAR release. One
     * row is created for each PST line defined in the project. The mode for
     * this lines is initialized by the settings from the PVAR-Release.
     *
     * @param project
     * @param pvar
     * @param pvarRelease
     * @return
     */
    public EcvTableData createTableDataForProjectAndRelease(DmRq1SoftwareProject project, DmRq1Pvar pvar) {
        assert (project != null);
        assert (pvar != null);

        Rq1DerivativeMapping predecessorMapping = pvar.DERIVATIVES.getValue();

        EcvTableData data = super.createTableData();

        Map<String, String> hardwareMap = pvar.PROPLATO_HARDWARE.getTableDescription().createHardwareMap(pvar.PROPLATO_HARDWARE.getValue());

        for (String derivative : project.getDerivativeNames()) {

            Mode mode = predecessorMapping.getModeForDerivative(derivative);
            if (mode == null) {
                mode = Mode.NOT_DECIDED;
            }
            String hardware = hardwareMap.get(derivative);
            if (hardware == null) {
                hardware = "";
            }

            EcvTableRow row = data.createRow();
            MAPPING.setValue(row, mode.getModeString());
            DERIVATIVE.setValue(row, derivative);
            PLANNED_DATE.setValue(row, EcvDate.getEmpty());
            PROPLATO_HARDWARE.setValue(row, hardware);
            data.addRow(row);
        }

        return (data);
    }

    /**
     *
     * @param pvarRelease
     */
    public void writeToPvar(EcvTableData data, DmRq1Pvar pvar) {
        assert (pvar != null);
        assert (data != null);
        assert (data.getDescription() instanceof DmRq1Table_PvarDerivativeSettings);

        Rq1DerivativeMapping derivativeMap = new Rq1DerivativeMapping();
        Rq1DerivativePlannedDates plannedDates = new Rq1DerivativePlannedDates();
        Map<String, String> hardwareMap = new TreeMap<>();

        for (EcvTableRow row : data.getRows()) {
            String derivativeName = (String) row.getValueAt(DERIVATIVE);
            String hardware = (String) row.getValueAt(PROPLATO_HARDWARE);
            Mode mode = Mode.getModeForString((String) row.getValueAt(MAPPING));
            EcvDate plannedDate = (EcvDate) row.getValueAt(PLANNED_DATE);

            if ((hardware != null) && (hardware.isEmpty() == false)) {
                hardwareMap.put(derivativeName, hardware);
            }
            if (mode != null) {
                derivativeMap.addMapping(mode, derivativeName);
            }
            if ((plannedDate != null) && (plannedDate.isEmpty() == false)) {
                plannedDates.addDate(derivativeName, plannedDate);
            }
        }

        pvar.PROPLATO_HARDWARE.setValue(pvar.PROPLATO_HARDWARE.getTableDescription().setHardware(hardwareMap));
        pvar.DERIVATIVES.setValue(derivativeMap);
        pvar.PLANNED_DATE_DERIVATIVES.setValue(plannedDates);
    }

    public void recalculatePlannedDates(EcvTableData data, EcvDate pilotPlannedDate, Integer delayForDerivative) {
        assert (data != null);
        assert (pilotPlannedDate != null);

        //
        // Set date for pilot and collect mandatory derivatives in alphabetical order.
        //
        Map<String, EcvTableRow> mandatoryRows = new TreeMap<>();
        for (EcvTableRow row : data.getRows()) {
            Mode mode = Mode.getModeForString((String) row.getValueAt(MAPPING));

            switch (mode) {
                case MANDATORY:
                    mandatoryRows.put(DERIVATIVE.getValue(row), row);
                    break;
                case PILOT:
                    PLANNED_DATE.setValue(row, pilotPlannedDate);
                    break;

                default:
                    PLANNED_DATE.setValue(row, null);
                    break;
            }
        }

        //
        // Set planned date for mandatory derivatives
        //
        EcvDate mandatoryPlannedDate = pilotPlannedDate;
        for (EcvTableRow row : mandatoryRows.values()) {
            PLANNED_DATE.setValue(row, mandatoryPlannedDate.addDays(delayForDerivative));
            mandatoryPlannedDate = PLANNED_DATE.getValue(row);
        }

    }
}
