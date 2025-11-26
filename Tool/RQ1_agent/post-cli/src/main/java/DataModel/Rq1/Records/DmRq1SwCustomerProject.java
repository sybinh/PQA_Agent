/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Rq1Cache.Records.Rq1SwCustomerProject;

/**
 *
 * @author gug2wi
 */
public class DmRq1SwCustomerProject extends DmRq1SoftwareProject {

    public DmRq1SwCustomerProject(String subjectType, Rq1SwCustomerProject rq1Project) {
        super(subjectType, rq1Project);

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
