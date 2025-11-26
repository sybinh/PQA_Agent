/*
 * Copyright (c) 2009, 2019 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package OslcAccess;

import Rq1Cache.Records.Rq1AttributeName;
import java.util.Map;
import java.util.TreeMap;

/**
 * Provides hints for the load methods of OslcClient. Use this class to optimize
 * the read access to the OSLC server.
 * <p>
 * By default, OslcAccess uses lazy load when loading records from the OSLC
 * server. This means, it loads only those elements and lists which are actually
 * requested by a call to one of the load method. As a result, OslcAccess loads
 * data with many small and fast server requests. This approach fits to many GUI
 * applications, because the user gets fast response to its requests. But this
 * approach does not fit to applications which load mass data. Because
 * OslcAccess creates a lot of small server requests, whenever the application
 * navigates along the data model. This class helps to solve this problem.
 * <p>
 * The following parameters are available:
 * <ul>
 * <li> isLoadRecordContentActive(): True, if the content of a record shall be
 * loaded. The record content consists of all plain fields (e.g. text field,
 * date field, XML field, ...) and fields that hold the reference to one record
 * (e.g. predecessor).<br>
 * False, if only the reference to the record shall be loaded.
 * <li> Hints for fields: A list of OslcLoadHint for fields of the record.
 * </ul>
 */
public class OslcLoadHint {

    private boolean loadRecordContentActive = false;
    private boolean dependsOnSubRecords = false;
    final private Map<String, OslcLoadHint> fieldsToLoad = new TreeMap<>();

    /**
     * Create a load hint.
     *
     * @param loadRecordContentActive True, if the record content shall be
     * loaded. False, if not.
     */
    public OslcLoadHint(boolean loadRecordContentActive) {
        this.loadRecordContentActive = loadRecordContentActive;
    }

    /**
     *
     * @param loadRecordContentActive True, if the record content shall be
     * loaded. False, if not.
     * @return The load hint for concatenation of commands
     */
    public OslcLoadHint setLoadRecordContentActive(boolean loadRecordContentActive) {
        this.loadRecordContentActive = loadRecordContentActive;
        return (this);
    }

    /**
     * Is true, if the record content shall be loaded. The record content
     * consists of all plain fields (e.g. text field, date field, XML field,
     * ...) and fields that hold the reference to one record (e.g. predecessor).
     *
     * @return
     */
    public boolean isLoadRecordContentActive() {
        return loadRecordContentActive;
    }

    /**
     *
     * @param loadSubRecordFirst True, if the sub records shall be loaded
     * before.
     */
    public void setDependsOnSubRecords(boolean loadSubRecordFirst) {
        this.dependsOnSubRecords = loadSubRecordFirst;
    }

    public boolean isDependsOnSubRecords() {
        return dependsOnSubRecords;
    }

    /**
     * Create a hint for following a field, add this hint as field hint and
     * return the created hint.
     *
     * @param fieldName Name of the field for which the hint will be created.
     * @param loadFieldContentActive Indicates, if the record content for the
     * field shall be loaded.
     * @return The created hint for the field.
     */
    public OslcLoadHint followField(String fieldName, boolean loadFieldContentActive) {
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);

        OslcLoadHint fieldHint = new OslcLoadHint(loadFieldContentActive);

        fieldsToLoad.put(fieldName, fieldHint);

        return (fieldHint);
    }

    public OslcLoadHint followField(Rq1AttributeName attributeName, boolean loadFieldContentActive) {
        return (followField(attributeName.getName(), loadFieldContentActive));
    }

    public OslcLoadHint followField(String fieldName, OslcLoadHint hintForField) {
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);
        assert (hintForField != null);

        fieldsToLoad.put(fieldName, hintForField);

        return (this);
    }

    public OslcLoadHint followField(Rq1AttributeName attributeName, OslcLoadHint hintForField) {
        return (followField(attributeName.getName(), hintForField));
    }

    public OslcLoadHint addFieldHint(String fieldName, OslcLoadHint hintForField) {
        return (followField(fieldName, hintForField));
    }

    public boolean hasHintForField(String fieldName) {
        assert (fieldName != null);
        return (fieldsToLoad.containsKey(fieldName));
    }

    public OslcLoadHint getHintForField(String fieldName) {
        assert (fieldName != null);
        return (fieldsToLoad.get(fieldName));
    }

    /**
     * Added for debugging.
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(OslcLoadHint.class.getName()).append("{");
        b.append(loadRecordContentActive).append(";");
        b.append(dependsOnSubRecords).append(";");
        for (Map.Entry<String, OslcLoadHint> entry : fieldsToLoad.entrySet()) {
            b.append(entry.getKey()).append("->");
            b.append(entry.getValue().toString());
        }
        b.append("}");
        return (b.toString());
    }

}
