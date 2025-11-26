/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1WorkItem_IssueSW;
import Rq1Data.Templates.Rq1TemplateI;
import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1WorkItem_IssueSW extends DmRq1WorkItem_SoftwareIssue {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1WorkItem_IssueSW(Rq1WorkItem_IssueSW rq1WorkItem) {
        super("Workitem-I-SW", rq1WorkItem);
    }

    public static DmRq1WorkItem_IssueSW createBasedOnIssueSW(DmRq1IssueSW issueSW, DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        return (createBasedOnIssueSW(issueSW, targetProject, template, EnumSet.noneOf(TitleSuffix.class), null));
    }

    public static DmRq1WorkItem_IssueSW createBasedOnIssueSW(DmRq1IssueSW issueSW, DmRq1Project targetProject, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {

        assert (issueSW != null);
        assert (targetProject != null);
        assert (suffixSet != null);

        DmRq1WorkItem_IssueSW workItem = DmRq1ElementCache.createWorkItem_IssueSW();
        String suffix;
        //
        // Take over content from parent
        //
        workItem.ACCOUNT_NUMBERS.setValue(issueSW.ACCOUNT_NUMBERS.getValue());

        //
        // Connect Issue-WorkItem
        //
        workItem.ISSUE.setElement(issueSW);
        issueSW.WORKITEMS.addElement(workItem);

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
        suffix = TitleSuffix.generateSuffix(suffixSet, suffixSeparator, issueSW);
        workItem.TITLE.setValue(workItem.TITLE.getValue() + suffix);
        return (workItem);
    }

    public static DmRq1WorkItem_IssueSW createBasedonWorkItem(DmRq1WorkItem_IssueSW workItem) {
        assert (workItem != null);

        DmRq1WorkItem_IssueSW clonedWorkItem = DmRq1ElementCache.createWorkItem_IssueSW();

        //
        // takeover content from workItem
        //
        clonedWorkItem = (DmRq1WorkItem_IssueSW) cloneBasicContentofWorkItem(workItem, clonedWorkItem);

        clonedWorkItem.COMPL_LINK.setValue(workItem.COMPL_LINK.getValueAsText());

        if (workItem.COMPL_RESULT.getValue() != null) {
            clonedWorkItem.COMPL_RESULT.setValue(workItem.COMPL_RESULT.getValue());
        }

        //
        // Connect Issue-WorkItem
        //
        clonedWorkItem.ISSUE.setElement(workItem.ISSUE.getElement());
        workItem.ISSUE.getElement().WORKITEMS.addElement(clonedWorkItem);

        //
        // Connect WorkItem - Project
        //
        clonedWorkItem.PROJECT.setElement(workItem.PROJECT.getElement());
        workItem.PROJECT.getElement().OPEN_WORKITEMS.addElement(clonedWorkItem);

        return clonedWorkItem;
    }

    @Override
    public DmRq1WorkItem cloneWorkItem() {
        return (createBasedonWorkItem(this));
    }

}
