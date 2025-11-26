/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import DataModel.DmConstantField_Text;
import Doors.DoorsObject;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DmDoorsObject_Requirement extends DmDoorsObject {

    final public DmConstantField_Text ASIL;
    final public DmConstantField_Text COMMENT;
    final public DmConstantField_Text CRQ;
    final public DmConstantField_Text DESCRIPTION;
    final public DmConstantField_Text EDIT_STATE;
    final public DmConstantField_Text QUALIFIED_FOR_REGRESSION_TEST;
    final public DmConstantField_Text REVIEW_COMMENT;
    final public DmConstantField_Text VAR_FUNC_SW;
    final public DmConstantField_Text VAR_FUNC_SYS;
    final public DmConstantField_Text VERIFICATION_CRITERIA;

    public DmDoorsObject_Requirement(ElementType elementType, DoorsObject doorsObject) {
        super(elementType, doorsObject);
        assert (doorsObject != null);

        //
        // Add DmFields for fields configured in DOORS for all element types and known/expected by IPE
        //
        ASIL = extractUserDefinedField("ASIL");
        COMMENT = extractUserDefinedField("Comment");
        CRQ = extractUserDefinedField("CRQ");
        DESCRIPTION = extractUserDefinedField("Description (en)");
        EDIT_STATE = extractUserDefinedField("EditState", "Edit State");
        QUALIFIED_FOR_REGRESSION_TEST = extractUserDefinedField("Qualified for Regression Test");
        REVIEW_COMMENT = extractUserDefinedField("ReviewComment", "Review Comment");
        VAR_FUNC_SW = extractUserDefinedField("VAR_FUNC_SW");
        VAR_FUNC_SYS = extractUserDefinedField("VAR_FUNC_SYS");
        VERIFICATION_CRITERIA = extractUserDefinedField("VerificationCriteria", "Verification Criteria");
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
