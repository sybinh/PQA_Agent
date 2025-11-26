/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1FcRelease;
import Ipe.Annotations.EcvElementList;
import Monitoring.RuleExecutionGroup;
import Monitoring.Warning;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gug2wi
 */
public class Rule_Fc_NamingConvention extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "FC Name Check",
            "The title of a FC has to fit to one of the following formats:\n"
            + "\n"
            + "a) FC : <NameOfFC> / <VersionNumber>\n"
            + "b) FX : <NameOfFC> / <VersionNumber>\n"
            + "\n"
            + "<NameOfFC> ... has to start with a character and may continue with characters and digits and underlines.\n"
            + "\n"
            + "<VersionNumber> ... is a series of numbers, characters and underlines seperated by dots."
            + "\n"
            + "The FC is marked with the warning 'Invalid Title', if the name does not met this format.");

    final private DmRq1FcRelease myRelease;

    public Rule_Fc_NamingConvention(DmRq1FcRelease myRelease) {
        super(description, myRelease);
        this.myRelease = myRelease;
    }

    @Override
    public void executeRule() {
        //
        // Do not check if element is conflicted.
        //
        if (myRelease.isConflicted() == true) {
            return;
        }

        if (isTitleOk(myRelease.getTitle()) != true) {
            addMarker(myRelease, new Warning(this, "Invalid Title", "Title \"" + myRelease.getTitle() + "\" does not fit to the naming convention."));
        }
    }

    // static public to simplify testing
    static public boolean isTitleOk(String title) {
        assert (title != null);

        boolean ok = title.matches("^(FC|FCGEN|FX|FY|FC-ARA|FC-ARB) +: +[a-zA-Z][a-zA-Z0-9_]* +/ +[0-9][a-zA-Z0-9_\\.]*");

        return (ok);
    }

    static final private Pattern matchPattern = Pattern.compile("(FCGEN|FX|FY|FC-ARA|FC-ARB|FC)? *:? *([a-zA-Z][a-zA-Z0-9_]*)");

    static public String extractFcName(String title) {
        Matcher matcher = matchPattern.matcher(title);
        if (matcher.find() == true) {
            String name = matcher.group(2);
            return (name);
        } else {
            java.util.logging.Logger.getLogger(Rule_Fc_NamingConvention.class
                    .getCanonicalName()).warning("Cannot extract name from \"" + title + "\"");
            return (title);
        }
    }

}
