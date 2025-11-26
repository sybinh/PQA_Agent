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
import Rq1Cache.Records.Rq1FcCollection;
import Rq1Data.Templates.Rq1TemplateI;
import util.UiWorker;

/**
 *
 * @author GUG2WI
 */
public class DmRq1FcCollection extends DmRq1Fc {

    final public DmRq1Field_Enumeration CLASSIFICATION;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1FcCollection(Rq1FcCollection rq1FcCollection) {
        super("FC-Collection", rq1FcCollection);

        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, rq1FcCollection.CLASSIFICATION, "Classification"));

    }

    public static DmRq1FcCollection create() {
        Rq1FcCollection rq1Record = new Rq1FcCollection();
        DmRq1FcCollection dmRecord = new DmRq1FcCollection(rq1Record);
        DmRq1ElementCache.addElement(rq1Record, dmRecord);
        return (dmRecord);
    }

    public static DmRq1FcCollection createBasedOnPredecessor(DmRq1Fc predecessor, final DmRq1SoftwareProject targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        final DmRq1FcCollection newFcCollection = create();

        //
        // Take over content from parent
        //
        newFcCollection.TITLE.setValue(predecessor.TITLE.getValue() + "_succ");

        if (predecessor instanceof DmRq1FcCollection) {
            newFcCollection.CLASSIFICATION.setValue(((DmRq1FcCollection) predecessor).CLASSIFICATION.getValue());
        }

        //
        // Connect with predecessor
        //
        predecessor.SUCCESSORS.addElement(newFcCollection);
        newFcCollection.PREDECESSOR.setElement(predecessor);

        //
        // Connect with Project
        //
        newFcCollection.PROJECT.setElement(targetProject);
        UiWorker.execute(new UiWorker<Void>("Add FC to project") {

            @Override
            protected Void backgroundTask() {
                // Execution in background because of https://rb-wam.bosch.com/tracker01/browse/ECVTOOL-404 - Create Successor BC lasts too long
                // targetProject.ALL_RELEASES.addElement(newBcRelease); -> not necessary
                targetProject.OPEN_FC.addElement(newFcCollection);
                return (null);
            }
        });
        if (template != null) {
            template.execute(newFcCollection);
        }
        return (newFcCollection);
    }
    
    final public void addSuccessor(DmRq1Fc newSuccessor) throws ExistsAlready {
        super.addSuccessor(newSuccessor);
    }

    @Override
    public int getPositionInRoadMap() {
        return -90;
    }
}
