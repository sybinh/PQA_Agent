/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package util;

import ToolUsageLogger.ToolUsageLogger;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.logging.Level;

/**
 * Reduced PushBackReader that knows the current position (line and column) in
 * the input stream.
 *
 * @author gug2wi
 */
public class EcvPositionAwarePushbackReader extends PushbackReader {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(EcvPositionAwarePushbackReader.class.getCanonicalName());
    private static final int BUFFER_SIZE = 10;
    final private StringBuilder readData;

    /**
     * Describes a position in the input stream.
     */
    static public class Position {

        private int line;
        private int column;

        private Position() {
            this.line = -1;
            this.column = -1;
        }

        private Position(int line, int column) {
            this.line = line;
            this.column = column;
        }

        /**
         * Added for performance reasons. The creation of a new object for each
         * file position leads to huge overhead in memory, because gc can not
         * collect them as they are created. Test showed up to 10% memory
         * consumption with about 250.000 objects of this class.
         *
         * @param line
         * @param column
         */
        private void set(int line, int column) {
            this.line = line;
            this.column = column;
        }

        @Override
        public String toString() {
            return ("line:" + Integer.toString(line) + ",column:" + Integer.toString(column));
        }
    }

    static private class PositionBuffer {

        final private Position[] array;
        private int index;

        private PositionBuffer() {
            array = new Position[20];
            for (int i = 0; i < 20; i++) {
                array[i] = new Position();
            }
            index = -1;
        }

        void push(int line, int column) {
            if (index < 19) {
                index++;
            } else {
                index = 0;
            }
            array[index].set(line, column);
        }

        Position pop() {
            if (index > 0) {
                return (array[index--]);
            } else {
                index = 19;
                return (array[0]);
            }
        }
    }

    final private PositionBuffer buffer = new PositionBuffer();
    private int currentLine = 0;
    private int currentColumn = 0;

    public EcvPositionAwarePushbackReader(Reader inputReader) {
        super(inputReader, BUFFER_SIZE);
        currentLine = 0;
        currentColumn = 0;
        readData = new StringBuilder(200);
    }

    @Override
    public int read() throws IOException {

        int c_int = super.read();

        if (c_int >= 0) {
            // Handle stream begin (start of first line)
            if (currentLine == 0) {
                currentLine = 1;
            }
            buffer.push(currentLine, currentColumn);

            switch ((char) c_int) {
                case '\n':
                    currentLine++;
                    currentColumn = 0;
                    break;
                default:
                    currentColumn++;
                    break;
            }
            try {
                readData.append((char) c_int);
            } catch (java.lang.OutOfMemoryError ex) {
                logger.log(Level.SEVERE, "append failed.", ex);
                logger.severe("readData.length()=" + readData.length());
                ToolUsageLogger.logError(EcvPositionAwarePushbackReader.class.getCanonicalName(), ex);
                throw (ex);
            }
        }

        return (c_int);
    }

    @Override
    public void unread(int c) throws IOException {
        Position p = buffer.pop();
        currentLine = p.line;
        currentColumn = p.column;
        readData.setLength(readData.length() - 1);
        super.unread(c);
    }

    public Position getPosition() {
        return (new Position(currentLine, currentColumn));
    }

    public String getReadData() {
        return (readData.toString());
    }

    public String getNextData(int i) throws IOException {
        assert (i > 0);
        assert (i <= 1000);

        StringBuilder b = new StringBuilder(i);

        int c_int;

        while ((b.length() < i) && ((c_int = super.read()) >= 0)) {
            b.append((char) c_int);
        }
        return (b.toString());
    }
}
