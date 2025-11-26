/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * <p>
 * Supports the RQ1 specific date handling. All dates within java programs of
 * the ECV tool team shall be handled with this class.
 * </p>
 *
 * <p>
 * Implements the date storage, date comparison, data calculation and the RQ1
 * specific date encoding and decoding.
 * </p>
 *
 * @author GUG2WI
 */
public class EcvDateTime implements Comparable<EcvDateTime> {

    static public class MySimpleDateFormat {

        final private SimpleDateFormat formater;

        public MySimpleDateFormat(String format) {
            formater = new SimpleDateFormat(format);
        }

        public MySimpleDateFormat(String format, int minimalDaysInFirstWeek) {
            formater = new SimpleDateFormat(format);
            formater.getCalendar().setMinimalDaysInFirstWeek(minimalDaysInFirstWeek);
        }

        synchronized public String format(Date date) {
            return (formater.format(date));
        }

        synchronized public Date parse(String value) throws ParseException {
            return (formater.parse(value));
        }

        synchronized public void setTimeZone(TimeZone timeZone) {
            formater.setTimeZone(timeZone);
        }

    }

    static public class DateTimeParseException extends Exception {

        public DateTimeParseException(String s) {
            super(s);
            assert (s != null);
            assert (s.isEmpty() == false);
        }

        public DateTimeParseException(String s, Throwable cause) {
            super(s, cause);
            assert (s != null);
            assert (s.isEmpty() == false);
            assert (cause != null);
        }

    }

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EcvDateTime.class.getCanonicalName());
    //
    private static final String rq1DateTimePattern = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z"; // e.g. 2012-08-30T22:00:00Z
    private static final MySimpleDateFormat rq1DateTimeFormater = new MySimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    //
    private static final MySimpleDateFormat uiDateTimeFormater = new MySimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static final MySimpleDateFormat filenameDateTimeFormat = new MySimpleDateFormat("yyyy-MM-dd_HH-mm");
    private static final MySimpleDateFormat isoDateTimeFormater = new MySimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //
    private static final SimpleDateFormat cfdDateFormater = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat xmlDateFormater = new SimpleDateFormat("yyyy-MM-dd");

    private boolean isValid;
    private long timeSince1970; // Time since 1.1.1970 0:00:00 in Milliseconds

    /**
     * Creates an empty time element.
     */
    public EcvDateTime() {
        clear();
    }

    public EcvDateTime(EcvDateTime sourceTime) {
        assert (sourceTime != null);
        this.isValid = sourceTime.isValid;
        this.timeSince1970 = sourceTime.timeSince1970;
    }

    /**
     * Sets the time to empty.
     */
    final public EcvDateTime clear() {
        isValid = false;
        return (this);
    }

    final public EcvDateTime setNow() {
        return (setTime(new Date().getTime()));
    }

    private EcvDateTime setTime(long timeSince1970) {
        this.timeSince1970 = timeSince1970;
        isValid = true;
        return (this);
    }

    /**
     * Supports the parsing of a time in the format of the RQ1 database.
     *
     * @param dbValue Content of a time field from the RQ1 database.
     * @throws DateTimeParseException If the input is not in a valid RQ1 date
     * format.
     */
    final public void setRq1Value(String dbValue) throws DateTimeParseException {
        assert (dbValue != null);
        if (dbValue.isEmpty()) {
            clear();
        } else {
            if (dbValue.matches(rq1DateTimePattern) == false) {
                clear();
                throw (new DateTimeParseException("Unexpected Time Format: >" + dbValue + "<"));
            }
            synchronized (rq1DateTimeFormater) {
                rq1DateTimeFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    setTime(rq1DateTimeFormater.parse(dbValue).getTime());
                } catch (NumberFormatException | ParseException ex) {
                    throw (new DateTimeParseException("Error when parsing time string: >" + dbValue + "<", ex));
                }
            }
        }
    }

    /**
     * Builds a string containing the time in the format of the RQ1 database.
     *
     * @return
     */
    final public String getRq1Value() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (rq1DateTimeFormater.format(new Date(timeSince1970)));
        }
    }

    final public String getCfdDateValue() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (cfdDateFormater.format(new Date(timeSince1970)));
        }
    }

    final public String getFlowDateValue() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (xmlDateFormater.format(new Date(timeSince1970)));
        }
    }

    /**
     * Builds a string containing the time in the format for the UI.
     *
     * @return
     */
    final public String getUiValue() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (uiDateTimeFormater.format(new Date(timeSince1970)));
        }
    }

    /**
     * Returns the date and time in an kind of ISO format:<br>
     * YYYY-MM-DD HH:MM:SS
     *
     * @return
     */
    final public String getIsoValue() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (isoDateTimeFormater.format(new Date(timeSince1970)));
        }
    }

    /**
     * Builds a string containing the time in the format for a valid filename.
     *
     * @return
     */
    public final String getFileNameValue() {
        if (isEmpty() == true) {
            return "";
        } else {
            return filenameDateTimeFormat.format(new Date(timeSince1970));
        }
    }

    final public boolean isEmpty() {
        return (!isValid);
    }

    @Override
    public String toString() {
        return (getUiValue());
    }

    @Override
    public int compareTo(EcvDateTime testTime) {
        //
        // Handle null values for useful row sorting in tabs.
        //
        if ((testTime != null) && (testTime.isValid == true)) {
            if (isValid == true) {
                return (Long.signum(timeSince1970 - testTime.timeSince1970));
            } else {
                return (0);
            }

        } else if (isValid == true) {
            return (1);
        } else {
            return (0);
        }

    }

    public static int compare(EcvDateTime testTime1, EcvDateTime testTime2) {
        if (testTime1 == null) {
            if ((testTime2 == null) || (testTime2.isValid == false)) {
                return (0);
            } else {
                return (-1);
            }
        } else {
            return (testTime1.compareTo(testTime2));
        }
    }

    public boolean equals(EcvDateTime testTime) {
        return (compareTo(testTime) == 0);

    }

    @Override
    public boolean equals(Object testDate) {
        if ((testDate == null) && (isValid == false)) {
            return (true);
        } else if (testDate instanceof EcvDateTime) {
            return (equals((EcvDateTime) testDate));
        } else {
            return (false);
        }
    }

    /**
     * Creates a new EcvTime object that contains the same date as the source
     * object.
     *
     * @return Copy of the source object.
     */
    public EcvDateTime copy() {
        return (new EcvDateTime(this));
    }

    /**
     * Returns an EcvDateTime object with the current time.
     *
     * @return
     */
    public static EcvDateTime getNow() {
        EcvDateTime today = new EcvDateTime();
        today.setNow();
        return today;
    }
}
