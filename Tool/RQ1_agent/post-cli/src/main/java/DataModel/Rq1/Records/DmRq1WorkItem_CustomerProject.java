/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Data.Enumerations.SubCategory_WorkItem;
import Rq1Cache.Records.Rq1WorkItem_CustomerProject;
import Rq1Data.Templates.Rq1Template;

/**
 *
 * @author GUG2WI
 */
public class DmRq1WorkItem_CustomerProject extends DmRq1WorkItem {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1WorkItem_CustomerProject(Rq1WorkItem_CustomerProject rq1WorkItem) {
        super("Workitem-CustPrj", rq1WorkItem);
    }

    @Override
    public String getResponsible() {
        return (PROJECT.getElement().RESPONSIBLE_AT_BOSCH.getValue());
    }

    public static DmRq1WorkItem_CustomerProject createBasedOnCustomerProject(DmRq1SwCustomerProject project, Rq1Template template) {

        assert (project != null);

        DmRq1WorkItem_CustomerProject workItem = DmRq1ElementCache.createWorkItem_CustomerProject();

        //
        // Set sub category
        //
        workItem.SUBCATEGORY.setValue(SubCategory_WorkItem.CUST_PRJ);

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
            template.execute(workItem.rq1Record);
        }
        return (workItem);
    }
    
    public static DmRq1WorkItem_CustomerProject createBasedonWorkItem(DmRq1WorkItem_CustomerProject workItem) {
        assert(workItem != null);
                
        DmRq1WorkItem_CustomerProject clonedWorkItem = DmRq1ElementCache.createWorkItem_CustomerProject();
        
        //
        // takeover content from workItem
        //
       
        clonedWorkItem = (DmRq1WorkItem_CustomerProject) cloneBasicContentofWorkItem(workItem, clonedWorkItem);

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
