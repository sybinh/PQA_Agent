/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import java.util.List;

/**
 * An object that can be marked with objects of type {@link Marker}.
 * <p>
 * This interface supports the adding and removing of markers as well as the
 * examination for existing markers. Additionally, it is possible to add
 * listener for changes of the markers.
 *
 * @author gug2wi
 */
public interface MarkableI {

    /**
     * Listener for changes on a {@link MarkableI} object.
     */
    public interface MarkerListener {

        /**
         * Called when markers on the observed object where added or removed.
         *
         * @param changedMarkable The observed object where markers where added
         * or removed.
         */
        void markerChanged(MarkableI changedMarkable);
    }

    /**
     * Adds the given listener to the list of listeners.
     * <p>
     * The listener is triggered each time when a mark was added or removed.
     * <p>
     *
     *
     * @param newListener Object that shall be added to the listener list.
     */
    void addMarkerListener(MarkerListener newListener);

    /**
     * Removes the given listener from the list of listener.
     *
     * @param obsoletListener Object that shall be removed from the listener
     * list.
     */
    void removeMarkerListener(MarkerListener obsoletListener);

    /**
     * Sets the given marker on the {@link MarkableI} and removes all other
     * markers that belong to the same rule as the new marker.
     *
     * @param newMark Marker that shall be set.
     */
    void setMarker(Marker newMark);

    /**
     * Adds the given marker on the {@link MarkableI}.
     *
     * @param newMark Marker that shall be set.
     */
    void addMarker(Marker newMark);

    /**
     * Removes all markers that belong to the given rule.
     *
     * @param rule SwitchableRuleI for which the markers shall be removed.
     * @return True, if at least one marker was removed; false, if no marker was
     * removed.
     */
    boolean removeMarkers(RuleI rule);

    /**
     * Checks, if at least one {@link Marker} of type T_MARKER is set on the
     * object.
     *
     * @param <T_MARKER> Type of marker for which the has-criteria shall be
     * checked.
     * @param wantedMarkerClass Class of the marker for which the has-criteria
     * shall be checked.
     * @return True, if at least one {@link Marker} is set.
     */
    <T_MARKER extends Class<? extends MarkerI>> boolean hasMarker(T_MARKER wantedMarkerClass);

    /**
     * Checks, if at least one {@link Failure} is set on the object.
     *
     * @return True, if at least one {@link Failure} is set.
     */
    default boolean hasFailure() {
        return (hasMarker(Failure.class));
    }
    
    default boolean hasWriteToDatabaseFailure() {
        return (hasMarker(WriteToDatabaseFailure.class));
    }

    /**
     * Checks, if at least one {@link Warning} is set on the object.
     *
     * @return True, if at least one {@link Warning} is set.
     */
    default boolean hasWarning() {
        return (hasMarker(Warning.class));
    }

    /**
     * Checks, if at least one {@link Hint} is set on the object.
     *
     * @return True, if at least one {@link Hint} is set.
     */
    default boolean hasHint() {
        return (hasMarker(Hint.class));
    }

    /**
     * Checks, if at least one {@link ToDo} is set on the object.
     *
     * @return True, if at least one {@link ToDo} is set.
     */
    default boolean hasToDo() {
        return (hasMarker(ToDo.class));
    }

    /**
     * Checks, if at least one {@link Info} is set on the object.
     *
     * @return True, if at least one {@link Info} is set.
     */
    default boolean hasInfo() {
        return (hasMarker(Info.class));
    }

    /**
     * Checks, if at least one {@link Marker} is set on the object.
     *
     * @return True, if at least one {@link Marker} is set.
     */
    default boolean hasMarker() {
        return (hasMarker(Marker.class));
    }

    /**
     * Gets the list of markers that fit to the given marker class.
     *
     * @param <T_MARKER> Type of marker for which the markers shall be returned.
     * @param wantedMarkerClass Class of the markers for which the markers shall
     * be returned.
     * @return
     */
    <T_MARKER extends MarkerI> List<T_MARKER> getMarkers(Class<T_MARKER> wantedMarkerClass);

    /**
     * Gets the list of {@link Failure} from the object.
     *
     * @return All {@link Failure} set on the object.
     */
    default List<Failure> getFailures() {
        return (getMarkers(Failure.class));
    }
    
    default List<WriteToDatabaseFailure> getWriteToDatabaseFailure() {
        return (getMarkers(WriteToDatabaseFailure.class));
    }

    /**
     * Gets the list of {@link Warning} from the object.
     *
     * @return All {@link Warning} set on the object.
     */
    default List<Warning> getWarnings() {
        return (getMarkers(Warning.class));
    }

    /**
     * Gets the list of {@link Hint} from the object.
     *
     * @return All {@link Hint} set on the object.
     */
    default List<Hint> getHints() {
        return (getMarkers(Hint.class));
    }

    /**
     * Gets the list of {@link ToDo} from the object.
     *
     * @return All {@link ToDo} set on the object.
     */
    default List<ToDo> getToDos() {
        return (getMarkers(ToDo.class));
    }

    /**
     * Gets the list of {@link Info} from the object.
     *
     * @return All {@link Info} set on the object.
     */
    default List<Info> getInfos() {
        return (getMarkers(Info.class));
    }

    /**
     * Gets the list of {@link Marker} from the object.
     *
     * @return All {@link Marker} set on the object.
     */
    default List<Marker> getMarkers() {
        return (getMarkers(Marker.class));
    }
}
