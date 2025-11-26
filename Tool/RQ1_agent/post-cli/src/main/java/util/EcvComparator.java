/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.util.Comparator;
import java.util.logging.Logger;

/**
 * Provides smart sorting for objects in tables and lists.
 *
 * @author GUG2WI
 */
public class EcvComparator implements Comparator {

    final private static Logger LOGGER = Logger.getLogger(EcvComparator.class.getCanonicalName());

    @Override
    public int compare(Object o1, Object o2) {
        return (compareObjects(o1, o2));
    }

    public static int compareObjects(Object o1, Object o2) {

        // Handle null values
        if (o1 == null) {
            if (o2 == null) {
                return (0);
            } else {
                return (-1);
            }
        } else if (o2 == null) {
            return (1);
        }

        // Handle dates
        String string1, string2; // Set values during date check to prevent multiple call of toString().
        if (o1 instanceof EcvDate) {
            if (o2 instanceof EcvDate) {
                return (((EcvDate) o1).compareTo((EcvDate) o2));
            } else {
                string2 = o2.toString();
                if ("".equals(string2)) {
                    return (1);
                } else {
                    string1 = o1.toString();
                }
            }
        } else if (o2 instanceof EcvDate) {
            string1 = o1.toString();
            if ("".equals(string1)) {
                return (-1);
            } else {
                string2 = o2.toString();
            }
        } else {
            string1 = o1.toString();
            string2 = o2.toString();
        }

        // Calculate string lengths
        int string1Length = string1.length();
        int string2Length = string2.length();
        int minLength = string1Length <= string2Length ? string1Length : string2Length;

        // Handle empty strings
        if (string1Length == 0) {
            if (string2Length == 0) {
                return (0);
            } else {
                return (-1);
            }
        } else if (string2Length == 0) {
            return (1);
        }

        // Compare strings
        for (int position = 0; position < minLength; position++) {
            char c1 = string1.charAt(position);
            char c2 = string2.charAt(position);

            if (Character.isDigit(c1)) {
                if (Character.isDigit(c2)) {
                    Long i = getFullNumber(string1, position);
                    Long j = getFullNumber(string2, position);
                    int k = i.compareTo(j);
                    if (k == 0) {
                        continue;
                    } else {
                        return (k);
                    }
                } else {
                    return (1);
                }
            } else if (Character.isDigit(c2)) {
                return (-1);
            } else {
                int k = Character.compare(Character.toLowerCase(c1), Character.toLowerCase(c2));
                if (k == 0) {
                    continue;
                } else {
                    return (k);
                }
            }

        }

        return (Integer.signum(string1Length - string2Length));
    }

    /**
     * Takes a string an returns the full number included
     *
     * @param object The string to be checked for the full number
     * @param position The position from where on the number should be generated
     * @return A Long with the full number from the string
     */
    private static Long getFullNumber(String object, int position) {
        String subObject = object.substring(position, object.length());
        int tempPosition = 0;
        String returnInt = "";
        while (tempPosition < subObject.length() && Character.isDigit(subObject.charAt(tempPosition))) {
            returnInt += subObject.charAt(tempPosition);
            tempPosition++;
        }
        try {
            return Long.parseLong(returnInt, 10);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
    
    /**
     * Takes a string and checks if it is numeric.
     * 
     * @param str String, to be checked.
     * @return true, if str is numeric.
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
