/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Doors.Records;

import Doors.DoorsFolder;
import Doors.DoorsLoadErrorRecord;
import Doors.DoorsModule;
import Doors.DoorsObject;
import Doors.DoorsProject;
import Doors.DoorsRecord;
import Doors.DoorsRecordFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import static DataModel.Doors.Records.DmDoorsObject_PTSA_1x.ATTRIBUTENAME_DOORS_OBJECT_TYPE;
import static DataModel.Doors.Records.DmDoorsObject_PTSA_1x.ATTRIBUTENAME_DOORS_OBJECT_TYPE_;
import static DataModel.Doors.Records.DmDoorsObject_PTSA_1x.ATTRIBUTENAME_DOORS_OBJECT_TYPE_PS_EC;

/**
 *
 * @author gug2wi
 */
public class DmDoorsFactory {

    static private final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(DmDoorsFactory.class.getCanonicalName());

    final private static IdentityHashMap<DoorsRecord, DmDoorsElement> elementCache = new IdentityHashMap<>();
    final private static Map<String, DmDoorsRequirementWithoutUrlRecord> withoutUrlCache = new TreeMap<>();

    public static synchronized DmDoorsRequirementWithoutUrlRecord getElementWithoutUrl(String requirement) {
        assert (requirement != null);
        assert (requirement.isEmpty() == false);

        DmDoorsRequirementWithoutUrlRecord record = withoutUrlCache.get(requirement);
        if (record == null) {
            record = new DmDoorsRequirementWithoutUrlRecord(requirement);
            withoutUrlCache.put(requirement, record);
        }

        return (record);
    }

    public static DmDoorsElement getElementByUrl(String url) {
        List<String> urlList = new ArrayList<>();
        urlList.add(url);
        Map<String, DmDoorsElement> elementMap = getElementsByUrls(urlList);
        return (elementMap.get(url));
    }

    public static synchronized Map<String, DmDoorsElement> getElementsByUrls(Collection<String> urls) {
        assert (urls != null);

        Map<String, DmDoorsElement> result = new TreeMap<>();
        if (urls.isEmpty() == false) {
            Map<String, DoorsRecord> doorsRecordMap = DoorsRecordFactory.getRecordsByUrls(urls);

            for (String url : urls) {
                DoorsRecord doorsRecord = doorsRecordMap.get(url);
                if (doorsRecord == null) {
                    result.put(url, new DmDoorsLoadErrorRecord(url, "No record received for URL."));
                    LOGGER.log(Level.WARNING, "No record received for URL {0}", url);
                } else {
                    result.put(url, getElementByRecord(doorsRecord));
                }
            }
        }
        return (result);
    }

    public static synchronized DmDoorsElement getElementByRecord(DoorsRecord doorsRecord) {
        assert (doorsRecord != null);

        DmDoorsElement dmDoorsElement = elementCache.get(doorsRecord);

        if (dmDoorsElement == null) {
            dmDoorsElement = createDmElement(doorsRecord);
            elementCache.put(doorsRecord, dmDoorsElement);
        }

        return (dmDoorsElement);
    }

    private static DmDoorsElement createDmElement(DoorsRecord doorsRecord) {

        if (doorsRecord instanceof DoorsProject) {
            return (new DmDoorsProject((DoorsProject) doorsRecord));
        }

        if (doorsRecord instanceof DoorsFolder) {
            return (new DmDoorsFolder((DoorsFolder) doorsRecord));
        }

        if (doorsRecord instanceof DoorsModule) {
            return (new DmDoorsModule((DoorsModule) doorsRecord));
        }

        if (doorsRecord instanceof DoorsObject) {
            return (createDmObject((DoorsObject) doorsRecord));
        }

        if (doorsRecord instanceof DoorsLoadErrorRecord) {
            return (new DmDoorsLoadErrorRecord((DoorsLoadErrorRecord) doorsRecord));
        }

        throw (new InternalError("Unexpected type of doors element: " + doorsRecord.getClass().getCanonicalName()));
    }

    private static DmDoorsObject createDmObject(DoorsObject doorsObject) {
        assert (doorsObject != null);

        DmDoorsObject stkhObject = createStakeHolderObject(doorsObject);
        if (stkhObject != null) {
            return (stkhObject);
        }

        DmDoorsObject articaftObject = createArtifactObject(doorsObject);
        if (articaftObject != null) {
            return (articaftObject);
        }

        DmDoorsObject ptsa10Object = createPtsa10Object(doorsObject);
        if (ptsa10Object != null) {
            return (ptsa10Object);
        }

        return (new DmDoorsObject_Unspecific(doorsObject));
    }

    /**
     * Key: 'ObjectType' or 'ObjectType_PS-EC'
     *
     * @param doorsObject
     * @return
     */
    private static DmDoorsObject createPtsa10Object(DoorsObject doorsObject) {

        String objectTypeName = ATTRIBUTENAME_DOORS_OBJECT_TYPE_;
        String objectType = doorsObject.getUserDefinedFields().get(objectTypeName);

        if (objectType == null) {
            objectTypeName = ATTRIBUTENAME_DOORS_OBJECT_TYPE_PS_EC;
            objectType = doorsObject.getUserDefinedFields().get(objectTypeName);
        }

        if (objectType == null) {
            objectTypeName = ATTRIBUTENAME_DOORS_OBJECT_TYPE;
            objectType = doorsObject.getUserDefinedFields().get(objectTypeName);
        }

        if (objectType != null) {
            switch (objectType) {
                case "FUNC_REQ":
                    if (objectTypeName.equals(ATTRIBUTENAME_DOORS_OBJECT_TYPE_PS_EC)) {
                        return (new DmDoorsObject_PTSA_1x_L1_FUNC_REQ(doorsObject));
                    } else {
                        return (new DmDoorsObject_PTSA_1x_FunctionalRequirement(doorsObject));
                    }
                case "NON_FUNC_REQ":
                    if (objectTypeName.equals(ATTRIBUTENAME_DOORS_OBJECT_TYPE_PS_EC)) {
                        return (new DmDoorsObject_PTSA_1x_L1_NON_FUNC_REQ(doorsObject));
                    } else {
                        return (new DmDoorsObject_PTSA_1x_NonFunctionalRequirement(doorsObject));
                    }
                case "DETAILED_SW_REQ":
                    return (new DmDoorsObject_PTSA_1x_DetailedSoftwareRequirement(doorsObject));
                case "SOFTWARE_REQ":
                    return (new DmDoorsObject_PTSA_1x_SoftwareRequirement(doorsObject));
                case "SYSTEM_REQ":
                    return (new DmDoorsObject_PTSA_1x_SystemRequirement(doorsObject));
                case "TEST_CASE":
                    return (new DmDoorsObject_TestCase(doorsObject));
                case "HEADER":
                    return (new DmDoorsObject_PTSA_1x_Header(doorsObject));
                case "INFO":
                    if (objectTypeName.equals(ATTRIBUTENAME_DOORS_OBJECT_TYPE_PS_EC)) {
                        return (new DmDoorsObject_PTSA_1x_L1_INFO(doorsObject));
                    } else {
                        return (new DmDoorsObject_PTSA_1x_Info(doorsObject));
                    }
                case "INTERFACE_REQ":
                    return (new DmDoorsObject_PTSA_1x_InterfaceRequirement(doorsObject));
                case "DESIGN_DECISION":
                    return (new DmDoorsObject_PTSA_1x_DesignDecision(doorsObject));
            }
        }

        return (null);
    }

    /**
     * Key: '_Object Type_'
     *
     * @param doorsObject
     * @return
     */
    private static DmDoorsObject createStakeHolderObject(DoorsObject doorsObject) {

        String objectType = doorsObject.getUserDefinedFields().get("_Object Type_");

        if (objectType != null) {
            switch (objectType) {
                case "STKH_HEADER":
                    return (new DmDoorsObject_PTSA_20_STKH_HEADER(doorsObject));
                case "STKH_INFO":
                    return (new DmDoorsObject_PTSA_20_STKH_INFO(doorsObject));
                case "STKH_FUNC_REQ":
                    return (new DmDoorsObject_PTSA_20_STKH_FUNC_REQ(doorsObject));
                case "STKH_NON_FUNC_REQ":
                    return (new DmDoorsObject_PTSA_20_STKH_NON_FUNC_REQ(doorsObject));
            }
        }

        return (null);
    }

    /**
     * Key 'Artefact Type'.
     *
     * @param doorsObject
     * @return
     */
    private static DmDoorsObject createArtifactObject(DoorsObject doorsObject) {

        String objectType = doorsObject.getUserDefinedFields().get("Artefact Type");

        if (objectType != null) {
            switch (objectType) {
                case "HEADER":
                    return (new DmDoorsObject_PTSA_20_ATYPE_HEADER(doorsObject));
                case "INFO":
                    return (new DmDoorsObject_PTSA_20_ATYPE_INFO(doorsObject));
                case "MO_FUNC_REQ":
                    return (new DmDoorsObject_PTSA_20_ATYPE_MO_FUNC_REQ(doorsObject));
                case "MO_NON_FUNC_REQ":
                    return (new DmDoorsObject_PTSA_20_ATYPE_MO_NON_FUNC_REQ(doorsObject));
                case "SC_FUNC_REQ":
                    return (new DmDoorsObject_PTSA_20_ATYPE_SC_FUNC_REQ(doorsObject));
                case "SC_NON_FUNC_REQ":
                    return (new DmDoorsObject_PTSA_20_ATYPE_SC_NON_FUNC_REQ(doorsObject));
                case "SC_INTERFACE_REQ":
                    return (new DmDoorsObject_PTSA_20_ATYPE_SC_INTERFACE_REQ(doorsObject));
                case "BC-FC_FUNC_REQ":
                    return (new DmDoorsObject_PTSA_20_ATYPE_BC_FC_FUNC_REQ(doorsObject));
                case "BC-FC_NON_FUNC_REQ":
                    return (new DmDoorsObject_PTSA_20_ATYPE_BC_FC_NON_FUNC_REQ(doorsObject));
                case "BC-FC_INTERFACE_REQ":
                    return (new DmDoorsObject_PTSA_20_ATYPE_BC_FC_INTERFACE_REQ(doorsObject));
                case "QLTY_NON_FUNC_REQ":
                    return (new DmDoorsObject_PTSA_20_ATYPE_QLTY_NON_FUNC_REQ(doorsObject));
            }
        }

        return (null);
    }

}
