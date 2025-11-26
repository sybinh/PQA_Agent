/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Review;

import DataModel.Xml.DmXmlElementListField;
import DataModel.DmConstantField_Date;
import DataModel.DmConstantField_Text;
import DataModel.DmElementField_ReadOnlyI;
import DataModel.DmValueField_Enumeration;
import DataModel.DmValueField_Text;
import DataModel.GPM.DmGpmMilestone;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataModel.Rq1.Records.DmRq1Project;
import ToolUsageLogger.ToolUsageLogger;
import util.EcvLoginManager;
import RestClient.Exceptions.NoLoginDataException;
import SharePoint.SPCommandExecutorUser;
import UiSupport.EcvUserMessage;
import UiSupport.UiTreeViewRootElementI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.EcvDate;
import util.EcvEnumeration;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlEmptyElement;
import util.EcvXmlTextElement;

/**
 * @author cil83wi
 */
public class DmReview_ReviewDaten extends DmReview_Element implements UiTreeViewRootElementI {

    final public DmValueField_Text FILE_PATH;
    final public DmValueField_Text FILE_NAME;
    final public DmConstantField_Text CREATED_NAME;
    final public DmConstantField_Date CREATED_DATE;

    final public DmConstantField_Text PROJECT_ID;
    final public DmConstantField_Text PROJECT_TITLE;
    final public DmConstantField_Text PROJECT_PIC_ECU;
    final public DmElementField_ReadOnlyI<DmRq1ElementInterface> PROJECT;

    final public DmXmlElementListField<DmReview_Milestone> MILESTONE;

    final public DmValueField_Enumeration FULLFILLED_Q1;
    final public DmValueField_Text COMMENT_Q1;

    final public DmValueField_Enumeration FULLFILLED_Q2;
    final public DmValueField_Text COMMENT_Q2;

    final public DmValueField_Enumeration FULLFILLED_Q3;
    final public DmValueField_Text COMMENT_Q3;

    //Container
    static private final String ROOT = "Review";
    static private final String MILESTONE_CONTAINER = "Milestone_On_Project";
    static private final String REVIEWER_CONTAINER = "Reviewer";
    static private final String PROJECT_CONTAINER = "Project";
    static private final String REVIEW_CONCLUDED_CONTAINER = "Review_Concluded";

    //
    static private final String REVIEW_CONCLUDED_FULLFILLED_Q1_TAG = "Fullfilled_Q1";
    static private final String REVIEW_CONCLUDED_COMMENT_Q1_TAG = "Comment_Q1";
    static private final String REVIEW_CONCLUDED_FULLFILLED_Q2_TAG = "Fullfilled_Q2";
    static private final String REVIEW_CONCLUDED_COMMENT_Q2_TAG = "Comment_Q2";
    static private final String REVIEW_CONCLUDED_FULLFILLED_Q3_TAG = "Fullfilled_Q3";
    static private final String REVIEW_CONCLUDED_COMMENT_Q3_TAG = "Comment_Q3";

    //Reviewer
    static private final String REVIEWER_NAME_TAG = "Reviewer_Name";
    static private final String REVIEWER_DATE_TAG = "Reviewer_Date";

    //Project
    static private final String PROJECT_ID_TAG = "Project_ID";
    static private final String PROJECT_TITLE_TAG = "Project_Title";
    static private final String PROJECT_PCU_ID_TAG = "Project_PIC_ECU";

    private ReviewDestination saveReviewDestination = ReviewDestination.NODESTINATION;

    private SPCommandExecutorUser sharepointConnection = null;

    private String filename;
    private boolean reviewValid = false;

    private static final Logger LOGGER = Logger.getLogger(DmReview_ReviewDaten.class.getCanonicalName());

    public enum ReviewDestination {
        NODESTINATION,
        CREATE;
    }

    ;

    public enum Fullfilled implements EcvEnumeration {

        EMPTY("") {
            @Override
            public String toString() {
                return ("<Empty>");
            }
        },
        YES("Yes"),
        NO("No");

        private final String fullfilledState;

        private Fullfilled(String fullfilledState) {
            this.fullfilledState = fullfilledState;
        }

        @Override
        public String getText() {
            return fullfilledState;
        }

        @Override
        public String toString() {
            return getText();
        }

        public static Fullfilled getEnumForValue(String value) {

            for (Fullfilled possibleValue : Fullfilled.values()) {
                if (possibleValue.getText().equals(value)) {
                    return possibleValue;
                }
            }
            return Fullfilled.EMPTY;

        }

    }

    public DmReview_ReviewDaten(EcvXmlContainerElement reviewDaten, String fullPath, ReviewDestination destination) {
        super("Review of Time Schedule of Project");
        assert (reviewDaten != null);
        assert (fullPath != null);

        this.saveReviewDestination = destination;

        addField(FILE_PATH = new DmValueField_Text("Path", fullPath));
        addField(CREATED_NAME = createTextField(reviewDaten, REVIEWER_CONTAINER, REVIEWER_NAME_TAG, "Reviewer"));
        addField(CREATED_DATE = createDateField(reviewDaten, REVIEWER_CONTAINER, REVIEWER_DATE_TAG, "Datum"));

        addField(PROJECT_ID = createTextField(reviewDaten, PROJECT_CONTAINER, PROJECT_ID_TAG, "Project ID"));
        addField(PROJECT_TITLE = createTextField(reviewDaten, PROJECT_CONTAINER, PROJECT_TITLE_TAG, "Project Title"));
        addField(PROJECT_PIC_ECU = createTextField(reviewDaten, PROJECT_CONTAINER, PROJECT_PCU_ID_TAG, "PIC ECU ID"));
        addField(PROJECT = new DmReview_Rq1ElementField(PROJECT_ID, "Project"));

        filename = PROJECT_TITLE.getValue() + "_schedule.xml";
        filename = filename.replace(" ", "");
        filename = filename.replace("\"", "");
        filename = filename.replace("#", "");
        filename = filename.replace("%", "");
        filename = filename.replace("*", "");
        filename = filename.replace(":", "");
        filename = filename.replace("<", "");
        filename = filename.replace(">", "");
        filename = filename.replace("?", "");
        filename = filename.replace("/", "");
        filename = filename.replace("\\", "");
        filename = filename.replace("|", "");
        addField(FILE_NAME = new DmValueField_Text("Filename", fullPath + "/" + filename));

        Fullfilled fullfilledTemp = Fullfilled.getEnumForValue(getTextasString(reviewDaten, REVIEW_CONCLUDED_CONTAINER, REVIEW_CONCLUDED_FULLFILLED_Q1_TAG));
        addField(FULLFILLED_Q1 = createValueEnumerationField("Fullfilled Q1?", fullfilledTemp, Fullfilled.values()));
        addField(COMMENT_Q1 = createValueTextField(reviewDaten, REVIEW_CONCLUDED_CONTAINER, REVIEW_CONCLUDED_COMMENT_Q1_TAG, "Comments / Link to the open point in the project OPL Q1"));

        fullfilledTemp = Fullfilled.getEnumForValue(getTextasString(reviewDaten, REVIEW_CONCLUDED_CONTAINER, REVIEW_CONCLUDED_FULLFILLED_Q2_TAG));
        addField(FULLFILLED_Q2 = createValueEnumerationField("Fullfilled Q2?", fullfilledTemp, Fullfilled.values()));
        addField(COMMENT_Q2 = createValueTextField(reviewDaten, REVIEW_CONCLUDED_CONTAINER, REVIEW_CONCLUDED_COMMENT_Q2_TAG, "Comments / Link to the open point in the project OPL Q2"));

        fullfilledTemp = Fullfilled.getEnumForValue(getTextasString(reviewDaten, REVIEW_CONCLUDED_CONTAINER, REVIEW_CONCLUDED_FULLFILLED_Q3_TAG));
        addField(FULLFILLED_Q3 = createValueEnumerationField("Fullfilled Q3?", fullfilledTemp, Fullfilled.values()));
        addField(COMMENT_Q3 = createValueTextField(reviewDaten, REVIEW_CONCLUDED_CONTAINER, REVIEW_CONCLUDED_COMMENT_Q3_TAG, "Comments / Link to the open point in the project OPL Q3"));

        addField(MILESTONE = createXmlListField(reviewDaten, "Milestone_On_Project", "Milestone", "Time Schedule of Project", new XmlElementBuilderI<DmReview_Milestone>() {
            @Override
            public DmReview_Milestone build(EcvXmlContainerElement xmlContainer) {
                String type;
                try {
                    type = xmlContainer.getText(DmReview_Milestone.MILESTONE_TYPE_TAG);
                } catch (EcvXmlElement.NotfoundException ex) {
                    LOGGER.log(Level.SEVERE, "Type of milestone not found.", ex);
                    ToolUsageLogger.logError(DmReview_ReviewDaten.class.getCanonicalName(), ex);
                    type = "Milestone";
                }

                return (new DmReview_Milestone(xmlContainer, type));
            }
        }));
    }

    @Override
    public String getViewTitle() {
        return (getElementType() + ": " + getTitle());
    }

    @Override
    public String getTitle() {
        return (FILE_NAME.getValueAsText());
    }

    @Override
    public String getId() {
        return (CREATED_NAME.getValue() + "-" + CREATED_DATE.getValue().getXmlValue());
    }

    @Override
    public String toString() {
        return (getElementType() + ": " + CREATED_NAME.getValueAsText() + " " + CREATED_DATE.getValue().getXmlValue());
    }

    public static EcvXmlContainerElement createXMLReviewForProject(DmRq1Project project) {

        EcvXmlContainerElement result = new EcvXmlContainerElement(ROOT);

        /*
        Reviewer
         */
        EcvXmlContainerElement reviewer = new EcvXmlContainerElement(REVIEWER_CONTAINER);
        result.addElement(reviewer);
        try {
            reviewer.addElement(new EcvXmlTextElement(REVIEWER_NAME_TAG, EcvLoginManager.getFirstLoginData().getLoginName()));
        } catch (NoLoginDataException ex) {
            try {
                reviewer.addElement(new EcvXmlTextElement(REVIEWER_NAME_TAG, EcvLoginManager.getNextLoginData().getLoginName()));
            } catch (NoLoginDataException ex1) {
                LOGGER.log(Level.WARNING, "No name for the reviewer found ", ex1);
            }
        }
        reviewer.addElement(new EcvXmlTextElement(REVIEWER_DATE_TAG, EcvDate.getToday().getXmlValue()));

        /*
        Project
         */
        result.addElement(getDataAsXMLForProject(project));

        /*    
        Creation of milestone - Workitem    
         */
        EcvXmlContainerElement xmlMilestones = new EcvXmlContainerElement(MILESTONE_CONTAINER);
        result.addElement(xmlMilestones);

        for (DmGpmMilestone dmGpmMilestone : project.TIME_SCHEDULE_OF_PROJECT.getElementList()) {
            xmlMilestones.addElement(DmReview_Milestone.getMilestoneDataAsXmlForReview(dmGpmMilestone));
        }

        return result;

    }

    static private EcvXmlContainerElement getDataAsXMLForProject(DmRq1Project project) {

        EcvXmlContainerElement projectXMLContainer = new EcvXmlContainerElement(PROJECT_CONTAINER);
        projectXMLContainer.addElement(new EcvXmlTextElement(PROJECT_ID_TAG, project.getId()));
        projectXMLContainer.addElement(new EcvXmlTextElement(PROJECT_TITLE_TAG, project.getTitle()));
        projectXMLContainer.addElement(new EcvXmlTextElement(PROJECT_PCU_ID_TAG, project.IMPACT_CATEGORY.getValueAsText()));

        return projectXMLContainer;
    }

    public EcvXmlContainerElement createXMLFromConcludedReview() {

        EcvXmlContainerElement result = new EcvXmlContainerElement(ROOT);

        /*
        Reviewer
         */
        EcvXmlContainerElement reviewer = new EcvXmlContainerElement(REVIEWER_CONTAINER);
        result.addElement(reviewer);

        reviewer.addElement(new EcvXmlTextElement(REVIEWER_NAME_TAG, CREATED_NAME.getValue()));

        reviewer.addElement(new EcvXmlTextElement(REVIEWER_DATE_TAG, CREATED_DATE.getDate().getXmlValue()));

        /*
        Project
         */
        EcvXmlContainerElement projectXMLContainer = new EcvXmlContainerElement(PROJECT_CONTAINER);
        projectXMLContainer.addElement(new EcvXmlTextElement(PROJECT_ID_TAG, PROJECT_ID.getValue()));
        projectXMLContainer.addElement(new EcvXmlTextElement(PROJECT_TITLE_TAG, PROJECT_TITLE.getValue()));
        projectXMLContainer.addElement(new EcvXmlTextElement(PROJECT_PCU_ID_TAG, PROJECT_PIC_ECU.getValue()));
        result.addElement(projectXMLContainer);
        /*
        Workproduct Review 
         */
        EcvXmlContainerElement previewConcludedContainer = new EcvXmlContainerElement(REVIEW_CONCLUDED_CONTAINER);
        previewConcludedContainer.addElement(new EcvXmlTextElement(REVIEW_CONCLUDED_FULLFILLED_Q1_TAG, FULLFILLED_Q1.getValueAsText()));
        if (COMMENT_Q1.getValue().isEmpty() == false) {
            previewConcludedContainer.addElement(new EcvXmlTextElement(REVIEW_CONCLUDED_COMMENT_Q1_TAG, COMMENT_Q1.getValue()));
        } else {
            previewConcludedContainer.addElement(new EcvXmlEmptyElement(REVIEW_CONCLUDED_COMMENT_Q1_TAG));
        }
        previewConcludedContainer.addElement(new EcvXmlTextElement(REVIEW_CONCLUDED_FULLFILLED_Q2_TAG, FULLFILLED_Q2.getValueAsText()));
        if (COMMENT_Q2.getValue().isEmpty() == false) {
            previewConcludedContainer.addElement(new EcvXmlTextElement(REVIEW_CONCLUDED_COMMENT_Q2_TAG, COMMENT_Q2.getValue()));
        } else {
            previewConcludedContainer.addElement(new EcvXmlEmptyElement(REVIEW_CONCLUDED_COMMENT_Q2_TAG));
        }
        previewConcludedContainer.addElement(new EcvXmlTextElement(REVIEW_CONCLUDED_FULLFILLED_Q3_TAG, FULLFILLED_Q3.getValueAsText()));
        if (COMMENT_Q3.getValue().isEmpty() == false) {
            previewConcludedContainer.addElement(new EcvXmlTextElement(REVIEW_CONCLUDED_COMMENT_Q3_TAG, COMMENT_Q3.getValue()));
        } else {
            previewConcludedContainer.addElement(new EcvXmlEmptyElement(REVIEW_CONCLUDED_COMMENT_Q3_TAG));
        }

        result.addElement(previewConcludedContainer);

        /*    
        Creation of milestone - Workitem    
         */
        EcvXmlContainerElement xmlMilestones = new EcvXmlContainerElement(MILESTONE_CONTAINER);
        result.addElement(xmlMilestones);

        for (DmReview_Milestone milestone : MILESTONE.getElementList()) {
            xmlMilestones.addElement(milestone.getReviewMilestoneDataAsXml());
        }

        return result;

    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean save() {

        String messageHead = "These fields are mandatory:\n\n";
        StringBuilder messageBody = new StringBuilder();
        if (FULLFILLED_Q1.getValue().equals(Fullfilled.EMPTY)) {
            messageBody.append("Fullfilled Q1?\n");
        }
        if (FULLFILLED_Q2.getValue().equals(Fullfilled.EMPTY)) {
            messageBody.append("Fullfilled Q2?\n");
        }
        if (FULLFILLED_Q3.getValue().equals(Fullfilled.EMPTY)) {
            messageBody.append("Fullfilled Q3?\n");
        }
        String messageFoot = "\nNo concluded review will be created.\n";
        if (messageBody.length() == 0) {

            reviewValid = FULLFILLED_Q1.getValue().equals(Fullfilled.YES) && FULLFILLED_Q2.getValue().equals(Fullfilled.YES) && FULLFILLED_Q3.getValue().equals(Fullfilled.YES);

            saveRoutine();
            return true;
        }
        EcvUserMessage.showMessageDialog(messageHead + messageBody.toString() + messageFoot, "Some mandatory fields are not filled", EcvUserMessage.MessageType.ERROR_MESSAGE);
        return false;
    }

    private void saveRoutine() {

        if (sharepointConnection == null) {
            localSaveRoutine();
        } else {
            sharepointSaveRoutine();
        }
        fireChange();
    }

    private void localSaveRoutine() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
        chooser.setFileFilter(filter);

        chooser.setSelectedFile(
                new File(chooser.getCurrentDirectory().getAbsolutePath()
                        + "\\" + filename));
        int rueckgabeWert = chooser.showSaveDialog(null);

        if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {

            filename = chooser.getSelectedFile().getName();
            String dir;

            if (filename.contains(".xml") == false) {
                filename = filename.trim() + ".xml";
            }

            dir = chooser.getCurrentDirectory().toString() + "\\" + filename;

            writeFile(dir, createXMLFromConcludedReview().getXmlString());

        }
    }

    private void sharepointSaveRoutine() {

        String xmlStructure = createXMLFromConcludedReview().getXmlString();

        if (xmlStructure != null) {
            try {
                String tempBase = sharepointConnection.getBaseSiteUrl();
                String tempRelative = sharepointConnection.getRelativeSiteUrl();
                String requestUrl = tempBase + tempRelative + "/_api/web/GetFolderByServerRelativeUrl('" + tempRelative + "/Documents" + FILE_PATH.getValueAsText() + "')/Files/add(url='" + filename + "',overwrite=true)";
                int statusCode = sharepointConnection.performPost(requestUrl, sharepointConnection.getToken(tempRelative + "/_api/contextinfo"), xmlStructure);
                if (statusCode != 200) {
                    EcvUserMessage.showMessageDialog("Writing to sharepoint failed", "Uploading failed", EcvUserMessage.MessageType.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Writing the review to sharepoint failed", ex);
                ToolUsageLogger.logError(DmReview_ReviewDaten.class.getCanonicalName(), ex);
            }
        }
        createRevision();

    }

    private void createRevision() {
        int statusCode;
        statusCode = requestConceptualVersionItemIdAndRevisionSetUrl();
        if (statusCode != 200 && statusCode != 204) {
            EcvUserMessage.showMessageDialog("Requested file could not be accessed.", "Sharepoint operation failed", EcvUserMessage.MessageType.ERROR_MESSAGE);
            return;
        }
        statusCode = checkoutFile();
        if (statusCode != 200) {
            EcvUserMessage.showMessageDialog("Check out of file failed.", "Sharepoint operation failed", EcvUserMessage.MessageType.ERROR_MESSAGE);
            return;
        }
        statusCode = createRevisionForFile();
        if (statusCode != 200) {
            EcvUserMessage.showMessageDialog("Creation of revision for review failed", "Sharepoint operation failed", EcvUserMessage.MessageType.ERROR_MESSAGE);
            return;
        }
        EcvUserMessage.showMessageDialog("Creation of revision for review was successful", "Revision created successful", EcvUserMessage.MessageType.INFORMATION_MESSAGE);
    }

    public int requestConceptualVersionItemIdAndRevisionSetUrl() {
        return sharepointConnection.requestConceptualVersionItemIdAndRevisionSetUrl(FILE_PATH.getValueAsText(), filename);
    }

    public int checkoutFile() {
        return sharepointConnection.checkoutFile(FILE_PATH.getValueAsText(), filename);
    }

    public int createRevisionForFile() {
        return sharepointConnection.createRevisionForFile(FILE_PATH.getValueAsText(), filename, reviewValid);
    }

    private void writeFile(String dir, String content) {
        File xmlFile = new File(dir);
        try {
            xmlFile.createNewFile();
            FileWriter xmlWriter = new FileWriter(dir);
            xmlWriter.write(content);
            xmlWriter.close();
            EcvUserMessage.showMessageDialog("Saving the file locally was successful.", "Saving file was successful", EcvUserMessage.MessageType.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            EcvUserMessage.showMessageDialog("The file could not be saved in\n" + dir, "Saving file was not successful", EcvUserMessage.MessageType.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(DmReview_ReviewDaten.class.getCanonicalName(), ex);
        }
    }

    public void updatePathVariables(String path, SPCommandExecutorUser connection) {

        this.sharepointConnection = connection;
        FILE_PATH.setValue(path);
        filename = PROJECT_TITLE.getValue() + "_schedule.xml";
        filename = filename.replace(" ", "");
        filename = filename.replace("\"", "");
        filename = filename.replace("#", "");
        filename = filename.replace("%", "");
        filename = filename.replace("*", "");
        filename = filename.replace(":", "");
        filename = filename.replace("<", "");
        filename = filename.replace(">", "");
        filename = filename.replace("?", "");
        filename = filename.replace("/", "");
        filename = filename.replace("\\", "");
        filename = filename.replace("|", "");
        FILE_NAME.setValue(path + "/" + filename);
        saveReviewDestination = ReviewDestination.CREATE;

    }

    public ReviewDestination getSaveReviewDestination() {
        return saveReviewDestination;
    }

    public void setSharepointConnection(SPCommandExecutorUser sharepointConnection) {
        this.sharepointConnection = sharepointConnection;
    }

}