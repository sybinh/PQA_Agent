/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 *
 * @author GUG2WI
 */
public class EcvParser {

    static final private String DEFAULT_NAME_ALLOWED_FIRST_CHARACTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_";
    static final private String DEFAULT_NAME_ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789:_-.";

    private String nameAllowedFirstCharacter = DEFAULT_NAME_ALLOWED_FIRST_CHARACTER;
    private String nameAllowedCharacters = DEFAULT_NAME_ALLOWED_CHARACTERS;

    /**
     * Represents the parent element, if a parse exception occurred during the
     * parsing of a sub element.
     */
    static public interface Parent {

        /**
         * Return the full name, which is the path of element names to the
         * current element, of the element.
         *
         * @return The full name which means the path name.
         */
        public String getFullName();
    }

    static public class ParseException extends Exception {

        private String message;
        private String readData;
        private String nextData;
        private Parent parent;

        ParseException(String message, EcvPositionAwarePushbackReader pbReader) {
            super(message);
            init(message, pbReader, null);
        }

        ParseException(String message, EcvPositionAwarePushbackReader pbReader, Parent parent) {
            super(message);
            init(message, pbReader, parent);
        }

        ParseException(String message, EcvPositionAwarePushbackReader pbReader, Throwable cause) {
            super(message, cause);
            init(message, pbReader, null);
        }

        ParseException(String message, EcvPositionAwarePushbackReader pbReader, Parent parent, Throwable cause) {
            super(message, cause);
            init(message, pbReader, parent);
        }

        private void init(String message, EcvPositionAwarePushbackReader pbReader, Parent parent) {
            assert (pbReader != null);
            assert (message != null);

            this.message = message;
            this.readData = pbReader.getReadData();
            this.parent = parent;
            try {
                nextData = pbReader.getNextData(100);
            } catch (IOException ex) {
                // Do nothing. We are already in error routine.
            }
        }

        @Override
        public String getMessage() {
            StringBuilder builder = new StringBuilder(300);
            builder.append(message);
            if (parent != null) {
                builder.append("\n");
                builder.append("Parent element: ").append(parent.getFullName());
            }
            return (builder.toString());
        }

        public String getErrorLog() {
            StringBuilder builder = new StringBuilder(300);

            builder.append("=== Parse Error Start ===\n");
            builder.append(message).append("\n");
            if (parent != null) {
                builder.append("Parent element: ").append(parent.getFullName()).append("\n");
            }
            if (readData != null) {
                builder.append("--- Already read data (").append(readData.length()).append(" Byte) ---\n");
                builder.append(readData).append("\n");
            } else {
                builder.append("--- Already read data (").append(0).append(" Byte) ---\n");
            }

            if (nextData != null) {
                builder.append("--- Next data (").append(nextData.length()).append(" Byte) ---\n");
            } else {
                builder.append("--- Next data (").append(0).append(" Byte) ---\n");
            }

            builder.append(nextData).append("\n");

            builder.append("=== Parse Error End ===\n");

            return (builder.toString());
        }

        /**
         * Checks whether or not the exception was thrown after all available
         * input was read.
         *
         * @return True, if no further input was available when the error
         * occurred.
         */
        public boolean endOfInputReached() {
            if ((nextData == null) || (nextData.isEmpty() == true)) {
                return (true);
            } else {
                return (false);
            }
        }
    }

    final protected EcvPositionAwarePushbackReader input;
    protected Parent currentParent; // Used only for error handling in methods called for parsing. This reduces the parameter list of this methods.

    protected EcvParser(String inputString) {
        assert (inputString != null);
        input = new EcvPositionAwarePushbackReader(new StringReader(inputString));
    }

    protected EcvParser(File file) throws FileNotFoundException {
        assert (file != null);
        input = new EcvPositionAwarePushbackReader(new FileReader(file));
    }

    /**
     * Creates a parser for parsing from the provided InputStreamReader.
     *
     * @param inputReader Reader from which the XML shall be parsed.
     */
    protected EcvParser(InputStreamReader inputReader) {
        assert (inputReader != null);
        input = new EcvPositionAwarePushbackReader(inputReader);
    }

    protected EcvParser(EcvPositionAwarePushbackReader pushBackReader) {
        assert (pushBackReader != null);
        input = pushBackReader;
    }

    final protected void setNameAllowedCharacters(String nameAllowedFirstCharacter, String nameAllowedCharacters) {
        assert (nameAllowedFirstCharacter != null);
        assert (nameAllowedFirstCharacter.isEmpty() == false);
        assert (nameAllowedCharacters != null);
        assert (nameAllowedCharacters.isEmpty() == false);

        this.nameAllowedFirstCharacter = nameAllowedFirstCharacter;
        this.nameAllowedCharacters = nameAllowedCharacters;
    }

    public String getReadData() {
        return input.getReadData();
    }

    /**
     * Ensures that the content on the current input position matches the given
     * string. An exception is thrown, if this is not the case. The current
     * input position is moved after the expected string, if the string matches.
     *
     * @param expected The string that is expected to start at the current input
     * position.
     * @throws ParseException
     *
     */
    protected void expectString(String expected) throws ParseException {
        try {
            for (int i = 0; i < expected.length(); i++) {
                int readChar = input.read();
                if ((char) readChar != expected.charAt(i)) {
                    String txt = "Expected string \"" + expected + "\" not found at " + input.getPosition().toString();
                    throw new ParseException(txt, input, currentParent);
                }
            }
        } catch (IOException ex) {
            String txt = "Expected string \"" + expected + "\" not found at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
    }

    protected String parseName() throws ParseException {
        StringBuilder name = new StringBuilder(50);
        boolean firstCharacter = true;
        int c_int;
        int i;
        char c;
        try {
            while ((c_int = input.read()) >= 0) {
                c = (char) c_int;
                if (firstCharacter) {
                    if (nameAllowedFirstCharacter.indexOf(c) < 0) {
                        String txt = "Name expected at " + input.getPosition().toString();
                        throw new ParseException(txt, input, currentParent);
                    }
                    firstCharacter = false;
                } else {
                    if (nameAllowedCharacters.indexOf(c) < 0) {
                        try {
                            input.unread(c);
                        } catch (IOException ex) {
                            String txt = "Unread failed at " + input.getPosition().toString() + " when scanning for name.";
                            throw new ParseException(txt, input, currentParent, ex);
                        }
                        break;
                    }
                }
                name.append(c);
            }
        } catch (IOException ex) {
            String txt = "Read failed at " + input.getPosition().toString() + " when scanning for name.";
            throw new ParseException(txt, input, currentParent, ex);
        }
        if (name.length() == 0) {
            String txt = "Name expected at " + input.getPosition().toString() + " but EOF reached.";
            throw new ParseException(txt, input, currentParent);
        }
        String nameString = name.toString();
        return nameString;
    }

    /**
     * Checks whether or not the input on the current input position matches the
     * given character. The current input position remains unchanged.
     *
     * @param c The character for which the test shall be done.
     * @return true, if the character matches; false otherwise
     * @throws ParseException
     */
    protected boolean checkCharacter(char c) throws ParseException {
        try {
            return (lookAhead() == c);
        } catch (ParseException ex) {
            String txt = "Look ahead failed while testing for character '" + c + "' at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
    }

    /**
     * Checks whether or not the input on the current input position matches the
     * given character. The current input position remains unchanged, if there
     * is no match. The current input position is moved behind the matching
     * character, if there is a match.
     *
     * @param c The character for which the test shall be done.
     * @return true, if the character matches; false otherwise
     * @throws ParseException
     */
    protected boolean checkAndSkipCharacter(char c) throws ParseException {

        //
        // Read character
        //
        char r;
        try {
            r = (char) input.read();
        } catch (IOException ex) {
            String txt = "Read failed while check and skip for character .'" + c + "' at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }

        //
        // Stop if matches
        //
        if (r == c) {
            return (true);
        }

        //
        // Unread not matching character
        //
        try {
            input.unread(r);
        } catch (IOException ex) {
            String txt = "Unread failed while check and skip for character .'" + c + "' at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        return (false);
    }

    /**
     * Gets character on the current input position. The current input position
     * remains unchanged. Note that the method fails with an ParseException, if
     * the end of input was reached.
     *
     * @return The character on the current input position.
     * @throws ParseException If read fails. Also if end of input was reached.
     */
    protected char lookAhead() throws ParseException {
        char r;
        try {
            r = (char) input.read();
        } catch (IOException ex) {
            String txt = "Read failed while look ahead." + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        try {
            input.unread(r);
        } catch (IOException ex) {
            String txt = "Unread failed while look ahead." + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        return (r);
    }

    /**
     * Checks that the content on the current input position matches the given
     * string. The current input position is unchanged.
     *
     * @param s String that shall be checked.
     * @return true, if the tested string is on that input position. false
     * otherwise.
     * @throws util.EcvParser.ParseException
     *
     */
    protected boolean checkString(String s) throws ParseException {
        char[] r = new char[s.length()];
        try {
            for (int i = 0; i < s.length(); i++) {
                r[i] = (char) input.read();
            }
        } catch (IOException ex) {
            String txt = "Read failed while testing for string \"" + s + "\" at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        try {
            for (int i = s.length() - 1; i >= 0; i--) {
                input.unread(r[i]);
            }
        } catch (IOException ex) {
            String txt = "Unread failed while testing for string \"" + s + "\" at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != r[i]) {
                return false;
            }
        }
        return true;
    }

    protected String parseQuotedText() throws ParseException {
        if (checkCharacter('\'')) {
            expectString("'");
            return parseTextInclusive('\'');
        } else if (checkCharacter('"')) {
            expectString("\"");
            return parseTextInclusive('"');
        } else {
            return "";
        }
    }

    protected String parseTextInclusive(char delimiter) throws ParseException {
        String text = parseTextExclusive(delimiter);
        expectCharacter(delimiter);
        return text;
    }

    protected String parseTextExclusive(char delimiter) throws ParseException {
        StringBuilder text = new StringBuilder(100);
        int c_int;
        char c;
        EcvPositionAwarePushbackReader.Position startPosition = input.getPosition();
        try {
            while ((c_int = input.read()) >= 0) {

                c = (char) c_int;
                if (c == delimiter) {
                    input.unread(c_int);
                    return text.toString();
                } else {
                    text.append(c);
                }
            }
        } catch (IOException ex) {
            String txt = "Read failed at " + input.getPosition().toString() + ", when scanning text starting at " + startPosition.toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        // EOF while scanning for delimiter
        String txt = "EOF at " + input.getPosition().toString() + " while scanning text starting at " + startPosition.toString();
        throw new ParseException(txt, input, currentParent);
    }

    @SuppressWarnings(value = "empty-statement")
    protected void skipCharacters(String toSkip) throws ParseException {
        int c_int;
        try {
            while (toSkip.indexOf(c_int = input.read()) >= 0) {
                ;
            }
        } catch (IOException ex) {
            String txt = "Read failed while skipping \"" + toSkip + "\" at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        if (c_int >= 0) {
            try {
                input.unread(c_int);
            } catch (IOException ex) {
                String txt = "Unread failed while skipping \"" + toSkip + "\" at " + input.getPosition().toString();
                throw new ParseException(txt, input, currentParent, ex);
            }
        }
    }

    /**
     * Ensures that the content on the current input position continues with
     * characters from the allowed characters. An exception is thrown, if this
     * is not the case. The current input position is moved after the part of
     * input consisting of allowed characters. Additionally, the part of allowed
     * characters is returned.
     *
     * @param allowedCharacters The list of characters allowed.
     * @return The part of allowed characters.
     * @throws ParseException
     */
    protected String expectCharacters(String allowedCharacters) throws ParseException {
        StringBuilder parsedText = new StringBuilder(100);
        int c_int;
        try {
            while (allowedCharacters.indexOf(c_int = input.read()) >= 0) {
                parsedText.append((char) c_int);
            }
        } catch (IOException ex) {
            String txt = "Read failed while parsing \"" + allowedCharacters + "\" at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        if (c_int >= 0) {
            try {
                input.unread(c_int);
            } catch (IOException ex) {
                String txt = "Unread failed while parsing \"" + allowedCharacters + "\" at " + input.getPosition().toString();
                throw new ParseException(txt, input, currentParent, ex);
            }
        }
        if (parsedText.length() == 0) {
            String txt = "Expected characters '" + allowedCharacters + "' not found at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent);
        }
        return parsedText.toString();
    }

    protected void skipWhitespace() throws EcvXmlParser.ParseException {
        skipCharacters(" \t\n\r\u00A0");
    }

    /**
     * Ensures that the content on the current input position matches the given
     * character. An exception is thrown, if this is not the case. The current
     * input position is moved after the expected character, if the character
     * matches.
     *
     * @param expected The character that is expected at the current input
     * position.
     * @throws ParseException
     *
     */
    protected void expectCharacter(char expected) throws EcvXmlParser.ParseException {
        try {
            char r = (char) input.read();
            if (r != expected) {
                String txt = "Expected char '" + expected + "' not found at " + input.getPosition().toString();
                throw new ParseException(txt, input, currentParent);
            }
        } catch (IOException ex) {
            String txt = "Expected char '" + expected + "' not found at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
    }

    /**
     * Walks through the input stream until the end string is found and moves
     * the input position after the end string.
     * <p>
     * This implementation uses a very simple algorithm! It does not work if the
     * searched string contains repeated characters! But it works for the end of
     * comments in XML, because comments are not allowed to contain "--".
     *
     * @param end End string.
     * @throws ParseException
     */
    protected void skipAfterEndString(String end) throws ParseException {
        EcvPositionAwarePushbackReader.Position startPosition = input.getPosition();
        int positionInEndString = 0;
        int c_int;
        try {
            while ((c_int = input.read()) >= 0) {
                if ((char) c_int == end.charAt(positionInEndString)) {
                    positionInEndString++;
                    if (positionInEndString >= end.length()) {
                        return;
                    }
                } else {
                    // This is a very simple algorithm! It does not work if the searched string contains repeated characters!
                    positionInEndString = 0;
                }
            }
        } catch (IOException ex) {
            String txt = "Read failed at " + input.getPosition().toString() + ", when scanning for \"" + end + "\" from " + startPosition.toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        //
        // EOF while scanning for end string :-(
        //
        String txt = "Expected string \"" + end + "\" not found from " + startPosition.toString() + " to EOF at " + input.getPosition().toString();
        throw new ParseException(txt, input, currentParent);
    }

    /**
     * Walks through the input stream until the end string is found, moves the
     * input position after the end string and returns the text before the end
     * string.
     * <p>
     * This implementation uses a very simple algorithm! It does not work if the
     * searched string contains repeated characters! But it works for the end of
     * comments in XML, because comments are not allowed to contain "--".
     *
     * @param end End string.
     * @throws ParseException
     */
    protected String parseWithoutEndString(String end, int sizeLimit) throws ParseException {
        EcvPositionAwarePushbackReader.Position startPosition = input.getPosition();
        StringBuilder result = new StringBuilder();
        while (checkString(end) == false) {
            try {
                int c_int = input.read();
                if (c_int < 0) {
                    //
                    // EOF while scanning for end string :-(
                    //
                    String txt = "Expected string \"" + end + "\" not found from " + startPosition.toString() + " to EOF at " + input.getPosition().toString();
                    throw new ParseException(txt, input, currentParent);
                }
                result.append((char) c_int);
                if ( result.length() >= sizeLimit){
                    //
                    // Size limit reached while scanning for end string :-(
                    //
                    String txt = "Expected string \"" + end + "\" not found from " + startPosition.toString() + " to size limit at " + input.getPosition().toString();
                    throw new ParseException(txt, input, currentParent);
                }
            } catch (IOException ex) {
                String txt = "Read failed at " + input.getPosition().toString() + ", when scanning for \"" + end + "\" from " + startPosition.toString();
                throw new ParseException(txt, input, currentParent, ex);
            }
        }
        expectString(end);
        return (result.toString());
    }

    final private int EOF_1 = -1;
    final private int EOF_65535 = 65535; // Some JDK deliver 65535 instead of -1 for EOF.

    protected boolean checkEnd() throws ParseException {
        int c_int;
        try {
            c_int = input.read();
        } catch (IOException ex) {
            String txt = "Read failed while testing for end at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        if ((c_int == EOF_1) || (c_int == EOF_65535)) {
            return true;
        }
        try {
            input.unread(c_int);
        } catch (IOException ex) {
            String txt = "Unread failed while testing for end at " + input.getPosition().toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        return false;
    }

    //--------------------------------------------------------------------------
    //
    // Support for coding and decoding with escape characters
    //
    //--------------------------------------------------------------------------
    /**
     * Supports the encoding of a text with escape sequences as this is used
     * e.g. for XML, HTML, JSON ...
     *
     * @param clearText The text without any encoding.
     * @param encodeTable The table of encoding sequences. encodeTable[x][0]
     * contains the character sequence to replace with encodeTable[x][1].
     * @return The encoded Text.
     */
    static public String encodeWithEscapeSequence(String clearText, CharSequence[][] encodeTable) {
        assert (clearText != null);
        assert (encodeTable != null);

        String encodedText = clearText;
        for (int i = 0; i < encodeTable.length; i++) {
            if (encodeTable[i][0].length() > 0) {
                encodedText = encodedText.replace(encodeTable[i][0], encodeTable[i][1]);
            }
        }
        return (encodedText);
    }
}
