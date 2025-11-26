/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1;

import DataModel.DmFieldI;
import DataModel.Rq1.Fields.DmRq1Field_Reference;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1User;
import UiSupport.AssigneeFilter;
import java.util.ArrayList;
import java.util.Collection;
import util.EcvMapSet;

/**
 * Supports the forwarding of an RQ1 element to another RQ1 project. This
 * interface is used to mark RQ1 elements that can be forwarded to projects. It
 * also defines the methods necessary to execute the forwarding in the database.
 *
 * @author gug2wi
 */
public interface DmRq1ForwardableElementI {

    /**
     * Forward the element to a project.
     *
     * @param project Project where the element shall be forwarded to.
     * @param assignee Assignee which shall be set in forwarded project
     */
    void forward(DmRq1Project project, DmRq1User assignee);

    /**
     * Returns the project of the predecessor element. This project is presented
     * to the user as a choice for the target project. This element should
     * return null, if no predecessor exists or if it does not make sense to
     * forward the element to the project of the predecessor.
     *
     * @return If a predecessor exists and it's project makes sense as target
     * project, then this project. Otherwise null.
     */
    default DmRq1Project getProjectOfPredecessor() {
        return (null);
    }

    /**
     * Returns the set Project of the element or null if no project is set.
     *
     * @return the set project of the element
     */
    DmRq1Project getProject();

    /**
     * Returns the type of project class to which the element can be forwarded.
     *
     * @return Target project class.
     */
    default Class<? extends DmRq1Project> getTargetProjectClass() {
        return (DmRq1Project.class);
    }

    EcvMapSet<AssigneeFilter, DmRq1Project> getProjects();

    /**
     *
     * @return
     */
    default Collection<DmFieldI> getEditableFields() {
        return new ArrayList<>();
    }

    /**
     * Returns the requester field, if such a field exists.
     *
     * @return The requester field or null.
     */
    DmRq1Field_Reference<DmRq1User> getRequesterField();

    boolean save();

}
