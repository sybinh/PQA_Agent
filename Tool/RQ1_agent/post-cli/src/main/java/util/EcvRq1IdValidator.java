/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author THS83WI
 */
public class EcvRq1IdValidator {
    
    private static final String PATTERN = "(((RQONE)|(RQ1IN))[0-9]{8,8}(?!\\d))";

    /**
     * This method returns all RQONE-IDs from any text.
     * Every RQONE-ID can only exist one time in this set.
     * @param input any text as String
     * @return all RQONE-IDs in a TreeSet<String>
     */
    
    public static TreeSet<String> validateInput(String input) {
        
        TreeSet<String> rqoneIds = new TreeSet<>();

        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(input);
        while(matcher.find()) {
            rqoneIds.add(matcher.group(1));
        }
        return rqoneIds;
    } 
}

