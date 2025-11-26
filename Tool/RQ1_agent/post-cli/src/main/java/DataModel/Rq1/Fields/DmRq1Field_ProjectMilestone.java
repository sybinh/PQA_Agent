/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1WorkItem;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implements the project milestone field in work items.
 *
 * @author gug2wi
 */
public class DmRq1Field_ProjectMilestone extends DmRq1Field_Text {

    private final DmRq1Field_Reference<DmRq1Project> projectField;

    public DmRq1Field_ProjectMilestone(DmRq1WorkItem workItem, Rq1FieldI_Text rq1Field, String nameForUserInterface) {
        super(rq1Field, nameForUserInterface);

        projectField = workItem.PROJECT;
        assert (projectField != null);
    }

    public Set<String> getValidMilestoneNames() {

        Set<String> result = new TreeSet<>();

        DmRq1Project project = projectField.getElement();
        if (project == null) {
            return (result);
        }

        for (DmRq1Field_MilestoneTable.Record record : project.MILESTONES_TABLE.getValueAsList()) {
            String name = record.getName();
            if ((name != null) && (name.isEmpty() == false)) {
                result.add(name);
            }
        }

        return (result);
    }

}
