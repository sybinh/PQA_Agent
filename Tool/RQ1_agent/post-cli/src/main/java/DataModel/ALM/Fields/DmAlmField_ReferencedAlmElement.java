/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.ALM.Fields;

import DataModel.ALM.Records.DmAlmElementFactory;
import DataModel.ALM.Records.DmAlmElementI;
import DataModel.ALM.Records.DmAlmToAlmReference;
import DataModel.DmField;
import DataModel.DmMappedElementListField_ReadOnlyI;
import DataModel.DmMappedElement;
import DataStore.ALM.DsAlmRecordI;
import DataStore.DsFieldI_TextList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import util.SafeArrayList;

/**
 *
 * @author GUG2WI
 */
public class DmAlmField_ReferencedAlmElement extends DmField implements DmMappedElementListField_ReadOnlyI<DmAlmToAlmReference, DmAlmElementI> {

    private SafeArrayList<DmMappedElement<DmAlmToAlmReference, DmAlmElementI>> content = null;

    final private Map<String, DsFieldI_TextList<DsAlmRecordI>> fieldMap = new TreeMap<>();

    public DmAlmField_ReferencedAlmElement(String nameForUserInterface) {
        super(nameForUserInterface);
    }

    public void addDsField(String nameOfField, DsFieldI_TextList<DsAlmRecordI> dsField) {
        assert (nameOfField != null);
        assert (nameOfField.isEmpty() == false);
        assert (dsField != null);
        assert (fieldMap.containsKey(nameOfField) == false);
        assert (fieldMap.containsValue(dsField) == false);

        fieldMap.put(nameOfField, dsField);
    }

    @Override
    public List<DmMappedElement<DmAlmToAlmReference, DmAlmElementI>> getElementList() {
        if (content == null) {
            SafeArrayList<DmMappedElement<DmAlmToAlmReference, DmAlmElementI>> newContent = new SafeArrayList<>();
            for (Map.Entry<String, DsFieldI_TextList<DsAlmRecordI>> fieldEntry : fieldMap.entrySet()) {
                String nameOfField = fieldEntry.getKey();
                DsFieldI_TextList<DsAlmRecordI> dsField = fieldEntry.getValue();
                List<String> urlList = dsField.getDataModelValue();
                if (urlList != null) {
                    for (String url : urlList) {
                        DmAlmElementI dmElement = DmAlmElementFactory.getElementByUrl(url);
                        newContent.add(new DmMappedElement<>(new DmAlmToAlmReference(nameOfField), dmElement));
                    }
                }
            }
            content = newContent;
        }
        return (content.getImmutableList());
    }

    @Override
    public boolean isLoaded() {
        return (content != null);
    }

    @Override
    public void reload() {
        content = null;
        fireFieldChanged();
    }

}
