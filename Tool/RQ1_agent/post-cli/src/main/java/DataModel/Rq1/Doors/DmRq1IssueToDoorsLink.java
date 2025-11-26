/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Doors;

import DataModel.DmConstantField_Text;
import DataModel.DmElement;
import DataModel.DmLinkInterface;
import DataModel.DmSourceField_Text;
import DataModel.DmValueFieldI_Text;
import DataModel.Doors.Records.DmDoorsElement;
import DataModel.Rq1.Records.DmRq1Issue;

/**
 *
 * @author gug2wi
 */
public class DmRq1IssueToDoorsLink extends DmElement implements DmLinkInterface {

    final private DmRq1ToDoorsLinkType linkType;

    final private DmRq1Issue rq1Issue;
    final private DmDoorsElement doorsElement;
    final public DmSourceField_Text LINK_TYPE;
    final public DmValueFieldI_Text REQUIREMENT_ID;
    final public DmValueFieldI_Text LINK_TO_DOORS;

    public DmRq1IssueToDoorsLink(DmRq1Issue rq1Issue, DmRq1ToDoorsLinkType linkType, String requirementId, String linkToDoors, DmDoorsElement doorsElement) {
        super(linkType.getLinkTypeName());

        assert (rq1Issue != null);
        assert (linkType != null);
        assert (requirementId != null);
        assert (linkToDoors != null);
        assert (doorsElement != null);

        this.rq1Issue = rq1Issue;
        this.doorsElement = doorsElement;
        this.linkType = linkType;
        addField(LINK_TYPE = new DmSourceField_Text("Link Type") {
            @Override
            public String getValue() {
                return (linkType.getLinkTypeName());
            }
        });
        addField(REQUIREMENT_ID = new DmConstantField_Text("Requirement ID", requirementId));
        addField(LINK_TO_DOORS = new DmConstantField_Text("Link to DOORS", linkToDoors));
    }

    @Override
    public void reload() {

    }

    @Override
    public String getTitle() {
        return (REQUIREMENT_ID.getValue());
    }

    @Override
    public String getId() {
        return (REQUIREMENT_ID.getValue());
    }

    public DmRq1ToDoorsLinkType getLinkType() {
        return linkType;
    }

    public boolean isBackReferenceRequired() {
        return (linkType.isBackReferenceRequired());
    }

    public DmRq1Issue getRq1Issue() {
        return rq1Issue;
    }

    public DmDoorsElement getDoorsElement() {
        return doorsElement;
    }

    @Override
    public String toString() {
        return (LINK_TYPE.getValueAsText() + "-" + REQUIREMENT_ID.getValueAsText());
    }

}
