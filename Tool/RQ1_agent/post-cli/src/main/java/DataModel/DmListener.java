/*
 * Copyright (c) 2009, 2017 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import util.EcvWeakList;

/**
 * Implements a abstract listener for changes on DmElementI and DmFieldI. The
 * class manages the dependencies and triggers a abstract method when one of the
 * elements was changed.
 *
 * @author gug2wi
 */
public abstract class DmListener {

    private class ElementListener implements DmElementI.ChangeListener {

        @Override
        public void changed(DmElementI changedElement) {
            dependencyChanged();
        }

    }

    private class FieldListener implements DmFieldI.ChangeListener {

        @Override
        public void changed(DmFieldI changedElement) {
            dependencyChanged();
        }

    }

    private EcvWeakList<DmElementI> elementDependencies = null;
    private ElementListener elementListener = null;
    //
    private EcvWeakList<DmFieldI> fieldDependencies = null;
    private FieldListener fieldListener = null;

    final public void addDependency(DmElementI dmElement) {
        assert (dmElement != null);

        if (elementDependencies == null) {
            elementListener = new ElementListener();
            elementDependencies = new EcvWeakList<>();
        }

        elementDependencies.add(dmElement);
        dmElement.addChangeListener(elementListener);
    }

    final public void addDependency(DmFieldI dmField) {
        assert (dmField != null);

        if (fieldDependencies == null) {
            fieldListener = new FieldListener();
            fieldDependencies = new EcvWeakList<>();
        }

        fieldDependencies.add(dmField);
        dmField.addChangeListener(fieldListener);
    }

    final public void removeAllDependencies() {
        if (elementDependencies != null) {
            for (DmElementI element : elementDependencies.getCopy()) {
                element.removeChangeListener(elementListener);
            }
            elementDependencies.clear();
        }
        if (fieldDependencies != null) {
            for (DmFieldI element : fieldDependencies.getCopy()) {
                element.removeChangeListener(fieldListener);
            }
            fieldDependencies.clear();
        }
    }

    /**
     * This method is called when one of the added dependencies was changed.
     */
    protected abstract void dependencyChanged();
}
