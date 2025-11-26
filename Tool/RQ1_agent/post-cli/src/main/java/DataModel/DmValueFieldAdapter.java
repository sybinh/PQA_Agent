/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import Monitoring.Marker;
import Monitoring.MarkerI;
import Monitoring.RuleI;
import java.util.List;

/**
 * Adapts the type of a field to a value field type.
 *
 * @author GUG2WI
 */
public abstract class DmValueFieldAdapter<T_OUTSIDE, T_INSIDE extends DmFieldI> implements DmValueFieldI<T_OUTSIDE> {

    final protected T_INSIDE fieldInside;

    public DmValueFieldAdapter(T_INSIDE fieldInside) {
        assert (fieldInside != null);
        this.fieldInside = fieldInside;
    }

    @Override
    public String toString() {
        return ("DmValueFieldAdapter for " + fieldInside.toString());
    }

    //--------------------------------------------------------------------------
    //
    // Delegate methods from outside to inside
    //
    //--------------------------------------------------------------------------
    @Override
    public String getNameForUserInterface() {
        return (fieldInside.getNameForUserInterface());
    }

    @Override
    public boolean isReadOnly() {
        return (fieldInside.isReadOnly());
    }

    @Override
    public void addChangeListener(ChangeListener newListener) {
        fieldInside.addChangeListener(newListener);
    }

    @Override
    public void removeChangeListener(ChangeListener obsoletListener) {
        fieldInside.removeChangeListener(obsoletListener);
    }

    @Override
    public void addMarkerListener(MarkerListener newListener) {
        fieldInside.addMarkerListener(newListener);
    }

    @Override
    public void removeMarkerListener(MarkerListener obsoletListener) {
        fieldInside.removeMarkerListener(obsoletListener);
    }

    @Override
    public void setMarker(Marker newMark) {
        fieldInside.setMarker(newMark);
    }

    @Override
    public void addMarker(Marker newMark) {
        fieldInside.addMarker(newMark);
    }

    @Override
    public boolean removeMarkers(RuleI rule) {
        return fieldInside.removeMarkers(rule);
    }

    @Override
    public List<Marker> getMarkers() {
        return fieldInside.getMarkers();
    }

    @Override
    public <T_MARKER extends Class<? extends MarkerI>> boolean hasMarker(T_MARKER wantedMarkerClass) {
        return (fieldInside.hasMarker(wantedMarkerClass));
    }

    @Override
    public <T_MARKER extends MarkerI> List<T_MARKER> getMarkers(Class<T_MARKER> wantedMarkerClass) {
        return (fieldInside.getMarkers(wantedMarkerClass));
    }

    @Override
    public void setAttribute(Object newAttribute) {
        fieldInside.setAttribute(newAttribute);
    }

    @Override
    public void setAttribute(Object newAttribute1, Object newAttribute2) {
        fieldInside.setAttribute(newAttribute1, newAttribute2);
    }

    @Override
    public boolean hasAttribute(Object lockupAttribute) {
        return fieldInside.hasAttribute(lockupAttribute);
    }

}
