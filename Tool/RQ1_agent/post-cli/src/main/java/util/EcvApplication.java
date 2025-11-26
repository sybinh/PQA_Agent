/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import Ipe.Annotations.EcvCompileTime;
import RestClient.Exceptions.AuthorizationException;
import RestClient.Exceptions.NoLoginDataException;
import RestClient.Exceptions.NotFoundException;
import RestClient.Exceptions.RestException;
import UiSupport.EcvMainWindowProviderI;
import UiSupport.EcvUserMessage;
import UiSupport.EcvUserMessage.MessageType;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * Container for all global informations for a typical application of the tool
 * team.
 *
 * @author gug2wi
 */
@EcvCompileTime()
public class EcvApplication {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(EcvApplication.class.getCanonicalName());
    private static EcvMainWindowProviderI mainWindowProvider;

    public static enum ApplicationType {

        UserInterface, // Used for GUIs. EcvTool will request the login data from the user and provides user information via dialog boxes.
        DaemonProcess // Used for background processes. No login window appears. No error message via dialog boxes. Login data has to be set via setLoginData().
    }


    static private String toolName = "";
    static private String toolVersionForLogging = "";
    static private String toolVersionForAccessControl = "";
    static private ApplicationType applicationType = null;
    static private Exception lastException = null;
    static private String userName = "";
    static private JFrame mainWindow = null;
    static private EcvLoginData loginData = null;

    /**
     * To prevent creation of default constructor.
     */
    private EcvApplication() {

    }

    /**
     * Set the application data for the OSLC interface and the application type.
     *
     * @param toolName Title of the application sent via OSLC to the server.
     * @param toolVersionForLogging Version of the application that shall be
     * used in the log files and in the RQ1 history.
     * @param toolVersionForAccessControl Version of the application that shall
     * be used for access control on RQ1 interface.
     * @param applicationType Sets the type of the application (GUI or Daemon).
     */
    public static void setApplicationData(String toolName, String toolVersionForLogging, String toolVersionForAccessControl, ApplicationType applicationType) {
        assert (toolName != null);
        assert (toolName.isEmpty() == false);
        assert (toolVersionForLogging != null);
        assert (toolVersionForLogging.isEmpty() == false);
        assert (toolVersionForAccessControl != null);
        assert (toolVersionForAccessControl.isEmpty() == false);
        assert (applicationType != null);

        EcvApplication.toolName = toolName;
        EcvApplication.toolVersionForLogging = toolVersionForLogging;
        EcvApplication.toolVersionForAccessControl = toolVersionForAccessControl;
        EcvApplication.applicationType = applicationType;

        LOGGER.info("Application Data Set \n"
                + "Toolname: " + toolName + " \n"
                + "Toolversion: " + toolVersionForLogging + " \n"
                + "Toolversion OSLC: " + toolVersionForAccessControl + " \n"
                + "Type: " + applicationType.name());
    }

    public static boolean isApplicationDataSet() {
        return (applicationType != null);
    }

    public static String getToolName() {
        assert (applicationType != null);
        return (toolName);
    }

    public static String getToolVersionForLogging() {
        assert (applicationType != null);
        return (toolVersionForLogging);
    }

    public static String getToolVersionForAccessControl() {
        assert (applicationType != null);
        return (toolVersionForAccessControl);
    }

    public static String getApplicationTitleAndVersion() {
        assert (applicationType != null);
        return (toolName + " - " + toolVersionForLogging);
    }

    public static ApplicationType getApplicationType() {
        assert (applicationType != null);
        return (applicationType);
    }

    public static String getBuild() {
        return (EcvApplicationCompileTime.compileTime);
    }

    public static Exception getLastException() {
        return (lastException);
    }

    public static void handleException(Exception ex) {

        lastException = ex;

        if (applicationType != ApplicationType.UserInterface) {
            handleExceptionInDaemonProcess(ex);
        } else {
            handleExceptionInUserInterfaceProcess(ex);
        }
    }

    public static void handleException(Throwable ex, final String message, final String title, MessageType messageType) {
        assert (message != null);
        assert (title != null);

        if (applicationType != ApplicationType.UserInterface) {
            throw (new EcvApplicationException(ex));
        } else {
            EcvUserMessage.showMessageDialog(limitMessageSize(message, 26), title, messageType);
        }
    }

    private static void handleExceptionInDaemonProcess(Exception ex) {

        if (ex instanceof NotFoundException) {
            //
            // Do not forward exception. Applications get's empty result
            //
            Logger.getLogger(EcvApplication.class.getCanonicalName()).log(Level.WARNING, "Not found exception catched.", ex);
            return;
        }
        throw (new EcvApplicationException(ex));
    }

    private static void handleExceptionInUserInterfaceProcess(Exception ex) {

        if ((ex instanceof NoLoginDataException) || (ex instanceof AuthorizationException)) {
            //
            // Do nothing.
            //
        } else if (ex instanceof NotFoundException) {
            //
            // Show specific message to user
            //
            EcvUserMessage.showMessageDialog(
                    "Element " + ((NotFoundException) ex).getAddressForUi() + " not found.",
                    "Element not found.",
                    MessageType.INFORMATION_MESSAGE
            );
        } else if (ex instanceof RestException) {
            //
            // Show general message to user
            //
            RestException o_ex = (RestException) ex;
            EcvUserMessage.showMessageDialog(limitMessageSize(o_ex.getMessageDialogText(), 26), o_ex.getMessageDialogTitle(), MessageType.INFORMATION_MESSAGE);
        }
    }

    static String limitMessageSize(String fullMessage, int maxNumberOfLines) {
        assert (fullMessage != null);
        assert (maxNumberOfLines > 0);

        int l_message = fullMessage.length();

        int numberOfLines = 0;
        for (int index = 0; index < l_message; index++) {
            if (fullMessage.charAt(index) == '\n') {
                numberOfLines++;
                if (numberOfLines >= maxNumberOfLines) {
                    String limitedMessage = fullMessage.substring(0, index + 1) + "...";
                    return (limitedMessage);
                }
            }
        }

        return (fullMessage);
    }

    /**
     * Returns the current user name or an empty string, if no user name was set
     * yet.
     *
     * @return User name or empty string.
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * Sets the current user name. This method should only be used by the
     * database interface to set the user name after successfull login.
     *
     * @param userName User name. Empty string to reset the user name.
     */
    public static void setUserName(String userName) {
        assert (userName != null);
        EcvApplication.userName = userName;
    }

    /**
     * Returns the main window of the application, if the application has a GUI.
     *
     * @return Main window of application or null.
     */
    public static JFrame getMainWindow() {
        if (mainWindowProvider != null) {
            return mainWindowProvider.getApplicationMainWindow();
        } else {
            return mainWindow;
        }
    }

    /**
     * Sets the main window of the application. The window should be set as soon
     * as the first window is created.
     *
     * DEPRECATED - PLEASE PROVIDE AN EcvMainWindowProvider INSTEAD!
     *
     * @param mainWindow
     */
    @Deprecated
    public static void setMainWindow(JFrame mainWindow) {
        EcvApplication.mainWindow = mainWindow;
    }

    /**
     * Sets the main windowprovider of the application. The window should be set
     * as soon as the first window is created.
     *
     * @param mainWindowProvider
     */
    public static void setMainWindowProvider(EcvMainWindowProviderI mainWindowProvider) {
        EcvApplication.mainWindowProvider = mainWindowProvider;
    }

    /**
     * Checks if a main window is set.
     * <p>
     * No main window means, that the application runs in background.
     *
     * @return true if a main window is set.
     */
    public static boolean hasMainWindow() {
        return (mainWindowProvider != null);
    }

    public static EcvLoginData getLoginData() {
        return loginData;
    }

    /**
     * Sets the login data for daemon processes.
     *
     * @param loginData Login data to be used on the OSLC connection.
     */
    public static void setLoginData(EcvLoginData loginData) {
        assert (loginData != null);

        EcvApplication.loginData = loginData;
    }

    public static void LogHeapSize(String positionInProgram) {
        assert (positionInProgram != null);
        assert (positionInProgram.isEmpty() == false);
        LOGGER.info(positionInProgram + " Heap Size: " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().toString());
    }

    public static void LogHeapSize() {
        LOGGER.info("Heap Size: " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().toString());
    }

    public static final String java_main = "sun.java.command";

    /**
     * Used to restart the instance of the running java process/programm
     *
     * @throws IOException
     */
    public static void restartIPE() throws IOException {
        String javaBin = System.getProperty("java.home") + "/bin/java";
        List<String> javaArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        StringBuffer javaArgumentLines = new StringBuffer();
        for (String argument : javaArguments) {
            if (!argument.contains("-agentlib")) {
                javaArgumentLines.append(argument).append(" ");
            }
        }
        final StringBuilder commandLine = new StringBuilder("\"" + javaBin + "\"" + javaArgumentLines);
        String[] main = System.getProperty(java_main).split(" ");
        if (main[0].endsWith(".jar")) {
            commandLine.append("-jar " + new File(main[0]).getPath());
        } else {
            commandLine.append("-cp \"" + System.getProperty("java.class.path") + "\" " + main[0]);
        }
        for (int index = 1; index < main.length; index++) {
            commandLine.append(" ").append(main[index]);
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec(commandLine.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        System.exit(0);
    }

}
