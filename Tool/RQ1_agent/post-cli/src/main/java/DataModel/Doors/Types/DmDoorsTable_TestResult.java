/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Types;

import DataModel.DmFieldI;
import DataModel.DmValueFieldI;
import DataModel.UiSupport.DmUiTableSource;
import java.util.List;
import util.EcvTableColumn_String;
import util.EcvTableData;
import util.EcvTableDescription;
import util.EcvTableRow;

/**
 *
 * @author GUG2WI
 */
public class DmDoorsTable_TestResult extends EcvTableDescription implements DmUiTableSource {

    public static class Record {

        final private String derivative;
        final private String testDocuments;
        final private String testResult;

        public Record(String derivative, String testDocuments, String testResult) {
            this.derivative = derivative != null ? derivative : "";
            this.testDocuments = testDocuments != null ? testDocuments : "";
            this.testResult = testResult != null ? testResult : "";
        }

        public String getDerivative() {
            return derivative;
        }

        public String getTestDocuments() {
            return testDocuments;
        }

        public String getTestResult() {
            return testResult;
        }

    }

    final public EcvTableColumn_String DERIVATIVE;
    final public EcvTableColumn_String TEST_DOCUMENTS;
    final public EcvTableColumn_String TEST_RESULT;

    final private DmValueFieldI<List<Record>> resultField;

    public DmDoorsTable_TestResult(DmValueFieldI<List<Record>> resultField) {
        assert (resultField != null);

        this.resultField = resultField;

        addIpeColumn(DERIVATIVE = new EcvTableColumn_String("Derivative", 12));
        addIpeColumn(TEST_RESULT = new EcvTableColumn_String("Testresult", 12));
        addIpeColumn(TEST_DOCUMENTS = new EcvTableColumn_String("TestDocuments", 12));
    }

    @Override
    public DmFieldI getDmField() {
        return (resultField);
    }

    @Override
    public EcvTableDescription getTableDescription() {
        return (this);
    }

    @Override
    public boolean useLazyLoad() {
        return (false);
    }

    @Override
    public EcvTableData getValue() {
        EcvTableData tableData = super.createTableData();
        for (Record record : resultField.getValue()) {
            EcvTableRow row = tableData.createAndAddRow();
            DERIVATIVE.setValue(row, record.getDerivative());
            TEST_RESULT.setValue(row, record.getTestResult());
            TEST_DOCUMENTS.setValue(row, record.testDocuments);
        }
        return (tableData);
    }

    @Override
    public void setValue(EcvTableData newData) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
