/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

/**
 *
 * @author gug2wi
 */
public class OslcIndirectReferenceListField extends OslcDirectReferenceListField implements OslcIndirectReferenceListFieldI {

    final String indirectOslcPropertyName;
    final OslcRecordTypeI indirectReferencedRecordType;

    public OslcIndirectReferenceListField(
            String oslcPropertyName, OslcRecordTypeI mapRecordType,
            String indirectOslcPropertyName, OslcRecordTypeI indirectReferencedRecordType) {

        super(oslcPropertyName, mapRecordType);

        assert (indirectOslcPropertyName != null);
        assert (indirectOslcPropertyName.isEmpty() == false);
        assert (indirectReferencedRecordType != null);

        this.indirectOslcPropertyName = indirectOslcPropertyName;
        this.indirectReferencedRecordType = indirectReferencedRecordType;
    }

    @Override
    public String getIndirectOslcPropertyName() {
        return (indirectOslcPropertyName);
    }

    @Override
    public OslcRecordTypeI getIndirectReferencedRecordType() {
        return (indirectReferencedRecordType);
    }

    @Override
    public boolean copyOslcValue(OslcIndirectReferenceListFieldI field) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
