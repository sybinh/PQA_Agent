/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmConstantField_Text;
import DataModel.Doors.Fields.DmDoorsField_TestResult;
import static DataModel.Doors.Records.DmDoorsObject_PTSA_1x.ATTRIBUTENAME_DOORS_OBJECT_TYPE;
import Doors.DoorsObject;
import java.util.Set;
import java.util.TreeSet;

public class DmDoorsObject_TestCase extends DmDoorsObject {

    final public DmConstantField_Text OBJECT_TYPE;

    final public DmConstantField_Text CLASSIC_TEST_ID;
    final public DmConstantField_Text COMMENT;
    final public DmConstantField_Text EXPECTED_RESULT;
    final public DmConstantField_Text MATURITY;
    final public DmConstantField_Text PREREQUISITE;
    final public DmConstantField_Text REVIEW_COMMENT;
    final public DmConstantField_Text STIMULI;
    final public DmConstantField_Text TEST_METHOD;
    final public DmConstantField_Text QUALIFIED_FOR_REGRESSION_TEST;

    final public DmDoorsField_TestResult TEST_RESULT;

    public DmDoorsObject_TestCase(DoorsObject doorsObject) {
        super(ElementType.TEST_CASE, doorsObject);
        assert (doorsObject != null);

        OBJECT_TYPE = extractUserDefinedField(ATTRIBUTENAME_DOORS_OBJECT_TYPE, "Object Type");

        TEST_METHOD = extractUserDefinedField("TestMethod");
        COMMENT = extractUserDefinedField("Comment");
        PREREQUISITE = extractUserDefinedField("Prerequisite");
        STIMULI = extractUserDefinedField("Stimuli");
        EXPECTED_RESULT = extractUserDefinedField("Expected Result");
        CLASSIC_TEST_ID = extractUserDefinedField("FT-Test ID");
        MATURITY = extractUserDefinedField("Maturity");
        REVIEW_COMMENT = extractUserDefinedField("ReviewComment");
        QUALIFIED_FOR_REGRESSION_TEST = extractUserDefinedField("Qualified for Regression Test");

        TEST_RESULT = new DmDoorsField_TestResult("Test Result");

        //
        // Fill table with test results
        //
        Set<String> fieldNames = new TreeSet<>(getFreeUserDefinedFieldNames());
        for (String fieldName : fieldNames) {
            String derivative = checkAndGetDerivative(fieldName, "Link to Test Documents");
            if (derivative != null) {
                TEST_RESULT.addTestDocument(derivative, extractUserDefinedFieldValue(fieldName));
            } else {
                derivative = checkAndGetDerivative(fieldName, "Testresult");
                if (derivative != null) {
                    TEST_RESULT.addTestResult(derivative, extractUserDefinedFieldValue(fieldName));
                }
            }

        }

        finishExtractionOfUserDefinedFields();
    }

    private String checkAndGetDerivative(String fieldName, String startsWith) {
        assert (fieldName != null);
        assert (startsWith != null);
        assert (startsWith.isEmpty() == false);

        if (fieldName.startsWith(startsWith) == true) {
            String derivative = fieldName.substring(startsWith.length()).trim();
            return (derivative);
        }
        return (null);
    }

}
