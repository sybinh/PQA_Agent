/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import Monitoring.MarkableI;
import util.EcvAttributeI;
import util.EcvListenerManager;

/**
 * Defines the interface for a field in the data model.
 *
 * Note that a field is not always connected to a element in the data model,
 * although most fields are.
 *
 * @author gug2wi
 */
public interface DmFieldI extends EcvAttributeI, MarkableI {

    //---------------------------------------------------------------------
    //
    // Interface to propagate changes
    //
    //---------------------------------------------------------------------
    public interface ChangeListener extends EcvListenerManager.ChangeListener<DmFieldI> {
    }

    void addChangeListener(ChangeListener newListener);

    void removeChangeListener(ChangeListener obsoletListener);

    //---------------------------------------------------------------------
    //
    // Interface to handle attributes
    //
    //---------------------------------------------------------------------
    /**
     * Attributes supported for DmFieldI
     */
    static public class Attribute {

        final static public String NUMBER = "Number";
        /**
         * Field is a single line text field
         */
        final static public String SINGLELINE_TEXT = "SinglelineTextField";
        /**
         * Field is a multi line text field
         */
        final static public String MULTILINE_TEXT = "MultilineTextField";
        /**
         * Field is a enumeration type and integration step
         */
        public static final String INTEGRATION_STEP = "IntegrationStepField";
        /**
         * Field is a enumeration type and integration step
         */
        public static final String ASIL_CLASSIFICATION = "AsilClassificationField";
        /**
         * Field is a enumeration type and product
         */
        public static final String PRODUCT = "Product";
        /**
         * Field is a multi line text field containing formating via HTML.
         */
        final static public String HTML_TEXT = "HtmlField";
        /**
         * Field is read only on the UI
         */
        final static public String READ_ONLY_ON_UI = "ReadOnlyOnUi";
        /**
         * Field is available for bulk operations
         */
        final static public String FIELD_FOR_BULK_OPERATION = "BulkOperation";
        /**
         * Field is available for the user defined tabs
         */
        final static public String FIELD_FOR_USER_DEFINED_TAB = "UserDefinedTab";
        /**
         * Field is by default visible in the user defined tab.
         */
        final static public String VISIBLE_BY_DEFAULT = "VisibleByDefault";
        /**
         * Field is available for a special filter and active in default.
         */
        final static public String FIELD_FOR_TEXT_SEARCH_DEFAULT_ON = "FieldForTextSearchDefaultOn";
        /**
         * Field is available for a special filter and not active in default.
         */
        final static public String FIELD_FOR_TEXT_SEARCH_DEFAULT_OFF = "FieldForTextSearchDefaultOff";
        /**
         * Field is available for a User Assistant's configurable rule.
         */
        final static public String FIELD_ATTACHMENT_FOR_CONFIG_RULES = "FieldAttachmentForConfigurableRules";
        /**
         * Field has this attribute will be ignore by User Assistant
         */
        final static public String IGNORE_FOR_CONFIG_RULES = "FieldIgnoreForConfigurableRules";
    }

    //---------------------------------------------------------------------
    //
    // Other interfaces
    //
    //---------------------------------------------------------------------
    String getNameForUserInterface();

    boolean isReadOnly();

    /**
     * Called before an element is saved. The field should push all changed data
     * to the data store object to ensure that the latest state is saved to the
     * data store.
     *
     * @return true, if anything was pushed. false otherwise.
     */
    default boolean pushChangesToDatastore() {
        return (false);
    }
}
