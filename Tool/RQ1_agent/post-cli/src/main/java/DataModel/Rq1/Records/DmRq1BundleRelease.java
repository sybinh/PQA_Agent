/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_Enumeration;
import DataModel.Rq1.Fields.DmRq1Field_MappedReferenceList;
import static DataModel.Rq1.Records.DmRq1ElementCache.addElement;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1BundleRelease;
import Rq1Data.Enumerations.YesNoEmpty;
import Rq1Data.Templates.Rq1TemplateI;

/**
 *
 * @author gug2wi
 */
public class DmRq1BundleRelease extends DmRq1HardwareRelease {
    
    final public DmRq1Field_Enumeration CATEGORY;
    final public DmRq1Field_Enumeration CLASSIFICATION;
    
    final public DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Pst> HAS_MAPPED_PST;
    final public DmRq1Field_MappedReferenceList<DmRq1Rrm, DmRq1Pst> HAS_MAPPED_ECU;
    

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1BundleRelease(Rq1BundleRelease release) {
        super("Bundle", release);
        
        addField(CATEGORY = new DmRq1Field_Enumeration(this, release.CATEGORY, "Category"));
        addField(CLASSIFICATION = new DmRq1Field_Enumeration(this, release.CLASSIFICATION, "Classification"));

        addField(HAS_MAPPED_PST = new DmRq1Field_MappedReferenceList<>(this, release.HAS_MAPPED_PST, "Mapped PVER/PVER Collections"));
        addField(HAS_MAPPED_ECU = new DmRq1Field_MappedReferenceList<>(this, release.HAS_MAPPED_ECU, "Mapped H-ECU"));
    }

    static DmRq1BundleRelease create() {
        Rq1BundleRelease rq1Release = new Rq1BundleRelease();
        DmRq1BundleRelease dmRelease = new DmRq1BundleRelease(rq1Release);
        addElement(rq1Release, dmRelease);
        return (dmRelease);
    }

    public static DmRq1BundleRelease createBasedOnProject(DmRq1Project project, Rq1TemplateI template) {

        assert (project != null);

        DmRq1BundleRelease release = create();

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

}
