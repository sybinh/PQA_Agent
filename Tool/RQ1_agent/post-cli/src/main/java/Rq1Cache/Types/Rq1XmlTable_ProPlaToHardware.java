/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import util.EcvTableRow;

/**
 * <p>
 * - Encoding and decoding of the hardware field in the ProPlaTo-Tags
 * </p>
 * <p>
 * - Facility methods to access the hardware settings for ProPlaTo.
 * </p>
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_ProPlaToHardware extends Rq1XmlTable {

    //
    // Example of ProPlaTo-Tag for PVAR:
    //
    //    <ProPlaTo>
    //        <Hardware Derivat="DMG1001A01C1349">MG1CS001-9.1</Hardware>
    //        <Hardware Derivat="DMG1001A01C1296">MG1CS001-9.1</Hardware>
    //        <Hardware Derivat="DMG1001A01C1398">MG1CS001-9.1</Hardware>
    //        <Programmstandskennung>Funktional</Programmstandskennung>
    //    </ProPlaTo>
    //
    final public Rq1XmlTableColumn_String DERIVAT;
    final public Rq1XmlTableColumn_String HARDWARE;

    public Rq1XmlTable_ProPlaToHardware() {
        addXmlColumn(DERIVAT = new Rq1XmlTableColumn_String("Derivative", 15, "Derivat", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(HARDWARE = new Rq1XmlTableColumn_String("Hardware", 10, "-", ColumnEncodingMethod.CONTENT));

//         setMaxRowCount(100);
    }

    /**
     * Extracts a map that contains the hardware assigned to derivatives from
     * the given table data.
     *
     * @param data Table data that contain the mapping.
     * @return Map with the derivate name as key and the hardware string as
     * value.
     */
    final public Map<String, String> createHardwareMap(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_ProPlaToHardware);

        Map<String, String> map = new TreeMap<>();

        for (EcvTableRow row : data.getRows()) {
            String derivateName = row.getValueAt(DERIVAT).toString();
            assert (derivateName != null);
            String hardware = row.getValueAt(HARDWARE).toString();
            if (hardware != null) {
                map.put(derivateName, hardware);
            }
        }

        return (map);
    }

    /**
     * Creates data based on the given hardware map.
     *
     * @param map
     * @return Table data containing the settings from the given map.
     */
    final public EcvTableData setHardware(Map<String, String> map) {
        assert (map != null);

        EcvTableData data = createTableData();

        for (Entry<String, String> entry : map.entrySet()) {
            EcvTableRow row = data.createRow();
            DERIVAT.setValue(row, entry.getKey());
            HARDWARE.setValue(row, entry.getValue());
            data.addRow(row);
        }

        return (data);
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    static public class Record {

        final private String derivative;
        final private String hardware;

        public Record(String derivative, String hardware) {
            assert (derivative != null);

            this.derivative = derivative.trim();
            this.hardware = hardware != null ? hardware.trim() : "";
        }

        public String getDerivative() {
            return derivative;
        }

        public String getHardware() {
            return hardware;
        }

    }

    static public List<Record> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_ProPlaToHardware);

        Rq1XmlTable_ProPlaToHardware d = (Rq1XmlTable_ProPlaToHardware) data.getDescription();

        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            String derivate = (String) row.getValueAt(d.DERIVAT);
            String hw = (String) row.getValueAt(d.HARDWARE);
            result.add(new Record(derivate, hw));
        }
        return (result);
    }

}
