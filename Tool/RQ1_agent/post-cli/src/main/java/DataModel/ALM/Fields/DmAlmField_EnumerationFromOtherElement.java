/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.ALM.Records.DmAlmElement;
import DataModel.ALM.Records.DmAlmElementFactory;
import DataModel.DmField;
import DataStore.ALM.DsAlmField_ResourceList;
import java.util.List;
import DataModel.DmValueFieldI_Enumeration_FromOtherElement;
import RestClient.Exceptions.NoLoginDataException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.EcvLoginManager;

/**
 *
 * @author CNI83WI
 * @param <T_ELEMENT> ALM Element, that is referenced by this field.
 */
public class DmAlmField_EnumerationFromOtherElement<T_ELEMENT extends DmAlmElement> extends DmField implements DmValueFieldI_Enumeration_FromOtherElement<T_ELEMENT> {

    public static interface NullConverter {

        boolean isUrlNullElement(String url);

        String getNullUrl();

    }

    public static class DefaultNullConverter implements NullConverter {

        @Override
        public boolean isUrlNullElement(String url) {
            return (false);
        }

        @Override
        public String getNullUrl() {
            return ("");
        }

    }

    private NullConverter nullConverter = new DefaultNullConverter();

    public void setNullConverter(NullConverter nullConverter) {
        this.nullConverter = nullConverter;
    }

    static private final Logger LOGGER = Logger.getLogger(DmAlmField_EnumerationFromOtherElement.class.getCanonicalName());

    final private DsAlmField_ResourceList dsResourceListField;
    final private String type;
    final private String projectArea;
    final private String property;

    public DmAlmField_EnumerationFromOtherElement(DsAlmField_ResourceList dsResourceListField, String type, String projectArea, String property, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (dsResourceListField != null);
        assert ((dsResourceListField.getDataModelValue() == null) || (dsResourceListField.getDataModelValue().size() <= 1));

        this.dsResourceListField = dsResourceListField;
        this.property = property;
        this.type = getTypeString(type);
        this.projectArea = getProjectAreaCode(projectArea);
    }

    public String getProjectAreaCode() {
        return projectArea;
    }

    public final String getTypeUrl() {
        String typeUrl = null;
        try {
            String oslcUrl = EcvLoginManager.getFirstLoginData().getServerDescription().getAlmServer().getOslcUrl();
            typeUrl = oslcUrl + "/ccm/oslc/context/" + projectArea + "/shapes/workitems/" + type + "/property/" + property + "/allowedValues";     
        } catch (NoLoginDataException ex) {
            LOGGER.log(Level.SEVERE, "Failed to initialize Field: " + this.getNameForUserInterface(), ex);
        }
        return (typeUrl);
    }

    @Override
    public final String getValueUrl() {
        List<String> urlList = dsResourceListField.getDataModelValue();
        if ((urlList == null) || (urlList.isEmpty())) {
            return (null);
        }

        if (urlList.size() > 1) {
            throw new Error("To many url in " + this.toString());
        }

        return (urlList.get(0));
    }

    @Override
    public boolean isReadOnly() {
        return (false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T_ELEMENT getValue() {
        String url = getValueUrl();
        if ((url != null) && (url.isEmpty() == false) && nullConverter.isUrlNullElement(url) == false) {
            T_ELEMENT dmElement = (T_ELEMENT) DmAlmElementFactory.getElementByUrl(url);
            return (dmElement);
        }
        return (null);
    }

    @Override
    public void setValue(T_ELEMENT v) {
        List<String> resourceList = new ArrayList<>();
        if (v == null) {
            resourceList.add(nullConverter.getNullUrl());
        } else {
            resourceList.add(v.getUrl());
        }

        dsResourceListField.setDataModelValue(resourceList);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T_ELEMENT> getValidInputValues() {
        ArrayList<T_ELEMENT> result = new ArrayList<>();
        for (DmAlmElement e : DmAlmEnumerationManager.getEnumerationElements(getTypeUrl())) {
            result.add((T_ELEMENT) e);
        }
        return (result);
    }
    
    public final String getTypeString(String elementType){
        String value = elementType.replaceAll("\\s+", "");
        String typeString = "com.ibm.team.workitem.workItemType." + value.substring(0,1).toLowerCase() + value.substring(1);
        return (typeString);
    }
    
    public final String getProjectAreaCode(String projectAreaUrl) {
        String projectAreaCode = null;
        if(projectAreaUrl != null) {
            projectAreaCode = projectAreaUrl.substring(projectAreaUrl.lastIndexOf("/") + 1);
        }
        
        return(projectAreaCode);
    }

}
