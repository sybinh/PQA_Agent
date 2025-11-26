/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Records;

import DataModel.ALM.Fields.DmAlmField_Boolean;
import DataModel.ALM.Fields.DmAlmField_Text;
import DataModel.ALM.Fields.DmAlmField_Date;
import DataModel.ALM.Fields.DmAlmField_Enumeration;
import DataModel.ALM.Fields.DmAlmField_EnumerationFromOtherElement;
import DataModel.ALM.Fields.DmAlmField_EnumerationFromText;
import DataModel.ALM.Fields.DmAlmField_ExternalResourceList;
import DataModel.ALM.Fields.DmAlmField_Number;
import DataModel.ALM.Fields.DmAlmField_ReferencedAlmElement;
import DataModel.ALM.Fields.DmAlmField_Resource;
import DataModel.ALM.Fields.DmAlmField_ResourceAsEnumeration;
import DataModel.DmElement;
import DataModel.DmSourceField_ReadOnly_Text;
import DataStore.ALM.DsAlmField;
import DataStore.ALM.DsAlmField_Boolean;
import DataStore.ALM.DsAlmField_Date;
import DataStore.ALM.DsAlmField_Number;
import DataStore.ALM.DsAlmField_ResourceList;
import DataStore.ALM.DsAlmField_Text;
import DataStore.ALM.DsAlmRecord;
import DataStore.ALM.DsAlmRecordI;
import DataStore.DsRecordI;
import Monitoring.MarkableI;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public abstract class DmAlmElement extends DmElement implements DmAlmElementI, DsAlmRecordI.ChangeListener, MarkableI.MarkerListener {

    private static final Logger LOGGER = Logger.getLogger(DmAlmElement.class.getCanonicalName());

    final private DsAlmRecord dsAlmRecord;
    final private Map<String, DsAlmField<?>> unassignedDsFields;

    public DmSourceField_ReadOnly_Text URL;
    final public DmAlmField_ReferencedAlmElement REFERENCED_ALM_ELEMENTS;

    @SuppressWarnings("unchecked")
    protected DmAlmElement(String elementType, DsAlmRecord dsAlmRecord) {
        super(elementType);
        assert (dsAlmRecord != null);

        this.dsAlmRecord = dsAlmRecord;
        unassignedDsFields = new TreeMap<>();
        for (DsAlmField field : dsAlmRecord.getFields()) {
            unassignedDsFields.put(field.getFieldName(), field);
        }

        addField(URL = new DmSourceField_ReadOnly_Text("Url") {
            @Override
            public String getValue() {
                return (getUrl());
            }
        });

        addField(REFERENCED_ALM_ELEMENTS = new DmAlmField_ReferencedAlmElement("Referenced ALM Elements"));

        dsAlmRecord.addChangeListener(this);
        dsAlmRecord.addMarkerListener(this);
    }

    protected DmAlmElement(String elementType) {
        super(elementType);

        this.dsAlmRecord = new DsAlmRecord();

        unassignedDsFields = new TreeMap<>();
        for (DsAlmField field : dsAlmRecord.getFields()) {
            unassignedDsFields.put(field.getFieldName(), field);
        }
        
        addField(URL = new DmSourceField_ReadOnly_Text("Url") {
            @Override
            public String getValue() {
                return (getUrl());
            }
        });

        addField(REFERENCED_ALM_ELEMENTS = new DmAlmField_ReferencedAlmElement("Referenced ALM Elements"));

        dsAlmRecord.addChangeListener(this);
        dsAlmRecord.addMarkerListener(this);
    }

    public boolean existsInDatabase() {
        return (dsAlmRecord.existsInDatabase());
    }

    @Override
    public String getUrl() {
        return (dsAlmRecord.getUrl());
    }

    public void openInAlm() {
        dsAlmRecord.openInAlm();
    }

    public abstract String getStatus();

    @Override
    public void changed(DsRecordI changedElement) {
        super.fireChange();
    }

    @Override
    public void markerChanged(MarkableI changedMarkable) {
        fireMarkerChange();
    }

    @Override
    public boolean save() {
        super.save();
        if (dsAlmRecord.save()) {
            return (true);
        }
        return (false);
    }

    @Override
    public void reload() {
        dsAlmRecord.reload();
    }

    //--------------------------------------------------------------------------
    //
    // Support for field creation
    //
    //--------------------------------------------------------------------------
    final protected DmAlmField_Text addTextField(String nameInAlm, String nameForUserInterface) {
        return (addTextField(nameInAlm, nameForUserInterface, false));
    }

    final protected DmAlmField_Text addTextField(String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);
        return (addTextField(arrayOf(nameInAlm), nameForUserInterface, createDsFieldIfNotExisting));
    }

    final protected DmAlmField_Text addTextField(String[] namesInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = getDsField(namesInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_Text(dsAlmRecord, namesInAlm[0], null));
            } else {
                handleMissingField(namesInAlm);
            }
        } else if (dsField instanceof DsAlmField_Text == false) {
            handleWrongFieldType(namesInAlm.toString(), dsField);
        }

        return (addField(new DmAlmField_Text((DsAlmField_Text) dsField, nameForUserInterface)));
    }

    final protected DmAlmField_Date addDateField(String nameInAlm, String nameForUserInterface) {
        return (addDateField(nameInAlm, nameForUserInterface, false));
    }

    final protected DmAlmField_Date addDateField(String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);

        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = unassignedDsFields.remove(nameInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_Date(dsAlmRecord, nameInAlm, null));
            } else {
                handleMissingField(nameInAlm);
            }
        } else if (dsField instanceof DsAlmField_Date == false) {
            handleWrongFieldType(nameInAlm, dsField);
        }

        return (addField(new DmAlmField_Date((DsAlmField_Date) dsField, nameForUserInterface)));
    }

    final protected DmAlmField_Boolean addBooleanField(String nameInAlm, String nameForUserInterface) {
        return (addBooleanField(nameInAlm, nameForUserInterface, false));
    }

    final protected DmAlmField_Boolean addBooleanField(String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);
        return (addBooleanField(arrayOf(nameInAlm), nameForUserInterface, createDsFieldIfNotExisting));
    }

    final protected DmAlmField_Boolean addBooleanField(String[] namesInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = getDsField(namesInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_Boolean(dsAlmRecord, namesInAlm[0], null));
            } else {
                handleMissingField(namesInAlm);
            }
        } else if (dsField instanceof DsAlmField_Boolean == false) {
            handleWrongFieldType(namesInAlm.toString(), dsField);
        }

        return (addField(new DmAlmField_Boolean((DsAlmField_Boolean) dsField, nameForUserInterface)));
    }

    final protected DmAlmField_Number addNumberField(String nameInAlm, String nameForUserInterface) {
        return (addNumberField(nameInAlm, nameForUserInterface, false));
    }

    final protected DmAlmField_Number addNumberField(String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);

        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField<?> dsField = unassignedDsFields.remove(nameInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_Number(dsAlmRecord, nameInAlm, null));
            } else {
                handleMissingField(nameInAlm);
            }
        } else if (dsField instanceof DsAlmField_Number == false) {
            handleWrongFieldType(nameInAlm, dsField);
        }

        return (addField(new DmAlmField_Number((DsAlmField_Number) dsField, nameForUserInterface)));
    }

    final protected DmAlmField_Enumeration addEnumerationField(String nameInAlm, String nameForUserInterface) {
        return (addEnumerationField(nameInAlm, nameForUserInterface, false));
    }

    final protected DmAlmField_Enumeration addEnumerationField(String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);

        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = unassignedDsFields.remove(nameInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_ResourceList(dsAlmRecord, nameInAlm, null));
            } else {
                handleMissingField(nameInAlm);
            }
        } else if (dsField instanceof DsAlmField_ResourceList == false) {
            handleWrongFieldType(nameInAlm, dsField);
        }
        return (addField(new DmAlmField_Enumeration((DsAlmField_ResourceList) dsField, nameForUserInterface)));
    }

    final protected <T_ELEMENT extends DmAlmElement> DmAlmField_EnumerationFromOtherElement<T_ELEMENT> addEnumerationField(String type, String projectArea, String property, String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (type != null);
        assert (property != null);
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);

        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = unassignedDsFields.remove(nameInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_ResourceList(dsAlmRecord, nameInAlm, null));
            } else {
                handleMissingField(nameInAlm);
            }
        } else if (dsField instanceof DsAlmField_ResourceList == false) {
            handleWrongFieldType(nameInAlm, dsField);
        }

        return (addField(new DmAlmField_EnumerationFromOtherElement<>((DsAlmField_ResourceList) dsField, type, projectArea, property, nameForUserInterface)));
    }

    final protected DmAlmField_EnumerationFromText addEnumerationFromTextField(String nameInAlm, EcvEnumeration[] validValues, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);

        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = unassignedDsFields.remove(nameInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_Text(dsAlmRecord, nameInAlm, null));
            } else {
                handleMissingField(nameInAlm);
            }
        } else if (dsField instanceof DsAlmField_Text == false) {
            handleWrongFieldType(nameInAlm, dsField);
        }

        return (addField(new DmAlmField_EnumerationFromText((DsAlmField_Text) dsField, nameForUserInterface, validValues)));
    }

    final protected void addResourceListField(String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);
        addResourceListField(arrayOf(nameInAlm), nameForUserInterface, createDsFieldIfNotExisting);
    }

    final protected void addResourceListField(String[] namesInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = getDsField(namesInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_ResourceList(dsAlmRecord, namesInAlm[0], null));
            } else {
                handleMissingField(namesInAlm);
            }
        } else if (dsField instanceof DsAlmField_ResourceList == false) {
            handleWrongFieldType(namesInAlm.toString(), dsField);
        }

        REFERENCED_ALM_ELEMENTS.addDsField(nameForUserInterface, (DsAlmField_ResourceList) dsField);
    }

    final protected void addResourceListFieldForProjectArea(String nameForUserInterface, DsAlmField dsField) {
        assert (dsField != null);
        REFERENCED_ALM_ELEMENTS.addDsField(nameForUserInterface, (DsAlmField_ResourceList) dsField);
    }

    final protected DmAlmField_Resource addResourceField(String nameInAlm, String nameForUserInterface) {
        return (addResourceField(nameInAlm, nameForUserInterface, false));
    }

    final protected DmAlmField_Resource addResourceField(String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);

        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = unassignedDsFields.remove(nameInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_ResourceList(dsAlmRecord, nameInAlm, null));
            } else {
                handleMissingField(nameInAlm);
            }
        } else if (dsField instanceof DsAlmField_ResourceList == false) {
            handleWrongFieldType(nameInAlm, dsField);
        }

        return (addField(new DmAlmField_Resource<>((DsAlmField_ResourceList) dsField, nameForUserInterface)));
    }

    final protected DmAlmField_ExternalResourceList addExternalResourceField(String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);

        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = unassignedDsFields.remove(nameInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_ResourceList(dsAlmRecord, nameInAlm, null));
            } else {
                handleMissingField(nameInAlm);
            }
        } else if (dsField instanceof DsAlmField_ResourceList == false) {
            handleWrongFieldType(nameInAlm, dsField);
        }

        return (addField(new DmAlmField_ExternalResourceList((DsAlmField_ResourceList) dsField, nameForUserInterface)));
    }

    final protected DmAlmField_ResourceAsEnumeration addResourceAsEnumerationField(String nameInAlm, String nameForUserInterface) {
        return (addResourceAsEnumerationField(nameInAlm, nameForUserInterface, false));
    }

    final protected DmAlmField_ResourceAsEnumeration addResourceAsEnumerationField(String nameInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);
        return (addResourceAsEnumerationField(arrayOf(nameInAlm), nameForUserInterface, createDsFieldIfNotExisting));
    }

    final protected DmAlmField_ResourceAsEnumeration addResourceAsEnumerationField(String[] namesInAlm, String nameForUserInterface, boolean createDsFieldIfNotExisting) {
        if (existsInDatabase() == false) {
            createDsFieldIfNotExisting = true;
        }

        DsAlmField dsField = getDsField(namesInAlm);
        if (dsField == null) {
            if (createDsFieldIfNotExisting == true) {
                dsField = dsAlmRecord.addAlmField(new DsAlmField_ResourceList(dsAlmRecord, namesInAlm[0], null));
            } else {
                handleMissingField(namesInAlm);
            }
        } else if (dsField instanceof DsAlmField_ResourceList == false) {
            handleWrongFieldType(namesInAlm.toString(), dsField);
        }
        return (addField(new DmAlmField_ResourceAsEnumeration((DsAlmField_ResourceList) dsField, nameForUserInterface)));
    }

    private DsAlmField getDsField(String[] namesInAlm) {
        assert (namesInAlm != null);
        assert (namesInAlm.length > 0);
        for (String nameInAlm : namesInAlm) {
            assert (nameInAlm != null);
            assert (nameInAlm.isEmpty() == false);
            DsAlmField dsField = unassignedDsFields.remove(nameInAlm);
            if (dsField != null) {
                return (dsField);
            }
        }
        return (null);
    }

    final protected String[] arrayOf(String... namesInAlm
    ) {
        return (namesInAlm);
    }

    final protected String[] create_j_names(String fieldName) {
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);

        String[] result = new String[10];
        result[0] = "j.0:" + fieldName;
        result[1] = "j.1:" + fieldName;
        result[2] = "j.2:" + fieldName;
        result[3] = "j.3:" + fieldName;
        result[4] = "j.4:" + fieldName;
        result[5] = "j.5:" + fieldName;
        result[6] = "j.6:" + fieldName;
        result[7] = "j.7:" + fieldName;
        result[8] = "j.8:" + fieldName;
        result[9] = "j.9:" + fieldName;

        return (result);
    }

    private void handleWrongFieldType(String nameInAlm, DsAlmField<?> field) {
        throw (new Error("Invalid type (" + field.getClass().getName() + ") for " + nameInAlm));
    }

    private void handleMissingField(String[] namesInAlm) {

        if (namesInAlm.length == 1) {
            handleMissingField(namesInAlm[0]);
        } else {
            StringBuilder b = new StringBuilder();
            for (String nameInAlm : namesInAlm) {
                if (b.length() != 0) {
                    b.append(",");
                }
                b.append("'").append(nameInAlm).append("'");
            }
            throw (new Error("Fields " + b.toString() + " missing."));
        }
    }

    private void handleMissingField(String nameInAlm) {
        throw (new Error("Field '" + nameInAlm + "' missing for Workitem " + this.getId() + "."));
    }

    final protected void ignoreField(String nameInAlm) {
        assert (nameInAlm != null);
        assert (nameInAlm.isEmpty() == false);

        DsAlmField<?> field = unassignedDsFields.remove(nameInAlm);
        if (field == null) {
            LOGGER.log(Level.FINER, "{0}, Field not found: {1}", new Object[]{this.getClass().getSimpleName(), nameInAlm});
        }
    }

    @Override
    final public String checkForUnusedFields() {

        if (unassignedDsFields.isEmpty() == false) {
            StringBuilder b = new StringBuilder();
            b.append("Unused fields for " + toString() + ":");
            for (DsAlmField<?> field : unassignedDsFields.values()) {
                b.append("\n").append(field.getClass().getSimpleName()).append(": ").append(field.getFieldName()).append("= ").append(field.getDataModelValue());
            }
            LOGGER.warning(b.toString());

            return (b.toString());
        } else {
            return ("No unused fields");
        }

    }

    @Override
    public boolean isEditable() {
        return (true);
    }

}
