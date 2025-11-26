/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import util.EcvTableData;
import util.EcvTableRow;

public class Rq1XmlTable_MissingPlanningForOpt extends Rq1XmlTable {

    private static final Logger LOGGER = Logger.getLogger(Rq1XmlTable_MissingPlanningForOpt.class.getCanonicalName());

    final private Rq1XmlTableColumn_String BC_NAME;
    final private Rq1XmlTableColumn_String BC_VERSION;
    final private Rq1XmlTableColumn_String FC_NAME;
    final private Rq1XmlTableColumn_String FC_VERSION;

    public Rq1XmlTable_MissingPlanningForOpt() {
        addXmlColumn(BC_NAME = new Rq1XmlTableColumn_String("BC-Name", 10, "bcName", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(BC_VERSION = new Rq1XmlTableColumn_String("BC-Version", 10, "bcVersion", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(FC_NAME = new Rq1XmlTableColumn_String("FC-Name", 10, "fcName", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(FC_VERSION = new Rq1XmlTableColumn_String("FC-Version", 10, "fcVersion", ColumnEncodingMethod.ATTRIBUTE));
    }

    //--------------------------------------------------------------------------
    //
    // Record based access
    //
    //--------------------------------------------------------------------------
    public static class Value {

        final private String bcName;
        final private String bcVersion;
        final private String fcName;
        final private String fcVersion;

        public Value(String bcName, String bcVersion, String fcName, String fcVersion) {
            this.bcName = bcName;
            this.bcVersion = bcVersion;
            this.fcName = fcName;
            this.fcVersion = fcVersion;
        }

        public String getBcName() {
            return bcName;
        }

        public String getBcVersion() {
            return bcVersion;
        }

        public String getFcName() {
            return fcName;
        }

        public String getFcVersion() {
            return fcVersion;
        }
        
        

    }

    public List<Value> extract(EcvTableData data) {
        assert (data != null);
        assert (data.getDescription() instanceof Rq1XmlTable_MissingPlanningForOpt);

        List<Value> result = new ArrayList<>();

        for (EcvTableRow row : data.getRows()) {
            row.getValueAt(0);
            result.add(new Value(BC_NAME.getValue(row), BC_VERSION.getValue(row), FC_NAME.getValue(row), FC_VERSION.getValue(row)));
        }
        return (result);
    }

    public EcvTableData pack(List<Value> values) {
        assert (values != null);

        EcvTableData result = createTableData();
        for (Value value : values) {
            if (value != null) {
                EcvTableRow row = result.createAndAddRow();
                BC_NAME.setValue(row, value.bcName);
                BC_VERSION.setValue(row, value.bcVersion);
                FC_NAME.setValue(row, value.fcName);
                FC_VERSION.setValue(row, value.fcVersion);
            }
        }
        return (result);
    }
}
