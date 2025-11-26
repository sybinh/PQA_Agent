/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import DataStore.DsFieldI;
import DataStore.DsRecordI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A field in the data model that is directly connected to a field in the data
 * store and hold the same data type as the connected field in the data store.
 * Each setValue() and getValue() goes directly to the field in the data store.
 * <br>
 * The class supports also the adding of alternative data store fields. If the
 * primary data source field contains null, then the value is taken from the
 * alternative data source field. If a value is written, the value goes always
 * to the primary data source field and the value in the alternative data source
 * field is set to null.
 *
 * @author gug2wi
 * @param <T_DS_RECORD> Type of data source parent record.
 * @param <T_CONTENT> Type of content hold by the field in data model and data
 * store.
 */
public class DmToDsValueField<T_DS_RECORD extends DsRecordI<?>, T_CONTENT> extends DmToDsField<T_CONTENT> implements DmValueFieldI<T_CONTENT> {

    public enum WriteMode {
        WRITE_ONLY_TO_PRIMARY_FIELD,
        WRITE_ALSO_TO_ALTERNATIVE_FIELDS;
    }

    private int widthInCharacter;

    private List<DsFieldI<? extends DsRecordI<?>, T_CONTENT>> alternativeFields = null;
    private WriteMode writeMode = WriteMode.WRITE_ONLY_TO_PRIMARY_FIELD;

    /**
     * Creates the field and connects it to the data source field.
     *
     * @param dsField The primary data source field.
     * @param nameForUserInterface Name of the field for the UI.
     */
    public DmToDsValueField(DsFieldI<? extends DsRecordI<?>, T_CONTENT> dsField, String nameForUserInterface) {
        super(dsField, nameForUserInterface);
        this.widthInCharacter = -1;
    }

    /**
     * Sets the writeMode for the field to write in alternate data sources for
     * the field.
     *
     * @param newWriteMode - Write mode for the field
     */
    public void setWriteMode(WriteMode newWriteMode) {
        assert (newWriteMode != null);
        writeMode = newWriteMode;
    }

    /**
     * Adds an alternative field. An alternative field is used to read the value
     * of the field, if the primary field has the value null.
     * <br>
     * Note that this field will never be used for writing. The writing is
     * always done in the primary field. The value of all alternative fields
     * will be set to null during the writing.
     *
     * @param alternativeField The alternative data source field.
     */
    public void addAlternativeField(DsFieldI<? extends DsRecordI<?>, T_CONTENT> alternativeField) {
        assert (alternativeField != null);
        assert (alternativeField != dsField);

        if (alternativeFields == null) {
            alternativeFields = new ArrayList<>(1);
        }

        for (DsFieldI<? extends DsRecordI<?>, T_CONTENT> af : alternativeFields) {
            assert (af != alternativeField);
        }

        alternativeFields.add(alternativeField);
    }

    /**
     * Checks whether or not the alternative field shall be used if the given
     * value is in the default field.
     */
    protected boolean switchToAlternativeField(T_CONTENT value) {
        if (value == null) {
            return (true);
        } else if (value.toString().isEmpty()) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Returns the current value. Might be null.
     *
     * @return The current value. Might be null.
     */
    @Override
    public T_CONTENT getValue() {
        if (alternativeFields == null) {
            return (dsField.getDataModelValue());
        } else {
            T_CONTENT value = dsField.getDataModelValue();
            Iterator<DsFieldI<? extends DsRecordI<?>, T_CONTENT>> fieldIterator = alternativeFields.iterator();
            while (switchToAlternativeField(value) && fieldIterator.hasNext()) {
                value = fieldIterator.next().getDataModelValue();
            }
            return (value);
        }
    }

    @Override
    public void setValue(T_CONTENT v) {
        dsField.setDataModelValue(v);
        if (alternativeFields != null) {
            switch (writeMode) {
                case WRITE_ONLY_TO_PRIMARY_FIELD:
                    for (DsFieldI<? extends DsRecordI<?>, T_CONTENT> field : alternativeFields) {
                        if (field.isReadOnly() == false) {
                            field.setDataModelValue(null);
                        }
                    }
                    break;
                case WRITE_ALSO_TO_ALTERNATIVE_FIELDS:
                    for (DsFieldI<? extends DsRecordI<?>, T_CONTENT> field : alternativeFields) {
                        if (field.isReadOnly() == false) {
                            field.setDataModelValue(v);
                        }
                    }
                    break;
                default:
                    throw (new Error("Unexpected writeMode: " + writeMode));
            }
        }
    }

    @Override
    public int getWidthInCharacter() {
        return this.widthInCharacter;
    }

    protected void setWidthInCharacter(int widthInCharacter) {
        this.widthInCharacter = widthInCharacter;
    }
}
