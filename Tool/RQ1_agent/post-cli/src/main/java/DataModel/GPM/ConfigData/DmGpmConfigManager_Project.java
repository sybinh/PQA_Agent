/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM.ConfigData;

import DataModel.ALM.Fields.DmAlmField_Enumeration;
import DataModel.Rq1.Records.DmRq1Attachment;
import DataModel.Rq1.Records.DmRq1Project;
import RestClient.Exceptions.WriteToDatabaseRejected;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.EcvUserMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import util.EcvDateTime;
import util.EcvParser;
import util.EcvXmlElement;
import util.EcvXmlParser;
import util.UiWorker;

/**
 *
 * @author GUG2WI
 */
public class DmGpmConfigManager_Project {

    final private static Logger LOGGER = Logger.getLogger(DmGpmConfigManager_Project.class.getCanonicalName());

    private static final String PROJECT_SPECIFIC_GPM_ATTACHMENT_NAME = "ProjectSpecificGpmConfig";

    final private static Map<DmRq1Project, DmGpmProjectConfig_Data> projectConfigDataMap = new IdentityHashMap<>();

    public static DmGpmProjectConfig_Data getProjectConfigData(DmRq1Project project) {
        assert (project != null);
        DmGpmProjectConfig_Data projectConfigData = projectConfigDataMap.get(project);
        if (projectConfigData == null) {
            projectConfigData = loadProjectConfigData(project);
            projectConfigDataMap.put(project, projectConfigData);
        }
        return (projectConfigData);
    }

    public static void setProjectConfigData(DmGpmProjectConfig_Data newData, DmRq1Project project) throws IOException, WriteToDatabaseRejected {
        assert (newData != null);
        assert (project != null);

        projectConfigDataMap.put(project, newData);

        int version = 1;
        DmRq1Attachment lastAttachment = getLastAttachment(project);
        if (lastAttachment != null) {
            version = checkAndGetVersionNumber(lastAttachment) + 1;
        }

        LOGGER.info("Create version " + version + " for project " + project.toString());

        String fileName = createAttachmentFileName(version);
        String description = "Project Configuration from " + EcvDateTime.getNow().getUiValue();
        String content = newData.provideAsXml().getXmlString();

        DmRq1Attachment attachment = DmRq1Attachment.create(project, fileName, description, content);

        project.ATTACHMENTS.reload();
    }

    public static void clearCacheForProjectConfigData(DmRq1Project project) {
        assert (project != null);
        projectConfigDataMap.remove(project);
        if (project.ATTACHMENTS.isLoaded()) {
            project.ATTACHMENTS.reload();
        }
    }

    private static DmGpmProjectConfig_Data loadProjectConfigData(DmRq1Project project) {
        assert (project != null);
        UiWorker<DmGpmProjectConfig_Data> worker = new UiWorker<DmGpmProjectConfig_Data>(UiWorker.LOADING) {

            @Override
            protected DmGpmProjectConfig_Data backgroundTask() {
                try {
                    return (load());
                } catch (IOException | EcvParser.ParseException ex) {
                    LOGGER.log(Level.SEVERE, "Error loading project specific GPM configuration file for project " + project.getRq1Id() + ".", ex);
                    ToolUsageLogger.logError(DmGpmConfigManager_Project.class.getCanonicalName(), ex);
                    SwingUtilities.invokeLater(() -> {
                        EcvUserMessage.showMessageDialog("Error loading project specific GPM configuration file for project " + project.getRq1Id() + ".\n\n" + ex.getMessage(), "GPM Config Error", EcvUserMessage.MessageType.ERROR_MESSAGE);
                    });
                    return (new DmGpmProjectConfig_Data());
                }
            }

            private DmGpmProjectConfig_Data load() throws IOException, EcvParser.ParseException {
                DmRq1Attachment attachment = getLastAttachment(project);
                if (attachment != null) {
                    LOGGER.info("Load version " + checkAndGetVersionNumber(attachment) + " for project " + project.toString());
                    //
                    // Load file content
                    //
                    List<byte[]> attachmentContent = attachment.downloadAsByteList();
                    ByteArrayOutputStream attachmentStream = new ByteArrayOutputStream();
                    for (byte[] part : attachmentContent) {
                        attachmentStream.write(part);
                    }
                    byte[] attachmentConcatenated = attachmentStream.toByteArray();
                    InputStream attachmentXml = new ByteArrayInputStream(attachmentConcatenated);

                    //
                    // Parse file content
                    //
                    EcvXmlParser parser = new EcvXmlParser(new InputStreamReader(attachmentXml, "UTF-8"));
                    EcvXmlElement xmlContent = parser.parse();
                    return (new DmGpmProjectConfig_Data(xmlContent));

                }
                return (new DmGpmProjectConfig_Data());
            }
        };
        DmGpmProjectConfig_Data projectConfigData = worker.executeAndWait(worker);
        return (projectConfigData);
    }

    private static DmRq1Attachment getLastAttachment(DmRq1Project project) {
        assert (project != null);

        DmRq1Attachment lastAttachment = null;
        int lastVersion = -1;

        for (DmRq1Attachment attachment : project.ATTACHMENTS.getElementList()) {
            int version = checkAndGetVersionNumber(attachment);
            if (version > lastVersion) {
                lastAttachment = attachment;
                lastVersion = version;
            }
        }

        return (lastAttachment);
    }

    static String createAttachmentFileName(int version) {
        assert (version >= 0);
        return (PROJECT_SPECIFIC_GPM_ATTACHMENT_NAME + "_" + Integer.toString(version) + ".xml");
    }

    static int checkAndGetVersionNumber(DmRq1Attachment attachment) {
        assert (attachment != null);
        return (checkAndGetVersionNumber(attachment.FILENAME.getValue()));
    }

    static int checkAndGetVersionNumber(String attachmentTitle) {
        assert (attachmentTitle != null);

        String[] dotParts = attachmentTitle.split("\\.");
        if (dotParts.length == 2 && dotParts[1].equals("xml")) {
            if (dotParts[0].startsWith(PROJECT_SPECIFIC_GPM_ATTACHMENT_NAME)) {

                String[] underLineParts = dotParts[0].split("_");
                if (underLineParts.length == 1) {
                    return (0);
                } else {
                    try {
                        return (Integer.parseInt(underLineParts[1]));
                    } catch (NumberFormatException ex) {
                        return (-1);
                    }
                }

            }
        }

        return (-1);
    }

}
