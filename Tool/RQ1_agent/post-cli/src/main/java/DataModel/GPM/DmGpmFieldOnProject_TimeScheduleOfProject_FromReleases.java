/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.Rq1.Fields.DmRq1Field_ReferenceList;
import DataModel.Rq1.Records.DmRq1EcuRelease;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1PvarRelease;
import DataModel.Rq1.Records.DmRq1PverRelease;
import DataModel.Rq1.Records.DmRq1Release;
import DataModel.Rq1.Records.DmRq1ReleaseRecord;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author GUG2WI
 */
class DmGpmFieldOnProject_TimeScheduleOfProject_FromReleases extends DmGpmFieldOnProject_TimeScheduleOfProject_SubList<DmGpmMilestone_FromRelease, DmRq1Field_ReferenceList<DmRq1ReleaseRecord>> {

    final private static Map<DmRq1Release, DmGpmMilestone_FromRelease> pstToMilestoneCache = new IdentityHashMap<>();

    DmGpmFieldOnProject_TimeScheduleOfProject_FromReleases(DmGpmFieldOnProject_TimeScheduleOfProject timeScheduleField, DmRq1Project project) {
        super(timeScheduleField, project, project.ALL_RELEASES, DependencyType.FIELD);
    }

    @Override
    protected List<DmGpmMilestone_FromRelease> loadElementList() {

        List<DmGpmMilestone_FromRelease> newContent = new ArrayList<>();
        for (DmRq1ReleaseRecord release : sourceField.getElementList()) {
            if (release.isCanceled() == false) {
                DmGpmMilestone_FromRelease milestone = pstToMilestoneCache.get(release);
                if (milestone == null) {
                    if (release instanceof DmRq1PverRelease) {
                        milestone = new DmGpmMilestone_FromPverRelease((DmRq1PverRelease) release);
                    } else if (release instanceof DmRq1PvarRelease) {
                        milestone = new DmGpmMilestone_FromPvarRelease((DmRq1PvarRelease) release);
                    } else if (release instanceof DmRq1EcuRelease) {
                        milestone = new DmGpmMilestone_FromEcuRelease((DmRq1EcuRelease) release);
                    }
                    if (milestone != null) {
                        pstToMilestoneCache.put((DmRq1Release) release, milestone);
                    }
                }
                if (milestone != null) {
                    newContent.add(milestone);
                }
            }
        }
        return (newContent);
    }

    @Override
    protected void reloadSourceField() {
        sourceField.reload();
    }

}
