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
 * @param <T_RECORD> Type of the record referenced by the field.
 */
public class OslcDirectReferenceField<T_RECORD extends OslcRecordI> extends OslcField implements OslcDirectReferenceFieldI<T_RECORD> {

    final private OslcRecordTypeI referencedRecordType;

    public OslcDirectReferenceField(String oslcName, OslcRecordTypeI referencedRecordType) {
        super(oslcName);

        this.referencedRecordType = referencedRecordType;
    }

    @Override
    public OslcRecordTypeI getReferencedRecordType() {
        return (referencedRecordType);
    }

    @Override
    public boolean setOslcValue(OslcRecordReference<T_RECORD> recordReference) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isOslcWritePending() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOslcWriteSuccessfull() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean copyOslcValue(OslcDirectReferenceFieldI<T_RECORD> field) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
