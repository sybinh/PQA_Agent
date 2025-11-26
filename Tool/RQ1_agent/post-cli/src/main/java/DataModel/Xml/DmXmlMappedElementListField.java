/*
 * Copyright (c) 2009, 2021 Robert Bosch GmbH and its subsidiaries.
 * This program and the accompanying materials are made available under
 * the terms of the Bosch Internal Open Source License v4
 * which accompanies this distribution, and is available at
 * http://bios.intranet.bosch.com/bioslv4.txt
 */
package DataModel.Xml;

import DataModel.DmElementI;
import DataModel.DmElementReferenceI;
import DataModel.DmMappedElementListField_ReadOnlyFromSource;
import DataModel.DmMappedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import util.UiWorker;

/**
 *
 * @author GUG2WI
 */
public class DmXmlMappedElementListField<T_TARGET extends DmXmlMappedContainerElementI> extends DmMappedElementListField_ReadOnlyFromSource<DmElementI, T_TARGET> {

    final private Collection<T_TARGET> values;

    public DmXmlMappedElementListField(String nameForUserInterface, Collection<T_TARGET> values) {
        super(nameForUserInterface);
        assert (values != null);
        this.values = values;
    }

    @Override
    protected Collection<DmMappedElement<DmElementI, T_TARGET>> loadElementList() {
        List<DmMappedElement<DmElementI, T_TARGET>> result = new ArrayList<>(values.size());
        for (T_TARGET target : values) {
            DmElementI map = target.getMappingElement();
            DmMappedElement<DmElementI, T_TARGET> element = new DmMappedElement<>(map, target);
            result.add(element);
        }
        loadInBackground(result);
        return (result);
    }

    private void loadInBackground(List<DmMappedElement<DmElementI, T_TARGET>> mapsToLoad) {

        List<DmElementReferenceI> references = new ArrayList<>();
        for (DmMappedElement<DmElementI, T_TARGET> mappedElement : mapsToLoad) {
            if (mappedElement.getMap() instanceof DmElementReferenceI) {
                references.add((DmElementReferenceI) mappedElement.getMap());
            }
        }

        UiWorker.execute(new UiWorker<Void>(UiWorker.LOADING) {
            @Override
            protected Void backgroundTask() {
                for (DmElementReferenceI reference : references) {
                    reference.getReferencedElement();
                }
                return (null);
            }
        }
        );
    }

}
