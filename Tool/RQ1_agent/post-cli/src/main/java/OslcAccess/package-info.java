/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */

/**
 * <h1>OSLC client interface for ECVTOOL</h1>
 *
 * This package implements the OSLC client interface used by the other packages
 * of ECVTOOL.
 * <p>
 * The current implementation is tailed to the OSCL interface provided by the
 * RQ1 OSLC server. Some adaptations might be necessary to support the access to
 * other OSLC servers, e.g. to SDOM.
 *
 * <h1>Main classes of the package</h1>
 *
 * {@link OslcAccess.OslcClient} provides the interface to the OSLC client for
 * other packages in the ECVTOOL. All calls into the package shall be done via
 * this class.
 * <p>
 * {@link OslcAccess.OslcFieldI} Root of the class hierarchy for fields and their
 * values on the OSLC interface.
 * <p>
 * {@link OslcAccess.OslcRecordI} Root of the class hierarchy for records on the
 * OSLC interface.
 *
 *
 *
 * <h1>Certificates for RQ1 access</h1>
 * The access to the RQ1 OSLC server is secured by BOSCH specific certificates.
 * These certificates have to exist in the Java certificate store to establish
 * the connection to the RQ1 OSLC server.
 * <p>
 * The standard installation of the JRE within BOSCH provided by PEACY contains
 * this BOSCH certificates. JDK's, which are installed for the development
 * manually, do not contain this certificates. They have to be added to the JDK
 * before a connection to RQ1 is possible out of the development environment.
 * Use the Java standard tools to add the needed certificates to the JDK.
 *
 *
 */
package OslcAccess;
