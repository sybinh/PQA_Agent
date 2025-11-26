/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author miw83wi
 */
public class Rq1Id {

    private static final Pattern memberProjectId = Pattern.compile("(RQONE[0-9]{8})");

    /**
     * Cleans the inputted id
     *
     * @param id
     * @return
     */
    public static String cleanId(String id) {

        String trimed = id.trim();

        if (trimed.isEmpty() == true) {
            return (trimed);
        }

        //
        // Full RQ1-Id entered
        //
        if (trimed.matches("RQONE[0-9]{8,8}")) {
            return (trimed);
        }

        //
        // Only number from RQ1-Id entered
        //
        if (trimed.matches("[0-9]{1,8}")) {
            return ("RQONE00000000".substring(0, 13 - trimed.length()) + trimed);
        }

        //
        // Only blanks entered
        //
        return (null);
    }

    /**
     * Returns RQ1ID from given STring
     *
     * @param inputString
     * @return
     */
    public static String extractRQ1IDFromString(String inputString) {
        assert (inputString != null);
        Matcher matcher = memberProjectId.matcher(inputString);
        if ((matcher.find() == true) && (matcher.groupCount() == 1)) {
            return (matcher.group(1));
        } else {
            return (null);
        }

    }
}