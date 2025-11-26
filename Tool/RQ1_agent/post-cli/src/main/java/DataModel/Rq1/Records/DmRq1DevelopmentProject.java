/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Rq1Cache.Records.Rq1SwDevelopmentProject;
import Ipe.Annotations.IpeFactoryConstructor;

/**
 *
 * @author GUG2WI
 */
public class DmRq1DevelopmentProject extends DmRq1SoftwareProject {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1DevelopmentProject(Rq1SwDevelopmentProject rq1DevelopmentProject) {
        super("DevPrj", rq1DevelopmentProject);

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
