/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import static DataModel.Rq1.Records.DmRq1ElementCache.addElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1CompRelease;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Templates.Rq1TemplateI;
import util.UiWorker;

/**
 *
 * @author gug2wi
 */
public class DmRq1CompRelease extends DmRq1HardwareRelease {

    final public DmRq1Field_MappedReferenceList<DmRq1Rrm_Mod_Comp, DmRq1ModRelease> MAPPED_PARENT;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1CompRelease(Rq1CompRelease release) {
        super("HW-COMP", release);

        addField(MAPPED_PARENT = new DmRq1Field_MappedReferenceList<>(this, release.HAS_MAPPED_PARENTS, "Parents"));
    }

    static DmRq1CompRelease create() {
        Rq1CompRelease rq1Release = new Rq1CompRelease();
        DmRq1CompRelease dmRelease = new DmRq1CompRelease(rq1Release);
        addElement(rq1Release, dmRelease);
        return (dmRelease);
    }

    /**
     * Creates a ECU-RELEASE-HW based on a hardware project
     *
     * @param project
     * @param template
     * @return ECU-RELEASE-HW
     */
    public static DmRq1CompRelease createBasedOnProject(DmRq1Project project, Rq1TemplateI template) {

        assert (project != null);

        DmRq1CompRelease release = create();

        //
        // Set fixed values
        //
        release.BASED_ON_PREDECESSOR.setValue(YesNoEmpty.NO);

        //
        // Take over content from parent
        //
        release.ACCOUNT_NUMBERS.setValue(project.ACCOUNT_NUMBERS.getValue());

        //
        // Connect Release - Project
        //
        release.PROJECT.setElement(project);
        project.OPEN_RELEASES.addElement(release);
        if (template != null) {
            template.execute(release);
        }
        return (release);
    }

    public static DmRq1CompRelease createBasedOnPredecessor(DmRq1CompRelease predecessor, DmRq1Project targetProject, Rq1TemplateI template) {
        assert (predecessor != null);
        assert (targetProject != null);

        final DmRq1CompRelease newRelease = create();

        //
        // Take over content from predecessor
        //
        newRelease.TITLE.setValue(predecessor.TITLE.getValue() + "_succ");

        //
        // Connect with predecessor
        //
        predecessor.SUCCESSORS.addElement(newRelease);
        newRelease.PREDECESSOR.setElement(predecessor);

        //
        // Connect with Project
        //
        newRelease.PROJECT.setElement(targetProject);
        UiWorker.execute(new UiWorker<Void>("Add HW-COMP to project") {

            @Override
            protected Void backgroundTask() {
                targetProject.OPEN_HW_COMP_RELEASES.addElement(newRelease);
                return (null);
            }
        });
        if (template != null) {
            template.execute(newRelease);
        }
        return (newRelease);
    }

    public final void addSuccessor(DmRq1CompRelease newSuccessor) throws ExistsAlready {
        super.addSuccessor(newSuccessor);
    }

}
