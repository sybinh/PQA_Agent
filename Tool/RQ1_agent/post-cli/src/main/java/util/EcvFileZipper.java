/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import ToolUsageLogger.ToolUsageLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author miw83wi
 */
public class EcvFileZipper {

    /**
     * Zips all the files in the file list to the user directory into an archive
     * called diagnosisinformation.zip
     *
     * @param files list of all files, which need to be zipped
     */
    public static boolean zipFileList(List<File> files, File destination) {
        try {

            FileOutputStream fos = new FileOutputStream(destination);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File s : files) {
                FileInputStream fis = new FileInputStream(s);
                ZipEntry zipEntry = new ZipEntry(s.getName());
                zipEntry.setTime(s.lastModified());
                zos.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zos.write(bytes, 0, length);
                }

                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EcvFileZipper.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(EcvFileZipper.class.getName(), ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(EcvFileZipper.class.getName()).log(Level.SEVERE, null, ex);
            ToolUsageLogger.logError(EcvFileZipper.class.getName(), ex);
            return false;
        }

    }

    public static boolean createDiagnosisInfo(File destination) {

        List<File> files = new ArrayList<>();
        File folder = new File("/Users/" + System.getProperty("user.name"));
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().contains("Ipe_") && file.getName().endsWith(".log")) {
                files.add(file);
            }
        }
        Preferences pref = Preferences.userRoot().node("/DgsEcIpe");
        File registry = null;
        try {
            try {
                registry = File.createTempFile("registryexport", ".xml", new File("C:/Users/" + System.getProperty("user.name") + "/"));
                FileOutputStream stream = new FileOutputStream(registry);
                pref.exportSubtree(stream);
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(EcvFileZipper.class.getName()).log(Level.SEVERE, null, ex);
                ToolUsageLogger.logError(EcvFileZipper.class.getName(), ex);
            }
            files.add(registry);
        } catch (BackingStoreException ex) {
            Logger.getLogger(EcvFileZipper.class.getName()).log(Level.SEVERE, "No registry export was created", ex);
            ToolUsageLogger.logError(EcvFileZipper.class.getName(), ex);
        }

        try {
            PrintWriter writer = new PrintWriter("C:/Users/" + System.getProperty("user.name") + "/" + EcvApplication.getToolVersionForLogging() + " " +  EcvDateTime.getNow().getFileNameValue() + ".txt", "UTF-8");
            writer.close();
            File version = new File("C:/Users/" + System.getProperty("user.name") + "/" + EcvApplication.getToolVersionForLogging() + " " + EcvDateTime.getNow().getFileNameValue() + ".txt");
            files.add(version);
        } catch (IOException e) {
            Logger.getLogger(EcvFileZipper.class.getName()).log(Level.SEVERE, "No registry version file was created", e);
            ToolUsageLogger.logError(EcvFileZipper.class.getName(), e);
        }
        boolean zipped = zipFileList(files, destination);

        try {
            if (!registry.delete()) {
                throw new FileDeleteException();
            }
        } catch (FileDeleteException e) {
            Logger.getLogger(EcvFileZipper.class.getName()).log(Level.SEVERE, "Registry Export was not Deleted", e);
            ToolUsageLogger.logError(EcvFileZipper.class.getName(), e);
        }

        return zipped;
    }

    static class FileDeleteException extends Exception {

    }
}
