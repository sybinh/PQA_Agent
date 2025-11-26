/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import org.apache.http.client.methods.HttpRequestBase;

/**
 *
 * @author gug2wi
 */
public enum OslcProtocolVersion {

    NONE() {
        @Override
        public void addRequestHeader(HttpRequestBase command) {
        }
    },
    OSLC_10() {
        @Override
        public String getPropertyToken() {
            return ("oslc_cm.properties");
        }

        @Override
        public void addRequestHeader(HttpRequestBase command
        ) {
            assert (command != null);
            command.setHeader("Accept", "application/xml");
            command.setHeader("OSLC-Core-Version", "1.0");
        }
    },
    OSLC_20() {
        @Override
        public String getPropertyToken() {
            return ("oslc.properties");
        }

        @Override
        public void addRequestHeader(HttpRequestBase command) {
            assert (command != null);
            command.setHeader("Accept", "application/xml");
            command.setHeader("OSLC-Core-Version", "2.0");
        }

    },
    OSLC_20_RDF_XML() {
        @Override
        public String getPropertyToken() {
            return ("oslc.properties");
        }

        @Override
        public void addRequestHeader(HttpRequestBase command) {
            assert (command != null);
            command.setHeader("Accept", "application/rdf+xml");
            command.setHeader("OSLC-Core-Version", "2.0");
        }

    };

    abstract public void addRequestHeader(HttpRequestBase command);

    public String getPropertyToken() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
