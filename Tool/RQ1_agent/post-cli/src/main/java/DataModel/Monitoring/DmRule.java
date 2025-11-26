/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Monitoring;

import DataModel.DmElementI;
import DataModel.DmFieldI;
import DataModel.DmListener;
import Monitoring.SwitchableRule;
import Monitoring.Marker;
import Monitoring.RuleDescription;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvWeakList;

/**
 * A rule assigned to an DmElementI.
 *
 * The class manages the dependencies of the rule and the warnings created by
 * the rule. The rule is executed when one of the dependencies changes. Before
 * executing the rule, all warnings and dependencies are removed.
 *
 * @author GUG2WI
 * @param <T_ELEMENT> The type of element to which the rule is assigned.
 */
public abstract class DmRule<T_ELEMENT extends DmElementI> extends SwitchableRule {

    final protected T_ELEMENT dmElement;

    private DmListener listener = null;
    private EcvWeakList<DmElementI> markedElements = null;
    private EcvWeakList<DmFieldI> markedFields = null;

    protected DmRule(RuleDescription description, T_ELEMENT assignedDmElement) {
        super(description);
        assert (assignedDmElement != null);

        this.dmElement = assignedDmElement;
    }

    private synchronized void reset() {
        if (listener != null) {
            listener.removeAllDependencies();
        }

        //
        // The markers from fields are removed before the marker from elements are removed.
        // This order is important, because usually listeners are only set on elements. Not on fields.
        // Therefore, changes on fields are only recognized by listening objects when the markers on elements are changed.
        //
        if (markedFields != null) {
            for (DmFieldI e : markedFields.getCopy()) {
                e.removeMarkers(this);
            }
            markedFields.clear();
        }
        if (markedElements != null) {
            for (DmElementI e : markedElements.getCopy()) {
                e.removeMarkers(this);
            }
            markedElements.clear();
        }
    }

    @Override
    final protected void activateRule() {
        reset();
        getListener().addDependency(dmElement);
        executeRule();
    }

    @Override
    final protected void deactivateRule() {
        reset();
    }

    private DmListener getListener() {
        if (listener == null) {
            listener = new DmListener() {
                @Override
                protected void dependencyChanged() {
                    reset();
                    getListener().addDependency(dmElement);
                    executeRule();
                }
            };
        }
        return (listener);
    }

    final protected void addDependency(DmElementI dmElement) {
        assert (dmElement != null);
        if (dmElement == this.dmElement) {
            Error e = new Error("Superflous adding of assigned element.");
            Logger.getLogger(this.getClass().getCanonicalName()).log(Level.WARNING, "Superflous adding.", e);
        }
        getListener().addDependency(dmElement);
    }

    final protected void addDependency(DmFieldI dmField) {
        assert (dmField != null);
        getListener().addDependency(dmField);
    }

    final protected synchronized void addMarker(DmElementI elementToMark, Marker newMarker) {
        assert (elementToMark != null);
        assert (newMarker != null);

        elementToMark.addMarker(newMarker);

        if (markedElements == null) {
            markedElements = new EcvWeakList<>();
        }
        markedElements.add(elementToMark);
    }

    final protected synchronized void addMarker(DmFieldI fieldToMark, Marker newMarker) {
        assert (fieldToMark != null);
        assert (newMarker != null);

        fieldToMark.addMarker(newMarker);

        if (markedFields == null) {
            markedFields = new EcvWeakList<>();
        }
        markedFields.add(fieldToMark);
    }

    protected abstract void executeRule();
}
