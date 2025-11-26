/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Rq1Cache.Types;

/**
 * <p>
 * - Encoding and decoding of the hardware field in the ProPlaTo-Tags
 * </p>
 * <p>
 * - Facility methods to access the hardware settings for ProPlaTo.
 * </p>
 *
 * @author GUG2WI
 */
public class Rq1XmlTable_IntegrationSteps extends Rq1XmlTable {

    // Example of IntegrationStep-Tag for SoftwareRelease:
    //
    //  <IntegrationSteps>
    //      <IntegrationStep id="IntegrationStep0" name="21A0" comment=""/>
    //      <IntegrationStep id="IntegrationStep1" name="21B0" comment=""/>
    //      <IntegrationStep id="IntegrationStep3" name="21C0" comment="Final"/>
    //  </IntegrationSteps>
    //
    final public Rq1XmlTableColumn_String FIELDNAME;
    final public Rq1XmlTableColumn_String NAME;
    final public Rq1XmlTableColumn_String COMMENT;

    public Rq1XmlTable_IntegrationSteps() {
        addXmlColumn(FIELDNAME = new Rq1XmlTableColumn_String("id", 15, "id", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(NAME = new Rq1XmlTableColumn_String("name", 10, "name", ColumnEncodingMethod.ATTRIBUTE));
        addXmlColumn(COMMENT = new Rq1XmlTableColumn_String("comment", 10, "comment", ColumnEncodingMethod.ATTRIBUTE));
    }
}
