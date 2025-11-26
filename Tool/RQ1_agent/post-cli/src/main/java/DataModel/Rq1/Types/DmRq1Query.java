/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Types;

import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1ElementCache;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import OslcAccess.OslcLoadHint;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Rq1RecordDescription;
import Rq1Cache.Rq1RecordType;
import Rq1Cache.Types.Rq1Query;
import Rq1Cache.Types.Rq1Reference;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import util.EcvDate;
import util.EcvEnumeration;

/**
 *
 * @author hfi5wi
 * @param <T_ELEMENT>
 */
public class DmRq1Query<T_ELEMENT extends DmRq1Element> {

    final private Rq1Query rq1Query;

    public DmRq1Query(Rq1RecordType recordType) {
        assert (recordType != null);
        rq1Query = new Rq1Query(recordType);
    }

    public void setLoadHint(OslcLoadHint loadHint) {
        assert (loadHint != null);
        rq1Query.setLoadHint(loadHint);
    }
    
    @SuppressWarnings("unchecked")
    public List<T_ELEMENT> getResult() {
        List<T_ELEMENT> result = new ArrayList<>();
        for (Rq1Reference reference : rq1Query.getReferenceList(true)) {
            DmRq1ElementInterface dmElement = DmRq1ElementCache.getElement(reference.getRecord());
            result.add((T_ELEMENT) dmElement);
        }
        return (result);
    }

    //--------------------------------------------------------------------------
    //
    // Forward criterias for query
    //
    //--------------------------------------------------------------------------
    public void addCriteria_ValueList(String fieldname, String[] allowedValues) {
        rq1Query.addCriteria_ValueList(fieldname, allowedValues);
    }
    
    public void addCriteria_ValueList(Rq1AttributeName attributeName, String[] wantedValues) {
        rq1Query.addCriteria_ValueList(attributeName.getName(), wantedValues);
    }
    
    public void addCriteria_ValueList(Rq1AttributeName attributeName, EnumSet<? extends EcvEnumeration> allowedValues) {
        rq1Query.addCriteria_ValueList(attributeName, allowedValues);
    }
    
    public void addCriteria_Value(Rq1AttributeName attributeName, String wantedValue) {
        rq1Query.addCriteria_Value(attributeName.getName(), wantedValue);
    }
    
    public final void addCriteria_isLaterOrEqualThen(String fieldname, EcvDate testDate) {
        rq1Query.addCriteria_isLaterOrEqualThen(fieldname, testDate);
    }

    public void addCriteria_FixedValues(Rq1RecordDescription.FixedRecordValue[] fixedRecordValues) {
        rq1Query.addCriteria_FixedValues(fixedRecordValues);
    }

}
