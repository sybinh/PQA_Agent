/*
 *
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 *
 */

package DataModel.Acquisition;

import DataModel.DmConstantField_Date;
import DataModel.DmConstantField_Text;
import DataModel.DmValueField_Enumeration;
import DataModel.DmValueField_Text;
import DataModel.Review.DmReview_ReviewDaten;
import DataModel.Xml.DmXmlElementListField;
import Rq1Data.Enumerations.ImpactCategory;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.EcvUserMessage;
import UiSupport.UiTreeViewRootElementI;
import util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author cil83wi
 */

public class DmAcquisition_Project extends DmAcquisition_Element implements UiTreeViewRootElementI {

    final public DmValueField_Text TITLE;
    final public DmValueField_Enumeration PIC_ECU;
    final public DmValueField_Text DESCRIPTION;
    final public DmConstantField_Text USERNAME;
    final public DmConstantField_Date CREATED_DATE;


    final public DmXmlElementListField<DmAcquisition_Milestone> MILESTONES;
    final public DmXmlElementListField<DmAcquisition_PVER_PVAR> PST;
    final public DmXmlElementListField<DmAcquisition_Workitem> WORKITEMS;
    final public DmXmlElementListField<DmAcquisition_Element> TIME_SCHEDULE_OF_PROJECT;

    static private final String ROOT = "Acquisition";
    static private final String PROJECT_CONTAINER = "Project";
    static private final String TITLE_TAG = "Title";
    static private final String PIC_ECU_TAG = "PIC_ECU";
    static private final String DESCRIPTION_TAG = "Description";
    static private final String USERNAME_TAG = "User_Name";
    static private final String CREATED_DATE_TAG = "Created_Date";

    static private final String PROJECT_NAME_FIXED_PORTION = "Acquisition Project";

    private String absolutePath;

    private static final Logger LOGGER = Logger.getLogger(DmAcquisition_Project.class.getCanonicalName());

    public DmAcquisition_Project(String absoultePath) {
        super(PROJECT_NAME_FIXED_PORTION);

        this.absolutePath = absoultePath;

        addField(TITLE = new DmValueField_Text("Title", ""));
        addField(PIC_ECU = new DmValueField_Enumeration("PIC ECU", ImpactCategory.EMPTY, ImpactCategory.values()));
        addField(DESCRIPTION = new DmValueField_Text("Description", ""));

        addField(USERNAME = new DmConstantField_Text("Creator", EcvLoginManager.getCurrentUserFullName()));
        addField(CREATED_DATE = new DmConstantField_Date("Created Date", EcvDate.getToday()));

        addField(MILESTONES = new DmXmlElementListField<>("Open Milestone", new TreeSet<>()));

        addField(PST = new DmXmlElementListField<>("Open PVER & PVAR", new TreeSet<>()));

        addField(WORKITEMS = new DmXmlElementListField<>("Open Workitems", new TreeSet<>()));

        addField(TIME_SCHEDULE_OF_PROJECT = new DmXmlElementListField<>("Time Schedule of Project", new TreeSet<>()));

    }

    public DmAcquisition_Project(EcvXmlContainerElement reviewDaten, String absolutePath) {
        super(PROJECT_NAME_FIXED_PORTION);

        this.absolutePath = absolutePath;

        addField(TITLE = createValueTextField(reviewDaten, PROJECT_CONTAINER, TITLE_TAG, "Project Title"));
        ImpactCategory impactCategoryTemp = ImpactCategory.getByValue(getTextasString(reviewDaten, PROJECT_CONTAINER, PIC_ECU_TAG));
        addField(PIC_ECU = createValueEnumerationField("PIC ECU", impactCategoryTemp, ImpactCategory.values()));
        addField(DESCRIPTION = createValueTextField(reviewDaten, PROJECT_CONTAINER, DESCRIPTION_TAG, "Description"));
        addField(USERNAME = createTextField(reviewDaten, PROJECT_CONTAINER, USERNAME_TAG, "Creator"));
        addField(CREATED_DATE = createDateField(reviewDaten, PROJECT_CONTAINER, CREATED_DATE_TAG, "Datum"));

        addField(MILESTONES = createXmlListField(reviewDaten, "Open_Milestones", "Milestone", "Open Milestones", new XmlElementBuilderI<DmAcquisition_Milestone>() {
            @Override
            public DmAcquisition_Milestone build(EcvXmlContainerElement xmlContainer) {
                String type = "New Milestone";
                return (new DmAcquisition_Milestone(xmlContainer, type));
            }
        }));

        addField(PST = createXmlListField(reviewDaten, "OPEN_PVER_PVAR", "PVERPVAR", "Open PVER & PVAR", new XmlElementBuilderI<DmAcquisition_PVER_PVAR>() {
            @Override
            public DmAcquisition_PVER_PVAR build(EcvXmlContainerElement xmlContainer) {
                return (new DmAcquisition_PVER_PVAR(xmlContainer));
            }
        }));

        addField(WORKITEMS = createXmlListField(reviewDaten, "OPEN_WORKITEMS", "Workitem", "Open Workitems", new XmlElementBuilderI<DmAcquisition_Workitem>() {
            @Override
            public DmAcquisition_Workitem build(EcvXmlContainerElement xmlContainer) {
                return (new DmAcquisition_Workitem(xmlContainer));
            }
        }));

        addField(TIME_SCHEDULE_OF_PROJECT = createXmlListField(reviewDaten, "TIME_SCHEDULE_OF_PROJECT", "Time Schedule of Project", "Time Schedule of Project", new XmlElementBuilderI<DmAcquisition_Element>() {
            @Override
            public DmAcquisition_Element build(EcvXmlContainerElement xmlContainer) {
                return (new DmAcquisition_Workitem(xmlContainer));
            }
        }));

    }

    @Override
    public String getTitle() {

        if (TITLE.getValue().isBlank()) {
            return "NEW Project";
        }

        return TITLE.getValue();
    }

    @Override
    public String getId() {
        return "";
    }

    @Override
    public String getViewTitle() {
        return ("Acquisition Project");
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean save() {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        chooser.setFileFilter(filter);


        chooser.setSelectedFile(
                new File(absolutePath));
        int rueckgabeWert = chooser.showSaveDialog(null);

        if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
            absolutePath = chooser.getSelectedFile().getAbsolutePath();

            if (absolutePath.contains(".xml") == false) {
                absolutePath += ".xml";
            }
            return writeFile(absolutePath, provideAsXml().getXmlString());
        }
        return false;
    }

    public EcvXmlContainerElement provideAsXml() {
        EcvXmlContainerElement result = new EcvXmlContainerElement(ROOT);

        /*
        Project
         */
        EcvXmlContainerElement projectXMLContainer = new EcvXmlContainerElement(PROJECT_CONTAINER);
        result.addElement(projectXMLContainer);

        String tempVar = TITLE.getValue();

        if (tempVar.isEmpty() == false) {
            projectXMLContainer.addElement(new EcvXmlTextElement(TITLE_TAG, TITLE.getValue()));
        } else {
            projectXMLContainer.addElement(new EcvXmlEmptyElement(TITLE_TAG));
        }

        tempVar = PIC_ECU.getValue().getText();
        if (tempVar.isEmpty() == false) {
            projectXMLContainer.addElement(new EcvXmlTextElement(PIC_ECU_TAG, PIC_ECU.getValue().getText()));
        } else {
            projectXMLContainer.addElement(new EcvXmlEmptyElement(PIC_ECU_TAG));
        }

        tempVar = DESCRIPTION.getValue();
        if (tempVar.isEmpty() == false) {
            projectXMLContainer.addElement(new EcvXmlTextElement(DESCRIPTION_TAG, DESCRIPTION.getValue()));
        } else {
            projectXMLContainer.addElement(new EcvXmlEmptyElement(DESCRIPTION_TAG));
        }

        projectXMLContainer.addElement(new EcvXmlTextElement(USERNAME_TAG, USERNAME.getValue()));
        projectXMLContainer.addElement(new EcvXmlTextElement(CREATED_DATE_TAG, CREATED_DATE.getDate().getXmlValue()));

        return result;
    }

    private boolean writeFile(String dir, String content) {
        File xmlFile = new File(dir);
        try {
            xmlFile.createNewFile();
            FileWriter xmlWriter = new FileWriter(dir);
            xmlWriter.write(content);
            xmlWriter.close();
            EcvUserMessage.showMessageDialog("Saving the file locally was successful.", "Saving file was successful", EcvUserMessage.MessageType.INFORMATION_MESSAGE);
            return true;
        } catch (IOException ex) {
            EcvUserMessage.showMessageDialog("The file could not be saved in\n" + dir, "Saving file was not successful", EcvUserMessage.MessageType.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmReview_ReviewDaten.class.getCanonicalName(), ex);
            return false;
        }
    }

}
