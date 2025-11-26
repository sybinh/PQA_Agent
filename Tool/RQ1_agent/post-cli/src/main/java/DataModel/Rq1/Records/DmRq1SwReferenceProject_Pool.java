/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import Ipe.Annotations.IpeFactoryConstructor;
import Rq1Cache.Records.Rq1SwReferenceProject_Pool;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GUG2WI
 */
public class DmRq1SwReferenceProject_Pool extends DmRq1SwReferenceProject implements DmRq1SwPoolProjectI {

    final public DmRq1Field_ReferenceList<DmRq1Project> POOL_PROJECT_MEMBERS;

    @IpeFactoryConstructor("DataModel.Rq1.Records.DmRq1Element")
    public DmRq1SwReferenceProject_Pool(Rq1SwReferenceProject_Pool rq1Project) {
        super("RefPoolPrj", rq1Project);

        addField(POOL_PROJECT_MEMBERS = new DmRq1Field_ReferenceList<>(this, rq1Project.HAS_POOL_PROJECT_MEMBERS, "Pool Projects"));
    }

    @Override
    public List<DmRq1Pst> getOpenPstOnMemberProjects() {
        List<DmRq1Pst> openPstMemberProject = new ArrayList<>();
        for (DmRq1Project memberProject : POOL_PROJECT_MEMBERS.getElementList()) {
            for ( DmRq1ReleaseRecord openReleaseMemberProject : memberProject.OPEN_RELEASES.getElementList()) {
                if (openReleaseMemberProject instanceof DmRq1Pst) {
                    openPstMemberProject.add((DmRq1Pst) openReleaseMemberProject);
                }
            }
        }
        return openPstMemberProject;
    }

    @Override
    public List<DmRq1Pst> getAllPstOnMemberProjects() {
        List<DmRq1Pst> allReleaseMemberProject = new ArrayList<>();
        for (DmRq1Project memberProject : POOL_PROJECT_MEMBERS.getElementList()) {
            for ( DmRq1ReleaseRecord allReleaseMemberProjectRelease : memberProject.ALL_RELEASES.getElementList()) {
                if (allReleaseMemberProjectRelease instanceof DmRq1Pst) {
                    allReleaseMemberProject.add((DmRq1Pst) allReleaseMemberProjectRelease);
                }
            }
        }
        return allReleaseMemberProject;
    }

}
