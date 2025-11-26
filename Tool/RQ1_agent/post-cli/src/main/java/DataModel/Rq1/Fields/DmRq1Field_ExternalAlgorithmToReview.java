/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmSourceField_Enumeration;
import DataModel.DmSourceField_Text;
import DataModel.DmValueFieldI_Enumeration_EagerLoad;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Data.Enumerations.ExternalAlgorithmsToReview;
import util.EcvEnumeration;
import util.EcvEnumerationValue;
import util.EcvXmlTextElement;

/**
 * Special field to handle the tags in external tags field.
 *
 * @author gug2wi
 */
public class DmRq1Field_ExternalAlgorithmToReview {

    final private String TAG_ALGORITHMS_TO_REVIEW = "AlgorithmsToReview";
    final private String TAG_ALGORITHMS_TO_REVIEW_COMMENT = "AlgorithmsToReview_Comment";

    public class AlgorithmField extends DmSourceField_Enumeration implements DmValueFieldI_Enumeration_EagerLoad {

        private AlgorithmField(String nameForUserInterface) {
            super(nameForUserInterface);
        }

        @Override
        public EcvEnumeration getValue() {
            readValues();
            return (valueAlgorithm);
        }

        @Override
        public void setValue(EcvEnumeration v) {
            valueAlgorithm = v;
            writeValues();
        }

        @Override
        public EcvEnumeration[] getValidInputValues() {
            getValue();
            return (EcvEnumerationValue.createValueList(ExternalAlgorithmsToReview.values(), valueAlgorithm));
        }

    }

    public class AlgorithmCommentField extends DmSourceField_Text {

        private AlgorithmCommentField(String nameForUserInterface) {
            super(nameForUserInterface);
        }

        @Override
        public String getValue() {
            readValues();
            return (valueComment);
        }

        @Override
        public void setValue(String v) {
            valueComment = v;
            writeValues();
        }

    }

    public class ExternalTagsField extends DmSourceField_Text {

        public ExternalTagsField(String nameForUserInterface) {
            super(nameForUserInterface);
        }

        @Override
        public String getValue() {
            readValues();
            return (valuesExternalTags);
        }

        @Override
        public void setValue(String v) {
            valuesExternalTags = v;
            writeValues();
        }

    }

    final private AlgorithmField fieldAlgorithm = new AlgorithmField("External Algorithms to Review");
    final private AlgorithmCommentField fieldComment = new AlgorithmCommentField("External Algorithms to Review Comment");
    final private ExternalTagsField fieldExternalTags = new ExternalTagsField("External Tags");

    private String valuesExternalTags;
    private EcvEnumeration valueAlgorithm;
    private String valueComment;

    final private Rq1FieldI_Text rq1ExternalTagsField;
    private String lastRq1Value;

    public DmRq1Field_ExternalAlgorithmToReview(Rq1FieldI_Text rq1ExternalTagsField) {
        assert (rq1ExternalTagsField != null);

        this.rq1ExternalTagsField = rq1ExternalTagsField;

        valueAlgorithm = null;
        valueComment = null;
        valuesExternalTags = null;

        lastRq1Value = null;
    }

    public AlgorithmField getAlgorithmField() {
        return fieldAlgorithm;
    }

    public AlgorithmCommentField getCommentField() {
        return fieldComment;
    }

    public ExternalTagsField getExternalTagsField() {
        return fieldExternalTags;
    }

    private void readValues() {
        String currentRq1Value = rq1ExternalTagsField.getDataModelValue();
        boolean changed = (lastRq1Value == null) ? (currentRq1Value != null) : !(lastRq1Value.equals(currentRq1Value));
        if (changed == true) {
            extractValues(currentRq1Value);
            lastRq1Value = currentRq1Value;
        }
    }

    private void extractValues(String rq1Value) {
        //
        // Handle null value
        //
        if (rq1Value == null) {
            valuesExternalTags = null;
            valueAlgorithm = null;
            valueComment = null;
            return;
        }

        //
        // Extract and remove A2R
        //
        String algStartTag = "<" + TAG_ALGORITHMS_TO_REVIEW + ">";
        String algEndTag = "</" + TAG_ALGORITHMS_TO_REVIEW + ">";
        int algStartIndex = rq1Value.indexOf(algStartTag);
        int algEndIndex = rq1Value.indexOf(algEndTag);
        if ((algStartIndex >= 0) && (algEndIndex > algStartIndex)) {
            String algText = rq1Value.substring(algStartIndex + algStartTag.length(), algEndIndex);
            valueAlgorithm = EcvEnumerationValue.createFromList(algText, ExternalAlgorithmsToReview.values());
            rq1Value = rq1Value.replaceFirst(algStartTag + algText + algEndTag, "");
            if ((rq1Value.length() > algStartIndex) && (rq1Value.charAt(algStartIndex) == '\n')) {
                // Remove newline after tag
                rq1Value = rq1Value.substring(0, algStartIndex) + rq1Value.substring(algStartIndex + 1, rq1Value.length());
            }
        } else {
            valueAlgorithm = null;
        }

        //
        // Extract and remove A2R-Comment
        //
        String commentStartTag = "<" + TAG_ALGORITHMS_TO_REVIEW_COMMENT + ">";
        String commentEndTag = "</" + TAG_ALGORITHMS_TO_REVIEW_COMMENT + ">";
        int commentStartIndex = rq1Value.indexOf(commentStartTag);
        int commentEndIndex = rq1Value.indexOf(commentEndTag);
        if ((commentStartIndex >= 0) && (commentEndIndex > commentStartIndex)) {
            valueComment = rq1Value.substring(commentStartIndex + commentStartTag.length(), commentEndIndex);
            rq1Value = rq1Value.replaceFirst(commentStartTag + valueComment + commentEndTag, "");
            if ((rq1Value.length() > commentStartIndex) && (rq1Value.charAt(commentStartIndex) == '\n')) {
                // Remove newline after tag
                rq1Value = rq1Value.substring(0, commentStartIndex) + rq1Value.substring(commentStartIndex + 1, rq1Value.length());
            }
        } else {
            valueComment = null;
        }

        //
        // Set rest
        //
        valuesExternalTags = rq1Value.trim();
    }

    private void writeValues() {
        String newRq1Value = combineValues();
        if (newRq1Value.toString().equals(lastRq1Value) == false) {
            rq1ExternalTagsField.setDataModelValue(newRq1Value.toString());
            lastRq1Value = newRq1Value.toString();
        }
    }

    // Module visible for test reasons
    String combineValues() {
        StringBuilder result = new StringBuilder();

        if ((valuesExternalTags != null) && (valuesExternalTags.isEmpty() == false)) {
            result.append(valuesExternalTags);
        }

        if ((valueAlgorithm != null) && (valueAlgorithm != ExternalAlgorithmsToReview.EMPTY)) {
            if (result.length() > 0) {
                result.append("\n");
            }
            result.append(new EcvXmlTextElement(TAG_ALGORITHMS_TO_REVIEW, valueAlgorithm.getText()).getXmlString());
        }

        if ((valueComment != null) && (valueComment.isEmpty() == false)) {
            if (result.length() > 0) {
                result.append("\n");
            }
            result.append(new EcvXmlTextElement(TAG_ALGORITHMS_TO_REVIEW_COMMENT, valueComment).getXmlString());
        }

        return (result.toString());
    }

}
