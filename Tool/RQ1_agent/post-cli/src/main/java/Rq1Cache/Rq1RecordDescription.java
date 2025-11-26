/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache;

import java.util.ArrayList;
import java.util.List;

public interface Rq1RecordDescription {

    static public class FixedRecordValue {

        final private String fieldName;
        final private String fixedValue;

        public FixedRecordValue(String name, String value) {
            assert (name != null);
            assert (name.isEmpty() == false);
            assert (value != null);
            assert (value.isEmpty() == false);

            fieldName = name;
            fixedValue = value;
        }

        public String getFieldName() {
            return (fieldName);
        }

        public String getValue() {
            return (fixedValue);
        }

        public boolean isOnlyOneValueAllowed() {
            return (fixedValue.charAt(0) != '(');
        }

        public List<String> getValues() {
            return (splitValues(fixedValue));
        }

        public boolean matchWithValue(String value) {
            return (value.matches(fixedValue));
        }

        static public FixedRecordValue[] create(String fixedRecordValueString) {
            assert (fixedRecordValueString != null);

            FixedRecordValue[] fixedRecordValues;

            if (fixedRecordValueString.isEmpty() == false) {
                String[] nameValueList = fixedRecordValueString.split(",");
                fixedRecordValues = new FixedRecordValue[nameValueList.length];
                for (int i = 0; i < nameValueList.length; i++) {
                    String[] s = nameValueList[i].split("=");
                    if (s.length != 2) {
                        throw (new Error("Invalid fixed values: \"" + fixedRecordValueString + "\""));
                    }
                    fixedRecordValues[i] = new FixedRecordValue(s[0], s[1]);
                }
            } else {
                fixedRecordValues = new FixedRecordValue[0];
            }

            return (fixedRecordValues);
        }

        /**
         * Made static public to enable testing.
         *
         * @param values
         * @return
         */
        static public List<String> splitValues(String values) {
            assert (values != null);

            List<String> result = new ArrayList<>();
            for (String v : values.split("[\\(\\|\\)]")) {
                if (v.isEmpty() == false) {
                    result.add(v);
                }
            }

            return (result);
        }

        @Override
        public String toString() {
            return (fieldName + "=" + fixedValue);
        }
    }

    public Rq1RecordType getRecordType();

    public FixedRecordValue[] getFixedRecordValues();
}
