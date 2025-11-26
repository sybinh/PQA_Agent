/*
 *  Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 *  This program and the accompanying materials are made available under
 *  the terms of the Bosch Internal Open Source License v4
 *  which accompanies this distribution, and is available at
 *  http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * This class provides a utility to open RQONE elements in IPE.
 *
 * @author PMR83WI
 */
public final class IPEUtility {

    private static String pathToIpe;

    private IPEUtility() {
    }

    /**
     * Initializes the Utility by checking if IPE is installed on the system and
     * setting the path.
     */
    public static void init() {
        if (!isIpeInstalled()) {
            //check if IPE is installed and search for it once
            findIpe();
        }
    }

    /**
     * Returns the path IPE is installed to.
     *
     * @return IPE's path
     */
    public static String getIpePath() {
        return isIpeInstalled() ? pathToIpe : "";
    }

    /**
     * Checks if IPE is installed on the system and saves IPE's path.
     *
     * @return true if IPE was found on the system, else false
     */
    private static boolean findIpe() {
        String ipeName = "ipe.cmd";
        File[] files = File.listRoots();
        for (File root : files) {
            String rootPath = root.getAbsolutePath();
            if (rootPath.contains("C:")) {
                //only check local folder
                String path = findFile(rootPath + "toolbase", ipeName);
                //toolbase installs IPE in C:\toolbase\
                if (path.contains(ipeName)) {
                    pathToIpe = path;
                    //search for map file, stored in C:\temp\
                    path = findFile(rootPath + "temp", "IPE.map");
                    if (!path.contains("IPE.map")) {
                        //create MAP file IPE needs for launch
                        final String mapFileDir = "C:" + File.separator + "temp";
                        File file = new File(mapFileDir);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        Path writePath = FileSystems.getDefault().getPath(mapFileDir + File.separator + "IPE.map");
                        try (BufferedWriter outputWriter = Files.newBufferedWriter(writePath, StandardCharsets.UTF_8)) {
                            outputWriter.write("-------------");// 13x - char
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether IPE is installed on the system.
     *
     * @return true if IPE is installed with toolbase, else false
     */
    public static boolean isIpeInstalled() {
        return pathToIpe != null;
    }

    /**
     * This function recursively goes through all files and directories of the
     * parent directory and returns the first file that matches fileName.
     *
     * @param directory the parent directory to search
     * @param fileName the filename to search for
     * @return
     */
    private static String findFile(File directory, String fileName) {
        String tmp = "";
        if (directory.list() != null) {
            for (String file : directory.list()) {
                String path = directory.getAbsolutePath();
                if (!path.substring(path.length() - 1).equals(File.separator)) {
                    path += File.separator;
                }
                path += file;
                File f = new File(path);
                if (f.isDirectory() && !file.contains(fileName)) {
                    tmp = findFile(f, fileName);
                    if (tmp.contains(fileName)) {
                        return tmp;
                    }
                } else if (file.contains(fileName)) {
                    return f.getAbsolutePath();
                }
            }
        }
        return tmp;
    }

    /**
     * This function recursively goes through all files and directories of the
     * parent directory and returns the first file that matches fileName.
     *
     * @param directory the parent directory to search
     * @param fileName the filename to search for
     * @return
     */
    private static String findFile(String directory, String fileName) {
        return findFile(new File(directory), fileName);
    }

    /**
     * Opens an element with the id rq1Id in IPE. Deprecated, use openInIpe()
     * instead.
     *
     * @param rq1Id a RQONE id
     */
    @Deprecated
    public static void openInIpeViaJava(String rq1Id) {
        if (!isIpeInstalled()) {
            //check if IPE is installed and search for it once
            findIpe();
        }
        String[] cmdarr = new String[4];
        cmdarr[0] = "java";
        cmdarr[1] = "-jar";
        cmdarr[2] = pathToIpe;
        cmdarr[3] = rq1Id;
        try {
            if (isIpeOpen()) {
                Runtime.getRuntime().exec(cmdarr);
            } else {
                //Open IPE, if this is not done, it will be stuck sometimes when searching for an id
                Desktop.getDesktop().open(new File(pathToIpe));
                int count = 0;
                while (!isIpeOpen()) {
                    //wait until IPE is opened, with a delay of 1 second
                    try {
                        TimeUnit.MILLISECONDS.sleep(1300);
                        if (count++ == 11) {
                            break;
                        }
                    } catch (InterruptedException ex) {
                    }
                }
                Runtime.getRuntime().exec(cmdarr);
            }
        } catch (IOException ex) {
        }
    }

    public static void openInIpe(String rq1Id) throws IOException {
        if (!isIpeInstalled()) {
            //check if IPE is installed and search for it once
            findIpe();
        }
        Runtime.getRuntime().exec(pathToIpe + " " + rq1Id);
    }

    /**
     * Uses a powershell command to check whether the jar is a running process.
     * May take some time to finish.
     *
     * @param jar a String containing the name of a jar
     * @return true if IPE is already running, else false
     */
    public static boolean isJarOpen(String jar) {
        //Getting the version
        String command = "powershell";//powershell.exe "
        //command to find ipe process
        String input = "Get-WmiObject Win32_Process | Where-Object {($_.CommandLine -like \"*" + jar + "*\")} "
                + "| Format-Table -HideTableHeaders Name";
        // Executing the command
        Process powerShellProcess;
        try {
            ProcessBuilder ps = new ProcessBuilder(command);
            //Open powershell
            powerShellProcess = ps.start();
            //set input aka command to execute
            BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(
                    powerShellProcess.getOutputStream()));
            stdin.write(input);
            stdin.close();
            // Getting the results
            BufferedReader stdout = new BufferedReader(new InputStreamReader(
                    powerShellProcess.getInputStream()));
            String line;
            //read output
            while ((line = stdout.readLine()) != null) {
                //the output also has the line that send the input so we exclude java*
                if (line.contains("java")) {
                    //if java process using Ipe.jar was found
                    stdout.close();
                    return true;
                }
            }
            stdout.close();
            return false;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Checks whether IPE is a running process.
     *
     * @return true if IPE is running
     */
    public static boolean isIpeOpen() {
        return isJarOpen("Ipe.jar");
    }
}
