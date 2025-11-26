/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1WorkItem_HwComp;
import Rq1Data.Templates.Rq1TemplateI;
import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1WorkItem_HwComp extends DmRq1WorkItem_Hardware {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1WorkItem_HwComp(Rq1WorkItem_HwComp rq1WorkItem) {
        super("Workitem-HW-COMP", rq1WorkItem);
    }

    private static DmRq1WorkItem_HwComp create() {
        Rq1WorkItem_HwComp rq1Record = new Rq1WorkItem_HwComp();
        DmRq1WorkItem_HwComp dmRecord = new DmRq1WorkItem_HwComp(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmRecord);
        return (dmRecord);
    }

    public static DmRq1WorkItem_HwComp createBasedOnRelease(DmRq1CompRelease release, DmRq1Project targetProject, Rq1TemplateI template) {
        return (createBasedOnRelease(release, targetProject, template, EnumSet.noneOf(TitleSuffix.class), null));
    }

    public static DmRq1WorkItem_HwComp createBasedOnRelease(DmRq1CompRelease release, DmRq1Project targetProject, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {
        assert (release != null);
        assert (targetProject != null);

        DmRq1WorkItem_HwComp workItem = create();
        String suffix;

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

        //
        // Excute Template
        //
        if (template != null) {
            template.execute(workItem);
        }

        //
        // Add a suffix to the title (optional)
        //
        if (suffixSet != null) {
            suffix = TitleSuffix.generateSuffix(suffixSet, suffixSeparator, release);
            workItem.TITLE.setValue(workItem.TITLE.getValue() + suffix);
        }

        return (workItem);
    }

    public static DmRq1WorkItem_HwComp createBasedonWorkItem(DmRq1WorkItem workItem) {
        assert (workItem != null);

        DmRq1WorkItem_HwComp clonedWorkItem = create();

        //
        // takeover content from workItem
        //
        clonedWorkItem = (DmRq1WorkItem_HwComp) cloneBasicContentofWorkItem(workItem, clonedWorkItem);

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
