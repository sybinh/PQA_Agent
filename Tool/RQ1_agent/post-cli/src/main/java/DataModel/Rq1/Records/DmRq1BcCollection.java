/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1BcCollection;
import Rq1Data.Templates.Rq1TemplateI;
import util.UiWorker;

/**
 *
 * @author GUG2WI
 */
public class DmRq1BcCollection extends DmRq1Bc {

    final public DmRq1Field_Enumeration CLASSIFICATION;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1BcCollection(Rq1BcCollection rq1BcCollection) {
        super("BC-Collection", rq1BcCollection);

        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1BcCollection.CLASSIFICATION, "Classification"));
    }

    public static DmRq1BcCollection create() {
        Rq1BcCollection rq1Record = new Rq1BcCollection();
        DmRq1BcCollection dmRecord = new DmRq1BcCollection(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmRecord);
        return (dmRecord);
    }

    public static DmRq1BcCollection createBasedOnPredecessor(DmRq1Bc predecessor, final DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        final DmRq1BcCollection newBcCollection = create();

        //
        // Take over content from parent
        //
//        newBcRelease.ACCOUNT_NUMBERS.setValue(bcRelease.ACCOUNT_NUMBERS.getValueNeverNull());
        newBcCollection.TITLE.setValue(predecessor.TITLE.getValue() + "_succ");
//        newBcRelease.BASED_ON_PREDECESSOR.setValue(YesNoEmpty.YES);

        if (predecessor instanceof DmRq1BcCollection) {
            newBcCollection.CLASSIFICATION.setValue(((DmRq1BcCollection) predecessor).CLASSIFICATION.getValue());
        }

        //
        // Connect with predecessor
        //
        predecessor.SUCCESSORS.addElement(newBcCollection);
        newBcCollection.PREDECESSOR.setElement(predecessor);

        //
        // Connect with Project
        //
        newBcCollection.PROJECT.setElement(targetProject);
        UiWorker.execute(new UiWorker<Void>("Add BC to project") {

            @Override
            protected Void backgroundTask() {
                // Execution in background because of https://rb-wam.bosch.com/tracker01/browse/ECVTOOL-404 - Create Successor BC lasts too long
                // targetProject.ALL_RELEASES.addElement(newBcRelease); -> not necessary
                targetProject.OPEN_BC.addElement(newBcCollection);
                return (null);
            }
        });
        if (template != null) {
            template.execute(newBcCollection);
        }
        return (newBcCollection);
    }

    @Override
    public int getPositionInRoadMap() {
        return -90;
    }

    public final void addSuccessor(DmRq1Bc newSuccessor) throws ExistsAlready {
        super.addSuccessor(newSuccessor);
    }

}
