/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the parsing of input in JSON format to the object structure for
 * JSON.
 *
 * @author GUG2WI
 */
public class EcvJsonParser extends EcvParser {

    final private static Logger LOGGER = Logger.getLogger(EcvJsonParser.class.getCanonicalName());

    public EcvJsonParser(String inputString) {
        super(inputString);
    }

    public EcvJsonParser(File inputFile) throws FileNotFoundException {
        super(inputFile);
    }

    public EcvJsonParser(InputStreamReader inputReader) throws FileNotFoundException {
        super(inputReader);
    }

    public EcvJsonParser(EcvPositionAwarePushbackReader pushBackReader) {
        super(pushBackReader);
    }

    /**
     * Parse the input provided to the constructor.
     *
     * @return The parsed JSON content.
     * @throws util.EcvParser.ParseException If the input does not contain valid
     * JSON syntax.
     */
    public EcvJsonTopLevelValue parse() throws ParseException {

        try {

            skipWhitespace();
            if (checkEnd() == true) {
                return (null);
            }

            EcvJsonTopLevelValue result = parseTopLevelValue();
            skipWhitespace();
            if (checkEnd() == false) {

            }

            return (result);

        } catch (ParseException ex) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Parsing of JSON value failed", ex);
                LOGGER.warning(ex.getErrorLog());
            }

            throw (ex);
        }
    }

    private EcvJsonTopLevelValue parseTopLevelValue() throws ParseException {
        skipWhitespace();

        char nextChar = lookAhead();

        switch (nextChar) {
            case '{':
                return (parseObject());
            case '[':
                return (parseArray());
            default:
                String txt = "Expected char '{' or '[' not found at " + input.getPosition().toString();
                throw new ParseException(txt, input);
        }

    }

    private EcvJsonValue parseValue() throws ParseException {

        skipWhitespace();

        char nextChar = lookAhead();

        switch (nextChar) {
            case '{':
                return (parseObject());
            case '[':
                return (parseArray());
            case '"':
                return (new EcvJsonString(parseString()));
            default:
                if (checkString(EcvJsonBoolean.JSON_TRUE)) {
                    expectString(EcvJsonBoolean.JSON_TRUE);
                    return (new EcvJsonBoolean(true));
                } else if (checkString(EcvJsonBoolean.JSON_FALSE)) {
                    expectString(EcvJsonBoolean.JSON_FALSE);
                    return (new EcvJsonBoolean(false));
                }
                if (checkString(EcvJsonNull.JSON_NULL) == true) {
                    expectString(EcvJsonNull.JSON_NULL);
                    return (new EcvJsonNull());
                } else {
                    return (parseNumber());
                }
        }
    }

    private EcvJsonObject parseObject() throws ParseException {

        expectCharacter('{');
        skipWhitespace();

        EcvJsonObject object = new EcvJsonObject();

        if (checkAndSkipCharacter('}')) {
            return (object);
        }

        do {
            skipWhitespace();
            String name = parseString();
            skipWhitespace();
            expectCharacter(':');
            skipWhitespace();

            EcvJsonValue value = parseValue();
            object.add(new EcvJsonMember<>(name, value));
            skipWhitespace();

        } while (checkAndSkipCharacter(','));

        skipWhitespace();
        expectCharacter('}');

        return (object);
    }

    private EcvJsonArray parseArray() throws ParseException {

        expectCharacter('[');
        skipWhitespace();

        EcvJsonArray array = new EcvJsonArray();

        while (checkCharacter(']') == false) {

            EcvJsonValue value = parseValue();
            array.addValue(value);
            skipWhitespace();

            if (checkCharacter(']') == false) {
                expectCharacter(',');
                skipWhitespace();
            }
        }

        expectCharacter(']');

        return (array);
    }

    private String parseString() throws ParseException {

        expectCharacter('"');

        EcvPositionAwarePushbackReader.Position startPosition = input.getPosition();
        boolean inEscape = false;
        int c_int;
        char c;

        StringBuilder text = new StringBuilder(100);

        try {
            while ((c_int = input.read()) >= 0) {

                c = (char) c_int;
                if (inEscape == true) {
                    switch (c) {
                        case 'n':
                            text.append('\n');
                            break;
                        case 't':
                            text.append('\t');
                            break;
                        case 'b':
                            text.append('\b');
                            break;
                        case 'r':
                            text.append('\r');
                            break;
                        case 'f':
                            text.append('\f');
                            break;
                        case 'u':
                            throw new ParseException("Parsing of hex coded string not yet supported.", input);
                        default:
                            text.append(c);
                            break;
                    }
                    inEscape = false;
                } else if (c == '\\') {
                    inEscape = true;
                } else if (c == '"') {
                    return text.toString();
                } else {
                    text.append(c);
                }
            }
        } catch (IOException ex) {
            String txt = "Read failed at " + input.getPosition().toString() + " while parsing string starting at " + startPosition.toString();
            throw new ParseException(txt, input, currentParent, ex);
        }
        // EOF
        String txt = "EOF at " + input.getPosition().toString() + " while parsing string starting at " + startPosition.toString();
        throw new ParseException(txt, input, currentParent);
    }

    private EcvJsonNumber parseNumber() throws ParseException {

        String numberString = expectCharacters("-0123456789.");

        if (numberString.indexOf(".") >= 0) {
            //
            // This is a number with comma. Use double to parse it.
            //
            double d;
            try {
                d = Double.parseDouble(numberString);
            } catch (NumberFormatException ex) {
                throw (new ParseException("Unexpected parse error.", input, ex));
            }
            return (new EcvJsonNumber(d));
        }

        long i;
        try {
            i = Long.parseLong(numberString);
        } catch (NumberFormatException ex) {
            throw (new ParseException("Unexpected parse error.", input, ex));
        }

        return (new EcvJsonNumber(i));
    }

}
