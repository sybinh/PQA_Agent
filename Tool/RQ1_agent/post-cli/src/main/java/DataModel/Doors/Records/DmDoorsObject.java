/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import ConfigurableRules.ConfigRule.util.Constants;
import DataModel.DmConstantField_Text;
import DataModel.DmElementField_ReadOnlyFromSource;
import DataModel.DmElementListField_ReadOnlyFromSource;
import DataModel.DmFieldI;
import DataModel.DmToDsField_Text;
import DataModel.Doors.Monitoring.DmDoorsRule_CheckBaselinedObject;
import DataModel.Rq1.Records.DmRq1Element;
import DataModel.Rq1.Records.DmRq1ElementInterface;
import DataStore.Exceptions.DsFieldContentFailure;
import Doors.DoorsObject;
import Monitoring.Rule;
import Monitoring.RuleDescription;
import Monitoring.RuleExecutionGroup;
import UiSupport.UiTreeViewRootElementI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Represents an object from the DOORS database on the data model level. An
 * object in the DOORS database is a record in a module of the DOORS database.
 *
 * @author gug2wi
 */
public abstract class DmDoorsObject extends DmDoorsRecord implements UiTreeViewRootElementI, Comparable<DmDoorsObject> {

    static private final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DmDoorsObject.class.getCanonicalName());

    final static public String ATTRIBUTENAME_DOORS_OBJECT_TYPE = "ObjectType";
    final static public String ATTRIBUTENAME_DOORS_OBJECT_TYPE_ = "_Object Type_";
    final static public String ATTRIBUTENAME_DOORS_OBJECT_TYPE_PS_EC = "ObjectType_PS-EC";

    private Map<String, String> unusedUserDefinedFields;
    private ArrayList<DmFieldI> predefinedFields;
    private Map<String, DmConstantField_Text> otherFields = null;

    final protected DoorsObject doorsObject;
    protected DmDoorsModule parentModule = null;

    final public DmElementField_ReadOnlyFromSource<DmDoorsModule> PARENT_MODULE;
    final public DmConstantField_Text RDF_ABOUT;

    final public DmConstantField_Text ABSOLUTE_NUMBER;
    final public DmConstantField_Text LAST_MODIFIED_TIME;
    final public DmToDsField_Text OBJECT_HEADING;
    final public DmConstantField_Text OBJECT_IDENTIFIER;
    final public DmConstantField_Text OBJECT_LEVEL;
    final public DmConstantField_Text OBJECT_NUMBER;
    final public DmToDsField_Text OBJECT_SHORT_TEXT;
    final public DmToDsField_Text OBJECT_TEXT;

    final public DmElementListField_ReadOnlyFromSource<DmDoorsElement> INCOMING_DOORS_ELEMENTS;
    final public DmElementListField_ReadOnlyFromSource<DmDoorsElement> OUTGOING_DOORS_ELEMENTS;
    final public DmElementListField_ReadOnlyFromSource<DmRq1ElementInterface> REFERENCED_RQ1_ELEMENTS;
    final public DmElementListField_ReadOnlyFromSource<DmDoorsElement> INCOMING_EXTERNAL_ELEMENTS;
    final public DmElementListField_ReadOnlyFromSource<DmDoorsElement> OUTGOING_EXTERNAL_ELEMENTS;

    public String rq1Level = Constants.BLANK;

    final static public Rule expectedAttributesExist = new Rule(new RuleDescription(
            EnumSet.of(RuleExecutionGroup.DATA_STORE),
            "Check for mandatory attributes in DOORS object.",
            "Several mandatory attributes are defined for the elements stored in DOORS.\n"
            + "These mandatory attributes depend on the type of element. E.g. if it is a requirement or a system test.\n"
            + "IPE shows the mandatory attributes in fields in a defined layout.\n"
            + "\n"
            + "Missing attributes are highlighted in the layout with a warning on the field that shows the attribute content."));

    protected DmDoorsObject(ElementType elementType, DoorsObject doorsObject) {
        super(elementType, doorsObject);
        assert (doorsObject != null);
        this.doorsObject = doorsObject;

        unusedUserDefinedFields = doorsObject.getUserDefinedFields();

        //-----------------------------------
        // Add fields for parent and own URL
        //-----------------------------------
        addField(PARENT_MODULE = new DmElementField_ReadOnlyFromSource<DmDoorsModule>("Module") {
            @Override
            public DmDoorsModule getElement() {
                if (parentModule == null) {
                    parentModule = (DmDoorsModule) DmDoorsFactory.getElementByRecord(doorsObject.getParentModule());
                }
                return (parentModule);
            }
        });
        // Set attribute for User Assistant ignore this field
        PARENT_MODULE.setAttribute(DmFieldI.Attribute.IGNORE_FOR_CONFIG_RULES);

        addField(RDF_ABOUT = new DmConstantField_Text("RDF About", doorsObject.getIdentifier().getRdfAboutPath()));

        //---------------------------------------------
        // Add fields available for all DOORS objects.
        //---------------------------------------------
        addField(ABSOLUTE_NUMBER = new DmConstantField_Text("Absolute Number", doorsObject.ABSOLUTE_NUMBER.getDataModelValue()));

        addField(OBJECT_HEADING = new DmToDsField_Text<>(doorsObject.OBJECT_HEADING, "Object Heading"));
        OBJECT_HEADING.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);

        addField(OBJECT_IDENTIFIER = new DmConstantField_Text("Object Identifier", doorsObject.IDENTIFIER.getDataModelValue()));
        OBJECT_IDENTIFIER.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);

        addField(OBJECT_LEVEL = new DmConstantField_Text("Object Level", doorsObject.LEVEL.getDataModelValue()));
        addField(OBJECT_NUMBER = new DmConstantField_Text("Object Number", doorsObject.NUMBER.getDataModelValue()));
        addField(OBJECT_SHORT_TEXT = new DmToDsField_Text<>(doorsObject.OBJECT_SHORT_TEXT, "Object Short Text"));

        addField(OBJECT_TEXT = new DmToDsField_Text<>(doorsObject.OBJECT_TEXT, "Object Text"));
        OBJECT_TEXT.setAttribute(DmFieldI.Attribute.MULTILINE_TEXT, DmFieldI.Attribute.HTML_TEXT);
        OBJECT_TEXT.setAttribute(DmFieldI.Attribute.FIELD_FOR_TEXT_SEARCH_DEFAULT_ON);

        if (doorsObject.LAST_MODIFIED_TIME.getDataModelValue() != null) {
            addField(LAST_MODIFIED_TIME = new DmConstantField_Text("Last Modified Time", doorsObject.LAST_MODIFIED_TIME.getDataModelValue()));
        } else {
            LAST_MODIFIED_TIME = extractUserDefinedField("Last Modified Time");
        }

        //
        // Referenced elements
        // 
        addField(INCOMING_DOORS_ELEMENTS = new DmElementListField_ReadOnlyFromSource<DmDoorsElement>("Incoming Links to DOORS Objects") {
            @Override
            protected List<DmDoorsElement> loadElementList() {
                Map<String, DmDoorsElement> resultMap = DmDoorsFactory.getElementsByUrls(doorsObject.getUrlsOfIncomingLinks());
                List<DmDoorsElement> resultList = new ArrayList<>(resultMap.values());
                return (resultList);
            }
        });
        addField(OUTGOING_DOORS_ELEMENTS = new DmElementListField_ReadOnlyFromSource<DmDoorsElement>("Outgoing Links to DOORS Objects") {
            @Override
            protected List<DmDoorsElement> loadElementList() {
                Map<String, DmDoorsElement> resultMap = DmDoorsFactory.getElementsByUrls(doorsObject.getUrlsOfOutgoingLinks());
                List<DmDoorsElement> resultList = new ArrayList<>(resultMap.values());
                return (resultList);
            }
        });
        addField(REFERENCED_RQ1_ELEMENTS = new DmElementListField_ReadOnlyFromSource<DmRq1ElementInterface>("Referenced RQ1 Elements") {
            @Override
            protected List<DmRq1ElementInterface> loadElementList() {
                return (getReferencedRq1Elements());
            }
        });
        addField(INCOMING_EXTERNAL_ELEMENTS = new DmElementListField_ReadOnlyFromSource<DmDoorsElement>("Incoming Links to External Objects") {
            @Override
            protected List<DmDoorsElement> loadElementList() {
                List<DmDoorsElement> result = new ArrayList<>();
                for (String url : doorsObject.getUrlOfExternalIncomingLinks()) {
                    DmDoorsElement dmDoorsElement = DmDoorsFactory.getElementByUrl(url);
                    result.add(dmDoorsElement);
                }
                return (result);
            }
        });
        addField(OUTGOING_EXTERNAL_ELEMENTS = new DmElementListField_ReadOnlyFromSource<DmDoorsElement>("Outgoing Links to External Objects") {
            @Override
            protected List<DmDoorsElement> loadElementList() {
                List<DmDoorsElement> result = new ArrayList<>();
                for (String url : doorsObject.getUrlOfExternalOutgoingLinks()) {
                    DmDoorsElement dmDoorsElement = DmDoorsFactory.getElementByUrl(url);
                    result.add(dmDoorsElement);
                }
                return (result);
            }
        });

        addRule(new DmDoorsRule_CheckBaselinedObject(this));
    }

    //--------------------------------------------------------------------------
    //
    // Manage field creation
    //
    //--------------------------------------------------------------------------
    final protected DmConstantField_Text extractUserDefinedField(String fieldName) {
        assert (otherFields == null);
        return (extractUserDefinedField(fieldName, fieldName));
    }

    final protected DmConstantField_Text extractUserDefinedField(String fieldName, String nameForUserInterface) {
        assert (otherFields == null);
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);
        assert (nameForUserInterface != null);
        assert (nameForUserInterface.isEmpty() == false);

        DmConstantField_Text field = extractOptionalUserDefinedField(fieldName, nameForUserInterface);
        if (field == null) {
            field = createMissingField(nameForUserInterface, fieldName);
        }

        return (field);
    }

    final protected DmConstantField_Text extractOptionalUserDefinedField(String fieldName) {
        return (extractOptionalUserDefinedField(fieldName, fieldName));
    }

    final protected DmConstantField_Text extractOptionalUserDefinedField(String fieldName, String nameForUserInterface) {
        assert (otherFields == null);
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);
        assert (nameForUserInterface != null);
        assert (nameForUserInterface.isEmpty() == false);

        if (unusedUserDefinedFields.containsKey(fieldName)) {
            DmConstantField_Text field = new DmConstantField_Text(nameForUserInterface, unusedUserDefinedFields.get(fieldName));
            unusedUserDefinedFields.remove(fieldName);
            addField(field);
            return (field);
        } else {
            return (null);
        }
    }

    final protected DmConstantField_Text extractUserDefinedFieldFromList(String nameForUserInterface, String... fieldNames) {
        assert (nameForUserInterface != null);
        assert (nameForUserInterface.isEmpty() == false);
        assert (fieldNames != null);
        assert (fieldNames.length >= 1);

        for (String fieldName : fieldNames) {
            DmConstantField_Text field = extractOptionalUserDefinedField(fieldName, nameForUserInterface);
            if (field != null) {
                return (field);
            }
        }
        return (createMissingField(nameForUserInterface, fieldNames));
    }

    final protected DmConstantField_Text createMissingField(String nameForUserInterface, String... fieldName) {
        assert (nameForUserInterface != null);
        assert (nameForUserInterface.isEmpty() == false);
        assert (fieldName.length >= 1);

        DmConstantField_Text field = new DmConstantField_Text(nameForUserInterface, "");
        if (fieldName.length == 1) {
            field.setMarker(new DsFieldContentFailure(expectedAttributesExist,
                    "Attribute '" + fieldName[0] + "' is missing.",
                    "The attribute '" + fieldName[0] + "' is missing in the DOORS object.\n"
                    + "Please check the module definition."));
        } else {
            field.setMarker(new DsFieldContentFailure(expectedAttributesExist,
                    "Attributes '" + Arrays.stream(fieldName).collect(Collectors.joining("', '")) + "' are missing.",
                    "The attributes '" + Arrays.stream(fieldName).collect(Collectors.joining("', '")) + "' are missing in the DOORS object.\n"
                    + "Please check the module definition."));
        }
        addField(field);
        return (field);
    }

    final protected Set<String> getFreeUserDefinedFieldNames() {
        return (unusedUserDefinedFields.keySet());
    }

    final protected String extractUserDefinedFieldValue(String fieldName) {
        assert (fieldName != null);
        assert (fieldName.isEmpty() == false);
        String value = unusedUserDefinedFields.get(fieldName);
        unusedUserDefinedFields.remove(fieldName);
        return (value);
    }

    final protected void finishExtractionOfUserDefinedFields() {
        assert (otherFields == null);

        predefinedFields = new ArrayList<>(super.getFields());
        otherFields = new TreeMap<>();
        for (Map.Entry<String, String> entry : unusedUserDefinedFields.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            while (getFieldByName(fieldName) != null) {
                LOGGER.warning("Field >" + fieldName + "< exists already.");
                fieldName += "_";
            }
            DmConstantField_Text newField = new DmConstantField_Text(fieldName, fieldValue);
            otherFields.put(entry.getKey(), newField);
            addField(newField);
        }
        unusedUserDefinedFields = null;
    }

    final protected DmConstantField_Text extractFieldForState() {
        return (extractUserDefinedFieldFromList("Status", "_Status_", "EditState_PS-EC", "Status", "EditState"));
    }

    final protected DmConstantField_Text extractFieldForObjectType() {
        return (extractUserDefinedFieldFromList("Object Type", ATTRIBUTENAME_DOORS_OBJECT_TYPE_, ATTRIBUTENAME_DOORS_OBJECT_TYPE_PS_EC, ATTRIBUTENAME_DOORS_OBJECT_TYPE));
    }

    final protected DmConstantField_Text extractFieldForCRQ() {
        return (extractUserDefinedFieldFromList("CRQ", "_CRQ_", "CRQ_PS-EC", "CRQ"));
    }

    final protected DmConstantField_Text extractFieldForAffectedComponent() {
        return (extractUserDefinedFieldFromList("Affected Component", "_Affected Component_", "AffectedComponent_PS-EC", "Affected Component", "AffectedComponent"));
    }

    final protected DmConstantField_Text extractFieldForSafetyClassification() {
        return (extractUserDefinedFieldFromList("Safety Classification", "_Safety Classification_", "ASIL_PS-EC", "Safety Classification", "ASIL"));
    }

    final protected DmConstantField_Text extractFieldForAllocation() {
        return (extractUserDefinedFieldFromList("Allocation", "_Allocation_", "Allocation_PS-EC", "Allocation", "Allocation_"));
    }

    final protected DmConstantField_Text extractFieldForVerificationCriteria() {
        return (extractUserDefinedFieldFromList("Verification Criteria", "Verification Criteria", "Verification Criteria_", "VerificationCriteria"));
    }

    final protected DmConstantField_Text extractFieldForReviewComment() {
        return (extractUserDefinedFieldFromList("Review Comment", "Review Comment", "Review Comment_", "ReviewComment"));
    }

    //--------------------------------------------------------------------------
    //
    // Get field information
    //
    //--------------------------------------------------------------------------
    final public String showExtractedFields() {
        return (getTypeAndFieldsAsTextSorted(predefinedFields));
    }

    final public String showNotExtractedFields() {
        return (getTypeAndFieldsAsTextSorted(otherFields.values()));
    }

    public Collection<DmConstantField_Text> getFreeUserDefinedFields() {
        if (unusedUserDefinedFields != null) {
            finishExtractionOfUserDefinedFields();
        }
        return otherFields.values();
    }

    //--------------------------------------------------------------------------
    //
    // Miscelaneous methods
    //
    //--------------------------------------------------------------------------
    @Override
    public String getId() {
        return (OBJECT_IDENTIFIER.getValueAsText());
    }

    @Override
    public String getTitle() {
        return (OBJECT_IDENTIFIER.getValueAsText());
    }

    private List<DmRq1ElementInterface> getReferencedRq1Elements() {
        List<DmRq1ElementInterface> result = new ArrayList<>();

        for (String rq1Id : getIdOfReferencedRq1Elements()) {
            String trimmedId = rq1Id.trim();
            if (trimmedId.isEmpty() == false) {
                DmRq1ElementInterface dmRq1Element = DmRq1Element.getElementByRq1Id(rq1Id);
                result.add(dmRq1Element);
            }
        }

        return (result);
    }

    @Override
    public String getViewTitle() {
        return (getElementType() + ": " + getTitle());
    }

    @Override
    public String toString() {
        return (OBJECT_IDENTIFIER.getValueAsText() + " - " + RDF_ABOUT.getValueAsText());
    }

    @Override
    public String getDoorsObjectIdentifier() {
        return (OBJECT_IDENTIFIER.getValueAsText());
    }

    @Override
    public int compareTo(DmDoorsObject otherDmDoorsObject) {

        //
        // Handle null element
        //
        if (otherDmDoorsObject == null) {
            return (-1);
        }

        //
        // Compare module id
        //
        String myModuleId = doorsObject.getIdentifier().getModuleId();
        String otherModuleId = otherDmDoorsObject.doorsObject.getIdentifier().getModuleId();
        int moduleCompare = myModuleId.compareTo(otherModuleId);
        if (moduleCompare != 0) {
            return (moduleCompare);
        }

        //
        // Compare object number which contains the current element hierarchy and order
        //
        return (compareObjectNumber(OBJECT_NUMBER.getValue(), otherDmDoorsObject.OBJECT_NUMBER.getValue()));
    }

    static int compareObjectNumber(String objectNumber1, String objectNumber2) {

        boolean o1Null = (objectNumber1 == null) || objectNumber1.isEmpty();
        boolean o2Null = (objectNumber2 == null) || objectNumber2.isEmpty();
        if (o1Null) {
            if (o2Null) {
                return (0);
            } else {
                return (1);
            }
        } else if (o2Null) {
            return (-1);
        }

        String chapters1[] = objectNumber1.split("-");
        String chapters2[] = objectNumber2.split("-");
        for (int i = 0; (i < chapters1.length) && (i < chapters2.length); i++) {
            int c = compareChapterNumber(chapters1[i], chapters2[i]);
            if (c != 0) {
                return (c);
            }
        }
        return (chapters1.length - chapters2.length);
    }

    static int compareChapterNumber(String chapter1, String chapter2) {

        boolean o1Null = (chapter1 == null) || chapter1.isEmpty();
        boolean o2Null = (chapter2 == null) || chapter2.isEmpty();
        if (o1Null) {
            if (o2Null) {
                return (0);
            } else {
                return (1);
            }
        } else if (o2Null) {
            return (-1);
        }

        String parts1[] = chapter1.split("\\.");
        String parts2[] = chapter2.split("\\.");
        for (int i = 0; (i < parts1.length) && (i < parts2.length); i++) {
            int n1 = Integer.parseInt(parts1[i]);
            int n2 = Integer.parseInt(parts2[i]);
            if (n1 != n2) {
                return (n1 - n2);
            }
        }
        return (parts1.length - parts2.length);
    }

    public String getRq1Level() {
        return this.rq1Level;
    }
}
