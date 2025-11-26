/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import Monitoring.RuleManager;
import java.util.*;
import util.EcvListenerManager;

/**
 * Implements the common functions of all elements in the data model.
 *
 * @author gug2wi
 */
public abstract class DmElement extends RuleManager implements DmElementI {

    static protected class EditorManager extends EcvListenerManager<DmElementI> {

        public EditorManager(DmElementI managedElement) {
            super(managedElement);
        }

        final public void fireEditStateChange() {

            //
            // Work with a copy of the listener list, because the listener might change the listener list.
            //
            for (ChangeListener<DmElementI> listener : copyCurrentList()) {
                if (listener instanceof DmElementI.Editor) {
                    ((DmElementI.Editor) listener).editStateChanged(managedElement);
                }
            }

        }

        public EditState getEditState() {

            EditState state = EditState.UNCHANGED;
            //
            // Work with a copy of the listener list, because the listener might change the listener list.
            //
            for (ChangeListener<DmElementI> listener : copyCurrentList()) {
                if (listener instanceof Editor) {
                    if (((Editor) listener).getEditState(managedElement) == EditState.CHANGED) {
                        state = EditState.CHANGED;
                    }
                }
            }
            return (state);
        }

    }
    //
    private EditorManager listenerManager;

    //
    // Record and field descriptions and field objects
    //
    final private String elementType;
    final private List<DmFieldI> fieldList;

    /**
     * Creates a new elements and assigns the type.
     *
     * @param elementType Element type that is shown on the GUI.
     */
    protected DmElement(String elementType) {
        assert (elementType != null);
        assert (elementType.isEmpty() == false);

        this.elementType = elementType;
        fieldList = new ArrayList<>();
        listenerManager = null; // new EditorManager(this);
    }

    //
    // Add fields
    //
    final protected <T_FIELD extends DmFieldI> T_FIELD addField(T_FIELD newField) {
        assert (newField != null);
        assert (fieldList.contains(newField) == false);
        //
        // Ensure that nameForUserInterface is unique for the record
        //
        for (DmFieldI field : fieldList) {
            if (field.getNameForUserInterface().equals(newField.getNameForUserInterface())) {
                throw (new Error("Name for user interface '" + field.getNameForUserInterface() + "' already used. Field: " + field.toString()));
            }
        }
        fieldList.add(newField);
        return (newField);
    }

    @Override
    public String getElementType() {
        return (elementType);
    }

    @Override
    public String getElementClass() {
        return (elementType);
    }

    @Override
    public String getElementConfigurationType() {
        return (getElementType());
    }

    final public String getIdTitle() {
        StringBuilder b = new StringBuilder(50);
        b.append(getId());
        if ((getTitle() != null) && (getTitle().isEmpty() == false)) {
            b.append(" - ").append(getTitle());
        }
        return (b.toString());
    }

    final public String getTypeIdTitle() {
        StringBuilder b = new StringBuilder(50);
        b.append(getElementType()).append(": ");
        b.append(getId());
        if ((getTitle() != null) && (getTitle().isEmpty() == false)) {
            b.append(" - ").append(getTitle());
        }
        return (b.toString());
    }

    final public String getTypeId() {
        StringBuilder b = new StringBuilder(50);
        b.append(getElementType()).append(": ");
        b.append(getId());
        return (b.toString());
    }

    @Override
    public List<DmFieldI> getFields() {
        return (fieldList);
    }

    @Override
    public DmFieldI getFieldByName(String name) {
        for (DmFieldI field : fieldList) {
            if (field.getNameForUserInterface().equals(name)) {
                return (field);
            }
        }
        return (null);
    }

    @Override
    final public String getFieldsAsText() {
        StringBuilder text = new StringBuilder(300);
        text.append("-------------------------------------------------------------\n");
        for (DmFieldI field : fieldList) {
            if (field instanceof DmValueFieldI) {
                text.append(field.getNameForUserInterface());
                text.append(": ");
                text.append(((DmValueFieldI) field).getValueAsText());
                text.append("\n");
                text.append("-------------------------------------------------------------\n");
            }
        }
        return (text.toString());
    }

    @Override
    public boolean save() {
        for (DmFieldI field : fieldList) {
            field.pushChangesToDatastore();
        }
        return (true);
    }

    //
    // Change handling
    //
    @Override
    final public void addChangeListener(ChangeListener newListener) {
        if (listenerManager == null) {
            listenerManager = new EditorManager(this);
        }
        listenerManager.addChangeListener(newListener);
    }

    @Override
    final public void removeChangeListener(ChangeListener obsoletListener) {
        if (listenerManager != null) {
            listenerManager.removeChangeListener(obsoletListener);
        }
    }

    @Override
    final public void editStateChanged() {
        if (listenerManager != null) {
            listenerManager.fireEditStateChange();
        }
    }

    @Override
    final public EditState getEditState() {
        if (listenerManager == null) {
            return (EditState.UNCHANGED);
        }
        return (listenerManager.getEditState());
    }

    final public void fireChange() {
        if (listenerManager != null) {
            listenerManager.fireChange();
        }
    }

    @Override
    public boolean isEditable() {
        return (false);
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return (o == this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.elementType);
        return hash;
    }

    //--------------------------------------------------------------------------
    //
    // Support for automatic test.
    //
    //--------------------------------------------------------------------------
    @Override
    final public String getTypeAndFieldsAsTextSorted() {
        return (getTypeAndFieldsAsTextSorted(fieldList));
    }

    final public String getTypeAndFieldsAsTextSorted(Collection<? extends DmFieldI> fields) {

        ArrayList<DmFieldI> sortedList = new ArrayList<>(fields);
        sortedList.sort((DmFieldI t, DmFieldI t1) -> {
            return (t.getNameForUserInterface().compareTo(t1.getNameForUserInterface()));
        });

        StringBuilder text = new StringBuilder(300);
        text.append(this.getClass().getSimpleName()).append(":\n");
        text.append("-------------------------------------------------------------\n");
        for (DmFieldI field : sortedList) {
            if (field instanceof DmValueFieldI) {
                text.append(field.getNameForUserInterface());
                text.append(": ");
                text.append(((DmValueFieldI) field).getValueAsText());
                text.append("\n");
                text.append("-------------------------------------------------------------\n");
            }
        }
        return (text.toString());
    }

}
