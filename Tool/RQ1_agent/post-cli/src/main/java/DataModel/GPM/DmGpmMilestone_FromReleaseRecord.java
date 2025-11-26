/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.GPM;

import DataModel.DmElementI;
import DataModel.DmElementListField_FromSource;
import DataModel.DmValueField_Date;
import DataModel.DmValueField_Text;
import DataModel.GPM.ConfigData.DmGpmConfig_Task;
import DataModel.Rq1.Records.DmRq1Project;
import DataModel.Rq1.Records.DmRq1ReleaseRecord;
import DataModel.Rq1.Records.DmRq1User;
import DataModel.Rq1.Records.DmRq1WorkItem;
import Monitoring.Failure;
import Monitoring.Hint;
import Monitoring.Info;
import Monitoring.MarkableI;
import Monitoring.Marker;
import Monitoring.ToDo;
import Monitoring.Warning;
import Monitoring.WriteToDatabaseFailure;
import java.util.Collection;
import java.util.List;
import util.EcvDate;

/**
 *
 * @author GUG2WI
 */
public abstract class DmGpmMilestone_FromReleaseRecord<DM_RELEASE extends DmRq1ReleaseRecord> extends DmGpmMilestone implements DmElementI.ChangeListener, MarkableI.MarkerListener {

    final protected DM_RELEASE release;

    public DmGpmMilestone_FromReleaseRecord(DM_RELEASE release, Type type, String elementType, DmRq1Project dmRq1Project) {
        super(type, elementType, dmRq1Project);
        assert (release != null);
        this.release = release;

        addField(NAME = new DmValueField_Text("Name", release.TITLE.getValue()) {
            @Override
            public List<WriteToDatabaseFailure> getWriteToDatabaseFailure() {
                return release.TITLE.getWriteToDatabaseFailure();
            }

            @Override
            public boolean hasWriteToDatabaseFailure() {
                return release.TITLE.hasWriteToDatabaseFailure();
            }

            @Override
            public boolean hasFailure() {
                return release.TITLE.hasFailure();
            }

            @Override
            public List<Failure> getFailures() {
                return release.TITLE.getFailures();
            }

        });
        addField(DATE = new DmValueField_Date("Date", release.PLANNED_DATE.getDate()) {
            @Override
            public List<WriteToDatabaseFailure> getWriteToDatabaseFailure() {
                return release.PLANNED_DATE.getWriteToDatabaseFailure();
            }

            @Override
            public boolean hasWriteToDatabaseFailure() {
                return release.PLANNED_DATE.hasWriteToDatabaseFailure();
            }

            @Override
            public boolean hasFailure() {
                return release.PLANNED_DATE.hasFailure();
            }

            @Override
            public List<Failure> getFailures() {
                return release.PLANNED_DATE.getFailures();
            }

        });
        addField(ALL_WORKITEMS = new DmElementListField_FromSource<DmRq1WorkItem>("All Workitems") {
            @Override
            protected Collection<DmRq1WorkItem> loadElementList() {
                addDependency(release.WORKITEMS);
                return (release.WORKITEMS.getElementList());
            }

            @Override
            public void addElement(DmRq1WorkItem element) {
                release.WORKITEMS.addElementUnique(element);
            }
        });

        release.addChangeListener(this);
        release.addMarkerListener(this);
    }

    @Override
    public String getId() {
        return (release.getId());
    }

    public abstract DmRq1WorkItem createAndSaveWorkitem(DmGpmConfig_Task configTableRow, String title, EcvDate plannedDate, String effort, DmRq1User assignee);

    //--------------------------------------------------------------------------
    //
    // Monitoring.MarkableI
    //
    //--------------------------------------------------------------------------
    @Override
    public void markerChanged(MarkableI changedMarkable) {
        if (release != null) {
            fireMarkerChange();
        }
    }

    @Override
    public List<Marker> getMarkers() {
        return release.getMarkers();
    }

    @Override
    public List<Info> getInfos() {
        return release.getInfos();
    }

    @Override
    public List<ToDo> getToDos() {
        return release.getToDos();
    }

    @Override
    public List<Hint> getHints() {
        return release.getHints();
    }

    @Override
    public List<Warning> getWarnings() {
        return release.getWarnings();
    }

    @Override
    public List<WriteToDatabaseFailure> getWriteToDatabaseFailure() {
        return release.getWriteToDatabaseFailure();
    }

    @Override
    public List<Failure> getFailures() {
        return release.getFailures();
    }

    @Override
    public boolean hasMarker() {
        return release.hasMarker();
    }

    @Override
    public boolean hasInfo() {
        return release.hasInfo();
    }

    @Override
    public boolean hasToDo() {
        return release.hasToDo();
    }

    @Override
    public boolean hasHint() {
        return release.hasHint();
    }

    @Override
    public boolean hasWarning() {
        return release.hasWarning();
    }

    @Override
    public boolean hasWriteToDatabaseFailure() {
        return release.hasWriteToDatabaseFailure();
    }

    @Override
    public boolean hasFailure() {
        return release.hasFailure();
    }

}
