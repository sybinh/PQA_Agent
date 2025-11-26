/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.DmField;
import DataModel.DmValueFieldI_Text;
import DataStore.ALM.DsAlmField_ResourceList;
import java.util.List;

/**
 *
 * @author CNI83WI
 */
public class DmAlmField_ExternalResourceList extends DmField implements DmValueFieldI_Text {

    final private DsAlmField_ResourceList dsResourceListField;

    public DmAlmField_ExternalResourceList(DsAlmField_ResourceList dsResourceListField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (dsResourceListField != null);

        this.dsResourceListField = dsResourceListField;
    }

    @Override
    public boolean isReadOnly() {
        return (true);
    }

    public List<String> getUrlList() {
        List<String> urlList = dsResourceListField.getDataModelValue();
        if ((urlList == null) || (urlList.isEmpty())) {
            return (null);
        }

        return (urlList);
    }

    @Override
    public String getValue() {
        if (getUrlList() != null) {
            String urls = getUrlList().toString();
            return (urls.substring(1,urls.length()-1));
        } else {
            return ("");
        }       
    }

    @Override
    public void setValue(String v) {       
    }

}
