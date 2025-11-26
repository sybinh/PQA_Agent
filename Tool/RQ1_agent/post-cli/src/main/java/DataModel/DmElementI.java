/*
 * Copyright (c) 2009, 2018 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import util.EcvListenerManager;
import Monitoring.MarkableI;
import Monitoring.RuleManagerI;
import java.util.List;

/**
 * Defines the interface for all elements of the data model.
 *
 * @author GUG2WI
 */
public interface DmElementI extends MarkableI, RuleManagerI {

    //--------------------------------------------------------------
    //
    // Interface to propagate changes
    //
    //--------------------------------------------------------------
    public interface ChangeListener extends EcvListenerManager.ChangeListener<DmElementI> {

    }

    void addChangeListener(ChangeListener newListener);

    void removeChangeListener(ChangeListener obsoletListener);

    //--------------------------------------------------------------
    //
    // Interface to manage edit state
    //
    //--------------------------------------------------------------
    boolean isEditable();

    public enum EditState {

        UNCHANGED(),
        CHANGED();
    }

    public interface Editor extends ChangeListener {

        public EditState getEditState(DmElementI dmElement);

        public void editStateChanged(DmElementI dmElement);
    }

    void editStateChanged();

    EditState getEditState();

    /**
     * Save all changes made in the fields of the element to the database. If
     * the record does not yet exists in the database, then the record will be
     * created.
     *
     * @return true ... If the writing to the database was successful for all
     * fields. Or if no field was changed and no writing was necessary.
     * <p>
     * false ... Otherwise.
     */
    boolean save();

    //
    // Interface to trigger the update of the element from the database.
    //
    void reload();

    /**
     * Returns the fields contained in the element
     *
     * @return List of all fields in the element.
     */
    List<DmFieldI> getFields();

    /**
     * Returns the field with the given name on the user interface.
     *
     * @param name Name of the wanted field.
     * @return The field, if a field with this name exists. null, if no field
     * with this name exists.
     */
    DmFieldI getFieldByName(String name);

    /**
     * Returns the names and content of all fields as a text.
     *
     * @return
     */
    String getFieldsAsText();

    /**
     * Return a text for the type of the element that is shown to the user on
     * the UI. This type is shown in e.e. in the tree view, in the panel of the
     * element and so on.
     *
     * @return Type of the element.
     */
    String getElementType();

    /**
     * Return a text for the class of an element that will be shown to the user
     * on the UI. E.g. Release, Issue, Workitem, ...
     *
     * @return Name of the class of the element
     */
    String getElementClass();

    /**
     * Return a name for the type of the element that shall be used to save
     * configuration data.
     *
     * @return Type for configuration data.
     */
    String getElementConfigurationType();

    String getTitle();

    String getId();

    /**
     * Return all data. Useful for automatic tests.
     * @return
     */
    String getTypeAndFieldsAsTextSorted();

}
