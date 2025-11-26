/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.List;
import util.EcvTableData;
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
public class Rq1XmlTable_ProPlaToProjectPstRail extends Rq1XmlTable {

    //
    // Example of ProPlaTo-Tag for PVAR:
    //
    //  <ProPlaTo>
    //      <PSTSchiene Name="DMG1001A01P1296">
    //         <Projektcluster>Audi EA888</Projektcluster>
    //         <Fahrzeug></Fahrzeug>
    //         <Motor></Motor>
    //         <Getriebe></Getriebe>
    //         <Abgasstandard></Abgasstandard>
    //         <Eigenschaften></Eigenschaften>
    //         <Hardware></Hardware>
    //      </PSTSchiene>
    //      <PSTSchiene Name="DMG1001A01P1349">
    //         <Projektcluster>Audi EA888</Projektcluster>
    //         <Fahrzeug></Fahrzeug>
    //         <Motor></Motor>
    //         <Getriebe></Getriebe>
    //         <Abgasstandard></Abgasstandard>
    //         <Eigenschaften></Eigenschaften>
    //         <Hardware></Hardware>
    //       </PSTSchiene>
    //    </ProPlaTo>
    //
    final public Rq1XmlTableColumn_String RAIL;
    final public Rq1XmlTableColumn_String CLUSTER;
    final public Rq1XmlTableColumn_String AUTOMOTIVE;
    final public Rq1XmlTableColumn_String ENGINE;
    final public Rq1XmlTableColumn_String TRANSMISSION;
    final public Rq1XmlTableColumn_String EXHAUSTSTANDARD;
    final public Rq1XmlTableColumn_String PROPERTIES;
    final public Rq1XmlTableColumn_String HARDWARE;

    public Rq1XmlTable_ProPlaToProjectPstRail() {
        addXmlColumn(RAIL = new Rq1XmlTableColumn_String("Programmstandsschiene", 15, "Name", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(CLUSTER = new Rq1XmlTableColumn_String("Projektcluster", 10, "Projektcluster", ColumnEncodingMethod.ELEMENT_CONTENT));
        CLUSTER.setOptional();
        addXmlColumn(AUTOMOTIVE = new Rq1XmlTableColumn_String("Fahrzeug", 10, "Fahrzeug", ColumnEncodingMethod.ELEMENT_CONTENT));
        AUTOMOTIVE.setOptional();
        addXmlColumn(ENGINE = new Rq1XmlTableColumn_String("Motor", 10, "Motor", ColumnEncodingMethod.ELEMENT_CONTENT));
        ENGINE.setOptional();
        addXmlColumn(TRANSMISSION = new Rq1XmlTableColumn_String("Getriebe", 10, "Getriebe", ColumnEncodingMethod.ELEMENT_CONTENT));
        TRANSMISSION.setOptional();
        addXmlColumn(EXHAUSTSTANDARD = new Rq1XmlTableColumn_String("Abgasstandard", 10, "Abgasstandard", ColumnEncodingMethod.ELEMENT_CONTENT));
        EXHAUSTSTANDARD.setOptional();
        addXmlColumn(PROPERTIES = new Rq1XmlTableColumn_String("Eigenschaften", 10, "Eigenschaften", ColumnEncodingMethod.ELEMENT_CONTENT));
        PROPERTIES.setOptional();
        addXmlColumn(HARDWARE = new Rq1XmlTableColumn_String("Hardware", 10, "Hardware", ColumnEncodingMethod.ELEMENT_CONTENT));
        HARDWARE.setOptional();

//        setMaxRowCount(30);
    }

    //--------------------------------------------------------------------------
    //
    // Support for record based access
    //
    //--------------------------------------------------------------------------
    static public class Record {

        final private String schiene;
        final private String cluster;
        final private String fahrzeug;
        final private String motor;
        final private String getriebe;
        final private List<String> abgasStandard;
        final private String eigenschaften;
        final private String hardware;

        @SuppressWarnings("unchecked")
        private Record(EcvTableData data, EcvTableRow row) {
            assert (data != null);
            assert (row != null);

            Rq1XmlTable_ProPlaToProjectPstRail d = (Rq1XmlTable_ProPlaToProjectPstRail) data.getDescription();

            schiene = (String) row.getValueAt(d.RAIL);
            cluster = (String) row.getValueAt(d.CLUSTER);
            fahrzeug = (String) row.getValueAt(d.AUTOMOTIVE);
            motor = (String) row.getValueAt(d.ENGINE);
            getriebe = (String) row.getValueAt(d.TRANSMISSION);

            String abgasStandardString = ((String) row.getValueAt(d.EXHAUSTSTANDARD));
            abgasStandard = new ArrayList<>();
            if (abgasStandardString != null) {
                for (String s : abgasStandardString.split(",")) {
                    abgasStandard.add(s.trim());
                }
            }

            eigenschaften = (String) row.getValueAt(d.PROPERTIES);
            hardware = (String) row.getValueAt(d.HARDWARE);
        }

        public String getSchiene() {
            return schiene;
        }

        public String getCluster() {
            return cluster;
        }

        public String getFahrzeug() {
            return fahrzeug;
        }

        public String getMotor() {
            return motor;
        }

        public String getGetriebe() {
            return getriebe;
        }

        public List<String> getAbgasStandard() {
            return abgasStandard;
        }

        public String getEigenschaften() {
            return eigenschaften;
        }

        public String getHardware() {
            return hardware;
        }

    }

    static public List<Record> toRecordList(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_ProPlaToProjectPstRail);

        List<Record> result = new ArrayList<>();
        for (EcvTableRow row : data.getRows()) {
            result.add(new Record(data, row));
        }
        return (result);
    }

}
