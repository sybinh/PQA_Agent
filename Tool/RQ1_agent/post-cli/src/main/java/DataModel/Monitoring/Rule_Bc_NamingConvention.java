/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.Rq1.Monitoring.DmRq1RuleDescription;
import DataModel.Rq1.Records.DmRq1BcRelease;
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
public class Rule_Bc_NamingConvention extends DmRule {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    static final public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "BC Name Check",
            "The title of a BC has to fit to one of the following formats:\n"
            + "\n"
            + "a) BC : <NameOfComponent> / <VersionNumber>\n"
            + "b) BC_MO : Mo / <VersionNumber>\n"
            + "c) BX : <NameOfComponent> / <VersionNumberOrName>\n"
            + "d) CEL : <NameOfComponent> / <VersionNumberOrName>\n"
            + "e) DX : <NameOfComponent> / <VersionNumber>\n"
            + "f) IC : <NameOfComponent> / <VersionNumberOrName>\n"
            + "\n"
            + "<NameOfComponent> ... Only characters allowed.\n"
            + "\n"
            + "<VersionNumber> ... has to start with number continued by series of numbers, characters and underlines seperated by dots.\n"
            + "<VersionNumberOrName> ... has to start with number or character continued by series of numbers, characters and underlines seperated by dots.\n"
            + "\n"
            + "The BC is marked with the warning 'Invalid Title', if the name does not fit to this format.");

    final private DmRq1BcRelease myRelease;

    public Rule_Bc_NamingConvention(DmRq1BcRelease myRelease) {
        super(description, myRelease);
        assert (myRelease != null);
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

        boolean ok = title.matches("((BC|BCC) *: *([a-zA-Z_]+) */ *[0-9]+(\\.[0-9]+){0,2}(_[a-zA-Z0-9_\\.]*)? *)"
                + "|"
                + "(BC_MO *: *(Mo|MoVW) */ *([0-9]+(\\.[0-9]+){0,2}(_[a-zA-Z0-9_\\.]*)?) *)"
                + "|"
                + "((BX|CEL|DX|IC) *: *[a-zA-Z_]+ */ *([a-zA-Z0-9][a-zA-Z0-9_\\.]*) *)");

        return (ok);
    }

    static final private Pattern matchPattern = Pattern.compile("(BC_MO|BCC|BC|BX|CEL|CC|DX|IC)? *:? *([a-zA-Z_]+)");

    static public String extractBcName(String title) {
        Matcher matcher = matchPattern.matcher(title);
        if (matcher.find() == true) {
            String name = matcher.group(2);
            return (name);
        } else {
            java.util.logging.Logger.getLogger(Rule_Bc_NamingConvention.class
                    .getCanonicalName()).warning("Cannot extract name from \"" + title + "\"");
            return (title);
        }
    }

    static public String extractBcType(String title) {
        assert (title != null);

        Matcher matcher = matchPattern.matcher(title);
        if (matcher.find() == true) {
            String type = matcher.group(1);
            return (type != null ? type : "");
        } else {
            java.util.logging.Logger.getLogger(Rule_Bc_NamingConvention.class
                    .getCanonicalName()).warning("Cannot extract type from \"" + title + "\"");
            return (title);
        }
    }

}
