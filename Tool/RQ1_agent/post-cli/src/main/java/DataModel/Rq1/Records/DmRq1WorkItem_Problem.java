/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1WorkItem_Problem;
import Rq1Data.Templates.Rq1TemplateI;
import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1WorkItem_Problem extends DmRq1WorkItem {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1WorkItem_Problem(Rq1WorkItem_Problem rq1WorkItem) {
        super("Workitem-Problem", rq1WorkItem);
    }

    static DmRq1WorkItem_Problem create() {
        Rq1WorkItem_Problem rq1WorkItem = new Rq1WorkItem_Problem();
        DmRq1WorkItem_Problem dmWorkItem = new DmRq1WorkItem_Problem(rq1WorkItem);
        DmRq1ElementCache.addElement(rq1WorkItem, dmWorkItem);
        return (dmWorkItem);
    }

    public static DmRq1WorkItem_Problem createBasedOnWorkItem(DmRq1WorkItem_Problem workItem) {
        assert (workItem != null);

        DmRq1WorkItem_Problem clonedWorkItem = create();

        //
        // takeover content from workItem
        //
        clonedWorkItem = (DmRq1WorkItem_Problem) cloneBasicContentofWorkItem(workItem, clonedWorkItem);

        //
        // Connect Problem-WorkItem
        //
        clonedWorkItem.PROBLEM.setElement(workItem.PROBLEM.getElement());
        workItem.PROBLEM.getElement().WORKITEMS.addElement(clonedWorkItem);

        //
        // Connect WorkItem - Project
        //
        clonedWorkItem.PROJECT.setElement(workItem.PROJECT.getElement());
        workItem.PROJECT.getElement().OPEN_WORKITEMS.addElement(clonedWorkItem);

        return clonedWorkItem;
    }

    public static DmRq1WorkItem_Problem createBasedOnProblem(DmRq1Problem dmProblem, DmRq1SoftwareProject targetProject, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {
        assert (dmProblem != null);
        assert (targetProject != null);

        DmRq1WorkItem_Problem newWorkitem = create();

        //
        // Connect Problem-WorkItem
        //
        newWorkitem.PROBLEM.setElement(dmProblem);
        dmProblem.WORKITEMS.addElement(newWorkitem);

        //
        // Connect WorkItem - Project
        //
        newWorkitem.PROJECT.setElement(targetProject);
        targetProject.OPEN_WORKITEMS.addElement(newWorkitem);

        //
        // Handle template and suffix
        //
        if (template != null) {
            template.execute(newWorkitem);
        }
        String suffix = TitleSuffix.generateSuffix(suffixSet, suffixSeparator, dmProblem);
        newWorkitem.TITLE.setValue(newWorkitem.TITLE.getValue() + suffix);

        return newWorkitem;
    }

    @Override
    public DmRq1WorkItem cloneWorkItem() {
        return (createBasedOnWorkItem(this));
    }

    public void setProblem(DmRq1Problem dmProblem) throws DmRq1MapExistsException {
        assert (dmProblem != null);
        for (DmRq1WorkItem mappedWorkitem : dmProblem.WORKITEMS.getElementList()) {
            if (this.equals(mappedWorkitem)) {
                throw new DmRq1MapExistsException();
            }
        }
        if (PROBLEM.isElementSet() == true) {
            PROBLEM.getElement().WORKITEMS.removeElement(this);
        }
        PROBLEM.setElement(dmProblem);
        dmProblem.WORKITEMS.addElement(this);
    }

}
