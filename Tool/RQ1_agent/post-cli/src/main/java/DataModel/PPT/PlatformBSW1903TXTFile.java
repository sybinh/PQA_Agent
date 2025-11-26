/*
 *  Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 *  This program and the accompanying materials are made available under
 *  the terms of the Bosch Internal Open Source License v4
 *  which accompanies this distribution, and is available at
 *  http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author DUR3WI
 */
public class PlatformBSW1903TXTFile {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(PlatformBSW1903TXTFile.class.getCanonicalName());

    private static final ArrayList<String> data = new ArrayList<>(350);

    private PlatformBSW1903TXTFile() {
        throw new AssertionError();
    }

    static {
        InputStream resource = ConcernBaselineTXTFile.class.getResourceAsStream("Platform_BSW_1903.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public static Boolean isPlatformBSW(String FC) {
        if (FC == null || !FC.contains(" / ")) {
            return false;
        }
        return data.contains(FC);
    }

}
