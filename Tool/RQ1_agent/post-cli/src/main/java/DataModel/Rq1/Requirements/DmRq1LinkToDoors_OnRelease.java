/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Requirements;

import DataModel.DmElement;
import DataModel.DmElementI;
import DataModel.DmSourceField_ReadOnly;
import DataModel.DmValueFieldI;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 *
 * @author gug2wi
 */
public class DmRq1LinkToDoors_OnRelease extends DmElement {

    final private DmElementI doorsElement;

    final public DmValueFieldI<List<DmRq1LinkToRequirement_OnIssueAndIrm>> LINK_ON_ISSUES;

    public DmRq1LinkToDoors_OnRelease(List<DmRq1LinkToRequirement_OnIssueAndIrm> issueLinks) {
        super("LINK_to_DOORS");

        doorsElement = issueLinks.get(0).getRequirement();

        assert (issueLinks != null);
        assert (issueLinks.isEmpty() == false);
        assert (doorsElement != null);
        for (DmRq1LinkToRequirement_OnIssueAndIrm issueLink : issueLinks) {
            assert (doorsElement == issueLink.getRequirement());
        }

        addField(LINK_ON_ISSUES = new DmSourceField_ReadOnly<List<DmRq1LinkToRequirement_OnIssueAndIrm>>("Link on Issues") {
            @Override
            public List<DmRq1LinkToRequirement_OnIssueAndIrm> getValue() {
                return (issueLinks);
            }
        });
    }

    @Override
    public String getTitle() {
        Set<String> linkTypes = new TreeSet<>();
        for (DmRq1LinkToRequirement_OnIssueAndIrm link : LINK_ON_ISSUES.getValue()) {
            linkTypes.add(link.getLinkType().getLinkTypeName());
        }

        StringBuilder b = new StringBuilder();
        for (String linkType : linkTypes) {
            if (b.length() > 0) {
                b.append(", ");
            }
            b.append(linkType);
        }

        return (b.toString());
    }

    @Override
    public String getId() {
        return (doorsElement.getId());
    }

    public DmElementI getDoorsElement() {
        return doorsElement;
    }

    @Override
    public void reload() {

    }

    @Override
    public String toString() {

//        List<String> l = new ArrayList<>();
//        for (DmRq1LinkToRequirement_OnIssueAndIrm v : LINK_ON_ISSUES.getValue()) {
//            l.add(v.toString());
//        }
//        l.sort((s1, s2) -> s1.compareTo(s2));

        String s = LINK_ON_ISSUES.getValue().stream().map((t) -> t.toString()).sorted().collect(Collectors.joining(", ", "[", "]"));
        return (s);
    }

    public Set<String> getLinkTypesAsString() {
        TreeSet<String> result = new TreeSet<>();
        for (DmRq1LinkToRequirement_OnIssueAndIrm link : LINK_ON_ISSUES.getValue()) {
            result.add(link.getLinkType().getLinkTypeName());
        }
        return (result);
    }

}
