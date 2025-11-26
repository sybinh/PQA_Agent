/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Monitoring;

import DataModel.Monitoring.DmRule;
import DataModel.Rq1.Records.DmRq1BaseElement;
import Ipe.Annotations.EcvElementList;
import Monitoring.Info;
import Monitoring.RuleExecutionGroup;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gug2wi
 */
public class Rule_Info_InternalComment extends DmRule<DmRq1BaseElement> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    final static public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Extract Infos from Internal Comments",
            "This rule extracts Infos from the field Internal Comment of Projects, Releases, Issues, IRMs and RRMs.\n"
            + "\n"
            + "Each line starting with the text 'Info:' is considered as an info. "
            + "The ':' after 'info' is mandatory. Any upper/lower character combination for 'info' is valid. "
            + "The rest of the line is considered as the text of the info.\n"
            + "\n"
            + "For each info found in the Internal Comment, a Info-Marker is set on the element.");

    final static private Pattern infoPattern = Pattern.compile("\\G\\s*([iI][nN][fF][oO]\\:)?[ \\t]*(.*)");

    public Rule_Info_InternalComment(DmRq1BaseElement dmElement) {
        super(description, dmElement);
    }

    @Override
    public void executeRule() {
        String internalComment = dmElement.INTERNAL_COMMENT.getValue();
        for (String info : extractInfos(internalComment)) {
            if (info.isEmpty() == true) {
                //
                // The title of a marker is not allowed to be empty.
                //
                info = "-";
            }
            addMarker(dmElement, new Info(this, info, internalComment).addAffectedElement(dmElement));
        }
    }

    /**
     * Made static protected to facilitate testing. Do no use from outside of
     * the class.
     *
     * @param comment
     * @return
     */
    static public List<String> extractInfos(String comment) {
        assert (comment != null);

        List<String> l = new ArrayList<>(1);
        Matcher infoMatcher = infoPattern.matcher(comment);

        while (infoMatcher.find()) {
            if (infoMatcher.group(1) != null) {
                l.add(infoMatcher.group(2));
            }
        }

        return (l);
    }

}
