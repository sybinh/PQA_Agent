/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.Rq1.DmRq1LinkInterface;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Records.Rq1Map;
import UiSupport.AssigneeFilter;
import UiSupport.BulkOperationRq1UserI;
import UiSupport.UiTreeViewRootElementI;
import java.util.ArrayList;
import java.util.List;
import util.EcvMapSet;
import util.MailSendable;

/**
 *
 * @author GUG2WI
 */
abstract public class DmRq1MapElement extends DmRq1BaseElement implements DmRq1LinkInterface, MailSendable, UiTreeViewRootElementI, BulkOperationRq1UserI {

    final public DmRq1Field_Text ASSIGNEE_FULLNAME;
    final public DmRq1Field_Text ASSIGNEE_EMAIL;
    final public DmRq1Field_Text ASSIGNEE_LOGIN_NAME;
    final public DmRq1Field_Reference<DmRq1User> ASSIGNEE;

    DmRq1MapElement(String elementType, Rq1Map rq1Map) {
        super(elementType, rq1Map);

        //
        // Create and add fields
        //
        addField(ASSIGNEE_FULLNAME = new DmRq1Field_Text(this, rq1Map.ASSIGNEE_FULLNAME, "Fullname Assignee"));
        addField(ASSIGNEE_EMAIL = new DmRq1Field_Text(this, rq1Map.ASSIGNEE_EMAIL, "E-Mail Assignee"));
        addField(ASSIGNEE_LOGIN_NAME = new DmRq1Field_Text(this, rq1Map.ASSIGNEE_LOGIN_NAME, "Shortcut Assignee"));
        addField(ASSIGNEE = new DmRq1Field_Reference<>(this, rq1Map.ASSIGNEE, "Assignee"));

        ASSIGNEE.setAttribute(DmFieldI.Attribute.FIELD_FOR_BULK_OPERATION);
    }

    @Override
    final public String toString() {
        return (getElementType() + ": " + ((Rq1Map) getRq1Record()).getId());
    }

    @Override
    final public String getTitle() {
        return (toString());
    }

    final public String getAssigneeLoginName() {
        return (ASSIGNEE_LOGIN_NAME.getValue());
    }

    final public String getAssigneeFullName() {
        return (ASSIGNEE_FULLNAME.getValue());
    }

    @Override
    public abstract EcvMapSet<AssigneeFilter, DmRq1Project> getProjects();

    @Override
    public List<MailActionType> getActionName() {
        List<MailActionType> mailActionTypes = new ArrayList<>();
        mailActionTypes.add(MailActionType.ASSIGNEEMAP);
        return mailActionTypes;
    }

    @Override
    public String getAssigneeMail() {
        return ASSIGNEE_EMAIL.getValueAsText();
    }

    @Override
    public String getProjectLeaderMail() {
        return null;
    }

    @Override
    public String getRequesterMail() {
        return null;
    }
    
    @Override
    public String getContactMail() {
        return null;
    }
}
