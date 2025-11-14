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
import Monitoring.RuleExecutionGroup;
import Monitoring.ToDo;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gug2wi
 */
public class Rule_ToDo_InternalComment extends DmRule<DmRq1BaseElement> {

    @EcvElementList("DataModel.Rq1.Monitoring.DmRq1RuleDescription")
    final static public DmRq1RuleDescription description = new DmRq1RuleDescription(
            EnumSet.of(RuleExecutionGroup.ELEMENT_INTEGRITY),
            "Extract ToDos from Internal Comments",
            "This rule extracts ToDos from the field Internal Comment of Projects, Releases, Issues, IRMs and RRMs.\n"
            + "\n"
            + "Each line starting with the text 'ToDo:' is considered as an todo. "
            + "The ':' after 'todo' is mandatory. Any upper/lower character combination for 'todo' is valid. "
            + "The rest of the line is considered as the text of the todo.\n"
            + "\n"
            + "For each todo found in the Internal Comment, a ToDo-Marker is set on the element.");

    final static private Pattern toDoPattern = Pattern.compile("\\G\\s*([tT][Oo][Dd][Oo]\\:)?[ \\t]*(.*)");

    public Rule_ToDo_InternalComment(DmRq1BaseElement dmElement) {
        super(description, dmElement);
    }

    @Override
    public void executeRule() {
        String internalComment = dmElement.INTERNAL_COMMENT.getValue();
        for (String todo : extractToDos(internalComment)) {
            addMarker(dmElement, new ToDo(this, todo, internalComment).addAffectedElement(dmElement));
        }
    }

    /**
     * Made static protected to facilitate testing. Do no use from outside of
     * the class.
     *
     * @param comment
     * @return
     */
    static public List<String> extractToDos(String comment) {
        assert (comment != null);

        List<String> l = new ArrayList<>(1);
        Matcher toDoMatcher = toDoPattern.matcher(comment);
//        toDoMatcher.useAnchoringBounds(false);

        while (toDoMatcher.find()) {
            if (toDoMatcher.group(1) != null) {
                l.add(toDoMatcher.group(2));
            }
        }

        return (l);
    }
}
