/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1WorkItem_IssueFD;
import Rq1Data.Templates.Rq1TemplateI;
import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1WorkItem_IssueFD extends DmRq1WorkItem_SoftwareIssue {

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1WorkItem_IssueFD(Rq1WorkItem_IssueFD rq1WorkItem) {
        super("Workitem-I-FD", rq1WorkItem);
    }

    public static DmRq1WorkItem_IssueFD createBasedOnIssueFD(DmRq1IssueFD issueFD, DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        return (createBasedOnIssueFD(issueFD, targetProject, template, EnumSet.noneOf(TitleSuffix.class), null));
    }

    public static DmRq1WorkItem_IssueFD createBasedOnIssueFD(DmRq1IssueFD issueFD, DmRq1Project targetProject, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {

        assert (issueFD != null);
        assert (targetProject != null);
        assert (suffixSet != null);

        DmRq1WorkItem_IssueFD workItem = DmRq1ElementCache.createWorkItem_IssueFD();
        String suffix;
        //
        // Take over content from parent
        //
        workItem.ACCOUNT_NUMBERS.setValue(issueFD.ACCOUNT_NUMBERS.getValue());

        //
        // Connect Issue-WorkItem
        //
        workItem.ISSUE.setElement(issueFD);
        issueFD.WORKITEMS.addElement(workItem);

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
        suffix = TitleSuffix.generateSuffix(suffixSet, suffixSeparator, issueFD);
        workItem.TITLE.setValue(workItem.TITLE.getValue() + suffix);
        return (workItem);
    }

    public static DmRq1WorkItem_IssueFD createBasedonWorkItem(DmRq1WorkItem_IssueFD workItem) {
        assert (workItem != null);

        DmRq1WorkItem_IssueFD clonedWorkItem = DmRq1ElementCache.createWorkItem_IssueFD();

        //
        // takeover content from workItem
        //
        clonedWorkItem = (DmRq1WorkItem_IssueFD) cloneBasicContentofWorkItem(workItem, clonedWorkItem);

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
