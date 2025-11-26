/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Review;

import DataModel.DmConstantField_Date;
import DataModel.DmConstantField_Text;
import DataModel.GPM.DmGpmMilestone;
import DataModel.GPM.DmGpmMilestone_FromRelease;
import DataModel.Rq1.Records.DmRq1WorkItem;
import DataModel.Xml.DmXmlElementListField;
import util.EcvDate;
import util.EcvXmlContainerElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author cil83wi
 */
public class DmReview_Milestone extends DmReview_Element {

    final public DmConstantField_Text ID;
    final public DmConstantField_Text NAME;
    final public DmConstantField_Date DATE;
    final public DmConstantField_Text CLASSIFICATION;
    final public DmConstantField_Text TYPE;
    final public DmXmlElementListField<DmReview_Workitem> WorkItems;

    //Container
    public static final String MILESTONE_SUB_CONTAINER = "Milestone";
    public static final String WORKITEM_CONTAINER = "Workitem_On_Milestone";

    //Milestone
    public static final String MILESTONE_ID_TAG = "Milestone_Id";
    public static final String MILESTONE_NAME_TAG = "Milestone_Name";
    public static final String MILESTONE_PLANNED_TAG = "Milestone_Planning";
    public static final String MILESTONE_TYPE_TAG = "Milestone_Type";
    public static final String MILESTONE_CLASSIFICATION_TAG = "Milestone_Classification";

    public DmReview_Milestone(EcvXmlContainerElement milestone, String type) {
        super(type);
        assert (milestone != null);

        addField(ID = createTextField(milestone, MILESTONE_ID_TAG, "ID"));
        addField(NAME = createTextField(milestone, MILESTONE_NAME_TAG, "Name"));
        addField(DATE = createDateField(milestone, MILESTONE_PLANNED_TAG, "Date"));
        if (milestone.getXmlString().contains(MILESTONE_CLASSIFICATION_TAG)){
            addField(CLASSIFICATION = createTextField(milestone, MILESTONE_CLASSIFICATION_TAG, "Classification"));
        }else{
            addField(CLASSIFICATION = new DmConstantField_Text("Classification", ""));
        }

        addField(TYPE = createTextField(milestone, MILESTONE_TYPE_TAG, "Type"));
        addField(WorkItems = createXmlListField(milestone, WORKITEM_CONTAINER, "Workitem", "Workitem on milestone", (EcvXmlContainerElement xmlContainer) -> {
            return (new DmReview_Workitem(xmlContainer));
        }));
    }

    @Override
    public String getTitle() {
        return (NAME.getValueAsText());
    }

    @Override
    public String getId() {
        return (ID.getValueAsText().split("/")[0]);
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + ID.getValueAsText() + " " + NAME.getValueAsText());
    }

    protected static EcvXmlContainerElement getMilestoneDataAsXmlForReview(DmGpmMilestone milestone) {

        EcvXmlContainerElement milestoneXMLContainer = new EcvXmlContainerElement(MILESTONE_SUB_CONTAINER);

        milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_ID_TAG, milestone.getId()));
        milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_NAME_TAG, milestone.NAME.getValue()));
        EcvDate plannedDate = milestone.getValueFromField_DATE();
        if (plannedDate != null && plannedDate.getXmlValue().isEmpty() == false) {
            milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_PLANNED_TAG, milestone.DATE.getValue().getXmlValue()));
        } else {
            milestoneXMLContainer.addElement(new EcvXmlEmptyElement(MILESTONE_PLANNED_TAG));
        }
        if (milestone instanceof DmGpmMilestone_FromRelease && ((DmGpmMilestone_FromRelease) milestone).getClassification().isBlank() == false) {
            milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_CLASSIFICATION_TAG, ((DmGpmMilestone_FromRelease) milestone).getClassification()));
        }
        String type = milestone.getType().getText().equals(DmGpmMilestone.Type.RQ1_PROJECT.getText()) ? "Milestone" : milestone.getType().getText();
        milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_TYPE_TAG, type));

        EcvXmlContainerElement xmlWorkitems = new EcvXmlContainerElement(WORKITEM_CONTAINER);
        milestoneXMLContainer.addElement(xmlWorkitems);

        for (DmRq1WorkItem dmRq1WorkItem : milestone.ALL_WORKITEMS.getElementList()) {
            xmlWorkitems.addElement(DmReview_Workitem.getWorkitemDataAsXmlForReview(dmRq1WorkItem));
        }

        return milestoneXMLContainer;

    }

    protected EcvXmlContainerElement getReviewMilestoneDataAsXml() {

        EcvXmlContainerElement milestoneXMLContainer = new EcvXmlContainerElement(MILESTONE_SUB_CONTAINER);

        milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_ID_TAG, ID.getValue()));
        milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_NAME_TAG, NAME.getValue()));
        String plannedDate = DATE.getDate().getXmlValue();
        if (plannedDate != null && plannedDate.isEmpty() == false) {
            milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_PLANNED_TAG, plannedDate));
        } else {
            milestoneXMLContainer.addElement(new EcvXmlEmptyElement(MILESTONE_PLANNED_TAG));
        }
        
        if (CLASSIFICATION.getValue().isEmpty() == false) {
            milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_CLASSIFICATION_TAG, CLASSIFICATION.getValue()));
        }else{
            milestoneXMLContainer.addElement(new EcvXmlEmptyElement(MILESTONE_CLASSIFICATION_TAG));
        }

        milestoneXMLContainer.addElement(new EcvXmlTextElement(MILESTONE_TYPE_TAG, TYPE.getValue()));

        EcvXmlContainerElement xmlWorkitems = new EcvXmlContainerElement(WORKITEM_CONTAINER);
        milestoneXMLContainer.addElement(xmlWorkitems);

        for (DmReview_Workitem workitem : WorkItems.getElementList()) {
            xmlWorkitems.addElement(workitem.getReviewWorkitemDataAsXml());
        }

        return milestoneXMLContainer;

    }

}
