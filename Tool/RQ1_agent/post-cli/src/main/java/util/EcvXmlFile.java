/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import ToolUsageLogger.ToolUsageLogger;
import UiSupport.EcvUserMessage;
import UiSupport.EcvUserMessage.MessageType;
import UiSupport.UiThreadChecker;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author gug2wi
 */
public class EcvXmlFile {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EcvXmlFile.class.getCanonicalName());
    //
    private final JFrame parentFrame;
    private EcvXmlContainerElement fileContent;
    private Path exportFilePath;
    private BufferedWriter exportFile;

    public EcvXmlFile(JFrame jFrame) {
        assert (jFrame != null);
        fileContent = null;
        exportFilePath = null;
        exportFile = null;
        this.parentFrame = jFrame;
    }

    final public boolean openFileForWrite() {
        UiThreadChecker.ensureDispatchThread();

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                //It sometimes happens, that pathname.toString() returns null
                if (pathname != null && pathname.toString() != null) {
                    if ((pathname.isDirectory()) || (pathname.toString().toLowerCase().endsWith(".xml") == true)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return ("XML files");
            }
        });

        if (fileChooser.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            if (file.getName().toLowerCase().endsWith(".xml") == false) {
                file = new File(file.toString() + ".xml");
            }
            exportFilePath = file.toPath();

            try {
                exportFile = Files.newBufferedWriter(exportFilePath,
                        Charset.forName("UTF8"),
                        StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND);
                return (true);

            } catch (FileAlreadyExistsException x) {
                int overWrite = EcvUserMessage.showConfirmDialog(
                        "File " + exportFilePath.toString() + " exists already.\n\nOverwrite file ?",
                        "File Exists",
                        JOptionPane.YES_NO_OPTION,
                        MessageType.WARNING_MESSAGE);

                if (overWrite == JOptionPane.YES_OPTION) {
                    try {
                        exportFile = Files.newBufferedWriter(exportFilePath,
                                Charset.forName("UTF8"),
                                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                        return (true);

                    } catch (java.nio.file.AccessDeniedException ex) {
                        logger.log(Level.SEVERE, "Access denided to file " + exportFilePath.toString(), ex);
                        ToolUsageLogger.logError(EcvXmlFile.class.getCanonicalName(), ex);
                        EcvUserMessage.showMessageDialog(
                                "Access denied to " + exportFilePath.toString(),
                                "File Access Error",
                                MessageType.WARNING_MESSAGE);
                        return (false);

                    } catch (IOException ex) {
                        logger.log(Level.SEVERE, "Cannot write to file " + exportFilePath.toString(), ex);
                        ToolUsageLogger.logError(EcvXmlFile.class.getCanonicalName(), ex);
                        EcvUserMessage.showMessageDialog(
                                "Cannot write to " + exportFilePath.toString(),
                                "File Access Error",
                                MessageType.WARNING_MESSAGE);
                        return (false);
                    }

                } else {
                    return (false);
                }

            } catch (java.nio.file.AccessDeniedException ex) {
                logger.log(Level.SEVERE, "Access denided to file " + exportFilePath.toString(), ex);
                ToolUsageLogger.logError(EcvXmlFile.class.getCanonicalName(), ex);
                EcvUserMessage.showMessageDialog(
                        "Access denided to " + exportFilePath.toString(),
                        "File Access Error",
                        MessageType.WARNING_MESSAGE);
                return (false);

            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Couldn't create file " + exportFilePath.toString(), ex);
                ToolUsageLogger.logError(EcvXmlFile.class.getCanonicalName(), ex);
                EcvUserMessage.showMessageDialog(
                        "Couldn't create " + exportFilePath.toString() + "\n\n" + ex.getMessage(),
                        "File Access Error",
                        MessageType.WARNING_MESSAGE);
                return (false);
            }
        } else {
            return (false);
        }
    }

    final public void setContent(EcvXmlContainerElement newContent) {
        assert (newContent != null);
        fileContent = newContent;
    }

    final public void writeFile() {
        assert (fileContent != null);
        assert (exportFile != null);
        assert (exportFilePath != null);

        //
        // Write content to file
        //
        String s = fileContent.getXmlString(EcvXmlElement.EncodeFormat.LONG_EMPTY_VALUE);
        try {
            exportFile.write(s);
            exportFile.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Access failed to file " + exportFilePath.toString(), ex);
            ToolUsageLogger.logError(EcvXmlFile.class.getCanonicalName(), ex);
            EcvUserMessage.showMessageDialog(
                    "Access failed to " + exportFilePath.toString(),
                    "File Access Error",
                    MessageType.WARNING_MESSAGE);
        }

    }
}
