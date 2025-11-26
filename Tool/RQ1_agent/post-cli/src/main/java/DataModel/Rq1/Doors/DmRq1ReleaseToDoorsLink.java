/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Doors;

import DataModel.DmElement;
import DataModel.DmLinkInterface;
import DataModel.DmSourceField;
import DataModel.DmValueFieldI;
import DataModel.Doors.Records.DmDoorsElement;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author gug2wi
 */
public class DmRq1ReleaseToDoorsLink extends DmElement implements DmLinkInterface {

    final private DmDoorsElement doorsElement;

    final public DmValueFieldI<List<DmRq1IssueToDoorsLink>> LINK_ON_ISSUES;

    public DmRq1ReleaseToDoorsLink(List<DmRq1IssueToDoorsLink> issueLinks) {
        super("LINK_to_DOORS");

        assert (issueLinks != null);
        assert (issueLinks.isEmpty() == false);

        doorsElement = issueLinks.get(0).getDoorsElement();

        addField(LINK_ON_ISSUES = new DmSourceField<List<DmRq1IssueToDoorsLink>>("Link on Issues") {
            @Override
            public List<DmRq1IssueToDoorsLink> getValue() {
                return (issueLinks);
            }
        });
    }

    @Override
    public String getTitle() {

        StringBuilder b = new StringBuilder();
        for (DmRq1IssueToDoorsLink link : LINK_ON_ISSUES.getValue()) {
            if (b.length() > 0) {
                b.append(", ");
            }
            b.append(link.getLinkType().getLinkTypeName());
        }
        return (b.toString());
    }

    @Override
    public String getId() {
        return (doorsElement.getId());
    }

    public DmDoorsElement getDoorsElement() {
        return doorsElement;
    }

    @Override
    public void reload() {

    }

    @Override
    public String toString() {
        return ("DmRq1ReleaseToDoorsLink:" + doorsElement.toString());
    }

    public Set<String> getLinkTypesAsString() {
        TreeSet<String> result = new TreeSet<>();
        for (DmRq1IssueToDoorsLink link : LINK_ON_ISSUES.getValue()) {
            result.add(link.LINK_TYPE.getValueAsText());
        }
        return (result);
    }

}
