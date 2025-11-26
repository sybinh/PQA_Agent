/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Rq1NodeDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Data.MetaData.Rq1MetadataExpression;
import Rq1Data.MetaData.Rq1MetadataExpressionFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gug2wi
 */
public class Rq1MetadataChoiceList extends Rq1Metadata {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Rq1MetadataChoiceList.class.getCanonicalName());

    /**
     * Container for a list of choices (strings) and the criteria for this list.
     */
    static public class ChoiceList {

        final private Map<String, String> criteriaList; // Criteria list for Name/Value
        final private List<Rq1MetadataExpression> expressions;
        final private List<String> values;

        private ChoiceList() {
            criteriaList = new TreeMap<>();
            values = new ArrayList<>(7);
            expressions = new ArrayList<>();
        }

        /**
         * Adds a criteria to the list.
         *
         * @param criteriaName Name of the variable.
         * @param criteriaValue Value of the variable.
         */
        private void addCriteria(String criteriaName, String criteriaValue) {
            assert (criteriaName != null);
            assert (criteriaName.trim().isEmpty() == false);
            assert (criteriaValue != null);
            criteriaList.put(criteriaName.trim(), criteriaValue);
        }

        /**
         * Adds a Expression to the list.
         *
         * @param expression The expression to be added.
         */
        private void addExpression(Rq1MetadataExpression expression) {
            assert (expression != null);
            expressions.add(expression);
        }

        private boolean matchCriteria(Map<String, String> testCriteria) {
            assert (testCriteria != null);
            //
            // A value in a criteria might contain several values, separated by comma.
            // Therefore, we need a specific implementation. It is not enougth to use equal().
            //
            for (Map.Entry<String, String> entry : criteriaList.entrySet()) {
                String testValue = testCriteria.get(entry.getKey().trim());
                boolean match = false;
                for (String criteriaValue : entry.getValue().split(",")) {
                    if (criteriaValue.trim().equals(testValue)) {
                        match = true;
                    }
                }
                if (match == false) {
                    return (false);
                }
            }

            return (true);
        }

        private void addChoice(String value) {
            assert (value != null);
            values.add(value);
        }

        private List<String> getValues() {
            return (values);
        }

        @Override
        public String toString() {
            return (criteriaList + "-" + values);
        }
    }

    private List<ChoiceList> choiceLists = null;

    public Rq1MetadataChoiceList() {
        super(Rq1NodeDescription.METADATA_CHOICELIST);
    }

    private List<String> getChoiceList(Map<String, String> criteria) {

        if (choiceLists == null) {
            choiceLists = scanChoiceList(VALUE.getDataModelValue());
        }

        for (ChoiceList list : choiceLists) {
            if (list.matchCriteria(criteria)) {
                return (list.getValues());
            }
        }

        return (new ArrayList<>(0));
    }

    /**
     * Reads the valid input values for a meta data choice list from RQ1.
     *
     * @param recordType Type of the RQ1 record that contains the field.
     * @param fieldName Name of the field in the RQ1 record for which the values
     * shall be provided.
     * @param criteriaString Criteria to select the list within the meta data
     * record.
     * @return The valid values for the given parameters.
     */
    static public List<String> getValidInputValues(Rq1RecordType recordType, Rq1AttributeName fieldName, String criteriaString) {
        assert (recordType != null);
        assert (fieldName != null);
        assert (criteriaString != null);

        //
        // Parse criterias
        //
        Map<String, String> criteria = new TreeMap<>();
        if (criteriaString.isEmpty() == false) {
            String[] expressions = criteriaString.trim().split(";");
            for (String expression : expressions) {
                String trimedExpression = expression.trim();
                String[] parts = trimedExpression.split("=");
                if (parts.length == 2) {
                    criteria.put(parts[0], parts[1]);
                } else {
                    LOGGER.warning("Error parsing criteriaString for " + recordType.getText() + "/" + fieldName.getName() + ": Invalid expression \"" + trimedExpression + "\".");
                }
            }
        }

        // Return a copy of the original list, because the caller might change the list after getting it.
        return (new ArrayList<>(getValidInputValues(recordType, fieldName, criteria)));
    }

    static private List<String> getValidInputValues(Rq1RecordType recordType, Rq1AttributeName fieldName, Map<String, String> criteria) {
        assert (recordType != null);
        assert (fieldName != null);
        assert (criteria != null);

        String metadataName = recordType.getOslcType() + "." + fieldName.getName();

        Rq1MetadataChoiceList metadata = (Rq1MetadataChoiceList) getMetaData(metadataName);
        if (metadata != null) {
            return (metadata.getChoiceList(criteria));
        }

        return (new ArrayList<>(0));
    }

    /**
     * Made static for test reasons only.
     *
     * @param value
     * @return
     */
    static List<ChoiceList> scanChoiceList(String value) {
        assert (value != null);
        Pattern p = Pattern.compile("((\\[(.*?)\\][\\r\\n])?([^\\[]*))", Pattern.DOTALL);
        Matcher m = p.matcher(value);
        List<ChoiceList> result = new ArrayList<>();
        while (m.find()) {
            ChoiceList list = scanSingleChoiceList(m.group(3), m.group(4));
            if (list != null) {
                boolean addList = true;
                for (Rq1MetadataExpression expression : list.expressions) {
                    if (expression.isReady() && !expression.evaluate()) {
                        addList = false;
                    } else {
                        //The expression is either not ready (Not Valid)
                        //or the expression is true so the list should not be 
                        //filted instead we move to the next expression
                        //for evaluation. 
                    }
                }
                if (addList) {
                    result.add(list);
                }
            }
        }

        return (result);
    }

    static private ChoiceList scanSingleChoiceList(String expressions, String values) {
        ChoiceList list = new ChoiceList();
        if (expressions != null && !expressions.isEmpty()) {
            for (String expression : expressions.split(";")) {
                String trimedExpression = expression.trim();
                List<Rq1MetadataExpression> expressionList = Rq1MetadataExpressionFactory.parseExpression(trimedExpression);
                if (!expressionList.isEmpty()) {
                    for (Rq1MetadataExpression rq1MetadataExpression : expressionList) {
                        if (rq1MetadataExpression.isReady()) {
                            list.addExpression(rq1MetadataExpression);
                        }
                    }
                } else if (trimedExpression.isEmpty() == false) {
                    //Parse Criteria (UI Filters)
                    if (trimedExpression.startsWith("<<") == false) {
                        String[] parts = trimedExpression.split("=");
                        if (parts.length == 2) {
                            list.addCriteria(parts[0], parts[1]);
                        } else {
                            LOGGER.warning("Error parsing metadata: Invalid expression \"" + trimedExpression + "\".");
                            LOGGER.warning("Expressions \"" + expressions + "\".");
                            Exception ex = new Exception("Error parsing metadata.");
                            LOGGER.log(Level.WARNING, "Stack trace for error.", ex);
                        }
                    }
                }
            }
        }

        if (values != null && !values.isEmpty()) {
            for (String value : values.split("\n")) {
                String trimmedValue = value.trim();
                if (trimmedValue.isEmpty() || trimmedValue.equals("''") || trimmedValue.startsWith("#") || trimmedValue.startsWith("{")) {
                    //Ignore Comment
                } else {
                    list.addChoice(trimmedValue);
                }

            }
        }

        return list.values.isEmpty() ? null : list;
    }
}
