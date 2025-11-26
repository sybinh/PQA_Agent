/*
 *  Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 *  This program and the accompanying materials are made available under
 *  the terms of the Bosch Internal Open Source License v4
 *  which accompanies this distribution, and is available at
 *  http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.PPT;

import java.util.HashMap;
import java.io.*;
import java.util.logging.Level;

/**
 *
 * @author DUR3WI
 */
public class RequirementIDs {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(RequirementIDs.class.getCanonicalName());

    static HashMap<String, ids> entries = new HashMap<>();

    private static final ids noEntry = new ids("N/A", "N/A");

    // supress default constructor for noninstantiability
    private RequirementIDs() {
        throw new AssertionError();

    }

    public static ids getIds(String key) {
        return entries.getOrDefault(key, noEntry);
    }

    public static String InternalID2CustomerID(String InternalID) {

        for (ids entry : entries.values()) {
            if (entry.InternalID.equals(InternalID)) {
                return entry.CustomerID;
            }
        }

        return noEntry.CustomerID;
    }

    static {
        loadList();
    }

    private static void loadList() {
        // !! THE FILE HAS TO BE IN UTF-8 !! convert (asci ->) UTF-8 if necessary
        InputStream resource = RequirementIDs.class.getResourceAsStream("RequirementIDs.csv");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource))) {

            String zeile = "";

            while (null != (zeile = br.readLine())) {
                String[] split = zeile.split(";");
                if (split[0] != null && !split[0].isEmpty()) {
                    entries.put(split[0], new ids(
                            split.length > 1 && split[1] != null ? split[1] : "",
                            split.length > 2 && split[2] != null ? split[2] : ""));
                }
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public static class ids {

        final public String CustomerID;
        final public String InternalID;

        public ids(String ObjectID, String OriginalID) {
            this.CustomerID = ObjectID;
            this.InternalID = OriginalID;
        }

    }
}
