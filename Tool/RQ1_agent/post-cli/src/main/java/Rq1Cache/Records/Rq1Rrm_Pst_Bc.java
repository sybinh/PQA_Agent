/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Records;

import Rq1Cache.Fields.Rq1DatabaseField_Xml;
import Rq1Cache.Fields.Rq1DatabaseField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Combined;
import Rq1Cache.Fields.Rq1XmlSubField_Combined_TextPart;
import Rq1Cache.Fields.Rq1XmlSubField_Date;
import Rq1Cache.Fields.Rq1XmlSubField_Enumeration;
import Rq1Cache.Fields.Rq1XmlSubField_Table;
import Rq1Cache.Fields.Rq1XmlSubField_Text;
import Rq1Cache.Fields.Rq1XmlSubField_Xml;
import Rq1Cache.Rq1LinkDescription;
import Rq1Cache.Types.Rq1XmlTable_CustomerResponse_RRM;
import Rq1Cache.Types.Rq1XmlTable_RrmChangesToIssues;
import Rq1Data.Enumerations.InMaIntState;
import Rq1Data.Enumerations.IntHint_State;
import Rq1Data.Enumerations.IntegratorState;
import Rq1Data.Enumerations.YesNoEmpty;

/**
 *
 * @author gug2wi
 */
public class Rq1Rrm_Pst_Bc extends Rq1Rrm {

    final public Rq1XmlSubField_Date REQUESTED_IMPLEMENTATION_DATE;
    final public Rq1DatabaseField_Date REQUESTED_IMPLEMENTATION_FREEZE;
    final public Rq1XmlSubField_Text RB_COMMENT;
    final public Rq1XmlSubField_Text CALIBRATION_COMMENT;
    final public Rq1XmlSubField_Text CALIBRATION_STATE;
    final public Rq1XmlSubField_Text INTEGRATOR_COMMENT;
    final public Rq1XmlSubField_Enumeration INTEGRATOR_STATE;

    final public Rq1DatabaseField_Xml CHANGES_TO_ISSUES_XML;
    final public Rq1XmlSubField_Table<Rq1XmlTable_RrmChangesToIssues> CHANGES_TO_ISSUES;

    final private Rq1XmlSubField_Xml CUSTOMER_RESPONSE_XML;
    final public Rq1XmlSubField_Table<Rq1XmlTable_CustomerResponse_RRM> CUSTOMER_RESPONSE;

    final private Rq1XmlSubField_Combined INT_HINT;
    final public Rq1XmlSubField_Combined_TextPart INT_HINT_COMMENT;

    final public Rq1XmlSubField_Enumeration IN_MA_INTEGRATION_STATUS;

    //
    // Fields for ECB
    //
    final public Rq1XmlSubField_Combined ECB_INT_HINT_COMMENT_FIELD;
    final public Rq1XmlSubField_Combined_TextPart ECB_INT_HINT_COMMENT_TEXT;
    final public Rq1XmlSubField_Text ECB_INT_HINT_REQUESTER;
    final public Rq1XmlSubField_Enumeration ECB_INT_HINT_STATE;
    final public Rq1XmlSubField_Enumeration ECB_COMMERCIAL_PILOT;
    final public Rq1XmlSubField_Text ECB_INTEGRATOR;

    public Rq1Rrm_Pst_Bc() {
        this(Rq1LinkDescription.RRM_PVAR_REL_BC_REL);
    }

    public Rq1Rrm_Pst_Bc(Rq1LinkDescription mapDescription) {
        super(mapDescription);

        addField(REQUESTED_IMPLEMENTATION_FREEZE = new Rq1DatabaseField_Date(this, "RequestedImplementFreeze"));
        addField(REQUESTED_IMPLEMENTATION_DATE = new Rq1XmlSubField_Date(this, TAGS, "RequestedImplementationDate"));
        addField(RB_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "RB_Comment"));
        RB_COMMENT.addAlternativName("KundenKommentar");
        addField(CALIBRATION_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "CalibrationComment"));
        addField(CALIBRATION_STATE = new Rq1XmlSubField_Text(this, TAGS, "CalibrationState"));
        addField(INTEGRATOR_COMMENT = new Rq1XmlSubField_Text(this, TAGS, "IntegratorComment"));
        addField(INTEGRATOR_STATE = new Rq1XmlSubField_Enumeration(this, TAGS, "IntegratorState", IntegratorState.values(), IntegratorState.EMPTY));

        addField(CHANGES_TO_ISSUES_XML = new Rq1DatabaseField_Xml(this, "ChangesToIssues"));
        addField(CHANGES_TO_ISSUES
                = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_RrmChangesToIssues(), CHANGES_TO_ISSUES_XML, "ChangeIssue"));

        addField(CUSTOMER_RESPONSE_XML = new Rq1XmlSubField_Xml(this, TAGS, "CustomerResponse"));
        CUSTOMER_RESPONSE_XML.setOptional();
        addField(CUSTOMER_RESPONSE = new Rq1XmlSubField_Table<>(this, new Rq1XmlTable_CustomerResponse_RRM(), CUSTOMER_RESPONSE_XML, "Response"));
        CUSTOMER_RESPONSE.setOptional();

        addField(INT_HINT = new Rq1XmlSubField_Combined(this, TAGS, "IntHint"));
        addField(INT_HINT_COMMENT = new Rq1XmlSubField_Combined_TextPart(this, INT_HINT));
        INT_HINT_COMMENT.setReadOnly();

        addField(IN_MA_INTEGRATION_STATUS = new Rq1XmlSubField_Enumeration(this, TAGS, "InMaIntState", InMaIntState.values(), InMaIntState.EMPTY));

        //
        // Fields for ECB
        //
        addField(ECB_INT_HINT_COMMENT_FIELD = new Rq1XmlSubField_Combined(this, INT_HINT, "Comment"));
        addField(ECB_INT_HINT_COMMENT_TEXT = new Rq1XmlSubField_Combined_TextPart(this, ECB_INT_HINT_COMMENT_FIELD));
        addField(ECB_INT_HINT_REQUESTER = new Rq1XmlSubField_Text(this, INT_HINT, "Requester"));
        addField(ECB_INT_HINT_STATE = new Rq1XmlSubField_Enumeration(this, INT_HINT, "State", IntHint_State.values(), IntHint_State.EMPTY));
        ECB_INT_HINT_STATE.acceptInvalidValuesInDatabase();
        addField(ECB_COMMERCIAL_PILOT = new Rq1XmlSubField_Enumeration(this, TAGS, "Commercial-Pilot", YesNoEmpty.values(), YesNoEmpty.EMPTY));
        ECB_COMMERCIAL_PILOT.acceptInvalidValuesInDatabase();
        addField(ECB_INTEGRATOR = new Rq1XmlSubField_Text(this, TAGS, "Integrator"));
    }
}
