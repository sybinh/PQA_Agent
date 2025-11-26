/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_DerivativesTable;
import DataModel.Rq1.Fields.DmRq1Field_Table;
import Rq1Cache.Records.Rq1Pvar;
import Rq1Cache.Types.Rq1XmlTable_ProPlaToHardware;
import Rq1Data.Types.Rq1DerivativeMapping;
import Rq1Data.Types.Rq1DerivativePlannedDates;
import Rq1Data.Types.Rq1LineEcuProject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import util.EcvDate;
import util.EcvTableData;
import util.EcvTableRow;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Pvar extends DmRq1Pst {

    final public DmRq1Field_Table<Rq1XmlTable_ProPlaToHardware> PROPLATO_HARDWARE;

    public DmRq1Pvar(String subjectType, Rq1Pvar rq1PvarRelease) {
        super(subjectType, rq1PvarRelease);

        addField(PROPLATO_HARDWARE = new DmRq1Field_Table<>(this, rq1PvarRelease.PROPLATO_HARDWARE, "Hardware"));
    }

    @Override
    final public boolean areDerivativesRelevant() {
        return true;
    }

    static public class LinkedBc {

        final private DmRq1Bc bc;
        final List<String> derivatives;

        public LinkedBc(DmRq1Bc bc, List<String> derivatives) {
            assert (bc != null);
            assert (derivatives != null);
            assert (derivatives.isEmpty() == false);

            this.bc = bc;
            this.derivatives = derivatives;
        }

        public DmRq1Bc getBc() {
            return (bc);
        }

        public List<String> getDerivatives() {
            return (derivatives);
        }
    }

    @Override
    final public SortedSet<String> getActiveDerivatives() {
        SortedSet<String> result = new TreeSet<>();
        Rq1DerivativeMapping m = DERIVATIVES.getValue();
        for (String d : m.getDerivativeNames()) {
            if ((m.getModeForDerivative(d) != Rq1DerivativeMapping.Mode.EXCLUDED)
                    && (m.getModeForDerivative(d) != Rq1DerivativeMapping.Mode.NOT_DECIDED)) {
                result.add(d);
            }
        }
        return (result);
    }

    @Override
    final public Set<String> getAll_ProPlaTo_Cluster() {
        Set<String> result = new TreeSet<>();

        if (PROJECT.getElement() instanceof DmRq1SwCustomerProject_Leaf) {

            //
            // Build map to convert (internal) derivative to (external) schiene.
            //
            DmRq1SwCustomerProject_Leaf project = (DmRq1SwCustomerProject_Leaf) PROJECT.getElement();
            Map<String, String> derivateToSchiene = new TreeMap<>();
            for (Rq1LineEcuProject pstLine : project.getPstLines()) {
                if ((pstLine.getExternalName().isEmpty() == false) && (pstLine.getInternalName().isEmpty() == false)) {
                    derivateToSchiene.put(pstLine.getInternalName(), pstLine.getExternalName());
                }
            }

            //
            // Add all schiene for avtive derivatives
            //
            for (String derivative : getActiveDerivatives()) {
                if ((derivative != null) && derivative.trim().isEmpty() == false) {
                    if (derivateToSchiene.containsKey(derivative)) {
                        String cluster = project.getClusterForProgrammstandsschiene(derivateToSchiene.get(derivative));
                        if (cluster != null) {
                            result.add(cluster);
                        }
                    }

                }
            }
        }

        return result;
    }

    @Override
    final public Set<String> getAll_ProPlaTo_Schiene() {
        Set<String> result = new TreeSet<>();

        if (PROJECT.getElement() instanceof DmRq1SwCustomerProject_Leaf) {

            //
            // Build map to convert (internal) derivative to (external) schiene.
            //
            DmRq1SwCustomerProject_Leaf project = (DmRq1SwCustomerProject_Leaf) PROJECT.getElement();
            Map<String, String> derivateToSchiene = new TreeMap<>();
            for (Rq1LineEcuProject pstLine : project.getPstLines()) {
                if ((pstLine.getExternalName().isEmpty() == false) && (pstLine.getInternalName().isEmpty() == false)) {
                    derivateToSchiene.put(pstLine.getInternalName(), pstLine.getExternalName());
                }
            }

            //
            // Add all schiene for active derivatives
            //
            for (String derivative : getActiveDerivatives()) {
                if ((derivative != null) && derivative.trim().isEmpty() == false) {
                    if (derivateToSchiene.containsKey(derivative)) {
                        String schiene = derivateToSchiene.get(derivative);
                        if (schiene != null) {
                            //
                            // Check if schiene is a ProPlaTo schiene
                            //
                            String cluster = ((DmRq1SwCustomerProject_Leaf) PROJECT.getElement()).getClusterForProgrammstandsschiene(schiene);
                            if (cluster != null) {
                                result.add(schiene);
                            }
                        }
                    }

                }
            }
        }

        return result;
    }

    @Override
    final public Set<String> getAll_ProPlaTo_Hardware() {
        Set<String> result = new TreeSet<>();
        for (Rq1XmlTable_ProPlaToHardware.Record record : Rq1XmlTable_ProPlaToHardware.extract(PROPLATO_HARDWARE.getValue())) {
            String hardware = record.getHardware();
            if ((hardware != null) && hardware.trim().isEmpty() == false) {
                result.add(hardware.trim());
            }
        }
        return result;
    }

    @Override
    final public Map<String, String> getHardwareInformation() {
        Map<String, String> retVal = new HashMap<>();
        EcvTableData tableData = PROPLATO_HARDWARE.getValue();
        Rq1XmlTable_ProPlaToHardware desc = PROPLATO_HARDWARE.getTableDescription();
        for (EcvTableRow row : tableData.getRows()) {
            if (row.getValueAt(desc.DERIVAT) != null && row.getValueAt(desc.HARDWARE) != null) {
                retVal.put(row.getValueAt(desc.DERIVAT).toString(), row.getValueAt(desc.HARDWARE).toString());
            }
        }
        return retVal;
    }

    @Override
    final public Map<String, String> getHardwareInformationProPlaTo() {
        Map<String, String> retVal = new HashMap<>();
        EcvTableData tableData = PROPLATO_HARDWARE.getValue();
        Rq1XmlTable_ProPlaToHardware desc = PROPLATO_HARDWARE.getTableDescription();
        for (EcvTableRow row : tableData.getRows()) {
            if (row.getValueAt(desc.DERIVAT) != null && row.getValueAt(desc.HARDWARE) != null) {
                String derivativeLine = getProPlaToLineExternal(row.getValueAt(desc.DERIVAT).toString());
                retVal.put(derivativeLine, row.getValueAt(desc.HARDWARE).toString());
            }
        }
        return retVal;
    }

    @Override
    final public boolean isInProPlaToLine(String line) {
        DmRq1Field_DerivativesTable derivativeTable = this.DERIVATIVES_TABLE;
        EcvTableData dataTable = derivativeTable.getValue();
        for (EcvTableRow rowDeriv : dataTable.getRows()) {
            //Check if the Derivative matches with the searched line
            //Handle the Name difference between Lien and Derivative Naem
            String derivativeLine = getProPlaToLineExternal(rowDeriv.getValueAt(1).toString());
            if (derivativeLine != null) {
                if (derivativeLine.equals(line)
                        && !rowDeriv.getValueAt(0).equals("[E]") && !rowDeriv.getValueAt(0).equals("[]")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks the external description of the customer project and returns the
     * internal name of the given line
     *
     * @param line the name of the line on customer side
     * @return the line how it is displayed at Bosch side
     */
    private String getProPlaToLineExternal(String line) {
        DmRq1Project prj = this.PROJECT.getElement();
        if (prj instanceof DmRq1SwCustomerProject_Leaf) {
            DmRq1SwCustomerProject_Leaf cprj = (DmRq1SwCustomerProject_Leaf) prj;
            List<Rq1LineEcuProject> lines = cprj.PST_LINES_FROM_EXTERNAL_DESCRIPTION.getValue();
            for (Rq1LineEcuProject ecuLine : lines) {
                if (ecuLine.getInternalName().toString().equals(line)) {
                    return ecuLine.getExternalName();
                }
            }
        }
        return line;
    }

    /**
     * Checks the external description of the customer project and returns the
     * internal name of the given line
     *
     * @param externalName the name of the line on customer side
     * @return The line or lines how it is displayed at Bosch side
     */
    private List<String> getPstLineInternalNamesForExternalName(String externalName) {
        List<String> retval = new LinkedList<>();
        DmRq1Project prj = this.PROJECT.getElement();
        if (prj instanceof DmRq1SwCustomerProject_Leaf) {
            DmRq1SwCustomerProject_Leaf cprj = (DmRq1SwCustomerProject_Leaf) prj;
            List<Rq1LineEcuProject> lines = cprj.PST_LINES_FROM_EXTERNAL_DESCRIPTION.getValue();
            for (Rq1LineEcuProject ecuLine : lines) {
                if (ecuLine.getExternalName().toString().equals(externalName)) {
                    retval.add(ecuLine.getInternalName());
                }
            }
        }
        if (retval.isEmpty()) {
            retval.add(externalName);
        }
        return retval;
    }

    @Override
    final public boolean getProPlaToKennungMeta(String line) {
        DmRq1Field_DerivativesTable derivativeTable = this.DERIVATIVES_TABLE;
        EcvTableData dataTable = derivativeTable.getValue();
        for (EcvTableRow rowDeriv : dataTable.getRows()) {
            //Check if the Derivative matches with the searched line
            //Handle the Name difference between Lien and Derivative Naem
            String derivativeLine = getProPlaToLineExternal(rowDeriv.getValueAt(1).toString());
            if (derivativeLine.equals(line)) {
                return rowDeriv.getValueAt(0).equals("[P]");
            }
        }
        return false;
    }

    /**
     *
     * @return the META Line, if there is one null, if there is no one
     */
    @Override
    final public String getMetaSchiene() {
        DmRq1Field_DerivativesTable derivativeTable = this.DERIVATIVES_TABLE;
        EcvTableData dataTable = derivativeTable.getValue();
        for (EcvTableRow rowDeriv : dataTable.getRows()) {
            //If the derivative line is Pilot
            if (rowDeriv.getValueAt(0).equals("[P]")) {
                return (getProPlaToLineExternal(rowDeriv.getValueAt(1).toString()));
            }
        }
        return null;
    }

    /**
     * delivers the planned Date of the given Line
     *
     * @param line the line, which should be checked
     * @return the PlannedDate of the given Line if there is no Line, the
     * Planned Date of the PFAM
     */
    @Override
    final public EcvDate getPlannedDateForPstLineByExternalName(String line) {
        Rq1DerivativePlannedDates dates = this.PLANNED_DATE_DERIVATIVES.getValue();
        for (String help : this.getPstLineInternalNamesForExternalName(line)) {
            if (dates.getDateForDerivative(help) != null) {
                return dates.getDateForDerivative(help);
            }
        }
        return this.PLANNED_DATE.getValue();
    }

    /**
     * delivers the planned date of PVAR (Collection) and its derivatives
     *
     * @return all available planned dates (null if no date is given)
     */
    @Override
    final public TreeSet<EcvDate> getAllPlannedDates() {
        TreeSet<EcvDate> allDates = new TreeSet<>();
        EcvDate pvarPlannedDate = this.PLANNED_DATE.getValue();
        if (pvarPlannedDate == null) {
            allDates.add(EcvDate.getEmpty());
        } else {
            allDates.add(pvarPlannedDate);
        }

        Rq1DerivativePlannedDates dates = this.PLANNED_DATE_DERIVATIVES.getValue();
        Rq1DerivativeMapping derivatives = DERIVATIVES.getValue();
        Set<String> names = new TreeSet<>();
        if (derivatives != null && !derivatives.isEmpty()) {
            names = derivatives.getDerivativeNames();
        }

        int datesSize = 0;
        if (dates != null) {
            if (dates.getDates() != null && !dates.getDates().isEmpty()) {
                datesSize = dates.getDates().size();
                allDates.addAll(dates.getDates().values());
            }
        }
        int emptyDates = names.size() - datesSize;
        for (int i = 0; i < emptyDates; i++) {
            allDates.add(EcvDate.getEmpty());
        }

        return allDates;
    }

}
