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

public class DmDoorsObject_PTSA_1x_Requirement extends DmDoorsObject_PTSA_1x {

    final public DmConstantField_Text OBJECT_TYPE;

    final public DmConstantField_Text CRQ;
    final public DmConstantField_Text DESCRIPTION;
    final public DmConstantField_Text EDIT_STATE;
    final public DmConstantField_Text QUALIFIED_FOR_REGRESSION_TEST;
    final public DmConstantField_Text REVIEW_COMMENT;
    final public DmConstantField_Text VAR_FUNC_SW;
    final public DmConstantField_Text VAR_FUNC_SYS;
    final public DmConstantField_Text VERIFICATION_CRITERIA;

    public DmDoorsObject_PTSA_1x_Requirement(ElementType elementType, DoorsObject doorsObject) {
        super(elementType, doorsObject);
        assert (doorsObject != null);

        //
        // Add DmFields for fields configured in DOORS for all element types and known/expected by IPE
        //
        OBJECT_TYPE = extractFieldForObjectType();
        CRQ = extractFieldForCRQ();
        DESCRIPTION = extractUserDefinedField("Description (en)");

        EDIT_STATE = extractFieldForState();

        QUALIFIED_FOR_REGRESSION_TEST = extractUserDefinedField("Qualified for Regression Test");
        REVIEW_COMMENT = extractFieldForReviewComment();
        VAR_FUNC_SW = extractUserDefinedField("VAR_FUNC_SW");
        VAR_FUNC_SYS = extractUserDefinedField("VAR_FUNC_SYS");
        VERIFICATION_CRITERIA = extractFieldForVerificationCriteria();

        // User Assistant
        addRule(new ConfigurableRuleManagerRule_DoorsObject(this));
    }

    @Override
    public Collection<String> getIdOfReferencedRq1Elements() {
        return (extractIdOfReferencedRq1Elements(CRQ.getValue()));
    }

    @Override
    public String getDoorsEditState() {
        return (EDIT_STATE.getValueAsText());
    }

    @Override
    public String getDoorsAsilLevel() {
        return (ASIL.getValueAsText());
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
