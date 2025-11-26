/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import Monitoring.MarkableI;
import Monitoring.MarkableI.MarkerListener;
import Monitoring.MarkerI;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a class that combines the content of two or more list field for
 * mapped elements.
 *
 * @author GUG2WI
 * @param <T_MAP>
 * @param <T_TARGET>
 */
public class DmMappedElementListField_Combiner<T_MAP extends DmElementI, T_TARGET extends DmElementI> extends DmField implements DmFieldI.ChangeListener, MarkerListener, DmMappedElementListFieldI<T_MAP, T_TARGET> {

    final private List<DmMappedElementListFieldI<T_MAP, T_TARGET>> fields;

    @SuppressWarnings("LeakingThisInConstructor")
    public DmMappedElementListField_Combiner(String nameForUserInterface, DmMappedElementListFieldI<T_MAP, T_TARGET> field1, DmMappedElementListFieldI<T_MAP, T_TARGET> field2) {
        super(nameForUserInterface);

        assert (field1 != null);
        assert (field2 != null);

        fields = new ArrayList<>(2);
        fields.add(field1);
        fields.add(field2);

        field1.addChangeListener(this);
        field2.addChangeListener(this);

        field1.addMarkerListener(this);
        field2.addMarkerListener(this);
    }

    @Override
    public List<DmMappedElement<T_MAP, T_TARGET>> getElementList() {
        List<DmMappedElement<T_MAP, T_TARGET>> result = new ArrayList<>();
        fields.forEach((field) -> {
            result.addAll(field.getElementList());
        });
        return (result);
    }

    @Override
    /**
     * Adds the new element to the first field provided to the constructor.
     */
    public void addElement(DmMappedElement<T_MAP, T_TARGET> newElement) {
        fields.get(0).addElement(newElement);
    }

    @Override
    public void reload() {
        fields.forEach((field) -> {
            field.reload();
        });
    }

    @Override
    public boolean isReadOnly() {
        for (DmMappedElementListFieldI<T_MAP, T_TARGET> field : fields) {
            if (field.isReadOnly() == true) {
                return (true);
            }
        }
        return (false);
    }

    @Override
    public boolean isLoaded() {
        for (DmMappedElementListFieldI<T_MAP, T_TARGET> field : fields) {
            if (field.isReadOnly() == false) {
                return (false);
            }
        }
        return (true);
    }

    //---------------------------------------------------------------------
    //
    // Propagate changes
    //
    //---------------------------------------------------------------------
    @Override
    public void changed(DmFieldI changedElement) {
        fireFieldChanged();
    }

    //---------------------------------------------------------------------
    //
    // Combine markers
    //
    //---------------------------------------------------------------------
    @Override
    public void markerChanged(MarkableI changedMarkable) {
        super.fireMarkerChange();
    }

    @Override
    public <T_MARKER_CLASS extends Class<? extends MarkerI>> boolean hasMarker(T_MARKER_CLASS wantedMarkerClass) {
        if (super.hasMarker(wantedMarkerClass) == true) {
            return (true);
        }
        for (DmMappedElementListFieldI<T_MAP, T_TARGET> field : fields) {
            if (field.hasMarker(wantedMarkerClass) == true) {
                return (true);
            }
        }
        return (false);
    }

    @Override
    public <T_MARKER extends MarkerI> List<T_MARKER> getMarkers(Class<T_MARKER> wantedMarkerClass) {
        List<T_MARKER> result = super.getMarkers(wantedMarkerClass);
        fields.forEach((field) -> {
            result.addAll(field.getMarkers(wantedMarkerClass));
        });
        return (result);
    }

}
