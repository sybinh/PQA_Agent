/*
 * Copyright (c) 2009, 2016 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess.Exceptions;

import OslcAccess.OslcWriteableFieldI;
import Rq1Cache.Records.Rq1RecordInterface;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gug2wi
 */
public class OverwriteError extends WriteToDatabaseRejected {

    public static class ChangedField {

        final private OslcWriteableFieldI field;
        final private String oldDbValue;
        final private String currentDbValue;
        final private String newDbValue;

        public ChangedField(OslcWriteableFieldI field, String oldDbValue, String currentDbValue, String newDbValue) {
            assert (field != null);
            assert (oldDbValue != null);
            assert (currentDbValue != null);
            assert (newDbValue != null);

            this.field = field;
            this.oldDbValue = oldDbValue;
            this.currentDbValue = currentDbValue;
            this.newDbValue = newDbValue;
        }
    }

    final private Rq1RecordInterface affectedRecord;
    final private List<ChangedField> changedFields;

    public OverwriteError(Rq1RecordInterface affectedRecord) {
        assert (affectedRecord != null);

        this.affectedRecord = affectedRecord;
        changedFields = new ArrayList<>(10);
    }

    public void addField(ChangedField changedField) {
        assert (changedField != null);

        changedFields.add(changedField);
    }

    @Override
    public String getMessageForUi() {
        return ("Write to database would overwrite changes done by other users.");
    }

    @Override
    public String getDetailedMessageForUi() {
        StringBuilder b = new StringBuilder(100);
        for (ChangedField field : changedFields) {
            b.append("Fieldname: ").append(field.field.getOslcPropertyName()).append("\n");
            b.append("Fieldname in database: ").append(field.field.getOslcPropertyName()).append("\n");
            b.append("--- Old value from database ---\n");
            b.append(field.oldDbValue).append("\n");
            b.append("--- Current value from database ---\n");
            b.append(field.currentDbValue).append("\n");
            b.append("--- New value to write ---\n");
            b.append(field.newDbValue).append("\n");
            b.append("-------------------------\n");
            b.append("\n");
        }
        return (b.toString());
    }

}
