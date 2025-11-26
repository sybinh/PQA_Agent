/*
 *  Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 *  This program and the accompanying materials are made available under
 *  the terms of the Bosch Internal Open Source License v4
 *  which accompanies this distribution, and is available at
 *  http://bios.intranet.bosch.com/bioslv4.txt
 */
package launcher;

import frontend.FnCCat_MainWindowController;
import OslcAccess.Rq1.OslcRq1Client;
import RestClient.Exceptions.RestException;
import RestClient.Exceptions.RestInternalException;
import RestClient.GeneralServerDescription;
import Rq1Cache.Records.Rq1RecordInterface;
import backend.EcvSharePointClient;
import exceptions.FnCCat_TaskInterruptedException;
import backend.FnCCat_TaskManager;
import backend.util.FnCCat_ResourceLists;
import backend.util.FnCCat_SharePointResourceFileDownloader;
import backend.util.filereaders.FnCCat_ArchitectureFileReader;
import backend.util.filereaders.FnCCat_AufsetzpunktListReader;
import backend.util.filereaders.FnCCat_BaselineListReader;
import backend.util.filereaders.FnCCat_BcResponsibleReader;
import backend.util.filereaders.FnCCat_ConfigFileReader;
import backend.util.filereaders.FnCCat_ExcludeListReader;
import backend.util.filereaders.FnCCat_PlatformBSWListReader;
import dataobjects.FnCCat_ResourceListElement;
import enumerations.FnCCat_CommentEnum;
import enumerations.FnCCat_ResourceEnum;
import exceptions.EcvAuthenticationException;
import exceptions.EcvFileDownloadException;
import exceptions.EcvHttpException;
import exceptions.EcvServiceException;
import exceptions.FnCCat_XLSFileReaderException;
import frontend.util.FnCCat_CustomAlert;
import frontend.util.FnCCat_DialogUtil;
import frontend.util.FnCCat_ProjectSelectionAlert;
import frontend.util.FnCCat_ReleaseNotesBuilder;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.EcvApplication;

/**
 *
 * @author ISM83WI
 */
public class FnCCat_ToolLauncher extends Application {

    private static String selectedProject;
    public FnCCat_MainWindowController mainWindowController;
    public EcvApplication.LoginData loginData;
    private static final Logger LOGGER = Logger.getLogger(FnCCat_ToolLauncher.class.getName());

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        launch(args);
        exit();
    }

    public static void exit() {
        System.exit(0);
    }

    @Override
    public void start(Stage rootStage) throws Exception {
        LOGGER.log(Level.INFO, "FnCCat_Checker started");
        FnCCat_ProjectSelectionAlert projectSelectionAlert = new FnCCat_ProjectSelectionAlert(Alert.AlertType.INFORMATION, 200, true, "FnCCat - Checker", "FnCCat - Checker", true, null);
        projectSelectionAlert.setGraphic(null);
        projectSelectionAlert.initStyle(StageStyle.DECORATED);
        projectSelectionAlert.showAndWait();
        if (projectSelectionAlert.shouldCloseApp()) {
            exit();
        }
        selectedProject = projectSelectionAlert.getSelectedProject();
        this.loginData = new EcvApplication.LoginData(projectSelectionAlert.getUserName(), projectSelectionAlert.getPassword().toCharArray(), GeneralServerDescription.getDescriptionByName(projectSelectionAlert.getSelectedDatabase()));
        EcvApplication.setLoginData(loginData);

        FnCCat_CustomAlert startAlert = new FnCCat_CustomAlert(Alert.AlertType.INFORMATION, 200, true, "FnCCat - Checker", "FnCCat - Checker", true, "Login to RQ1... (1/11)");
        startAlert.setGraphic(null);
        startAlert.initStyle(StageStyle.UNDECORATED);

        FnCCat_CustomAlert exitAlert = new FnCCat_CustomAlert(Alert.AlertType.CONFIRMATION, 100, false, "Exit Application", "FnCCat - Checker", false, "Do you want to exit the application?");
        exitAlert.changeFont(Font.font(14));

        Thread startScreen = new Thread() {
            @Override
            public void run() {
                EcvApplication.setApplicationData("OfficeUtils", "1.0", "1.0", EcvApplication.ApplicationType.UserInterface);
                OslcRq1Client<Rq1RecordInterface> client = OslcRq1Client.getOslcClient();

                try {
                    LOGGER.log(Level.INFO, "Login to RQ1");
                    client.loadLoginUser();
                } catch (RestInternalException ex) {
                    Logger.getLogger(FnCCat_ToolLauncher.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RestException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            LOGGER.log(Level.SEVERE, "Login to RQ1 failed.");
                            startAlert.showError("Login has failed. Tool will exit.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                }
//--------------------------------------------------------------------------------------------------------------------------------------------------------               
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Downloading Resources... (2/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Downloading Resources from SharePoint.");
                    FnCCat_SharePointResourceFileDownloader.downloadResources(selectedProject);
                    LOGGER.log(Level.INFO, "Downloading Resources from SharePoint successful.");
                } catch (EcvAuthenticationException | EcvHttpException | EcvServiceException | EcvFileDownloadException ex) {
                    LOGGER.log(Level.SEVERE, "Downloading Resources from SharePoint failed.");
                    boolean toggle = true;
                    LOGGER.log(Level.INFO, "Checking if all resource files exist locally.");
                    for (FnCCat_ResourceEnum value : FnCCat_ResourceEnum.getEnumSet(selectedProject)) {
                        File resourcefile;
                        if (value == FnCCat_ResourceEnum.ARCHITECTURE_LIST || value == FnCCat_ResourceEnum.CONFIG_FILE) {
                            resourcefile = new File(System.getProperty("user.home") + File.separator + "FnCCatCheckerResources" + File.separator + value.toString());
                        } else {
                            resourcefile = new File(System.getProperty("user.home") + File.separator + "FnCCatCheckerResources" + File.separator + value.getFolderName() + File.separator + value.toString());
                        }
                        if (!resourcefile.exists()) {
                            toggle = false;
                            break;
                        }
                    }
                    if (toggle) {
                        LOGGER.log(Level.INFO, "All resource files exist locally.");
                        final CountDownLatch latchToWaitForJavaFx = new CountDownLatch(1);
                        final FutureTask query = new FutureTask(new Callable() {
                            @Override
                            public Object call() throws Exception {
                                Optional<ButtonType> result = FnCCat_DialogUtil.askConfirmation("Confirmation",
                                        "Tool couldn't download newest resource files from SharePoint.\nContinue with local resources?",
                                        "Press Continue to use local resources.\nPress Cancel to the close tool.");
                                latchToWaitForJavaFx.countDown();
                                return result;
                            }
                        });
                        try {
                            Platform.runLater(query);
                            latchToWaitForJavaFx.await();
                            if (ButtonType.OK != ((Optional<ButtonType>) query.get()).get()) {
                                Platform.runLater(() -> {
                                    if (startAlert.isShowing()) {
                                        LOGGER.log(Level.SEVERE, "User closed the tool.");
                                        startAlert.showError("Tool will exit now.");
                                        startAlert.getDialogPane().getScene().getWindow().requestFocus();
                                    }
                                });
                                return;
                            }
                        } catch (InterruptedException | ExecutionException ex1) {
                            LOGGER.log(Level.SEVERE, null, ex1);
                        }
                    } else {
                        Platform.runLater(() -> {
                            if (startAlert.isShowing()) {
                                LOGGER.log(Level.SEVERE, "Not all necessary resource Files exist.");
                                startAlert.showError("Couldn't download resource files from SharePoint");
                                startAlert.getDialogPane().getScene().getWindow().requestFocus();
                            }
                        });
                        return;
                    }
                }
//--------------------------------------------------------------------------------------------------------------------------------------------------------
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Checking Version... (3/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Reading the Config File.");
                    String releasedVersion = FnCCat_ConfigFileReader.readConfigFile(new File(System.getProperty("user.home") + File.separator + "FnCCatCheckerResources" + File.separator + FnCCat_ResourceEnum.CONFIG_FILE.toString()));
                    LOGGER.log(Level.INFO, "Reading the Config File successful.");
                    if (!FnCCat_ReleaseNotesBuilder.RELEASE_VERSION.equals(releasedVersion)) {                    
                        final CountDownLatch latchToWaitForJavaFx = new CountDownLatch(1);
                        final FutureTask query = new FutureTask(new Callable() {
                            @Override
                            public Object call() throws Exception {
                                FnCCat_DialogUtil.showDialog(Alert.AlertType.ERROR, "Tool Version Error", "Local tool version doesn't match latest version", "Latest Version: " + releasedVersion + "\nLocal Version: " + FnCCat_ReleaseNotesBuilder.RELEASE_VERSION + "\nThere is a new version avaiable. Please download it from the FnCCat PMT site.");
                                latchToWaitForJavaFx.countDown();
                                return null;
                            }
                        });
                        try {
                            Platform.runLater(query);
                            latchToWaitForJavaFx.await();
                            Platform.runLater(() -> {
                                if (startAlert.isShowing()) {
                                    LOGGER.log(Level.SEVERE, "Local Tool Version  doesn't match latest Tool Version.");
                                    startAlert.showError("Tool will exit now.");
                                    startAlert.getDialogPane().getScene().getWindow().requestFocus();
                                }
                            });
                            return;

                        } catch (InterruptedException ex1) {
                            LOGGER.log(Level.SEVERE, null, ex1);
                        }

                    }
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            LOGGER.log(Level.SEVERE, "Reading the Config File failed.");
                            startAlert.showError("Reading the Config File has failed.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                } catch (FnCCat_TaskInterruptedException ignore) {
                }
//-------------------------------------------------------------------------------------------------------------------------------------------------------- 
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Reading Aufsetzpunkt List... (4/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Reading the Aufsetzpunkt List.");
                    FnCCat_ResourceLists.addResourceMap(new FnCCat_ResourceListElement(FnCCat_AufsetzpunktListReader.readAufsetzpunktMap(selectedProject), FnCCat_CommentEnum.IN_AUFSETZPUNKT));
                    LOGGER.log(Level.INFO, "Reading the Aufsetzpunkt List successful.");
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            LOGGER.log(Level.SEVERE, "Reading the Aufsetzpunkt List failed.");
                            startAlert.showError("Reading the Aufsetzpunkt List has failed.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                } catch (FnCCat_TaskInterruptedException ignore) {
                }

//--------------------------------------------------------------------------------------------------------------------------------------------------------
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Reading Platform-BSW List 1903... (5/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Reading the Platform-BSW List 1903.");
                    FnCCat_ResourceLists.addResourceMap(new FnCCat_ResourceListElement(FnCCat_PlatformBSWListReader.readPlatformBSWMap(selectedProject, "Zentrale_BsM_Bewertung_1903"), FnCCat_CommentEnum.IN_PLATFORM_BSW_1903));
                    LOGGER.log(Level.INFO, "Reading the Platform-BSW List 1903 successful.");
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            LOGGER.log(Level.SEVERE, "Reading the Platform-BSW List 1903 failed.");
                            startAlert.showError("Reading the Platform-BSW List has 1903 failed.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                } catch (FnCCat_TaskInterruptedException ex) {
                }

//--------------------------------------------------------------------------------------------------------------------------------------------------------                
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Reading Platform-BSW List 1909... (6/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Reading the Platform-BSW List 1909.");
                    FnCCat_ResourceLists.addResourceMap(new FnCCat_ResourceListElement(FnCCat_PlatformBSWListReader.readPlatformBSWMap(selectedProject, "Zentrale_BsM_Bewertung_1909"), FnCCat_CommentEnum.IN_PLATFORM_BSW_1909));
                    LOGGER.log(Level.INFO, "Reading the Platform-BSW List 1909 successful.");
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            LOGGER.log(Level.SEVERE, "Reading the Platform-BSW List 1909 failed.");
                            startAlert.showError("Reading the Platform-BSW List has 1909 failed.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                } catch (FnCCat_TaskInterruptedException ex) {
                }

//--------------------------------------------------------------------------------------------------------------------------------------------------------                
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Reading Platform-BSW List 2003... (7/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Reading the Platform-BSW List 2003.");
                    FnCCat_ResourceLists.addResourceMap(new FnCCat_ResourceListElement(FnCCat_PlatformBSWListReader.readPlatformBSWMap(selectedProject, "Zentrale_BsM_Bewertung_2003"), FnCCat_CommentEnum.IN_PLATFORM_BSW_2003));
                    LOGGER.log(Level.INFO, "Reading the Platform-BSW List 2003 successful.");
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            LOGGER.log(Level.SEVERE, "Reading the Platform-BSW List 2009 failed.");
                            startAlert.showError("Reading the Platform-BSW List has 2009 failed.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                } catch (FnCCat_TaskInterruptedException ex) {
                }

//--------------------------------------------------------------------------------------------------------------------------------------------------------
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Reading Exclude List... (8/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Reading the Exclude List.");
                    FnCCat_ResourceLists.addResourceMap(new FnCCat_ResourceListElement(FnCCat_ExcludeListReader.readExcludeMap(selectedProject), FnCCat_CommentEnum.IN_EXCLUDE));
                    LOGGER.log(Level.INFO, "Reading the Exclude List successful.");
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            LOGGER.log(Level.SEVERE, "Reading the Exclude List failed.");
                            startAlert.showError("Reading the Exclude List has failed.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                } catch (FnCCat_TaskInterruptedException ignore) {
                }

//--------------------------------------------------------------------------------------------------------------------------------------------------------
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Reading Baseline List... (9/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Reading the Baseline List.");
                    FnCCat_ResourceLists.addResourceMap(new FnCCat_ResourceListElement(FnCCat_BaselineListReader.readNewBaselineMap(selectedProject), FnCCat_CommentEnum.IN_BASELINE));
                    LOGGER.log(Level.INFO, "Reading the Baseline List successful.");
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            LOGGER.log(Level.SEVERE, "Reading the Baseline List failed.");
                            startAlert.showError("Reading the Baseline List has failed.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                } catch (FnCCat_TaskInterruptedException ignore) {
                }

//--------------------------------------------------------------------------------------------------------------------------------------------------------
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Reading Architecture List... (10/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Reading the Architecture List.");
                    FnCCat_ArchitectureFileReader.getFCBCMap();
                    LOGGER.log(Level.INFO, "Reading the Architecture List successfull.");
                } catch (IOException | FnCCat_XLSFileReaderException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            LOGGER.log(Level.SEVERE, "Reading the Architecture List failed.");
                            startAlert.showError("Reading the Architecture List has failed.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                } catch (FnCCat_TaskInterruptedException ignore) {
                }

//--------------------------------------------------------------------------------------------------------------------------------------------------------                
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setContentLabel("Reading BC Resposible List... (11/11)");
                    }
                });

                try {
                    LOGGER.log(Level.INFO, "Reading the Architecture List.");
                    FnCCat_BcResponsibleReader.readBcResponsibleMap(selectedProject);
                    LOGGER.log(Level.INFO, "Reading the Architecture List successful.");
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        if (startAlert.isShowing()) {
                            startAlert.showError("Reading the BC Responsible List has failed.");
                            startAlert.getDialogPane().getScene().getWindow().requestFocus();
                        }
                    });
                    return;
                } catch (FnCCat_TaskInterruptedException ignore) {
                } catch (FnCCat_XLSFileReaderException ex) {
                    Logger.getLogger(FnCCat_ToolLauncher.class.getName()).log(Level.SEVERE, null, ex);
                }

//--------------------------------------------------------------------------------------------------------------------------------------------------------
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FnCCat_ToolLauncher.class.getName()).log(Level.SEVERE, null, ex);
                }

//--------------------------------------------------------------------------------------------------------------------------------------------------------
                Platform.runLater(() -> {
                    if (startAlert.isShowing()) {
                        startAlert.setCloseApp(false);
                        startAlert.close();
                    }
                });
            }
        };
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        startScreen.start();
        startAlert.setOnCloseRequest((event) -> {
            if (startAlert.shouldCloseApp()) {
                exit();
            }
        });
        ((Stage) startAlert.getDialogPane().getScene().getWindow()).toFront();
        startAlert.showAndWait();

        final Button btOk = (Button) exitAlert.getDialogPane().lookupButton(ButtonType.OK);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            exitAlert.setContentLabel("Stopping Application...");
            exitAlert.getDialogPane().getButtonTypes().forEach(btnType -> {
                exitAlert.getDialogPane().lookupButton(btnType).setVisible(false);
            });
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    FnCCat_TaskManager.getInstance().shutdown();
                    boolean finished;
                    do {
                        finished = FnCCat_TaskManager.getInstance().isTerminated();
                        if (!finished) {
                            Thread.sleep(1000);
                        }
                    } while (!finished);
                    return null;
                }
            };
            task.setOnSucceeded((x) -> {
                exit();
            });
            new Thread(task).start();
            event.consume();
        }
        );
        EcvSharePointClient.getInstance().shutdown();
        LOGGER.log(Level.INFO, "FnCCat_Checker started successful");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main_window.fxml"));
        Parent root = loader.load();
        mainWindowController = (FnCCat_MainWindowController) loader.getController();
        Scene mainScene = new Scene(root);
        rootStage.getIcons().add(new Image("resources/cat.png"));
        rootStage.setTitle("FnCCat - Checker (Developed by PS-EC/ECV-S)");
        rootStage.setScene(mainScene);
        rootStage.setMaximized(true);
        rootStage.setMinWidth(800);
        rootStage.setMinHeight(600);
        rootStage.show();
        rootStage.setOnCloseRequest(value -> {
            exitAlert.show();
            value.consume();
        });
    }

    @Override
    public void stop() throws Exception {
        LOGGER.log(Level.INFO, "Shutting down FnCCat_Checker");
        super.stop();
        if (!FnCCat_TaskManager.getInstance().isShutdown()) {
            FnCCat_TaskManager.getInstance().shutdown();
        }
        exit();
    }

    public static String getSelectedProject() {
        return selectedProject;
    }
}
