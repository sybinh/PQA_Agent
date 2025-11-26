/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmElementI;
import Rq1Cache.Fields.Interfaces.Rq1FieldI_Text;
import Rq1Cache.Records.Rq1MetadataChoiceList;
import UiSupport.UiThreadChecker;
import java.util.SortedSet;
import java.util.TreeSet;
import util.EcvEnumeration;
import util.EcvEnumerationInstance;

/**
 * Implements an enumeration field whose valid values are read from the RQ1
 * metadata.
 *
 * @author gug2wi
 */
public class DmRq1Field_MetadataChoiceList extends DmRq1Field_TextAsEnumeration {

    final private String metaDataName;
    final private String criteriaString;
    private EcvEnumeration[] validInputValues = null;

    public DmRq1Field_MetadataChoiceList(DmElementI parent, Rq1FieldI_Text rq1Field, String nameForUserInterface, String metaDataName, String criteriaString) {
        super(parent, rq1Field, nameForUserInterface);
        assert (metaDataName != null);
        assert (metaDataName.isEmpty() == false);
        assert (criteriaString != null);

        this.metaDataName = metaDataName;
        this.criteriaString = criteriaString;
    }

    @Override
    public synchronized EcvEnumeration[] getValidInputValues() {
        UiThreadChecker.ensureBackgroundThread();
        if (validInputValues == null) {

            SortedSet<EcvEnumeration> result = new TreeSet<>();

            //
            // Get current field value
            //
            EcvEnumeration currentValue = getValue();

            //
            // Add all values from meta data
            //
            int orderNo = 1;
            for (String validValue : Rq1MetadataChoiceList.getValidInputValues(metaDataName, criteriaString)) {
                result.add(new EcvEnumerationInstance(validValue, orderNo));
                orderNo++;
                //
                // Check if current value matches the allowed value
                //
                if ((currentValue != null) && (currentValue.getText().equals(validValue)) == true) {
                    currentValue = null;
                }
            }

            //
            // Add current value, if it did not match any of the valid Values
            //
            if (currentValue != null) {
                result.add(currentValue);
            }

            validInputValues = result.toArray(new EcvEnumeration[0]);
        }

        return (validInputValues);
    }

    @Override
    public EcvEnumeration[] getValidInputValues_NonBlocking() {
        return (validInputValues);
    }

}
