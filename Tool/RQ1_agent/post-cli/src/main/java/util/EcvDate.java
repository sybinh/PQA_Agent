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
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class EcvDate implements Comparable<EcvDate> {

    static public class DateParseException extends Exception {

        public DateParseException(String s) {
            super(s);
            assert (s != null);
            assert (s.isEmpty() == false);
        }

        public DateParseException(String s, Throwable cause) {
            super(s, cause);
            assert (s != null);
            assert (s.isEmpty() == false);
            assert (cause != null);
        }

    }

    /**
     * Provides a thread safe access to the pattern.
     */
    static private class MyPattern {

        final private Pattern pattern;

        private MyPattern(String patternString) {
            pattern = Pattern.compile(patternString);
        }

        synchronized private Matcher matcher(String uiValue) {
            return (pattern.matcher(uiValue));
        }

    }

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(EcvDate.class.getCanonicalName());
    //
    private static final String oslcDatePattern_String = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z"; // e.g. 2012-08-30T22:00:00Z
    private static final DateTimeFormatter olscDateParser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", new Locale("de", "DE"));
    private static final DateTimeFormatter olscDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T12:00:00Z'", new Locale("de", "DE"));
    private static final MyPattern oslcDatePattern = new MyPattern(oslcDatePattern_String);

    private static final String specialOslcDatePattern_String = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}"; // e.g. 2012-08-30 22:00:00
    private static final DateTimeFormatter specialOslcDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss", new Locale("de", "DE"));
    //
    private static final String almDatePattern_String = "[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{2,3}Z"; // e.g. 2020-02-13T11:09:09.782Z
    private static final DateTimeFormatter almDateParser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", new Locale("de", "DE"));
    //
    private static final String mcrDatePattern_String = "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]"; // e.g. 2012-08-30 22:00:00.0
    private static final DateTimeFormatter mcrDateParser = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.S", new Locale("de", "DE"));

    //
    private static final String xmlDatePatternForOldFormat_String = "[0-3][0-9]\\.[01]?[0-9]\\.((19)|(20))[0-9]{2}"; // e.g. 15.12.2012
    private static final DateTimeFormatter xmlDateParserForOldFormat = DateTimeFormatter.ofPattern("d.M.yyyy", new Locale("de", "DE"));
    private static final MyPattern xmlDatePatternForOldFormat = new MyPattern(xmlDatePatternForOldFormat_String);
    //
    private static final String xmlDatePatternForNewFormat_String = "((19)|(20))[0-9]{2}-[01][0-9]-[0-3][0-9]"; // e.g. 2012-12-15
    private static final DateTimeFormatter xmlDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("de", "DE"));
    private static final MyPattern xmlDatePatternForNewFormat = new MyPattern(xmlDatePatternForNewFormat_String);
    //
    private static final DateTimeFormatter roadmapDateFormaterYear = DateTimeFormatter.ofPattern("yyyy", new Locale("de", "DE"));
    private static final DateTimeFormatter roadmapDateFormaterMonth = DateTimeFormatter.ofPattern("MM/yy", new Locale("de", "DE"));
    private static final DateTimeFormatter roadmapDateFormaterWeek = DateTimeFormatter.ofPattern("ww/yy", new Locale("de", "DE"));
    private static final DateTimeFormatter roadmapDateFormaterDay = DateTimeFormatter.ofPattern("dd.MM.yy", new Locale("de", "DE"));
    //
    private static final String cmpDatePattern_String = "((19)|(20))[0-9]{2}[01][0-9][0-3][0-9]";
    private static final MyPattern cmpDatePattern = new MyPattern(cmpDatePattern_String);
    private static final DateTimeFormatter cmpCalendarWeekFormatter = DateTimeFormatter.ofPattern("yyyyww", new Locale("de", "DE"));
    private static final DateTimeFormatter intCalendarWeekFormatter = DateTimeFormatter.ofPattern("ww", new Locale("de", "DE"));
    //
    private static final DateTimeFormatter uiDateFormatter = DateTimeFormatter.ofPattern("EEE dd.MM.yyyy 'CW'ww'/'YY", new Locale("de", "DE"));

    private static final DateTimeFormatter uiCalendarWeekFormatter = DateTimeFormatter.ofPattern("'CW'ww'/'YY", new Locale("de", "DE"));

    private static final DateTimeFormatter getWeekOfYearFormat = DateTimeFormatter.ofPattern("ww", new Locale("de", "DE"));

    private static final String uiDatePattern_EEE = "(?<EEE>(Mo|Di|Mi|Do|Fr|Sa|So))";
    private static final String uiDatePattern_ddMMyyyy = "(?<dd>[0-9]+)\\.(?<MM>[0-9]+)\\.(?<yyyy>[0-9]+)";
    private static final String uiDatePattern_CWwwyy = "((CW)? *(?<ww>[0-9]+) *\\/(?<yy>[0-9]+))";
    private static final String uiDatePattern_String = "(" + uiDatePattern_EEE + ")? *(" + uiDatePattern_ddMMyyyy + ")? *(" + uiDatePattern_CWwwyy + ")?";
    private static final MyPattern uiDatePattern = new MyPattern(uiDatePattern_String);
    //
    private static final String rq1TemplateDatePattern_String = "[0-9]{4}-[0-9]{2}-[0-9]{2}T12:00:00Z"; // e.g. 2012-08-30T22:00:00Z
    private static final DateTimeFormatter rq1TemplateDateParser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T12:00:00Z'");
    //
    private static final MyPattern[] patternList = {oslcDatePattern,
        xmlDatePatternForOldFormat,
        xmlDatePatternForNewFormat,
        cmpDatePattern,
        uiDatePattern};

    final private LocalDate localDate;
    final private DayOfWeek dayOfWeek;
    final private long epochDay;

    final static private EcvDate emptyDate = new EcvDate();

    //**************************************************************************
    //
    // Constructors. All private
    //
    //**************************************************************************
    private EcvDate() {
        localDate = null;
        dayOfWeek = null;
        epochDay = -1;
    }

    private EcvDate(Date newDate) {
        assert (newDate != null);

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(newDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        dayOfWeek = localDate.getDayOfWeek();
        epochDay = localDate.toEpochDay();
    }

    private EcvDate(LocalDate newDate) {
        assert (newDate != null);
        localDate = newDate;
        dayOfWeek = localDate.getDayOfWeek();
        epochDay = localDate.toEpochDay();
    }

    /**
     * Create a date according to the given values.
     *
     * @param year Value of the year. e.g. 2015
     * @param month Number of the month. januar = 1
     * @param day Number of the day in the month.
     */
    private EcvDate(int year, int month, int day) {
        localDate = LocalDate.of(year, month, day);
        dayOfWeek = localDate.getDayOfWeek();
        epochDay = localDate.toEpochDay();
    }

    private EcvDate(Calendar newDate) {
        assert (newDate != null);

        Calendar calendar = (Calendar) newDate.clone();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        dayOfWeek = localDate.getDayOfWeek();
        epochDay = localDate.toEpochDay();
    }

    //**************************************************************************
    //
    // Public creation methods
    //
    //**************************************************************************
    /**
     * Returns an empty EcvDate object.
     *
     * @return The empty EcvDate object.
     */
    public static EcvDate getEmpty() {
        return (emptyDate);
    }

    /**
     * Returns the given date or an empty date if null is supplied.
     *
     * @param possibleNullDate
     * @return
     */
    public static EcvDate getNotNull(EcvDate possibleNullDate) {
        return ((possibleNullDate != null) ? possibleNullDate : getEmpty());
    }

    /**
     * Returns an EcvDate object with the date of today.
     *
     * @return The date of today.
     */
    public static EcvDate getToday() {
        return (new EcvDate(LocalDate.now()));
    }

    /**
     * Returns a new EcvDate object which is numberOfDays different to today..
     *
     * @param numberOfDays Number of dates to add. Negative values give dates
     * earlier then today. 0 gives today.
     * @return The date of today + numberOfDays.
     */
    public static EcvDate getToday(int numberOfDays) {
        return (new EcvDate(LocalDate.now()).addDays(numberOfDays));
    }

    /**
     * Returns a date according to the given values.
     *
     * @param year Value of the year. e.g. 2015
     * @param month Number of the month. january = 1
     * @param day Number of the day in the month.
     * @return
     */
    static public EcvDate getDate(int year, int month, int day) {
        return (new EcvDate(year, month, day));
    }

    public static EcvDate getDate(Calendar newDate) {
        assert (newDate != null);

        return (new EcvDate(newDate));
    }

    //**************************************************************************
    //
    // Parsing of values
    //
    //**************************************************************************
    /**
     * Supports the parsing of a date in the format of the OSLC interface.
     *
     * @param dbValue Content of a date field from the OSLC database.
     * @return The parsed date value.
     * @throws util.EcvDate.DateParseException If the input is not in a valid
     * OSLC date format.
     */
    final public static EcvDate parseOslcValue(String dbValue) throws DateParseException {
        assert (dbValue != null);
        if (dbValue.isEmpty()) {
            return (getEmpty());
        } else {
            if (dbValue.matches(oslcDatePattern_String) == false) {
                throw (new DateParseException("Unexpected Date Format: >" + dbValue + "<"));
            }
            try {
                return (new EcvDate(LocalDate.parse(dbValue, olscDateParser)));
            } catch (DateTimeException ex) {
                LOGGER.log(Level.WARNING, "Error when parsing >" + dbValue + "<", ex);
                throw (new DateParseException("Invalid date value: >" + dbValue + "<"));
            }
        }
    }

    final public static EcvDate parseSpecialOslcValue(String dbValue) throws DateParseException {
        assert (dbValue != null);
        if (dbValue.isEmpty()) {
            return (getEmpty());
        } else {
            if (dbValue.matches(specialOslcDatePattern_String) == false) {
                throw (new DateParseException("Unexpected Date Format: >" + dbValue + "<"));
            }
            try {
                return (new EcvDate(LocalDate.parse(dbValue, specialOslcDateFormatter)));
            } catch (DateTimeException ex) {
                LOGGER.log(Level.WARNING, "Error when parsing >" + dbValue + "<", ex);
                throw (new DateParseException("Invalid date value: >" + dbValue + "<"));
            }
        }
    }

    /**
     * Supports the parsing of a date in the format of the OSLC interface.
     *
     * @param dbValue Content of a date field from the OSLC database. .
     * @return The parsed date value or null, if not valid date was found.
     */
    final public static EcvDate getDateForOslcValue(String dbValue) {
        assert (dbValue != null);

        if ((dbValue.isEmpty() == false) && (dbValue.matches(oslcDatePattern_String) == true)) {
            try {
                return (new EcvDate(LocalDate.parse(dbValue, olscDateParser)));
            } catch (DateTimeException ex) {
                LOGGER.log(Level.WARNING, "Error when parsing >" + dbValue + "<", ex);
                return (null);
            }
        }

        return (null);
    }

    final public static EcvDate parseRq1TemplateValue(String dbValue) throws DateParseException {
        assert (dbValue != null);
        if (dbValue.isEmpty()) {
            return (getEmpty());
        } else {
            if (dbValue.matches(rq1TemplateDatePattern_String) == false) {
                throw (new DateParseException("Unexpected Date Format: >" + dbValue + "<"));
            }
            try {
                return (new EcvDate(LocalDate.parse(dbValue, rq1TemplateDateParser)));
            } catch (DateTimeException ex) {
                LOGGER.log(Level.WARNING, "Error when parsing >" + dbValue + "<", ex);
                throw (new DateParseException("Invalid date value: >" + dbValue + "<"));
            }
        }
    }

    final public static EcvDate parseAlmValue(String dbValue) throws DateParseException {
        assert (dbValue != null);
        if (dbValue.isEmpty()) {
            return (getEmpty());
        } else {
            if (dbValue.matches(almDatePattern_String) == false) {
                throw (new DateParseException("Unexpected Date Format: >" + dbValue + "<"));
            }
            try {
                return (new EcvDate(LocalDate.parse(dbValue, almDateParser)));
            } catch (DateTimeException ex) {
                LOGGER.log(Level.WARNING, "Error when parsing >" + dbValue + "<", ex);
                throw (new DateParseException("Invalid date value: >" + dbValue + "<"));
            }
        }
    }

    final public static EcvDate parseMcrValue(String dbValue) throws DateParseException {
        assert (dbValue != null);
        if (dbValue.isEmpty()) {
            return (getEmpty());
        } else {
            if (dbValue.matches(mcrDatePattern_String) == false) {
                throw (new DateParseException("Unexpected Date Format: >" + dbValue + "<"));
            }
            try {
                return (new EcvDate(LocalDate.parse(dbValue, mcrDateParser)));
            } catch (DateTimeException ex) {
                LOGGER.log(Level.WARNING, "Error when parsing >" + dbValue + "<", ex);
                throw (new DateParseException("Invalid date value: >" + dbValue + "<"));
            }
        }
    }

    /**
     * Supports the parsing of a date in the format used in the XML fields in
     * the RQ1 database. An empty string is considered as an valid value and a
     * empty date field is returned in this case.
     *
     * @param xmlValue Content of a XML date field from the RQ1 database.
     * @return The date parsed from the xmlValue. The empty date, if xmlValue is
     * empty.
     * @throws util.EcvDate.DateParseException If the input is not in a valid
     * RQ1 date format.
     */
    final public static EcvDate parseXmlValue(String xmlValue) throws DateParseException {
        assert (xmlValue != null);

        //
        // Ignore empty values and values containing only white space.
        //
        String xmlValueTrimmed = xmlValue.trim();
        if (xmlValueTrimmed.isEmpty()) {
            return (getEmpty());
        }

        if (xmlValueTrimmed.matches(xmlDatePatternForNewFormat_String) == true) {
            //
            // Parse new format
            //
            try {
                return (new EcvDate(LocalDate.parse(xmlValueTrimmed, xmlDateFormatter)));
            } catch (DateTimeException ex) {
                LOGGER.log(Level.WARNING, "Error when parsing >" + xmlValue + "<", ex);
                throw (new DateParseException("Invalid date value: >" + xmlValue + "<"));
            }

        } else if (xmlValueTrimmed.matches(xmlDatePatternForOldFormat_String) == true) {
            //
            // Parse old format
            //
            try {
                return (new EcvDate(LocalDate.parse(xmlValueTrimmed, xmlDateParserForOldFormat)));
            } catch (DateTimeException ex) {
                LOGGER.log(Level.WARNING, "Error when parsing >" + xmlValue + "<", ex);
                throw (new DateParseException("Invalid date value: >" + xmlValue + "<"));
            }
        } else {
            //
            // Handle wrong format
            //
            throw (new DateParseException("Unexpected Date Format: >" + xmlValue + "<"));
        }
    }

    /**
     * Creates a new EcvDate object, if the given string contains a valid XML
     * formatted date. If a null pointer is provided, then a null pointer is
     * returned. If the xmlValue is the empty string, null is returned.
     *
     * @param xmlValue
     * @return
     */
    public static EcvDate getDateForXmlValueOrNull(Object xmlValue) {
        if (xmlValue instanceof String) {
            try {
                EcvDate parsedDate = parseXmlValue((String) xmlValue);
                if (parsedDate.isEmpty() == false) {
                    return (parsedDate);
                }
            } catch (DateParseException ex) {
            }
        }
        return (null);
    }

    final public static EcvDate parseUiValue(String uiValue) throws ParseException {
        return (parseUiValue(EcvDate.emptyDate, uiValue));
    }

    /**
     * Supports the changing of a date value in the GUI.
     *
     * @param oldDate
     * @param uiValue
     * @return
     * @throws ParseException
     */
    final public static EcvDate parseUiValue(EcvDate oldDate, String uiValue) throws ParseException {
        assert (oldDate != null);
        assert (uiValue != null);

        Matcher matcher = uiDatePattern.matcher(uiValue);
        if (matcher.matches() == false) {
            throw (new ParseException("Cannot parse \"" + uiValue + "\".", 0));
        }

        String EEE = matcher.group("EEE");
        String dd = matcher.group("dd");
        String MM = matcher.group("MM");
        String yyyy = matcher.group("yyyy");
        String ww = matcher.group("ww");
        String yy = matcher.group("yy");

        if ((EEE == null) && (dd == null) && (MM == null) && (yyyy == null) && (ww == null) && (yy == null)) {
            return (getEmpty());
        }

        if (oldDate.isEmpty() == false) {
            //
            // Changed value. Check changes and set date.
            //
            String uiDate = oldDate.getUiDate();
            String EEE_old = uiDate.substring(0, 2);
            String dd_old = uiDate.substring(3, 5);
            String MM_old = uiDate.substring(6, 8);
            String yyyy_old = uiDate.substring(9, 13);
            String ww_old = uiDate.substring(16, 18);
            String yy_old = uiDate.substring(19, 21);

            if ((dd != null) && (MM != null) && (yyyy != null)
                    && ((dd_old.equals(dd) == false) || (MM_old.equals(MM) == false) || (yyyy_old.equals(yyyy) == false))) {
                // Concrete day given
                return (new EcvDate(new SimpleDateFormat("dd.MM.yyyyyyyyyyyyyy".substring(0, 6 + yyyy.length())).parse(dd + "." + MM + "." + yyyy)));

                //
            } else if ((EEE != null) && (ww != null) && (yy != null)
                    && ((EEE_old.equals(EEE) == false) || (ww_old.equals(ww) == false) || (yy_old.equals(yy) == false))) {
                // CW and day of week given
                return (new EcvDate(new SimpleDateFormat("EEE ww/yy").parse(EEE + ". " + ww + "/" + yy)));
                //
            } else if ((ww != null) && (yy != null)
                    && ((ww_old.equals(ww) == false) || (yy_old.equals(yy) == false))) {
                // CW and day of week given
                return (new EcvDate(new SimpleDateFormat("EEE ww/yy").parse("Fr. " + ww + "/" + yy)));
                //
            } else {
                throw (new ParseException("Not enought values in \"" + uiValue + "\"", 0));
            }
        } else //
        // New value. Check which values are set and set the date.
        //
        if ((dd != null) && (MM != null) && (yyyy != null)) {
            // Concrete day given
            return (new EcvDate(new SimpleDateFormat("dd.MM.yyyyyyyyyyyyyy".substring(0, 6 + yyyy.length())).parse(dd + "." + MM + "." + yyyy)));
            //
        } else if ((EEE != null) && (ww != null) && (yy != null)) {
            // CW and day of week given
            return (new EcvDate(new SimpleDateFormat("EEE ww/yy").parse(EEE + ". " + ww + "/" + yy)));
            //
        } else if ((ww != null) && (yy != null)) {
            // CW given. Take friday as day.
            return (new EcvDate(new SimpleDateFormat("EEE ww/yy").parse("Fr. " + ww + "/" + yy)));
            //
        } else {
            throw (new ParseException("Not enought values in \"" + uiValue + "\"", 0));
        }
    }

    //**************************************************************************
    //
    // Rest of the world
    //
    //**************************************************************************
    /**
     * Builds a string containing the date in the format of the OSLC database.
     *
     * @return
     */
    final public String getOslcValue() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (olscDateFormatter.format(localDate));
        }
    }

    /**
     * Returns the date as a string representation used by RQ1 Templates.
     *
     * @return
     */
    final public String getRq1TemplateValue() {
        return (getOslcValue());
    }

    /**
     * Builds a string containing the date in the format of a XML date field in
     * the RQ1 database.
     *
     * @return
     */
    final public String getXmlValue() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (xmlDateFormatter.format(localDate));
        }
    }

    /**
     * Returns string representation of a day for use in the roadmap
     *
     * @return String representation of date
     */
    final public String getRoadmapValueDay() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (roadmapDateFormaterDay.format(localDate));
        }
    }

    /**
     * Returns string representation of a week for use in the roadmap
     *
     * @return String representation of date
     */
    final public String getRoadmapValueWeek() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return "CW" + (roadmapDateFormaterWeek.format(localDate));
        }
    }

    /**
     * Returns string representation of a month for use in the roadmap
     *
     * @return String representation of date
     */
    final public String getRoadmapValueMonth() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (roadmapDateFormaterMonth.format(localDate));
        }
    }

    /**
     * Returns string representation of a year for use in the roadmap
     *
     * @return String representation of date
     */
    final public String getRoadmapValueYear() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (roadmapDateFormaterYear.format(localDate));
        }
    }

    final public static boolean isNullOrEmpty(EcvDate testDate) {
        if ((testDate == null) || (testDate.isEmpty())) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Checks that the date is no an empty date. The method accepts null values.
     * A null value is considered as an empty date.
     *
     * @param testDate
     * @return
     */
    final public static boolean isNotEmpty(EcvDate testDate) {
        return (!isNullOrEmpty(testDate));
    }

    final public boolean isEmpty() {
        return (localDate == null);
    }

    final public boolean isNotEmpty() {
        return (localDate != null);
    }

    /**
     * Returns the calendar week of the date in the format for the GUI.
     *
     * @return The string representation of the calendar week.
     */
    final public String getUiCwString() {
        if (isEmpty() == true) {
            return ("CW??/??");
        } else {
            return (uiCalendarWeekFormatter.format(localDate));
        }
    }

    /**
     * Returns the calendar week of the date in a format that can be used for
     * comparisions.
     *
     * @return A string containing the calendar week in a compareable format.
     */
    final public String getCmpCwString() {
        if (isEmpty() == true) {
            return ("");
        } else {
            return (cmpCalendarWeekFormatter.format(localDate));
        }
    }

    /**
     *   @return the number of cw week as int
     */
    final public int getCw() {
        if (isEmpty() == true) {
            return -1;
        } else {
            return Integer.parseInt(intCalendarWeekFormatter.format(localDate));
        }
    }

    /**
     *  @return the number of cw week as String with CW prefix
     */
    final public String getCwAsString() {
        if (getCw() < 0){
            return "";
        }else if (getCw() < 10){
            return "CW0" + getCw();
        }else{
            return "CW" + getCw();
        }
    }

    /**
     *  @return the number of quarter
     */
    final public int getQuarter() {
        int month = getMonth();
        if (month >= Calendar.JANUARY && month <= Calendar.MARCH) {
            return 1;

        }else if (month >= Calendar.APRIL && month <= Calendar.JUNE) {
            return 2;

        }else if (month >= Calendar.JULY && month <= Calendar.SEPTEMBER) {
            return  3;
        }else{
            return  4;
        }
    }

    /**
     *  @return the number of quarter as String with Q prefix
     */
    final public String getQuarterAsString() {
        if (getQuarter() < 0){
            return "";
        }else{
            return "Q" + getQuarter();
        }
    }

    /**
     * Returns the date in the format for the UI.
     *
     * @return String filled with the date formatted for the UI.
     */
    final public String getUiDate() {
        if (localDate != null) {
            // Oracle JDK created the localDate without a '.' after the week name.
            // OpenJDK adds a '.' after the week day.
            // To keep the result same, the replace() was added here.
            return (uiDateFormatter.format(localDate).replace(". ", " "));
        } else {
            return ("");
        }
    }

    /**
     * Returns the date in UI format.
     *
     * @return
     */
    @Override
    public String toString() {
        return (getUiDate());
    }

    //**************************************************************************
    //
    // Comparison methods
    //
    //**************************************************************************
    public static int compare(EcvDate date1, EcvDate date2) {
        if ((date1 == null) || (date1.localDate == null)) {
            if (date2 == null) {
                return (0); // null, null
            } else if (date2.localDate == null) {
                return (0); // null, null
            } else {
                return (1); // null, wert
            }
        } else if (date2 == null) {
            return (-1); // wert, null
        } else if (date2.localDate == null) {
            return (-1); // wert, null
        } else {
            return ((int) (date1.epochDay - date2.epochDay)); // wert, wert
        }
    }

    public int compareTo(Date testDate) {
        assert (localDate != null);
        assert (testDate != null);
        return (compareTo(new EcvDate(testDate)));
    }

    @Override
    public int compareTo(EcvDate testDate) {
        //
        // Handle null values for useful row sorting in tabs.
        //
        if (localDate == null) {
            if (testDate == null) {
                return (0); // null, null
            } else if (testDate.localDate == null) {
                return (0); // null, null
            } else {
                return (1); // null, wert
            }
        } else if (testDate == null) {
            return (-1); // wert, null
        } else if (testDate.localDate == null) {
            return (-1); // wert, null
        } else {
            return ((int) (epochDay - testDate.epochDay)); // wert, wert
        }

    }

    public int compareTo(LocalDate testDate) {
        //
        // Handle null values for useful row sorting in tabs.
        //
        if (localDate == null) {
            if (testDate == null) {
                return (0); // null, null
            } else {
                return (1); // null, wert
            }
        } else {
            return ((int) (epochDay - testDate.toEpochDay())); // wert, wert
        }

    }

    /**
     * This is a package private method for testing purposes.
     */
    int testCompare(String testDate) {
        assert (localDate != null);
        assert (testDate != null);
        assert (testDate.matches(cmpDatePattern_String)) : testDate;

        int year = Integer.parseInt(testDate.substring(0, 4));
        int month = Integer.parseInt(testDate.substring(4, 6));
        int day = Integer.parseInt(testDate.substring(6, 8));

        LocalDate test = LocalDate.of(year, month, day);

        return (localDate.compareTo(test));
    }

    /**
     *
     * @param testDate Format: YYYYmmdd
     * @return
     */
    public boolean equals(String testDate) {
        assert (testDate != null);
        assert (testDate.matches(cmpDatePattern_String)) : testDate;
        if (localDate == null) {
            return (false);
        }
        return (testCompare(testDate) == 0);
    }

    public boolean equals(Date testDate) {
        assert (testDate != null);
        if (localDate == null) {
            return (false);
        }
        return (compareTo(testDate) == 0);
    }

    public boolean equals(EcvDate testDate) {
        if (testDate == null) {
            return (false);
        } else if (testDate == this) {
            return (true);
        } else if (isEmpty() != testDate.isEmpty()) {
            return (false);
        } else if (isEmpty() == true) {
            return (true);
        } else {
            return (compareTo(testDate) == 0);
        }
    }

    @Override
    public boolean equals(Object testDate) {
        if ((testDate == null) || ((testDate instanceof EcvDate) == false)) {
            return (false);
        }
        return (equals((EcvDate) testDate));
    }

    /**
     * Compares two date values for equality. Null values are allowed.
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean equals(EcvDate date1, EcvDate date2) {
        if (date1 != null) {
            return (date1.equals(date2));
        } else {
            return (date2 == null);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.localDate);
        return hash;
    }

    /**
     * Returns true, if the date is set and earlier then today.
     *
     * @return true, if the date is set and earlier then today.
     */
    public boolean isInThePast() {
        if (this.isEmpty()) {
            return (false);
        } else {
            return (compareTo(LocalDate.now()) < 0);
        }
    }

    /**
     * Returns true, if the date is set and later then or equal to today.
     *
     * @return true, if the date is set and later then or equal to today.
     */
    public boolean isNotInThePast() {
        if (isEmpty()) {
            return (false);
        } else {
            return (!isInThePast());
        }
    }

    public boolean isToday() {
        if (this.isEmpty()) {
            return (false);
        } else {
            return (compareTo(LocalDate.now()) == 0);
        }
    }

    /**
     * Returns true, if the date is set and later then today.
     *
     * @return true, if the date is set and later then today.
     */
    public boolean isInTheFuture() {
        if (this.isEmpty()) {
            return (false);
        } else {
            return (compareTo(LocalDate.now()) > 0);
        }
    }

    public boolean isEarlierThen(EcvDate testDate) {
        assert (testDate != null);
        if (this.isEmpty() || testDate.isEmpty()) {
            return (false);
        } else {
            return (compareTo(testDate) < 0);
        }
    }

    /**
     * Checks whether or not the date in the object is earlier or equal to test
     * date. Returns false, if any of the two dates is empty.
     *
     * @param testDate
     * @return
     */
    public boolean isEarlierOrEqualThen(EcvDate testDate) {
        assert (testDate != null);
        if (this.isEmpty() || testDate.isEmpty()) {
            return (false);
        } else {
            return (compareTo(testDate) <= 0);
        }
    }

    public boolean isLaterThen(EcvDate testDate) {
        assert (testDate != null);
        if (this.isEmpty() || testDate.isEmpty()) {
            return (false);
        } else {
            return (compareTo(testDate) > 0);
        }
    }

    public boolean isLaterOrEqualThen(EcvDate testDate) {
        assert (testDate != null);
        if (this.isEmpty() || testDate.isEmpty()) {
            return (false);
        } else {
            return (compareTo(testDate) >= 0);
        }
    }

    //**************************************************************************
    //
    // Date arithmetics
    //
    //**************************************************************************
    /**
     * Returns the number of days between to dates.
     *
     * @param secondDate
     * @return &gt; 0 if secondDate is later, 0 if secondDate is equal, &lt; 0
     * if second date is before
     */
    public int getDaysBetween(EcvDate secondDate) {
        assert (secondDate != null);

        if ((isEmpty() == false) && (secondDate.isEmpty() == false)) {
            return ((int) (secondDate.epochDay - epochDay));
        }
        return (0);
    }

    /**
     * This method is used to get the noOfDaysCountFromGiven start and endDate
     * without weekends.
     *
     * If Start and end date is same returns 0. Both start and end date will be
     * take for count consideration.
     *
     * @param secondDate
     * @return &gt; 0 if secondDate is later, 0 if secondDate is equal, &lt; 0
     * if second date is before
     */
    public int getDaysBetweenExcludingWeekends(EcvDate secondDate) {
        assert (secondDate != null);

        //
        // Special cases: Empty dates and same day
        //
        if (isEmpty() || secondDate.isEmpty() || (epochDay == secondDate.epochDay)) {
            return (0);
        }

        //
        // Move days away from weekend
        //
        long thisEpochDay;
        DayOfWeek thisDayOfWeek;
        if (this.dayOfWeek == DayOfWeek.SATURDAY) {
            thisEpochDay = this.epochDay - 1;
            thisDayOfWeek = DayOfWeek.FRIDAY;
        } else if (this.dayOfWeek == DayOfWeek.SUNDAY) {
            thisEpochDay = this.epochDay - 2;
            thisDayOfWeek = DayOfWeek.FRIDAY;
        } else {
            thisEpochDay = this.epochDay;
            thisDayOfWeek = this.dayOfWeek;
        }
        long secondEpochDay;
        DayOfWeek secondDayOfWeek;
        if (secondDate.dayOfWeek == DayOfWeek.SATURDAY) {
            secondEpochDay = secondDate.epochDay - 1;
            secondDayOfWeek = DayOfWeek.FRIDAY;
        } else if (secondDate.dayOfWeek == DayOfWeek.SUNDAY) {
            secondEpochDay = secondDate.epochDay - 2;
            secondDayOfWeek = DayOfWeek.FRIDAY;
        } else {
            secondEpochDay = secondDate.epochDay;
            secondDayOfWeek = secondDate.dayOfWeek;
        }

        //
        // Check if dates are now on the same friday
        //
        if (thisEpochDay == secondEpochDay) {
            return (0);
        }

        //
        // Order dates
        //
        long sign;
        long startEpochDay;
        long endEpochDay;
        DayOfWeek startDayOfWeek;
        if (thisEpochDay < secondEpochDay) {
            sign = +1;
            startEpochDay = thisEpochDay;
            startDayOfWeek = thisDayOfWeek;
            endEpochDay = secondEpochDay;
        } else {
            sign = -1;
            startEpochDay = secondEpochDay;
            startDayOfWeek = secondDayOfWeek;
            endEpochDay = thisEpochDay;
        }

        //
        // Split in full weeks and remaining days
        //
        long totalDaysBetween = endEpochDay - startEpochDay;
        long numberOfWeeksSpanned = totalDaysBetween / 7;
        long remainingDays = totalDaysBetween - (7 * numberOfWeeksSpanned);

        //
        // Remove weekend from remaining days
        //
        if (remainingDays > 0) {
            switch (startDayOfWeek) {
                case MONDAY:
                    assert (remainingDays <= 5) : "Invalid value " + remainingDays + " reached for " + this.getXmlValue() + " and " + secondDate.getXmlValue();
                    break;
                case TUESDAY:
                    if (remainingDays > 3) {
                        remainingDays -= 2;
                    }
                    break;
                case WEDNESDAY:
                    if (remainingDays > 2) {
                        remainingDays -= 2;
                    }
                    break;
                case THURSDAY:
                    if (remainingDays > 1) {
                        remainingDays -= 2;
                    }
                    break;
                case FRIDAY:
                    remainingDays -= 2;
                    break;
                default:
                    throw (new Error("Invalid state reached for " + this.getXmlValue() + " and " + secondDate.getXmlValue()));
            }
        }

        long workingDaysBetween = (5 * numberOfWeeksSpanned) + remainingDays;

        return ((int) (sign * workingDaysBetween));
    }

    /**
     * Returns the number of months between two dates.
     *
     * @param secondDate
     * @return number of dates between two dates
     */
    public int getMonthsBetween(EcvDate secondDate) {
        return (secondDate.getYear() - getYear()) * 12 + (secondDate.getMonth() - getMonth()) + 1;
    }

    /**
     * Returns a date with the same month and year but on the first day of the
     * month.
     *
     * @return EcvDate with the same year and month but with the 1 for the day.
     */
    public EcvDate getFirstDayOfMonth() {
        return (new EcvDate(localDate.withDayOfMonth(1)));
    }

    public EcvDate getLastDayOfWeek() {
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return (this);
        } else {
            return (new EcvDate(localDate.plusDays(7 - dayOfWeek.getValue())));
        }
    }

    /**
     * Returns a date with the same month and year but on the last day of the
     * month.
     *
     * @return
     */
    public EcvDate getLastDayOfMonth() {
        return (new EcvDate(localDate.plusMonths(1).withDayOfMonth(1).minusDays(1)));
    }

    /**
     * This method is used to get Monday and Friday based on given date.From
     * Given date this method will identify the week of the year, from that week
     * it will return Monday and Friday in a list.
     *
     * From Given date this method will identify the week of the * year,in which
     * week this date will be present from that week it will return Monday and
     * Friday dates in a list.
     *
     * @return Monday will be 0th position and FriDay will be 1st position of
     * the list.
     */
    public List<EcvDate> getMonAndFridayOfTheWeekBasedOnGivenDate() {
        List<EcvDate> dateList = new ArrayList<>();

        if (dayOfWeek == DayOfWeek.MONDAY) {
            dateList.add(this);
        } else {
            dateList.add(this.addDays(DayOfWeek.MONDAY.getValue() - dayOfWeek.getValue()));
        }

        if (dayOfWeek == DayOfWeek.FRIDAY) {
            dateList.add(this);
        } else {
            dateList.add(this.addDays(DayOfWeek.FRIDAY.getValue() - dayOfWeek.getValue()));
        }
        return dateList;
    }

    public EcvDate getLastDayOfYear() {
        return (new EcvDate(localDate.withMonth(12).withDayOfMonth(31)));
    }

    /**
     * This method is used to get WeekDayFromGiven Date.If day is weekdays
     * method will returns weekday itself.If day is an Saturday and Sunday this
     * method will return current Friday of the week.
     *
     * @return returns WeekDay.
     */
    public EcvDate getPreviousWeekDay() {
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return (addDays(-1));
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            return (addDays(-2));
        }
        return (this);
    }

    /**
     * This method is used to get NextWeekDay.If Date is Saturday or Sunday it
     * will next following Monday and If given date is weekDay it returns Given
     * WeekDay itself.
     *
     * @return returns WeekDay.
     */
    public EcvDate getNextWeekDay() {
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            return (addDays(+2));
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            return (addDays(+1));
        }
        return (this);
    }

    /**
     * This method is used to check given date is Friday or not.
     *
     * @return If Given date is Friday returns True else returns False.
     */
    public Boolean checkIsFriday() {
        return (dayOfWeek == DayOfWeek.FRIDAY);
    }

    /**
     * Returns the number of days, excluding Saturday and Sunday, within the
     * period defined by the date of the object and the secondDate. The period
     * goes from the earlier date to the later date, both dates inclusive. The
     * returned value is always > 0.
     *
     * @param secondDate
     * @return
     */
    public int getDaysInPeriodExcludingWeekends(EcvDate secondDate) {
        assert (secondDate != null);

        // Get days between and add one, if the first day is a weekday.
        int daysBetween = Math.abs(getDaysBetweenExcludingWeekends(secondDate));
        if (epochDay < secondDate.epochDay) {
            if ((dayOfWeek != DayOfWeek.SATURDAY) && (dayOfWeek != DayOfWeek.SUNDAY)) {
                return (daysBetween + 1);
            }
        } else {
            if ((secondDate.dayOfWeek != DayOfWeek.SATURDAY) && (secondDate.dayOfWeek != DayOfWeek.SUNDAY)) {
                return (daysBetween + 1);
            }
        }
        return (daysBetween);
    }

    /**
     * Returns the calendar weeks between to dates.
     *
     * @param secondDate
     * @return &gt; 0 if secondDate is later, 0 if secondDate is equal, &lt; 0
     * if second date is before
     */
    public int getCalendarWeeksBetween(EcvDate secondDate) {
        assert (secondDate != null);
        if ((isEmpty() == false) && (secondDate.isEmpty() == false)) {

            int daysSinceMonday = dayOfWeek.getValue() - 1;
            long dayNumberForFirstDayOfWeek = epochDay - daysSinceMonday;
            long dayNumberForSecondDate = secondDate.epochDay;
            float difference = dayNumberForSecondDate - dayNumberForFirstDayOfWeek;

            return ((int) Math.floor(difference / 7));

        } else {
            return (0);
        }
    }

    /**
     * Returns the date of the beginning of the next or previous CW. diff = 0 -
     * beginning of the current CW
     *
     * @param diff
     * @return
     */
    public EcvDate getDateOfCalenderWeekStartInRelationToCurrentDate(int diff) {
        if ((diff == 0) && (dayOfWeek == DayOfWeek.MONDAY)) {
            return (this);
        } else {
            return (new EcvDate(localDate.plusWeeks(diff).plusDays(DayOfWeek.MONDAY.getValue() - dayOfWeek.getValue())));
        }
    }

    /**
     * Returns the earliest of both dates. Or the empty date, if both dates are
     * empty.
     *
     * @param secondDate
     * @return
     */
    public EcvDate getEarliest(EcvDate secondDate) {
        assert (secondDate != null);
        if (isEmpty()) {
            return (secondDate);
        } else if (secondDate.isEmpty()) {
            return (this);
        } else if (isLaterThen(secondDate)) {
            return (secondDate);
        } else {
            return (this);
        }
    }

    /**
     * Returns the later of both dates. Or the empty date, if both dates are
     * empty.
     *
     * @param secondDate
     * @return
     */
    public EcvDate getLatest(EcvDate secondDate) {
        assert (secondDate != null);
        if (isEmpty()) {
            return (secondDate);
        } else if (secondDate.isEmpty()) {
            return (this);
        } else if (isEarlierThen(secondDate)) {
            return (secondDate);
        } else {
            return (this);
        }
    }

    /**
     * Returns a new EcvDate object which is numberOfDays different to the date.
     *
     * @param numberOfDays Number of dates to add. Negative values give dates
     * earlier then the source date.
     * @return
     */
    public EcvDate addDays(long numberOfDays) {
        if (localDate != null) {
            return (new EcvDate(localDate.plusDays(numberOfDays)));
        } else {
            return (getEmpty());
        }
    }

    /**
     * This method adds workDays (MONDAY - FRIDAY) to a given number of days. If
     * the number of days is negative than this method subtracts the working
     * days from the calendar object.
     *
     *
     * @param numberOfDays
     * @return new EcvDate instance
     */
    public EcvDate addWorkDays(int numberOfDays) {
        if (localDate != null) {

            if (numberOfDays == 0) {
                return (this);
            }

            long resultDayOfEpoch;
            if (numberOfDays > 0) {
                resultDayOfEpoch = addWorkDays_forward(numberOfDays);
            } else {
                resultDayOfEpoch = addWorkDays_backward(-numberOfDays);
            }

            return (new EcvDate(LocalDate.ofEpochDay(resultDayOfEpoch)));

        } else {
            return (getEmpty());
        }
    }

    private long addWorkDays_forward(int numberOfDays) {
        assert (numberOfDays > 0);

        // Move to next monday, if it is a weekend.
        long startEpochDay;
        DayOfWeek startDayOfWeek;
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            startEpochDay = epochDay + 2;
            startDayOfWeek = DayOfWeek.MONDAY;
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            startEpochDay = epochDay + 1;
            startDayOfWeek = DayOfWeek.MONDAY;
        } else {
            startEpochDay = epochDay;
            startDayOfWeek = dayOfWeek;
        }

        int workDaysTillFirstWeekend = DayOfWeek.FRIDAY.getValue() - startDayOfWeek.getValue();
        if (numberOfDays <= workDaysTillFirstWeekend) {
            return (startEpochDay + numberOfDays);
        }

        int workDaysAfterFirstWeekend = numberOfDays - workDaysTillFirstWeekend;
        int fullWeeks = (workDaysAfterFirstWeekend - 1) / 5;
        int workDaysInLastWeek = workDaysAfterFirstWeekend - (fullWeeks * 5);

        return (startEpochDay + workDaysTillFirstWeekend + 2 + (7 * fullWeeks) + workDaysInLastWeek);
    }

    private long addWorkDays_backward(int numberOfDays) {
        assert (numberOfDays > 0);

        // Move to previous friday, if it is a weekend.
        long startEpochDay;
        DayOfWeek startDayOfWeek;
        if (dayOfWeek == DayOfWeek.SATURDAY) {
            startEpochDay = epochDay - 1;
            startDayOfWeek = DayOfWeek.FRIDAY;
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            startEpochDay = epochDay - 2;
            startDayOfWeek = DayOfWeek.FRIDAY;
        } else {
            startEpochDay = epochDay;
            startDayOfWeek = dayOfWeek;
        }

        int workDaysTillFirstWeekend = startDayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
        if (numberOfDays <= workDaysTillFirstWeekend) {
            return (startEpochDay - numberOfDays);
        }

        int workDaysAfterFirstWeekend = numberOfDays - workDaysTillFirstWeekend;
        int fullWeeks = (workDaysAfterFirstWeekend - 1) / 5;
        int workDaysInLastWeek = workDaysAfterFirstWeekend - (fullWeeks * 5);

        return (startEpochDay - workDaysTillFirstWeekend - 2 - (7 * fullWeeks) - workDaysInLastWeek);
    }

    public EcvDate addWeek(int numberOfWeeks) {
        if (localDate != null) {
            return (new EcvDate(localDate.plusWeeks(numberOfWeeks)));
        } else {
            return (getEmpty());
        }
    }

    public EcvDate addMonth(int numberOfMonth) {
        if (localDate != null) {
            return (new EcvDate(localDate.plusMonths(numberOfMonth)));
        } else {
            return (getEmpty());
        }
    }

    public EcvDate addYear(int numberOfYear) {
        if (localDate != null) {
            return (new EcvDate(localDate.plusYears(numberOfYear)));
        } else {
            return (EcvDate.getEmpty());
        }
    }

    //**************************************************************************
    //
    // Getters for specific format or parts
    //
    //**************************************************************************
    public String getDayOfWeekName() {
        return (getUiDate().substring(0, 2));
    }

    public String getWeekOfYear() {
        return (localDate.format(getWeekOfYearFormat));
    }

    public String getDayOfMonthAsString() {
        return (Integer.toString(localDate.getDayOfMonth()));
    }

    /**
     * Returns the day of the week.
     *
     * 1 is monday
     *
     * 7 is sunday
     */
    public int getDayOfWeek() {
        if (localDate != null) {
            return (dayOfWeek.getValue());
        } else {
            return (0);
        }
    }

    /**
     * Returns the month as number.
     *
     * @return 0 - 11
     */
    public int getMonth() {
        if (localDate != null) {
            return (localDate.getMonthValue() - 1);
        } else {
            return (0);
        }
    }

    public String getMonthAsString() {
        return (Integer.toString(localDate.getMonthValue()));
    }

    public String getYearAsString() {
        return (Integer.toString(localDate.getYear()));
    }

    public String getYearAsShortString() {
        return (getYearAsString().substring(2, 4));
    }

    /**
     * The day in the month starting with 0 for the first day.
     *
     * @return
     */
    public int getDayOfMonth() {
        if (localDate != null) {
            return (localDate.getDayOfMonth());
        } else {
            return (0);
        }
    }

    /**
     * Returns the corresponding year value
     *
     * @return year in integer
     */
    public int getYear() {
        if (localDate != null) {
            return (localDate.getYear());
        } else {
            return (0);
        }
    }

    public static List<EcvDate> findDatesInString(String input) {
        List<EcvDate> output = new ArrayList<>();

        if (input == null) {
            return output;
        }

        List<Matcher> matcherList = new ArrayList<>();

        for (MyPattern pattern : patternList) {
            matcherList.add(pattern.matcher(input));
        }

        for (Matcher matcher : matcherList) {
            while (matcher.find()) {
                EcvDate ecvDate = matchedStringToEcvDate(matcher.group(), matcher.pattern().pattern());

                if (ecvDate != null) {
                    output.add(ecvDate);
                }
            }
        }

        return output;
    }

    private static EcvDate matchedStringToEcvDate(String matchedString, String patternString) {
        try {
            switch (patternString) {

                case oslcDatePattern_String:
                    return (parseOslcValue(matchedString));

                case xmlDatePatternForOldFormat_String:
                    return (parseXmlValue(matchedString));

                case xmlDatePatternForNewFormat_String:
                    return (parseXmlValue(matchedString));

                default:
                    return null;
            }
        } catch (DateParseException e) {
            return null;
        }
    }

}
