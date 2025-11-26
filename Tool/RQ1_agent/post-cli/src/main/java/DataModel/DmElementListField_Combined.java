/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A list field that contains all elements of other list fields. No duplicates
 * are added to the list.
 *
 * @author gug2wi
 * @param <T_ELEMENT> Type of elements hold by the list.
 */
public class DmElementListField_Combined<T_ELEMENT extends DmElementI> extends DmField implements DmElementListField_ReadOnlyI<T_ELEMENT> {

    final private List<DmElementListField_ReadOnlyI<T_ELEMENT>> fields;
    final private List<DmMappedElementListField_ReadOnlyI<DmElementI, T_ELEMENT>> mappedFields;

    @SafeVarargs
    public DmElementListField_Combined(String nameForUserInterface, DmElementListField_ReadOnlyI<T_ELEMENT> field1, DmElementListField_ReadOnlyI<T_ELEMENT>... otherFields) {
        super(nameForUserInterface);
        assert (field1 != null);
        assert (otherFields != null);

        fields = new ArrayList<>();
        fields.add(field1);
        for (DmElementListField_ReadOnlyI<T_ELEMENT> field : otherFields) {
            fields.add(field);
        }

        mappedFields = null;
    }

    @SafeVarargs
    public DmElementListField_Combined(String nameForUserInterface, DmMappedElementListField_ReadOnlyI<DmElementI, T_ELEMENT> field1, DmMappedElementListField_ReadOnlyI<DmElementI, T_ELEMENT>... otherFields) {
        super(nameForUserInterface);
        assert (field1 != null);
        assert (otherFields != null);

        fields = null;

        mappedFields = new ArrayList<>();
        mappedFields.add(field1);
        for (DmMappedElementListField_ReadOnlyI<DmElementI, T_ELEMENT> field : otherFields) {
            mappedFields.add(field);
        }
    }

    @Override
    public List<T_ELEMENT> getElementList() {
        List<T_ELEMENT> result = new ArrayList<>();
        if (fields != null) {
            for (DmElementListField_ReadOnlyI<T_ELEMENT> field : fields) {
                for (T_ELEMENT element : field.getElementList()) {
                    if (result.contains(element) == false) {
                        result.add(element);
                    }
                }
            }
        } else {
            for (DmMappedElementListField_ReadOnlyI<DmElementI, T_ELEMENT> field : mappedFields) {
                for (DmMappedElement<DmElementI, T_ELEMENT> map : field.getElementList()) {
                    T_ELEMENT element = map.getTarget();
                    if (result.contains(element) == false) {
                        result.add(element);
                    }
                }
            }
        }
        return (result);
    }

    @Override
    public void reload() {
        for (DmElementListField_ReadOnlyI<T_ELEMENT> field : fields) {
            field.reload();
        }
        fireFieldChanged();
    }

}
