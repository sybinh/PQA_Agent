/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import ConfigurableRules.ConfigRule.Monitoring.ConfigurableRuleManagerRule_DoorsObject;
import DataModel.DmConstantField_Text;
import Doors.DoorsObject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DmDoorsObject_PTSA_1x_L1_REQ extends DmDoorsObject_PTSA_1x {

    final public DmConstantField_Text ACCEPTANCE_CRITERIA;
    final public DmConstantField_Text AFFECTED_COMPONENT_PS_EC;
    final public DmConstantField_Text ALLOCATION;
    final public DmConstantField_Text CRQ_PS_EC;
    final public DmConstantField_Text DESCRIPTION_MAIN_LANGUAGE;
    final public DmConstantField_Text EDIT_STATE_PS_EC;
    final public DmConstantField_Text ELICITATION_COMMENT;
    final public DmConstantField_Text ELICITATION_FEATURES;
    final public DmConstantField_Text EXTERNAL_REVIEW_COMMENT;
    final public DmConstantField_Text OBJECT_TYPE;
    final public DmConstantField_Text REVIEW_COMMENT;
    final public DmConstantField_Text TAGS;
    final public DmConstantField_Text VAR_FUNC_SW;
    final public DmConstantField_Text VAR_FUNC_SYS;

    public DmDoorsObject_PTSA_1x_L1_REQ(ElementType elementType, DoorsObject doorsObject) {
        super(elementType, doorsObject);
        assert (doorsObject != null);

        //
        // Add DmFields for fields configured in DOORS for all element types and known/expected by IPE
        //
        ACCEPTANCE_CRITERIA = extractUserDefinedField("Acceptance Criteria");
        AFFECTED_COMPONENT_PS_EC = extractFieldForAffectedComponent();
        ALLOCATION = extractFieldForAllocation();
        CRQ_PS_EC = extractFieldForCRQ();
        DESCRIPTION_MAIN_LANGUAGE = extractUserDefinedField("Description (main language)", "Description");

        EDIT_STATE_PS_EC = extractFieldForState();

        ELICITATION_COMMENT = extractUserDefinedField("Elicitation Comment");
        ELICITATION_FEATURES = extractUserDefinedField("Elicitation Features");
        EXTERNAL_REVIEW_COMMENT = extractUserDefinedField("External Review Comment");
        OBJECT_TYPE = extractFieldForObjectType();
        REVIEW_COMMENT = extractUserDefinedField("ReviewComment");
        TAGS = extractUserDefinedField("Tags");
        VAR_FUNC_SW = extractUserDefinedField("VAR_FUNC_SW");
        VAR_FUNC_SYS = extractUserDefinedField("VAR_FUNC_SYS");

        // User Assistant
        addRule(new ConfigurableRuleManagerRule_DoorsObject(this));
    }

    @Override
    final public Collection<String> getIdOfReferencedRq1Elements() {
        return (extractIdOfReferencedRq1Elements(CRQ_PS_EC.getValue()));
    }

    @Override
    final public String getDoorsEditState() {
        return (EDIT_STATE_PS_EC.getValueAsText());
    }

    @Override
    final public String getDoorsAsilLevel() {
        return (ASIL.getValueAsText());
    }

    @Override
    final public String getDoorsAllocation() {
        return (ALLOCATION.getValueAsText());
    }

    static Collection<String> extractIdOfReferencedRq1Elements(String value) {

        Pattern numberPattern = Pattern.compile("RQONE[0-9]{8}");
        Set<String> result = new TreeSet<>();

        if (value != null) {
            String[] lines = value.split("\n");
            for (String line : lines) {
                Matcher matcher = numberPattern.matcher(line);
                while (matcher.find() == true) {
                    result.add(matcher.group());
                }
            }

        }
        return (result);
    }

}
