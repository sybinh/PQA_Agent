/*
 * Copyright (c) 2009, 2020 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.DmField;
import DataModel.DmValueFieldI_Enumeration_EagerLoad;
import DataStore.ALM.DsAlmField_ResourceList;
import java.util.List;
import util.EcvEnumeration;
import util.EcvEnumerationValue;

/**
 *
 * @author GUG2WI
 */
public class DmAlmField_ResourceAsEnumeration extends DmField implements DmValueFieldI_Enumeration_EagerLoad {

    final private DsAlmField_ResourceList dsResourceListField;

    public DmAlmField_ResourceAsEnumeration(DsAlmField_ResourceList dsResourceListField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (dsResourceListField != null);
        assert ((dsResourceListField.getDataModelValue() == null) || (dsResourceListField.getDataModelValue().size() <= 1));

        this.dsResourceListField = dsResourceListField;
    }

    private String getUrl() {
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
    public EcvEnumeration getValue() {
        String url = getUrl();
        if (url == null) {
            return (null);
        }
        int lastIndex = url.lastIndexOf("/");
        String text = url.substring(lastIndex + 1);
        return (new EcvEnumerationValue(text, 0));
    }

    @Override
    public void setValue(EcvEnumeration v) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EcvEnumeration[] getValidInputValues() {
        return (new EcvEnumeration[0]);
    }

    @Override
    public boolean isReadOnly() {
        return (true);
    }

}
