/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Records;

import DataModel.DmElement;
import DataModel.DmElementListFieldI;
import DataModel.DmFieldI;
import DataStore.DsRecordI;
import Ipe.Annotations.IpeFactoryClass;
import Monitoring.AffectedObject;
import Monitoring.MarkableI;
import Monitoring.MarkerI;
import OslcAccess.OslcLoadHint;
import OslcAccess.Rq1.OslcRq1ServerDescription.LinkType;
import Rq1Cache.Records.Rq1AttributeName;
import Rq1Cache.Records.Rq1RecordInterface;
import java.util.ArrayList;
import java.util.List;
import util.EcvEnumeration;

/**
 * DmElement that refers to a RQ1-Record.
 *
 * @author gug2wi
 */
@IpeFactoryClass("Rq1Cache.Records.Rq1RecordInterface")
public abstract class DmRq1Element extends DmElement implements DmRq1ElementInterface, Rq1RecordInterface.ChangeListener, MarkableI.MarkerListener, AffectedObject {

    static public DmRq1ElementInterface getElementByRq1Id(String rq1Id) {
        assert (rq1Id != null);
        assert (rq1Id.isEmpty() == false);

        return (getElementById(rq1Id));
    }

    static public DmRq1ElementInterface getElementById(String id) {
        assert (id != null);
        assert (id.isEmpty() == false);

        Rq1RecordInterface rq1Record = Rq1Cache.Rq1RecordIndex.getRecordById(id);

        DmRq1ElementInterface dmElement = null;

        if (rq1Record != null) {
            dmElement = DmRq1ElementCache.getElement(rq1Record);
        }

        return (dmElement);
    }

    final protected Rq1RecordInterface rq1Record;

    @SuppressWarnings("LeakingThisInConstructor")
    protected DmRq1Element(String elementType, Rq1RecordInterface rq1Record) {
        super(elementType);
        assert (rq1Record != null);
        this.rq1Record = rq1Record;
        rq1Record.addChangeListener(this);
        rq1Record.addMarkerListener(this);
    }

    @Override
    public Rq1RecordInterface getRq1Record() {
        return (rq1Record);
    }

    @Override
    public boolean existsInDatabase() {
        return (rq1Record.existsInDatabase());
    }

    @Override
    final public void openInRq1(LinkType linkType) {
        assert (linkType != null);
        rq1Record.openInRq1(linkType);
    }

    protected void loadIntoCache(OslcLoadHint loadHint) {
        assert (loadHint != null);

        rq1Record.loadCache(loadHint);
    }

    @Override
    final public void changed(DsRecordI changedRq1Record) {
        super.fireChange();
    }

    @Override
    final public void markerChanged(MarkableI changedMarkable) {
        fireMarkerChange();
    }

    @Override
    public String getId() {
        return (rq1Record.getId());
    }

    @Override
    public <T_MARKER_CLASS extends Class<? extends MarkerI>> boolean hasMarker(T_MARKER_CLASS wantedMarkerClass) {
        return (super.hasMarker(wantedMarkerClass) || rq1Record.hasMarker(wantedMarkerClass));
    }

    @Override
    public <T_MARKER extends MarkerI> List<T_MARKER> getMarkers(Class<T_MARKER> wantedMarkerClass) {
        ArrayList<T_MARKER> fullList = new ArrayList<>();
        fullList.addAll(super.getMarkers(wantedMarkerClass));
        fullList.addAll(rq1Record.getMarkers(wantedMarkerClass));
        return (fullList);
    }

    @Override
    public void reload() {
        rq1Record.reload();
        for (DmFieldI field : getFields()) {
            if (field instanceof DmElementListFieldI) {
                ((DmElementListFieldI) field).reload();
            }
        }
    }

    @Override
    public boolean save() {
        super.save();
        return (rq1Record.save(null));
    }

    /**
     * Save the changed fields of the record to the database and use the given
     * field order in the OSLC command.
     *
     * @param fieldOrder
     * @return
     */
    public boolean save(Rq1AttributeName... fieldOrder) {
        super.save();
        return (rq1Record.save(fieldOrder));
    }

    public boolean save(List<Rq1AttributeName> fieldOrder) {
        if ((fieldOrder != null) && (fieldOrder.isEmpty() == false)) {
            return (rq1Record.save(fieldOrder.toArray(new Rq1AttributeName[fieldOrder.size()])));
        } else {
            return (rq1Record.save(null));
        }
    }

    @Override
    final public boolean isInLifeCycleState(EcvEnumeration lifeCycleState) {
        assert (isValidLifeCycleState(lifeCycleState));
        return (lifeCycleState == getLifeCycleState());
    }

    @Override
    final public boolean isInLifeCycleState(EcvEnumeration lifeCycleState1, EcvEnumeration lifeCycleState2) {
        assert (isValidLifeCycleState(lifeCycleState1));
        assert (isValidLifeCycleState(lifeCycleState2));
        EcvEnumeration current = getLifeCycleState();
        return ((lifeCycleState1 == current) || (lifeCycleState2 == current));
    }

    @Override
    final public boolean isInLifeCycleState(EcvEnumeration lifeCycleState1, EcvEnumeration lifeCycleState2, EcvEnumeration lifeCycleState3) {
        assert (isValidLifeCycleState(lifeCycleState1));
        assert (isValidLifeCycleState(lifeCycleState2));
        assert (isValidLifeCycleState(lifeCycleState3));
        EcvEnumeration current = getLifeCycleState();
        return ((lifeCycleState1 == current) || (lifeCycleState2 == current) || (lifeCycleState3 == current));
    }

    @Override
    final public boolean isInLifeCycleState(EcvEnumeration lifeCycleState1, EcvEnumeration lifeCycleState2, EcvEnumeration lifeCycleState3, EcvEnumeration lifeCycleState4) {
        assert (isValidLifeCycleState(lifeCycleState1));
        assert (isValidLifeCycleState(lifeCycleState2));
        assert (isValidLifeCycleState(lifeCycleState3));
        assert (isValidLifeCycleState(lifeCycleState4));
        EcvEnumeration current = getLifeCycleState();
        return ((lifeCycleState1 == current) || (lifeCycleState2 == current) || (lifeCycleState3 == current) || (lifeCycleState4 == current));
    }

    @Override
    final public boolean isInLifeCycleState(EcvEnumeration lifeCycleState1, EcvEnumeration lifeCycleState2, EcvEnumeration lifeCycleState3, EcvEnumeration lifeCycleState4, EcvEnumeration lifeCycleState5) {
        assert (isValidLifeCycleState(lifeCycleState1));
        assert (isValidLifeCycleState(lifeCycleState2));
        assert (isValidLifeCycleState(lifeCycleState3));
        assert (isValidLifeCycleState(lifeCycleState4));
        assert (isValidLifeCycleState(lifeCycleState5));
        EcvEnumeration current = getLifeCycleState();
        return ((lifeCycleState1 == current) || (lifeCycleState2 == current) || (lifeCycleState3 == current) || (lifeCycleState4 == current) || (lifeCycleState5 == current));
    }

    private boolean isValidLifeCycleState(EcvEnumeration lifeCycleState) {
        for (EcvEnumeration ecvEnumeration : getValidLifeCycleStates()) {
            if (lifeCycleState == ecvEnumeration) {
                return (true);
            }
        }
        return (false);
    }

    protected EcvEnumeration[] getValidLifeCycleStates() {
        return (new EcvEnumeration[0]);
    }

    final public String getFlowPanelTitle() {
        return (toString());
    }
}
