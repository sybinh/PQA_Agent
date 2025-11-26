/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_CustomerResponse_Workitem;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1WorkItem_Pst;
import Rq1Cache.Records.Rq1WorkItem_Pvar;
import Rq1Cache.Records.Rq1WorkItem_Pver;
import Rq1Data.Enumerations.SubCategory_WorkItem;
import Rq1Data.Templates.Rq1TemplateI;
import java.util.EnumSet;

/**
 *
 * @author GUG2WI
 */
public class DmRq1WorkItem_Pst extends DmRq1WorkItem_Software {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DmRq1WorkItem_Pst.class.getCanonicalName());

    final public DmRq1Field_CustomerResponse_Workitem CUSTOMER_RESPONSE;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1WorkItem_Pst(Rq1WorkItem_Pst rq1WorkItem) {
        super("Workitem-PST", rq1WorkItem);

        addField(CUSTOMER_RESPONSE = new DmRq1Field_CustomerResponse_Workitem(this, rq1WorkItem.CUSTOMER_RESPONSE, "Customer Response"));
    }

    private static DmRq1WorkItem_Pst createWorkItem_Pst(DmRq1Pst targetRelease) {
        assert (targetRelease != null);

        Rq1WorkItem_Pst rq1Record;
        if (targetRelease instanceof DmRq1Pvar) {
            rq1Record = new Rq1WorkItem_Pvar();
        } else {
            rq1Record = new Rq1WorkItem_Pver();
        }
        DmRq1WorkItem_Pst dmElement = new DmRq1WorkItem_Pst(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmElement);
        return (dmElement);
    }

    private static DmRq1WorkItem_Pst createWorkItem_Pst(DmRq1Milestone milestone) {
        assert (milestone != null);

        Rq1WorkItem_Pst rq1Record = new Rq1WorkItem_Pver();
        DmRq1WorkItem_Pst workitem = new DmRq1WorkItem_Pst(rq1Record);
        workitem.SUBCATEGORY.setValue(SubCategory_WorkItem.PVER);
        DmRq1ElementCache.addElement(rq1Record, workitem);
        return (workitem);
    }

    public static DmRq1WorkItem_Pst createBasedOnPst(DmRq1Pst release) {
        assert (release != null);
        return (createBasedOnPst(release, release.PROJECT.getElement(), null, EnumSet.noneOf(TitleSuffix.class), null));
    }

    public static DmRq1WorkItem_Pst createBasedOnPst(DmRq1Pst release, DmRq1Project targetProject, Rq1TemplateI template) {
        return (createBasedOnPst(release, targetProject, template, EnumSet.noneOf(TitleSuffix.class), null));
    }

    public static DmRq1WorkItem_Pst createBasedOnPst(DmRq1Pst release, DmRq1Project targetProject, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {

        assert (release != null);
        assert (targetProject != null);
        assert (suffixSet != null);

        DmRq1WorkItem_Pst workItem = createWorkItem_Pst(release);
        String suffix;
        //
        // Set sub category
        //
        if (release instanceof DmRq1PvarRelease) {
            workItem.SUBCATEGORY.setValue(SubCategory_WorkItem.PVAR_PFAM);
        } else if (release instanceof DmRq1PverRelease) {
            workItem.SUBCATEGORY.setValue(SubCategory_WorkItem.PVER);
        } else {
            logger.warning("Unknown class for sub category: " + release.getClass().getCanonicalName());
        }

        //
        // Take over content from parent
        //
        workItem.ACCOUNT_NUMBERS.setValue(release.ACCOUNT_NUMBERS.getValue());

        //
        // Connect Release-WorkItem
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

    public static DmRq1WorkItem_Pst createBasedonWorkItem(DmRq1WorkItem_Pst workItem) {
        assert (workItem != null);

        DmRq1WorkItem_Pst clonedWorkItem = createWorkItem_Pst((DmRq1Pst) workItem.RELEASE.getElement());

        //
        // takeover content from workItem
        //
        clonedWorkItem = (DmRq1WorkItem_Pst) cloneBasicContentofWorkItem(workItem, clonedWorkItem);

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

    public static DmRq1WorkItem_Pst createBasedOnMilestone(DmRq1Milestone milestone) {
        assert (milestone != null);
        return (createBasedOnMilestone(milestone, milestone.PROJECT.getElement(), null, null, null));
    }

    public static DmRq1WorkItem_Pst createBasedOnMilestone(DmRq1Milestone milestone, DmRq1Project targetProject, Rq1TemplateI template, EnumSet<TitleSuffix> suffixSet, String suffixSeparator) {

        assert (milestone != null);
        assert (targetProject != null);

        DmRq1WorkItem_Pst workItem = createWorkItem_Pst(milestone);

        //
        // Take over content from parent
        //
        workItem.ACCOUNT_NUMBERS.setValue(milestone.ACCOUNT_NUMBERS.getValue());

        //
        // Connect Release-WorkItem
        //
        workItem.MILESTONE.setElement(milestone);
        milestone.WORKITEMS.addElement(workItem);

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
        String suffix = TitleSuffix.generateSuffix(suffixSet, suffixSeparator, milestone);
        workItem.TITLE.setValue(workItem.TITLE.getValue() + suffix);
        return (workItem);
    }

    @Override
    public DmRq1WorkItem cloneWorkItem() {
        return createBasedonWorkItem(this);
    }

    public void setMilestone(DmRq1Milestone dmMilestone) throws DmRq1MapExistsException {
        assert (dmMilestone != null);
        for (DmRq1WorkItem mappedWorkitem : dmMilestone.WORKITEMS.getElementList()) {
            if (this == mappedWorkitem) {
                throw new DmRq1MapExistsException();
            }
        }
        if (MILESTONE.isElementSet() == true) {
            MILESTONE.getElement().WORKITEMS.removeElement(this);
        }
        MILESTONE.setElement(dmMilestone);
        dmMilestone.WORKITEMS.addElement(this);
    }

}
