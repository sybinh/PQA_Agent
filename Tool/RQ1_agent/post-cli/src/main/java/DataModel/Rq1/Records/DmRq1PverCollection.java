/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1PverCollection;
import Rq1Data.Enumerations.ClassificationPstCollection;
import Rq1Data.Enumerations.Scope;
import Rq1Data.Enumerations.YesNoEmpty;

/**
 *
 * @author GUG2WI
 */
public class DmRq1PverCollection extends DmRq1Pver implements DmRq1PstCollectionI {

    final public DmRq1Field_Enumeration CLASSIFICATION;
    public static final String ELEMENTTYPE = "PVER-Collection";

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1PverCollection(Rq1PverCollection rq1Pst) {
        super(ELEMENTTYPE, rq1Pst);

        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1Pst.CLASSIFICATION, "Classification"));
    }

    @Override
    public DmRq1Field_Enumeration getClassificationField() {
        return (CLASSIFICATION);
    }

    public static DmRq1PverCollection create() {
        Rq1PverCollection rq1Record = new Rq1PverCollection();
        DmRq1PverCollection dmElement = new DmRq1PverCollection(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmElement);
        return (dmElement);
    }

    public static DmRq1PverCollection createBasedOnCustomerProject(DmRq1SwCustomerProject project) {
        assert (project != null);

        DmRq1PverCollection collection = create();

        collection.ACCOUNT_NUMBERS.setValue(project.ACCOUNT_NUMBERS.getValue());
        collection.CLASSIFICATION.setValue(ClassificationPstCollection.MISC);
        collection.SCOPE.setValue(Scope.INTERNAL);
        collection.BASED_ON_PREDECESSOR.setValue(YesNoEmpty.NO);

        //
        // Connect Collection - Project
        //
        collection.PROJECT.setElement(project);
        project.OPEN_PST_COLLECTIONS.addElement(collection);

        return (collection);
    }

    @Override
    public int getPositionInRoadMap() {
        return -95;
    }
}
