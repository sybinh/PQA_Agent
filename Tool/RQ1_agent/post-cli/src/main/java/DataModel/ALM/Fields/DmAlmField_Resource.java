/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.ALM.Records.DmAlmElementFactory;
import DataModel.ALM.Records.DmAlmElement;
import DataModel.DmElementField_ReadOnlyI;
import DataModel.DmField;
import DataModel.DmValueFieldI_Text;
import DataStore.ALM.DsAlmField_ResourceList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GUG2WI
 * @param <T_ELEMENT>
 */
public class DmAlmField_Resource<T_ELEMENT extends DmAlmElement> extends DmField implements DmValueFieldI_Text, DmElementField_ReadOnlyI<T_ELEMENT> {

    final private DsAlmField_ResourceList dsResourceListField;

    public DmAlmField_Resource(DsAlmField_ResourceList dsResourceListField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (dsResourceListField != null);
        assert ((dsResourceListField.getDataModelValue() == null) || (dsResourceListField.getDataModelValue().size() <= 1)); // It's a list !!!

        this.dsResourceListField = dsResourceListField;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T_ELEMENT getElement() {
        String url = getUrl();
        if ((url != null) && (url.isEmpty() == false)) {
            T_ELEMENT dmElement = (T_ELEMENT) DmAlmElementFactory.getElementByUrl(url);
            return (dmElement);
        }
        return (null);
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
    public String getValue() {
        return (getUrl());
    }

    @Override
    public void setValue(String v) {
        List<String> urlList = new ArrayList<>();
        urlList.add(v);
        dsResourceListField.setDataModelValue(urlList);
    }

}
