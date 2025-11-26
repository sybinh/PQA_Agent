/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1SoftwareRelease;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import Rq1Data.Enumerations.LifeCycleState_Release;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.EcvDate;

/**
 *
 * @author cil83wi
 */
public class Rule_CheckForMissing_BaselineLink extends DmRule<DmRq1SoftwareRelease> {

    static EcvDate compareDate = EcvDate.getDate(2019, 1, 2);

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Check for link to DOORS baseline",
            "A link to the DOORS baseline has to exist in the field 'Description' or 'Internal Comment' or 'Tags' of each PVER/PVAR/BC in Life Cycle State 'Developed' or 'Closed'.\n"
            + "This warning concerns all PVER/PVAR/BC with Submit Date >= 01.01.2019.\n");

    public Rule_CheckForMissing_BaselineLink(DmRq1SoftwareRelease myRelease) {
        super(description, myRelease);
    }

    static public boolean findDoorsLink(String textToSearch) {
        Pattern DOORS_LINK = Pattern.compile("(doors://)|(((http://)|(https://))(.*)(dwa/rm/urn:rational))(.*)");
        Matcher matcher = DOORS_LINK.matcher(textToSearch);
        return matcher.find();
    }

    @Override
    public void executeRule() {

        if ((dmElement.isInLifeCycleState(LifeCycleState_Release.DEVELOPED, LifeCycleState_Release.CLOSED))
                && (dmElement.SUBMIT_DATE.getDate().isLaterOrEqualThen(EcvDate.getDate(2019, 1, 1)))) {

            String tagList = dmElement.TAGLIST.getValueAsText();
            String description = dmElement.DESCRIPTION.getValueAsText();
            String internalComment = dmElement.INTERNAL_COMMENT.getValueAsText();

            if ((findDoorsLink(tagList) == false) &&(findDoorsLink(description) == false) && (findDoorsLink(internalComment) == false)) {

                addMarker(dmElement, new Warning(this, "Link to the DOORS baseline is missing.",
                        "The link to the DOORS baseline is missing in " + dmElement.getTypeIdTitle() + "\n"
                        + "LifeCycleState: " + dmElement.getLifeCycleState().getText())
                        .addAffectedElement(dmElement));
            }
        }
    }
}
