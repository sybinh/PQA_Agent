/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient.Exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.EcvXmlContainerElement;

/**
 *
 * @author gug2wi
 */
public class ConstraintViolationReportedByServer extends WriteToDatabaseRejected {

    static public class FieldConstraintViolation {

        final private String fieldName;
        final private String message;

        public FieldConstraintViolation(String fieldName, String message) {
            assert (fieldName != null);
            assert (fieldName.isEmpty() == false);
            assert (message != null);
            this.fieldName = fieldName;
            this.message = message;

        }

        public String getFieldName() {
            return (fieldName);
        }

        public String getMessage() {
            return (message);
        }

        @Override
        public String toString() {
            return (fieldName + "-" + message);
        }
    }
    //
    final private int statusCode;
    final private EcvXmlContainerElement xmlServerResponse;
    final private String topMessage;
    final private List<FieldConstraintViolation> invalidFields;
    private String detailedMessage;

    public ConstraintViolationReportedByServer(int statusCode, EcvXmlContainerElement xmlServerResponse, String topMessage, List<FieldConstraintViolation> fields) {
        assert (topMessage != null);
        assert (topMessage.isEmpty() == false);
        assert (fields != null);

        this.statusCode = statusCode;
        this.xmlServerResponse = xmlServerResponse;
        this.topMessage = topMessage;
        this.invalidFields = fields;
        this.detailedMessage = null;
    }

    public ConstraintViolationReportedByServer(int statusCode, EcvXmlContainerElement xmlServerResponse, String topMessage, String detailedMessage) {
        assert (topMessage != null);
        assert (topMessage.isEmpty() == false);
        assert (detailedMessage != null);
        assert (detailedMessage.isEmpty() == false);

        this.statusCode = statusCode;
        this.xmlServerResponse = xmlServerResponse;
        this.topMessage = topMessage;
        this.invalidFields = new ArrayList<>();
        this.detailedMessage = detailedMessage;
    }

    public ConstraintViolationReportedByServer(int statusCode, EcvXmlContainerElement xmlServerResponse, String topMessage, Set<String> detailedMessages) {
        assert (topMessage != null);
        assert (topMessage.isEmpty() == false);
        assert (detailedMessages != null);
        assert (detailedMessages.isEmpty() == false);

        this.statusCode = statusCode;
        this.xmlServerResponse = xmlServerResponse;
        this.topMessage = topMessage;
        this.invalidFields = extractFields(detailedMessages);
        if (this.invalidFields.isEmpty()) {
            StringBuilder b = new StringBuilder();
            for (String detailedMessage : detailedMessages) {
                if (detailedMessage != null) {
                    if (b.length() > 0) {
                        b.append("\n");
                    }
                    b.append(detailedMessage);
                }
            }
            this.detailedMessage = b.toString();
        } else {
            this.detailedMessage = null;
        }
    }

    @Override
    public String getMessageForUi() {
        return (topMessage);
    }

    @Override
    public String getMessage() {
        StringBuilder b = new StringBuilder(100);
        b.append(topMessage).append("\n");
        b.append("Status Code: ").append(Integer.toString(statusCode)).append("\n");
        if (xmlServerResponse != null) {
            b.append(xmlServerResponse.getUiString_WithoutContainer());
        } else {
            b.append(getDetailedMessageForUi());
        }
        return (b.toString());
    }

    @Override
    public String getDetailedMessageForUi() {
        if (detailedMessage == null) {
            StringBuilder b = new StringBuilder(100);
            for (FieldConstraintViolation field : invalidFields) {
                if (b.length() > 0) {
                    b.append("\n");
                }
                b.append("Field ").append(field.getFieldName()).append(":\n");
                b.append("      ").append(field.getMessage());
            }
            detailedMessage = b.toString();
        }
        return (detailedMessage);
    }

    public List<FieldConstraintViolation> getInvalidFields() {
        return invalidFields;
    }

    final private static String PATTERN_CURRENTLY_READONLY = "The field \"(?<name>\\w+)\" cannot be modified because it is currently read-only.";

    static List<FieldConstraintViolation> extractFields(Set<String> detailedMessages) {
        List<FieldConstraintViolation> result = new ArrayList<>();
        if (detailedMessages != null) {
            for (String detailedMessage : detailedMessages) {
                if (detailedMessage != null) {
                    Pattern pattern = Pattern.compile(PATTERN_CURRENTLY_READONLY);
                    Matcher matcher = pattern.matcher(detailedMessage);
                    while (matcher.find()) {
                        String field = matcher.group("name");
                        result.add(new FieldConstraintViolation(field, "Cannot be modified because it is currently read-only."));
                    }
                }
            }
        }
        return (result);
    }

}
