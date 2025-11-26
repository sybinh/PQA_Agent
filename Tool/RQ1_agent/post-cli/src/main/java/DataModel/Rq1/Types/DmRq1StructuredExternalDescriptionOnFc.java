/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author GUG2WI
 */
public class DmRq1StructuredExternalDescriptionOnFc {

    public static final String PATTERN_BLANK_OR_MINUS = "[ -]";
    public static final String PATTERN_TEXT_TILL_EOL = "\\s*(.*)\\n";
    public static final String PATTERN_FOR_END_OF_BLOCK = " *([^\\n]*)";

    public static final String TEXT_FOR_CHANGE_DESCRIPTION = "Change Description:";
    public static final String TEXT_FOR_REQUIREMENT_NEUTRALITY = "Requirement-Neutrality?:";
    private static final String PATTERN_FOR_REQUIREMENT_NEUTRALITY = "Requirement" + PATTERN_BLANK_OR_MINUS + "Neutrality?\\?:";
    public static final String TEXT_FOR_REQUIREMENT_NEUTRALITY_COMMENT = "Requirement-Neutrality Comment:";
    private static final String PATTERN_FOR_REQUIREMENT_NEUTRALITY_COMMENT = "Requirement" + PATTERN_BLANK_OR_MINUS + "Neutrality Comment:";
    public static final String TEXT_FOR_FUNCTIONAL_NEUTRALITY = "Functional-Neutrality?:";
    private static final String PATTERN_FOR_FUNCTIONAL_NEUTRALITY = "Functional" + PATTERN_BLANK_OR_MINUS + "Neutrality\\?:";
    public static final String TEXT_FOR_FUNCTIONAL_NEUTRALITY_COMMENT = "Functional-Neutrality Comment:";
    private static final String PATTERN_FOR_FUNCTIONAL_NEUTRALITY_COMMENT = "Functional" + PATTERN_BLANK_OR_MINUS + "Neutrality Comment:";
    public static final String TEXT_FOR_HEX_NEUTRALITY = "Hex-Neutrality?:";
    private static final String PATTERN_FOR_HEX_NEUTRALITY = "Hex-Neutrality\\?:";
    public static final String TEXT_FOR_IMPACT_ON_HEX_NEUTRALITY_COMMENT = "Impact on Hex-Neutrality Comment:";
    private static final String PATTERN_FOR_IMPACT_ON_HEX_NEUTRALITY_COMMENT = "(?:Impact on )?Hex-Neutrality Comment:";
    public static final String TEXT_FOR_IMPACT_ON_HEX_NEUTRALITY = "Impact on Hex-Neutrality?:";
    private static final String PATTERN_FOR_IMPACT_ON_HEX_NEUTRALITY = "Impact on Hex-Neutrality\\?:";

    private static final String PATTERN = "([\\s\\S]*?)"
            + TEXT_FOR_CHANGE_DESCRIPTION + PATTERN_TEXT_TILL_EOL                           // 2
            + PATTERN_FOR_REQUIREMENT_NEUTRALITY + PATTERN_TEXT_TILL_EOL                    // 3
            + PATTERN_FOR_REQUIREMENT_NEUTRALITY_COMMENT + PATTERN_TEXT_TILL_EOL            // 4
            + PATTERN_FOR_FUNCTIONAL_NEUTRALITY + PATTERN_TEXT_TILL_EOL                     // 5
            + PATTERN_FOR_FUNCTIONAL_NEUTRALITY_COMMENT + PATTERN_TEXT_TILL_EOL             // 6
            + "(" + PATTERN_FOR_HEX_NEUTRALITY + PATTERN_TEXT_TILL_EOL + ")?"               // 8
            + "(" + PATTERN_FOR_IMPACT_ON_HEX_NEUTRALITY + PATTERN_TEXT_TILL_EOL + ")?"     // 10
            + PATTERN_FOR_IMPACT_ON_HEX_NEUTRALITY_COMMENT + PATTERN_FOR_END_OF_BLOCK;           // 11

    public static class Record {

        private final String changeDescription;
        private final String requirementNeutrality;
        private final String requirementNeutralityComment;
        private final String functionalNeutrality;
        private final String functionalNeutralityComment;
        private final String hexNeutrality;
        private final String impactOnHexNeutrality;
        private final String impactOnHexNeutralityComment;

        public Record(String changeDescription,
                String requirementNeutrality, String requirementNeutralityComment,
                String functionalNeutrality, String functionalNeutralityComment,
                String hexNeutrality, String impactOnHexNeutrality, String impactOnHexNeutralityComment) {
            this.changeDescription = changeDescription;
            this.requirementNeutrality = requirementNeutrality;
            this.requirementNeutralityComment = requirementNeutralityComment;
            this.functionalNeutrality = functionalNeutrality;
            this.functionalNeutralityComment = functionalNeutralityComment;
            if (impactOnHexNeutrality == null || impactOnHexNeutrality.isEmpty()) {
                this.hexNeutrality = hexNeutrality;
            } else {
                this.hexNeutrality = "";
            }
            this.impactOnHexNeutrality = impactOnHexNeutrality;
            this.impactOnHexNeutralityComment = impactOnHexNeutralityComment;
        }

        public String getChangeDescription() {
            return changeDescription;
        }

        public String getRequirementNeutrality() {
            return requirementNeutrality;
        }

        public String getRequirementNeutralityComment() {
            return requirementNeutralityComment;
        }

        public String getFunctionalNeutrality() {
            return functionalNeutrality;
        }

        public String getFunctionalNeutralityComment() {
            return functionalNeutralityComment;
        }

        public String getHexNeutrality() {
            return hexNeutrality;
        }

        public String getImpactOnHexNeutrality() {
            return impactOnHexNeutrality;
        }

        public String getImpactOnHexNeutralityComment() {
            return impactOnHexNeutralityComment;
        }

        public String getTextForExternalDescriptionField() {
            StringBuilder builder = new StringBuilder(100);
            builder.append(TEXT_FOR_CHANGE_DESCRIPTION).append(" ").append(changeDescription).append("\n");
            builder.append(TEXT_FOR_REQUIREMENT_NEUTRALITY).append(" ").append(requirementNeutrality).append("\n");
            builder.append(TEXT_FOR_REQUIREMENT_NEUTRALITY_COMMENT).append(" ").append(requirementNeutralityComment).append("\n");
            builder.append(TEXT_FOR_FUNCTIONAL_NEUTRALITY).append(" ").append(functionalNeutrality).append("\n");
            builder.append(TEXT_FOR_FUNCTIONAL_NEUTRALITY_COMMENT).append(" ").append(functionalNeutralityComment).append("\n");
            if (hexNeutrality != null && hexNeutrality.isBlank() == false) {
                builder.append(TEXT_FOR_HEX_NEUTRALITY).append(" ").append(hexNeutrality).append("\n");
            }
            builder.append(TEXT_FOR_IMPACT_ON_HEX_NEUTRALITY).append(" ").append(impactOnHexNeutrality).append("\n");
            builder.append(TEXT_FOR_IMPACT_ON_HEX_NEUTRALITY_COMMENT).append(" ").append(impactOnHexNeutralityComment);
            return (builder.toString());
        }

        public boolean isEmpty() {
            return (isEmpty(changeDescription)
                    && isEmpty(requirementNeutrality)
                    && isEmpty(requirementNeutralityComment)
                    && isEmpty(functionalNeutrality)
                    && isEmpty(functionalNeutralityComment)
                    && isEmpty(hexNeutrality)
                    && isEmpty(impactOnHexNeutrality)
                    && isEmpty(impactOnHexNeutralityComment));
        }

        private boolean isEmpty(String s) {
            if (s == null || s.isEmpty()) {
                return (true);
            } else {
                return (false);
            }
        }

    }

    public static class FieldContentEntry {

        private final String textBefore;
        private final Record structuredData;

        public FieldContentEntry(String textBefore, Record structuredData) {
            this.textBefore = textBefore;
            this.structuredData = structuredData;
        }

        public String getTextBefore() {
            return textBefore;
        }

        public Record getStructuredData() {
            return structuredData;
        }
    }

    public static class FieldContent {

        private final List<FieldContentEntry> content = new ArrayList<>();
        private String rest;

        public FieldContent() {
        }

        public void add(FieldContentEntry entry) {
            assert (entry != null);
            content.add(entry);
        }

        public void setRest(String rest) {
            this.rest = rest;
        }

        public List<FieldContentEntry> getContent() {
            return content;
        }

        public String getRest() {
            return rest;
        }

        public static FieldContent parseTextFromExternalDescriptionField(String fieldContent) {

            FieldContent result = new FieldContent();

            if ((fieldContent != null) && (fieldContent.trim().isEmpty() == false)) {

                Pattern pattern = Pattern.compile(PATTERN);
                Matcher matcher = pattern.matcher(fieldContent);
                matcher.useAnchoringBounds(false);
                boolean anythingFound = false;
                int endPosition = 0;

                while (matcher.find() == true) {
                    anythingFound = true;

                    
                    String fullMatch = matcher.group(0);
                    String textBefore = matcher.group(1);
                    String changeDescription = matcher.group(2);
                    String requirementNeutrality = matcher.group(3);
                    String requirementNeutralityComment = matcher.group(4);
                    String functionalNeutrality = matcher.group(5);
                    String impactOnHexNeutralityComment = (matcher.groupCount() >= 11) ? matcher.group(11) : "";

                    //
                    // Due to some optional  values sometimes the wrong values would be written into the following fields,
                    // so the writing of these values is a bit complex
                    //
                    String functionalNeutralityComment = matcher.group(6);
                    String hexNeutrality = "";
                    String impactOnHexNeutrality = "";
                    
                    if(matcher.group(6) != null && matcher.group(6).contains(TEXT_FOR_HEX_NEUTRALITY)) {
                        if (matcher.group(6).contains(TEXT_FOR_IMPACT_ON_HEX_NEUTRALITY)) {
                            impactOnHexNeutrality = matcher.group(6).substring(TEXT_FOR_IMPACT_ON_HEX_NEUTRALITY.length() + 1);
                        }
                        else {
                            hexNeutrality = matcher.group(6).substring(TEXT_FOR_HEX_NEUTRALITY.length() + 1);
                        }
                        functionalNeutralityComment = "";
                    }
                    else if (matcher.group(7) != null && matcher.group(7).contains(TEXT_FOR_HEX_NEUTRALITY)) {
                        if(matcher.group(7).contains(TEXT_FOR_IMPACT_ON_HEX_NEUTRALITY) == false) {
                            hexNeutrality = matcher.group(8);
                        }
                        else {
                            impactOnHexNeutrality = matcher.group(8).substring(TEXT_FOR_IMPACT_ON_HEX_NEUTRALITY.length() + 1);
                        }
                    }
                    
                    if(matcher.group(9) != null && matcher.group(9).contains(TEXT_FOR_IMPACT_ON_HEX_NEUTRALITY)){
                        impactOnHexNeutrality = matcher.group(10);
                    }
                    
                    if(impactOnHexNeutrality != null && impactOnHexNeutrality.isEmpty() == false) {
                        hexNeutrality = "";
                    }
                    
                    endPosition = matcher.end();
                    
                    Record structuredPart = new Record(
                            changeDescription,
                            requirementNeutrality, requirementNeutralityComment,
                            functionalNeutrality, functionalNeutralityComment,
                            hexNeutrality, impactOnHexNeutrality, impactOnHexNeutralityComment);
                    FieldContentEntry entry = new FieldContentEntry(textBefore, structuredPart);
                    result.add(entry);
                }

                if (anythingFound == true) {
                    String rest = fieldContent.substring(endPosition);
                    result.setRest(rest);
                    return (result);
                } else {
                    result.setRest(fieldContent);
                }
            }

            return (result);
        }

        public String buildTextForExternalDescriptionField() {
            StringBuilder builder = new StringBuilder();

            boolean isFirst = true;
            for (FieldContentEntry entry : content) {
                if (entry.textBefore != null && entry.textBefore.isEmpty() == false) {
                    builder.append(entry.textBefore);
                } else {
                    if (isFirst == false) {
                        builder.append("\n\n");
                    }
                }
                isFirst = false;
                builder.append(entry.structuredData.getTextForExternalDescriptionField());
                
            }
            
            if (rest != null && rest.isEmpty() == false) {
                if (content.isEmpty() == false) {
                    builder.append("\n");
                }
                builder.append(rest);
            }

            return (builder.toString());
        }
    }
}
