/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
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
public class Rule_Fc_NamingConvention extends DmRule<DmRq1FcRelease> {

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

    public Rule_Fc_NamingConvention(DmRq1FcRelease myRelease) {
        super(description, myRelease);
    }

    @Override
    public void executeRule() {
        //
        // Do not check if element is conflicted.
        //
        if (dmElement.isConflicted() == true) {
            return;
        }

        if (isTitleOk(dmElement.getTitle()) != true) {
            addMarker(dmElement, new Warning(this, "Invalid Title", "Title \"" + dmElement.getTitle() + "\" does not fit to the naming convention."));
        }
    }

    static final private String TYPE_PATTERN = "(FCGEN|FCL|FCP|FC-ARA|FC-ARB|FC|FX|FY)";
    static final private String NAME_PATTERN = "([a-zA-Z][a-zA-Z0-9_]*)";
    static final private String VERSION_PATTERN = "([0-9][a-zA-Z0-9_\\.]*)";

    static final private Pattern TYPE_NAME_PATTERN = Pattern.compile(TYPE_PATTERN + " +: +" + NAME_PATTERN);
    static final private Pattern TITLE_PATTERN = Pattern.compile(TYPE_PATTERN + " +: +" + NAME_PATTERN + " +/ +" + VERSION_PATTERN);

    // static to simplify testing
    static boolean isTitleOk(String title) {
        assert (title != null);

        boolean ok = title.matches("^" + TITLE_PATTERN + "*");

        return (ok);
    }

    static final private Pattern extractFcNamePattern = Pattern.compile(TYPE_PATTERN + "? *:? *" + NAME_PATTERN);

    static public String extractFcName(String title) {
        Matcher matcher = extractFcNamePattern.matcher(title);
        if (matcher.find() == true) {
            String name = matcher.group(2);
            return (name);
        } else {
            java.util.logging.Logger.getLogger(Rule_Fc_NamingConvention.class
                    .getCanonicalName()).warning("Cannot extract name from \"" + title + "\"");
            return (title);
        }
    }

    /**
     * Returns the first FC mentioned in the given text. The methods knows all
     * the possible types of a FC.
     *
     * @param text The text. E.g. the title of an work item.
     * @return The full FC title or null if no FC was found.
     */
    static public String getFirstFcFromText(String text) {

        if ((text == null) || (text.isEmpty() == true)) {
            return (null);
        }

        Matcher titleMatcher = TITLE_PATTERN.matcher(text);
        if (titleMatcher.find() == true) {
            String type = titleMatcher.group(1);
            String name = titleMatcher.group(2);
            String version = titleMatcher.group(3);
            return (type + " : " + name + " / " + version);
        } else {
            Matcher nameMatcher = TYPE_NAME_PATTERN.matcher(text);
            if (nameMatcher.find() == true) {
                String type = nameMatcher.group(1);
                String name = nameMatcher.group(2);
                return (type + " : " + name);
            }
            return (null);
        }
    }

}
