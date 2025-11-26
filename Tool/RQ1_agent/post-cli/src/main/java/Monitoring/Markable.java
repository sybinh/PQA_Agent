/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package Monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import util.EcvWeakList;

/**
 * Implements {@link MarkableI}. Intended to be used by all classes implementing
 * {@link MarkableI}. Either as sub class or via aggregation.
 *
 * @author gug2wi
 */
public class Markable implements MarkableI {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Markable.class.getCanonicalName());
    //
    private CopyOnWriteArrayList<Marker> markers = null;
    private EcvWeakList<MarkerListener> markerListeners = null;

    /**
     * Creates the object with no markers set.
     */
    public Markable() {
    }

    @Override
    final public void setMarker(Marker newMark) {
        assert (newMark != null);
        removeMarkersInternal(newMark.getRule());
        addMarkerInternal(newMark);
        fireMarkerChange();
    }

    @Override
    final public void addMarker(Marker newMark) {
        assert (newMark != null);
        addMarkerInternal(newMark);
        fireMarkerChange();
    }

    @Override
    final public boolean removeMarkers(RuleI rule) {
        if (removeMarkersInternal(rule) == true) {
            fireMarkerChange();
            return (true);
        } else {
            return (false);
        }

    }

    private void addMarkerInternal(Marker newMark) {
        assert (newMark != null);
        if (newMark instanceof Failure) {
            LOGGER.warning("Failure set");
            LOGGER.log(Level.WARNING, "Title: {0}", newMark.getTitle());
            LOGGER.log(Level.WARNING, "Description: {0}", newMark.getDescription());
        }
        if (markers == null) {
            markers = new CopyOnWriteArrayList<>();
        }
        if (LOGGER.isLoggable(Level.FINER)) {
            StringBuilder b = new StringBuilder();
            b.append("Markable: ").append(Integer.toHexString(System.identityHashCode(this))).append(" ").append(this.toString()).append("\n");
            b.append("Marker: ").append(Integer.toHexString(System.identityHashCode(newMark))).append(" ").append(newMark.toString()).append("\n");
            RuleI rule = newMark.getRule();
            b.append("Rule: ").append(Integer.toHexString(System.identityHashCode(rule))).append(" ").append(rule.toString()).append("\n");
            b.append("Already mapped Markers:");
            for (Marker marker : markers) {
                b.append("\n").append(Integer.toHexString(System.identityHashCode(marker))).append(" ").append(marker.toString());
            }
            LOGGER.finest(b.toString());
            LOGGER.log(Level.FINEST, "Stack for addMarker().", new Exception("No error. Only debug log"));
        }
        markers.add(newMark);
    }

    private boolean removeMarkersInternal(RuleI rule) {
        assert (rule != null);

        List<Marker> markerToDelete = new ArrayList<>();
        if (markers != null) {
            for (Marker marker : markers) {
                if (marker.getRule() == rule) {
                    markerToDelete.add(marker);
                }
            }
            markers.removeAll(markerToDelete);
        }
        return (markerToDelete.isEmpty() == false);
    }

    @Override
    public <T_MARKER extends Class<? extends MarkerI>> boolean hasMarker(T_MARKER wantedMarkerClass) {
        assert (wantedMarkerClass != null);
        if (markers != null) {
            for (Marker m : markers) {
                if (wantedMarkerClass.isInstance(m)) {
                    return (true);
                }
            }
        }
        return (false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T_MARKER extends MarkerI> List<T_MARKER> getMarkers(Class<T_MARKER> wantedMarkerClass) {
        List<T_MARKER> result = new ArrayList<>();
        if (markers != null) {
            for (Marker m : markers) {
                if (wantedMarkerClass.isInstance(m)) {
                    result.add((T_MARKER) m);
                }
            }
        }
        return (result);
    }

    /**
     * Adds the given listener to the list of listeners.
     * <p>
     * The listener is triggered each time when a mark was added or removed.
     * <p>
     * BasicMarkable uses a WeakReference to refer to the listeners. Therefore,
     * it is not necessary to remove a listener after the listener gets
     * obsolete.
     *
     * @param newListener Object that shall be added to the listener list.
     */
    @Override
    final public synchronized void addMarkerListener(MarkerListener newListener) {
        assert (newListener != null);
        if (markerListeners == null) {
            markerListeners = new EcvWeakList<>(1);
        }
        markerListeners.add(newListener);
    }

    @Override
    final public synchronized void removeMarkerListener(MarkerListener obsoletListener) {
        assert (obsoletListener != null);
        if (markerListeners != null) {
            markerListeners.remove(obsoletListener);
        }
    }

    private List<MarkerListener> copyMarkerListeners() {
        if (markerListeners == null) {
            return (null);
        }
        return (markerListeners.getCopy());
    }

    /**
     * Fires
     * {@link MarkableI.MarkerListener#markerChanged(Monitoring.MarkableI)} to
     * all marker listeners.
     */
    protected void fireMarkerChange() {
        List<MarkerListener> copy = copyMarkerListeners();
        if (copy != null) {
            for (MarkerListener listener : copy) {
                listener.markerChanged(this);
            }
        }
    }
}
