/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import DataModel.PPT.Records.DmPPTRecord;
import ToolUsageLogger.ToolUsageLogger;
import UiSupport.EcvUserMessage;
import UiSupport.EcvUserMessage.MessageType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import util.EcvAppendedData;
import util.EcvXmlContainerElement;
import util.EcvXmlElement;
import util.EcvXmlTextElement;

/**
 * Supports the writing of markers to a file.
 *
 * @author gug2wi
 */
public class MarkerExportFile {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MarkerExportFile.class.getCanonicalName());
    //
    final private JFrame parentFrame;
    final private HashSet<Marker> markerList;
    final private HashSet<DmPPTRecord> records;
    final private Map<Marker, EcvAppendedData> appendedDataMap;
    private Path exportFilePath;
    private BufferedWriter exportFile;
    //private EnumSet<DefaultField> activeDefaultFields = EnumSet.allOf(DefaultField.class);
    private List<MarkerWarningExportEnum> activeFields;

    /**
     * Create a export file for which the output file shall be selected by the
     * user.
     *
     * @param parentFrame Parent for the file chooser dialog.
     */
    public MarkerExportFile(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.markerList = new HashSet<>(100);
        this.exportFile = null;
        this.appendedDataMap = new HashMap<>(100);
        this.records = new HashSet<>();
        activeFields = getActiveFields();
    }

    /**
     * Create a export file that writes to the given writer.
     *
     * @param exportFile Target file/stream.
     */
    public MarkerExportFile(BufferedWriter exportFile) {
        this.parentFrame = null;
        this.markerList = new HashSet<>(100);
        this.exportFile = exportFile;
        this.appendedDataMap = new HashMap<>(100);
        this.records = new HashSet<>();
        activeFields = getActiveFields();
    }

    public MarkerExportFile() {
        this.parentFrame = null;
        this.markerList = new HashSet<>(100);
        this.exportFile = null;
        this.appendedDataMap = new HashMap<>(100);
        this.records = new HashSet<>();
        activeFields = getActiveFields();
    }

    /*
     public void setActiveDefaultFields(EnumSet<DefaultField> activeDefaultFields) {
     assert(activeDefaultFields != null);
     this.activeDefaultFields = activeDefaultFields;
     } 
     */
    /**
     * Lets the user choose the output file.
     *
     * @return true, if a file was selected; false, if the user did not choose a
     * file.
     */
    public boolean openFile() {

        //Check if it got canceled
        if (!isCanceled()) {

            JFileChooser fileChooser = new JFileChooser();

            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    File p = pathname;
                    if ((p != null) && (p.isDirectory())) {
                        String s = p.toString();
                        if (s != null) {
                            if (s.toLowerCase().endsWith(".xml")) {
                                return true;
                            }
                        }
                    }
                    return (false);
                }

                @Override
                public String getDescription() {
                    return ("XML files");
                }
            });

            if (fileChooser.showSaveDialog(new java.awt.Frame()) == JFileChooser.APPROVE_OPTION) {

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
                            MessageType.QUESTION_MESSAGE);

                    if (overWrite == JOptionPane.YES_OPTION) {
                        try {
                            exportFile = Files.newBufferedWriter(exportFilePath,
                                    Charset.forName("UTF8"),
                                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                            return (true);

                        } catch (java.nio.file.AccessDeniedException ex) {
                            logger.log(Level.SEVERE, "Access denided to file " + exportFilePath.toString(), ex);
                            ToolUsageLogger.logError(MarkerExportFile.class.getCanonicalName(), ex);
                            EcvUserMessage.showMessageDialog(
                                    "Access denied to " + exportFilePath.toString(),
                                    "File Access Error",
                                    MessageType.WARNING_MESSAGE);
                            return (false);

                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, "Cannot write to file " + exportFilePath.toString(), ex);
                            ToolUsageLogger.logError(MarkerExportFile.class.getCanonicalName(), ex);
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
                    ToolUsageLogger.logError(MarkerExportFile.class.getCanonicalName(), ex);
                    EcvUserMessage.showMessageDialog(
                            "Access denided to " + exportFilePath.toString(),
                            "File Access Error",
                            MessageType.WARNING_MESSAGE);
                    return (false);

                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Couldn't create file " + exportFilePath.toString(), ex);
                    ToolUsageLogger.logError(MarkerExportFile.class.getCanonicalName(), ex);
                    EcvUserMessage.showMessageDialog(
                            "Couldn't create " + exportFilePath.toString() + "\n\n" + ex.getMessage(),
                            "File Access Error",
                            MessageType.WARNING_MESSAGE);
                    return (false);
                }
            } else {
                return (false);
            }
        } else {
            return (false);
        }
    }

    /**
     * Is used by background processes
     *
     * @param bufferedWriter => used for the output
     * @return true
     */
    public boolean openFile(BufferedWriter bufferedWriter) {
        this.activeFields = getActiveFields();
        this.exportFile = bufferedWriter;
        return true;
    }

    /**
     * Adds all markers on the given markable to the marker file.
     *
     * @param markable MarkableI object whose marks shall be added to the file.
     */
    public void addMarkers(MarkableI markable) {
        assert (markable != null);
        assert (exportFile != null);
        markerList.addAll(markable.getFailures());
        markerList.addAll(markable.getHints());
        markerList.addAll(markable.getToDos());
        markerList.addAll(markable.getWarnings());
    }

    /**
     * Add all given Markers of the container Element with the additional
     * Information
     *
     * @param markable Container of the markers
     * @param appendedData The additional Information to the markable Element
     */
    public void addMarkersAndAppendedData(MarkableI markable, EcvAppendedData appendedData) {
        assert (markable != null);
        assert (exportFile != null);
        for (Marker mark : markable.getFailures()) {
            this.addMark(mark, appendedData);
        }
        for (Marker mark : markable.getHints()) {
            this.addMark(mark, appendedData);
        }
        for (Marker mark : markable.getToDos()) {
            this.addMark(mark, appendedData);
        }
        for (Marker mark : markable.getWarnings()) {
            this.addMark(mark, appendedData);
        }
    }

    /**
     * Adds the given marker to the file.
     *
     * @param marker Marker that shall be added to the file.
     * @param appendedData Data that shall be appended to the Marker data.
     */
    public void addMark(Marker marker, EcvAppendedData appendedData) {
        assert (marker != null);
        assert (exportFile != null);
        markerList.add(marker);
        if (appendedData != null) {
            appendedDataMap.put(marker, appendedData);
        }
    }

    public void addPPTRecords(Collection<? extends DmPPTRecord> loadData) {
        for (DmPPTRecord record : loadData) {
            this.addPPTRecord(record);
        }
    }

    public void addPPTRecord(DmPPTRecord record) {
        assert (record != null);
        assert (exportFile != null);
        if (!records.contains(record)) {
            records.add(record);
        }
    }

    /**
     * Writes the added markers to the output file and closes the output file.
     */
    public void finishFile() {
        assert (exportFile != null);

        //Go through Record List
        for (DmPPTRecord record : records) {
            try {
            addMarkersAndAppendedData(record, record.getEcvAppendedData());
            } catch (NullPointerException ex) {
                logger.warning("For the given record " + record.getId() + " there was a Nullpointer"
                        + " Exception by getting the EcvAppended Data");
        }
        }

        //
        // Create file content
        //
        EcvXmlContainerElement fileContent = new EcvXmlContainerElement("FileContent");
        for (Marker marker : markerList) {
            if (marker.markerForExport() == true) {
                EcvXmlContainerElement xmlMark = new EcvXmlContainerElement("Marker");
                if (activeFields == null) {
                    activeFields = getActiveFields();
                }
                for (MarkerWarningExportEnum e : activeFields) {
                    if (e.getActivated()) {
                        if (!e.extractValue(marker).isEmpty()) {
                            xmlMark.addElement(new EcvXmlTextElement(e.getValue(), e.extractValue(marker)));
                        }
                    }
                }

                if (appendedDataMap.containsKey(marker)) {
                    for (EcvXmlTextElement element : appendedDataMap.get(marker).getEcvXmlTextElementList()) {
                        xmlMark.addElement(element);
                    }
                }

                fileContent.addElement(xmlMark);
            }
        }

        //
        // Write content to file
        //
        String s = fileContent.getXmlString(EcvXmlElement.EncodeFormat.LONG_EMPTY_VALUE);
        try {
            exportFile.write(s);
            exportFile.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Access failed to file " + exportFilePath.toString(), ex);
            ToolUsageLogger.logError(MarkerExportFile.class.getCanonicalName(), ex);
            EcvUserMessage.showMessageDialog(
                    "Access failed to " + exportFilePath.toString(),
                    "File Access Error",
                    MessageType.WARNING_MESSAGE);
        }
    }

    public List<MarkerWarningExportEnum> getActiveFields() {
        return (Arrays.asList(MarkerWarningExportEnum.values()));
    }

    public boolean isCanceled() {
        return false;
    }
}
