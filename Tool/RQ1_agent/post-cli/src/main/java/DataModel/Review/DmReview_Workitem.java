/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Review;

import DataModel.DmConstantField_Date;
import DataModel.Xml.DmXmlMappedContainerElementI;
import DataModel.DmConstantField_Text;
import DataModel.DmElementField_ReadOnlyI;
import DataModel.DmElementI;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1WorkItem;
import util.EcvDate;
import util.EcvXmlContainerElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 *
 * @author cil83wi
 */
public class DmReview_Workitem extends DmReview_Element implements DmXmlMappedContainerElementI {

    final public DmConstantField_Text ID;
    final public DmConstantField_Text NAME;
    final public DmConstantField_Date START;
    final public DmConstantField_Date PLANNED;
    final public DmConstantField_Date ACTUAL;
    final public DmConstantField_Text GPJM_ID;
    final public DmConstantField_Text LCS;
    final public DmElementField_ReadOnlyI<DmRq1ElementInterface> WORKITEMS;

    //Container
    public static final String WORKITEM_SUB_CONTAINER = "Workitem";

    //Workitem
    public static final String WORKITEM_ID_TAG = "Workitem_ID";
    public static final String WORKITEM_NAME_TAG = "Workitem_Name";
    public static final String WORKITEM_START_TAG = "Workitem_Start";
    public static final String WORKITEM_PLANNED_TAG = "Workitem_Planned";
    public static final String WORKITEM_ACTUAL_TAG = "Workitem_Actual";
    public static final String WORKITEM_GPJM_ID_TAG = "Workitem_GPJM_ID";
    public static final String WORKITEM_LCS = "Workitem_LCS";

    DmReview_Workitem(EcvXmlContainerElement programmStand) {
        super("Workitem");
        assert (programmStand != null);

        addField(ID = createTextField(programmStand, WORKITEM_ID_TAG, "ID"));
        addField(NAME = createTextField(programmStand, WORKITEM_NAME_TAG, "Name"));
        addField(START = createDateField(programmStand, WORKITEM_START_TAG, "Start Date"));
        addField(PLANNED = createDateField(programmStand, WORKITEM_PLANNED_TAG, "Planned Date"));
        addField(ACTUAL = createDateField(programmStand, WORKITEM_ACTUAL_TAG, "Actual Date"));
        addField(GPJM_ID = createTextField(programmStand, WORKITEM_GPJM_ID_TAG, "GuidedPjM Task ID"));
        addField(LCS = createTextField(programmStand, WORKITEM_LCS, "Life Cycle State"));
        addField(WORKITEMS = new DmReview_Rq1ElementField(ID, WORKITEM_SUB_CONTAINER));
    }

    @Override
    public String getTitle() {
        return (NAME.getValueAsText());
    }

    @Override
    public String getId() {
        return (ID.getValueAsText());
    }

    @Override
    public DmElementI getMappingElement() {
        return (mappingRq1Element(ID));
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + ID.getValueAsText() + " " + NAME.getValueAsText());
    }

    protected static EcvXmlContainerElement getWorkitemDataAsXmlForReview(DmRq1WorkItem workitem) {

        EcvXmlContainerElement workitemXMLContainer = new EcvXmlContainerElement(WORKITEM_SUB_CONTAINER);
        workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_ID_TAG, workitem.getId()));
        workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_NAME_TAG, workitem.TITLE.getValue()));

        EcvDate startDate = workitem.START_DATE.getValue();
        if (startDate != null && startDate.getXmlValue().isEmpty() == false) {
            workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_START_TAG, startDate.getXmlValue()));
        } else {
            workitemXMLContainer.addElement(new EcvXmlEmptyElement(WORKITEM_START_TAG));
        }

        EcvDate plannedDate = workitem.PLANNED_DATE.getValue();
        if (plannedDate != null && plannedDate.getXmlValue().isEmpty() == false) {
            workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_PLANNED_TAG, plannedDate.getXmlValue()));
        } else {
            workitemXMLContainer.addElement(new EcvXmlEmptyElement(WORKITEM_PLANNED_TAG));
        }

        EcvDate actualDate = workitem.ACTUAL_DATE.getValue();
        if (actualDate != null && actualDate.getXmlValue().isEmpty() == false) {
            workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_ACTUAL_TAG, actualDate.getXmlValue()));
        } else {
            workitemXMLContainer.addElement(new EcvXmlEmptyElement(WORKITEM_ACTUAL_TAG));
        }

        String gpmTaskId = workitem.getGpmTaskId();
        if (gpmTaskId != null && gpmTaskId.isEmpty() == false) {
            workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_GPJM_ID_TAG, workitem.getGpmTaskId()));
        } else {
            workitemXMLContainer.addElement(new EcvXmlEmptyElement(WORKITEM_GPJM_ID_TAG));
        }

        workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_LCS, workitem.LIFE_CYCLE_STATE.getValue().getText()));

        return workitemXMLContainer;
    }

    protected EcvXmlContainerElement getReviewWorkitemDataAsXml() {

        EcvXmlContainerElement workitemXMLContainer = new EcvXmlContainerElement(WORKITEM_SUB_CONTAINER);
        workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_ID_TAG, ID.getValue()));
        workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_NAME_TAG, NAME.getValue()));

        EcvDate startDate = START.getValue();
        if (startDate != null && startDate.getXmlValue().isEmpty() == false) {
            workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_START_TAG, startDate.getXmlValue()));
        } else {
            workitemXMLContainer.addElement(new EcvXmlEmptyElement(WORKITEM_START_TAG));
        }

        EcvDate plannedDate = PLANNED.getValue();
        if (plannedDate != null && plannedDate.getXmlValue().isEmpty() == false) {
            workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_PLANNED_TAG, plannedDate.getXmlValue()));
        } else {
            workitemXMLContainer.addElement(new EcvXmlEmptyElement(WORKITEM_PLANNED_TAG));
        }

        EcvDate actualDate = ACTUAL.getValue();
        if (actualDate != null && actualDate.getXmlValue().isEmpty() == false) {
            workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_ACTUAL_TAG, actualDate.getXmlValue()));
        } else {
            workitemXMLContainer.addElement(new EcvXmlEmptyElement(WORKITEM_ACTUAL_TAG));
        }

        String gpmTaskId = GPJM_ID.getValue();
        if (gpmTaskId != null && gpmTaskId.isEmpty() == false) {
            workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_GPJM_ID_TAG, gpmTaskId));
        } else {
            workitemXMLContainer.addElement(new EcvXmlEmptyElement(WORKITEM_GPJM_ID_TAG));
        }

        workitemXMLContainer.addElement(new EcvXmlTextElement(WORKITEM_LCS, LCS.getValue()));

        return workitemXMLContainer;
    }

}
