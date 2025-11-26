/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Fields;

import DataModel.DmSourceField_ReadOnly;
import DataModel.Doors.Types.DmDoorsTable_TestResult.Record;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author GUG2WI
 */
public class DmDoorsField_TestResult extends DmSourceField_ReadOnly<List<Record>> {

    final private Set<String> derivatives = new TreeSet<>();
    final private Map<String, String> testResults = new TreeMap<>();
    final private Map<String, String> testDocuments = new TreeMap<>();

    public DmDoorsField_TestResult(String nameForUserInterface) {
        super(nameForUserInterface);
    }

    @Override
    public List<Record> getValue() {
        List<Record> result = new ArrayList<>();
        for (String derivative : derivatives) {
            result.add(new Record(derivative, testDocuments.get(derivative), testResults.get(derivative)));
        }
        return (result);
    }

    public void addTestResult(String derivative, String testResult) {
        assert (derivative != null);
        derivatives.add(derivative);
        testResults.put(derivative, testResult);
    }

    public void addTestDocument(String derivative, String testDocument) {
        assert (derivative != null);
        derivatives.add(derivative);
        testDocuments.put(derivative, testDocument);
    }

}
