/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package RestClient;

import ToolUsageLogger.ToolUsageLogger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.logging.Level;

/**
 *
 * @author GUG2WI
 */
public class RestCertificateManager {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(RestCertificateManager.class.getCanonicalName());

    static private boolean storeSet = false;

    public static void setStore() {

        if (storeSet == false) {

            InputStream stream = RestCertificateManager.class.getResourceAsStream("IPE_TrustStore");
            String path = System.getProperty("user.home") + File.separator + "Ipe.key";
            File file = new File(path);
            try {
                Files.delete(file.toPath());
            } catch (NoSuchFileException ex) {
                // It's o.k. if the file does not exist.
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Delete trust store " + path, ex);
                ToolUsageLogger.logError(RestCertificateManager.class.getCanonicalName(), ex);
            }
            try {
                Files.copy(stream, file.toPath());
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Copy trust store to file " + path, ex);
                ToolUsageLogger.logError(RestCertificateManager.class.getCanonicalName(), ex);
            }

            System.setProperty("javax.net.ssl.trustStore", path);
            System.setProperty("javax.net.ssl.keyStorePassword", "IPE_TrustStore");
            storeSet = true;
        }

    }

}
