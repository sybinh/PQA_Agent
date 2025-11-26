/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_ExternalDescription_PstLines;
import DataModel.Rq1.Fields.DmRq1Field_PstLinesEcuProjectComplete;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Rq1Cache.Records.Rq1SwReferenceProject;

/**
 *
 * @author GUG2WI
 */
public class DmRq1SwReferenceProject extends DmRq1SoftwareProject {

    final private DmRq1Field_Text EXTERNAL_ID;

    final private DmRq1Field_ExternalDescription_PstLines PST_LINES_FROM_EXTERNAL_DESCRIPTION;
    final private DmRq1Field_PstLinesEcuProjectComplete PST_LINES;

    public DmRq1SwReferenceProject(String subjectType, Rq1SwReferenceProject rq1EcuProject) {
        super(subjectType, rq1EcuProject);

        //
        // Create and add fields
        //
        addField(EXTERNAL_ID = new DmRq1Field_Text(this, rq1EcuProject.EXTERNAL_ID, "External ID"));

        addField((PST_LINES_FROM_EXTERNAL_DESCRIPTION = new DmRq1Field_ExternalDescription_PstLines(this, rq1EcuProject.EXTERNAL_DESCRIPTION, "PST lines from External Description")));
        addField((PST_LINES = new DmRq1Field_PstLinesEcuProjectComplete(this, PST_LINES_FROM_EXTERNAL_DESCRIPTION, rq1EcuProject.EXTERNAL_ID, "PST Lines")));
    }

    static public class ExistsAlreadyException extends Exception {

    }

    public void addWorkitem(DmRq1WorkItem workitem) throws ExistsAlreadyException {
        for (DmRq1WorkItem item : ALL_WORKITEMS.getElementList()) {
            if (item.equals(workitem)) {
                throw new ExistsAlreadyException();

            }
        }
        workitem.PROJECT.getElement().ALL_WORKITEMS.removeElement(workitem);
        workitem.PROJECT.setElement(this);
        ALL_WORKITEMS.addElement(workitem);

    }
}
