/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Fields.DmRq1Field_Text;
import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1IssueSW;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Leaf;
import DataModel.Rq1.Records.DmRq1SwCustomerProject_Pool;
import DataModel.Rq1.Records.DmRq1Pst;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Issue;
import Rq1Data.Enumerations.LifeCycleState_Project;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.EnumSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gug2wi
 */
public class Rule_AccountNumberFormat extends DmRule {

    static private final Logger LOGGER = Logger.getLogger(Rule_AccountNumberFormat.class.getCanonicalName());

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription DESCRIPTION = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.UI_VISIBLE),
            "Account Number Format",
            "Checks that the field for account numbers contains at least one line that fits to the expected format:\n"
            + "\n"
            + "Expected formats:\n"
            + "BM-<8 digits>_<3 digits>\n"
            + "BE-<digits>-<digits>...\n"
            + ""
            + "Any content is allowed in the line after the number and a blank.\n"
            + ""
            + "Examples:\n"
            + "BM-12345678_123\n"
            + "BM-40487349_947 text\n"
            //            + "Format a)\n"
            //            + "<AccountNumber> <Type> <Text>\n"
            //            + "<AccountNumber> is a number or a U followed by a number.\n"
            //            + "<Type> is one of FC-EA, IN-EA or SW-EA.\n"
            //            + "<Text> is an optional text.\n"
            //            + "\n"
            //            + "Format b)\n"
            //            + "BE-<Number with hyphen> <text>\n"
            //            + "\n"
            //            + "Examples:\n"
            //            + "012345689 FC-EA text\n"
            //            + "012456789 IN-EA text\n"
            //            + "U12456789 SW-EA text\n"
            //            + "U12456789 FC-EA\n"
            + "BE-0027623 text\n"
            + "BE-000467-01-11-50-58\n"
            + "\n"
            + "Note:\n"
            + "The check is only done for non emtpy account number fields.\n"
            + "The check is done for customer projects, PVAR and PVER and I-SW which belong to the ECV pool project (" + DmRq1Project.PoolProject.VW.getRq1Id() + ").\n"
            + "The check is not done for PVAR and PVER in state DEVELOPED, CLOSED, CANCELLED and CONFLICTED.\n"
            + "The check is not done for projects in state CANCELLED and CLOSED.\n"
            + "The check is not done for I-SW in state CONFLICTED, CANCELLED and CLOSED.\n");
//            + "\n"
//            + "Note: The regular expression for the format is the following: ((BE-[0-9-]+|((U|0)[0-9]+ +(FC-EA|IN-EA|SW-EA)))( +.*)?)");

    static final private String accountNumberPattern = "(\\s*(U|0|BE-)[0-9]+ +(FC-EA|IN-EA|SW-EA)( +.*)?)*"; // Format, if all lines have to match the pattern
    static final private String ACCOUNT_NUMBER_PATTERN_OLD = "((BE-[0-9-]+|((U|0)[0-9]+ +(FC-EA|IN-EA|SW-EA)))( +.*)?)"; // Format, if one correct line is enought.
    static final private String ACCOUNT_NUMBER_PATTERN = "(((BE-[0-9-]+)|(BM-[0-9]{8,8}_[0-9]{3,3}))( +.*)?)"; // Format for MCR-ID. One correct line is enought.
    static final private Matcher ACCOUNT_NUMBER_MATCHER = Pattern.compile(ACCOUNT_NUMBER_PATTERN).matcher("");

    final private DmRq1SwCustomerProject_Leaf project;
    final private DmRq1Pst release;
    final private DmRq1IssueSW issue;
    final private DmRq1Field_Text accountNumberField;

    public Rule_AccountNumberFormat(DmRq1SwCustomerProject_Leaf project) {
        super(DESCRIPTION, project);
        this.project = project;
        this.release = null;
        this.issue = null;
        accountNumberField = project.ACCOUNT_NUMBERS;
    }

    public Rule_AccountNumberFormat(DmRq1Pst release) {
        super(DESCRIPTION, release);
        this.project = null;
        this.release = release;
        this.issue = null;
        accountNumberField = release.ACCOUNT_NUMBERS;
    }

    public Rule_AccountNumberFormat(DmRq1IssueSW issue) {
        super(DESCRIPTION, issue);
        this.project = null;
        this.release = null;
        this.issue = issue;
        LOGGER.finer("I-SW: " + issue.toString());
        accountNumberField = issue.ACCOUNT_NUMBERS;
    }

    @Override
    public void executeRule() {

        DmRq1SwCustomerProject_Pool poolProject = null;

        if (project != null) {
            if (project.isInLifeCycleState(LifeCycleState_Project.CANCELED, LifeCycleState_Project.CLOSED)) {
                return;
            } else if (project.POOL_PROJECT.getElement() instanceof DmRq1SwCustomerProject_Pool) {
                poolProject = (DmRq1SwCustomerProject_Pool) project.POOL_PROJECT.getElement();
            }
        } else if (release != null) {
            if (release.isInLifeCycleState(LifeCycleState_Release.DEVELOPED, LifeCycleState_Release.CLOSED, LifeCycleState_Release.CANCELED, LifeCycleState_Release.CONFLICTED)) {
                return;
            } else if ((release.PROJECT.getElement() != null) && (release.PROJECT.getElement().POOL_PROJECT.getElement() instanceof DmRq1SwCustomerProject_Pool)) {
                poolProject = (DmRq1SwCustomerProject_Pool) release.PROJECT.getElement().POOL_PROJECT.getElement();
            }
        } else if (issue != null) {
            LOGGER.finer("I-SW: " + issue.toString());
            if (issue.isInLifeCycleState(LifeCycleState_Issue.CONFLICTED, LifeCycleState_Issue.CANCELED, LifeCycleState_Issue.CLOSED, LifeCycleState_Issue.CONFLICTED)) {
                return;
            } else if (issue.PROJECT.getElement() instanceof DmRq1SwCustomerProject_Pool) {
                poolProject = (DmRq1SwCustomerProject_Pool) issue.PROJECT.getElement();
            } else if ((issue.PROJECT.getElement() != null) && (issue.PROJECT.getElement().POOL_PROJECT.getElement() instanceof DmRq1SwCustomerProject_Pool)) {
                poolProject = (DmRq1SwCustomerProject_Pool) issue.PROJECT.getElement().POOL_PROJECT.getElement();
            }
        }

        if (DmRq1Project.PoolProject.VW.isPoolProject(poolProject) == true) {
            if (getAccountNumberOk(accountNumberField.getValueAsText()) == null) {
                addMarker(assignedDmElement, new Warning(this, "Invalid account number format", "Account numbers \"" + accountNumberField.getValueAsText() + "\" do not fit to the format definition."));
            }
        }
    }

    /**
     * Returns a string to improve testing.
     *
     * @param accountNumber
     * @return
     */
    static synchronized String getAccountNumberOk(final String accountNumber) {
        assert (accountNumber != null);

        if (accountNumber.isEmpty()) {
            return ("");
        }

        try {
            ACCOUNT_NUMBER_MATCHER.reset(accountNumber);

            ACCOUNT_NUMBER_MATCHER.useAnchoringBounds(true);

            if (ACCOUNT_NUMBER_MATCHER.find() == true) {
                return (ACCOUNT_NUMBER_MATCHER.group(2));
            } else {
                return (null);
            }

        } catch (java.lang.StringIndexOutOfBoundsException ex) {
            Logger.getLogger(Rule_AccountNumberFormat.class.getCanonicalName()).severe("accountNumber=" + accountNumber);
            throw (new Error("Check failed for account number=>" + accountNumber + "<", ex));
        }
    }
}
