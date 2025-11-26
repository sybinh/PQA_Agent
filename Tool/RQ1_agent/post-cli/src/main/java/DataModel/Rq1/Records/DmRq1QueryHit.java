/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmFieldI;
import DataModel.DmQueryHitI;
import DataModel.Rq1.Fields.DmRq1Field_Date;
import DataModel.Rq1.Fields.DmRq1Field_Text;
import Ipe.Annotations.IpeFactoryConstructor;
import OslcAccess.OslcRecordTypeI;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Date;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Fields.Rq1FieldI;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1QueryHit;

/**
 *
 * @author gug2wi
 */
public class DmRq1QueryHit extends DmRq1Element implements DmQueryHitI<DmRq1Element>, DmRq1SubjectInterface {

    final private Rq1QueryHit hit;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1QueryHit(Rq1QueryHit hit) {
        super("RQ1-QUERY-HIT", hit);
        this.hit = hit;

        for (Rq1FieldI rq1Field : hit.getFields()) {
            if (rq1Field instanceof Rq1FieldI_Text) {
                DmRq1Field_Text dmField = new DmRq1Field_Text(this, (Rq1FieldI_Text) rq1Field, rq1Field.getFieldName());
                addField(dmField);
            } else if (rq1Field instanceof Rq1FieldI_Date) {
                DmRq1Field_Date dmField = new DmRq1Field_Date(this, (Rq1FieldI_Date) rq1Field, rq1Field.getFieldName());
                addField(dmField);
            }
        }

    }

    @Override
    public String getTitle() {
        return (hit.getTitle());
    }

    @Override
    public String toString() {
        return (hit.getTitle());
    }

    @Override
    public OslcRecordTypeI getReferencedRecordType() {
        return (hit.getReferencedRecordType());
    }

    @Override
    public DmRq1Element getReferencedElement() {
        return ((DmRq1Element) DmRq1ElementCache.getElement(hit.getReferencedRecord()));
    }

    @Override
    public String getId() {
        return (hit.getReferencedId());
    }

    @Override
    public String getRq1Id() {
        return ("QueryHit");
    }

    @Override
    public boolean existsInDatabase() {
        return (true);
    }

    /**
     * Returns the text field with the given name from the referenced record.
     *
     * @param name Name of the wanted field.
     * @return The field from the record or null, if the field does not exist or
     * is not of type text.
     */
    public DmRq1Field_Text getTextField(Rq1AttributeName name) {
        assert (name != null);
        DmFieldI field = getFieldByName(name.getName());
        if (field instanceof DmRq1Field_Text) {
            return ((DmRq1Field_Text) field);
        } else {
            return (null);
        }
    }

    /**
     * Returns the enumeration field with the given name from the referenced
     * record.
     *
     * @param name Name of the wanted field.
     * @return The field from the record or null, if the field does not exist or
     * is not of type enumeration.
     */
    public DmRq1Field_Date getDateField(Rq1AttributeName name) {
        assert (name != null);
        DmFieldI field = getFieldByName(name.getName());
        if (field instanceof DmRq1Field_Date) {
            return ((DmRq1Field_Date) field);
        } else {
            return (null);
        }
    }

}
