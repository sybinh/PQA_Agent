/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataStore.DsFieldI;
import DataStore.DsRecordI;
import Monitoring.MarkableI;
import Monitoring.MarkableI.MarkerListener;
import Monitoring.MarkerI;
import java.util.ArrayList;
import java.util.List;

/**
 * A field in the data model that is directly connected to a field in the data
 * store.
 *
 * @author gug2wi
 * @param <T_DS_CONTENT> Type of the content hold by the data source field.
 */
public abstract class DmToDsField<T_DS_CONTENT> extends DmField implements MarkerListener {

    final protected DsFieldI<? extends DsRecordI<?>, T_DS_CONTENT> dsField;

    public DmToDsField(DsFieldI<? extends DsRecordI<?>, T_DS_CONTENT> dsField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (dsField != null);
        this.dsField = dsField;
        dsField.addMarkerListener(this);
    }

    @Override
    public boolean isReadOnly() {
        return (dsField.isReadOnly());
    }

    @Override
    public <T_MARKER_CLASS extends Class<? extends MarkerI>> boolean hasMarker(T_MARKER_CLASS wantedMarkerClass) {
        return (super.hasMarker(wantedMarkerClass) || dsField.hasMarker(wantedMarkerClass));
    }

    @Override
    public <T_MARKER extends MarkerI> List<T_MARKER> getMarkers(Class<T_MARKER> wantedMarkerClass) {
        ArrayList<T_MARKER> fullList = new ArrayList<>();
        fullList.addAll(super.getMarkers(wantedMarkerClass));
        fullList.addAll(dsField.getMarkers(wantedMarkerClass));
        return (fullList);
    }

    @Override
    public void markerChanged(MarkableI changedMarkable) {
        fireMarkerChange();
    }
    
}
