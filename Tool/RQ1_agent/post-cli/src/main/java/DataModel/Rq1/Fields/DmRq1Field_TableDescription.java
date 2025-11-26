/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Rq1.Fields;

import DataModel.DmField;
import DataModel.DmFieldI;
import DataModel.UiSupport.DmUiTableSource;
import Monitoring.Marker;
import Monitoring.MarkerI;
import Monitoring.RuleI;
import java.util.List;
import util.EcvTableDescription;

/**
 *
 * @author GUG2WI
 */
public abstract class DmRq1Field_TableDescription extends EcvTableDescription implements DmUiTableSource, DmFieldI {

    final private DmField dmField;

    protected DmRq1Field_TableDescription(String nameForUserInterface) {
        assert (nameForUserInterface != null);
        assert (nameForUserInterface.isEmpty() == false);

        dmField = new DmField(nameForUserInterface) {
            @Override
            public boolean isReadOnly() {
                return (DmRq1Field_TableDescription.this.isReadOnly());
            }
        };
    }

    @Override
    final public DmFieldI getDmField() {
        return (this);
    }

    @Override
    final public EcvTableDescription getTableDescription() {
        return (this);
    }

    @Override
    final public void addChangeListener(ChangeListener newListener) {
        dmField.addChangeListener(newListener);
    }

    @Override
    final public void removeChangeListener(ChangeListener obsoletListener) {
        dmField.removeChangeListener(obsoletListener);
    }

    @Override
    final public String getNameForUserInterface() {
        return dmField.getNameForUserInterface();
    }

    @Override
    final public void setAttribute(Object newAttribute) {
        dmField.setAttribute(newAttribute);
    }

    @Override
    final public void setAttribute(Object newAttribute1, Object newAttribute2) {
        dmField.setAttribute(newAttribute1, newAttribute2);
    }

    @Override
    final public boolean hasAttribute(Object lockupAttribute) {
        return dmField.hasAttribute(lockupAttribute);
    }

    @Override
    final public void addMarkerListener(MarkerListener newListener) {
        dmField.addMarkerListener(newListener);
    }

    @Override
    final public void removeMarkerListener(MarkerListener obsoletListener) {
        dmField.removeMarkerListener(obsoletListener);
    }

    @Override
    final public void setMarker(Marker newMark) {
        dmField.setMarker(newMark);
    }

    @Override
    final public void addMarker(Marker newMark) {
        dmField.addMarker(newMark);
    }

    @Override
    final public boolean removeMarkers(RuleI rule) {
        return dmField.removeMarkers(rule);
    }

    @Override
    final public <T_MARKER extends Class<? extends MarkerI>> boolean hasMarker(T_MARKER wantedMarkerClass) {
        return dmField.hasMarker(wantedMarkerClass);
    }

    @Override
    final public <T_MARKER extends MarkerI> List<T_MARKER> getMarkers(Class<T_MARKER> wantedMarkerClass) {
        return dmField.getMarkers(wantedMarkerClass);
    }

}
