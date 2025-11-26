/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.DmField;
import DataModel.DmValueFieldI_Enumeration_EagerLoad;
import DataStore.ALM.DsAlmField_ResourceList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import util.EcvComparator;
import util.EcvEnumeration;

/**
 *
 * @author GUG2WI
 */
public class DmAlmField_Enumeration extends DmField implements DmValueFieldI_Enumeration_EagerLoad {

    static private final Logger LOGGER = Logger.getLogger(DmAlmField_Enumeration.class.getCanonicalName());

    final private DsAlmField_ResourceList dsResourceListField;
    final private DmAlmEnumerationList validValues;

    public DmAlmField_Enumeration(DsAlmField_ResourceList dsResourceListField, String nameForUserInterface) {
        super(nameForUserInterface);
        assert (dsResourceListField != null);
        assert ((dsResourceListField.getDataModelValue() == null) || (dsResourceListField.getDataModelValue().size() <= 1));

        this.dsResourceListField = dsResourceListField;

        validValues = DmAlmEnumerationManager.getEnumerations(getTypeUrl());
    }

    private String getTypeUrl() {
        String valueUrl = getValueUrl();
        if (valueUrl == null) {
            return (null);
        }
        int lastIndex = valueUrl.lastIndexOf("/");
        String typeUrl = valueUrl.substring(0, lastIndex);
        return (typeUrl);
    }

    private String getValueUrl() {
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
        String url = getValueUrl();
        if (url == null) {
            return (null);
        }
        int lastIndex = url.lastIndexOf("/");
        String text = url.substring(lastIndex + 1);

        for (DmAlmEnumerationValue value : validValues.getArray()) {
            if (value.getIdentifier().equals(text)) {
                return (value);
            }
        }

        return (null);
    }

    @Override
    public void setValue(EcvEnumeration v) {
        if (v instanceof DmAlmEnumerationValue) {
            DmAlmEnumerationValue realValue = (DmAlmEnumerationValue) v;
            List<String> resourceList = new ArrayList<>();
            String resource = realValue.getResource();
            resourceList.add(resource);
            dsResourceListField.setDataModelValue(resourceList);
        } else {
            LOGGER.log(Level.SEVERE, "Unexpected Enumeration Type:\n", v.getClass());
        }
    }

    @Override
    public EcvEnumeration[] getValidInputValues() {
        List<EcvEnumeration> validValuesAsList = Arrays.asList(validValues.getArray());

        //Sort Values
        Collections.sort(validValuesAsList, (EcvEnumeration o1, EcvEnumeration o2) -> {
            if (o2.getText().equals("Unassigned") && o2.getText().equals("Unassigned")) {
                return 0;
            } else if(o1.getText().equals("Unassigned")) {
                return -1;
            } else if (o2.getText().equals("Unassigned")) {
                return 1;
            }  else {
                if (EcvComparator.isNumeric(o1.getText()) || EcvComparator.isNumeric(o2.getText())) {
                    //both numeric
                    if (EcvComparator.isNumeric(o1.getText()) && EcvComparator.isNumeric(o2.getText())) {
                        int int1 = Integer.valueOf(o1.getText());
                        int int2 = Integer.valueOf(o2.getText());
                        return (Integer.compare(int1, int2));
                        //only one numeric
                    } else {
                        if (EcvComparator.isNumeric(o1.getText())) {
                            return (-1);
                        } else {
                            return (1);
                        }
                    }
                    //both non-numeric
                } else {
                    return (o1.getText().compareTo(o2.getText()));
                }
            }
        });

        EcvEnumeration[] validInputValues = new EcvEnumeration[validValuesAsList.size()];
        for (int i = 0; i < validValuesAsList.size(); i++) {
            validInputValues[i] = validValuesAsList.get(i);
        }
        return (validInputValues);
    }

    @Override
    public boolean isReadOnly() {
        return (false);
    }

}
