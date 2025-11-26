/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1WorkItem_Bc;
import Rq1Data.Enumerations.SubCategory_WorkItem;
import Rq1Data.Templates.Rq1TemplateI;
import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1WorkItem_Bc extends DmRq1WorkItem_Software {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1WorkItem_Bc(Rq1WorkItem_Bc rq1WorkItem) {
        super("Workitem-BC", rq1WorkItem);
    }

    public static DmRq1WorkItem_Bc createBasedOnBcRelease(DmRq1Bc release, DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        return (createBasedOnBcRelease(release, targetProject, template, EnumSet.noneOf(TitleSuffix.class), null));
    }

    public static DmRq1WorkItem_Bc createBasedOnBcRelease(DmRq1Bc release, DmRq1Project targetProject, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {

        assert (release != null);
        assert (targetProject != null);
        assert (suffixSet != null);

        DmRq1WorkItem_Bc workItem = DmRq1ElementCache.createWorkItem_Bc();
        String suffix;
        //
        // Set sub category
        //
        workItem.SUBCATEGORY.setValue(SubCategory_WorkItem.BC);

        //
        // Take over content from parent
        //
        workItem.ACCOUNT_NUMBERS.setValue(release.ACCOUNT_NUMBERS.getValue());

        //
        // Connect Issue-WorkItem
        //
        workItem.RELEASE.setElement(release);
        release.WORKITEMS.addElement(workItem);

        //
        // Connect WorkItem - Project
        //
        workItem.PROJECT.setElement(targetProject);
        targetProject.OPEN_WORKITEMS.addElement(workItem);
        if (template != null) {
            template.execute(workItem);
        }

        //
        // add a suffix to the title (optional)
        //
        suffix = TitleSuffix.generateSuffix(suffixSet, suffixSeparator, release);
        workItem.TITLE.setValue(workItem.TITLE.getValue() + suffix);
        return (workItem);
    }

    public static DmRq1WorkItem_Bc createBasedonWorkItem(DmRq1WorkItem_Bc workItem) {
        assert (workItem != null);

        DmRq1WorkItem_Bc clonedWorkItem = DmRq1ElementCache.createWorkItem_Bc();

        //
        // takeover content from workItem
        //
        clonedWorkItem = (DmRq1WorkItem_Bc) cloneBasicContentofWorkItem(workItem, clonedWorkItem);

        //
        // Connect Release-WorkItem
        //
        clonedWorkItem.RELEASE.setElement(workItem.RELEASE.getElement());
        workItem.RELEASE.getElement().WORKITEMS.addElement(clonedWorkItem);

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
