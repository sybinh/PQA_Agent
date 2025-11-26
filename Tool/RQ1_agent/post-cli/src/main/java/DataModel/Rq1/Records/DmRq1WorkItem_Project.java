/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import static DataModel.Rq1.Records.DmRq1WorkItem_Project.createBasedonWorkItem;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1WorkItem_CustPrj;
import Rq1Cache.Records.Rq1WorkItem_DevPrj;
import Rq1Cache.Records.Rq1WorkItem_HwCustomerProject;
import Rq1Cache.Records.Rq1WorkItem_HwPlatformProject;
import Rq1Cache.Records.Rq1WorkItem_Project;
import Rq1Cache.Records.Rq1WorkItem_RefPrj;
import Rq1Data.Templates.Rq1TemplateI;

/**
 *
 * @author GUG2WI
 */
public class DmRq1WorkItem_Project extends DmRq1WorkItem {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1WorkItem_Project(Rq1WorkItem_Project rq1WorkItem) {
        super(rq1WorkItem.getSubjectType(), rq1WorkItem);
    }

    public static DmRq1WorkItem_Project createBasedOnProject(DmRq1Project project, Rq1TemplateI template) {
        assert (project != null);

        Rq1WorkItem_Project rq1Workitem;
        if (project instanceof DmRq1SwCustomerProject) {
            rq1Workitem = new Rq1WorkItem_CustPrj();
        } else if (project instanceof DmRq1DevelopmentProject) {
            rq1Workitem = new Rq1WorkItem_DevPrj();
        } else if (project instanceof DmRq1SwReferenceProject) {
            rq1Workitem = new Rq1WorkItem_RefPrj();
        } else if (project instanceof DmRq1HwCustomerProject_Leaf) {
            rq1Workitem = new Rq1WorkItem_HwCustomerProject();
        } else if (project instanceof DmRq1HwCustomerProject_Pool) {
            rq1Workitem = new Rq1WorkItem_HwCustomerProject();
        } else if (project instanceof DmRq1HwPlatformProject_Leaf) {
            rq1Workitem = new Rq1WorkItem_HwPlatformProject();
        } else {
            throw (new Error("Unexpected class " + project.getClass().getCanonicalName()));
        }
        DmRq1WorkItem_Project workItem = new DmRq1WorkItem_Project(rq1Workitem);
        DmRq1ElementCache.addElement(rq1Workitem, workItem);

        //
        // Take over content from parent
        //
        workItem.ACCOUNT_NUMBERS.setValue(project.ACCOUNT_NUMBERS.getValue());

        //
        // Connect WorkItem - Project
        //
        workItem.PROJECT.setElement(project);
        project.OPEN_WORKITEMS.addElement(workItem);
        if (template != null) {
            template.execute(workItem);
        }
        return (workItem);
    }

    public static DmRq1WorkItem_Project createBasedonWorkItem(DmRq1WorkItem_Project workItem) {
        assert (workItem != null);

        Rq1WorkItem_Project newRq1Workitem;
        if (workItem.getRq1Record() instanceof Rq1WorkItem_CustPrj) {
            newRq1Workitem = new Rq1WorkItem_CustPrj();
        } else if (workItem.getRq1Record() instanceof Rq1WorkItem_DevPrj) {
            newRq1Workitem = new Rq1WorkItem_DevPrj();
        } else if (workItem.getRq1Record() instanceof Rq1WorkItem_RefPrj) {
            newRq1Workitem = new Rq1WorkItem_RefPrj();
        } else {
            throw (new Error("Unexpected class " + workItem.getRq1Record().getClass().getCanonicalName()));
        }
        DmRq1WorkItem_Project clonedWorkItem = new DmRq1WorkItem_Project(newRq1Workitem);
        DmRq1ElementCache.addElement(newRq1Workitem, clonedWorkItem);

        //
        // takeover content from workItem
        //
        clonedWorkItem = (DmRq1WorkItem_Project) cloneBasicContentofWorkItem(workItem, clonedWorkItem);

        //
        // Connect WorkItem - Project
        //
        clonedWorkItem.PROJECT.setElement(workItem.PROJECT.getElement());
        workItem.PROJECT.getElement().OPEN_WORKITEMS.addElement(clonedWorkItem);

        return clonedWorkItem;
    }

    @Override
    public DmRq1WorkItem cloneWorkItem() {
        return createBasedonWorkItem(this);
    }

}
